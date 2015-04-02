package com.cbrc.smis.action;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.entity.AfTemplateReview;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.service.AFTemplateReviewDelegate;

public class AddTemplateReviewAction extends Action{

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AFReportForm reportInForm = (AFReportForm)form ;
		
		String msg = "";
		String type = request.getParameter("type");
		String templateIdAndVersionIdStr = request.getParameter("templateIdAndVersionIdStr");
		
		
		if(type.equals("one")){//单个校验
			AfTemplateReview review = new AfTemplateReview();
			review.getId().setTemplateId(reportInForm.getTemplateId());
			review.getId().setVersionId(reportInForm.getVersionId());
			review.getId().setTerm(reportInForm.getDate());
			review.setReviewStatus(Config.CHECK_FLAG_PASS.toString());//以审阅
			review.setReviewDate(getCurrDate());
			try {
				AFTemplateReviewDelegate.insertAfTemplateReview(review);
				msg = "SUCCESS";
			} catch (Exception e) {
				msg = review.getId().getTemplateId()+"审阅失败";
				e.printStackTrace();
			}	
		}
		if(type.equals("more")){
			boolean isSuccess = true;
			String[] strs = templateIdAndVersionIdStr.split("&");//分割成多个 模板编号_版本号  形式
			for(String s : strs){
				AfTemplateReview review = new AfTemplateReview();
				review.getId().setTemplateId(s.split("_")[0]);//模板编号
				review.getId().setVersionId(s.split("_")[1]);//版本号
				review.getId().setTerm(reportInForm.getDate());
				review.setReviewStatus(Config.CHECK_FLAG_PASS.toString());//以审阅
				review.setReviewDate(getCurrDate());
				try {
					AFTemplateReviewDelegate.insertAfTemplateReview(review);
					//msg = "<script>alert('审阅成功')</script>";
				} catch (Exception e) {
					msg += s.split("_")[0]+"审阅失败!";
					e.printStackTrace();
					isSuccess = false;
				}
			}
			if(isSuccess){
				msg = "SUCCESS";
			}
		}
		request.getSession().setAttribute("msg", msg);
		// TODO Auto-generated method stub
		ActionForward forword = new ActionForward("/leadReviewAction.do?date="+reportInForm.getDate());
		forword.setRedirect(true);
		return forword;
	}
	
	private String getCurrDate(){
		Calendar c = Calendar.getInstance();
		String year = String.valueOf(c.get(Calendar.YEAR));
		String month = String.valueOf((c.get(Calendar.MONTH)+1)<10?"0"+(c.get(Calendar.MONTH)+1):(c.get(Calendar.MONTH)+1));
		String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
		return year+"-"+month+"-"+day;
	}
	
}
