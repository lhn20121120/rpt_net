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
 * 区分显示法人报表还是分支报表过滤器
 * 
 * @author Luyf
 * @date 2013-10-22
 */
public class TemplateFilter implements Filter {
	private boolean flag = false;
	private Map<String, String> resultMap = null;
	FitechException log = new FitechException(TemplateFilter.class);

	/**
	 * 需要过滤的请求
	 */
	private String needFilterPage = "";

	/**
	 * doFilter方法
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

			// 删除已有的对法人或者分支报表的进行的过滤操作
			/** 用户报表审核权限 */
			operator.setChildRepCheckPodedom(romoveRule(operator.getChildRepCheckPodedom()));
			/** 用户报表查看权限 */
			operator.setChildRepSearchPopedom(romoveRule(operator.getChildRepSearchPopedom()));
			/** 用户报表报送权限 */
			operator.setChildRepReportPopedom(romoveRule(operator.getChildRepReportPopedom()));
			/** 用户报表复核权限 */
			operator.setChildRepVerifyPopedom(romoveRule(operator.getChildRepVerifyPopedom()));

			String flag1104 = request.getParameter("Flag1104");
			if (flag1104 != null && flag1104.trim().length() > 0) {// 此时进行法人和分支报表的过滤操作
				// G_GF
				String val = ruleMap.get(flag1104);// 得到传入的法人分支条线对应的过滤规则
				if (val != null && val.trim().length() > 0) {
					// 准备需要进行过滤的调整
					String[] ruleArr = val.split("_");
					StringBuffer sb = new StringBuffer(" and ");
					if (ruleArr.length == 1) {// 只过滤需要报表的情况
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
					} else if (ruleArr.length == 2) {// 过滤需要报表的、同时也过滤不需要的报表
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
					// 设定过滤条件
					String rule = sb.toString();
					/** 用户报表审核权限 */
					operator.setChildRepCheckPodedom(operator.getChildRepCheckPodedom() + rule);
					/** 用户报表查看权限 */
					operator.setChildRepSearchPopedom(operator.getChildRepSearchPopedom() + rule);
					/** 用户报表报送权限 */
					operator.setChildRepReportPopedom(operator.getChildRepReportPopedom() + rule);
					/** 用户报表复核权限 */
					operator.setChildRepVerifyPopedom(operator.getChildRepVerifyPopedom() + rule);
				}
			}
		}
		chain.doFilter(request, response);
	}

	/**
	 * 如果字符串存在括号，则删除左括号之前最后一个and（包括and）的所有字符
	 */
	private String romoveRule(String str) {
		// 判断是否为空
		if (str == null || str.trim().length() == 0) {
			return null;
		}
		// 判断是否包含括号，如果包含括号，则说明需要删除已经添加的法人分支条线过滤信息;否则，则不用进行任何操作
		if (str.indexOf("(") == -1) {
			return str;
		}
		// where viewOrgRep.powType=1 and viewOrgRep.userId=60372 and (viewOrgRep.childRepId like 'G%' and viewOrgRep.childRepId not like 'GF%')
		int len = str.toLowerCase().lastIndexOf("and", str.indexOf("("));// 取最后一个左括号之前的and字符所在的位置
		String result = str.substring(0, len);

		return result;
	}

	/**
	 * 分解需要设定的过滤规则
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
	 * destroy方法
	 * 
	 * @return void
	 */
	public void destroy() {

	}

	/**
	 * init方法
	 * 
	 * @param config
	 *            FilterConfig
	 * @return void
	 */
	public void init(FilterConfig config) throws ServletException {
		this.needFilterPage = config.getInitParameter("needFilterPage");
	}

	/**
	 * 判断当前请求的URI是否需要过滤
	 * 
	 * @param uri
	 *            String 请求的URI
	 * @param loginPage
	 *            String 系统登录页
	 * @param notFilterPage
	 *            String 不需要过滤的页
	 * @param session
	 *            HttpSession
	 * @return boolean 需要过滤，返回true;否则，返回false
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
