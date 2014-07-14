<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html lang="true">
<head>
	<html:base />

	<title>��Ԫ��ʽ�趨</title>

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
		//��ǰ������ڵ��ı���
		var onFocusTextArea = null; 
		
		//��ʾ��
		function createTree()
		{
			tree=new dhtmlXTreeObject("treeObj","100%","100%",0);
			tree.setImagePath("../../image/treeImgs/");
			tree.enableCheckBoxes(0);
			tree.enableThreeStateCheckboxes(true);
			tree.setOnDblClickHandler(treeOnDBClick);
			tree.loadXML("../../xml/ETL.xml");
		}
		//������ŵ���¼�
		function operatorClick(operatorValue)
		{
			insertAtCaret(operatorValue);
		}
		
		//�ı�������꽹��
		function textAreaOnFocus(textAreaObj)
		{
			onFocusTextArea = textAreaObj;
		}
		//���ڵ�˫���¼�
		function treeOnDBClick(id)
		{
			//�����ýڵ��Id,�鿴�ýڵ��Ƿ��ǿ���Ч����Ľڵ�
			var isEnableUse = id.charAt(0);		
			if(isEnableUse=='Y')
			{
				//�ڵ����� 1-��ʵ�� 2-γ�ȱ� 3-ָ�� 4-ֵ
				var nodeType = id.charAt(2);
				//ֵ���� 0-��ֵ�� 1-�ַ���
				var valueType = id.charAt(4);
				//�ýڵ��ֵ
				var value = id.substr(id.lastIndexOf("_")+1,id.length);
				//�Ѳ�ͬ���͵�ֵ,���ϲ�ͬ�ı�־								
				if(nodeType==1 || nodeType==3)
					value = "{"+value+"}";
				else if(nodeType==2)
					value = "["+value+"]";
				else if(nodeType==4 && valueType==1)
					value = "'"+value+"'";
					
				insertAtCaret(value);
			}	
		}
		
		//��ý���
		function storeCaret(textEl) 
		{
			if (textEl.createTextRange) 
				textEl.caretPos = document.selection.createRange().duplicate(); 
		}
		
		/*�ڵ�ǰ��괦�����ַ�*/
		function insertAtCaret(text) 
		{
			if(onFocusTextArea==null)
			{
				alert('��ѡ��Ҫ������ı���!');
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
							<bean:write name="CellName" />��Ԫ��
						</logic:present>��ʽ�趨</span>
				</th>
			</TR>
			<tr>
				<td colspan="2" align="left" bgcolor="#EEEEEE">
					����������:
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
											<input type="button" value="���SQL����>>" class="input-button" style="width:200;height:20" onclick="operatorClick('<>')">
										</td>
									</tr>
									<tr bgcolor="#ffffff">
										<td height="20" colspan="4" align="center">
											�������
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
											���㹫ʽ:
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
					<input type="submit" value="ȷ  ��" class="input-button">
					&nbsp;
					<input type="button" value="��  ��" class="input-button" onclick="window.history.back()" />
				</td>
			</tr>
		</table>
		<script language="javascript">	
			createTree();
		</script>
	</html:form>

</body>

</html:html>
