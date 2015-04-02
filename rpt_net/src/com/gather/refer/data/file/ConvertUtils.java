package com.gather.refer.data.file;

import com.gather.common.BusinessCommonUtil;
import com.gather.struts.forms.HandleReferFileForm;

public class ConvertUtils {


   /**
    * @author linfeng
    * @function ��xmlBean�е�����copy�� HandleReferFileForm�У���ҳ����ʾ
    * @param ListingXmlBean
    * @return HandleReferFileForm
    */
	
	public static HandleReferFileForm convert(ListingXmlBean xmlBean){
		HandleReferFileForm toForm=new HandleReferFileForm();
		/**
		 * ���
		 * 1�����û�еõ���Ӧ�����ݷ�Χ������
		 * 1�����û�еõ���Ӧ��Ƶ�����ƣ�����
		 * 1�����û�еõ���Ӧ�Ļ������ƣ�����
		 * 4������ַ���ת�������ֳ���(�꣬������orgId,freqId,dataRangeId)˵�����������⣬����
		 */
		toForm.setDataRange(new Integer(Integer.parseInt(xmlBean.getDataRangeId())));
		if(xmlBean.getDataRangeId()!=null && !xmlBean.getDataRangeId().equals("")){
			toForm.setDataRangeName(BusinessCommonUtil.getDataRangeName(new Integer(Integer.parseInt(xmlBean.getDataRangeId()))));
		}else{
			toForm.setDataRangeName("��������ݷ�Χ");
		}
		
		
		toForm.setFrequency(new Integer(Integer.parseInt(xmlBean.getFrequencyId())));
		if(xmlBean.getFrequencyId()!=null && !xmlBean.getFrequencyId().equals("")){
			toForm.setFreqName(BusinessCommonUtil.getFrequencyName(new Integer(Integer.parseInt(xmlBean.getFrequencyId()))));
		}else{
			toForm.setFreqName("��");
		}
		
		toForm.setOrgId(xmlBean.getOrgId());
		if(xmlBean.getOrgId()!=null && !xmlBean.getOrgId().equals("")){
			toForm.setOrgName(BusinessCommonUtil.getOrgName(xmlBean.getOrgId()));	
		}else{
			toForm.setOrgName("�޴˻�������");
		}
		
		toForm.setReportId(xmlBean.getReportId());
		toForm.setVersion(xmlBean.getVersion());
		if(xmlBean.getVersion()!=null && !xmlBean.getVersion().equals("")){
			toForm.setReportName(BusinessCommonUtil.getChildReportName(xmlBean.getReportId(),xmlBean.getVersion()));	
		}else{
			toForm.setReportName("�޴˱�������");
		}
		
		toForm.setTerms(new Integer(Integer.parseInt(xmlBean.getTerms())));
		toForm.setYear(Integer.parseInt(xmlBean.getYear()));
		
		toForm.setState(xmlBean.getState());
		toForm.setStateMsg(xmlBean.getMsg());
		
		//// System.out.println("xmlBean.getDataRangeId() is:"+xmlBean.getDataRangeId());
		//// System.out.println("xmlBean.getDataRangeId()2 is:"+new Integer(Integer.parseInt(xmlBean.getDataRangeId())));
		//// System.out.println("xmlBean.getFrequencyId() is:"+xmlBean.getFrequencyId());
		//// System.out.println("getDataRangeName() is:"+BusinessCommonUtil.getDataRangeName(Integer.getInteger(xmlBean.getDataRangeId())));
		//// System.out.println("getFrequencyName() is:"+BusinessCommonUtil.getFrequencyName(Integer.getInteger(xmlBean.getFrequencyId())));
		//����ҵ�������м�飬������״̬
        //toForm.setState();
		return toForm;
	}

}
