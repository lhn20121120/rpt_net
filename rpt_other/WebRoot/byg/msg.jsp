<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="fp" uri="/fitechpage-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<link href="css/index.css" rel="stylesheet" type="text/css" />
<link href="css/common0.css" rel="stylesheet" type="text/css" />
<link href="css/common.css" rel="stylesheet" type="text/css" />
<link href="css/table.css" rel="stylesheet" type="text/css" />
<link href="css/globalStyle.css" rel="stylesheet" type="text/css" />
<link href="css/thd.css" rel="stylesheet" type="text/css" />

	<script type="text/javascript" src="<%=request.getContextPath() %>/dwr/interface/msgInfoService.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/dwr/engine.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/dwr/util.js"></script>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/scripts/jquery/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/scripts/jquery/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/scripts/demo.css">
	<script type="text/javascript" src="<%=request.getContextPath() %>/scripts/jquery/jquery-1.6.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/scripts/jquery/jquery.easyui.min.js"></script>
	<script language="javascript" src="scripts/progressBar.js"></script>
	<!--<style>
		.toolbar{
			height:30px;
			padding:5px;
		}
	</style>
	<style>
		.toolbar{
			height:30px;
			padding:5px;
		}
		.buttonStyle{
	border:0;
    margin:0 7px 0 0;
	background:url(<%=request.getContextPath() %>/images/btn.png) no-repeat;
	font-size:12px;
    line-height:100%;
    font-weight:bold;
    color:#fff;
    cursor:hand;
    padding:4px 10px 4px 6px;
	filter:progid:DXImageTransform.Microsoft.Shadow(Strength=2,Direction=175,Color='#000000');
	width:70px;
	height:25px;
}
	</style>-->
	<script>
	var  progressBar=new ProgressBar("正在处理，请稍后........");
		function resize(){
			$('#test').window('open');
		}
		function addOrg() {
			//行级列表框
			var orgs = document.getElementById('orgs');
			//选中行级列表框
			var selectOrgs = document.getElementById('selectOrgs');
		
			addSelect(orgs, selectOrgs);
		}
	
		//删除机构
		function delOrg() {
			//行级列表框
			var orgs = document.getElementById('orgs');
			//选中行级列表框
			var selectOrgs = document.getElementById('selectOrgs');
			//选中行级列表框中的项目
			delSelect(orgs, selectOrgs);
		}

		function addSelect(select1, select2) {
			//选中行级列表框中的项目
			var select1Options = select1.options;
			//所有行级列表框中的项目
			var select2Options = select2.options;
			for ( var i = 0; i < select1Options.length; i++) {
				if (select1.options[i].selected) {
					//查看是否已经选中过
					var isExit = false;
					for ( var j = 0; j < select2Options.length; j++) {
						if (select1.options[i].value == select2.options[j].value)
							isExit = true;
					}
					if (isExit == false) {
						select2.options[select2.length] = new Option(
								select1.options[i].text, select1.options[i].value);
						select1.options[i].style.color = "gray";
					}
				}
			}
		}

		function delSelect(select1, select2) {
			var select1Options = select1.options;
			//所有行级列表框中的项目
			var select2Options = select2.options;
			var optionLen = select2Options.length;
			var offset = 0;
			for ( var i = 0; i < optionLen; i++) {
				if (select2.options[i - offset].selected) {
					//改变全部行级列表框中对应的颜色
					for ( var j = 0; j < select1Options.length; j++) {
						if (select1.options[j].value == select2.options[i - offset].value)
							select1.options[j].style.color = "#003366";
					}
					select2.remove(i - offset);
					offset++;
				}
			}
		}
		
		$(document).ready(function(){	
			$("#orgcls").change(function(){
				var cid=$("#orgcls").find("option:selected").val();
				$.ajax({
					  type: "GET",
					  url: "trendCheck_initOrg.action?trendCheckVo.orgClsId="+cid,
					  dataType: "json",
					  success : function(data){
					  		var returnData=eval(data);
					  		var orginfos = data.trendCheckVo.orgList;
					  		if(orginfos!=null){
					  		var options = '';
					  		$("#orgs").empty();
					  		$("#orgs").css("width",200);
						  		for(var i=0;i<orginfos.length;i++){
						  			options =  $("<option value='"+orginfos[i].orgid+"' style='width:100%'>"+orginfos[i].orgName+"</option>");
						  			$("#orgs").append(options);
						  		}
						  		
					  		}
					  }
		     	});
			});
		});

		
		//增加文件列
		function addRow(){
			var curTable=document.getElementById("columnTable");
			var count=curTable.rows.length;
			var newRow=curTable.insertRow(count-1);
			var newCell_1=newRow.insertCell(0);
			newCell_1.innerHTML="&nbsp;";


			var newCell_2=newRow.insertCell(1);
			
			newCell_2.innerHTML="<input type='file' name='fileNames["+count+"]' />&nbsp;&nbsp;<a href='javascript:addRow()'>添加</a>&nbsp;&nbsp;<input type='button' onclick='viewDelet()' value='删除'/>";	
		}

		

		

		//查看邮件
		function writeDivSelect(msgId,type){
			msgInfoService.updateMsgInfoReadered(msgId,1);
		
			msgInfoService.findMsgInfoById(msgId,function(tmsgInfo){
				var pp= $('#tabsDiv').tabs('getSelected');
				$('#tabsDiv').tabs('select','写邮件');
				
				$("#touserNameTR").css("display","block");//显示机构行
				$("#touserNameTD").text("发件机构:");
				$("#touserNameTR input[name='tmsgInfo.touserName']").val(tmsgInfo.userName);//机构数据
				$("#msgTitleTR input[name='tmsgInfo.msgTitle']").val(tmsgInfo.msgTitle);//机构标题
				$("#contentArea").val(tmsgInfo.content);//邮件内容
				
				if(type=='read'){
					$("#saveA").css("display","none");
					$("#resetA").css("display","none");
					$("#saveModelA").css("display","none");
					$("#touserNameTR input[name='tmsgInfo.touserName']").attr("readonly",true);
					$("#msgTitleTR input[name='tmsgInfo.msgTitle']").attr("readonly",true);
					$("#contentArea").attr("readonly",true);//邮件内容
					
					//$("#touserNameTR input[name='tmsgInfo.touserName']").css("border","none");
					//$("#msgTitleTR input[name='tmsgInfo.msgTitle']").css("border","none");
					//$("#contentArea").css("border","none");//邮件内容
					
				}
				
			});
			
		}

		//存草稿
		function saveModel(){
			$("#msgStateInput").val(2);
			$("#msgForm").attr("action","tmsginfo!saveModel.action?type=2")
			document.getElementById('msgForm').submit();
		}

		//获取公告信息
		function getPubMsg(msgId){
			
			msgInfoService.findMsgInfoById(msgId,function(tmsgInfo){
				var  info=new Object();
				info.title=tmsgInfo.msgTitle;
				info.content=tmsgInfo.content;
				info.userName=tmsgInfo.userName;
				info.touserName=tmsgInfo.touserName;
				info.viewFileName=tmsgInfo.viewFileName;
				info.startTime=tmsgInfo.startTime;
				info.filePath="无附件";
				
				if(info.viewFileName!=null && info.viewFileName!="")
					info.filePath="<a href='tmsginfo!down.action?tmsgInfo.filename="+tmsgInfo.filename+"' alt='下载附件' style='cursor:hand;'>"+tmsgInfo.viewFileName+"</a>";
					window.showModalDialog("<%=request.getContextPath() %>/byg/showMsgInfo.jsp",info,"dialogWidth=700px;dialogHeight=400px;scroll:no;");
				
				
				
			})
		}

		//回复按钮 获得邮件信息
		function writeMsgInfo(msgId,url){
			msgInfoService.findMsgInfoById(msgId,function(tmsgInfo){
				
				$("#msgForm").attr("action",url);
				$("#touserNameTR input[name='tmsgInfo.touserName']").val(tmsgInfo.userName+";");//收件人机构数据
				$("#msgTitleTR input[name='tmsgInfo.msgTitle']").val(tmsgInfo.msgTitle);//机构标题
				$("#contentArea").val("");//邮件内容
				//给隐藏域赋值
				$("#msgIdInput").val(msgId);//邮件ID
				$("#reverStateInput").val(tmsgInfo.revertState);
				$("#toUaserIdInput").val(tmsgInfo.userId);

				$("#touserNameTR input[name='tmsgInfo.touserName']").attr("readonly",false);
				$("#msgTitleTR input[name='tmsgInfo.msgTitle']").attr("readonly",false);
				$("#contentArea").attr("readonly",false);//邮件内容

				//$("#myOwnMsgDiv").html($("#replyDiv").html());
				$("#msgListDiv").css("display","none");
				$("#replyDiv").css("display","block");
				if(tmsgInfo.filename!=null && tmsgInfo.filename!=""){//草稿有附件
					
					var fileName = $("<a href='tmsginfo!down.action?tmsgInfo.filename="+tmsgInfo.filename+"' alt='下载附件' style='cursor:hand;'>"+tmsgInfo.viewFileName+"</a>&nbsp;&nbsp;&nbsp;&nbsp;<input type='button' value='重新上传' onclick='replyModelFile()'/>")
					var fileNameInput = $("<input name='tmsgInfo.filename' type='hidden' value='"+tmsgInfo.filename+"'/>");
					var vileFileNameInput = $("<input name='tmsgInfo.viewFileName' type='hidden' value='"+tmsgInfo.viewFileName+"'>");
					$("#fileNameDiv").empty();
					$("#fileNameDiv").append(fileName);
					$("#fileNameDiv").append(fileNameInput);
					$("#fileNameDiv").append(vileFileNameInput);
					$("#isAddFileNameTD").text("附件名称:");
					$("#addFileNameDiv").css("display","none");//隐藏上传草稿
					$("#fileNameDiv").css("display","inline");//显示草稿
				}
			});
		}

		//重新上传草稿
		function replyModelFile(){
			$("#isAddFileNameTD").text("添加附件:");
			$("#addFileNameDiv").css("display","inline");//显示上传草稿
			$("#fileNameDiv").css("display","none");//隐藏草稿
		}
		//查询草稿
		function searchModel(msgId){
			msgInfoService.findMsgInfoById(msgId,function(tmsgInfo){
				var pp= $('#tabsDiv').tabs('getSelected');
				$('#tabsDiv').tabs('select','写邮件');
				
				$("#msgForm").attr("tmsginfo!saveModel.action");
				
				if(tmsgInfo.touserName!="" || tmsgInfo.touserName!=null){
					$("#touserNameTR").css("display","block");
				}
				$("#toUaserIdInput").val(tmsgInfo.userId);
				$("#touserNameTR input[name='tmsgInfo.touserName']").val(tmsgInfo.userName);//收件人机构数据
				$("#msgTitleTR input[name='tmsgInfo.msgTitle']").val(tmsgInfo.msgTitle);//机构标题
				$("#contentArea").val(tmsgInfo.content);//邮件内容
				$("#msgIdInput").val(msgId);//邮件ID
				$("#reverStateInput").val(tmsgInfo.reverState);//回复

				
			});
		}
		

		function getFilePath(){
			$("#fineNameInput").val($("#fileFile").val());
		}
		//返回按钮
		function returnMsg(){
			$("#msgListDiv").css("display","block");
			$("#replyDiv").css("display","none");
		}

		function checkData(){
			if(document.getElementById("upfileType").checked==true){
				document.getElementById("upfileType").value=1;
				}
			
			if($("#msgTitleTR input[name='tmsgInfo.msgTitle']").val()==""){
				alert("邮件标题不能为空");
				return false;
			}
			if($("#contentArea").val()==""){
				alert("邮件内容不能为空");
				return false;
			}
			$("#msgForm").submit();
			return;
		}

		function likeSearchPub(){
			$('#likeSearchPubForm').submit();
		}

		function likeSearch(){
			$('#likeSearchForm').submit();
		}
		
		function deleteMsg(msgId){
			if(confirm("确定要彻底删除消息吗?")){
				
				window.location="<%=request.getContextPath() %>/tmsginfo!deleteMsg.action?msgId="+msgId;
				progressBar.show();
				
			}
		}
	</script>
</head>
<body >
	


	<div  style="height:90px;padding:8px;background:#ffffff;margin-top:10px;">
		<table width="100%" border="0" cellspacing="0" cellpadding="0"  >
			<tr height="40px">
				<td nowrap="nowrap">
					<div class="toolbar">
						<div class="tableinfo" >
							<span><img src="images/icon01.gif" /> </span><b style="font-size:12px;">当前位置  >> 信息处理>> 已读消息</b>
						</div>
					</div></td>
			</tr>
		</table>

	
	</div>
<%--	<div region="center" title="消息区" style="overflow:hidden;width:90%;" >--%>
<%--		<div class="easyui-tabs" fit="true"  border="false" id="tabsDiv" style="overflow: hidden;width:90%;">--%>
			<div  style="overflow:auto;padding:5px;width:100%;margin-top:-30px;"> 
					<table border="1"  style="margin-left: 50px;"  cellspacing="0" cellpadding="5" width="90%">
<%--					<tr height="20px">--%>
<%--						<td align="center" colspan="3" style="color: #0C507C;padding:4px 2px 4px 4px;background-color: #4bacff;font-size: 12px;font-weight: bolder;border-bottom: 0px solid #88C5EE;filter:progid:DXImageTransform.Microsoft.gradient(startcolorstr=#ffffff,endcolorstr=#bfe1f2,gradientType=0);border-bottom: 1px solid #88C5EE;">--%>
<%--							已读消息--%>
<%--						</td>--%>
<%--						--%>
<%--					</tr>--%>
					<tr height="20px" >
						<th align="center" width="15%" >发件人</th>
						<th align="center" width="35%" >标题</th>
						<th align="center" width="10%" >时间</th>
						<th align="center" width="20%" >操作</th>

					</tr>
					<s:if test="pubMsgResult==null || pubMsgResult.results==null || pubMsgResult.results.size()==0">
						<tr>
							<td colspan="4" style="padding:4px 2px 4px 4px;">暂无信息</td>
						</tr>
					</s:if>
					<s:else>
					<s:iterator value="pubMsgResult.results" id="m">
						<tr  onMouseOver="this.style.backgroundColor='menu'" onMouseOut="this.style.backgroundColor='#FFFFFF'" style="cursor: hand;font-weight:''" >
							<td align="center" style="padding:4px 2px 4px 4px;">
								
							<s:property value="#m.userName"/>&nbsp;
							</td>
							<td align="center" style="padding:4px 2px 4px 4px;">
								
							<s:property value="#m.msgTitle"/>&nbsp;
							</td>
							<td align="center" style="padding:4px 2px 4px 4px;"><s:property value="#m.startTime"/></td>
							<td align="center" style="padding:4px 2px 4px 4px;"><a href='javascript:getPubMsg(<s:property value="#m.msgId"/>)'>查看</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href='javascript:deleteMsg(<s:property value="#m.msgId"/>)'>删除</a></td>
						</tr>
					</s:iterator>
					</s:else>
				</table>			
			<s:if test="pubMsgResult!=null && pubMsgResult.results!=null && pubMsgResult.results.size!=0">
						<form action="tmsginfo!findMsgByOwn.action" id="pubMsgcurPageForm" method="post" >
					 	<table width="90%" border="0" style="font-size: 12px;font-family: 宋体;margin-left: 50px;margin-top: 30px;">
								<tr>
								
									<td align="left" >共<s:property value="pubMsgResult.totalCount"/>条记录
									&nbsp;&nbsp;第<s:property value="pubMsgResult.currentPage"/>/<s:property value="pubMsgResult.pageCount"/>页	
									<s:hidden name="msgInfoResultpageNo" />
									<s:hidden name="pubMsgResultpageNo" ></s:hidden>
									<input type="hidden" name="currentPage" value="<s:property value='pubMsgResult.currentPage'/>"/>	
									<input type="hidden" name="pageCount" value="<s:property value='pubMsgResult.pageCount'/>">				
								</td>
								<td align="right">
							       <img src="<%=request.getContextPath()%>/images/first.gif"  onclick="turntoFirst('pubMsgcurPageForm','pubMsgResultpageNo')" style="cursor: hand;vertical-align: middle;">
							       <img src="<%=request.getContextPath()%>/images/up.gif" onclick="up('pubMsgcurPageForm','pubMsgResultpageNo')" style="cursor: hand;vertical-align: middle;">
							       <img src="<%=request.getContextPath()%>/images/down.gif" onclick="down('pubMsgcurPageForm','pubMsgResultpageNo')" style="cursor: hand;vertical-align: middle;">
							       <img src="<%=request.getContextPath()%>/images/last.gif" onclick="turntoLast('pubMsgcurPageForm','pubMsgResultpageNo')" style="cursor: hand;vertical-align: middle;">
									&nbsp;
									跳转至<input type="text" style="width: 30px;height:10px" id="form1_pageNo"/>页
									 <img src="<%=request.getContextPath()%>/images/goto.gif" onclick="turntoPage('pubMsgcurPageForm','pubMsgResultpageNo')" style="cursor: hand;vertical-align: middle;">
										<input  type="hidden" value="0" name="type"/>
									</td>
								</tr>
								
							</table>
							</form>
							<jsp:include page="/page.jsp"  flush="true"></jsp:include>	
					</s:if>
   		   </div>
<%--		</div>	--%>
<%--		--%>
<%--		--%>
<%--</div>--%>
	
</body>
<script type="text/javascript">
	
//	function alertMsg(){
				var msg='<s:property value="#request.alertMsg"/>';
				
				if(msg!=''&&msg!='null'){
					
					alert('<s:property value="#request.alertMsg"/>');
					
				}
//			}

</script>
</html>