package com.cbrc.smis.action;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.MessageResources;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MChildReportForm;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechPDF;
import com.cbrc.smis.util.FitechPDFReport;
import com.cbrc.smis.util.FitechResource;
import com.cbrc.smis.util.FitechUtil;
/**
 * ������ģ���ļ�������������
 * 
 * @author rds
 * @date 2005-12-5
 */
public class UploadTemplateAction extends Action {
	/**
	 * Performs action.
	 * @param mapping Action mapping.
	 * @param form Action form.
	 * @param request HTTP request.
	 * @param response HTTP response.
	 * @exception IOException if an input/output error occurs
	 * @exception ServletException if a servlet exception occurs
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		Locale locale=getLocale(request);
		MessageResources resources=getResources(request);
		
		FitechMessages messages=new FitechMessages();
		
		MChildReportForm mChildReportForm = (MChildReportForm) form;
		
		FormFile tmplFile=mChildReportForm.getTemplateFile();
		
		boolean flag=false;		
		Integer reportStyle=null;
		
		if(tmplFile!=null && tmplFile.getFileSize()>0){
			if(tmplFile.getContentType().equals(Config.FILE_CONTENT_TYPE_PDF)){
				if(tmplFile.getFileSize()<Config.FILE_MAX_SIZE){
					String tmpFileName=FitechUtil.writeFile(tmplFile.getInputStream(),Config.EXT_PDF);
					FitechPDF fitechPDF=new FitechPDF();
					if(fitechPDF.parse(tmplFile.getInputStream())==true){
						String title=fitechPDF.getValueByFieldName(FitechPDFReport.TITLE);
						String subTitle=fitechPDF.getValueByFieldName(FitechPDFReport.SUBTITLE);
						/*// System.out.println("title:" + title);
						// System.out.println("subTitle:" + subTitle);*/
						String curUnit=fitechPDF.getValueByFieldName(FitechPDFReport.CURUNIT);
						String version=fitechPDF.getValueByFieldName(FitechPDFReport.version);
						//String tmpFileName=FitechUtil.writeFile(tmplFile.getInputStream(),Config.EXT_PDF);
						/*// System.out.println("tmpFileName:" + tmpFileName);*/
						reportStyle=fitechPDF.getReportStyle();
						if(tmpFileName!=null){
							request.setAttribute("ReportTitle",title + (subTitle!=null && !subTitle.equals("")?"-" + subTitle:""));
							request.setAttribute("ReportCurUnit",curUnit);
							request.setAttribute("ReportVesion",version);
							request.setAttribute("TmpFileName",tmpFileName);
							request.setAttribute("ReportStyle",reportStyle);  //PDF����ģ�������
							//request.setAttribute("ReportName",title + (subTitle!=null && !subTitle.equals("")?"-" + subTitle:""));							
							/*// System.out.println("title:" + title);
							// System.out.println("subTitle:" + subTitle);.
							// System.out.println("curUnit:" + curUnit);
							// System.out.println("version:" + version);*/
							messages.add(FitechResource.getMessage(locale,resources,"upload.template.success"));
							flag=true;
						}else{  //���ϴ���PDFģ���ļ�������ʱĿ¼ʧ��
							messages.add(FitechResource.getMessage(locale,resources,"template.file.save.error"));
						}
					}else{  //����PDFģ�����
						messages.add(FitechResource.getMessage(locale,resources,"template.file.parse.error"));
					}
				}else{  //���صı���ģ���ļ�����1M
					messages.add(FitechResource.getMessage(locale,resources,"upload.template.extreme"));
				}
			}else{  //ѡ���ϴ��ı���ģ���ļ�����PDF���� 
				messages.add(FitechResource.getMessage(locale,resources,"template.file.contentType.error"));
			}
		}else{ //�ϴ��ı���ģ���ļ�������
			messages.add(FitechResource.getMessage(locale,resources,"get.upload.template.file.error"));
		}
		
		if(messages!=null && messages.getSize()>0) request.setAttribute(Config.MESSAGES,messages);
		
		if(flag==true)	//���뱨��ģ���ļ��ɹ������뱣�汣��ҳ��
			if(reportStyle.compareTo(Config.REPORT_STYLE_DD)==0){
				return mapping.findForward("saveTmpt");  //��Ե�ʽ����ı���
			}else{
				return mapping.findForward("saveTmptQD"); //�嵥ʽ����ģ��ı���
			}
		else	//���뱨��ģ���ļ�ʧ�ܣ�����ԭҳ��
			return mapping.getInputForward();
	}
}
