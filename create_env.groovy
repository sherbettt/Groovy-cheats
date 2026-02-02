def createContainer(){
    def ID_LXC=800
    def IMAGE_NAME="debian-12-standard_12.12-1_amd64.tar.zst"
    def HOSTNAME="cycle-single-builder"

    sh label: '=== Checking storage Proxmox ===', script: '''
        pvesm status
    '''

    echo "=== Creating Proxmox Container ==="
    sh label: 'Creating Proxmox Container', script: """
        pct create ${ID_LXC} \\
            /stg/1tb/template/cache/${IMAGE_NAME} \\
            --unprivileged 0 \\
            --hostname ${HOSTNAME} \\
            --storage ssd_1tb \\
            --password 'runtelorg' \\
            --memory 4096 \\
            --cores 4 \\
            --swap 1024 \\
            --rootfs ssd_1tb:38 \\
            --net0 name=eth0,bridge=vmbr0,ip=dhcp,ip6=auto \\
            --features "nesting=1,fuse=1,keyctl=1,mount=nfs;cifs" \\
            --start \\
            --onboot 0 \\
            --cmode shell \\
            --description 'Node for cycle_single_node project'
    """      

    sh label: '=== Checking container ID_LXC ===', script: """
        pct list | grep ${ID_LXC}
        pct exec ${ID_LXC} -- hostname
        pct exec ${ID_LXC} -- ip a s eth0
    """

    sh label: '=== Check created LXC conf ===', script: """
        ls -alF /etc/pve/lxc/${ID_LXC}.conf || true
    """

    // Получаем динамический IP адрес контейнера
    def getContainerIp_alt(){
        sh label: '=== Checking container IP ===', script: """
            pct exec ${ID_LXC} -- hostname -I | awk '{print \$1}'
        """
    }
    return getContainerIp_alt()

    // Функция для получения IP адреса контейнера
    def getContainerIp() {
        def ip = sh(
            script: """
                pct exec ${ID_LXC} -- ip a s eth0 | grep 'inet ' | awk '{print \$2}' | cut -d/ -f1
            """,
            returnStdout: true
        ).trim()
        echo "Получен динамический IP адрес контейнера: ${ip}"
        return ip
    }

    // Устанавливаем статический IP адрес после создания контейнера
    def realIp = getContainerIp()

    // Функция для изменения IP адреса на статический
    /**
    * @param staticIp - статический IP адрес
    * def realIp = getContainerIp() - создает переменную с именем realIp и сохраняет в нее IP адрес контейнера
    * setStaticIp(realIp) - передает значение внешней переменной realIp в параметр функции; 
    * Независимо от того, как назван параметр (realIp или staticIp), передается одно и то же значение
    */
    def setStaticIp(staticIp) {  
        echo "Останавливаем контейнер ${ID_LXC}"
        sh "pct stop ${ID_LXC}"
    
        echo "Изменяем Динамический IP ${staticIp} на статический"
        sh "pct set ${ID_LXC} -net0 name=eth0,gw=192.168.87.1,bridge=vmbr0,ip=${staticIp}/24"
    
        echo "Запускаем контейнер ${ID_LXC}"
        sh "pct start ${ID_LXC}"
    }


    // Пример использования функции для установки статического IP
    // setStaticIp(${ID_LXC}, "${realIp}")
    
    return realIp

    /**
    def createBackup(String containerID, pmx6IP) {
        sh """
            ssh -o StrictHostKeyChecking=no root@${pmx6IP} '
                echo "Создаём бекап контейнера ${containerID}..."
                pct stop ${containerID}
                vzdump ${containerID} --storage ssd_1tb --mode stop --compress zstd
                pct start ${containerID}
        
                # Находим бекапы
                BACKUP_FILE=\$(ls -t /stg/1tb/dump/vzdump-lxc-${containerID}-*.tar.zst | head -1)
                echo "Бекап создан: \$BACKUP_FILE"
            '
        """
    }
    **/

def findBackup(String containerID, pmx6IP) {
    sh """
        ssh -o StrictHostKeyChecking=no root@${pmx6IP} '
            echo "Находим последний бекап контейнера ${containerID}"        
            BACKUP_FILE=\$(ls -t /stg/1tb/dump/vzdump-lxc-${containerID}-*.tar.zst | head -1)
            echo "Бекап создан: \$BACKUP_FILE"
        '
    """
}



def restoreBackup(String containerID, pmx6IP) {
    sh """
        ssh -o StrictHostKeyChecking=no root@${pmx6IP} '
            echo "Восстанавливаем бекап контейнера ${containerID}"
            pct stop ${containerID}
            pct destroy ${containerID}
            pct restore ${containerID} vzdump-lxc-800-2026_02_02-14_04_07.tar.zst --storage ssd_1tb
            pct start ${containerID}
        '
    """
}



}




