<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="java.util.*" %>
<%@ page import="com.fitech.net.config.Config" %>
<%@ page import="com.fitech.net.form.IDataRelationForm" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%	
	String childRepId = request.getParameter("childRepId")==null?(request.getAttribute("childRepId")!=null?request.getAttribute("childRepId").toString():null):request.getParameter("childRepId");
	String versionId = request.getParameter("versionId")==null?(request.getAttribute("versionId")!=null?request.getAttribute("versionId").toString():null):request.getParameter("versionId");
	String css=request.getParameter("css")==null?(request.getAttribute("css")!=null?request.getAttribute("css").toString():null):request.getParameter("css");
	String obtain_js=request.getParameter("obtain_js")==null?(request.getAttribute("obtain_js")!=null?request.getAttribute("obtain_js").toString():null):request.getParameter("obtain_js");
	String idrId=request.getParameter("idrId")==null?(request.getAttribute("idrId")!=null?request.getAttribute("idrId").toString():null):request.getParameter("idrId");
	
	HashMap map = new HashMap();
	if(session.getAttribute(Config.DATA_RELATION_IS_SET) != null) map = (HashMap)session.getAttribute(Config.DATA_RELATION_IS_SET);
	
	IDataRelationForm iDataRelationForm = map.get(idrId) != null ? (IDataRelationForm)map.get(idrId) : null;
	if(map.containsKey(idrId)) iDataRelationForm = (IDataRelationForm)map.get(idrId);
		
	String idrRelative= iDataRelationForm != null ? iDataRelationForm.getIdrRelative() : null;
	String idrFormula = iDataRelationForm != null ? iDataRelationForm.getIdrFormula() : null;
	String idrDefaultvalue = iDataRelationForm != null ? iDataRelationForm.getIdrDefaultvalue() : null;
	String idrInitvalue = iDataRelationForm != null ? iDataRelationForm.getIdrInitvalue() : null;
%>
<jsp:useBean id="utilCellForm" scope="page" class="com.fitech.net.form.UtilCellForm" />
<jsp:setProperty property="childRepId" name="utilCellForm" value="<%=childRepId%>"/>
<jsp:setProperty property="versionId" name="utilCellForm" value="<%=versionId%>"/>

 
<html:html locale="true">
	<head>	
		<title>模板列表</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gbk">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<LINK REL="StyleSheet" HREF="<%=css%>" TYPE="text/css">
		<script language="JavaScript" src="<%=obtain_js%>"></script>
	</head>
	<body onload="initPage()">
		<html:form action="/template/saveProTmpt.do" enctype="multipart/form-data">
			<table width="98%" border="0" align="center">
				<tr><td></td></tr>
				<tr class="titletab">
					<td>
      		    	<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">				      
						<input type="hidden" name="childRepId" value="<%=childRepId%>">
						<input type="hidden" name="versionId" value="<%=versionId%>">
      		       		<tr>
      		          		<td width="25%">&nbsp;单元格项：
								<html:select property="idrId" styleId="idrId" onchange="changeCell()">									
									<html:optionsCollection name="utilCellForm" property="cellList"/>
								</html:select>
							</td>
							<td width="40%">
								<table border="0">
									<tr><td>关联方式：</td>
										<td align="left">
											<input id="radio1" type="radio" value=1 onclick="radio1OnClick()">业务系统生成&nbsp;&nbsp;<input type="button" id="button1" style="display:none" value="进入公式设定" onclick="setFormula()"><br>
			   		             		    <input id="radio2" type="radio" value=2 onclick="radio2OnClick()">手工维护&nbsp;&nbsp;&nbsp;&nbsp;<input id="text2" style="display:none" value=0><br>
			   		             		    <input id="radio3" type="radio" value=3 onclick="radio3OnClick()">计算项<br>	    
									  	</td>									  	
									</tr>
								</table>   		             		             		    
							</td>
      		          		<td align="left" width="32%"><label id="label1" style="display:none">公  式:&nbsp;</label>
      		          			<html:textarea styleId="formulaText" property="formulaText" cols="30" rows="5" readonly="true" style="display:none"/>
      		          		</td>
      		      		</tr>
      		      		<tr>
	      		      		<td colspan="2">
	      		      			<input type="checkbox" id="isInit" onclick="checkboxOnClick()">是否初始化数据&nbsp;&nbsp;
	      		      			<input type="text" id="init" style="display:none">
	      		      		</td>
      		      		</tr>      		      		
					</table>      		    	
				</td>
			</tr>  
			<tr>
				<td align="right">
					<INPUT class="input-button" id="modify" type="button" value="确认设定" onclick="saveDateToSession()">
	      		    <INPUT class="input-button" id="modify" type="button" value="纪录查看" onclick="showAllData()">
				</td>
      		    <td>&nbsp;&nbsp;</td>
			</tr> 	
            <tr>
				<td align="center">
					<INPUT class="input-button" id="save" type="button" value=" 保存设定 " onclick="_submit()">
      		      		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<INPUT class="input-button" id="Button3" type="button" value=" 返   回 " onclick="window.history.back();">
      		      		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				</td>
			</tr>
		</table>
	</html:form>
	
	<script language="javascript">		
		function radio1OnClick(){
			document.getElementById("radio1").checked=true;
			document.getElementById("radio2").checked=false;
			document.getElementById("radio3").checked=false;
			document.getElementById("text2").style.display="none";
			document.getElementById("button1").style.display="";
			document.getElementById("formulaText").style.display="";
			document.getElementById("label1").style.display="";
		}
		
		function radio2OnClick(){
			document.getElementById("radio1").checked=false;
			document.getElementById("radio2").checked=true;
			document.getElementById("radio3").checked=false;
			document.getElementById("text2").style.display="";
			document.getElementById("button1").style.display="none";
			document.getElementById("formulaText").style.display="none";
			document.getElementById("label1").style.display="none";
		}
	    function radio3OnClick(){
		    document.getElementById("radio1").checked=false;
			document.getElementById("radio2").checked=false;
			document.getElementById("radio3").checked=true;
			document.getElementById("text2").style.display="none";
			document.getElementById("button1").style.display="none";
			document.getElementById("formulaText").style.display="none";
			document.getElementById("label1").style.display="none";
		}
		function checkboxOnClick(){
			if(document.getElementById("isInit").checked==false){
				document.getElementById("isInit").checked=false;
				document.getElementById("init").style.display="none";
			}else{
				document.getElementById("isInit").checked=true;
			    document.getElementById("init").style.display="";			    
			}
	    }
	    
	    function initPage(){   
	       if("<%=idrId%>"!=null&&"<%=idrId%>"!="null"){
	          var cellList=document.getElementById("idrId");
	          cellList.value = "<%=idrId%>";
              if("<%=idrRelative%>"=="1"){
                 radio1OnClick();
                 document.getElementById("formulaText").value="<%=idrFormula%>";
              }
              if("<%=idrRelative%>"=="2"){
                 radio2OnClick();
                 document.getElementById("text2").value="<%=idrDefaultvalue%>";
              }
              if("<%=idrRelative%>"=="3"){
                 radio3OnClick();
              }
		  	  var idrInitvalue="<%=idrInitvalue%>";
		  	  
		  	  if(idrInitvalue!=null&&idrInitvalue!="null"){
		  	     document.getElementById("isInit").checked=true;
		         document.getElementById("init").style.display="";
		         document.getElementById("init").value="<%=idrInitvalue%>";
		  	  }
	       }else{
	       	  var idrId=document.forms[0].idrId.value;
	       	  window.location="<%=request.getContextPath()%>/template/changeProTmpt.do?idrId="+idrId+"&childRepId=<%=childRepId%>&versionId=<%=versionId%>&method=changeCell";       
	       }
	    }
	    
	    function changeCell(){
	       var idrId=document.forms[0].idrId.value;
	       window.location="<%=request.getContextPath()%>/template/changeProTmpt.do?idrId="+idrId+"&childRepId=<%=childRepId%>&versionId=<%=versionId%>&method=changeCell";       
	    }
	    
	    function saveDateToSession(){	       
	       var idrId=document.forms[0].idrId.value;
	       var idrRelative=null;
	       if(document.getElementById("radio1").checked==true)	       
	           idrRelative=1;
	       
	       if(document.getElementById("radio2").checked==true)	       
	           idrRelative=2;
	       
	       if(document.getElementById("radio3").checked==true)	       
	           idrRelative=3;
	      	       
	       var idrDefaultvalue=null;
	       var idrInitvalue=null;
	       
	       if(idrRelative==null){
	          alert("请选择关联方式！")
	          return false;
	       }
	       if(idrRelative==1){
	          alert("业务系统生成数据无法在此保存");
	          return false;
	       }
	       if(idrRelative==2){
	          idrDefaultvalue=document.getElementById("text2").value;
	          if(idrDefaultvalue==null||idrDefaultvalue.Trim()==''){
	             alert("请输入手工维护的值");
	             document.getElementById("text2").focus();
	             return false;
	          }
	       }
	       if(document.getElementById("isInit").checked==true){
	          idrInitvalue=document.getElementById("init").value;
	          if(idrInitvalue==null||idrInitvalue.Trim()==''){
	             alert("请输入初始化数据");
	             document.getElementById("init").focus();
	             return false;
	          }
	       }
	       var url="<%=request.getContextPath()%>/template/changeProTmpt.do?method=saveDateToSession&idrId="+idrId+"&childRepId=<%=childRepId%>&versionId=<%=versionId%>";
		if(idrRelative!=null)
	       	url+="&idrRelative="+idrRelative;
	       	if(idrRelative==2)
	       		url+="&idrDefaultvalue="+idrDefaultvalue;
	       	if(idrInitvalue!=null&&idrInitvalue!='')
	       		url+="&idrInitvalue="+idrInitvalue;	       
	       window.location=url;
	    }
	    
	    function _submit(){
	        window.parent.location="<%=request.getContextPath()%>/template/saveProTmpt.do?childRepId=<%=childRepId%>&versionId=<%=versionId%>";
	    }
	    
	    function showAllData(){
	       var url= "<%=request.getContextPath()%>/template/changeProTmpt.do?method=showAllData&childRepId=<%=childRepId%>&versionId=<%=versionId%>";
	       newwin =  window.open(url,"已设定单元格列表","scrollbars=yes,left=200,top=200,height=400,width=800,status=no,toolbar=no,menubar=no,location=no,resizable=no,");
           newwin.focus();
	    }
	    function setFormula(){
	    	var list = document.getElementById("idrId");	    		    	
	    	window.parent.location="<%=request.getContextPath()%>/template/viewDataRelationFormualSetting.do?idrId=" +list.value; 
	    }	
	    String.prototype.Trim=function(){
			return this.replace(/(^\s*)|(\s*$)/g,"");
		}    
	</script> 	
	</body>  	 
</html:html>