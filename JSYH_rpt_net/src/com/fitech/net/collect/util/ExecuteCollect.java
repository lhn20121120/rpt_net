 package com.fitech.net.collect.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import com.cbrc.smis.adapter.StrutsListingTableDelegate;
import com.cbrc.smis.adapter.StrutsMCurrDelegate;
import com.cbrc.smis.form.MActuRepForm;
import com.cbrc.smis.form.MCurrForm;
import com.cbrc.smis.hibernate.MCurr;
import com.cbrc.smis.hibernate.ReportIn;
import com.cbrc.smis.security.Operator;
import com.fitech.net.adapter.StrutsCollectDelegate;
import com.fitech.net.adapter.StrutsCollectTypeDelegate;
import com.fitech.net.common.MCurrUtil;
import com.fitech.net.form.CollectTypeForm;
import com.fitech.net.collect.util.CollectUtil;
public class ExecuteCollect {
	
	public boolean collectReport(String childRepId,String versionId,String reportName,Integer dataRangeId,String dataRangeDesc,int year,int month,Operator operator,Integer curId)
	{
		boolean flag=false;
		String childOrgIds="";
		
		if(operator == null) return flag;
		/**
		 * �Ȼ������趨���ܷ�ʽ�ı�������
		 */
		List collectTypeFormList = StrutsCollectTypeDelegate.selectCollectTypes(operator.getOrgId(),childRepId,versionId);
		if(collectTypeFormList != null && collectTypeFormList.size() > 0){
			for(int i=0;i<collectTypeFormList.size();i++){
				CollectTypeForm collectTypeForm = (CollectTypeForm)collectTypeFormList.get(i);
				childOrgIds = StrutsCollectTypeDelegate.selectCollectOrgIds(collectTypeForm.getCollectId());
				
				ArrayList alDoc=new ArrayList();
				PreCollect preCollect=new PreCollect();
                               
				alDoc=preCollect.getNeededReportsDoc(childRepId,versionId,dataRangeId,year,month,childOrgIds,curId);
				if(alDoc.size()==0){
					// System.out.println("�������ϱ������û���!!!!!!!!!");
					continue;
				}
				Document totalDoc=null;
				
				CollectEngine ce=new CollectEngine(childRepId,versionId,alDoc);
				//��ʼ����
				totalDoc=ce.process();
				
				Integer repInId=null;
				if(totalDoc!=null){
				//������Report_in��Report_in_data��
				repInId=CollectUtil.insertDB(childRepId,versionId,reportName,dataRangeId,dataRangeDesc,year,month,operator,totalDoc,collectTypeForm.getCollectId(),curId);
				//���嵥ʽ������Report_in_info��
				if(!CollectUtil.isListRep(childRepId)){
					String root_next_name="";
					//�ж��Ƿ��������ļ��ṹ���ļ��ṹ��form1/Subform1����ģ��
					if(CollectUtil.isSpecRep(childRepId)){
						root_next_name=CollectUtil.Subform1Name;
					}else{
						root_next_name=CollectUtil.p1Name;
					}
					
					CollectUtil.insertRepInInfo(childRepId,versionId,repInId,root_next_name);
					flag=true;
					}
				//�嵥ʽ������Report_in_info��
				}
				if(CollectUtil.isListRep(childRepId)){
					//���ݿ��ж�Ӧĳ���嵥ʽ����ı���
					String tableName="";
					try {
						tableName=StrutsListingTableDelegate.getTalbeName(childRepId,versionId);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					ce.insertListRep(totalDoc,repInId,tableName);
					flag=true;
				}
			}
		}
		
		//��ȡ��ǰ�����������ӻ���	    
	    childOrgIds = operator.getChildOrgIds();
	    
		ArrayList alDoc=new ArrayList();
		PreCollect preCollect=new PreCollect();
		alDoc=preCollect.getNeededReportsDoc(childRepId,versionId,dataRangeId,year,month,childOrgIds,curId);
		if(alDoc.size()==0){
			// System.out.println("�������ϱ������û���!!!!!!!!!");
			return true;
		}
		Document totalDoc=null;
		
		CollectEngine ce=new CollectEngine(childRepId,versionId,alDoc);
		//��ʼ����
		totalDoc=ce.process();
		
		Integer repInId=null;
		if(totalDoc!=null){
		//������Report_in��Report_in_data��
		repInId=CollectUtil.insertDB(childRepId,versionId,reportName,dataRangeId,dataRangeDesc,year,month,operator,totalDoc,curId);
		//���嵥ʽ������Report_in_info��
		if(!CollectUtil.isListRep(childRepId)){
			String root_next_name="";
			//�ж��Ƿ��������ļ��ṹ���ļ��ṹ��form1/Subform1����ģ��
			if(CollectUtil.isSpecRep(childRepId)){
				root_next_name=CollectUtil.Subform1Name;
			}else{
				root_next_name=CollectUtil.p1Name;
			}
			
			CollectUtil.insertRepInInfo(childRepId,versionId,repInId,root_next_name);
			flag=true;
			}
		//�嵥ʽ������Report_in_info��
		}
		if(CollectUtil.isListRep(childRepId)){
			//���ݿ��ж�Ӧĳ���嵥ʽ����ı���
			String tableName="";
			try {
				tableName=StrutsListingTableDelegate.getTalbeName(childRepId,versionId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ce.insertListRep(totalDoc,repInId,tableName);
			flag=true;
		}
		
		return flag;
	}
	
	
	
	/**ȫ������*/
	public void execute_DB2Excel(List repList,int year,int month,Operator operator)
	{
        if(repList==null){
			return;
		}
		MActuRepForm marf=null;
		for(int i=0;i<repList.size();i++)
		{
			marf=(MActuRepForm)repList.get(i);
			
			List mCurrFormList = MCurrUtil.newInstance().isExist(marf.getChildRepId());
		    
		    if(mCurrFormList == null || mCurrFormList.size() <=0){
		    	mCurrFormList = new ArrayList();
		    	
		    	MCurr mCurr = null;
				try {
					mCurr = StrutsMCurrDelegate.getMCurr("�����");
				} catch (Exception e) {
					mCurr = null;
					e.printStackTrace();
				}
		    	if(mCurr == null) continue;
		    	
		    	MCurrForm mCurrForm = new MCurrForm();
		    	mCurrForm.setCurId(mCurr.getCurId());
		    	mCurrForm.setCurName(mCurr.getCurId() + "_" + "�����");
		    	mCurrFormList.add(mCurrForm);
		    }
		    for(int j=0;j<mCurrFormList.size();j++){
		    	MCurrForm mCurrForm = (MCurrForm)mCurrFormList.get(j);
		    	this.collectReport(marf.getChildRepId(),marf.getVersionId(),marf.getReportName(),marf.getDataRangeId(),marf.getDataRgDesc(),year,month,operator,mCurrForm.getCurId());
		    }			
		}		
	}
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * �����ϱ����ݲ�����XML��ʽ���Ǵ�Report_in_info ������ȡexcel��Ԫ�����ݻ���
     */
   
    /**
     * ��ȡ�ϱ���ʵ���ӱ���Id�ַ���
     * @author  gongming
     * @date    2007-09-12
     * 
     * @param childRepId        String          �ӱ���Id
     * @param versionId         String          �汾��
     * @param reportName        reportName      ������
     * @param dataRangeId       Integer         ���ݷ�ΧId
     * @param dataRangeDesc     String          ��������
     * @param year              int             ���
     * @param month             int             �·�
     * @param operator          Operator        ����Ա
     * @param curId             Integer         ����Id
     * @return  String
     */
    public String getRepInIds(String childRepId,String versionId,String reportName,Integer dataRangeId,String dataRangeDesc,int year,int month,Operator operator,Integer curId)
    {
        String childOrgIds= "";        
        if(operator == null) return null;
        
        childOrgIds = operator.getChildOrgIds();
       
        PreCollect preCollect=new PreCollect();       
        return preCollect.getNeededReportsIds(childRepId,versionId,dataRangeId,year,month,childOrgIds,curId);
    }
    
    /**
     * ���ܵ�������
     * @author  gongming
     * @date    2007-09-12 
     * @param childRepId        String          �ӱ���Id
     * @param versionId         String          �汾��
     * @param reportName        reportName      ������
     * @param dataRangeId       Integer         ���ݷ�ΧId
     * @param dataRangeDesc     String          ��������
     * @param year              int             ���
     * @param month             int             �·�
     * @param operator          Operator        ����Ա
     * @param curId             Integer         ����Id
     * @return  ���ܳɹ�����true  ���򷵻�false
     */
    public boolean collectReports(String childRepId,String versionId,String reportName,Integer dataRangeId,String dataRangeDesc,int year,int month,Operator operator,Integer curId)
    {              
        String repInIds = null;       
        repInIds = getRepInIds(childRepId,versionId,reportName,dataRangeId,dataRangeDesc,year,month,operator,curId);
       
        if(repInIds == null ||"".equals(repInIds)) return false;
        ReportIn reportIn = CollectUtil.createReportIn(childRepId,versionId,reportName,dataRangeId,dataRangeDesc,year,month,operator,curId);
        return StrutsCollectDelegate.saveCollectValue(repInIds,reportIn,operator);
    }
    
    public boolean collectMultReport(){
        return true;
    }
    /**
     * �������б���
     * @author  gongming
     * @date    2007-09-12
     * 
     * @param repList       List        �ӱ�����
     * @param year          int         �������
     * @param month         int         �·�
     * @param operator      Operator    ����Ա
     */
    public boolean collectAllReport(List repList,int year,int month,Operator operator)
    {
        if(repList==null)   return false;
        List repInIdLst = new ArrayList();
        MActuRepForm marf=null;
        for(int i=0;i<repList.size();i++)
        {
            marf=(MActuRepForm)repList.get(i);            
            List mCurrFormList = MCurrUtil.newInstance().isExist(marf.getChildRepId());
            
            if(mCurrFormList == null || mCurrFormList.size() <=0){
                mCurrFormList = new ArrayList();                
                MCurr mCurr = null;
                try {
                    mCurr = StrutsMCurrDelegate.getMCurr("�����");
                } catch (Exception e) {
                    mCurr = null;
                    e.printStackTrace();
                }
                if(mCurr == null) continue;
                
                MCurrForm mCurrForm = new MCurrForm();
                mCurrForm.setCurId(mCurr.getCurId());
                mCurrForm.setCurName(mCurr.getCurId() + "_" + "�����");
                mCurrFormList.add(mCurrForm);
            }
            //�϶δ����Ϊԭ������д���ɱ���������
            int size = mCurrFormList.size();
            
            
            // ѭ����ȡʵ���ϱ��ӱ���Id ������һ���µ�ReportIn
            for(int j  =0;j < size; j++){
                MCurrForm mCurrForm = (MCurrForm)mCurrFormList.get(j);
                String repInIds = getRepInIds(marf.getChildRepId(),marf.getVersionId(),marf.getReportName(),marf.getDataRangeId(),marf.getDataRgDesc(),year,month,operator,mCurrForm.getCurId());
               
                if(repInIds != null && !"".equals(repInIds)){
                    ReportIn reportIn = CollectUtil.createReportIn(marf.getChildRepId(),marf.getVersionId(),marf.getReportName(),marf.getDataRangeId(),marf.getDataRgDesc(),year,month,operator,mCurrForm.getCurId());
                    if(reportIn != null){
                        Object[] rep = new  Object[]{repInIds,reportIn};
                        repInIdLst.add(rep);
                    }
                }
            }           
        }      
        return StrutsCollectDelegate.saveAllCollectValue(repInIdLst,operator);
    }
}
