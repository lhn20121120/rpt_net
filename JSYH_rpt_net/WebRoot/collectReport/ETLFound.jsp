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
					alert("�����뱨��ʱ��!");
			//		form.year.focus();
					
					return false;
				}
			if(form.month.value==""){
					alert("�����뱨��ʱ�䣡");
			//		form.term.focus();
					return false;
				}
			if(isNaN(form.year.value)){ 
				   alert("��������ȷ�ı���ʱ�䣡"); 
			//	   form.year.focus(); 
				   return false; 
				}
			if(isNaN(form.month.value)){ 
				   alert("��������ȷ�ı���ʱ�䣡"); 
			//	   form.term.focus(); 
				   return false; 
				}
			if(form.month.value <1 || form.month.value >12){
					alert("��������ȷ�ı���ʱ�䣡");
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
				��ǰλ�� >> ����ͳ�� >> ETL��Ϣ��ѯ
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
									�������ƣ�
									

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
									�������ƣ�
									<input type="text" name="repName" size="25" styleClass="input-text" value="<%=repName%>"/>
								</td>
								<td>
									<input type="submit" value=" �� ѯ " />
								</td>
								<td>
								</td>
							</tr>
						</table>
						<table cellspacing="0" cellpadding="0" border="0" width="100%" align="center">
							<tr>
								
								<td height="25">&nbsp;
									����ʱ�䣺
									<input name="year" maxlength="4" size="6" value="<%=year%>" class="input-text" />
									��
									<input name="month" maxlength="2" size="4" value="<%=month%>" class="input-text"/>
									��
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
					ETL�����б�
				</th>
			</tr>
		<tr class="middle">
			<td align="center">���</td>
		    <td align="center">��������</td>
		    <td align="center">����ʱ��</td>
		     <td align="center">��������</td>
		    
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
	    			<td> δ�ҵ������²�ѯ��Ϣ</td>
	    	<% 
	    			}
	    			else{
	    	%>	
	    	
	    		<td>��ǰ��<span style="COLOR: #ff0000" >
	    	
	    		<%=nowPage%></span>ҳ/��<span style="COLOR: #ff0000" ><%=lastPage%></span>ҳ &nbsp
	    		��¼������<span style="COLOR: #ff0000" ><%=max%></span>��</td>
	    		<td align="right" >
	    		<a href="javascript:_page(1)" >��ҳ</a>
	    		<a href="javascript:_page(<%=prePage%>)" >��һҳ</a>
	    		
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
	    		<a href="javascript:_page(<%=nextPage%>)" >��һҳ</a>
	    		<a href="javascript:_page(<%=lastPage%>)" >ĩҳ</a>
	    		</td>
	    	<%
	    			} 
	    		}
	    	%>
	    	
	    </tr>
	<table>
</body>
</html:html>