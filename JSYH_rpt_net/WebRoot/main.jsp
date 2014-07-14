<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import="com.fitech.net.common.InfoUtil" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<jsp:directive.page import="java.sql.Connection"/>
<jsp:directive.page import="com.cbrc.smis.dao.DBConn"/>
<jsp:directive.page import="java.sql.ResultSet"/>
<html:html locale="true">
	<head>
	<html:base/>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="style/globalStyle.css" rel="stylesheet" type="text/css">
		<script src="script/globalScript.js" type="text/javascript" language="javascript"></script>
		<script language="javascript" src="js/menu_click.js"></script>
	</head>
	<%//公告
	DBConn dbconn=new DBConn();
	ResultSet rs=null;
	try{
	Connection conn=dbconn.openSession().connection();
	rs=conn.createStatement().executeQuery("select BULL_ID from BULLETIN where BULL_STATE=1");
	while(rs.next()){
		%>
		<script type="text/javascript">
		window.open('<%=request.getContextPath()%>/bulletin/BulletinView.jsp?bullId='+<%=rs.getInt(1)%>,'newwindow','height=420,width=510,top=70,left=42,toolbar=no,menubar=no,scrollbars=yes,resizable=no,location=no,status=no'); //弹出窗口
		</script>
		<%
	}
	}catch(Exception e){
		e.printStackTrace();
	}finally{
		if(rs!=null)rs.close();
		dbconn.closeSession();
	}
	
	
	%>
	
	
	
	
	
	
	<%
		com.cbrc.smis.security.Operator operator = null;
		if(session.getAttribute(com.cbrc.smis.common.Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
			operator = (com.cbrc.smis.security.Operator)session.getAttribute(com.cbrc.smis.common.Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		else
			operator = new com.cbrc.smis.security.Operator();
		
		boolean isLowerOrg = false;
		/** 所有最低一级机构 */ 
		//String mostLowerOrgIds = com.fitech.net.adapter.StrutsOrgNetDelegate.getMostLowerOrgIds();
		String mostLowerOrgIds = "";
		String[] orgIds = mostLowerOrgIds.split(",");
		if(operator != null && operator.getOrgId() != null){
			for(int i=0;i<orgIds.length;i++){
				if(orgIds[i].trim().equals(operator.getOrgId().trim())){
					isLowerOrg = true;
					break;
				}
			}
		}
		int tssl =0;  //提示数量
		int shwtg = 0;  //审核未通过数量
		String result = null;
		String resArr[] = null;
		if(isLowerOrg == true){
			result = InfoUtil.getWBSL(operator);
			if(result!=null)
			{
				resArr=result.split(",");
				tssl = Integer.parseInt(resArr[0]);
				shwtg =Integer.parseInt(resArr[1]);
			}
			
		}else{
			tssl = InfoUtil.getWSSL(operator);
		}
		String desc = "";
		String godesc = "";
		String url = "";
		String wtgStr ="";
		if(tssl != 0){
			if(isLowerOrg == true){
				desc = "张报表未报送";
				if(shwtg!=0){
					wtgStr = "张报表审核未通过";
				}
				godesc = "点击进入数据报送页面";
				url = "/viewDataReport.do";
			}else{
				desc = "张报表未审核";
				godesc = "点击进入报表审核页面";
				url = "/dateQuary/manualCheckRep.do";
			}
		}

	%>
	<script language="javascript">
		var num = "<%=tssl%>";
		
		num=0;
	</script><%
	%>
	<body scroll="no">
		<script language="JavaScript">
			if(num != 0){
				window.onload = getMsg;
			}
				
			window.onresize = resizeDiv;
			window.onerror = function(){}
			//短信提示使用(asilas添加)
			var divTop,divLeft,divWidth,divHeight,docHeight,docWidth,objTimer,i = 0;
			function getMsg(){
				try{
					divTop = parseInt(document.getElementById("eMeng").style.top,10)
					divLeft = parseInt(document.getElementById("eMeng").style.left,10)
					divHeight = parseInt(document.getElementById("eMeng").offsetHeight,10)
					divWidth = parseInt(document.getElementById("eMeng").offsetWidth,10)
					docWidth = document.body.clientWidth;
					docHeight = document.body.clientHeight;
					document.getElementById("eMeng").style.top = parseInt(document.body.scrollTop,10) + docHeight + 10;// divHeight
					document.getElementById("eMeng").style.left = parseInt(document.body.scrollLeft,10) + docWidth - divWidth
					document.getElementById("eMeng").style.visibility="visible"
					objTimer = window.setInterval("moveDiv()",10)
				}catch(e){}
			}
		
			function resizeDiv(){
				i+=1
				if(i>5000) closeDiv()
				try{
					divHeight = parseInt(document.getElementById("eMeng").offsetHeight,10)
					divWidth = parseInt(document.getElementById("eMeng").offsetWidth,10)
					docWidth = document.body.clientWidth;
					docHeight = document.body.clientHeight;
					document.getElementById("eMeng").style.top = docHeight - divHeight + parseInt(document.body.scrollTop,10)
					document.getElementById("eMeng").style.left = docWidth - divWidth + parseInt(document.body.scrollLeft,10)
				}catch(e){}
			}
		
			function moveDiv(){
				try{
					if(parseInt(document.getElementById("eMeng").style.top,10) <= (docHeight - divHeight + parseInt(document.body.scrollTop,10))){
						window.clearInterval(objTimer)
						objTimer = window.setInterval("resizeDiv()",1)
					}
					divTop = parseInt(document.getElementById("eMeng").style.top,10)
					document.getElementById("eMeng").style.top = divTop - 1
				}catch(e){}
			}
			
			function closeDiv(){
				document.getElementById('eMeng').style.visibility='hidden';
				if(objTimer) window.clearInterval(objTimer)
			}
			function forward(){
				window.location="<%=request.getContextPath()%><%=url%>";
			}
		</script>
		<table height="500" align="center">
			<tr >
				<td>
					&nbsp;
				</td>
			</tr>
		</table>		
		<DIV id=eMeng style="BORDER-RIGHT: #455690 1px solid; BORDER-TOP: #a6b4cf 1px solid; Z-INDEX:99999; LEFT: 0px; VISIBILITY: hidden; BORDER-LEFT: #a6b4cf 1px solid; WIDTH:   180px; BORDER-BOTTOM: #455690 1px solid; POSITION: absolute; TOP: 0px; HEIGHT: 116px; BACKGROUND-COLOR: #c9d3f3">
			<TABLE style="BORDER-TOP: #ffffff 1px solid; BORDER-LEFT: #ffffff 1px solid" cellSpacing=0 cellPadding=0 width="100%" bgColor=#cfdef4 border=0>
				<TBODY>
					<TR>
						<TD style="FONT-SIZE: 12px; BACKGROUND-IMAGE: url(msgTopBg.gif); COLOR: #0f2c8c" width=30 height=24></TD>
						<TD style="FONT-WEIGHT: normal; FONT-SIZE: 12px; BACKGROUND-IMAGE: url(msgTopBg.gif); COLOR: #1f336b; PADDING-TOP: 4px;PADDING-left: 4px" vAlign=center width="100%"> 短消息提示：</TD>
						<TD style="BACKGROUND-IMAGE: url(msgTopBg.gif); PADDING-TOP: 2px;PADDING-right:2px" vAlign=center align=right width=19><span title=关闭 style="CURSOR: hand;color:red;font-size:12px;font-weight:bold;margin-right:4px;" onclick=closeDiv() >×</span></TD>
					</TR>
					<TR>
						<TD style="PADDING-RIGHT: 1px; BACKGROUND-IMAGE: url(1msgBottomBg.jpg); PADDING-BOTTOM: 1px" colSpan=3 height=90>
							<DIV style="BORDER-RIGHT: #b9c9ef 1px solid; PADDING-RIGHT: 13px; BORDER-TOP: #728eb8 1px solid; PADDING-LEFT: 13px; FONT-SIZE: 12px; PADDING-BOTTOM: 13px; BORDER-LEFT: #728eb8 1px solid; WIDTH: 100%; COLOR: #1f336b; PADDING-TOP: 18px; BORDER-BOTTOM: #b9c9ef 1px solid; HEIGHT: 100%">
							您有<font color=#FF0000><%=tssl%></font><%=desc%><BR><%if(shwtg!=0){%>您有<font color=#FF0000><%=shwtg%></font><%=wtgStr%><%}%><BR><BR>
							<DIV align=center style="word-break:break-all"><a href="javascript:forward();"><font color=#FF0000><%=godesc%></font></a></DIV>       
						</TD>
					</TR>
				</TBODY>
			</TABLE>
		</DIV>
	</body>
</html:html>
