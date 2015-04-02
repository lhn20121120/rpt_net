package com.fitech.net.collect.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.dom4j.Branch;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.cbrc.smis.adapter.StrutsMCellDelegate;
import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.adapter.StrutsMDataRgTypeDelegate;
import com.cbrc.smis.adapter.StrutsReportInDataDelegate;
import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.adapter.StrutsReportInInfoDelegate;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.MActuRepForm;
import com.cbrc.smis.form.MCellForm;
import com.cbrc.smis.form.ReportInInfoForm;
import com.cbrc.smis.hibernate.MCell;
import com.cbrc.smis.hibernate.MChildReport;
import com.cbrc.smis.hibernate.MChildReportPK;
import com.cbrc.smis.hibernate.MCurr;
import com.cbrc.smis.hibernate.MDataRgType;
import com.cbrc.smis.hibernate.MMainRep;
import com.cbrc.smis.hibernate.ReportIn;
import com.cbrc.smis.hibernate.ReportInData;
import com.cbrc.smis.hibernate.ReportInInfo;
import com.cbrc.smis.hibernate.ReportInInfoPK;
import com.cbrc.smis.security.Operator;
import com.fitech.net.collect.common.DocAndBlob;
import com.fitech.net.common.StringTool;
import com.fitech.net.config.Config;


/**
 * 
 * @author ����ΰ
 *
 */
public class CollectUtil {
	
	public static final String fName="F";
	public static final String p1Name="P1";
	public static final String form1Name="form1";
	public static final String Subform1Name="Subform1";
	public static final String root=fName+"/"+p1Name+"/";
	public static final String listRoot="form1/Subform1/";
	public static final String detailName="detail";
	public static final String totalName="total";
	public static final String colName="COL";
	public static final String fitechTitleName="fitechTitle";
	public static final String nothing="0";
	private static final String[] ARRCOLS={"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","AA","AB","AC","AD","AE","AF","AG","AH","AI","AJ","AK","AL","AM","AN","AO","AP","AQ","AR","AS","AT","AU","AV","AW","AX","AY","AZ"};
	/**�����ļ��ṹ���ļ��ṹ��form1/Subform1���ı���*/
	public static final String[] specRep={"S3201","S3202","S3203","S3204","S3205","S3206","S3207","S3209","S3300","S3401","S3402","S3500","S3600","S3700","S3800","S4400"};
	/**�嵥����*/
	public static final String[] listRep={"S3201","S3202","S3203","S3204","S3205","S3206","S3207","S3209","S3300","S3401","S3402","S3500","S3600","S3700","S3800","S4400"};
	/**������嵥����*/
	public static final String[] specListRep={"S3209","S3402","S3600"};
	
	/**��ʼ��Document*/
	public static Document initDoc(ArrayList alDoc)
	{
		Document totalDoc=null;
		if(alDoc!=null && alDoc.size()>0)
		{
			totalDoc=(Document)((Document)alDoc.get(0)).clone();
		}
		return totalDoc;
	}
	
	/**��ʼ���嵥ʽDocument*/
	public static Document initListDoc(ArrayList alDoc)
	{
		Document totalDoc=null;
		if(alDoc!=null && alDoc.size()>0)
		{
			/**�Ӽ����п�¡��һ��Document*/
			totalDoc=(Document)((Document)alDoc.get(0)).clone();
		}
		List nodes=totalDoc.selectNodes(listRoot+detailName);
		Element e=(Element)totalDoc.selectSingleNode("form1/Subform1");
		if(nodes!=null && nodes.size()>0){
			/**ɾ�����е�detail�ڵ�*/
			for(int i=0;i<nodes.size();i++)
			{
				e.remove((Element)totalDoc.selectSingleNode(listRoot+detailName));
			}
		}
		DocAndBlob.docToResultFile(totalDoc,"temp");
		return totalDoc;
	}
	
	/**��ʼ��������嵥ʽDocument*/
	public static Document initSpecListDoc(ArrayList alDoc)
	{
		Document totalDoc=null;
		if(alDoc!=null && alDoc.size()>0)
		{
			/**�Ӽ����п�¡��һ��Document*/
			totalDoc=(Document)((Document)alDoc.get(0)).clone();
		}
		
		Element root=totalDoc.getRootElement();
		if(root==null) return totalDoc;
		/**Subform1�ڵ㼯��*/
		List sfList=root.selectNodes(Subform1Name);
		if(sfList==null) return totalDoc;
		if(sfList.size()==0) return totalDoc;
		Element sf=null;
		/**detail�ڵ㼯��*/
		List detailList=null;
		/**ɾ�����е�detail�ڵ�*/
		for(int i=0;i<sfList.size();i++)
		{
			sf=(Element)sfList.get(i);
			detailList=sf.selectNodes(detailName+(i+1));
			if(detailList!=null && detailList.size()>0)
			{
				for(int j=0;j<detailList.size();j++)
				{
					sf.remove((Element)detailList.get(j));
				}
			}
		}
		
		
		return totalDoc;
	}
	
	/**�������п����ۼӵĵ�Ԫ��
	 * 
	 * @param ��ʼ�кͽ�����ֻ����AA-AZ֮��
	 * 
	 */
	public static Document collectSpecUnit(Document totalDoc,ArrayList alDoc,String startCol,int startRow,String endCol,int endRow,String[] specId)
	{
		if(startCol!=null && !startCol.equals("") && endCol!=null && !endCol.equals("")){			
			if(startCol.length()==2 && endCol.length()==2){
				for(char col=startCol.charAt(1);col<=endCol.charAt(1);col++)
				{
					for(int row=startRow;row<=endRow;row++)
					{
						String unitId=String.valueOf("A"+col)+String.valueOf(row);						
						/**���õ�Ԫ���Ƿ����*/
						if(elementByIdIsExist(totalDoc,unitId)){
							/**���õ�Ԫ���Ƿ��������*/
							if(dataIsExist(totalDoc,unitId)){
								/**���õ�Ԫ��������Ƿ�������*/
								if(dataIsNum(totalDoc,unitId)){
									/**����Ƿ������ⵥԪ��,�������,����л���*/
									if(!isSpecId(unitId,specId)){
										addDocsById(totalDoc,alDoc,unitId);
									}
								}
							}
						}
					}
					
				}
			}
		}
		return totalDoc;
	}
	
	/**�������п����ۼӵĵ�Ԫ��
	 * 
	 * @param ��ʼ�кͽ�����ֻ����A-Z֮��
	 * 
	 */
	public static Document collectUnits(Document totalDoc,ArrayList alDoc,String startCol,int startRow,String endCol,int endRow,String[] specId)
	{
		if(startCol!=null && !startCol.equals("") && endCol!=null && !endCol.equals("")){
			int start = 0;
			int end = 0;
			for(int i=0;i<ARRCOLS.length;i++){
				if(ARRCOLS[i].equals(startCol)) start=i;
				if(ARRCOLS[i].equals(endCol)) end = i;
			}
			for(int j=start;j<=end;j++){
				for(int row=startRow;row<=endRow;row++){
					String unitId = ARRCOLS[j]+String.valueOf(row);
					/**����Ƿ������ⵥԪ��,�������,����л���*/
					if(!isSpecId(unitId,specId)){
						addDocsById(totalDoc,alDoc,unitId);
					}
				}
			}
//			for(char col=startCol.charAt(0);col<=endCol.charAt(0);col++)
//			{
//				for(int row=startRow;row<=endRow;row++)
//				{
//					String unitId=String.valueOf(col)+String.valueOf(row);
//					/**����Ƿ������ⵥԪ��,�������,����л���*/
//					if(!isSpecId(unitId,specId)){
//						addDocsById(totalDoc,alDoc,unitId);
//					}
//				}
//			}
		}
		return totalDoc;
	}
	
	/**�����嵥ʽ����*/
	public static Document collectListReport(ArrayList alDoc,int startCol,int endCol,String[] specCol)
	{
		if(alDoc==null || alDoc.size()==0) return null;
		Document totalDoc=initListDoc(alDoc);
		
		/**����detail�ڵ�*/
		totalDoc=collectDetail(totalDoc,alDoc);
			
			Element totalE=(Element)totalDoc.selectSingleNode(listRoot+totalName);
			/**����total�ڵ�*/
			if(totalE!=null){
				/**�����е�detail�ڵ㼯��*/
				List detailList=totalDoc.selectNodes(listRoot+detailName);
				Element totalColE=null;
				for(int k=startCol;k<=endCol;k++)
				{
					/**�ж��Ƿ��ǲ�����ܵ���*/
					if(!isSpecCol(k,specCol)){
						/**total�ڵ��е�COL�ڵ�*/
						totalColE=(Element)totalDoc.selectSingleNode(listRoot+totalName+"/"+colName+k);
						if(totalColE!=null){
							/**int���͵Ľ��*/
							int intResult=0;
							/**double���͵Ľ��*/
							double dblResult=0;
							Element detailColE=null;
							for(int m=0;m<detailList.size();m++)
							{
								detailColE=(Element)(((Element)detailList.get(m)).selectSingleNode(colName+k));
								String type=null;
								if(detailColE!=null && StringTool.strIsNum(detailColE.getText())){
									type=StringTool.getDataType(detailColE.getText());
									if(type!=null){
										/**�ж��������ͣ�֮������ô������Ϊ�еĵ�Ԫ����Integer�ͣ��еĵ�Ԫ����Double��,���������ݲ�����*/
										if(type.equals("Integer"))
											intResult=intResult+Integer.parseInt(StringTool.deleteDH(detailColE.getText()));
										if(type.equals("Double"))
											dblResult=dblResult+Double.parseDouble(StringTool.deleteDH(detailColE.getText()));
									}
								}
							}
							if(intResult!=0)
								totalColE.setText(String.valueOf(intResult));
							if(dblResult!=0)
								totalColE.setText(String.valueOf(dblResult));
						}
					}
				}
			}
		
		return totalDoc;
	}
	
	/**�����嵥ʽ�����detail��*/
	public static Document collectDetail(Document totalDoc,ArrayList alDoc)
	{
		/**�������detail�ڵ�ļ���*/
		ArrayList alDetail=new ArrayList();
		Document tempDoc=null;
		Element tempE=null;
		/**����alDoc,ȡ��detail�ڵ��ŵ�alDetail*/
		for(int i=0;i<alDoc.size();i++)
		{
			List nodes=null;
			tempDoc=(Document)alDoc.get(i);
			tempE=(Element)tempDoc.selectSingleNode("form1/Subform1");
			if(tempE!=null){
				nodes=tempE.selectNodes(detailName);
				if(nodes!=null && nodes.size()>=1){
					for(int j=0;j<nodes.size();j++)
					{
						alDetail.add((Node)nodes.get(j));
					}
				}
			}
		}
			
		/**����detail�ڵ�*/
		if(totalDoc==null) return null;
		Element Subform1E=(Element)totalDoc.selectSingleNode("form1/Subform1");
		Element root=totalDoc.getRootElement();
		if(Subform1E!=null){
			Element detailE=null;
			for(int k=0;k<alDetail.size();k++){
				detailE=Subform1E.addElement("detail");
				((Branch)detailE).appendContent((Branch)alDetail.get(k));
			}
		}
		return totalDoc;
	}
	
	/**����������嵥ʽ����,������S3209,S3402,S3600��*/
	public static Document collectSpecListRep(Document totalDoc,ArrayList alDoc,int startCol,int endCol)
	{
		Element root=totalDoc.getRootElement();
		if(root==null) return totalDoc;
		List sfList=root.selectNodes(Subform1Name);
		if(sfList==null) return totalDoc;
		if(sfList.size()==0) return totalDoc;
		//��ʼ����
		Element sf=null;
		for(int i=0;i<sfList.size();i++)
		{
			//ȡ��һ��Subform1�ڵ�
			sf=(Element)sfList.get(i);
			//һ��Document
			Document tempDoc=null;
			//����Document��detail�ڵ㼯��
			List detailList=null;
			//����alDoc,��detail�ڵ����Subform1
			for(int j=0;j<alDoc.size();j++)
			{
				//ȡ��һ��Document
				tempDoc=(Document)alDoc.get(j);
				//ȡ��detail�ڵ㼯��
				detailList=tempDoc.selectNodes(form1Name+"/"+Subform1Name+"/"+detailName+(i+1));
				//���detailList�ǿգ����ÿ��detailList�ڵ����Subform1�ڵ�
				if(detailList!=null && detailList.size()>0)
				{
					Element detail=null;
					for(int m=0;m<detailList.size();m++)
					{
						detail=sf.addElement(detailName+(i+1));
						((Branch)detail).appendContent((Branch)detailList.get(m));
					}
				}
			}
			
			//����total�ڵ�
			Element total=(Element)sf.selectSingleNode(CollectUtil.totalName+(i+1));
			List dList=sf.selectNodes(CollectUtil.detailName+(i+1));
			Element d=null;
			Element col=null;
			if(total!=null && dList!=null && dList.size()>0){
				for(int n=startCol;n<=endCol;n++)
				{
					double result=0;
					for(int k=0;k<dList.size();k++)
					{
						d=(Element)dList.get(k);
						col=(Element)d.selectSingleNode(CollectUtil.colName+n);
						if(col!=null && !col.getText().equals("") && StringTool.strIsNum(col.getText())){
							result=result+Double.parseDouble(StringTool.deleteDH(col.getText()));
						}
					}
					col=(Element)total.selectSingleNode(CollectUtil.colName+n);
					if(col!=null){
						col.setText(String.valueOf(result));
					}
				}
			}
		}//for sfList
		return totalDoc;
	}

	/**���õ�Ԫ���Ƿ��������*/
	public static boolean dataIsExist(Document totalDoc,String unitId)
	{
		boolean flag=false;
		Element tmpE=(Element)totalDoc.selectSingleNode(root+unitId);
		String data="";
		if(tmpE!=null){
			data=tmpE.getText();
		}else{
			return false;
		}
		
		if(data!=null && data.length()>0){
			flag=true;
		}
		
		return flag;
	}
	
	/**���õ�Ԫ��������Ƿ�������*/
	public static boolean dataIsNum(Document totalDoc,String unitId)
	{
		boolean flag=false;
		Element tmpE=(Element)totalDoc.selectSingleNode(root+unitId);
		String data=tmpE.getText();
		if(data!=null && data.length()>0){
			//ȥ���ַ����еĶ���
			data=StringTool.deleteDH(data);
			if(StringTool.strIsNum(data)){
				flag=true;
			}
		}
		return flag;
	}
	
	/**����Ƿ������ⵥԪ��*/
	public static boolean isSpecId(String unitId,String[] specId)
	{
		boolean flag=true;
		if(specId==null){
			return false;
		}
		for(int i=0;i<specId.length;i++)
		{
			if(unitId.equalsIgnoreCase(specId[i])){
				return flag;
			}
		}
		flag=false;
		return flag;
	}
	
	/**�ڻ����嵥ʽ����ʱʹ��
	 * ����Ƿ���������
	 * @return
	 */
	public static boolean isSpecCol(int col,String[] specCol)
	{
			String colStr=String.valueOf(col);
			boolean flag=true;
			if(specCol==null){
				return false;
			}
			for(int i=0;i<specCol.length;i++)
			{
				if(specCol[i].equals(colStr)){
					return flag;
				}
			}
			flag=false;
			return flag;
	}
	
	
	/**���ܵ��������ۼӵĵ�Ԫ��*/
	public static void addDocsById(Document totalDoc,ArrayList alDoc,String id)
	{
		double result=0;
		Document tmpDoc=null;
		Element tmpE=null;
		Element totalE=(Element)totalDoc.selectSingleNode("F/P1/"+id);
		if(totalE==null){
			Element root = totalDoc.getRootElement();
			Element element= root.element("P1");
			element.addElement(id);
			totalE=(Element)totalDoc.selectSingleNode("F/P1/"+id);			
		}
		for(int i=0;i<alDoc.size();i++)
		{
			tmpDoc=(Document)alDoc.get(i);
			/**���õ�Ԫ���Ƿ����*/
			if(elementByIdIsExist(tmpDoc,id)){			
				tmpE=(Element)tmpDoc.selectSingleNode(root+id);
				/**���õ�Ԫ���Ƿ��������*/
				if(dataIsExist(tmpDoc,id)){
					/**���õ�Ԫ��������Ƿ�������*/
					if(dataIsNum(tmpDoc,id)){
						result=result+Double.parseDouble(StringTool.deleteDH(tmpE.getText()));
					}
				}
			}
		}
		if(totalE!=null)
		totalE.setText(String.valueOf(result));
	}
	

	/**���õ�Ԫ���Ƿ����*/
	public static boolean elementByIdIsExist(Document totalDoc,String unitId)
	{
		boolean flag=false;
		if(totalDoc==null) return false;
		Element e=(Element)totalDoc.selectSingleNode(root+unitId);
		if(e!=null){
			flag=true;
		}
		return flag;
	}
	
	/**�ж��Ƿ��������ļ��ṹ���ļ��ṹ��form1/Subform1����ģ��*/
	public static boolean isSpecRep(String childRepId)
	{
		boolean flag=false;
		if(childRepId==null || childRepId.equals(""))
			return flag;
		for(int i=0;i<specRep.length;i++)
		{
			if(childRepId.equals(specRep[i])){
				flag=true;
				return flag;
			}
		}
		return flag;
	}
	
	/**�ж��Ƿ����嵥ʽģ��*/
	public static boolean isListRep(String childRepId)
	{
		boolean flag=false;
		if(listRep==null) return flag;
		if(listRep.length==0) return flag;
		for(int i=0;i<listRep.length;i++)
		{
			if(childRepId.equals(listRep[i])){
				flag=true;
				break;
			}
		}
		return flag;
	}
	
	/**����ReportIn*/
	public static ReportIn createReportIn(String childRepId,String versionId,String reportName,Integer dataRangeId,String dataRangeDesc,int year,int month,Operator operator,Integer curId)
	{
		String repName="";
		MChildReport mcr1=StrutsMChildReportDelegate.getMChileReport(childRepId,versionId);
		MMainRep mmr=null;
		if(mcr1!=null){
			mmr=mcr1.getMMainRep();
		}
		if(mmr!=null){
			repName=mmr.getRepCnName();
		}
		
		MChildReportPK  comp_id=new MChildReportPK();
		comp_id.setChildRepId(childRepId);
		comp_id.setVersionId(versionId);
		MChildReport mcr=new MChildReport();
		mcr.setComp_id(comp_id);
		MDataRgType mdrt=null;
		try {
			mdrt=StrutsMDataRgTypeDelegate.getMDataRgTypeOncb(dataRangeId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		MCurr mCurr = new MCurr();
		mCurr.setCurId(curId);
		
		ReportIn repIn=new ReportIn();
		repIn.setMChildReport(mcr);

		repIn.setRepName(repName);
		repIn.setMDataRgType(mdrt);
		repIn.setYear(new Integer(year));
		repIn.setTerm(new Integer(month));
		repIn.setMCurr(mCurr);
		repIn.setCheckFlag(Config.CHECK_FLAG_UNREPORT);
		repIn.setOrgId(operator.getOrgId());
		return repIn;
	}
	
	/**����ReportIn*/
	public static ReportIn createReportIn(String childRepId,String versionId,String reportName,Integer dataRangeId,String dataRangeDesc,int year,int month,Operator operator,Integer collectId,Integer curId)
	{
		String repName="";
		MChildReport mcr1=StrutsMChildReportDelegate.getMChileReport(childRepId,versionId);
		MMainRep mmr=null;
		if(mcr1!=null){
			mmr=mcr1.getMMainRep();
		}
		if(mmr!=null){
			repName=mmr.getRepCnName();
		}
		
		MChildReportPK  comp_id=new MChildReportPK();
		comp_id.setChildRepId(childRepId);
		comp_id.setVersionId(versionId);
		MChildReport mcr=new MChildReport();
		mcr.setComp_id(comp_id);
		MDataRgType mdrt=null;
		try {
			mdrt=StrutsMDataRgTypeDelegate.getMDataRgTypeOncb(dataRangeId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		MCurr mCurr = new MCurr();
		mCurr.setCurId(curId);
		
		ReportIn repIn=new ReportIn();
		repIn.setMChildReport(mcr);

		repIn.setRepName(repName);
		repIn.setMDataRgType(mdrt);
		repIn.setYear(new Integer(year));
		repIn.setTerm(new Integer(month));
		repIn.setMCurr(mCurr);
		repIn.setCheckFlag(Config.CHECK_FLAG_UNREPORT);
		repIn.setOrgId(operator.getOrgId());
		repIn.setPackage(collectId);
		return repIn;
	}
	
	/**����ReportInData*/
	public static ReportInData createRepInData(ReportIn repIn,String childRepId,String versionId,Document totalDoc)
	{
		ReportInData rid=new ReportInData();
		InputStream input=null;
		int size=0;
		Blob xmlBlob=null;
		try {
			input = new FileInputStream(DocAndBlob.docToResultFile(totalDoc,childRepId+"_"+versionId));
			size = input.available();
			xmlBlob = Hibernate.createBlob(input);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		rid.setRepInId(repIn.getRepInId());
		rid.setXml(xmlBlob);
		rid.setXmlSize(new Integer(size));
		return rid;
	}
	
	/**insert��report_in��report_in_data*/
	public static Integer insertDB(String childRepId,String versionId,String reportName,Integer dataRangeId,String dataRangeDesc,int year,int month,Operator operator,Document totalDoc,Integer curId)
	{
		Integer repInId=null;
		DBConn conn=new DBConn();
		Session session=conn.beginTransaction();
		ReportIn repIn=null;
		ReportInData rid=null;
		repIn=createReportIn(childRepId,versionId,reportName,dataRangeId,dataRangeDesc,year,month,operator,curId);
		try {
			session.save(repIn);
		} catch (HibernateException e1) {
			conn.endTransaction(false);
			e1.printStackTrace();
			return null;
		}
		if(repIn!=null){
			rid=createRepInData(repIn,childRepId,versionId,totalDoc);
			try {
				session.save(rid);
				conn.endTransaction(true);
				repInId=repIn.getRepInId();
			} catch (HibernateException e) {
				conn.endTransaction(false);
				e.printStackTrace();
			}finally{
				if(conn!=null) conn.closeSession();
			}
		}
		return repInId;
	}
	
	/**insert��report_in��report_in_data*/
	public static Integer insertDB(String childRepId,String versionId,String reportName,Integer dataRangeId,String dataRangeDesc,int year,int month,Operator operator,Document totalDoc,Integer collectId,Integer curId)
	{
		Integer repInId=null;
		DBConn conn=new DBConn();
		Session session=conn.beginTransaction();
		ReportIn repIn=null;
		ReportInData rid=null;
		repIn=createReportIn(childRepId,versionId,reportName,dataRangeId,dataRangeDesc,year,month,operator,collectId,curId);
		try {
			session.save(repIn);
		} catch (HibernateException e1) {
			conn.endTransaction(false);
			e1.printStackTrace();
			return null;
		}
		if(repIn!=null){
			rid=createRepInData(repIn,childRepId,versionId,totalDoc);
			try {
				session.save(rid);
				conn.endTransaction(true);
				repInId=repIn.getRepInId();
			} catch (HibernateException e) {
				conn.endTransaction(false);
				e.printStackTrace();
			}finally{
				if(conn!=null) conn.closeSession();
			}
		}
		return repInId;
	}
	
	/**insert��ReportInInfo*/
	public static void insertRepInInfo(String childRepId,String versionId,Integer repInId,String root_next_name)
	{
		if(repInId==null) return;
		List cellList=StrutsMCellDelegate.getAllCell(childRepId,versionId);
		if(cellList==null) return;
		if(cellList.size()==0) return;
		ReportInData rid=StrutsReportInDataDelegate.getReportInData(repInId);
		if(rid==null) return;
		Blob xml=rid.getXml();
		SAXReader reader=new SAXReader();
		Document totalDoc=null;
		try {
			if(xml!=null)
			totalDoc=reader.read(xml.getBinaryStream());
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(cellList==null || cellList.size()==0){
			return;
		}
		Element root=totalDoc.getRootElement();
		Element root_next=(Element)root.selectSingleNode(root_next_name);
		if(root_next==null){
			return;
		}
		
		MCell cell=null;
		String cellName=null;
		ReportInInfo repInInfo=null;
		ReportInInfoPK comp_id=new ReportInInfoPK();
		comp_id.setRepInId(repInId);
		Element e=null;
		
		DBConn conn=new DBConn();
		Session session=conn.beginTransaction();
		for(int i=0;i<cellList.size();i++)
		{
			repInInfo=new ReportInInfo();
			cell=(MCell)cellList.get(i);
			comp_id.setCellId(cell.getCellId());
			repInInfo.setComp_id(comp_id);
			cellName=cell.getCellName();
			e=(Element)root_next.selectSingleNode(cellName);
			String eValue=nothing;
			if(e!=null){ 
				eValue=e.getText();
				if(eValue==null || eValue.equals("")){
					eValue=nothing;
				}
			}

			repInInfo.setReportValue(eValue);
			try {
				session.save(repInInfo);
				session.flush();
			} catch (HibernateException e1) {
				e1.printStackTrace();
			} 
		}
		conn.endTransaction(true);
	}
	
	/**�ж�ĳ�������Ƿ��ѻ���*/
	public static boolean hasCollected(MActuRepForm mar,String curOrgId,int year,int mon)
	{
		boolean flag=false;
		if(mar==null) {
			return flag;
		}
		//��õ�ǰ�����ѻ��ܺõ�ĳ������
		List list=StrutsReportInDelegate.getHasCollectedRepsByCurOrg(mar,curOrgId,year,mon);
		if(list==null) {
			return false;
		}
		if(list.size()==0) {
			return false;
		}
		if(list.size()>0) {
			flag=true;
		}
		return flag;
	}
	
	/**��Ӧ����������ȥ���Ѿ����ܺõı���*/
	public static List deleteHasCollectedReps(List resList,String curOrgId,int year,int mon)
	{
		if(resList==null) return resList;
		if(resList.size()==0) return resList;
		ArrayList al=new ArrayList();
		MActuRepForm mar=null;
		for(int i=0;i<resList.size();i++)
		{
			mar=(MActuRepForm)resList.get(i);
			//�жϸñ����Ƿ��ѻ���
			if(hasCollected(mar,curOrgId,year,mon)){
				al.add(mar);
			}
		}
		if(resList!=null && al.size()>0){
			for(int j=0;j<al.size();j++)
			{
				resList.remove((MActuRepForm)al.get(j));
			}
		}
		return resList;
	}

	/**���ܷ�1104�ĵ�Ե��ۼӱ���*/
	public static boolean p2p_add_collect(String childRepId,String versionId,String ids,Integer repInId)
	{
		//���ر�ʶ
		boolean flag=false;
		if(repInId==null)
			return false;
		if(ids==null)
			return false;
		if(ids.equals(""))
			return false;
		//����ӱ���id������
		String[] ids1=ids.split(",");
		if(ids1.length<=0)
			return false;
		//����childRepId,versionId��ȡ��Ӧ��MCellForm���󼯺�
		List cells=StrutsMCellDelegate.getCells(childRepId,versionId);
		if(cells==null){
			return false;
		}
		if(cells.size()<=0){
			return false;
		}
		DBConn conn=new DBConn();
		Session session=conn.beginTransaction();
		MCellForm mcf=new MCellForm();
		Integer cellId=null;
		String id="";
		ReportInInfoForm riif=new ReportInInfoForm();
		ReportInInfo rii=null;
		ReportInInfoPK riiPK=null;
		for(int i=0;i<cells.size();i++)
		{
			//ȡ��һ��MCellForm����
			mcf=(MCellForm)cells.get(i);
			if(mcf!=null){
				//ȡ��cellId
				cellId=mcf.getCellId();
			}
			
			riiPK=new ReportInInfoPK();
			riiPK.setRepInId(repInId);
			riiPK.setCellId(cellId);
			rii=new ReportInInfo();
			rii.setComp_id(riiPK);
			String value="";
			double result=0;
			for(int j=0;j<ids1.length;j++)
			{
				id=ids1[j];
				riif=StrutsReportInInfoDelegate.getNeedReportInInfoForm(Integer.valueOf(id),cellId);
				
				if(riif!=null){
					value=riif.getReportValue();
				}
				if(value!=null && StringTool.strIsNum(value)){
					result=result+Double.parseDouble(StringTool.deleteDH(value));
				}
			}
			rii.setReportValue(String.valueOf(result));
			try {
				session.save(rii);
				session.flush();
				flag = true;
			} catch (HibernateException e) {
				flag = false;
				e.printStackTrace();
			} finally{
				if(conn!=null) conn.endTransaction(flag);
			}
		}
		return flag;
	}
	
}
