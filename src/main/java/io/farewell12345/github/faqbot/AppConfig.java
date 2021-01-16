package io.farewell12345.github.faqbot;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Map;

import static io.farewell12345.github.faqbot.DTO.model.DBUntilKt.logger;

public class AppConfig {
    private static AppConfig INSTANCE = null;
    public String dbUrl;
    public String dbUser;
    public String dbPwd;
    public Long BotQQ;
    public String BotPwd;
    public Long SuperUser;
    public String gameAPI;
    public String gameDBUrl;
    public ArrayList<String> times;
    public String[] DisRepetitionScence = new String[]{"复 读 禁 止", "禁 止 复 读"};
    private AppConfig() throws FileNotFoundException {
        Yaml yml = new Yaml();
        Map<String, Object> data = yml.load(new FileReader( new File("config.yml")));
        dbUrl= (String) data.get("dbUrl");
        if (dbUrl==null){
            dbUrl = "jdbc:mysql://localhost:3306/fqa?serverTimezone=UTC&characterEncoding=UTF-8";
        }
        gameDBUrl =(String) data.get("gameDBUrl");
        gameAPI = (String) data.get("GameAPI");
        dbUser= (String) data.get("dbUser");
        dbPwd= (String) data.get("dbPwd");
        SuperUser = Long.valueOf((String) data.get("superUser"));
        BotQQ= Long.valueOf((String) data.get("botQQ"));
        BotPwd= (String) data.get("botPwd");
        times = (ArrayList<String>) data.get("time");
        logger().info("配置加载完成！"+dbUrl);
    }

    public static AppConfig getInstance() throws FileNotFoundException {
        if (INSTANCE == null){
            INSTANCE=new AppConfig();
        }
        return INSTANCE;
    }
}
//class main {
//    public static void main(String[] args) {
//        try {
//            AppConfig.getInstance();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//}
