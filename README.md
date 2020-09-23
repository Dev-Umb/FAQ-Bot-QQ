# FAQ-Bot-QQ
  基于Miari框架和MariaDB开发的群内问答机器人
   
  支持艾特消息以及图片消息的存储
## 使用方法：
  1. 首先创建名为fqa的数据库，将项目中的fqa.sql文件导入
  2. 创建config.yml文件，填入下面的配置：
      ```yaml
        dbUrl: "" # 你的数据库端口链接
        dbUser: "" # 数据库用户名
        dbPwd: "" # 数据库密码
        botQQ: "" # 机器人的QQ
        botPwd: "" # 机器人的密码
      ``` 
  3. 打开数据库，编译Main.Kt开始使用
  
## 指令菜单
1. 添加问题 问题名
2. 删除问题 问题名
3. 修改问题 问题名
4. \# 问题id （快速索引功能）
5. 列表（群内所有问题列表）

详细可见BotMsgListener.Kt文件

## 使用场景：

  各大迎新群或工作室群提供快捷的提问回答服务

## 相关项目链接：
  感谢[Mirai](https://github.com/mamoe/mirai)的开发者们提供的bot框架
  
  感谢[HelloWorld](https://github.com/mzdluo123)给予的相关技术指导
