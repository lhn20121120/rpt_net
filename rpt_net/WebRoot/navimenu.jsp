<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@page import="com.cbrc.auth.form.OperatorForm"%>
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
		
		OperatorForm operatorForm = null;
		operatorForm = (OperatorForm)session.getAttribute("OPERATORFORM");
		
		/** 报表选中标志 **/
		String reportFlg = "0";
		String flag1104 = "G";
		
		if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
			reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
			flag1104 = (String) session.getAttribute(Config.FLAG_1104);
		}
		
		/**业务条线*/
		String business = null;
		if(session.getAttribute("business")!=null){
			business = (String) session.getAttribute("business");
		}
		
		boolean yjhbb = false;
		boolean yjhbb_f = false;//银监会报表分支条线
		boolean rhbb = false;
		boolean qtbb = false;
		boolean sjbl = false;
		boolean etljk = false;
		boolean taskgl=false;//任务管理
		
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
		
		boolean yjh_bbcl_yrwdz = operator.isExitsThisUrl("YRWDZ"); //任务定制
		boolean yjh_bbcl_yrwgl = operator.isExitsThisUrl("YRWGL"); //任务管理
		boolean yjh_bbcl_yddrw = operator.isExitsThisUrl("YDDRW"); //待定任务
		boolean yjh_bbcl_yrbbsc = operator.isExitsThisUrl("YRWBBSC"); //报表生成
		boolean yjh_bbcl_yrcbrw = operator.isExitsThisUrl("YCBRW"); //重报任务
		boolean yjh_xxgl_yydxx  = operator.isExitsThisUrl("yjxx");  //已读信息
		boolean yjh_xxgl_yxxgl  = operator.isExitsThisUrl("yjxxgl");  //信息管理
		
		boolean yjh_bbcx_ybbcx = operator.isExitsThisUrl("YBBCX"); //报表查询
		boolean yjh_bbcx_ybbtz = operator.isExitsThisUrl("YBBTZ"); //报表统计
		boolean yjh_bbcx_ylhcx = operator.isExitsThisUrl("YLHCX"); //灵活查询
		boolean yjh_bbcx_ybbdc = operator.isExitsThisUrl("YBBDC"); //报表导出
		boolean yjh_bbcx_ybbrzcx = operator.isExitsThisUrl("YBBRZCX"); //报表日志查询
		boolean yjh_bbgl_ybbdygl = operator.isExitsThisUrl("YBBDYGL"); //报表定义管理
		boolean yjh_bbgl_ygbgxgl  = operator.isExitsThisUrl("YGBGXGL"); //归并关系管理
		boolean yjh_bbgl_yjygxgl  = operator.isExitsThisUrl("YJYGXGL"); //校验关系管理
		boolean yjh_bbgl_ybbsxgl  = operator.isExitsThisUrl("YBBSXGL"); //报表时限管理
		//银监会分支条线--开始
		boolean yjh_bbcl_ybbsc_f = operator.isExitsThisUrl("YBBSC"); //报表生成
		
		boolean yjh_bbcl_sc_f = operator.isExitsThisUrl("YSC"); //报表填报
		boolean yjh_bbcl_ybbjy_f = operator.isExitsThisUrl("YBBJY"); //报表校验
		boolean yjh_bbcl_ybbhz_f = operator.isExitsThisUrl("YBBHZ"); //报表汇总
		boolean yjh_bbcl_ybbfh_f = operator.isExitsThisUrl("YBBFH"); //报表复核
		boolean yjh_bbcl_ybbsq_f = operator.isExitsThisUrl("YBBSQ"); //报表审签
		boolean yjh_bbcl_ybbcb_f = operator.isExitsThisUrl("YBBCB"); //报表重报
		boolean yjh_bbcl_yrwdz_f = operator.isExitsThisUrl("YRWDZ"); //任务定制
		boolean yjh_bbcl_yrwgl_f = operator.isExitsThisUrl("YRWGL"); //任务管理
		boolean yjh_bbcl_yrbbsc_f = operator.isExitsThisUrl("YRWBBSC"); //报表生成
		boolean yjh_bbcl_yddrw_f = operator.isExitsThisUrl("YDDRW"); //待定任务
		boolean yjh_bbcl_yrcbrw_f = operator.isExitsThisUrl("YCBRW"); //重报任务
		
		boolean yjh_bbcx_ybbcx_f = operator.isExitsThisUrl("YBBCX"); //报表查询
		boolean yjh_bbcx_ylhcx_f = operator.isExitsThisUrl("YLHCX"); //灵活查询
		boolean yjh_bbcx_sjcx_f = operator.isExitsThisUrl("YSJCX"); //数据查询
		boolean yjh_bbcx_hjcx_f = operator.isExitsThisUrl("YHJCX"); //痕迹查询
		boolean yjh_bbcx_ybbdc_f = operator.isExitsThisUrl("YBBDC"); //报表导出
		boolean yjh_bbcx_ybbtz_f = operator.isExitsThisUrl("YBBTZ"); //报表统计
		
		boolean yjh_bbgl_ybbdygl_f = operator.isExitsThisUrl("YBBDYGL"); //报表定义管理
		boolean yjh_bbgl_ygbgxgl_f  = operator.isExitsThisUrl("YGBGXGL"); //归并关系管理
		boolean yjh_bbgl_ybbsxgl_f  = operator.isExitsThisUrl("YBBSXGL"); //报表时限管理
		boolean yjh_bbgl_yjygxgl_f  = operator.isExitsThisUrl("YJYGXGL"); //校验关系管理
		
		boolean yjh_xxgl_yydxx_f  = operator.isExitsThisUrl("yjxx");  //已读信息
		boolean yjh_xxgl_yxxgl_f  = operator.isExitsThisUrl("yjxxgl");  //信息管理
		//银监会分支条线--结束
		boolean rh_bbcl_rbbsc  = operator.isExitsThisUrl("RBBSC"); //报表生成
		boolean rh_bbcl_rbbxz  = operator.isExitsThisUrl("RBBXZ"); //报表下载
		boolean rh_bbcl_rsc  =  operator.isExitsThisUrl("RSC"); //报表上传
		boolean rh_bbcl_rbbfh  = operator.isExitsThisUrl("RBBFH"); //报表复核
		boolean rh_bbcl_rbbjy  =  operator.isExitsThisUrl("RBBJY"); //报表校验
		boolean rh_bbcl_rbbhz  = operator.isExitsThisUrl("RBBHZ"); //报表汇总
		boolean rh_bbcl_rbbcb  = operator.isExitsThisUrl("RBBCB"); //报表重报
		
		boolean rh_bbcl_rrwdz = operator.isExitsThisUrl("RRWDZ"); //任务定制
		boolean rh_bbcl_rrwgl = operator.isExitsThisUrl("RRWGL"); //任务管理
		boolean rh_bbcl_rddrw = operator.isExitsThisUrl("RDDRW"); //待定任务
		boolean rh_bbcl_rrbbsc = operator.isExitsThisUrl("RRWBBSC"); //报表生成
		boolean rh_bbcl_rrcbrw = operator.isExitsThisUrl("RCBRW"); //重报任务
		boolean rh_xxgl_rydxx  = operator.isExitsThisUrl("rhxx");  //已读信息
		boolean rh_xxgl_rxxgl  = operator.isExitsThisUrl("rhxxgl");  //信息管理
		
		boolean rh_bbcx_rbbcx  =  operator.isExitsThisUrl("RBBCX"); //报表查询
		boolean rh_bbcx_rlhcx  =  operator.isExitsThisUrl("RLHCX"); //灵活查询
		boolean rh_bbcx_rbbdc  = operator.isExitsThisUrl("RBBDC"); //报表导出
		boolean rh_bbcx_rhbbdc = operator.isExitsThisUrl("RHBBDC");//人行格式导出
		boolean rh_bbcx_rbbrzcx  = operator.isExitsThisUrl("RBBRZCX"); //报表日志查询
		boolean rh_bbcx_rbbtz = operator.isExitsThisUrl("RBBTZ");//报表统计
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
		
		boolean qt_bbcl_qrwdz = operator.isExitsThisUrl("QRWDZ"); //任务定制
		boolean qt_bbcl_qrwgl = operator.isExitsThisUrl("QRWGL"); //任务管理
		boolean qt_bbcl_qddrw = operator.isExitsThisUrl("QDDRW"); //待定任务
		boolean qt_bbcl_qrbbsc = operator.isExitsThisUrl("QRWBBSC"); //报表生成
		boolean qt_bbcl_qrcbrw = operator.isExitsThisUrl("QCBRW"); //重报任务
		boolean qt_xxgl_qydxx  = operator.isExitsThisUrl("qtxx");  //已读信息
		boolean qt_xxgl_qxxgl  = operator.isExitsThisUrl("qtxxgl");  //信息管理
		
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
		boolean xtgl_xtcssd = operator.isExitsThisUrl("XTCSSD");//汇率管理
		boolean fxjc  =  operator.isExitsThisUrl("FXJC"); //风险监测
		
		boolean bb_sjcj = operator.isExitsThisUrl("BBBCJ"); //数据采集
		boolean bb_bbsc = operator.isExitsThisUrl("BMBXZ"); //模板下载
		boolean bb_bbcx = operator.isExitsThisUrl("BBBCK"); //报表查看
		
		/**ETL监控*/
		boolean etl_rwpz=operator.isExitsThisUrl("RWPZ");//任务配置
		boolean etl_rwdd=operator.isExitsThisUrl("RWDD");//任务调度
		boolean etl_rwjk=operator.isExitsThisUrl("RWJK");//任务监控
		boolean etl_fwqpz=operator.isExitsThisUrl("FWQPZ");//服务器配置
		
		/**任务管理*/
		boolean task_rwdz=operator.isExitsThisUrl("RWDZ");//任务定制
		boolean task_ddrw=operator.isExitsThisUrl("DDRW");//待办任务
		boolean task_bwsc=operator.isExitsThisUrl("BWSC");//报文上报
		boolean task_rwgl=operator.isExitsThisUrl("RWGL");//任务管理
		boolean task_rwcb=operator.isExitsThisUrl("RWCB");//任务重报
		boolean etl_bsgl = operator.isExitsThisUrl("BSGL");//部署管理
		//银监数据查询
		boolean yjh_bbcx_sjcx = operator.isExitsThisUrl("YSJCX");
		//人行数据查询
		boolean rmyh_bbcx_sjcx = operator.isExitsThisUrl("RSJCX");
		//痕迹查询
		boolean yjh_bbcx_hjcx = operator.isExitsThisUrl("YHJCX");
		//映射关系管理
		boolean xtgl_ysgxgl = operator.isExitsThisUrl("YSGXGL");
		//在线用户管理
		boolean xtgl_zxyhgl = operator.isExitsThisUrl("ZXYHGL");
		
		if(yjh_bbcl_ybbsc || yjh_bbcl_ybbxz || yjh_bbcl_sc || yjh_bbcl_ybbfh || yjh_bbcl_ybbjy 
			|| yjh_bbcl_ybbhz || yjh_bbcl_ybbsq || yjh_bbcl_ybbcb || yjh_bbcx_ybbcx || yjh_bbcx_ylhcx ||yjh_bbcx_hjcx || yjh_bbcx_sjcx
			|| yjh_bbcx_ybbdc || yjh_bbcx_ybbrzcx || yjh_bbgl_ybbdygl || yjh_bbgl_ygbgxgl || yjh_bbgl_yjygxgl || yjh_bbcl_yrwdz || yjh_bbcl_yrwgl 
			|| yjh_bbcl_yddrw || yjh_bbcl_yrbbsc || yjh_bbcl_yrcbrw || yjh_xxgl_yydxx || yjh_xxgl_yxxgl){
			if(business != null){
				if(business.equals("yjbb"))yjhbb =true;
			}else{
				yjhbb =true;
			}
		}
		if(yjh_bbcl_ybbsc_f || yjh_bbcl_sc_f || yjh_bbcl_ybbfh_f || yjh_bbcl_ybbjy_f || yjh_bbcl_ybbhz_f 
			|| yjh_bbcl_ybbsq_f || yjh_bbcl_ybbcb_f || yjh_bbcx_ybbcx_f || yjh_bbcx_ylhcx_f ||yjh_bbcx_hjcx_f || yjh_bbcx_sjcx_f
			|| yjh_bbcx_ybbdc_f || yjh_bbgl_ybbdygl_f || yjh_bbgl_ygbgxgl_f || yjh_bbgl_yjygxgl_f || yjh_bbcl_yrwdz_f || yjh_bbcl_yrwgl_f 
			|| yjh_bbcl_yddrw_f || yjh_bbcl_yrbbsc_f || yjh_bbcl_yrcbrw_f || yjh_xxgl_yydxx_f || yjh_xxgl_yxxgl_f || yjh_bbcx_ybbtz_f|| yjh_bbgl_ybbsxgl_f){
			if(business != null){
				if(business.equals("yjbb_f"))yjhbb_f =true;
			}else{
				yjhbb_f =true;
			}
		}
		if(rh_bbcl_rbbsc || rh_bbcl_rbbxz || rh_bbcl_rsc || rh_bbcl_rbbfh || rh_bbcl_rbbjy 
			|| rh_bbcl_rbbhz || rh_bbcl_rbbcb || rh_bbcx_rbbcx || rh_bbcx_rlhcx   
			|| rh_bbcx_rbbdc || rh_bbcx_rhbbdc || rh_bbcx_rbbrzcx  || rh_bbgl_rbbdygl  || rh_bbgl_rgbgxgl  || rh_bbgl_rjygxgl 
			|| rh_bbcl_rcj || rh_bbcl_rrwdz || rh_bbcl_rrwgl || rh_bbcl_rddrw || rh_bbcl_rrbbsc ||rh_bbcl_rrcbrw
			|| rh_xxgl_rydxx || rh_xxgl_rxxgl){
			if(business != null){
				if(business.equals("rhbb"))rhbb = true;
			}else{
				rhbb =true;
			}
		}
		if(qt_bbcl_qbbsc   || qt_bbcl_qbbxz   || qt_bbcl_qsc   || qt_bbcl_qbbfh   || qt_bbcl_qbbjy   
			|| qt_bbcl_qbbhz   || qt_bbcl_qbbcb   || qt_bbcx_qbbcx   || qt_bbcx_qlhcx   || qt_bbcx_qfxbcx   
			|| qt_bbcx_qbbrzcx   || qt_bbgl_qbbdygl   || qt_bbgl_qgbgxgl   || qt_bbgl_qjygxgl 
			|| qt_bbcl_qbcj || qt_bbcl_qrwdz || qt_bbcl_qrwgl || qt_bbcl_qddrw || qt_bbcl_qrbbsc || qt_bbcl_qrcbrw 
			||qt_xxgl_qydxx || qt_xxgl_qxxgl){
			if(business != null){
				if(business.equals("hnbb"))qtbb = true;
			}else{
				qtbb =true;
			}
		}
		if(bb_sjcj   || bb_bbsc   || bb_bbcx ){
			sjbl =true;
		}
		if(etl_rwpz || etl_rwdd || etl_rwjk || etl_fwqpz || etl_bsgl)
			etljk=true;

		if(task_rwdz || task_ddrw || task_rwgl || task_rwcb||task_bwsc)
			taskgl=true;
	%>

	<html:base />
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="css/globalStyle.css" type="text/css" rel="stylesheet">
	<script language="JavaScript" src="js/tree.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.4.min.js"></script>
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
			
			var scriptyjh_bbcl_yrwdz =<%=yjh_bbcl_yrwdz %>;
			var scriptyjh_bbcl_yrwgl =<%=yjh_bbcl_yrwgl %>;
			var scriptyjh_bbcl_yddrw =<%=yjh_bbcl_yddrw %>;
			var scriptyjh_bbcl_yrbbsc =<%=yjh_bbcl_yrbbsc %>;
			var scriptyjh_bbcl_yrcbrw =<%=yjh_bbcl_yrcbrw %>;
			var scriptyjh_xxcl_yydxx = <%=yjh_xxgl_yydxx%>;
			var scriptyjh_xxgl_yxxgl = <%=yjh_xxgl_yxxgl%>;
			
			var scriptyjh_bbcx_ybbcx=<%=yjh_bbcx_ybbcx%>;
			var scriptyjh_bbcx_ylhcx=<%=yjh_bbcx_ylhcx%>;
			var scriptyjh_bbcx_ybbdc=<%=yjh_bbcx_ybbdc%>;
			var scriptyjh_bbcx_ybbrzcx=<%=yjh_bbcx_ybbrzcx%>;
			var scriptyjh_bbgl_ybbdygl=<%=yjh_bbgl_ybbdygl%>;
			var scriptyjh_bbgl_ygbgxgl=<%=yjh_bbgl_ygbgxgl%>;
			var scriptyjh_bbgl_yjygxgl=<%=yjh_bbgl_yjygxgl%>;
			var scriptyjh_bbgl_ybbsxgl=<%=yjh_bbgl_ybbsxgl%>;
			//银监报表分支条线：设定开始
			var scriptyjh_bbcl_ybbsc_f=<%=yjh_bbcl_ybbsc_f%>;
			
			var scriptyjh_bbcl_sc_f=<%=yjh_bbcl_sc_f%>;
			var scriptyjh_bbcl_ybbjy_f=<%=yjh_bbcl_ybbjy_f%>;
			var scriptyjh_bbcl_ybbhz_f=<%=yjh_bbcl_ybbhz_f%>;
			var scriptyjh_bbcl_ybbfh_f=<%=yjh_bbcl_ybbfh_f%>;
			var scriptyjh_bbcl_ybbsq_f=<%=yjh_bbcl_ybbsq_f%>;
			var scriptyjh_bbcl_ybbcb_f=<%=yjh_bbcl_ybbcb_f%>;
			var scriptyjh_bbcl_yrwdz_f =<%=yjh_bbcl_yrwdz_f %>;
			var scriptyjh_bbcl_yrwgl_f =<%=yjh_bbcl_yrwgl_f %>;
			var scriptyjh_bbcl_yrbbsc_f =<%=yjh_bbcl_yrbbsc_f %>;
			var scriptyjh_bbcl_yddrw_f =<%=yjh_bbcl_yddrw_f %>;
			var scriptyjh_bbcl_yrcbrw_f =<%=yjh_bbcl_yrcbrw_f %>;
			
			var scriptyjh_bbcx_ybbcx_f=<%=yjh_bbcx_ybbcx_f%>;
			var scriptyjh_bbcx_ylhcx_f=<%=yjh_bbcx_ylhcx_f%>;
			var scriptyjh_bbcx_sjcx_f=<%=yjh_bbcx_sjcx_f%>;
			var scriptyjh_bbcx_hjcx_f = <%=yjh_bbcx_hjcx_f%> 
			var scriptyjh_bbcx_ybbdc_f=<%=yjh_bbcx_ybbdc_f%>;
			var scriptyjh_bbcx_ybbtz_f=<%=yjh_bbcx_ybbtz_f%>;
			
			var scriptyjh_bbgl_ybbdygl_f=<%=yjh_bbgl_ybbdygl_f%>;
			var scriptyjh_bbgl_ygbgxgl_f=<%=yjh_bbgl_ygbgxgl_f%>;
			var scriptyjh_bbgl_ybbsxgl_f=<%=yjh_bbgl_ybbsxgl_f%>;
			var scriptyjh_bbgl_yjygxgl_f=<%=yjh_bbgl_yjygxgl_f%>;
			
			var scriptyjh_xxcl_yydxx_f = <%=yjh_xxgl_yydxx_f%>;
			var scriptyjh_xxgl_yxxgl_f = <%=yjh_xxgl_yxxgl_f%>;
			//银监报表分支条线：设定结束
			var scriptrh_bbcl_rbbsc=<%=rh_bbcl_rbbsc%>;
			var scriptrh_bbcl_rbbxz=<%=rh_bbcl_rbbxz%>;
			var scriptrh_bbcl_rsc=<%=rh_bbcl_rsc%>;
			var scriptrh_bbcl_rbbfh=<%=rh_bbcl_rbbfh%>;
			var scriptrh_bbcl_rbbjy=<%=rh_bbcl_rbbjy%>;
			var scriptrh_bbcl_rbbhz=<%=rh_bbcl_rbbhz%>;
			var scriptrh_bbcl_rbbcb=<%=rh_bbcl_rbbcb%>;
			var scriptrh_xxgl_rydxx=<%=rh_xxgl_rydxx%>;
			var scriptrh_xxgl_rxxgl =<%=rh_xxgl_rxxgl%>;
			
			var scriptrh_bbcl_rrwdz=<%=rh_bbcl_rrwdz%>;
			var scriptrh_bbcl_rrwgl=<%=rh_bbcl_rrwgl%>;
			var scriptrh_bbcl_rddrw=<%=rh_bbcl_rddrw%>;
			var scriptrh_bbcl_rrbbsc=<%=rh_bbcl_rrbbsc%>;
			var scriptrh_bbcl_rrcbrw=<%=rh_bbcl_rrcbrw%>;
			
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
			
			var scriptqt_bbcl_qrwdz =<%=qt_bbcl_qrwdz %>;
			var scriptqt_bbcl_qrwgl =<%=qt_bbcl_qrwgl %>;
			var scriptqt_bbcl_qddrw =<%=qt_bbcl_qddrw %>;
			var scriptqt_bbcl_qrbbsc =<%=qt_bbcl_qrbbsc %>;
			var scriptqt_bbcl_qrcbrw =<%=qt_bbcl_qrcbrw %>;
			var scriptqt_xxgl_qydxx = <%=qt_xxgl_qydxx%>;
			var scriptqt_xxgl_qxxgl = <%=qt_xxgl_qxxgl%>;
			
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

			var scriptbb_sjcj = <%=bb_sjcj%>;
			var scriptbb_bbsc = <%=bb_bbsc%>;
			var scriptbb_bbcx = <%=bb_bbcx%>;

			var scriptxtgl_ysgxgl = <%=xtgl_ysgxgl%>//映射关系管理
			//数据查询
			var scriptyjh_bbcx_sjcx = <%=yjh_bbcx_sjcx%>;
			//痕迹查询
			var scriptyjh_bbcx_hjcx = <%=yjh_bbcx_hjcx%> 
			
			var scriptFlag1104 = "<%=flag1104%>";//银监会报表分支条线是否启用标志位
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
			 * 银监会信息处理
			 */
			 var YXXCL = "YXXCL";
			 //银监会报表分支条线开始
			 /**
			 * 银监会数据采集 
			 */
			var YSJCJ_F="YSJCJ_F";
			/**
			 * 银监会报表处理 
			 */
			var YBBCL_F="YBBCL_F";
			/**
			 * 银监会报表查询
			 */
			var YBBCX_F="YBBCX_F";
			/**
			 * 银监会报表管理
			 */
			var YBBGL_F="YBBGL_F";
			/**
			 * 银监会信息处理
			 */
			 var YXXCL_F = "YXXCL_F";
			//银监会报表分支条线结束
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
			 * 人行信息处理
			 */
			 var RXXCL = "RXXCL";
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
			 * 其他信息处理
			 */
			 var QXXCL = "QXXCL";
			
			/**
			 * 系统管理
			 */
			var XTGL="XTGL";
			
			/**
			 * 信息公告
			 */
			var XXGG="XXGG";

			/**
			 * 数据补录
			 */
			var SJBL="SJBL";

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
			function menu_pic_click1(menu){
				
				
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
				scriptxtgl_rzgl == true || scriptxtgl_ysgxgl == true))
					eval("document.getElementById('tr_" + XTGL + "').style.display='none'");
				
				if(menu!=YSJCJ && (scriptReportFlg=="1"&&scriptFlag1104!='F') && (scriptyjh_bbcl_ybbsc == true || scriptyjh_bbcl_ybbxz == true ))
					eval("document.getElementById('tr_" + YSJCJ + "').style.display='none'");
					
				if(menu!=YBBCL && (scriptReportFlg=="1"&&scriptFlag1104!='F') && (scriptyjh_bbcl_sc == true || scriptyjh_bbcl_ybbfh == true || 
				scriptyjh_bbcl_ybbjy == true || scriptyjh_bbcl_ybbhz == true || 
				scriptyjh_bbcl_ybbsq == true || scriptyjh_bbcl_ybbcb == true
				|| scriptyjh_bbcl_yrwdz == true || scriptyjh_bbcl_yrwgl == true || scriptyjh_bbcl_yddrw == true
				|| scriptyjh_bbcl_yrbbsc == true || scriptyjh_bbcl_yrcbrw == true))
					eval("document.getElementById('tr_" + YBBCL + "').style.display='none'");
					
				if(menu!=YBBCX && (scriptReportFlg=="1"&&scriptFlag1104!='F') && (scriptyjh_bbcx_ybbcx == true || scriptyjh_bbcx_ylhcx == true || 
				scriptyjh_bbcx_ybbdc == true || scriptyjh_bbcx_ybbrzcx == true
				|| scriptyjh_bbcx_sjcx == true || scriptyjh_bbcx_hjcx == true))
					eval("document.getElementById('tr_" + YBBCX + "').style.display='none'");
	
				if(menu!=YBBGL && (scriptReportFlg=="1"&&scriptFlag1104!='F') && (scriptyjh_bbgl_ybbdygl == true || scriptyjh_bbgl_ygbgxgl == true || 
				scriptyjh_bbgl_yjygxgl == true || scriptyjh_bbgl_ybbsxgl == true))
					eval("document.getElementById('tr_" + YBBGL + "').style.display='none'");
					
				//银监信息处理
				if(menu!=YXXCL && (scriptReportFlg=="1"&&scriptFlag1104!='F') && (scriptyjh_xxcl_yydxx == true || scriptyjh_xxgl_yxxgl == true))
					eval("document.getElementById('tr_" + YXXCL + "').style.display='none'");	
				//银监报表分支条线开始	
				if(menu!=YSJCJ_F && (scriptReportFlg=="1"&&scriptFlag1104=='F') && (scriptyjh_bbcl_ybbsc_f == true ))
					eval("document.getElementById('tr_" + YSJCJ_F + "').style.display='none'");
					
				if(menu!=YBBCL_F && (scriptReportFlg=="1"&&scriptFlag1104=='F') && (scriptyjh_bbcl_sc_f == true || scriptyjh_bbcl_ybbfh_f == true || 
				scriptyjh_bbcl_ybbjy_f == true || scriptyjh_bbcl_ybbhz_f == true || 
				scriptyjh_bbcl_ybbsq_f == true || scriptyjh_bbcl_ybbcb_f == true
				|| scriptyjh_bbcl_yrwdz_f == true || scriptyjh_bbcl_yrwgl_f == true || scriptyjh_bbcl_yddrw_f == true
				|| scriptyjh_bbcl_yrbbsc_f == true || scriptyjh_bbcl_yrcbrw_f == true))
					eval("document.getElementById('tr_" + YBBCL_F + "').style.display='none'");
					
				if(menu!=YBBCX_F && (scriptReportFlg=="1"&&scriptFlag1104=='F') && (scriptyjh_bbcx_ybbcx_f == true || scriptyjh_bbcx_ylhcx_f == true || 
				scriptyjh_bbcx_ybbdc_f == true || scriptyjh_bbcx_ybbtz_f == true
				|| scriptyjh_bbcx_sjcx_f == true || scriptyjh_bbcx_hjcx_f == true))
					eval("document.getElementById('tr_" + YBBCX_F + "').style.display='none'");
	
				if(menu!=YBBGL_F && (scriptReportFlg=="1"&&scriptFlag1104=='F') && (scriptyjh_bbgl_ybbdygl_f == true || scriptyjh_bbgl_ygbgxgl_f == true || 
				scriptyjh_bbgl_yjygxgl_f == true || scriptyjh_bbgl_ybbsxgl_f == true))
					eval("document.getElementById('tr_" + YBBGL_F + "').style.display='none'");
					
				//银监信息处理
				if(menu!=YXXCL_F && (scriptReportFlg=="1"&&scriptFlag1104=='F') && (scriptyjh_xxcl_yydxx_f == true || scriptyjh_xxgl_yxxgl_f == true))
					eval("document.getElementById('tr_" + YXXCL_F + "').style.display='none'");
				//银监报表分支条线结束

				//if(menu!=RSJCJ && (scriptReportFlg=="2") && (scriptrh_bbcl_rbbsc == true || scriptrh_bbcl_rbbxz == true || scriptrh_bbcl_rcj == true))
				//	eval("document.getElementById('tr_" + RSJCJ + "').style.display='none'");
				if(menu!=RBBCL && (scriptReportFlg=="2") && (scriptrh_bbcl_rsc == true || 
				scriptrh_bbcl_rbbfh == true || scriptrh_bbcl_rbbjy == true || scriptrh_bbcl_rbbhz == true || 
				scriptrh_bbcl_rbbcb == true || scriptrh_bbcx_rhbbdc == true
				 || scriptrh_bbcl_rrwdz == true ||scriptrh_bbcl_rrwgl == true ||scriptrh_bbcl_rddrw == true
				 ||scriptrh_bbcl_rrbbsc == true ||scriptrh_bbcl_rrcbrw == true))
					eval("document.getElementById('tr_" + RBBCL + "').style.display='none'");
	
				if(menu!=RBBCX && (scriptReportFlg=="2") && (scriptrh_bbcx_rbbcx == true || scriptrh_bbcx_rlhcx == true || 
				scriptrh_bbcx_rbbdc == true|| scriptrh_bbcx_rbbrzcx == true))
					eval("document.getElementById('tr_" + RBBCX + "').style.display='none'");
	
				if(menu!=RBBGL && (scriptReportFlg=="2") && (scriptrh_bbgl_rbbdygl == true || scriptrh_bbgl_rgbgxgl == true || 
				scriptrh_bbgl_rjygxgl == true || scriptrh_bbgl_rbbsxgl == true))
					eval("document.getElementById('tr_" + RBBGL + "').style.display='none'");
				
				//人行信息处理	
				if(menu!=RXXCL && (scriptReportFlg=="2") && (scriptrh_xxgl_rydxx == true || scriptrh_xxgl_rxxgl == true))
					eval("document.getElementById('tr_" + RXXCL + "').style.display='none'");
				
				if(menu!=QSJCJ && (scriptReportFlg=="3") && (scriptqt_bbcl_qbbsc == true || scriptqt_bbcl_qbbxz == true || scriptqt_bbcl_qbcj == true))
					eval("document.getElementById('tr_" + QSJCJ + "').style.display='none'");
				
				if(menu!=QBBCL && (scriptReportFlg=="3") && ( scriptqt_bbcl_qsc == true || 
				scriptqt_bbcl_qbbfh == true || scriptqt_bbcl_qbbjy == true || scriptqt_bbcl_qbbhz == true || 
				scriptqt_bbcl_qbbcb == true || scriptqt_bbcl_qrwdz == true || scriptqt_bbcl_qrwgl == true || 
				scriptqt_bbcl_qddrw == true || scriptqt_bbcl_qrbbsc == true || scriptqt_bbcl_qrcbrw == true))
					eval("document.getElementById('tr_" + QBBCL + "').style.display='none'");
	
				if(menu!=QBBCX && (scriptReportFlg=="3") && (scriptqt_bbcx_qbbcx == true || scriptqt_bbcx_qlhcx == true || 
				scriptqt_bbcx_qfxbcx == true || scriptqt_bbcx_qbbrzcx == true))
					eval("document.getElementById('tr_" + QBBCX + "').style.display='none'");
	
				if(menu!=QBBGL && (scriptReportFlg=="3") && (scriptqt_bbgl_qbbdygl == true || scriptqt_bbgl_qgbgxgl == true || 
				scriptqt_bbgl_qjygxgl == true || scriptqt_bbgl_qbbsxgl == true))
					eval("document.getElementById('tr_" + QBBGL + "').style.display='none'");
					
				//其他信息处理
				if(menu!=QXXCL && (scriptReportFlg=="3") && (scriptqt_xxgl_qydxx == true || scriptqt_xxgl_qxxgl == true))
					eval("document.getElementById('tr_" + QXXCL + "').style.display='none'");			

				if(menu!=QBBGL && (scriptReportFlg=="3") && (scriptqt_bbgl_qbbdygl == true || scriptqt_bbgl_qgbgxgl == true || 
						scriptqt_bbgl_qjygxgl == true || scriptqt_bbgl_qbbsxgl == true))
							eval("document.getElementById('tr_" + QBBGL + "').style.display='none'");
							
							
		

				if(menu==XTGL || menu=='ETL'){
						eval("document.getElementById('cbrc').style.display='none'");
						eval("document.getElementById('cbrc_f').style.display='none'");
						//eval("document.getElementById('pboc').style.display='none'");
						eval("document.getElementById('other').style.display='none'");
						eval("document.getElementById('tr_pboc').style.display='none'");
						
						//eval("document.getElementById('tr_SJBL').style.display='none'");						
				}

				if(menu==SJBL){
					eval("document.getElementById('cbrc').style.display='none'");
					eval("document.getElementById('cbrc_f').style.display='none'");
					//eval("document.getElementById('pboc').style.display='none'");
					eval("document.getElementById('other').style.display='none'");
					eval("document.getElementById('tr_XTGL').style.display='none'");
					
					eval("document.getElementById('tr_pboc').style.display='none'");						
				}
			}
			
		    /**
		     * 点击菜单事件
		     */
		    function menu_click(_url){
		    	if(_url=="") return;
		    	window.parent.mainFrame.contents.location.assign(_url);
	//			window.open(_url);
		    } 
		    function changeReport(reportObj){	 
				var reportFlg=reportObj;
				var url="<%=request.getContextPath()%>/reportPortal.do?reportFlg="+reportFlg;
				if(reportObj==1){
					url+="&Flag1104=G";
				}
				window.location.href=url;
			 }
			 //银监会报表分支条线
			 function changeReport_F(reportObj){	 
				var reportFlg=reportObj;
				var url="<%=request.getContextPath()%>/reportPortal.do?reportFlg="+reportFlg+"&Flag1104=F";
				window.location.href=url;
			 }
			 function loadpage(){
			 	window.parent.mainFrame.contents.location.assign('main.jsp');
			 }

			 function changeReport_BL(reportObj){	 
					var reportFlg=reportObj;
				$.ajax({ 
				    type : "post", 
				    url : "<%=request.getContextPath()%>/reportPortal.do?reportFlg="+reportFlg+"&fowardFlag=sjbl",
				    async : false, 
				    success : function(data){} 
				    }); 
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
			/**
			 *转到分析系统
			 */			 
			function toUrl(){				
					window.open("<%=Config.WEBROOTULR%>/redirect.jsp");
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
			<td background="image/navisp.jpg" width="160">
			</td>
		</tr>
		
		<tr>
			<td height="3">
			</td>
		</tr>
		
		
		<%
			if(yjhbb){
		 %>
		<tr>
			<td>
				<img src="image/rpt_cbrc.gif" onclick="changeReport(1)">
			</td>
		</tr>
		<%
			}
		 %>
		<!--银监会报表(Begin)--->
		<tr id="cbrc">
		<td>
			<table width="100%">
			<%
				//}
				if(reportFlg.equals("1")&&!flag1104.equals("F")){
	
				if (yjh_bbcl_ybbsc) {
			%>
			<tr>
				<td height="30" width="155"  background="image/button_blue.jpg">
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
								<a href="afReportProduct.do" target="mainFrame">报表生成</a>	
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
				|| yjh_bbcl_ybbhz   || yjh_bbcl_ybbsq || yjh_bbcl_ybbcb
				||yjh_bbcl_yrwdz || yjh_bbcl_yrwgl || yjh_bbcl_yddrw 
				|| yjh_bbcl_yrbbsc || yjh_bbcl_yrcbrw ) {
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
							if(Config.SYSTEM_SCHEMA_FLAG==0){
								if (true ||yjh_bbcl_sc) {
						%>
						<tr>
							<td  background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="viewDataReport.do" target="mainFrame">报表填报</a>
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
								<a href="dateQuary/manualCheckRepSHJY.do" target="mainFrame">报表校验</a>
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
								<a href="viewCollectData.do?orgId=<%=operator.getOrgId() %>" target="mainFrame">报表汇总</a>
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
								<a href="dateQuary/manualCheckRep.do" target="mainFrame">报表审核</a>
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
								<a href="report_mgr/report_again_mgr/report_again_mgr.jsp" target="mainFrame">报表重报</a>
							</td>
						</tr>
						<%
						}
						}
						%>
						
						
							<%
								if(Config.SYSTEM_SCHEMA_FLAG==1){
								if(yjh_bbcl_yrwdz){
							 %>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="/rpt_other/findAll_workTaskInfoAction.action?reportFlag=yjtx" target="mainFrame" >任务定制</a>
								</td>
							</tr>
							<%
								}
								if(yjh_bbcl_yrwgl){
							 %>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="/rpt_other/workTaskInfoOrMoniAction!findWorkTaskInfoOrMoni.action?reportFlag=yjtx" target="mainFrame">任务管理</a>
								</td>
							</tr>
							<%
								}
								if(yjh_bbcl_yrbbsc){
							 %>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="/rpt_other/pendingTaskAction!findBWSCTasks.action?reportFlag=yjtx" target="mainFrame">报表输出</a>
								</td>
							</tr>
							<%
								}
								if(yjh_bbcl_yddrw){
							 %>
							 <tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="/rpt_other/pendingTaskAction.action?reportFlag=yjtx" target="mainFrame">待办任务</a>
								</td>
							</tr>
							 <%} 
								if(yjh_bbcl_yrcbrw){
							 %>
								<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="/rpt_other/pendingTaskAction!findAllRepTaskInfo.action?repFlag=1&reportFlag=yjtx" target="mainFrame">重报任务</a>
								</td>
							</tr>
							<%
								}}
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
				|| yjh_bbcx_ylhcx  || yjh_bbcx_ybbdc  || yjh_bbcx_ybbrzcx || yjh_bbcx_sjcx || yjh_bbcx_hjcx ) {
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
								<a href="reportSearch/viewReportStatAction.do" target="mainFrame">报表查询</a>
							</td>
						</tr>
						<%
						}
						%>
	
						<%
							//if (yjh_bbcx_ylhcx) {
							if(false){
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="customViewAFReport.do" target="mainFrame">灵活查询</a>
							</td>
						</tr>
						<%
						}
						%>
						
						<%
							if(yjh_bbcx_sjcx){
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="searchDataInitAction.do" target="mainFrame">数据查询</a>
							</td>
						</tr>
						<%
							}
						%>
						<%
							if(yjh_bbcx_hjcx){
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="viewDataTraceAction.do" target="mainFrame">痕迹查询</a>
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
								<a href="exportAFReport.do" target="mainFrame">报表导出</a>
							</td>
						</tr>
						<%
						}
						%>
						
						<%
							if (yjh_bbcx_ybbtz) {
							//if(false){
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="reportStatisticsCollect.do?reportFlg=<%=reportFlg %>" target="mainFrame">报表统计</a>
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
								<a href="template/viewTemplate.do" target="mainFrame">报表定义管理</a>
							</td>
						</tr>
<%--						<tr>--%>
<%--							<td background="image/button_q.gif" height="25">--%>
<%--								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;--%>
<%--								<a href="../FitEditOnline/excelToHtml!editOnline.action?tempFilePath=C:\fitech_ora\G0100_1010.xls" target="mainFrame">报表取数定义</a>--%>
<%--							</td>--%>
<%--						</tr>--%>
						<%
						}
						%>
						
						<%
							if (yjh_bbgl_ygbgxgl) {
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="afreportmerger.do" target="mainFrame">归并关系查看</a>
							</td>
						</tr>
						<%
						}
						%>
						<%
							//if (yjh_bbgl_ygbgxgl  ) {
								if(false){
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="baobiaofancha.do" target="mainFrame">模板反查维护</a>
							</td>
						</tr>
						<%
						}
						%>
						<%
							if (yjh_bbgl_yjygxgl) {
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="afreportcheck.do" target="mainFrame">校验维护查看</a>
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
			<tr>
					<%
						}
						if(yjh_xxgl_yydxx || yjh_xxgl_yxxgl){
					%>
						<td height="30"  background="image/button_blue.jpg">
							<a href="javascript:menu_pic_click('YXXCL')"> 
							<font class=b size="3"><b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;信息处理</b></font> </a>
						</td>
			</tr>
				<tr id="tr_YXXCL" style="display:none">
						<td>
							<!--信息处理(Begin)--->
							<table border="0" cellpadding="0" cellspacing="1" width="100%"
							align="center">
							<%
							if (yjh_xxgl_yydxx) {
							%>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="/rpt_other/tmsginfo!findMsgByOwn.action" target="mainFrame">已读消息</a>
								</td>
							</tr>
							<%} if (yjh_xxgl_yxxgl) {%>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="/rpt_other/tmsginfo!findOwnPublishMsg.action" target="mainFrame">消息管理</a>
								</td>
							</tr>
							<%} %>
							</table>
							<!--信息处理(End)--->
						</td>
					</tr>
			<%
				}
				}
			%>
			</table>	
		</td>
	    </tr>
		<!--银监会报表(End)--->
		
		<%
			if(Config.YJ_BRANCH_BUSI_LINE==1 && yjhbb_f){
		 %>
		<tr>
			<td>
				<img src="image/rpt_cbrc.gif" onclick="changeReport_F(1)">
			</td>
		</tr>
		<%
			}
		 %>
		<!--银监会报表:分支条线(Begin)--->
		<tr id="cbrc_f">
		<td>
			<table width="100%">
			<%
				//}
				if(reportFlg.equals("1")&&flag1104.equals("F")){
	
				if (yjh_bbcl_ybbsc_f) {
			%>
			<tr>
				<td height="30" width="155"  background="image/button_blue.jpg">
					<a href="javascript:menu_pic_click('YSJCJ_F')"> 
				<font class=b size="3"><b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;数据采集</b></font> </a>
				</td>
			</tr>
			<tr id="tr_YSJCJ_F" style="display:none">
				<td>
					<!--报表处理(Begin)--->
					<table border="0" cellpadding="0" cellspacing="1" width="100%"
						align="center">
	
						<%
							if (yjh_bbcl_ybbsc_f) {
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="afReportProduct.do" target="mainFrame">报表生成</a>	
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
				if ( yjh_bbcl_sc_f   || yjh_bbcl_ybbfh_f || yjh_bbcl_ybbjy_f 
				|| yjh_bbcl_ybbhz_f   || yjh_bbcl_ybbsq_f || yjh_bbcl_ybbcb_f
				||yjh_bbcl_yrwdz_f || yjh_bbcl_yrwgl_f || yjh_bbcl_yddrw_f 
				|| yjh_bbcl_yrbbsc_f || yjh_bbcl_yrcbrw_f ) {
			%>
			<tr>
				<td height="30"  background="image/button_blue.jpg">
					<a href="javascript:menu_pic_click('YBBCL_F')"> 
				<font class=b size="3"><b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;报表处理</b></font> </a>
				</td>
			</tr>
	
			<tr id="tr_YBBCL_F" style="display:none">
				<td>
					<!--报表处理(Begin)--->
					<table border="0" cellpadding="0" cellspacing="1" width="100%"
						align="center">
						
						<%	
							if(Config.SYSTEM_SCHEMA_FLAG==0){
								if (yjh_bbcl_sc_f) {
						%>
						<tr>
							<td  background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="viewDataReport.do" target="mainFrame">报表填报</a>
							</td>
						</tr>
						<%
						}
						%>
	
						<%
							if (yjh_bbcl_ybbjy_f ) {
						%>
						<tr>
							<td  background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="dateQuary/manualCheckRepSHJY.do" target="mainFrame">报表校验</a>
							</td>
						</tr>
						<%
						}
						%>
						
						<%
							if (yjh_bbcl_ybbhz_f ) {
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="viewCollectData.do?orgId=<%=operator.getOrgId() %>" target="mainFrame">报表汇总</a>
							</td>
						</tr>
						<%
						}
						%>
						
						<%-- 
						<tr>
	
							<td background="image/button_q.gif" height="25">
								<%
									if (yjh_bbcl_ybbfh_f ) {
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
							if (yjh_bbcl_ybbsq_f ) {
						%>
						<tr>
							<td  background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="dateQuary/manualCheckRep.do" target="mainFrame">报表审核</a>
							</td>
						</tr>
						<%
						}
						%>
	
						<%
							if (yjh_bbcl_ybbcb_f) {
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="report_mgr/report_again_mgr/report_again_mgr.jsp" target="mainFrame">报表重报</a>
							</td>
						</tr>
						<%
						}
						}
						%>
						
						
							<%
								if(Config.SYSTEM_SCHEMA_FLAG==1){
								if(yjh_bbcl_yrwdz_f){
							 %>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="/rpt_other/findAll_workTaskInfoAction.action?reportFlag=yjtx" target="mainFrame" >任务定制</a>
								</td>
							</tr>
							<%
								}
								if(yjh_bbcl_yrwgl_f){
							 %>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="/rpt_other/workTaskInfoOrMoniAction!findWorkTaskInfoOrMoni.action?reportFlag=yjtx" target="mainFrame">任务管理</a>
								</td>
							</tr>
							<%
								}
								if(yjh_bbcl_yrbbsc_f){
							 %>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="/rpt_other/pendingTaskAction!findBWSCTasks.action?reportFlag=yjtx" target="mainFrame">报表输出</a>
								</td>
							</tr>
							<%
								}
								if(yjh_bbcl_yddrw_f){
							 %>
							 <tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="/rpt_other/pendingTaskAction.action?reportFlag=yjtx" target="mainFrame">待办任务</a>
								</td>
							</tr>
							 <%} 
								if(yjh_bbcl_yrcbrw_f){
							 %>
								<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="/rpt_other/pendingTaskAction!findAllRepTaskInfo.action?repFlag=1&reportFlag=yjtx" target="mainFrame">重报任务</a>
								</td>
							</tr>
							<%
								}}
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
				if (yjh_bbcx_ybbcx_f         
				|| yjh_bbcx_ylhcx_f  || yjh_bbcx_ybbdc_f  || yjh_bbcx_ybbtz_f || yjh_bbcx_sjcx_f || yjh_bbcx_hjcx_f ) {
			%>
			<tr>
				<td height="30"  background="image/button_blue.jpg">
					<a href="javascript:menu_pic_click('YBBCX_F')"> 
					<font class=b size="3"><b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;报表查询</b></font> </a>
				</td>
			</tr>
			
			<tr id="tr_YBBCX_F" style="display:none">
				<td>
					<!--报表查询(Begin)--->
					<table border="0" cellpadding="0" cellspacing="1" width="100%"
						align="center">
	
						<%
							if (yjh_bbcx_ybbcx_f ) {
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="reportSearch/viewReportStatAction.do" target="mainFrame">报表查询</a>
							</td>
						</tr>
						<%
						}
						%>
	
						<%
							//if (yjh_bbcx_ylhcx_f) {
							if(false){
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="customViewAFReport.do" target="mainFrame">灵活查询</a>
							</td>
						</tr>
						<%
						}
						%>
						
						<%
							if(yjh_bbcx_sjcx_f){
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="searchDataInitAction.do" target="mainFrame">数据查询</a>
							</td>
						</tr>
						<%
							}
						%>
						<%
							if(yjh_bbcx_hjcx_f){
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="viewDataTraceAction.do" target="mainFrame">痕迹查询</a>
							</td>
						</tr>
						<%
							}
						%>
						
						<%
							if (yjh_bbcx_ybbdc_f) {
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="exportAFReport.do" target="mainFrame">报表导出</a>
							</td>
						</tr>
						<%
						}
						%>
						
						<%
							if (yjh_bbcx_ybbtz_f) {
							//if(false){
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="reportStatisticsCollect.do?reportFlg=<%=reportFlg %>" target="mainFrame">报表统计</a>
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
				if (yjh_bbgl_ybbdygl_f        
				|| yjh_bbgl_ygbgxgl_f || yjh_bbgl_yjygxgl_f || yjh_bbgl_ybbsxgl_f) {
			%>
			<tr>
				<td height="30"  background="image/button_blue.jpg">
					<a href="javascript:menu_pic_click('YBBGL_F')"> 
					<font class=b size="3"><b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;报表管理</b></font> </a>
				</td>
			</tr>
			
			<tr id="tr_YBBGL_F" style="display:none">
				<td>
					<!--报表管理(Begin)--->
					<table border="0" cellpadding="0" cellspacing="1" width="100%"
						align="center">
		
						<%
							if (yjh_bbgl_ybbdygl_f ) {
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="template/viewTemplate.do" target="mainFrame">报表定义管理</a>
							</td>
						</tr>
						<%
						}
						%>
						
						<%
							if (yjh_bbgl_ygbgxgl_f) {
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="afreportmerger.do" target="mainFrame">归并关系查看</a>
							</td>
						</tr>
						<%
						}
						%>
						<%
							//if (yjh_bbgl_ybbsxgl_f  ) {
								if(false){
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="baobiaofancha.do" target="mainFrame">模板反查维护</a>
							</td>
						</tr>
						<%
						}
						%>
						<%
							if (yjh_bbgl_yjygxgl_f) {
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="afreportcheck.do" target="mainFrame">校验维护查看</a>
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
			<tr>
					<%
						}
						if(yjh_xxgl_yydxx_f || yjh_xxgl_yxxgl_f){
					%>
						<td height="30"  background="image/button_blue.jpg">
							<a href="javascript:menu_pic_click('YXXCL_F')"> 
							<font class=b size="3"><b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;信息处理</b></font> </a>
						</td>
			</tr>
				<tr id="tr_YXXCL_F" style="display:none">
						<td>
							<!--信息处理(Begin)--->
							<table border="0" cellpadding="0" cellspacing="1" width="100%"
							align="center">
							<%
							if (yjh_xxgl_yydxx_f) {
							%>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="/rpt_other/tmsginfo!findMsgByOwn.action" target="mainFrame">已读消息</a>
								</td>
							</tr>
							<%} if (yjh_xxgl_yxxgl_f) {%>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="/rpt_other/tmsginfo!findOwnPublishMsg.action" target="mainFrame">消息管理</a>
								</td>
							</tr>
							<%} %>
							</table>
							<!--信息处理(End)--->
						</td>
					</tr>
			<%
				}
				}
			%>
			</table>	
		</td>
	    </tr>
		<!--银监会报表:分支条线(End)--->
		
		<%
		 	if(false){
		%>
		<!--村镇银行(Begin)--->
		<tr>
			<td>
				<img src="image/country.gif" onclick="changeReport(1)">
			</td>
		</tr>
		<tr id="pboc">
		<td>
			<table width="100%">
			<%
				//}
				if(reportFlg.equals("1")){
				
				//if (yjh_bbcl_ybbsc ) {
					if(false){
			%>
			<tr>
			
				<td height="30" width="155"  background="image/button_blue.jpg">
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
							if (yjh_bbcl_ybbsc) {
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="../rpt_net_cunzhen/afReportProduct.do?userName=<%=operatorForm.getUserName() %>&pwd=<%=operatorForm.getPassword() %>&loginDate=<%=operatorForm.getLoginDate() %>" target="mainFrame" >报表生成</a>	
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
							if (yjh_bbcl_sc) {
						%>
						<tr>
							<td  background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="../rpt_net_cunzhen/viewDataReport.do?userName=<%=operatorForm.getUserName() %>&pwd=<%=operatorForm.getPassword() %>&loginDate=<%=operatorForm.getLoginDate() %>" target="mainFrame">报表填报</a>
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
								<a href="../rpt_net_cunzhen/dateQuary/manualCheckRepSHJY.do?userName=<%=operatorForm.getUserName() %>&pwd=<%=operatorForm.getPassword() %>&loginDate=<%=operatorForm.getLoginDate() %>" target="mainFrame">报表校验</a>
							</td>
						</tr>
						<%
						}
						%>
						
						<%
							//if (yjh_bbcl_ybbhz ) {
								if(false){
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="../rpt_net_cunzhen/viewCollectData.do?orgId=<%=operator.getOrgId() %>&userName=<%=operatorForm.getUserName() %>&pwd=<%=operatorForm.getPassword() %>&loginDate=<%=operatorForm.getLoginDate() %>" target="mainFrame">报表汇总</a>
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
								<a href="../rpt_net_cunzhen/dateQuary/manualCheckRep.do?userName=<%=operatorForm.getUserName() %>&pwd=<%=operatorForm.getPassword() %>&loginDate=<%=operatorForm.getLoginDate() %>" target="mainFrame">报表审核</a>
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
								<a href="../rpt_net_cunzhen/report_mgr/report_again_mgr/report_again_mgr.jsp?userName=<%=operatorForm.getUserName() %>&pwd=<%=operatorForm.getPassword() %>&loginDate=<%=operatorForm.getLoginDate() %>" target="mainFrame">报表重报</a>
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
							if (yjh_bbcx_ybbcx ) {
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="../rpt_net_cunzhen/reportSearch/viewReportStatAction.do?userName=<%=operatorForm.getUserName() %>&pwd=<%=operatorForm.getPassword() %>&loginDate=<%=operatorForm.getLoginDate() %>" target="mainFrame">报表查询</a>
							</td>
						</tr>
						<%
						}
						%>
	
						<%
							if (yjh_bbcx_ylhcx) {
							//if(false){
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="../rpt_net_cunzhen/customViewAFReport.do?userName=<%=operatorForm.getUserName() %>&pwd=<%=operatorForm.getPassword() %>&loginDate=<%=operatorForm.getLoginDate() %>" target="mainFrame">灵活查询</a>
							</td>
						</tr>
						<%
						}
						%>
						
						<%if(false){ %>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="../rpt_net_cunzhen/viewCustomAFReportDetail.do?templateId=0101&versionId=1010&userName=<%=operatorForm.getUserName() %>&pwd=<%=operatorForm.getPassword() %>&loginDate=<%=operatorForm.getLoginDate() %>" target="mainFrame">数据查询</a>
							</td>
						</tr>
						<%} %>
						<%
							if (yjh_bbcx_ybbdc) {
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="../rpt_net_cunzhen/exportAFReport.do?userName=<%=operatorForm.getUserName() %>&pwd=<%=operatorForm.getPassword() %>&loginDate=<%=operatorForm.getLoginDate() %>" target="mainFrame">报表导出</a>
							</td>
						</tr>
						<%
						}
						%>
						
						<%
							if (yjh_bbcx_ybbrzcx) {
							//if(false){
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="../rpt_net_cunzhen/reportStatisticsCollect.do?reportFlg=<%=reportFlg %>&userName=<%=operatorForm.getUserName() %>&pwd=<%=operatorForm.getPassword() %>&loginDate=<%=operatorForm.getLoginDate() %>" target="mainFrame">报表统计</a>
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
							if (yjh_bbgl_ybbdygl ) {
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="../rpt_net_cunzhen/template/viewTemplate.do?userName=<%=operatorForm.getUserName() %>&pwd=<%=operatorForm.getPassword() %>&loginDate=<%=operatorForm.getLoginDate() %>" target="mainFrame">报表定义管理</a>
							</td>
						</tr>
						<%
						}
						%>
						
						<%
							//if (yjh_bbgl_ygbgxgl  ) {
								if(false){
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="../rpt_net_cunzhen/afreportmerger.do?userName=<%=operatorForm.getUserName() %>&pwd=<%=operatorForm.getPassword() %>&loginDate=<%=operatorForm.getLoginDate() %>" target="mainFrame">归并关系查看</a>
							</td>
						</tr>
						<%
						}
						%>
						<%
							//if (yjh_bbgl_ygbgxgl  ) {
								if(false){
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="javascript:menu_click('../rpt_net_cunzhen/baobiaofancha.do?userName=<%=operatorForm.getUserName() %>&pwd=<%=operatorForm.getPassword() %>&loginDate=<%=operatorForm.getLoginDate() %>')">模板反查维护</a>
							</td>
						</tr>
						<%
						}
						%>
						<%
							//if (yjh_bbgl_yjygxgl ) {
								if(false){
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="../rpt_net_cunzhen/afreportcheck.do?userName=<%=operatorForm.getUserName() %>&pwd=<%=operatorForm.getPassword() %>&loginDate=<%=operatorForm.getLoginDate() %>" target="mainFrame">校验维护查看</a>
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
			
			<!-- 机构管理 -->
			<tr>
				<td height="30"  background="image/button_blue.jpg">
					<a href="javascript:menu_pic_click('RJGGL')"> 
					<font class=b size="3"><b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;机构管理</b></font> </a>
				</td>
			</tr>
			<tr id="tr_RJGGL" style="display:none">
				<td>
					<table border="0" cellpadding="0" cellspacing="1" width="100%"
						align="center">
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="../rpt_net_cunzhen/system_mgr/OrgInfo/view.do?userName=<%=operatorForm.getUserName() %>&pwd=<%=operatorForm.getPassword() %>&loginDate=<%=operatorForm.getLoginDate() %>" target="mainFrame">机构管理</a>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<%
			}
			}
			%>
			</table></td>
		</tr>
	
		<%
			}
		%>	
		<!--村镇银行报表(End)--->
		
		
		<!--人行报表(Begin)--->
		<%
			if(true){
			if(rhbb){
		 %>
		<tr>
			<td>
				<img src="image/rpt_pboc.gif" onclick="changeReport(2)">
			</td>
		</tr>
		<%
			}
		 %>
		<tr id="tr_pboc">
		<td>
			<table width="100%">
			<%
	
				if(reportFlg.equals("2")){
					
				
			%>
			<%
				if( rh_bbcl_rcj  || rh_bbcl_rbbsc ||  rh_bbcl_rbbxz) {
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
							if(false || rh_bbcl_rcj) {
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="viewGatherReport.do" target="mainFrame">数据采集</a>
							</td>
						</tr>
						<%
						}
						%>
	
						<%
							if( rh_bbcl_rbbsc) {
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="afReportProduct.do" target="mainFrame">报表生成</a>
							</td>
						</tr>
						<%
						}
						%>
	
						<%
							if(false || rh_bbcl_rbbxz) {
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="report/reportDownloadList.do" target="mainFrame">报表下载</a>
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
				if(  rh_bbcl_rsc   || rh_bbcl_rbbfh || rh_bbcl_rbbjy  
				|| rh_bbcl_rbbhz  || rh_bbcl_rbbcb || rh_bbcx_rhbbdc 
				|| rh_bbcl_rrwdz || rh_bbcl_rrwgl || rh_bbcl_rddrw 
				|| rh_bbcl_rrbbsc ||rh_bbcl_rrcbrw
				) {
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
							if(Config.SYSTEM_SCHEMA_FLAG==0){
								if(rh_bbcl_rrwdz) {
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="viewNXDataReport.do" target="mainFrame">报表上传</a>
							</td>
						</tr>
						<%
						}
						%>
	
						<%
							if( rh_bbcl_rbbhz) {
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="viewCollectNX.do" target="mainFrame">报表汇总</a>
							</td>
						</tr>
						<%
						}
						%>
	
						<%
							if( rh_bbcl_rbbjy) {
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="dateQuary/manualCheckRepValidate.do" target="mainFrame">报表校验</a>
							</td>
						</tr>
						<%
						}
						%>
						
						<%
							if(true || rh_bbcl_rbbfh) {
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a	href="dateQuary/manualCheckRepNXRecheck.do" target="mainFrame">报表审核</a>
							</td>
						</tr>
						<%
						}
						%>
		
						<%
							if( rh_bbcl_rbbcb) {
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="gznx/report_again/report_again_lst.jsp" target="mainFrame">报表重报</a>
							</td>
						</tr>
						<%
						}
						}
						%>
	
						
						
						<%
							if( rh_bbcx_rhbbdc ) {
						%>					
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="exportRhAFReport.do?styleFlg=new" target="mainFrame">报表报送</a>
							</td>
						</tr>					
						<%
						}
						%>
						
						<%
							if(Config.SYSTEM_SCHEMA_FLAG==1){
								if(rh_bbcl_rrwdz){
							 %>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="/rpt_other/findAll_workTaskInfoAction.action?reportFlag=rhtx" target="mainFrame" >任务定制</a>
								</td>
							</tr>
							<%
								}
								if(rh_bbcl_rrwgl){
							 %>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="/rpt_other/workTaskInfoOrMoniAction!findWorkTaskInfoOrMoni.action?reportFlag=rhtx" target="mainFrame">任务管理</a>
								</td>
							</tr>
							<%
								}
							 %>
							 <%
								if(rh_bbcl_rrbbsc){
							 %>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="/rpt_other/pendingTaskAction!findBWSCTasks.action?reportFlag=rhtx" target="mainFrame">报文生成</a>
								</td>
							</tr>
							<%
								}
							 %>
							<%
								if(rh_bbcl_rddrw){
							 %>
							 <tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="/rpt_other/pendingTaskAction.action?reportFlag=rhtx" target="mainFrame">待办任务</a>
								</td>
							</tr>
							 <%} 
								if(rh_bbcl_rrcbrw){
							 %>
								<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="/rpt_other/pendingTaskAction!findAllRepTaskInfo.action?repFlag=1&reportFlag=rhtx" target="mainFrame">重报任务</a>
								</td>
							</tr>
							<%
								}}
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
				if( rh_bbcx_rbbcx     
				|| rh_bbcx_rlhcx || rh_bbcx_rbbdc || rh_bbcx_rbbrzcx || yjh_bbcx_sjcx) {
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
							if( rh_bbcx_rbbcx) {
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="reportSearch/viewReportStatNX.do" target="mainFrame">报表查询</a>
							</td>
						</tr>
						<%
						}
						%>
						<%
							if(yjh_bbcx_hjcx_f){
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="viewDataTraceAction.do" target="mainFrame">痕迹查询</a>
							</td>
						</tr>
						<%
							}
						%>
	                    <%
							if(rmyh_bbcx_sjcx) {
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="searchPBOCDataInitAction.do" target="mainFrame">数据查询</a>
							</td>
						</tr>
						<%
						}
						%>
						
						<%
							if( rh_bbcx_rlhcx) {
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="customViewAFReport.do" target="mainFrame">灵活查询</a>
							</td>
						</tr>
						<%
						}
						%>
						
						<%
							if( rh_bbcx_rbbdc ) {
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="exportAFReport.do" target="mainFrame">报表导出</a>
							</td>
						</tr>
						<%
						}
						%>
											
						<%
							if(rh_bbcx_rbbtz ) {
							//if(false){
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="reportStatisticsCollect.do?reportFlg=<%=reportFlg %>" target="mainFrame">报表统计</a>
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
				if( rh_bbgl_rbbdygl   
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
							if( rh_bbgl_rbbdygl) {
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="afreportDefine.do" target="mainFrame">报表定义管理</a>
							</td>
						</tr>
						<%
						}
						%>
						 
						<%
							if( rh_bbgl_rgbgxgl) {
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="afreportmerger.do" target="mainFrame">归并关系管理</a>
							</td>
						</tr>
						<%
						}
						%>
						
						<%
							if( rh_bbgl_rjygxgl) {
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="afreportcheck.do" target="mainFrame">校验维护管理</a>
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
			<tr>
					<%
						}
						if(rh_xxgl_rydxx || rh_xxgl_rxxgl){
					%>
						<td height="30"  background="image/button_blue.jpg">
							<a href="javascript:menu_pic_click('RXXCL')"> 
							<font class=b size="3"><b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;信息处理</b></font> </a>
						</td>
			</tr>
				<tr id="tr_RXXCL" style="display:none">
						<td>
							<!--信息处理(Begin)--->
							<table border="0" cellpadding="0" cellspacing="1" width="100%"
							align="center">
							<%
							if (rh_xxgl_rydxx) {
							%>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="/rpt_other/tmsginfo!findMsgByOwn.action" target="mainFrame">已读消息</a>
								</td>
							</tr>
							<%} if (rh_xxgl_rxxgl) {%>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="/rpt_other/tmsginfo!findOwnPublishMsg.action" target="mainFrame">消息管理</a>
								</td>
							</tr>
							<%} %>
							</table>
							<!--信息处理(End)--->
						</td>
					</tr>
			<%
				}
				}
			%>
			</table>
		</td>
		</tr>
		
		<%
			}
		%>
		<!--人行报表(End)--->
		
		<!--其他报表(Begin)--->
		<%
			if(qtbb){
		 %>
		<tr>
			<td>
				<img src="image/rpt_other.gif" onclick="changeReport(3)">
			</td>
		</tr>
		<%
			}
		 %>
		<tr id="other">
			<td>
				<table width="100%">
				<%
					if(reportFlg.equals("3")){
					
					if( qt_bbcl_qbcj  || qt_bbcl_qbbsc) {
				%>
				<tr>
					<td height="30" width="155" background="image/button_blue.jpg">
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
								if( qt_bbcl_qbbsc) {
							%>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="afReportProduct.do?orgId=<%=operator.getOrgId() %>" target="mainFrame">报表生成</a>	
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
					if(  qt_bbcl_qsc || qt_bbcl_qbbfh || qt_bbcl_qbbjy  
					|| qt_bbcl_qbbhz  || qt_bbcl_qbbcb || qt_bbcl_qbcj
					|| qt_bbcl_qrwdz || qt_bbcl_qrwgl || qt_bbcl_qddrw 
					|| qt_bbcl_qrbbsc || qt_bbcl_qrcbrw ) {
				%>
				<tr>
					<td height="30" background="image/button_blue.jpg">
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
								if(Config.SYSTEM_SCHEMA_FLAG==0){
								if( qt_bbcl_qsc ) {
							%>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="viewNXDataReport.do?orgId=<%=operator.getOrgId() %>" target="mainFrame">报表填报</a>
								</td>
							</tr>
							<%
							}
							%>
		
							<%
								if( qt_bbcl_qbbjy) {
							%>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="dateQuary/manualCheckRepValidate.do" target="mainFrame">报表校验</a>
								</td>
							</tr>
							<%
							}
							%>
							
							<%
								if( qt_bbcl_qbbhz) {
							%>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="viewCollectNX.do?orgId=<%=operator.getOrgId() %>" target="mainFrame">报表汇总</a>
								</td>
							</tr>
							<%
							}
							%>
							
							<%
								if( qt_bbcl_qbbfh) {
							%>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="dateQuary/manualCheckRepNXRecheck.do" target="mainFrame">报表审核</a>
								</td>
							</tr>
							<%
							}
							%>
			 
							<%
								if( qt_bbcl_qbbcb) {
							%>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="gznx/report_again/report_again_lst.jsp" target="mainFrame">报表重报</a>
								</td>
							</tr>
							<%
							}
							}
							%>
		
							<%
								if(Config.SYSTEM_SCHEMA_FLAG==1){
									if(qt_bbcl_qrwdz){
							 %>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="/rpt_other/findAll_workTaskInfoAction.action?reportFlag=qttx" target="mainFrame" >任务定制</a>
								</td>
							</tr>
							<%
								}
								if(qt_bbcl_qrwgl){
							 %>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="/rpt_other/workTaskInfoOrMoniAction!findWorkTaskInfoOrMoni.action?reportFlag=qttx" target="mainFrame">任务管理</a>
								</td>
							</tr>
							<%
								}
								if(qt_bbcl_qrbbsc){
							 %>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="/rpt_other/pendingTaskAction!findBWSCTasks.action?reportFlag=qttx" target="mainFrame">报文生成</a>
								</td>
							</tr>
							<%
								}
								if(qt_bbcl_qddrw){
							 %>
							 <tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="/rpt_other/pendingTaskAction.action?reportFlag=qttx" target="mainFrame">待办任务</a>
								</td>
							</tr>
							 <%} 
								if(qt_bbcl_qrcbrw ){
							 %>
								<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="/rpt_other/pendingTaskAction!findAllRepTaskInfo.action?repFlag=1&reportFlag=qttx" target="mainFrame">重报任务</a>
								</td>
							</tr>
							<%
								}
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
					if( qt_bbcx_qbbcx    
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
								if( qt_bbcx_qbbcx) {
							%>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="reportSearch/viewReportStatNX.do?orgId=<%=operator.getOrgId() %>" target="mainFrame">报表查询</a>
								</td>
							</tr>
							<%
							}
							%>
		
		
							<%
								//if( qt_bbcx_qlhcx) {
								if(false){
							%>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="customViewAFReport.do" target="mainFrame">灵活查询</a>
								</td>
							</tr>
							<%
							}
							%>
							
							<%
								if( qt_bbcx_qfxbcx ) {
							%>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="exportAFReport.do" target="mainFrame">报表导出</a>
								</td>
							</tr>
							<%
							}
							if(yjh_bbcx_hjcx){
						    %>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="viewDataTraceAction.do" target="mainFrame">痕迹查询</a>
							</td>
						</tr>
							<%
								}
								if( qt_bbcx_qbbrzcx) {
							//	if(false){
							%>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="reportStatisticsCollect.do?reportFlg=<%=reportFlg %>" target="mainFrame">报表统计</a>
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
					if( qt_bbgl_qbbdygl  
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
								if( qt_bbgl_qbbdygl) {
							%>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="afreportDefine.do" target="mainFrame">报表定义管理</a>
								</td>
							</tr>
							<%
							}
							%>
							 
							<%
								if( qt_bbgl_qgbgxgl&&false) {
							%>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="afreportmerger.do" target="mainFrame">归并关系查看</a>
								</td>
							</tr>
							<%
							}
							%>
		
							<%
								if( qt_bbgl_qjygxgl&&false) {
							%>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="afreportcheck.do" target="mainFrame">校验维护查看</a>
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
				
		 		<tr>
					<%
						}
						if(qt_xxgl_qydxx || qt_xxgl_qxxgl){
					%>
						<td height="30"  background="image/button_blue.jpg">
							<a href="javascript:menu_pic_click('QXXCL')"> 
							<font class=b size="3"><b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;信息处理</b></font> </a>
						</td>
			</tr>
				<tr id="tr_QXXCL" style="display:none">
						<td>
							<!--信息处理(Begin)--->
							<table border="0" cellpadding="0" cellspacing="1" width="100%"
							align="center">
							<%
							if (qt_xxgl_qydxx) {
							%>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="/rpt_other/tmsginfo!findMsgByOwn.action" target="mainFrame">已读消息</a>
								</td>
							</tr>
							<%} if (qt_xxgl_qxxgl) {%>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="/rpt_other/tmsginfo!findOwnPublishMsg.action" target="mainFrame">消息管理</a>
								</td>
							</tr>
							<%} %>
							</table>
							<!--信息处理(End)--->
						</td>
					</tr>
			<%
				}
				}
			%>
				</table>
			</td>
		</tr>
		<!--其他报表(end)--->
		<%
			if( sjbl) {
			//if(false){
		%>		
		<tr>
			<td height="35" >
			<img src="image/rpt_gat.gif" onclick="menu_pic_click('SJBL');changeReport_BL(3)">	
			<%--
				<a href="javascript:menu_pic_click('SJBL')">
				<font class=b size="3"><b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;数据补录</b></font>
				</a>
			--%>	
			</td>
		</tr>
		<%
			}
		%>
		<tr id="tr_SJBL" style="display:none">
			<td>
				<!--报表查询(Begin)--->
				<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
					<%
						if( bb_sjcj) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="viewGatherReport.do?orgId=<%=operator.getOrgId() %>" target="mainFrame">数据补录</a>
						</td>
					</tr>
					<%
					}
					%>
					
					<tr style="display:none">
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="searchGatherReportAction.do?orgId=<%=operator.getOrgId() %>" target="mainFrame">统计查询</a>
						</td>
					</tr>
					<tr style="display:none">
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="bstj/bstj_overview.jsp" target="mainFrame">报送统计</a>
						</td>
					</tr>
					
					<%
						if( bb_bbsc) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="report/reportDownloadList.do?orgId=<%=operator.getOrgId() %>" target="mainFrame">模板下载</a>	
						</td>
					</tr>
					<%
					}
					%>
					
					<%
						if( bb_bbcx) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="reportSearch/viewReportGatherStat.do?orgId=<%=operator.getOrgId() %>" target="mainFrame">报表查询</a>
						</td>
					</tr>
					<%
					}
					%>
					
					<tr>
						<td height="5"></td>
					</tr>
				</table>
				<!--报表查询(End)-->
			</td>
		</tr>
		
		
		
		
		<tr>
			<td height="35" >
			<img src="image/rpt_sys.gif" onclick="menu_pic_click('XTGL')">
			</td>
		</tr>
		<tr id="tr_XTGL" style="display:none">
			<td>
				<!--系统管理(Begin)--->
				<table border="0" cellpadding="0" cellspacing="1" width="100%"
					align="center">

					<%
						if( xtgl_jggl ) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="system_mgr/OrgInfo/view.do" target="mainFrame">机构管理</a>
						</td>
					</tr>
					<%
					}
					%>

					<%
					if( operator.isExitsThisUrl("popedom_mgr/viewDepartment.do")) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="popedom_mgr/viewDepartment.do" target="mainFrame">部门管理</a>
						</td>
					</tr>
					<%
					}
					%>

					<%
						if( xtgl_yhzgl&&Config.ROLE_GROUP_UNION_FLAG!=1) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="popedom_mgr/viewMUserGrp.do" target="mainFrame">用户组管理</a>
						</td>
					</tr>		
					<%
					}
					%>

					
					<%
						if( xtgl_jsgl ) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="popedom_mgr/viewRole.do" target="mainFrame">角色管理</a>
						</td>
					</tr>
					<%
					}
					%>

					<%
						if( xtgl_thgl) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="popedom_mgr/viewOperator.do" target="mainFrame">用户管理</a>
						</td>
					</tr>
					<%
					}
					%>

					<%
						if(xtgl_zxyhgl) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="popedom_mgr/viewOnlineOperator.do?m=searchOnlineUser" target="mainFrame">在线用户管理</a>
						</td>
					</tr>
					<%
					}
					%>

					<%
						if( operator.isExitsThisUrl("system_mgr/viewResetPwd.do") && !Config.PORTAL) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">		
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="system_mgr/viewResetPwd.do" target="mainFrame">重置密码</a>
						</td>
					</tr>
					<%
					}
					%>

	
					<%
						if( xtgl_zdgl) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="system_mgr/viewCodeLib.do" target="mainFrame">字典管理</a>
						</td>
					</tr>		
					<%
					}
					%>

				
				 <!-- 	
					<%
						if( xtgl_zxyh) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="system_mgr/viewOnlineUser.do" target="mainFrame">在线用户</a>
						</td>
					</tr>
					<%
					}
					%>
				 -->
					
					<%
						//if( xtgl_hlgl) {
						if(false){
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click
								('sysManage/exchangeRate/exchangeRateMgr.do?method=viewExchangeRate" target="mainFrame">汇率管理</a>
						</td>
					</tr>
					<%
					}
					%>

					<%
						//if( xtgl_hlgl) {
						if(false){
					%>
					<tr>
						<td background="image/button_q.gif" height="25">			
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="sysManage/specialValMgr.do?method=viewSpecVal" target="mainFrame">特殊校验管理</a>
						</td>
					</tr>			
					<%
					}
					%>

					<%
						if( xtgl_rzgl) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="system_mgr/viewLogIn.do" target="mainFrame">日志管理</a>
						</td>
					</tr>					
					<%
					}
					%>
					<%
						if(xtgl_ysgxgl){
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="<%=request.getContextPath() %>/searchVorgRelInitAction.do" target="mainFrame">映射关系管理</a>
						</td>
					</tr>
					<%
						}
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="system_mgr/modifyPwd.jsp" target="mainFrame">修改密码</a>
						</td>
					</tr>
					
					<tr>
							<td height="30"  background="image/button_blue.jpg">
								<a href="javascript:menu_pic_click1('XXCL')"> 
								<font class=b size="3"><b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;消息处理</b></font> </a>
							</td>
					</tr>
					<tr id="tr_XXCL" style="display:none">
					<td>
						<!--报表管理(Begin)--->
						<table border="0" cellpadding="0" cellspacing="1" width="100%"
							align="center">
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="/rpt_other/tmsginfo!findMsgByOwn.action" target="mainFrame">已读消息</a>
								</td>
							</tr>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="/rpt_other/tmsginfo!findOwnPublishMsg.action" target="mainFrame">消息管理</a>
								</td>
							</tr>
						
						</table>
					</td>
					</tr>
					
					
				</table>
				<!--系统管理(End)-->
			</td>
		</tr>

		<%
		// }
			//if( fxjc) {
			if(false){
		%>
		<tr>
			<td height="35" >
			<img src="image/rpt_fit.gif" onclick="toUrl()">	
			</td>
		</tr>

		
		<%
			}
			
			if( etljk) {
			//	if( false) {
		%>
		<tr>
			<td>
				<img src="image/etl.gif" onclick="menu_pic_click('ETL')">
			</td>
		</tr>
		<tr id="etl">
			<td>
				<table width="100%">
				
				
				<tr id="tr_ETL" style="display:none">
					<td>
						<!--报表处理(Begin)--->
						<table border="0" cellpadding="0" cellspacing="1" width="100%"
							align="center">
		
							
		
							<%
								if(etl_rwpz){
							 %>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="../../FITETL/sjcq/rwpz/index_task_edit.jsp" target="mainFrame" >任务配置</a>
								</td>
							</tr>
							<%
								}
								if(etl_rwdd){
							 %>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="../../FITETL/findEtlTaskInfoListByRWJK.action" target="mainFrame">任务调度</a>
								</td>
							</tr>
							<%
								}
								if(etl_rwjk){
							 %>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="../../FITETL/sjcq/rwjk/rwjk_index.jsp" target="mainFrame">任务监控</a>
								</td>
							</tr>
							<%
								}
								if(etl_fwqpz){
							 %>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="../../FITETL/etlFileServerInfo!initMethod.action" target="mainFrame">服务器配置</a>
								</td>
							</tr>
							<%
								}
							 %>
							 <%
							 	if(operator.getUserName().equals("admin")){		
							 %>
							 <tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="../../FITETL/getsql/index.jsp" target="mainFrame">部署管理</a>
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
				</table>
			</td>
		</tr>
		<%
			}
		%>
		<!--任务管理（start add by wmm）-->
		<%
		
			
			if (taskgl&&false) {
			//	if (false) {
		%>
		<tr>
			<td>
				<img src="image/task_mgr.jpg" onclick="menu_pic_click('TASK')">
			</td>
		</tr>
		<tr id="task">
			<td>
				<table width="100%">
				
				
				<tr id="tr_TASK" style="display:none">
					<td>
						
						<table border="0" cellpadding="0" cellspacing="1" width="100%"
							align="center">
		
							
		
							<%
								if(task_rwdz){
							 %>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="/rpt_other/findAll_workTaskInfoAction.action" target="mainFrame" >任务定制</a>
								</td>
							</tr>
							<%
								}
								if(task_ddrw){
							 %>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="/rpt_other/pendingTaskAction.action" target="mainFrame">待办任务</a>
								</td>
							</tr>
							<%
								}
								if(task_bwsc){
							 %>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="/rpt_other/pendingTaskAction!findBWSCTasks.action" target="mainFrame">报文生成</a>
								</td>
							</tr>
							<%
								}
								if(task_rwgl){
							 %>
							 <tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="/rpt_other/workTaskInfoOrMoniAction!findWorkTaskInfoOrMoni.action" target="mainFrame">任务管理</a>
								</td>
							</tr>
							 <%} 
								if(task_rwcb){
							 %>
								<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="/rpt_other/pendingTaskAction!findAllRepTaskInfo.action?repFlag=1" target="mainFrame">重报任务</a>
								</td>
							</tr>
							<%
								}
							%>
						
							<tr>
								<td height="5"></td>
							</tr>
						</table>
						
					</td>
				</tr>
				</table>
			</td>
		</tr>
		<%
			}
		%>

		
	<!--任务管理（end add by wmm）-->	
		
	</table>

</body>
</html:html>


