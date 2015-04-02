<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.List,com.cbrc.smis.security.Operator"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<%@ page import="com.fitech.net.adapter.StrutsOrgNetDelegate"%>
<%@ page import="com.fitech.net.hibernate.OrgNet"%>
<%@ page import="com.fitech.net.hibernate.OrgType"%>

<html:html locale="true">
<head>

	<%
		Operator operator = null;
		if (session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME) != null)
			operator = (Operator) session
			.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		else
			operator = new Operator();

		/**机构设定的权限**/
		boolean GG_GGJB = operator.isExitsThisUrl("viewOrgLayer.do"); //机构级别设定
		boolean GG_GGLX = operator.isExitsThisUrl("viewOrgType.do"); //机构类型设定
		boolean GG_GGDQ = operator.isExitsThisUrl("viewMRegion.do"); //机构地区设定
		boolean GG_GG = operator.isExitsThisUrl("viewOrgNet.do"); //机构设定

		/**报表管理**/
		boolean bbgl_xzmb = operator
				.isExitsThisUrl("template/add/index.jsp"); //新增1104模板
		boolean bbgl_xzf1104mb = operator
				.isExitsThisUrl("template/add/Not1104Index.jsp"); //新增非1104模板
		boolean bbgl_mbwh = operator
				.isExitsThisUrl("template/viewMChildReport.do"); //模板维护
		boolean bbgl_mbck = operator
				.isExitsThisUrl("template/viewTemplate.do"); //模板查看
		boolean bbgl_sjgxwh = operator
				.isExitsThisUrl("template/viewProTmpt.do"); //数据关系维护
		//	boolean bbgl_scbb = operator.isExitsThisUrl("template/protect/createExcel.jsp");  //生成报表
		boolean bbgl_scbb = operator
				.isExitsThisUrl("template/protect/createExcelNEW.jsp"); //生成报表

		/**取数管理**/
		boolean qsgl_Excelcq = operator
				.isExitsThisUrl("viewTemplateNet.do"); //Excel取数
		boolean qsgl_sjsc = operator
				.isExitsThisUrl("templateDataBuildList.do"); //数据生成
		boolean qsgl_wbcq = operator.isExitsThisUrl("viewformula2.do"); //文本抽取

		/**数据审核**/
		boolean sjsh_shjy = operator
				.isExitsThisUrl("/dateQuary/manualCheckRepSHJY.do"); //批量校验
		boolean sjsh_bbsh = operator
				.isExitsThisUrl("dateQuary/manualCheckRep.do"); //报表审核
		boolean sjsh_bbck = operator
				.isExitsThisUrl("reportSearch/searchRep.do"); //报表查看
		boolean sjsh_cbgl = operator
				.isExitsThisUrl("report_mgr/report_again_mgr/report_again_mgr_frm.jsp"); //重报管理		

		/**数据统计**/
		boolean sjtj_sjhz = operator
				.isExitsThisUrl("collectReport/search_data_stat.jsp"); //数据汇总
		boolean sjtj_hzfssd = operator
				.isExitsThisUrl("collectType/viewCollectType.do"); //汇总方式设定

		/**信息查看**/
		boolean xxcx_bbck = operator
				.isExitsThisUrl("reportSearch/viewReportStatAction.do"); //报表查看		
		boolean xxcx_bstj = operator.isExitsThisUrl("reportStatistics.do"); //报送统计
		boolean xxcx_sjdc = operator.isExitsThisUrl("viewReport.do"); //数据导出

		/**数据报送**/
		boolean sjbc_sjwjxz = operator
				.isExitsThisUrl("template/templateDownloadList.do"); //数据文件下载
		boolean sjbc_sjsb = operator
				.isExitsThisUrl("template/data_report/data_upreport.jsp"); //数据上报
		boolean sjbc_sjtz = operator.isExitsThisUrl("viewonlineupdate.do"); //数据调整
		boolean sjbc_sbsj = operator.isExitsThisUrl("viewOnLineSJBS.do"); //上报汇总数据
		boolean sjbc_qthzsj = operator
				.isExitsThisUrl("collectType/viewOtherCollect.do"); //其它汇总数据
		boolean sjbc_scsbwj = operator.isExitsThisUrl("downLoadReport"); //数据文件备份

		/**指标分析**/
		boolean zbfx_zbdy = operator
				.isExitsThisUrl("target/viewTargetDefine.do"); //指标定义
		boolean zbfx_zbfxyj = operator
				.isExitsThisUrl("target/viewTargetGenerate.do"); //指标分析预警
		boolean zbfx_zbywsd = operator
				.isExitsThisUrl("target/viewTargetNormal.do"); //指标业务设定
		boolean zbfx_zblxsd = operator
				.isExitsThisUrl("target/viewTargetBusiness.do"); //指标类型设定

		/**参数设定**/
		boolean cssd_hbdwsd = operator
				.isExitsThisUrl("config/ViewCurUnit.do"); //货币单位设定
		boolean cssd_sjkjsd = operator
				.isExitsThisUrl("config/ViewDataRangeType.do"); //数据口径设定
		boolean cssd_sbpd = operator
				.isExitsThisUrl("config/ViewCurRepFreqence.do"); //上报频度设定
		boolean cssd_jylb = operator
				.isExitsThisUrl("config/ViewCurVerifyType.do"); //校验类别设定
		boolean cssd_bzsd = operator.isExitsThisUrl("config/ViewCurr.do"); //币种设定
		boolean cssd_hlsd = operator.isExitsThisUrl("exchangerate/Find.do"); //汇率设定
		boolean cssd_csbsd = operator
				.isExitsThisUrl("config/ViewVParam.do"); //参数表设定
		boolean cssd_zbgssd = operator
				.isExitsThisUrl("template/ViewTargetFormual.do"); //取数指标公式设定
		boolean cssd_Syssd = operator
				.isExitsThisUrl("config/ViewSysPar.do"); //系统参数设定

		/**权限管理**/
		boolean qxgl_bmgl = operator
				.isExitsThisUrl("popedom_mgr/viewDepartment.do"); //部门管理
		boolean qzgl_yhgl = operator
				.isExitsThisUrl("popedom_mgr/viewOperator.do"); //用户管理
		boolean qzgl_jsgl = operator
				.isExitsThisUrl("popedom_mgr/viewRole.do"); //角色管理
		boolean qzgl_yhzgl = operator
				.isExitsThisUrl("popedom_mgr/viewMUserGrp.do"); //用户组管理
		boolean qzgl_gncd = operator
				.isExitsThisUrl("popedom_mgr/viewToolSetting.do"); //功能菜单管理
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
			
			var GG_GGJBJ=<%=GG_GGJB%>;
			var GG_GGLXJ=<%=GG_GGLX%>;
			var GG_GGDQJ=<%=GG_GGDQ%>;
			var GG_GGJ=<%=GG_GG%>;
			
			var scriptBbgl_xzmb=<%=bbgl_xzmb%>;
			var scriptBbgl_xzf1104mb=<%=bbgl_xzf1104mb%>;
			var scriptBbgl_mbwh=<%=bbgl_mbwh%>;
			var scriptBbgl_mbck=<%=bbgl_mbck%>;
			var scriptBbgl_sjgxwh=<%=bbgl_sjgxwh%>;
			var scriptBbgl_scbb=<%=bbgl_scbb%>;
			
			var scriptQsgl_excelcq=<%=qsgl_Excelcq%>;
			var scriptQsgl_sjsc=<%=qsgl_sjsc%>;
			var scriptQsgl_wbcq=<%=qsgl_wbcq%>;
			
			var scriptSjsh_shjy=<%=sjsh_shjy%>;
			var scriptSjsh_bbsh=<%=sjsh_bbsh%>;
			var scriptSjsh_bbck=<%=sjsh_bbck%>;
			var scriptSjsh_cbgl=<%=sjsh_cbgl%>;			
			
			var scriptSjtj_sjhz=<%=sjtj_sjhz%>;
			var scriptSjtj_hzfssd=<%=sjtj_hzfssd%>;
			
			var scriptXxcx_bstj=<%=xxcx_bstj%>;
			var scriptXxcx_sjdc=<%=xxcx_sjdc%>;
			var scriptXxcx_bbck=<%=xxcx_bbck%>;
						
			var scriptSjbc_sjwjz=<%=sjbc_sjwjxz%>;
			var scriptSjbc_sjsb=<%=sjbc_sjsb%>;
			var scriptSjbc_sjtz=<%=sjbc_sjtz%>;
			var scriptSjbc_sbsj=<%=sjbc_sbsj%>;
			var scriptSjbc_scsbwj=<%=sjbc_scsbwj%>;
			var scriptSjbc_qthzsj=<%=sjbc_qthzsj%>;
			
			var scriptZbfx_zbdy=<%=zbfx_zbdy%>;
			var scriptZbfx_zbfxyj=<%=zbfx_zbfxyj%>;
			var scriptZbfx_zbywsd=<%=zbfx_zbywsd%>;
			var scriptZbfx_zblxsd=<%=zbfx_zblxsd%>;
				
			var scriptCssd_hbdwsd=<%=cssd_hbdwsd%>;
			var scriptCssd_sjkjsd=<%=cssd_sjkjsd%>;
			var scriptCssd_sbpd=<%=cssd_sbpd%>;
			var scriptCssd_jylb=<%=cssd_jylb%>;
			var scriptCssd_bzsd=<%=cssd_bzsd%>;
			var scriptCssd_hlsd=<%=cssd_hlsd%>;
			var scriptCssd_csbsd=<%=cssd_csbsd%>;	
			var scriptCssd_zbgssd=<%=cssd_zbgssd%>;
			var scriptcssd_Syssd= <%=cssd_Syssd%>;
			
			var scriptQxgl_bmgl=<%=qxgl_bmgl%>;
			var scriptQxgl_yhgl=<%=qzgl_yhgl%>;
			var scriptQxgl_jsgl=<%=qzgl_jsgl%>;
			var scriptQxgl_yhzgl=<%=qzgl_yhzgl%>;
			var scriptQxgl_gncd=<%=qzgl_gncd%>;
			
			/**
			 * 报表管理 
			 */
			var BBGL="BBGL";
			/**
			 * 数据审核
			 */
			var SJSH="SJSH";
			/**
			 * 数据统计
			 */
			var SJTJ="SJTJ";
			/**
			 * 数据补录
			 */
			var SJBL="SJBL";
			/**
			 * 信息查询
			 */
			var XXCX="XXCX";
			/**
			 * 指标分析
			 */
			var ZBFX="ZBFX";
			/**
			 * 取数管理
			 */
			var QSGL="QSGL";
			/**
			 * 参数设定
			 */
			var CSSD="CSSD";
			/**
			 * 机构管理
			 */
			var JGGL="JGGL";
			/**
			 * 系统管理
			 */
			var XTGL="XTGL";
			/**
			 * 信息发布
			 */
			var XXJH="XXJH"; 
			/**
			 * 权限管理
			 */
			var QXGL="QXGL";
			/**
			 * 数据报送
			 */
			var SJBS="SJBS";
			/**
			 * 是否有子菜单展开
			 */ 
			 var is_menu_show=false;
			 
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
			 * 初始化菜单图片显示
			 */
			function menu_pic_init(){
				if(scriptBbgl_xzmb == true || scriptBbgl_xzf1104mb == true || scriptBbgl_mbwh == true 
					|| scriptBbgl_mbck == true || scriptBbgl_sjgxwh == true || scriptBbgl_scbb == true)
			 		eval("document.getElementById('tr_" + BBGL + "').style.display='none'");
			 		
				if(scriptSjsh_shjy == true || scriptSjsh_bbsh == true || scriptSjsh_bbck == true || scriptSjsh_cbgl == true)
			 		eval("document.getElementById('tr_" + SJSH + "').style.display='none'");
			 		
				if(scriptSjtj_sjhz == true || scriptSjtj_hzfssd == true)
			 		eval("document.getElementById('tr_" + SJTJ + "').style.display='none'");
			 		
				if(scriptQsgl_excelcq == true || scriptQsgl_sjsc == true || scriptQsgl_wbcq == true)
			 		eval("document.getElementById('tr_" + QSGL + "').style.display='none'");
			 		
			 	if(scriptZbfx_zbdy == true || scriptZbfx_zbfxyj == true || scriptZbfx_zbywsd == true || scriptZbfx_zblxsd == true)
			 		eval("document.getElementById('tr_" + ZBFX + "').style.display='none'");
			 		
			 	if(scriptCssd_hbdwsd == true || scriptCssd_sjkjsd == true || scriptCssd_sbpd == true || scriptCssd_jylb == true
			 		|| scriptCssd_bzsd == true || scriptCssd_hlsd == true || scriptCssd_csbsd == true || scriptCssd_zbgssd == true
			 		|| scriptcssd_Syssd == true)
			 		eval("document.getElementById('tr_" + CSSD + "').style.display='none'");
			 		
		

			 	if(GG_GGJBJ == true || GG_GGLXJ == true || GG_GGDQJ == true || GG_GGJ == true)
			 		eval("document.getElementById('tr_" + JGGL + "').style.display='none'");
			 		
			 	eval("document.getElementById('tr_" + XTGL + "').style.display='none'");
			 	
			 	if(scriptQxgl_bmgl == true || scriptQxgl_yhgl == true || scriptQxgl_jsgl == true || scriptQxgl_yhzgl == true || scriptQxgl_gncd == true)
			 		eval("document.getElementById('tr_" + QXGL + "').style.display='none'");
			 		
			 	if(scriptSjbc_sjwjz == true || scriptSjbc_sjsb == true || scriptSjbc_sjtz == true 
			 		|| scriptSjbc_sbsj == true || scriptSjbc_scsbwj == true || scriptSjbc_qthzsj == true)
			 		eval("document.getElementById('tr_" + SJBS + "').style.display='none'");
			 		
				if(scriptXxcx_bstj == true || scriptXxcx_sjdc == true || scriptXxcx_bbck == true)
					eval("document.getElementById('tr_" + XXCX + "').style.display='none'");
			}
			
			/**
			 * 隐藏子菜单
			 */
			function menu_hide(menu){
			 	if(menu!=BBGL && (scriptBbgl_xzmb == true || scriptBbgl_xzf1104mb == true || scriptBbgl_mbwh == true 
					|| scriptBbgl_mbck == true || scriptBbgl_sjgxwh == true || scriptBbgl_scbb == true))
			 		eval("document.getElementById('tr_" + BBGL + "').style.display='none'");
			 		
			 	if(menu!=SJSH && (scriptSjsh_shjy == true || scriptSjsh_bbsh == true || scriptSjsh_bbck == true || scriptSjsh_cbgl == true || scriptSjsh_ckzhbb == true))
			 		eval("document.getElementById('tr_" + SJSH + "').style.display='none'");
			 		
			 	if(menu!=SJTJ && (scriptSjtj_sjhz== true || scriptSjtj_hzfssd == true))
			 		eval("document.getElementById('tr_" + SJTJ + "').style.display='none'");
			 		
			 	if(menu!=QSGL && (scriptQsgl_excelcq == true || scriptQsgl_sjsc == true || scriptQsgl_wbcq == true))
			 		eval("document.getElementById('tr_" + QSGL + "').style.display='none'");
			 		
			 	if(menu!=ZBFX && (scriptZbfx_zbdy == true || scriptZbfx_zbfxyj == true || scriptZbfx_zbywsd == true || scriptZbfx_zblxsd == true))
			 		eval("document.getElementById('tr_" + ZBFX + "').style.display='none'");
			 					
			 	if(menu!=CSSD && (scriptCssd_hbdwsd == true || scriptCssd_sjkjsd == true || scriptCssd_sbpd == true || scriptCssd_jylb == true
			 		|| scriptCssd_bzsd == true || scriptCssd_hlsd == true || scriptCssd_csbsd == true || scriptCssd_zbgssd == true
			 		|| scriptcssd_Syssd == true ))
			 		eval("document.getElementById('tr_" + CSSD + "').style.display='none'");
			
			 		
			 	if(menu!=JGGL && (GG_GGJBJ == true || GG_GGLXJ == true || GG_GGDQJ == true || GG_GGJ == true))
			 		eval("document.getElementById('tr_" + JGGL + "').style.display='none'");
			 			
			 	if(menu!=XTGL)
			 		eval("document.getElementById('tr_" + XTGL + "').style.display='none'");
			 		
			 	if(menu!=QXGL && (scriptQxgl_bmgl == true || scriptQxgl_yhgl == true || scriptQxgl_jsgl == true || scriptQxgl_yhzgl == true 
			 		|| scriptQxgl_gncd == true))
			 		eval("document.getElementById('tr_" + QXGL + "').style.display='none'");
			 		
			 	if(menu!=SJBS && (scriptSjbc_sjwjz == true || scriptSjbc_sjsb == true || scriptSjbc_sjtz == true 
			 		|| scriptSjbc_sbsj == true || scriptSjbc_scsbwj == true || scriptSjbc_qthzsj == true))
			 		eval("document.getElementById('tr_" + SJBS + "').style.display='none'");
			
				if(menu!=XXCX && (scriptXxcx_bstj == true || scriptXxcx_sjdc == true || scriptXxcx_bbck == true))
			 		eval("document.getElementById('tr_" + XXCX + "').style.display='none'");
			}
			
		    /**
		     * 点击菜单事件
		     */
		    function menu_click(_url){
		    	if(_url=="") return;
		    	window.parent.mainFrame.contents.location.assign(_url);
		    } 
		    
		    /**
			 * 注销事件
			 */
			 function _exit(){
			 	if(confirm("您确定要注销用户吗?\n")==true){
			 		window.location="logout.jsp";
			 	}
			 }
		</script>
</head>
<body background="image/navibk.jpg">

	<table border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td>
				<img src="image/navitop.jpg">
			</td>
		</tr>
		<%
					if (operator.isExitsThisUrl("template/add/index.jsp")
					|| operator.isExitsThisUrl("template/add/Not1104Index.jsp")
					|| operator.isExitsThisUrl("template/viewMChildReport.do")
					|| operator.isExitsThisUrl("template/viewTemplate.do")
					|| operator.isExitsThisUrl("template/viewProTmpt.do")
					|| operator
					.isExitsThisUrl("template/protect/createExcelNEW.jsp")) {
		%>
		<tr>
			<td height="35">
				<a href="javascript:menu_pic_click(BBGL)"> <img
						src="image/bbgl_n.jpg" border="0"> </a>
			</td>
		</tr>

		<tr id="tr_BBGL">
			<td>
				<!--报表管理(Begin)--->
				<table border="0" cellpadding="0" cellspacing="0" width="85%"
					align="center">
					<tr>
						<td>
							<%
							if (operator.isExitsThisUrl("template/add/index.jsp")) {
							%>
							<img src="image/page.gif" border="0">
							<a href="javascript:menu_click('template/add/index.jsp')">新增PDF模板</a>
							<%
							}
							%>
						</td>
					</tr>

					<tr>
						<td>
							<%
							if (operator.isExitsThisUrl("template/add/Not1104Index.jsp")) {
							%>
							<img src="image/page.gif" border="0">
							<a href="javascript:menu_click('template/add/Not1104Index.jsp')">新增EXCEL模板</a>
							<%
							}
							%>
						</td>
					</tr>
					<tr>
						<td>
							<%
							if (operator.isExitsThisUrl("template/viewMChildReport.do")) {
							%>
							<img src="image/page.gif" border="0">
							<a href="javascript:menu_click('template/viewMChildReport.do')">模板维护</a>
							<%
							}
							%>
						</td>
					</tr>
					<tr>
						<td>
							<%
							if (operator.isExitsThisUrl("template/viewTemplate.do")) {
							%>
							<img src="image/page.gif" border="0">
							<a href="javascript:menu_click('template/viewTemplate.do')">模板查看</a>
							<%
							}
							%>
						</td>
					</tr>
					<%
					if (Config.BANK_NAME.equals("CZBANK")) {
					%>
					<tr>
						<td>
							<%
							if (operator.isExitsThisUrl("template/viewProTmpt.do")) {
							%>
							<img src="image/page.gif" border="0">
							<a href="javascript:menu_click('template/viewProTmpt.do')">数据关系维护</a>
							<%
							}
							%>
						</td>
					</tr>

					<tr>

						<td>
							<%
										if (operator
										.isExitsThisUrl("template/protect/createExcelNEW.jsp")) {
							%>
							<img src="image/page.gif" border="0">
							<a
								href="javascript:menu_click('template/protect/createExcelNEW.jsp')">生成报表</a>
							<%
							}
							%>
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
		<tr id="l3">
			<td height="1" valign="bottom" background="image/navibk.jpg">
				<img src="image/navisp.jpg">
			</td>
		</tr>
		<%
			}

			if (operator.isExitsThisUrl("viewTemplateNet.do")
					|| operator.isExitsThisUrl("templateDataBuildList.do")
					|| operator.isExitsThisUrl("viewformula2.do")) {
		%>
		<tr>
			<td height="35">
				<a href="javascript:menu_pic_click(QSGL)"> <img
						src="image/qsgl_n.jpg" border="0">
				</a>
			</td>
		</tr>
		<tr id="tr_QSGL">
			<td>
				<!--取数管理(Begin)-->
				<table border="0" cellpadding="0" cellspacing="0" width="85%"
					align="center">
					<!-- <tr>
								<td>
									<%
										if(operator.isExitsThisUrl("viewTemplateNet.do"))
										{			
									%>
										<img src="image/page.gif" border="0"><a href="javascript:menu_click('viewTemplateNet.do')">Excel抽取</a>
									<%
										}
									%>
								</td>
							</tr>
							<tr>
								<td>
									<%
										if(operator.isExitsThisUrl("templateDataBuildList.do"))
										{			
									%>
										<img src="image/page.gif" border="0"><a href="javascript:menu_click('templateDataBuildList.do')">数据生成</a>
									<%
										}
									%>
								</td>
							</tr>
							<tr>
								<td>
									<%
										if(operator.isExitsThisUrl("viewformula2.do"))
										{			
									%>
										<img src="image/page.gif" border="0"><a href="javascript:menu_click('obtain/text/viewformula2.do')">文本抽取</a>
									<%
										}
									%>
								</td>
							</tr>-->
				</table>
				<!--取数管理(End)-->
			</td>
		</tr>
		<tr id="l3">
			<td height="1" valign="bottom" background="image/navibk.jpg">
				<img src="image/navisp.jpg">
			</td>
		</tr>
		<%
			}

			if (operator.isExitsThisUrl("dateQuary/manualCheckRep.do")
					|| operator.isExitsThisUrl("reportSearch/searchRep.do")
					|| operator
					.isExitsThisUrl("report_mgr/report_again_mgr/report_again_mgr_frm.jsp")) {
		%>
		<tr>
			<td height="35">
				<a href="javascript:menu_pic_click(SJSH)"> <img
						src="image/sjsh_n.jpg" border="0">
				</a>
			</td>
		</tr>
		<tr id="tr_SJSH">
			<td>
				<!--数据审核(Begin)-->
				<table border="0" cellpadding="0" cellspacing="0" width="85%"
					align="center">
					<tr>
						<td>
							<%
							if (operator.isExitsThisUrl("dateQuary/manualCheckRepSHJY.do")) {
							%>
							<img src="image/page.gif" border="0">
							<a
								href="javascript:menu_click('dateQuary/manualCheckRepSHJY.do')">批量校验</a>
							<%
							}
							%>
						</td>
					</tr>
					<tr>
						<td>
							<%
							if (operator.isExitsThisUrl("dateQuary/manualCheckRep.do")) {
							%>
							<img src="image/page.gif" border="0">
							<a href="javascript:menu_click('dateQuary/manualCheckRep.do')">报表审核</a>
							<%
							}
							%>
						</td>
					</tr>
					<tr>
						<td>
							<%
									if (operator
									.isExitsThisUrl("report_mgr/report_again_mgr/report_again_mgr_frm.jsp")) {
							%>
							<img src="image/page.gif" border="0">
							<a href="javascript:menu_click('selForseReportAgain.do')">重报管理</a>
							<%
							}
							%>
						</td>
					</tr>

					<tr>
						<td height="5"></td>
					</tr>
				</table>
				<!--数据审核(End)-->
			</td>
		</tr>

		<tr id="l3">
			<td height="1" valign="bottom" background="image/navibk.jpg">
				<img src="image/navisp.jpg">
			</td>
		</tr>

		<%
			}
			if (operator.isExitsThisUrl("collectReport/search_data_stat.jsp")
			//	|| operator.isExitsThisUrl("collectType/viewCollectType.do")
			//	|| operator.isExitsThisUrl("analysis/selectACompareLog.do")
			) {
		%>
		<tr>
			<td height="35">
				<a href="javascript:menu_pic_click(SJTJ)"> <img
						src="image/sjtj_n.jpg" border="0">
				</a>
			</td>
		</tr>
		<tr id="tr_SJTJ">
			<td>
				<!--数据统计(Begin)-->
				<table border="0" cellpadding="0" cellspacing="0" width="85%"
					align="center">
					<tr>
						<td>
							<%
								if (operator.isExitsThisUrl("collectReport/search_data_stat.jsp")) {
							%>
							<img src="image/page.gif" border="0"><a href="javascript:menu_click('viewCollectData.do')">数据汇总</a>
								<!--<img src="image/page.gif" border="0"><a href="javascript:menu_click('collectReport/search_data_stat.jsp')">数据汇总</a>-->
							<%
							}
							%>
						</td>
					</tr>
					<tr>
						<td>
							<%
								if (operator.isExitsThisUrl("collectReport/jobLogMgr.do?method=toFindJob")) {
							%>
							<img src="image/page.gif" border="0">
							<a href="javascript:menu_click('collectReport/jobLogMgr.do?method=toFindJob')">ETL监控</a>
							<%
							}
							%>
						</td>
					</tr>
					<!-- 
							  <tr>
								<td>
								<%//								if(operator.isExitsThisUrl("analysis/selectACompareLog.do")){			
									%>
									<img src="image/page.gif" border="0"><a href="javascript:menu_click('analysis/selectACompareLog.do')">数据比对</a>
								<%
									//	}
									%>
								</td>
							</tr>	
							 -->
				</table>
				<!--数据统计(End)-->
			</td>
		</tr>

		<tr id="l3">
			<td height="1" valign="bottom" background="image/navibk.jpg">
				<img src="image/navisp.jpg">
			</td>
		</tr>
		<%
			}
			if (operator.isExitsThisUrl("template/templateDownloadList.do")
					|| operator
					.isExitsThisUrl("template/data_report/data_upreport.jsp")
					|| operator.isExitsThisUrl("viewonlineupdate.do")
					|| operator.isExitsThisUrl("viewOnLineSJBS.do")
					|| operator.isExitsThisUrl("downLoadReport")
					|| operator
					.isExitsThisUrl("collectType/viewOtherCollect.do")) {
		%>

		<tr>
			<td height="35">
				<a href="javascript:menu_pic_click(SJBS)"> <img
						src="image/sjbs_n.jpg" border="0">
				</a>
			</td>
		</tr>
		<tr id="tr_SJBS">
			<td>
				<!--数据报送(Begin)-->
				<table border="0" cellpadding="0" cellspacing="0" width="85%"
					align="center">
					<tr>
						<td>
							<%
							if (operator.isExitsThisUrl("template/templateDownloadList.do")) {
							%>
							<img src="image/page.gif" border="0">
							<a
								href="javascript:menu_click('template/templateDownloadList.do')">数据文件下载</a>
							<%
							}
							%>
						</td>
					</tr>
					<tr>
						<td>
							<%
									if (operator
									.isExitsThisUrl("template/data_report/data_upreport.jsp")) {
							%>
							<img src="image/page.gif" border="0">
							<a href="javascript:menu_click('viewDataReport.do')">数据上报</a>
							<%
							}
							%>
						</td>
					</tr>

				</table>
				<!--数据报送(End)-->
			</td>
		</tr>

		<tr id="l3">
			<td height="1" valign="bottom" background="image/navibk.jpg">
				<img src="image/navisp.jpg">
			</td>
		</tr>

		<%
			}

			if (operator.isExitsThisUrl("viewReport.do")
					|| operator.isExitsThisUrl("reportStatistics.do")
					|| operator
					.isExitsThisUrl("reportSearch/viewReportStatAction.do")) {
		%>
		<tr>
			<td height="35">
				<a href="javascript:menu_pic_click(XXCX)"> <img
						src="image/xxcx_n.jpg" border="0">
				</a>
			</td>
		</tr>
		<tr id="tr_XXCX">
			<td>
				<!--信息查询(Begin)-->
				<table border="0" cellpadding="0" cellspacing="0" width="85%"
					align="center">
					<tr>
						<td>
							<%
									if (operator
									.isExitsThisUrl("reportSearch/viewReportStatAction.do")) {
							%>
							<img src="image/page.gif" border="0">
							<a
								href="javascript:menu_click('reportSearch/viewReportStatAction.do')">报表查看</a>
							<%
							}
							%>
						</td>
					</tr>
					<tr>
						<td>
							<%
							if (operator.isExitsThisUrl("viewReport.do")) {
							%>
							<img src="image/page.gif" border="0">
							<a href="javascript:menu_click('viewReport.do')">数据导出</a>
							<%
							}
							%>
						</td>
					</tr>
				</table>
				<!--信息查询(End)-->
			</td>
		</tr>
		<tr id="l3">
			<td height="1" valign="bottom" background="image/navibk.jpg">
				<img src="image/navisp.jpg">
			</td>
		</tr>
		<%
			}

			if (operator.isExitsThisUrl("target/viewTargetDefine.do")
					|| operator.isExitsThisUrl("target/viewTargetGenerate.do")
					|| operator.isExitsThisUrl("target/viewTargetNormal.do")
					|| operator.isExitsThisUrl("target/viewTargetBusiness.do")) {
		%>
		<tr>
			<td height="35">
				<a href="javascript:menu_pic_click(ZBFX)"> <%--							<img src="image/tjfx.jpg" border="0"></a>--%>
					<img src="image/zbfx.jpg" border="0">
				</a>
			</td>
		</tr>
		<tr id="tr_ZBFX">
			<td>
				<!--指标分析(Begin)-->
				<table border="0" cellpadding="0" cellspacing="0" width="85%"
					align="center">
					<tr>
						<td>
							<%
							if (operator.isExitsThisUrl("target/viewTargetDefine.do")) {
							%>
							<img src="image/page.gif" border="0">
							<a href="javascript:menu_click('target/viewTargetDefine.do')">指标定义</a>
							<%
							}
							%>
						</td>
					</tr>
					<tr>
						<td>
							<%
							if (operator.isExitsThisUrl("target/viewTargetGenerate.do")) {
							%>
							<img src="image/page.gif" border="0">
							<a href="javascript:menu_click('target/viewTargetGenerate.do')">指标分析预警</a>
							<%
							}
							%>
						</td>
					</tr>
					<tr>
						<td>
							<%
							if (operator.isExitsThisUrl("target/viewTargetNormal.do")) {
							%>
							<img src="image/page.gif" border="0">
							<a href="javascript:menu_click('target/viewTargetNormal.do')">指标业务设定</a>
							<%
							}
							%>
						</td>
					</tr>

					<tr>
						<td>
							<%
							if (operator.isExitsThisUrl("target/viewTargetBusiness.do")) {
							%>
							<img src="image/page.gif" border="0">
							<a href="javascript:menu_click('target/viewTargetBusiness.do')">指标类型设定</a>
							<%
							}
							%>
						</td>
					</tr>


				</table>
				<!--指标分析(End)-->
			</td>
		</tr>
		<tr id="l3">
			<td height="1" valign="bottom" background="image/navibk.jpg">
				<img src="image/navisp.jpg">
			</td>
		</tr>
		<%
			}
			if (operator.isExitsThisUrl("config/ViewCurUnit.do")
					|| operator.isExitsThisUrl("config/ViewDataRangeType.do")
					|| operator.isExitsThisUrl("config/ViewCurRepFreqence.do")
					|| operator.isExitsThisUrl("config/ViewCurVerifyType.do")
					|| operator.isExitsThisUrl("config/ViewCurr.do")
					|| operator.isExitsThisUrl("exchangerate/Find.do")
					|| operator.isExitsThisUrl("config/ViewVParam.do")
					|| operator.isExitsThisUrl("template/ViewTargetFormual.do")
					|| operator.isExitsThisUrl("config/ViewSysPar.do")) {
		%>
		<tr>
			<td height="35">
				<a href="javascript:menu_pic_click(CSSD)"> <img
						src="image/navin3.jpg" border="0"> </a>
			</td>
		</tr>
		<tr id="tr_CSSD">
			<td>
				<!--参数设定(Begin)--->
				<table border="0" cellpadding="0" cellspacing="0" width="85%"
					align="center">
					<tr>
						<td>
							<%
							if (operator.isExitsThisUrl("config/ViewCurUnit.do")) {
							%>
							<img src="image/page.gif" border="0">
							<a href="javascript:menu_click('config/ViewCurUnit.do')">货币单位设定</a>
							<%
							}
							%>
						</td>
					</tr>
					<tr>
						<td>
							<%
							if (operator.isExitsThisUrl("config/ViewDataRangeType.do")) {
							%>
							<img src="image/page.gif" border="0">
							<a href="javascript:menu_click('config/ViewDataRangeType.do')">数据口径类别设定</a>
							<%
							}
							%>
						</td>
					</tr>
					<tr>
						<td>
							<%
							if (operator.isExitsThisUrl("config/ViewCurRepFreqence.do")) {
							%>
							<img src="image/page.gif" border="0">
							<a href="javascript:menu_click('config/ViewCurRepFreqence.do')">上报频度设定</a>
							<%
							}
							%>
						</td>
					</tr>
					<tr>
						<td>
							<%
							if (operator.isExitsThisUrl("config/ViewCurVerifyType.do")) {
							%>
							<img src="image/page.gif" border="0">
							<a href="javascript:menu_click('config/ViewCurVerifyType.do')">校验类别设定</a>
							<%
							}
							%>
						</td>
					</tr>
					<tr>
						<td>
							<%
							if (operator.isExitsThisUrl("config/ViewCurr.do")) {
							%>
							<img src="image/page.gif" border="0">
							<a href="javascript:menu_click('config/ViewCurr.do')">币种设定</a>
							<%
							}
							%>
						</td>
					</tr>
					<tr>
						<td>
							<%
							if (operator.isExitsThisUrl("config/ViewSysPar.do")) {
							%>
							<img src="image/page.gif" border="0">
							<a href="javascript:menu_click('config/ViewSysPar.do')">系统参数设定</a>
							<%
							}
							%>
						</td>
					</tr>
					<tr>
						<td height="5"></td>
					</tr>
				</table>
				<!--参数设定(End)-->
			</td>
		</tr>

		<tr id="l3">
			<td height="1" valign="bottom" background="image/navibk.jpg">
				<img src="image/navisp.jpg">
			</td>
		</tr>
		<%
			}
			if (operator.isExitsThisUrl("viewOrgLayer.do")
					|| operator.isExitsThisUrl("viewOrgType.do")
					|| operator.isExitsThisUrl("viewMRegion.do")
					|| operator.isExitsThisUrl("viewOrgNet.do")) {
		%>

		<tr>
			<td height="35">
				<a href="javascript:menu_pic_click(JGGL)"> <img
						src="image/org.jpg" border="0">
				</a>
			</td>
		</tr>
		<tr id="tr_JGGL">
			<td>
				<!--机构管理(Begin)--->
				<table border="0" cellpadding="0" cellspacing="0" width="85%"
					align="center">
					<!--<tr>
								<td>
									<%
										if(operator.isExitsThisUrl("viewOrgLayer.do"))
										{			
									%>
										<img src="image/page.gif" border="0"><a href="javascript:menu_click('viewOrgLayer.do')">机构级别设定</a>
									<%
										}
									%>
								</td>
							</tr>-->
					<tr>
						<td>
							<%
							if (operator.isExitsThisUrl("viewOrgType.do")) {
							%>
							<img src="image/page.gif" border="0">
							<a href="javascript:menu_click('viewOrgType.do')">机构类型设定</a>
							<%
							}
							%>
						</td>
					</tr>
<%--					<tr>--%>
<%--						<td>--%>
<%--							<%--%>
<%--							if (operator.isExitsThisUrl("viewMRegion.do")) {--%>
<%--							%>--%>
<%--							<img src="image/page.gif" border="0">--%>
<%--							<a href="javascript:menu_click('viewMRegion.do')">机构地区设定</a>--%>
<%--							<%--%>
<%--							}--%>
<%--							%>--%>
<%--						</td>--%>
<%--					</tr>--%>
					<tr>
						<td>
							<%
							if (operator.isExitsThisUrl("viewOrgNet.do")) {
							%>
							<img src="image/page.gif" border="0">
							<a href="javascript:menu_click('org/selectOrgNet.do')">机构设定</a>
							<%
							}
							%>
						</td>
					</tr>
					<tr>
						<td height="5"></td>
					</tr>
				</table>
				<!--机构管理(End)-->
			</td>
		</tr>
		<tr id="l3">
			<td height="1" valign="bottom" background="image/navibk.jpg">
				<img src="image/navisp.jpg">
			</td>
		</tr>
		<%
		}
		%>
		<tr>
			<td height="35">
				<a href="javascript:menu_pic_click(XTGL)"> <img
						src="image/navin4.jpg" border="0">
				</a>
			</td>
		</tr>
		<tr id="tr_XTGL">
			<td>
				<!--系统管理(Begin)--->
				<table border="0" cellpadding="0" cellspacing="0" width="85%"
					align="center">
					<tr>
						<td>
							<img src="image/page.gif" border="0">
							<a href="javascript:menu_click('system_mgr/modifyPwd.jsp')">修改密码</a>
						</td>
					</tr>
					<tr>
						<td>
							<%
							if (operator.isExitsThisUrl("system_mgr/viewResetPwd.do")) {
							%>
							<img src="image/page.gif" border="0">
							<a href="javascript:menu_click('system_mgr/viewResetPwd.do')">重置密码</a>
							<%
							}
							%>
						</td>
					</tr>
					<tr>
						<td>
							<%
							if (operator.isExitsThisUrl("system_mgr/viewLogIn.do")) {
							%>
							<img src="image/page.gif" border="0">
							<a href="javascript:menu_click('system_mgr/viewLogIn.do')">日志管理</a>
							<%
							}
							%>
						</td>
					</tr>
					<tr>
						<td>
							<%
							if (operator.isExitsThisUrl("bulletin/BulletinList2.jsp")) {
							%>
							<img src="image/page.gif" border="0">
							<a href="javascript:menu_click('bulletin/BulletinList2.jsp')">公告查看</a>
							<%
							}
							%>
						</td>
					</tr>
					<tr>
						<td>
							<%
							if (operator.isExitsThisUrl("system_mgr/viewRemindTips.jsp")) {
							%>
							<img src="image/page.gif" border="0">
							<a href="javascript:menu_click('system_mgr/viewRemindTips.jsp')">贴士提醒</a>
							<%
							}
							%>
						</td>
					</tr>
					<tr>
						<td>
							<%
							if (operator.isExitsThisUrl("bulletin/BulletinList.jsp")) {
							%>
							<img src="image/page.gif" border="0">
							<a href="javascript:menu_click('bulletin/BulletinList.jsp')">公告管理</a>
							<%
							}
							%>
						</td>
					</tr>
					<tr>
						<td>
							<%
							if (operator.isExitsThisUrl("bulletin/bulletinNormal.do")) {
							%>
							<img src="image/page.gif" border="0">
							<a
								href="javascript:menu_click('bulletin/bulletinNormal.do?method=view')">公告查看</a>
							<%
							}
							%>
						</td>
					</tr>
					<tr>
						<td height="5"></td>
					</tr>
				</table>
				<!--系统管理(End)-->
			</td>
		</tr>
		<tr id="l3">
			<td height="1" valign="bottom" background="image/navibk.jpg">
				<img src="image/navisp.jpg">
			</td>
		</tr>

		<%
					if (operator.isExitsThisUrl("popedom_mgr/viewDepartment.do")
					|| operator.isExitsThisUrl("popedom_mgr/viewOperator.do")
					|| operator.isExitsThisUrl("popedom_mgr/viewRole.do")
					|| operator.isExitsThisUrl("popedom_mgr/viewMUserGrp.do")
					|| operator
					.isExitsThisUrl("popedom_mgr/viewToolSetting.do")) {
		%>
		<tr>
			<td height="35">
				<a href="javascript:menu_pic_click(QXGL)"> <img
						src="image/qxgl.jpg" border="0">
				</a>
			</td>
		</tr>
		<tr id="tr_QXGL">
			<td>
				<!-- 权限管理(Begin)-->
				<table border="0" cellpadding="0" cellspacing="0" width="85%"
					align="center">
					<tr>
						<td>
							<%
							if (operator.isExitsThisUrl("popedom_mgr/viewDepartment.do")) {
							%>
							<img src="image/page.gif" border="0">
							<a href="javascript:menu_click('popedom_mgr/viewDepartment.do')">部门管理</a>
							<%
							}
							%>
						</td>
					</tr>

					<tr>
						<td>
							<%
							if (operator.isExitsThisUrl("popedom_mgr/viewOperator.do")) {
							%>
							<img src="image/page.gif" border="0">
							<a href="javascript:menu_click('popedom_mgr/viewOperator.do')">用户管理</a>
							<%
							}
							%>
						</td>
					</tr>
					<tr>
						<td>
							<%
							if (operator.isExitsThisUrl("popedom_mgr/viewRole.do")) {
							%>
							<img src="image/page.gif" border="0">
							<a href="javascript:menu_click('popedom_mgr/viewRole.do')">角色管理</a>
							<%
							}
							%>
						</td>
					</tr>
					<tr>
						<td>
							<%
							if (operator.isExitsThisUrl("popedom_mgr/viewMUserGrp.do")) {
							%>
							<img src="image/page.gif" border="0">
							<a href="javascript:menu_click('popedom_mgr/viewMUserGrp.do')">用户组管理</a>
							<%
							}
							%>
						</td>
					</tr>
<%--					<tr>--%>
<%--						<td>--%>
<%--							<%--%>
<%--							if (operator.isExitsThisUrl("popedom_mgr/viewToolSetting.do")) {--%>
<%--							%>--%>
<%--							<img src="image/page.gif" border="0">--%>
<%--							<a href="javascript:menu_click('popedom_mgr/viewToolSetting.do')">功能菜单管理</a>--%>
<%--							<%--%>
<%--							}--%>
<%--							%>--%>
<%--						</td>--%>
<%--					</tr>--%>
				</table>
				<!-- 权限管理(End)-->
			</td>
		</tr>
		<tr id="l3">
			<td height="1" valign="bottom" background="image/navibk.jpg">
				<img src="image/navisp.jpg">
			</td>
		</tr>
		<%
		}
		%>

		<tr>
			<td height="35">
				<a href="javascript:_exit()"><img src="image/navin5.jpg"
						border="0">
				</a>
			</td>
		</tr>
	</table>
	<script language="javascript">
			menu_pic_init();
			function downLoadReport(){
				window.location="<%=request.getContextPath()%>/servlets/downLoadReportServlet";
			}
		</script>
</body>
</html:html>
