<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.util.FitechUtil"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<jsp:useBean id="utilForm" scope="page" class="com.cbrc.smis.form.UtilForm"/>
<%
	String childRepId=null,versionId=null;
	Integer OATId=new Integer(0);
	
	if(request.getParameter("childRepId")!=null) childRepId=(String)request.getParameter("childRepId");
	if(request.getParameter("versionId")!=null) versionId=(String)request.getParameter("versionId");
	
	
	if(childRepId!=null) request.setAttribute("ChildRepID",childRepId);
	if(versionId!=null) request.setAttribute("VersionID",versionId);
	boolean system_schema_id = false;
	if(Config.SYSTEM_SCHEMA_FLAG==1){
		system_schema_id = true;
	}
%>

<html:html locale="true">
	<head>
		<html:base/>
		<title>������Χ������Ƶ���趨</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../../css/common.css" type="text/css" rel="stylesheet">
		<script type="text/javascript" src="../../js/comm.js"></script>
		<script type="text/javascript" src="../../js/func.js"></script>
		<script language="javascript">	
			//��֤����Ϣ
			
function CheckNum(InputValue)
	{	
	    var reg=/^([1-9])+$/
		  var isValid
			
			isValid=reg.exec(InputValue)
			if (!isValid)
			{
				//alert("???? "+InputValue+" ??????")
				return false
			}
			return true
	} 
			
			
			function _bsplValidate(){
				var objTbl=document.getElementById('tbl');
				var objForm=document.forms['form1'];
				var objRepFreqIds=objForm.elements['repFreqIds'];
				var objNormalTimes=objForm.elements['normalTimes'];
				var objDelayTimes=objForm.elements['delayTimes'];
				
				var flag=false;
				for(var i=3;i<=objTbl.rows.length;i++){
					if(objRepFreqIds[i-3].checked){
						if(isEmpty(objNormalTimes[i-3].value)==true){
							alert("�����뱨��ʱ��!\n");
							objNormalTimes[i-3].focus();
							return false;
						}else{
							if(CheckNum(objNormalTimes[i-3].value)==false){
								alert("��������ȷ�ı���ʱ��!\n");
								objNormalTimes[i-3].focus();
								objNormalTimes[i-3].select();
								return false;
							}
						}
						if(isEmpty(objDelayTimes[i-3].value)==false && CheckNum(objDelayTimes[i-3].value)==false){
							alert("��������ȷ�Ĳ���ʱ�� !\n");
							objDelayTimes[i-3].focus();
							objDelayTimes[i-3].select();
							return false;
						}
						flag=true;
					}
				}
				
				if(flag==false){
					alert("����ȷ�趨������Χ������Ƶ��ʱ��!\n");
					return false;
				}else{
					return true;
				}
			}
		

	  		
	  			 /**
			   * �����¼�
			   */
			   function _back(templateId,versionId){
			   	 var form=document.forms['frmBJGX'];
			   	 window.location="<%=request.getContextPath()%>/viewAFTemplateDetail.do?templateId="+templateId+"&versionId="+versionId+"&bak2=2";
			   }

		</script>
	</head>
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<body background="../image/total.gif">
		<table border="0" cellspacing="0" cellpadding="4" width="80%" align="center">
			<tr><td height="8"></td></tr>
			<tr>
				<td>
					��ǰλ�� >> ������� >> ģ��ά�� >> ����Ƶ���޸�
				</td>
			</tr>
			<tr><td height="10"></td></tr>		
		</table>
		<br>
		<html:form  styleId="form1" action="/gznx/updateMActuRept" method="Post" onsubmit="return _bsplValidate()">
		<TABLE cellSpacing="1" cellPadding="4" width="80%" border="0" class="tbcolor" id="tbl">
			<TR class="tbcolor1">
				<th colspan="4" align="center" id="list" height="30">
					<span style="FONT-SIZE: 11pt">
					<logic:present name="ReportName" scope="request">
						��<bean:write name="ReportName" scope="request"/>��
					</logic:present>������Χ������Ƶ��ʱ���趨</span>
				</th>
			</TR>
			<TR bgcolor="#FFFFFF">
				<TD align="center" width="40%" class="tableHeader">
					<b>ѡ��</b>
				</TD>
				<TD align="center" width="20%" class="tableHeader">
					<b>Ƶ��</b>
				</TD>
				<TD align="center" width="20%" class="tableHeader">
					<b>���б���ʱ��</b>
				</TD>
				<TD align="center" width="20%" class="tableHeader">
					<b>��֧����ʱ��</b>
				</TD>
			</tr>
				<logic:present name="ChildRepID" scope="request">
					<input type="hidden" name="childRepId" value="<bean:write name="ChildRepID"/>">
				</logic:present>
				<logic:present name="VersionID" scope="request">
					<input type="hidden" name="versionId" value="<bean:write name="VersionID"/>">
				</logic:present>
					<% 
						if(system_schema_id){
					%>
				<logic:present name="Records" scope="request">
					<logic:iterate id="item" name="Records" >
						<tr bgcolor="#FFFFFF">
							<TD align="center">
								<logic:notEmpty name="item" property="repFreqId">
									<INPUT type="checkbox" name="repFreqIds" value='<bean:write name="item" property="dataRangeId"/>' checked/>								
								</logic:notEmpty>
								<logic:empty name="item" property="repFreqId">
									<INPUT type="checkbox" name="repFreqIds" value='<bean:write name="item" property="dataRangeId"/>' />
								</logic:empty>
								</TD>
							<td align="center">
								<bean:write name="item" property="repFreqName"/>
								<input type="hidden" name="dataRangeIds" value="<bean:write name="item" property="dataRangeId"/>"/>
							</td>
							<logic:equal name="item" property="dataRangeId" value="4">
								<TD align="center">
									<logic:notEmpty name="item" property="repFreqId">
										<input class="input-text" type="text" size="6" name="normalTimes"  maxlength="3"  readonly="readonly" value="<bean:write name="item" property="normalTime"/>" title="Ĭ��ֵ�����޸�">						
									</logic:notEmpty>
									<logic:empty name="item" property="repFreqId">
										<input class="input-text" type="text" size="6" name="normalTimes"  maxlength="3" readonly="readonly"  value="1" title="Ĭ��ֵ�����޸�">
									</logic:empty>
									
								</TD>
								<TD align="center">
									<logic:notEmpty name="item" property="repFreqId">
										<input class="input-text" type="text" size="6" name="delayTimes"  maxlength="3" readonly="readonly" value="<bean:write name="item" property="delayTime"/>" title="Ĭ��ֵ�����޸�">						
									</logic:notEmpty>
									<logic:empty name="item" property="repFreqId">
										<input class="input-text" type="text" size="6" name="delayTimes"   maxlength="3" readonly="readonly" value="1" title="Ĭ��ֵ�����޸�"> 
									</logic:empty>								
								</TD>		
							</logic:equal>
							<logic:notEqual name="item" property="dataRangeId" value="4">
								<TD align="center">
									<logic:notEmpty name="item" property="repFreqId">
										<input class="input-text" type="text" size="6" name="normalTimes"  maxlength="2" readonly="readonly" value="<bean:write name="item" property="normalTime"/>" title="Ĭ��ֵ�����޸�">						
									</logic:notEmpty>
									<logic:empty name="item" property="repFreqId">
										<input class="input-text" type="text" size="6" name="normalTimes"  maxlength="2"  value="1" readonly="readonly" title="Ĭ��ֵ�����޸�">
									</logic:empty>
									
								</TD>
								<TD align="center">
									<logic:notEmpty name="item" property="repFreqId">
										<input class="input-text" type="text" size="6" name="delayTimes"  maxlength="2" readonly="readonly" value="<bean:write name="item" property="delayTime"/>" title="Ĭ��ֵ�����޸�">						
									</logic:notEmpty>
									<logic:empty name="item" property="repFreqId">
										<input class="input-text" type="text" size="6" name="delayTimes"   maxlength="2" value="1" readonly="readonly" title="Ĭ��ֵ�����޸�">
									</logic:empty>								
								</TD>		
							</logic:notEqual>
								
						</tr>
					</logic:iterate>
				</logic:present>
				<% 
				}else{
				%>
				<logic:present name="Records" scope="request">
					<logic:iterate id="item" name="Records" >
						<tr bgcolor="#FFFFFF">
							<TD align="center">
								<logic:notEmpty name="item" property="repFreqId">
									<INPUT type="checkbox" name="repFreqIds" value='<bean:write name="item" property="dataRangeId"/>' checked/>								
								</logic:notEmpty>
								<logic:empty name="item" property="repFreqId">
									<INPUT type="checkbox" name="repFreqIds" value='<bean:write name="item" property="dataRangeId"/>' />
								</logic:empty>
								</TD>
							<td align="center">
								<bean:write name="item" property="repFreqName"/>
								<input type="hidden" name="dataRangeIds" value="<bean:write name="item" property="dataRangeId"/>"/>
							</td>
							<logic:equal name="item" property="dataRangeId" value="4">
								<TD align="center">
									<logic:notEmpty name="item" property="repFreqId">
										<input class="input-text" type="text" size="6" name="normalTimes"  maxlength="3" value="<bean:write name="item" property="normalTime"/>">						
									</logic:notEmpty>
									<logic:empty name="item" property="repFreqId">
										<input class="input-text" type="text" size="6" name="normalTimes"  maxlength="3">
									</logic:empty>
									
								</TD>
								<TD align="center">
									<logic:notEmpty name="item" property="repFreqId">
										<input class="input-text" type="text" size="6" name="delayTimes"  maxlength="3" value="<bean:write name="item" property="delayTime"/>">						
									</logic:notEmpty>
									<logic:empty name="item" property="repFreqId">
										<input class="input-text" type="text" size="6" name="delayTimes"   maxlength="3">
									</logic:empty>								
								</TD>		
							</logic:equal>
							<logic:notEqual name="item" property="dataRangeId" value="4">
								<TD align="center">
									<logic:notEmpty name="item" property="repFreqId">
										<input class="input-text" type="text" size="6" name="normalTimes"  maxlength="2" value="<bean:write name="item" property="normalTime"/>">						
									</logic:notEmpty>
									<logic:empty name="item" property="repFreqId">
										<input class="input-text" type="text" size="6" name="normalTimes"  maxlength="2">
									</logic:empty>
									
								</TD>
								<TD align="center">
									<logic:notEmpty name="item" property="repFreqId">
										<input class="input-text" type="text" size="6" name="delayTimes"  maxlength="2" value="<bean:write name="item" property="delayTime"/>">						
									</logic:notEmpty>
									<logic:empty name="item" property="repFreqId">
										<input class="input-text" type="text" size="6" name="delayTimes"   maxlength="2">
									</logic:empty>								
								</TD>		
							</logic:notEqual>
								
						</tr>
					</logic:iterate>
				</logic:present>
				<%
					}
				%>
				
				
				
				<logic:notPresent name="Records" scope="request">
					<tr bgcolor="#FFFFFF">
						<td colspan="4">
							��ƥ���¼
						</td>
					</tr>
				</logic:notPresent>
		</TABLE>
		
		<table border="0" cellspacing="0" cellpadding="4" width="80%" align="center">
			<TR>
				<TD align="center">
					<input type="submit" value=" �� �� " class="input-button">&nbsp;
					<input class="input-button" type="button" value=" �� �� " onclick="_back('<bean:write name='ObjForm' property='childRepId'/>','<bean:write name='ObjForm' property='versionId'/>')">
				</TD>
			</TR>
		</table>

		</html:form>
		

	</body>
</html:html>
