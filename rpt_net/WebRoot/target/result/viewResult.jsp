<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<jsp:useBean id="configBean" scope="page" class="com.cbrc.smis.common.Config" />
<jsp:useBean id="utiltargetDefineForm" scope="page" class="com.fitech.net.form.UtilTargetDefineForm" />
<%@ page import="com.cbrc.smis.common.Config"%>
<%
	String yearAndMonth=(String)request.getAttribute("yearAndMonth");
	session.setAttribute(Config.MESSAGES,request.getAttribute(Config.MESSAGES));
	String orgId = (String)request.getAttribute("orgId");
%>
<html:html locale="true">
<head>
	<html:base />
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" type="text/css" rel="stylesheet">
	
	<script language="JavaScript" type="text/JavaScript">
		<logic:present name="<%=configBean.APART_PAGE_OBJECT%>" scope="request">
	    	var curPage="<bean:write name='ApartPage' property='curPage'/>";
	    </logic:present>
	    <logic:notPresent name="<%=configBean.APART_PAGE_OBJECT%>" scope="request">
	    	var curPage="1";
	    </logic:notPresent>
	    
	    <logic:present name="actuTargetResultForm" scope="request">
	    	var orgId="<bean:write name='actuTargetResultForm' property='orgId'/>";
	    </logic:present>
	    <logic:notPresent name="actuTargetResultForm" scope="request">
	    	var curPage="";
	    </logic:notPresent>
	    function _viewWarnDetail(targetId,id,repFreId,dataRangeId){
			window.location="<%=request.getContextPath()%>/target/optionWarnDetail.do?targetDefineId="+targetId
		  						+"&id="+id
		  						+"&curPage="+curPage
		  						+"&repFreId="+repFreId
		  						+"&dataRangeId="+dataRangeId
		  						+"&orgId="+orgId;
	    }
	    function  _viewPreStandDetail(targetId,id,preStandardValue){
	    	if(preStandardValue=='��'){
	    		alert("�Բ���! û�б�����ֵ!( ����ָ��ֵΪ0 )");
	    		return;
	    	}	
	    	window.location="<%=request.getContextPath()%>/target/viewPreStandDetail.do?targetDefineId="+targetId
		  						+"&id="+id
		                        +"&curPage="+curPage
		                        +"&preStandardValue="+preStandardValue;
	    }
	    function _viewDetail(id,name){
			window.location="<%=request.getContextPath()%>/target/viewDetail.do?targetDefineId="+id
		  						+"&targetDefineName='"+name+"'"
		                         +"&curPage="+curPage;
		}
		function  _generater(){
			var objForm=document.forms["frm"];
	     
			if(objForm.year.value=="" || objForm.month.value==""){
				alert("����������°���");
	      		return false;
	      	}
			var yam = '<%=yearAndMonth%>';

			if((yam.indexOf(objForm.year.value+'-'+objForm.month.value))>-1){
	      		if(confirm("���ڵ�ָ��������,�Ƿ���������?\n")==true){
					window.location="<%=request.getContextPath()%>/target/generateTarget.do?"
							+"year="+objForm.year.value
	     					+"&month="+objForm.month.value;
	      		
	      		}else return;
			}else{
				window.location="<%=request.getContextPath()%>/target/generateTarget.do?"
						+"year="+objForm.year.value
			     		+"&month="+objForm.month.value+"&orgId="+orgId;
			}
			prodress1.style.display = "none" ;
			prodress.style.display = "" ;
	   }
	</script>
</head>
<body>
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				window.open("<%=request.getContextPath()%>/target/result/showTargetInfo.jsp","", "height=250, width=300,toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no");
			</script>
		</logic:greaterThan>
	</logic:present>
	<label id="prodress" style="display:none">
		<span class="txt-main" style="color:#FF3300">��������ָ�꣬���Ժ�......</span>
	</label>
  	<label id="prodress1" >
  	
	<table border="0" width="98%" align="center">
		<tr>
			<td>
				��ǰλ�� >> ָ����� >> ָ�����Ԥ��
			</td>
		</tr>
	</table>
	<table cellspacing="1" cellpadding="4" border="0" width="98%" align="center">
		<html:form method="post" action="/target/viewTargetGenerate" styleId="frm">
			<tr>
				<td>
					<fieldset id="fieldset">
						<table cellspacing="0" cellpadding="0" border="0" width="100%" align="center" height="28">
							<tr>
								<td align="center">
									ָ�����ƣ�
									<html:text property="targetDefineName"  styleClass="input-text" />
								</td>
								<TD align="left">ָ������:
									<html:select property="businessId" >
									    <html:option value="-1">--��ѡ��ָ������--</html:option>       
										<html:optionsCollection name="utiltargetDefineForm" property="businessList"/>   
									</html:select>
								</TD>
								<td  align="center">
									ָ��ʱ�䣺
									<html:text property="year" maxlength="4" size="6" styleClass="input-text" />
									&nbsp;��
									<html:text property="month" maxlength="2" size="4" styleClass="input-text" />
									&nbsp;��
								</td>
			
								<td >
									<html:submit styleClass="input-button" value=" �� ѯ " />
								</td>
								<td>
									<input type="button" class="input-button" onclick="_generater()" value="ָ������" />
								</td>
							</tr>
						</table>
					</fieldset>
				</td>
			</tr>
		</html:form>
	</table>

	<TABLE cellSpacing="1" cellPadding="4" width="98%" border="0" class="tbcolor">
		<tr class="titletab">
			<th colspan="9" align="center" id="list">
				<strong> ָ�������б� </strong>
			</th>
		</tr>
		<tr align="center" class="middle">
			<td class="tableHeader" align="center" width="11%">
				ָ������
			</td>
			<td class="tableHeader" align="center" width="7%">
				ָ��ҵ������
			</td>
			<td class="tableHeader" align="center" width="11%">
				ָ������
			</td>
			
			<td class="tableHeader" align="center" width="5%">
				ָ��ֵ
			</td>
			<td class="tableHeader" align="center" width="5%">
				������ֵ
			</td>
			<td class="tableHeader" align="center" width="5%">
				ʱ��
			</td>

			<td class="tableHeader" align="center" width="5%">
				���ҵ�λ
			</td>
			<td class="tableHeader" align="center" width="3%">
				Ƶ��
			</td>
			<td class="tableHeader" align="center" width="10%">
				���Ϳھ�
			</td>
		</tr>
		
		<% 
			int beforChange=-1;  //ָ�����
			boolean flag=true;
			
			int beforeNorChange=-1; //ָ��ҵ��
			boolean norFlag=true;
        %>
		<logic:present name="Records" scope="request">
			<logic:iterate id="item" name="Records" indexId="indexid">
				<TR bgcolor="#FFFFFF">				
			 	<%
					//Ϊҳ��������(ָ�����/ָ��ҵ��)������ʾ�����⴦��
					//ָ�����
					Integer tChange=((com.fitech.net.form.ActuTargetResultForm)item).getChange();
					int tempChange=0;
					if(tChange==null){
						tempChange=0;
					}else{
						tempChange=tChange.intValue();
					}
					//ָ��ҵ��
					Integer tNorChange=((com.fitech.net.form.ActuTargetResultForm)item).getNorChange();
					int tempNorChange=0;
					if(tNorChange==null){
						tempNorChange=0;
					}else{
						tempNorChange=tNorChange.intValue();
					}			     
					//��ʼ����"ָ�����"�У���ʼ��beforChange����
					if (beforChange==-1 || beforChange==0){
			        	beforChange=tempChange;
			        	flag=true;
					}
					if (beforChange!=0 && flag==true)
					{
			    	%>	 
						<td align="center" rowspan=<bean:write name="item" property="change"/> >
							<bean:write name="item" property="businessName" /><br>
						</td>
					<% 
					}
				   		beforChange--;
				   		flag=false;
				   					
					//��ʼ����"ָ��ҵ��"�У���ʼ��beforNorChange����
					if (beforeNorChange==-1 || beforeNorChange==0){
						beforeNorChange=tempNorChange;
						norFlag=true;
					}
					if (beforeNorChange!=0 && norFlag==true){
					%>
						<td align="center" rowspan=<bean:write name="item" property="norChange"/>>
							<bean:write name="item" property="normalName"  /><br>
						</td>
					<%
					}
						beforeNorChange--;
						norFlag=false;
					%>
					<td align="center">
						<a href="javascript:_viewDetail('<bean:write name="item" property="targetDefineId"/>','<bean:write name="item" property="targetDefineName"/>','<bean:write name="item" property="orgId"/>')"> <bean:write name="item" property="targetDefineName" /> </a>
					<br></td>
					<TD align="center">
						<logic:notEmpty name="item" property="color">
							<a href="javascript:_viewWarnDetail('<bean:write name="item" property="targetDefineId"/>','<bean:write name="item" property="id"/>','<bean:write name="item" property="repFreId"/>','<bean:write name="item" property="dataRangeId"/>')"> <FONT color='<bean:write name="item" property="color"/>'><bean:write name="item" property="targetResult" />%</FONT>
								</a>
						</logic:notEmpty>
					<br></TD>
					<TD align="center">
						<logic:notEmpty name="item" property="preStandardValue">
							<FONT color='<bean:write name="item" property="preStandardColor"/>'> <bean:write name="item" property="preStandardValue" />% </FONT> 
						</logic:notEmpty><br>
					</TD>
					<TD align="center">
						<bean:write name="item" property="year" />-<bean:write name="item" property="month" /><br>
					</TD>
					<td align="center">
						<bean:write name="item" property="curUnitName" />
					</td>
					<TD align="center">
						<bean:write name="item" property="repFreName" /><br>
					</TD>
					<td align="center">
						<bean:write name="item" property="dataRangeName" />
					</td>
				</TR>
			</logic:iterate>
		</logic:present>
		<logic:notPresent name="Records" scope="request">
			<tr bgcolor="#FFFFFF">
				<td colspan="9">
					���޼�¼
				</td>
			</tr>
		</logic:notPresent>
	</table>
 </label>
</body>
</html:html>
