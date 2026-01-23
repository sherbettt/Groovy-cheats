Задавать в build.groovy

```groovy
def getIP() {
    def ip = sh(
        script: "ip addr show eth0 | grep 'inet ' | awk '{print \$2}' | cut -d/ -f1",
        returnStdout: true
    ).trim()
    return ip
}

def getHostname() {
    def hostname = sh(
        script: "hostname",
        returnStdout: true
    ).trim()
    return hostname
}

def getFQDN() {
    def fqdn = getHostname() + "." + getDomain()
    return fqdn
}

def getDomain() {
    def domain = sh(
        script: "hostname -d",
        returnStdout: true
    ).trim()
    return domain
}

def getArch() {
    def arch = sh(
        script: "uname -m",
        returnStdout: true
    ).trim()
    return arch
}

def getOS() {
    def os = sh(
        script: "uname -s",
        returnStdout: true
    ).trim()
    return os
}

def getKernel() {
    def kernel = sh(
        script: "uname -r",
        returnStdout: true
    ).trim()
    return kernel
}

def getDistro() {
    def distro = sh(
        script: "lsb_release -d | awk '{print \$2}'",
        returnStdout: true
    ).trim()
    return distro
}   

def getRelease() {
    def release = sh(
        script: "lsb_release -r | awk '{print \$2}'",
        returnStdout: true
    ).trim()
    return release
}

def getCodename() {
    def codename = sh(
        script: "lsb_release -c | awk '{print \$2}'",
        returnStdout: true
    ).trim()
    return codename
}

def getArchitecture() {
    def architecture = sh(
        script: "uname -m",
        returnStdout: true
    ).trim()
    return architecture
}

def getKernelVersion() {
    def kernelVersion = sh(
        script: "uname -r",
        returnStdout: true
    ).trim()
    return kernelVersion
}

```
