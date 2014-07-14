/*
 * Created on 2006-5-17
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.fitech.net.collect.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.dom4j.Document;
import org.dom4j.Element;

import com.cbrc.smis.adapter.StrutsListingTableDelegate;
import com.fitech.net.collect.CollectReport;
import com.fitech.net.common.XmlParser;
import com.fitech.net.config.Config;

/**
 * @author Administrator
 *
 * TODO To 
 * 
 */
public class CollectEngine {
	/**����ģʽ true:������ģʽ false:�౨��ģʽ*/
	private boolean flag;	
	/**ģ��ID*/
	private String templetID;
	/**ģ��汾��*/
	private String versionID;
	/**ģ�弯��*/
	private HashSet templetSet;
	/**ģ�������ļ�*/
	private static Document doc;
	private ArrayList al;
	
	private CollectReport collectReport = null;

	Locale LOCALE=Locale.CHINA;
//	/**������ܱ���*/
//	public static final String[] especRep={"G0105","G1301","G1302","G1303","G1304","G1401","G1402","G1403","G1500","G2300","G2400","G3301","G3302"};
//
//	/**�����ⵥԪ��ı���*/
//	public static final String[] especCellRep={"G2200","G4200"};

	public CollectEngine(String reportID, String versionID){
		this.setTempletID(reportID);
	    this.setVersionID(versionID);
		this.setFlag(true);
	}
	
	public CollectEngine(String reportID, 
						 String versionID, 
						 ArrayList al){
		this.setAl(al);
		this.setVersionID(versionID);
		this.setTempletID(reportID);
		this.setFlag(true);
	}
	
	public CollectEngine(HashSet templetSet){
		this.setTempletSet(templetSet);
		this.setFlag(false);
	}
	
	/**
	 * ��ø�ģ��Ĵ���ʵ��
	 * 
	 * @param reportID
	 * @return
	 */
	public Collect getReportCollectInstance(String reportID, String versionID){
		Collect currentCollect = null;
		
		String classPath = getClassPath(reportID, versionID);
		if (!"".equals(classPath)) {			
			try {
				currentCollect = (Collect)Class.forName(classPath).newInstance();			
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return currentCollect;
	}

	/**
	 * ��������
	 * @return
	 */
	private Document processSingleTemplet(){
		Document totalDoc=null;
		String reportID = this.getTempletID();
		String versionID = this.getVersionID();

		if (reportID == null || 
				"".equals(reportID) || 
				isExistReport(reportID, versionID) == false) {
			return null;
		}else{			
			collectReport=new CollectReport(reportID, versionID);
			  
			     totalDoc=collectReport.startCollectReport(this.getAl());
		}
		
		return totalDoc;
	}

	/**
	 * ��������
	 * @return
	 */
	private Object processSingleTemplet(String reportID, String versionID){
		Document totalDoc=null;
		Object logObj = null;
		if (reportID == null || 
				"".equals(reportID) || 
				isExistReport(reportID, versionID) == false) {
			// System.out.println("�ñ�������");
			return "�ñ�������";
		}else{
			
			collectReport=new CollectReport(reportID, versionID);
		     totalDoc=collectReport.startCollectReport(this.getAl());
			if(totalDoc!=null){
				logObj = "���ܳɹ�!";
			}
		}		
		return logObj;
	}
	
	/**
	 * ����������
	 * @return ��־���� key:reportID value:log
	 */
	private HashMap processMultiTemplet(){
		HashMap logMap = new HashMap();
		
		HashSet templets = this.getTempletSet();
		if (templets != null && templets.size() != 0) {
			Iterator iter = templets.iterator();			
			while(iter.hasNext()){
				TemplateInfo ti = (TemplateInfo)iter.next();
				String reportID = ti.getReportID();
				String versionID = ti.getVersionID();
				logMap.put(String.valueOf(new StringBuffer(reportID).append("_").append(versionID)), 
						processSingleTemplet(reportID, versionID));
			}
		}
		
		return logMap;
	}
	
	/**
	 * �жϸñ����Ƿ��������ļ��д���
	 * @param reportID ����ID
	 * 
	 * @return ������ڷ���true����false 
	 */
	private boolean isExistReport(String reportID, String versionID){
		boolean flag = false;
		
//		Element templateEle = (Element)doc.getRootElement().selectSingleNode(
//				"templates/template[@reportID='"+ reportID +"' and @versionID='"+ versionID +"']");
		Element templateEle = (Element)doc.getRootElement().selectSingleNode(
				"templates/template[@reportID='"+ reportID +"']");
		if (templateEle != null) {
			flag = true;
		}
		
		return flag;
	}
	
	/**
	 * ����嵥ʽģ��Ĵ���ʵ��
	 * 
	 * @param reportID
	 * @return
	 */
	private ListCollect getListReportCollectInstance(String reportID, String versionID){
		ListCollect currentCollect = null;
		
		String classPath = getClassPath(reportID, versionID);
		if (!"".equals(classPath)) {			
			try {
				currentCollect = (ListCollect)Class.forName(classPath).newInstance();			
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return currentCollect;
	}
	
	/**
	 * ���������ļ���ø�ģ�����·��
	 * @param reportID ģ������
	 * @return ���������·��
	 */
	private String getClassPath(String reportID, String versionID){
		
		String packagePath = "";
	
		Element rootEle = doc.getRootElement();
		/*
		 * ����package��
		 */
		Element templatesEle = (Element)rootEle.selectSingleNode("templates");
		String packageStr = "";
		if (templatesEle != null) {
			packageStr = String.valueOf(templatesEle.valueOf("@package"));
		}
		
		/*
		 * ����Class��
		 */
		Element templateEle = (Element)templatesEle.selectSingleNode("" +
				"template[@reportID='"+ reportID +"' and @versionID='"+ versionID +"']");
//		Element templateEle = (Element)templatesEle.selectSingleNode("" +
//				"template[@reportID='"+ reportID +"']");
		if (templateEle != null) {
			packagePath = packageStr + "." + String.valueOf(templateEle.valueOf("@class"));
		}
		
		return packagePath;
	}
	
	
	
	/**
	 * ִ��������
	 * @param flag ���ļ������־ true ���ļ�����ģʽ false ���ļ�����ģʽ
	 */
	public Document process(){
		boolean flag = isFlag();
		Document totalDoc=null;
		if (flag) {
			totalDoc = processSingleTemplet();
			//logManager(log);
		}else{
			HashMap logMap = processMultiTemplet();
			this.logManager(logMap);
		}
		return totalDoc;
	}
	
	/**
	 * ���嵥ʽ����ĵ�Ԫ����report_in_info��
	 * @return
	 */
	public void insertListRep(Document totalDoc,Integer repInId,String tableName)
	{
		String reportID = this.getTempletID();
		String versionID = this.getVersionID();
		if (reportID == null || 
				"".equals(reportID) || 
				isExistReport(reportID, versionID) == false) {
			// System.out.println("�ñ�������");
			return;
		}else{
		//	collect = this.getListReportCollectInstance(reportID, versionID);
			//��ʼ���
			/* �ж��Ƿ����嵥ʽ����������(S3209,S3402,S3600) */
			int QDReportType = collectReport.ListingReportType;  
			int maxCol =collectReport.maxCol;
			if(QDReportType == 0){				
				this.insertListRep(totalDoc,repInId,tableName,maxCol); 
			}else if(QDReportType == 1){
				this.insertListingRepS(totalDoc,repInId,tableName,maxCol);
			}else if(QDReportType == 2){
				
				this.insertS3401(totalDoc,repInId,tableName,maxCol,collectReport.specialCol);
			}
			else{
				// System.out.println("~~~~~~collectReport.ListingReportType="+collectReport.ListingReportType + " ������ȡ����");
			}
//			//��ȡ��־
//			logObj = collect.getLogs();
//			//ֹͣ���ܳ���
//			collect.stop();
		}
	}
	
	/**
	 * ���嵥ʽ����ĵ�Ԫ����report_in_info��(S3401)
	 * @return
	 */
	public  void insertS3401(Document totalDoc,Integer repInId,String tableName,int maxCol,String[] specialCol)
	{
		if(totalDoc==null) return;
		if(repInId==null) return;
		if(tableName==null || tableName.equals("")) return;
		if(specialCol==null || specialCol.length<=0) return ;
		 int specialCol1=-1;
		 int specialCol2=-1;
		try{
			 specialCol1=Integer.parseInt(specialCol[0]);
			 specialCol2=Integer.parseInt(specialCol[1]);
		}catch(Exception e){
			// System.out.println("���S3401�������д���!!!");
			e.printStackTrace();
		}
		
		int endCol=maxCol;
		//���ڵ�
		Element root=totalDoc.getRootElement();
		if(root==null) return;
		//Subform1�ڵ�
		Element sf=(Element)root.selectSingleNode(CollectUtil.Subform1Name);
		if(sf==null) return;
		//���Զ�������ֵ
		int seq=1;
		//���sql���ļ��϶���
		ArrayList sqls=new ArrayList();
		//sql���
		StringBuffer sql=null;
		//detail�ڵ㼯��
		List detailList=sf.selectNodes(CollectUtil.detailName);
		//detail�ڵ�
		Element detail=null;
		//����detail�ڵ�
		if(detailList!=null && detailList.size()>0){
			for(int i=0;i<detailList.size();i++)
			{
				detail=(Element)detailList.get(i);
				Element e=null;
				String eValue="";
				for(int j=1;j<=3;j++)
				{
					sql=new StringBuffer();
					sql.append("insert into ");
					sql.append(tableName);
					sql.append("(rep_in_id,col1");
					for(int m=2;m<=endCol;m++)
					{
						sql.append(",col");
						sql.append(String.valueOf(m));
					}
					sql.append(") values(");
					sql.append(String.valueOf(repInId.intValue()));
					sql.append(",");
					sql.append("'");
					sql.append(String.valueOf(seq));
					sql.append("'");
					//����col2-col4
					for(int k=2;k<=4;k++)
					{
						e=(Element)detail.selectSingleNode(CollectUtil.colName+k);
						if(e!=null){
							eValue=e.getText();
							if(eValue==null || eValue.equals("")){
								eValue="0";
							}
						}else{
							eValue="0";
						}
						sql.append(",");
						sql.append("'");
						sql.append(eValue);
						sql.append("'");
					}
					//����col5-col13
					for(int m=5;m<specialCol1;m++)
					{
						e=(Element)detail.selectSingleNode(CollectUtil.colName+m+"-"+j);
						if(e!=null){
							eValue=e.getText();
							if(eValue==null || eValue.equals("")){
								eValue="0";
							}
						}else{
							eValue="0";
						}
						sql.append(",");
						sql.append("'");
						sql.append(eValue);
						sql.append("'");
					}
					//����col14-col15
					for(int n=specialCol1;n<=specialCol2;n++)
					{
						e=(Element)detail.selectSingleNode(CollectUtil.colName+n);
						if(e!=null){
							eValue=e.getText();
							if(eValue==null || eValue.equals("")){
								eValue="0";
							}
						}else{
							eValue="0";
						}
						sql.append(",");
						sql.append("'");
						sql.append(eValue);
						sql.append("'");
					}
					//����col16-col20
					for(int a=specialCol2+1;a<=endCol;a++)
					{
						e=(Element)detail.selectSingleNode(CollectUtil.colName+a+"-"+j);
						if(e!=null){
							eValue=e.getText();
							if(eValue==null || eValue.equals("")){
								eValue="0";
							}
						}else{
							eValue="0";
						}
						sql.append(",");
						sql.append("'");
						sql.append(eValue);
						sql.append("'");
					}
					sql.append(")");
					sqls.add(sql.toString());
					seq++;
				}//for ÿ��detail��1-3
			}//for detailList
		}//if detailList
		
		//����total�ڵ�
		Element total=(Element)sf.selectSingleNode(CollectUtil.totalName);
		if(total==null) return;
		sql=new StringBuffer();
		sql.append("insert into ");
		sql.append(tableName);
		sql.append("(rep_in_id,col1");
		for(int m=2;m<=endCol;m++)
		{
			sql.append(",col");
			sql.append(String.valueOf(m));
		}
		sql.append(") values(");
		sql.append(String.valueOf(repInId.intValue()));
		sql.append(",");
		sql.append("'");
		sql.append(String.valueOf(seq));
		sql.append("'");
		Element e1=null;
		String value1="";
		//����col2-col20
		for(int b=2;b<=endCol;b++)
		{
			e1=(Element)total.selectSingleNode(CollectUtil.colName+b);
			if(e1!=null){
				value1=e1.getText();
				if(value1==null || value1.equals("")){
					value1="0";
				}
			}else{
				value1="0";
			}
			sql.append(",");
			sql.append("'");
			sql.append(value1);
			sql.append("'");
		}//for total
		sql.append(")");
		sqls.add(sql.toString());
		seq++;
		StrutsListingTableDelegate.insertListRep(sqls);
	}

	/**
	 * ���嵥ʽ����ĵ�Ԫ����report_in_info��(���ӱ��)
	 * @return
	 */
	public void insertListingRepS(Document totalDoc,Integer repInId,String tableName,int maxCol)
	{
		int endCol=maxCol;
		//���
		int seq=1;
		//���sql���ļ��϶���
		ArrayList sqls=new ArrayList();
		//���ڵ�
		Element root=null;
		root=totalDoc.getRootElement();
		if(root==null) return;
		//ȡ�����е�Subform1�ڵ� (�ж��ٸ��ӱ�,���ж��ٸ�subform1)
		List sfList=root.selectNodes(CollectUtil.Subform1Name);         
		if(sfList==null) return;
		if(sfList.size()==0) return;
		Element sf=null;
		List detailList=null;
		Element total=null;
		//����sfList
		StringBuffer sql=null;
		for(int i=0;i<sfList.size();i++)
		{
			sf=(Element)sfList.get(i);  //ȡ����֮һ���ӱ�
			detailList=sf.selectNodes(CollectUtil.detailName+(i+1));
			Element detail=null;
			Element col=null;
			String colValue="";
			//����detail����
			if(detailList!=null && detailList.size()>0){
				for(int j=0;j<detailList.size();j++)
				{
					detail=(Element)detailList.get(j);
					sql=new StringBuffer();
					sql.append("insert into ");
					sql.append(tableName);
					sql.append("(rep_in_id,type,col1");
					for(int m=2;m<=endCol;m++)
					{
						sql.append(",");
						sql.append(CollectUtil.colName+m);
					}
					sql.append(") values(");
					sql.append(String.valueOf(repInId.intValue()));
					sql.append(",");
					sql.append("'");
					sql.append(detail.getName());
					sql.append("'");
					sql.append(",");
					sql.append("'");
					sql.append(String.valueOf(seq));
					sql.append("'");
					
					for(int n=2;n<=endCol;n++)
					{
						col=(Element)detail.selectSingleNode(CollectUtil.colName+n);
						if(col!=null){
							colValue=col.getText();
							if(colValue==null || colValue.equals("")){
								colValue="0";
							}
						}
						if(col==null){
							colValue="0";
						}
						sql.append(",");
						sql.append("'");
						sql.append(colValue);
						sql.append("'");
					}
					sql.append(")");
					sqls.add(sql.toString());
					seq++;
				}
			}
			//����total����
			total=(Element)sf.selectSingleNode(CollectUtil.totalName+(i+1));
			if(total!=null){
				sql=new StringBuffer();
				sql.append("insert into ");
				sql.append(tableName);
				sql.append("(rep_in_id,type,col1");
				for(int m=2;m<=endCol;m++)
				{
					sql.append(",");
					sql.append(CollectUtil.colName+m);
				}
				sql.append(") values(");
				sql.append(String.valueOf(repInId.intValue()));
				sql.append(",");
				sql.append("'");
				sql.append(detail.getName());
				sql.append("'");
				sql.append(",");
				sql.append("'");
				sql.append(String.valueOf(seq));
				sql.append("'");
				
				for(int n=2;n<=endCol;n++)
				{
					col=(Element)total.selectSingleNode(CollectUtil.colName+n);
					if(col!=null){
						colValue=col.getText();
						if(colValue==null || colValue.equals("")){
							colValue="0";
						}
					}
					if(col==null){
						colValue="0";
					}
					sql.append(",");
					sql.append("'");
					sql.append(colValue);
					sql.append("'");
				}
				sql.append(")");
				sqls.add(sql.toString());
				seq++;
			}
		}//for sfList
		
		StrutsListingTableDelegate.insertListRep(sqls);
	}
	/**
	 * ��(��ͨ)�嵥ʽ����ĵ�Ԫ����report_in_info��
	 * 
	 * @return
	 */
	public void insertListRep(Document totalDoc, Integer repInId,
			String tableName,int listingEndCol) {
		int endCol=listingEndCol;;
		// ���
		int seq = 1;
		// ���sql���ļ��϶���
		ArrayList sqls = new ArrayList();
		// ���ڵ�
		Element root = null;
		root = totalDoc.getRootElement();
		if (root == null)
			return;
		// Subform1�ڵ�
		Element sf = null;
		sf = (Element) root.selectSingleNode(CollectUtil.Subform1Name);
		if (sf == null)
			return;
		// detail�ڵ㼯��
		List details = null;
		details = sf.selectNodes(CollectUtil.detailName);
		if (details == null)
			return;
		if (details.size() == 0)
			return;
		// detail�ڵ�
		Element detail = null;
		// sql���
		StringBuffer sql = null;
		// ����details
		for (int i = 0; i < details.size(); i++) {
			detail = (Element) details.get(i);
			sql = new StringBuffer();
			sql.append("insert into ");
			sql.append(tableName);
			sql.append("(rep_in_id,col1");
			for (int m = 2; m <= endCol; m++) {
				sql.append(",col");
				sql.append(String.valueOf(m));
			}
			sql.append(")");
			sql.append(" values(");
			sql.append(String.valueOf(repInId.intValue()));
			sql.append(",");
			sql.append("'");
			sql.append(String.valueOf(seq));
			sql.append("'");
			// COL�ڵ�
			Element col = null;
			// COL�ڵ��ֵ
			String colValue = "0";
			// ȡ����COL�ڵ��ֵ��д��sql���
			for (int j = 2; j <= endCol; j++) {
				col = (Element) detail
						.selectSingleNode(CollectUtil.colName + j);
				if (col != null) {
					colValue = col.getText();
					if (colValue == null || colValue.equals("")) {
						colValue = "0";
					}
				}
				if (col == null) {
					colValue = "0";
				}
				sql.append(",");
				sql.append("'");
				sql.append(colValue);
				sql.append("'");
			}
			sql.append(")");
			sqls.add(sql.toString());
			seq++;
		}
		// total�ڵ�
		Element total = null;
		total = (Element) sf.selectSingleNode(CollectUtil.totalName);
		if (total != null) {
			sql = new StringBuffer();
			sql.append("insert into ");
			sql.append(tableName);
			sql.append("(rep_in_id,col1");
			for (int m = 2; m <= endCol; m++) {
				sql.append(",col");
				sql.append(String.valueOf(m));
			}
			sql.append(")");
			sql.append(" values(");
			sql.append(String.valueOf(repInId.intValue()));
			sql.append(",");
			sql.append("'");
			sql.append(String.valueOf(String.valueOf(seq)));
			sql.append("'");
			// total��COL�ڵ�
			Element total_col = null; 
			// COL�ڵ��ֵ
			String total_colValue = "0";
			// ȡ����COL�ڵ��ֵ��д��sql���
			for (int j = 2; j <= endCol; j++) {
				total_col = (Element) total
						.selectSingleNode(CollectUtil.colName + j);
				if (total_col != null) {
					total_colValue = total_col.getText();
					if (total_colValue == null || total_colValue.equals("")) {
						total_colValue = "0";
					}
				}
				if (total_col == null) {
					total_colValue = "0";
				}
				sql.append(",");
				sql.append("'");
				sql.append(total_colValue);
				sql.append("'");
			}
			sql.append(")");
			sqls.add(sql.toString());
		}
		// ��report_in_info��
		StrutsListingTableDelegate.insertListRep(sqls);
	}
	
	
	private void logManager(Object log){
		//������־
	}

	private void logManager(HashMap logMap){
		//������־
	}
	/**
	 * @return Returns the templetID.
	 */
	public String getTempletID() {
		return templetID;
	}
	/**
	 * @param templetID The templetID to set.
	 */
	public void setTempletID(String templetID) {
		this.templetID = templetID;
	}
	/**
	 * @return Returns the templetSet.
	 */
	public HashSet getTempletSet() {
		return templetSet;
	}
	/**
	 * @param templetSet The templetSet to set.
	 */
	public void setTempletSet(HashSet templetSet) {
		this.templetSet = templetSet;
	}	
	/**
	 * @return Returns the flag.
	 */
	public boolean isFlag() {
		return flag;
	}
	/**
	 * @param flag The flag to set.
	 */
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
	static{
		//��ʼ��,ģ�������ļ�
		doc = XmlParser.parseXml(Config.getCollectConfigFilePath());
	}

	/**
	 * @return Returns the al.
	 */
	public ArrayList getAl() {
		return al;
	}
	/**
	 * @param al The al to set.
	 */
	public void setAl(ArrayList al) {
		this.al = al;
	}
	/**
	 * @return Returns the versionID.
	 */
	public String getVersionID() {
		return versionID;
	}
	/**
	 * @param versionID The versionID to set.
	 */
	public void setVersionID(String versionID) {
		this.versionID = versionID;
	}
}