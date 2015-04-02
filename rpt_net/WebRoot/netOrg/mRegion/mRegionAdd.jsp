<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import="com.fitech.net.adapter.StrutsMRegionDelegate" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<jsp:useBean id="utilMRegionForm" scope="page" class="com.fitech.net.form.UtilMRegionForm" />

<html:html locale="true">
	<head>
	<html:base/>
    <title>机构地区设定</title>
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
    <link href="../../css/common.css" type="text/css" rel="stylesheet">
  </head> 
  
  <body>
	<table border="0" width="90%" align="center">
		<tr><td height="8"></td></tr>
		<tr>
			 <td>当前位置 >> 	机构管理 >> 机构地区新增</td>
		</tr>
		<tr><td height="10"></td></tr>
	</table>
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	
		<html:form action="/insertMRegion" method="post" onsubmit="return _submit(this)">
				<table border="0" width="50%" align="center" cellspacing="1" cellpadding="4" class="tbcolor">
					<TR class="tableHeader" >
						<TD width="15%" valign="bottom">
						机构地区设定
						</TD>
					</TR>
					
					<TR align="center" valign="middle" bgcolor="#FFFFFF">
					  <TD>
						<table border="0" width="100%" align="center" cellspacing="1" cellpadding="4">
						  <TR>
						    <TD align="right">地区名称：</TD>
						    <TD><html:text property="region_name" size="20" styleClass="input-text" maxlength="20"/></TD>
				          </TR>				          
				          <TR>
				          	<TD align="right">地区所属上级地区：</TD>
				          	<TD>								
								<html:select property="pre_region_id">
									<html:option value="-1">--请选择上级地区--</html:option>
									<html:optionsCollection name="utilMRegionForm" property="regions"/>
								</html:select>
							</TD>							
				          </TR>
					      <TR>
					        <TD align="center" colspan="2">
				               <html:submit styleClass="input-button"  value="新增"/>
				            </TD>
				          </TR>
				        </table>
				      </TD>
					</TR>
				</table>
		</html:form>	
	</body>
	<SCRIPT language="javascript">
		function _submit(form){
			if(form.region_name.value.Trim()==""){
				alert("请输入机构地区名称！");
				form.region_name.focus();
				return false;
			}
			if(form.org_type_id.value=="-1"){
				alert("请选择所属机构类型！");
				form.org_type_id.focus();
				return false;
			}
			if(form.pre_region_id.value=="-1"){
				if(form.org_type_id.value != maxOTId){
					alert("请选择所属上级地区！");
					form.pre_region_id.focus();
					return false;
				}
				else
					return true;
			}
		}
		function orgType_Change(optionMenu){
			removeMRegion(optionMenu);
			var orgType=mRegionForm.org_type_id.value;
			var arr=map[orgType];
			optionMenu[0]=new Option("--请选择所属上级地区--",-1);
			for(i=1;i<arr.length;i++){
				var obj = arr[i].split(",");
				optionMenu[i]=new Option(obj[0],obj[1]);
			}
		}  
		function removeMRegion(optionMenu){
			for(i=0;i<optionMenu.options.length;i++){
				//optionMenu.options[i]=null;
				optionMenu.options.remove(optionMenu.options.length-1);
			}
			optionMenu.options.length=0;
		}
		String.prototype.Trim=function(){
			return this.replace(/(^\s*)|(\s*$)/g,"");
		}	    
	</SCRIPT>
</html:html>

