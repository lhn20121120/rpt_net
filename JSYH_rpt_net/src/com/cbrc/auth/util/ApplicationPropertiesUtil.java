package com.cbrc.auth.util;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

public class ApplicationPropertiesUtil {
	private static Properties config = null;

	static {

		config = new Properties();
		try {
			InputStream in = ApplicationPropertiesUtil.class.getResourceAsStream("/application.properties");
			config.load(in);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 根据key读取value
	public static String getValue(String key) {
		// Properties props = new Properties();
		try {
			String value = config.getProperty(key);
			//System.out.println(key + value);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
	
			return null;
		}
	}

	// 读取properties的全部信�?	
	public static void readAllProperties() {
		try {

			Enumeration<?> en = config.propertyNames();
			while (en.hasMoreElements()) {
				String key = (String) en.nextElement();
				String property = config.getProperty(key);
				//System.out.println(key + property);
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}
	
	

	public static Properties getConfig() {
		return config;
	}


	public static void main(String args[]) {
		System.out.println(ApplicationPropertiesUtil.getValue("new_portal_url"));
	}
}