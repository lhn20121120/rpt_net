package com.gather.refer.data.file;

import java.util.Date;

import javax.servlet.http.HttpSession;

import com.gather.adapter.StrutsReportDelegate;
import com.gather.common.BusinessCommonUtil;
import com.gather.common.DateUtil;
import com.gather.hibernate.Report;
import com.gather.refer.data.OtherUtil;


public class BusinessRuleChecker {

	private String errorMessage="";
	
	public static String BASE_INFO = "";
	public static final String NO_ORG_RIGHT = "��û��Ȩ�޴����˻���������";
	public static final String NOT_REQUIRE_REFER = "���������ϱ��˱���";
	public static final String NOT_EXIST_VERSION = "ϵͳ�����ڴ˰汾�ı���";
	public static final String ERRLY_VERSION = "�˰汾��δ����";
	public static final String BACK_VERSION = "�汾�Ѿ����ڣ��������µİ汾";
	public static final String NOT_REACH_THIS_TIME = "�ϱ�ʱ��̫�磬��û�е��ϱ�ʱ��";
	public static final String ERROR_TIME = "�����ʱ�������������������ύ";
	public static final String ERROR_DATA_REANGE  = "��������ݷ�Χ";
	public static final String ERROR_FREQUENCE = "�����Ƶ��";
	public static final String ERROR_YEAR = "���ڸ�ʽ�쳣(��)";
	
	public static final String NOT_REFERED = "����û���ύ";
	public static final String REFERED = "�����Ѿ��ύ";
	public static final String PASS_CHECK = "�����Ѿ��ύ����ͨ�����";
	public static final String NOT_PASS_CHECK = "�����Ѿ��ύ��û��ͨ�����";
	public static final String NEED_REPEAT_REFER = "�ϴ��ϱ����ݴ������⣬��Ҫ�ر�";
	
	public static final String SYS_NO_ORG = "ϵͳ�����ڴ˻���";
	public static final String SYS_NO_REPORT = "ϵͳ�����ڴ˱���";
	public static final String SYS_NO_DATA_RANGE = "ϵͳ�����ڴ����ݷ�Χ";
	public static final String SYS_NO_FREQUENCY = "ϵͳ�����ڴ�����Ƶ��";
	
	public static final String STATE_YES = "ͨ��";
	public static final String STATE_NO = "��ͨ��";
	public static final String STATE_TO_LATE = "�ٱ�";
	public static final String STATE_REPEAT = "�ر�";
	
	public BusinessRuleChecker(){}
	
	public boolean check(ListingXmlBean xmlBean,HttpSession session){
		
		if(!checkOrgReange(xmlBean,session)) return false;  //1�����ȼ������Ƿ����
		if(!checkReportId(xmlBean,session)) return false; //2���������µı���ģ��(id)�Ƿ����
		if(!checkSysInfo(xmlBean)) return false;//3�����ϵͳ����Ӧ�����ݷ�Χ��Ƶ���Ƿ����
		if(!checkVersion(xmlBean)) return false; //4�����汾�����������ʾ�����°汾
		if(!checkFrequency(xmlBean)) return false;//5����֤Ƶ��
		if(!checkDataRange(xmlBean)) return false; //6����֤���ݷ�Χ
		if(!checkRepPeriodVaidity(xmlBean)) return false; //����ύ�������Ƿ��ڰ汾��Ч����
		if(!checkTerm(xmlBean)) return false; //7����֤��ǰ��Ƶ���Ƿ������Ӧ������
		if(!checkYearAndTerm(xmlBean)) return false; //8����֤ �� �� ����
		if(!checkReferedState(xmlBean)) return false;  //9��������Ƿ��Ѿ��ύ��ͨ��
        return true;
		
		//1,����Ӧ��Ƶ���Ƿ����
		//2,���Ƶ�ʶ�Ӧ��������ʱ�仹û�е�����ʾ̫��
		//3,��������Ƿ��Ѿ��ύ
		//3.1����Ѿ��ύ����ʾ�Ѿ��ύ�������ٴ��ϱ�(false)
		//3.2���״̬�ǲ��ϸ���ʾ���ϸ񣬿����ر����ϱ�״̬Ϊ(true)
	}
	
	//���ⱨ��Ĵ��������Ͳ�����֤��ֱ��ͨ��
	private boolean checkSpecial(ListingXmlBean xmlBean){
		String orgId=xmlBean.getReportId();
		if(orgId.equalsIgnoreCase("G3301") || orgId.equalsIgnoreCase("G3302")){
			xmlBean.setState(BusinessRuleChecker.STATE_YES);
		    return true;
		}
		return false;
	}
	//���ϵͳ����Ӧ�����ݷ�Χ��Ƶ���Ƿ����
	private boolean checkSysInfo(ListingXmlBean xmlBean){
		int[] freqs=BusinessCommonUtil.getFrequency();
		int[] dataRanges=BusinessCommonUtil.getDataRange();
			int freq=Integer.parseInt(xmlBean.getFrequencyId());
			int dataRange=Integer.parseInt(xmlBean.getDataRangeId());
			
			int x=0;
			//���Ƶ��
			for(int j=0;j<freqs.length;j++){
				if(freq==freqs[j]){
					x=1;         //���ֱ�־
					break;
				}
			}
          //������ݷ�Χ
			if(x==0){//˵��Ƶ�ʼ�鷢�����⣬����Ҫ������ݷ�Χ��
				xmlBean.setState(BusinessRuleChecker.STATE_NO);
				xmlBean.setMsg(BusinessRuleChecker.SYS_NO_FREQUENCY);
				return false;
			}else{   //���������ݷ�Χ 
				for(int j=0;j<dataRanges.length;j++){
					if(dataRange==dataRanges[j]){
						x=1;    //���ֱ�־
						break;
					}
				}
			}
			if(x==0) { //˵�����ݷ�Χ��������
				//creatBaseInfo(xmlBean.getOrgId(),xmlBean.getReportId(),xmlBean.getVersion(),xmlBean.getYear(),xmlBean.getTerms(),xmlBean.getFrequencyId(),xmlBean.getDataRangeId());
				xmlBean.setState(BusinessRuleChecker.STATE_NO);
				xmlBean.setMsg(BusinessRuleChecker.SYS_NO_FREQUENCY);
				return false;
			}
		xmlBean.setState(BusinessRuleChecker.STATE_YES);
		return true;
	}
	
	//������Χ  ������������ڣ�����false �����򷵻�true
	private boolean checkOrgReange(ListingXmlBean xmlBean,HttpSession session){
		String[] orgIds=BusinessCommonUtil.getOrgId((String)session.getAttribute("orgId"));
	
			String orgId=xmlBean.getOrgId();
			int x=0;  //��־ ���Ϊ0 ������ �����Ϊ1 ����
			for(int j=0;j<orgIds.length;j++){
				// System.out.println("check org,the orgid is:"+orgIds[j]);
				if(orgId.trim().equals(orgIds[j].trim())){
					// System.out.println("====wether was executed ,and j is: "+j+"---");
					x=1;
					break;
				}
			}
			// System.out.println("========xmlBean orgId is: "+orgId+" x is:"+x);
			if(x==0){//(xx����xx����xx�汾xx��xx��xx��)
				xmlBean.setState(BusinessRuleChecker.STATE_NO);
				xmlBean.setMsg(BusinessRuleChecker.NO_ORG_RIGHT+" : "+ BusinessCommonUtil.getOrgName(xmlBean.getOrgId()));
				return false;
			}
		xmlBean.setState(BusinessRuleChecker.STATE_YES);
		return true;
	}
	//�����µ�ģ������(id)
	private boolean checkReportId(ListingXmlBean xmlBean,HttpSession session){
		String[] childReportIds=BusinessCommonUtil.getSubReportId(BusinessCommonUtil.getOrgId((String)session.getAttribute("orgId")));
	
			String childReportId=xmlBean.getReportId();
			int x=0;  //��־ ���Ϊ0 ������ �����Ϊ1 ����
			for(int j=0;j<childReportIds.length;j++){
				if(childReportId.trim().equals(childReportIds[j].trim())){
					x=1;
					break;
				}
			}
			if(x==0){//(xx����xx����xx�汾xx��xx��xx��)
				xmlBean.setState(BusinessRuleChecker.STATE_NO);
				xmlBean.setMsg(BusinessRuleChecker.NOT_REQUIRE_REFER+" : "+ BusinessCommonUtil.getChildReportName(xmlBean.getReportId(),xmlBean.getVersion()));
				//this.setErrorMessage(BusinessRuleChecker.NOT_REQUIRE_REFER+" : "+ BusinessCommonUtil.getChildReportName(xmlBean.getReportId(),xmlBean.getVersion()));
				return false;
			}
		xmlBean.setState(BusinessRuleChecker.STATE_YES);
		return true;
	}
	//�汾�����������ʾ�����°汾
	private boolean checkVersion(ListingXmlBean xmlBean){
			if(!BusinessCommonUtil.getNewVersion(xmlBean.getReportId()).equals(xmlBean.getVersion())){
				String reportName=BusinessCommonUtil.getChildReportName(xmlBean.getReportId());
				xmlBean.setState(BusinessRuleChecker.STATE_NO);
				xmlBean.setMsg(reportName+"-v"+xmlBean.getVersion()+")"+BusinessRuleChecker.BACK_VERSION);
				return true;
			}
		xmlBean.setState(BusinessRuleChecker.STATE_YES);
		return true;
	}
	
	//��֤��ǰ�ϴ��汾�Ƿ���Ч(�Ƿ�����Ч��)
	private boolean checkRepPeriodVaidity(ListingXmlBean xmlBean){
		Date[] checkDate=BusinessCommonUtil.getOneReportTime(xmlBean.getReportId(),xmlBean.getVersion());
		Date today=new Date();
		if(checkDate==null || checkDate[0]==null){
			//ϵͳ�����ڴ˱����ı���
			xmlBean.setState(BusinessRuleChecker.STATE_NO);
			xmlBean.setMsg(BusinessRuleChecker.BASE_INFO+BusinessRuleChecker.NOT_EXIST_VERSION);
			return false;
		}else if(DateUtil.compareDate(checkDate[0],today)==1){
			//�����ʼʱ����ڵ��죬˵���汾��δ����
			xmlBean.setState(BusinessRuleChecker.STATE_NO);
			xmlBean.setMsg(BusinessRuleChecker.BASE_INFO+BusinessRuleChecker.ERRLY_VERSION);
			return false;
		}else if(DateUtil.compareDate(today,checkDate[1])==1){
            //���������ڽ���ʱ�䣬˵���汾�Ѿ�����
			xmlBean.setState(BusinessRuleChecker.STATE_NO);
			xmlBean.setMsg(BusinessRuleChecker.BASE_INFO+BusinessRuleChecker.BACK_VERSION);
			return false;
		}
		xmlBean.setState(BusinessRuleChecker.STATE_YES);
		return true;
	}
	
	//��֤��Ӧ�����Ƿ���ڴ�Ƶ��
	private boolean checkFrequency(ListingXmlBean xmlBean){
			String childReportId=xmlBean.getReportId();
			String version=xmlBean.getVersion(); 
			// System.out.println("---xmlBean.childReportId is: "+childReportId+"xmlBean.getVersion() is: "+version);
			String[] freqIds=BusinessCommonUtil.getFreqIds(childReportId,version);
			int x=0;
			for(int j=0;j<freqIds.length;j++){
				// System.out.println("-----freqIds[j]"+freqIds[j]+" and xmlBean.getFrequencyId() is:"+xmlBean.getFrequencyId());
			if(Integer.parseInt(xmlBean.getFrequencyId())==Integer.parseInt(freqIds[j])){
					x=1;
				    break;
				}
			}
			if(x==0){   //˵��û���ҵ�
				String reportName=BusinessCommonUtil.getChildReportName(childReportId);
				xmlBean.setState(BusinessRuleChecker.STATE_NO);
				xmlBean.setMsg("("+reportName+"-v"+xmlBean.getVersion()+")"+BusinessRuleChecker.ERROR_FREQUENCE);
				return false;
			}
		xmlBean.setState(BusinessRuleChecker.STATE_YES);
		return true;
	}
	
	private boolean checkTerm(ListingXmlBean xmlBean){
		//��֤���ݵ�ǰƵ���Ƿ������Ӧ������
		return true;
	}
	//��֤ �� �� ����
	private boolean checkYearAndTerm(ListingXmlBean xmlBean){
		//��֤Ƶ�ʶ�Ӧ��ʱ���Ƿ����
		//1�����ṩ������ȷ�������ύ��ʱ���
		//1.1�����û�е�����ʾ̫��(false)
		//1.2����Ѿ����ڣ��鿴�Ƿ��Ѿ��ϱ�
		//1.2.1����Ѿ��ϱ���������
		//1.2.2���û���ϱ���Ϊ�ٱ�(true),�������ݿ��м��ϳٱ�����(default:today)
			String year=xmlBean.getYear();
			String term=xmlBean.getTerms();
			String freq=xmlBean.getFrequencyId();
			String reportId=xmlBean.getReportId();
			String version=xmlBean.getVersion();
			String dataRange=xmlBean.getDataRangeId();
			Date[] seDate=null;
			try{
			    seDate=OtherUtil.getStartAndEndDate(reportId,version,dataRange,freq,year,term);
			}catch(Exception e){
				e.printStackTrace();
				//������������⣬�Ǵ��������(year)��ɵ�,����û��ȡ�������յ�ֵ
				xmlBean.setState(BusinessRuleChecker.STATE_NO);
				xmlBean.setMsg(BusinessRuleChecker.ERROR_TIME);
				return false;
			}
			Date startDate=seDate[0];
			Date endDate=seDate[1];
			if(DateUtil.compareDate(startDate,new Date())==1){
				//̫��,û�е���
				creatBaseInfo(xmlBean.getOrgId(),reportId,version,year,term,freq,dataRange);
				xmlBean.setState(BusinessRuleChecker.STATE_NO);
				xmlBean.setMsg(BusinessRuleChecker.BASE_INFO+BusinessRuleChecker.NOT_REACH_THIS_TIME);
				return false;
			}else if(DateUtil.compareDate(new Date(),endDate)==1){
			    //̫��
		        xmlBean.setState(BusinessRuleChecker.STATE_TO_LATE);
		        xmlBean.setMsg(BusinessRuleChecker.STATE_TO_LATE);
		        return true;
			}else{
				return checkReferedState(xmlBean);
			}
	}
	//��֤���ݷ�Χ
	private boolean checkDataRange(ListingXmlBean xmlBean){
			String childReportId=xmlBean.getReportId();
			String version=xmlBean.getVersion(); 
			String freq=xmlBean.getFrequencyId();
			String[] dataRangeId=BusinessCommonUtil.getDataRange(childReportId,version,freq);
			int x=0;
			for(int j=0;j<dataRangeId.length;j++){
				if(Integer.parseInt(xmlBean.getDataRangeId())==Integer.parseInt(dataRangeId[j])){
					x=1;
					break;
				}
			}
			if(x==0){   //˵��û���ҵ�
				String reportName=BusinessCommonUtil.getChildReportName(childReportId);
				String freqName=BusinessCommonUtil.getFrequencyName(new Integer(Integer.parseInt(freq)));
				xmlBean.setState(BusinessRuleChecker.STATE_NO);
				xmlBean.setMsg("("+reportName+"-v"+xmlBean.getVersion()+" "+freqName+"��)"+BusinessRuleChecker.ERROR_DATA_REANGE);
				return false;
			}
		xmlBean.setState(BusinessRuleChecker.STATE_YES);
		return true;
	}
	//��֤�Ƿ��Ѿ��ύ��ͨ��������Ѿ��ύ �� ��֤�ϸ񣬲����ٴ��ύ��������ϸ񣬿����ύ
	private boolean checkReferedState(ListingXmlBean xmlBean){
          Report report=StrutsReportDelegate.getReport(xmlBean);
          if(report==null){
      	    xmlBean.setState(BusinessRuleChecker.STATE_YES);
			xmlBean.setMsg(BusinessRuleChecker.NOT_REFERED);
    	     return true;
          }else if(report.getFileFlag().intValue()==0){
        	    xmlBean.setState(BusinessRuleChecker.STATE_NO);
				xmlBean.setMsg(BusinessRuleChecker.REFERED);
        	  return false;
          }else if(report.getFileFlag().intValue()==1){
      	    xmlBean.setState(BusinessRuleChecker.STATE_NO);
			xmlBean.setMsg(BusinessRuleChecker.PASS_CHECK);
        	  return false;
          }else if(report.getFileFlag().intValue()==2){
      	    xmlBean.setState(BusinessRuleChecker.STATE_REPEAT);
			xmlBean.setMsg(BusinessRuleChecker.NOT_PASS_CHECK);
        	  return true;
          }else if(report.getReportFlag().equals("1")){
      	    xmlBean.setState(BusinessRuleChecker.STATE_REPEAT);
			xmlBean.setMsg(BusinessRuleChecker.NEED_REPEAT_REFER);
        	  return true;
          }
		return false;
	}
	
	/**
	 * 	public static final String REFERED = "�����Ѿ��ύ";
	public static final String PASS_CHECK = "�����Ѿ��ύ����ͨ�����";
	public static final String NOT_PASS_CHECK = "�����Ѿ��ύ��û��ͨ�����";
	public static final String NEED_REPEAT_REFER = "�ϴ��ϱ����ݴ������⣬��Ҫ�ر�";
	 */
	
	/**
	 * @return Returns the errorMessage.
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
	/**
	 * @param errorMessage The errorMessage to set.
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	private void creatBaseInfo(String orgId,String reportId,String version,String year,String term,String freq,String dataRangeId){
	   StringBuffer temp=new StringBuffer();
	   String orgName=BusinessCommonUtil.getOrgName(orgId);
	   String reportName=BusinessCommonUtil.getChildReportName(reportId,version);
	   String dataRangeName=BusinessCommonUtil.getDataRangeName(new Integer(Integer.parseInt(dataRangeId)));
	   String frequency=BusinessCommonUtil.getFrequencyName(new Integer(Integer.parseInt(freq)));
       //(xx����xx����xx�汾xx��xx��xx��Χ����xx��)
	   temp.append("(");
	   temp.append(orgName+" ");
	   temp.append(reportName+"���� ");
	   temp.append("��"+version+"�汾 ");
	   temp.append(year+"�� ");
	   temp.append(term+"��");
	   temp.append(frequency+"��");
	   temp.append(dataRangeName+"��Χ���� ");
	   temp.append(")");
	   
	   this.BASE_INFO=temp.toString();
	   // System.out.println("BusinessRuleChecker.BASE_INFO is: "+this.BASE_INFO);
	}

}
