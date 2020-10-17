package io.farewell12345.github.faqbot;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

import static io.farewell12345.github.faqbot.curd.DBUntilKt.logger;

public class AppConfig {
    private static AppConfig INSTANCE = null;
    public String dbUrl;
    public String dbUser;
    public String dbPwd;
    public Long BotQQ;
    public String BotPwd;
    public Long SuperUser;
    private AppConfig() throws FileNotFoundException {
        Yaml yml = new Yaml();
        Map<String,String> data = yml.load(new FileReader( new File("config.yml")));
        dbUrl=data.get("dbUrl");
        if (dbUrl==null){
            dbUrl = "jdbc:mysql://localhost:3306/fqa?serverTimezone=UTC&characterEncoding=UTF-8";
        }
        dbUser=data.get("dbUser");
        dbPwd=data.get("dbPwd");
        SuperUser = Long.valueOf(data.get("superUser"));
        BotQQ= Long.valueOf(data.get("botQQ"));
        BotPwd=data.get("botPwd");
        logger().info("配置加载完成！"+dbUrl);
    }

    public static AppConfig getInstance() throws FileNotFoundException {
        if (INSTANCE == null){
            INSTANCE=new AppConfig();
        }
        return INSTANCE;
    }
}

