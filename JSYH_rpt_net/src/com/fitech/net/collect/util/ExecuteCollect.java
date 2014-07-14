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
		 * 先汇总已设定汇总方式的报表数据
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
					// System.out.println("无数据上报，不用汇总!!!!!!!!!");
					continue;
				}
				Document totalDoc=null;
				
				CollectEngine ce=new CollectEngine(childRepId,versionId,alDoc);
				//开始汇总
				totalDoc=ce.process();
				
				Integer repInId=null;
				if(totalDoc!=null){
				//报表入Report_in和Report_in_data表
				repInId=CollectUtil.insertDB(childRepId,versionId,reportName,dataRangeId,dataRangeDesc,year,month,operator,totalDoc,collectTypeForm.getCollectId(),curId);
				//非清单式报表入Report_in_info表
				if(!CollectUtil.isListRep(childRepId)){
					String root_next_name="";
					//判断是否是特殊文件结构（文件结构是form1/Subform1）的模板
					if(CollectUtil.isSpecRep(childRepId)){
						root_next_name=CollectUtil.Subform1Name;
					}else{
						root_next_name=CollectUtil.p1Name;
					}
					
					CollectUtil.insertRepInInfo(childRepId,versionId,repInId,root_next_name);
					flag=true;
					}
				//清单式报表入Report_in_info表
				}
				if(CollectUtil.isListRep(childRepId)){
					//数据库中对应某个清单式报表的表名
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
		
		//获取当前机构的所有子机构	    
	    childOrgIds = operator.getChildOrgIds();
	    
		ArrayList alDoc=new ArrayList();
		PreCollect preCollect=new PreCollect();
		alDoc=preCollect.getNeededReportsDoc(childRepId,versionId,dataRangeId,year,month,childOrgIds,curId);
		if(alDoc.size()==0){
			// System.out.println("无数据上报，不用汇总!!!!!!!!!");
			return true;
		}
		Document totalDoc=null;
		
		CollectEngine ce=new CollectEngine(childRepId,versionId,alDoc);
		//开始汇总
		totalDoc=ce.process();
		
		Integer repInId=null;
		if(totalDoc!=null){
		//报表入Report_in和Report_in_data表
		repInId=CollectUtil.insertDB(childRepId,versionId,reportName,dataRangeId,dataRangeDesc,year,month,operator,totalDoc,curId);
		//非清单式报表入Report_in_info表
		if(!CollectUtil.isListRep(childRepId)){
			String root_next_name="";
			//判断是否是特殊文件结构（文件结构是form1/Subform1）的模板
			if(CollectUtil.isSpecRep(childRepId)){
				root_next_name=CollectUtil.Subform1Name;
			}else{
				root_next_name=CollectUtil.p1Name;
			}
			
			CollectUtil.insertRepInInfo(childRepId,versionId,repInId,root_next_name);
			flag=true;
			}
		//清单式报表入Report_in_info表
		}
		if(CollectUtil.isListRep(childRepId)){
			//数据库中对应某个清单式报表的表名
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
	
	
	
	/**全部汇总*/
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
					mCurr = StrutsMCurrDelegate.getMCurr("人民币");
				} catch (Exception e) {
					mCurr = null;
					e.printStackTrace();
				}
		    	if(mCurr == null) continue;
		    	
		    	MCurrForm mCurrForm = new MCurrForm();
		    	mCurrForm.setCurId(mCurr.getCurId());
		    	mCurrForm.setCurName(mCurr.getCurId() + "_" + "人民币");
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
     * 汇总上报数据不采用XML方式而是从Report_in_info 表中提取excel单元格数据汇总
     */
   
    /**
     * 获取上报的实际子报表Id字符串
     * @author  gongming
     * @date    2007-09-12
     * 
     * @param childRepId        String          子报表Id
     * @param versionId         String          版本号
     * @param reportName        reportName      报表名
     * @param dataRangeId       Integer         数据范围Id
     * @param dataRangeDesc     String          数据描述
     * @param year              int             年份
     * @param month             int             月份
     * @param operator          Operator        操作员
     * @param curId             Integer         币种Id
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
     * 汇总单个报表
     * @author  gongming
     * @date    2007-09-12 
     * @param childRepId        String          子报表Id
     * @param versionId         String          版本号
     * @param reportName        reportName      报表名
     * @param dataRangeId       Integer         数据范围Id
     * @param dataRangeDesc     String          数据描述
     * @param year              int             年份
     * @param month             int             月份
     * @param operator          Operator        操作员
     * @param curId             Integer         币种Id
     * @return  汇总成功返回true  否则返回false
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
     * 汇总所有报表
     * @author  gongming
     * @date    2007-09-12
     * 
     * @param repList       List        子报表集合
     * @param year          int         报表年份
     * @param month         int         月份
     * @param operator      Operator    操作员
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
                    mCurr = StrutsMCurrDelegate.getMCurr("人民币");
                } catch (Exception e) {
                    mCurr = null;
                    e.printStackTrace();
                }
                if(mCurr == null) continue;
                
                MCurrForm mCurrForm = new MCurrForm();
                mCurrForm.setCurId(mCurr.getCurId());
                mCurrForm.setCurName(mCurr.getCurId() + "_" + "人民币");
                mCurrFormList.add(mCurrForm);
            }
            //上段代码均为原作者所写，由本方法借用
            int size = mCurrFormList.size();
            
            
            // 循环获取实际上报子报表Id 和生成一个新的ReportIn
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
