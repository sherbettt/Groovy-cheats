#!/usr/bin/env groovy

def findBuiltPackagesSimple() {
    def workspacePath = "/root/git/"
    def directory = new File(workspacePath)
    
    if (!directory.exists() || !directory.directory) {
        println "Директория не существует или не является папкой: $workspacePath"
        return []
    }
    
    // Находим все файлы, соответствующие шаблону
    def packageFiles = directory.listFiles({ file -> 
        file.isFile() && file.name ==~ /runtel-sbc.*\.deb/
    } as FileFilter)
    
    if (!packageFiles || packageFiles.size() == 0) {
        println "Собранные пакеты не найдены"
        return []
    }
    
    // Извлекаем только имена файлов
    def packages = packageFiles.collect { it.name }
    println "Найдены пакеты runtel-sbc: ${packages.join(', ')}"
    
    return packages
}

// --------------------

// Groovy DSL (Domain Specific Language) ниже

/**
 * Поиск собранных пакетов в формате runtel-sbc-*.deb
 *
 * @return список имен найденных пакетов или пустой список, если пакеты не найдены
 */
def findBuiltPackages() {
    // (alt var) find /var/lib/jenkins/workspace -name 'runtel-sbc*.deb' -type f 2>/dev/null
    // поиск runtel-sbc*.deb | извлечение имён без пути || если код возврата не 0 -> 'No packages found'
    def packages = sh(
        script: "ls /var/lib/jenkins/workspace/runtel-sbc*.deb 2>/dev/null | xargs -n 1 basename 2>/dev/null || echo 'No packages found'",
        returnStdout: true
    ).trim()
    
    if (packages == "No packages found") {
        echo "Собранные пакеты не найдены"
        return []
    }
    
    def packageList = packages.split('\n')                              // Разбиваем строку с именами пакетов (по одному на строку) в массив
    echo "Найдены пакеты runtel-sbc: ${packageList.join(', ')}"         // Логируем найденные пакеты в удобочитаемом формате
    return packageList                                                  // Возвращаем список (массив) имен пакетов
}

