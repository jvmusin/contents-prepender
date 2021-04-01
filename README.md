# Contents Prepender

[![Gradle CI](https://github.com/jvmusin/contents-prepender/actions/workflows/gradle-build.yml/badge.svg)](https://github.com/jvmusin/contents-prepender/actions/workflows/gradle-build.yml)
[![Google Java Format](https://github.com/jvmusin/contents-prepender/actions/workflows/google-java-format.yml/badge.svg)](https://github.com/jvmusin/contents-prepender/actions/workflows/google-java-format.yml)

## Возможности

Проект умеет читать `Markdown` файлы, генерировать из них оглавление и добавлять отформатированное оглавление к файлу.

Запускается через `./gradlew run --args="path/to/input.md"` и выводит результат в `stdout`.

Запуск на примере из описания задачи: `./gradlew run --args="examples/input.md"`.

## Реализация

Проект разбит на несколько частей

### Contents Extractor

Вытаскивает из строк документа все заголовки. Реализован в [ContentsExtractor].

### Contents Validator

Принимает на вход оглавление и проверяет его на корректность.

Оглавление некорректно, если самый первый заголовок имеет уровень больше, чем первый, либо если заголовок с
индексом `i+1` имеет уровень хотя бы на 2 больший, чем заголовок `i`.

Также оглавление некорректно, если заголовок имеет уровень, меньший 1, но такое поведение получить, кроме как где-то
сломав код, нельзя.

Реализован в [ContentsValidator].

### Contents Renderer

Принимает на вход оглавление и строит из него строки в том формате, который указан в описании задачи.

Из оглавлений вида `[уровень, имя]` для следующих оглавлений

```
[1, My Project]
[2, Implementation]
[3, Step 1]
[3, Step 2]
```

Построит следующие строки

```
1. [My Project](#my-project)
    1. [Idea](#idea)
    2. [Implementation](#implementation)
        1. [Step 1](#step-1)
        2. [Step 2](#step-2)
```

И вернёт эти строки.

Реализован в [ContentsRenderer].

### Contents Prepender

Собирает воедино все вышеописанные обработчики и позволяет для строки с контентом файла на входе получить контент
результирующего файла.

План следующий:

* Разбить контент на строки;
* Из строк достать все заголовки, тем самым получив оглавление ([ContentsExtractor]);
* Проверить оглавление на корректность ([ContentsValidator]);
* Отрисовать оглавление ([ContentsRenderer]);
* Склеить оглавление и контент из ввода и вернуть.

Если в файле не было оглавлений, то ничего не добавляется.

Из файла [input.md](examples/input.md)

```
# My Project
## Idea
content
## Implementation
### Step 1
content
### Step 2
content
```

Получится [output.md](examples/output.md)

```
1. [My Project](#my-project)
    1. [Idea](#idea)
    2. [Implementation](#implementation)
        1. [Step 1](#step-1)
        2. [Step 2](#step-2)

# My Project
## Idea
content
## Implementation
### Step 1
content
### Step 2
content
```

Реализован в [ContentsPrepender].

[ContentsExtractor]: src/main/java/jvmusin/contentsprepender/ContentsExtractor.java

[ContentsValidator]: src/main/java/jvmusin/contentsprepender/ContentsValidator.java

[ContentsRenderer]: src/main/java/jvmusin/contentsprepender/ContentsRenderer.java

[ContentsPrepender]: src/main/java/jvmusin/contentsprepender/ContentsPrepender.java
