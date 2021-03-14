package io.farewell12345.github.faqbot;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

import static io.farewell12345.github.faqbot.DTO.model.DBUntilKt.logger;

public class AppConfig {
    private static AppConfig INSTANCE = null;
    public String dbUrl;
    public String dbUser;
    public String dbPwd;
    public Long botQQ;
    public String botPwd;
    public Long superUser;
    public String gameAPI;
    public String gameDBUrl;
    public String[] disRepetitionScence = new String[]{"复 读 禁 止", "禁 止 复 读"};
    public String[] draws = {"大吉","大凶","小吉","吉大于凶","凶大于吉","小凶","中凶","中吉"};
    private AppConfig() throws FileNotFoundException {
        Yaml yml = new Yaml();
        Map<String, Object> data = yml.load(new FileReader( new File("config.yml")));
        dbUrl= (String) data.get("dbUrl");
        if (dbUrl==null){
            throw new NullPointerException("请在config.yml文件中正确配置jdbc数据库url");
        }
        gameDBUrl =(String) data.get("gameDBUrl");
        gameAPI = (String) data.get("GameAPI");
        dbUser= (String) data.get("dbUser");
        dbPwd= (String) data.get("dbPwd");
        superUser = Long.valueOf((String) data.get("superUser"));
        botQQ = Long.valueOf((String) data.get("botQQ"));
        botPwd = (String) data.get("botPwd");
        logger().info("配置加载完成！"+dbUrl);
    }

    public static AppConfig getInstance() throws FileNotFoundException {
        if (INSTANCE == null){
            INSTANCE=new AppConfig();
        }
        return INSTANCE;
    }
}
