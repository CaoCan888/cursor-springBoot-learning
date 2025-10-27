#!/bin/bash

# Redis 安装脚本
# 适用于 Ubuntu/Debian 系统

echo "开始安装 Redis..."

# 更新系统包
sudo apt-get update

# 安装 Redis
sudo apt-get install -y redis-server

# 配置 Redis
sudo sed -i 's/bind 127.0.0.1 ::1/bind 0.0.0.0/g' /etc/redis/redis.conf
sudo sed -i 's/protected-mode yes/protected-mode no/g' /etc/redis/redis.conf

# 如需设置密码，取消下面的注释并修改密码
# sudo sed -i 's/# requirepass foobared/requirepass yourpassword/g' /etc/redis/redis.conf

# 启动 Redis 服务
sudo systemctl start redis-server

# 设置开机自启
sudo systemctl enable redis-server

# 检查 Redis 状态
echo "检查 Redis 状态..."
sudo systemctl status redis-server

echo "安装完成！"
echo "Redis 监听地址: 0.0.0.0:6379"
echo "测试连接: redis-cli ping"
