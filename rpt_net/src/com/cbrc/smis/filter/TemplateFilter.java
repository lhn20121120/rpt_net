package com.cbrc.smis.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;

/**
 * ������ʾ���˱����Ƿ�֧���������
 * 
 * @author Luyf
 * @date 2013-10-22
 */
public class TemplateFilter implements Filter {
	private boolean flag = false;
	private Map<String, String> resultMap = null;
	FitechException log = new FitechException(TemplateFilter.class);

	/**
	 * ��Ҫ���˵�����
	 */
	private String needFilterPage = "";

	/**
	 * doFilter����
	 * 
	 * @param request
	 *            ServletRequest
	 * @param response
	 *            ServletResponse
	 * @param chain
	 *            FilterChain
	 * @return void
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		// HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession();
		String uri = req.getRequestURI();

		Operator operator = null;
		if (session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME) != null)
			operator = (Operator) session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);

		boolean flag = isNeedFilter(uri, needFilterPage, session);

		if (flag && Config.YJ_BRANCH_BUSI_LINE == 1 && operator != null) {
			Map<String, String> ruleMap = getRuleMap();

			// ɾ�����еĶԷ��˻��߷�֧����Ľ��еĹ��˲���
			/** �û��������Ȩ�� */
			operator.setChildRepCheckPodedom(romoveRule(operator.getChildRepCheckPodedom()));
			/** �û�����鿴Ȩ�� */
			operator.setChildRepSearchPopedom(romoveRule(operator.getChildRepSearchPopedom()));
			/** �û�������Ȩ�� */
			operator.setChildRepReportPopedom(romoveRule(operator.getChildRepReportPopedom()));
			/** �û�������Ȩ�� */
			operator.setChildRepVerifyPopedom(romoveRule(operator.getChildRepVerifyPopedom()));

			String flag1104 = request.getParameter("Flag1104");
			if (flag1104 != null && flag1104.trim().length() > 0) {// ��ʱ���з��˺ͷ�֧����Ĺ��˲���
				// G_GF
				String val = ruleMap.get(flag1104);// �õ�����ķ��˷�֧���߶�Ӧ�Ĺ��˹���
				if (val != null && val.trim().length() > 0) {
					// ׼����Ҫ���й��˵ĵ���
					String[] ruleArr = val.split("_");
					StringBuffer sb = new StringBuffer(" and ");
					if (ruleArr.length == 1) {// ֻ������Ҫ��������
						sb.append("(");
						String[] strs = ruleArr[0].split(",");
						for (int i = 0; i < strs.length; i++) {
							sb.append(" viewOrgRep.childRepId like '").append(strs[i]).append("%'");
							
							sb.append(" or");
						}
						String ruleString = sb.toString();
						ruleString = ruleString.substring(0, ruleString.lastIndexOf("or"));
						
						sb = new StringBuffer(ruleString);
						sb.append(")");
					//	sb.append("(viewOrgRep.childRepId like '").append(ruleArr[0]).append("%')");
					} else if (ruleArr.length == 2) {// ������Ҫ����ġ�ͬʱҲ���˲���Ҫ�ı���
						sb.append("((");
						String[] strs = ruleArr[0].split(",");
						for (int i = 0; i < strs.length; i++) {
							sb.append(" viewOrgRep.childRepId like '").append(strs[i]).append("%'");
							
							sb.append(" or");
						}
						String ruleString = sb.toString();
						ruleString = ruleString.substring(0, ruleString.lastIndexOf("or"));
						
						sb = new StringBuffer(ruleString+")");
					//	sb.append("(viewOrgRep.childRepId like '").append(ruleArr[0]+"%'");
						
						sb.append(" and viewOrgRep.childRepId not like '").append(ruleArr[1]).append("%'");
						
						sb.append(")");
					}
					// �趨��������
					String rule = sb.toString();
					/** �û��������Ȩ�� */
					operator.setChildRepCheckPodedom(operator.getChildRepCheckPodedom() + rule);
					/** �û�����鿴Ȩ�� */
					operator.setChildRepSearchPopedom(operator.getChildRepSearchPopedom() + rule);
					/** �û�������Ȩ�� */
					operator.setChildRepReportPopedom(operator.getChildRepReportPopedom() + rule);
					/** �û�������Ȩ�� */
					operator.setChildRepVerifyPopedom(operator.getChildRepVerifyPopedom() + rule);
				}
			}
		}
		chain.doFilter(request, response);
	}

	/**
	 * ����ַ����������ţ���ɾ��������֮ǰ���һ��and������and���������ַ�
	 */
	private String romoveRule(String str) {
		// �ж��Ƿ�Ϊ��
		if (str == null || str.trim().length() == 0) {
			return null;
		}
		// �ж��Ƿ�������ţ�����������ţ���˵����Ҫɾ���Ѿ���ӵķ��˷�֧���߹�����Ϣ;�������ý����κβ���
		if (str.indexOf("(") == -1) {
			return str;
		}
		// where viewOrgRep.powType=1 and viewOrgRep.userId=60372 and (viewOrgRep.childRepId like 'G%' and viewOrgRep.childRepId not like 'GF%')
		int len = str.toLowerCase().lastIndexOf("and", str.indexOf("("));// ȡ���һ��������֮ǰ��and�ַ����ڵ�λ��
		String result = str.substring(0, len);

		return result;
	}

	/**
	 * �ֽ���Ҫ�趨�Ĺ��˹���
	 */
	private Map<String, String> getRuleMap() {
		if (Config.YJ_BRANCH_BUSI_LINE_RULE == null || Config.YJ_BRANCH_BUSI_LINE_RULE.trim().length() == 0) {
			return null;
		}
		if (resultMap == null || resultMap.size() == 0) {
			resultMap = new HashMap<String, String>();
			// G:G_GF,F:C
			String[] tempArr1 = Config.YJ_BRANCH_BUSI_LINE_RULE.split(";");
			for (int i = 0; i < tempArr1.length; i++) {
				String temp = tempArr1[i];
				// G:G_GF
				String[] tempArr2 = temp.split(":");
				resultMap.put(tempArr2[0], tempArr2[1]);
			}
		}

		return resultMap;
	}

	/**
	 * destroy����
	 * 
	 * @return void
	 */
	public void destroy() {

	}

	/**
	 * init����
	 * 
	 * @param config
	 *            FilterConfig
	 * @return void
	 */
	public void init(FilterConfig config) throws ServletException {
		this.needFilterPage = config.getInitParameter("needFilterPage");
	}

	/**
	 * �жϵ�ǰ�����URI�Ƿ���Ҫ����
	 * 
	 * @param uri
	 *            String �����URI
	 * @param loginPage
	 *            String ϵͳ��¼ҳ
	 * @param notFilterPage
	 *            String ����Ҫ���˵�ҳ
	 * @param session
	 *            HttpSession
	 * @return boolean ��Ҫ���ˣ�����true;���򣬷���false
	 */
	private boolean isNeedFilter(String uri, String needFilterPage, HttpSession session) {
		boolean result = false;

		if (uri == null)
			return false;

		String[] arrNeedFilterPage = needFilterPage.split(",");
		if (arrNeedFilterPage != null && arrNeedFilterPage.length > 0) {
			for (int i = 0; i < arrNeedFilterPage.length; i++) {
				if (uri.indexOf(arrNeedFilterPage[i]) >= 0) {
					result = true;
					break;
				}
			}
		}

		return result;
	}
}
