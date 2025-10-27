#!/bin/bash

# Redis 安装脚本
# 适用于 CentOS/RHEL 系统

echo "开始安装 Redis..."

# 安装 EPEL 仓库
sudo yum install -y epel-release

# 安装 Redis
sudo yum install -y redis

# 配置 Redis 允许远程访问
sudo sed -i 's/bind 127.0.0.1/bind 0.0.0.0/g' /etc/redis.conf
sudo sed -i 's/protected-mode yes/protected-mode no/g' /etc/redis.conf

# 如需设置密码，取消下面的注释并修改密码
# sudo sed -i 's/# requirepass foobared/requirepass yourpassword/g' /etc/redis.conf

# 启动 Redis 服务
sudo systemctl start redis

# 设置开机自启
sudo systemctl enable redis

# 检查 Redis 状态
echo "检查 Redis 状态..."
sudo systemctl status redis

echo "安装完成！"
echo "Redis 监听地址: 0.0.0.0:6379"
echo "测试连接: redis-cli ping"
