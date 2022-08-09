import ctypes
import inspect
import threading
from LAC import LAC
from datetime import datetime
from typing import List


# DFA算法
class DFAFilter:
    def __init__(self):
        self.keyword_chains = {}
        self.delimit = '\r\n'

    def add(self, keyword):
        global last_level, last_char
        keyword = keyword.lower()
        chars = keyword.strip(self.delimit)
        if not chars:
            return
        level = self.keyword_chains
        for i in range(len(chars)):
            if chars[i] in level:
                level = level[chars[i]]
            else:
                if not isinstance(level, dict):
                    break
                for j in range(i, len(chars)):
                    level[chars[j]] = {}
                    last_level, last_char = level, chars[j]
                    level = level[chars[j]]
                last_level[last_char] = {self.delimit: 0}
                break
        if i == len(chars) - 1:
            level[self.delimit] = 0

    def parse(self, path):
        with open(path, encoding='utf-8') as f:
            for keyword in f:
                self.add(str(keyword).strip('\n'))

    def parse_by_data(self, data: str):
        for keyword in data.split("\n"):
            if keyword and len(keyword) > 1:
                self.add(str(keyword).strip('\n').strip(' '))

    def filter(self, message, repl="*"):
        fuck_words = []
        message = message.lower()
        ret = []
        start = 0
        while start < len(message):
            level = self.keyword_chains
            step_ins = 0
            fuck_word = []
            for char in message[start:]:
                if char in level:
                    step_ins += 1
                    fuck_word.append(char)
                    if self.delimit not in level[char]:
                        level = level[char]
                    else:
                        fuck_words.append("".join(fuck_word))
                        ret.append(repl * step_ins)
                        start += step_ins - 1
                        break
                else:
                    ret.append(message[start])
                    break
            else:
                ret.append(message[start])
            start += 1

        return ''.join(ret), fuck_words


model_uri = "model/model.pkl"
stop_word_uri = "data/cn_stopwords.txt"
keyword_uri = "data/keywords.txt"

n_word_tag = ['n', 'nz', 'PER', 'LOC', 'ORG', 'TIME', 'nw', 's', 'f', 'm', 't', 'v', 'x', 'y', 'z']


def read_stopwords():
    file_name = stop_word_uri
    with open(file_name, 'r', encoding='utf-8') as f:
        stopwords = f.read()
    return stopwords


def get_extraction_keywords(text) -> List[str]:
    global lac
    results = lac.run(text)
    words = results[0]
    tags = results[1]
    result = []
    for word in words:
        if word in stop_words or len(word) < 2:
            try:
                tags.pop(words.index(word))
                words.remove(word)
            except:
                pass
    for i in range(len(tags)):
        if tags[i] in n_word_tag:
            if words[i] not in result:
                result.append(words[i])
    return result


def add_keyword_data_set(text):
    keywords = get_extraction_keywords(text)
    with open(keyword_uri, 'r', encoding='utf-8') as f:
        data_set = f.readlines()
        f.close()

    for keyword in keywords:
        if keyword not in data_set:
            with open(keyword_uri, 'a', encoding='utf-8') as f:
                f.write(keyword + '\n')
                f.close()


import joblib


def getFilterByData(data: str):
    global model
    model = DFAFilter()
    model.parse_by_data(data)
    return model


def save(model, model_name: str):
    joblib.dump(model, model_name)


def load(model_name: str):
    return joblib.load(model_name)


def train(data):
    global model
    model = getFilterByData(data)
    save(model, model_uri)
    return model


def predict(data):
    global model
    write_test_log("predict\n")
    if model is None:
        model = load(model_uri)
    write_test_log("predict load model\n")
    try:
        text, words = model.filter(data)
    except Exception as e:
        write_test_log(str(e))
        return
    write_test_log("predict filter\n")
    isFake = True if (len(words) > 4) else False
    return isFake, words


from flask import Flask
from flask import request

app = Flask(__name__)

import time


class ServiceLive:
    live_time = True

    @staticmethod
    def get_live():
        return ServiceLive.live_time

    t_lock = threading.Lock()

    @staticmethod
    def set_live(value):
        ServiceLive.t_lock.acquire()
        ServiceLive.live_time = value
        ServiceLive.t_lock.release()


def _async_raise(tid, exctype):
    """raises the exception, performs cleanup if needed"""
    tid = ctypes.c_long(tid)
    if not inspect.isclass(exctype):
        exctype = type(exctype)
    res = ctypes.pythonapi.PyThreadState_SetAsyncExc(tid, ctypes.py_object(exctype))
    if res == 0:
        raise ValueError("invalid thread id")
    elif res != 1:
        # """if it returns a number greater than one, you're in trouble,
        # and you should call it again with exc=NULL to revert the effect"""
        ctypes.pythonapi.PyThreadState_SetAsyncExc(tid, None)
        raise SystemError("PyThreadState_SetAsyncExc failed")


def stop_thread(thread):
    _async_raise(thread.ident, SystemExit)


def listener_heart():
    while True:
        time.sleep(20)
        write_test_log(f"check receiver status:{ServiceLive.get_live()}")
        if not ServiceLive.get_live():
            break
        else:
            ServiceLive.set_live(False)
    # 如果超过60秒没有收到心跳消息，则终止程序
    stop_thread(worker)
    write_test_log("disconnect bot, 20s")
    import sys
    sys.exit()


@app.route('/heart', methods=['GET'])
def heart():
    try:
        # 更新live_time
        ServiceLive.set_live(True)
        write_test_log(f"receiver bot heart,receiver status:{ServiceLive.get_live()}")
        return 'success'
    except Exception as e:
        write_test_log(str(e))
        return str(e)


@app.route('/train', methods=['GET'])
def train_model():
    global model
    with open("data/keywords.txt", 'r', encoding='utf-8') as f:
        data = f.readlines()
        f.close()
    model = train(''.join(data))
    return 'success'


@app.route('/keywords/add', methods=["GET", "POST"])
def addKeywords():
    text = request.form['text']
    add_keyword_data_set(text)
    return 'success'


@app.route('/predict', methods=['GET', 'POST'])
def predict_url():
    write_test_log("start predict")
    text = request.form['text']
    try:
        isFake, words = predict(text)
        return {
            "isFake": isFake,
            "words": words
        }
    except Exception as e:
        return e


@app.route("/", methods=['GET'])
def hello():
    import os
    return os.getcwd()


def write_test_log(log):
    with open("test.txt", 'a+') as f:
        f.write(f"{datetime.now()} : {log} \n")
        f.close()


# @app.handle_exception(e=Exception)
# def exception():
#     write_test_log(e)


if __name__ == '__main__':
    import logging

    # log = logging.getLogger('werkzeug')
    # log.disabled = True
    # app.logger.disabled = True
    try:
        # 监听心跳，若长时间未收到心跳消息则终止程序
        threading.Thread(target=listener_heart).start()
        write_test_log("start")
        stop_words = read_stopwords()
        write_test_log("start stop words")
        model = load(model_uri)
        write_test_log("load model")
        lac = LAC(mode='lac')
        write_test_log("load lac")
        worker = threading.Thread(target=app.run, args=("127.0.0.1", 9001))
        worker.start()
        worker.join()
    except Exception as e:
        write_test_log(str(e))
