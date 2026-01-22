#!/usr/bin/env groovy

// Используем bash для выполнения полной команды
def cmd = "ls /root/git/runtel-sbc*.deb 2>/dev/null | xargs -n 1 basename 2>/dev/null"
def process = ["bash", "-c", cmd].execute()
def result = process.text.readLines()

if (result) {
    result.each { println it }
} else {
    println "Пакеты не найдены"
}
