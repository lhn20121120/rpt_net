package com.cbrc.smis.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.org.form.AFDataTraceForm;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.entity.AFDataTrace;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.service.IAFDataTraceService;
import com.cbrc.smis.service.impl.AFDataTraceServiceImpl;
import com.cbrc.smis.util.FitechLog;
import com.sun.org.apache.commons.beanutils.BeanUtils;

public class AddAFDataTraceAction extends Action{

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		//String[] list = request.getParameterValues("dataList");
		//单元格名称
		String[] cellNames = request.getParameterValues("cellName");
		//原始值
		String[] originalDatas = request.getParameterValues("originalData");
		//修改值
		String[] changeDatas = request.getParameterValues("changeData");
		//最终值
		String[] finalDatas = request.getParameterValues("finalData");
		String reportFlg = "0";
		HttpSession session = request.getSession();
		if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
			reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
		}
		//备注
		String[] descs = request.getParameterValues("desc");
		String repInId = request.getParameter("repInId");
		
		Operator operator = null; 
		if(request.getSession().getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
			operator = (Operator)request.getSession().getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);     
		if(operator==null)
			return null;
		
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");
		String toDay = sdf.format(date);
		IAFDataTraceService traceService = new AFDataTraceServiceImpl();
		if(cellNames!=null && originalDatas!=null && changeDatas!=null && finalDatas!=null && descs!=null){
			for(int i=0;i<cellNames.length;i++){
				String cellName = cellNames[i];
				String originalData = originalDatas[i];
				String changeData = changeDatas[i];
				String finalData = finalDatas[i];
				
				String desc = java.net.URLDecoder.decode(descs[i],"UTF-8");
				
				AFDataTrace af = new AFDataTrace();
				af.setCellName(cellName);
				af.setOriginalData(originalData);
				af.setChangeData(changeData);
				af.setFinalData(finalData);
				af.setDescTrace(desc);
				af.setDateTime(toDay);
				af.setRepInId(repInId);
				af.setUsername(operator.getUserName());
				af.setStatus(0);
				try {
					traceService.addAFDataTrace(af);
					//第一个参数1为增加数据痕迹记录日志
					FitechLog.writeTraceLog(1, request, af, true ,reportFlg);
				} catch (Exception e) {
					e.printStackTrace();
					//第一个参数1为增加数据痕迹记录日志
					FitechLog.writeTraceLog(1, request, af, false , reportFlg);
				}
			}
		}		
		return null;
	}
	
}
