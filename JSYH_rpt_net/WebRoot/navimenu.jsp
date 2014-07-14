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
		
		/** ����ѡ�б�־ **/
		String reportFlg = "0";
		
		if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
			reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
		}
		
		boolean yjhbb = false;
		boolean rhbb = false;
		boolean qtbb = false;
		boolean sjbl = false;
		boolean etljk = false;
		
		boolean ldsy_jyyl = operator.isExitsThisUrl("JYZKYL"); //��Ӫ״��һ��
		boolean xxgg_xxfb = operator.isExitsThisUrl("XXFB"); //��Ϣ����
		boolean xxgg_xxck = operator.isExitsThisUrl("XXCK"); //��Ϣ�鿴
		boolean xtgl_jggl = operator.isExitsThisUrl("JGGL"); //��������
		boolean xtgl_thgl = operator.isExitsThisUrl("YHGL"); //�û�����
		boolean xtgl_jsgl = operator.isExitsThisUrl("JSGL"); //��ɫ����
		boolean xtgl_yhzgl = operator.isExitsThisUrl("YHZGL"); //�û������
		boolean xtgl_zxyh = operator.isExitsThisUrl("ZXYH"); //�����û�
		boolean xtgl_zdgl = operator.isExitsThisUrl("ZDGL"); //�ֵ����
		boolean xtgl_rzgl = operator.isExitsThisUrl("RZGL"); //��־����
		boolean yjh_bbcl_ybbsc = operator.isExitsThisUrl("YBBSC"); //��������
		boolean yjh_bbcl_ybbxz = operator.isExitsThisUrl("YBBXZ"); //��������
		boolean yjh_bbcl_sc = operator.isExitsThisUrl("YSC"); //�����ϴ�
		boolean yjh_bbcl_ybbfh = operator.isExitsThisUrl("YBBFH"); //������
		boolean yjh_bbcl_ybbjy = operator.isExitsThisUrl("YBBJY"); //����У��
		boolean yjh_bbcl_ybbhz = operator.isExitsThisUrl("YBBHZ"); //�������
		boolean yjh_bbcl_ybbsq = operator.isExitsThisUrl("YBBSQ"); //������ǩ
		boolean yjh_bbcl_ybbcb = operator.isExitsThisUrl("YBBCB"); //�����ر�
		boolean yjh_bbcx_ybbcx = operator.isExitsThisUrl("YBBCX"); //�����ѯ
		boolean yjh_bbcx_ylhcx = operator.isExitsThisUrl("YLHCX"); //����ѯ
		boolean yjh_bbcx_ybbdc = operator.isExitsThisUrl("YBBDC"); //������
		boolean yjh_bbcx_ybbrzcx = operator.isExitsThisUrl("YBBRZCX"); //������־��ѯ
		boolean yjh_bbgl_ybbdygl = operator.isExitsThisUrl("YBBDYGL"); //���������
		boolean yjh_bbgl_ygbgxgl  = operator.isExitsThisUrl("YGBGXGL"); //�鲢��ϵ����
		boolean yjh_bbgl_yjygxgl  = operator.isExitsThisUrl("YJYGXGL"); //У���ϵ����
		boolean yjh_bbgl_ybbsxgl  = operator.isExitsThisUrl("YBBSXGL"); //����ʱ�޹���
		boolean rh_bbcl_rbbsc  = operator.isExitsThisUrl("RBBSC"); //��������
		boolean rh_bbcl_rbbxz  = operator.isExitsThisUrl("RBBXZ"); //��������
		boolean rh_bbcl_rsc  =  operator.isExitsThisUrl("RSC"); //�����ϴ�
		boolean rh_bbcl_rbbfh  = operator.isExitsThisUrl("RBBFH"); //������
		boolean rh_bbcl_rbbjy  =  operator.isExitsThisUrl("RBBJY"); //����У��
		boolean rh_bbcl_rbbhz  = operator.isExitsThisUrl("RBBHZ"); //�������
		boolean rh_bbcl_rbbcb  = operator.isExitsThisUrl("RBBCB"); //�����ر�
		boolean rh_bbcx_rbbcx  =  operator.isExitsThisUrl("RBBCX"); //�����ѯ
		boolean rh_bbcx_rlhcx  =  operator.isExitsThisUrl("RLHCX"); //����ѯ
		boolean rh_bbcx_rbbdc  = operator.isExitsThisUrl("RBBDC"); //������
		boolean rh_bbcx_rhbbdc = operator.isExitsThisUrl("RHBBDC");//���и�ʽ����
		boolean rh_bbcx_rbbrzcx  = operator.isExitsThisUrl("RBBRZCX"); //������־��ѯ
		boolean rh_bbgl_rbbdygl  =  operator.isExitsThisUrl("RBBDYGL"); //���������
		boolean rh_bbgl_rgbgxgl  =  operator.isExitsThisUrl("RGBGXGL"); //�鲢��ϵ����
		boolean rh_bbgl_rjygxgl  = operator.isExitsThisUrl("RJYGXGL"); //У���ϵ����
		boolean rh_bbgl_rbbsxgl  = operator.isExitsThisUrl("RBBSXGL"); //����ʱ�޹���
		boolean qt_bbcl_qbbsc  =  operator.isExitsThisUrl("QBBSC"); //��������
		boolean qt_bbcl_qbbxz  =  operator.isExitsThisUrl("QBBXZ"); //��������
		boolean qt_bbcl_qsc  =  operator.isExitsThisUrl("QSC"); //�����ϴ�
		boolean qt_bbcl_qbbfh  = operator.isExitsThisUrl("QBBFH"); //������
		boolean qt_bbcl_qbbjy  =  operator.isExitsThisUrl("QBBJY"); //����У��
		boolean qt_bbcl_qbbhz  =  operator.isExitsThisUrl("QBBHZ"); //�������
		boolean qt_bbcl_qbbcb  =  operator.isExitsThisUrl("QBBCB"); //�����ر�
		boolean qt_bbcx_qbbcx  =  operator.isExitsThisUrl("QBBCX"); //�����ѯ
		boolean qt_bbcx_qlhcx  =  operator.isExitsThisUrl("QLHCX"); //����ѯ
		boolean qt_bbcx_qfxbcx  =  operator.isExitsThisUrl("QFXBCX"); //�������ѯ
		boolean qt_bbcx_qbbrzcx  =  operator.isExitsThisUrl("QBBRZCX"); //������־��ѯ
		boolean qt_bbcx_day_report  =  operator.isExitsThisUrl("QRBBCX"); //�ձ����ѯ
		boolean qt_bbgl_qbbdygl  =  operator.isExitsThisUrl("QBBDYGL"); //���������
		boolean qt_bbgl_qgbgxgl  =  operator.isExitsThisUrl("QGBGXGL"); //�鲢��ϵ����
		boolean qt_bbgl_qjygxgl  =  operator.isExitsThisUrl("QJYGXGL"); //У���ϵ����
		boolean qt_bbgl_qbbsxgl  =  operator.isExitsThisUrl("QBBSXGL"); //����ʱ�޹���
		boolean rh_bbcl_rcj = operator.isExitsThisUrl("RBBCJ"); //���б������ݲɼ�
		boolean qt_bbcl_qbcj = operator.isExitsThisUrl("QBBCJ"); //�����������ݲɼ�
		boolean xtgl_hlgl = operator.isExitsThisUrl("HLGL");//���ʹ���
		boolean xtgl_xtcssd = operator.isExitsThisUrl("XTCSSD");//���ʹ���
		boolean fxjc  =  operator.isExitsThisUrl("FXJC"); //���ռ��
		
		boolean bb_sjcj = operator.isExitsThisUrl("BBBCJ"); //���ݲɼ�
		boolean bb_bbsc = operator.isExitsThisUrl("BMBXZ"); //ģ������
		boolean bb_bbcx = operator.isExitsThisUrl("BBBCK"); //����鿴
		
		/**ETL���*/
		boolean etl_rwpz=operator.isExitsThisUrl("RWPZ");//��������
		boolean etl_rwdd=operator.isExitsThisUrl("RWDD");//�������
		boolean etl_rwjk=operator.isExitsThisUrl("RWJK");//������
		boolean etl_fwqpz=operator.isExitsThisUrl("FWQPZ");//����������
		
		boolean etl_bsgl = operator.isExitsThisUrl("BSGL");//�������
		//���ݲ�ѯ
		boolean yjh_bbcx_sjcx = operator.isExitsThisUrl("YSJCX");
		//�ۼ���ѯ
		boolean yjh_bbcx_hjcx = operator.isExitsThisUrl("YHJCX");
		//ӳ���ϵ����
		boolean xtgl_ysgxgl = operator.isExitsThisUrl("YSGXGL");
		
		
		
		if(yjh_bbcl_ybbsc || yjh_bbcl_ybbxz || yjh_bbcl_sc || yjh_bbcl_ybbfh || yjh_bbcl_ybbjy 
			|| yjh_bbcl_ybbhz || yjh_bbcl_ybbsq || yjh_bbcl_ybbcb || yjh_bbcx_ybbcx || yjh_bbcx_ylhcx ||yjh_bbcx_hjcx || yjh_bbcx_sjcx
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
			|| qt_bbcx_qbbrzcx || qt_bbcx_day_report || qt_bbgl_qbbdygl   || qt_bbgl_qgbgxgl   || qt_bbgl_qjygxgl 
			|| qt_bbcl_qbcj  ){
			qtbb =true;
		}
		if(bb_sjcj   || bb_bbsc   || bb_bbcx ){
			sjbl =true;
		}
		if(etl_rwpz || etl_rwdd || etl_rwjk || etl_fwqpz || etl_bsgl)
			etljk=true;
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
			var scriptqt_bbcx_day_report=<%=qt_bbcx_day_report%>;
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

			var scriptxtgl_ysgxgl = <%=xtgl_ysgxgl%>//ӳ���ϵ����
			//���ݲ�ѯ
			var scriptyjh_bbcx_sjcx = <%=yjh_bbcx_sjcx%>;
			//�ۼ���ѯ
			var scriptyjh_bbcx_hjcx = <%=yjh_bbcx_hjcx%> 
			
			/**
			 * �쵼��ҳ 
			 */
			var LDSY="LDSY";
			/**
			 * ��������ݲɼ� 
			 */
			var YSJCJ="YSJCJ";
			/**
			 * ����ᱨ���� 
			 */
			var YBBCL="YBBCL";
			/**
			 * ����ᱨ���ѯ
			 */
			var YBBCX="YBBCX";
			/**
			 * ����ᱨ�����
			 */
			var YBBGL="YBBGL";
			/**
			 * �������ݲɼ� 
			 */
			var RSJCJ="RSJCJ";
			/**
			 * ���б����� 
			 */
			var RBBCL="RBBCL";
			/**
			 * ���б����ѯ 
			 */
			var RBBCX="RBBCX";
			/**
			 * ���б������
			 */
			var RBBGL="RBBGL";
			/**
			 * �������ݲɼ� 
			 */
			var QSJCJ="QSJCJ";
			/**
			 * ���������� 
			 */
			var QBBCL="QBBCL";
			/**
			 * ���������ѯ
			 */
			var QBBCX="QBBCX";
			/**
			 * �����������
			 */
			var QBBGL="QBBGL";
			
			/**
			 * ϵͳ����
			 */
			var XTGL="XTGL";
			
			/**
			 * ��Ϣ����
			 */
			var XXGG="XXGG";

			/**
			 * ���ݲ�¼
			 */
			var SJBL="SJBL";

			/**
			 * ����˵�ͼƬ�¼�
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
			 * �����Ӳ˵�
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
				
				if(menu!=YSJCJ && (scriptReportFlg=="1") && (scriptyjh_bbcl_ybbsc == true || scriptyjh_bbcl_ybbxz == true ))
					eval("document.getElementById('tr_" + YSJCJ + "').style.display='none'");
					
				if(menu!=YBBCL && (scriptReportFlg=="1") && (scriptyjh_bbcl_sc == true || scriptyjh_bbcl_ybbfh == true || 
				scriptyjh_bbcl_ybbjy == true || scriptyjh_bbcl_ybbhz == true || 
				scriptyjh_bbcl_ybbsq == true || scriptyjh_bbcl_ybbcb == true))
					eval("document.getElementById('tr_" + YBBCL + "').style.display='none'");
					
				if(menu!=YBBCX && (scriptReportFlg=="1") && (scriptyjh_bbcx_ybbcx == true || scriptyjh_bbcx_ylhcx == true || 
				scriptyjh_bbcx_ybbdc == true || scriptyjh_bbcx_ybbrzcx == true
				|| scriptyjh_bbcx_sjcx == true || scriptyjh_bbcx_hjcx == true))
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
				scriptqt_bbcx_qfxbcx == true || scriptqt_bbcx_qbbrzcx == true|| scriptqt_bbcx_day_report == true))
					eval("document.getElementById('tr_" + QBBCX + "').style.display='none'");
	
				if(menu!=QBBGL && (scriptReportFlg=="3") && (scriptqt_bbgl_qbbdygl == true || scriptqt_bbgl_qgbgxgl == true || 
				scriptqt_bbgl_qjygxgl == true || scriptqt_bbgl_qbbsxgl == true))
					eval("document.getElementById('tr_" + QBBGL + "').style.display='none'");		

				if(menu!=QBBGL && (scriptReportFlg=="3") && (scriptqt_bbgl_qbbdygl == true || scriptqt_bbgl_qgbgxgl == true || 
						scriptqt_bbgl_qjygxgl == true || scriptqt_bbgl_qbbsxgl == true))
							eval("document.getElementById('tr_" + QBBGL + "').style.display='none'");
							
							
		

				if(menu==XTGL){
						eval("document.getElementById('cbrc').style.display='none'");
						eval("document.getElementById('pboc').style.display='none'");
						eval("document.getElementById('other').style.display='none'");
						eval("document.getElementById('tr_SJBL').style.display='none'");						
				}

				if(menu==SJBL){
					eval("document.getElementById('cbrc').style.display='none'");
					eval("document.getElementById('pboc').style.display='none'");
					eval("document.getElementById('other').style.display='none'");
					eval("document.getElementById('tr_XTGL').style.display='none'");
				}
			}
			
		    /**
		     * ����˵��¼�
		     */
		    function menu_click(_url){
		    	if(_url=="") return;
		    	window.parent.mainFrame.contents.location.assign(_url);
	//			window.open(_url);
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

			//����ѡ������
			function switchBarOn()
			{
				if (document.getElementById("div1"))
				{
				 document.all("div1").style.display="";
				}
			}


			//����ѡ������
			function switchBarOff()
			{
				if (document.getElementById("div1"))
				{
				 document.all("div1").style.display="none";
				}
			}
			/**
			 *ת������ϵͳ
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
		<!--����ᱨ��(Begin)--->
		<tr id="cbrc">
		<td>
			<table width="100%">
			<%
				//}
				if(reportFlg.equals("1")){
	
				if (yjh_bbcl_ybbsc ) {
			%>
			<tr>
				<td height="30" width="155"  background="image/button_blue.jpg">
					<a href="javascript:menu_pic_click('YSJCJ')"> 
				<font class=b size="3"><b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;���ݲɼ�</b></font> </a>
				</td>
			</tr>
			<tr id="tr_YSJCJ" style="display:none">
				<td>
					<!--������(Begin)--->
					<table border="0" cellpadding="0" cellspacing="1" width="100%"
						align="center">
	
						<%
							if (yjh_bbcl_ybbsc) {
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="afReportProduct.do" target="mainFrame">��������</a>	
							</td>
						</tr>
						<%
						}
						%>						
						
						
	
						<tr>
							<td height="5"></td>
						</tr>
					</table>
					<!--�������(End)-->
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
				<font class=b size="3"><b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;������</b></font> </a>
				</td>
			</tr>
	
			<tr id="tr_YBBCL" style="display:none">
				<td>
					<!--������(Begin)--->
					<table border="0" cellpadding="0" cellspacing="1" width="100%"
						align="center">
						
						<%
							if (yjh_bbcl_sc) {
						%>
						<tr>
							<td  background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="viewDataReport.do" target="mainFrame">�����</a>
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
								<a href="dateQuary/manualCheckRepSHJY.do" target="mainFrame">����У��</a>
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
								<a href="viewCollectData.do?orgId=<%=operator.getOrgId() %>" target="mainFrame">�������</a>
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
								<a	href="javascript:menu_click('dateQuary/manualCheckRepRecheck.do')">������</a>
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
								<a href="dateQuary/manualCheckRep.do" target="mainFrame">�������</a>
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
								<a href="selForseReportAgain.do" target="mainFrame">�����ر�</a>
							</td>
						</tr>
						<%
						}
						%>
						
						<tr>
							<td height="5"></td>
						</tr>
					</table>
					<!--�������(End)-->
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
					<font class=b size="3"><b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;�����ѯ</b></font> </a>
				</td>
			</tr>
			
			<tr id="tr_YBBCX" style="display:none">
				<td>
					<!--�����ѯ(Begin)--->
					<table border="0" cellpadding="0" cellspacing="1" width="100%"
						align="center">
	
						<%
							if (yjh_bbcx_ybbcx ) {
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="reportSearch/viewReportStatAction.do" target="mainFrame">�����ѯ</a>
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
								<a href="customViewAFReport.do" target="mainFrame">����ѯ</a>
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
								<a href="searchDataInitAction.do" target="mainFrame">���ݲ�ѯ</a>
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
								<a href="viewDataTraceAction.do" target="mainFrame">�ۼ���ѯ</a>
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
								<a href="exportAFReport.do" target="mainFrame">������</a>
							</td>
						</tr>
						<%
						}
						%>
						
						<%
							//if (yjh_bbcx_ybbrzcx) {
							if(false){
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="reportStatisticsCollect.do?reportFlg=<%=reportFlg %>" target="mainFrame">����ͳ��</a>
							</td>
						</tr>
						<%
						}
						%>
									
						<tr>
							<td height="5"></td>
						</tr>
					</table>
					<!--�������(End)-->
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
					<font class=b size="3"><b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;�������</b></font> </a>
				</td>
			</tr>
			
			<tr id="tr_YBBGL" style="display:none">
				<td>
					<!--�������(Begin)--->
					<table border="0" cellpadding="0" cellspacing="1" width="100%"
						align="center">
		
						<%
							if (yjh_bbgl_ybbdygl ) {
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="template/viewTemplate.do" target="mainFrame">���������</a>
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
								<a href="afreportmerger.do" target="mainFrame">�鲢��ϵ�鿴</a>
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
								<a href="baobiaofancha.do" target="mainFrame">ģ�巴��ά��</a>
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
								<a href="afreportcheck.do" target="mainFrame">У��ά���鿴</a>
							</td>
						</tr>					
						<%
						}
						%>
	
						<tr>
							<td height="5"></td>
						</tr>
					</table>
					<!--�������(End)-->
				</td>
			</tr>
			<%
				}
				}
			%>
			</table>	
		</td>
	    </tr>
		
		<!--����ᱨ��(End)--->
		
		<%
		 	if(false){
		%>
		<!--��������(Begin)--->
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
				<font class=b size="3"><b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;���ݲɼ�</b></font> </a>
				</td>
			</tr>
			<tr id="tr_RSJCJ" style="display:none">
				<td>
					<!--������(Begin)--->
					<table border="0" cellpadding="0" cellspacing="1" width="100%"
						align="center">
	
						<%
							if (yjh_bbcl_ybbsc) {
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="../rpt_net_cunzhen/afReportProduct.do?userName=<%=operatorForm.getUserName() %>&pwd=<%=operatorForm.getPassword() %>&loginDate=<%=operatorForm.getLoginDate() %>" target="mainFrame" >��������</a>	
							</td>
						</tr>
						<%
						}
						%>						
						
						
	
						<tr>
							<td height="5"></td>
						</tr>
					</table>
					<!--�������(End)-->
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
				<font class=b size="3"><b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;������</b></font> </a>
				</td>
			</tr>
	
			<tr id="tr_RBBCL" style="display:none">
				<td>
					<!--������(Begin)--->
					<table border="0" cellpadding="0" cellspacing="1" width="100%"
						align="center">
						
						<%
							if (yjh_bbcl_sc) {
						%>
						<tr>
							<td  background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="../rpt_net_cunzhen/viewDataReport.do?userName=<%=operatorForm.getUserName() %>&pwd=<%=operatorForm.getPassword() %>&loginDate=<%=operatorForm.getLoginDate() %>" target="mainFrame">�����</a>
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
								<a href="../rpt_net_cunzhen/dateQuary/manualCheckRepSHJY.do?userName=<%=operatorForm.getUserName() %>&pwd=<%=operatorForm.getPassword() %>&loginDate=<%=operatorForm.getLoginDate() %>" target="mainFrame">����У��</a>
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
								<a href="../rpt_net_cunzhen/viewCollectData.do?orgId=<%=operator.getOrgId() %>&userName=<%=operatorForm.getUserName() %>&pwd=<%=operatorForm.getPassword() %>&loginDate=<%=operatorForm.getLoginDate() %>" target="mainFrame">�������</a>
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
								<a	href="javascript:menu_click('dateQuary/manualCheckRepRecheck.do')">������</a>
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
								<a href="../rpt_net_cunzhen/dateQuary/manualCheckRep.do?userName=<%=operatorForm.getUserName() %>&pwd=<%=operatorForm.getPassword() %>&loginDate=<%=operatorForm.getLoginDate() %>" target="mainFrame">�������</a>
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
								<a href="../rpt_net_cunzhen/report_mgr/report_again_mgr/report_again_mgr.jsp?userName=<%=operatorForm.getUserName() %>&pwd=<%=operatorForm.getPassword() %>&loginDate=<%=operatorForm.getLoginDate() %>" target="mainFrame">�����ر�</a>
							</td>
						</tr>
						<%
						}
						%>
						
						<tr>
							<td height="5"></td>
						</tr>
					</table>
					<!--�������(End)-->
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
					<font class=b size="3"><b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;�����ѯ</b></font> </a>
				</td>
			</tr>
			
			<tr id="tr_RBBCX" style="display:none">
				<td>
					<!--�����ѯ(Begin)--->
					<table border="0" cellpadding="0" cellspacing="1" width="100%"
						align="center">
	
						<%
							if (yjh_bbcx_ybbcx ) {
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="../rpt_net_cunzhen/reportSearch/viewReportStatAction.do?userName=<%=operatorForm.getUserName() %>&pwd=<%=operatorForm.getPassword() %>&loginDate=<%=operatorForm.getLoginDate() %>" target="mainFrame">�����ѯ</a>
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
								<a href="../rpt_net_cunzhen/customViewAFReport.do?userName=<%=operatorForm.getUserName() %>&pwd=<%=operatorForm.getPassword() %>&loginDate=<%=operatorForm.getLoginDate() %>" target="mainFrame">����ѯ</a>
							</td>
						</tr>
						<%
						}
						%>
						
						<%if(false){ %>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="../rpt_net_cunzhen/viewCustomAFReportDetail.do?templateId=0101&versionId=1010&userName=<%=operatorForm.getUserName() %>&pwd=<%=operatorForm.getPassword() %>&loginDate=<%=operatorForm.getLoginDate() %>" target="mainFrame">���ݲ�ѯ</a>
							</td>
						</tr>
						<%} %>
						<%
							if (yjh_bbcx_ybbdc) {
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="../rpt_net_cunzhen/exportAFReport.do?userName=<%=operatorForm.getUserName() %>&pwd=<%=operatorForm.getPassword() %>&loginDate=<%=operatorForm.getLoginDate() %>" target="mainFrame">������</a>
							</td>
						</tr>
						<%
						}
						%>
						
						<%
							//if (yjh_bbcx_ybbrzcx) {
							if(false){
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="../rpt_net_cunzhen/reportStatisticsCollect.do?reportFlg=<%=reportFlg %>&userName=<%=operatorForm.getUserName() %>&pwd=<%=operatorForm.getPassword() %>&loginDate=<%=operatorForm.getLoginDate() %>" target="mainFrame">����ͳ��</a>
							</td>
						</tr>
						<%
						}
						%>
									
						<tr>
							<td height="5"></td>
						</tr>
					</table>
					<!--�������(End)-->
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
					<font class=b size="3"><b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;�������</b></font> </a>
				</td>
			</tr>
			
			<tr id="tr_RBBGL" style="display:none">
				<td>
					<!--�������(Begin)--->
					<table border="0" cellpadding="0" cellspacing="1" width="100%"
						align="center">
		
						<%
							if (yjh_bbgl_ybbdygl ) {
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="../rpt_net_cunzhen/template/viewTemplate.do?userName=<%=operatorForm.getUserName() %>&pwd=<%=operatorForm.getPassword() %>&loginDate=<%=operatorForm.getLoginDate() %>" target="mainFrame">���������</a>
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
								<a href="../rpt_net_cunzhen/afreportmerger.do?userName=<%=operatorForm.getUserName() %>&pwd=<%=operatorForm.getPassword() %>&loginDate=<%=operatorForm.getLoginDate() %>" target="mainFrame">�鲢��ϵ�鿴</a>
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
								<a href="javascript:menu_click('../rpt_net_cunzhen/baobiaofancha.do?userName=<%=operatorForm.getUserName() %>&pwd=<%=operatorForm.getPassword() %>&loginDate=<%=operatorForm.getLoginDate() %>')">ģ�巴��ά��</a>
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
								<a href="../rpt_net_cunzhen/afreportcheck.do?userName=<%=operatorForm.getUserName() %>&pwd=<%=operatorForm.getPassword() %>&loginDate=<%=operatorForm.getLoginDate() %>" target="mainFrame">У��ά���鿴</a>
							</td>
						</tr>					
						<%
						}
						%>
	
						<tr>
							<td height="5"></td>
						</tr>
					</table>
					<!--�������(End)-->
					
				</td>
			</tr>
			
			<!-- �������� -->
			<tr>
				<td height="30"  background="image/button_blue.jpg">
					<a href="javascript:menu_pic_click('RJGGL')"> 
					<font class=b size="3"><b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;��������</b></font> </a>
				</td>
			</tr>
			<tr id="tr_RJGGL" style="display:none">
				<td>
					<table border="0" cellpadding="0" cellspacing="1" width="100%"
						align="center">
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="../rpt_net_cunzhen/system_mgr/OrgInfo/view.do?userName=<%=operatorForm.getUserName() %>&pwd=<%=operatorForm.getPassword() %>&loginDate=<%=operatorForm.getLoginDate() %>" target="mainFrame">��������</a>
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
		<!--�������б���(End)--->
		
		
		<!--���б���(Begin)--->
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
		<tr id="pboc">
		<td>
			<table width="100%">
			<%
	
				if(reportFlg.equals("2")){
					
				
			%>
			<%
				if (rh_bbcl_rcj  || rh_bbcl_rbbsc ||  rh_bbcl_rbbxz) {
			%>
			<tr>
				<td height="30"  background="image/button_blue.jpg">
					<a href="javascript:menu_pic_click('RSJCJ')"> 
				<font class=b size="3"><b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;���ݲɼ�</b></font> </a>
				</td>
			</tr>
			<tr id="tr_RSJCJ" style="display:none">
				<td>
					<!--������(Begin)--->
					<table border="0" cellpadding="0" cellspacing="1" width="100%"
						align="center">
	
						<%
							if (rh_bbcl_rcj) {
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="viewGatherReport.do" target="mainFrame">���ݲɼ�</a>
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
								<a href="afReportProduct.do" target="mainFrame">��������</a>
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
								<a href="report/reportDownloadList.do" target="mainFrame">��������</a>
							</td>
						</tr>
						<%
						}
						%>
	
						<tr>
							<td height="5"></td>
						</tr>
					</table>
					<!--�������(End)-->
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
					<font class=b size="3"><b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;������</b></font> </a>
				</td>
			</tr>
	
			<tr id="tr_RBBCL" style="display:none">
				<td>
					<!--������(Begin)--->
					<table border="0" cellpadding="0" cellspacing="1" width="100%"
						align="center">
						
						<%
							if (rh_bbcl_rsc) {
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="viewNXDataReport.do" target="mainFrame">�����ϴ�</a>
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
								<a href="viewCollectNX.do" target="mainFrame">�������</a>
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
								<a href="dateQuary/manualCheckRepValidate.do" target="mainFrame">����У��</a>
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
								<a	href="dateQuary/manualCheckRepNXRecheck.do" target="mainFrame">�������</a>
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
								<a href="selReportAgainNX.do" target="mainFrame">�����ر�</a>
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
								<a href="exportRhAFReport.do" target="mainFrame">������</a>
							</td>
						</tr>					
						<%
						}
						%>
						
						<tr>
							<td height="5"></td>
						</tr>
					</table>
					<!--�������(End)-->
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
					<font class=b size="3"><b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;�����ѯ</b></font> </a>
				</td>
			</tr>
			
			<tr id="tr_RBBCX" style="display:none">
				<td>
					<!--�����ѯ(Begin)--->
					<table border="0" cellpadding="0" cellspacing="1" width="100%"
						align="center">
	
						<%
							if (rh_bbcx_rbbcx) {
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="reportSearch/viewReportStatNX.do" target="mainFrame">�����ѯ</a>
							</td>
						</tr>
						<%
						}
						%>
	
						<%
							if (rh_bbcx_rlhcx) {
							//if(false){
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="customViewAFReport.do" target="mainFrame">����ѯ</a>
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
								<a href="exportAFReport.do" target="mainFrame">������</a>
							</td>
						</tr>
						<%
						}
						%>
											
						<%
							//if (rh_bbcx_rbbrzcx ) {
							if(false){
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="reportStatisticsCollect.do?reportFlg=<%=reportFlg %>" target="mainFrame">����ͳ��</a>
							</td>
						</tr>
						<%
						}
						%>
									
						<tr>
							<td height="5"></td>
						</tr>
					</table>
					<!--�������(End)-->
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
					<font class=b size="3"><b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;�������</b></font> </a>
				</td>
			</tr>
			
			<tr id="tr_RBBGL" style="display:none">
				<td>
					<!--�������(Begin)--->
					<table border="0" cellpadding="0" cellspacing="1" width="100%"
						align="center">
	
						<%
							if (rh_bbgl_rbbdygl) {
						%>
						<tr>
							<td background="image/button_q.gif" height="25">
								&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
								<a href="afreportDefine.do" target="mainFrame">���������</a>
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
								<a href="afreportmerger.do" target="mainFrame">�鲢��ϵ�鿴</a>
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
								<a href="afreportcheck.do" target="mainFrame">У��ά���鿴</a>
							</td>
						</tr>
						<%
						}
						%>
	
						<tr>
							<td height="5"></td>
						</tr>
					</table>
					<!--�������(End)-->
				</td>
			</tr>
			<%
				}}
			%>
			</table>
		</td>
		</tr>
		
		<%
			}
		%>
		<!--���б���(End)--->
		
		<!--��������(Begin)--->
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
					
					if (qt_bbcl_qbcj  || qt_bbcl_qbbsc) {
				%>
				<tr>
					<td height="30" width="155" background="image/button_blue.jpg">
						<a href="javascript:menu_pic_click('QSJCJ')"> 
					<font class=b size="3"><b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;���ݲɼ�</b></font> </a>
					</td>
				</tr>
				<tr id="tr_QSJCJ" style="display:none">
					<td>
						<!--������(Begin)--->
						<table border="0" cellpadding="0" cellspacing="1" width="100%"
							align="center">
		
							<%
								if (qt_bbcl_qbbsc) {
							%>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="afReportProduct.do?orgId=<%=operator.getOrgId() %>" target="mainFrame">��������</a>	
								</td>
							</tr>
							<%
							}
							%>
		
							<tr>
								<td height="5"></td>
							</tr>
						</table>
						<!--�������(End)-->
					</td>
				</tr>
				
				<%} 		
					if ( qt_bbcl_qsc || qt_bbcl_qbbfh || qt_bbcl_qbbjy  
					|| qt_bbcl_qbbhz  || qt_bbcl_qbbcb || qt_bbcl_qbcj) {
				%>
				<tr>
					<td height="30" background="image/button_blue.jpg">
						<a href="javascript:menu_pic_click('QBBCL')"> 
					<font class=b size="3"><b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;������</b></font> </a>
					</td>
				</tr>
		
				<tr id="tr_QBBCL" style="display:none">
					<td>
						<!--������(Begin)--->
						<table border="0" cellpadding="0" cellspacing="1" width="100%"
							align="center">
							
							<%
								if (qt_bbcl_qsc ) {
							%>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="viewNXDataReport.do?orgId=<%=operator.getOrgId() %>" target="mainFrame">�����</a>
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
									<a href="dateQuary/manualCheckRepValidate.do" target="mainFrame">����У��</a>
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
									<a href="viewCollectNX.do?orgId=<%=operator.getOrgId() %>" target="mainFrame">�������</a>
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
									<a href="dateQuary/manualCheckRepNXRecheck.do" target="mainFrame">�������</a>
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
									<a href="gznx/report_again/report_again_lst.jsp" target="mainFrame">�����ر�</a>
								</td>
							</tr>
							<%
							}
							%>
		
							
							<tr>
								<td height="5"></td>
							</tr>
						</table>
						<!--�������(End)-->
					</td>
				</tr>
				
				<%
					}
					if (qt_bbcx_qbbcx    
					|| qt_bbcx_qlhcx    || qt_bbcx_qfxbcx  || qt_bbcx_qbbrzcx || qt_bbcx_day_report) {
				%>
				<tr>
					<td height="30"  background="image/button_blue.jpg">
						<a href="javascript:menu_pic_click('QBBCX')"> 
						<font class=b size="3"><b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;�����ѯ</b></font> </a>
					</td>
				</tr>
				
				<tr id="tr_QBBCX" style="display:none">
					<td>
						<!--�����ѯ(Begin)--->
						<table border="0" cellpadding="0" cellspacing="1" width="100%"
							align="center">
		
							<%
								if (qt_bbcx_qbbcx) {
							%>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="reportSearch/viewReportStatNX.do?orgId=<%=operator.getOrgId() %>" target="mainFrame">�����ѯ</a>
								</td>
							</tr>
							<%
							}
							%>
		
		
							<%
								//if (qt_bbcx_qlhcx) {
								if(false){
							%>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="customViewAFReport.do" target="mainFrame">����ѯ</a>
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
									<a href="exportAFReport.do" target="mainFrame">������</a>
								</td>
							</tr>
							<%
							}
							%>
		
							
							<%
								//if (qt_bbcx_qbbrzcx) {
								if(false){
							%>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="reportStatisticsCollect.do?reportFlg=<%=reportFlg %>" target="mainFrame">����ͳ��</a>
								</td>
							</tr>
							<%
							}
							%>
							
							<%
								if(qt_bbcx_day_report){
							%>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="dayTask.do?method=view" target="mainFrame">�ձ����ѯ</a>
								</td>
							</tr>
							<%
							}
							%>
									
							<tr>
								<td height="5"></td>
							</tr>
						</table>
						<!--�������(End)-->
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
						<font class=b size="3"><b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;�������</b></font> </a>
					</td>
				</tr>
				
				<tr id="tr_QBBGL" style="display:none">
					<td>
						<!--�������(Begin)--->
						<table border="0" cellpadding="0" cellspacing="1" width="100%"
							align="center">
		
							<%
								if (qt_bbgl_qbbdygl) {
							%>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="afreportDefine.do" target="mainFrame">���������</a>
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
									<a href="afreportmerger.do" target="mainFrame">�鲢��ϵ�鿴</a>
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
									<a href="afreportcheck.do" target="mainFrame">У��ά���鿴</a>
								</td>
							</tr>
							<%
							}
							%>
		
							<tr>
								<td height="5"></td>
							</tr>
						</table>
						<!--�������(End)-->
					</td>
				</tr>
				
		 		<%
					}
					}
				//	if (xtgl_jggl 
				//	|| xtgl_thgl || xtgl_jsgl  || xtgl_yhzgl || xtgl_zxyh || xtgl_zdgl || xtgl_rzgl || xtgl_hlgl) {
				%>
				</table>
			</td>
		</tr>
		<!--��������(end)--->
		<%
			if (sjbl) {
			//if(false){
		%>		
		<tr>
			<td height="35" >
			<img src="image/rpt_gat.gif" onclick="changeReport(4)">	
			<%--
				<a href="javascript:menu_pic_click('SJBL')">
				<font class=b size="3"><b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;���ݲ�¼</b></font>
				</a>
			--%>	
			</td>
		</tr>
		<%
			}
		%>
		<tr id="tr_SJBL" style="">
			<td><% if(reportFlg.equals("4")){ %>
				<!--�����ѯ(Begin)--->
				<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
					<%
						if (bb_sjcj) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="viewGatherReport.do?orgId=<%=operator.getOrgId() %>" target="mainFrame">���ݲ�¼</a>
						</td>
					</tr>
					<%
					}
					%>
					<%
						if(false){
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="searchGatherReportAction.do?orgId=<%=operator.getOrgId() %>" target="mainFrame">ͳ�Ʋ�ѯ</a>
						</td>
					</tr>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="bstj/bstj_overview.jsp" target="mainFrame">����ͳ��</a>
						</td>
					</tr>
					<%
						}
					%>
					<%
						if (bb_bbsc) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="report/reportDownloadList.do?orgId=<%=operator.getOrgId() %>" target="mainFrame">ģ������</a>	
						</td>
					</tr>
					<%
					}
					%>
					
					<%
						if (bb_bbcx) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="reportSearch/viewReportGatherStat.do?orgId=<%=operator.getOrgId() %>" target="mainFrame">�����ѯ</a>
						</td>
					</tr>
					<%
					}
					%>
					
					<tr>
						<td height="5"></td>
					</tr>
				</table><% } %>
				<!--�����ѯ(End)-->
			</td>
		</tr>
		
		
		
		
		<tr>
			<td height="35" >
			<img src="image/rpt_sys.gif" onclick="menu_pic_click('XTGL')">
			</td>
		</tr>
		<tr id="tr_XTGL" style="display:none">
			<td>
				<!--ϵͳ����(Begin)--->
				<table border="0" cellpadding="0" cellspacing="1" width="100%"
					align="center">

					<%
						if (xtgl_jggl ) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="system_mgr/OrgInfo/view.do" target="mainFrame">��������</a>
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
							<a href="popedom_mgr/viewDepartment.do" target="mainFrame">���Ź���</a>
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
							<a href="popedom_mgr/viewMUserGrp.do" target="mainFrame">�û������</a>
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
							<a href="popedom_mgr/viewRole.do" target="mainFrame">��ɫ����</a>
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
							<a href="popedom_mgr/viewOperator.do" target="mainFrame">�û�����</a>
						</td>
					</tr>
					<%
					}
					%>

					<%
						if (operator.isExitsThisUrl("system_mgr/viewResetPwd.do")) {
					%>
					<tr>
						<td background="image/button_q.gif" height="25">		
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="system_mgr/viewResetPwd.do" target="mainFrame">��������</a>
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
							<a href="system_mgr/viewCodeLib.do" target="mainFrame">�ֵ����</a>
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
							<a href="system_mgr/viewOnlineUser.do" target="mainFrame">�����û�</a>
						</td>
					</tr>
					<%
					}
					%>
				 -->
					
					<%
						//if (xtgl_hlgl) {
						if(false){
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="javascript:menu_click
								('sysManage/exchangeRate/exchangeRateMgr.do?method=viewExchangeRate" target="mainFrame">���ʹ���</a>
						</td>
					</tr>
					<%
					}
					%>

					<%
						//if (xtgl_hlgl) {
						if(false){
					%>
					<tr>
						<td background="image/button_q.gif" height="25">			
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="sysManage/specialValMgr.do?method=viewSpecVal" target="mainFrame">����У�����</a>
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
							<a href="system_mgr/viewLogIn.do" target="mainFrame">��־����</a>
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
							<a href="<%=request.getContextPath() %>/searchVorgRelInitAction.do" target="mainFrame">ӳ���ϵ����</a>
						</td>
					</tr>
					<%
						}
					%>
					<tr>
						<td background="image/button_q.gif" height="25">
							&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
							<a href="system_mgr/modifyPwd.jsp" target="mainFrame">�޸�����</a>
						</td>
					</tr>
					
				</table>
				<!--ϵͳ����(End)-->
			</td>
		</tr>

		<%
		// }
			//if (fxjc) {
			if(false){
		%>
		<tr>
			<td height="35" >
			<img src="image/rpt_fit.gif" onclick="toUrl()">	
			</td>
		</tr>

		
		<%
			}
			
			if (etljk) {
			//	if (false) {
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
						<!--������(Begin)--->
						<table border="0" cellpadding="0" cellspacing="1" width="100%"
							align="center">
		
							
		
							<%
								if(etl_rwpz){
							 %>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="../../FITETL/sjcq/rwpz/index_task_edit.jsp" target="mainFrame" >��������</a>
								</td>
							</tr>
							<%
								}
								if(etl_rwdd){
							 %>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="../../FITETL/findEtlTaskInfoListByRWJK.action" target="mainFrame">�������</a>
								</td>
							</tr>
							<%
								}
								if(etl_rwjk){
							 %>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="../../FITETL/sjcq/rwjk/rwjk_index.jsp" target="mainFrame">������</a>
								</td>
							</tr>
							<%
								}
								if(etl_fwqpz){
							 %>
							<tr>
								<td background="image/button_q.gif" height="25">
									&nbsp;<img src="image/page.gif" border="0">&nbsp;&nbsp;
									<a href="../../FITETL/etlFileServerInfo!initMethod.action" target="mainFrame">����������</a>
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
									<a href="../../FITETL/getsql/index.jsp" target="mainFrame">�������</a>
								</td>
							</tr>
							 <%
								}
							 %>
							<tr>
								<td height="5"></td>
							</tr>
						</table>
						<!--�������(End)-->
					</td>
				</tr>
				</table>
			</td>
		</tr>
		<%
			}
		%>
		
		
	</table>

</body>
</html:html>


