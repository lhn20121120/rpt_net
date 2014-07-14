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
 *	查看业务系统生成公式
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
		/*取得传过来的单元格Id*/
		Integer idrId = IDataRelationForm.getIdrId();
		
		MCell mCell = StrutsMCellDelegate.getMCell(idrId);
		/*单元格名称*/
		String cellName = mCell != null ? mCell.getCellName() : "";
		
		if(idrId!=null && cellName!=null)
		{
			//所需参数传给前台
			request.setAttribute("IdrId",idrId);
			request.setAttribute("CellName",cellName);
			
			HttpSession session = request.getSession();
			/*取得存放在Session中的已经设置的数据关系*/
			if(session.getAttribute(Config.DATA_RELATION_IS_SET)!=null);
			{
				Map dataRelationMap = (Map)session.getAttribute(Config.DATA_RELATION_IS_SET);
				/*查看该单元格是否设置过数据关系,如果设置过关系则取出已经设置的关系*/
				
				if(dataRelationMap!=null && dataRelationMap.containsKey(String.valueOf(idrId)))
				{
					IDataRelationForm setInfo = (IDataRelationForm)dataRelationMap.get(String.valueOf(idrId));
					/*取得已经设定的公式*/
					String formulaStr = setInfo.getIdrFormula();
					if(formulaStr!=null)
						request.setAttribute("IdrFormula",formulaStr);
				}
			}
			//ETL的配置文件转换成可用树结构显示的XML
			ETLXmlToXml.CreateETLXml(ETLXmlToXml.ParserXML());
			//创建一个显示指标的树结构
			List targetList=StrutsFormulaDelegate.selectTargetFormula();
			if(targetList!=null )
			ETLXmlToXml.createETLIndexXml(targetList);
			
			return mapping.findForward("view");	
		}
		else
		{
			messages.add("设置失败!");
			return new ActionForward("");
		}
	}
}