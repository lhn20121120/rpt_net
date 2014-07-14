package com.gather.resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.apache.struts.util.MessageResources;


/** 
 * @author rds
 * 
 * This class is a Custom Message Resource implementation for Struts. This class searches through
 * a series of resource files passed to it via the constructor to obtain the localised value for a
 * key passed. If the key cannot be resolved, it pritns a warning message to the logger output.
 */
public class CustomMessageResources extends MessageResources
{

    //hashmap holding the resource bundle for which we have already loaded the values
    protected HashMap bundlesLoaded = new HashMap();

    //hashmap for holding ther resource bundle key and value
    protected HashMap resourceMap = new HashMap();

    //the array containing the names of the resource bundles to look for
    List bundles = new ArrayList();

    /**
     * Constructor accepting a list of resource bundle files.
     * @param configFiles A List of resouce bundle files.
     */
    public CustomMessageResources(List configFiles)
    {
        super(null, null);
        this.bundles = configFiles;
        // TODO Auto-generated constructor stub
    }

    /** (non-Javadoc)
     * @see org.apache.struts.util.MessageResources#getMessage(java.util.Locale, java.lang.String)
     */
    public String getMessage(Locale locale, String key)
    {

        final String methodSig = "getMessage";
        String message = null;
        //initialise the variables we need
        String localeKey = "zh_CN";
        if (!super.defaultLocale.equals(locale))
        {
            this.localeKey(locale);
        }

        //go through all the resource bundle files and load them
        for (int i = 0; i < bundles.size(); i++)
        {
            String config = (String) bundles.get(i);
            this.loadBundle(localeKey, config);
            //now look if the message can be found
            message = (String) resourceMap.get(key);
            //message found, so get out of the loop
            if (message != null)
            {
                break;
            }
        }
        if(message==null)
        {
            // System.out.println("COULD NOT FIND RESOURCE FILE VALUE FOR KEY: " + key);
        }
        //// System.out.println("message:" + message);
        return message;
    }

    /**
     * Loads the resource bundle based on the locale.
     * @param localeKey The locale
     * @param config The resource file.
     */
    protected void loadBundle(String localeKey, String config)
    {
        final String methodSig = "loadBundle";
        String fileName = null;
        InputStream inStream = null;
        Properties props = new Properties();
        //first check if this bundle is alreay loaded previously
        if (bundlesLoaded.get(config) != null)
        {
            return;
        }

        //otherwise mark this bundle being loaded
        this.bundlesLoaded.put(config, localeKey);

        // Set up to load the property resource for this locale key, if we can
        fileName = config.replace('.', '/');
        if (localeKey.length() > 0)
        {
            fileName += "_" + localeKey;
        }
        fileName += ".properties";
        
        //obtain a class loader
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        //if this classloader is null, use the class loader used to load this class
        if (classLoader == null)
        {
            classLoader = this.getClass().getClassLoader();
        }

        //load the file as Stream
        inStream = classLoader.getResourceAsStream(fileName);

        //check if the Stream in not null
        if (inStream != null)
        {
            //populate the properties from this input Stream
            try
            {
                props.load(inStream);
            } catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally
            {
                try
                {
                    inStream.close();
                } catch (IOException e1)
                {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }

            //now retrieve the key value pairs from the resource file and
            //put it in the map
            synchronized (this.resourceMap)
            {
                Iterator iterator = props.keySet().iterator();

                while (iterator.hasNext())
                {
                    String key = (String) iterator.next();
                    //String messageKey = this.messageKey(localeKey,key);
                    String value = props.getProperty(key);

                    this.resourceMap.put(key, value);
                }
            }

        } else
        {
            // System.out.println("COULD NOT LOAD RESOURCE FILE: " + fileName);
        }


    }

}
