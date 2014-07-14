package com.fitech.net.hibernate;

/**
 *描述：
 *日期：2008-1-27
 *作者：吴昊
 */
public class ReprotExcelMappingKey  implements java.io.Serializable
{

	// Fields

	private String excelId;

	private String reportId;

	private String versionId;

	// Constructors

	/** default constructor */
	public ReprotExcelMappingKey()
	{
	}

	/** full constructor */
	public ReprotExcelMappingKey(String excelId, String reportId, String versionId)
	{
		this.excelId = excelId;
		this.reportId = reportId;
		this.versionId = versionId;
	}

	// Property accessors

	public String getExcelId()
	{
		return this.excelId;
	}

	public void setExcelId(String excelId)
	{
		this.excelId = excelId;
	}

	public String getReportId()
	{
		return this.reportId;
	}

	public void setReportId(String reportId)
	{
		this.reportId = reportId;
	}

	public String getVersionId()
	{
		return this.versionId;
	}

	public void setVersionId(String versionId)
	{
		this.versionId = versionId;
	}

	public boolean equals(Object other)
	{
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ReprotExcelMappingKey))
			return false;
		ReprotExcelMappingKey castOther = (ReprotExcelMappingKey) other;

		return ((this.getExcelId() == castOther.getExcelId()) || (this.getExcelId() != null && castOther.getExcelId() != null && this.getExcelId().equals(
				castOther.getExcelId())))
				&& ((this.getReportId() == castOther.getReportId()) || (this.getReportId() != null && castOther.getReportId() != null && this.getReportId()
						.equals(castOther.getReportId())))
				&& ((this.getVersionId() == castOther.getVersionId()) || (this.getVersionId() != null && castOther.getVersionId() != null && this
						.getVersionId().equals(castOther.getVersionId())));
	}

	public int hashCode()
	{
		int result = 17;

		result = 37 * result + (getExcelId() == null ? 0 : this.getExcelId().hashCode());
		result = 37 * result + (getReportId() == null ? 0 : this.getReportId().hashCode());
		result = 37 * result + (getVersionId() == null ? 0 : this.getVersionId().hashCode());
		return result;
	}

}