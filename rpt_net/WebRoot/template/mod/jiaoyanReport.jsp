<%@ page language="java" import="java.lang.*,com.cbrc.smis.common.Config,java.io.File" pageEncoding="GBK"%>
<%@ page contentType="text/html;charset=gbk" import="java.io.*"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<%
	/** 报表选中标志 **/
	String reportFlg = "0";
	if (session.getAttribute(Config.REPORT_SESSION_FLG) != null) {
		reportFlg = (String) session
				.getAttribute(Config.REPORT_SESSION_FLG);
	}
	String excelPath = Config.RAQ_TEMPLATE_PATH+"templateFiles" + File.separator +"excel" + File.separator;
	excelPath = excelPath.replace("\\", "\\\\");
%>
<html:html locale="true">
<head>
	<html:base />
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" rel="stylesheet" type="text/css">
	<link rel="stylesheet" type="text/css" href="../css/pageControl.css" />
	<link href="../../css/calendar-blue.css" type="text/css"
		rel="stylesheet">
	<link href="../../css/common.css" rel="stylesheet" type="text/css">
	<link href="../../css/tree.css" rel="stylesheet" type="text/css">
	<script language="javascript" src="../../js/jquery-1.4.2.js"></script>
	<style type="text/css">
		#t1 td{
			height: 25px;
			border-right:1 solid #cccccc;
			border-bottom:1 solid #cccccc;
		}
	</style>
	<script type="text/javascript">
			function _back(tenplateId,versionId){
				window.location="<%=request.getContextPath()%>/viewAFTemplateDetail.do?templateId="+tenplateId+"&versionId="+versionId+"&bak2=2";
			}
			var split1="@td@";
			var split2="@tds@";
			/**展开报表**/
			function showEditOnline(templateId, versionId){
				var param ="<%=excelPath%>";
				var path=param+templateId+"_"+versionId+".xls";
				$.post(
						"<%=request.getContextPath()%>/template/fileaction.do",
						{"fileName":path},
						function(rv){
							if(rv==0){
								var url = "/FitEditOnline/excelToHtml!editOnline.action?tempFilePath="+path;
								var mes = window.showModalDialog(url, null,"dialogWidth:1024px;dialogHeight:768px;resizable:yes;scroll:yes");
								if(mes!=undefined && mes!=""){
									var datas=[];
									if(mes.indexOf(split2)!=-1){
										datas=mes.split(split2);
										if(datas!="" && datas.length>0){
											for(var i=0;i<datas.length;i++){
												if(datas[i].indexOf(split1)!=-1){
													putData(datas[i]);
												}
											}
										}
									}else{
										if(mes.indexOf(split1)!=-1){
											putData(mes);
										}
									}
								}
							}else{
								alert(path+"文件不存在!");
							}
						}
					);
			}

			function putData(data){
				var mes = data.split(split1);//A7@td@xxx@tds@A8@td@xxx
				if(mes[1]!=""){
					if(mes[1].indexOf(".00")!=-1)
						mes[1]=mes[1].substr(0,mes[1].indexOf(".00"));
					var res=existsData(mes[0],mes[1]);
					if(res==false){
						var content = "<tr height='20' align='center' id='new"+mes[0]+"' bgcolor='#FFFFFF'><td><input type='checkbox' value='new"+mes[0]+"' name='cks'/></td><td>" + mes[0] + "</td><td>"
							+ mes[1] + "</td></tr>";
						$("#t1 tr:eq(0)").after(content);
					}
				}//修改了对于选中空的单元格则不能添加
				else{
					alert("不能添加空的单元格值!");
				}
			}
			function _submit(templateId, versionId,savetype,reportName,next) {
				var tl = $("#t1 tr");
				var allstrs = "";
				var str = "";
				tl.each(function(i) {
					if (i > 0) {
						var t1 = $("#t1 tr:eq(" + i + ") td:eq(1)").attr("innerHTML");
						var t2 = $("#t1 tr:eq(" + i + ") td:eq(2)").attr("innerHTML");
						str = t1 + "@td@" + t2;
						allstrs += str + "-";
					}
				});
				if(allstrs!=""){
					var fm1=document.getElementById("jiaoyanTable");
					document.getElementById("strs").value=allstrs;
					document.getElementById("templateId").value=templateId;
					document.getElementById("versionId").value=versionId;
					document.getElementById("savetype").value=savetype;
					document.getElementById("reportName").value=reportName;
					fm1.action='<%=request.getContextPath()%>/template/saveDataAction.do';
					fm1.submit();
				}else{
					if(next!=""){
						var fm2=document.getElementById("tiaozhuanTable");
						document.getElementById("childRepId1").value=templateId;
						document.getElementById("versionId1").value=versionId;
						document.getElementById("opration").value="next";
						document.getElementById("reportName1").value=reportName;
						fm2.action='<%=request.getContextPath()%>/gznx/preSetHZSD.do';
						fm2.submit();
					}
				}
			}

			function existsData(t1,t2){
				var res=false;
				var tl = $("#t1 tr");
				tl.each(function(i){
					if(i>0){
						var d1 = $("#t1 tr:eq(" + i + ") td:eq(1)").attr("innerHTML");
						var d2 = $("#t1 tr:eq(" + i + ") td:eq(2)").attr("innerHTML");
						if(t1==d1 && t2==d2){
							res=true;
						}
					}
				});
				return res;
			}
			 /**
		     * 全选操作
		     */
			function _selAll(){			
				var checkAll = document.getElementById("checkAll");
				var cks=document.getElementsByName("cks");
			  	  	
				for(var i=0;i<=cks.length;i++){
					try{
						if(checkAll.checked==false)
							cks[i].checked=false;
						else
							cks[i].checked=true;
					}catch(e){}
				}
			}
			function deleteAF(templateId,versionId,next,reportName){
				var cks=document.getElementsByName("cks");
				var result=false;
				for(var i=0;i<cks.length;i++){
					if(cks[i].checked){
						result=true;
					}
				}
				if(result){
					var strs="";
					if(confirm("确定删除吗?")){
						for(var i=0;i<cks.length;i++){
							if(cks[i].checked){
								if(cks[i].value.indexOf("new")!=-1){
									$("#"+cks[i].value).remove();
									i=i-1;
								}else
									strs+=cks[i].value+"-";
							}
						}
						if(strs.length>0){
							var fm3=document.getElementById("jiaoyanTable");
							document.getElementById("strs").value=strs;
							document.getElementById("childRepId").value=templateId;
							document.getElementById("versionId").value=versionId;
							document.getElementById("reportName").value=reportName;
							fm3.action='<%=request.getContextPath()%>/template/jiaoYanAction.do?type=edit&next='+next;
							fm3.submit();
						}
					}
				}else{alert("请选择一项删除!");}
				
			}

			function check1(th){
				
			}
			var count=0;
			function addRow(){
				var content = "<tr height='20' align='center' bgcolor='#FFFFFF' id='new"+count+"'><td><input type='checkbox' value='new"+count+"' name='cks'/></td><td><input type='text' value='adsfadsf'/></td><td>"
						+ "<input type='text' value='sadfsdf'/></td></tr>";
					$("#t1 tr:eq(0)").after(content);
					count++;
			}
</script>
</head>
<body>
	<form action="" method="post" id="jiaoyanTable">
		<input type="hidden" id="childRepId" name="childRepId"/>
		<input type="hidden" id="templateId" name="templateId"/>
		<input type="hidden" id="versionId" name="versionId"/>
		<input type="hidden" id="strs" name="strs"/>
		<input type="hidden" id="savetype" name="savetype"/>
		<input type="hidden" id="reportName" name="reportName"/>
	</form>
	<form action="" method="post" id="tiaozhuanTable">
		<input type="hidden" id="childRepId1" name="childRepId"/>
		<input type="hidden" id="versionId1" name="versionId"/>
		<input type="hidden" id="opration" name="opration"/>
		<input type="hidden" id="reportName1" name="reportName"/>
	</form>
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<table border="0" cellspacing="0" cellpadding="4" width="100%"
		align="center">
		<tr>
			<td height="8"></td>
		</tr>
		<tr>
			<td>
				当前位置 >> 报表管理 >> 报表定义管理 >> 校验报表信息
			</td>
		</tr>
		<tr>
			<td height="10"></td>
		</tr>
	</table>
	<table border="0" width="98%">
		<TR>
			<TD width="70%" align="left">
				
			</TD>
			<TD width="10%" align="right">
				<!-- <input type="button" name="addTemplateType" onclick="addRow()" value="添加行" class="input-button"> -->
			</TD>
			<td width="10%" align="left">
				<input type="button" name="addTemplateType1" onclick="deleteAF('${templateId}','${versionId}','${next}','${reportName}')" value="删      除" class="input-button">
			</td>
			<td width="10%" align="center">
				<input type="button" name="addTemplateType" onclick="showEditOnline('${templateId}','${versionId}')" value="选择单元格" class="input-button">
			</td>
		</TR>
	</table>
	<table cellpadding="4" cellspacing="1" border="0" width="100%"
		align="center" class="tbcolor" id="addTable">
		<tr class="titletab">
			<th align="center" colspan="2">
				校验报表
			</th>
		</tr>
		<tr align="center" bgcolor="#ffffff">
			<td align="center">
				<table id="t1" cellspacing="0" cellpadding="0" border="0"
					width="60%" align="center" class="tbcolor" style="border:1 solid #cccccc;border-right:0px;border-right:0px;border-bottom:0px;">
					<TR class="middle" align="center" height="20">
						<td width="5%" align="center"><input type="checkbox" id="checkAll" onclick="_selAll()"/></td>
						<TD width="25%" align="center">
							<b>单元格名称</b>
						</TD>
						<td width="70%" align="center">
							<b>单元格值</b>
						</td>
					</TR>
					<logic:present name="aflist" scope="request">
						<logic:iterate id="item" name="aflist">
							<tr onclick="check1(this)" height='20' align='center' bgcolor='#FFFFFF'>
								<td><input type="checkbox" name="cks" value="<bean:write name="item" property="id.templateId"/>,<bean:write name="item" property="id.versionId"/>,<bean:write name="item" property="id.cellName"/>"/></td>
								<td><bean:write name="item" property="id.cellName"/></td>
								<td><bean:write name="item" property="cellContext"/></td>
							</tr>
						</logic:iterate>
					</logic:present>
				</table>
			</td>
		</tr>
	</table>
	<logic:present name="next">
		<input type="button" class="input-button" name="save" value="下一步"
		id="subButton" onclick="_submit('${templateId}','${versionId}','1','${reportName}','${next}')">
		&nbsp;&nbsp;&nbsp;&nbsp;
	</logic:present>
	<logic:empty name="next">
		<input type="button" class="input-button" name="save" value="保   存"
		id="subButton" onclick="_submit('${templateId}','${versionId}','0','${reportName }','${next}')">
		<input type="button" class="input-button" onclick="_back('${templateId}','${versionId}')" value=" 返 回 ">
	</logic:empty>
</body>
</html:html>
