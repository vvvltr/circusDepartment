import re
import string

import nltk
from flask import Flask, request, jsonify
from nltk.corpus import stopwords
from sklearn.feature_extraction.text import TfidfVectorizer

nltk.download('stopwords')
nltk.download('punkt')
from bs4 import BeautifulSoup

app = Flask(__name__)


@app.route('/skills', methods=['POST'])
def get_skills():
    if not request.is_json:
        print("Invalid input, expected JSON")
        return jsonify({"error": "Invalid input, expected JSON"}), 400
    items = request.json.get('Items', [])
    if not items:
        print("No search terms provided")
        return jsonify({"error": "No search terms provided"}), 400
    results = perform_search(items)
    skills = get_descriptions_skills(items)
    return jsonify({"Items": results, "Skills": skills})


def get_descriptions_skills(items):
    print("Общий")
    _description = [item["Description"] for item in items]
    # Применение функцию удаления к каждому элементу
    _description = list(map(remove_html_tags, _description))
    _description = list(map(get_only_english, _description))
    # Получение стоп-слов
    stop_words = set(stopwords.words('english'))

    # Токенизация предложений с учетом игнорирования стоп-слов
    tokenized = [[word for word in doc if word not in stop_words] for doc in _description]

    all_words = [word for doc in tokenized for word in doc]
    all_words = [word.lower() for word in all_words]
    print(all_words)
    # Создание объекта TF-IDF для слов
    word_vectorizer = TfidfVectorizer(ngram_range=(1, 1), analyzer='word')

    # Создание объекта TF-IDF для фраз
    phrase_vectorizer = TfidfVectorizer(ngram_range=(2, 2), analyzer='word')

    # Преобразование текста в TF-IDF векторы
    X = word_vectorizer.fit_transform(list(filter(lambda x: ' ' not in x, all_words)))
    Y = phrase_vectorizer.fit_transform(list(filter(lambda x: ' ' in x, all_words)))

    print("Словарь:")
    print(word_vectorizer.get_feature_names_out())
    print(phrase_vectorizer.get_feature_names_out())

    # Получение словаря с парами (слово, индекс)
    word_vocab = word_vectorizer.vocabulary_
    phrase_vocab = phrase_vectorizer.vocabulary_
    vocab = {**word_vocab, **phrase_vocab}
    reverse_vocab = {index: word for word, index in vocab.items()}
    # Суммирование вхождений каждого слова
    word_counts = X.sum(axis=0)
    # Преобразование результатов в список кортежей (слово, количество вхождений)
    word_counts_list = [(reverse_vocab[i], count) for i, count in enumerate(word_counts.tolist()[0])]
    word_counts_1 = Y.sum(axis=0)
    # Преобразование результатов в список кортежей (слово, количество вхождений)
    word_counts_list_1 = [(reverse_vocab[i], count) for i, count in enumerate(word_counts_1.tolist()[0])]
    # Сортировка по количеству вхождений
    all_list = merge_tuples(word_counts_list + word_counts_list_1)
    all_list.sort(key=lambda x: x[1], reverse=True)
    top_20_words = all_list[:20]
    return [t[0] for t in top_20_words]


def merge_tuples(tuples):
    result_dict = {}
    for t in tuples:
        if t[0] in result_dict:
            result_dict[t[0]].append(t[1])
        else:
            result_dict[t[0]] = [t[1]]
    # Преобразование словаря в список кортежей
    result_list = [(k, v) for k, v in result_dict.items()]
    return result_list


def remove_html_tags(text):
    soup = BeautifulSoup(text, "html.parser")
    return soup.get_text()


def get_only_english(text):
    return list(filter(bool, [item.strip() for item in re.split(r'[^A-Za-z\s]', text)]))


def perform_search(items):
    results = []
    for item in items:
        skills = description(item["Description"])
        results.append({
            "id": item["Id"],
            "skills": skills
        })
    return results


def description(item):
    # Для удаления пунктуации
    item = remove_html_tags(item)
    all_words = get_only_english(item)
    return all_words


def remove_punctuation(text):
    translator = str.maketrans(string.punctuation, ' ' * len(string.punctuation))
    _text = text.replace("\n", " ")
    return _text.translate(translator)


if __name__ == '__main__':
    app.run(host='0.0.0.0')
