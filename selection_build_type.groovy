// Test on https://gwc-experiment.appspot.com/#Xyc4wJbSkn

// Wright logic
def version = "1.4.9~release-51"
def isProd = version.contains("~release")
def buildType = isProd ? "PRODUCTION" : "DEVELOPMENT"
println "Version: ${version}"
println "Contains ~release: ${version.contains("~release")}"
println "Is production: ${isProd}"
println "Build type: ${buildType}"

def version2 = "1.5.0~dev-43"
def isProd2 = version2.contains("~release")
def buildType2 = isProd2 ? "PRODUCTION" : "DEVELOPMENT"
println "\nVersion: ${version2}"
println "Contains ~release: ${version2.contains("~release")}"
println "Is production: ${isProd2}"
println "Build type: ${buildType2}"

