<!-- 该页面是报表润前展示excel页面 -->
<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/runqianReport4.tld" prefix="report"%>
<%@ page session="true" import="java.lang.StringBuffer,java.util.Map"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<%
	/** 报表选中标志 **/

	Map reportMap = (Map)request.getAttribute("reportParam");
	
	StringBuffer param = new StringBuffer("");
	String filename = (String)reportMap.get("filename");
	String reqquestUrl = "/editFXAFReport.do?"+(String)reportMap.get("requestUrl")+"&saveFlg=1";
	//获取参数和宏
	param.append("RangeID").append("=").append(reportMap.get("RangeID")).append(";");
	param.append("Freq").append("=").append(reportMap.get("Freq")).append(";");
	param.append("CCY").append("=").append(reportMap.get("CCY")).append(";");
	param.append("ReptDate").append("=").append(reportMap.get("ReptDate")).append(";");
	param.append("OrgID").append("=").append(reportMap.get("OrgID")).append(";");

	String backQry = "";
	if (session.getAttribute("backQry") != null){
		backQry = (String) session.getAttribute("backQry");
	}
	String templateId = (String)reportMap.get("templateId");
%>
<html>
<title>企业报表</title>
<head>
	<html:base/>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" type="text/css" rel="stylesheet">
	<script language="javascript" src="../../js/func.js"></script>
	<script language="javascript" src="<%=Config.WEBROOTULR%>/js/prototype-1.4.0.js"></script>
	<jsp:include page="../../calendar.jsp" flush="true">
		<jsp:param name="path" value="../../" />
	</jsp:include>
	<SCRIPT language="javascript">
	function doload(){
	  var os = document.getElementsByTagName("span");
	  var o = null;
	  var i=0;
	  for(i=0;i<os.length;i++){
	    if(os[i].innerHTML.indexOf("提交")!=-1){
	      os[i].style.visibility="hidden";
	      os[i].style.position="absolute";
	    }       
	  }
	}
		    //失败处理
	    function reportError(request)
	    {
	        alert('系统忙，请稍后再试...！');
	    }
	

   
	function saveReport(){         
		_submitTable( report1 );	  
	}
	function _back(){
		window.location = "viewGatherReport.do?<%=backQry%>";	
	}
	
	        function locationtb(objName){
	   //     self.moveTo(0,0);
       //     self.resizeTo(screen.availWidth,screen.availHeight);
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
	     		function fillColor(objName){
		  var tb = document.getElementById(objName);
		  var tds = tb.getElementsByTagName('td');
		  var i;
		  for(i=0;i<tds.length;i++){
		    var tmp=tds[i].getAttribute('inputDataType');
		    if(tmp!=null && tmp!=''){
		     tds[i].style.background='#D1F2FE';
	//	     tds[i].style.borderColor='#FB3004';
		     tds[i].title='请填报';
		    }	         
		  }        
		}


</SCRIPT>	
</head>
<body onload="doload()" >
<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
    <table border="0" width="100%" align="center">
			<tr height="30">
				<td>
					当前位置 &gt;&gt; 报表采集 &gt;&gt; 报表在线填报
				</td>
			</tr>
		</table>
		<table id=titleTable width=100% cellspacing=0 cellpadding=0 border=0  align="center"><tr>
		<td height="25" width=100% valign="middle"  style="font-size:13px" background="<%=request.getContextPath()%>/image/toolbar-bg.gif">
		<table width="100%">
			<tr align="center">
				<td>
					<input type="button" class="input-button" value=" 保 存 " onclick="saveReport()">
					&nbsp;&nbsp;&nbsp;		
					<input type="button" class="input-button" value=" 导出EXCEL "onclick="report1_saveAsExcel();return false;">
					&nbsp;&nbsp;&nbsp;
					<input type="button" class="input-button" value=" 返 回 "onclick="_back()">
					&nbsp;&nbsp;&nbsp;
				</td>			
			</tr>			
			<tr>
			
			</tr>
		</table>
		</td>
		</tr></table>
		
			
             <report:html name="report1"               
 					reportFileName="<%=filename%>"
					funcBarLocation="top" 
					needPageMark="no"
					generateParamForm="no"
					needLinkStyle="yes"	 
					params="<%=param.toString()%>"
					saveAsName = "<%=templateId%>"

					validOnSubmit="no"
					needScroll="yes"  
					scrollWidth="100%"
					scrollHeight="100%"
					selectText="yes"
					backAndRefresh="yes"  
	  				promptAfterSave="yes"  
					submitTarget="_self" 					
			 		keyRepeatError="yes" 
					inputExceptionPage="/gznx/reportadd/myError2.jsp"  
					saveDataByListener="no" 
					backAndRefresh="<%=reqquestUrl%>"/>
    <script language="javascript">
       locationtb('report1');
    </script>
    <br>
    <br>
</body>
</html>
