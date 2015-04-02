package com.fitech.framework.comp.file.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FitechProperties {

    private static Properties config = null;

    static {
    	  InputStream in = FitechProperties.class.getClassLoader().getResourceAsStream(
    	    "application.properties");
    	  config = new Properties();
    	  try {
    	   config.load(in);
    	   in.close();
    	  } catch (IOException e) {
    	   System.out.println("No AreaPhone.properties defined error");
    	  }
    }


    public static String readValue(String key) {
     try {
      String value = config.getProperty(key);
      return value;
     } catch (Exception e) {
      e.printStackTrace();
      System.err.println("ConfigInfoError" + e.toString());
      return null;
     }
    }

}
