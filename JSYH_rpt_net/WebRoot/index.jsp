<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd"> 
<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%     
  response.setHeader("Pragma","No-cache");     
  response.setHeader("Cache-Control","no-cache");     
  response.setHeader("Expires","0");     
  %> 
<html:html locale="true">
	<head>
		<html:base/>
		<title>��������-ͳ�Ʊ���ϵͳ</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="style/globalStyle.css" rel="stylesheet" type="text/css">
	</head>

    <c:if test="${!applicationScope.IS_INTEGRATE_PORTAL}" >
       <frameset id="superFrame" name="superFrame" rows="74,*" cols="*" border="0" frameSpacing="0" frameBorder="0">
		<frame name="topFrame" scrolling="no" noresize target="contents" src="top.jsp">
		<frameset name="firstFrame" rows="*" cols="170,*" framespacing="0" frameborder="no" border="0">
			<frame name="naviFrame" target="mainFrame" src="navimenu.jsp" scrolling="no" noresize>
			<frameset rows="1,*" cols="*" framespacing="0" frameborder="no" border="0">
				<frame name="middleFrame" target="mainFrame" src="middle.htm" noresize scrolling="no">
				<frameset rows="*" cols="1,*,1" framespacing="0" frameborder="no" border="0">
					<frame name="plugFrame" target="mainFrame" src="plug.htm" scrolling="no" noresize>
					<frame name="mainFrame" target="mainFrame" src="mainFrame.jsp">
					<frame name="rightFrame" target="mainFrame" src="right.htm" scrolling="no" noresize>
				</frameset>
			</frameset>
		</frameset>
		<noframes>
			<body>
				<p>����ҳʹ���˿�ܣ��������������֧�ֿ�ܡ�</p>
			</body>
		</noframes>
	</frameset>
    </c:if>
    <%--��portal���Ϻ���ʾportal��ͷ--%>
    <c:if test="${applicationScope.IS_INTEGRATE_PORTAL}" >
        <frameset id="superFrame" name="superFrame" rows="0,26,*,20" cols="*" border="0" frameSpacing="0" frameBorder="0">
            <frame scrolling="no" noresize src="">
            <frame name="topnavi" scrolling="no" noresize target="contents" src="main_navi.jsp">
           <frameset name="firstFrame" rows="*" cols="170,*" framespacing="0" frameborder="no" border="0">
				<frame name="naviFrame" target="mainFrame" src="navimenu.jsp" scrolling="auto" noresize>
				<frame name="mainFrame" target="mainFrame" src="main.jsp">
			</frameset>
            <frame src="copyright.jsp" name="copyright" scrolling="no" noresize>

            <noframes>
                <body>
                <p>����ҳʹ���˿�ܣ��������������֧�ֿ�ܡ�</p>
                </body>
            </noframes>
        </frameset>
    </c:if>
</html:html>
