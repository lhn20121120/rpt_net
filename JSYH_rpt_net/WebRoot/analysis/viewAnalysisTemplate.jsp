<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.common.Config" %>
<%@ page import="com.cbrc.smis.security.Operator" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.HashSet"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="com.fitech.net.form.AAnalysisTPForm"%>

<jsp:useBean id="utilFormBean" scope="page" class="com.cbrc.smis.form.UtilForm" />
<jsp:setProperty property="type" name="utilFormBean" value="view"/>
<%
	Operator operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
	String orgId = operator != null ? operator.getOrgId() : "";
	List list = (List)request.getAttribute("Records");
	HashMap map=(HashMap)request.getAttribute("MAP");
%>
<jsp:useBean id="utilSubOrgForm" scope="page" class="com.fitech.net.form.UtilSubOrgForm" />
<jsp:setProperty property="orgId" name="utilSubOrgForm" value="<%=orgId%>"/>
<jsp:useBean id="utilForm" scope="page" class="com.cbrc.smis.form.UtilForm" />
<jsp:useBean id="utilOrgForm" scope="page" class="com.cbrc.fitech.org.UtilForm" />

<script language="javascript">	
	var SPLIT_SYMBOL_COMMA="<%=Config.SPLIT_SYMBOL_COMMA%>";
	function _check(){
		if(form.year.value==""){
			alert("请输入报表时间!");
			form.year.focus();
			return false;
		}
		if(form.term.value==""){
			alert("请输入报表时间！");
			form.term.focus();
			return false;
		}
		if(isNaN(form.year.value)){ 
			alert("请输入正确的报表时间！"); 
			form.year.focus(); 
			return false; 
		}
		if(isNaN(form.term.value)){ 
		   alert("请输入正确的报表时间！"); 
		   form.term.focus(); 
		   return false; 
		}
		if(form.term.value <1 || form.term.value >12){
			alert("请输入正确的报表时间！");
			form.term.focus();
			return false;
		}	
		return true;	
	}	

	/**
	 * 下载单个数据文件
	 */
	function _viewReportData(ATId){		
		var year = form.year.value;
		var term = form.term.value;
		var orgId= document.getElementById("orgId");
		var dataRangeId= document.getElementById("dataRangeId");
		var curId= document.getElementById("curId");
		
		var orgIdvalue;
		var dataRangeIdvalue;
		var curIdvalue;
		
		//机构代码： ORG_ID
		//币种：CUR_ID
		//口径：DATA_RANGE_ID
		//年：YEAR
		//月：TERM
		//上一季：TERM1	 YEART1			
		//上两季：TERM2  YEART2
		//上三季TERM3  YEART3
		//去年：YEAR1
		//前年：YEAR2
		//上一月：YEAR11,TERM11
		//上二月：YEAR12,TERM12
	    //上四月: YEAR14,TERM14
		//上五月: YEAR15,TERM15
		var term1;
		var term2;
		var term3;
		var yearT1;
		var yearT2;
		var yearT3;
		var year1=year-1;
		var year2=year-2;
		var YEAR11,TERM11;
		var YEAR12,TERM12;
		var YEAR14,TERM14;
		var YEAR15,TERM15;
		if(term<'3' && term!=12 ){
			term1=9;
			yearT1=year-1;
			term2=6;
			yearT2=year-1;
			term3=3;
			yearT3=year-1;
			if(term=='1'){
				YEAR11=year-1;
				TERM11=12;
				YEAR12=year-1;
				TERM11=11;
				YEAR14=year-1;
				TERM14=9;
				YEAR15=year-1;
				TERM15=8;
				
			}
			if(term=='2'){
				YEAR11=year;
				TERM11=1;
				YEAR12=year-1;
				TERM11=12;
				YEAR14=year-1;
				TERM14=10;
				YEAR15=year-1;
				TERM15=9;
			}
		}else if(term<'6' && term>='3'){
			term1=12;
			yearT1=year-1;
			term2=9;
			yearT2=year-1;
			term3=6;
			yearT3=year-1;
			YEAR11=year;
			TERM11=term-1;
			YEAR12=year;
			TERM11=term-2;
			if(term == '3'){
				YEAR14=year-1;
				TERM14=11;
				YEAR15=year-1;
				TERM15=10;
			}
			if(term == '4'){
				YEAR14=year-1;
				TERM14=12;
				YEAR15=year-1;
				TERM15=11;
			}if(term == '5'){
				YEAR14=year;
				TERM14=1;
				YEAR15=year-1;
				TERM15=12;
			}
			
		}else if(term<'9' && term>='6'){
			term1=3;
			yearT1=year;
			term2=12;
			yearT2=year-1;
			term3=9;
			yearT3=year-1;
			YEAR11=year;
			TERM11=term-1;
			YEAR12=year;
			TERM11=term-2;
			YEAR14=year;
			TERM14=term-4;
			YEAR15=year;
			TERM15=term-5;
		}else if(term<'12' && term>='9'){
			term1=6;
			yearT1=year;
			term2=3;
			yearT2=year;
			term3=12;
			yearT3=year-1;
			YEAR11=year;
			TERM11=term-1;
			YEAR12=year;
			TERM11=term-2;
			YEAR14=year;
			TERM14=term-4;
			YEAR15=year;
			TERM15=term-5;
		}else if( term=='12' ){
			term1=9;
			yearT1=year;
			term2=6;
			yearT2=year;
			term3=3;
			yearT3=year;
			YEAR11=year;
			TERM11=term-1;
			YEAR12=year;
			TERM11=term-2;
			YEAR14=year;
			TERM14=term-4;
			YEAR15=year;
			TERM15=term-5;
		}
		if(_check()){
		
			for(var i=0;i<orgId.length;i++){
		  		if(orgId.options[i].selected ){		
		  			orgIdvalue=orgId.options[i].value;
		  			break;
		  		}
		  	}	
		  	for(var j=0;j<dataRangeId.length;j++){
		  		if(dataRangeId.options[j].selected ){		
		  			dataRangeIdvalue=dataRangeId.options[j].value;
		  			break;
		  		}
		  	}	
		  	for(var k=0;k<curId.length;i++){
		  		if(curId.options[k].selected ){		
		  			curIdvalue=curId.options[k].value;
		  			break;
		  		}
		  	}	
			var url="<%=request.getContextPath()%>/ReportServer?reportlet="+ATId+".cpt&ORG_ID="+orgIdvalue+"&CUR_ID="+curIdvalue+"&DATA_RANGE_ID="+dataRangeIdvalue+"&YEAR="+year+"&TERM="+term+"&YEART1="+yearT1+"&TERM1="+term1+"&YEART2="+yearT2+"&TERM2="+term2+"&YEART3="+yearT3+"&TERM3="+term3+"&YEAR1="+year1+"&YEAR2="+year2+"&TERM11="+TERM11+"&YEAR11="+YEAR11+"&YEAR12="+YEAR12+"&TERM12="+TERM12+"&YEAR14="+YEAR14+"&TERM14="+TERM14+"&YEAR15="+YEAR15+"&TERM15="+TERM15;
			//alert(url);
			window.open(url);
		}else{
			return;
		}
		
	
	}
		function viewAllData(){
			if(confirm("是否批量生成所有分析报表?\n")==true){
				alert("生成成功!");
			}
		}
		
		//删除提示
	     function _delete(ATId)
	     {
	     	if(confirm("你确认要删除该分析报表吗？"))
	     		window.location = "<%=request.getContextPath()%>/analysis/deleteAnalysisTemplate.do?ATId="+ATId;	
	     		
	     }
</script>

<html:html locale="true">
	<head>
		<html:base/>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../css/common.css" type="text/css" rel="stylesheet">
		<script language="javascript" src="../js/func.js"></script>
		
		<logic:present name="Message" scope="request">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:present>
	
	</head>
	<body style="TEXT-ALIGN: center">
		<table border="0" width="90%" align="center">
			<tr><td height="3"></td></tr>
			<tr>
				<td>
					当前位置 >> 统计分析 >> 分析数据查看
				</td>
			</tr>
		</table>
		<table cellSpacing="0" cellPadding="4" width="100%" border="0" align="center">
			<tr>
				<td>
					<fieldset id="fieldset">	
						<html:form action="/analysis/viewAnalysisTemplate.do" method="post" styleId="frmChk1" onsubmit="return _submit(this)">
							<table cellSpacing="0" cellPadding="4" width="100%" border="0" align="center" height="5">	
								<tr>
									<td height="3"> 
										报表名称：
										<html:text property="ATName" size="30" maxlength="50" styleClass="input-text" styleId="ATName"/>
									 </td>	
									<TD>
										模板类型：
										<html:select property="analyTypeID" ><html:optionsCollection name="utilFormBean" property="analysisTPType"/>
										</html:select>
									</TD>
								
									 <td>
									 	<html:submit styleClass="input-button" value=" 查 询 " />
									 </td>
								 </tr>							
							</table>
						</html:form>
					 </fieldset>		
				</td>
				</tr>				
		</table>
		<html:form action="/analysis/viewAnalysisTemplate.do" method="post" styleId="form" >
		<table cellSpacing="0" cellPadding="4" width="100%" border="0" align="center">
			<tr>
				<td>
					<fieldset id="fieldset1">					
						<table cellSpacing="0" cellPadding="6" width="100%" border="0" align="center">				
							<tr>							
								<td>
									时间：
									<input type="text" name="year" id="year" maxlength="4" size="6" value="2007" class="input-text">
									年
									<input type="text" name="term" id="term" maxlength="2" size="4" value="12" class="input-text">
									月
<%--									<html:text property="year" maxlength="4" size="4" styleClass="input-text" />--%>
<%--									&nbsp;年--%>
<%--									<html:text property="month" maxlength="2" size="2" styleClass="input-text" />--%>
<%--									&nbsp;期数--%>
									
									
								</td>
								<td  > 
									机构：
									<html:select property="orgId">	
									<logic:notEmpty name="utilOrgForm" property="orgList">							
										<html:optionsCollection name="utilOrgForm" property="orgList"/>
									</logic:notEmpty>
									</html:select>
								</td>		
								<td>
								口径：
									<html:select property="dataRangeId">	
									<logic:notEmpty name="utilForm" property="dataRgTypes">							
										<html:optionsCollection name="utilForm" property="dataRgTypes"/>
									</logic:notEmpty>
									</html:select>
								</td>	
								<td>
								币种：
								<html:select property="curId">	
									<logic:notEmpty name="utilForm" property="curIds">							
										<html:optionsCollection name="utilForm" property="curIds"/>
									</logic:notEmpty>
									</html:select>
								</td>
						
							</tr>
							<tr>
							<td>
									<input type='button' class='input-button' onClick='viewAllData()' value='批量生成分析报表'/>
								</td>
							</tr>				
						</table>		
					</fieldset>		
				</td>
				</tr>				
		</table>
		</html:form>
		<html:form action="/analysis/viewAnalysisTemplate.do" method="POST" styleId="frmChk">						
			<TABLE cellSpacing="0" width="100%" border="0" align="center" cellpadding="4">					
				<TR>
					<TD>
						<TABLE cellSpacing="1" cellPadding="4" width="100%" border="0" class="tbcolor">
							<tr class="titletab">
								<th colspan="8" align="center" id="list"><strong>模板列表</strong></th>
							</tr>
							<TR class="middle">
								<TD class="tableHeader" width="10%">报表类型</td>	
								<TD class="tableHeader" width="10%">报表编号</td>	
								<TD class="tableHeader" width="25%">报表名称</TD>		
								<TD class="tableHeader" width="10%">操作</td>																	
							</TR>
<%--							<html:analysisReportTag reportList="<%=list%>" />--%>
							<logic:present name="Records" scope="request">
																					
							
							<%
							List reportList=list;
							// hashSet  存放过滤后的typeId, 取不相同的值
							HashSet hashSet = new HashSet();
							 for(Iterator iterator=reportList.iterator();iterator.hasNext();){
								AAnalysisTPForm aTPForm =(AAnalysisTPForm)iterator.next();
								hashSet.add(aTPForm.getAnalyTypeID());
							}	
					
					for (Iterator it = hashSet.iterator(); it.hasNext();){
						Integer key=(Integer)it.next();
						boolean flag = true;
						boolean ff = true;
						for(Iterator iter=reportList.iterator();iter.hasNext();){
							AAnalysisTPForm aTPForm =(AAnalysisTPForm)iter.next();
							Integer typeId=aTPForm.getAnalyTypeID();
							// flag  判断是第几次出现，如第一次要合并单元格
							if(typeId.intValue()==key.intValue() && flag){
							//map 存放分析报表的类型和这个类型的记录数量键值对
								Integer count=(Integer)map.get(key);
									%>
											  		<TR bgcolor='#FFFFFF'>
													<td align='center' rowspan=<%=count%> ><%=aTPForm.getAnalyTypeName() %></td>
													<td align='center'><%=aTPForm.getATId() %></td>
													<td align='center'><%=aTPForm.getATName() %></td>
													<td align='center'>
														<input type='button' class='input-button' onClick='_viewReportData("<%=aTPForm.getATId()%>")' value='查 看'/>
														&nbsp;&nbsp;
														<input type='button' class='input-button' onClick='_delete("<%=aTPForm.getATId()%>")' value='删 除'/>
	
													</td></TR>
								<%		
								
									flag=false;								
							}else if(typeId.intValue()==key.intValue()){
						 %>
								
								  		<TR bgcolor='#FFFFFF'>										
										<td align='center'><%=aTPForm.getATId() %></td>
										<td align='center'><%=aTPForm.getATName() %></td>
										<td align='center'>
										<input type='button' class='input-button' onClick='_viewReportData("<%=aTPForm.getATId()%>")' value='查 看'/>
										&nbsp;&nbsp;
										<input type='button' class='input-button' onClick='_delete("<%=aTPForm.getATId()%>")' value='删 除'/>
										</td></TR>						
					<%		}
							
	
						}		
					}
							 %>
								</logic:present>									
							<logic:notPresent name="Records" scope="request">
								<tr bgcolor="#FFFFFF">
									<td colspan="8">暂无记录</td>
								</tr>
							</logic:notPresent>
						</TABLE>					
					</TD>
				</TR>
			</TABLE>	
		</html:form>	
	</body>
</html:html>
