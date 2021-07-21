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
    public String loliconKey;

    private AppConfig() throws FileNotFoundException {
        Yaml yml = new Yaml();
        Map<String, Object> data = yml.load(new FileReader(new File("config.yml")));
        dbUrl = (String) data.get("dbUrl");
        if (dbUrl == null) {
            throw new NullPointerException("Config Error!");
        }

        dbUser = (String) data.get("dbUser");
        dbPwd = (String) data.get("dbPwd");
        botQQ = Long.valueOf((String) data.get("botQQ"));
        botPwd = (String) data.get("botPwd");
        try {
            superUser = Long.valueOf((String) data.get("superUser"));
            loliconKey = (String) data.get("lolicon");
            gameDBUrl = (String) data.get("gameDBUrl");
            gameAPI = (String) data.get("GameAPI");
        } catch (Exception ignored) {
        }

        logger().info("Loading Success!" + dbUrl);
    }

    public static AppConfig getInstance() throws FileNotFoundException {
        if (INSTANCE == null) {
            INSTANCE = new AppConfig();
        }
        return INSTANCE;
    }
}
