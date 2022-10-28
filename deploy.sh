hexo clean
hexo g
hexo d

git checkout files
git add .
git commit -m deploy_files
git push -u origin files

read -n 1
echo 执行完成