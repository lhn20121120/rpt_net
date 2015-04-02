package com.cbrc.smis.other;

import java.util.Date;

public class Aditing {
	private Integer repDay;
	
	private Date finalLaterDate; 
	
	private Date realRepTime;
	
	private Date sureRepTime;
	
	private Integer laterDay;
	
	public Date getRealRepTime() {
		return realRepTime;
	}
	public void setRealRepTime(Date realRepTime) {
		this.realRepTime = realRepTime;
	}
	public Date getSureRepTime() {
		return sureRepTime;
	}
	public void setSureRepTime(Date sureRepTime) {
		this.sureRepTime = sureRepTime;
	}
	public Integer getLaterDay() {
		return laterDay;
	}
	public void setLaterDay(Integer laterDay) {
		this.laterDay = laterDay;
	}
	public Integer getRepDay() {
		return repDay;
	}
	public void setRepDay(Integer repDay) {
		this.repDay = repDay;
	}
	public Date getFinalLaterDate() {
		return finalLaterDate;
	}
	public void setFinalLaterDate(Date finalLaterDate) {
		this.finalLaterDate = finalLaterDate;
	}
	//��������
	private String repName=null;
	//�ϱ����
	private Integer year=null;
	//�ϱ��·�
	private Integer term=null;
	//�ϱ�ʱ��
	private Date reportDate = null;
	//����ھ�
	private String dataRgTypeName=null;
	//	����ھ�ID
	private Integer dataRangeId = null;
	//����id
	private Integer dataRgId=null;
	//����Ƶ��
	private String actuFreqName=null;
	//	����Ƶ��ID
	private Integer actuFreqID=null;
	//���ʹ���
	private Integer times=null;
	//������
	private String orgName=null;
	//��˱�־
	private Short checkFlag = null;
	//����ID
	private Integer repInId = null;
	//�ӱ���id
	private String childRepId=null;
	//�汾��
	private String versionId=null;
	//�쳣�仯��־
	private Integer abmormityChangeFlag=null;
	//δͨ����ԭ��
	private String why;
	//���ҵ�����
	private String currName = null;
	//���ҵ�ID
	private Integer curId = null;
	//����ID
	private String orgId = null;
	//xml�ļ���
	private String xmlFileName = null;
	//���ܷ�ʽ
	private String collectType = null;
	//�Ƿ���ܹ�  1  ���ܹ�   0 û�л���
	private Integer isCollected = null;
	//����У��ͨ����־
	private Short tblInnerValidateFlag = null;
	//���У���־
	private Short tblOuterValidateFlag = null;
	//����״̬
	private String repState;
	//�ȶԱ���ID
	private Integer compRepInId = null;
	
	//�����Ƿ��Ͳ����ͨ��
	private Integer isPass = null;
	
	// ���������
	private String repInFo = null;
	//  ʵ����������
	
	private Integer donum = null;
//	 Ӧ������������
	private Integer needOrgCount = null;
	
	//�ӱ���id
	private String templateId=null;
	
	private String batchId=null;
	
	//�죨��year��termͬʱ�ã�
	private Integer day = null;
	
	private Long forseReportAgainFlag = null;
	//������ʽ
	private Integer reportStyle;
	
	//�Ƿ�ǿ���ϱ�
	private Integer isFlag = null;
	
	private String reviewStatus;//����״̬
	
	public String getReviewStatus() {
		return reviewStatus;
	}
	public void setReviewStatus(String reviewStatus) {
		this.reviewStatus = reviewStatus;
	}
	public Integer getIsFlag() {
		return isFlag;
	}
	public void setIsFlag(Integer isFlag) {
		this.isFlag = isFlag;
	}
	public Integer getDay() {
		return day;
	}
	public void setDay(Integer day) {
		this.day = day;
	}
	public String getChildRepId() {
		return childRepId;
	}
	public void setChildRepId(String repId) {
		this.childRepId = repId;
	}
	public String getVersionId() {
		return versionId;
	}
	public void setVersionId(String id) {
		this.versionId = id;
	}
	public Short getCheckFlag() {
		return this.checkFlag;
	}
	public void setCheckFlag(Short flag) {
		this.checkFlag = flag;
	}
	public String getOrgName() {
		return this.orgName;
	}
	public void setOrgName(String name) {
		this.orgName = name;
	}
	public Integer getRepInId() {
		return this.repInId;
	}
	public void setRepInId(Integer inId) {
		this.repInId = inId;
	}
	public String getRepName() {
		return this.repName;
	}
	public void setRepName(String name) {
		this.repName = name;
	}
	public Date getReportDate() {
		return this.reportDate;
	}
	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}
	public Integer getAbmormityChangeFlag() {
		return abmormityChangeFlag;
	}
	public void setAbmormityChangeFlag(Integer abmormityChangeFlag) {
		this.abmormityChangeFlag = abmormityChangeFlag;
	}
	public void setAbmormityChangeFlag(Short abmormityChangeFlag) {
		this.abmormityChangeFlag = abmormityChangeFlag==null?null:new Integer(abmormityChangeFlag.intValue());
	}
	public Integer getTerm() {
		return term;
	}
	public void setTerm(Integer term) {
		this.term = term;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public Integer getTimes() {
		return times;
	}
	public void setTimes(Integer times) {
		this.times = times;
	}
	public String getActuFreqName() {
		return actuFreqName;
	}
	public void setActuFreqName(String actuFreqName) {
		this.actuFreqName = actuFreqName;
	}
	public String getDataRgTypeName() {
		return dataRgTypeName;
	}
	public void setDataRgTypeName(String dataRgTypeName) {
		this.dataRgTypeName = dataRgTypeName;
	}
	/**
	 * @return Returns the why.
	 */
	public String getWhy() {
		return why;
	}
	/**
	 * @param why The why to set.
	 */
	public void setWhy(String why) {
		this.why = why;
	}
	/**
	 * @return Returns the currName.
	 */
	public String getCurrName() {
		return currName;
	}
	/**
	 * @param currName The currName to set.
	 */
	public void setCurrName(String currName) {
		this.currName = currName;
	}
	/**
	 * @return Returns the _checkFlag.
	 */
	public Short get_checkFlag() {
		return this.checkFlag;
	}
	/**
	 * @param flag The _checkFlag to set.
	 */
	public void set_checkFlag(Short flag) {
		this.checkFlag = flag;
	}
	/**
	 * @return Returns the _childRepId.
	 */
	public String get_childRepId() {
		return this.childRepId;
	}
	/**
	 * @param repId The _childRepId to set.
	 */
	public void set_childRepId(String repId) {
		this.childRepId = repId;
	}
	/**
	 * @return Returns the _orgName.
	 */
	public String get_orgName() {
		return this.orgName;
	}
	/**
	 * @param name The _orgName to set.
	 */
	public void set_orgName(String name) {
		this.orgName = name;
	}
	/**
	 * @return Returns the _repInId.
	 */
	public Integer get_repInId() {
		return this.repInId;
	}
	/**
	 * @param inId The _repInId to set.
	 */
	public void set_repInId(Integer inId) {
		this.repInId = inId;
	}
	/**
	 * @return Returns the _repName.
	 */
	public String get_repName() {
		return this.repName;
	}
	/**
	 * @param name The _repName to set.
	 */
	public void set_repName(String name) {
		this.repName = name;
	}
	/**
	 * @return Returns the _reportDate.
	 */
	public Date get_reportDate() {
		return this.reportDate;
	}
	/**
	 * @param date The _reportDate to set.
	 */
	public void set_reportDate(Date date) {
		this.reportDate = date;
	}
	/**
	 * @return Returns the _versionId.
	 */
	public String get_versionId() {
		return this.versionId;
	}
	/**
	 * @param id The _versionId to set.
	 */
	public void set_versionId(String id) {
		this.versionId = id;
	}
	/**
	 * @return Returns the dataRgId.
	 */
	public Integer getDataRgId() {
		return dataRgId;
	}
	/**
	 * @param dataRgId The dataRgId to set.
	 */
	public void setDataRgId(Integer dataRgId) {
		this.dataRgId = dataRgId;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	/**
	 * @return Returns the xmlFileName.
	 */
	public String getXmlFileName() {
		return xmlFileName;
	}
	/**
	 * @param xmlFileName The xmlFileName to set.
	 */
	public void setXmlFileName(String xmlFileName) {
		this.xmlFileName = xmlFileName;
	}
	/**
	 * @return Returns the collectType.
	 */
	public String getCollectType() {
		return collectType;
	}
	/**
	 * @param collectType The collectType to set.
	 */
	public void setCollectType(String collectType) {
		this.collectType = collectType;
	}
	/**
	 * @return Returns the isCollected.
	 */
	public Integer getIsCollected() {
		return isCollected;
	}
	/**
	 * @param isCollected The isCollected to set.
	 */
	public void setIsCollected(Integer isCollected) {
		this.isCollected = isCollected;
	}
	/**
	 * @return Returns the tblInnerValidateFlag.
	 */
	public Short getTblInnerValidateFlag() {
		return tblInnerValidateFlag;
	}
	/**
	 * @param tblInnerValidateFlag The tblInnerValidateFlag to set.
	 */
	public void setTblInnerValidateFlag(Short tblInnerValidateFlag) {
		this.tblInnerValidateFlag = tblInnerValidateFlag;
	}
	/**
	 * @return Returns the curId.
	 */
	public Integer getCurId() {
		return curId;
	}
	/**
	 * @param curId The curId to set.
	 */
	public void setCurId(Integer curId) {
		this.curId = curId;
	}
	/**
	 * @return Returns the tblOuterValidateFlag.
	 */
	public Short getTblOuterValidateFlag() {
		return tblOuterValidateFlag;
	}
	/**
	 * @param tblOuterValidateFlag The tblOuterValidateFlag to set.
	 */
	public void setTblOuterValidateFlag(Short tblOuterValidateFlag) {
		this.tblOuterValidateFlag = tblOuterValidateFlag;
	}
	public Integer getDataRangeId(){
		return dataRangeId;
	}
	public void setDataRangeId(Integer dataRangeId){
		this.dataRangeId = dataRangeId;
	}
	public Integer getActuFreqID(){
		return actuFreqID;
	}
	public void setActuFreqID(Integer actuFreqID){
		this.actuFreqID = actuFreqID;
	}
	/**
	 * ���� repState
	 */
	public String getRepState() {
		repState="";
		switch( checkFlag.intValue()){
		case -1:
			//��˲�ͨ��
			repState="���ϸ�";
			break;	
		case 0:
			repState="�ѱ���";
			break;	
		case 1:
			//���ͨ��
			repState="�ϸ�";
			break;
		case 2:
			if(tblInnerValidateFlag!=null&&tblInnerValidateFlag.equals(new Integer(-1)))repState="����У�鲻ͨ��";
			if(tblInnerValidateFlag!=null&&tblInnerValidateFlag.equals(new Integer(1)))repState="����У��ͨ��";
			if(tblInnerValidateFlag==null)repState="����δУ��";
			if(tblOuterValidateFlag!=null&&tblOuterValidateFlag.equals(new Integer(-1)))repState+="���У�鲻ͨ��";
			if(tblOuterValidateFlag!=null&&tblOuterValidateFlag.equals(new Integer(1)))repState+="���У��ͨ��";
			if(tblOuterValidateFlag==null)repState+="���δУ��";
			break;
		case 3:
			repState="δ�";
			break;
		case 4:
			repState="δУ��";
			break;
		} 
		
		return repState;
	}
	/**
	 * ������repState 
	 * ���� repState
	 */
	public void setRepState(String repState) {
		this.repState = repState;
	}
	public Integer getIsPass(){
		return isPass;
	}
	public void setIsPass(Integer isPass){
		this.isPass = isPass;
	}
	public String getRepInFo(){
		return repInFo;
	}
	public void setRepInFo(String repInFo){
		this.repInFo = repInFo;
	}
	public Integer getDonum(){
		return donum;
	}
	public void setDonum(Integer donum){
		this.donum = donum;
	}
	public Integer getNeedOrgCount(){
		return needOrgCount;
	}
	public void setNeedOrgCount(Integer needOrgCount){
		this.needOrgCount = needOrgCount;
	}
	public Integer getCompRepInId() {
		return compRepInId;
	}
	public void setCompRepInId(Integer compRepInId) {
		this.compRepInId = compRepInId;
	}
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	public String getBatchId() {
		return batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	public Long getForseReportAgainFlag() {
		return forseReportAgainFlag;
	}
	public void setForseReportAgainFlag(Long forseReportAgainFlag) {
		this.forseReportAgainFlag = forseReportAgainFlag;
	}
	public Integer getReportStyle() {
		return reportStyle;
	}
	public void setReportStyle(Integer reportStyle) {
		this.reportStyle = reportStyle;
	}
}
