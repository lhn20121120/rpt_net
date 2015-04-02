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
 * @author wng.wl 报表汇总类
 * 
 */
public class CollectReport {

	/** 报表类型, 0 为点对点式, 1 为清单式 */
	private int reportType = -1;

	/** 点对点式 报表类型,0为普通累加报表,1为特殊报表(排序) */
//	private int P2PReportType = -1;

	/** 清单式 报表类型,0为普通,1为特殊报表 (含有多个子表) */
	public  int ListingReportType = -1;

	/** 从配置文件取得 */
	private int startRow = 0;

	public int endRow = 0;

	private char startCol;

	private char endCol;

	private int ListStartCol = 0;

	private int ListEndCol = 0;
	/** 最后一列 */
	public int maxCol = 0 ;

	/** 排序中比较的列 */
	private char compareCol;

	/** 要合计的单元格列表 */
	private List totalValue = new ArrayList();

	/** 排序的行除外的普通单元格 */
	private List commonCell = new ArrayList();

	private String fileName = null;
	
	/** 清单式报表中的特殊报表 */
	private String[] listingINSpecial={"S3401","S4400","S3800"};
	/** 是否是清单式报表中的特殊报表 */
	private boolean isSpecial=false;
    
	private String fName=null;
	
	/** 是否有比较列,由此来判断是不是排序报表 */
	private String IfHaeCompCol=null;
	/**
	 * 清单式报表的特殊列
	 */
	public String[] specialCol = null;
 
	private String versionID=null;
	private String reportID=null;
	/**既有累加又有排序的报表 */
	/**既有累加又有排序报表的标志量 */
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
	 * 初始化所有参数
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
				this.totalValue = this.getParameterList("totalValue", element); // 要合计的单元格列表
				this.commonCell = this.getParameterList("commonCell", element); // 排序的行除外的普通单元格
				this.addAndComp = Integer.parseInt(element.elementText("addAndComp"));
				
				if(this.addAndComp==1){
					this.PStartRow= Integer.parseInt(element.elementText("PStartRow"));
					this.PStartCol= (element.elementText("PStartCol")).charAt(0);
					this.PEndRow=Integer.parseInt(element.elementText("PEndRow"));
					this.PEndCol=(element.elementText("PEndCol")).charAt(0);
				}
			}
		} else if (this.reportType == 1) {
			/** 清单式 */

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
	 * 取出配置文件的参数,加入到List中
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
	 * 开始汇总数据
	 */
	public Document startCollectReport(ArrayList arrayList) {
		Document doc = null;
		try {
			boolean bool=this.initializeAllPar();
			if(!bool){
				// System.out.println("报表:"+this.fName+"初始化参数错误");
			}
		} catch (Exception e) {
			// System.out.println("报表:"+this.fName+"初始化参数失败!");
			e.printStackTrace();
		}
		/**
		 * 根据XML的参数判断是点对点式报表还是清单式的报表
		 */
		if (this.reportType == 0) {
			/** 根据是否有比较列的参数判断是累加报表还是排序的报表 */
			if (this.IfHaeCompCol == null) {
				try {
					CommonCollect commonCollect = new CommonCollect(startRow,
							endRow, startCol, endCol);
					doc = commonCollect.start(arrayList);

				} catch (Exception e) {
					// System.out.println("报表:"+this.fName+"汇总点对点式累加报表错误");
					e.printStackTrace();
				}
			} else if (this.IfHaeCompCol != null) {
				try {
					/** 查看报表是不是既有累加又有排序 */
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
					// System.out.println("报表:"+this.fName+"汇总点对点式排序报表错误");
					e.printStackTrace();
				}
			} else {
				 System.out.println("报表:"+this.fName+"~~~~~~IfHaeCompCol=" + this.IfHaeCompCol
						+ " 参数判断是累加报表还是排序的参数获取错误!");
			}
		} else if (this.reportType == 1) {	
		
			if (this.ListingReportType == 0) {  /* 清单式报表中的普通报表*/
				try {
					ListingCollect listCollect = new ListingCollect(
							this.ListStartCol, this.ListEndCol, this.specialCol);
					doc = listCollect.start(arrayList);
				} catch (Exception e) { 
					// System.out.println("报表:"+this.fName+"汇总清单式普通报表错误");
					e.printStackTrace();
				}
			} else if (this.ListingReportType == 1) { /* 是否是清单式报表中的特殊报表  */
				
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
				 System.out.println("报表:"+this.fName+"~~~~~~ListingReportType="
						+ this.ListingReportType + " 参数获取错误");
			}
		} else {
			 System.out.println("报表:"+this.fName+"~~~~~~reportType=" + this.reportType
					+ " 参数获取错误");
		}
		return doc;

	}

}