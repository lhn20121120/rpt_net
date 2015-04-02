package com.cbrc.smis.other;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.cbrc.smis.form.CalendarDetailForm;
import com.cbrc.smis.form.CalendarForm;
import com.cbrc.smis.form.CalendarTypeForm;
import com.cbrc.smis.form.MActuRepForm;
import com.cbrc.smis.form.MCurUnitForm;
import com.cbrc.smis.form.MDataRgTypeForm;
import com.cbrc.smis.form.MRepFreqForm;
import com.cbrc.smis.form.MRepTypeForm;
import com.cbrc.smis.form.ReportAgainSetForm;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.form.ValidateTypeForm;
import com.cbrc.smis.util.FitechException;
import com.gather.adapter.StrutsJieKou;

/**
 * 将内网的基础数据同步到外网的数据库中
 * @serialData 2005-12-25
 * @author 内网
 */
public class InnerToGather {
	 private static FitechException log=new FitechException(InnerToGather.class);
	 private final static SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * @author 唐磊
	 * 
	 * 将内网实际表单(ReportIn)的强制重报标志(Forse_Report_Again_Flag)同步到外网(Report)的重报标志(Report_Flag)
	 * 并且加入记录至外网数据库
	 */
	 public static boolean insertReportFlag(ReportInForm reportInForm) throws Exception{
		 boolean resultReportIn=false;
		 
		 if(reportInForm!=null && !reportInForm.equals("")){
			 try{
				 com.gather.struts.forms.ReportForm gatherReportForm=new com.gather.struts.forms.ReportForm();
				 
				 gatherReportForm.setRepId(reportInForm.getRepOutId());
				 
				 //将内网的强制重报标志(Short)转为外网的重报标志(Integer)
				 /*gatherReportForm.setReportFlag(
						 reportInForm.getForseReportAgainFlag()!=null?
						 new Integer(reportInForm.getForseReportAgainFlag().intValue()):
						 new Integer(0));*/
				 gatherReportForm.setReportFlag(reportInForm.getCause());
				 
				 //调用StrutsJieKou中的插入方法
				 resultReportIn=StrutsJieKou.update(gatherReportForm);
				 
			 }catch(Exception e){
				 log.printStackTrace(e);
			 }
		 }
		 return resultReportIn;
	 }
	
	 /**
	  * 同步外同重报设置信息
	  * 
	  * @param repInId
	  * @param repOutId
	  * @return boolean 操作成功，返回true;否则，返回false
	  * @throws Exception
	  */
	 public static boolean insertReportFlag(Integer repInId,Integer repOutId) throws Exception{
		 boolean resultReportIn=false;
		 // System.out.println("repInId:" + repInId + "\nrepOutId:" + repOutId);
		 if(repInId!=null && repOutId!=null){
			 try{
				 ReportAgainSetForm rasForm=com.cbrc.smis.adapter.StrutsReportAgainSetDelegate.getReportAgainSetInfo(repInId);
				 if(rasForm==null) return false;
				 com.gather.struts.forms.ReportForm gatherReportForm=new com.gather.struts.forms.ReportForm();
				 
				 gatherReportForm.setRepId(repOutId);
				 
				 //将内网的强制重报标志(Short)转为外网的重报标志(Integer)
				 //gatherReportForm.setReportFlag(new Integer(1));
				 gatherReportForm.setReportFlag(rasForm.getCause()!=null && !rasForm.getCause().equals("")?rasForm.getCause():"重报!");
				 
				 //调用StrutsJieKou中的插入方法
				 resultReportIn=StrutsJieKou.update(gatherReportForm);
				 
			 }catch(Exception e){
				 e.printStackTrace();
				 log.printStackTrace(e);
			 }
		 }
		 return resultReportIn;
	}
	 
	/**
	 * @author 唐磊
	 * 将工作日历表的记录加入外网数据库
	 */
	public static boolean insertCalendar(CalendarForm calendarForm) throws Exception{
		boolean resultCalendar=false;
		
		if(calendarForm!=null && !calendarForm.equals("")){
			try{
				com.gather.struts.forms.CalendarForm gatherCalendarForm=new com.gather.struts.forms.CalendarForm();
				
				gatherCalendarForm.setCalId(calendarForm.getCalId());
				gatherCalendarForm.setCalName(calendarForm.getCalName());
				gatherCalendarForm.setCalMethod(calendarForm.getCalMethod());
				resultCalendar=StrutsJieKou.create(gatherCalendarForm);
			}catch(Exception e){
				log.printStackTrace(e);
			}
		}
		return resultCalendar;
	}
	
	/**
	 * @author 唐磊
	 * 将工作日历明细表的记录加入外网数据库
	 *//*
	public static boolean insertCalendarDetail(List list) throws Exception{
		boolean resultCalendarDetail=false;
		
		if(list!=null && list.size()>0){
			try{
				com.gather.struts.forms.CalendarDetailForm gatherCalendarDetailForm=new com.gather.struts.forms.CalendarDetailForm();
				
				gatherCalendarDetailForm.setCalDay(calendarDetailForm.getCalDay().toString());
				gatherCalendarDetailForm.setCalMonth(calendarDetailForm.getCalMonth().toString());
				gatherCalendarDetailForm.setCalYear(calendarDetailForm.getCalYear().toString());
				gatherCalendarDetailForm.setCalId(calendarDetailForm.getCalId());
				gatherCalendarDetailForm.setCalTypeId(calendarDetailForm.getCalTypeId());
				gatherCalendarDetailForm.setCalDate(com.cbrc.smis.util.FitechUtil.parseDate(calendarDetailForm.getCalDate()));
				
				resultCalendarDetail=StrutsJieKou.create(gatherCalendarDetailForm);
				// System.out.println("calendarDetailForm.getCalDay()==========="+calendarDetailForm.getCalDay());
				// System.out.println("calendarDetailForm.getCalMonth()==========="+calendarDetailForm.getCalMonth());
				// System.out.println("calendarDetailForm.getCalYear()==========="+calendarDetailForm.getCalYear());
				// System.out.println("calendarDetailForm.getCalId()==========="+calendarDetailForm.getCalId());
				// System.out.println("calendarDetailForm.getCalTypeId()==========="+calendarDetailForm.getCalTypeId());
			}catch(Exception e){
				log.printStackTrace(e);
			}
		}
		return resultCalendarDetail;
	}*/
	
	
	
	/**
	 * @author 唐磊
	 * 将日历类型的记录加入外网数据库
	 */
	public static boolean insertCalendarType(CalendarTypeForm calendarTypeForm) throws Exception{
		boolean resultCalendarType=false;
		
		if(calendarTypeForm!=null && !calendarTypeForm.equals("")){
			try{
				com.gather.struts.forms.CalendarTypeForm gatherCalendarTypeForm=new com.gather.struts.forms.CalendarTypeForm();
				
				gatherCalendarTypeForm.setCalTypeId(calendarTypeForm.getCalTypeId());
				gatherCalendarTypeForm.setCalTypeName(calendarTypeForm.getCalTypeName());
				resultCalendarType=StrutsJieKou.create(gatherCalendarTypeForm);
			}catch(Exception e){
				log.printStackTrace(e);
			}
		}
		return resultCalendarType;
	}
	
	/**
	 * @author 唐磊
	 * 将数据校验情况的记录加入外网数据库
	 *//*
	public static boolean insertDataValidateInfo(DataValidateInfoForm dataValidateInfoForm) throws Exception{
		boolean resultDataValidateInfo=false;
		
		if(dataValidateInfoForm!=null && !dataValidateInfoForm.equals("")){
			try{
				com.gather.struts.forms.DataValidateInfoForm gatherDataValidateInfoForm=new com.gather.struts.forms.DataValidateInfoForm();
				
				gatherDataValidateInfoForm.setCellFormId(dataValidateInfoForm.getCellFormuId());
				gatherDataValidateInfoForm.setRepOutId(dataValidateInfoForm.getRepInId());
				//gatherDataValidateInfoForm.setSequenceId(1);
				gatherDataValidateInfoForm.setValidateTypeId(dataValidateInfoForm.getValidateTypeId());
				resultDataValidateInfo=StrutsJieKou.create(gatherDataValidateInfoForm);
			}catch(Exception e){
				log.printStackTrace(e);
			}
		}
		return resultDataValidateInfo;
	}*/
	
	/**
	 * @author 唐磊
	 * 将实际子报表的记录加入外网数据库
	 */
	public static boolean insertMActuRep(MActuRepForm mActuRepForm) throws Exception{
		boolean resultMActuRep=false;
		
		if(mActuRepForm!=null && !mActuRepForm.equals("")){
			try{
				com.gather.struts.forms.MActuRepForm gatherMActuRepForm=new com.gather.struts.forms.MActuRepForm();
				
				gatherMActuRepForm.setChildRepId(mActuRepForm.getChildRepId());
				gatherMActuRepForm.setDataRangeId(mActuRepForm.getDataRangeId());
				gatherMActuRepForm.setNormalTime(mActuRepForm.getNormalTime());
				gatherMActuRepForm.setRepFreqId(mActuRepForm.getRepFreqId());
				gatherMActuRepForm.setVersionId(mActuRepForm.getVersionId());
				gatherMActuRepForm.setDelayTime(mActuRepForm.getDelayTime());
				resultMActuRep=StrutsJieKou.create(gatherMActuRepForm);
			}catch(Exception e){
				log.printStackTrace(e);
			}
		}
		return resultMActuRep;
	}
	
	/**
	 * @author 唐磊
	 * 将代报关系的记录加入外网数据库
	 *//*
	public static boolean insertMappingRelation(MappingRelationForm mappingRelationForm) throws Exception{
		boolean resultMappingRelation=false;
		
		if(mappingRelationForm!=null && !mappingRelationForm.equals("")){
			try{
				com.gather.struts.forms.MappingRelationForm gatherMappingRelationForm=new com.gather.struts.forms.MappingRelationForm();
				
				gatherMappingRelationForm.setOrgid(mappingRelationForm.getOrgId().toString());
				gatherMappingRelationForm.setReplaceOrgId(mappingRelationForm.getReplaceOrgId().toString());
				gatherMappingRelationForm.setStartDate(mappingRelationForm.getStartDate());
				gatherMappingRelationForm.setEndDate(mappingRelationForm.getEndDate());
				gatherMappingRelationForm.setState(mappingRelationForm.get);
				gatherMappingRelationForm.setDelayTime(mappingRelationForm.getDelayTime());
				resultMappingRelation=StrutsJieKou.create(gatherMappingRelationForm);
			}catch(Exception e){
				log.printStackTrace(e);
			}
		}
		return resultMappingRelation;
	}*/
	
	
	
	/**
	 * @author 唐磊
	 * 将UpdateCalendarDetailde的查找的记录转换为StrutsJieKou中ActionForm
	 */
	public static List insertCalendarDetail(List list) throws Exception{
		List arrayList=null;
		
			try{
				if(list!=null && list.size()>0){
				arrayList=new ArrayList();
				for(int i=0;i<list.size();i++){
					com.gather.struts.forms.CalendarDetailForm gatherCalendarDetailForm=new com.gather.struts.forms.CalendarDetailForm();
					gatherCalendarDetailForm.setCalYear(((CalendarDetailForm)list.get(i)).getCalYear().toString());
					gatherCalendarDetailForm.setCalMonth(((CalendarDetailForm)list.get(i)).getCalMonth().toString());
					gatherCalendarDetailForm.setCalDay(((CalendarDetailForm)list.get(i)).getCalDay().toString());
					gatherCalendarDetailForm.setCalId(((CalendarDetailForm)list.get(i)).getCalId());
					gatherCalendarDetailForm.setCalTypeId(((CalendarDetailForm)list.get(i)).getCalTypeId());
					// System.out.println("CalDate:" + ((CalendarDetailForm)list.get(i)).getCalDate());
					gatherCalendarDetailForm.setCalDate(com.cbrc.smis.util.FitechUtil.parseDate(((CalendarDetailForm)list.get(i)).getCalDate()));
					arrayList.add(gatherCalendarDetailForm);
					}
				}
			}catch(Exception e){
				log.printStackTrace(e);
			}
		return arrayList;
	}
	
	
	/****************************************将内网的基础数据同步到外网的添加系列操作**********************************
	 * @author 唐磊
	 * 将校验类别的记录加入外网数据库
	 */
	
	public static boolean insertValidateType(ValidateTypeForm validateTypeForm) throws Exception{
		boolean resultValidateType=false;
		
		if(validateTypeForm!=null && !validateTypeForm.equals("")){
			try{
				com.gather.struts.forms.ValidateTypeForm gatherValidateTypeForm=new com.gather.struts.forms.ValidateTypeForm();
				
				gatherValidateTypeForm.setValidateTypeId(validateTypeForm.getValidateTypeId());
				gatherValidateTypeForm.setValidateTypeName(validateTypeForm.getValidateTypeName());
				resultValidateType=StrutsJieKou.create(gatherValidateTypeForm);
			}catch(Exception e){
				log.printStackTrace(e);
			}
		}
		return resultValidateType;
	}
	
	/**
	 * @author 唐磊
	 * 将报表类别的记录加入外网数据库
	 */
	
	public static boolean insertMRepType(MRepTypeForm mRepTypeForm) throws Exception{
		boolean resultMRepType=false;
		
		if(mRepTypeForm!=null && !mRepTypeForm.equals("")){
			try{
				com.gather.struts.forms.MRepTypeForm gatherMRepTypeForm=new com.gather.struts.forms.MRepTypeForm();
				
				gatherMRepTypeForm.setRepTypeId(mRepTypeForm.getRepTypeId());
				gatherMRepTypeForm.setRepTypeName(mRepTypeForm.getRepTypeName());
				resultMRepType=StrutsJieKou.create(gatherMRepTypeForm);
			}catch(Exception e){
				log.printStackTrace(e);
			}
		}
		return resultMRepType;
	}
	/**
	 *@唐磊
	 *将货币单位的记录加入外网数据库
	 */
	public static boolean insertCurUnit(MCurUnitForm mCurUnitForm) throws Exception{
		boolean resultCurUnit=false;
		
		
			try{
				com.gather.struts.forms.MCurUnitForm gatherCurUnitForm=new com.gather.struts.forms.MCurUnitForm();
				if(mCurUnitForm!=null && !mCurUnitForm.equals("")){
				gatherCurUnitForm.setCurUnit(mCurUnitForm.getCurUnit());
				gatherCurUnitForm.setCurUnitName(mCurUnitForm.getCurUnitName());
				resultCurUnit=StrutsJieKou.create(gatherCurUnitForm);
				}
			}catch(Exception e){
				log.printStackTrace(e);
			}
		return resultCurUnit;
	}
	
	/**
	 * @author 唐磊
	 * 将数据范围类别的记录加入外网数据库
	 */
	public static boolean insertDataRgType(MDataRgTypeForm mDataRgTypeForm) throws Exception{
		boolean resultDataRgType=false;
		
		if(mDataRgTypeForm!=null && !mDataRgTypeForm.equals("")){
			try{
				com.gather.struts.forms.MDataRgTypeForm gatherMDataRgTypeForm=new com.gather.struts.forms.MDataRgTypeForm();
				
				gatherMDataRgTypeForm.setDataRangeId(mDataRgTypeForm.getDataRangeId());
				gatherMDataRgTypeForm.setDataRgDesc(mDataRgTypeForm.getDataRgDesc());
				resultDataRgType=StrutsJieKou.create(gatherMDataRgTypeForm);
				
			}catch(Exception e){
				log.printStackTrace(e);
			}
		}
		return resultDataRgType;
	}
	/**
	 * @author 唐磊
	 * 将上报频度的记录加入外网数据库
	 */
	public static boolean insertMRepFreq(MRepFreqForm mRepFreqForm) throws Exception{
		boolean resultMRepFreq=false;
		
		if(mRepFreqForm!=null && !mRepFreqForm.equals("")){
			try{
				com.gather.struts.forms.MRepFreqForm gatherMRepFreqForm=new com.gather.struts.forms.MRepFreqForm();
				
				gatherMRepFreqForm.setRepFreqId(mRepFreqForm.getRepFreqId());
				gatherMRepFreqForm.setRepFreqName(mRepFreqForm.getRepFreqName());
				resultMRepFreq=StrutsJieKou.create(gatherMRepFreqForm);
			}catch(Exception e){
				log.printStackTrace(e);
			}
		}
		return resultMRepFreq;
	}
	
	/**
	 * **************************************将内网的基础数据同步到外网的更新系列操作****************************
	 *
	 *@唐磊
	 *将货币单位的记录更新入外网数据库
	 */
	public static boolean updateCurUnit(MCurUnitForm mCurUnitForm) throws Exception{
		boolean resultCurUnit=false;
		
		
		try {
			com.gather.struts.forms.MCurUnitForm gatherCurUnitForm = new com.gather.struts.forms.MCurUnitForm();
			if (mCurUnitForm != null && !mCurUnitForm.equals("")) {
				gatherCurUnitForm.setCurUnit(mCurUnitForm.getCurUnit());
				gatherCurUnitForm.setCurUnitName(mCurUnitForm.getCurUnitName());
				resultCurUnit = StrutsJieKou.update(gatherCurUnitForm);
			}
		} catch (Exception e) {
			log.printStackTrace(e);
		}
			
		return resultCurUnit;
	}
	
	/**
	 * @author 唐磊
	 * 将数据范围类别的记录更新入外网数据库
	 */
	public static boolean updateDataRgType(MDataRgTypeForm mDataRgTypeForm) throws Exception{
		boolean resultDataRgType=false;
		
		if(mDataRgTypeForm!=null && !mDataRgTypeForm.equals("")){
			try{
				com.gather.struts.forms.MDataRgTypeForm gatherMDataRgTypeForm=new com.gather.struts.forms.MDataRgTypeForm();
				
				gatherMDataRgTypeForm.setDataRangeId(mDataRgTypeForm.getDataRangeId());
				gatherMDataRgTypeForm.setDataRgDesc(mDataRgTypeForm.getDataRgDesc());
				resultDataRgType=StrutsJieKou.update(gatherMDataRgTypeForm);
				
			}catch(Exception e){
				log.printStackTrace(e);
			}
		}
		return resultDataRgType;
	}
	
	/**
	 * @author 唐磊
	 * 将校验类别的记录更新入外网数据库
	 */
	
	public static boolean updateValidateType(ValidateTypeForm validateTypeForm) throws Exception{
		boolean resultValidateType=false;
		
		if(validateTypeForm!=null && !validateTypeForm.equals("")){
			try{
				com.gather.struts.forms.ValidateTypeForm gatherValidateTypeForm=new com.gather.struts.forms.ValidateTypeForm();
				
				gatherValidateTypeForm.setValidateTypeId(validateTypeForm.getValidateTypeId());
				gatherValidateTypeForm.setValidateTypeName(validateTypeForm.getValidateTypeName());
				resultValidateType=StrutsJieKou.update(gatherValidateTypeForm);
			}catch(Exception e){
				log.printStackTrace(e);
			}
		}
		return resultValidateType;
	}
	
	/**
	 * @author 唐磊
	 * 将报表类别的记录更新入外网数据库
	 */
	
	public static boolean updateMRepType(MRepTypeForm mRepTypeForm) throws Exception{
		boolean resultMRepType=false;
		
		if(mRepTypeForm!=null && !mRepTypeForm.equals("")){
			try{
				com.gather.struts.forms.MRepTypeForm gatherMRepTypeForm=new com.gather.struts.forms.MRepTypeForm();
				
				gatherMRepTypeForm.setRepTypeId(mRepTypeForm.getRepTypeId());
				gatherMRepTypeForm.setRepTypeName(mRepTypeForm.getRepTypeName());
				resultMRepType=StrutsJieKou.update(gatherMRepTypeForm);
			}catch(Exception e){
				log.printStackTrace(e);
			}
		}
		return resultMRepType;
	}
	
	/**
	 * @author 唐磊
	 * 将上报频度的记录更新入外网数据库
	 */
	public static boolean updateMRepFreq(MRepFreqForm mRepFreqForm) throws Exception{
		boolean resultMRepFreq=false;
		
		if(mRepFreqForm!=null && !mRepFreqForm.equals("")){
			try{
				com.gather.struts.forms.MRepFreqForm gatherMRepFreqForm=new com.gather.struts.forms.MRepFreqForm();
				
				gatherMRepFreqForm.setRepFreqId(mRepFreqForm.getRepFreqId());
				gatherMRepFreqForm.setRepFreqName(mRepFreqForm.getRepFreqName());
				resultMRepFreq=StrutsJieKou.update(gatherMRepFreqForm);
			}catch(Exception e){
				log.printStackTrace(e);
			}
		}
		return resultMRepFreq;
	}
	
	/**
	 ****************************************基础数据的同步外网的删除的表单操作***************************
	 /**
	 *@唐磊
	 *将货币单位的记录从外网数据库删除
	 */
	public static boolean removeCurUnit(MCurUnitForm mCurUnitForm) throws Exception{
		boolean resultCurUnit=false;
		
		
			try{
				com.gather.struts.forms.MCurUnitForm gatherCurUnitForm=new com.gather.struts.forms.MCurUnitForm();
				if(mCurUnitForm!=null && !mCurUnitForm.equals("")){
				gatherCurUnitForm.setCurUnit(mCurUnitForm.getCurUnit());
				gatherCurUnitForm.setCurUnitName(mCurUnitForm.getCurUnitName());
				resultCurUnit=StrutsJieKou.remove(gatherCurUnitForm);
				}
			}catch(Exception e){
				log.printStackTrace(e);
			}
		return resultCurUnit;
	}
	
	/**
	 * @author 唐磊
	 * 将数据范围类别的记录从外网数据库删除
	 */
	public static boolean removeDataRgType(MDataRgTypeForm mDataRgTypeForm) throws Exception{
		boolean resultDataRgType=false;
		
		if(mDataRgTypeForm!=null && !mDataRgTypeForm.equals("")){
			try{
				com.gather.struts.forms.MDataRgTypeForm gatherMDataRgTypeForm=new com.gather.struts.forms.MDataRgTypeForm();
				
				gatherMDataRgTypeForm.setDataRangeId(mDataRgTypeForm.getDataRangeId());
				gatherMDataRgTypeForm.setDataRgDesc(mDataRgTypeForm.getDataRgDesc());
				resultDataRgType=StrutsJieKou.remove(gatherMDataRgTypeForm);
				
			}catch(Exception e){
				log.printStackTrace(e);
			}
		}
		return resultDataRgType;
	}
	
	/**
	 * @author 唐磊
	 * 将校验类别的记录从外网数据库删除
	 */
	
	public static boolean removeValidateType(ValidateTypeForm validateTypeForm) throws Exception{
		boolean resultValidateType=false;
		
		if(validateTypeForm!=null && !validateTypeForm.equals("")){
			try{
				com.gather.struts.forms.ValidateTypeForm gatherValidateTypeForm=new com.gather.struts.forms.ValidateTypeForm();
				
				gatherValidateTypeForm.setValidateTypeId(validateTypeForm.getValidateTypeId());
				gatherValidateTypeForm.setValidateTypeName(validateTypeForm.getValidateTypeName());
				resultValidateType=StrutsJieKou.remove(gatherValidateTypeForm);
			}catch(Exception e){
				log.printStackTrace(e);
			}
		}
		return resultValidateType;
	}
	
	/**
	 * @author 唐磊
	 * 将报表类别的记录从外网数据库删除
	 */
	
	public static boolean removeMRepType(MRepTypeForm mRepTypeForm) throws Exception{
		boolean resultMRepType=false;
		
		if(mRepTypeForm!=null && !mRepTypeForm.equals("")){
			try{
				com.gather.struts.forms.MRepTypeForm gatherMRepTypeForm=new com.gather.struts.forms.MRepTypeForm();
				
				gatherMRepTypeForm.setRepTypeId(mRepTypeForm.getRepTypeId());
				gatherMRepTypeForm.setRepTypeName(mRepTypeForm.getRepTypeName());
				resultMRepType=StrutsJieKou.remove(gatherMRepTypeForm);
			}catch(Exception e){
				log.printStackTrace(e);
			}
		}
		return resultMRepType;
	}
	
	/**
	 * @author 唐磊
	 * 将上报频度的记录从外网数据库删除
	 */
	public static boolean removeMRepFreq(MRepFreqForm mRepFreqForm) throws Exception{
		boolean resultMRepFreq=false;
		
		if(mRepFreqForm!=null && !mRepFreqForm.equals("")){
			try{
				com.gather.struts.forms.MRepFreqForm gatherMRepFreqForm=new com.gather.struts.forms.MRepFreqForm();
				
				gatherMRepFreqForm.setRepFreqId(mRepFreqForm.getRepFreqId());
				gatherMRepFreqForm.setRepFreqName(mRepFreqForm.getRepFreqName());
				resultMRepFreq=StrutsJieKou.remove(gatherMRepFreqForm);
			}catch(Exception e){
				log.printStackTrace(e);
			}
		}
		return resultMRepFreq;
	}
}
