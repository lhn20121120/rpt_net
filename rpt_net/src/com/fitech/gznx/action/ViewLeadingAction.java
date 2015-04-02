package com.fitech.gznx.action;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import com.fitech.gznx.common.Config;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.form.LendingQueryForm;
import com.fitech.gznx.graph.GraphDataSource;
import com.fitech.gznx.graph.GraphFactory;
import com.fitech.gznx.security.OperatorLead;
import com.fitech.gznx.service.XmlTreeUtil;
import com.fitech.gznx.util.FitechUtil;

/**
 * 
 * 类说明:该类用于处理领导首页的访问(该类用于处理领导首页的访问)
 * 
 * @author jack
 * @date 2009-12-6
 */
public class ViewLeadingAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
 		HttpSession session = request.getSession();
 
 		OperatorLead	operator = new OperatorLead();
 		
 		String contextP = this.getServlet().getServletContext().getRealPath("/");
 		operator.setContextP(contextP);
 		if (request.getParameter("flag") != null)
		{ // 指标集的分类访问
			
			try
			{	
				String orgId = "";
				String orgName ="";
				if (session.getAttribute(com.cbrc.smis.common.Config.OPERATOR_SESSION_ATTRIBUTE_NAME) != null 
						&& StringUtil.isEmpty(orgId)){
				 com.cbrc.smis.security.Operator operatororg = 
					 (com.cbrc.smis.security.Operator) session.getAttribute(
							 com.cbrc.smis.common.Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
				 orgId = operatororg.getOrgId();
				 orgName = operatororg.getOrgName();
				}
				String naviGraph = request.getParameter("naviGraph");
				String naviGraphName = null;
				Integer freq = Config.FREQ_MONTH;				
				String reptDate =FitechUtil.getLatestDataDate(Config.FREQ_DAY);
				String ccyId = "999";
				
				if(naviGraph.equals("cundaikuanqingkuang"))
				{
					naviGraphName = "存贷款情况";
				}
				else if(naviGraph.equals("caiwushouzhi"))
				{
					naviGraphName = "财务收支情况";					
				}
				else if(naviGraph.equals("yingliqingkuang"))
				{
					naviGraphName = "盈利性情况";					
				}
				else if(naviGraph.equals("zichanfuzhaizongti"))
				{
					naviGraphName = "资产负债总体情况";					
				}
				else if(naviGraph.equals("liudongxingqingkuang"))
				{
					naviGraphName = "流动性情况";
				}
				else if(naviGraph.equals("jingyingjihuazhixingqingkuang"))
				{
					naviGraphName = "经营计划执行情况";
				}
				else if(naviGraph.equals("hangyetoufangqingkuang"))
				{
					naviGraphName = "行业投放";
				}
				else if(naviGraph.equals("yewuliangqingkuang"))
				{
					naviGraphName = "业务量情况";
				}
				else if(naviGraph.equals("tongyeduibi"))
				{
					naviGraphName = "同业对比情况";					
				}
				
								
				GraphDataSource gs = new GraphDataSource(operator,naviGraph);
				gs.setCcyId(ccyId);
				gs.setReptDate(reptDate);
				gs.setFreq(String.valueOf(freq.intValue()));
				gs.setNaviGraphName(naviGraphName);
				gs.setOrgList(XmlTreeUtil.createOrgXml(request,"TREE1_NODES",null,false,true,true)) ;
				gs.setOrgId(orgId);
				gs.setOrgName(orgName);
				//设置查询条件
				LendingQueryForm lqf = (LendingQueryForm) form;
				lqf.setCcyId(gs.getCcyId());
				lqf.setRepDate(gs.getReptDate());
				lqf.setOrgId(orgId);
				GraphFactory gf = new GraphFactory(gs);
				gf.createGraph(operator , gs);

				if(session.getAttribute("gs")!=null)
					session.removeAttribute("gs");
				session.setAttribute("gs",gs);
			}
			catch (Exception e)
			{
				e.printStackTrace();

			}

		}

		else if (request.getParameter("measure") != null)
		{ // 处理根据参数输入的指标集查询
		
			GraphDataSource gs = (GraphDataSource)session.getAttribute("gs");
			GraphFactory gf = new GraphFactory(gs);
			
			LendingQueryForm lqf = new LendingQueryForm();

			RequestUtils.populate(lqf, request);

			if (lqf.getRepDate() != null && !lqf.getRepDate().equals("")){
				gs.setReptDate(lqf.getRepDate());
				
			}
			if(!StringUtil.isEmpty(lqf.getOrgId())){
				gs.setOrgId(lqf.getOrgId());
			}
			if(!StringUtil.isEmpty(lqf.getOrgName())){
				gs.setOrgName(lqf.getOrgName());
			}

			gf = new GraphFactory(gs);
			
			gf.createGraph(operator , gs);
			session.setAttribute("gs",gs);
		}

		return mapping.findForward("view_query.jsp");

	}
}
		
		

	

