package ink.umb.faqbot;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

public class AppConfig {
    private static AppConfig INSTANCE = null;
    public Long botQQ;
    public String botPwd;
    public String device;
    public Long superUser;
    public String fakeInfoUrl;
    public String fakeInfoKey;
    public String predictPyUri;
    private AppConfig() throws FileNotFoundException {
        Yaml yml = new Yaml();
        Map<String, Object> data = yml.load(new FileReader(new File("config.yml")));
        predictPyUri = "py/predict.py";
        botQQ = Long.valueOf((String) data.get("botQQ"));
        botPwd = (String) data.get("botPwd");
        try {
            device = (String) data.get("device");
            superUser = Long.valueOf((String) data.get("superUser"));
        } catch (Exception ignored) {
            device = "PHONE";
        }
    }

    public static AppConfig getInstance() throws FileNotFoundException {
        if (INSTANCE == null) {
            INSTANCE = new AppConfig();
        }
        return INSTANCE;
    }
}
