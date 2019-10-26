yum update -y
rpm –import https://www.elrepo.org/RPM-GPG-KEY-elrepo.org
rpm -Uvh http://www.elrepo.org/elrepo-release-7.0-2.el7.elrepo.noarch.rpm
yum install -y linux-firmware
yum install -y perl
wget http://acs-public-mirror.oss-cn-hangzhou.aliyuncs.com/elrepo-kernel-lt/kernel-lt-4.4.6-1.el7.elrepo.x86_64.rpm
wget http://acs-public-mirror.oss-cn-hangzhou.aliyuncs.com/elrepo-kernel-lt/kernel-lt-devel-4.4.6-1.el7.elrepo.x86_64.rpm
wget http://acs-public-mirror.oss-cn-hangzhou.aliyuncs.com/elrepo-kernel-lt/kernel-lt-headers-4.4.6-1.el7.elrepo.x86_64.rpm

rpm -i kernel-lt-4.4.6-1.el7.elrepo.x86_64.rpm
yum remove kernel-{firmware,headers,devel} -y
rpm -i kernel-lt-headers-4.4.6-1.el7.elrepo.x86_64.rpm
rpm -i kernel-lt-devel-4.4.6-1.el7.elrepo.x86_64.rpm
rm -f kernel-lt-headers-4.4.6-1.el7.elrepo.x86_64.rpm kernel-lt-devel-4.4.6-1.el7.elrepo.x86_64.rpm kernel-lt-4.4.6-1.el7.elrepo.x86_64.rpm

echo “exclude=kernel*” >> /etc/yum.conf
mkinitrd -f –allow-missing –with=xen-blkfront –preload=xen-blkfront –with=virtio_blk –preload=virtio_blk –with=virtio_pci –preload=virtio_pci –with=virtio_console –preload=virtio_console /boot/initramfs-4.4.6-1.el7.elrepo.x86_64.img 4.4.6-1.el7.elrepo.x86_64
grub2-set-default 'CentOS Linux (4.4.6-1.el7.elrepo.x86_64) 7 (Core)'
