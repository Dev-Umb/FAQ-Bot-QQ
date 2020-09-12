# FAQ-Bot-QQ
基于[Graia Framework](https://github.com/GraiaProject/Application)框架开发的Mirai机器人插件,目前集成了迎新/问答/百度/萌娘/说骚话功能

## 使用方法：

  1.下载[MiraiOK](https://github.com/LXY1226/MiraiOK)并参阅[文档](https://graiaproject.github.io/Application/)进行配置

  2.创建config.py文件，分别填入以下配置：
   ```python
   BOTQQ=123456
   
   #Bot的QQ，一定要写，否则会报找不到无头客户端的error
   
   API_ROOT='http://localhost:80'

  #本地地址
   
   AuthKey="graia-mirai-api-http-authkey"
  #你的软件key值
   ```
  config中的端口配置和key配置要与MiraiOK/plugin/MiraiAPIHTTP下的setting.yml中的端口配置相同

  3.启动MiraiOK并登录一个账号

  4.启动bot.py文件
  
## 指令菜单
```
1./start 开启百度和骚话功能
2./shutdown 关闭百度和骚话功能
3. 添加问题 问题名
4. 修改问题 问题名
5. 删除问题 问题名
6. 百度 词条
7. 萌娘 词条
8. .来点好听的
```

## 使用场景：

  各大迎新群或工作室群

## 相关项目链接：
  [Mirai](https://github.com/mamoe/mirai)

  [MiraiOK](https://github.com/LXY1226/MiraiOK)

  [Mirai-api-http](https://github.com/project-mirai/mirai-api-http)
