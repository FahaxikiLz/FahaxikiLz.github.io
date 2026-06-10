git checkout files
git add .
git commit -m deploy_files
git push -u origin files
echo "已提交到GitHub!"

sleep 2

npx hexo g --watch
echo "静态网站生成完毕！"

sleep 2

cd public

sleep 2

git checkout master
git add .
git commit -m deploy_master
git push -u origin master
echo "已提交到GitHub!"

echo 执行完成，按任意键结束
read -n 1