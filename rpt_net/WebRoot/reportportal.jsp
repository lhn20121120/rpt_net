<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html locale="true">
<head>
	<html:base/>	
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="css/common.css" type="text/css" rel="stylesheet">
	<script language="javascript">
			/**
		     * ����˵��¼�
		     */
		    function menu_click12(_url){
		    	if(_url=="") return;
				window.location="<%=request.getContextPath()%>/reportPortal.do?reportFlg="+_url;
		    	
		    } 
		    
		    function loadpage(){
				var turnflg = document.getElementById('trunFlg').value;
				if(turnflg=="1"){				
					window.parent.parent.naviFrame.location.reload();
					window.parent.mainFrame.location.reload();
				}
			}
		</script>
</head>

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0"  onload="loadpage()" class="toppic" >
<input type="hidden" id="trunFlg" value="<%=request.getAttribute("tunflg") %>">

<div id="Page_content" >

<div id="Page_left" >	
<table width="100%"  border="0" cellspacing="0" cellpadding="0">

<tr height="40%">
<br/><br/> <br/> 
<td height="84" align="center" colspan="2" > 
<a href="javascript:menu_click12('1')"><img src="image/NCCB_c.gif" width="120" height="120"  border="0" /></a>
</td>
<%--
<td width="251" rowspan="3" >
<table height="160" cellspacing="0" cellpadding="0" width="99%" 
align="center" border="0">
<tbody>
<tr > 
<td>
<fieldset id="fieldset" >
 <legend ><font color="#6699CC" size=2>����ᱨ��</font></legend> 
<table   border="0" height="100" align="center">
	<tr align="center">
<td class="title_td" >
	<a href="#" ><font color="#FF9933" size=2>G51�ͻ��������ͳ�Ʊ� </font></a>
</td>
</tr >
	<tr align="center">
<td class="title_td">
	<a href="#" ><font color="#FF9933" size=2>
G22�����Ա�������</font></a>
</td>
</tr>
	<tr align="center">
<td class="title_td">
	<a href="#" ><font color="#FF9933" size=2>G04�����</font></a>
</td>
</tr>

	<tr align="right">
<td class="title_td">
	<a href="#" ><font color="#FF9933" size=2>... ����  </font></a>
</td>
</tr>
</table>
</fieldset>
<br>
<fieldset id="fieldset2">
<legend><font color="#6699CC" size=2>���б���</font></legend>
<table  border="0" height="100" align="center">
	<tr align="center">
<td class="title_td">
	<a href="#" ><font color="#FF9933" size=2>���ڻ����۷������±���</font> </a>
</td>
</tr >
	<tr align="center">
<td class="title_td">
	<a href="#" ><font color="#FF9933" size=2>
��ѧ����ͳ���±���</font></a>
</td>
</tr>
	<tr align="center">
<td class="title_td">
	<a href="#" ><font color="#FF9933" size=2>��������󼾱���</font></a>
</td>
</tr>

	<tr align="right">
<td class="title_td">
	<a href="#" ><font color="#FF9933" size=2>... ���� </font> </a>
</td>
</tr>
</table>
</fieldset>
<br>
<fieldset id="fieldset3">
<legend><font color="#6699CC" size=2>��������</font></legend>
<table  border="0"  height="100"  align="center">
	<tr align="center">
<td class="title_td">
	<a href="#" ><font color="#FF9933" size=2>ũ���������Ѯ��ͳ�Ʊ� </font></a>
</td>
</tr >
	<tr align="center">
<td class="title_td">
	<a href="#" ><font color="#FF9933" size=2>
�ۺ�ҵ��ϵͳ���ֻ���</font></a>
</td>
</tr>
	<tr align="center">
<td class="title_td">
	<a href="#" ><font color="#FF9933" size=2>201ũ������������Ŵ��±���</font></a>
</td>
</tr>

	<tr align="right">
<td class="title_td">
	<a href="#" ><font color="#FF9933" size=2>... ���� </font> </a>
</td>
</tr>
</table>
</fieldset>



</td>
</tr>
</tbody>
</table>
</td>
</tr>
 --%>
<tr height="20%"><td></td></tr>
<tr height="40%"> 
<td height="133" colspan="1" > <div align="center"><a href="javascript:menu_click12('2')"><img src="image/NCCB_m.gif" width="120" height="120"  border="0" /></a></div></td>
<td  height="133"  colspan="1"> <div align="center" ><a href="javascript:menu_click12('3')"><img src="image/NCCB_p.gif" width="120" height="120"  border="0" /></a> 
</td>
</tr>
<tr> 
<td height="43" colspan="2">
</td>
</tr>
</table>		
</div>	

</div>

</body>
</html:html> 