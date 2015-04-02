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
		//显示指标树
		function createTargetTree()
		{
			targetTree=new dhtmlXTreeObject("targetTreeObj","100%","100%",0);
			targetTree.setImagePath("../../image/treeImgs/");
			targetTree.enableCheckBoxes(0);
			targetTree.enableThreeStateCheckboxes(true);
			targetTree.setOnDblClickHandler(treeOnDBClick);
			targetTree.loadXML("../../xml/ETL_Index_Tree.xml");
		}
		
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
		//校验公式的正确性,  bool==false校验公式语法      bool==true校验公式结果;
		function checkFormula(bool){
		    var textAreaStr = document.getElementById('idrFormula').value;
		    var IdrId = document.getElementById('IdrId').value;
		    var result = document.getElementById('result').value;		    
		   	var reptDateObj = document.getElementById('reptDate');
		   	
		    if(textAreaStr=="<P>&nbsp;</P>" || textAreaStr==""||textAreaStr=="<br>"){
		    	alert('公式为空!');
		    	return;
		    }	 	
		    
		    var reptDate = reptDateObj.value;
		    if(reptDate=="")
			{
				alert("请输入报表时间!\n");
				reptDateObj.focus();
				return;
			}
				
		    var url;	  
		    if(bool=="true"){
			     if( result==""){
			    	alert('请输入单元格的值!');
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
							<bean:write name="CellName" />单元格
							</logic:present>公式设定</span>
				</th>
			</TR>
						<tr>
							<td colspan="2" align="left" bgcolor="#EEEEEE">
								模板公式:
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
														计算公式:
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
															<input type="button" value="校验公式" class="input-button" style="width:100;height:25" onclick="checkFormula('false')">
														</td>	
														--><td>
														 选择时间:
															<input class="input-text" name="reptDate" id="reptDate" type="text" class="input-text">
															<img border="0" src="../../image/calendar.gif" onclick="return showCalendar('reptDate', 'y-mm-dd');" />
														</td>	
														<td colspan="1">
														    公式结果:
															<input class="input-text" size="20" name="result" id="result" type="text" class="input-text"  />
														</td>											
							
														<td colspan="1">
															<input type="button" value="校验公式结果" class="input-button" style="width:100;height:25" onclick="checkFormula('true')">
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
					<input type="submit" value="确  定" class="input-button">
					&nbsp;
					<input type="button" value="返  回" class="input-button" onclick="window.history.back()" />
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
