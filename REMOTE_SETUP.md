# 远程仓库设置指南

## 1. 创建GitHub远程仓库

1. 登录GitHub账号，点击右上角"+"图标，选择"New repository"
2. 填写仓库信息：
   - Repository name: `interproject-ai-chat`（建议使用此名称保持一致）
   - Description: `AI Chat System with backend (Spring Boot) and frontend (Vue.js)`
   - Visibility: 根据项目需求选择Public或Private
   - 勾选"Add a README file"（可选）
   - 选择.gitignore模板: `Java`
   - 点击"Create repository"

## 2. 连接本地仓库到远程仓库

在项目根目录执行以下命令：

```bash
# 添加远程仓库地址（请替换为你的GitHub仓库URL）
git remote add origin https://github.com/your-username/interproject-ai-chat.git

# 验证远程仓库配置
 git remote -v
# 预期输出：
# origin  https://github.com/your-username/interproject-ai-chat.git (fetch)
# origin  https://github.com/your-username/interproject-ai-chat.git (push)
```

## 3. 推送本地分支到远程仓库

```bash
# 推送main分支并设置上游跟踪
 git push -u origin main

# 推送develop分支并设置上游跟踪
 git push -u origin develop
```

## 4. 配置分支保护规则（GitHub网页端操作）

1. 进入GitHub仓库页面，点击"Settings" → "Branches"
2. 在"Branch protection rules"下点击"Add rule"
3. 为main分支添加保护：
   - Branch name pattern: `main`
   - 勾选"Require pull request reviews before merging"
   - 勾选"Require status checks to pass before merging"
   - 勾选"Require branches to be up to date before merging"
   - 添加必要的状态检查项（如CI构建、测试通过等）
   - 点击"Create"
4. 为develop分支添加类似保护规则（可适当放宽要求）

## 5. 团队协作工作流

### 开发者日常工作流程

```bash
# 确保本地develop分支最新
 git checkout develop
 git pull origin develop

# 创建功能分支
 git checkout -b feature/user-authentication develop

# 开发完成后提交并推送
 git add .
 git commit -m "[Feature] Implement user authentication"
 git push -u origin feature/user-authentication

# 在GitHub上创建Pull Request（develop ← feature/user-authentication）
# 经过代码审查后合并
```

### 版本发布流程

```bash
# 从develop创建release分支
 git checkout -b release/v1.0.0 develop

# 进行最终测试和修复
 git commit -m "[Fix] Resolve final release issues"
 git push -u origin release/v1.0.0

# 在GitHub上创建Pull Request（main ← release/v1.0.0）
# 合并后打标签
 git checkout main
 git pull origin main
 git tag -a v1.0.0 -m "Version 1.0.0"
 git push origin v1.0.0

# 将release分支合并回develop
 git checkout develop
 git merge --no-ff release/v1.0.0
 git push origin develop
```

## 6. 常见问题解决

### 推送失败（权限问题）
```bash
# 检查远程仓库URL是否正确
 git remote set-url origin https://github.com/your-username/interproject-ai-chat.git

# 或使用SSH协议（需配置SSH密钥）
 git remote set-url origin git@github.com:your-username/interproject-ai-chat.git
```

### 本地分支与远程不同步
```bash
 git fetch origin
 git checkout develop
 git merge origin/develop
```