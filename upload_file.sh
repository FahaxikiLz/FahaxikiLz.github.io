#!/bin/bash

# 服务器信息
USER="root"
HOST="117.72.199.196"
KEY_PATH="C:/Users/L Z/Desktop/MyBlog/MyBlog.pem"  # 使用正斜杠
REMOTE_PATH="/home/nginx/html"
LOCAL_PATH="C:/Users/L Z/Desktop/MyBlog/public"

# 删除远程目录中的文件
echo "正在删除文件夹..."
ssh -i "$KEY_PATH" "$USER@$HOST" "rm -rf $REMOTE_PATH/*"

# 重启 Nginx 的 Docker 容器
echo "正在重启nginx..."
ssh -i "$KEY_PATH" "$USER@$HOST" "docker restart nginx"

# 上传本地文件到远程目录
echo "正在上传文件..."
scp -i "$KEY_PATH" -r "$LOCAL_PATH"/* "$USER@$HOST:$REMOTE_PATH"

echo "文件上传完毕！"