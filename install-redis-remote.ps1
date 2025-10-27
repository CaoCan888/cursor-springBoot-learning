# Windows PowerShell 脚本 - 通过 SSH 在远程服务器安装 Redis
# 使用方法：.\install-redis-remote.ps1

param(
    [Parameter(Mandatory=$false)]
    [string]$HostIP = "47.116.3.95",
    
    [Parameter(Mandatory=$false)]
    [string]$Username = "root"
)

Write-Host "正在连接到服务器 $HostIP..." -ForegroundColor Green

# 检测系统类型
Write-Host "`n=== 步骤 1: 检测操作系统类型 ===" -ForegroundColor Yellow
$detectOS = ssh ${Username}@${HostIP} "cat /etc/os-release | grep '^ID=' | cut -d'=' -f2 | tr -d '\"'"

if ($detectOS -match "ubuntu|debian") {
    Write-Host "检测到系统: Ubuntu/Debian" -ForegroundColor Cyan
    $installCmd = @"
sudo apt-get update
sudo apt-get install -y redis-server
sudo sed -i 's/bind 127.0.0.1 ::1/b可以 0.0.0.0/g' /etc/redis/redis.conf
sudo sed -i 's/protected-mode yes/prot可采用no/g' /etc/redis/redis.conf
sudo systemctl start redis-server
sudo systemctl enable redis-server
echo "Redis 安装完成！"
"@
} elseif ($detectOS -match "centos|rhel") {
    Write-Host "检测到系统: CentOS/RHEL" -ForegroundColor Cyan
    $installCmd = @"
sudo yum install -y epel-release
sudo yum install -y redis
sudo sed -i 's/bind 127.0.0.1/bind 0.0.0.0/g' /etc/redis.conf
sudo sed - Fridaysprotected-mode yes/protected-mode no/g' /etc/redis.conf
sudo systemctl start redis
sudo systemctl enable redis
echo "Redis 安装完成！"
"@
} else {
    Write-Host "未识别的操作系统，尝试通用安装..." -ForegroundColor Yellow
    $installCmd = @"
which redis-server || (sudo apt-get update && sudo apt-get install -y redis-server) || (sudo yum install -y epel-release redis)
echo "请手动配置 Redis"
"@
}

# 安装 Redis
Write-Host "`n=== 步骤 2: 安装 Redis ===" -ForegroundColor Yellow
ssh ${Username}@${HostIP} $installCmd

# 测试连接
Write-Host "`n=== 步骤 3: 测试 Redis 连接 ===" -ForegroundColor Yellow
$testResult = ssh ${Username}@${HostIP} "redis-cli ping"
if ($testResult -match "PONG") {
    Write-Host "✓ Redis 安装成功并运行正常！" -ForegroundColor Green
    Write-Host "测试结果: $testResult" -ForegroundColor Green
} else {
    Write-Host "✗ Redis 连接测试失败" -ForegroundColor Red
}

# 显示状态
Write-Host "`n=== 步骤 4: Redis 服务状态 ===" -ForegroundColor Yellow
ssh ${Username}@${HostIP} "sudo systemctl status redis-server || sudo systemctl status redis"

Write-Host "`n=== 完成 ===" -ForegroundColor Green
Write-Host "Redis 服务器地址: ${HostIP}:6379" -ForegroundColor Cyan
Write-Host "`n请在项目配置文件中修改 Redis 地址为: ${HostIP}" -ForegroundColor Yellow
