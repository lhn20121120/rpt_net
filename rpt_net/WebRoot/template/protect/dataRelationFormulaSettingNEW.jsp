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
		//��ǰ������ڵ��ı���
		var onFocusTextArea = null; 
		//��ʾָ����
		function createTargetTree()
		{
			targetTree=new dhtmlXTreeObject("targetTreeObj","100%","100%",0);
			targetTree.setImagePath("../../image/treeImgs/");
			targetTree.enableCheckBoxes(0);
			targetTree.enableThreeStateCheckboxes(true);
			targetTree.setOnDblClickHandler(treeOnDBClick);
			targetTree.loadXML("../../xml/ETL_Index_Tree.xml");
		}
		
		//��ʾ��ʽ��
		function createTree()
		{
			tree=new dhtmlXTreeObject("treeObj","100%","100%",0);
			tree.setImagePath("../../image/treeImgs/");
			tree.enableCheckBoxes(0);
			tree.enableThreeStateCheckboxes(true);
			tree.setOnDblClickHandler(treeOnDBClick);
			tree.loadXML("../../xml/ETLTree.xml");
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
			if(isEnableUse!='N')
			{
				insertAtCaret(id);
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
		//У�鹫ʽ����ȷ��,  bool==falseУ�鹫ʽ�﷨      bool==trueУ�鹫ʽ���;
		function checkFormula(bool){
		    var textAreaStr = document.getElementById('idrFormula').value;
		    var IdrId = document.getElementById('IdrId').value;
		    var result = document.getElementById('result').value;		    
		   	var reptDateObj = document.getElementById('reptDate');
		   	
		    if(textAreaStr=="<P>&nbsp;</P>" || textAreaStr==""||textAreaStr=="<br>"){
		    	alert('��ʽΪ��!');
		    	return;
		    }	 	
		    
		    var reptDate = reptDateObj.value;
		    if(reptDate=="")
			{
				alert("�����뱨��ʱ��!\n");
				reptDateObj.focus();
				return;
			}
				
		    var url;	  
		    if(bool=="true"){
			     if( result==""){
			    	alert('�����뵥Ԫ���ֵ!');
			    	return;
			    }	 
		  	   url="<%=request.getContextPath()%>/template/checkFormula.do?textAreaStr=" + textAreaStr + "&IdrId=" + IdrId + "&reptDate=" + reptDate+"&result=" + result;
		  	}
		  	else{
		  		url="<%=request.getContextPath()%>/template/checkFormula.do?textAreaStr=" + textAreaStr + "&IdrId=" + IdrId ;
		  	}
		  	window.location=url;
		}
	</script>
</head>

<body>
<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<html:form action="/template/saveDataRelationFormualSetting" method="post" styleId="frmSel">
		<logic:present name="IdrId" scope="request">
			<input type="hidden" id="idrId" name="idrId" value="<bean:write name="IdrId" />">
		</logic:present>
		<logic:present name="CellName" scope="request">
			<input type="hidden" id="cellName" name="cellName" value="<bean:write name="CellName" />">
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
								ģ�幫ʽ:
							</td>
						</tr>
						<tr bgcolor="#ffffff">
							<td valign="middle" colspan="2">
								<table cellSpacing="0" cellPadding="0" width="100%" border="0" align="center" class="tbcolor">
									<tr bgcolor="#ffffff" height="90">									
										<td valign="top" colspan="2" >
												<div id="treeObj" style="width:100%; height:250;background-color:#f5f5f5;border :1px solid Silver;; overflow:auto;"></div>
										</td>
										<td valign="top">
											<div id="targetTreeObj" style="width:100%; height:250;background-color:#f5f5f5;border :1px solid Silver;; overflow:auto;"></div>
										</td>
									</tr>
								</table>
							</td>		
						</tr>
						
						

			<tr bgcolor="#ffffff">
				<td bgcolor="#ffffff" width="center">
					<table cellSpacing="0" cellPadding="4" width="100%" border="0" bgcolor="#ffffff" align="center" class="tbcolor">
						<tr bgcolor="#ffffff">
							<td width="200" height="180" bgcolor="#ffffff" align="center">
								<table cellSpacing="0" cellPadding="0" width="160" height="160" border="0" align="center" class="tbcolor">
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
									</tr>
									<tr bgcolor="#ffffff" align="center">
										<td width="40">
											<input type="button" value="/" class="input-button" style="width:50;height:40" onclick="operatorClick('/')">
										</td>
										<td>
											<input type="button" value="DJ()" class="input-button" style="width:50;height:40" onclick="operatorClick('DJ()')">
										</td>
										<td>
											<input type="button" value="DY()" class="input-button" style="width:50;height:40" onclick="operatorClick('DY()')">
										</td>
									</tr>
									<tr bgcolor="#ffffff" align="center">
										<td>
											<input type="button" value="(" class="input-button" style="width:50;height:40" onclick="operatorClick('(')">
										</td>
										<td>
											<input type="button" value=")" class="input-button" style="width:50;height:40" onclick="operatorClick(')')">
										</td>
										<td>
											<input type="button" value="" class="input-button" style="width:50;height:40" >
										</td>
									</tr>
								</table>	
							</td>
							<td width="100%" height="250" bgcolor="#ffffff">
								<table cellSpacing="0" cellPadding="0" width="100%" border="0" align="center" class="tbcolor">
									<tr bgcolor="#ffffff" height="90">
										<td >
											<table cellSpacing="0" cellPadding="0" width="100%" border="0" align="center" class="tbcolor">
												<tr bgcolor="#ffffff" height="90">									
													<td valign="middle" width="2%" align="center">
														���㹫ʽ:
													</td>
													<td colspan="1">
														<textArea name="idrFormula" class="input-text" rows="10" cols="90%" id="idrFormula" ONSELECT="storeCaret(this);" ONCLICK="storeCaret(this);" ONKEYUP="storeCaret(this);" onfocus="textAreaOnFocus(this)"><logic:present name="IdrFormula" scope="request"><bean:write name="IdrFormula"/></logic:present></textArea>
													</td>
												</tr>
											</table>
										</td>
									</tr>
									<tr   bgcolor="#ffffff" >
										<td width="100%" height="80"  align="center">
											<table cellSpacing="0" cellPadding="0" width="100%" border="0" align="center" class="tbcolor">
													<tr bgcolor="#ffffff"  >
														<!--<td valign="middle" width="50%">
															<input type="button" value="У�鹫ʽ" class="input-button" style="width:100;height:25" onclick="checkFormula('false')">
														</td>	
														--><td>
														 ѡ��ʱ��:
															<input class="input-text" name="reptDate" id="reptDate" type="text" class="input-text">
															<img border="0" src="../../image/calendar.gif" onclick="return showCalendar('reptDate', 'y-mm-dd');" />
														</td>	
														<td colspan="1">
														    ��ʽ���:
															<input class="input-text" size="20" name="result" id="result" type="text" class="input-text"  />
														</td>											
							
														<td colspan="1">
															<input type="button" value="У�鹫ʽ���" class="input-button" style="width:100;height:25" onclick="checkFormula('true')">
														</td>
														
													</tr>											
											</table>											
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
			createTargetTree();
		</script>
	</html:form>

</body>

</html:html>
