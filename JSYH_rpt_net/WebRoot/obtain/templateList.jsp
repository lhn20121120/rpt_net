<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:html locale="true">
        <head>	
	    <title>模板列表</title>
	    <meta http-equiv="Content-Type" content="text/html; charset=gbk">
	    <meta http-equiv="Pragma" content="no-cache">
	    <meta http-equiv="Cache-Control" content="no-cache">
	    <meta http-equiv="Expires" content="0">
	    <LINK REL="StyleSheet" HREF="<bean:write name="css"/>" TYPE="text/css">
	</head>
	<body>	
           <table width="95%" border="0" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
              <tr class="titletab">
                 <td>
                       		      <form name="form1" method="post" action="xxxx.do">
                 
      		    <table width="100%" border="0" align="center" cellpadding="4" cellspacing="1">				      
      		       <tr>
      		          <td align="right">
      		             模板名称：<input class="input-text" type="text" size="28" name="reportName">
      		          </td>
      		          <td>
      		             版本号：<INPUT class="input-text" id="Text2" type="text" size="10" name="versionId">
      		          </td>
      		          <td>
      		             <INPUT class="input-button" id="Button3" type="submit" value=" 查 询 " name="Button1">
      		          </td>
      		          
      		       </tr>
      		     
      		    </table>
      		     </form>
                 </td>
              </tr>
              <tr>
      	         <td bgcolor="#ffffff">
      		    <table width="100%" border="0" align="center" cellpadding="4" cellspacing="1">				      
      		       <tr class="titletab">
				<th colspan="7" align="center" id="list">
					<strong>
						报表模板列表
					</strong>
				</th>
			</tr>
			<TR class="middle">
				<TD class="tableHeader" width="5%">
					<b>编号</b>
				</TD>
				<TD class="tableHeader" width="30%">
					<b>报表模板名称</b>
				</TD>
				<TD class="tableHeader" width="10%">
					<b>版本号</b>
				</TD>
				<TD class="tableHeader" width="20%">
					<b>报表类型</b>
				</TD>
				<TD class="tableHeader" width="10%">
					<b>货币单位</b>
				</TD>
				<TD class="tableHeader" width="10%">
					<b>是否发布</b>
				</TD>
				<TD class="tableHeader" width="15%">
					<b>操作</b>
				</TD>									
			</TR>
			<TR bgcolor="#FFFFFF">
				<TD align="center">G1302</TD>
				<TD align="center">					
					<a href="/SMIS_IN_CA/selfReadExcelServlet?childRepId=G1302&versionId=0512" >
						G13最大十家次级类贷款情况表
					</a>
				</TD>
				<TD align="center">0512</TD>
				<TD align="center">基础报表-信用风险</TD>
				<TD align="center">万元</TD>
				<TD align="center">未发布</TD>
				<td align="center">
				   <a href="templateConfigurePre.do">配置数据源</a>
				   <a href="templateDataBuild.do">数据生成</a>
				</td>
			 </tr>	
			<TR bgcolor="#FFFFFF">
				<TD align="center">G1302</TD>
				<TD align="center">					
					<a href="/SMIS_IN_CA/selfReadExcelServlet?childRepId=G1302&versionId=0512" >
						G13最大十家次级类贷款情况表
					</a>
				</TD>
				<TD align="center">0512</TD>
				<TD align="center">基础报表-信用风险</TD>
				<TD align="center">万元</TD>
				<TD align="center">未发布</TD>
				<td align="center">
				   <a href="templateConfigurePre.do">配置数据源</a>
				</td>
			 </tr>	
			<TR bgcolor="#FFFFFF">
				<TD align="center">G1302</TD>
				<TD align="center">					
					<a href="/SMIS_IN_CA/selfReadExcelServlet?childRepId=G1302&versionId=0512" >
						G13最大十家次级类贷款情况表
					</a>
				</TD>
				<TD align="center">0512</TD>
				<TD align="center">基础报表-信用风险</TD>
				<TD align="center">万元</TD>
				<TD align="center">未发布</TD>
				<td align="center">
				   <a href="templateConfigurePre.do">配置数据源</a>
				</td>
			 </tr>	
			<TR bgcolor="#FFFFFF">
				<TD align="center">G1302</TD>
				<TD align="center">					
					<a href="/SMIS_IN_CA/servlets/selfReadExcelServlet?childRepId=G1302&versionId=0512" >
						G13最大十家次级类贷款情况表
					</a>
				</TD>
				<TD align="center">0512</TD>
				<TD align="center">基础报表-信用风险</TD>
				<TD align="center">万元</TD>
				<TD align="center">未发布</TD>
				<td align="center">
				   <a href="templateConfigurePre.do">配置数据源</a>
				</td>
			 </tr>	
			<TR bgcolor="#FFFFFF">
				<TD align="center">G1302</TD>
				<TD align="center">					
					<a href="/SMIS_IN_CA/servlets/selfReadExcelServlet?childRepId=G1302&versionId=0512" >
						G13最大十家次级类贷款情况表
					</a>
				</TD>
				<TD align="center">0512</TD>
				<TD align="center">基础报表-信用风险</TD>
				<TD align="center">万元</TD>
				<TD align="center">未发布</TD>
				<td align="center">
				   <a href="templateConfigurePre.do">配置数据源</a>
				</td>
			 </tr>	
      		    </table>
      		 </td>
      	      </tr>    
           </table>
	</body>    
</html:html>