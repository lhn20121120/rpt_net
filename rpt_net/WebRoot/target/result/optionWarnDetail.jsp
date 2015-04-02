
<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
	String deId = request.getAttribute("DeId") != null ? request.getAttribute("DeId").toString() : "";
	String repFreId = request.getAttribute("repFreId") != null ? request.getAttribute("repFreId").toString() : "" ;
	String dataRangeId = request.getAttribute("dataRangeId") != null ? request.getAttribute("dataRangeId").toString() : "";
	String orgId = request.getAttribute("orgId") != null ? request.getAttribute("orgId").toString() : "";
%>
<jsp:useBean id="targetForm" scope="request" class="com.cbrc.smis.form.UtilTargetForm">
	<jsp:setProperty name="targetForm" property="rangeId" value="<%=deId%>"/>	
	<jsp:setProperty name="targetForm" property="repFreId" value="<%=repFreId%>"/>
	<jsp:setProperty name="targetForm" property="dataRangeId" value="<%=dataRangeId%>"/>
	<jsp:setProperty name="targetForm" property="orgId" value="<%=orgId%>"/>
</jsp:useBean>
<jsp:useBean id="utiltargetDefineForm" scope="page" class="com.fitech.net.form.UtilTargetDefineForm" />
<jsp:useBean id="configBean" scope="page" class="com.cbrc.smis.common.Config"/>
<jsp:useBean id="tpForm" scope="page" class="com.cbrc.smis.form.TargetPreForm"/>
<jsp:useBean id="figure" scope="request" class="com.fitech.net.bean.Figure"/>

<script language="JavaScript" >

	function getData(){
		var year="";
		var startMonth="1";
		var endMonth="12";
		//选中年份列表框
		var yearList=document.getElementById('yearList');
		//选中年份列表框中的项目
		var selectYearList=yearList.options;       
		if(selectYearList.selectedIndex<0){
			alert("请选择要查看的年份!");
			return;
		}else{
			for(var i=0;i<selectYearList.length;i++){
				if(yearList.options[i].selected){
					year+=yearList.options[i].value+",";          			
				}
			}
		}
		//选中的起始月份
		startMonth=document.getElementById('startMonth').options.value; 
		//选中的结束月份
		endMonth=document.getElementById('endMonth').options.value;
          	
		if((startMonth-endMonth)>0)	{
			alert("起始月份不能大于结束月份");
			return ;
		}
          	
		var repFreId=<%=request.getAttribute("repFreId")%>
		var dataRangeId=<%=request.getAttribute("dataRangeId")%>
		var t_Id=<%=request.getAttribute("tid")%>
		var DeId=<%=request.getAttribute("DeId")%>
		var curpage=<%=request.getAttribute("curPage")%>
		var orgId='<%=request.getAttribute("orgId")%>';
     //	  if(repFreId=='2');
     	  
     	  
   	window.location.href="<%=request.getContextPath()%>/target/viewWarnDetail.do?year="+year
   						+"&startMonth="+startMonth
   						+"&endMonth="+endMonth
   						+"&id="+t_Id
   						+"&targetDefineId="+DeId
   						+"&curPage="+curpage
   						+"&repFreId="+repFreId
   						+"&dataRangeId="+dataRangeId
   						+"&orgId="+orgId;

          }
          
    </script>
<html:html locale="true">
<head>
	<html:base/>
<title> 指标定义</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" rel="stylesheet" type="text/css">

	<jsp:include page="../../calendar.jsp" flush="true">
		  <jsp:param name="path" value="../../"/>
		</jsp:include>
	<script language="javascript" src="../../js/func.js"></script>
	
	
</head>

<body >
		<logic:present name="Message" scope="request">
			<logic:greaterThan name="Message" property="size" value="0">
				<script language="javascript">
					alert("<bean:write name='Message' property='alertMsg'/>");
				</script>
			</logic:greaterThan>
		</logic:present>

	<table border="0" width="98%" align="center">
		
		<tr>
			 <td>当前位置 >> 	指标分析>> 	指标分析预警</td>
		</tr>
		
	</table>
   <table>
   <tr>
   <td height="10"></td>
   </tr>
   </table>
			<TABLE name="tbl" id="tbl" cellSpacing="1" cellPadding="1" width="98%" border="0" class="tbcolor" align="center">
      				<tr id="tbcolor">
            			<th align="center">
	            			
	            			<strong>
		                    <font face="Arial" size="2">
												
			                      预警信息 
												
		                     </font>
	                         </strong>
            			</th>
      				</tr>
   					<tr>
       					 <td align="right" bgcolor="#FFFFFF">   
       					  <html:form action="/target/viewPreStandDetail" method="Post" styleId="form1" >
       					        	
						<table width="100%"  border="0" >
									
									<TR>
										<TD align="right">
											 指标名称
										</TD>
										<TD>
											<html:text styleId="targetDefineName" property="targetDefineName" size="20" styleClass="input-text" readonly="true" />
										</TD>
										<TD align="right"> &nbsp;</TD>
										
										</TR><TR>
										<TD align="right"> &nbsp;指标值</TD>
										<TD>
										<html:text styleId="allWarnMessage" property="allWarnMessage" size="20" styleClass="input-text" readonly="true" />
											
										</TD>
										<TD align="right">下限</TD>
										<TD>
										<html:text styleId="temp1" property="temp1" size="15" styleClass="input-text" style="text" readonly="true" />
										</TD></TR><tr>
										<td align="right">上限</td>
										<td>
										<html:text styleId="temp2" property="temp2" size="20" styleClass="input-text"  readonly="true"/>
										</td>
										<td align="right"> &nbsp;颜色</td>				
										<TD >
						  						<logic:present name="actuTargetResultForm" property="color">							
														<label  style="background:<bean:write name="actuTargetResultForm" property="color"/>">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
												</logic:present>					
										</TD>
					                   </tr>
									<tr>
										<td align="right">公式</td>
										<td colspan="3">
											<html:text  property="preFormula" size="20" styleClass="input-text" style="width:79.5% " readonly="true" />
										</td>
									</tr>
									
								</table>
							
							</html:form>
		  </td>
   </tr>    
 </table><br/>

<logic:present name="figure" scope="request"> 
	<TABLE name="tb2" id="tb2" cellSpacing="1" cellPadding="1" width="98%" border="0" class="tbcolor" align="center">
	      	      		<tr id="tbcolor">
	            			<th align="center">	            			
		            			<strong>
			                    <font face="Arial" size="2">												
				                     指标分析预警图												
			                     </font>
		                         </strong>
	            			</th>
	      				</tr>
	   					<tr>
	       					 <td align="right" bgcolor="#FFFFFF">  
	              	
							<table width="100%"  border="0" >
	      		<tr> 	 
			     	<TD align="right">
			     		请选择要分析的年份:
			     	</TD>
			     	<TD>
		      			<html:select styleId="yearList" name="targetForm" property="years" multiple="true" size="4" style="width:100">
		      			  					
							<html:optionsCollection name="targetForm" property="years"/>
						</html:select>	      		
	      			</TD>	      		     			
		      			<TD align="right">
		      				起始月份:
		      			</TD>
		      			<TD>
		      				<html:select styleId="startMonth" name="tpForm" property="month">
		      					<html:option value="1">1月</html:option>	
		      					<html:option value="2">2月</html:option>
		      					<html:option value="3">3月</html:option>
		      					<html:option value="4">4月</html:option>
		      					<html:option value="5">5月</html:option>
		      					<html:option value="6">6月</html:option>
		      					<html:option value="7">7月</html:option>	
		      					<html:option value="8">8月</html:option>
		      					<html:option value="9">9月</html:option>
		      					<html:option value="10">10月</html:option>
		      					<html:option value="11">11月</html:option>
		      					<html:option value="12">12月</html:option>
		      				</html:select>
		      			</TD>
		      			<TD align="right">
		      				结束月份:
		      			</TD>
		      			<TD>
		      			
		      				<select id="endMonth" >
		      					<option value="1">1月</option>	
		      					<option value="2">2月</option>
		      					<option value="3">3月</option>
		      					<option value="4">4月</option>
		      					<option value="5">5月</option>
		      					<option value="6">6月</option>
								<option value="7">7月</option>	
		      					<option value="8">8月</option>
		      					<option value="9">9月</option>
		      					<option value="10">10月</option>
		      					<option value="11">11月</option>
		      					<option selected value="12" >12月</option>
		      				</select>
		      			</TD>
			   </tr> 
			   <tr>
			   <td>
			   </td>
			   <td></td><td></td><td></td>
			   	<td align="right">
			   	<html:submit styleClass="input-button"  value="确定" onclick="javascript:getData();"/>		   	
			   	</td>
			   </tr>
	 </table>
	 		  </td>
	   </tr>    
	 </table><br/>
 </logic:present>

	<table width="98%">
	<tr>
	<td colspan="6" align="right">
	 <html:button property="back" value="返  回" styleClass="input-button" onclick="history.back()" />
    </td>
    </tr>
    </table>
    <br>
</body>

</html:html>