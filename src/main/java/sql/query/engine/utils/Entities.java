package sql.query.engine.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Entities {
	private static final String ENTITIES_PROPERTIES_FILE = "entities.properties";
	private static final Properties ENTITIES = new Properties();
	
	public static String getEntity(String key) {
		if (ENTITIES.isEmpty()) {
			ClassLoader classLoader = Entities.class.getClassLoader();
		    InputStream inputStream = classLoader.getResourceAsStream(ENTITIES_PROPERTIES_FILE);
		    try {
				ENTITIES.load(inputStream);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ENTITIES.getProperty(key);
	}
}
