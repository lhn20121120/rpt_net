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

/**
 * �����ù�ʽ,����ҵ��ϵͳ���ɹ�ʽ
 * 
 * @author yaojie
 */
public final class SaveDataRelationFormualSettingActionNEW extends Action
{
	private static FitechException log = new FitechException(SaveDataRelationFormualSettingActionNEW.class);
	

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException
	{
		String curPage = request.getParameter("curPage") != null ? request.getParameter("curPage") : "1";
		FitechMessages messages = new FitechMessages();

		/*��Ԫ��Id*/
		String idrIdStr = "";
		/*��Ԫ������*/
		String cellName ="";
		/*��ʽ*/
		String formulaStr = "";		
		
		/* �����Ӧ�Ĳ��� */
		if (request.getParameter("IdrId") != null)
		{
			idrIdStr = request.getParameter("IdrId");
		}
		if (request.getParameter("idrFormula") != null)
		{
			formulaStr = request.getParameter("idrFormula");
		}
		/*��Ԫ��Id*/
		Integer idrId = null;
		if(idrIdStr!=""){
			idrId=Integer.valueOf(idrIdStr);
		}		
		MCell mCell = StrutsMCellDelegate.getMCell(idrId);
		/*��Ԫ������*/
		cellName = mCell != null ? mCell.getCellName() : "";

	
		
		if(idrId!=null && cellName!=null && formulaStr!=null && !formulaStr.equals(""))
		{
			try
			{
				if(formulaStr!=null && !formulaStr.equals(""))
				{
					/*�鿴Session����û�����ù�����Ϣ*/
					HttpSession session = request.getSession();
					HashMap sessionMap = null;
					if(session.getAttribute(Config.DATA_RELATION_IS_SET)!=null)
						sessionMap = (HashMap)session.getAttribute(Config.DATA_RELATION_IS_SET);
					else
						sessionMap = new HashMap();
					
					if(sessionMap!=null)
					{
						/*��������ù�����Ϣ,����¸���Ϣ,û�����½�*/
						IDataRelationForm setInfo = null;
						if(!sessionMap.containsKey(idrId))
							setInfo = new IDataRelationForm();
						else
							setInfo = (IDataRelationForm)sessionMap.get(idrId);
						setInfo.setCellName(cellName);
						setInfo.setIdrId(idrId);
						setInfo.setIdrFormula(formulaStr);
//			setInfo.setIdrSql(formulaStrParsed);
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
					messages.add("���湫ʽʧ��!");
					request.setAttribute("IdrFormula",formulaStr);
					request.setAttribute("IdrId",idrId);
					request.setAttribute("CellName",cellName);
					return mapping.findForward("fail");
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				messages.add("���湫ʽʧ��!");
				request.setAttribute("IdrFormula",formulaStr);
				request.setAttribute("IdrId",idrId);
				request.setAttribute("CellName",cellName);
				return mapping.findForward("fail");
			}		
		}
		else
		{
			messages.add("����ʧ��!");
			request.setAttribute("IdrFormula",formulaStr);
			request.setAttribute("IdrId",idrId);
			request.setAttribute("CellName",cellName);
			return mapping.findForward("fail");
		}
	}
}