<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import="com.cbrc.smis.common.Config,java.util.List" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
	List cbrclist =(List)request.getAttribute("cbrc");
	List pboclist =(List)request.getAttribute("pboc");
	List otherlist =(List)request.getAttribute("other");
%>
<html:html locale="true">
<head>
	<html:base/>
		<title>ģ�屨�ͷ�Χ����</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../../css/common.css" rel="stylesheet" type="text/css">
	
	<script language="javascript">
		var cbrclist = '<%=cbrclist%>';
		var pboclist = '<%=pboclist%>';
		var otherlist = '<%=otherlist%>';
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
						
			document.form1.selectRepIds.value = selectData;			
			document.form1.submit();			
		}
		//�����ϼ��˵�
		function back(){		
			window.location="<%=request.getContextPath()%>/system_mgr/OrgInfo/view.do";		
		}
		function changeReport(reportObj){
			var index=reportObj.selectedIndex;
			var reportFlg=reportObj.options[index].value;	
			//ȫ�������б��
			var allReportList = document.getElementById('allReport');
			//���б����б���е���Ŀ
			var allReportListOptions = allReportList.options;
			
			for(var i=allReportListOptions.length; i>=0; --i)
			{
				allReportList.remove(i);
			}
			
			if(reportFlg=="1"){
				var arrcbrc = cbrclist.split("],");
				for(var i=0;i<arrcbrc.length;i++){
					var cbrcdd = arrcbrc[i];
					cbrcdd = cbrcdd.replace("LabelValueBean[","");
					cbrcdd = cbrcdd.replace("[","");
					cbrcdd = cbrcdd.replace("]]","");
					var cbrcqq = cbrcdd.split(",");

					allReportList.options[i] = new Option("["+cbrcqq[0].Trim(),cbrcqq[1].Trim());
				 } 
			}else if(reportFlg=="2"){
				var arrpboc = pboclist.split("],");
				 for(var i=0;i<arrpboc.length;i++){			
					var cbrcdd = arrpboc[i];
					cbrcdd = cbrcdd.replace("LabelValueBean[","");
					cbrcdd = cbrcdd.replace("[","");
					cbrcdd = cbrcdd.replace("]]","");
					var cbrcqq = cbrcdd.split(",");
					allReportList.options[i] = new Option("["+cbrcqq[0].Trim(),cbrcqq[1].Trim());
				 } 
			}else if(reportFlg=="3"){
				var arrother = otherlist.split("],");
				 for(var i=0;i<arrother.length;i++){			
					var cbrcdd = arrother[i];
					cbrcdd = cbrcdd.replace("LabelValueBean[","");
					cbrcdd = cbrcdd.replace("[","");
					cbrcdd = cbrcdd.replace("]]","");
					var cbrcqq = cbrcdd.split(",");
					allReportList.options[i] = new Option("["+cbrcqq[0].Trim(),cbrcqq[1].Trim());
				 } 
			}
		}
		
		String.prototype.Trim=function(){
			return this.replace(/(^\s*)|(\s*$)/g,"");
		}
	</script>
</head>

<body>
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<br>
	<table width="80%" border="0" align="center">
		<tr>
			<td align="center">
				��������: &nbsp;
				<logic:present name="org_name" scope="request">
					<bean:write name="org_name"/>
				</logic:present>
			</td>
		</tr>
		<tr>
			<td >
				<div id=location> 
                 <div align="left">
                 	<strong>�������ͷ�Χ�趨</strong>
                 </div>
                </div>
			</td>
		</tr>
	</table>
	<html:form action="/orgBSFWUpdate" method="post" styleId="form1">
		<input type="hidden" name="orgId" value="<bean:write name="org_id"/>">
		<input type="hidden" id="selectRepIds" name="selectRepIds">
		<input type="hidden" id="selectRepVersions" name="selectRepVersions">
			<logic:present name="curPage">
			<input type="hidden" id="curPage" name="curPage" value='<bean:write name="curPage" />'>
			</logic:present>
		<table width="60%"  border="0" align="center">
			<tr>
				<td width="38%" align="left" valign="middle">      
					&nbsp;&nbsp;��ѡ�񱨱�&nbsp;
					<select id="reportFlg"  onchange="changeReport(this)">
						<option value="0">  </option>
						<option value="1">����ᱨ��</option>
						<option value="2">���б���</option>
						<option value="3">��������</option>
					</select>
	 			</td>
				<td width="23%"> </td>
				<td width="39%" > </td>
			    </tr>
		    <tr>
				<td width="38%" align="center" valign="middle">�����趨�ı��ͷ�Χ</td>
				<td width="23%"> 
				<td width="39%" align="center" valign="middle">���趨�ı��ͷ�Χ</td>
			</tr>
			<tr>
				<td align="center">
					<html:select styleId="allReport" property="childRepId" size="24" multiple="true" style="width:300">
						
					</html:select>
				</td>
		        <td align="center" valign="middle">
			        <p><html:button property="add" value="�����" styleClass="input-button" onclick="addReport()"/></p>
			        <p><html:button property="delete" value="��ɾ��" styleClass="input-button" onclick="delReport()"/></p>
		        </td>		        
		      	<td align="center">
					<html:select styleId="selectReport" property="selectRepList" size="24" multiple="true"  style="width:300">
		          		<logic:present name="orgHaveReport" scope="request">
		          			<html:optionsCollection name="orgHaveReport" label="label" value="value" />
		          		</logic:present>
					</html:select>      
		        </td>
			</tr>
		</table>
	</html:form>
	<table width="80%"  border="0" align="center">
		<tr>
			<td colspan="3">
				<div id=location></div>
			</td>
		</tr>
		<tr>
			<td align="center">
				<html:button property="submit" value="����" styleClass="input-button" onclick="submitData()"/>	   			
				<html:button property="back" value="����" styleClass="input-button" onclick="back()"/>
			</td>
		</tr>
	</table> 		  
</body>
</html:html>
