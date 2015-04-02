<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:html locale="true">
<head>
	<html:base /> 
		<title>指标定义</title>
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
				 	当前位置 >> 报表管理 >> 报表定义管理 >> 添加报表
				 </td>
			</tr>
			</table>
			<html:form action="/afReportAdd" enctype="multipart/form-data" method="POST">
			<br><br>
			<table width="90%" border="0" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
			<tr class="titletab">
				<th align="center" colspan="4">
					报表设定
				</th>
			</tr>
			<tr class="middle">
				<TD valign="top">
					<div align="center">
						<b>新增报表</b>
					</div>
					<br />					
					<table height="300" width="100%" border="0" align="center"
						cellpadding="0" cellspacing="1" bgcolor="#f5f5f5">
						<tr height="25">
							<td align="right" width="30%">
								报表模版：
							</td>
							<td>
								<html:file property="reportFile" size="20"></html:file>								
								<strong><font color="#FF0000">*</font> </strong>
							</td>
						</tr>
						<tr height="25">
							<td align="right" width="30%">
								报表ID：
							</td>
							<td>
								<html:text property="templateId" maxlength="20" size="20" ></html:text>
								<strong><font color="#FF0000">*</font> </strong>
							</td>
						</tr>
						<tr height="25">
							<td align="right">
								报表名称：
							</td>
							<td>
							<html:text property="templateName" maxlength="20" size="20" ></html:text>
								<strong><font color="#FF0000">*</font> </strong>
							</td>
						</tr>
						<tr height="25">
							<td align="right">
								报表类型：
							</td>
							<td>
							<html:select property="templateType">
								<option value="108">1104报表</option>					
								<option value="115" selected="selected">农信报表</option>					
								<option value="109">人行报表</option>
								<option value="109">财务报表</option>
								<option value="109">其他外报报表</option>
								<option value="109">分析报表</option>
								<option value="109">会计管理部</option>
								<option value="109">个人银行部</option>
								<option value="109">合规风险部</option>
							</html:select>
							
							</td>
						</tr>
						<tr height="25">
							<td align="right">
								报表分类：
							</td>
							<td>
								<html:select property="isReport">
									<option value="1">报送类</option>					
									<option value="2" >查询类</option>
								</html:select>
							</td>
						</tr>
						<tr height="25">
							<td align="right">
								起始日期：
							</td>
							<td>
								<html:text property="startDate" size="10" value="" readonly="readonly" style="text"/><img src="../image/calendar.gif" border="0" onclick="return showCalendar('endDate', 'yy-mm-dd');">						
							</td>
						</tr>
						<tr height="25">
							<td align="right">
								终止日期：
							</td>
							<td>
							<html:text property="endDate" size="10" value="" readonly="readonly" style="text"/>
							<img src="../image/calendar.gif" border="0" onclick="return showCalendar('startDate', 'yy-mm-dd');">							
							</td>
						</tr>

						<tr height="25">
							<td align="right" colspan="2">
							<html:submit value="增加报表"></html:submit>
								
							<input type="button" name="addOrg" value="返  回"
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
						<b>报表范围</b>
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
										['广州市联社',null,null,'200098','',
											['白云',null,null,'200102',''],
											['黄埔',null,null,'200103',''],
											['天河',null,null,'200104',''],
											['芳村',null,null,'200105',''],
											['海珠',null,null,'200106',''],
											['三元里',null,null,'200107',''],
											['花都',null,null,'200108',''],
											['番禺',null,null,'200109',''],
											['营业部',null,null,'2001011',''],
											['南沙',null,null,'2001012',''],
											['萝岗',null,null,'2001013',''],
											['白云新城',null,null,'2001014',''],
											['会展新城',null,null,'2001015',''],
											['珠江新城',null,null,'2001016',''],
											['西城',null,null,'2001017',''],
											['华南新城',null,null,'2001017',''],
											['羊城',null,null,'2001018',''],
											['增城',null,null,'200100',''],
											['从化',null,null,'200100','']
										],
										['广州市区',null,null,'200100','',]
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
						<b>频度,报表口径</b>
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
											['境内汇总数据',null,null,'200100','',
											['月报',null,null,'200098',''],
											['季报',null,null,'200102',''],
											['半年报',null,null,'200103',''],
											['年报',null,null,'200104','']],
											['法人汇总数据(含境外分行)',null,null,'200100','',
											['月报',null,null,'200098',''],
											['季报',null,null,'200102',''],
											['半年报',null,null,'200103',''],
											['年报',null,null,'200104','']],		
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
