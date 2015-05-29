<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.security.Operator"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<%@ page import="com.cbrc.smis.form.AbnormityChangeForm,com.cbrc.smis.common.Config"%>
<%@ page import="java.util.GregorianCalendar"%>
<%@ page import="java.util.Calendar"%>
<%
	Operator operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
	String orgId = operator != null ? operator.getOrgId() : "331010000";
	
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
	<script language="javascript" src="<%=request.getContextPath() %>/js/prototype-1.4.0.js"></script>
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
						case 1 : objCell.innerHTML="<td align='center'><INPUT class='input-text'  type='hidden' name='1' type='text' size='10' value='"+cells+"'><INPUT class='input-text' readOnly  name='year' type='text' size='10' value='"+year+"'></TD>"; break; 
						case 2 : objCell.innerHTML="<td align='center'><INPUT class='input-text'  type='hidden' name='2' type='text' size='10' value='"+cells+"'><INPUT class='input-text' readOnly  name='term' type='text' size='10' value='"+term+"'></TD>"; break;
						case 3 : objCell.innerHTML="<td align='center'><INPUT class='input-text'  type='hidden' name='repName' type='text' size='10' value='"+reportName+"'><INPUT class='input-text' readOnly  name='reportrepName' type='text' size='10' value='"+reportName+"'></TD>"; break;						
						case 4 : objCell.innerHTML="<td align='center'><INPUT class='input-text'  type='hidden' name='cellID' type='text' size='10' value='"+cells+"'><INPUT class='input-text'  readOnly  name='reportcellID' type='text' size='10' value='"+cells+"'></TD>"; break;
						case 5 : objCell.innerHTML="<td><img src='../image/del.gif' border='0' onClick='_delRow(this)' style='cursor:hand'  title=\"删除\"></td>";
						} 
						index=index+1;
					 }
				 }
				 isAddRow=true;
				 cells="";
				 reportCelltext="";
			 }


			//	查看趋势分析图
			 function viewAlanyMap(){

			  if(isAddRow==true){			     
				 var  startDate = document.getElementsByName('startDate');
				 var  endDate = document.getElementsByName('endDate');
				 var  startDateTerm = document.getElementsByName('startDateTerm');
				 var  endDateTerm = document.getElementsByName('endDateTerm');
				 
				 
				 var year1=startDate[0].value;
				 var year2=endDate[0].value;
				 var term1=startDateTerm[0].value;
				 var term2=endDateTerm[0].value;
				 if( changeDate(year1,year2,term1,term2) )
				 {
				 
				 
					  var	CHILD_REP_ID1="-1";
					  var	CELL_NAME1="-1";
					   var	CHILD_REP_ID2="-1";
					  var	CELL_NAME2="-1";
					   var	CHILD_REP_ID3="-1";
					  var	CELL_NAME3="-1";
					   var	sts2 = document.getElementsByName('repname');
					   var	sts3 = document.getElementsByName('cellID');
					   var j=1;
					   if(j<sts2.length)
						{
					      CHILD_REP_ID1=sts2[j].value ;
					  	  CELL_NAME1=sts3[j+1].value ;
						  j++;
						 
					  	}
	
					  	if(j<sts2.length)
						{
	
					      CHILD_REP_ID2=sts2[j].value ;
					  	  CELL_NAME2=sts3[j+1].value ;
						  j++;
					  	}
					  	if(j<sts2.length)
						{
		
					      CHILD_REP_ID3=sts2[j].value ;
					  	  CELL_NAME3=sts3[j+1].value ;
						  j++;
					  	}
			// 有问题, 
						 var url="<%=request.getContextPath()%>/ReportServer?reportlet=test.cpt&ORG_ID="+<%=orgId%>+"&START="+year1+term1+"&END="+year2+term2+"&REP_ID="+CHILD_REP_ID1+"&CELL_NAME1="+CELL_NAME1+"&CHILD_REP_ID2="+CHILD_REP_ID2+"&CELL_NAME2="+CELL_NAME2+"&CHILD_REP_ID3="+CHILD_REP_ID3+"&CELL_NAME3="+CELL_NAME3
					//	alert(url);
						window.open(url);
					}
				}else{
					 alert("请增加记录!");
				 		return false;
				}
			 
			 }
			  function changeDate(year1,year2,term1,term2){
				 
				 if(year1=="" || year2=="" || term1=="" ||  term2==""){
					alert("请输入报表时间!");
					return false;
				}
				if(isNaN(year1)){ 
				   alert("请输入正确的报表时间！"); 
				   return false; 
				}
				if(isNaN(term1)){ 
				   alert("请输入正确的报表时间！"); 
				   return false; 
				}
				if(isNaN(year2)){ 
				   alert("请输入正确的报表时间！"); 
				   return false; 
				}
				if(isNaN(term2)){ 
				   alert("请输入正确的报表时间！"); 
				   return false; 
				}
				if(term1 <1 || term1 >12){
					alert("请输入正确的报表时间！");
					return false;
				}
				if(term2 <1 || term2 >12){
					alert("请输入正确的报表时间！");
					return false;
				}
				if(year2-year1>2){
				 	alert("对不起,只支持分析二年内的数据!");
				 	return false;
				 }
				
				return true;
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
				当前位置 >> 统计分析>> 数据趋势分析
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
					
				</TD>
				<td>
				<input type="button" value="增加记录" class="input-button" onclick="_addRow()">
				</td>

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
				起始年份:&nbsp;
					<input type="text" id='startDate' size='8'  name='startDate' value="">
				期数:
					<input type="text" id='startDateTerm' size='8'  name='startDate' value="">
					 &nbsp;&nbsp; &nbsp;&nbsp; 
				结束年份:&nbsp;
					<input type="text"  id='endDate' size='8' name='endDate' value="">
				期数:
					<input type="text"  id='endDateTerm' size='8' name='endDate' value="">
				</td>
			</tr>
		</table>
	</html:form>
	<form method="post" name="frm" action="<%=request.getContextPath()%>/analysis/viewCompare.do">
		<table border="0" cellspacing="0" cellpadding="4" width="96%" align="center">
			<TR>
				<TD align="center">

					<input type="button" value="查看趋势分析图" class="input-button" onclick='viewAlanyMap()'>				
					&nbsp;&nbsp;
					<input type="hidden" name="standard" value="">
				</TD>
			</TR>
		</table>

	</form>
</body>
</html:html>
