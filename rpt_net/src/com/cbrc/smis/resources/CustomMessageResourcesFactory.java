
package com.cbrc.smis.resources;

import java.util.Arrays;

import org.apache.struts.util.MessageResources;
import org.apache.struts.util.MessageResourcesFactory;

/**
 * @author rds
 *
 * struts��Դ��������
 */
public class CustomMessageResourcesFactory extends MessageResourcesFactory{

    public MessageResources createResources(String config) {
        
        return new CustomMessageResources(Arrays.asList(config.split(",")));
    }

}