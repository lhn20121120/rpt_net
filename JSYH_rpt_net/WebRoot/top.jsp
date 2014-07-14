<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@page import="com.cbrc.smis.common.Config"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html:html locale="true">
	<head>
	<html:base/>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="style/globalStyle.css" type="text/css" rel="stylesheet">
			<script language="JavaScript" type="text/JavaScript">
<!--
function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}

		/**
		 * 注销事件
		 */
		 function _exit(){
		 	if(confirm("您确定要注销用户吗?\n")==true){
		 		window.parent.location="logout.jsp";
		 	}
		 }
		 /**
		 * 刷新
		 */
		 function onRefresh() {
		 	parent.mainFrame.location.reload();
  			parent.mainFrame.contents.location.reload();
  			parent.naviFrame.location.reload();
		 }
		 /**
		 * 帮助
		 */

		 /**
		 * 技术支持
		 */
		 function Support() {
		 	window.open("support.jsp","support","top=250px,left=380px,width=320,height=250, alwaysRaised:yes");	
		 }
		
//-->
			</script>
	</head>
	<body >
		<table cellSpacing="0" cellPadding="0" width="100%" border="0">
			<tr>
				<td width="800" height="70" background="image/tiao.gif"><img src="image/top_font.jpg"/></td>
				<td vAlign="bottom" align="right" width="224" background="image/tiao.gif">
					<table cellSpacing="0" cellPadding="5" width="224" border="0">
						<tr>
							<td align="center">
							<a href="javascript:onRefresh()"/>主页</a>
							</td>
							<td>|</td>
							<td  align="center">
							<a href="<%=request.getContextPath()%>/help.jsp" >帮助</a>
							</td>
							<td>|</td>
							<!--  
							<td width="22%" align="center">
							<a href="javascript:Support()">支持</a>
							</td>
							-->
							<td align="center">
							<a href="javascript:_exit()">退出</a>
							</td>
							<td width="10%" align="center">
							&nbsp;
							</td>
							<td width="22%" align="center" style="font-family: arial;">
								V2.1.0016
							</td>
						</tr>
						<tr>
						<td></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</body>
</html:html>
