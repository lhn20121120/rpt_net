/*
 * Created on 2005-12-21
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.gather.adapter;

import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import com.gather.common.Log;
import com.gather.dao.DBConn;
import com.gather.hibernate.CalendarDetail;
import com.gather.hibernate.Cell2Form;
import com.gather.hibernate.MCellForm;
import com.gather.hibernate.MChildReport;
import com.gather.hibernate.MChildReportPK;
import com.gather.hibernate.MOrg;
import com.gather.hibernate.MRepRange;
import com.gather.struts.forms.CalendarDetailForm;
import com.gather.struts.forms.Cell2FormForm;
import com.gather.struts.forms.DataValidateInfoForm;
import com.gather.struts.forms.MOrgForm;

/*******************************************************************************
 * @author 姬怀宝
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 ********************************************************************************/
public class StrutsJieKou {
	/**
	 * 更新机构的类别
	 * 
	 * @param orgType Integer 机构类别
	 * @param orgId String 机构ID
	 * @return boolean
	 */
	public static boolean updateOrgType(Integer orgType,String orgId){
		boolean result=false;
		
		if(orgType==null || orgId==null) return result;
		
		DBConn conn=null;
		Session session=null;
		
		try{
			conn=new DBConn();
			session=conn.beginTransaction();
			
			String hql="from MOrg o where trim(o.orgId)='" + orgId + "'";
			List list=session.find(hql);
			if(list!=null && list.size()>0){
				MOrg mOrg=(MOrg)list.get(0);
				if(mOrg!=null){
					mOrg.setOrgType(orgType);
					session.update(mOrg);
					session.flush();
					result=true;
				}
			}
		}catch(HibernateException he){
			result=false;
			he.printStackTrace();
		}catch(Exception e){
			result=false;
			e.printStackTrace();
		}finally{
			if(conn!=null) conn.endTransaction(result);
		}
		
		return result;
	}
	
	/**
	 * 更新机构的类别
	 * 
	 * @param orgType Integer 机构类别
	 * @param orgId String 机构ID
	 * @return boolean
	 */	
	public static boolean updatePatchOrgType(Integer orgType,List orgIds){
		boolean result=true;
		
		if(orgType==null || orgIds==null) return result;
		
		DBConn conn=null;
		Session session=null;
		
		try{
			conn=new DBConn();
			session=conn.beginTransaction();
			
			String orgId=null;
			
			String hql="";
			for(int i=0;i<orgIds.size();i++){
				orgId=(String)orgIds.get(i);
				hql="from MOrg o where trim(o.orgId)='" + orgId + "'";
				List list=session.find(hql);
				if(list!=null && list.size()>0){
					MOrg mOrg=(MOrg)list.get(0);
					if(mOrg!=null){
						mOrg.setOrgType(orgType);
						session.update(mOrg);
						session.flush();
						result=true;
					}
				}
			}
		}catch(HibernateException he){
			result=false;
			he.printStackTrace();
		}catch(Exception e){
			result=false;
			e.printStackTrace();
		}finally{
			if(conn!=null) conn.endTransaction(result);
		}
		
		return result;
	}
	
	/**
	 * 工作日历表 calId type="java.lang.Integer" column="CAL_ID" calName
	 * type="java.lang.String" length="20" column="CAL_NAME" calMethod
	 * type="java.lang.String" column="CAL_METHOD" length="18"
	 * 
	 * @param calendarForm
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean create(
			com.gather.struts.forms.CalendarForm calendarForm) {
		boolean result = false;
		if (calendarForm == null)
			return false;
		com.gather.hibernate.Calendar calendarPersistence = new com.gather.hibernate.Calendar();
		DBConn conn = null;
		Session session = null;
		try {
			TranslatorUtil.copyVoToPersistence(calendarPersistence,
					calendarForm);
			conn = new DBConn();
			session = conn.beginTransaction();
			session.save(calendarPersistence);
			session.flush();
			result = true;
		} catch (Exception e) {
			result = false;
			new Log(StrutsCalendarDelegate.class)
					.info(":::class:StrutsCalendarDelegate --  method: create 异常："
							+ e.getMessage());
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}

	/**
	 * 工作日历明细表 name="calYear" column="CAL_YEAR" type="java.lang.String"
	 * length="20" name="calMonth" column="CAL_MONTH" type="java.lang.String"
	 * length="20" name="calDay" column="CAL_DAY" type="java.lang.String
	 * length="20" name="calId" column="CAL_ID" type="java.lang.Integer"
	 * length="22" 工作日历表主键 name="calTypeId" column="CAL_TYPE_ID"
	 * type="java.lang.Integer" length="22" 日历类型表主键 name="calDate"
	 * type="java.util.Date" column="CAL_DATE" length="10"
	 * 
	 * @param calendarDetailForm
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean create(List calendarDetailFormlist) {
		boolean result = false;

		if (calendarDetailFormlist == null)
			return false;
		if (calendarDetailFormlist.size() <= 0)
			return false;

		DBConn conn = null;
		Session session = null;

		try {
			conn = new DBConn();
			session = conn.beginTransaction();

			com.gather.struts.forms.CalendarDetailForm _form = (com.gather.struts.forms.CalendarDetailForm) calendarDetailFormlist
					.get(0);
			String hql = "from CalendarDetail cd where cd.comp_id.calYear='"
					+ _form.getCalYear() + "' and " + "cd.comp_id.calMonth='"
					+ _form.getCalMonth() + "' and " + "cd.comp_id.calId="
					+ _form.getCalId();
			session.delete(hql);
			session.flush();
			
			Iterator it = calendarDetailFormlist.iterator();
			
			while (it.hasNext()) {
				CalendarDetailForm calendarDetailForm=new CalendarDetailForm();
				CalendarDetail calendarDetailPersistence = new CalendarDetail();
				calendarDetailForm = (CalendarDetailForm) it.next();
				//_form=(com.gather.struts.forms.CalendarDetailForm) it.next();
				//// System.out.println("***********************************");
				TranslatorUtil.copyVoToPersistence(calendarDetailPersistence,
						calendarDetailForm);
				session.save(calendarDetailPersistence);
				result = true;
			}
		} catch (Exception e) {
			new Log(StrutsCalendarDetailDelegate.class)
					.info(":::class:StrutsCalendarDetailDelegate --  method: create 异常："
							+ e.getMessage());
			result = false;
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}

		return result;
	}

	/**
	 * 日历类型表 name="com.gather.hibernate.CalendarType" table="CALENDAR_TYPE"
	 * name="calTypeId" type="java.lang.Integer" column="CAL_TYPE_ID"
	 * name="calTypeName" type="java.lang.String" column="CAL_TYPE_NAME"
	 * length="20"
	 * 
	 * @param calendarTypeForm
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean create(
			com.gather.struts.forms.CalendarTypeForm calendarTypeForm) {
		boolean result = false;
		if (calendarTypeForm == null)
			return false;
		com.gather.hibernate.CalendarType calendarTypePersistence = new com.gather.hibernate.CalendarType();
		DBConn conn = null;
		Session session = null;
		try {
			TranslatorUtil.copyVoToPersistence(calendarTypePersistence,
					calendarTypeForm);
			conn = new DBConn();
			session = conn.beginTransaction();
			session.save(calendarTypePersistence);
			session.flush();
			result = true;
		} catch (Exception e) {
			new Log(StrutsCalendarTypeDelegate.class)
					.info(":::class:StrutsCalendarTypeDelegate --  method: create 异常："
							+ e.getMessage());
			result = false;
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}

	/**
	 * 数据校验情况表 name="com.gather.hibernate.DataValidateInfo"
	 * table="DATA_VALIDATE_INFO" name="repOutId" column="REP_OUT_ID"
	 * type="java.lang.Integer" length="22" 外网实际表单表主键 name="sequenceId"
	 * column="SequenceId" type="java.lang.Integer length="22"
	 * name="validateTypeId" type="java.lang.Integer"column="VALIDATE_TYPE_ID"
	 * length="22" 校验类别表主键 name="cellFormId" type="java.lang.Integer"
	 * column="CELL_FORM_ID" length="22" 单元格公式描述表主键
	 * 
	 * @param dataValidateInfoForm
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean createPatchDataValidateInfo(List formList) {
		boolean result = false;
		
		if (formList == null) return false;
		
		DBConn conn = null;
		Session session = null;
		
		try {
			conn = new DBConn();
			session = conn.beginTransaction();
			 
			DataValidateInfoForm dataValidateInfoForm=null;
			dataValidateInfoForm=(DataValidateInfoForm)formList.get(0);
			String hql="from DataValidateInfo dvi where dvi.comp_id.repInId=" + 
				dataValidateInfoForm.getRepOutId() + " and dvi.validateTypeId=" +
				dataValidateInfoForm.getValidateTypeId();
			session.delete(hql);
			
			hql="select max(dvi.comp_id.sequenceId) from DataValidateInfo dvi";
			List list=session.find(hql);
			int maxSeq=0;
			if(list!=null && list.size()>0){
				maxSeq=list.get(0)!=null?((Integer)list.get(0)).intValue():0;
			}
			
			for(int i=0;i<formList.size();i++){
				dataValidateInfoForm=(DataValidateInfoForm)formList.get(i);
				dataValidateInfoForm.setSequenceId(new Integer(maxSeq));
				
				com.gather.hibernate.DataValidateInfo dataValidateInfoPersistence = new com.gather.hibernate.DataValidateInfo();
				TranslatorUtil.copyVoToPersistence(dataValidateInfoPersistence,dataValidateInfoForm);

				session.save(dataValidateInfoPersistence);
				session.flush();
				result = true;
			}			
		} catch (Exception e) {
			new Log(StrutsDataValidateInfoDelegate.class)
				.info(":::class:StrutsDataValidateInfoDelegate --  method: create 异常：" + e.getMessage());
			result = false;
			e.printStackTrace();
		} finally {
			if (conn != null) conn.endTransaction(result);
		}
		
		return result;
	}

	/**
	 * 实际子报表 name="com.gather.hibernate.MActuRep" table="M_ACTU_REP"
	 * name="versionId" column="VERSION_ID" type="java.lang.String" length="4"
	 * name="MChildReport" class="com.gather.hibernate.MChildReport"
	 * update="false" insert="false" name="repFreqId" column="REP_FREQ_ID"
	 * type="java.lang.Integer" length="22" name="MRepFreq"
	 * class="com.gather.hibernate.MRepFreq" update="false" insert="false"
	 * name="dataRangeId" column="DATA_RANGE_ID" type="java.lang.Integer"
	 * length="22" name="MDataRgType" class="com.gather.hibernate.MDataRgType"
	 * update="false" insert="false" name="childRepId" column="CHILD_REP_ID"
	 * type="java.lang.String" length="5" name="MChildReport"
	 * class="com.gather.hibernate.MChildReport" update="false" insert="false"
	 * name="delayTime column="DELAY_TIME" type="java.lang.Integer"
	 * column="DELAY_TIME" length="22" name="normalTime" column="NORMAL_TIME"
	 * type="java.lang.Integer" column="NORMAL_TIME" length="22"
	 * 
	 * @param mActuRepForm
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean create(
			com.gather.struts.forms.MActuRepForm mActuRepForm) {
		boolean result = false;

		com.gather.hibernate.MActuRep mActuRepPersistence = new com.gather.hibernate.MActuRep();
		DBConn conn = null;
		Session session = null;

		try {
			TranslatorUtil.copyVoToPersistence(mActuRepPersistence,mActuRepForm);
			
			/*// System.out.println("ChildRepId:" + mActuRepPersistence.getComp_id().getChildRepId());
			// System.out.println("VersionId:" + mActuRepPersistence.getComp_id().getVersionId());
			// System.out.println("DataRangeId:" + mActuRepPersistence.getComp_id().getDataRangeId());
			// System.out.println("OrgType:" + mActuRepPersistence.getComp_id().getOrgType());
			// System.out.println("RepFreqId:" + mActuRepPersistence.getComp_id().getRepFreqId());	*/
			
			conn = new DBConn();
			session = conn.beginTransaction();
			session.save(mActuRepPersistence);
			session.flush();
			result = true;
		} catch (Exception e) {
			new Log(StrutsMActuRepDelegate.class)
					.info(":::class:StrutsMActuRepDelegate --  method: getCalendarDetailInfo 异常："
							+ e.getMessage());
			result = false;
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}

		return result;
	}

	/**
	 * 代报关系表设定
	 * 
	 * name="orgid" column="ORGID" type="java.lang.String" length="17"
	 * name="MOrg" class="com.gather.hibernate.MOrg" update="false"
	 * insert="false" name="replaceOrgId" column="REPLACE_ORG_ID"
	 * type="java.lang.String"length="17" name="startDate" type="java.util.Date"
	 * column="START_DATE" length="7" name="endDate" type="java.util.Date"
	 * column="END_DATE" length="7" name="state" type="java.lang.Integer"
	 * column="STATE" length="22"
	 * 
	 * @param mappingRelationForm
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean create(
			com.gather.struts.forms.MappingRelationForm mappingRelationForm) {
		boolean result = false;
		if (mappingRelationForm == null)
			return false;
		com.gather.hibernate.MappingRelation mappingRelationPersistence = new com.gather.hibernate.MappingRelation();
		DBConn conn = null;
		Session session = null;
		try {
			TranslatorUtil.copyVoToPersistence(mappingRelationPersistence,
					mappingRelationForm);
			conn = new DBConn();
			session = conn.beginTransaction();
			session.save(mappingRelationPersistence);
			session.flush();
			result = true;
		} catch (Exception e) {
			new Log(StrutsMappingRelationDelegate.class)
					.info(":::class:StrutsMappingRelationDelegate --  method: create 异常："
							+ e.getMessage());
			result = false;
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}

	/**
	 * 单元格公式描述表 name="com.gather.hibernate.MCellForm" table="M_CELL_FORM"
	 * name="cellFormId" type="java.lang.Integer" column="CELL_FORM_ID"
	 * name="dataValidateInfos" lazy="true" inverse="true" cascade="none"
	 * name="cellForm" type="java.lang.String" column="CELL_FORM" length="500"
	 * name="formType" type="java.lang.Integer" column="FORM_TYPE" length="22"
	 * 
	 * @param mCellFormForm
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean create(com.gather.struts.forms.MCellFormForm mCellFormForm){
		boolean result = false;
		
		if(mCellFormForm==null) return result;
		
		MCellForm mCellFormPersistence = new MCellForm();
		
		DBConn conn = null;
		Session session = null;
		
		try {
			conn = new DBConn();
			session = conn.beginTransaction();
			
			mCellFormPersistence=(MCellForm)session.get(MCellForm.class,mCellFormForm.getCellFormId());
			
			if(mCellFormPersistence==null){
				mCellFormPersistence=new MCellForm();
				TranslatorUtil.copyVoToPersistence(mCellFormPersistence,mCellFormForm);
				session.save(mCellFormPersistence);
				session.flush();
			}
			result = true;
		} catch (Exception e) {
			new Log(StrutsMCellFormDelegate.class)
					.info(":::class:StrutsMCellFormDelegate --  method: create 异常：" + e.getMessage());
			result = false;
			e.printStackTrace();
		} finally {
			if (conn != null) conn.endTransaction(result);
		}
		
		return result;
	}

	public static boolean create(Cell2FormForm cell2FormForm){
		boolean result = false;
		
		if(cell2FormForm==null) return result;
		
		Cell2Form cell2Formersistence = new Cell2Form();
		
		DBConn conn = null;
		Session session = null;
		
		try {
			conn = new DBConn();
			session = conn.beginTransaction();
			
			cell2Formersistence=(Cell2Form)session.get(Cell2Form.class,cell2FormForm.getCell2FormId());
			
			if(cell2Formersistence==null){
				cell2Formersistence=new Cell2Form();
				TranslatorUtil.copyVoToPersistence(cell2Formersistence,cell2FormForm);
				session.save(cell2Formersistence);
				session.flush();
			}
			result = true;
		} catch (Exception e) {
			new Log(StrutsMCellFormDelegate.class).info(":::class:StrutsMCellFormDelegate --  method: create 异常：" + e.getMessage());
			result = false;
			e.printStackTrace();
		} finally {
			if (conn != null) conn.endTransaction(result);
		}
		
		return result;
	}
	
	/**
	 * 子报表 name="childRepId" column="CHILD_REP_ID" type="java.lang.String"
	 * length="5" name="versionId" column="VERSION_ID" type="java.lang.String"
	 * length="4" name="reportName" type="java.lang.String" column="REPORT_NAME"
	 * length="50" name="startDate" type="java.util.Date" column="START_DATE"
	 * length="10" name="endDate" type="java.util.Date" column="END_DATE"
	 * length="10"
	 * 
	 * 
	 * @param mChildReportForm
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean create(
			com.gather.struts.forms.MChildReportForm mChildReportForm) {
		boolean result = false;

		if (mChildReportForm == null)
			return false;

		com.gather.hibernate.MChildReport mChildReportPersistence = null;
		DBConn conn = null;
		Session session = null;

		try {
			mChildReportPersistence = new com.gather.hibernate.MChildReport();
			TranslatorUtil.copyVoToPersistence(mChildReportPersistence,
					mChildReportForm);
			// System.out.println("ChildrepID=="+mChildReportPersistence.getComp_id().getChildRepId());
			// System.out.println("VersionID=="+mChildReportPersistence.getComp_id().getVersionId());
			// System.out.println("ChildrepID=="+mChildReportPersistence.getReportName());
			// System.out.println("ChildrepID=="+mChildReportPersistence.getEndDate());
			// System.out.println("ChildrepID=="+mChildReportPersistence.getStartDate());
			// System.out.println("ChildrepID=="+mChildReportPersistence.getFormatTempId());
			// System.out.println("ChildrepID=="+mChildReportPersistence.getReportName());
			// System.out.println("***********************");
			
			conn = new DBConn();
			session = conn.beginTransaction();
			session.save(mChildReportPersistence);
			//session.flush();
			result = true;
		
		} catch (Exception e) {
			new Log(StrutsMChildReportDelegate.class)
					.info(":::class:StrutsMChildReportDelegate --  method: create 异常："
							+ e.getMessage());
			result = false;
			e.printStackTrace();
		} finally {
			//if (conn != null)
				conn.endTransaction(result);
		}

		return result;
	}

	/**
	 * 货币单位表 name="com.gather.hibernate.MCurUnit" table="M_CUR_UNIT"
	 * name="curUnit" type="java.lang.Integer" column="CUR_UNIT"
	 * name="curUnitName" type="java.lang.String" column="CUR_UNIT_NAME"
	 * length="20"
	 * 
	 * @param mCurUnitForm
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean create(
			com.gather.struts.forms.MCurUnitForm mCurUnitForm) {
		boolean result = false;
		if (mCurUnitForm == null)
			return false;
		com.gather.hibernate.MCurUnit mCurUnitPersistence = new com.gather.hibernate.MCurUnit();
		DBConn conn = null;
		Session session = null;
		try {
			TranslatorUtil.copyVoToPersistence(mCurUnitPersistence,
					mCurUnitForm);
			conn = new DBConn();
			session = conn.beginTransaction();
		
			session.save(mCurUnitPersistence);
			session.flush();
			result = true;
		} catch (Exception e) {
			new Log(StrutsMCurUnitDelegate.class)
					.info(":::class:StrutsMCurUnitDelegate --  method: create 异常："
							+ e.getMessage());
			result = false;
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}
	/**
	 * 货币单位表 name="com.gather.hibernate.MCurUnit" table="M_CUR_UNIT"
	 * name="curUnit" type="java.lang.Integer" column="CUR_UNIT"
	 * name="curUnitName" type="java.lang.String" column="CUR_UNIT_NAME"
	 * length="20"
	 * 
	 * @param mCurUnitForm
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean update(
			com.gather.struts.forms.MCurUnitForm mCurUnitForm) {
		boolean result = false;
		if (mCurUnitForm == null)
			return false;
		com.gather.hibernate.MCurUnit mCurUnitPersistence = new com.gather.hibernate.MCurUnit();
		DBConn conn = null;
		Session session = null;
		try {
			TranslatorUtil.copyVoToPersistence(mCurUnitPersistence,
					mCurUnitForm);
			conn = new DBConn();
			session = conn.beginTransaction();
		
			session.update(mCurUnitPersistence);
			session.flush();
			result = true;
		} catch (Exception e) {
			new Log(StrutsMCurUnitDelegate.class)
					.info(":::class:StrutsMCurUnitDelegate --  method: create 异常："
							+ e.getMessage());
			result = false;
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}

	/**
	 * 数据范围类别 name="com.gather.hibernate.MDataRgType" table="M_DATA_RG_TYPE"
	 * name="dataRangeId" type="java.lang.Integer" column="DATA_RANGE_ID"
	 * class="com.gather.hibernate.MActuRep" class="com.gather.hibernate.Report"
	 * class="com.gather.hibernate.Report" name="dataRgDesc"
	 * type="java.lang.String" column="DATA_RG_DESC" length="20"
	 * 
	 * @param mDataRgTypeForm
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean create(
			com.gather.struts.forms.MDataRgTypeForm mDataRgTypeForm) {
		boolean result = false;
		if (mDataRgTypeForm == null)
			return false;
		com.gather.hibernate.MDataRgType mDataRgTypePersistence = new com.gather.hibernate.MDataRgType();
		DBConn conn = null;
		Session session = null;
		try {
			TranslatorUtil.copyVoToPersistence(mDataRgTypePersistence,
					mDataRgTypeForm);
			conn = new DBConn();
			session = conn.beginTransaction();
			session.save(mDataRgTypePersistence);
			session.flush();
			result = true;
		} catch (Exception e) {
			new Log(StrutsMDataRgTypeDelegate.class)
					.info(":::class:StrutsMDataRgTypeDelegate --  method: create 异常："
							+ e.getMessage());
			result = false;
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.endTransaction(result);
			}
		}
		return result;
	}

	 /** 数据范围类别 name="com.gather.hibernate.MDataRgType" table="M_DATA_RG_TYPE"
		 * name="dataRangeId" type="java.lang.Integer" column="DATA_RANGE_ID"
		 * class="com.gather.hibernate.MActuRep" class="com.gather.hibernate.Report"
		 * class="com.gather.hibernate.Report" name="dataRgDesc"
		 * type="java.lang.String" column="DATA_RG_DESC" length="20"
		 * 
		 * @param mDataRgTypeForm
		 * @return boolean
		 * @throws Exception
		 */
		public static boolean update(
				com.gather.struts.forms.MDataRgTypeForm mDataRgTypeForm) {
			boolean result = false;
			if (mDataRgTypeForm == null)
				return false;
			com.gather.hibernate.MDataRgType mDataRgTypePersistence = new com.gather.hibernate.MDataRgType();
			DBConn conn = null;
			Session session = null;
			try {
				TranslatorUtil.copyVoToPersistence(mDataRgTypePersistence,
						mDataRgTypeForm);
				conn = new DBConn();
				session = conn.beginTransaction();
				session.update(mDataRgTypePersistence);
				session.flush();
				result = true;
			} catch (Exception e) {
				new Log(StrutsMDataRgTypeDelegate.class)
						.info(":::class:StrutsMDataRgTypeDelegate --  method: create 异常："
								+ e.getMessage());
				result = false;
				e.printStackTrace();
			} finally {
				if (conn != null) {
					conn.endTransaction(result);
				}
			}
			return result;
		}
	/**
	 * 主报表 name="com.gather.hibernate.MMainRep" table="M_MAIN_REP" name="repId"
	 * type="java.lang.Integer" column="REP_ID" name="repCnName"
	 * type="java.lang.String" column="REP_CN_NAME" length="50" name="repEnName"
	 * type="java.lang.String" column="REP_EN_NAME" length="50"
	 * 
	 * @param mMainRepForm
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean create(
			com.gather.struts.forms.MMainRepForm mMainRepForm) {
		boolean result = false;

		if (mMainRepForm == null)
			return false;

		DBConn conn = null;
		Session session = null;

		try {
			com.gather.hibernate.MMainRep mMainRepPersistence = new com.gather.hibernate.MMainRep();
			TranslatorUtil.copyVoToPersistence(mMainRepPersistence,	mMainRepForm);

			conn = new DBConn();
			session = conn.beginTransaction();

			session.save(mMainRepPersistence);
			session.flush();
			result = true;
		} catch (HibernateException he) {
			result = false;
			he.printStackTrace();
		} catch (Exception e) {
			new Log(StrutsMMainRepDelegate.class)
					.info(":::class:StrutsMMainRepDelegate --  method:create 异常："
							+ e.getMessage());
			result = false;
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.endTransaction(result);
			}
		}

		return result;
	}

	/**
	 * 机构表 name="com.gather.hibernate.MOrg" table="M_ORG" name="orgId"
	 * type="java.lang.String" column="ORG_ID"
	 * class="com.gather.hibernate.MRepRange"
	 * class="com.gather.hibernate.Report"
	 * class="com.gather.hibernate.RepRangeDownLog"
	 * class="com.gather.hibernate.MappingRelation" name="orgName"
	 * type="java.lang.String" column="ORG_NAME" length="20" name="orgType"
	 * type="java.lang.Integer" column="ORG_TYPE" length="22" name="isCorp"
	 * type="java.lang.String" column="IS_CORP" length="1" name="isInUsing"
	 * type="java.lang.String" column="IS_IN_USING" length="1"
	 * 
	 * @param mOrgForm
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean createPatchMOrg(List mOrgs) {
		boolean result = false;
		
		if(mOrgs==null) return result;
		
		DBConn conn = null;
		Session session = null;
		
		try {
			conn = new DBConn();
			session = conn.beginTransaction();
				
			MOrgForm mOrgForm=null;
			
			String hql="";
			for(int i=0;i<mOrgs.size();i++){
				mOrgForm=(MOrgForm)mOrgs.get(i);
				MOrg mOrgPersistence = null;
				
				hql="from MOrg mo where mo.orgId='" + mOrgForm.getOrgId() + "'";
				List list=session.find(hql);
				Object object=null;
				if(list!=null && list.size()>0) object=list.get(0);
				
				if(object==null){
					mOrgPersistence=new MOrg();
					TranslatorUtil.copyVoToPersistence(mOrgPersistence, mOrgForm);
					session.save(mOrgPersistence);
				}else{
					mOrgPersistence=(MOrg)object;
					TranslatorUtil.copyVoToPersistence(mOrgPersistence, mOrgForm);
					session.update(mOrgPersistence);
				}								
				session.flush();
				
				result = true;
			}
		} catch (Exception e) {
			new Log(StrutsMOrgDelegate.class)
					.info(":::class:StrutsMOrgDelegate --  method: create 异常：" + e.getMessage());
			result = false;
			e.printStackTrace();
		} finally {
			if (conn != null) conn.endTransaction(result);
		}
		
		return result;
	}

	/**
	 * 上报频度表 name="repFreqId" type="java.lang.Integer" column="REP_FREQ_ID"
	 * class="com.gather.hibernate.MActuRep" name="repFreqName"
	 * type="java.lang.String" column="REP_FREQ_NAME" length="20"
	 * 
	 * @param mRepFreqForm
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean create(
			com.gather.struts.forms.MRepFreqForm mRepFreqForm) {
		boolean result = false;
		if (mRepFreqForm == null)
			return false;
		com.gather.hibernate.MRepFreq mRepFreqPersistence = new com.gather.hibernate.MRepFreq();
		DBConn conn = null;
		Session session = null;
		try {
			TranslatorUtil.copyVoToPersistence(mRepFreqPersistence,
					mRepFreqForm);
			conn = new DBConn();
			session = conn.beginTransaction();
			session.save(mRepFreqPersistence);
			session.flush();
			result = true;
		} catch (Exception e) {
			new Log(StrutsMRepFreqDelegate.class)
					.info(":::class:StrutsMRepFreqDelegate --  method: create  异常："
							+ e.getMessage());
			result = false;
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}

	/**
	 * 上报频度表 name="repFreqId" type="java.lang.Integer" column="REP_FREQ_ID"
	 * class="com.gather.hibernate.MActuRep" name="repFreqName"
	 * type="java.lang.String" column="REP_FREQ_NAME" length="20"
	 * 
	 * @param mRepFreqForm
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean update(
			com.gather.struts.forms.MRepFreqForm mRepFreqForm) {
		boolean result = false;
		if (mRepFreqForm == null)
			return false;
		com.gather.hibernate.MRepFreq mRepFreqPersistence = new com.gather.hibernate.MRepFreq();
		DBConn conn = null;
		Session session = null;
		try {
			TranslatorUtil.copyVoToPersistence(mRepFreqPersistence,
					mRepFreqForm);
			conn = new DBConn();
			session = conn.beginTransaction();
			session.update(mRepFreqPersistence);
			session.flush();
			result = true;
		} catch (Exception e) {
			new Log(StrutsMRepFreqDelegate.class)
					.info(":::class:StrutsMRepFreqDelegate --  method: create  异常："
							+ e.getMessage());
			result = false;
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}
	/**
	 * 报表机构适用范围表 name="childRepId" column="CHILD_REP_ID" type="java.lang.String"
	 * length="5" class="com.gather.hibernate.MChildReport" name="orgId"
	 * column="ORG_ID" type="java.lang.String" length="17"
	 * class="com.gather.hibernate.MOrg" name="versionId" column="VERSION_ID"
	 * type="java.lang.String" length="4"
	 * class="com.gather.hibernate.MChildReport"
	 * 
	 * @param mRepRangeForm
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean create(
			com.gather.struts.forms.MRepRangeForm mRepRangeForm) {
		boolean result = false;

		if (mRepRangeForm == null)
			return false;

		DBConn conn = null;
		Session session = null;

		try {
			MRepRange mRepRangePersistence = new MRepRange();
			TranslatorUtil.copyVoToPersistence(mRepRangePersistence,
					mRepRangeForm);

			conn = new DBConn();
			session = conn.beginTransaction();
			session.save(mRepRangePersistence);
			session.flush();
			result = true;
		} catch (Exception e) {
			new Log(StrutsMRepRangeDelegate.class)
					.info(":::class:StrutsMRepRangeDelegate --  method: create 异常："
							+ e.getMessage());
			result = false;
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}

		return result;
	}

	/**
	 * 报表类别 name="repTypeId" type="java.lang.Integer" column="REP_TYPE_ID"
	 * class="com.gather.hibernate.MMainRep" name="repTypeName"
	 * type="java.lang.String" column="REP_TYPE_NAME" length="20"
	 * 
	 * @param mRepTypeForm
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean create(
			com.gather.struts.forms.MRepTypeForm mRepTypeForm) {
		boolean result = false;
		if (mRepTypeForm == null)
			return false;
		com.gather.hibernate.MRepType mRepTypePersistence = new com.gather.hibernate.MRepType();
		DBConn conn = null;
		Session session = null;
		try {
			TranslatorUtil.copyVoToPersistence(mRepTypePersistence,
					mRepTypeForm);
			conn = new DBConn();
			session = conn.beginTransaction();
			session.save(mRepTypePersistence);
			session.flush();
			result = true;
		} catch (Exception e) {
			new Log(StrutsMRepTypeDelegate.class)
					.info(":::class:StrutsMRepTypeDelegate --  method: create 异常："
							+ e.getMessage());
			result = false;
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}

	/**
	 * 报表类别 name="repTypeId" type="java.lang.Integer" column="REP_TYPE_ID"
	 * class="com.gather.hibernate.MMainRep" name="repTypeName"
	 * type="java.lang.String" column="REP_TYPE_NAME" length="20"
	 * 
	 * @param mRepTypeForm
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean update(
			com.gather.struts.forms.MRepTypeForm mRepTypeForm) {
		boolean result = false;
		if (mRepTypeForm == null)
			return false;
		com.gather.hibernate.MRepType mRepTypePersistence = new com.gather.hibernate.MRepType();
		DBConn conn = null;
		Session session = null;
		try {
			TranslatorUtil.copyVoToPersistence(mRepTypePersistence,
					mRepTypeForm);
			conn = new DBConn();
			session = conn.beginTransaction();
			session.update(mRepTypePersistence);
			session.flush();
			result = true;
		} catch (Exception e) {
			new Log(StrutsMRepTypeDelegate.class)
					.info(":::class:StrutsMRepTypeDelegate --  method: create 异常："
							+ e.getMessage());
			result = false;
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}
	/**
	 * 校验类别表 name="com.gather.hibernate.ValidateType" table="VALIDATE_TYPE"
	 * name="validateTypeId" type="java.lang.Integer" column="VALIDATE_TYPE_ID"
	 * class="com.gather.hibernate.DataValidateInfo" name="validateTypeName"
	 * type="java.lang.String" column="VALIDATE_TYPE_NAME" length="20"
	 * 
	 * @param validateTypeForm
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean create(
			com.gather.struts.forms.ValidateTypeForm validateTypeForm) {
		boolean result = false;
		if (validateTypeForm == null)
			return false;
		com.gather.hibernate.ValidateType validateTypePersistence = new com.gather.hibernate.ValidateType();
		DBConn conn = null;
		Session session = null;
		try {
			TranslatorUtil.copyVoToPersistence(validateTypePersistence,
					validateTypeForm);
			conn = new DBConn();
			session = conn.beginTransaction();
			session.save(validateTypePersistence);
			session.flush();
			result = true;
		} catch (Exception e) {
			new Log(StrutsValidateTypeDelegate.class)
					.info(":::class:StrutsValidateTypeDelegate --  method: create 异常："
							+ e.getMessage());
			result = false;
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;

	}

	
	/**
	 * 校验类别表 name="com.gather.hibernate.ValidateType" table="VALIDATE_TYPE"
	 * name="validateTypeId" type="java.lang.Integer" column="VALIDATE_TYPE_ID"
	 * class="com.gather.hibernate.DataValidateInfo" name="validateTypeName"
	 * type="java.lang.String" column="VALIDATE_TYPE_NAME" length="20"
	 * 
	 * @param validateTypeForm
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean update(
			com.gather.struts.forms.ValidateTypeForm validateTypeForm) {
		boolean result = false;
		if (validateTypeForm == null)
			return false;
		com.gather.hibernate.ValidateType validateTypePersistence = new com.gather.hibernate.ValidateType();
		DBConn conn = null;
		Session session = null;
		try {
			TranslatorUtil.copyVoToPersistence(validateTypePersistence,
					validateTypeForm);
			conn = new DBConn();
			session = conn.beginTransaction();
			session.update(validateTypePersistence);
			session.flush();
			result = true;
		} catch (Exception e) {
			new Log(StrutsValidateTypeDelegate.class)
					.info(":::class:StrutsValidateTypeDelegate --  method: create 异常："
							+ e.getMessage());
			result = false;
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;

	}
	/**
	 * 外网实际表单表
	 * 
	 * @param reportForm
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean create(com.gather.struts.forms.ReportForm reportForm) {
		boolean result = false;
		com.gather.hibernate.Report reportPersistence = new com.gather.hibernate.Report();
		DBConn conn = null;
		Session session = null;
		try {
			TranslatorUtil.copyVoToPersistence(reportPersistence, reportForm);
			
			conn = new DBConn();
			session = conn.beginTransaction();
			session.save(reportPersistence);
			session.flush();
			session.flush();
			result = true;
		} catch (Exception e) {
			new Log(StrutsReportDelegate.class)
					.info(":::class:StrutsReportDelegate --  method: create 异常："
							+ e.getMessage());
			result = false;
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}

	/**
	 * 机构模版下载记录 name="com.gather.hibernate.RepRangeDownLog"
	 * table="REP_RANGE_DOWN_LOG" name="orgId" column="ORG_ID"
	 * type="java.lang.String" length="17" name="childRepId"
	 * column="CHILD_REP_ID" type="java.lang.String" length="5"
	 * class="com.gather.hibernate.MChildReport" not-null="true"
	 * name="versionId" column="VERSION_ID" type="java.lang.String" length="4"
	 * class="com.gather.hibernate.MChildReport" not-null="true" name="state"
	 * type="java.lang.Integer" column="STATE" length="22"
	 * 
	 * @param repRangeDownLogForm
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean create(
			com.gather.struts.forms.RepRangeDownLogForm repRangeDownLogForm) {
		boolean result = false;
		if (repRangeDownLogForm == null)
			return false;
		com.gather.hibernate.RepRangeDownLog repRangeDownLogPersistence = new com.gather.hibernate.RepRangeDownLog();

		DBConn conn = null;
		Session session = null;
		try {
			TranslatorUtil.copyVoToPersistence(repRangeDownLogPersistence,
					repRangeDownLogForm);
			conn = new DBConn();
			session = conn.beginTransaction();
			session.save(repRangeDownLogPersistence);
			session.flush();
			result = true;
		} catch (Exception e) {
			new Log(StrutsRepRangeDownLogDelegate.class)
					.info(":::class:StrutsRepRangeDownLogDelegate --  method: create 异常："
							+ e.getMessage());
			result = false;
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}

	/**
	 * 子报表实际数据表 name="com.gather.hibernate.ReportData" table="REPORT_DATA"
	 * name="childRepId" column="CHILD_REP_ID" type="java.lang.String"
	 * length="5" name="versionId" column="VERSION_ID" type="java.lang.String"
	 * length="4" name="pdf" type="java.sql.Blob" column="PDF" length="4000"
	 * name="note" type="java.sql.Blob" column="NOTE" length="4000" name="xml"
	 * type="java.sql.Blob" column="XML" length="4000"
	 * 
	 * @param reportDataForm
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean create(
			com.gather.struts.forms.ReportDataForm reportDataForm) {
		boolean result = false;

		if (reportDataForm == null)
			return false;

		com.gather.hibernate.ReportData reportDataPersistence = new com.gather.hibernate.ReportData();

		DBConn conn = null;
		Session session = null;

		try {
			TranslatorUtil.copyVoToPersistence(reportDataPersistence,reportDataForm);

			conn = new DBConn();
			session = conn.beginTransaction();
			session.save(reportDataPersistence);
			session.flush();
			result = true;
		} catch (Exception e) {
			new Log(StrutsReportDataDelegate.class)
					.info(":::class:StrutsReportDataDelegate --  method: create 异常："
							+ e.getMessage());
			result = false;
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}

		return result;
	}

	/**
	 * 外网实际子报表的update方法
	 * @param reportForm
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean update(com.gather.struts.forms.ReportForm reportForm) {
		boolean result = false;
		if (reportForm == null)
			return false;
		com.gather.hibernate.Report reportPersistence = new com.gather.hibernate.Report();
		
		DBConn conn = null;
		Session session = null;
		try {
			conn = new DBConn();
			session = conn.beginTransaction();
			
			reportPersistence=(com.gather.hibernate.Report)session.get(com.gather.hibernate.Report.class, reportForm.getRepId());
			
			if(reportPersistence!=null){
				reportPersistence.setReportFlag(reportForm.getReportFlag());
				reportPersistence.setFileFlag(reportForm.getFileFlag());
				//TranslatorUtil.copyVoToPersistence(reportPersistence, reportForm);
				session.update(reportPersistence);
				session.flush();
				result = true;
			}
		} catch (Exception e) {
			new Log(StrutsReportDelegate.class)
					.info(":::class:StrutsReportDelegate --  method: update 异常："
							+ e.getMessage());
			result = false;
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
		// System.out.print("同步完成!");
		return result;
	}

	/**
	 * 子报表 name="childRepId" column="CHILD_REP_ID" type="java.lang.String"
	 * length="5" name="versionId" column="VERSION_ID" type="java.lang.String"
	 * length="4" name="reportName" type="java.lang.String" column="REPORT_NAME"
	 * length="50" name="startDate" type="java.util.Date" column="START_DATE"
	 * length="10" name="endDate" type="java.util.Date" column="END_DATE"
	 * length="10" name="MCurUnit" class="com.gather.hibernate.MCurUnit"
	 * not-null="true" cascade="none" column name="CUR_UNIT" name="MMainRep"
	 * class="com.gather.hibernate.MMainRep" not-null="true" cascade="none"
	 * column name="REP_ID"
	 * 
	 * @param mChildReportForm
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean update(
			com.gather.struts.forms.MChildReportForm mChildReportForm) {
		boolean result = false;

		com.gather.hibernate.MChildReport mChildReportPersistence = null;
		DBConn conn = null;
		try {
			conn = new DBConn();
			Session session = conn.beginTransaction();
			MChildReportPK pk = new MChildReportPK();
			pk.setChildRepId(mChildReportForm.getChildRepId());
			pk.setVersionId(mChildReportForm.getVersionId());

			mChildReportPersistence = (MChildReport) session.get(
					MChildReport.class, pk);
			if (mChildReportPersistence != null) {
				TranslatorUtil.copyVoToPersistence(mChildReportPersistence,
						mChildReportForm);
				session.update(mChildReportPersistence);
				session.flush();
				result = true;
			}
		} catch (Exception e) {
			new Log(StrutsMChildReportDelegate.class)
					.info(":::class:StrutsMChildReportDelegate --  method: update 异常："
							+ e.getMessage());
			result = false;
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}

		return result;
	}

	public static boolean update(
			com.gather.struts.forms.MappingRelationForm mappingRelationForm) {
		boolean result = false;
		if (mappingRelationForm == null)
			return false;
		com.gather.hibernate.MappingRelation mappingRelationPersistence = new com.gather.hibernate.MappingRelation();
		DBConn conn = null;
		Session session = null;
		try {
			TranslatorUtil.copyVoToPersistence(mappingRelationPersistence,
					mappingRelationForm);
			conn = new DBConn();
			session = conn.beginTransaction();
			session.update(mappingRelationPersistence);
			session.flush();
			result = true;
		} catch (Exception e) {
			new Log(StrutsMappingRelationDelegate.class)
					.info(":::class:StrutsMappingRelationDelegate --  method: update 异常："
							+ e.getMessage());
			result = false;
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}

	/**
	 * Remove (delete) an existing com.gather.struts.forms.MRepTypeForm object.
	 * 
	 * @param mRepTypeForm
	 *            The com.gather.struts.forms.MRepTypeForm object containing the
	 *            data to be deleted.
	 * @exception Exception
	 *                If the com.gather.struts.forms.MRepTypeForm object cannot
	 *                be removed.
	 */
	public static boolean remove(
			com.gather.struts.forms.MRepTypeForm mRepTypeForm) {
		boolean result = false;
		if (mRepTypeForm == null)
			return false;
		com.gather.hibernate.MRepType mRepTypePersistence = new com.gather.hibernate.MRepType();
		DBConn conn = null;
		Session session = null;
		try {
			conn = new DBConn();
			session = conn.beginTransaction();
			TranslatorUtil.copyVoToPersistence(mRepTypePersistence,
					mRepTypeForm);
			session.delete(mRepTypePersistence);
			session.flush();
			result = true;
		} catch (Exception e) {
			result = false;
			new Log(StrutsMRepTypeDelegate.class)
					.info(":::class:StrutsMRepTypeDelegate --  method: create remove："
							+ e.getMessage());
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}

	/**
	 * Remove (delete) an existing com.gather.struts.forms.MCurUnitForm object.
	 * 
	 * @param mCurUnitForm
	 *            The com.gather.struts.forms.MCurUnitForm object containing the
	 *            data to be deleted.
	 * @exception Exception
	 *                If the com.gather.struts.forms.MCurUnitForm object cannot
	 *                be removed.
	 */
	public static boolean remove(
			com.gather.struts.forms.MCurUnitForm mCurUnitForm) {
		boolean result = false;
		if (mCurUnitForm == null)
			return false;
		com.gather.hibernate.MCurUnit mCurUnitPersistence = new com.gather.hibernate.MCurUnit();
		DBConn conn = null;
		Session session = null;

		try {
			conn = new DBConn();
			session = conn.beginTransaction();
			TranslatorUtil.copyVoToPersistence(mCurUnitPersistence,
					mCurUnitForm);
			
			session.delete(mCurUnitPersistence);
			session.flush();
			result = true;
		} catch (Exception e) {
			new Log(StrutsMCurUnitDelegate.class)
					.info(":::class:StrutsMCurUnitDelegate --  method: create 异常："
							+ e.getMessage());
			result = false;
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}

	/**
	 * Remove (delete) an existing com.gather.struts.forms.MDataRgTypeForm
	 * object.
	 * 
	 * @param mDataRgTypeForm
	 *            The com.gather.struts.forms.MDataRgTypeForm object containing
	 *            the data to be deleted.
	 * @exception Exception
	 *                If the com.gather.struts.forms.MDataRgTypeForm object
	 *                cannot be removed.
	 */
	public static boolean remove(
			com.gather.struts.forms.MDataRgTypeForm mDataRgTypeForm) {
		boolean result = false;
		if (mDataRgTypeForm == null)
			return false;
		com.gather.hibernate.MDataRgType mDataRgTypePersistence = new com.gather.hibernate.MDataRgType();
		DBConn conn = null;
		Session session = null;
		try {
			conn = new DBConn();
			session = conn.beginTransaction();
			TranslatorUtil.copyVoToPersistence(mDataRgTypePersistence,
					mDataRgTypeForm);
			session.delete(mDataRgTypePersistence);
			session.flush();
			result = true;
		} catch (Exception e) {
			new Log(StrutsMDataRgTypeDelegate.class)
					.info(":::class:StrutsMDataRgTypeDelegate --  method: remove 异常："
							+ e.getMessage());
			result = false;
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}

	/**
	 * Remove (delete) an existing com.gather.struts.forms.MRepFreqForm object.
	 * 
	 * @param mRepFreqForm
	 *            The com.gather.struts.forms.MRepFreqForm object containing the
	 *            data to be deleted.
	 * @exception Exception
	 *                If the com.gather.struts.forms.MRepFreqForm object cannot
	 *                be removed.
	 */
	public static boolean remove(com.gather.struts.forms.MRepFreqForm mRepFreqForm) {
		boolean result = false;
		if (mRepFreqForm == null)
			return false;
		DBConn conn = null;
		Session session = null;
		com.gather.hibernate.MRepFreq mRepFreqPersistence = new com.gather.hibernate.MRepFreq();
		try {
			conn = new DBConn();
			session = conn.beginTransaction();
			TranslatorUtil.copyVoToPersistence(mRepFreqPersistence,
					mRepFreqForm);
			session.delete(mRepFreqPersistence);
			session.flush();
			result=true;
		} catch (Exception e) {
			new Log(StrutsMRepFreqDelegate.class)
					.info(":::class:StrutsMRepFreqDelegate --  method: remove 异常："
							+ e.getMessage());
			result = false;
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}

	/**
	 * Remove (delete) an existing com.gather.struts.forms.ValidateTypeForm
	 * object.
	 * 
	 * @param validateTypeForm
	 *            The com.gather.struts.forms.ValidateTypeForm object containing
	 *            the data to be deleted.
	 * @exception Exception
	 *                If the com.gather.struts.forms.ValidateTypeForm object
	 *                cannot be removed.
	 */
	public static boolean remove(com.gather.struts.forms.ValidateTypeForm validateTypeForm) {
		boolean result = false;
		
		if (validateTypeForm == null) return false;
		
		com.gather.hibernate.ValidateType validateTypePersistence = new com.gather.hibernate.ValidateType();
		
		DBConn conn = null;
		Session session = null;

		try {
			conn = new DBConn();
			session = conn.beginTransaction();
			TranslatorUtil.copyVoToPersistence(validateTypePersistence,validateTypeForm);
			session.delete(validateTypePersistence);
			session.flush();
			result = true;
		} catch (Exception e) {
			new Log(StrutsValidateTypeDelegate.class).info(":::class:StrutsValidateTypeDelegate --  method: remove 异常："
							+ e.getMessage());
			result = false;
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}
}