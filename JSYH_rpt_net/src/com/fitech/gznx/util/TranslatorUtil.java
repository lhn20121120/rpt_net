package com.fitech.gznx.util;

import com.cbrc.smis.hibernate.MChildReport;
import com.cbrc.smis.hibernate.MChildReportPK;
import com.cbrc.smis.hibernate.MCurr;
import com.cbrc.smis.hibernate.MDataRgType;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.form.AFTemplateForm;
import com.fitech.gznx.form.OrgInfoForm;
import com.fitech.gznx.po.AfDatavalidateinfo;
import com.fitech.gznx.po.AfOrg;
import com.fitech.gznx.po.AfPbocreportdata;
import com.fitech.gznx.po.AfReport;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.net.form.ETLReportForm;
import com.fitech.net.hibernate.ReprotExcelMapping;


public class TranslatorUtil {

	/**
	 * AfOrg to OrgInfoForm
	 * @param afOrg
	 * @param orgInfoForm
	 * @throws Exception
	 */
	public static void copyPersistenceToVo(AfOrg afOrg,
			OrgInfoForm orgInfoForm) throws Exception {
		// Persistence layer specific implementation
		orgInfoForm.setOrgId(afOrg.getOrgId());
		orgInfoForm.setOrgName(afOrg.getOrgName());
		orgInfoForm.setParentOrgId(afOrg.getPreOrgId());
//		orgInfoForm.setSetOrgId(afOrg.getSetOrgId());
//		if (afOrg.getPreOrgId() != null && !afOrg.getPreOrgId().equals("")) {
//			AfOrg preOrgNet = StrutsOrgNetDelegate.selectOne(afOrg
//					.getPreOrgId());
//			if (preOrgNet != null) {
//				orgInfoForm.setPre_org_name(preOrgNet.getOrgName());
//			}
//		}
		if (afOrg.getOrgType() != null) {
			orgInfoForm.setOrgType(afOrg.getOrgType());
		}
		if (afOrg.getRegionId() != null) {
			orgInfoForm.setOrgRegion(afOrg.getRegionId().toString());
		}
	}
	
	/**
	 * AfTemplate to AFTemplateForm
	 * @param afTemplatePersistence
	 * @param afTemplateForm
	 * @throws Exception
	 */
	public static void copyPersistenceToVo(
			AfTemplate afTemplatePersistence,
			AFTemplateForm afTemplateForm) throws Exception {
		
		afTemplateForm.setTemplateId(afTemplatePersistence.getId().getTemplateId());
		afTemplateForm.setVersionId(afTemplatePersistence.getId().getVersionId());
		
		afTemplateForm.setTemplateName(afTemplatePersistence.getTemplateName());
        
        afTemplateForm.setStartDate(afTemplatePersistence.getStartDate());
        afTemplateForm.setEndDate(afTemplatePersistence.getEndDate());
        
        afTemplateForm.setTemplateType(afTemplatePersistence.getTemplateType());

		
   }

   public static void copyVoToPersistence(
            AfReport reportInPersistence,
            AFReportForm reportInForm) throws Exception {
        // Persistence layer specific implementation
	   if(reportInForm.getRepId() != null){
        reportInPersistence.setRepId(Long.valueOf(reportInForm.getRepId()));
	   }
        if(reportInForm.getTemplateId()!=null && reportInForm.getVersionId()!=null) {
        	reportInPersistence.setTemplateId(reportInForm.getTemplateId());
        	reportInPersistence.setVersionId(reportInForm.getVersionId());
        }

        if(reportInForm.getCurId()!=null)
        	reportInPersistence.setCurId(Long.valueOf(reportInForm.getCurId()));
        
        reportInPersistence.setYear(Long.valueOf(reportInForm.getYear()));
        reportInPersistence.setTerm(Long.valueOf(reportInForm.getTerm()));
        reportInPersistence.setDay(Long.valueOf(reportInForm.getDay()));
        reportInPersistence.setTimes(Long.valueOf(reportInForm.getTimes()));
        reportInPersistence.setRepFreqId(Long.valueOf(reportInForm.getRepFreqId()));
        reportInPersistence.setOrgId(reportInForm.getOrgId());
        
        if(reportInForm.getTblOuterValidateFlag() != null)
        	reportInPersistence.setTblOuterValidateFlag(Long.valueOf(reportInForm
                .getTblOuterValidateFlag()));
        
        if(reportInForm.getTblInnerValidateFlag() != null)
        	reportInPersistence.setTblInnerValidateFlag(Long.valueOf(reportInForm
                .getTblInnerValidateFlag()));
        
        if(reportInForm.getReportDataWarehouseFlag() != null)
        	reportInPersistence.setReportDataWarehouseFlag(Long.valueOf(reportInForm
                .getReportDataWarehouseFlag()));
        
        if(reportInForm.getAbmormityChangeFlag() != null)
        	reportInPersistence.setAbmormityChangeFlag(Long.valueOf(reportInForm
                .getAbmormityChangeFlag()));
        
        reportInPersistence.setRepName(reportInForm.getRepName());
        reportInPersistence.setCheckFlag(Long.valueOf(reportInForm.getCheckFlag()));
        
        if(reportInForm.getPackage_() != null)
        	reportInPersistence.setPackage_(Long.valueOf(reportInForm.getPackage_()));
 
        reportInPersistence.setReportDate(reportInForm.getReportDate());

        if(reportInForm.getRepRangeFlag() != null)
        	reportInPersistence.setRepRangeFlag(Long.valueOf(reportInForm.getRepRangeFlag()));
        
        if(reportInForm.getForseReportAgainFlag() != null)
        	reportInPersistence.setForseReportAgainFlag(Long.valueOf(reportInForm.getForseReportAgainFlag()));
        
        if(reportInForm.getLaterReportDay() != null)
        	reportInPersistence.setLaterReportDay(Long.valueOf(reportInForm.getLaterReportDay()));
        
        if(reportInForm.getNotReportFlag() != null)
        	reportInPersistence.setNotReportFlag(Long.valueOf(reportInForm.getNotReportFlag()));
        
        if(reportInForm.getWriter() != null)
        	reportInPersistence.setWriter(reportInForm.getWriter());
        
        if(reportInForm.getChecker() != null)
        	reportInPersistence.setChecker(reportInForm.getChecker());
        
        if(reportInForm.getPrincipal() != null)
        	reportInPersistence.setPrincipal(reportInForm.getPrincipal());

    }
	
//    protected static void copyVoToPersistence(
//            AfPbocreportdata reportInInfoPersistence,
//            ReportForm reportInInfoForm)
//            throws Exception {
//        // Persistence layer specific implementation
//        reportInInfoPersistence.setReportValue(reportInInfoForm
//                .getReportValue());
//        // reportInInfoPersistence.setCellId(reportInInfoForm.getCellId());
//        // reportInInfoPersistence.setRepInId(reportInInfoForm.getRepInId());
//        reportInInfoPersistence.setThanPrevRise(reportInInfoForm
//                .getThanPrevRise());
//        reportInInfoPersistence.setThanSameRise(reportInInfoForm
//                .getThanSameRise());
//        reportInInfoPersistence.setThanSameFall(reportInInfoForm
//                .getThanSameFall());
//        reportInInfoPersistence.setThanPrevFall(reportInInfoForm
//                .getThanPrevFall());
//    }
   
   /**
    * Copy data from a com.cbrc.smis.hibernate.DataValidateInfo object into a
    * com.cbrc.smis.form.DataValidateInfoForm value object.
    * 
    * @param dataValidateInfoPersistence
    *            The com.cbrc.smis.hibernate.DataValidateInfo object from which
    *            the data is to be copied.
    * @param dataValidateInfoForm
    *            The com.cbrc.smis.form.DataValidateInfoForm value object into
    *            which the data is to be copied.
    */
   public static void copyPersistenceToVo(
           AfDatavalidateinfo dataValidateInfoPersistence,
           com.cbrc.smis.form.DataValidateInfoForm dataValidateInfoForm)
           throws Exception {
       // Persistence layer specific implementation
       dataValidateInfoForm.setRepInId(dataValidateInfoPersistence.getId().getRepId().intValue());
       if(dataValidateInfoPersistence.getValidateType()!=null){
       	dataValidateInfoForm.setValidateTypeId(dataValidateInfoPersistence.getValidateType().getValidateTypeId());
       	dataValidateInfoForm.setValidateTypeName(dataValidateInfoPersistence.getValidateType().getValidateTypeName());
       }
       if(dataValidateInfoPersistence.getAfValidateformula()!=null){
       	dataValidateInfoForm.setCellFormuId(dataValidateInfoPersistence.getAfValidateformula().getFormulaId().intValue());
       	dataValidateInfoForm.setCellFormu(dataValidateInfoPersistence.getAfValidateformula().getFormulaValue());
       	dataValidateInfoForm.setCellFormuView(dataValidateInfoPersistence.getAfValidateformula().getFormulaName());
       }
       dataValidateInfoForm.setResult(dataValidateInfoPersistence.getValidateFlg().intValue());
       if(dataValidateInfoPersistence.getSourceValue()!=null)
    	   dataValidateInfoForm.setSourceValue(dataValidateInfoPersistence.getSourceValue());
       if(dataValidateInfoPersistence.getTargetValue()!=null)
    	   dataValidateInfoForm.setTargetValue(dataValidateInfoPersistence.getTargetValue());
   }
}
