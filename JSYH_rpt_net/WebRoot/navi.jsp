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

		/**�����趨��Ȩ��**/
		boolean GG_GGJB = operator.isExitsThisUrl("viewOrgLayer.do"); //���������趨
		boolean GG_GGLX = operator.isExitsThisUrl("viewOrgType.do"); //���������趨
		boolean GG_GGDQ = operator.isExitsThisUrl("viewMRegion.do"); //���������趨
		boolean GG_GG = operator.isExitsThisUrl("viewOrgNet.do"); //�����趨

		/**�������**/
		boolean bbgl_xzmb = operator
				.isExitsThisUrl("template/add/index.jsp"); //����1104ģ��
		boolean bbgl_xzf1104mb = operator
				.isExitsThisUrl("template/add/Not1104Index.jsp"); //������1104ģ��
		boolean bbgl_mbwh = operator
				.isExitsThisUrl("template/viewMChildReport.do"); //ģ��ά��
		boolean bbgl_mbck = operator
				.isExitsThisUrl("template/viewTemplate.do"); //ģ��鿴
		boolean bbgl_sjgxwh = operator
				.isExitsThisUrl("template/viewProTmpt.do"); //���ݹ�ϵά��
		//	boolean bbgl_scbb = operator.isExitsThisUrl("template/protect/createExcel.jsp");  //���ɱ���
		boolean bbgl_scbb = operator
				.isExitsThisUrl("template/protect/createExcelNEW.jsp"); //���ɱ���

		/**ȡ������**/
		boolean qsgl_Excelcq = operator
				.isExitsThisUrl("viewTemplateNet.do"); //Excelȡ��
		boolean qsgl_sjsc = operator
				.isExitsThisUrl("templateDataBuildList.do"); //��������
		boolean qsgl_wbcq = operator.isExitsThisUrl("viewformula2.do"); //�ı���ȡ

		/**�������**/
		boolean sjsh_shjy = operator
				.isExitsThisUrl("/dateQuary/manualCheckRepSHJY.do"); //����У��
		boolean sjsh_bbsh = operator
				.isExitsThisUrl("dateQuary/manualCheckRep.do"); //�������
		boolean sjsh_bbck = operator
				.isExitsThisUrl("reportSearch/searchRep.do"); //����鿴
		boolean sjsh_cbgl = operator
				.isExitsThisUrl("report_mgr/report_again_mgr/report_again_mgr_frm.jsp"); //�ر�����		

		/**����ͳ��**/
		boolean sjtj_sjhz = operator
				.isExitsThisUrl("collectReport/search_data_stat.jsp"); //���ݻ���
		boolean sjtj_hzfssd = operator
				.isExitsThisUrl("collectType/viewCollectType.do"); //���ܷ�ʽ�趨

		/**��Ϣ�鿴**/
		boolean xxcx_bbck = operator
				.isExitsThisUrl("reportSearch/viewReportStatAction.do"); //����鿴		
		boolean xxcx_bstj = operator.isExitsThisUrl("reportStatistics.do"); //����ͳ��
		boolean xxcx_sjdc = operator.isExitsThisUrl("viewReport.do"); //���ݵ���

		/**���ݱ���**/
		boolean sjbc_sjwjxz = operator
				.isExitsThisUrl("template/templateDownloadList.do"); //�����ļ�����
		boolean sjbc_sjsb = operator
				.isExitsThisUrl("template/data_report/data_upreport.jsp"); //�����ϱ�
		boolean sjbc_sjtz = operator.isExitsThisUrl("viewonlineupdate.do"); //���ݵ���
		boolean sjbc_sbsj = operator.isExitsThisUrl("viewOnLineSJBS.do"); //�ϱ���������
		boolean sjbc_qthzsj = operator
				.isExitsThisUrl("collectType/viewOtherCollect.do"); //������������
		boolean sjbc_scsbwj = operator.isExitsThisUrl("downLoadReport"); //�����ļ�����

		/**ָ�����**/
		boolean zbfx_zbdy = operator
				.isExitsThisUrl("target/viewTargetDefine.do"); //ָ�궨��
		boolean zbfx_zbfxyj = operator
				.isExitsThisUrl("target/viewTargetGenerate.do"); //ָ�����Ԥ��
		boolean zbfx_zbywsd = operator
				.isExitsThisUrl("target/viewTargetNormal.do"); //ָ��ҵ���趨
		boolean zbfx_zblxsd = operator
				.isExitsThisUrl("target/viewTargetBusiness.do"); //ָ�������趨

		/**�����趨**/
		boolean cssd_hbdwsd = operator
				.isExitsThisUrl("config/ViewCurUnit.do"); //���ҵ�λ�趨
		boolean cssd_sjkjsd = operator
				.isExitsThisUrl("config/ViewDataRangeType.do"); //���ݿھ��趨
		boolean cssd_sbpd = operator
				.isExitsThisUrl("config/ViewCurRepFreqence.do"); //�ϱ�Ƶ���趨
		boolean cssd_jylb = operator
				.isExitsThisUrl("config/ViewCurVerifyType.do"); //У������趨
		boolean cssd_bzsd = operator.isExitsThisUrl("config/ViewCurr.do"); //�����趨
		boolean cssd_hlsd = operator.isExitsThisUrl("exchangerate/Find.do"); //�����趨
		boolean cssd_csbsd = operator
				.isExitsThisUrl("config/ViewVParam.do"); //�������趨
		boolean cssd_zbgssd = operator
				.isExitsThisUrl("template/ViewTargetFormual.do"); //ȡ��ָ�깫ʽ�趨
		boolean cssd_Syssd = operator
				.isExitsThisUrl("config/ViewSysPar.do"); //ϵͳ�����趨

		/**Ȩ�޹���**/
		boolean qxgl_bmgl = operator
				.isExitsThisUrl("popedom_mgr/viewDepartment.do"); //���Ź���
		boolean qzgl_yhgl = operator
				.isExitsThisUrl("popedom_mgr/viewOperator.do"); //�û�����
		boolean qzgl_jsgl = operator
				.isExitsThisUrl("popedom_mgr/viewRole.do"); //��ɫ����
		boolean qzgl_yhzgl = operator
				.isExitsThisUrl("popedom_mgr/viewMUserGrp.do"); //�û������
		boolean qzgl_gncd = operator
				.isExitsThisUrl("popedom_mgr/viewToolSetting.do"); //���ܲ˵�����
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
			 * ������� 
			 */
			var BBGL="BBGL";
			/**
			 * �������
			 */
			var SJSH="SJSH";
			/**
			 * ����ͳ��
			 */
			var SJTJ="SJTJ";
			/**
			 * ���ݲ�¼
			 */
			var SJBL="SJBL";
			/**
			 * ��Ϣ��ѯ
			 */
			var XXCX="XXCX";
			/**
			 * ָ�����
			 */
			var ZBFX="ZBFX";
			/**
			 * ȡ������
			 */
			var QSGL="QSGL";
			/**
			 * �����趨
			 */
			var CSSD="CSSD";
			/**
			 * ��������
			 */
			var JGGL="JGGL";
			/**
			 * ϵͳ����
			 */
			var XTGL="XTGL";
			/**
			 * ��Ϣ����
			 */
			var XXJH="XXJH"; 
			/**
			 * Ȩ�޹���
			 */
			var QXGL="QXGL";
			/**
			 * ���ݱ���
			 */
			var SJBS="SJBS";
			/**
			 * �Ƿ����Ӳ˵�չ��
			 */ 
			 var is_menu_show=false;
			 
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
			 * ��ʼ���˵�ͼƬ��ʾ
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
			 * �����Ӳ˵�
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
		     * ����˵��¼�
		     */
		    function menu_click(_url){
		    	if(_url=="") return;
		    	window.parent.mainFrame.contents.location.assign(_url);
		    } 
		    
		    /**
			 * ע���¼�
			 */
			 function _exit(){
			 	if(confirm("��ȷ��Ҫע���û���?\n")==true){
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
				<!--�������(Begin)--->
				<table border="0" cellpadding="0" cellspacing="0" width="85%"
					align="center">
					<tr>
						<td>
							<%
							if (operator.isExitsThisUrl("template/add/index.jsp")) {
							%>
							<img src="image/page.gif" border="0">
							<a href="javascript:menu_click('template/add/index.jsp')">����PDFģ��</a>
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
							<a href="javascript:menu_click('template/add/Not1104Index.jsp')">����EXCELģ��</a>
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
							<a href="javascript:menu_click('template/viewMChildReport.do')">ģ��ά��</a>
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
							<a href="javascript:menu_click('template/viewTemplate.do')">ģ��鿴</a>
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
							<a href="javascript:menu_click('template/viewProTmpt.do')">���ݹ�ϵά��</a>
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
								href="javascript:menu_click('template/protect/createExcelNEW.jsp')">���ɱ���</a>
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
				<!--�������(End)-->
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
				<!--ȡ������(Begin)-->
				<table border="0" cellpadding="0" cellspacing="0" width="85%"
					align="center">
					<!-- <tr>
								<td>
									<%
										if(operator.isExitsThisUrl("viewTemplateNet.do"))
										{			
									%>
										<img src="image/page.gif" border="0"><a href="javascript:menu_click('viewTemplateNet.do')">Excel��ȡ</a>
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
										<img src="image/page.gif" border="0"><a href="javascript:menu_click('templateDataBuildList.do')">��������</a>
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
										<img src="image/page.gif" border="0"><a href="javascript:menu_click('obtain/text/viewformula2.do')">�ı���ȡ</a>
									<%
										}
									%>
								</td>
							</tr>-->
				</table>
				<!--ȡ������(End)-->
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
				<!--�������(Begin)-->
				<table border="0" cellpadding="0" cellspacing="0" width="85%"
					align="center">
					<tr>
						<td>
							<%
							if (operator.isExitsThisUrl("dateQuary/manualCheckRepSHJY.do")) {
							%>
							<img src="image/page.gif" border="0">
							<a
								href="javascript:menu_click('dateQuary/manualCheckRepSHJY.do')">����У��</a>
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
							<a href="javascript:menu_click('dateQuary/manualCheckRep.do')">�������</a>
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
							<a href="javascript:menu_click('selForseReportAgain.do')">�ر�����</a>
							<%
							}
							%>
						</td>
					</tr>

					<tr>
						<td height="5"></td>
					</tr>
				</table>
				<!--�������(End)-->
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
				<!--����ͳ��(Begin)-->
				<table border="0" cellpadding="0" cellspacing="0" width="85%"
					align="center">
					<tr>
						<td>
							<%
								if (operator.isExitsThisUrl("collectReport/search_data_stat.jsp")) {
							%>
							<img src="image/page.gif" border="0"><a href="javascript:menu_click('viewCollectData.do')">���ݻ���</a>
								<!--<img src="image/page.gif" border="0"><a href="javascript:menu_click('collectReport/search_data_stat.jsp')">���ݻ���</a>-->
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
							<a href="javascript:menu_click('collectReport/jobLogMgr.do?method=toFindJob')">ETL���</a>
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
									<img src="image/page.gif" border="0"><a href="javascript:menu_click('analysis/selectACompareLog.do')">���ݱȶ�</a>
								<%
									//	}
									%>
								</td>
							</tr>	
							 -->
				</table>
				<!--����ͳ��(End)-->
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
				<!--���ݱ���(Begin)-->
				<table border="0" cellpadding="0" cellspacing="0" width="85%"
					align="center">
					<tr>
						<td>
							<%
							if (operator.isExitsThisUrl("template/templateDownloadList.do")) {
							%>
							<img src="image/page.gif" border="0">
							<a
								href="javascript:menu_click('template/templateDownloadList.do')">�����ļ�����</a>
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
							<a href="javascript:menu_click('viewDataReport.do')">�����ϱ�</a>
							<%
							}
							%>
						</td>
					</tr>

				</table>
				<!--���ݱ���(End)-->
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
				<!--��Ϣ��ѯ(Begin)-->
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
								href="javascript:menu_click('reportSearch/viewReportStatAction.do')">����鿴</a>
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
							<a href="javascript:menu_click('viewReport.do')">���ݵ���</a>
							<%
							}
							%>
						</td>
					</tr>
				</table>
				<!--��Ϣ��ѯ(End)-->
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
				<!--ָ�����(Begin)-->
				<table border="0" cellpadding="0" cellspacing="0" width="85%"
					align="center">
					<tr>
						<td>
							<%
							if (operator.isExitsThisUrl("target/viewTargetDefine.do")) {
							%>
							<img src="image/page.gif" border="0">
							<a href="javascript:menu_click('target/viewTargetDefine.do')">ָ�궨��</a>
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
							<a href="javascript:menu_click('target/viewTargetGenerate.do')">ָ�����Ԥ��</a>
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
							<a href="javascript:menu_click('target/viewTargetNormal.do')">ָ��ҵ���趨</a>
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
							<a href="javascript:menu_click('target/viewTargetBusiness.do')">ָ�������趨</a>
							<%
							}
							%>
						</td>
					</tr>


				</table>
				<!--ָ�����(End)-->
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
				<!--�����趨(Begin)--->
				<table border="0" cellpadding="0" cellspacing="0" width="85%"
					align="center">
					<tr>
						<td>
							<%
							if (operator.isExitsThisUrl("config/ViewCurUnit.do")) {
							%>
							<img src="image/page.gif" border="0">
							<a href="javascript:menu_click('config/ViewCurUnit.do')">���ҵ�λ�趨</a>
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
							<a href="javascript:menu_click('config/ViewDataRangeType.do')">���ݿھ�����趨</a>
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
							<a href="javascript:menu_click('config/ViewCurRepFreqence.do')">�ϱ�Ƶ���趨</a>
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
							<a href="javascript:menu_click('config/ViewCurVerifyType.do')">У������趨</a>
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
							<a href="javascript:menu_click('config/ViewCurr.do')">�����趨</a>
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
							<a href="javascript:menu_click('config/ViewSysPar.do')">ϵͳ�����趨</a>
							<%
							}
							%>
						</td>
					</tr>
					<tr>
						<td height="5"></td>
					</tr>
				</table>
				<!--�����趨(End)-->
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
				<!--��������(Begin)--->
				<table border="0" cellpadding="0" cellspacing="0" width="85%"
					align="center">
					<!--<tr>
								<td>
									<%
										if(operator.isExitsThisUrl("viewOrgLayer.do"))
										{			
									%>
										<img src="image/page.gif" border="0"><a href="javascript:menu_click('viewOrgLayer.do')">���������趨</a>
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
							<a href="javascript:menu_click('viewOrgType.do')">���������趨</a>
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
<%--							<a href="javascript:menu_click('viewMRegion.do')">���������趨</a>--%>
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
							<a href="javascript:menu_click('org/selectOrgNet.do')">�����趨</a>
							<%
							}
							%>
						</td>
					</tr>
					<tr>
						<td height="5"></td>
					</tr>
				</table>
				<!--��������(End)-->
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
				<!--ϵͳ����(Begin)--->
				<table border="0" cellpadding="0" cellspacing="0" width="85%"
					align="center">
					<tr>
						<td>
							<img src="image/page.gif" border="0">
							<a href="javascript:menu_click('system_mgr/modifyPwd.jsp')">�޸�����</a>
						</td>
					</tr>
					<tr>
						<td>
							<%
							if (operator.isExitsThisUrl("system_mgr/viewResetPwd.do")) {
							%>
							<img src="image/page.gif" border="0">
							<a href="javascript:menu_click('system_mgr/viewResetPwd.do')">��������</a>
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
							<a href="javascript:menu_click('system_mgr/viewLogIn.do')">��־����</a>
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
							<a href="javascript:menu_click('bulletin/BulletinList2.jsp')">����鿴</a>
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
							<a href="javascript:menu_click('system_mgr/viewRemindTips.jsp')">��ʿ����</a>
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
							<a href="javascript:menu_click('bulletin/BulletinList.jsp')">�������</a>
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
								href="javascript:menu_click('bulletin/bulletinNormal.do?method=view')">����鿴</a>
							<%
							}
							%>
						</td>
					</tr>
					<tr>
						<td height="5"></td>
					</tr>
				</table>
				<!--ϵͳ����(End)-->
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
				<!-- Ȩ�޹���(Begin)-->
				<table border="0" cellpadding="0" cellspacing="0" width="85%"
					align="center">
					<tr>
						<td>
							<%
							if (operator.isExitsThisUrl("popedom_mgr/viewDepartment.do")) {
							%>
							<img src="image/page.gif" border="0">
							<a href="javascript:menu_click('popedom_mgr/viewDepartment.do')">���Ź���</a>
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
							<a href="javascript:menu_click('popedom_mgr/viewOperator.do')">�û�����</a>
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
							<a href="javascript:menu_click('popedom_mgr/viewRole.do')">��ɫ����</a>
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
							<a href="javascript:menu_click('popedom_mgr/viewMUserGrp.do')">�û������</a>
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
<%--							<a href="javascript:menu_click('popedom_mgr/viewToolSetting.do')">���ܲ˵�����</a>--%>
<%--							<%--%>
<%--							}--%>
<%--							%>--%>
<%--						</td>--%>
<%--					</tr>--%>
				</table>
				<!-- Ȩ�޹���(End)-->
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
