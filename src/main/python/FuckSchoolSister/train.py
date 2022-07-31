# -*- coding: utf-8 -*-
# @Time    : 2022/7/30 16:36
# @Author  : 陈昊
# @Email   : i@umb.ink
# @File    : train.py
# @Software: PyCharm
import numpy as np
import sklearn

'''使用贝叶斯分类器训练文本识别模型'''
from sklearn.naive_bayes import MultinomialNB
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.feature_extraction.text import TfidfTransformer
from sklearn.pipeline import Pipeline
from sklearn.model_selection import train_test_split
from sklearn.metrics import classification_report
from sklearn.metrics import confusion_matrix
from sklearn.metrics import accuracy_score
from sklearn.metrics import precision_score
from sklearn.metrics import recall_score
from sklearn.metrics import f1_score
from sklearn.metrics import precision_recall_fscore_support
from sklearn.metrics import roc_auc_score

data_set_uri = {
    'fuck_sister': 'data/data-pssisterad.txt',
    'normal': 'data/data-normal.txt'
}
label_dict = {
    'fuck_sister': 1,
    'normal': 0
}
label_result = {value: key for key, value in label_dict.items()}


def read_data(file_name):
    '''读取数据'''
    data = []
    with open(file_name, 'r', encoding='utf-8') as f:
        lines = f.readlines()
        for line in lines:
            if len(line) > 1:
                data.append(line.strip())
        f.close()
    data = remove_stopwords(data)
    return data


'''读取停用词'''


def read_stopwords():
    file_name = 'data/cn_stopwords.txt'
    with open(file_name, 'r', encoding='utf-8') as f:
        stopwords = f.read()
    return stopwords


'''去除停用词'''


def remove_stopwords(data):
    '''
    :param data: 文本数据
    :param stopwords: 停用词
    :return: 去除停用词后的文本数据
    '''
    data_new = []
    stopwords = read_stopwords()
    for line in data:
        line_new = ''
        for word in line:
            if word not in stopwords:
                line_new += word
        data_new.append(line_new)
    return data_new


tf = TfidfTransformer()
vectorizer = CountVectorizer()

'''训练模型'''


def train_model(data_set_uri):
    global label_set, train_set
    for key in data_set_uri:
        data = read_data(data_set_uri[key])
        train_set.extend(data)
        [label_set.append(label_dict[key]) for _ in range(len(data))]
    train_set = np.array(train_set)
    label_set = np.array(label_set)
    X_train, X_test, y_train, y_test = train_test_split(train_set, label_set, test_size=0.2, random_state=0)
    clf = Pipeline([('vect', vectorizer), ('tfidf', tf), ('clf', MultinomialNB())])
    clf.fit(X_train, y_train)
    y_pred = clf.predict(X_test)
    print(classification_report(y_test, y_pred))
    print(confusion_matrix(y_test, y_pred))
    print(accuracy_score(y_test, y_pred))
    print(precision_score(y_test, y_pred, average='micro'))
    print(recall_score(y_test, y_pred, average='micro'))
    print(f1_score(y_test, y_pred, average='micro'))
    print(precision_recall_fscore_support(y_test, y_pred, average='micro'))
    try:
        save_model(clf)
    except:
        pass
    return clf


def predict(text):
    global clf
    if clf is None:
        clf = load_model()
    text_new = remove_stopwords([text])
    y_pred = clf.predict(text_new)
    return label_result[y_pred[0]]


def save_model(clf):
    import pickle
    with open('model/model.pkl', 'wb') as f:
        pickle.dump(clf, f)
    f.close()


def load_model():
    import pickle
    with open('model/model.pkl', 'rb') as f:
        clf = pickle.load(f)
    f.close()
    return clf


clf = None

# if __name__ == '__main__':
#     label_set = []
#     train_set = []  # 训练集
#     clf = train_model(data_set_uri)
