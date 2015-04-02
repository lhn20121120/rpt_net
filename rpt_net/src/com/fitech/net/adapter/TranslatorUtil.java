package com.fitech.net.adapter;

import com.fitech.net.form.ActuTargetResultForm;
import com.fitech.net.form.IDataRelationForm;
import com.fitech.net.form.TargetDefineForm;
import com.fitech.net.form.TargetDefineWarnForm;
import com.fitech.net.form.VParameterForm;
import com.fitech.net.hibernate.ActuTargetResult;
import com.fitech.net.hibernate.CollectTypeKey;
import com.fitech.net.hibernate.IDataRelation;
import com.fitech.net.hibernate.MBusiness;
import com.fitech.net.hibernate.MNormal;
import com.fitech.net.hibernate.OrgNet;
import com.fitech.net.hibernate.TargetDefine;
import com.fitech.net.hibernate.TargetDefineWarn;
import com.fitech.net.hibernate.VParameter;



public class TranslatorUtil {

	protected static void copyVoToPersistence(
            com.fitech.net.hibernate.OrgLayer orgLayerPersistence,
            com.fitech.net.form.OrgLayerForm orgLayerForm) throws Exception {
        // Persistence layer specific implementation
		orgLayerPersistence.setOrgLayerId(orgLayerForm.getOrg_layer_id());
		orgLayerPersistence.setOrgLayerName(orgLayerForm.getOrg_layer_name().trim());
    }
	
	protected static void copyPersistenceToVo(
			com.fitech.net.hibernate.OrgLayer orgLayerPersistence,
            com.fitech.net.form.OrgLayerForm orgLayerForm) throws Exception {
        // Persistence layer specific implementation
		orgLayerForm.setOrg_layer_id(orgLayerPersistence.getOrgLayerId());
		orgLayerForm.setOrg_layer_name(orgLayerPersistence.getOrgLayerName());
    }
	
	protected static void copyPersistenceToVo(
			com.fitech.net.hibernate.EtlIndex etlIndexPersistence,
            com.fitech.net.form.EtlIndexForm etlIndexForm) throws Exception {
		etlIndexForm.setIndexName(etlIndexPersistence.getIndexName());
		etlIndexForm.setFormual(etlIndexPersistence.getFormual());
		etlIndexForm.setDesc(etlIndexPersistence.getDesc());
    }
	
	protected static void copyVoToPersistence(
			com.fitech.net.hibernate.EtlIndex etlIndexPersistence,
            com.fitech.net.form.EtlIndexForm etlIndexForm) throws Exception {
		etlIndexPersistence.setIndexName(etlIndexForm.getIndexName());
		etlIndexPersistence.setFormual(etlIndexForm.getFormual());
		etlIndexPersistence.setDesc(etlIndexForm.getDesc());
    }
	protected static void copyVoToPersistence(TargetDefine define,
			TargetDefineForm defineForm)
	{
		define.setTargetDefineId(defineForm.getTargetDefineId());
		define.setDefineName(defineForm.getDefineName());
		define.setVersion(defineForm.getVersion());
		define.setDes(defineForm.getDes());
		define.setEndDate(defineForm.getEndDate());
		define.setFormula(defineForm.getFormula());
		define.setLaw(defineForm.getLaw());
		MBusiness business=new MBusiness();
		MNormal normal=new MNormal();
		business.setBusinessId(Integer.valueOf(defineForm.getBusinessId().trim()));
		business.setBusinessName(defineForm.getBusinessName());
		normal.setNormalId(Integer.valueOf(defineForm.getNormalId().trim()));
		normal.setNormalName(defineForm.getNormalName());
		
		define.setMbusiness(business);
		define.setMnormal(normal);
		
		define.setPreStandard(defineForm.getPreStandard());
		define.setStartDate(defineForm.getStartDate());
		define.setWarn(defineForm.getWarn());
		define.setSeasonStandard(defineForm.getSeasonStandard());
		define.setYearStandard(defineForm.getYearStandard());
		define.setSetOrgId(defineForm.getSetOrgId());
		
	}
	protected static void copyPersistenceToVo(ActuTargetResult result,ActuTargetResultForm resultForm)
	{
		resultForm.setCurUnitId(result.getCurUnitId());
		resultForm.setDataRangeId(result.getDataRangeId());
		resultForm.setId(result.getId());
		resultForm.setMonth(result.getMonth());
		resultForm.setOrgId(result.getOrgId());
		resultForm.setRegionId(result.getRegionId());
		resultForm.setRepFreId(result.getRepFreId());
		resultForm.setTargetDefineId(result.getTargetDefine().getTargetDefineId());
		resultForm.setTargetDefineName(result.getTargetDefine().getDefineName());
		
		resultForm.setNormalName(result.getTargetDefine().getMnormal().getNormalName());
		try{
			resultForm.setTargetResult(new Float(result.getTargetResult()));
		}catch(Exception ex){
			resultForm.setTargetResult(null);
		}
		resultForm.setTemp1(result.getTemp1());
		resultForm.setTemp2(result.getTemp2());
		resultForm.setYear(result.getYear());
		resultForm.setBusinessId(result.getTargetDefine().getMbusiness().getBusinessId());
		resultForm.setBusinessName(result.getTargetDefine().getMbusiness().getBusinessName());
		resultForm.setYearAndMonth(result.getYear()+"-"+result.getMonth());
	}
	protected static void copyVoToPersistence(ActuTargetResult resultForm,ActuTargetResultForm result)
	{
		resultForm.setCurUnitId(result.getCurUnitId());
		resultForm.setDataRangeId(result.getDataRangeId());
		resultForm.setId(result.getId());
		resultForm.setMonth(result.getMonth());
		resultForm.setOrgId(result.getOrgId());
		resultForm.setRegionId(result.getRegionId());
		resultForm.setRepFreId(result.getRepFreId());
		TargetDefine define=new TargetDefine();
		define.setTargetDefineId(result.getTargetDefineId());
		resultForm.setTargetDefine(define);
	
		resultForm.setTargetResult(StrutsActuTargetResultDelegate.formatNumber(result.getTargetResult().floatValue()));
		resultForm.setTemp1(result.getTemp1());
		resultForm.setTemp2(result.getTemp2());
		resultForm.setYear(result.getYear());
	}
	protected static void copyPersistenceToVo(TargetDefine define,
			TargetDefineForm defineForm)
	{
		defineForm.setTargetDefineId(define.getTargetDefineId());
		if(define.getMbusiness()!=null){
			defineForm.setBusinessId(define.getMbusiness().getBusinessId().toString());
			defineForm.setBusinessName(define.getMbusiness().getBusinessName());
		}
			
		defineForm.setDefineName(define.getDefineName());	
		defineForm.setVersion(define.getVersion());
		defineForm.setDes(define.getDes());
		defineForm.setEndDate(define.getEndDate());
		defineForm.setFormula(define.getFormula());
		defineForm.setLaw(define.getLaw());
		if(define.getMnormal()!=null){
			defineForm.setNormalId(define.getMnormal().getNormalId().toString());
			defineForm.setNormalName(define.getMnormal().getNormalName());
		}
		defineForm.setPreStandard(define.getPreStandard());
		defineForm.setSeasonStandard(define.getSeasonStandard());
		defineForm.setStartDate(define.getStartDate());
		defineForm.setTargetDefineId(define.getTargetDefineId());
		defineForm.setWarn(define.getWarn());
		defineForm.setYearStandard(define.getYearStandard());
		defineForm.setSetOrgId(define.getSetOrgId());		
	}
	protected static void copyVoToPersistence(
            com.fitech.net.hibernate.OrgType orgTypePersistence,
            com.fitech.net.form.OrgTypeForm orgTypeForm) throws Exception {
        // Persistence layer specific implementation
		orgTypePersistence.setOrgTypeId(orgTypeForm.getOrg_type_id());
		orgTypePersistence.setOrgTypeName(orgTypeForm.getOrg_type_name().trim());
    }
	
	protected static void copyPersistenceToVo(
			com.fitech.net.hibernate.OrgType orgTypePersistence,
            com.fitech.net.form.OrgTypeForm orgTypeForm) throws Exception {
        // Persistence layer specific implementation
		orgTypeForm.setOrg_type_id(orgTypePersistence.getOrgTypeId());
		orgTypeForm.setOrg_type_name(orgTypePersistence.getOrgTypeName());
    }
	
	protected static void copyVoToPersistence(
            com.fitech.net.hibernate.MRegion regionPersistence,
            com.fitech.net.form.MRegionForm regionForm) throws Exception {
        // Persistence layer specific implementation
		regionPersistence.setRegionId(regionForm.getRegion_id());
		regionPersistence.setRegionName(regionForm.getRegion_name().trim());
		regionPersistence.setPreRegionId(regionForm.getPre_region_id());
		regionPersistence.setSetOrgId(regionForm.getSetOrgId());
		com.fitech.net.hibernate.OrgType orgType = new com.fitech.net.hibernate.OrgType();
		orgType.setOrgTypeId(regionForm.getOrg_type_id());
		orgType.setOrgTypeName(regionForm.getOrg_type_name());
		regionPersistence.setOrgType(orgType);
    }
	
	protected static void copyPersistenceToVo(
			com.fitech.net.hibernate.MRegion regionPersistence,
            com.fitech.net.form.MRegionForm regionForm) throws Exception {
        // Persistence layer specific implementation
		regionForm.setRegion_id(regionPersistence.getRegionId());
		regionForm.setRegion_name(regionPersistence.getRegionName());
		regionForm.setPre_region_id(regionPersistence.getPreRegionId());
//		if(regionPersistence!=null && regionPersistence.getPreRegionId()!=null && regionPersistence.getPreRegionId().intValue()!=-1){
//			regionForm.setPre_region_name(StrutsMRegionDelegate.selectOne(regionPersistence.getPreRegionId()).getRegionName());
//		}
//		if(regionPersistence != null && regionPersistence.getOrgType() != null){
//			regionForm.setOrg_type_id(regionPersistence.getOrgType().getOrgTypeId());
//			regionForm.setOrg_type_name(regionPersistence.getOrgType().getOrgTypeName());
//		}	
    }
	
	protected static void copyVoToPersistence(
            com.fitech.net.hibernate.OrgNet orgNetPersistence,
            com.fitech.net.form.OrgNetForm orgNetForm) throws Exception {
        // Persistence layer specific implementation
		orgNetPersistence.setOrgId(orgNetForm.getOrg_id().trim());
		orgNetPersistence.setOrgName(orgNetForm.getOrg_name().trim());
		orgNetPersistence.setPreOrgId(orgNetForm.getPre_org_id() != null ? orgNetForm.getPre_org_id().trim() : null);
		orgNetPersistence.setSetOrgId(orgNetForm.getSetOrgId() != null ? orgNetForm.getSetOrgId().trim() : null);
		com.fitech.net.hibernate.MRegion mRegion = new com.fitech.net.hibernate.MRegion();
		mRegion.setRegionId(orgNetForm.getRegion_id());
		//mRegion.setRegionName(orgNetForm.getRegion_name());
		orgNetPersistence.setRegion(mRegion);
		com.fitech.net.hibernate.OrgType orgType = new com.fitech.net.hibernate.OrgType();
		orgType.setOrgTypeId(orgNetForm.getOrg_type_id());
		//orgType.setOrgTypeName(orgNetForm.getOrg_type_name());
		orgNetPersistence.setOrgType(orgType);

    }
	
	protected static void copyPersistenceToVo(
			com.fitech.net.hibernate.OrgNet orgNetPersistence,
            com.fitech.net.form.OrgNetForm orgNetForm) throws Exception {
        // Persistence layer specific implementation
		orgNetForm.setOrg_id(orgNetPersistence.getOrgId());
		orgNetForm.setOrg_name(orgNetPersistence.getOrgName());
		orgNetForm.setPre_org_id(orgNetPersistence.getPreOrgId());
		orgNetForm.setSetOrgId(orgNetPersistence.getSetOrgId());
		if(orgNetPersistence.getPreOrgId()!=null && !orgNetPersistence.getPreOrgId().equals("")){
			OrgNet preOrgNet=StrutsOrgNetDelegate.selectOne(orgNetPersistence.getPreOrgId());
			if(preOrgNet!=null){
				orgNetForm.setPre_org_name(preOrgNet.getOrgName());
			}
		}
		if(orgNetPersistence != null && orgNetPersistence.getOrgType() != null){
			orgNetForm.setOrg_type_id(orgNetPersistence.getOrgType().getOrgTypeId());
			orgNetForm.setOrg_type_name(orgNetPersistence.getOrgType().getOrgTypeName());
		}
		if(orgNetPersistence != null && orgNetPersistence.getRegion() != null){
			orgNetForm.setRegion_id(orgNetPersistence.getRegion().getRegionId());
			orgNetForm.setRegion_name(orgNetPersistence.getRegion().getRegionName());
		}
    }
	
	protected static void copyVoToPersistence(
            com.fitech.net.hibernate.CollectType collectTypePersistence,
            com.fitech.net.form.CollectTypeForm collectTypeForm) throws Exception {
		
		CollectTypeKey key = new CollectTypeKey();
		key.setChildRepId(collectTypeForm.getChildRepId());
		key.setVersionId(collectTypeForm.getVersionId());
		key.setCollectId(collectTypeForm.getCollectId());
		key.setCollectName(collectTypeForm.getCollectName());
		key.setCollectOrgId(collectTypeForm.getCollectOrgId());
		key.setOrgId(collectTypeForm.getOrgId());
		
		collectTypePersistence.setId(key);
    }
	
	protected static void copyPersistenceToVo(
			com.fitech.net.hibernate.CollectType collectTypePersistence,
            com.fitech.net.form.CollectTypeForm collectTypeForm) throws Exception {
		
		CollectTypeKey key = collectTypePersistence.getId();
		if(key != null){
			collectTypeForm.setChildRepId(key.getChildRepId());
			collectTypeForm.setVersionId(key.getVersionId());
			collectTypeForm.setCollectId(key.getCollectId());
			collectTypeForm.setCollectName(key.getCollectName());
			collectTypeForm.setOrgId(key.getOrgId());
			collectTypeForm.setCollectOrgId(key.getCollectOrgId());
		}		
    }
	
	public static void copyPersistenceToVo(
    		MNormal targetNormalPersistence,
    		com.fitech.net.form.TargetNormalForm targetNormalForm) throws Exception {
    	targetNormalForm.setNormalId(targetNormalPersistence.getNormalId());
    	targetNormalForm.setNormalName(targetNormalPersistence.getNormalName());
    	targetNormalForm.setNormalNote(targetNormalPersistence.getNormalNote());	
		
	}
    
    public static void copyVoToPersistence(
    		MNormal targetNormalPersistence,
    		com.fitech.net.form.TargetNormalForm targetNormalForm)
            throws Exception {
        // Persistence layer specific implementation
    	targetNormalPersistence.setNormalId(targetNormalForm
                .getNormalId());
    	targetNormalPersistence.setNormalName(targetNormalForm
                .getNormalName());
        // reportInInfoPersistence.setCellId(reportInInfoForm.getCellId());
        // reportInInfoPersistence.setRepInId(reportInInfoForm.getRepInId());
    	targetNormalPersistence.setNormalNote(targetNormalForm
                .getNormalNote());     
    }
    
    public static void copyPersistenceToVo(
    		MBusiness targetBusinessPersistence,
    		com.fitech.net.form.TargetBusinessForm targetBusinessForm) throws Exception {
    	targetBusinessForm.setBusinessId(targetBusinessPersistence.getBusinessId());
    	targetBusinessForm.setBusinessName(targetBusinessPersistence.getBusinessName());
    	targetBusinessForm.setBusinessNote(targetBusinessPersistence.getBusinessNote());	
		
	}
    public static void copyVoToPersistence(TargetDefineWarn targetP,TargetDefineWarnForm targetForm)
    {
    	targetP.setColor(targetForm.getColor());
    	targetP.setDownLimit(targetForm.getDownLimit());
    	targetP.setLeval(targetForm.getLeval());
    	TargetDefine define=new TargetDefine();
    	define.setTargetDefineId(targetForm.getTargetDefineId());
    	
    	targetP.setTargetDefine(define);
    	targetP.setType(targetForm.getType());
    	targetP.setUpLimit(targetForm.getUpLimit());
    }
    protected static void copyPersistenceToVo(TargetDefineWarn define,TargetDefineWarnForm form)
	{
	 form.setColor(define.getColor());
	 form.setDownLimit(define.getDownLimit());
	 form.setLeval(define.getLeval());
	 form.setId(form.getId());
	 form.setTargetDefineId(define.getTargetDefine().getTargetDefineId());
	 form.setTargetDefineName(define.getTargetDefine().getDefineName());
	 form.setType(define.getType());
	 form.setUpLimit(define.getUpLimit());
	}
    public static void copyVoToPersistence(
    		MBusiness targetBusinessPersistence,
    		com.fitech.net.form.TargetBusinessForm targetBusinessForm)
            throws Exception {
        // Persistence layer specific implementation
    	targetBusinessPersistence.setBusinessId(targetBusinessForm
                .getBusinessId());
    	targetBusinessPersistence.setBusinessName(targetBusinessForm
                .getBusinessName());
        // reportInInfoPersistence.setCellId(reportInInfoForm.getCellId());
        // reportInInfoPersistence.setRepInId(reportInInfoForm.getRepInId());
    	targetBusinessPersistence.setBusinessNote(targetBusinessForm
                .getBusinessNote());     
    }
    
	public static void copyVoToPersistence(IDataRelation idr,IDataRelationForm idrForm) throws Exception{
		idrForm.setFormulaText(idr.getIdrFormula());
		idrForm.setIdrFormula(idr.getIdrFormula());
		idrForm.setIdrDefaultvalue(idr.getIdrDefaultvalue());
		idrForm.setIdrId(idr.getIdrId());
		idrForm.setIdrInitvalue(idr.getIdrInitvalue());
		idrForm.setIdrSql(idr.getIdrSql());
		idrForm.setIdrRelative(idr.getIdrRelative());
	}
	
	public static void copyPersistenceToVo(IDataRelationForm idrForm,IDataRelation idr) throws Exception{
		idr.setIdrFormula(idrForm.getIdrFormula());
		idr.setIdrId(idrForm.getIdrId());
		idr.setIdrDefaultvalue(idrForm.getIdrDefaultvalue());
		idr.setIdrInitvalue(idrForm.getIdrInitvalue());
		idr.setIdrSql(idrForm.getIdrSql());
		idr.setIdrRelative(idrForm.getIdrRelative());
	}
	
    protected static void copyVoToPersistence(VParameter vParamPersistence, VParameterForm vParamForm) throws Exception {
    	vParamPersistence.setVpId(vParamForm.getVpId());
    	vParamPersistence.setVpNote(vParamForm.getVpNote());
    	vParamPersistence.setVpTableid(vParamForm.getVpTableid());
    	vParamPersistence.setVpTabledesc(vParamForm.getVpTabledesc());
    	vParamPersistence.setVpColumnid(vParamForm.getVpColumnid());
    	vParamPersistence.setVpColumndesc(vParamForm.getVpColumndesc());
    	vParamPersistence.setVpColtype(vParamForm.getVpColtype());
    	vParamPersistence.setVttId(vParamForm.getVttId());
    	vParamPersistence.setPre_vpId(vParamForm.getPre_vpId());
    }

    
    protected static void copyPersistenceToVo(VParameter vParamPersistence,VParameterForm vParamForm) throws Exception {
        // Persistence layer specific implementation
    	vParamForm.setVpId(vParamPersistence.getVpId());
    	vParamForm.setVpNote(vParamPersistence.getVpNote());
    	vParamForm.setVpTableid(vParamPersistence.getVpTableid());
    	vParamForm.setVpTabledesc(vParamPersistence.getVpTabledesc());
    	vParamForm.setVpColumnid(vParamPersistence.getVpColumnid());
    	vParamForm.setVpColumndesc(vParamPersistence.getVpColumndesc());
    	vParamForm.setVpColtype(vParamPersistence.getVpColtype());
    	vParamForm.setVttId(vParamPersistence.getVttId());
    	vParamForm.setPre_vpId(vParamPersistence.getPre_vpId());
    }

	


}
