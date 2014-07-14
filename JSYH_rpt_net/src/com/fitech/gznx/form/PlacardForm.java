package com.fitech.gznx.form;
import java.text.DecimalFormat;
import java.util.List;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;



/**
 * 公告Form
 * @author db2admin
 *
 */
public class PlacardForm extends ActionForm
{
    private Long placardId;
    private String title;
    private String contents;
    private String publicUserId;
    private String publicDate;
    private Integer fileId;
    private FormFile placardFile;
    private String startDate;
    private String endDate;
    private String userIdStr; 
    
    private String fileName;
    private Integer fileSize;
    private String fileSizeStr;
    private Integer flag;
    private String[] userList;
    
	/**
	 * 查询条件
	 */
    private String queryTerm;
	/**
	 * 针对用户Id列表
	 */
	private List userIdList;
	/**
	 * 针对用户查看详细信息
	 */
	private List userViewList;
	
    public String getContents()
	{
		return contents;
	}
	public void setContents(String contents)
	{
		this.contents = contents;
	}
	
	public FormFile getPlacardFile()
	{
		return placardFile;
	}
	public void setPlacardFile(FormFile placardFile)
	{
		this.placardFile = placardFile;
	}
	public Integer getFileId()
	{
		return fileId;
	}
	public void setFileId(Integer fileId)
	{
		this.fileId = fileId;
	}
	public Long getPlacardId()
	{
		return placardId;
	}
	public void setPlacardId(Long placardId)
	{
		this.placardId = placardId;
	}
	public String getPublicDate()
	{
		return publicDate;
	}
	public void setPublicDate(String publicDate)
	{
		this.publicDate = publicDate;
	}
	public String getPublicUserId()
	{
		return publicUserId;
	}
	public void setPublicUserId(String publicUserId)
	{
		this.publicUserId = publicUserId;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public String getEndDate()
	{
		return endDate;
	}
	public void setEndDate(String endDate)
	{
		this.endDate = endDate;
	}
	public String getStartDate()
	{
		return startDate;
	}
	public void setStartDate(String startDate)
	{
		this.startDate = startDate;
	}
	public String getUserIdStr()
	{
		return userIdStr;
	}
	public void setUserIdStr(String userIdStr)
	{
		this.userIdStr = userIdStr;
	}
	public String getQueryTerm()
	{
		return queryTerm;
	}
	public void setQueryTerm(String queryTerm)
	{
		this.queryTerm = queryTerm;
	}
	public List getUserIdList()
	{
		return userIdList;
	}
	public void setUserIdList(List userIdList)
	{
		this.userIdList = userIdList;
	}
	public List getUserViewList()
	{
		return userViewList;
	}
	public void setUserViewList(List userViewList)
	{
		this.userViewList = userViewList;
	}
	public String getFileName()
	{
		return fileName;
	}
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}
	public Integer getFileSize()
	{
		return fileSize;
	}
	public void setFileSize(Integer fileSize)
	{
		this.fileSize = fileSize;
	}
	
	public String getFileSizeStr()
	{
		return converFileSize(this.fileSize);
	}
	public Integer getFlag()
	{
		return flag;
	}
	public void setFlag(Integer flag)
	{
		this.flag = flag;
	}
	/**
	 * 将以字节为单位的文件大小，转换成KB或者M
	 * 
	 * @author db2admin
	 * @date 2007-4-4
	 * @param fileSize
	 * 
	 */
	private String converFileSize(Integer fileSize) {
		String sizeStr = "";
		if (fileSize != null) {
			double fileSizeNum = fileSize.doubleValue();
			DecimalFormat numFormat = new DecimalFormat("0.00");
			if (fileSizeNum < 1024)
				sizeStr = fileSizeNum + "B";
			if (fileSizeNum >= 1024 && fileSizeNum < 1024 * 1024)
				sizeStr = numFormat.format(fileSizeNum / 1024) + "KB";
			else if (fileSizeNum >= (1024 * 1024))
				sizeStr = numFormat.format(fileSizeNum / (1024 * 1024)) + "M";

		}
		return sizeStr;
	}
	public String[] getUserList() {
		return userList;
	}
	public void setUserList(String[] userList) {
		this.userList = userList;
	}
	
}