<%@	page contentType="text/html;charset=gbk"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Date"%>
<%@ page import="com.cbrc.smis.hibernate.ReportIn"%>
<%@ page import="com.fitech.net.form.ETLReportForm"%>
<html:html locale="true"> 
<head> 
<link href="<%=request.getContextPath()%>/css/common.css" rel="stylesheet" type="text/css">
<SCRIPT language="javascript">
	function _submit(form){
		
			if(form.year.value==""){
					alert("请输入报表时间!");
			//		form.year.focus();
					
					return false;
				}
			if(form.month.value==""){
					alert("请输入报表时间！");
			//		form.term.focus();
					return false;
				}
			if(isNaN(form.year.value)){ 
				   alert("请输入正确的报表时间！"); 
			//	   form.year.focus(); 
				   return false; 
				}
			if(isNaN(form.month.value)){ 
				   alert("请输入正确的报表时间！"); 
			//	   form.term.focus(); 
				   return false; 
				}
			if(form.month.value <1 || form.month.value >12){
					alert("请输入正确的报表时间！");
			//		form.term.focus();
					return false;
				}
			
		  	return true;
		}
		function viewPdf(repInId){
				 window.location="<%=request.getContextPath()%>/servlets/toExcelServlet?repInId=" + repInId; 
			 }
	<%
		ETLReportForm etlForm=(ETLReportForm)request.getAttribute("form"); 
		String year="";
		String month="";
		String orgName="";
		String repName="";
		if(etlForm!=null){
			year=etlForm.getYear();
			month=etlForm.getMonth();
			orgName=etlForm.getOrgName();
			repName=etlForm.getRepName();
		}
	%>
	function _page(number){
		form.page.value=number;
		form.submit();
	}
</script>
</head>
<body>
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			
		</logic:greaterThan>
	</logic:present>
	<table border="0" width="98%" align="center">
		<tr>
			<td height="4"></td>
		</tr>
		<tr>
			<td>
				当前位置 >> 数据统计 >> ETL信息查询
			</td>
		</tr>
		<tr>
			<td height="4"></td>
		</tr>
	</table>
	<table cellspacing="0" cellpadding="0" border="0" width="98%"
		align="center">
	</table>
	<%
		String context=request.getContextPath(); 
	%>
	<table cellspacing="0" cellpadding="0" border="0" width="98%" align="center" class="tbcolor">
		<form  name="form" id="frm"  action="<%=context%>/etlReportFound.do" method="post"  onsubmit="return _submit(this)" >
			<tr>
				<td>
					<fieldset id="fieldset">
						<table cellspacing="0" cellpadding="0" border="0" width="100%" align="center">
							<tr>
								<td height="5"></td>
							</tr>
							<tr>
								<td height="25" width="30%">&nbsp;
									机构名称：
									

									 <div style="POSITION: absolute">
										<table cellSpacing=0 cellPadding=0 border=0>
											<tr>
												<td>			
									
																						
																								
													<input type="text" name="orgName" value="<%=orgName%>"  style="position: absolute;left:0px;top:0px;width:81px;">
													
												</td>
											</tr>
										</table>
									</div>
								</td>
								<td>
									报表名称：
									<input type="text" name="repName" size="25" styleClass="input-text" value="<%=repName%>"/>
								</td>
								<td>
									<input type="submit" value=" 查 询 " />
								</td>
								<td>
								</td>
							</tr>
						</table>
						<table cellspacing="0" cellpadding="0" border="0" width="100%" align="center">
							<tr>
								
								<td height="25">&nbsp;
									报表时间：
									<input name="year" maxlength="4" size="6" value="<%=year%>" class="input-text" />
									年
									<input name="month" maxlength="2" size="4" value="<%=month%>" class="input-text"/>
									月
								</td>
							</tr>
							<tr>
								<td height="3"></td>
							</tr>
						</table>
					</fieldset>
				</td>
			</tr>
			<input type="hidden" name="page" value="1" />
	</table>	
	<table width="98%" border="0" cellpadding="4" cellspacing="1"
			class="tbcolor">
			<tr class="titletab" id="tbcolor">
				<th height="24" align="center" id="list" colspan="10">
					ETL数据列表
				</th>
			</tr>
		<tr class="middle">
			<td align="center">编号</td>
		    <td align="center">报表名称</td>
		    <td align="center">报表时间</td>
		     <td align="center">机构名称</td>
		    
	    </tr>
	    <% 
	    	List list=(List)request.getAttribute("result");
	    	if(list!=null){
		    	for(int i=0;i<list.size();i++){
		    		ReportIn in=(ReportIn)list.get(i);
		    		String reportId=in.getMChildReport().getComp_id().getChildRepId();
		    		String name=in.getRepName();
		   // 		Integer check=new Integer(in.getCheckFlag().intValue());
		    		String orgId=in.orgName; 
		    		Date date=in.getReportDate();
		    		Integer inId=in.getRepInId();
	    %>
	    <tr  bgcolor="#FFFFFF">
	    <td align="center"><%=reportId%></td>
	 	 <td align="center">
	 	 <a href="javascript:viewPdf(<%=inId%>)"> 
	 	 <%=name%>
	 	 </a>
	 	 </td>
	 	 <td align="center"><%=date%></td>
	 	 <td align="center"><%=orgId%></td>
	 	 
	 	</tr>
	    <%
	    		}
	    	
	    %>
	    </table>
	    <table width="98%" border="0" align="center" cellpadding="2" cellspacing="1">
	    <tr >
	    	<%
	    			Integer nowPage=(Integer)request.getAttribute("page");
	    			Integer maxPage=(Integer)request.getAttribute("maxPage");
	    			int max=maxPage.intValue();
	    			int lastPage=max==0?max:(max-1)/10+1;
	    			int prePage=nowPage.intValue()==1?1:(nowPage.intValue()-1);
	    	//		(nowPage.intValue()-1>maxPage.intValue()?nowPage.intValue():nowPage.intValue()+1);
	    			int nextPage=nowPage.intValue()==lastPage?nowPage.intValue():(nowPage.intValue()+1);
	    			if(max==0){
	    	%>
	    			<td> 未找到条件下查询信息</td>
	    	<% 
	    			}
	    			else{
	    	%>	
	    	
	    		<td>当前第<span style="COLOR: #ff0000" >
	    	
	    		<%=nowPage%></span>页/共<span style="COLOR: #ff0000" ><%=lastPage%></span>页 &nbsp
	    		记录总数共<span style="COLOR: #ff0000" ><%=max%></span>条</td>
	    		<td align="right" >
	    		<a href="javascript:_page(1)" >首页</a>
	    		<a href="javascript:_page(<%=prePage%>)" >上一页</a>
	    		
	    	<%
	    				for(int i=0;i<lastPage;i++){
	    	%>
	    		 <a href="javascript:_page(<%=i+1%>)">
	    		<%
	    			if(nowPage.intValue()==(i+1)){ 
	    		%>
	    		
	    			<span style="COLOR: #ff0000" >
	    		<%
	    		 }
	    		%>
	    		
	    		
	    		<%=i+1%></span> </a>
	    			
	    	<%
	    				}
	    	%>
	    		<a href="javascript:_page(<%=nextPage%>)" >下一页</a>
	    		<a href="javascript:_page(<%=lastPage%>)" >末页</a>
	    		</td>
	    	<%
	    			} 
	    		}
	    	%>
	    	
	    </tr>
	<table>
</body>
</html:html>