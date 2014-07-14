<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.form.AbnormityChangeForm,com.cbrc.smis.common.Config"%>
<%@ page import="com.fitech.net.form.TargetDefineWarnForm"%>
<jsp:useBean id="utilFormBean" scope="page" class="com.cbrc.smis.form.UtilForm" />
<%
	String select = "";
	
%>
<html:html locale="true">
<head>
	<html:base />
	<title>
		指标的预警设置
	</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" type="text/css" rel="stylesheet">
	<script language="javascript" src="../../js/func.js"></script>
	<script language="javascript">
		<!--
		    /**
		     * 是否已设定了机构或机构类型
		     */
		     var is_set_org_or_orgcl="0";
		     
			/**
			 *分隔符1
			 */
			 var SPLIT_SMYBOL_COMMA="<%=Config.SPLIT_SYMBOL_COMMA%>";
			 /**
			 *分隔符2
			 */
			 var SPLIT_SMYBOL_ESP="<%=Config.SPLIT_SYMBOL_ESP%>";
			 
			
			 
			
			  
			/**
			 *表单提交事件
			 */
			 function _submitFrm(){
			 
			 		var form=document.getElementById('frm');

			 		if(isEmpty(form.targetDefineId.value)==true )
			 		{
			 			alert("获取指标ID失败,操作终止!\n");
			 			return false;
				 	}
			 	
			 		setCellValue();
			 
			 		
			 		
			 		var objTbl=document.getElementById("tbl");
			 		var _standard="";       //异常变化设定的标准
			 		for(var i=1;i<objTbl.rows.length;i++){
			 			var objCell=objTbl.rows[i].cells[3];
			 			if(isEmpty(objCell.innerHTML)==true){
			 				alert("请选择颜色项!\n");
			 				focusCell(objCell);
			 				return false;
			 			}
			 			var _tmp=objCell.id;
			 			
			 			for(var j=1;j<cols-1;j++){
			 				
			 				if(j==3)
			 				{
			 				
			 				   var _cellValue=objTbl.rows[i].cells[j].innerHTML;
			 				   var indexstart=_cellValue.indexOf(":");
			 				   
			 				   var indexend=_cellValue.indexOf(">");
			 				  
			 				   _cellValue=_cellValue.substring(indexstart+1,indexend-1);
			 				   
			 				   	_tmp+=(isEmpty(_tmp)==true?"":SPLIT_SMYBOL_COMMA) + _cellValue;
			 				}
			 				else
			 				{
			 				var _cellValue=objTbl.rows[i].cells[j].innerText;
			 				
			 				_tmp+=(isEmpty(_tmp)==true?"":SPLIT_SMYBOL_COMMA) + _cellValue.replace("%","");
			 				}
			 			}
			 			_standard+= _tmp + SPLIT_SMYBOL_ESP;
			 		}
			 		
			 		//alert(_standard);
			 		if(isEmpty(_standard)==true){
			 			alert("请设定指标定义标准!\n");
			 			return false;
			 		}
			 		form.standard.value=_standard;
			 		
			 		return true;
			 }
			 
			/**
			 * 表格的行高
			 */
			 var rowHeight=25;
			/**
			 * 表格的列数
			 */ 
			 var cols=5;
			/**
			 * 当前单元格的行
			 */
			 var curRow=1;
			/**
			 * 当前单元格的列
			 */
			 var curCol=1;
			 
			 
			/**
			 * 增加表格的行
			 */
			 function _addRow(){
			 	 var objTbl=document.getElementById('tbl');

			 	 var objRow = objTbl.insertRow();  //增加行
			 	 objRow.height=rowHeight;
			 	 objRow.style.backgroundColor="#FFFFFF";
			 	 
			 	 var rows=objTbl.rows.length-1;
				 for(var i=0;i<cols;i++){
				 	  var objCell = objRow.insertCell();
				 	  objCell.style.textAlign="center";
						if(i==0){
							objCell.innerText=rows;
						}else{
							if(i==(cols-1)){
								objCell.innerHTML="<img src='../../image/del.gif' border='0' onClick='_delRow(this)' style='cursor:hand'  title=\"删除\">";
							}else{
								objCell.innertText="&nbsp;";
							}
						}
				 }
			 }
			 
			 /**
			  * 删除行
			  *
			  * @param rowIndex 行号
			  */
			  function _delRow(obj){
			  	try{
			  		var objTbl=document.getElementById('tbl');
					
						rowIndex=obj.offsetParent.parentElement.rowIndex;
			  		objTbl.deleteRow(rowIndex);

			  		for(var i=2;i<objTbl.rows.length;i++){
			  			objTbl.rows[i].cells[0].innerText=eval(i-1);
			  		}
			  		objTbl.refresh();
			  	}catch(e){
			  		alert("删除错误");
			  	}
			  }
			  
			  /**
			   * 单元格取值
			   */
			   function setCellValue(){
			   	 var objTbl=document.getElementById("tbl");
			   	 if((curCol>0 && curCol<cols-1) && (curRow>0 && curRow<objTbl.rows.length)){
			   	 	 var objCell=objTbl.rows[curRow].cells[curCol];
						if(curCol==3){
						 	 
								 var _obj=document.getElementById('colName');	
								 var _selectText="<label style=\"background:"+_obj.options[_obj.selectedIndex].value+"\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></label>";
								 
								 objCell.innerHTML=_selectText;
							 
							 
						 }else{
							 var _obj=document.getElementById('standard');
							 if(typeof(_obj)!="undefined"){
							 	  var _ov=_obj.value;
							     var objstr=_ov.substr(0,1);
							     if(objstr=="-"){
							     	_ov=_ov.substr(1,_ov.length);
							     	 objCell.innerText="-"+(_checkNum(_ov)==false?"0":_ov) + "%";
							     }
							     else{
							 		 objCell.innerText=(_checkNum(_obj.value)==false?"0":_obj.value) + "%";
							 	}
							 	 curCol=0;
							 }
						 }
					 }	
			   }
			   
			  /**
			   * 激活单元格
			   */
			   function focusCell(obj){
			   	 var objTbl=document.getElementById('tbl');
			   	 var tagName=obj.tagName;
			   	 if(tagName.toUpperCase()=="TD"){
			   	   	 curCol=obj.cellIndex;
			   	   	 curRow=obj.parentElement.rowIndex;
			   	   	 if((curCol>0 && curCol<cols-1) && (curRow>0 && curRow<objTbl.rows.length)){
				   	  	 var _value=obj.innerText;
				   	 			
				   	  		 if(curCol==3){	
					   	   	 obj.innerHTML=_SELECT;
					   	   	 var objSelect=document.getElementById('colName');

					   	   	 for(var i=0;i<objSelect.options.length;i++){
					   	   	 	 if(objSelect.options[i].text==_value){
					   	   	 	 	 objSelect.selectedIndex=i;
					   	   	 	 }
					   	   	 }
					   	   }else{
					   	   	 obj.innerHTML=_TEXT;
					   	   	 var objText=document.getElementById('standard');
					   	   	 objText.value=_value.replace("%","");
					   	   	 //objText.left=obj.offsetLeft;
					   	   	 //objText.top=obj.offsetTop;
					   	   	 //objText.width=obj.width;
					   	   	 objText.focus();
					   	   }
			   	   	 }
			   	   }	
			   }
			   
			  /**
			   * 跳转到指定的单元格
			   *
			   * @param rowIndex int 单元格的行
			   * @param colIndex int 单元格的列
			   * @return void
			   */
			   function gotoCell(rowIndex,colIndex){
			   	 var objTbl=document.getElementById("frm");
			   	 if(rowIndex<2 || rowIndex>=objTbl.rows.length) return;
			   	 
			   	 var objCell=objTbl.rows[rowIndex].cells[colIndex];
			   	 if(typeof(objCell)=="undefined") return;
			   	 
			   	 focusCell(objCell);
			   }
			   
			  /**
			   * 选中单元格事件
			   */
			   function enterCell(){
			   	 var obj=event.srcElement;
					 var objTbl=document.getElementById('tbl');
					 var tagName=obj.tagName;
					 
					 try{
					 	 if(tagName.toUpperCase()=="TD"){
							 setCellValue();
						 }
					 }catch(e1){
							curCol=0;
							curRow=0;
							alert(e1.description);
					 }
					 
			   	 try{
			   	 	 focusCell(obj);
			   	 	 /*
			   	   if(tagName.toUpperCase()=="TD"){
			   	   	 curCol=obj.cellIndex;
			   	   	 curRow=obj.parentElement.rowIndex;
			   	   	 if((curCol>0 && curCol<cols-1) && (curRow>1 && curRow<objTbl.rows.length)){
				   	   	 var _value=obj.innerText;
				   	 			
				   	 		 if(curCol==1){	
					   	   	 obj.innerHTML=_SELECT;
					   	   	 var objSelect=document.getElementById('colName');

					   	   	 for(var i=0;i<objSelect.options.length;i++){
					   	   	 	 if(objSelect.options[i].text==_value){
					   	   	 	 	 objSelect.selectedIndex=i;
					   	   	 	 }
					   	   	 }
					   	   }else{
					   	   	 obj.innerHTML=_TEXT;
					   	   	 var objText=document.getElementById('standard');
					   	   	 objText.value=_value.replace("%","");
					   	   	 //objText.left=obj.offsetLeft;
					   	   	 //objText.top=obj.offsetTop;
					   	   	 //objText.width=obj.width;
					   	   	 objText.focus();
					   	   }
			   	   	 }
			   	   }
			   	   */
			   	 }catch(E){
			   	 	 alert(E.description);
			   	 }
			   }
			   
			   /**
			    * 下拉框选择事件
			    */
			   function _selChange(obj){
			   	 var opObj=obj.offsetParent;
			   	 
			   	 var selValue=obj.options[obj.selectedIndex].value;
			   	 
			   	 var _curRow=opObj.parentElement.rowIndex;
			   	 var objTbl=document.getElementById('tbl');
			   	 
			   	 var flag=true;
			   	 for(var i=2;i<objTbl.rows.length;i++){	
			   	   if(objTbl.rows[i].cells[1].id==selValue){
			   	   	 flag=false;
			   	   	 break;
			   	   }
			   	 }
			   	 
			   	 if(flag==true){
			   	 	 opObj.id=selValue;
			   	 }else{
			   	 	 alert("你选择的项已经在在了，请重新选择!\n");
			   	 }
			   }
			   
			   var _TEXT="<input type=\"text\" id=\"standard\" name=\"standard\" value=\"\" class=\"input-text\" size=\"10\">%";
			   var _SELECT="<select name=\"colName\"  >"+
"<option value=\"red\" style=\"background:red\">&nbsp;&nbsp;&nbsp;&nbsp;</option>"+
"<option value=\"green\" style=\"background:green\">&nbsp;&nbsp;&nbsp;&nbsp;</option>"+
"<option value=\"blue\" style=\"background:blue\">&nbsp;&nbsp;&nbsp;&nbsp;</option>"+
"<option value=\"yellow\" style=\"background:yellow\">&nbsp;&nbsp;&nbsp;&nbsp;</option>"+
"<option value=\"black\" style=\"background:black\">&nbsp;&nbsp;&nbsp;&nbsp;</option>"+
"<option value=\"white\" style=\"background:white\">&nbsp;&nbsp;&nbsp;&nbsp;</option>"+
"</select>";
			   
			  /**
			   * 下一步事件
			   */
			   function _next(){
			   
			   
			   	 var form=document.forms['frm'];
			   	 window.location="<%=request.getContextPath()%>/target/add/addPreStandard.jsp?";
			   }
			   /**
			    *进入机构设置页面
			    */
			   function _setOrg()
			   {
			   var objFrm=document.forms['frm'];
			   window.location="<%=request.getContextPath()%>/template/ycbh/PreEditYCBH.do?"+
			            "&childRepId=" + objFrm.childRepId.value + 
			        	"&versionId=" + objFrm.versionId.value + 
			        	"&reportStyle=" + objFrm.reportStyle.value;
	  		 			
			   }
		//-->
		</script>
</head>
<body background="../image/total.gif">
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<TABLE cellSpacing="0" cellPadding="1" width="96%" border="0" align="center">
		<tr>
			<td height="10"></td>
		</tr>
	</table>
	<TABLE cellSpacing="1" cellPadding="1" width="96%" border="0" class="tbcolor" align="center">
		<tr class="tbcolor1">
			<th colspan="8" align="center" id="list0" height="30">
				<p style="MARGIN-TOP: 0px; MARGIN-BOTTOM: -2px">
					<span style="FONT-WEIGHT: 400">
						&nbsp;
					</span>
				</p>
				<span style="FONT-WEIGHT: 400">
					<P style="MARGIN-TOP: 0px; MARGIN-BOTTOM: -2px">
						<SPAN style="FONT-SIZE: 12pt">
							<STRONG>
								<logic:present name="ObjForm" scope="request">
									《<bean:write name="ObjForm" property="targetDefineName"/>》
								</logic:present>
								指标预警设置
							</STRONG>
						</SPAN>
					</P>
				</span>
			</th>
		</tr>
		<tr>
			
		</tr>
		<tr>
			<td colspan="8" bgcolor="#FFFFFF">
				
			</td>
		</tr>
	</table>
	<table border="0" cellpadding="0" cellspacing="0" width="96%">
		<tr>
			<td height="10"></td>
		</tr>
	</table>
	<TABLE name="tbl" id="tbl" cellSpacing="1" cellPadding="1" width="96%" border="0" class="tbcolor" align="center" onclick="enterCell()">
		<TR>
			<td width="6%" align="center"  class="tableHeader">
				<b>
					序号
				</b>
			</td>
			<td width="20%" align="center"  class="tableHeader">
				<b>
					下限
				</b>
			</td>
			<td width="32%" align="center"  class="tableHeader">
				<b>
					上限
				</b>
			</td>
			<td width="32%" align="center"  class="tableHeader">
				<b>
					颜色
				</b>
			</td>
			<td  class="tableHeader" width="10%">
				&nbsp;
			<td>
		</TR>
		
		<TR bgcolor="#FFFFFF" height="25">
			<TD width="2%" align="center">
				1
			</TD>
			
			<TD width="14%" align="center">
				<INPUT class="input-text" id="standard" name="standard" type="text" size="10" value="">
				%
			</TD>
			<TD width="14%" align="center"></TD>
			
			<TD width="14%" align="center"></TD>
			<td align="center">
				<img src="../../image/del.gif" border="0" onclick="_delRow()" style="cursor:hand" title="删除">
			</td>
		</TR>
	</TABLE>

	<form name="frm" method="post" action="../insertTargetDefineWarn.do" onsubmit="return _submitFrm()">
		<table border="0" cellspacing="0" cellpadding="4" width="96%" align="center">
			<TR>
				<TD align="center">
					<input type="button" value="增加规则" class="input-button" onclick="_addRow()">
					&nbsp;&nbsp;
					<input type="submit" value=" 下 一 步 " class="input-button">&nbsp;&nbsp;
					
				</TD>
			</TR>
		</table>
		<logic:present name="ObjForm" scope="request">
			<input type="hidden" name="targetDefineName" value="<bean:write name='ObjForm' property='targetDefineName'/>">
			<input type="hidden" name="targetDefineId" value="<bean:write name='ObjForm' property='targetDefineId'/>">
			
		</logic:present>
		<logic:notPresent name="ObjForm" scope="request">
			<input type="hidden" name="targetDefineName" value="">
			<input type="hidden" name="targetDefineId" value="">
		
		</logic:notPresent>
	
		<!-- 预警设置串值 -->
		<input type="hidden" name="standard" value="">
	</form>
</body>
</html:html>
