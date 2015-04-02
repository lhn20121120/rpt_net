<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import="java.util.List" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.*, java.lang.*,com.cbrc.smis.other.CalendarCell" %> 

<jsp:useBean id="utilForm" scope="page" class="com.cbrc.smis.form.UtilForm"/>

<html:html locale="true">
	<head>
	<html:base/>
		<title>工作日历设定</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../css/common.css" rel="stylesheet" type="text/css">
		<script language="javascript">
			//初始取当前的年月
			var today = new Date();
			var year = today.getYear();
			var month = today.getMonth()+1;
			//如果是从Action返回的，则取request范围内的年月值
			<logic:present name="year" scope="request">
				year = <bean:write name="year"/>;
			</logic:present>
			<logic:present name="month" scope="request">
				month = <bean:write name="month"/>;
			</logic:present>
			//月份加
			function _addMonth()
			{
				month = month + 1;
				if(month>12)
				{
					month = 1;
					year = year + 1;
				}
				location = "viewCalendarDetail.do?calYear="+year+"&calMonth="+month;
			}
			//月份减
			function _subMonth()
			{
				month = month -1;
				if(month<1)
				{
					month = 12;
					year = year - 1;
					if(year<1900)
					{	
						year=1900;
						month=1;
					}
				}
				location = "viewCalendarDetail.do?calYear="+year+"&calMonth="+month;
			}
			//取出上次的查询条件
	  		/*function _setQuery()
	  		{
	  			<logic:present name="selectCalId" scope="request">
	  				var listObj = document.selectForm.calId;
	  				for(var i=0;i<listObj.length;i++)
	  				{
	  					if(listObj.options[i].value == "<bean:write name='selectCalId'/>")
	  					{		
	  						listObj.selectedIndex = i;
	  						break;
	  					}
	  				}
	  			</logic:present>
	  		}*/
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
		<table style="WIDTH: 90%" height="80%" width="100%" align="center" border="0">
			<tr>
				<td id="operation" vAlign="top">
					<table cellSpacing="0" cellPadding="0" width="100%" align="center" border="0">
						<tr>
							<td vAlign="top" height="20"></td>
						</tr>
						<tr>
							<td vAlign="top">
								<fieldset id="fieldset" style="WIDTH: 100%; HEIGHT: 100%">
									<legend>
										<strong>&nbsp;工作日历&nbsp;</strong></legend>
						<html:form action="/system_mgr/updateCalendarDetail" method="POST">
									<TABLE height="100%" cellSpacing="2" cellPadding="2" width="100%" height="100%" align="center" border="0" >
										<TBODY>
											<TR>
												<td width="5%"></td>
												<TD align="center" width="78%">
													<table height="100%" width="100%" border="0">
														<tr height="10%">
															<td align="center"> 
																<font face="Arial">
																	<span style="FONT-WEIGHT: 700; FONT-SIZE: 16pt">
																		<IMG style="CURSOR: hand" height="20" src="../image/up.jpg" width="20" border="0" onclick="return _subMonth()">
																			<script language="javascript">
																				document.write(year);
																			</script>
																			年
																			<script language="javascript">
																				document.write(month);
																			</script>
																			月
																		<IMG style="CURSOR: hand" height="20" src="../image/down.jpg" width="20" border="0" onclick="return _addMonth()">
																	</span>
																</font>
															</td>
														</tr>
														<tr>
															<td>
																<TABLE id="calendar" height="100%" cellSpacing="1" width="100%" border="0" class="tbcolor" >
																<TR class="middle" >
																	<TD align="center" height="20">日</TD>
																	<TD align="center" height="20">一</TD>
																	<TD align="center" height="20">二</TD>
																	<TD align="center" height="20">三</TD>
																	<TD align="center" height="20">四</TD>
																	<TD align="center" height="20">五</TD>
																	<TD align="center" height="20">六</TD>
																</TR>
																<%
																	List list = (List)request.getAttribute("Calendar");
																	//if(list!=null && list.size()!=0)
																	//{
																	for(int i=0;i<35;i++)
																	{
																		if(i%7==0)
																		{
																%>
																			<tr >
																<%
																		}
																%>
																			<td align="center" bgcolor="#ffffff" height="30">
																<%
																		if(list!=null && list.size()!=0)
																		{
																			if(!list.get(i).equals(""))
																			{
																%>
																					<%=((CalendarCell)list.get(i)).getDay()%>
																				<br>
																				<input type="checkBox" name="workDay" value="<%=((CalendarCell)list.get(i)).getDay()%>"
																					<%
																						if(((CalendarCell)list.get(i)).isSetting())
																						{
																					%>
																							checked
																					<%
																						}
																					%>
																				/>
																				
																		<%
																			  }
																		}
																		
																		%>
																	</td>
																<%
																		if(i%7==6)
																		{
																%>
																			</tr>
																<%																			
																		}
																	 }
																%>
														</TABLE>
															</td>
														</tr>
													</table>	
												</TD>
												<TD align="center" vAlign="bottom">
														<img border="0" src="../image/selected.gif" width="13" height="13"> 工作日&nbsp;&nbsp;
													<p>
														<img border="0" src="../image/notselected.gif" width="13" height="13"> 非工作日
													</p>
													<p>
													</p>
													<P>
													</P>
													<p>
													</p>
													<p>
														<logic:present name="year" scope="request">
															<input type="hidden" name="calYear" value="<bean:write name="year"/>"/>
														</logic:present>
														<logic:present name="month" scope="request">
															<input type="hidden" name="calMonth" value="<bean:write name="month"/>"/>
														</logic:present>
														<html:submit styleClass="input-button" value="保存"/>
													</p>
												</TD>
											</TR>
										</TBODY></TABLE>
							</html:form>
								</fieldset></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</body>
</html:html>
