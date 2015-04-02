<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.common.ApartPage" %>
<%@ page import="com.cbrc.smis.common.Config" %>

<html:html locale="true">
<head>
	<html:base />
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../css/common.css" type="text/css" rel="stylesheet">
	<script language="javascript" src="../../js/func.js"></script>
	<script language="javascript">
	/*获取用户选择修改的状态，用户确认后提交修改请求*/
		function modJob(str,rep)
		{
			if(window.confirm('确认修改吗？'))
			{
				var	sts = document.getElementsByName(rep);
				var jobSts; 
				for(var i = 0; i < sts.length ; i ++)
				{
					if(sts[i].checked)
					{
						jobSts = sts[i].value;
					}
				}
				window.location.href('jobLogMgr.do?method=updateJob&sJobSts='+jobSts + str);
			}
		}
		/*批量修改*/
		function modJobBatch(str)
		{
			var job = eval(document.getElementsByName("job"));
			var param = '';
			var blChk = false;
			for(var i = 0; i < job.length; i ++)
			{
				if(job[i].checked)
				{
					blChk = true;
					var h =  parseInt(job[i].nextSibling.nodeValue.replace(" ",""));
					 
					var sts = document.getElementsByName("rep"+ h);
					//alert(sts.length);
					for(var j = 0; j < sts.length ; j ++)
					{
						if(sts[j].checked)
						{
							param = param + job[i].value + '&sJobSts='+ sts[j].value + '-';
						}
					}					
				}
			}				
			if(!blChk)
			{
				alert('没有选中修改的报表！');
			}
			else
			{
				//param = param.substring(1) + str;
				alert(param);	
				window.location.href('jobLogMgr.do?method=updateJob'+ param+ str );	
			}
		}
		function chkFull()
		{
			var chk = document.getElementById('chkAll');
			var blChk =chk.checked;
			var job = eval(document.getElementsByName("job"));
			for(var i = 0; i < job.length; i ++)
			{
				job[i].checked = blChk;
			}
		}
		function openError(paramURL)
		{
		   // alert(paramURL);
			window.open('jobLogMgr.do?method=viewError'+paramURL,'_blank','height=230,width=500,top=10,left=400');
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
	<table border="0" width="98%" align="center">
		<tr>
			<td height="4"></td>
		</tr>
		<tr>
			<td>
				当前位置 &gt;&gt; 数据统计&gt;&gt; ETL监控
			</td>
		</tr>
		<tr>
			<td height="4"></td>
		</tr>
	</table>
	
	
	<table cellspacing="0" cellpadding="4" border="0" width="98%" align="center">
		<html:form action="/collectReport/jobLogMgr?method=findJob" method="post" styleId="frm" onsubmit="return _submit(this)">
			<tr>
				<td>
					<fieldset id="fieldset">
						<table cellspacing="0" cellpadding="0" border="0" width="100%" align="center">
							<tr>
								<td height="5"></td>
							</tr>
							<tr>
								<td height="25" width="30%">&nbsp;
									报送机构：
									

									 <div style="POSITION: absolute">
										<table cellSpacing=0 cellPadding=0 border=0>
											<tr>
												<td>			
												<logic:notEmpty name="orgLst">
													<html:select property="orgId">
														<option value="">全部</option>
														<html:options property="orgId" labelProperty="orgName" collection="orgLst" />
													</html:select>		
												</logic:notEmpty>																											
												</td>
											</tr>
										</table>
									</div>
								</td>
								<td>
									报表名称：
									<html:text property="repName" size="25" styleClass="input-text" />
								</td>
								<td>
									<html:submit styleClass="input-button" value=" 查 询 " />
								</td>
								<td>
								</td>
							</tr>
						</table>
						<table cellspacing="0" cellpadding="0" border="0" width="100%" align="center">
							<tr>
								
								<td height="25">&nbsp;
									报表时间：
									<html:text property="year" maxlength="4" size="6" styleClass="input-text" />
									年
									<html:select property="term">
										<html:options name="jobForm" property="month"/>
									</html:select>
									月
								</td>
								<td>
									<html:radio property="jobSts" value="WAITTING">¤等待握手</html:radio>
									<html:radio property="jobSts" value="READY">☆已经握手</html:radio>
									<html:radio property="jobSts" value="ERROR">×异常</html:radio>
									<html:radio property="jobSts" value="DONE">√成功提取</html:radio>
									<html:radio property="jobSts" value="">全部</html:radio>
								</td>
							</tr>
							<tr>
								<td height="3"></td>
							</tr>
						</table>
					</fieldset>
				</td>
			</tr>
		</html:form>
	</table>	
	
	
	<table cellSpacing="0" cellPadding="4" width="98%" border="0" align="center">
		<html:form action="/collectReport/jobLogMgr?method=updateJob" method="post" styleId="jobFrm">
			<tr>
				<td>
					<table cellSpacing="1" cellPadding="4" width="100%" border="0" class="tbcolor">
						<TR class="titletab">
							<th align="center" valign="middle">
								<input type="checkbox" name="chkAll" onclick="chkFull()">
							</th>
							<th align="center" valign="middle">
								报表名称
							</th>
							<th align="center" valign="middle">
								口径
							</th>
							<th align="center" valign="middle">
								币种
							</th>
							<th align="center" valign="middle">
								上报子行
							</th>
							<th align="center" valign="middle">
								报表时间
							</th>
							<th align="center" valign="middle">
								开始时间
							</th>
							<th align="center" valign="middle">
								结束时间
							</th>
							<th align="center" valign="middle">
								状态
							</th>
							
							<Th width="5%" align="center" valign="middle">
								操作
							</Th>							
						<tr>			
						<%
							 int index = 0;
							 if(request.getAttribute("ApartPage") != null)
							{
								ApartPage apartPage = (ApartPage)request.getAttribute("ApartPage");
								if(apartPage != null)
								index =(apartPage.getCurPage() -1) * Config.PER_PAGE_ROWS;
							}
						%>
						<logic:present name="jobLogLst" scope="request">
						     <logic:iterate id="jobLog" name="jobLogLst" scope="request">
								<tr bgcolor="#FFFFFF">
									<td>
								    <input type="checkbox" name="job" value="<bean:write name='jobLog' property='paramURL'/>">
									<%= ++ index%>
									</td>
									<td align="center">
										<bean:write name="jobLog" property="repName"/>
									</td>
									
									
									<td align="center">
										<bean:write name="jobLog" property="id.dataRange.dataRgDesc"/>
									</td>
									<td align="center">
										<bean:write name="jobLog" property="id.cur.curName"/>
									</td>
									<td align="center">
										<bean:write name="jobLog" property="id.org.orgName"/>
									</td>
									<td align="center">
										<bean:write name="jobLog" property="id.year"/>
										-									
										<bean:write name="jobLog" property="id.term"/>
										
									</td>
									<td align="center">
										<bean:write name="jobLog" property="actStTm"/>
									</td>
									<td align="center">
										<bean:write name="jobLog" property="actEndTm"/>
									</td>
									<td align="center">
									<logic:match name="jobLog" property="jobSts" value="ERROR">
										<a href="javascript:openError('<bean:write name="jobLog" property="paramURL"/>')" ><font color="red"><bean:write name="jobLog" property="jobSts"/></font></a>
									</logic:match>
									<logic:notMatch name="jobLog" property="jobSts" value="ERROR">
									<font color="green"><bean:write name="jobLog" property="jobSts" /></font>
									</logic:notMatch>										
									</td>
									<td>
									<table>
										<tr>
											<td>
												<table>
													<tr>
														<td>
														<logic:match name="jobLog" property="jobSts" value="WAITTING">
															<input type="radio" value="WAITTING" checked="checked" name="rep<%=index%>">¤
														</logic:match>
														<logic:notMatch name="jobLog" property="jobSts" value="WAITTING">
															<input type="radio" value="WAITTING" name="rep<%=index%>">¤
														</logic:notMatch>
														</td>
														<td>
															<logic:match name="jobLog" property="jobSts" value="READY">
																<input type="radio" value="READY" checked="checked" name="rep<%=index%>">☆
															</logic:match>
															<logic:notMatch name="jobLog" property="jobSts" value="READY">
																<input type="radio" value="READY" name="rep<%=index%>">☆
															</logic:notMatch>
														</td>
												
														
														<td>
														<logic:match name="jobLog" property="jobSts" value="DONE">
															<input type="radio" value="DONE" checked="checked" name="rep<%=index%>">√
														</logic:match>
														<logic:notMatch name="jobLog" property="jobSts" value="DONE">
															<input type="radio" value="DONE" name="rep<%=index%>">√
														</logic:notMatch>			
														</td>
													</tr>
												</table>
											</td>
											<td >
												<input type="button" name="btn" value="修  改" class="input-button"  onclick="modJob('<bean:write name="jobLog" property="paramURL"/>-&repId=<bean:write name="jobLog" property="id.repId"/>&versionId=<bean:write name="jobLog" property="id.versionId"/><bean:write name="modUrl"/>','rep<%=index%>')">
											</td>
										</tr>
									</table>																				
									</td>
								</tr>	
							</logic:iterate>						
						</logic:present>			
						<logic:notPresent name="jobLogLst" scope="request">
							<tr align="center">
								<td bgcolor="#ffffff" colspan="10" align="left">
									暂无符合条件的记录
								</td>
							</tr>
						</logic:notPresent>
					</table>
				</td>
			</tr>
			</html:form>
	</table>
	
	<table cellSpacing="0" cellPadding="0" width="98%" border="0">
		<TR>
			<TD>
				<jsp:include page="../apartpage.jsp" flush="true">
					<jsp:param name="url" value="../collectReport/jobLogMgr.do?method=findJob" />
				</jsp:include>
			</TD>		
		</TR>
			<logic:present name="ApartPage" scope="request">
				<logic:greaterThan name="ApartPage" property="count" value="0">
				<tr>
					<td>
						<input type="button" name="bn" value="修改选中" class="input-button" onclick="modJobBatch('<bean:write name="modUrl"/>')">
					</td>
				</tr>
				</logic:greaterThan>
			</logic:present>
	</table>
</body>
</html:html>
