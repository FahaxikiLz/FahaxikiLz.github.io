git checkout master
git add .
git commit -m deploy_files
git push -u origin master
echo "已提交到GitHub!"

npx hexo g --watch
echo "静态网站生成完毕！"

bash "C:/Users/L Z/Desktop/MyBlog/upload_file.sh"

echo 执行完成，按任意键结束
read -n 1