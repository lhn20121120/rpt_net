package com.fitech.net.action;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.adapter.StrutsMCellDelegate;
import com.cbrc.smis.hibernate.MCell;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.net.config.Config;
import com.fitech.net.form.IDataRelationForm;
import com.fitech.net.template.util.DataRelationFormulaParseUtil;

/**
 * 解析该公式,保存业务系统生成公式
 * 
 * @author yaojie
 */
public final class SaveDataRelationFormualSettingAction extends Action
{
	private static FitechException log = new FitechException(SaveDataRelationFormualSettingAction.class);
	

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException
	{
		String curPage = request.getParameter("curPage") != null ? request.getParameter("curPage") : "1";
		FitechMessages messages = new FitechMessages();
		IDataRelationForm IDataRelationForm = (IDataRelationForm) form;
		/*单元格Id*/
		Integer idrId = IDataRelationForm.getIdrId();
		/*单元格名称*/
		String cellName = IDataRelationForm.getCellName();
		/*公式*/
		String formulaStr = IDataRelationForm.getIdrFormula();		
		MCell mCell = StrutsMCellDelegate.getMCell(idrId);
		
		if(idrId!=null && cellName!=null && formulaStr!=null && !formulaStr.equals(""))
		{
			try
			{
				/*公式解析工具类*/
				DataRelationFormulaParseUtil formulaParseUtil = new DataRelationFormulaParseUtil();
				/*解析公式*/
				String formulaStrParsed = formulaParseUtil.parseFormualStr(formulaStr);
				
				if(formulaStrParsed!=null && !formulaStrParsed.equals(""))
				{
					/*查看Session中有没有设置过的信息*/
					HttpSession session = request.getSession();
					HashMap sessionMap = null;
					if(session.getAttribute(Config.DATA_RELATION_IS_SET)!=null)
						sessionMap = (HashMap)session.getAttribute(Config.DATA_RELATION_IS_SET);
					else
						sessionMap = new HashMap();
					
					if(sessionMap!=null)
					{
						/*如果有设置过的信息,则更新该信息,没有则新建*/
						IDataRelationForm setInfo = null;
						if(!sessionMap.containsKey(idrId))
							setInfo = new IDataRelationForm();
						else
							setInfo = (IDataRelationForm)sessionMap.get(idrId);
						setInfo.setCellName(cellName);
						setInfo.setIdrId(idrId);
						setInfo.setIdrFormula(formulaStr);
						setInfo.setIdrSql(formulaStrParsed);
						setInfo.setModify(true);
						setInfo.setIdrRelative(com.cbrc.smis.common.Config.RELATIIONYWXTSC);
						
						sessionMap.remove(idrId);
						sessionMap.put(String.valueOf(idrId),setInfo);
						
						session.setAttribute(Config.DATA_RELATION_IS_SET,sessionMap);												
					}
					return new ActionForward("/template/proTmpt.do?childRepId=" + mCell.getMChildReport().getComp_id().getChildRepId() 
							+ "&versionId=" + mCell.getMChildReport().getComp_id().getVersionId() + "&idrId="+idrId+"&curPage=" + curPage);
				}
				else
				{
					messages.add("解析公式失败!请检查公式!");
					request.setAttribute("IdrFormula",formulaStr);
					request.setAttribute("IdrId",idrId);
					request.setAttribute("CellName",cellName);
					return mapping.findForward("fail");
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				messages.add("解析公式失败!请检查公式!");
				request.setAttribute("IdrFormula",formulaStr);
				request.setAttribute("IdrId",idrId);
				request.setAttribute("CellName",cellName);
				return mapping.findForward("fail");
			}		
		}
		else
		{
			messages.add("保存失败!");
			request.setAttribute("IdrFormula",formulaStr);
			request.setAttribute("IdrId",idrId);
			request.setAttribute("CellName",cellName);
			return mapping.findForward("fail");
		}
	}
}