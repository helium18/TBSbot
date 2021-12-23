import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class utils {

    public Properties getConfig() {
        File configFile = new File("config.properties");

        try {
            FileReader reader = new FileReader(configFile);
            Properties properties = new Properties();

            properties.load(reader);

            return properties;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void setConfig(Properties properties) {
        File configFile = new File("config.properties");

        FileWriter writer = null;
        try {
            writer = new FileWriter(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            properties.store(writer, "Configuration");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
