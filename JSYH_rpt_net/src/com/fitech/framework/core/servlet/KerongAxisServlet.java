package com.fitech.framework.core.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.axis2.Constants;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * 
 *   chengang@fitech.com.cn
 *
 */
public class KerongAxisServlet extends org.apache.axis2.transport.http.AxisServlet{
	
	private static final Log log = LogFactory.getLog(KerongAxisServlet.class);

	  /**
     * Initialize the Axis configuration context
     *
     * @param config Servlet configuration
     * @return ConfigurationContext
     * @throws ServletException
     */
    protected ConfigurationContext initConfigContext(ServletConfig config) throws ServletException {
    	//此处将原有的WarBasedAxisConfigurator换成科融公司自己实现的配置类，因为要实现WEBLOGIC兼容
    	//在WEBLOGIC下面，如果不改成如此，则必需要在web.xml里的AxisServlet加上axis2.repository.path参数
        try {
            ConfigurationContext configContext =
                    ConfigurationContextFactory
                            .createConfigurationContext(new KerongWarBasedAxisConfigurator(config));
            configContext.setProperty(Constants.CONTAINER_MANAGED, Constants.VALUE_TRUE);
            return configContext;
        } catch (Exception e) {
            log.info(e);
            throw new ServletException(e);
        }
    }
}
