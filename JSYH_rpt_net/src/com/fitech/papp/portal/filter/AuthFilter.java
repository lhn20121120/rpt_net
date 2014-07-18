package com.fitech.papp.portal.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cbrc.auth.adapter.StrutsOperatorDelegate;
import com.cbrc.auth.form.OperatorForm;
import com.cbrc.auth.util.ApplicationPropertiesUtil;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.security.OperatorHandler;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.common.DateUtil;
import com.fitech.gznx.service.OnlineUserUtil;
import com.fitech.papp.webservice.client.AuthWebServiceStub;
import com.fitech.papp.webservice.util.Blowfish;
import org.apache.log4j.Logger;

/**
 * Ȩ�޹�����
 *
 * @author Chris
 */
public class AuthFilter implements Filter {
    private static Logger logger = Logger.getLogger(AuthFilter.class);
    private boolean flag = false;
    FitechException log = new FitechException(AuthFilter.class);

    /**
     * ϵͳ��¼ҳ
     */
    private String loginPage = "";
    /**
     * ����Ҫ���˵�ҳ
     */
    private String notFilterPage = "";


    /**
     *
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {
        String url = ((HttpServletRequest) req).getRequestURI();
        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("P3P", "CP=CAO PSA OUR");//��IE�����frame�������session���׶�ʧ�����ϴ˷������Խ��
        HttpServletRequest request = (HttpServletRequest) req;
        HttpSession session = request.getSession();
        //���ж�session����û���û�
        if(isNeedFilter(url,loginPage,notFilterPage,session)==true) {
            if (null == session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)) {
                PrintWriter pw = res.getWriter();
                StringBuffer sb = new StringBuffer("");
                sb.append("<html><head><title>���µ�½</title></head><body>");
                sb.append("<script>");
                if(Config.IS_INTEGRATE_PORTAL)
                {
                    logger.info("sessionʧЧ����Ҫ���µ�¼." + Config.NEW_PORTAL_URL + "/login.jsp");
                    //response.sendRedirect(Config.NEW_PORTAL_URL + "/login.jsp");
                    sb.append("window.top.location.href = '" + Config.NEW_PORTAL_URL + "/login.jsp';");
                }
                //�����Լ��ĵ�¼ҳ
                else
                {
                    String path = ((HttpServletRequest) req).getContextPath();
                    logger.info("sessionʧЧ����Ҫ���µ�¼." + path);
                    sb.append("window.top.location.href = '" + path + "';");
                    //response.sendRedirect(path);
                }
                sb.append("</script>");
                sb.append("</body></html>");
                pw.write(sb.toString());
                pw.flush();
                pw.close();
                return;
            }
        }


        if (Config.IS_INTEGRATE_PORTAL) {
            if (url.indexOf("userLogin.do") < 0) {
                String sessionId = session.getId();

                AuthWebServiceStub authStub = new AuthWebServiceStub(
                        Config.NEW_PORTAL_URL + "/services/AuthWebService?wsdl");
                AuthWebServiceStub.IsExist isExist = new AuthWebServiceStub.IsExist();
                isExist.setSessionId(sessionId);
                AuthWebServiceStub.IsExistResponse ssr = authStub.isExist(isExist);

                if ("0".equals(new Blowfish().decode(ssr.get_return()))) {
                    logger.info("ƽ̨sessionʧЧ����Ҫ���µ�¼." + Config.NEW_PORTAL_URL + "/login.jsp");
                    response.sendRedirect(Config.NEW_PORTAL_URL + "/login.jsp");
                    return;
                }
                // 0������ ������ڣ������û���
                // �����û���
                Blowfish bf1 = new Blowfish();
                String userName = bf1.decode(ssr.get_return());

                if (null == session.getAttribute(Config.OPERATOR_SESSION_NAME)) {
                    logger.info("session is null, need login now.");

                    ServletContext application = session.getServletContext();
                    OperatorForm operatorForm = new OperatorForm();
                    operatorForm.setUserName(userName);
                    OperatorForm resultForm = StrutsOperatorDelegate
                            .userLoginValidate(operatorForm, userName);

                    HttpSession otherSession = (HttpSession) application
                            .getAttribute(operatorForm.getUserName());
                    if (otherSession != null) {
                        if (session != otherSession) {
                            OnlineUserUtil.removeOnlineUser(otherSession
                                    .getId());
                            try {

                                otherSession.invalidate();
                            } catch (Exception e) {
                                System.out.println("session��ע��");
                            }
                            otherSession = null;
                        }
                        application.removeAttribute(operatorForm.getUserName()
                                .trim());

                        /** ��¼�ɹ��򱣴��û���Ϣ��Session�� */
                        OperatorHandler operatorHandler = new OperatorHandler();
                        Operator operator = operatorHandler
                                .saveUserInfo(resultForm);

                        operator.setSessionId(session.getId());
                        operator.setIpAdd(request.getRemoteAddr());
                        if (session.getAttribute(Config.REPORT_SESSION_FLG) != null)
                            session.removeAttribute(Config.REPORT_SESSION_FLG);
                        if (session
                                .getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME) != null)
                            session
                                    .removeAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
                        if (session.getAttribute(Config.USER_LOGIN_DATE) != null) {
                            session.removeAttribute(Config.USER_LOGIN_DATE);
                        }
                        if (operatorForm.getLoginDate() == null
                                || operatorForm.getLoginDate().equals("")) {
                            String yestoday = DateUtil.getYestoday();
                            operatorForm.setLoginDate(yestoday);
                        }
                        session.setAttribute(Config.USER_LOGIN_DATE,
                                operatorForm.getLoginDate());

                        if (null == operatorForm.getPassword()
                                || "".equals(operatorForm.getPassword())) {
                            session.setAttribute(Config.USER_LOGIN_DATE,
                                    DateUtil.getLastMonth(DateUtil.getToday()));
                        }

                        session.setAttribute(
                                Config.OPERATOR_SESSION_ATTRIBUTE_NAME,
                                operator);
                        session.setAttribute("OPERATORFORM", operatorForm);

                    }
                }
            }
        }
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {

    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        this.loginPage = config.getInitParameter("loginPage");
        this.notFilterPage = config.getInitParameter("notFilterPage");

        if (this.loginPage == null)
            log.println("��������ʼ��ʱ����ȡϵͳ��¼ҳʧ��!");
    }


    /**
     * �жϵ�ǰ�����URI�Ƿ���Ҫ����
     *
     * @param uri           String �����URI
     * @param loginPage     String ϵͳ��¼ҳ
     * @param notFilterPage String ����Ҫ���˵�ҳ
     * @param session       HttpSession
     * @return boolean ��Ҫ���ˣ�����true;���򣬷���false
     */
    private boolean isNeedFilter(String uri, String loginPage, String notFilterPage, HttpSession session) {
        boolean result = true;

        if (uri == null) return false;

        if (uri.indexOf(loginPage) >= 0) {
            result = false;
        }

        String[] arrNotFilterPage = notFilterPage.split(",");

        if (arrNotFilterPage != null && arrNotFilterPage.length > 0) {
            for (int i = 0; i < arrNotFilterPage.length; i++) {
                if (uri.indexOf(arrNotFilterPage[i]) >= 0) {
                    result = false;
                    break;
                }
            }
        }

        return result;
    }

}
