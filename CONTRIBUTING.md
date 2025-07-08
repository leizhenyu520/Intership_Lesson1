# 贡献指南

## 分支管理策略

### 主要分支
- **main**: 存放系统的稳定版本代码，只有经过测试和审核的代码才能合并到此分支。
- **develop**: 开发主分支，用于集成各个功能模块的代码。所有功能开发完成后合并到此分支。

### 辅助分支
- **feature/功能名称**: 用于开发新功能，从develop分支创建，完成后合并回develop分支。
- **bugfix/bug编号**: 用于修复系统bug，从develop分支创建，修复后合并回develop分支；重要bug修复需同时合并到main分支。
- **release/版本号**: 准备发布新版本时从develop分支创建，测试和修复完成后合并到main和develop分支。

## 开发流程

### 功能开发
1. 从develop分支创建feature分支: `git checkout -b feature/your-feature-name develop`
2. 开发完成后提交变更并推送到远程仓库
3. 创建Pull Request到develop分支，等待代码审查
4. 审查通过后合并，删除feature分支

### Bug修复
1. 从develop分支创建bugfix分支: `git checkout -b bugfix/bug-id develop`
2. 修复完成后提交变更并推送到远程仓库
3. 创建Pull Request到develop分支，重要bug需同时创建PR到main分支
4. 审查通过后合并，删除bugfix分支

### 版本发布
1. 从develop分支创建release分支: `git checkout -b release/v1.0.0 develop`
2. 进行最终测试和bug修复
3. 完成后合并到main分支并打标签: `git tag -a v1.0.0 -m "Version 1.0.0"`
4. 同时合并回develop分支
5. 删除release分支

## 提交规范
- 提交信息应简洁明了，使用英文或中文
- 格式: `[类型]: 描述`，例如: `[Feature]: 添加用户登录功能`
- 类型包括: Feature(新功能)、Fix(bug修复)、Docs(文档)、Style(格式)、Refactor(重构)、Test(测试)、Chore(构建/依赖)

## 代码审查要求
- 至少一名团队成员审查通过
- 所有测试用例通过
- 代码符合项目编码规范
- 无安全漏洞