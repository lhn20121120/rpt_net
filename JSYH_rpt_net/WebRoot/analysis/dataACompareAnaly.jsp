<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.security.Operator"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<%@ page import="java.util.GregorianCalendar"%>
<%@ page import="java.util.Calendar"%>

<%@ page import="com.cbrc.smis.form.AbnormityChangeForm,com.cbrc.smis.common.Config"%>


<%
	Operator operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
	String orgId = operator != null ? operator.getOrgId() : "";
	
	 GregorianCalendar calendar = new GregorianCalendar();
    

                //��ȡ��ݺ�����
                String year = String.valueOf(calendar.get(Calendar.YEAR));
                String term = String.valueOf(calendar.get(Calendar.MONTH) + 1);
%>
<jsp:useBean id="utilFormBean" scope="page" class="com.cbrc.smis.form.UtilForm" />
<jsp:setProperty property="orgId" name="utilFormBean" value="<%=orgId%>"/>
<jsp:useBean id="utilCellForm" scope="page" class="com.fitech.net.form.UtilCellForm" />
<jsp:setProperty property="childRepId" name="utilCellForm" value=" " />
<jsp:setProperty property="versionId" name="utilCellForm" value=" " />



<html:html locale="true">
<head>
	<html:base />
	<title>���ݶԱȷ���</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../css/common.css" type="text/css" rel="stylesheet">
	<script language="javascript" src="../js/comm.js"></script>
	<script language="javascript" src="../js/func.js"></script>
	<script language="javascript" src="<%=Config.WEBROOTULR%>/js/prototype-1.4.0.js"></script>
	<script language="javascript">
		<!--
		      
			/**
			 *�ָ���1
			 */
			 var SPLIT_SMYBOL_COMMA="<%=Config.SPLIT_SYMBOL_COMMA%>";
			 /**
			 *�ָ���2
			 */
			 var SPLIT_SMYBOL_ESP="<%=Config.SPLIT_SYMBOL_ESP%>";
			 
		       //�Ƿ�ѡ�б���
	    		var enableReport = false;
	         //�Ƿ���Ե�Ԫ��
	   		 var enableCell = false;
	   		 
	   		 var isAddRow = false;
			
			/**
			 *���ύ�¼�
			 */
			
			 
			/**
			 * �����и�
			 */
			 var rowHeight=25;
			/**
			 * ��������
			 */ 
			 var cols=6;
			/**
			 * ��ǰ��Ԫ�����
			 */
			 var curRow=1;
			/**
			 * ��ǰ��Ԫ�����
			 */
			 var curCol=2;
			 
			 var reportCellID="";
			 var reportCelltext="";
			/**
			 * ���ӱ�����
			 */
			 function _addRow(){			 
			 	var year = document.getElementById('year').value; 
			 	var term = document.getElementById('term').value;   
			 	var reportName = document.getElementById('reportName').value;   
			 	var cells= reportCelltext;   
				var cell= reportCellID;   
			 	if(enableReport==false){
				 		alert("��ѡ�񱨱�!");
				 		return;
				}
			 	if(changeValue(year,term,cells)){
				 	
				 	if(enableCell==false){
				 		alert("��ѡ��Ԫ��!");
				 		return;
				 	}

			 	 var objTbl=document.getElementById('tbl');
			 	 var objRow = objTbl.insertRow();  //������
			 	 objRow.height=rowHeight;
			 	 objRow.style.backgroundColor="#FFFFFF";
			 	 
			 	 var rows=objTbl.rows.length-2;
			 	 var index=1;
				 for(var i=0;i<cols;i++){
				 	  var objCell = objRow.insertCell();
				 	  objCell.style.textAlign="center";
				 	  switch (i) 
						{ 
						case 0 : objCell.innerText=rows;break; 
						case 1 : objCell.innerHTML="<td align='center'><INPUT class='input-text' readOnly  name='year' type='text' size='10' value='"+year+"'></TD>"; break; 
						case 2 : objCell.innerHTML="<td align='center'><INPUT class='input-text' readOnly  name='term' type='text' size='10' value='"+term+"'></TD>"; break;
						case 3 : objCell.innerHTML="<td align='center'><INPUT class='input-text' readOnly  name='repName' type='text' size='10' value='"+reportName+"'></TD>"; break;
						case 4 : objCell.innerHTML="<td align='center'><INPUT class='input-text'  type='hidden' name='cellID' type='text' size='10' value='"+cell+"'><INPUT class='input-text'  readOnly  name='reportcellID' type='text' size='10' value='"+cells+"'></TD>"; break;
						case 5 : objCell.innerHTML="<img src='../image/del.gif' border='0' onClick='_delRow(this)' style='cursor:hand'  title=\"ɾ��\">";
						} 
						index=index+1;
					 }
				 }
				 isAddRow=true;
				 cells="";
				 reportCelltext="";
			 }
		
			 function _submitFrm(){	

			     if(isAddRow==true){			     
					 var  acType = document.getElementsByName('compareType');
					var acompType="";
				 	for(var m =0; m < acType.length ; m ++)
					{
						 if(acType[m].checked==true){
							 acompType+=acType[m].value+",";
						 }	
					 }
				 	if(acompType==''){			 	
				 	 	 alert("��ѡ��ȶ�����!");
				 		return false;
				 	}
					var str=acompType+"#";
					 		var	sts =  document.getElementsByName('year');
							var	sts1 = document.getElementsByName('term');
					 		var	sts2 = document.getElementsByName('repname');
					 		var	sts3 = document.getElementsByName('cellID');
						for(var i =1; i < sts.length ; i ++)
						{
							
							 str += sts[i].value+","+sts1[i].value+","+sts2[i].value+","+sts3[i+1].value+"&";
							
						}
					
					document.getElementById('standard').value=str;
				}else{
					 alert("�����Ӽ�¼!");
				 		return false;
				}
			}
			 function changeValue(year,term,cellID){
				 if(year==""){
					alert("�����뱨��ʱ��!");
					return false;
				}
				if(term==""){
					alert("�����뱨��ʱ�䣡");
					return false;
				}
				if(isNaN(year)){ 
				   alert("��������ȷ�ı���ʱ�䣡"); 
				   return false; 
				}
				if(isNaN(term)){ 
				   alert("��������ȷ�ı���ʱ�䣡"); 
				   return false; 
				}
				if(term <1 || term >12){
					alert("��������ȷ�ı���ʱ�䣡");
					return false;
				}
				if(_trim(cellID)==""){
					alert("��ѡ��Ԫ��ID��");
					return false;
				}
				return true;
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
			  		//alert(e.description);
			  	}
			  }
	
			   
			   
			  /**
			   * ѡ�е�Ԫ���¼�
			   */
		
	    function changeCellID(cellObj){

	        var index=cellObj.selectedIndex;
			 var cellId=cellObj.options[index].value;	
			 var celltext=cellObj.options[index].text;
			 reportCelltext = celltext
			 reportCellID=cellId;
			 enableCell=true;
	    }
	    
	    // ����ѡ���¼�
		  function changeReport(reportObj){	 
		  	 var index=reportObj.selectedIndex;
			 var childRepId=reportObj.options[index].value;	
			 try
		 	 {
			  	var validateURL = "<%=request.getContextPath()%>/analysis/selectCellId.do?childRepId="+childRepId; 
			    var param = "radom="+Math.random();
			   	new Ajax.Request(validateURL,{method: 'post',parameters:param,onComplete:validateReportHandler,onFailure: reportError});
		   	}
		   	catch(e)
		   	{
		   		alert('ϵͳæ�����Ժ�����...��');
		   	}  
		   	enableReport=true;
		   	reportCellID="";
		}  
		
		var cellStr ;
		  function repl(s1,s2){    
					return this.replace(new RegExp(s1,"gm"),s2);    
		}  
		//У��Handler
		function validateReportHandler(request)
		{
			try
			{
				
				var result = request.responseXML.getElementsByTagName('result')[0].firstChild.data;
			//	var result=request.responseText;
				if(isEmpty(result)==false)  
				  {				  	
				    cellStr=result.replace(new RegExp('{',"gm"),'<');
					document.getElementById('cellId').innerHTML=cellStr;
				  }	
				  else 
				  {
					
					 alert('ϵͳæ�����Ժ�����...��');
				  }
			}
			catch(e)
			{}
	    }
	    
	    //ʧ�ܴ���
	    function reportError(request)
	    {
	        alert('ϵͳæ�����Ժ�����...��');
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
	<table border="0" width="98%" align="center">

		<tr>
			<td>
				��ǰλ�� >> ͳ�Ʒ���>> ���ݶԱȷ���
			</td>
		</tr>

	</table>
	
		<table cellspacing="0" cellpadding="0" border="0" width="96%" align="center">
		<html:form action="/selForseReportAgain" method="post">
			<tr>
				<td>
					<fieldset id="fieldset">
						
	
		<table border="0" width="96%">

			<TR bgcolor="#FFFFFF"  >

				<TD>
				
					���:
					<INPUT class="input-text" id="year" name="year" type="text" size="10" value="<%=year %>">&nbsp;&nbsp;
					����:
					<INPUT class="input-text" id="term" name="term" type="text" size="10" value="<%=term %>">&nbsp;&nbsp;
				</TD>
				</TR>
				<TR>
				<TD>	
					����:
					<html:select property="repName" styleId="reportName" onchange="changeReport(this)">
						<html:optionsCollection name="utilFormBean" property="FRrepList" />
					</html:select>
					
					��Ԫ��:
					<span id='cellId'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="button" value="���Ӽ�¼" class="input-button" onclick="_addRow()">
				</TD>

			</TR>
		</table>
			</fieldset>
				</td>
			</tr>
		</html:form>
	</table><br/>
	<html:form action="/selForseReportAgain" method="post">
		<TABLE cellSpacing="0" width="100%" border="0" align="center" cellpadding="4">
			<TR>
				<TD>
					<TABLE name="tbl" id="tbl" cellSpacing="1" cellPadding="1" width="96%" border="0" class="tbcolor" align="center"
						onclick="enterCell()">
						<tr class="titletab">
							<th colspan="8" align="center" id="list0">
								<strong>���ݶԱȷ���</strong>
							</th>
						</tr>

						<TR>
							<td width="6%" align="center" class="tableHeader">
								<b> ��� </b>
							</td>
							<TD width="15%" align="center" class="tableHeader">
								<b> ��� </b>
							</TD>
							<TD width="15%" align="center" class="tableHeader">
								<b> ���� </b>
							</TD>
							<TD width="32%" align="center" class="tableHeader">
								<b> �������� </b>
							</TD>


							<TD width="16%" align="center" class="tableHeader">
								<b> ��Ԫ�� </b>
							</TD>
							<TD width="16%" align="center" class="tableHeader">

							</TD>
						</TR>
					</TABLE>
				</TD>
			</TR>
		</TABLE>
		<table width="96%" border="0" cellpadding="4" cellspacing="1">
			<tr>
				<td>
					<input type="checkbox"  name='compareType' value='1'>
					������ &nbsp;&nbsp;
					<input type="checkbox"  name='compareType' value='2'>
					�������� &nbsp;&nbsp;
					<input type="checkbox"   name='compareType' value='3'>
					��ȥ��ͬ��
				</td>
			</tr>
		</table>

	</html:form>
	<form method="post" name="frm" action="<%=request.getContextPath()%>/analysis/viewCompare.do" onsubmit="return _submitFrm()">
		<table border="0" cellspacing="0" cellpadding="4" width="96%" align="center">
			<TR>
				<TD align="center">
					<input type="submit" value="�鿴�������" class="input-button">
					&nbsp;&nbsp;
					
					<input type="hidden" name="standard" value="">
				</TD>
			</TR>
		</table>


		<!-- �쳣�仯���ô�ֵ -->
		<logic:notPresent name="curPage" scope="request">
			<input type="hidden" name="curPage" value="0">
		</logic:notPresent>
	</form>
</body>
</html:html>
