<%@ page contentType="text/html; charset=gbk" language="java"%>
<%@ page import="com.fitech.net.config.Config"%>
<%@ page import="java.io.File"%>
<jsp:directive.page import="com.cbrc.smis.adapter.StrutsReportInDelegate" />
<jsp:directive.page import="com.cbrc.smis.util.ReportExcelHandler" />
<jsp:directive.page import="com.cbrc.smis.util.FitechMessages" />
<jsp:useBean id="myUpload" scope="page" class="com.jspsmart.upload.SmartUpload" />
<%
   response.addHeader("Pragma", "no-cache");
   response.addHeader("Cache-Control", "no-cache");
   response.addHeader("Expires", "Thu,01 Jan 1970 00:00:01 GMT");
%>

<%
	FitechMessages messages = new FitechMessages();
	String orgId = request.getParameter("orgId");
	String repInId = request.getParameter("repInId");
	String year = request.getParameter("year") != null ? request.getParameter("year") : "";
	String term = request.getParameter("term") != null ? request.getParameter("term") : "";

	myUpload.initialize(pageContext);
	myUpload.setMaxFileSize(1048576);
	myUpload.upload();

	com.jspsmart.upload.File myFile = myUpload.getFiles().getFile(0);
	String fName = myUpload.getFiles().getFile(0).getFileName();
	String excelFilePath = Config.getCollectExcelFolder()  + File.separator  + year + "_" + term + File.separator +  orgId ;
	File excelFile = new File(excelFilePath);
	if (!excelFile.exists())
		excelFile.mkdirs();
    String fileName = excelFilePath + File.separator + fName;
	myFile.saveAs(fileName);

	try
	{
		Integer repIn_Id = null;
		if (repInId != null && !repInId.equals(""))
		{
			repIn_Id = Integer.valueOf(repInId);
			/*��Excel�ļ��������*/
			ReportExcelHandler excelHandler = new ReportExcelHandler(repIn_Id, fName);
			boolean result = excelHandler.copyExcelToDB(false);
			if (result)
			{
			/* �Ƿ��ֹ���ƽ��  1 ��ʾ�ѵ�ƽ,0.δ��ƽ*/
			Integer handFlag = new Integer(1);
			/* ���±�־(�Ƿ��ֹ���ƽ��)*/
			boolean blnHandFlag = StrutsReportInDelegate.updateReportInHandworkFlag(repIn_Id, handFlag);
			/* ���±�־( CheckFlag=3 δ���� )*/
			boolean blnCheckFlag = StrutsReportInDelegate.updateReportInCheckFlag(repIn_Id);
			if (blnHandFlag == true && blnCheckFlag == true)
				messages.add("�����޸ĳɹ���");
			}
			else
			{
				messages.add("�����޸�ʧ�ܣ�");
			}
		}
		else
		{
			messages.add("ϵͳæ�����Ժ���ʣ�");
			return;
		}
	}
	catch (Exception ex)
	{
		messages.add("ϵͳæ�����Ժ���ʣ�");
	}

	if (messages != null && messages.getSize() != 0)
		request.setAttribute(Config.MESSAGE, messages);
%>
