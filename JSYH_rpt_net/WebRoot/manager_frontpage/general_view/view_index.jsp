<!-- ��ҳ�����쵼��ҳ������ -->
<%@ page contentType="text/html; charset=GBK" language="java" import="com.fitech.gznx.common.Config"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%String appPath = request.getContextPath();
  String url="";
%>
<html:html locale="true">
<head>
	<html:base />
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/globalStyle.css" type="text/css" rel="stylesheet">
	<link href="../../css/common.css" type="text/css" rel="stylesheet">
	<script language="javascript" src="../../js/func.js"></script>
	<script language="javascript">	
		function clickNavi(s)
		{
			  var v = document.getElementById("waitDiv");
			  v.style.display='';
			  var visitPath = "<%=request.getContextPath()%>/manager_frontpage/general_view/ViewLeadingAction.do?flag=1&naviGraph="+s;
			  window.location = visitPath ;
		 }
	</script>
</head>
<body style="TEXT-ALIGN: center" background="../../image/lingdao_bg.gif">
	<table border="0" width="98%" align="center">
		<tr>

			<td align="right"><br>
				<img src="../../image/graphics_Click_1.gif" border="0"  style="cursor:hand" alt="�������" onClick="javascript:window.location='<%=request.getContextPath()%>/reportSearch/viewReportStatNX.do?isLeader=1'">
			    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			</td>
	
		</tr>  
	</table>
	<body>
	
	<br><br><br>
	<table width="98%" align="center" >
	        <TR><td>&nbsp;<br></td><td>&nbsp;<br></td><td>&nbsp;<br></td><td>&nbsp;<br></td></TR>
			<tr>
				<td align="center">
			        <!-- ��������Ӳ���ֵ���Ƕ�Ӧ��XML�ļ����ļ���ǰ׺ ����:cundaikuanqingkuang��Ӧcundaikuanqingkuang.xml  -->
				   <img src="../../image/cundaikuan.gif" border="0"  style="cursor:hand" alt="��������" onclick="clickNavi('cundaikuanqingkuang')">
				</td>
				  
				<td align="center">
				   <img src="../../image/caiwushouzhi.gif" border="0"  style="cursor:hand" alt="������֧���" onclick="clickNavi('caiwushouzhi')">
				</td>
				   
				<td align="center">
				   <img src="../../image/yingli.gif" border="0"  style="cursor:hand" alt="ӯ�����" onclick="clickNavi('yingliqingkuang')">
				</td>
				   
				
				
			</tr>
			<TR><td height="50"></td></TR>
			<tr>
			    <td align="center">
				   <img src="../../image/zichan.gif" border="0"  style="cursor:hand" alt="�ʲ���ծ�������" onclick="clickNavi('zichanfuzhaizongti')">
				</td>
				<td align="center">
				   <img src="../../image/liudong.gif" border="0"  style="cursor:hand" alt="���������" onclick="clickNavi('liudongxingqingkuang')">
				</td>
				
				<!--<td align="center">
				   <img src="../../image/yewu.gif" border="0"  style="cursor:hand" alt="ҵ�������" onclick="clickNavi('yewuliangqingkuang')">
				</td>
				
				<td align="center">
				   <img src="../../image/hangyetoufang.gif" border="0"  style="cursor:hand" alt="��ҵͶ�����" onclick="clickNavi('hangyetoufangqingkuang')">
				</td>
				-->
				<td align="center">
				   <img src="../../image/tongyeduibi.gif" border="0"  style="cursor:hand" alt="ͬҵ�Ա����" onclick="clickNavi('tongyeduibi')">
				</td><!--
				<td align="center">
				   <img src="../../image/jingyinjihua.gif" border="0"  style="cursor:hand" alt="��Ӫ�ƻ�ִ�����" onclick="clickNavi('jingyingjihuazhixingqingkuang')">
				</td>
			-->
			</tr>
	</table>
	<div id="waitDiv" style="display:none">
	    <h4><FONT color="#FF8000" >���Ժ�......</FONT></h4>
    </div>
</body>
</html:html>

<script language=javascript ></script>