 [pipeline-examples](https://github.com/jenkinsci/pipeline-examples)


# Обработка исключений в Groovy

В Groovy обработка исключений очень похожа на Java, но с некоторыми упрощениями и дополнительными возможностями. Вот основные примеры:

## Базовый пример обработки исключений

```groovy
try {
    def result = 10 / 0
    println "Результат: $result"
} catch (ArithmeticException e) {
    println "Ошибка: деление на ноль!"
} catch (Exception e) {
    println "Произошла неизвестная ошибка: ${e.message}"
} finally {
    println "Этот блок выполняется всегда"
}
```

## Использование Groovy-специфичных возможностей

### Необязательные скобки в catch

```groovy
try {
    def file = new File("nonexistent.txt").text
} catch FileNotFoundException e {
    println "Файл не найден: ${e.message}"
}
```

### Обработка нескольких исключений в одном блоке catch

```groovy
try {
    def num = "abc" as Integer
} catch (NumberFormatException | NullPointerException e) {
    println "Ошибка преобразования или null: ${e.class.name}"
}
```

## Создание собственных исключений

```groovy
class MyCustomException extends Exception {
    MyCustomException(String message) {
        super(message)
    }
}

try {
    throw new MyCustomException("Кастомная ошибка")
} catch (MyCustomException e) {
    println "Поймано кастомное исключение: ${e.message}"
}
```

## Использование try-with-resources

```groovy
new File("example.txt").withWriter { writer ->
    writer.writeLine("Hello, Groovy!")
    // Ресурс автоматически закроется
}

// Эквивалентно Java-версии:
try (def writer = new File("example.txt").newWriter()) {
    writer.writeLine("Hello, Groovy!")
}
```

## Безопасный доступ через оператор безопасной навигации (?.)

```groovy
def str = null
try {
    println str?.toUpperCase() // Не вызовет NullPointerException
} catch (Exception e) {
    println "Ошибка: ${e.message}"
}
```

## Использование методов fail() и shouldFail() для тестирования исключений

```groovy
// В тестах Spock или GroovyTestCase
def "test division by zero"() {
    when:
    def result = 10 / 0
    
    then:
    thrown(ArithmeticException)
}

// Альтернатива с shouldFail
def result = shouldFail(ArithmeticException) {
    10 / 0
}
```
-------
