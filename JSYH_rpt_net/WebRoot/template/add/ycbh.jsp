<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.form.AbnormityChangeForm,com.cbrc.smis.common.Config"%>
<jsp:useBean id="utilFormBean" scope="page" class="com.cbrc.smis.form.UtilForm" />
<%
	String select = "";

	if (request.getAttribute("ObjForm") != null) {
		AbnormityChangeForm form = (AbnormityChangeForm) request.getAttribute("ObjForm");
		if (form.getReportStyle().compareTo(Config.REPORT_STYLE_DD) == 0) { //点对点式
			utilFormBean.setInputCells(form.getChildRepId(), form.getVersionId());
			select = utilFormBean.getInputCellsSelect();
		} else if (form.getReportStyle().compareTo(Config.REPORT_STYLE_QD) == 0) { //清单式
			utilFormBean.setInputCols(form.getChildRepId(), form.getVersionId());
			select = utilFormBean.getInputColsSelect();
		}
	}
%>
<html:html locale="true">
<head>
	<html:base />
	<title>
		设定异常数据变化标准
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
			 * 机构详细的设定
			 *
			 * @param orgCls String 机构类别
			 * @param orgClsName String 机构类别名称
			 * @return void
			 */
			 function _set_detail(orgCls,orgClsName){
			   var objFrm=document.forms['frm'];
			   window.location="<%=request.getContextPath()%>/template/viewMOrg.do?orgClsId=" + orgCls + 
			    "&orgClsName=" + orgClsName +
			   	"&childRepId=" + objFrm.childRepId.value + 
			   	"&versionId=" + objFrm.versionId.value + 
			   	"&reportStyle=" + objFrm.reportStyle.value;
			 }
			 
			 /**
			  * 显示某机构类别下的用户已选择的机构列表
			  *
			  * @param orgCls String 机构类别
			  * @param orgClsName String 机构类别名称
			  * @return void
			  */
			  function _show_detail(orgCls,orgClsName){
			  	var objFrm=document.forms['frm'];
			    window.location="<%=request.getContextPath()%>/template/viewSelectedOrg.do?orgClsId=" + orgCls + 
			      "&orgClsName=" + orgClsName +
			   	  "&childRepId=" + objFrm.childRepId.value + 
			   	  "&versionId=" + objFrm.versionId.value + 
			   	  "&reportStyle=" + objFrm.reportStyle.value;
			  }
			  
			/**
			 *表单提交事件
			 */
			 function _submitFrm(){
			 
			 		var form=document.getElementById('frm');

			 		if(isEmpty(form.childRepId.value)==true || 
			 			isEmpty(form.versionId.value)==true ||
			 			isEmpty(form.reportStyle.value)==true){
			 			alert("获取需设定异常变化的报表信息失败，操作终止!\n");
			 			return false;
				 	}
			 	
			 		setCellValue();
			 
			 		
			 		
			 		var objTbl=document.getElementById("tbl");
			 		var _standard="";       //异常变化设定的标准
			 		for(var i=2;i<objTbl.rows.length;i++){
			 			var objCell=objTbl.rows[i].cells[1];
			 			if(isEmpty(objCell.innerText)==true){
			 				alert("请选择异常变化项!\n");
			 				focusCell(objCell);
			 				return false;
			 			}
			 			var _tmp=objCell.id;
			 			for(var j=2;j<cols-1;j++){
			 				var _cellValue=objTbl.rows[i].cells[j].innerText;
			 				_tmp+=(isEmpty(_tmp)==true?"":SPLIT_SMYBOL_COMMA) + _cellValue.replace("%","");
			 			}
			 			_standard+= _tmp + SPLIT_SMYBOL_ESP;
			 		}
			 		
			 		//alert(_standard);
			 		if(isEmpty(_standard)==true){
			 			alert("请设定异常变化标准!\n");
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
			 var cols=7;
			/**
			 * 当前单元格的行
			 */
			 var curRow=2;
			/**
			 * 当前单元格的列
			 */
			 var curCol=2;
			 
			 
			/**
			 * 增加表格的行
			 */
			 function _addRow(){
			 	 var objTbl=document.getElementById('tbl');

			 	 var objRow = objTbl.insertRow();  //增加行
			 	 objRow.height=rowHeight;
			 	 objRow.style.backgroundColor="#FFFFFF";
			 	 
			 	 var rows=objTbl.rows.length-2;
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
			  		alert(e.description);
			  	}
			  }
			  
			  /**
			   * 单元格取值
			   */
			   function setCellValue(){
			   	 var objTbl=document.getElementById("tbl");
			   	 if((curCol>0 && curCol<cols-1) && (curRow>1 && curRow<objTbl.rows.length)){
			   	 	 var objCell=objTbl.rows[curRow].cells[curCol];
						 if(curCol==1){
						 	 if(isEmpty(objCell.id)==false){
								 var _obj=document.getElementById('colName');	
								 objCell.innerText=_obj.options[_obj.selectedIndex].text;
							 }else{							 	
							 	 objCell.innerText="";
							 }
							 curCol=0;
						 }else{
							 var _obj=document.getElementById('standard');
							 if(typeof(_obj)!="undefined"){
							 	 objCell.innerText=(_checkNum(_obj.value)==false?"0":_obj.value) + "%";
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
			   var _SELECT="<%=select%>";
			   
			  /**
			   * 下一步事件
			   */
			   function _next(){
			   	 var form=document.forms['frm'];
			   	 window.location="<%=request.getContextPath()%>/template/add/bpfb.jsp?" + 
			   	 	"childRepId=" + form.childRepId.value + 
			   	 	"&versionId=" + form.versionId.value + 
			   	 	"&reportName=" + form.reportName.value +
			   	 	"&reportStyle=" + form.reportStyle.value;
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
									《<bean:write name="ObjForm" property="reportName"/>》
								</logic:present>
								数据异常变化标准设置
							</STRONG>
						</SPAN>
					</P>
				</span>
			</th>
		</tr>
		<tr>
			<td colspan="8" height="25" valign="middle">
					<a href="javascript:_setOrg()">请先设置数据异常变化所适用的机构类型或金融机构：</a>
			</td>
		</tr>
		<tr>
			<td colspan="8" bgcolor="#FFFFFF">
				<table border="0" cellpadding="4" cellspacing="0" width="100%">
					<logic:present name="OrgCls" scope="request">
						<bean:size name="OrgCls" id="Count" />
						<%int i = 0;%>
						<logic:iterate id="item" name="OrgCls" scope="request" indexId="index">
							<%if (i == 0) {
							%>
							<tr>
								<%}%>
								<td align="left">
									<logic:equal name="item" property="selAll" value="1">
										<img src="../../image/notselected.gif" border="0"><bean:write name="item" property="orgClsNm" />
									</logic:equal>
									<logic:equal name="item" property="selAll" value="0">
										<input type="checkbox" id="chb<bean:write name='index'/>" name="chb<bean:write name='index'/>" value="<bean:write name='item' property='orgClsId'/>">
										<a href="javascript:_set_detail('<bean:write name="item" property="orgClsId"/>','<bean:write name="item" property="orgClsNm"/>')">
											<bean:write name="item" property="orgClsNm" />
										</a>
									</logic:equal>
									&nbsp;
									<logic:equal name="item" property="selAll" value="1">
										<script language="javascript">
										  is_set_org_or_orgcl="1";
										</script>
										<a href="javascript:_show_detail('<bean:write name="item" property="orgClsId"/>','<bean:write name="item" property="orgClsNm"/>')">
											<img src="../../image/card.jpg" border="0" title="查看详细">
										</a>
									</logic:equal>
								</td>
								<%if (i == 2) {
				i = 0;

			%>
							</tr>
							<%} else {
				i += 1;
			}

			%>
						</logic:iterate>
						<input type="hidden" id="count" name="count" value="<%=Count==null?0:((Integer)Count).intValue()%>" />
						<%if (i == 1) {
				out.println("<td></td><td></td></tr>\n");
			}
			if (i == 2) {
				out.println("<td></td></tr>\n");
			}

		%>
					</logic:present>
				</table>
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
			<td width="6%" align="center" rowspan="2" class="tableHeader">
				<b>
					序号
				</b>
			</td>
			<TD width="20%" align="center" rowspan="2" class="tableHeader">
				<b>
					异常变化项目
				</b>
			</TD>
			<TD width="32%" align="center" colspan="2" class="tableHeader">
				<b>
					比上期
				</b>
			</TD>
			<TD width="32%" align="center" colspan="2" class="tableHeader">
				<b>
					比上年同期
				</b>
			</TD>
			<td rowspan="2" class="tableHeader" width="10%">
				&nbsp;
			<td>
		</TR>
		<TR>
			<TD width="16%" align="center" class="tableHeader">
				<b>
					上升标准
				</b>
			</TD>
			<TD width="16%" align="center" class="tableHeader">
				<b>
					下降标准
				</b>
			</TD>
			<TD width="16%" align="center" class="tableHeader">
				<b>
					上升标准
				</b>
			</TD>
			<TD width="16%" align="center" class="tableHeader">
				<b>
					下降标准
				</b>
			</TD>
		</TR>
		<TR bgcolor="#FFFFFF" height="25">
			<TD width="2%" align="center">
				1
			</TD>
			<TD width="14%" align="center"></TD>
			<TD width="14%" align="center">
				<INPUT class="input-text" id="standard" name="standard" type="text" size="10" value="">
				%
			</TD>
			<TD width="14%" align="center"></TD>
			<TD width="14%" align="center"></TD>
			<TD width="14%" align="center"></TD>
			<td align="center">
				<img src="../../image/del.gif" border="0" onclick="_delRow()" style="cursor:hand" title="删除">
			</td>
		</TR>
	</TABLE>

	<form name="frm" method="post" action="../saveYCBH.do" onsubmit="return _submitFrm()">
		<table border="0" cellspacing="0" cellpadding="4" width="96%" align="center">
			<TR>
				<TD align="center">
					<input type="button" value="增加规则" class="input-button" onclick="_addRow()">
					&nbsp;&nbsp;
					<input type="submit" value=" 保 存 " class="input-button">&nbsp;&nbsp;
					<input type="button" value=" 下一步 " class="input-button" onclick="_next()">
				</TD>
			</TR>
		</table>
		<logic:present name="ObjForm" scope="request">
			<input type="hidden" name="childRepId" value="<bean:write name='ObjForm' property='childRepId'/>">
			<input type="hidden" name="versionId" value="<bean:write name='ObjForm' property='versionId'/>">
			<input type="hidden" name="reportStyle" value="<bean:write name='ObjForm' property='reportStyle'/>">
			<input type="hidden" name="reportName" value="<bean:write name='ObjForm' property='reportName'/>">
		</logic:present>
		<logic:notPresent name="ObjForm" scope="request">
			<input type="hidden" name="childRepId" value="">
			<input type="hidden" name="versionId" value="">
			<input type="hidden" name="reportStyle" value="">
			<input type="hidden" name="reportName" value="">
		</logic:notPresent>
		<!-- 选择的机构类型列表 -->
		<input type="hidden" name="orgCls" value="">
		<!-- 异常变化设置串值 -->
		<input type="hidden" name="standard" value="">
	</form>
</body>
</html:html>
