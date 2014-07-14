package com.fitech.net.action;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.adapter.StrutsMCellDelegate;
import com.cbrc.smis.hibernate.MCell;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.net.adapter.StrutsFormulaDelegate;
import com.fitech.net.common.ETLXmlToXml;
import com.fitech.net.config.Config;
import com.fitech.net.form.IDataRelationForm;
/**
 *	�鿴ҵ��ϵͳ���ɹ�ʽ
 *
 * @author yaojie
 */
public final class ViewDataRelationFormualSettingActionNEW extends Action
{
	private static FitechException log = new FitechException(ViewDataRelationFormualSettingActionNEW.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException
	{
		FitechMessages messages = new FitechMessages();
		IDataRelationForm IDataRelationForm = new IDataRelationForm();
		
		RequestUtils.populate(IDataRelationForm, request);
		/*ȡ�ô������ĵ�Ԫ��Id*/
		Integer idrId = IDataRelationForm.getIdrId();
		
		MCell mCell = StrutsMCellDelegate.getMCell(idrId);
		/*��Ԫ������*/
		String cellName = mCell != null ? mCell.getCellName() : "";
		
		if(idrId!=null && cellName!=null)
		{
			//�����������ǰ̨
			request.setAttribute("IdrId",idrId);
			request.setAttribute("CellName",cellName);
			
			HttpSession session = request.getSession();
			/*ȡ�ô����Session�е��Ѿ����õ����ݹ�ϵ*/
			if(session.getAttribute(Config.DATA_RELATION_IS_SET)!=null);
			{
				Map dataRelationMap = (Map)session.getAttribute(Config.DATA_RELATION_IS_SET);
				/*�鿴�õ�Ԫ���Ƿ����ù����ݹ�ϵ,������ù���ϵ��ȡ���Ѿ����õĹ�ϵ*/
				
				if(dataRelationMap!=null && dataRelationMap.containsKey(String.valueOf(idrId)))
				{
					IDataRelationForm setInfo = (IDataRelationForm)dataRelationMap.get(String.valueOf(idrId));
					/*ȡ���Ѿ��趨�Ĺ�ʽ*/
					String formulaStr = setInfo.getIdrFormula();
					if(formulaStr!=null)
						request.setAttribute("IdrFormula",formulaStr);
				}
			}
			//ETL�������ļ�ת���ɿ������ṹ��ʾ��XML
			ETLXmlToXml.CreateETLXml(ETLXmlToXml.ParserXML());
			//����һ����ʾָ������ṹ
			List targetList=StrutsFormulaDelegate.selectTargetFormula();
			if(targetList!=null )
			ETLXmlToXml.createETLIndexXml(targetList);
			
			return mapping.findForward("view");	
		}
		else
		{
			messages.add("����ʧ��!");
			return new ActionForward("");
		}
	}
}