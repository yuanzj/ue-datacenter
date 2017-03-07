package net.rokyinfo.basecommon.config;

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

	private Config() {

		parameters = new HashMap<String, String>();
		try {
			String filePath = System.getProperty("user.dir") + "/conf/config.properties";
			InputStream in = new BufferedInputStream(new FileInputStream(filePath));
			Properties properties = new Properties();

			properties.load(in);

			Enumeration<Object> enu = properties.keys();
			while (enu.hasMoreElements()) {

				String key = (String) enu.nextElement();
				parameters.put(key, properties.getProperty(key));
			}

		} catch (IOException e) {

			System.out.println("No properties defined error");
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
}
