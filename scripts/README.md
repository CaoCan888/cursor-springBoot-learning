# Redis 安装指南

## 方式一：使用脚本安装（推荐）

### Ubuntu/Debian 系统
```bash
# 连接服务器
ssh root@47.116.3.95

# 上传脚本（或直接创建）
wget https://your-domain.com/scripts/install-redis.sh
# 或者直接复制脚本内容

# 赋予执行权限
chmod +x install-redis.sh

# 执行安装
./install-redis.sh
```

### CentOS/RHEL 系统
```bash
chmod +x install-redis-centos.sh
./install-redis-centos.sh
```

## 方式二：手动安装

### Ubuntu/Debian
```bash
# 1. 更新系统
sudo apt-get update

# 2. 安装 Redis
sudo apt-get install -y redis-server

# 3. 编辑配置文件
sudo nano /etc/redis/redis.conf

# 修改以下配置：
# bind 127.0.0.1 ::1 改为 bind 0.0.0.0
# protected-mode yes 改为 protected-mode no

# 4. 启动 Redis
sudo systemctl start redis-server

# 5. 设置开机自启
sudo systemctl enable redis-server

# 6. 检查状态
sudo systemctl status redis-server
```

### CentOS/RHEL
```bash
# 1. 安装 EPEL 仓库
sudo yum install -y epel-release

# 2. 安装 Redis
sudo yum install -y redis

# 3. 编辑配置文件
sudo nano /etc/redis.conf

# 修改配置同上

# 4. 启动 Redis
sudo systemctl start redis
sudo systemctl enable redis

# 5. 检查状态
sudo systemctl status redis
```

## 方式三：Docker 安装（最简单）

```bash
# 拉取 Redis 镜像
docker pull redis:7-alpine

# 启动 Redis 容器
docker run -d \
  --name redis-learning \
  -p 6379:6379 \
  -v redis-data:/data \
  redis:7-alpine

# 测试连接
docker exec -it redis-learning redis-cli ping
```

## 安全配置（重要）

### 1. 设置密码
编辑配置文件添加密码：
```bash
# 编辑配置文件
sudo nano /etc/redis/redis.conf

# 找到 # requirepass foobared，取消注释并修改
requirepass your_strong_password_here
```

### 2. 重启服务
```bash
sudo systemctl restart redis-server  # Ubuntu
sudo systemctl restart redis         # CentOS
```

### 3. 防火墙配置
```bash
# 开放 6379 端口
sudo ufw allow 6379/tcp        # Ubuntu
sudo firewall-cmd --add-port=6379/tcp --permanent  # CentOS
sudo firewall-cmd --reload     # CentOS
```

## 测试连接

### 本地测试
```bash
redis-cli ping
# 应该返回: PONG

# 如果设置了密码
redis-cli -a your_password ping
```

### 远程测试
```bash
# 连接测试
redis-cli -h 47.116.3.95 -p 6379 ping

# 如果设置了密码
redis-cli -h 47.116.3.95 -p 6379 -a your_password ping
```

## 修改项目配置

安装完成后，修改项目配置文件：

```yaml
# src/main/resources/application.yml
spring:
  data:
    redis:
      host: 47.116.3.95  # 修改为服务器地址
      port: 6379
      password: your_password  # 如果设置了密码
```

## 常用命令

```bash
# 查看 Redis 状态
sudo systemctl status redis-server

# 启动 Redis
sudo systemctl start redis-server

# 停止 Redis
sudo systemctl stop redis-server

# 重启 Redis
sudo systemctl restart redis-server

# 查看 Redis 日志
sudo tail -f /var/log/redis/redis-server.log

# 进入 Redis CLI
redis-cli

# 在 Redis CLI 中查看所有 key
KEYS *

# 查看 Redis 信息
INFO
```
