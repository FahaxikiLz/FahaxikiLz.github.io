git checkout files
git add .
git commit -m deploy_files
git push -u origin files
echo "已提交到GitHub!"

npx hexo g --watch
echo "静态网站生成完毕！"

cd public

git checkout master
git add .
git commit -m deploy_master
git push -u origin master
echo "已提交到GitHub!"

echo 执行完成，按任意键结束
read -n 1