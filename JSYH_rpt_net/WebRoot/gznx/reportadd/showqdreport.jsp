<!-- ��ҳ���Ǳ�����ǰչʾexcelҳ�� -->
<%@ page contentType="text/html; charset=gb2312" language="java" errorPage=""  import="java.io.*"%>
<%@ page session="true" import="java.lang.StringBuffer,java.util.Map, com.runqian.report4.util.ReportUtils,com.runqian.report4.*"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/runqianReport4.tld" prefix="report" %>
<html>
<title>��ҵ����</title>
<head>
	<html:base/>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	
	<link href="../../css/common.css" type="text/css" rel="stylesheet">
	<script language="javascript" src="../../runqianReport.js"></script>
	<style type="text/css">
   		#descDiv td{border:1px solid black;}
   		#descTable td{border:none;}
   	</style>
   	<script type="text/javascript">
   		window.onload=function(){
  			//��˲鿴������Ҫ��ʾ���ӱ�ע�Ĺ���
		  if("${type}"=="search")
		  {
		  	document.getElementById("addRemarkTr").style.display="none";
		  }
		  //�����޸ģ�����ʾ���ӱ�ע�Ĺ���
		  else if("${type}"=="marge")
		  	document.getElementById("addRemarkTr").style.display="block";
		  //������� ����Ϊ��ʾ���ӱ�ע
		  else
		  	document.getElementById("addRemarkTr").style.display="block";
  		}
  		/****���ƶ�div��ע��					************/
	var currentMoveObj=null;
   		var relLeft,relTop;
   		
   		function ondown(obj,e)
   		{
   			currentMoveObj=document.getElementById("descDiv");//��ȡ�˶���
   			var x=e.clientX;//��ȡ��ǰx,y����
   			var y=e.clientY;
   			
   			relLeft=x-currentMoveObj.style.pixelLeft;//��ǰx�����굽�ؼ���߿�ľ���
   			relTop=y-currentMoveObj.style.pixelTop;//���ϱ߿�ľ���
   			currentMoveObj.style.position="absolute";//���Զ�λ
   		}
   		
   		window.document.onmouseup=function()
   		{
   			currentMoveObj=null;//���
   		}
   		
   		function moves(e)
   		{
   		 	if(currentMoveObj!=null)
   		 	{
   		 		var x=e.clientX;//��ȡ��ǰx,y����
   				var y=e.clientY;
   		 		currentMoveObj.style.pixelLeft=x-relLeft;//ͨ����ǰ�����x�����굽�ؼ���߿�ľ������ �õ��ƶ���Ŀؼ�����߾�
   		 		currentMoveObj.style.pixelTop=y-relTop;//ͬ�ϻ���ϱ߾�
   		 	}
   		}
   		
   		function showthis(obj,e)
   		{
   			alert(e.clientX);
   			alert(e.clientY);
   		}
   	/****end *���ƶ�div��ע��					************/
   	</script>	
</head>
<body  >
<jsp:include page="qdtoolbar.jsp" flush="false" />
		<table width="98%" border="0" cellpadding="2" cellspacing="1" align="center">
			<tr>
				<td> 
				<input type="button" class="input-button" value=" �رմ��� " onclick="javascript:window.close()">
				</td>
			
			</tr>
		</table>
		<%             
			request.setCharacterEncoding( "GBK" );
			Map reportMap = (Map)session.getAttribute("reportParam");
			StringBuffer param = new StringBuffer("");
			String filename = (String)reportMap.get("filename");
			//��ȡ�����ͺ�
			param.append("RangeID").append("=").append(reportMap.get("RangeID")).append(";");
			param.append("Freq").append("=").append(reportMap.get("Freq")).append(";");
			param.append("CCY").append("=").append(reportMap.get("CCY")).append(";");
			param.append("ReptDate").append("=").append(reportMap.get("ReptDate")).append(";");
			param.append("OrgID").append("=").append(reportMap.get("OrgID")).append(";");				
			param.append("RepID").append("=").append(reportMap.get("RepID")).append(";");
			String tablename = "AF_QD_"+(String)reportMap.get("templateId");
			String countquer = "query('SELECT count(*) FROM "+tablename+" where rep_id="+reportMap.get("RepID")+"')";
			System.out.println(countquer);
			%>			
		<%--Nick:2013-07-15--ע��ԭ�б�ǩ���˱�ǩ�������ҳ��windows���ص���Excelʱ�ᱨ��ͬʱLinux��������ʱ������������������ÿ��sheet�е����ݼ�¼����һ��
		<report:extHtml 
			name="report1" 
			reportFileName="<%=filename%>"
			totalCountExp="<%=countquer%>"
			pageCount="25"			
			funcBarLocation=""
			needPageMark="yes"
			needLinkStyle="yes"
			params="<%=param.toString()%>"
			startRowParamName="startRow" 			
			endRowParamName="endRow"   
			exceptionPage="/gznx/reportadd/myError2.jsp"
		/>
		--%>
		<report:html  name="report1" 
		    reportFileName="<%=filename%>"
			params="<%=param.toString()%>"
			exceptionPage="/gznx/reportadd/myError2.jsp" 
			funcBarLocation=""
			needPageMark="no" 
			generateParamForm="no" 
		    needPageMark="yes"
		    needScroll="yes" 
		    scrollWidth="100%"
			scrollHeight="100%"
			selectText="no"
		    height="-2"
		    scale="1.0"
		/>
	<%
		if(request.getAttribute("repDesc")!=null && !request.getAttribute("repDesc").equals(""))
		{
	 %>
	<div id="descDiv" style="width:325px;height:315px;left:688px;top:121px;text-align:middle;position:absolute;">
    		<table width="325px" cellspacing="0" style="text-align:center;position: absolute;" cellpadding="0" >
    		<tr onmousemove="moves(event)" onmousedown="ondown(this,event)" style="cursor: move">
    			<td style="background-color: #8DCAFC;height:25px;font-size:13px;font-weight: bold;border-bottom: none">
    				��ע��
    			</td>
    		</tr>
    		<tr>
    			<td width="100%" style="text-align: center;padding: 0px;margin: 0px">
    				<table width="98%" height="98%" style="border:none">
    					<tr>
    						<td style="border:none"><textarea cols="10" rows="8" style="vertical-align: top;width:100%;overflow: auto;background-color: #f7f4f4" readonly="readonly" >${repDesc}</textarea>
    						</td>
    					</tr>
    				</table>
    			</td>
    		</tr>
    		<tr id="addRemarkTr">
    			<td style="border-top: none">
    				<table width="98%" height="98%" style="border:none">
    					<tr>
    						<td style="border:none"><textarea cols="10" rows="4" id="descTextarea" style="vertical-align: top;width:100%;overflow: auto">
    						</textarea></td>
    					</tr>
    				</table>
    			</td>
    		</tr>
    	</table> 
    </div>	
    <%
    	}
     %>			
    <script language="javascript">
    	//���÷�ҳ��ʾֵ
		document.getElementById( "t_page_span" ).innerHTML=report1_getTotalPage();
		document.getElementById( "c_page_span" ).innerHTML=report1_getCurrPage();   
    </script>
</body>
</html>
