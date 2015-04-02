<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@page import="com.fitech.model.worktask.security.Operator"%>
<%@page import="com.fitech.model.worktask.common.WorkTaskConfig"%>
<%@page import="com.fitech.framework.core.common.Config"%>
<%
	String path = request.getContextPath();
	
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	Operator operator = null;
if (session.getAttribute(WorkTaskConfig.OPERATOR_SESSION_NAME) != null)
	operator = (Operator) session
			.getAttribute(WorkTaskConfig.OPERATOR_SESSION_NAME);
String 	userId= operator.getOperatorId()+"";



%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>scripts/themes/default/easyui.css" />
	<link rel="stylesheet" type="text/css" href="<%=basePath%>scripts/themes/icon.css"/>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>scripts/demo.css"/>
	<script type="text/javascript" src="<%=basePath%>js/jquery-1.4.2.js"></script>
	<script type="text/javascript" src="<%=basePath%>scripts/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>scripts/easyui-lang-zh_CN.js"></script>
	<script language="javascript" src="scripts/progressBar.js"></script>
    
    
    <link href="css/index.css" rel="stylesheet" type="text/css" />
<link href="css/common0.css" rel="stylesheet" type="text/css" />
<link href="css/common.css" rel="stylesheet" type="text/css" />
<link href="css/table.css" rel="stylesheet" type="text/css" />
<link href="css/globalStyle.css" rel="stylesheet"
	type="text/css" />
<link href="css/thd.css" rel="stylesheet" type="text/css" />
<link href="css/animate/theme/jquery-ui-1.8.11.redmond.css"
	rel="stylesheet" />
	
	 <link rel="stylesheet" type="text/css" href="scripts/themes/default/easyui.css" />
	<link rel="stylesheet" type="text/css" href="scripts/themes/icon.css"/>
<%--	<link rel="stylesheet" type="text/css" href="scripts/demo.css"/>--%>
	<script type="text/javascript" src="<%=request.getContextPath() %>/dwr/interface/msgInfoService.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/dwr/engine.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/dwr/util.js"></script>
<%--	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/scripts/jquery/themes/default/easyui.css">--%>
<%--	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/scripts/jquery/themes/icon.css">--%>
<%--	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/scripts/demo.css">--%>
<%--	<script type="text/javascript" src="<%=request.getContextPath() %>/scripts/jquery/jquery-1.6.min.js"></script>--%>
<%--	<script type="text/javascript" src="<%=request.getContextPath() %>/scripts/jquery/jquery.easyui.min.js"></script>--%>
	 <link href="<%=request.getContextPath()%>/css/globalStyle.css"
			rel="stylesheet" type="text/css" />
				<script type="text/javascript" src="js/jquery-1.4.2.js"></script>
	<script type="text/javascript" src="scripts/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="scripts/easyui-lang-zh_CN.js"></script>
	<script language="javascript" src="scripts/progressBar.js"></script>
	
	<style>
		.toolbar{
			height:30px;
			padding:5px;
		}
		
	</style>
	<script type="text/javascript">
	
	
	var  progressBar=new ProgressBar("正在跳转，请稍后........");
	
		function addOrg() {
			//行级列表框
			var orgs = document.getElementById('orgsSelect');
			//选中行级列表框
			var selectOrgs = document.getElementById('selectOrgs');
			addSelect(orgs, selectOrgs);
		}
	
		//删除机构
		function delOrg() {
			//行级列表框
			var orgs = document.getElementById('orgsSelect');
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
				if (select1Options[i].selected) {
					
					//查看是否已经选中过
					var isExit = false;
					
					for ( var j = 0; j < select2Options.length; j++) {
						if (select1.options[i].value == select2.options[j].value){
							isExit = true;
						}
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

//		$(function(){
//			$("#orgcls").change(function(){
				//showProcessBar();
				
//				var id=$(this).val();
				//alert(id);
				//$("#orgs").empty()	
//				$.get("bbsh_getOrgList.action?report.orgclsid="+id,function(data){
//					$("#orgDIV").css("display","block");
//					$("#pubMsgDiv").css("display","none");
					
//					$("#orgDIV select[name='orgs']").html(data);
//					
//				});
//			});
//		});
		

	
		//查看消息
		function writeDivSelect(msgId,type){
			msgInfoService.updateMsgInfoReadered(msgId,1);
		
			msgInfoService.findMsgInfoById(msgId,function(tmsgInfo){
				var pp= $('#tabsDiv').tabs('getSelected');
				$('#tabsDiv').tabs('select','写消息');
				
				$("#touserNameTR").css("display","block");//显示机构行
				$("#touserNameTD").text("发件机构:");
				$("#touserNameTR textarea[name='tmsgInfo.touserName']").val(tmsgInfo.userName);//机构数据
				$("#msgTitleTR input[name='tmsgInfo.msgTitle']").val(tmsgInfo.msgTitle);//机构标题
				$("#contentArea").val(tmsgInfo.content);//消息内容
				
			//	alert(event.srcElement.style.fontWeight);
				
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

		

		

		function getFilePath(){
			$("#fineNameInput").val($("#fileFile").val());
		}

		//添加群发机构
		function addOrgs(){
			var orgNames="";
			var orgIds="";
			$("#selectOrgs option").each(function(){
				if($(this).text()!=null && $(this).text()!=""){
					orgNames+=$(this).text()+";";
					orgIds+=$(this).val()+";";
				}
				
			})
			if(orgNames!=""){
				$("#touserNameTR textarea[name='tmsgInfo.touserName']").text(orgNames);
				$("#toUaserIdInput").val(orgIds);
			}
			
		}

		function checkData(){
			if($("#msgTitleTR input[name='tmsgInfo.msgTitle']").val()==""){
				alert("消息标题不能为空");
				return;
			}
			if($("#contentArea").val()==""){
				alert("消息内容不能为空");
				return;
			}
			if($("#touserNameTR textarea[name='tmsgInfo.touserName']").val()=="≮请添加用户,不能为空≯"){
				alert("收件人不能为空");
				return;
			}
			
			$("#msgForm").submit();
			progressBar.show();
		}

		function clearMsgData(){
			$("#touserNameTR textarea[name='tmsgInfo.touserName']").val("≮请添加用户,不能为空≯");//收件人机构数据
			$("#touserNameTR textarea[name='tmsgInfo.touserName']").attr("readonly",true);
			$("#msgTitleTR input[name='tmsgInfo.msgTitle']").val("");//机构标题
			$("#contentArea").val("");//消息内容
			//给隐藏域赋值
			$("#msgIdInput").val("");//消息ID
			$("#reverStateInput").val("");
			
		}
	
		$(function(){
		$('#userTree').tree({
			checkbox: true,
			
			cascadeCheck:true,
			
			url: '<%=request.getContextPath()%>/json/user_tree_data_<%=userId%>.json',
			
			onCheck:function(node,checked){
				
				$("#touserNameTR textarea[name='tmsgInfo.touserName']").val(getCheckedUserTree());
				$("#touserId").val(getCheckedUserIdTree());
				
				
			},
			onContextMenu: function(e, node){
				
				e.preventDefault();
				
				$('#userTree').tree('select', node.target);
				$('#mm').menu('show', {
					left: 0,
					top: e.pageY
				});
			}
		});
		$('#roleTree').tree({
			checkbox: true,
			
			cascadeCheck:true,
			
			url: '<%=request.getContextPath()%>/json/role_tree_data_<%=userId%>.json',
			
			onCheck:function(node,checked){
				
				$("#touserNameTR textarea[name='tmsgInfo.touserName']").val(getCheckedRoleTree());
				$("#touserId").val(getCheckedRoleIdTree());
				
			},
			onContextMenu: function(e, node){
				
				e.preventDefault();
				
				$('#roleTree').tree('select', node.target);
			}
		});
		});
		
			function getCheckedUserTree(){
				var nodes = $('#userTree').tree('getChecked');
				var s = '';
				var ids='';
				for(var i=0; i<nodes.length; i++){
					var temp=nodes[i].id;
					if (s!= ''&&temp.indexOf("_")!=-1&&ids.indexOf(temp.split("_")[0])==-1){
						s += ';';
						ids+=';';
					} 
					if(temp.indexOf("_")!=-1&&ids.indexOf(temp.split("_")[0])==-1) {
						s+=nodes[i].text;
						ids+=temp.split("_")[0];
					}
				}
				return s ;
			}
			function getCheckedUserIdTree(){
				var nodes = $('#userTree').tree('getChecked');
				var s = '';
				for(var i=0; i<nodes.length; i++){
					var temp=nodes[i].id;
					if (s!= ''&&temp.indexOf("_")!=-1&&s.indexOf(temp.split("_")[0])==-1) s += ';';
					if(temp.indexOf("_")!=-1&&s.indexOf(temp.split("_")[0])==-1) s+=temp.split("_")[0];
				}
				return s ;
			}
	
			function getCheckedRoleTree(){
				var nodes = $('#roleTree').tree('getChecked');
				var s = '';
				var ids='';
				for(var i=0; i<nodes.length; i++){
					var temp=nodes[i].id;
					if (s!= ''&&temp.indexOf("_")!=-1&&ids.indexOf(temp.split("_")[0])==-1) {
						s += ';';
						ids+=';';
					}
					if(temp.indexOf("_")!=-1&&ids.indexOf(temp.split("_")[0])==-1) {
						s+=nodes[i].text;
						ids+=temp.split("_")[0];
					}
				}
				return s ;
			}
			function getCheckedRoleIdTree(){
				var nodes = $('#roleTree').tree('getChecked');
				var s = '';
				for(var i=0; i<nodes.length; i++){
					var temp=nodes[i].id;
					if (s!= ''&&temp.indexOf("_")!=-1&&s.indexOf(temp.split("_")[0])==-1) s += ';';
					if(temp.indexOf("_")!=-1&&s.indexOf(temp.split("_")[0])==-1) s+=temp.split("_")[0];
				}
				return s ;
			}
			function alertMsg(){
				var msg='<s:property value="#request.alertMsg"/>';
				if(msg!=''&&msg!='null'){
					
					alert('<s:property value="#request.alertMsg"/>');
					
				}
			}
		
			//删除已发送消息 add by wmm
			function deleteMsg(msgId){
			if(confirm("确定要彻底删除消息吗?")){
				$.ajax({   
						          type : "post",   
						          url : "<%=request.getContextPath() %>/tmsginfo!deletePublishMsg.action",   
								  data: "msgId="+msgId,
						          async : false,   
						          success : function(data){   
										if(data==undefined || parseInt(data)==1){
											
											$("#"+msgId).remove();
											alert("删除成功");
										}else{
											alert("删除失败");
										}
						          }   
					          });
				
				
			}
		}
	</script>
</head>
<body class="easyui-layout" >
		
	
	<div region="east"  title="" split="true" style="width:250px">
		<div class="easyui-tabs" fit="true"  border="false" id="tabsDiv" style="overflow: hidden;">
		
			<div title="用户选择"  iconCls="icon-reload" split="true" style="width:180;overflow:auto;" >
				<ul class="easyui-tree" id="userTree" style="text-align: left;margin-left:50px;"></ul>
			</div>
			<div title="角色选择"  style="padding:5px;overflow: auto;" >
			<ul class="easyui-tree" id="roleTree" style="text-align: left;margin-left:50px;"></ul>
			</div>
		
		</div>
	</div>
<div region="north"  style="height:55px;padding:8px;background:#ffffff;">

	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		  <tr>
		    <td align="left">
		    <span><img src="images/icon01.gif" /></span>
		    <span style="font-family:Arial;font-size:12px;font-weight:bold;font-style:normal;text-decoration:none;color:#EBAD06;">当前位置  >>信息处理>> 消息管理</span>
		    </td>
		  </tr>
		</table>

	</div>
	<div region="center" title="" style="overflow:hidden;width:90%;margin-top:1px;" >
		<div class="easyui-tabs" fit="true"  border="false" id="tabsDiv" style="overflow: hidden;">
			
			<div title="写消息"  style="padding:5px;overflow: auto;" id="writeDiv">
				<form id="msgForm" action="tmsginfo!savePubMsg.action?type=1" method="post" enctype="multipart/form-data" > 
					<table width="800px" height="90%"style="border:1px solid #88C5EE; " cellpadding="0" cellspacing="0" id="columnTable" >
						<tr>
							<td colspan="2" style="color: #0C507C;padding:4px 2px 4px 4px;background-color: #4bacff;filter:progid:DXImageTransform.Microsoft.gradient(startcolorstr=#ffffff,endcolorstr=#bfe1f2,gradientType=0);font-size: 12px;font-weight: bolder;border-bottom: 1px solid #88C5EE;">
								发送消息,请注意附件不能超过10M！
							</td>
						</tr>
						
					
						<tr id="touserNameTR" ">
						    
						    <input type="hidden" name="tmsgInfo.revertState" id="reverStateInput" />
							<td id="touserNameTD"    style="width:100px;text-align: right;font-style:normal;font-size: 12px;font-weight:bold;  color:seagreen;font-family:Arial;" >接收用户:</td>
							<td style="padding-top: 10px">
								<textarea name="tmsgInfo.touserName"  readonly="readonly" onpropertychange="this.style.height=this.scrollHeight+'px';" oninput="this.style.height=this.scrollHeight+'px';" style="overflow:hidden;height:30px;width:600px;" >≮请添加用户,不能为空≯</textarea>
							</td>
						</tr>
						<tr id="msgTitleTR" >
							<td style="width:100px;text-align: right;font-style:normal;font-size: 12px;font-family:Arial;font-weight:bold;">标&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;题:</td>
							<td ><input name="tmsgInfo.msgTitle" style="width:600px;height:25px;padding-top: 7px;" ></td>
						</tr>
						<tr   id="fileNameTR" height="25px">
							<td id="isAddFileNameTD" style="width:100px;text-align: right;font-style:normal;font-size: 12px;font-family:Arial;font-weight:bold;">添加附件:</td>
							<td id="fileNameTD">
								<div id="addFileNameDiv" style="display: inline;">
								<s:file name="upload"  id="upload" onchange="getFilePath()" cssStyle="width:600px;height:25px;vertical-align:bottom;padding-top: 5px; "/><input name="fileName" type="hidden" id="fineNameInput"/>
								</div>
								
							</td>
						</tr>
						
						<tr >
							<td valign="top" height="40px" id="contentTD" style="padding-top:17px; width:100px;text-align: right;font-size: 12px;font-weight:bold;font-style:normal;font-family:Arial;">内&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;容:</td>
							<td>
								<textarea id="contentArea"  style="width:600px;overflow:auto;" rows="6" name="tmsgInfo.content"></textarea>
							</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td  align="center" style="padding:4px 4px 2px 4px;">
								
								<input type="button" class="buttonStyle" onclick="checkData()" value="发布"/>&nbsp;&nbsp;&nbsp;
								<input type="button" class="buttonStyle" onclick="clearMsgData()" value="重置"/>
								
							
								<input type="hidden" name="tmsgInfo.msgId" id="msgIdInput" value="<s:property value='tmsgInfo.msgId'/>"/>
								<input type="hidden" name="tmsgInfo.msgState" id="msgStateInput">
								<input type="hidden" name="tmsgInfo.touserId" id="touserId">
								
							</td>
						</tr>
						
					</table>
				</form>
			</div>



			<div title="已发消息" id="myOwnMsgDiv"  style="overflow:auto;padding:5px;">
				<table style="border:1px solid #88C5EE;font-size: 12px" cellspacing="0" cellpadding="0" width="95%">
					<tr>
						<td colspan="4" style="color: #0C507C;padding:4px 2px 4px 4px;background-color: #4bacff;font-size: 12px;font-weight: bolder;border: 1px solid #88C5EE;filter:progid:DXImageTransform.Microsoft.gradient(startcolorstr=#ffffff,endcolorstr=#bfe1f2,gradientType=0);">已发消息</td>
					</tr>
					<tr>
						<td align="center" width="20%" style="background:url(<%=request.getContextPath() %>/images/content_bg.jpg) repeat-x bottom #fff;padding:4px 2px 4px 4px;border: 1px solid #88C5EE;">收件人</td>
						<td align="center" width="25%" style="background:url(<%=request.getContextPath() %>/images/content_bg.jpg) repeat-x bottom #fff;padding:4px 2px 4px 4px;border: 1px solid #88C5EE;">标题</td>
						<td align="center" width="15%" style="background:url(<%=request.getContextPath() %>/images/content_bg.jpg) repeat-x bottom #fff;padding:4px 2px 4px 4px;border: 1px solid #88C5EE;">时间</td>
						<td align="center" width="30%" style="background:url(<%=request.getContextPath() %>/images/content_bg.jpg) repeat-x bottom #fff;padding:4px 2px 4px 4px;border: 1px solid #88C5EE;">操作</td>
					</tr>
					<s:if test="msgInfoResult==null || msgInfoResult.results==null || msgInfoResult.results.size()==0">
						<tr>
							<td colspan="4" style="padding:4px 2px 4px 4px;">暂无信息</td>
						</tr>
					</s:if>
					<s:else>
					<s:iterator value="msgInfoResult.results" id="m">
						<tr onMouseOver="this.style.backgroundColor='menu'" onMouseOut="this.style.backgroundColor='#FFFFFF'"  id='<s:property value="#m.msgId"/>' style="cursor: hand" >
							<td align="center" style="padding:4px 2px 4px 4px;border-right: 1px solid #88C5EE;border-bottom: 1px solid #88C5EE;">
								<s:if test="#m.touserName==null || #m.touserName==''">
									全部机构
								</s:if>
								<s:else>
									<s:if test="#m.touserName.length()>13">
										<s:property value="#m.touserName.substring(0,13)"/>...
									</s:if>
									<s:else>
										<s:property value="#m.touserName"/>
									</s:else>
									
								</s:else>
								</td>
							<td align="center" style="padding:4px 2px 4px 4px;border-right: 1px solid #88C5EE;border-bottom: 1px solid #88C5EE;">
							
							<s:property value="#m.msgTitle"/>&nbsp;
							</td>
							<td align="center" style="padding:4px 2px 4px 4px;border-bottom: 1px solid #88C5EE;"><s:property value="#m.startTime"/></td>
							<td align="center" style="padding:4px 2px 4px 4px;border-left:1px solid #88C5EE;border-bottom: 1px solid #88C5EE;"><a href='javascript:getPubMsg(<s:property value="#m.msgId"/>)'>查看</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href='javascript:deleteMsg(<s:property value="#m.msgId"/>)'>删除</a></td>
						</tr>
					</s:iterator>
					</s:else>
				
				</table>
				<s:if test="msgInfoResult!=null && msgInfoResult.results!=null && msgInfoResult.results.size!=0">
								<form action="tmsginfo!findDisMsgInfoList.action" id="msgcurPageForm" method="post">
							 	<table border="0" style="font-size: 12px;width: 95%;margin-top:30px;">
										<tr>
										<s:hidden name="msgInfoResultpageNo" />
										<s:hidden name="modelResultpageNo" ></s:hidden>
											<td align="left" >共<s:property value="msgInfoResult.totalCount"/>条记录
											&nbsp;&nbsp;第<s:property value="msgInfoResult.currentPage"/>/<s:property value="msgInfoResult.pageCount"/>页	
											<input type="hidden" name="currentPage" value="<s:property value='msgInfoResult.currentPage'/>"/>	
											<input type="hidden" name="pageCount" value="<s:property value='msgInfoResult.pageCount'/>"/>				
										</td>
										<td align="right">
											<img src="<%=request.getContextPath()%>/images/first.gif"  onclick="turntoFirst('msgcurPageForm','msgInfoResultpageNo')" style="cursor: hand;vertical-align: middle;" />
									       <img src="<%=request.getContextPath()%>/images/up.gif" onclick="up('msgcurPageForm','msgInfoResultpageNo')" style="cursor: hand;vertical-align: middle;" />
									       <img src="<%=request.getContextPath()%>/images/down.gif" onclick="down('msgcurPageForm','msgInfoResultpageNo')" style="cursor: hand;vertical-align: middle;" />
									       <img src="<%=request.getContextPath()%>/images/last.gif" onclick="turntoLast('msgcurPageForm','msgInfoResultpageNo')" style="cursor: hand;vertical-align: middle;" />
											&nbsp;&nbsp;
											跳转至<input type="text" style="width: 30px;height:10px" id="form1_pageNo"/>页
											 <img src="<%=request.getContextPath()%>/images/goto.gif" onclick="turntoPage('msgcurPageForm','msgInfoResultpageNo')" style="cursor: hand;vertical-align: middle;" />
												<input  type="hidden" value="1" name="type"/>
											</td>
								
										</tr>
										
									</table>
							</form>
							<jsp:include page="/page.jsp"  flush="true"></jsp:include>	
						</s:if>
						
			</div>
			
			
			

		</div>
		
</div>
<jsp:include page="/page.jsp"  flush="true"></jsp:include>	
</body>
<script type="text/javascript">
	setTimeout(alertMsg(),5000);
	function alertMsg(){
				var msg='<s:property value="#request.alertMsg"/>';
				
				if(msg!=''&&msg!='null'){
					
					alert('<s:property value="#request.alertMsg"/>');
					
				}
			}

</script>
</html>