<!-- 该页面是报表润前展示excel页面 -->
<%@ page contentType="text/html; charset=gb2312" language="java" errorPage=""  import="java.io.*"%>
<%@ page session="true" import="java.lang.StringBuffer,java.util.Map, com.runqian.report4.util.ReportUtils,com.runqian.report4.*"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/runqianReport4.tld" prefix="report" %>

<html>
<title>企业报表</title>
<head>
	<html:base/>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	
	<link href="../../css/common.css" type="text/css" rel="stylesheet">
	<script language="javascript" src="../../runqianReport.js"></script>
	
</head>
<body  >

<SCRIPT language="javascript">
  

        function locationtb(objName){
	      
	        var tb = document.getElementById(objName);	        
		    var top=tb.style.top; // 表格距顶边高度
		    var height=tb.clientHeight; 
		    var endHeight = top + height;  // 表格底部高度
		    var except = document.body.clientHeight-endHeight; // 表格距底部高度
		    var except2=except/2;
		    if(except>200){
		      tb.style.position="absolute";
		      tb.style.top=top+except2-50;
		    }
		    var left=tb.style.left;
		    var width=tb.clientWidth;
		    var rwidth=left + width;
		    except = document.body.clientWidth-rwidth; // 表格距底部高度
		    except2=except/2;
		    if(except>200){
		      tb.style.position="absolute";
		      tb.style.left=left+except2-20;
		    }
		   
	     }	 

</SCRIPT>
    
<jsp:include page="toolbar.jsp" flush="false" />
		<table width="98%" border="0" cellpadding="2" cellspacing="1" align="center">
			<tr>
				<td> 
				<input type="button" class="input-button" value=" 关闭窗口 " onclick="javascript:window.close()">
				</td>
			
			</tr>
		</table>
		<%             

				Map reportMap = (Map)request.getAttribute("reportParam");
                
				StringBuffer param = new StringBuffer("");
				String filename = (String)reportMap.get("filename");
			
				
				//获取参数和宏
				
				param.append("templateId").append("=").append(reportMap.get("templateId")).append(";");
				param.append("versionId").append("=").append(reportMap.get("versionId")).append(";");
				param.append("cellIds").append("=").append(reportMap.get("cellIds")).append(";");
				param.append("orgIds").append("=").append(reportMap.get("orgIds")).append(";");
				param.append("curId").append("=").append(reportMap.get("curId")).append(";");
				param.append("datarangeId").append("=").append(reportMap.get("datarangeId")).append(";");
				param.append("startDate").append("=").append(reportMap.get("startDate")).append(";");
				param.append("endDate").append("=").append(reportMap.get("endDate")).append(";");
				param.append("repFreqId").append("=").append(reportMap.get("repFreqId")).append(";");
		%>			
             <report:html name="report1"               
 					 reportFileName="<%=filename %>"
					funcBarLocation="top" 				
					functionBarColor="#12632256" 
					funcBarFontFace="宋体" 
					funcBarFontSize="12pt" 
					funcBarFontColor="#B3B3FF" 					
					separator="&nbsp;" 					
					generateParamForm="no" 
					displayNoLinkPageMark="yes" 		 
					params="<%=param.toString()%>"					
					needOfflineInput="no" 
									
					needDirectPrint="no" 
					needScroll="yes" 
					scrollBorder="border:0.5px solid black"  
					scrollWidth="100%"
					scrollHeight="105%"
					selectText="yes"
					promptAfterSave="yes"  
			 		 keyRepeatError="yes"  
					inputExceptionPage="/jsp/myError.jsp"  					
					saveDataByListener="no" 
				    backAndRefresh=""/>
			
    <script language="javascript">       
       locationtb('report1');
    </script>
</body>
</html>
