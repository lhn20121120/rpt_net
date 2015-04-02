<%@ page contentType="text/html; charset=gbk" language="java"%>
<%@ page import="javax.servlet.jsp.PageContext"%>
<%@ page import="com.fitech.net.config.Config"%>
<%@ page import="java.io.File"%>
<%@ page import="com.cbrc.smis.form.ReportInForm"%>
<%@ page import="com.cbrc.smis.system.cb.InputData"%>
<jsp:directive.page import="com.cbrc.smis.adapter.StrutsReportInDelegate" />
<jsp:directive.page import="com.cbrc.smis.hibernate.ReportIn" />
<jsp:directive.page import="com.cbrc.smis.util.ReportExcelHandler" />
<jsp:directive.page import="com.cbrc.smis.adapter.Procedure" />
<jsp:directive.page import="java.util.Date" />
<jsp:directive.page import="com.cbrc.smis.util.FitechMessages" />
<jsp:directive.page import="java.text.SimpleDateFormat"/>
<jsp:useBean id="myUpload" scope="page" class="com.jspsmart.upload.SmartUpload" />
<%
	response.addHeader("Pragma", "no-cache");
	response.addHeader("Cache-Control", "no-cache");
	response.addHeader("Expires", "Thu,01 Jan 1970 00:00:01 GMT");
%>
<%
	FitechMessages messages = new FitechMessages();
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	
	String orgId = request.getParameter("orgId");
	String year = request.getParameter("year") != null ? request.getParameter("year") : "";
	String term = request.getParameter("term") != null ? request.getParameter("term") : "";
	String versionId = request.getParameter("versionId");
	String childRepId = request.getParameter("childRepId");
	String curId = request.getParameter("curId") != null ? request.getParameter("curId") : "";
	String dataRangeId = request.getParameter("dataRangeId") != null ? request.getParameter("dataRangeId") : "";
	String repName = request.getParameter("repName") != null ? request.getParameter("repName") : "";
	if (com.cbrc.smis.common.Config.WEB_SERVER_TYPE == 1) {
			repName = new String(repName.getBytes("iso-8859-1"), "gb2312");
	}
	//�������ͣ�У�� validate or ����  or �޸�
	String requestType = request.getParameter("type") != null ? request.getParameter("type") : "";

	myUpload.initialize(pageContext);
	myUpload.setMaxFileSize(1048576);
	myUpload.upload();

	com.jspsmart.upload.File myFile = myUpload.getFiles().getFile(0);

	//Excel�ļ�����Ŀ¼
	String excelFilePath = Config.getCollectExcelFolder()  + File.separator  + year + "_" + term + File.separator +  orgId;

	File excelFile = new File(excelFilePath);
	if (!excelFile.exists())
		excelFile.mkdirs();

	String fileName = childRepId + "_" + versionId + "_"+dataRangeId+"_"+curId+".xls";
	excelFilePath = excelFilePath + File.separator + fileName;

	myFile.saveAs(excelFilePath);
	
	//��������Ϣ
	ReportInForm reportInForm = new ReportInForm();
	reportInForm.setOrgId(orgId);
	reportInForm.setChildRepId(childRepId);
	reportInForm.setVersionId(versionId);
	reportInForm.setYear(new Integer(year));
	reportInForm.setTerm(new Integer(term));
	reportInForm.setDataRangeId(new Integer(dataRangeId));
	reportInForm.setCurId(new Integer(curId));
	reportInForm.setTimes(new Integer(1));
	reportInForm.setReportDate(new Date());
	reportInForm.setRepName(repName.trim());
	//checkFlag 4Ϊ����״̬, 
//	reportInForm.setCheckFlag(new Short("4"));
	//��ǰ�������� 1����ʾ����� 2����ʾ�����޸�
	int operateType = 1;
	
	try
	{
		Integer repInId = null;
		//�����ǵ�һ�α���,�򽲱��������Ϣ�����
		if(request.getParameter("repInId")==null)
		{	
			//��������Ϣ���
			ReportIn reportIn = StrutsReportInDelegate.create(reportInForm); 
			if(reportIn!=null)
				repInId = reportIn.getRepInId();
		}
		//�б���ID��������ʾ�������޸�
		else
		{
			repInId = new Integer(request.getParameter("repInId"));
			operateType  = 2;
		}
	
		if (repInId != null)
		{
			
			/*��Excel�ļ��������*/
			ReportExcelHandler excelHandler = new ReportExcelHandler(repInId, excelFilePath);
			boolean result = excelHandler.copyExcelToDB();

			if (result)
			{
				// System.out.println("У�鿪ʼ==" + new Date());
				boolean validateResult =Procedure.runBNJY(repInId);
				// System.out.println("У�����==" + new Date());
				//У��
				if (requestType.equals("validate"))
				{
					StrutsReportInDelegate.updateReportInCheckFlag(repInId,
					com.fitech.net.config.Config.CHECK_FLAG_AFTERJY);
		
					if (validateResult == true)
					{
						messages.add("����У��ɹ���");
					}
					else
					{
						messages.add("����У��ʧ�ܣ�");
					}
				}
				//����
				else
				{
					if (validateResult == true)
					{
						messages.add("���ͳɹ���");
					}
					else
					{
						messages.add("����ʧ�ܣ�");
					}
				}

			}
			//���Excel�������ʧ�ܣ���ɾ������Ļ�����Ϣ
			else
			{
				//����������������Excel����ʧ�ܺ����������Ļ�����Ϣ�������޸�����
				if(operateType==1)
					//ɾ���Ѿ��������������
					StrutsReportInDelegate.remove(repInId);
			}
		}
		else
		{
			messages.add("ϵͳæ�����Ժ���ʣ�");
		}
	}
	catch (Exception ex)
	{

		messages.add("ϵͳæ�����Ժ���ʣ�");
	}

	if (messages != null && messages.getSize() != 0)
		request.setAttribute(Config.MESSAGE, messages);
%>
