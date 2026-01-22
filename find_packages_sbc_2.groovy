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
