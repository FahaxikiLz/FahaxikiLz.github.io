# 在主仓库操作，不要进入 public 子目录
git checkout files
git add .
git commit -m "deploy_files"
git push -u origin files
echo "已提交到GitHub!"

# 生成静态文件
npx hexo g
echo "静态网站生成完毕！"

# 直接部署到 GitHub Pages（使用 hexo-deployer-git）
npx hexo d
echo "已部署到GitHub Pages!"

echo "执行完成，按任意键结束"
read -n 1