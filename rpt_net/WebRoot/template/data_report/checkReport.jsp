<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:html locale="true">
<Iframe id="frame1"   name="frame1" src="<%=request.getAttribute("HTML") %>" width="100%" height="100%" scrolling="yes" frameborder="0">
</iframe>
</html:html>