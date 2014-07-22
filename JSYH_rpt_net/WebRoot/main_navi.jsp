<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.List,com.cbrc.smis.security.Operator" %>
<%@ page import="com.cbrc.smis.common.Config" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="fitechUtil" scope="page" class="com.cbrc.smis.util.FitechUtil"/>
<%
	String today=fitechUtil.getToday(fitechUtil.CHINESEDATE) + "&nbsp;" + fitechUtil.getDay();
	
	Operator operator = null;
		if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
			operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		else
			operator = new Operator();
			
					/** ±¨±íÑ¡ÖÐ±êÖ¾ **/
		String reportFlg = "0";
		
		if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
			reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
		}
		String reportname = "";
		if(reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)){
			reportname =com.fitech.gznx.common.Config.CBRC_REPORT_NAME;
		} else if(reportFlg.equals(com.fitech.gznx.common.Config.PBOC_REPORT)){
			reportname =com.fitech.gznx.common.Config.PBOC_REPORT_NAME;
		}  else if(reportFlg.equals(com.fitech.gznx.common.Config.OTHER_REPORT)){
			reportname =com.fitech.gznx.common.Config.OTHER_REPORT_NAME;
		} 
		
	
%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-cache">
    <meta http-equiv="Expires" content="0">
    <link href="style/globalStyle.css" rel="stylesheet" type="text/css">
    <script src="script/globalScript.js" type="text/javascript" language="javascript"></script>
    <script language="javascript">

        var strColumns_Current = "215,*";

        function movenext() {
            top.deeptree.movenext();
        }

        function moveprevious() {
            top.deeptree.moveprevious();
        }

        function showtop() {
            <c:if test="${applicationScope.IS_INTEGRATE_PORTAL}" >
            window.top.superFrame.rows = "90,*";
            </c:if>
            <c:if test="${!applicationScope.IS_INTEGRATE_PORTAL}" >
            window.top.superFrame.rows = "70,26,*,20";
            </c:if>
        }

        function hidetop() {
            <c:if test="${applicationScope.IS_INTEGRATE_PORTAL}" >
            window.top.superFrame.rows = "0,*";
            </c:if>
            <c:if test="${!applicationScope.IS_INTEGRATE_PORTAL}" >
            window.top.superFrame.rows = "0,26,*,20";
            </c:if>
        }

        function showall() {

            window.top.superFrame.rows = "74,*";
            window.top.firstFrame.cols = "170,*";
        }

        function hideall() {
            window.top.superFrame.rows = "0,*";
            window.top.firstFrame.cols = "0,*";
        }

        function showleft() {
            <c:if test="${applicationScope.IS_INTEGRATE_PORTAL}" >
            window.top.frames[1].firstFrame.cols = "170,*";
            </c:if>
            <c:if test="${!applicationScope.IS_INTEGRATE_PORTAL}" >
            window.top.firstFrame.cols = "170,*";
            </c:if>
        }

        function hideleft() {
            <c:if test="${applicationScope.IS_INTEGRATE_PORTAL}" >
            window.top.frames[1].firstFrame.cols = "0,*";
            </c:if>
            <c:if test="${!applicationScope.IS_INTEGRATE_PORTAL}" >
            window.top.firstFrame.cols = "0,*";
            </c:if>
        }

        function synctoc() {
            parent.parent.mainFrame.location.reload();
            parent.parent.mainFrame.contents.location.reload();
            parent.parent.naviFrame.location.reload();
        }

        function displaybutton() {
            document.all.showtoc.style.display = "block";
        }

        function mouseover(item) {
            switch (item) {
                case "moveall" :
                    window.status = "Òþ²Ø¿ò¼Ü";
                    document.all.imgMoveAll.src = "image/moveall2.gif";
                    break;

                case "moveself" :
                    window.status = "ÏÔÊ¾¿ò¼Ü";
                    document.all.imgMoveSelf.src = "image/moveself2.gif";
                    break;

                case "moveprevious" :
                    window.status = "Òþ²ØÉÏ¿ò¼Ü";
                    document.all.imgMovePrevious.src = "image/moveprevious2.gif";
                    break;

                case "movenext" :
                    window.status = "ÏÔÊ¾ÏÂ¿ò¼Ü";
                    document.all.imgMoveNext.src = "image/movenext2.gif";
                    break;

                case "moveleft" :
                    window.status = "Òþ²Ø×ó¿ò¼Ü";
                    document.all.imgMoveLeft.src = "image/moveleft2.gif";
                    break;

                case "moveright" :
                    window.status = "ÏÔÊ¾×ó¿ò¼Ü";
                    document.all.imgMoveRight.src = "image/moveright2.gif";
                    break;

                case "synctoc" :
                    window.status = "Í¬²½µ±Ç°Ò³";
                    document.all.imgSyncToc.src = "image/synctoc2.gif"
                    break;
            }

        }

        function mouseout(item) {
            switch (item) {
                case "moveall" :
                    window.status = "";
                    document.all.imgMoveAll.src = "image/moveall1.gif";
                    break;

                case "moveself" :
                    window.status = "";
                    document.all.imgMoveSelf.src = "image/moveself1.gif";
                    break;

                case "moveprevious" :
                    window.status = "";
                    document.all.imgMovePrevious.src = "image/moveprevious1.gif";
                    break;

                case "movenext" :
                    window.status = "";
                    document.all.imgMoveNext.src = "image/movenext1.gif";
                    break;

                case "moveleft" :
                    window.status = "";
                    document.all.imgMoveLeft.src = "image/moveleft1.gif";
                    break;

                case "moveright" :
                    window.status = "";
                    document.all.imgMoveRight.src = "image/moveright1.gif";
                    break;

                case "synctoc" :
                    window.status = "";
                    document.all.imgSyncToc.src = "image/synctoc1.gif"
                    break;
            }
        }

    </script>
</head>
<body>
<table cellspacing="0" cellpadding="0" background="${pageContext.request.contextPath}/image/main_top.jpg" width="100%">
    <tr>
        <%
            String orgName = operator.getOrgName();
            String deptName = operator.getDeptName();
            String operatorName = operator.getOperatorName();
            if (orgName == null)
                orgName = "";
            if (deptName == null)
                deptName = "";
            if (operatorName == null)
                operatorName = "";
        %>
        <td align="left" width="80%">
            <span style="margin-left: 170px;"></span>
            &nbsp;&nbsp;&nbsp;»¶Ó­Äú [ <%=orgName%> ]&nbsp;<%=operatorName%>&nbsp;|&nbsp;½ñÌìÊÇ<%=today%>&nbsp;&nbsp;&nbsp;&nbsp;

        </td>
        <td align="right" width="20%">
            <img id="imgMoveLeft" style="cursor:hand" onmouseover="mouseover('moveleft');"
                 onmouseout="mouseout('moveleft')" onclick="hideleft();" title="Òþ²Ø×ó¿ò¼Ü" src="image/moveleft1.gif"
                 border="0" alt="Òþ²Ø×ó¿ò¼Ü"/>
            <img id="imgMoveRight" style="cursor:hand" onmouseover="mouseover('moveright');"
                 onmouseout="mouseout('moveright')"
                 onclick="showleft();" title="ÏÔÊ¾×ó¿ò¼Ü" src="image/moveright1.gif" border="0" alt="ÏÔÊ¾×ó¿ò¼Ü"/>
            <img id="imgMovePrevious" style="cursor:hand" onmouseover="mouseover('moveprevious');"
                 onmouseout="mouseout('moveprevious')" onclick="hidetop();" title="Òþ²ØÉÏ¿ò¼Ü" src="image/moveprevious1.gif"
                 border="0" alt="Òþ²ØÉÏ¿ò¼Ü"/>
            <img id="imgMoveNext" style="cursor:hand" onmouseover="mouseover('movenext');"
                 onmouseout="mouseout('movenext')"
                 onclick="showtop();" title="ÏÔÊ¾ÉÏ¿ò¼Ü" src="image/movenext1.gif" border="0" alt="ÏÔÊ¾ÉÏ¿ò¼Ü"/>

            <div style="width: 10px;">&nbsp;</div>
        </td>
    </tr>
</table>
</body>
</html>
