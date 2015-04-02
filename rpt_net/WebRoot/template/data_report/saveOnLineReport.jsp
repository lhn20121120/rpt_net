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
	//请求类型，校验 validate or 报送  or 修改
	String requestType = request.getParameter("type") != null ? request.getParameter("type") : "";

	myUpload.initialize(pageContext);
	myUpload.setMaxFileSize(1048576);
	myUpload.upload();

	com.jspsmart.upload.File myFile = myUpload.getFiles().getFile(0);

	//Excel文件保存目录
	String excelFilePath = Config.getCollectExcelFolder()  + File.separator  + year + "_" + term + File.separator +  orgId;

	File excelFile = new File(excelFilePath);
	if (!excelFile.exists())
		excelFile.mkdirs();

	String fileName = childRepId + "_" + versionId + "_"+dataRangeId+"_"+curId+".xls";
	excelFilePath = excelFilePath + File.separator + fileName;

	myFile.saveAs(excelFilePath);
	
	//存放相关信息
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
	//checkFlag 4为保存状态, 
//	reportInForm.setCheckFlag(new Short("4"));
	//当前操作类型 1－表示在线填报 2－表示在线修改
	int operateType = 1;
	
	try
	{
		Integer repInId = null;
		//无则是第一次保存,则讲报表基本信息先入库
		if(request.getParameter("repInId")==null)
		{	
			//将基本信息入库
			ReportIn reportIn = StrutsReportInDelegate.create(reportInForm); 
			if(reportIn!=null)
				repInId = reportIn.getRepInId();
		}
		//有报表ID参数，表示是在线修改
		else
		{
			repInId = new Integer(request.getParameter("repInId"));
			operateType  = 2;
		}
	
		if (repInId != null)
		{
			
			/*将Excel文件解析入库*/
			ReportExcelHandler excelHandler = new ReportExcelHandler(repInId, excelFilePath);
			boolean result = excelHandler.copyExcelToDB();

			if (result)
			{
				// System.out.println("校验开始==" + new Date());
				boolean validateResult =Procedure.runBNJY(repInId);
				// System.out.println("校验结束==" + new Date());
				//校验
				if (requestType.equals("validate"))
				{
					StrutsReportInDelegate.updateReportInCheckFlag(repInId,
					com.fitech.net.config.Config.CHECK_FLAG_AFTERJY);
		
					if (validateResult == true)
					{
						messages.add("数据校验成功！");
					}
					else
					{
						messages.add("数据校验失败！");
					}
				}
				//报送
				else
				{
					if (validateResult == true)
					{
						messages.add("报送成功！");
					}
					else
					{
						messages.add("报送失败！");
					}
				}

			}
			//如果Excel数据入库失败，则删除插入的基本信息
			else
			{
				//如果是在线填报，解析Excel数据失败后则清除插入的基本信息，在线修改则不用
				if(operateType==1)
					//删除已经插入基础的数据
					StrutsReportInDelegate.remove(repInId);
			}
		}
		else
		{
			messages.add("系统忙，请稍后访问！");
		}
	}
	catch (Exception ex)
	{

		messages.add("系统忙，请稍后访问！");
	}

	if (messages != null && messages.getSize() != 0)
		request.setAttribute(Config.MESSAGE, messages);
%>
