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
		   //����ѯ����
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
				   list.add("��������ȷ��ģ�����Ͷ�Ӧ�İ汾��");
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
					   list.add("�ļ�������");
				   if(result==2)
					   list.add("�кŲ�������");
				   if(result==3)
					   list.add("�����в��ܲμӼ���");
				   
					 if(result==4)
						 list.add("��ʽ����");
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
		boolean result=false;				//��result���
		obtaintext FormulaData=new obtaintext();
		 if (FormulaData==null ) 
		   {
			   return  result;
		   }
//		���Ӷ���ĳ�ʼ��
		   DBConn conn=null;
		   //�Ự����ĳ�ʼ��
		   Session session=null;
		   try
		   {
			   
			   
			   copyVoToPersistence(FormulaData,addFormulaForm);
			   //ʵ�������Ӷ���
			   conn =new DBConn();
			   //�Ự����Ϊ���Ӷ������������
			   session=conn.beginTransaction();
			  
			   //�Ự���󱣴�־ò����
			   session.save(FormulaData);
			   session.flush();
			   
			  
			   result=true;
		   }
		   catch(HibernateException e)
		   {
			   e.printStackTrace() ;
		   }
		   finally{
			   //�������״̬��,��Ͽ�,��������,����
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
