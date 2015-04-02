<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.security.Operator"%>
<%@ page import="com.cbrc.smis.common.Config"%>


<html:html locale="true">
<head>

	<%
		Operator operator = null;
		if (session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME) != null)
			operator = (Operator) session
			.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		else
			operator = new Operator();
		
		/** 报表选中标志 **/
		String reportFlg = "0";
		
		if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
			reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
		}
		
		boolean yjhbb = false;
		boolean rhbb = false;
		boolean qtbb = false;
		boolean ldsy_jyyl = operator.isExitsThisUrl("JYZKYL"); //经营状况一览
		boolean xxgg_xxfb = operator.isExitsThisUrl("XXFB"); //信息发布
		boolean xxgg_xxck = operator.isExitsThisUrl("XXCK"); //信息查看
		boolean xtgl_jggl = operator.isExitsThisUrl("JGGL"); //机构管理
		boolean xtgl_thgl = operator.isExitsThisUrl("YHGL"); //用户管理
		boolean xtgl_jsgl = operator.isExitsThisUrl("JSGL"); //角色管理
		boolean xtgl_yhzgl = operator.isExitsThisUrl("YHZGL"); //用户组管理
		boolean xtgl_zxyh = operator.isExitsThisUrl("ZXYH"); //在线用户
		boolean xtgl_zdgl = operator.isExitsThisUrl("ZDGL"); //字典管理
		boolean xtgl_rzgl = operator.isExitsThisUrl("RZGL"); //日志管理
		boolean yjh_bbcl_ybbsc = operator.isExitsThisUrl("YBBSC"); //报表生成
		boolean yjh_bbcl_ybbxz = operator.isExitsThisUrl("YBBXZ"); //报表下载
		boolean yjh_bbcl_sc = operator.isExitsThisUrl("YSC"); //报表上传
		boolean yjh_bbcl_ybbfh = operator.isExitsThisUrl("YBBFH"); //报表复核
		boolean yjh_bbcl_ybbjy = operator.isExitsThisUrl("YBBJY"); //报表校验
		boolean yjh_bbcl_ybbhz = operator.isExitsThisUrl("YBBHZ"); //报表汇总
		boolean yjh_bbcl_ybbsq = operator.isExitsThisUrl("YBBSQ"); //报表审签
		boolean yjh_bbcl_ybbcb = operator.isExitsThisUrl("YBBCB"); //报表重报
		boolean yjh_bbcx_ybbcx = operator.isExitsThisUrl("YBBCX"); //报表查询
		boolean yjh_bbcx_ylhcx = operator.isExitsThisUrl("YLHCX"); //灵活查询
		boolean yjh_bbcx_ybbdc = operator.isExitsThisUrl("YBBDC"); //报表导出
		boolean yjh_bbcx_ybbrzcx = operator.isExitsThisUrl("YBBRZCX"); //报表日志查询
		boolean yjh_bbgl_ybbdygl = operator.isExitsThisUrl("YBBDYGL"); //报表定义管理
		boolean yjh_bbgl_ygbgxgl  = operator.isExitsThisUrl("YGBGXGL"); //归并关系管理
		boolean yjh_bbgl_yjygxgl  = operator.isExitsThisUrl("YJYGXGL"); //校验关系管理
		boolean yjh_bbgl_ybbsxgl  = operator.isExitsThisUrl("YBBSXGL"); //报表时限管理
		boolean rh_bbcl_rbbsc  = operator.isExitsThisUrl("RBBSC"); //报表生成
		boolean rh_bbcl_rbbxz  = operator.isExitsThisUrl("RBBXZ"); //报表下载
		boolean rh_bbcl_rsc  =  operator.isExitsThisUrl("RSC"); //报表上传
		boolean rh_bbcl_rbbfh  = operator.isExitsThisUrl("RBBFH"); //报表复核
		boolean rh_bbcl_rbbjy  =  operator.isExitsThisUrl("RBBJY"); //报表校验
		boolean rh_bbcl_rbbhz  = operator.isExitsThisUrl("RBBHZ"); //报表汇总
		boolean rh_bbcl_rbbcb  = operator.isExitsThisUrl("RBBCB"); //报表重报
		boolean rh_bbcx_rbbcx  =  operator.isExitsThisUrl("RBBCX"); //报表查询
		boolean rh_bbcx_rlhcx  =  operator.isExitsThisUrl("RLHCX"); //灵活查询
		boolean rh_bbcx_rbbdc  = operator.isExitsThisUrl("RBBDC"); //报表导出
		boolean rh_bbcx_rhbbdc = operator.isExitsThisUrl("RHBBDC");//人行格式导出
		boolean rh_bbcx_rbbrzcx  = operator.isExitsThisUrl("RBBRZCX"); //报表日志查询
		boolean rh_bbgl_rbbdygl  =  operator.isExitsThisUrl("RBBDYGL"); //报表定义管理
		boolean rh_bbgl_rgbgxgl  =  operator.isExitsThisUrl("RGBGXGL"); //归并关系管理
		boolean rh_bbgl_rjygxgl  = operator.isExitsThisUrl("RJYGXGL"); //校验关系管理
		boolean rh_bbgl_rbbsxgl  = operator.isExitsThisUrl("RBBSXGL"); //报表时限管理
		boolean qt_bbcl_qbbsc  =  operator.isExitsThisUrl("QBBSC"); //报表生成
		boolean qt_bbcl_qbbxz  =  operator.isExitsThisUrl("QBBXZ"); //报表下载
		boolean qt_bbcl_qsc  =  operator.isExitsThisUrl("QSC"); //报表上传
		boolean qt_bbcl_qbbfh  = operator.isExitsThisUrl("QBBFH"); //报表复核
		boolean qt_bbcl_qbbjy  =  operator.isExitsThisUrl("QBBJY"); //报表校验
		boolean qt_bbcl_qbbhz  =  operator.isExitsThisUrl("QBBHZ"); //报表汇总
		boolean qt_bbcl_qbbcb  =  operator.isExitsThisUrl("QBBCB"); //报表重报
		boolean qt_bbcx_qbbcx  =  operator.isExitsThisUrl("QBBCX"); //报表查询
		boolean qt_bbcx_qlhcx  =  operator.isExitsThisUrl("QLHCX"); //灵活查询
		boolean qt_bbcx_qfxbcx  =  operator.isExitsThisUrl("QFXBCX"); //分析表查询
		boolean qt_bbcx_qbbrzcx  =  operator.isExitsThisUrl("QBBRZCX"); //报表日志查询
		boolean qt_bbgl_qbbdygl  =  operator.isExitsThisUrl("QBBDYGL"); //报表定义管理
		boolean qt_bbgl_qgbgxgl  =  operator.isExitsThisUrl("QGBGXGL"); //归并关系管理
		boolean qt_bbgl_qjygxgl  =  operator.isExitsThisUrl("QJYGXGL"); //校验关系管理
		boolean qt_bbgl_qbbsxgl  =  operator.isExitsThisUrl("QBBSXGL"); //报表时限管理
		boolean rh_bbcl_rcj = operator.isExitsThisUrl("RBBCJ"); //人行报表数据采集
		boolean qt_bbcl_qbcj = operator.isExitsThisUrl("QBBCJ"); //其他报表数据采集
		boolean xtgl_hlgl = operator.isExitsThisUrl("HLGL");//汇率管理
		
		boolean fxjc  =  operator.isExitsThisUrl("FXJC"); //风险监测
		if(yjh_bbcl_ybbsc || yjh_bbcl_ybbxz || yjh_bbcl_sc || yjh_bbcl_ybbfh || yjh_bbcl_ybbjy 
			|| yjh_bbcl_ybbhz || yjh_bbcl_ybbsq || yjh_bbcl_ybbcb || yjh_bbcx_ybbcx || yjh_bbcx_ylhcx 
			|| yjh_bbcx_ybbdc || yjh_bbcx_ybbrzcx || yjh_bbgl_ybbdygl || yjh_bbgl_ygbgxgl || yjh_bbgl_yjygxgl  ){
			yjhbb =true;
		}
		if(rh_bbcl_rbbsc || rh_bbcl_rbbxz || rh_bbcl_rsc || rh_bbcl_rbbfh || rh_bbcl_rbbjy 
			|| rh_bbcl_rbbhz || rh_bbcl_rbbcb || rh_bbcx_rbbcx || rh_bbcx_rlhcx   
			|| rh_bbcx_rbbdc || rh_bbcx_rhbbdc || rh_bbcx_rbbrzcx  || rh_bbgl_rbbdygl  || rh_bbgl_rgbgxgl  || rh_bbgl_rjygxgl 
			|| rh_bbcl_rcj ){
			rhbb =true;
		}
		if(qt_bbcl_qbbsc   || qt_bbcl_qbbxz   || qt_bbcl_qsc   || qt_bbcl_qbbfh   || qt_bbcl_qbbjy   
			|| qt_bbcl_qbbhz   || qt_bbcl_qbbcb   || qt_bbcx_qbbcx   || qt_bbcx_qlhcx   || qt_bbcx_qfxbcx   
			|| qt_bbcx_qbbrzcx   || qt_bbgl_qbbdygl   || qt_bbgl_qgbgxgl   || qt_bbgl_qjygxgl 
			|| qt_bbcl_qbcj  ){
			qtbb =true;
		}
	%>

	<html:base />
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="css/globalStyle.css" type="text/css" rel="stylesheet">
	<script language="JavaScript" src="js/tree.js"></script>
	<script language="JavaScript" src="js/tree_tpl.js"></script>
	<script language="javascript">
			
			var scriptldsy_jyyl=<%=ldsy_jyyl%>;
			var scriptxxgg_xxfb=<%=xxgg_xxfb%>;
			var scriptxxgg_xxck=<%=xxgg_xxck%>;
			var scriptxtgl_jggl=<%=xtgl_jggl%>;
			var scriptxtgl_thgl=<%=xtgl_thgl%>;
			var scriptxtgl_jsgl=<%=xtgl_jsgl%>;
			var scriptxtgl_yhzgl=<%=xtgl_yhzgl%>;
			var scriptxtgl_zxyh=<%=xtgl_zxyh%>;
			var scriptxtgl_zdgl=<%=xtgl_zdgl%>;
			var scriptxtgl_rzgl=<%=xtgl_rzgl%>;
			var scriptyjh_bbcl_ybbsc=<%=yjh_bbcl_ybbsc%>;
			var scriptyjh_bbcl_ybbxz=<%=yjh_bbcl_ybbxz%>;
			var scriptyjh_bbcl_sc=<%=yjh_bbcl_sc%>;
			var scriptyjh_bbcl_ybbfh=<%=yjh_bbcl_ybbfh%>;
			var scriptyjh_bbcl_ybbjy=<%=yjh_bbcl_ybbjy%>;
			var scriptyjh_bbcl_ybbhz=<%=yjh_bbcl_ybbhz%>;
			var scriptyjh_bbcl_ybbsq=<%=yjh_bbcl_ybbsq%>;
			var scriptyjh_bbcl_ybbcb=<%=yjh_bbcl_ybbcb%>;
			var scriptyjh_bbcx_ybbcx=<%=yjh_bbcx_ybbcx%>;
			var scriptyjh_bbcx_ylhcx=<%=yjh_bbcx_ylhcx%>;
			var scriptyjh_bbcx_ybbdc=<%=yjh_bbcx_ybbdc%>;
			var scriptyjh_bbcx_ybbrzcx=<%=yjh_bbcx_ybbrzcx%>;
			var scriptyjh_bbgl_ybbdygl=<%=yjh_bbgl_ybbdygl%>;
			var scriptyjh_bbgl_ygbgxgl=<%=yjh_bbgl_ygbgxgl%>;
			var scriptyjh_bbgl_yjygxgl=<%=yjh_bbgl_yjygxgl%>;
			var scriptyjh_bbgl_ybbsxgl=<%=yjh_bbgl_ybbsxgl%>;
			var scriptrh_bbcl_rbbsc=<%=rh_bbcl_rbbsc%>;
			var scriptrh_bbcl_rbbxz=<%=rh_bbcl_rbbxz%>;
			var scriptrh_bbcl_rsc=<%=rh_bbcl_rsc%>;
			var scriptrh_bbcl_rbbfh=<%=rh_bbcl_rbbfh%>;
			var scriptrh_bbcl_rbbjy=<%=rh_bbcl_rbbjy%>;
			var scriptrh_bbcl_rbbhz=<%=rh_bbcl_rbbhz%>;
			var scriptrh_bbcl_rbbcb=<%=rh_bbcl_rbbcb%>;
			var scriptrh_bbcx_rbbcx=<%=rh_bbcx_rbbcx%>;
			var scriptrh_bbcx_rlhcx=<%=rh_bbcx_rlhcx%>;
			var scriptrh_bbcx_rbbdc=<%=rh_bbcx_rbbdc%>;
			var scriptrh_bbcx_rhbbdc=<%=rh_bbcx_rhbbdc%>;
			var scriptrh_bbcx_rbbrzcx=<%=rh_bbcx_rbbrzcx%>;
			var scriptrh_bbgl_rbbdygl=<%=rh_bbgl_rbbdygl%>;
			var scriptrh_bbgl_rgbgxgl=<%=rh_bbgl_rgbgxgl%>;
			var scriptrh_bbgl_rjygxgl=<%=rh_bbgl_rjygxgl%>;
			var scriptrh_bbgl_rbbsxgl=<%=rh_bbgl_rbbsxgl%>;
			var scriptqt_bbcl_qbbsc=<%=qt_bbcl_qbbsc%>;
			var scriptqt_bbcl_qbbxz=<%=qt_bbcl_qbbxz%>;
			var scriptqt_bbcl_qsc=<%=qt_bbcl_qsc%>;
			var scriptqt_bbcl_qbbfh=<%=qt_bbcl_qbbfh%>;
			var scriptqt_bbcl_qbbjy=<%=qt_bbcl_qbbjy%>;
			var scriptqt_bbcl_qbbhz=<%=qt_bbcl_qbbhz%>;
			var scriptqt_bbcl_qbbcb=<%=qt_bbcl_qbbcb%>;
			var scriptqt_bbcx_qbbcx=<%=qt_bbcx_qbbcx%>;
			var scriptqt_bbcx_qlhcx=<%=qt_bbcx_qlhcx%>;
			var scriptqt_bbcx_qfxbcx=<%=qt_bbcx_qfxbcx%>;
			var scriptqt_bbcx_qbbrzcx=<%=qt_bbcx_qbbrzcx%>;
			var scriptqt_bbgl_qbbdygl=<%=qt_bbgl_qbbdygl%>;
			var scriptqt_bbgl_qgbgxgl=<%=qt_bbgl_qgbgxgl%>;
			var scriptqt_bbgl_qjygxgl=<%=qt_bbgl_qjygxgl%>;
			var scriptqt_bbgl_qbbsxgl=<%=qt_bbgl_qbbsxgl%>;
			
			var scriptrh_bbcl_rcj=<%=rh_bbcl_rcj%>;
			var scriptqt_bbcl_qbcj=<%=qt_bbcl_qbcj%>;
			
			var scriptReportFlg=<%=reportFlg%>;

			/**
			 * 领导首页 
			 */
			var LDSY="LDSY";
			/**
			 * 银监会数据采集 
			 */
			var YSJCJ="YSJCJ";
			/**
			 * 银监会报表处理 
			 */
			var YBBCL="YBBCL";
			/**
			 * 银监会报表查询
			 */
			var YBBCX="YBBCX";
			/**
			 * 银监会报表管理
			 */
			var YBBGL="YBBGL";
			/**
			 * 人行数据采集 
			 */
			var RSJCJ="RSJCJ";
			/**
			 * 人行报表处理 
			 */
			var RBBCL="RBBCL";
			/**
			 * 人行报表查询 
			 */
			var RBBCX="RBBCX";
			/**
			 * 人行报表管理
			 */
			var RBBGL="RBBGL";
			/**
			 * 其他数据采集 
			 */
			var QSJCJ="QSJCJ";
			/**
			 * 其他报表处理 
			 */
			var QBBCL="QBBCL";
			/**
			 * 其他报表查询
			 */
			var QBBCX="QBBCX";
			/**
			 * 其他报表管理
			 */
			var QBBGL="QBBGL";
			
			/**
			 * 系统管理
			 */
			var XTGL="XTGL";
			
			/**
			 * 信息公告
			 */
			var XXGG="XXGG";
			

			/**
			 * 点击菜单图片事件
			 */
			function menu_pic_click(menu){
				menu_hide(menu);
				
				var obj=eval("document.getElementById('tr_" + menu + "')");
				
				if(obj.style.display==""){ 
					obj.style.display="none"  
				}else{
					obj.style.display="";
				}
			}
			
			/**
			 * 隐藏子菜单
			 */
			function menu_hide(menu){
				//if(menu!=LDSY && (scriptldsy_jyyl == true))
				//	eval("document.getElementById('tr_" + LDSY + "').style.display='none'");
					
				//if(menu!=XXGG && (scriptxxgg_xxfb == true || scriptxxgg_xxck == true)) 
				//	eval("document.getElementById('tr_" + XXGG + "').style.display='none'");
					
				if(menu!=XTGL && (scriptxtgl_jggl == true || scriptxtgl_thgl == true || scriptxtgl_jsgl == true || 
				scriptxtgl_yhzgl == true || scriptxtgl_zxyh == true || scriptxtgl_zdgl == true || 
				scriptxtgl_rzgl == true))
					eval("document.getElementById('tr_" + XTGL + "').style.display='none'");
				
				if(menu!=YSJCJ && (scriptReportFlg=="1") && (scriptyjh_bbcl_ybbsc == true || scriptyjh_bbcl_ybbxz == true ))
					eval("document.getElementById('tr_" + YSJCJ + "').style.display='none'");
					
				if(menu!=YBBCL && (scriptReportFlg=="1") && (scriptyjh_bbcl_sc == true || scriptyjh_bbcl_ybbfh == true || 
				scriptyjh_bbcl_ybbjy == true || scriptyjh_bbcl_ybbhz == true || 
				scriptyjh_bbcl_ybbsq == true || scriptyjh_bbcl_ybbcb == true))
					eval("document.getElementById('tr_" + YBBCL + "').style.display='none'");
					
				if(menu!=YBBCX && (scriptReportFlg=="1") && (scriptyjh_bbcx_ybbcx == true || scriptyjh_bbcx_ylhcx == true || 
				scriptyjh_bbcx_ybbdc == true || scriptyjh_bbcx_ybbrzcx == true))
					eval("document.getElementById('tr_" + YBBCX + "').style.display='none'");
	
				if(menu!=YBBGL && (scriptReportFlg=="1") && (scriptyjh_bbgl_ybbdygl == true || scriptyjh_bbgl_ygbgxgl == true || 
				scriptyjh_bbgl_yjygxgl == true || scriptyjh_bbgl_ybbsxgl == true))
					eval("document.getElementById('tr_" + YBBGL + "').style.display='none'");
	
				if(menu!=RSJCJ && (scriptReportFlg=="2") && (scriptrh_bbcl_rbbsc == true || scriptrh_bbcl_rbbxz == true || scriptrh_bbcl_rcj == true))
					eval("document.getElementById('tr_" + RSJCJ + "').style.display='none'");
				
				if(menu!=RBBCL && (scriptReportFlg=="2") && (scriptrh_bbcl_rsc == true || 
				scriptrh_bbcl_rbbfh == true || scriptrh_bbcl_rbbjy == true || scriptrh_bbcl_rbbhz == true || 
				scriptrh_bbcl_rbbcb == true || scriptrh_bbcx_rhbbdc == true))
					eval("document.getElementById('tr_" + RBBCL + "').style.display='none'");
	
				if(menu!=RBBCX && (scriptReportFlg=="2") && (scriptrh_bbcx_rbbcx == true || scriptrh_bbcx_rlhcx == true || 
				scriptrh_bbcx_rbbdc == true|| scriptrh_bbcx_rbbrzcx == true))
					eval("document.getElementById('tr_" + RBBCX + "').style.display='none'");
	
				if(menu!=RBBGL && (scriptReportFlg=="2") && (scriptrh_bbgl_rbbdygl == true || scriptrh_bbgl_rgbgxgl == true || 
				scriptrh_bbgl_rjygxgl == true || scriptrh_bbgl_rbbsxgl == true))
					eval("document.getElementById('tr_" + RBBGL + "').style.display='none'");
				
				if(menu!=QSJCJ && (scriptReportFlg=="3") && (scriptqt_bbcl_qbbsc == true || scriptqt_bbcl_qbbxz == true || scriptqt_bbcl_qbcj == true))
					eval("document.getElementById('tr_" + QSJCJ + "').style.display='none'");
				
				if(menu!=QBBCL && (scriptReportFlg=="3") && ( scriptqt_bbcl_qsc == true || 
				scriptqt_bbcl_qbbfh == true || scriptqt_bbcl_qbbjy == true || scriptqt_bbcl_qbbhz == true || 
				scriptqt_bbcl_qbbcb == true))
					eval("document.getElementById('tr_" + QBBCL + "').style.display='none'");
	
				if(menu!=QBBCX && (scriptReportFlg=="3") && (scriptqt_bbcx_qbbcx == true || scriptqt_bbcx_qlhcx == true || 
				scriptqt_bbcx_qfxbcx == true || scriptqt_bbcx_qbbrzcx == true))
					eval("document.getElementById('tr_" + QBBCX + "').style.display='none'");
	
				if(menu!=QBBGL && (scriptReportFlg=="3") && (scriptqt_bbgl_qbbdygl == true || scriptqt_bbgl_qgbgxgl == true || 
				scriptqt_bbgl_qjygxgl == true || scriptqt_bbgl_qbbsxgl == true))
					eval("document.getElementById('tr_" + QBBGL + "').style.display='none'");		
			 
			}
			
		    /**
		     * 点击菜单事件
		     */
		    function menu_click(_url){
		    	if(_url=="") return;
		    	window.parent.mainFrame.contents.location.assign(_url);
		    } 
		     function changeReport(reportObj){	 
			  	 //var index=reportObj.selectedIndex;
				 //var reportFlg=reportObj.options[index].value;
				var reportFlg=reportObj;
				window.location="<%=request.getContextPath()%>/reportPortal.do?reportFlg="+reportFlg;
				
			 }
			 function loadpage(){
			 	window.parent.mainFrame.contents.location.assign('main.jsp');
			 }

			//报表选择区域
			function switchBarOn()
			{
				if (document.getElementById("div1"))
				{
				 document.all("div1").style.display="";
				}
			}


			//报表选择区域
			function switchBarOff()
			{
				if (document.getElementById("div1"))
				{
				 document.all("div1").style.display="none";
				}
			}
		</script>
</head>
<body background="image/navibk.jpg" onload="loadpage()">
	
	<table border="0" cellpadding="0" cellspacing="1">
		<tr>
			<td height="1" width="160" >				
			</td>
		</tr>
		<tr>
			<td height="30" width="160" align="center">
			<%
				if (reportFlg.equals("0")) {
			%>
			<img src="image/rpt_choose.gif" onmouseover="switchBarOn()">
			<% 
				}else if(reportFlg.equals("1")) {
			%>
			<img src="image/rpt_choose.gif" onmouseover="switchBarOn()">
			<%
				}else if(reportFlg.equals("2")) {
			%>
			<img src="image/rpt_choose.gif" onmouseover="switchBarOn()">
			<% 
				}else if(reportFlg.equals("3")) {
			%>
			<img src="image/rpt_choose.gif" onmouseover="switchBarOn()">
			<%	} %>
			
			<div id='div1' style="display:none;position:absolute;z-index:1;left:10px;top:55px;"  onmouseout="switchBarOff()" >
			<table border="0" cellpadding="1" cellspacing="1">
					<%
						if (reportFlg.equals("0")) {
					%>
					<tr>
						<td background="image/button_q.gif" height="75">
						<input class="input-button" onclick="changeReport(1)" type="button" value="银监会报表">
						<input class="input-button" onclick="changeReport(2)" type="button" value="人行报表">
						<input class="input-button" onclick="changeReport(3)" type="button" value="其他报表">
						</td>
					</tr>
					<% 
						}else if(reportFlg.equals("1")) {
					%>
					<tr>
						<td background="image/button_q.gif" height="75">
						<input class="input-button" onclick="changeReport(2)" type="button" value="人行报表">
						<input class="input-button" onclick="changeReport(3)" type="button" value="其他报表">
						<input class="input-button" onclick="changeReport(0)" type="button" value="返回首页">
						</td>
					</tr>
					<% 
						}else if(reportFlg.equals("2")) {
					%>
					<tr>
						<td background="image/button_q.gif" height="75">					
						<input class="input-button" onclick="changeReport(1)" type="button" value="银监会报表">
						<input class="input-button" onclick="changeReport(3)" type="button" value="其他报表">
						<input class="input-button" onclick="changeReport(0)" type="button" value="返回首页">
						</td>
					</tr>
					<% 
						}else if(reportFlg.equals("3")) {
					%>
					<tr>
						<td>
						<input class="input-button" onclick="changeReport(1)" type="button" value="人行报表">
						<input class="input-button" onclick="changeReport(2)" type="button" value="其他报表">
						<input class="input-button" onclick="changeReport(0)" type="button" value="返回首页">
						</td>
					</tr>					
					<%	} %>
				</table>
				</div>
			</td>
		</tr>

		<tr>
			<td background="image/navisp.jpg" width="160">
			</td>
		</tr>
		
		<tr>
			<td height="3">
			</td>
		</tr>
		
		<%
			//if (ldsy_jyyl) {
		%>
		<%-- <tr>
			<td height="30"  background="image/button_blue.jpg">	
					<a href="javascript:menu_pic_click('LDSY')"> 
					 <font class=b size="3"><b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;领导首页</b></font></a>
			</td>
		</tr>
		<tr id="tr_LDSY"  style="display:none">
			<td>
				<!--报表查询(Begin)--->
				<table border="0" cellpadding="0" cellspacing="1" width="100%"
					align="center">
					<tr>
						<td background="image/button_q.gif" height="25">
							
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('manager_frontpage/general_view/view_index.jsp')">经营状况一览 </a>
							
						</td>
					</tr>
					
					<tr>
						<td height="5"></td>
					</tr>
				</table>				
			</td>
		</tr>

		--%>
		
		<!--银监会报表(Begin)--->
		<%
			//}
			if(reportFlg.equals("1")){

			if (yjh_bbcl_ybbsc  || yjh_bbcl_ybbxz ) {
		%>
		<tr>
			<td height="30"  background="image/button_blue.jpg">
				<a href="javascript:menu_pic_click('YSJCJ')"> 
			<font class=b size="3"><b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;数据采集</b></font> </a>
			</td>
		</tr>
		<tr id="tr_YSJCJ" style="display:none">
			<td>
				<!--报表处理(Begin)--->
				<table border="0" cellpadding="0" cellspacing="1" width="100%"
					align="center">

					<%
						if (yjh_bbcl_ybbsc) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('afReportProduct.do')">报表生成</a>	
						</td>
					</tr>
					<%
					}
					%>						

					<%
						if (yjh_bbcl_ybbxz) {
					%>
					<tr>
						<td  background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('template/templateDownloadList.do')">报表下载</a>
						</td>
					</tr>
					<%
					}
					%>	

					<tr>
						<td height="5"></td>
					</tr>
				</table>
				<!--报表管理(End)-->
			</td>
		</tr>

		
		<%} %>
		<%
			if ( yjh_bbcl_sc   || yjh_bbcl_ybbfh || yjh_bbcl_ybbjy 
			|| yjh_bbcl_ybbhz   || yjh_bbcl_ybbsq || yjh_bbcl_ybbcb ) {
		%>
		<tr>
			<td height="30"  background="image/button_blue.jpg">
				<a href="javascript:menu_pic_click('YBBCL')"> 
			<font class=b size="3"><b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;报表处理</b></font> </a>
			</td>
		</tr>

		<tr id="tr_YBBCL" style="display:none">
			<td>
				<!--报表处理(Begin)--->
				<table border="0" cellpadding="0" cellspacing="1" width="100%"
					align="center">
					
					<%
						if (yjh_bbcl_sc) {
					%>
					<tr>
						<td  background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('viewDataReport.do')">报表上报</a>
						</td>
					</tr>
					<%
					}
					%>

					<%
						if (yjh_bbcl_ybbjy ) {
					%>
					<tr>
						<td  background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('dateQuary/manualCheckRepSHJY.do')">报表校验</a>
						</td>
					</tr>
					<%
					}
					%>
					
					<%
						if (yjh_bbcl_ybbhz ) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('collectReport/search_data_stat.jsp')">报表汇总</a>
						</td>
					</tr>
					<%
					}
					%>
					
					<%-- 
					<tr>

						<td background="image/button_q.gif" height="25">
							<%
								if (yjh_bbcl_ybbfh ) {
							%>
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a	href="javascript:menu_click('dateQuary/manualCheckRepRecheck.do')">报表复核</a>
							<%
							}
							%>
						</td>
					</tr>
					--%>

					<%
						if (yjh_bbcl_ybbsq ) {
					%>
					<tr>
						<td  background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('dateQuary/manualCheckRep.do')">报表审核</a>
						</td>
					</tr>
					<%
					}
					%>

					<%
						if (yjh_bbcl_ybbcb) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('report_mgr/report_again_mgr/report_again_mgr.jsp')">报表重报</a>
						</td>
					</tr>
					<%
					}
					%>
					
					<tr>
						<td height="5"></td>
					</tr>
				</table>
				<!--报表管理(End)-->
			</td>
		</tr>

		<%
			}
			if (yjh_bbcx_ybbcx         
			|| yjh_bbcx_ylhcx  || yjh_bbcx_ybbdc  || yjh_bbcx_ybbrzcx ) {
		%>
		<tr>
			<td height="30"  background="image/button_blue.jpg">
				<a href="javascript:menu_pic_click('YBBCX')"> 
				<font class=b size="3"><b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;报表查询</b></font> </a>
			</td>
		</tr>
		
		<tr id="tr_YBBCX" style="display:none">
			<td>
				<!--报表查询(Begin)--->
				<table border="0" cellpadding="0" cellspacing="1" width="100%"
					align="center">

					<%
						if (yjh_bbcx_ybbcx ) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('reportSearch/viewReportStatAction.do')">报表查询</a>
						</td>
					</tr>
					<%
					}
					%>

					<%
						if (yjh_bbcx_ylhcx) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('customViewAFReport.do')">灵活查询</a>
						</td>
					</tr>
					<%
					}
					%>
					
					<%
						if (yjh_bbcx_ybbdc) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('exportAFReport.do')">报表导出</a>
						</td>
					</tr>
					<%
					}
					%>
					
					<%
						if (yjh_bbcx_ybbrzcx) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('reportStatisticsCollect.do?reportFlg=<%=reportFlg %>')">报表统计</a>
						</td>
					</tr>
					<%
					}
					%>
								
					<tr>
						<td height="5"></td>
					</tr>
				</table>
				<!--报表管理(End)-->
			</td>
		</tr>
	
		
		<%
			}
			if (yjh_bbgl_ybbdygl        
			|| yjh_bbgl_ygbgxgl || yjh_bbgl_yjygxgl || yjh_bbgl_ybbsxgl) {
		%>
		<tr>
			<td height="30"  background="image/button_blue.jpg">
				<a href="javascript:menu_pic_click('YBBGL')"> 
				<font class=b size="3"><b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;报表管理</b></font> </a>
			</td>
		</tr>
		
		<tr id="tr_YBBGL" style="display:none">
			<td>
				<!--报表管理(Begin)--->
				<table border="0" cellpadding="0" cellspacing="1" width="100%"
					align="center">
	
					<%
						if (yjh_bbgl_ybbdygl ) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('template/viewTemplate.do')">报表定义管理</a>
						</td>
					</tr>
					<%
					}
					%>
					
					<%
						if (yjh_bbgl_ygbgxgl  ) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('afreportmerger.do')">归并关系管理</a>
						</td>
					</tr>
					<%
					}
					%>

					<%
						if (yjh_bbgl_yjygxgl ) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('afreportcheck.do')">校验维护管理</a>
						</td>
					</tr>					
					<%
					}
					%>

					<tr>
						<td height="5"></td>
					</tr>
				</table>
				<!--报表管理(End)-->
			</td>
		</tr>

		<!--银监会报表(End)--->
		<!--人行报表(Begin)--->
		<%
			}
			}

			if(reportFlg.equals("2")){
			
		%>
		<%
			if (rh_bbcl_rcj  || rh_bbcl_rbbsc ||  rh_bbcl_rbbxz) {
		%>
		<tr>
			<td height="30"  background="image/button_blue.jpg">
				<a href="javascript:menu_pic_click('RSJCJ')"> 
			<font class=b size="3"><b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;数据采集</b></font> </a>
			</td>
		</tr>
		<tr id="tr_RSJCJ" style="display:none">
			<td>
				<!--报表处理(Begin)--->
				<table border="0" cellpadding="0" cellspacing="1" width="100%"
					align="center">

					<%
						if (rh_bbcl_rcj) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('viewGatherReport.do')">数据采集</a>
						</td>
					</tr>
					<%
					}
					%>

					<%
						if (rh_bbcl_rbbsc) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('afReportProduct.do')">报表生成</a>
						</td>
					</tr>
					<%
					}
					%>

					<%
						if (rh_bbcl_rbbxz) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('report/reportDownloadList.do')">报表下载</a>
						</td>
					</tr>
					<%
					}
					%>

					<tr>
						<td height="5"></td>
					</tr>
				</table>
				<!--报表管理(End)-->
			</td>
		</tr>

		<%} %>
		<%
			if ( rh_bbcl_rsc   || rh_bbcl_rbbfh || rh_bbcl_rbbjy  
			|| rh_bbcl_rbbhz  || rh_bbcl_rbbcb || rh_bbcx_rhbbdc) {
		%>
		<tr>
			<td height="30"  background="image/button_blue.jpg">
				<a href="javascript:menu_pic_click('RBBCL')"> 
				<font class=b size="3"><b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;报表处理</b></font> </a>
			</td>
		</tr>

		<tr id="tr_RBBCL" style="display:none">
			<td>
				<!--报表处理(Begin)--->
				<table border="0" cellpadding="0" cellspacing="1" width="100%"
					align="center">
					
					<%
						if (rh_bbcl_rsc) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('viewNXDataReport.do')">报表上传</a>
						</td>
					</tr>
					<%
					}
					%>

					<%
						if (rh_bbcl_rbbhz) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('viewCollectNX.do')">报表汇总</a>
						</td>
					</tr>
					<%
					}
					%>

					<%
						if (rh_bbcl_rbbjy) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('dateQuary/manualCheckRepValidate.do')">报表校验</a>
						</td>
					</tr>
					<%
					}
					%>
					
					<%
						if (rh_bbcl_rbbfh) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a	href="javascript:menu_click('dateQuary/manualCheckRepNXRecheck.do')">报表审核</a>
						</td>
					</tr>
					<%
					}
					%>
	
					<%
						if (rh_bbcl_rbbcb) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('gznx/report_again/report_again_lst.jsp')">报表重报</a>
						</td>
					</tr>
					<%
					}
					%>

					
					
					<%
						if (rh_bbcx_rhbbdc ) {
					%>					
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('exportRhAFReport.do')">报表报送</a>
						</td>
					</tr>					
					<%
					}
					%>
					
					<tr>
						<td height="5"></td>
					</tr>
				</table>
				<!--报表管理(End)-->
			</td>
		</tr>
				
		
		<%
			}
			if (rh_bbcx_rbbcx     
			|| rh_bbcx_rlhcx || rh_bbcx_rbbdc || rh_bbcx_rbbrzcx) {
		%>
		<tr>
			<td height="30"  background="image/button_blue.jpg">
				<a href="javascript:menu_pic_click('RBBCX')"> 
				<font class=b size="3"><b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;报表查询</b></font> </a>
			</td>
		</tr>
		
		<tr id="tr_RBBCX" style="display:none">
			<td>
				<!--报表查询(Begin)--->
				<table border="0" cellpadding="0" cellspacing="1" width="100%"
					align="center">

					<%
						if (rh_bbcx_rbbcx) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('reportSearch/viewReportStatNX.do')">报表查询</a>
						</td>
					</tr>
					<%
					}
					%>

					<%
						if (rh_bbcx_rlhcx) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('customViewAFReport.do')">灵活查询</a>
						</td>
					</tr>
					<%
					}
					%>
					
					<%
						if (rh_bbcx_rbbdc ) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('exportAFReport.do')">报表导出</a>
						</td>
					</tr>
					<%
					}
					%>
										
					<%
						if (rh_bbcx_rbbrzcx ) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('reportStatisticsCollect.do?reportFlg=<%=reportFlg %>')">报表统计</a>
						</td>
					</tr>
					<%
					}
					%>
								
					<tr>
						<td height="5"></td>
					</tr>
				</table>
				<!--报表管理(End)-->
			</td>
		</tr>
		
		<%
			}
			if (rh_bbgl_rbbdygl   
			|| rh_bbgl_rgbgxgl   || rh_bbgl_rjygxgl   || rh_bbgl_rbbsxgl ) {
		%>
		<tr>
			<td height="30"  background="image/button_blue.jpg">
				<a href="javascript:menu_pic_click('RBBGL')"> 
				<font class=b size="3"><b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;报表管理</b></font> </a>
			</td>
		</tr>
		
		<tr id="tr_RBBGL" style="display:none">
			<td>
				<!--报表管理(Begin)--->
				<table border="0" cellpadding="0" cellspacing="1" width="100%"
					align="center">

					<%
						if (rh_bbgl_rbbdygl) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('afreportDefine.do')">报表定义管理</a>
						</td>
					</tr>
					<%
					}
					%>
					 
					<%
						if (rh_bbgl_rgbgxgl) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('afreportmerger.do')">归并关系管理</a>
						</td>
					</tr>
					<%
					}
					%>
					
					<%
						if (rh_bbgl_rjygxgl) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('afreportcheck.do')">校验维护管理</a>
						</td>
					</tr>
					<%
					}
					%>

					<tr>
						<td height="5"></td>
					</tr>
				</table>
				<!--报表管理(End)-->
			</td>
		</tr>
		
		<!--人行报表(End)--->
		<!--其他报表(Begin)--->
		<%
			}
			}
			if(reportFlg.equals("3")){
			

			if (qt_bbcl_qbcj  || qt_bbcl_qbbsc ||  qt_bbcl_qbbxz) {
		%>
		<tr>
			<td height="30"  background="image/button_blue.jpg">
				<a href="javascript:menu_pic_click('QSJCJ')"> 
			<font class=b size="3"><b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;数据采集</b></font> </a>
			</td>
		</tr>
		<tr id="tr_QSJCJ" style="display:none">
			<td>
				<!--报表处理(Begin)--->
				<table border="0" cellpadding="0" cellspacing="1" width="100%"
					align="center">

					<%
						if (qt_bbcl_qbcj) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('viewGatherReport.do')">报表采集</a>
						</td>
					</tr>
					<%
					}
					%>

					<%
						if (qt_bbcl_qbbsc) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('afReportProduct.do')">报表生成</a>	
						</td>
					</tr>
					<%
					}
					%>

					<%
						if (qt_bbcl_qbbxz) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('report/reportDownloadList.do')">报表下载</a>
						</td>
					</tr>
					<%
					}
					%>

					<tr>
						<td height="5"></td>
					</tr>
				</table>
				<!--报表管理(End)-->
			</td>
		</tr>
		
		<%} 		
			if ( qt_bbcl_qsc || qt_bbcl_qbbfh || qt_bbcl_qbbjy  
			|| qt_bbcl_qbbhz  || qt_bbcl_qbbcb || qt_bbcl_qbcj) {
		%>
		<tr>
			<td height="30"  background="image/button_blue.jpg">
				<a href="javascript:menu_pic_click('QBBCL')"> 
			<font class=b size="3"><b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;报表处理</b></font> </a>
			</td>
		</tr>

		<tr id="tr_QBBCL" style="display:none">
			<td>
				<!--报表处理(Begin)--->
				<table border="0" cellpadding="0" cellspacing="1" width="100%"
					align="center">
					
					<%
						if (qt_bbcl_qsc ) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('viewNXDataReport.do')">报表上传</a>
						</td>
					</tr>
					<%
					}
					%>

					<%
						if (qt_bbcl_qbbhz) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('viewCollectNX.do')">报表汇总</a>
						</td>
					</tr>
					<%
					}
					%>


					<%
						if (qt_bbcl_qbbjy) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('dateQuary/manualCheckRepValidate.do')">报表校验</a>
						</td>
					</tr>
					<%
					}
					%>

					
					<%
						if (qt_bbcl_qbbfh) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('dateQuary/manualCheckRepNXRecheck.do')">报表审核</a>
						</td>
					</tr>
					<%
					}
					%>
	 
					<%
						if (qt_bbcl_qbbcb) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('gznx/report_again/report_again_lst.jsp')">报表重报</a>
						</td>
					</tr>
					<%
					}
					%>

					
					<tr>
						<td height="5"></td>
					</tr>
				</table>
				<!--报表管理(End)-->
			</td>
		</tr>
		
		<%
			}
			if (qt_bbcx_qbbcx    
			|| qt_bbcx_qlhcx    || qt_bbcx_qfxbcx  || qt_bbcx_qbbrzcx ) {
		%>
		<tr>
			<td height="30"  background="image/button_blue.jpg">
				<a href="javascript:menu_pic_click('QBBCX')"> 
				<font class=b size="3"><b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;报表查询</b></font> </a>
			</td>
		</tr>
		
		<tr id="tr_QBBCX" style="display:none">
			<td>
				<!--报表查询(Begin)--->
				<table border="0" cellpadding="0" cellspacing="1" width="100%"
					align="center">

					<%
						if (qt_bbcx_qbbcx) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('reportSearch/viewReportStatNX.do')">报表查询</a>
						</td>
					</tr>
					<%
					}
					%>


					<%
						if (qt_bbcx_qlhcx) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('customViewAFReport.do')">灵活查询</a>
						</td>
					</tr>
					<%
					}
					%>
					
					<%
						if (qt_bbcx_qfxbcx ) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('exportAFReport.do')">报表导出</a>
						</td>
					</tr>
					<%
					}
					%>

					
					<%
						if (qt_bbcx_qbbrzcx) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('reportStatisticsCollect.do?reportFlg=<%=reportFlg %>')">报表统计</a>
						</td>
					</tr>
					<%
					}
					%>
								
					<tr>
						<td height="5"></td>
					</tr>
				</table>
				<!--报表管理(End)-->
			</td>
		</tr>
		
		<%
			}
			if (qt_bbgl_qbbdygl  
			|| qt_bbgl_qgbgxgl  || qt_bbgl_qjygxgl  || qt_bbgl_qbbsxgl) {
		%>
		<tr>
			<td height="30"  background="image/button_blue.jpg">
				<a href="javascript:menu_pic_click('QBBGL')"> 
				<font class=b size="3"><b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;报表管理</b></font> </a>
			</td>
		</tr>
		
		<tr id="tr_QBBGL" style="display:none">
			<td>
				<!--报表管理(Begin)--->
				<table border="0" cellpadding="0" cellspacing="1" width="100%"
					align="center">

					<%
						if (qt_bbgl_qbbdygl) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('afreportDefine.do')">报表定义管理</a>
						</td>
					</tr>
					<%
					}
					%>
					 
					<%
						if (qt_bbgl_qgbgxgl) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('afreportmerger.do')">归并关系管理</a>
						</td>
					</tr>
					<%
					}
					%>

					<%
						if (qt_bbgl_qjygxgl) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('afreportcheck.do')">校验维护管理</a>
						</td>
					</tr>
					<%
					}
					%>

					

					<tr>
						<td height="5"></td>
					</tr>
				</table>
				<!--报表管理(End)-->
			</td>
		</tr>
		
 		<%
			}
			}
		//	if (xtgl_jggl 
		//	|| xtgl_thgl || xtgl_jsgl  || xtgl_yhzgl || xtgl_zxyh || xtgl_zdgl || xtgl_rzgl || xtgl_hlgl) {
		%>
		
		<tr>
			<td height="30"  background="image/button_blue.jpg">
				<a href="javascript:menu_pic_click('XTGL')">
				<font class=b size="3"><b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;系统管理</b></font>
				</a>
			</td>
		</tr>
		<tr id="tr_XTGL" style="display:none">
			<td>
				<!--系统管理(Begin)--->
				<table border="0" cellpadding="0" cellspacing="1" width="100%"
					align="center">

					<%
						if (xtgl_jggl ) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('system_mgr/OrgInfo/view.do')">机构管理</a>
						</td>
					</tr>
					<%
					}
					%>

					<%
					if (operator.isExitsThisUrl("popedom_mgr/viewDepartment.do")) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('popedom_mgr/viewDepartment.do')">部门管理</a>
						</td>
					</tr>
					<%
					}
					%>

					<%
						if (xtgl_thgl) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('popedom_mgr/viewOperator.do')">用户管理</a>
						</td>
					</tr>
					<%
					}
					%>

					<%
						if (xtgl_yhzgl) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('popedom_mgr/viewMUserGrp.do')">用户组管理</a>
						</td>
					</tr>		
					<%
					}
					%>

					
					<%
						if (xtgl_jsgl ) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('popedom_mgr/viewRole.do')">角色管理</a>
						</td>
					</tr>
					<%
					}
					%>


					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('system_mgr/modifyPwd.jsp')">修改密码</a>
						</td>
					</tr>

					<%
						if (operator.isExitsThisUrl("system_mgr/viewResetPwd.do")) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">		
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('system_mgr/viewResetPwd.do')">重置密码</a>
						</td>
					</tr>
					<%
					}
					%>

	
					<%
						if (xtgl_zdgl) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('system_mgr/viewCodeLib.do')">字典管理</a>
						</td>
					</tr>		
					<%
					}
					%>

				
				 <!-- 	
					<%
						if (xtgl_zxyh) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('system_mgr/viewOnlineUser.do')">在线用户</a>
						</td>
					</tr>
					<%
					}
					%>
				 -->
					
					<%
						if (xtgl_hlgl) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click
								('sysManage/exchangeRate/exchangeRateMgr.do?method=viewExchangeRate')">汇率管理</a>
						</td>
					</tr>
					<%
					}
					%>

					<%
						if (xtgl_hlgl) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">			
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click
								('sysManage/specialValMgr.do?method=viewSpecVal')">特殊校验管理</a>
						</td>
					</tr>			
					<%
					}
					%>

					<%
						if (xtgl_rzgl) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click('system_mgr/viewLogIn.do')">日志管理</a>
						</td>
					</tr>					
					<%
					}
					%>
			
				</table>
				<!--系统管理(End)-->
			</td>
		</tr>

		<%
		// }
			if (fxjc) {
		%>
		<tr>
			<td height="30"  background="image/button_blue.jpg">	
					<a href="javascript:toUrl()"> <font class=b size="3"><b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;监管分析</b></font></a>
			</td>
		</tr>

		
		<%
			}
			
			//if (xxgg_xxfb 
			//|| xxgg_xxck) {
		%><%--
		<tr>
			<td height="30"  background="image/button_blue.jpg">
				
				<a href="javascript:menu_pic_click('XXGG')"> 
				<font class=b size="3"><b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;信息公告</b></font> </a>
				
			</td>
		</tr>
		
		<tr id="tr_XXGG" style="display:none">
			<td>
				<!--报表查询(Begin)--->
				<table border="0" cellpadding="0" cellspacing="0" width="85%"
					align="center">
					<tr>
						<td background="image/button_q.gif" height="25">
							<%
								if (xxgg_xxck) {
							%>
							<img src="image/page.gif" border="0">
							<a href="javascript:menu_click('placard_mgr/viewPlacardUserViewAction.do')">公告查看</a>
							<%
								}
							%>
						</td>
					</tr>
					<tr>
						<td background="image/button_q.gif" height="25">
							<%
								if (xxgg_xxfb ) {
							%>
							<img src="image/page.gif" border="0">
							<a href="javascript:menu_click('placard_mgr/viewPlacardAction.do')">信息发布</a>
							<%
								}
							%>
						</td>
					</tr>
					<tr>
						<td height="5"></td>
					</tr>
				</table>
				<!--报表查询(End)-->
			</td>
		</tr>

		<%
			}
		%>
		--%>
		
	</table>

</body>
</html:html>


