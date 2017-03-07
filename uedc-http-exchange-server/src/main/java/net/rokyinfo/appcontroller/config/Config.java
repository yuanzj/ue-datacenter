package net.rokyinfo.appcontroller.config;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Config {

    private static Config config = null;

    private Map<String, String> parameters;

    private Map<String, String> noLoginUrls;

    private Config() {

        parameters = new HashMap<String, String>();
        noLoginUrls = new HashMap<String, String>();

        InputStream in = null;
        InputStream in2 = null;

        try {
            String filePath = System.getProperty("user.dir") + "/conf/config.properties";
            in = new BufferedInputStream(new FileInputStream(filePath));
            Properties properties = new Properties();

            properties.load(in);

            Enumeration<Object> enu = properties.keys();
            while (enu.hasMoreElements()) {

                String key = (String) enu.nextElement();
                parameters.put(key, properties.getProperty(key));
            }

            filePath = System.getProperty("user.dir") + "/conf/no_login.properties";
            in2 = new BufferedInputStream(new FileInputStream(filePath));
            properties.clear();

            properties.load(in2);

            enu = properties.keys();
            while (enu.hasMoreElements()) {

                String key = (String) enu.nextElement();
                noLoginUrls.put(key, properties.getProperty(key));
            }

        } catch (IOException e) {

            System.out.println("No properties defined error");
        } finally {

            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (in2 != null) {
                try {
                    in2.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Config getConfig() {

        if (config == null) {

            synchronized (Config.class) {

                if (config == null) {

                    config = new Config();
                }
            }
        }

        return config;
    }

    public String getParameter(String key) {

        return this.parameters.get(key);
    }

    public Map<String, String> getNoLoginUrls() {
        return noLoginUrls;
    }
}
