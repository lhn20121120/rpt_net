package com.fitech.net.org;



public class TranslatorUtil {
	
//	 protected static void copyPersistenceToVo(
//			 com.fitech.net.org.hibernate.Orgnet orgPersistence, 
//			 com.fitech.net.org.form.OrgnetForm orgnetForm) throws Exception {
//		      // Persistence layer specific implementation
//		 orgnetForm.setOrgId(orgPersistence.getOrgId());
//		 orgnetForm.setOrgName(orgPersistence.getOrgName());
//		 orgnetForm.setOrgCode(orgPersistence.getOrgCode());
//		 orgnetForm.setOrgClsId(orgPersistence.getOrgClsId());
//		 /*orgnetForm.setOrgClsName(orgPersistence.getOrgClsName());*/
//		 orgnetForm.setIsCorp(orgPersistence.getIsCorp());
//		 orgnetForm.setOrgType(orgPersistence.getOrgType());
//		 orgnetForm.setParent_Org_Id(orgPersistence.getParent_Org_Id());
//		 orgnetForm.setRegionId(orgPersistence.getRegionId());
//		 orgnetForm.setOat_Id(orgPersistence.getOat_Id());
//		 orgnetForm.setDepartmentId(orgPersistence.getDepartmentId());
//		 orgnetForm.setDeptName(orgPersistence.getDeptName());
//		 orgnetForm.setOrglayerId(orgPersistence.getOrglayerId());
//		 orgnetForm.setOrglayer(orgPersistence.getOrglayer());
//			}
//	 protected static void copyPersistenceToVo101(
//			 com.fitech.net.org.hibernate.Orgnet orgPersistence, 
//			 com.fitech.net.org.form.OrgnetForm orgnetForm) throws Exception {
//		      // Persistence layer specific implementation
//		 orgnetForm.setOrgId(orgPersistence.getOrgId());	
//		 orgnetForm.setOrgName(orgPersistence.getOrgName());
//		 orgnetForm.setDepartmentId(orgPersistence.getDepartmentId());
//		 orgnetForm.setDeptName(orgPersistence.getDeptName());
//			}
//	 protected static void copyPersistenceToVo1011(
//			 com.fitech.net.org.hibernate.Orgnet orgPersistence, 
//			 com.fitech.net.org.form.OrgnetForm orgnetForm) throws Exception {
//		      // Persistence layer specific implementation
//		 orgnetForm.setOrgId(orgPersistence.getOrgId());	
//		 orgnetForm.setOrgName(orgPersistence.getOrgName());
//		 orgnetForm.setOrglayerId(orgPersistence.getOrglayerId());
//		 orgnetForm.setOrglayer(orgPersistence.getOrglayer());
//			}
	 protected static void copyPersistenceToVo2(
			 com.fitech.net.region.hibernate.Region orgPersistence, 
			 com.fitech.net.region.form.RegionForm regionForm) throws Exception {
		      // Persistence layer specific implementation
		 regionForm.setRegionId(orgPersistence.getRegionId());
		 regionForm.setRegionName(orgPersistence.getRegionName());
		 regionForm.setRegionTypId(orgPersistence.getRegionTypId());		 
			}
	 
	 protected static void copyPersistenceToVo3(
			 com.fitech.net.orgcls.hibernate.MOrgClNet orgclsPersistence, 
			 com.fitech.net.orgcls.form.OrgclsNetForm orgclsNetForm) throws Exception {
		      // Persistence layer specific implementation
		     orgclsNetForm.setOrgClsId(orgclsPersistence.getOrgClsId());
		     orgclsNetForm.setOrgClsNm(orgclsPersistence.getOrgClsNm());
		     orgclsNetForm.setOrgClsType(orgclsPersistence.getOrgClsType());
			}
	 protected static void copyPersistenceToVo4(
			 com.fitech.net.regiontyp.hibernate.RegionTypNet orgclsPersistence, 
			 com.fitech.net.regiontyp.form.RegionTypNetForm orgclsNetForm) throws Exception {
		      // Persistence layer specific implementation
		     orgclsNetForm.setRegionTypId(orgclsPersistence.getRegionTypId());
		     orgclsNetForm.setRegionTypNm(orgclsPersistence.getRegionTypNm());
		    
			}
	 protected static void copyPersistenceToVo44(
			 com.fitech.net.orglayer.hibernate.OrgLayer orgclsPersistence, 
			 com.fitech.net.orglayer.form.OrgLayerForm orgclsNetForm) throws Exception {
		      // Persistence layer specific implementation
		     orgclsNetForm.setOrgLayerId(orgclsPersistence.getOrgLayerId());
		     orgclsNetForm.setOrgLayer(orgclsPersistence.getOrgLayer());
		    
			}
	 
	 protected static void copyPersistenceToVo10(
			 com.fitech.net.orgcls.hibernate.MOrgClNet mOrgClPersistence, 
			 com.fitech.net.orgcls.form.OrgclsNetForm mOrgClForm) throws Exception {
		      // Persistence layer specific implementation
		        mOrgClForm.setOrgClsId(mOrgClPersistence.getOrgClsId());
		        mOrgClForm.setOrgClsNm(mOrgClPersistence.getOrgClsNm());
		        mOrgClForm.setOrgClsType(mOrgClPersistence.getOrgClsType());
				
			}
	 protected static void copyPersistenceToVoOrgcls(
			 com.fitech.net.orgcls.hibernate.MOrgClNet mOrgClPersistence, 
			 com.fitech.net.orgcls.form.OrgclsNetForm mOrgClForm) throws Exception {
		      // Persistence layer specific implementation
		        mOrgClForm.setOrgClsId(mOrgClPersistence.getOrgClsId());
		        mOrgClForm.setOrgClsNm(mOrgClPersistence.getOrgClsNm());
		        mOrgClForm.setOrgClsType(mOrgClPersistence.getOrgClsType());
				
			}
//	 protected static void copyPersistenceToVo100(			
//		        com.fitech.net.org.hibernate.Orgnet orgPersistence, 
//				 com.fitech.net.org.form.OrgnetForm orgnetForm) throws Exception {
//			      // Persistence layer specific implementation
//		 orgnetForm.setOrgClsId(orgPersistence.getOrgClsId());
//			 orgnetForm.setOrgId(orgPersistence.getOrgId());
//			 orgnetForm.setOrgName(orgPersistence.getOrgName());
//			 orgnetForm.setOrgCode(orgPersistence.getOrgCode());			
//			/* orgnetForm.setOrgClsName(orgPersistence.getOrgClsName());*/
//			 orgnetForm.setIsCorp(orgPersistence.getIsCorp());
//			 orgnetForm.setOrgType(orgPersistence.getOrgType());
//			 orgnetForm.setParent_Org_Id(orgPersistence.getParent_Org_Id());
//			 orgnetForm.setRegionId(orgPersistence.getRegionId());	
//				
//			}
	protected static void copyPersistenceToVo11(
			 com.fitech.net.region.hibernate.Region regionPersistence, 
			 com.fitech.net.region.form.RegionForm regionTypNetForm) throws Exception {
		      // Persistence layer specific implementation
		regionTypNetForm.setRegionId(regionPersistence.getRegionId());
		regionTypNetForm.setRegionName(regionPersistence.getRegionName());
		regionTypNetForm.setRegionTypId(regionPersistence.getRegionTypId());
			}
//	protected static void copyPersistenceToVo111(
//			 com.fitech.net.org.hibernate.Orgnet regionPersistence, 
//			 com.fitech.net.org.form.OrgnetForm regionTypNetForm) throws Exception {
//		      // Persistence layer specific implementation
//		regionTypNetForm.setDepartmentId(regionPersistence.getDepartmentId());
//		regionTypNetForm.setDeptName(regionPersistence.getDeptName());		     
//			}
	protected static void copyPersistenceToVo111(
			com.cbrc.smis.hibernate.OrgActuType orgActuTypePersistence, 
			 com.cbrc.smis.form.OrgActuTypeForm orgActuTypeForm) throws Exception {
		      // Persistence layer specific implementation
		orgActuTypeForm.setOATId(orgActuTypePersistence.getOATId());
		orgActuTypeForm.setOATName(orgActuTypePersistence.getOATName());
		orgActuTypeForm.setOATMemo(orgActuTypePersistence.getOATMemo());
			}
}
