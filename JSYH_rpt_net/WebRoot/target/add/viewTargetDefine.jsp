<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<jsp:useBean id="configBean" scope="page" class="com.cbrc.smis.common.Config"/>
<jsp:useBean id="utiltargetDefineForm" scope="page" class="com.fitech.net.form.UtilTargetDefineForm" />
<%@ page import="com.cbrc.smis.common.Config"%>
<html:html locale="true">
  <head>
     <html:base/>
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
	    	var SPLIT_SYMBOL_COMMA="<%=Config.SPLIT_SYMBOL_COMMA%>";
			function _delete(id){
				if(confirm("你确定要删除吗?"))
				window.location="<%=request.getContextPath()%>/target/deleteTargetDefine.do?targetDefineId="+id+"&curPage="+curPage;
			}
			
			function _editwarn(id,name){
				window.location="<%=request.getContextPath()%>/target/editWarn.do?targetDefineId="+id
			  						+"&targetDefineName="+name+"&curPage="+curPage;
			}
			function _editprestandard(id,name){
				window.location="<%=request.getContextPath()%>/target/editPreStandard.do?targetDefineId="+id
			                         +"&targetDefineName="+name+"&curPage="+curPage;
			}
			function _edit(id){
				window.location="<%=request.getContextPath()%>/target/editTargetDefine.do?targetDefineId="+id
			                         +"&curPage="+curPage;
			}
		
			function _add(){
					//window.location="./add/addTargetDefine.jsp";
					window.location="./add/addTargetDefine.jsp?add=true";
			
			}
			function _viewDetail(id,name){
		 		window.location="<%=request.getContextPath()%>/target/viewDetail.do?targetDefineId="+id
		  						+"&targetDefineName="+name+"&curPage="+curPage;
			}
			
			/**
		  	 * 保存 
		  	 * 
		  	 * @param flag 
		  	 * @return void
		  	 */
			function _save(){
				var objForm=document.getElementById("frmChk");
		  	  	var count=objForm.elements['countChk'].value;
		  	  	
		  	  	var targetIds="";
		  	  	var obj=null;
		  	  	for(var i=1;i<=count;i++){
		  	  		try{
			  	  		obj=eval("objForm.elements['chk" + i + "']");
			  	  		
			  	  		if(obj.checked==true){
			  	  			targetIds+=(targetIds==""?"":SPLIT_SYMBOL_COMMA) + (obj.value+"|1");
			  	  		}else{
							targetIds+=(targetIds==""?"":SPLIT_SYMBOL_COMMA) + (obj.value+"|0");
			  	  		}
		  	  		}catch(e){alert(e);}
		  	  	}	
		  	  		
				var allFlags="";
				var qry="targetIds=" + targetIds + "&curPage=" + curPage;
				window.location="<%=request.getContextPath()%>/target/saveTargetDefine.do?" + qry;			  	  				
			}
			
			/**
			 * 保存所有指标
			 */
			function _saveAll(flag){
				var objFrm=document.forms['frm1'];
				document.getElementById('flag_h').value=flag;
				document.getElementById('defineName_h').value=objFrm.elements['defineName'].value
				document.getElementById('normalId_h').value=objFrm.elements['normalId'].value
				document.getElementById('businessId_h').value=objFrm.elements['businessId'].value;

				if(flag=='save'){
					if(confirm("您确定要保存所有的指标吗?\n")==true){
						document.frmsave.submit();			  	  				
					}
					return false;
				}else{
					if(confirm("您确定要取消所有的指标吗?\n")==true){
						document.frmsave.submit();				
					}
					return false;
				}
			}
			
			/**
			 * 全选操作
			 */
			function _selAll(){
				var formObj=document.getElementById("frmChk");
		  	  	var count=formObj.elements['countChk'].value;
		  	  	
		  	  	for(var i=1;i<=count;i++){
					try{
						if(formObj.elements['chkAll'].checked==false)
			  	  			eval("formObj.elements['chk" + i + "'].checked=false");
			  	  		else
			  	  			eval("formObj.elements['chk" + i + "'].checked=true");
		  	  		}catch(e){}
		  	  	}
			}
		</SCRIPT>
  </head>
  <body>
  <logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	
	<table border="0" width="98%" align="center">
		<tr>
			<td>当前位置 >> 	指标定义 >> 指标定义设定</td>
		</tr>
	</table>
	<table cellspacing="0" cellpadding="0" border="0" width="98%" align="center">		
		<html:form method="post" styleId="frm1" action="/target/viewTargetDefine" >
			<tr>
				<td>
					<fieldset id="fieldset" height="40">
					<br/>
						<table cellspacing="0" cellpadding="0" border="0" width="98%" align="center">
							<tr>
								<td align="left" >
									 指标名称:<html:text styleId="defineName" property="defineName" size="20" styleClass="input-text" />
								</td>
								<TD align="left">指标类型:
									<html:select property="businessId" >
									    <html:option value="-1">--请选择指标类型--</html:option>       
										<html:optionsCollection name="utiltargetDefineForm" property="businessList"/>   
									</html:select>
								</TD>
								<TD align="left">指标业务类型:
									<html:select property="normalId" >
									    <html:option value="-1">--请选择指标业务类型--</html:option>
									    <html:optionsCollection name="utiltargetDefineForm" property="normalList"/>     
								    </html:select>
								</TD>
								<td >
									<input class="input-button" type="submit" name="Submit" value="查  询">
								</td>
								<td width="10%"></td>
							</tr>
							
						</table>
					</fieldset>
				</td>				
			</tr>
		</html:form>
	</table><br/>
	<table cellSpacing="0" cellPadding="4" width="98%" border="0" align="center">
		<html:form method="post"  styleId="frmsave" action="/target/saveAllTargetDefine" >	
			<tr>
				<td align="left">
					<input type="button" value="保存本页指标" class="input-button" title="保存当前页的指标" onclick="_save()">													
					<INPUT class="input-button" type="Button" name="新     增" value="新     增" onclick="return _add()"/>															
				</td>	
				<td align="right">
					<input type="button" value="保存所有指标" class="input-button"  onclick="return _saveAll('save')" title="保存所有指标">	
					<input type="button" value="取消所有指标" class="input-button"  onclick="return _saveAll('cancel')" title="取消所有指标">
				</td>		
			</tr>
			<input type="hidden" id="flag_h" name="flag" value="" />
			<input type="hidden" id="defineName_h" name="defineName" value="" />
			<input type="hidden" id="normalId_h" name="normalId" value="" />
			<input type="hidden" id="businessId_h" name="businessId" value="" />
		</html:form>		
	</table>
	
	<TABLE cellSpacing="1" cellPadding="4" width="98%" border="0"  class="tbcolor">
		<tr class="titletab">
       		<th colspan="10" align="center" id="list">
				<strong>指标定义列表</strong>
			</th>
		</tr>
		<html:form action="/dateQuary/saveCheckRep" method="post" styleId="frmChk">
        	<tr align="center" class="middle">
        		<td width="5%" align="center" valign="middle"  class="tableHeader">
					<input type="checkbox" name="chkAll" onclick="_selAll()">
			  	</td>
			  	<td class="tableHeader" align="center" width="18%">指标名称</td>
				<td class="tableHeader" align="center" width="10%">指标类型</td>
				<td class="tableHeader" align="center" width="10%">指标业务类型</td>
				<td class="tableHeader" align="center" width="5%">版本号</td>
				<td class="tableHeader" align="center" width="10%">开始时间</td>
				<td class="tableHeader" align="center" width="10%">结束时间</td>
				<td class="tableHeader" align="center" width="22%" style="word-break:break-all;">公式</td>
				<td class="tableHeader" align="center" width="15%">操作</td>
			</tr>
       		<%int i=0;%>
	<logic:present name="Records" scope="request">
		<logic:iterate id="item" name="Records">
					<%i++;%>
			<TR bgcolor="#FFFFFF">
				<td align="center">
					<logic:notEmpty name="item" property="warn" >
						<logic:equal name="item" property="warn" value="1">
							<input type="checkbox" name="chk<%=i%>" value="<bean:write name="item" property="targetDefineId"/>" checked>
						</logic:equal>
						<logic:equal name="item" property="warn" value="0">
							<input type="checkbox" name="chk<%=i%>" value="<bean:write name="item" property="targetDefineId"/>">
						</logic:equal>
					</logic:notEmpty>
					<logic:empty name="item" property="warn">
						<input type="checkbox" name="chk<%=i%>" value="<bean:write name="item" property="targetDefineId"/>">
					</logic:empty>
				</td>
													
				<TD align="center">
					<logic:notEmpty name="item" property="defineName">
						<a href="javascript:_viewDetail('<bean:write name="item" property="targetDefineId"/>','<bean:write name="item" property="defineName"/>')"><bean:write name="item" property="defineName"/></a>
					</logic:notEmpty>
					<logic:empty name="item" property="defineName">无</logic:empty>
				</TD>
				<td align="center"><bean:write name="item" property="businessName"/></td>
				<td align="center"><bean:write name="item" property="normalName"/></td>
				<td align="center"><bean:write name="item" property="version"/></td>
				<TD align="center">
					<logic:notEmpty name="item" property="startDate">
						<bean:write name="item" property="startDate"/>
					</logic:notEmpty>
					<logic:empty name="item" property="startDate">无</logic:empty>
				</TD>
				<td align="center"><bean:write name="item" property="endDate"/></td>
				<td style="word-break:break-all;" align="center"><bean:write name="item" property="formula"/></td>
				<td  align="center" >
					<img src="../../image/zbfwbj.gif" border="0" title="指标预警编辑" style="cursor:hand" onclick="javascript:_editwarn('<bean:write name="item" property="targetDefineId"/>','<bean:write name="item" property="defineName"/>')">&nbsp;
					<img src="../../image/bsqbj.gif" border="0" title="比上期预警编辑" style="cursor:hand" onclick="javascript:_editprestandard('<bean:write name="item" property="targetDefineId"/>','<bean:write name="item" property="defineName"/>')">&nbsp;
					<img src="../../image/zbbj.gif" border="0" title="指标编辑" style="cursor:hand" onclick="javascript:_edit('<bean:write name="item" property="targetDefineId"/>')">&nbsp;
					<img src="../../image/del.gif" border="0" title="删除指标" style="cursor:hand" onclick="javascript:_delete('<bean:write name="item" property="targetDefineId"/>')">									
				</td>
			</TR>
		</logic:iterate>
	</logic:present>
	<input type="hidden" name="countChk" value="<%=i%>">
		<logic:notPresent name="Records" scope="request">
			<tr bgcolor="#FFFFFF">
				<td colspan="9">暂无记录</td>
			</tr>
		</logic:notPresent>
									
			</html:form>					
		</table>
		<table cellSpacing="1" cellPadding="4" width="98%" border="0">
			<tr>
				<TD colspan="7" bgcolor="#FFFFFF">
					<jsp:include page="../../apartpage.jsp" flush="true">
						<jsp:param name="url" value="../../target/viewTargetDefine.do" />
					</jsp:include>	
				</TD>
			</tr>
		</table>
	</body>
</html:html>
