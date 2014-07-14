<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.fitech.net.common.ETLXmlToXml"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html lang="true">
<head>
	<html:base />

	<title>取数公式指标设定</title>

	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" type="text/css" rel="stylesheet">
	<jsp:include page="../../calendar.jsp" flush="true">
		<jsp:param name="path" value="../../" />
	</jsp:include>
	<script src="../../js/Org_Tree_for_xml.js"></script>

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
			
		//显示公式树
		function createTree()
		{
			tree=new dhtmlXTreeObject("treeObj","100%","100%",0);
			tree.setImagePath("../../image/treeImgs/");
			tree.enableCheckBoxes(0);
			tree.enableThreeStateCheckboxes(true);
			tree.setOnDblClickHandler(treeOnDBClick);
			tree.loadXML("../../xml/ETLTree.xml");
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
			if(isEnableUse!='N')
			{
				insertAtCaret(id);
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
		function _submit(form)
		{
			if(form.indexName.value==""){
				alert ("指标名不能为空！");
				return false;
			}
			else if(form.formual.value=="")
			{
				alert ("指标公式不能为空！");
				return false;
			}
			else{
				return true;
			}
		}	
		
		
	</script>
</head>
<%
  ETLXmlToXml.CreateETLXml(ETLXmlToXml.ParserXML());
%>
<body>
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<html:form action="/template/addEtlIndexFormual" method="post" styleId="frmSel" onsubmit="return _submit(this)">
		<!--<logic:present name="IdrId" scope="request">
			<input type="hidden" id="idrId" name="idrId" value="<bean:write name="IdrId" />">
		</logic:present>
		<logic:present name="CellName" scope="request">
			<input type="hidden" id="cellName" name="cellName" value="<bean:write name="CellName" />">
		</logic:present>
		--><table cellSpacing="1" cellPadding="4" width="96%" border="0" align="center" bgcolor="#ffffff" class="tbcolor">
			<TR class="tbcolor1">
				<th align="center" height="30" colspan="2">
					<span style="FONT-SIZE: 11pt"> 修改指标公式:</span>
				</th>
			</TR>
			<tr>
				<td colspan="2" align="left" bgcolor="#EEEEEE">
					检索公式:
				</td>
			</tr>
			<tr bgcolor="#ffffff">
				<td valign="top" colspan="2">
					<div id="treeObj" style="width:100%; height:200;background-color:#f5f5f5;border :1px solid Silver;; overflow:auto;"></div>
				</td>
			</tr>

			<tr bgcolor="#ffffff">
				<td bgcolor="#ffffff" width="center">
					<table cellSpacing="0" cellPadding="0" width="100%" border="0" align="center" class="tbcolor">
						<tr bgcolor="#ffffff" height="40">
							<td valign="middle" width="10%" align="center">
								指标公式名称:
							</td>
							<td colspan="1">
								<input type="text" name="indexName" class="input-text"  id="indexName" value=""  size="50" ><!--
								<input type="button" value="查看公式名是否存在" class="input-button" onclick="targetName()" />
							--></td>							
						</tr>
						<tr bgcolor="#ffffff" height="90">
							<td valign="middle" width="10%" align="center">
								指标公式:
							</td>
							<td colspan="1">
								<textArea name="formual" class="input-text" rows="10" style="width:90% " id="idrFormula" ONSELECT="storeCaret(this);" ONCLICK="storeCaret(this);" ONKEYUP="storeCaret(this);" onfocus="textAreaOnFocus(this)"></textArea>
							</td>							
						</tr>
						<tr bgcolor="#ffffff" height="90">
							<td valign="middle" width="10%" align="center">
								指标公式描述:
							</td>
							<td colspan="1">
								<textArea name="desc" class="input-text" rows="4" style="width:90% " id="formulaDesc" ></textArea>
							</td>							
						</tr>
					</table>
				</td>
			</tr>
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
