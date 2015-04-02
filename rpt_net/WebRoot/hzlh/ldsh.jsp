<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd"> 

<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.cbrc.smis.other.Aditing"%>
<%@page import="com.cbrc.smis.common.Config"%>
<html>
  <head>
   <!--hzlh/ldsh.jsp-->
	<link href="<%=request.getContextPath() %>/css/index.css" rel="stylesheet" type="text/css" />
	
	<link href="<%=request.getContextPath() %>/css/table.css" rel="stylesheet" type="text/css" />
	<link href="<%=request.getContextPath() %>/script/qyts/css/globalStyle.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/script/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/script/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/script/demo.css">
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.4.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/script/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/script/easyui-lang-zh_CN.js"></script>

	
	<script>
		<logic:present name="<%=Config.APART_PAGE_OBJECT%>" scope="request">
			var curPage="<bean:write name='ApartPage' property='curPage'/>";
		</logic:present>			
		<logic:notPresent name="<%=Config.APART_PAGE_OBJECT%>" scope="request">
			var curPage="1";
		</logic:notPresent>   
	</script>
	<style type="text/css">
		td {
			padding: 2px;
		}
	</style>
	<script type="text/javascript">
		var msg = "${msg}";
		$(function(){
			if(msg=="SUCCESS"){
				$.messager.alert('结果','审阅成功!','info');
				
			}else if(msg!="SUCCESS" && msg!=""){
				$.messager.alert('结果',msg,'error');
			}
			<%request.getSession().removeAttribute("msg");%>
		})
		
		function addReview(templateId,versionId,date){
			$("#templateIdInput").val(templateId);
			$("#versionIdInput").val(versionId);
			$("#dateInput").val(date);
			$("#typeInput").val("one");//单个审阅
			$("#addReviewForm").submit();
		}
		function changeAllCheck(obj){
			$(":checkbox[name='chk']").each(function(){
				$(this).attr("checked",$(obj).attr("checked"));
			});
		}
		function moreReview(){
			var checkStr = "";
			$(":checkbox[name='chk']").each(function(){
				if($(this).attr("checked")){
					checkStr += (checkStr==""?$(this).val():"&"+$(this).val());
				}
			});
			if(checkStr==""){
				$.messager.alert('提示','请选择审阅的报表!','info');
			}
			$("#dateInput").val("<%=request.getAttribute("date") %>");
			
			$("#strInput").val(checkStr);
			$("#typeInput").val("more");//批量审阅
			$("#addReviewForm").submit();
			
		}

		function returnSearch(){
			
			document.getElementById('searchFileForm').submit();	
		}
		function viewPdf(repInId,templateId,versionId,curId,repFreqId,year,term,orgId){
			 window.open("<%=request.getContextPath()%>/editAFReport.do?statusFlg=1&repInId=" + repInId+"&templateId="+templateId+"&versionId="+versionId+"&curId="+curId+"&repFreqId="+repFreqId+"&year="+year+"&term="+term+"&orgId="+orgId); 
		}
	</script>
  </head>
  
  <body >
 
  	
  	<table style="border:solid 1px #bdd5e0;" border="0" cellspacing="0" cellpadding="0" align="center" width="100%" >
  		<tr>
  			<td style="padding-left:10px">
  				<form action="leadReviewAction.do" method="post" id="searchFileForm">
			  					报表编号：<input name="templateId" style="width: 100px" value="<%=(request.getAttribute("templateId")==null||request.getAttribute("templateId")=="")?"":request.getAttribute("templateId") %>">
			  					&nbsp;&nbsp;
			  					报表名称：<input name="path" name="repName" style="width: 200px" value="<%=(request.getAttribute("repName")==null||request.getAttribute("repName")=="")?"":request.getAttribute("repName") %>">
			  					&nbsp;&nbsp;
			  					报表期数：<input id="dd" name="date" class="easyui-datebox" required="true" value="<%=request.getAttribute("date") %>" readonly="readonly"></input>
			  					&nbsp;&nbsp;
			  					报表状态：
			  					<select  style="width:100px;border:solid 1px #bdd5e0;" name="reviewStatus" readonly="readonly">
			  						<option value="<%=Config.DEFAULT_VALUE %>">全部</option>
			  						<option value="<%=Config.CHECK_FLAG_PASS %>">已审阅</option>	
			  						<option value="<%=Config.CHECK_FLAG_UNCHECK %>">未审阅</option>				
			  					</select>

			  					
			  	</form>
  			</td>
  			<td><a href="javascript:returnSearch();" class="easyui-linkbutton" plain="true" iconCls="icon-search">查看</a> 			</td>
  		</tr>
  	</table>
  			<div class="tableinfo" style="margin-top:15px;margin-bottom:0px;width:100%">
  				<table width="100%">
  					<tr>
  						<td>
  							<input type="button" onclick="moreReview()" style="width:63px; height:26px; line-height:26px; text-align:center; background:url(<%=request.getContextPath() %>/image/btnbg.gif) no-repeat; color:#fff; border:none; cursor:pointer;" value="批量审阅">
  						</td>
  						<td style="text-align:right;font-family:宋体;padding-bottom:0px">
  							当前报表期数：<%=request.getAttribute("date") %>，已审阅：<%=request.getAttribute("flagPass") %>
  						</td>
  					</tr>
  				</table>
  					
  					
  				</div>
  			<form action="selectsql.html" method="post" id="execSQLForm">
		    	<table width="100%" border="0" cellspacing="0" cellpadding="0"  class="ntable"  style="margin-top:10px;" id="secondTable">
                      <tr>
                      	<td width="2%" bgcolor="#e6effb" align="center"><input type="checkbox" onclick="changeAllCheck(this)" id="headCheck" style="vertical-align: middle;"/></td>
                      	<td width="" bgcolor="#e6effb" align="center"><strong>报表编号</strong></td>
                        <td width="" bgcolor="#e6effb" align="center"><strong>报表名称</strong></td>
                        <td width="" bgcolor="#e6effb" align="center"><strong>版本号</strong></td>
                        <td width="" bgcolor="#e6effb" align="center"><strong>报表机构</strong></td>
                        <td width="" bgcolor="#e6effb" align="center"><strong>报表期数</strong></td>
                        <td width="" bgcolor="#e6effb" align="center"><strong>报表状态</strong></td>
                        <td bgcolor="#e6effb" align="center" ><strong>操作</strong></td>
                      </tr>
                      <%
                      	List<Aditing> resList = (List<Aditing>)request.getAttribute("resList");
                      	if(resList!=null && resList.size()>0){
                      		for(Aditing a : resList){
                      			
                      %>
                      <tr>
                      	<td width="2%" align="center">
                      		<%if(a.getReviewStatus().equals(Config.CHECK_FLAG_UNCHECK.toString())){ %>
                      		<input type="checkbox" name="chk" style="vertical-align: middle;" value="<%=a.getChildRepId() %>_<%=a.getVersionId() %>"/>
                      		<%}else if(a.getReviewStatus().equals(Config.CHECK_FLAG_PASS.toString())){ %>
                      		--
                      		<%} %>
                      	</td>
                      	<td width=""  align="center"><%=a.getChildRepId() %></td>
                        <td width=""  align="center">
                        <a href="javascript:viewPdf('<%=a.getRepInId() %>',
											'<%=a.getChildRepId() %>',
											'<%=a.getVersionId() %>',
											'<%=a.getCurId() %>',
											'<%=a.getActuFreqID() %>',
											'<%=a.getYear() %>',
											'<%=a.getTerm() %>',
											'<%=a.getOrgId() %>')">				
                        		<%=a.getRepName() %>
                        </a>	
                        </td>
                        <td width=""  align="center"><%=a.getVersionId() %></td>
                        <td width=""  align="center"><%=a.getOrgName() %></td>
                        <td width="10%"  align="center"><%=request.getAttribute("date") %></td>	
                        <td width=""  align="center">
                        	<%if(a.getReviewStatus().equals(Config.CHECK_FLAG_UNCHECK.toString())){ %>
                        		<span style="color:red">未审阅</span>
                        	<%}else if(a.getReviewStatus().equals(Config.CHECK_FLAG_PASS.toString())){ %>
                        		<span style="color:green">已审阅</span>
                        	<%} %>
                        </td>
                        <td  align="center" >
                        	<%if(a.getReviewStatus().equals(Config.CHECK_FLAG_UNCHECK.toString())){ %>
                        		<a href="javascript:addReview('<%=a.getChildRepId() %>','<%=a.getVersionId() %>','<%=request.getAttribute("date") %>')">审阅</a>
                        	<%}else if(a.getReviewStatus().equals(Config.CHECK_FLAG_PASS.toString())){ %>
                        		--
                        	<%} %>
                        	
						</td>
                      </tr>
                      <%
                      		}
                      	}else{
                      %>
                      	<tr>
                      		<td colspan="8" align="left">暂无数据</td>
                      	</tr>
                      <%
                      	}
                      %>
                        
					</table>
		    </form>
		    <table cellSpacing="0" cellPadding="0" width="98%" border="0">
		<TR>
			<TD>
				<jsp:include page="../apartpage.jsp" flush="true">
					<jsp:param name="url" value="../leadReviewAction.do" />
				</jsp:include>
			</TD>
		</TR>
	</table>
	
	<form action="addTemplateReviewAction.do" method="post" style="display:none" id="addReviewForm">
		<input name="templateId" id="templateIdInput">
		<input name="date" id="dateInput">
		<input name="versionId" id="versionIdInput">
		<input name="type" id="typeInput">
		<input name="templateIdAndVersionIdStr" id="strInput">
	</form>
  </body>
</html>
