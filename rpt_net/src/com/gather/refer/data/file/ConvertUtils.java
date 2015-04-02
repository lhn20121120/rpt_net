package com.gather.refer.data.file;

import com.gather.common.BusinessCommonUtil;
import com.gather.struts.forms.HandleReferFileForm;

public class ConvertUtils {


   /**
    * @author linfeng
    * @function 把xmlBean中的数据copy到 HandleReferFileForm中，供页面显示
    * @param ListingXmlBean
    * @return HandleReferFileForm
    */
	
	public static HandleReferFileForm convert(ListingXmlBean xmlBean){
		HandleReferFileForm toForm=new HandleReferFileForm();
		/**
		 * 检查
		 * 1，如果没有得到相应的数据范围，处理
		 * 1，如果没有得到相应的频率名称，处理
		 * 1，如果没有得到相应的机构名称，处理
		 * 4，如果字符串转换成数字出错，(年，期数，orgId,freqId,dataRangeId)说明数据有问题，处理
		 */
		toForm.setDataRange(new Integer(Integer.parseInt(xmlBean.getDataRangeId())));
		if(xmlBean.getDataRangeId()!=null && !xmlBean.getDataRangeId().equals("")){
			toForm.setDataRangeName(BusinessCommonUtil.getDataRangeName(new Integer(Integer.parseInt(xmlBean.getDataRangeId()))));
		}else{
			toForm.setDataRangeName("错误的数据范围");
		}
		
		
		toForm.setFrequency(new Integer(Integer.parseInt(xmlBean.getFrequencyId())));
		if(xmlBean.getFrequencyId()!=null && !xmlBean.getFrequencyId().equals("")){
			toForm.setFreqName(BusinessCommonUtil.getFrequencyName(new Integer(Integer.parseInt(xmlBean.getFrequencyId()))));
		}else{
			toForm.setFreqName("无");
		}
		
		toForm.setOrgId(xmlBean.getOrgId());
		if(xmlBean.getOrgId()!=null && !xmlBean.getOrgId().equals("")){
			toForm.setOrgName(BusinessCommonUtil.getOrgName(xmlBean.getOrgId()));	
		}else{
			toForm.setOrgName("无此机构名称");
		}
		
		toForm.setReportId(xmlBean.getReportId());
		toForm.setVersion(xmlBean.getVersion());
		if(xmlBean.getVersion()!=null && !xmlBean.getVersion().equals("")){
			toForm.setReportName(BusinessCommonUtil.getChildReportName(xmlBean.getReportId(),xmlBean.getVersion()));	
		}else{
			toForm.setReportName("无此报表名称");
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
		//根据业务规则进行检查，并设置状态
        //toForm.setState();
		return toForm;
	}

}
