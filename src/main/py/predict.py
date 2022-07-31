import threading


def load_model():
    import pickle
    with open('model/model.pkl', 'rb') as f:
        clf = pickle.load(f)
        f.close()
    return clf


label_dict = {
    'fuck_sister': 1,
    'normal': 0
}
label_result = {value: key for key, value in label_dict.items()}


def read_stopwords():
    file_name = '../python/FuckSchoolSister/data/cn_stopwords.txt'
    with open(file_name, 'r', encoding='utf-8') as f:
        stopwords = f.read()
    return stopwords


def remove_stopwords(data):
    data_new = []
    stopwords = read_stopwords()
    for line in data:
        line_new = ''
        for word in line:
            if word not in stopwords:
                line_new += word
        data_new.append(line_new)
    return data_new


clf = load_model()


def async_load_model():
    global clf
    clf = load_model()


def predict(text):
    global clf
    if clf is None:
        print("model no found")
        return
    print("load model")
    text_new = remove_stopwords([text])
    y_pred = clf.predict(text_new)
    print("get predict")
    return label_result[y_pred[0]]


import socketserver


class myTCPhandler(socketserver.BaseRequestHandler):
    def handle(self):
        while True:
            self.data = self.request.recv(1024).decode('UTF-8', 'ignore').strip()
            if not self.data:
                break
            print(self.data)
            self.feedback_data = (predict(self.data)).encode("utf8")
            print("发送成功")
            self.request.sendall(self.feedback_data)


if __name__ == '__main__':
    host = '127.0.0.1'
    port = 9001
    server = socketserver.ThreadingTCPServer((host, port), myTCPhandler)
    server.serve_forever()
