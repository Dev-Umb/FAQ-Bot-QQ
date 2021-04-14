# FAQ-Bot-QQ
  基于Miari框架和MariaDB开发的群内问答机器人
   
  支持艾特消息以及图片消息的存储
## 现有功能：
  1.群内问答
  
  2.反群内复读
  
  3.每日游戏推荐（数据来源：小黑盒,接口：https://api.xiaoheihe.cn/game/web/all_recommend/?os_type=web&version=999.0.0&hkey=da69a1104606268883cb2301c3f02770&_time=）
  
  4.群组内抽签创建
  
  5.消息多群转发
  
  6.图片边缘提取（图片转线稿）
  
  7.涩图
  
  8.群内活动群组创建
  
  9.求签运势,测试今日某一件事的运气

## 使用方法：
  1. 首先创建名为faq的数据库（其实叫啥都行啦，最后config文件中的DBUrl正确即可），将项目中SQL文件夹中的question.sql和welcome.sql导入数据库中
  2. 创建nuc_game数据库（叫啥都行gameDBUrl正确即可），将SQL文件夹下的game_sql导入创建好的游戏数据库
3. 创建config.yml文件，填入下面的配置：
      ```yaml
        dbUrl: "" # 你的数据库端口链接，示例：“jdbc:mysql://localhost:3306/faq?serverTimezone=UTC&characterEncoding=UTF-8
        dbUser: "" # 数据库用户名
        dbPwd: "" # 数据库密码
        botQQ: "" # 机器人的QQ
        botPwd: "" # 机器人的密码
        gameDBUrl: "" # 群内游戏群组数据库端口链接，示例： “jdbc:mysql://localhost:3306/nuc_game?serverTimezone=UTC&characterEncoding=UTF-8”
        GameAPI: "" # 小黑盒的游戏数据接口
        lolicon: "lolicon api key" # lolicon的apikey，用于请求涩图
      ``` 
  4. 打开数据库，编译Main.Kt开始使用
  
## 指令菜单
**注意：以下功能部分需要使用.command 指令开启**
1. 添加问题 问题名
2. 删除问题 问题名
3. 修改问题 问题名
4. \# 问题id （快速索引功能）
5. 列表（群内所有问题列表）
6. 同步问答 目标同步群号 （同步不同群的问答）
7. 游戏推荐（需要开启指令）
8. 创建抽签（创建一个抽签活动，格式：创建抽签 数字1,数字2）
9. 通知转发（需加bot为好友，然后群内成员向bot发送私聊消息即可自动转发到群中，可用于相同消息多群转发或通知发布）


**(详细可见BotGroupCommandListener.Kt文件**

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
