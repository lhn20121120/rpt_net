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
 * @author 王东伟
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
	/**特殊文件结构（文件结构是form1/Subform1）的报表*/
	public static final String[] specRep={"S3201","S3202","S3203","S3204","S3205","S3206","S3207","S3209","S3300","S3401","S3402","S3500","S3600","S3700","S3800","S4400"};
	/**清单报表*/
	public static final String[] listRep={"S3201","S3202","S3203","S3204","S3205","S3206","S3207","S3209","S3300","S3401","S3402","S3500","S3600","S3700","S3800","S4400"};
	/**特殊的清单报表*/
	public static final String[] specListRep={"S3209","S3402","S3600"};
	
	/**初始化Document*/
	public static Document initDoc(ArrayList alDoc)
	{
		Document totalDoc=null;
		if(alDoc!=null && alDoc.size()>0)
		{
			totalDoc=(Document)((Document)alDoc.get(0)).clone();
		}
		return totalDoc;
	}
	
	/**初始化清单式Document*/
	public static Document initListDoc(ArrayList alDoc)
	{
		Document totalDoc=null;
		if(alDoc!=null && alDoc.size()>0)
		{
			/**从集合中克隆出一个Document*/
			totalDoc=(Document)((Document)alDoc.get(0)).clone();
		}
		List nodes=totalDoc.selectNodes(listRoot+detailName);
		Element e=(Element)totalDoc.selectSingleNode("form1/Subform1");
		if(nodes!=null && nodes.size()>0){
			/**删掉所有的detail节点*/
			for(int i=0;i<nodes.size();i++)
			{
				e.remove((Element)totalDoc.selectSingleNode(listRoot+detailName));
			}
		}
		DocAndBlob.docToResultFile(totalDoc,"temp");
		return totalDoc;
	}
	
	/**初始化特殊的清单式Document*/
	public static Document initSpecListDoc(ArrayList alDoc)
	{
		Document totalDoc=null;
		if(alDoc!=null && alDoc.size()>0)
		{
			/**从集合中克隆出一个Document*/
			totalDoc=(Document)((Document)alDoc.get(0)).clone();
		}
		
		Element root=totalDoc.getRootElement();
		if(root==null) return totalDoc;
		/**Subform1节点集合*/
		List sfList=root.selectNodes(Subform1Name);
		if(sfList==null) return totalDoc;
		if(sfList.size()==0) return totalDoc;
		Element sf=null;
		/**detail节点集合*/
		List detailList=null;
		/**删掉所有的detail节点*/
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
	
	/**汇总所有可以累加的单元格
	 * 
	 * @param 起始列和结束列只能在AA-AZ之间
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
						/**检查该单元格是否存在*/
						if(elementByIdIsExist(totalDoc,unitId)){
							/**检查该单元格是否存在数据*/
							if(dataIsExist(totalDoc,unitId)){
								/**检查该单元格的数据是否是数字*/
								if(dataIsNum(totalDoc,unitId)){
									/**检查是否是特殊单元格,如果不是,则进行汇总*/
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
	
	/**汇总所有可以累加的单元格
	 * 
	 * @param 起始列和结束列只能在A-Z之间
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
					/**检查是否是特殊单元格,如果不是,则进行汇总*/
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
//					/**检查是否是特殊单元格,如果不是,则进行汇总*/
//					if(!isSpecId(unitId,specId)){
//						addDocsById(totalDoc,alDoc,unitId);
//					}
//				}
//			}
		}
		return totalDoc;
	}
	
	/**汇总清单式报表*/
	public static Document collectListReport(ArrayList alDoc,int startCol,int endCol,String[] specCol)
	{
		if(alDoc==null || alDoc.size()==0) return null;
		Document totalDoc=initListDoc(alDoc);
		
		/**汇总detail节点*/
		totalDoc=collectDetail(totalDoc,alDoc);
			
			Element totalE=(Element)totalDoc.selectSingleNode(listRoot+totalName);
			/**汇总total节点*/
			if(totalE!=null){
				/**报表中的detail节点集合*/
				List detailList=totalDoc.selectNodes(listRoot+detailName);
				Element totalColE=null;
				for(int k=startCol;k<=endCol;k++)
				{
					/**判断是否是不需汇总的列*/
					if(!isSpecCol(k,specCol)){
						/**total节点中的COL节点*/
						totalColE=(Element)totalDoc.selectSingleNode(listRoot+totalName+"/"+colName+k);
						if(totalColE!=null){
							/**int类型的结果*/
							int intResult=0;
							/**double类型的结果*/
							double dblResult=0;
							Element detailColE=null;
							for(int m=0;m<detailList.size();m++)
							{
								detailColE=(Element)(((Element)detailList.get(m)).selectSingleNode(colName+k));
								String type=null;
								if(detailColE!=null && StringTool.strIsNum(detailColE.getText())){
									type=StringTool.getDataType(detailColE.getText());
									if(type!=null){
										/**判断数据类型，之所以这么做是因为有的单元格是Integer型，有的单元格是Double型,这两种数据不兼容*/
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
	
	/**汇总清单式报表的detail行*/
	public static Document collectDetail(Document totalDoc,ArrayList alDoc)
	{
		/**存放所有detail节点的集合*/
		ArrayList alDetail=new ArrayList();
		Document tempDoc=null;
		Element tempE=null;
		/**遍历alDoc,取出detail节点存放到alDetail*/
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
			
		/**汇总detail节点*/
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
	
	/**汇总特殊的清单式报表,包括（S3209,S3402,S3600）*/
	public static Document collectSpecListRep(Document totalDoc,ArrayList alDoc,int startCol,int endCol)
	{
		Element root=totalDoc.getRootElement();
		if(root==null) return totalDoc;
		List sfList=root.selectNodes(Subform1Name);
		if(sfList==null) return totalDoc;
		if(sfList.size()==0) return totalDoc;
		//开始汇总
		Element sf=null;
		for(int i=0;i<sfList.size();i++)
		{
			//取出一个Subform1节点
			sf=(Element)sfList.get(i);
			//一个Document
			Document tempDoc=null;
			//单个Document的detail节点集合
			List detailList=null;
			//遍历alDoc,把detail节点放入Subform1
			for(int j=0;j<alDoc.size();j++)
			{
				//取出一个Document
				tempDoc=(Document)alDoc.get(j);
				//取出detail节点集合
				detailList=tempDoc.selectNodes(form1Name+"/"+Subform1Name+"/"+detailName+(i+1));
				//如果detailList非空，则把每个detailList节点插入Subform1节点
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
			
			//处理total节点
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

	/**检查该单元格是否存在数据*/
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
	
	/**检查该单元格的数据是否是数字*/
	public static boolean dataIsNum(Document totalDoc,String unitId)
	{
		boolean flag=false;
		Element tmpE=(Element)totalDoc.selectSingleNode(root+unitId);
		String data=tmpE.getText();
		if(data!=null && data.length()>0){
			//去掉字符串中的逗号
			data=StringTool.deleteDH(data);
			if(StringTool.strIsNum(data)){
				flag=true;
			}
		}
		return flag;
	}
	
	/**检查是否是特殊单元格*/
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
	
	/**在汇总清单式报表时使用
	 * 检查是否是特殊列
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
	
	
	/**汇总单个可以累加的单元格*/
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
			/**检查该单元格是否存在*/
			if(elementByIdIsExist(tmpDoc,id)){			
				tmpE=(Element)tmpDoc.selectSingleNode(root+id);
				/**检查该单元格是否存在数据*/
				if(dataIsExist(tmpDoc,id)){
					/**检查该单元格的数据是否是数字*/
					if(dataIsNum(tmpDoc,id)){
						result=result+Double.parseDouble(StringTool.deleteDH(tmpE.getText()));
					}
				}
			}
		}
		if(totalE!=null)
		totalE.setText(String.valueOf(result));
	}
	

	/**检查该单元格是否存在*/
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
	
	/**判断是否是特殊文件结构（文件结构是form1/Subform1）的模板*/
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
	
	/**判断是否是清单式模板*/
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
	
	/**构造ReportIn*/
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
	
	/**构造ReportIn*/
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
	
	/**构造ReportInData*/
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
	
	/**insert表report_in和report_in_data*/
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
	
	/**insert表report_in和report_in_data*/
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
	
	/**insert表ReportInInfo*/
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
	
	/**判断某个报表是否已汇总*/
	public static boolean hasCollected(MActuRepForm mar,String curOrgId,int year,int mon)
	{
		boolean flag=false;
		if(mar==null) {
			return flag;
		}
		//获得当前机构已汇总好的某个报表
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
	
	/**从应报报表集合中去除已经汇总好的报表*/
	public static List deleteHasCollectedReps(List resList,String curOrgId,int year,int mon)
	{
		if(resList==null) return resList;
		if(resList.size()==0) return resList;
		ArrayList al=new ArrayList();
		MActuRepForm mar=null;
		for(int i=0;i<resList.size();i++)
		{
			mar=(MActuRepForm)resList.get(i);
			//判断该报表是否已汇总
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

	/**汇总非1104的点对点累加报表*/
	public static boolean p2p_add_collect(String childRepId,String versionId,String ids,Integer repInId)
	{
		//返回标识
		boolean flag=false;
		if(repInId==null)
			return false;
		if(ids==null)
			return false;
		if(ids.equals(""))
			return false;
		//存放子报表id的数组
		String[] ids1=ids.split(",");
		if(ids1.length<=0)
			return false;
		//根据childRepId,versionId获取对应的MCellForm对象集合
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
			//取出一个MCellForm对象
			mcf=(MCellForm)cells.get(i);
			if(mcf!=null){
				//取得cellId
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
