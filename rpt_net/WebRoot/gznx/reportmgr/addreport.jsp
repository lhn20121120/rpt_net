<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:html locale="true">
<head>
	<html:base /> 
		<title>ָ�궨��</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../../css/common.css" rel="stylesheet" type="text/css">
		<link rel="stylesheet" type="text/css" href="../css/pageControl.css" />
		<script type="text/javascript" src="../../js/prototype-1.4.0.js"></script>
		<link rel="stylesheet" type="text/css" media="all" href="../../js/jscalendar/calendar.css" title="Aqua" />
		<script type="text/javascript" src="../../js/jscalendar/calendar.js"></script>
		<script type="text/javascript" src="../../js/jscalendar/lang/calendar-zh.js"></script>
		<script type="text/javascript" src="../../js/jscalendar/pageInclude.js"></script>
		<link href="../../css/common.css" rel="stylesheet" type="text/css">
		<link href="../../css/tree.css" rel="stylesheet" type="text/css">
		<script type="text/javascript" src="../../js/tree/tree.js"></script>
		<script type="text/javascript" src="../../js/tree/defTreeFormat.js"></script>

		<script language="javascript">

			function baosoncheck(){
				document.getElementById("treeObj_org").style.display="";
			}
			function submitreport(){
				document.forms[0].submit();
				
			}
		</script>
	</head>
	<body onload="initMethod()">
			<table border="0"width="95%">
			<tr>
				 <td>
				 	��ǰλ�� >> ������� >> ��������� >> ��ӱ���
				 </td>
			</tr>
			</table>
			<html:form action="/afReportAdd" enctype="multipart/form-data" method="POST">
			<br><br>
			<table width="90%" border="0" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
			<tr class="titletab">
				<th align="center" colspan="4">
					�����趨
				</th>
			</tr>
			<tr class="middle">
				<TD valign="top">
					<div align="center">
						<b>��������</b>
					</div>
					<br />					
					<table height="300" width="100%" border="0" align="center"
						cellpadding="0" cellspacing="1" bgcolor="#f5f5f5">
						<tr height="25">
							<td align="right" width="30%">
								����ģ�棺
							</td>
							<td>
								<html:file property="reportFile" size="20"></html:file>								
								<strong><font color="#FF0000">*</font> </strong>
							</td>
						</tr>
						<tr height="25">
							<td align="right" width="30%">
								����ID��
							</td>
							<td>
								<html:text property="templateId" maxlength="20" size="20" ></html:text>
								<strong><font color="#FF0000">*</font> </strong>
							</td>
						</tr>
						<tr height="25">
							<td align="right">
								�������ƣ�
							</td>
							<td>
							<html:text property="templateName" maxlength="20" size="20" ></html:text>
								<strong><font color="#FF0000">*</font> </strong>
							</td>
						</tr>
						<tr height="25">
							<td align="right">
								�������ͣ�
							</td>
							<td>
							<html:select property="templateType">
								<option value="108">1104����</option>					
								<option value="115" selected="selected">ũ�ű���</option>					
								<option value="109">���б���</option>
								<option value="109">���񱨱�</option>
								<option value="109">�����ⱨ����</option>
								<option value="109">��������</option>
								<option value="109">��ƹ���</option>
								<option value="109">�������в�</option>
								<option value="109">�Ϲ���ղ�</option>
							</html:select>
							
							</td>
						</tr>
						<tr height="25">
							<td align="right">
								������ࣺ
							</td>
							<td>
								<html:select property="isReport">
									<option value="1">������</option>					
									<option value="2" >��ѯ��</option>
								</html:select>
							</td>
						</tr>
						<tr height="25">
							<td align="right">
								��ʼ���ڣ�
							</td>
							<td>
								<html:text property="startDate" size="10" value="" readonly="readonly" style="text"/><img src="../image/calendar.gif" border="0" onclick="return showCalendar('endDate', 'yy-mm-dd');">						
							</td>
						</tr>
						<tr height="25">
							<td align="right">
								��ֹ���ڣ�
							</td>
							<td>
							<html:text property="endDate" size="10" value="" readonly="readonly" style="text"/>
							<img src="../image/calendar.gif" border="0" onclick="return showCalendar('startDate', 'yy-mm-dd');">							
							</td>
						</tr>

						<tr height="25">
							<td align="right" colspan="2">
							<html:submit value="���ӱ���"></html:submit>
								
							<input type="button" name="addOrg" value="��  ��"
								Class="input-button"
								onclick="window.location.assign('baobiaodinyiguanli.htm');" />
								
							</td>
						</tr>
						<tr height="25">
							<td colspan="2">
								&nbsp;
							</td>
						</tr>
					</table>
				</TD>
				<TD width="20%">
					<div align="center">
						<b>����Χ</b>
					</div>
					<br />
					<table width="100%" border="0" height="300" align="center" cellpadding="0"
						cellspacing="1" class="tbcolor">
						<tr>
							<td width="100%">
								<div  id="treeObj_org"
									style="width:100%; height:300;background-color:#f5f5f5;border :1px solid Silver;; overflow:auto;">
									<script type="text/javascript">
										var TREE1_NODES = [
										['����������',null,null,'200098','',
											['����',null,null,'200102',''],
											['����',null,null,'200103',''],
											['���',null,null,'200104',''],
											['����',null,null,'200105',''],
											['����',null,null,'200106',''],
											['��Ԫ��',null,null,'200107',''],
											['����',null,null,'200108',''],
											['��خ',null,null,'200109',''],
											['Ӫҵ��',null,null,'2001011',''],
											['��ɳ',null,null,'2001012',''],
											['�ܸ�',null,null,'2001013',''],
											['�����³�',null,null,'2001014',''],
											['��չ�³�',null,null,'2001015',''],
											['�齭�³�',null,null,'2001016',''],
											['����',null,null,'2001017',''],
											['�����³�',null,null,'2001017',''],
											['���',null,null,'2001018',''],
											['����',null,null,'200100',''],
											['�ӻ�',null,null,'200100','']
										],
										['��������',null,null,'200100','',]
										];
								        var orgTree= new ListTree("orgTree", TREE1_NODES, DEF_TREE_FORMAT,"orgTree");
								        orgTree.init();
													</script>
										</div>
									</td>
								</tr>
							</table>
						</TD>
						<TD width="20%">
					<div align="center">
						<b>Ƶ��,����ھ�</b>
					</div>
					<br />
					<table width="100%" border="0" height="300" align="center" cellpadding="0"
						cellspacing="1" class="tbcolor">
						<tr>
							<td width="100%">
								<div  id="treeObj_fre"
									style="width:100%; height:300;background-color:#f5f5f5;border :1px solid Silver;; overflow:auto;">
									<script type="text/javascript">
										var TREE3_NODES = [
											['���ڻ�������',null,null,'200100','',
											['�±�',null,null,'200098',''],
											['����',null,null,'200102',''],
											['���걨',null,null,'200103',''],
											['�걨',null,null,'200104','']],
											['���˻�������(���������)',null,null,'200100','',
											['�±�',null,null,'200098',''],
											['����',null,null,'200102',''],
											['���걨',null,null,'200103',''],
											['�걨',null,null,'200104','']],		
										];
								        var orgTree3= new ListTree("orgTree3", TREE3_NODES, DEF_TREE_FORMAT,"orgTree3");
								        orgTree3.init();
										</script>
										</div>
									</td>
								</tr>
							</table>
						</TD>
					</TR>
			</table>
								
	</html:form>
</body>
</html:html>
