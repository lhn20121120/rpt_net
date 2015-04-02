package com.fitech.net.collect;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

import com.fitech.net.common.XmlParser;
import com.fitech.net.config.Config;

/**
 * 
 * @author wng.wl ���������
 * 
 */
public class CollectReport {

	/** ��������, 0 Ϊ��Ե�ʽ, 1 Ϊ�嵥ʽ */
	private int reportType = -1;

	/** ��Ե�ʽ ��������,0Ϊ��ͨ�ۼӱ���,1Ϊ���ⱨ��(����) */
//	private int P2PReportType = -1;

	/** �嵥ʽ ��������,0Ϊ��ͨ,1Ϊ���ⱨ�� (���ж���ӱ�) */
	public  int ListingReportType = -1;

	/** �������ļ�ȡ�� */
	private int startRow = 0;

	public int endRow = 0;

	private char startCol;

	private char endCol;

	private int ListStartCol = 0;

	private int ListEndCol = 0;
	/** ���һ�� */
	public int maxCol = 0 ;

	/** �����бȽϵ��� */
	private char compareCol;

	/** Ҫ�ϼƵĵ�Ԫ���б� */
	private List totalValue = new ArrayList();

	/** ������г������ͨ��Ԫ�� */
	private List commonCell = new ArrayList();

	private String fileName = null;
	
	/** �嵥ʽ�����е����ⱨ�� */
	private String[] listingINSpecial={"S3401","S4400","S3800"};
	/** �Ƿ����嵥ʽ�����е����ⱨ�� */
	private boolean isSpecial=false;
    
	private String fName=null;
	
	/** �Ƿ��бȽ���,�ɴ����ж��ǲ������򱨱� */
	private String IfHaeCompCol=null;
	/**
	 * �嵥ʽ�����������
	 */
	public String[] specialCol = null;
 
	private String versionID=null;
	private String reportID=null;
	/**�����ۼ���������ı��� */
	/**�����ۼ��������򱨱�ı�־�� */
	private int addAndComp = 0; 
	private int PStartRow= 0;
	private char PStartCol;
	private int PEndRow=0;
	private char PEndCol;
	
	public CollectReport(String reportID, String versionID) {
		this.reportID=reportID;
		this.versionID=versionID;
		
		String fileName = Config.REAL_ROOT_PATH + File.separator
				+ "XmlResources" + File.separator + "collectXMLConfig.xml";
		this.fileName = fileName;                   
		
		this.fName=reportID + "_" + versionID;
		for(int i=0;i<listingINSpecial.length;i++){
			if(this.fName.equals(listingINSpecial[i])){
				isSpecial=true;
				break;
			}
		}
	}

	/**
	 * ��ʼ�����в���
	 */
	public boolean initializeAllPar() {
		Element element=XmlParser.getElement(this.fileName,versionID,reportID);
		if(element==null ) return false;
		this.reportType = Integer.parseInt(element.elementText("reportType"));
		if (this.reportType == 0) {
			this.startRow = Integer.parseInt(element.elementText("startRow"));
			this.endRow = Integer.parseInt(element.elementText("endRow"));
			this.startCol = (element.elementText("startCol")).charAt(0);
			this.endCol = (element.elementText("endCol")).charAt(0);
			this.IfHaeCompCol=element.elementText("compareCol");			
			if(IfHaeCompCol!=null)
			{
				this.compareCol = (element.elementText("compareCol")).charAt(0);
				this.totalValue = this.getParameterList("totalValue", element); // Ҫ�ϼƵĵ�Ԫ���б�
				this.commonCell = this.getParameterList("commonCell", element); // ������г������ͨ��Ԫ��
				this.addAndComp = Integer.parseInt(element.elementText("addAndComp"));
				
				if(this.addAndComp==1){
					this.PStartRow= Integer.parseInt(element.elementText("PStartRow"));
					this.PStartCol= (element.elementText("PStartCol")).charAt(0);
					this.PEndRow=Integer.parseInt(element.elementText("PEndRow"));
					this.PEndCol=(element.elementText("PEndCol")).charAt(0);
				}
			}
		} else if (this.reportType == 1) {
			/** �嵥ʽ */

			this.ListStartCol = Integer.parseInt(element
					.elementText("ListStartCol"));
			this.ListEndCol = Integer.parseInt(element.elementText("ListEndCol"));
			this.maxCol = Integer.parseInt(element.elementText("maxCol"));
			this.ListingReportType = Integer.parseInt(element
					.elementText("ListingReportType"));
			String specCol = element.elementText("specialCol");
			if (specCol != null && !specCol.equals("")) {
				this.specialCol = specCol.split(",");
			}
		}
		return true;
	}

	/**
	 * ȡ�������ļ��Ĳ���,���뵽List��
	 */
	private List getParameterList(String par, Element root) {
		String tempValue = root.elementText(par);
		List list =  new ArrayList();
		if(tempValue!=null){
			String cellList[] = tempValue.split(",");
			for(int i=0;i<cellList.length;i++){
				list.add(cellList[i]);
			}
		}
		return list;
	}

	/**
	 * ��ʼ��������
	 */
	public Document startCollectReport(ArrayList arrayList) {
		Document doc = null;
		try {
			boolean bool=this.initializeAllPar();
			if(!bool){
				// System.out.println("����:"+this.fName+"��ʼ����������");
			}
		} catch (Exception e) {
			// System.out.println("����:"+this.fName+"��ʼ������ʧ��!");
			e.printStackTrace();
		}
		/**
		 * ����XML�Ĳ����ж��ǵ�Ե�ʽ�������嵥ʽ�ı���
		 */
		if (this.reportType == 0) {
			/** �����Ƿ��бȽ��еĲ����ж����ۼӱ���������ı��� */
			if (this.IfHaeCompCol == null) {
				try {
					CommonCollect commonCollect = new CommonCollect(startRow,
							endRow, startCol, endCol);
					doc = commonCollect.start(arrayList);

				} catch (Exception e) {
					// System.out.println("����:"+this.fName+"���ܵ�Ե�ʽ�ۼӱ������");
					e.printStackTrace();
				}
			} else if (this.IfHaeCompCol != null) {
				try {
					/** �鿴�����ǲ��Ǽ����ۼ��������� */
						if(this.addAndComp==1){
							CommonCollect commonCollect = new CommonCollect(startRow,
									endRow, startCol, endCol);
							doc = commonCollect.start(arrayList);
							SpecialCollect specialCollect1 = new SpecialCollect(
									PStartRow, PEndRow, PStartCol, PEndCol, compareCol,
									totalValue, commonCell,this.addAndComp);
							doc = specialCollect1.start(arrayList,doc);
						}else{					
							SpecialCollect specialCollect = new SpecialCollect(
									startRow, endRow, startCol, endCol, compareCol,
									totalValue, commonCell);
							doc = specialCollect.start(arrayList);
						}
				} catch (Exception e) {
					// System.out.println("����:"+this.fName+"���ܵ�Ե�ʽ���򱨱����");
					e.printStackTrace();
				}
			} else {
				 System.out.println("����:"+this.fName+"~~~~~~IfHaeCompCol=" + this.IfHaeCompCol
						+ " �����ж����ۼӱ���������Ĳ�����ȡ����!");
			}
		} else if (this.reportType == 1) {	
		
			if (this.ListingReportType == 0) {  /* �嵥ʽ�����е���ͨ����*/
				try {
					ListingCollect listCollect = new ListingCollect(
							this.ListStartCol, this.ListEndCol, this.specialCol);
					doc = listCollect.start(arrayList);
				} catch (Exception e) { 
					// System.out.println("����:"+this.fName+"�����嵥ʽ��ͨ�������");
					e.printStackTrace();
				}
			} else if (this.ListingReportType == 1) { /* �Ƿ����嵥ʽ�����е����ⱨ��  */
				
				if(isSpecial){
					if("S3401".equals(this.fileName)){
						S3401Collect s3401Collect = new S3401Collect(this.ListStartCol, this.ListEndCol, this.specialCol);
						s3401Collect.start(arrayList);
					}
					else if("S4800".equals(this.fileName)){
						
					}
					else{
						
					}
				}else{
					ListingSpecialCollect listSpecialCollect = new ListingSpecialCollect(
							this.ListStartCol, this.ListEndCol);
					doc = listSpecialCollect.start(arrayList);
				}
			} else {
				 System.out.println("����:"+this.fName+"~~~~~~ListingReportType="
						+ this.ListingReportType + " ������ȡ����");
			}
		} else {
			 System.out.println("����:"+this.fName+"~~~~~~reportType=" + this.reportType
					+ " ������ȡ����");
		}
		return doc;

	}

}