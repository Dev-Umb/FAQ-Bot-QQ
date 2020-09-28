package io.farewell.github.qabot;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.Map;

public class AppConfig {
    private static AppConfig INSTANCE = null;
    public String dbUrl;
    public String dbUser;
    public String dbPwd;
    public Long BotQQ;
    public String BotPwd;
    private AppConfig() throws FileNotFoundException {
        Yaml yml = new Yaml();
        Map<String,String> data = yml.load(new FileReader( new File("config.yml")));
        dbUrl=data.get("dbUrl");
        dbUser=data.get("dbUser");
        dbPwd=data.get("dbPwd");
        BotQQ= Long.valueOf(data.get("botQQ"));
        BotPwd=data.get("botPwd");
    }

    public static AppConfig getInstance() throws FileNotFoundException {
        if (INSTANCE == null){
            INSTANCE=new AppConfig();
        }
        return INSTANCE;
    }
}

