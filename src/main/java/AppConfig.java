import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.List;

public class AppConfig {
    private static AppConfig INSTANCE = null;
    public String dbUrl;
    public String dbUser;
    public String dbPwd;
    public List<Long> admin;
    public String help;

    private static File configFile;

    private AppConfig() {
    }

    public static AppConfig getInstance() {

        return INSTANCE;
    }

    public boolean isAdmin(Long user) {
        if (admin == null) {
            return false;
        }
        return admin.contains(user);
    }

    public static void loadConfig(File file) throws IOException {
        if (INSTANCE != null) {
            return;
        }
        configFile = file;
        FileInputStream fileInputStream = new FileInputStream(file);
        fileInputStream.close();
    }

    public static void reload() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(configFile);
        fileInputStream.close();
    }
    public static void loadHelp() throws IOException {
        File helpFile = new File("help.txt");
        StringBuilder sb = new StringBuilder();
        FileReader fileReader = new FileReader(helpFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        while ((line = bufferedReader.readLine()) != null ){
            sb.append(line);
            sb.append("\n");
        }
        INSTANCE.help = sb.toString();
        bufferedReader.close();
        fileReader.close();
    }

}

