package com.fitech.net.obtain.text;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechMessages;

public class AddFormulaAction  extends Action{

	public ActionForward execute(ActionMapping mapping, 
			ActionForm form,
			HttpServletRequest request, 
			HttpServletResponse response)
			throws IOException, ServletException {
//		Locale locale = this.getLocale(request);
//		   MessageResources resources=getResources(request);
		   FitechMessages messages = new FitechMessages();
		   //将查询条件
		   AddFormulaForm addFormulaForm = (AddFormulaForm) form;
		   RequestUtils.populate(addFormulaForm,request);
		  
		   boolean insertResult = false;		  
		   request.removeAttribute("Message");
		 
		   try
		   {
			   int count=StructsSearchFormulaDelegate.getReportCount(addFormulaForm.getChildReportId().trim(),addFormulaForm.getVersionId().trim(),null);			  
			   if (count<1)
			   {
				   ArrayList list=new ArrayList();
				   list.add("请输入真确的模板名和对应的版本号");
				   messages.setMessages(list);
				   request.setAttribute("Message",messages);
			   }
			   else
			   {
				   
			   manage man =new manage();
			   man.setFormula(addFormulaForm.getFormula());
			   man.setFilename(addFormulaForm.getDataSourceEname());
			   man.setGigoucolumn(Integer.parseInt(addFormulaForm.getOrgId().trim()));
			   int result=0;
			   ArrayList list=new ArrayList();
			   try
			   {
			   result=man.check();
			   }
			   catch(Exception e)
			   {
				   result=4;
			   }
			   if(result!=0 && result!=1)
			   {
				   insertResult=false;
				  
				   if(result==1)
					   list.add("文件不存在");
				   if(result==2)
					   list.add("列号不是数字");
				   if(result==3)
					   list.add("机构列不能参加计算");
				   
					 if(result==4)
						 list.add("公式不对");
				   messages.setMessages(list);
				   request.setAttribute("Message",messages);
			   }
			   else
			   {
				   
				 //  addFormulaForm.setChildReportId(StructsSearchFormulaDelegate.getChildPepId(addFormulaForm.getVersionId().trim(),addFormulaForm.getReportname().trim()));
			   insertResult=create(addFormulaForm);
			   }
			   }
		   }
		   catch(Exception e)
		   {
			   e.printStackTrace();
		   }
		   if (insertResult==false)
		   {
			  
			   return  mapping.findForward("addformula");
			  
		   }
		   else
		   {
			 
			 return  new ActionForward("/obtain/text/viewformula2.do");
		   }
		   
		
	}
	private boolean create(AddFormulaForm addFormulaForm)
	{
		boolean result=false;				//置result标记
		obtaintext FormulaData=new obtaintext();
		 if (FormulaData==null ) 
		   {
			   return  result;
		   }
//		连接对象的初始化
		   DBConn conn=null;
		   //会话对象的初始化
		   Session session=null;
		   try
		   {
			   
			   
			   copyVoToPersistence(FormulaData,addFormulaForm);
			   //实例化连接对象
			   conn =new DBConn();
			   //会话对象为连接对象的事务属性
			   session=conn.beginTransaction();
			  
			   //会话对象保存持久层对象
			   session.save(FormulaData);
			   session.flush();
			   
			  
			   result=true;
		   }
		   catch(HibernateException e)
		   {
			   e.printStackTrace() ;
		   }
		   finally{
			   //如果连接状态有,则断开,结束事务,返回
			   if(conn!=null) conn.endTransaction(result);
		   }
		   return result;
		
	}

	private void copyVoToPersistence(obtaintext  FormulaData,AddFormulaForm addFormulaForm)
	{
		FormulaData.setChildReportId(addFormulaForm.getChildReportId());
		FormulaData.setDataSourceCname(addFormulaForm.getDataSourceCname());
		FormulaData.setDataSourceEname(addFormulaForm.getDataSourceEname());
		FormulaData.setDes(addFormulaForm.getDes());
		FormulaData.setFlag(addFormulaForm.getFlag());
		FormulaData.setFormula(addFormulaForm.getFormula());
		//FormulaData.setReportname(addFormulaForm.getReportname());
		FormulaData.setRowColumn(addFormulaForm.getRowColumn());
		FormulaData.setSplitChar(addFormulaForm.getSplitChar());
		FormulaData.setVersionId(addFormulaForm.getVersionId());
		FormulaData.setFlag("0");
		FormulaData.setOrgId(addFormulaForm.getOrgId());
		
	}
}
