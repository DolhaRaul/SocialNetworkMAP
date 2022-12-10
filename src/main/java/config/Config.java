package config;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Config {
    /**
     * clasa Config se va incarca cu datele in package ul "resources"(package ul unde ne aflam acum), cu datele din fisierul
     * "config.properties", deci path ul e "resources/config.properties"
     */
    public static final URL CONFIG_URL = Config.class.getClassLoader().
            getResource("config.properties");

    /**
     * Obtinem proprietatile generale
     * @return Proprietatile clasei Config
     */
    public static Properties getProperties() {
        Properties properties=new Properties();
        try {
            String CONFIG_LOCATION = CONFIG_URL.getFile();
            if(CONFIG_LOCATION.contains(".jar")){
                int index = CONFIG_LOCATION.indexOf(".jar")+4;
                String jarPath = CONFIG_LOCATION.substring(8, index);
                String entryPath = CONFIG_LOCATION.substring(index+2);
                try(JarFile jarFile = new JarFile(jarPath))
                {
                    JarEntry jarEntry = jarFile.getJarEntry(entryPath);
                    InputStream stream = jarFile.getInputStream(jarEntry);
                    properties.load(stream);
                }
            }
            else {
                properties.load(new FileReader(CONFIG_LOCATION));
            }
            Objects.requireNonNull(properties);
            return properties;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Cannot load config properties");
        }
    }
}
