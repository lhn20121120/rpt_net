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
    

                //获取年份和期数
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
	<title>数据对比分析</title>
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
			 *分隔符1
			 */
			 var SPLIT_SMYBOL_COMMA="<%=Config.SPLIT_SYMBOL_COMMA%>";
			 /**
			 *分隔符2
			 */
			 var SPLIT_SMYBOL_ESP="<%=Config.SPLIT_SYMBOL_ESP%>";
			 
		       //是否选中报表
	    		var enableReport = false;
	         //是否可以单元格
	   		 var enableCell = false;
	   		 
	   		 var isAddRow = false;
			
			/**
			 *表单提交事件
			 */
			
			 
			/**
			 * 表格的行高
			 */
			 var rowHeight=25;
			/**
			 * 表格的列数
			 */ 
			 var cols=6;
			/**
			 * 当前单元格的行
			 */
			 var curRow=1;
			/**
			 * 当前单元格的列
			 */
			 var curCol=2;
			 
			 var reportCellID="";
			 var reportCelltext="";
			/**
			 * 增加表格的行
			 */
			 function _addRow(){			 
			 	var year = document.getElementById('year').value; 
			 	var term = document.getElementById('term').value;   
			 	var reportName = document.getElementById('reportName').value;   
			 	var cells= reportCelltext;   
				var cell= reportCellID;   
			 	if(enableReport==false){
				 		alert("请选择报表!");
				 		return;
				}
			 	if(changeValue(year,term,cells)){
				 	
				 	if(enableCell==false){
				 		alert("请选择单元格!");
				 		return;
				 	}

			 	 var objTbl=document.getElementById('tbl');
			 	 var objRow = objTbl.insertRow();  //增加行
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
						case 5 : objCell.innerHTML="<img src='../image/del.gif' border='0' onClick='_delRow(this)' style='cursor:hand'  title=\"删除\">";
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
				 	 	 alert("请选择比对类型!");
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
					 alert("请增加记录!");
				 		return false;
				}
			}
			 function changeValue(year,term,cellID){
				 if(year==""){
					alert("请输入报表时间!");
					return false;
				}
				if(term==""){
					alert("请输入报表时间！");
					return false;
				}
				if(isNaN(year)){ 
				   alert("请输入正确的报表时间！"); 
				   return false; 
				}
				if(isNaN(term)){ 
				   alert("请输入正确的报表时间！"); 
				   return false; 
				}
				if(term <1 || term >12){
					alert("请输入正确的报表时间！");
					return false;
				}
				if(_trim(cellID)==""){
					alert("请选择单元格ID！");
					return false;
				}
				return true;
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
			  		//alert(e.description);
			  	}
			  }
	
			   
			   
			  /**
			   * 选中单元格事件
			   */
		
	    function changeCellID(cellObj){

	        var index=cellObj.selectedIndex;
			 var cellId=cellObj.options[index].value;	
			 var celltext=cellObj.options[index].text;
			 reportCelltext = celltext
			 reportCellID=cellId;
			 enableCell=true;
	    }
	    
	    // 报表选择事件
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
		   		alert('系统忙，请稍后再试...！');
		   	}  
		   	enableReport=true;
		   	reportCellID="";
		}  
		
		var cellStr ;
		  function repl(s1,s2){    
					return this.replace(new RegExp(s1,"gm"),s2);    
		}  
		//校验Handler
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
					
					 alert('系统忙，请稍后再试...！');
				  }
			}
			catch(e)
			{}
	    }
	    
	    //失败处理
	    function reportError(request)
	    {
	        alert('系统忙，请稍后再试...！');
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
				当前位置 >> 统计分析>> 数据对比分析
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
				
					年份:
					<INPUT class="input-text" id="year" name="year" type="text" size="10" value="<%=year %>">&nbsp;&nbsp;
					期数:
					<INPUT class="input-text" id="term" name="term" type="text" size="10" value="<%=term %>">&nbsp;&nbsp;
				</TD>
				</TR>
				<TR>
				<TD>	
					名称:
					<html:select property="repName" styleId="reportName" onchange="changeReport(this)">
						<html:optionsCollection name="utilFormBean" property="FRrepList" />
					</html:select>
					
					单元格:
					<span id='cellId'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="button" value="增加记录" class="input-button" onclick="_addRow()">
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
								<strong>数据对比分析</strong>
							</th>
						</tr>

						<TR>
							<td width="6%" align="center" class="tableHeader">
								<b> 序号 </b>
							</td>
							<TD width="15%" align="center" class="tableHeader">
								<b> 年份 </b>
							</TD>
							<TD width="15%" align="center" class="tableHeader">
								<b> 期数 </b>
							</TD>
							<TD width="32%" align="center" class="tableHeader">
								<b> 报表名称 </b>
							</TD>


							<TD width="16%" align="center" class="tableHeader">
								<b> 单元格 </b>
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
					比上期 &nbsp;&nbsp;
					<input type="checkbox"  name='compareType' value='2'>
					比上两期 &nbsp;&nbsp;
					<input type="checkbox"   name='compareType' value='3'>
					比去年同期
				</td>
			</tr>
		</table>

	</html:form>
	<form method="post" name="frm" action="<%=request.getContextPath()%>/analysis/viewCompare.do" onsubmit="return _submitFrm()">
		<table border="0" cellspacing="0" cellpadding="4" width="96%" align="center">
			<TR>
				<TD align="center">
					<input type="submit" value="查看分析结果" class="input-button">
					&nbsp;&nbsp;
					
					<input type="hidden" name="standard" value="">
				</TD>
			</TR>
		</table>


		<!-- 异常变化设置串值 -->
		<logic:notPresent name="curPage" scope="request">
			<input type="hidden" name="curPage" value="0">
		</logic:notPresent>
	</form>
</body>
</html:html>
