<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<jsp:directive.page import="com.cbrc.smis.dao.DBConn"/>
<jsp:directive.page import="java.sql.Connection"/>
<jsp:directive.page import="java.sql.Statement"/>
<jsp:directive.page import="java.sql.ResultSet"/>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<html:html locale="true">
<head>
	<html:base/>
		<title>ETL���ݷ�Χ</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../../css/common.css" rel="stylesheet" type="text/css">
	
	<script language="javascript">
		//���ӱ���Ȩ��
		function addReport(){
			//ȫ�������б��
			var allReportList = document.getElementById('allReport');			
			//ѡ�б����б��
			var selectReportList= document.getElementById('selectReport');			
			//���б����б���е���Ŀ
			var allReportListOptions = allReportList.options;			
			//ѡ�б����б���е���Ŀ
			var selectReportOptions = selectReportList.options;			
			for(var i=0; i<allReportListOptions.length; i++){
				if(allReportList.options[i].selected){
					//�鿴�Ƿ��Ѿ�ѡ�й�
					var isExit = false; 
					for (var j=0;j<selectReportOptions.length;j++){
						if(allReportList.options[i].value==selectReportList.options[j].value)
						    isExit =true;
				    }
					if(isExit==false){
						selectReportList.options[selectReportList.length] = new Option(allReportList.options[i].text,allReportList.options[i].value);
			    		allReportList.options[i].style.color ="gray";
			    	}
			    }					
			}
		}
	
		//ɾ������Ȩ��
		function delReport(){
			//ȫ�������б��
			var allReportList = document.getElementById('allReport');			
			//ѡ�б����б��
			var selectReportList= document.getElementById('selectReport');			
			//���б����б���е���Ŀ
			var allReportListOptions = allReportList.options;			
			//ѡ�б����б���е���Ŀ
			var selectReportOptions = selectReportList.options;						
			var optionLen = selectReportOptions.length;			
			var offset = 0; 			
			for(var i=0; i<optionLen; i++){
				if(selectReportList.options[i-offset].selected){
					//�ı�ȫ�����ܿ��ж�Ӧ����ɫ
					for(var j=0; j<allReportListOptions.length; j++){
						if(allReportList.options[j].value==selectReportList.options[i-offset].value)
							allReportList.options[j].style.color ="black";
					}					
					selectReportList.remove(i-offset);		
					offset++;
				}
			}
		}
		
		//����--�ύ����
		function submitData(){
			//ѡ�б����б��
			var selectReportList= document.getElementById('selectReport');
			//ѡ�й����б���е���Ŀ
			var selectReportListOptions = selectReportList.options;
			//��ȡѡ������ݣ��������ӳ��ַ�����֮���á�,����
			var selectData = "";
			var repVersion = "";
			for(var i=0; i<selectReportListOptions.length; i++){ 				
				selectData += (selectReportListOptions.options[i].value + ",");
			}
			selectData = selectData.substring(0,selectData.lastIndexOf(","));
				
			document.form1.tarIds.value = selectData;			
			document.form1.submit();			
		}
		//�����ϼ��˵�
		function back(){
			window.location="<%=request.getContextPath()%>/viewOrgNet.do?back=_back";			
		}
	</script>
	<%
		DBConn conn=new DBConn();
	
		Connection connection=conn.beginTransaction().connection();
		Statement st=connection.createStatement();
		ResultSet rs=null;
		String tarIds=request.getParameter("tarIds");
		String orgId=request.getParameter("orgId");
		
		if(tarIds!=null&&orgId!=null){
			try{
				String[] tarId=tarIds.split(",");
				st.addBatch("delete from EXCEL_ORG where SRC_ID='"+orgId+"'");
				for(int i=0;i<tarId.length&&!tarId[i].equals("");i++){
					st.addBatch("insert into EXCEL_ORG (EO_ID,SRC_ID,TAR_ID) values(nextval for SEQ_REPORT_IN,'"+orgId+"','"+tarId[i]+"')");
				}
			
				st.executeBatch();
				connection.commit();
			}catch(Exception e){
				e.printStackTrace();
			}
		
		}
		
		
		String orgName="";
		if(orgId!=null){
			rs=st.executeQuery("select ORG_NAME from ORG where ORG_ID='"+orgId+"'");
			if(rs.next()){
				orgName=rs.getString(1);
			}
		}
		
		
	%>
</head>

<body>
	<br>
	<table width="80%" border="0" align="center">
		<tr>
			<td align="center">
				��������: &nbsp;
				<%=orgName %>
			</td>
		</tr>
		<tr>
			<td >
				<div id=location> 
                 <div align="left">
                 	<strong>ETL���ݷ�Χ�趨</strong>
                 </div>
                </div>
			</td>
		</tr>
	</table>
	<form action="ETLOrg.jsp" method="post" id="form1" name="form1">
		<input type="hidden" name="orgId" value="<%=orgId %>">
		<input type="hidden" id="tarIds" name="tarIds">
			
		<table width="60%"  border="0" align="center">
		    <tr>
				<td width="38%" align="center" valign="middle">�����趨�����ݷ�Χ</td>
				<td width="23%"> 
				<td width="39%" align="center" valign="middle">���趨�����ݷ�Χ</td>
			</tr>
			<tr>
				<td align="center">
					<select id="allReport" name="childRepId" size="24" multiple="true" style="width:300">
					<% 
					rs=st.executeQuery("select ORG_ID,ORG_NAME from V_GF_ORG ");
					while(rs.next()){					
					%>
					<option value="<%=rs.getString(1) %>">(<%=rs.getString(1) %>)<%=rs.getString(2) %></option>
					<%} %>
				      </select>
				</td>
		        <td align="center" valign="middle">
			        <p><input type="button" name="add" value="�����" styleClass="input-button" onclick="addReport()"/></p>
			        <p><input type="button" name="delete" value="��ɾ��" styleClass="input-button" onclick="delReport()"/></p>
		        </td>		        
		      	<td align="center">
					<select id="selectReport" property="selectRepList" size="24" multiple="true"  style="width:300">
		          		<% 
					rs=st.executeQuery("select B.ORG_ID,B.ORG_NAME from EXCEL_ORG A left join V_GF_ORG  B on A.TAR_ID=B.ORG_ID WHERE A.SRC_ID='"+orgId+"'");
					while(rs.next()){					
					%>
					<option value="<%=rs.getString(1) %>">(<%=rs.getString(1) %>)<%=rs.getString(2) %></option>
					<%} %>
					</select>      
		        </td>
			</tr>
		</table>
	</form>
	<table width="80%"  border="0" align="center">
		<tr>
			<td colspan="3">
				<div id=location></div>
			</td>
		</tr>
		<tr>
			<td align="right">
				<input type="submit" name="submit" value="����" styleClass="input-button" onclick="submitData()"/>	   			
				<input type="button" name="back" value="����" styleClass="input-button" onclick="back()"/>
			</td>
		</tr>
	</table> 	
	<%
	conn.closeSession();
	%>	  
</body>
</html:html>
