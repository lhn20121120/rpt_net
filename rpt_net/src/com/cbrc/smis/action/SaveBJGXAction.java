package com.cbrc.smis.action;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.adapter.StrutsMCellFormuDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MCellFormuForm;
import com.cbrc.smis.form.MChildReportForm;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.cbrc.smis.util.FitechTemplate;
import com.cbrc.smis.util.FitechUtil;
import com.gather.adapter.StrutsJieKou;

/**
 * 保存报表的表内表间关系设定
 * 
 * @author rds
 * @serialData 2005-12-08
 */
public class SaveBJGXAction extends Action
{
	/**
	 * Performs action.
	 * 
	 * @param mapping
	 *            Action mapping.
	 * @param form
	 *            Action form.
	 * @param request
	 *            HTTP request.
	 * @param response
	 *            HTTP response.
	 * @exception IOException
	 *                if an input/output error occurs
	 * @exception ServletException
	 *                if a servlet exception occurs
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException
	{

		Locale locale = getLocale(request);
		MessageResources resources = getResources(request);

		FitechMessages messages = new FitechMessages();

		MCellFormuForm mCellForumForm = (MCellFormuForm) form;
		RequestUtils.populate(mCellForumForm, request);

		HttpSession session = request.getSession();
		com.cbrc.smis.security.Operator operator = (com.cbrc.smis.security.Operator) session
				.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		String orgId = "";
		if (operator != null)
		{
			orgId = operator.getOrgId();
		}

		String expression = mCellForumForm.getExpression();
		// if(request.getParameter("expression")!=null)
		// expression=(String)request.getParameter("expression");
		// System.out.println(mCellForumForm.getExpression());
		boolean flag = false;

		try
		{
			FitechTemplate template = new FitechTemplate();

			if (mCellForumForm.getReportStyle().compareTo(Config.REPORT_STYLE_DD) == 0)
			{ // 点对点式
				flag = template.validate(mCellForumForm, expression, locale, resources);
			}
			else if (mCellForumForm.getReportStyle().compareTo(Config.REPORT_STYLE_QD) == 0)
			{ // 清单式
				flag = template.validateEsp(mCellForumForm, expression, locale, resources);
			}
			
			if (flag == true)
			{ // 表达式校验

				List cells = template.getMCellToFormuList();
				if (cells != null && cells.size() > 0)
				{
					flag = com.cbrc.smis.adapter.StrutsMCellFormuDelegate.savePatch(cells);
					// System.out.println("***入库！＝"+flag);
				}

				if (flag == true)
				{ // 表内表间设定成功

					messages.add(FitechResource.getMessage(locale, resources, "bjgx.set.success"));
					/** 将表内表间关系表达式同步到外网* */
					// synchrocnizedGather(messages,locale,resources,mCellForumForm);
				}
				else
				{ // 表内表间设定失败
					messages.add(FitechResource.getMessage(locale, resources, "bjgx.set.failed"));
				}
			}
			else
			{ // 获取关系表达式校验不通过的原因
				messages.add(template.getError());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			messages.add(FitechResource.getMessage(locale, resources, "errors.system"));
		}

		if (messages != null && messages.getSize() > 0)
			request.setAttribute(Config.MESSAGES, messages);

		String param = mapping.getParameter();
		FitechUtil.removeAttribute(mapping, request);

		if (flag == true)
		{ // 操作成功，进入报送范围设定页面
			String url = "";

			if (param.toUpperCase().equals("ADD") == true)
			{ // 新增操作完成，跳转到下一步骤
				String path = mapping.findForward("tbfwSet").getPath();
				if (mCellForumForm.getChildRepId() != null)
				{
					url += (url.equals("") ? "" : "&") + "childRepId=" + mCellForumForm.getChildRepId();
					request.setAttribute("childRepId", mCellForumForm.getChildRepId());
				}
				if (mCellForumForm.getVersionId() != null)
				{
					url += (url.equals("") ? "" : "&") + "versionId=" + mCellForumForm.getVersionId();
					request.setAttribute("versionId", mCellForumForm.getVersionId());
				}
				// // System.out.println("[SaveBJGXAction]ReportName:" +
				// mCellForumForm.getReportName());
				if (mCellForumForm.getReportName() != null)
				{
					url += (url.equals("") ? "" : "&") + "reportName=" + mCellForumForm.getReportName();
					request.setAttribute("ReportName", mCellForumForm.getReportName());
				}
				if (mCellForumForm.getReportStyle() != null)
				{
					url += (url.equals("") ? "" : "&") + "reportStyle=" + mCellForumForm.getReportStyle();
					request.setAttribute("ReportStyle", mCellForumForm.getReportStyle());
				}
				if (orgId != null)
				{
					url += (url.equals("") ? "" : "&") + "orgId=" + orgId;
					request.setAttribute("orgId", orgId);
				}
				// System.out.println("savebjgx" + path + "?" + url);
				return new ActionForward(path + "?" + url);
			}
			else
			{ // 维护追加操作完成，跳转报表PDF的列表信息
				// String path=mapping.findForward("view").getPath();
				MChildReportForm mc=(MChildReportForm)request.getSession().getAttribute("mChildReportForm");
				String path="/template/viewTemplateDetail.do?childRepId="
									+mc.getChildRepId()+"&&reportName="
								    +mc.getReportName()+"&&versionId="+mc.getVersionId()+"&&bak2="+mc.getBak2();
				//String path = "/template/viewMChildReport.do?reportName=&versionId=";
				String curPage = "";
				request.getSession().removeAttribute("mChildReportForm");
				if (request.getParameter("curPage") != null)
					curPage = (String) request.getParameter("curPage");
				System.out.println(path + "&curPage=" + curPage);
				return new ActionForward(path + "&curPage=" + curPage);
			}
		}
		else
		{ // 操作失败，返回原页面
			request.setAttribute("ObjForm", mCellForumForm);
			if (param.toUpperCase().equals("ADD") == true)
			{
				request.setAttribute("ReportName", mCellForumForm.getReportName());
				return mapping.getInputForward();
			}
			else
			{
				return new ActionForward(mapping.getInputForward().getPath() + "?curPage="
						+ (request.getParameter("curPage") == null ? "" : (String) request.getParameter("curPage")));
			}
		}
	}

	/**
	 * 模板发布，将模板的信息同步到外网
	 * 
	 * @return void
	 */
	private void synchrocnizedGather(FitechMessages messages, Locale locale, MessageResources resources,
			MCellFormuForm mCellForumForm)
	{
		// System.out.println("同步外网M_Cell_Formu表数据");
		List listFormu = StrutsMCellFormuDelegate
				.findAll(mCellForumForm.getChildRepId(), mCellForumForm.getVersionId());

		if (listFormu != null && listFormu.size() > 0)
		{
			MCellFormuForm _mCellFormuForm = null;
			for (int i = 0; i < listFormu.size(); i++)
			{
				_mCellFormuForm = (MCellFormuForm) listFormu.get(i);
				com.gather.struts.forms.MCellFormForm _mfForm = new com.gather.struts.forms.MCellFormForm();
				_mfForm.setCellFormId(_mCellFormuForm.getCellFormuId());
				_mfForm.setCellForm(_mCellFormuForm.getCellFormu());
				_mfForm.setFormType(_mCellFormuForm.getFormuType());
				if (StrutsJieKou.create(_mfForm) == false)
				{
					messages.add(FitechResource.getMessage(locale, resources, "synchrocnized.template.failed"));
					return;
				}
			}
		}
	}

	/*
	 * private List getCells(MCellFormuForm mCellForumForm,String expression)
	 * throws Exception{ List cells=null; // System.out.println("expression:" +
	 * expression); if(mCellForumForm==null || expression==null) return cells;
	 * 
	 * cells=new ArrayList();
	 * 
	 * String[] exps=expression.split(Config.SPLIT_SYMBOL_ESP);
	 * 
	 * if(exps==null) return cells;
	 * 
	 * for(int i=0;i<exps.length;i++){ String[]
	 * exp=exps[i].split(Config.SPLIT_SYMBOL_COMMA); Integer
	 * cellId=StrutsMCellDelegate.getCellId(mCellForumForm.getChildRepId(),
	 * mCellForumForm.getVersionId(), FitechUtil.getCellName(exp[0]));
	 * 
	 * if(cellId==null) break;
	 * 
	 * MCellFormuForm form=new MCellFormuForm();
	 * form.setChildRepId(mCellForumForm.getChildRepId());
	 * form.setVersionId(mCellForumForm.getVersionId());
	 * form.setCellFormu(exp[0]); //form.setCellId(cellId);
	 * form.setFormuType(Integer.valueOf(exp[1]));
	 * 
	 * cells.add(form); }
	 * 
	 * return cells;
	 *  }
	 */
}
