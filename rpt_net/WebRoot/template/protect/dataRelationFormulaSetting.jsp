<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html lang="true">
<head>
	<html:base />

	<title>单元格公式设定</title>

	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" type="text/css" rel="stylesheet">
	<script src="../../js/Tree_for_xml.js"></script>

	<style rel="STYLESHEET" type="text/css">
			.defaultTreeTable{margin : 0;padding : 0;border : 0;}
			.containerTableStyle { overflow : auto;}
			.standartTreeRow{	font-family : Verdana, Geneva, Arial, Helvetica, sans-serif; 	font-size : 14px; -moz-user-select: none; }
			.selectedTreeRow{ background-color : navy; color:white; font-family : Verdana, Geneva, Arial, Helvetica, sans-serif; 		font-size : 14x;  -moz-user-select: none;  }
			.standartTreeImage{ width:14x; height:1px;  overflow:hidden; border:0; padding:0; margin:0; }			
			.hiddenRow { width:1px;   overflow:hidden;  }
			.dragSpanDiv{ 	font-size : 12px; 	border: thin solid 1 1 1 1; }
		</style>
	<script language="javascript">				
		//当前光标所在的文本域
		var onFocusTextArea = null; 
		
		//显示树
		function createTree()
		{
			tree=new dhtmlXTreeObject("treeObj","100%","100%",0);
			tree.setImagePath("../../image/treeImgs/");
			tree.enableCheckBoxes(0);
			tree.enableThreeStateCheckboxes(true);
			tree.setOnDblClickHandler(treeOnDBClick);
			tree.loadXML("../../xml/ETL.xml");
		}
		//运算符号点击事件
		function operatorClick(operatorValue)
		{
			insertAtCaret(operatorValue);
		}
		
		//文本域获得鼠标焦点
		function textAreaOnFocus(textAreaObj)
		{
			onFocusTextArea = textAreaObj;
		}
		//树节点双击事件
		function treeOnDBClick(id)
		{
			//解析该节点的Id,查看该节点是否是可有效点击的节点
			var isEnableUse = id.charAt(0);		
			if(isEnableUse=='Y')
			{
				//节点类型 1-事实表 2-纬度表 3-指标 4-值
				var nodeType = id.charAt(2);
				//值类型 0-数值型 1-字符型
				var valueType = id.charAt(4);
				//该节点的值
				var value = id.substr(id.lastIndexOf("_")+1,id.length);
				//把不同类型的值,加上不同的标志								
				if(nodeType==1 || nodeType==3)
					value = "{"+value+"}";
				else if(nodeType==2)
					value = "["+value+"]";
				else if(nodeType==4 && valueType==1)
					value = "'"+value+"'";
					
				insertAtCaret(value);
			}	
		}
		
		//获得焦点
		function storeCaret(textEl) 
		{
			if (textEl.createTextRange) 
				textEl.caretPos = document.selection.createRange().duplicate(); 
		}
		
		/*在当前光标处插入字符*/
		function insertAtCaret(text) 
		{
			if(onFocusTextArea==null)
			{
				alert('请选择要输入的文本框!');
				return;
			}
			if (onFocusTextArea.createTextRange && onFocusTextArea.caretPos) 
			{
				var caretPos = onFocusTextArea.caretPos;
				caretPos.text =caretPos.text.charAt(caretPos.text.length - 1) == ' ' ?text + ' ' : text; 
			} 
			else 
				onFocusTextArea.value = text;
		} 
	</script>
</head>

<body>
	<html:form action="/template/saveDataRelationFormualSetting" method="post" styleId="frmSel">
		<logic:present name="IdrId" scope="request">
			<input type="hidden" name="idrId" value="<bean:write name="IdrId" />">
		</logic:present>
		<logic:present name="CellName" scope="request">
			<input type="hidden" name="cellName" value="<bean:write name="CellName" />">
		</logic:present>
		<table cellSpacing="1" cellPadding="4" width="96%" border="0" align="center" bgcolor="#ffffff" class="tbcolor">
			<TR class="tbcolor1">
				<th align="center" height="30" colspan="2">
					<span style="FONT-SIZE: 11pt"> <logic:present name="CellName" scope="request">
							<bean:write name="CellName" />单元格
						</logic:present>公式设定</span>
				</th>
			</TR>
			<tr>
				<td colspan="2" align="left" bgcolor="#EEEEEE">
					检索对象树:
				</td>
			</tr>
			<tr bgcolor="#ffffff">
				<td valign="top" colspan="2">
					<div id="treeObj" style="width:100%; height:200;background-color:#f5f5f5;border :1px solid Silver;; overflow:auto;"></div>
				</td>
			</tr>
			<tr bgcolor="#ffffff">
				<td bgcolor="#ffffff" width="center">
					<table cellSpacing="0" cellPadding="4" width="100%" border="0" bgcolor="#ffffff" align="center" class="tbcolor">
						<tr bgcolor="#ffffff">
							<td width="200" height="180" bgcolor="#ffffff" align="center">
								<table cellSpacing="0" cellPadding="0" width="160" height="160" border="0" align="center" class="tbcolor">
									<tr bgcolor="#ffffff" align="center">
										<td colspan="4">
											<input type="button" value="添加SQL定义>>" class="input-button" style="width:200;height:20" onclick="operatorClick('<>')">
										</td>
									</tr>
									<tr bgcolor="#ffffff">
										<td height="20" colspan="4" align="center">
											运算符号
										</td>
									</tr>
									<tr bgcolor="#ffffff" align="center">
										<td width="40">
											<input type="button" value="+" class="input-button" style="width:50;height:40" onclick="operatorClick('+')">
										</td>
										<td width="40">
											<input type="button" value="-" class="input-button" style="width:50;height:40" onclick="operatorClick('-')">
										</td>
										<td width="40">
											<input type="button" value="*" class="input-button" style="width:50;height:40" onclick="operatorClick('*')">
										</td>
										<td width="40">
											<input type="button" value="/" class="input-button" style="width:50;height:40" onclick="operatorClick('/')">
										</td>
									</tr>
									<tr bgcolor="#ffffff" align="center">
										<td>
											<input type="button" value="&gt;" class="input-button" style="width:50;height:40" onclick="operatorClick('&gt;')">
										</td>
										<td>
											<input type="button" value="&lt;" class="input-button" style="width:50;height:40" onclick="operatorClick('&lt;')">
										</td>
										<td>
											<input type="button" value="(" class="input-button" style="width:50;height:40" onclick="operatorClick('(')">
										</td>
										<td>
											<input type="button" value=")" class="input-button" style="width:50;height:40" onclick="operatorClick(')')">
										</td>
									</tr>
									<tr bgcolor="#ffffff" align="center">
										<td>
											<input type="button" value="AND" class="input-button" style="width:50;height:40" onclick="operatorClick('AND')">
										</td>
										<td>
											<input type="button" value="OR" class="input-button" style="width:50;height:40" onclick="operatorClick('OR')">
										</td>
										<td>
											<input type="button" value="NOT" class="input-button" style="width:50;height:40" onclick="operatorClick('NOT')">
										</td>
										<td>
											<input type="button" value="=" class="input-button" style="width:50;height:40" onclick="operatorClick('=')">
										</td>
									</tr>
									<tr bgcolor="#ffffff" align="center">
										<td>
											<input type="button" value="BETWEEN" class="input-button" style="width:50;height:40" onclick="operatorClick('BETWEEN AND')">
										</td>
										<td>
											<input type="button" value="IN" class="input-button" style="width:50;height:40" onclick="operatorClick('IN')">
										</td>
										<td>
											<input type="button" value="SUM" class="input-button" style="width:50;height:40" onclick="operatorClick('SUM()')">
										</td>
										<td>
											<input type="button" value="AVG" class="input-button" style="width:50;height:40" onclick="operatorClick('AVG()')">
										</td>
									</tr>
									
								</table>
							</td>
							<td width="100%" height="180" bgcolor="#ffffff">
								<table cellSpacing="0" cellPadding="0" width="100%" border="0" align="center" class="tbcolor">
									<tr bgcolor="#ffffff" height="90">
										<td valign="middle" width="10%" align="center">
											计算公式:
										</td>
										<td colspan="1">
											<textArea name="idrFormula" class="input-text" rows="10" cols="90%" id="idrFormula" ONSELECT="storeCaret(this);" ONCLICK="storeCaret(this);" ONKEYUP="storeCaret(this);" onfocus="textAreaOnFocus(this)"><logic:present name="IdrFormula" scope="request"><bean:write name="IdrFormula"/></logic:present></textArea>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			<tr>
			<tr bgcolor="#ffffff">
				<td align="center">
					<input type="submit" value="确  定" class="input-button">
					&nbsp;
					<input type="button" value="返  回" class="input-button" onclick="window.history.back()" />
				</td>
			</tr>
		</table>
		<script language="javascript">	
			createTree();
		</script>
	</html:form>

</body>

</html:html>
