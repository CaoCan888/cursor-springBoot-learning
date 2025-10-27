# 🚀 Redis 快速安装指南

## 方式一：您自己连接服务器执行（最简单）

### 1. 连接服务器
```bash
ssh root@47.116.3.95
```

### 2. 执行安装命令（Ubuntu/Debian）
```bash
sudo apt-get update && sudo apt-get install -y redis-server && \
sudo sed -i 's/bind 127.0.0.1 ::1/bind 0.0.0.0/g' /etc/redis/redis.conf && \
sudo sed -i 's/protected-mode yes/protected-mode no/g' /etc/redis/redis.conf && \
sudo systemctl start redis-server && \
sudo systemctl enable redis-server && \
echo "Redis 安装完成！" && redis-cli ping
```

### 3. 执行安装命令（CentOS）
```bash
sudo yum install -y epel-release redis && \
sudo sed -i 's/bind 127.0.0.1/bind 0.0.0.0/g' /etc/redis.conf && \
sudo sed -i 's/protected-mode yes/protected-mode no/g' /etc/redis.conf && \
sudo systemctl start redis && \
sudo systemctl enable redis && \
echo "Redis 安装完成！" && redis-cli ping
```

## 方式二：使用 PowerShell 脚本自动安装

### 在 Windows 上执行
```powershell
# 1. 进入项目目录
cd d:\workspace\cursor-springBoot-learning

# 2. 执行安装脚本（需要先配置 SSH 密钥，或手动输入密码）
.\install-redis-remote.ps1

# 如果使用其他用户名或IP
.\install-redis-remote.ps1 -Username admin -HostIP 47.116.3.95
```

**注意**：此脚本需要您已经配置好 SSH 密钥认证，或者需要手动输入密码。

## 方式三：Docker 安装（推荐，最简单）

### 1. 连接服务器
```bash
ssh root@47.116.3.95
```

### 2. 安装 Docker（如果还没有）
```bash
curl -fsSL https://get.docker.com -o get-docker.sh
sh get-docker.sh
```

### 3. 启动 Redis
```bash
docker run -d \
  --name redis-learning \
  --restart=always \
  -p 6379:6379 \
  -v redis-data:/data \
  redis:7-alpine redis-server --appendonly yes
```

### 4. 测试连接
```bash
docker exec -it redis-learning redis-cli ping
```

## 安装完成后配置项目

### 修改 application.yml
```yaml
spring:
  data:
    redis:
      host: 47.116.3.95  # 改为你的服务器地址
      port: 6379
      password:  # 如果设置了密码，在这里填写
```

## 验证安装

### 从本地机器测试
```powershell
# 使用 Docker 测试连接
docker run -it --rm redis redis-cli -h 47.所有.3.95 -p 6379 ping
```

应该返回：`PONG`

## 常见问题

1. **无法连接**：检查服务器防火墙是否开放 6379 端口
2. **权限不足**：使用 `sudo` 执行命令
3. **找不到命令**：确认系统包已更新

## 推荐方案

**最简单**：直接使用 Docker 方式，一条命令即可完成。

**最稳定**：使用系统包管理工具安装。
