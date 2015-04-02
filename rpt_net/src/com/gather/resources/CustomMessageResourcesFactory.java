/*
 * Created on 2005-5-25
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.gather.resources;

import java.util.Arrays;

import org.apache.struts.util.MessageResources;
import org.apache.struts.util.MessageResourcesFactory;

/**
 * @author rds
 *
 * struts资源处理工厂类
 */
public class CustomMessageResourcesFactory extends MessageResourcesFactory{

    public MessageResources createResources(String config) {
        
        return new CustomMessageResources(Arrays.asList(config.split(",")));
    }

}