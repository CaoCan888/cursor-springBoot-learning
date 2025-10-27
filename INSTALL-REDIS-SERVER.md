# 在服务器 47.116.3.95 上安装 Redis

## 🚀 快速安装（推荐方式）

### 方式一：一键脚本安装

#### Ubuntu/Debian 系统
```bash
# 1. 连接到服务器
ssh root@47.116.3.95

# 2. 创建安装脚本并执行
cat > /tmp/install-redis.sh << 'EOF'
#!/bin/bash
sudo apt-get update
sudo apt-get install -y redis-server
sudo sed -i 's/bind 127.0.0.1 ::1/bind 0.0.0.0/g' /etc/redis/redis.conf
sudo sed -i 's/protected-mode yes/protected-mode no/g' /etc/redis/redis.conf
sudo systemctl start redis-server
sudo systemctl enable redis-server
sudo systemctl status redis-server
EOF

chmod +x /tmp/install-redis.sh
/tmp/install-redis.sh

# 3. 测试连接
redis-cli ping
```

#### CentOS/RHEL 系统
```bash
cat > /tmp/install-redis.sh << 'EOF'
#!/bin/bash
sudo yum install -y epel-release
sudo yum install -y redis
sudo sed -i 's/bind 127.0.0.1/bind 0.0.0.0/g' /etc/redis.conf
sudo sed -i 's/protected-mode yes/protected-mode no/g' /etc/redis.conf
sudo systemctl start redis
sudo systemctl enable redis
sudo systemctl status redis
EOF

chmod +x /tmp/install-redis.sh
/tmp/install-redis.sh
```

### 方式二：Docker 安装（最简单）

```bash
# 1. 连接到服务器
ssh root@47.116.3.95

# 2. 安装 Docker（如果未安装）
curl -fsSL https://get.docker.com -o get-docker.sh
sh get-docker.sh

# 3. 启动 Redis 容器
docker run -d \
  --name redis-learning \
  --restart=always \
  -p 6379:6379 \
  -v redis-data:/data \
  redis:7-alpine redis-server --appendonly yes

# 4. 测试连接
docker exec -it redis-learning redis-cli ping
```

## 🔧 配置修改

安装完成后，如果后续需要远程访问，需要修改配置文件：

### Ubuntu/Debian
```bash
# 编辑配置
sudo nano /etc/redis/redis.conf

# 修改以下两行：
bind 127.0.0.1 ::1  →  bind 0.0.0.0
protected-mode yes   →  protected-mode no
```

### CentOS
```bash
sudo nano /etc/redis.conf

# 修改同上
```

### 重启服务
```bash
# Ubuntu
sudo systemctl restart redis-server

# CentOS
sudo systemctl restart redis

# 检查状态
sudo systemctl status redis-server
```

## 🔒 安全配置（可选但推荐）

### 设置密码
```bash
# 编辑配置文件
sudo nano /etc/redis/redis.conf  # Ubuntu
# 或
sudo nano /etc/redis.conf  # CentOS

# 找到 # requirepass foobared，取消注释并设置强密码
requirepass YourStrongPassword123!

# 重启服务
sudo systemctl restart redis-server
```

### 配置防火墙
```bash
# Ubuntu
sudo ufw allow 6379/tcp
sudo ufw reload

# CentOS
sudo firewall-cmd --add-port=6379/tcp --permanent
sudo firewall-cmd --reload
```

## ✅ 验证安装

### 本地测试
```bash
redis-cli ping
# 应该返回: PONG

# 如果设置了密码
redis-cli -a YourStrongPassword123! ping
```

### 远程测试（从本地机器）
```bash
# 在本地机器（Windows）上，使用 Redis CLI 或 PowerShell 测试

# 安装 Redis CLI（Windows）
# 下载：https://github.com/microsoftarchive/redis/releases

# 或使用 Docker 测试
docker run -it --rm redis redis-cli -h 47.116.3.95 -p 6379 ping

# 如果设置了密码
docker run -it --rm redis redis-cli -h 47.116.3.95 -p 6379 -a YourStrongPassword123! ping
```

## 📝 修改项目配置

Redis 安装成功后，需要修改 Spring Boot 项目的配置文件：

### 编辑 application.yml
```yaml
spring:
  data:
    redis:
      host: 47.116.3.95  # 修改为你的服务器地址
      port: 6379
      password: YourStrongPassword123!  # 如果设置了密码
      timeout: 2000
      lettuce:
        pool:
          max-active: 8
          max-idle: 8
          min-idle: 0
```

### 或者在本地启动 Redis（开发环境）
```bash
# 使用项目中提供的 docker-compose
docker-compose up -d
```

## <!--mdc_validator
MDC012
-->
## 📊 管理命令

```bash
# 查看 Redis 状态
sudo systemctl status redis-server  # Ubuntu
sudo systemctl status redis         # CentOS

# 启动/停止/重启
sudo systemctl start redis-server
sudo systemctl stop redis-server
sudo systemctl restart redis-server

# 查看 Redis 日志
sudo tail -f /var/log/redis/redis-server.log  # Ubuntu
sudo journalctl -u redis -f                    # CentOS

# 进入 Redis CLI
redis-cli

# Redis CLI 常用命令
KEYS *              # 查看所有 key
INFO                # 查看 Redis 信息
FLUSHALL            # 清空所有数据（谨慎使用）
CONFIG GET *        # 查看所有配置
```

## ❓ 常见问题

### 1. 无法远程连接
- 检查防火墙是否开放 6379 端口
- 确认 bind 配置为 0.0.0.0
- 确认 protected-mode 设置为 no

### 2. 连接被拒绝
- 检查 Redis 服务是否运行：`sudo systemctl status redis-server`
- 检查 Redis 监听的端口：`netstat -tlnp | grep 6379`

### 3. 忘记密码
```bash
# 注释掉密码配置
sudo nano /etc/redis/redis.conf
# 在 requirepass 前加 # 注释
sudo systemctl restart redis-server
```

## 🎯 下一步

1. Redis 安装成功后，启动 Spring Boot 项目
2. 测试访问：http://localhost:8080/api/counter/test
3. 查看线程池监控：http://localhost:8080/api/monitor/thread-pool
4. 开始练习题目（参考 EXERCISES.md）
