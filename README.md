# FAQ-Bot-QQ
  基于Miari框架和MariaDB开发的群内问答机器人
   
  支持艾特消息以及图片消息的存储
## 现有功能：
  1.群内问答
  
  2.迎新

## 使用方法：
  1. 首先创建名为faq的数据库（其实叫啥都行啦，最后config文件中的DBUrl正确即可），将项目中SQL文件夹中的question.sql和welcome.sql导入数据库中  
2. 创建config.yml文件，填入下面的配置：
      ```yaml
        dbUrl: "" # 你的数据库端口链接，示例：“jdbc:mysql://localhost:3306/faq?serverTimezone=UTC&characterEncoding=UTF-8
        dbUser: "" # 数据库用户名
        dbPwd: "" # 数据库密码
        botQQ: "" # 机器人的QQ
        botPwd: "" # 机器人的密码
        superUser: "" # 超级用户，也就是开发者自己
        predict: "py/predict.py" # 不需要管，这是ps学姐识别服务
      ``` 
  3. 打开数据库，编译Main.Kt开始使用
 
 
如果不想这么麻烦，**可以直接去下载release，下载.zip文件，解压后配置config.yml和数据库，然后直接运行./bin/QABotRefect 二进制文件即可**
  
## 指令菜单
**注意：以下功能部分需要使用.command 指令开启**
1. 添加问题 问题名
2. 删除问题 问题名
3. 修改问题 问题名
4. \# 问题id （快速索引功能）
5. 列表（群内所有问题列表）
6. 同步问答 目标同步群号 （同步不同群的问答）

**(详细可见BotGroupCommandListener.Kt文件**

新特性：支持添加自定义Service

食用方法：

指令：
```shell
.command addService 匹配指令 服务url 请求方法
```
实例：（以最简单的一言API为例）
```shell
.command addService 一言 https://v1.jinrishici.com/rensheng.txt get
```
以上指令中，“一言”为触发指令，之后是所需要请求的url地址，最后是请求方式：暂时仅支持GET和POST两种
如果使用POST，则bot会尝试获取触发指令后的请求参数存入post-form中，以空格作为分隔符

需要注意的是该特性暂时不支持解析json响应体（后续会更新），所以暂时请你尽量将返回设置为纯文本


**或向bot发送帮助和'.command help'查看功能列表和指令列表)**

## 使用场景：

  各大迎新群或工作室群提供快捷的提问回答服务

## 相关项目链接：
  感谢[Mirai](https://github.com/mamoe/mirai)的开发者们提供的bot框架
  
  感谢[HelloWorld](https://github.com/mzdluo123)给予的相关技术指导
  
## 注
  ~~使用过程中您遇到的bug都是feature~~
  
  遇到bug请提交Issue
  
  此项目仍然在更新中~
  (学业繁重，本项目作为一个大型shi山，更新维护频率较慢)
