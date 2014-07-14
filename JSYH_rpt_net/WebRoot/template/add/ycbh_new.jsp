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
		if (form.getReportStyle().compareTo(Config.REPORT_STYLE_DD) == 0) { //��Ե�ʽ
			utilFormBean.setInputCells(form.getChildRepId(), form.getVersionId());
			select = utilFormBean.getInputCellsSelect();
		} else if (form.getReportStyle().compareTo(Config.REPORT_STYLE_QD) == 0) { //�嵥ʽ
			utilFormBean.setInputCols(form.getChildRepId(), form.getVersionId());
			select = utilFormBean.getInputColsSelect();
		}
	}
%>
<html:html locale="true">
<head>
	<html:base />
	<title>
		�趨�쳣���ݱ仯��׼
	</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" type="text/css" rel="stylesheet">
	<script language="javascript" src="../../js/comm.js"></script>
	<script language="javascript" src="../../js/func.js"></script>
	<script language="javascript">
		<!--
		    /**
		     * �Ƿ����趨�˻������������
		     */
		     var is_set_org_or_orgcl="0";
		     
			/**
			 *�ָ���1
			 */
			 var SPLIT_SMYBOL_COMMA="<%=Config.SPLIT_SYMBOL_COMMA%>";
			 /**
			 *�ָ���2
			 */
			 var SPLIT_SMYBOL_ESP="<%=Config.SPLIT_SYMBOL_ESP%>";
			 
			/**
			 * ������ϸ���趨
			 *
			 * @param orgCls String �������
			 * @param orgClsName String �����������
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
			  * ��ʾĳ��������µ��û���ѡ��Ļ����б�
			  *
			  * @param orgCls String �������
			  * @param orgClsName String �����������
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
			 *���ύ�¼�
			 */
			 function _submitFrm(){
			 		var form=document.getElementById('frm');

			 		if(isEmpty(form.childRepId.value)==true || 
			 			isEmpty(form.versionId.value)==true ||
			 			isEmpty(form.reportStyle.value)==true){
			 			alert("��ȡ���趨�쳣�仯�ı�����Ϣʧ�ܣ�������ֹ!\n");
			 			return false;
				 	}
			 		
			 		setCellValue();
			 		
			 		var objCount=document.getElementById("count");
			 		
			 		
			 		var objTbl=document.getElementById("tbl");
			 		var _standard="";       //�쳣�仯�趨�ı�׼
			 		for(var i=2;i<objTbl.rows.length;i++){
			 			var objCell=objTbl.rows[i].cells[1];
			 			if(isEmpty(objCell.innerText)==true){
			 				alert("��ѡ���쳣�仯��!\n");
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
			 			alert("���趨�쳣�仯��׼!\n");
			 			return false;
			 		}
			 		form.standard.value=_standard;
			 		
			 		
			 		var formObj=document.getElementById("frm1");
				  var box=formObj.elements['orgIds'];
				  var orgIds=">>";
				
					if(box!=null && box.length!=0)
					{
						for(var i=0;i<box.length;i++)
						{
						var v=box[i];
						
						  if (v.checked==true)
						        orgIds=orgIds+","+v.value;
						}
						
					}
					form.orgCls.value=orgIds;
				
			 		return true;
			 }
			 
			/**
			 * �����и�
			 */
			 var rowHeight=25;
			/**
			 * ��������
			 */ 
			 var cols=7;
			/**
			 * ��ǰ��Ԫ�����
			 */
			 var curRow=2;
			/**
			 * ��ǰ��Ԫ�����
			 */
			 var curCol=2;
			 
			 
			/**
			 * ���ӱ�����
			 */
			 function _addRow(){
			 	 var objTbl=document.getElementById('tbl');

			 	 var objRow = objTbl.insertRow();  //������
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
								objCell.innerHTML="<img src='../../image/del.gif' border='0' onClick='_delRow(this)' style='cursor:hand'  title=\"ɾ��\">";
							}else{
								objCell.innertText="&nbsp;";
							}
						}
				 }
			 }
			 
			 /**
			  * ɾ����
			  *
			  * @param rowIndex �к�
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
			   * ��Ԫ��ȡֵ
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
			   * ���Ԫ��
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
			   * ��ת��ָ���ĵ�Ԫ��
			   *
			   * @param rowIndex int ��Ԫ�����
			   * @param colIndex int ��Ԫ�����
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
			   * ѡ�е�Ԫ���¼�
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
			    * ������ѡ���¼�
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
			   	 	 alert("��ѡ������Ѿ������ˣ�������ѡ��!\n");
			   	 }
			   }
			   
			   var _TEXT="<input type=\"text\" id=\"standard\" name=\"standard\" value=\"\" class=\"input-text\" size=\"10\">%";
			   var _SELECT="<%=select%>";
			   
			   <logic:present name="JS" scope="request">
			   	<bean:write name="JS" scope="request" filter="false"/>
			   </logic:present>
			   <logic:present name="JSOrgCls" scope="request">
			    <bean:write name="JSOrgCls" scope="request" filter="false"/>
			   </logic:present>
			   
			  /**
			   * ��һ��
			   */ 
			   function _back(){
			   	 var form=document.forms['frm'];
			   	 window.location="<%=request.getContextPath()%>/template/add/bpfb.jsp?" + 
			   	 	"childRepId=" + form.childRepId.value + 
			   	 	"&versionId=" + form.versionId.value + 
			   	 	"&reportName=" + form.reportName.value +
			   	 	"&reportStyle=" + form.reportStyle.value;
			   }
			   /**
			    *�����������ҳ��
			    */
			   function _setOrg()
			   {
			   var objFrm=document.forms['frm'];
			   window.location="<%=request.getContextPath()%>/template/ycbh/PreEditYCBH.do?"+
			            "&childRepId=" + objFrm.childRepId.value + 
			        	"&versionId=" + objFrm.versionId.value + 
			        	"&reportStyle=" + objFrm.reportStyle.value;
	  		 			
			   }
			   //������һ������
			   function _NextOrg(childRepId,versionId,orgId)
			   {
			   
			   var formObj=document.getElementById("frm1");
				var box=formObj.elements['orgIds'];
				var select=false;
				if(box!=null && box.length!=0)
				{
					for(var i=0;i<box.length;i++)
					{
					var v=box[i];
					 if(v.value==orgId)
					  {
					  	
					    select=v.checked;
					    
					  }
					}
					
				}
				if(select)
				{
				var orgIds="";
				
				if(box!=null && box.length!=0)
				{
					for(var i=0;i<box.length;i++)
					{
					var v=box[i];
					
					  if (v.checked==true)
					        orgIds=orgIds+","+v.value;
					}
					
				}
			   window.location="<%=request.getContextPath()%>/template/ycbh/PAddYCBH.do?"+
			   			"&childRepId="+childRepId+
			   			"&versionId="+versionId+
			   			"&orgId="+orgId+
			   			"&curOrgId=-1"+
			   			"&orgIds="+orgIds;
			   	}
			    else
			    {
			      alert("����ѡ�и�ѡ��");
			    }
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
									��<bean:write name="ObjForm" property="reportName"/>��
								</logic:present>
								�����쳣�仯��׼����
							</STRONG>
						</SPAN>
					</P>
				</span>
			</th>
		</tr>
		<tr>
			<td colspan="8" height="25" valign="middle">
				�������������쳣�仯�����õĻ������ͻ���ڻ�����
			</td>
		</tr>
		<tr>
			<td colspan="8" bgcolor="#FFFFFF">
			<form method="post"  name="frm1" action="../ycbh/updateYCBH.do" >
				<table border="0" cellpadding="4" cellspacing="0" width="100%">
				<% int i=1;%>
				<logic:present name="lowerOrgList" scope="request">

			    <logic:iterate id="item" name="lowerOrgList">
						    <logic:notEmpty name="item" property="orgName">
						    
						    <%
						    	
							%>
							
		                    
						    <%
						    if((i % 3)==1){
						    %>
						    <tr>
								<td  align="left">
									<logic:equal name="item" property="preOrgId" value="true">
									<input type="checkbox" name="orgIds" value="<bean:write name="item" property="orgId"/>" checked/>
								</logic:equal>
								<logic:notEqual name="item" property="preOrgId" value="true">
									<input type="checkbox" name="orgIds" value="<bean:write name="item" property="orgId"/>" />
								</logic:notEqual>
									<a href="javascript:_NextOrg('<bean:write name="ObjForm" property="childRepId"/>','<bean:write name="ObjForm" property="versionId"/>','<bean:write name="item" property="orgId"/>')"><bean:write name="item" property="orgName"/></a>
								</td>
							
							<%
							}else{
							%>
							<td  align="left">
							    <logic:equal name="item" property="preOrgId" value="true">
									<input type="checkbox" name="orgIds" value="<bean:write name="item" property="orgId"/>" checked/>
								</logic:equal>
								<logic:notEqual name="item" property="preOrgId" value="true">
									<input type="checkbox" name="orgIds" value="<bean:write name="item" property="orgId"/>" />
								</logic:notEqual>
								
								<a href="javascript:_NextOrg('<bean:write name="ObjForm" property="childRepId"/>','<bean:write name="ObjForm" property="versionId"/>','<bean:write name="item" property="orgId"/>')"><bean:write name="item" property="orgName"/></a>
							</td>
							<%}%>
							</logic:notEmpty>
							<%if((i % 3)==0){%>
						
							<%}%>
							<logic:empty name="item" property="orgName">
							<%
							for(int j=1;j<=(3-((i-1)%3));j++){
							%>
							<td></td>
							<%
							}
							%>
							
							</logic:empty>
						<%
						i=i+1;
						%>
			    	</logic:iterate>
			    	</logic:present>
				</table>
				</form>
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
					���
				</b>
			</td>
			<TD width="20%" align="center" rowspan="2" class="tableHeader">
				<b>
					�쳣�仯��Ŀ
				</b>
			</TD>
			<TD width="32%" align="center" colspan="2" class="tableHeader">
				<b>
					������
				</b>
			</TD>
			<TD width="32%" align="center" colspan="2" class="tableHeader">
				<b>
					������ͬ��
				</b>
			</TD>
			<td rowspan="2" class="tableHeader" width="10%">
				&nbsp;
			<td>
		</TR>
		<TR>
			<TD width="16%" align="center" class="tableHeader">
				<b>
					������׼
				</b>
			</TD>
			<TD width="16%" align="center" class="tableHeader">
				<b>
					�½���׼
				</b>
			</TD>
			<TD width="16%" align="center" class="tableHeader">
				<b>
					������׼
				</b>
			</TD>
			<TD width="16%" align="center" class="tableHeader">
				<b>
					�½���׼
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
				<img src="../../image/del.gif" border="0" onclick="_delRow()" style="cursor:hand" title="ɾ��">
			</td>
		</TR>
	</TABLE>

	<form method="post"  name="frm" action="../../template/saveYCBH.do" onsubmit="return _submitFrm()">
		<table border="0" cellspacing="0" cellpadding="4" width="96%" align="center">
			<TR>
				<TD align="center">
					<input type="button" value="���ӹ���" class="input-button" onclick="_addRow()">
					&nbsp;&nbsp;
					<input type="submit" value=" �� �� " class="input-button">&nbsp;&nbsp;
					<input type="button" value=" ��һ�� " class="input-button" onclick="_back()">
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
		<!-- ѡ��Ļ��������б� -->
		<input type="hidden" name="orgCls" value="">
		<!-- �쳣�仯���ô�ֵ -->
		<input type="hidden" name="standard" value="">
		<logic:present name="curPage" scope="request">
		<input type="hidden" name="curPage" value="<bean:write name='curPage' scope='request'/>">
		</logic:present>
		<logic:notPresent name="curPage" scope="request">
		<input type="hidden" name="curPage" value="0">
		</logic:notPresent>
	</form>
	<script language="javascript">
		if(typeof(arrAC)!="undefined" && arrAC!=null && arrAC.length>0){
			var objTbl=document.getElementById('tbl');
			objTbl.rows[2].cells[1].id=arrAC[0].id;
			objTbl.rows[2].cells[1].innerText=arrAC[0].fieldName;
			objTbl.rows[2].cells[2].innerText=arrAC[0].prevFallStandard + "%";
			objTbl.rows[2].cells[3].innerText=arrAC[0].prevRiseStandard + "%";
			objTbl.rows[2].cells[4].innerText=arrAC[0].sameFallStandard + "%";
			objTbl.rows[2].cells[5].innerText=arrAC[0].sameRiseStandard + "%";
			document.getElementById("standard").value=arrAC[0].prevFallStandard;
			for(var i=1;i<arrAC.length;i++){
				 var objRow = objTbl.insertRow();  //������
			 	 objRow.height=rowHeight;
			 	 objRow.style.backgroundColor="#FFFFFF";
			 	 
			 	 var rows=objTbl.rows.length-2;
				 for(var j=0;j<cols;j++){
				 	  var objCell = objRow.insertCell();
				 	  objCell.style.textAlign="center";
						if(j==0){
							objCell.innerText=rows;
						}else{
							if(j==(cols-1)){
								objCell.innerHTML="<img src='../../image/del.gif' border='0' onClick='_delRow(this)' style='cursor:hand'  title=\"ɾ��\">";
							}
						}
					}
					objTbl.rows[2+i].cells[1].id=arrAC[i].id;
					objTbl.rows[2+i].cells[1].innerText=arrAC[i].fieldName;
					objTbl.rows[2+i].cells[2].innerText=arrAC[i].prevFallStandard + "%";
					objTbl.rows[2+i].cells[3].innerText=arrAC[i].prevRiseStandard + "%";
					objTbl.rows[2+i].cells[4].innerText=arrAC[i].sameFallStandard + "%";
					objTbl.rows[2+i].cells[5].innerText=arrAC[i].sameRiseStandard + "%";
			}
		}

	</script>
</body>
</html:html>
