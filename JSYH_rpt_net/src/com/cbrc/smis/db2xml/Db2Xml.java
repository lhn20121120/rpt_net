package com.cbrc.smis.db2xml;

import java.io.File;
import java.io.FileWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.cbrc.org.adapter.StrutsMOrgDelegate;
import com.cbrc.smis.adapter.StrutsCellPercentDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.hibernate.ReportIn;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechLog;
import com.fitech.net.adapter.StrutsOrgNetDelegate;
/**
 * 
 * @author 姚捷
 * 
 * 把数据库表转成xml文件
 *
 */
public class Db2Xml {
	/**
	 * 单元格的默认值
	 */
    public static final String DEFAULTNUMBERVALUE="0";

    private static FitechException log = new FitechException(Db2Xml.class);
    
    /**为Db2Xml类提供服务的类*/
    private Db2XmlUtil db2XmlHandler = new Db2XmlUtil();
    
    /**文件存放路径*/
    private String fileSavePath = "";
    
    /**日期型转字符串*/
    private SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    
    /**所有需要转xml的报表数量*/
    private int reportTotal = 0;
    /**生成失败的报表数量*/
    private int failReportNum = 0;
    /**生成成功的报表数量*/
    private int successReportNum = 0;
    
    /**特殊报表(需要生成文件时变成T2的报表)*/
    /*private String[] specialReport ={"G1301","G1302","G1303","G1304","G1402","G2400","S3403","G1401","S4400","G2300"};*/
    private String[] specialReport ={"G1301","G1302","G1303","G1304","G1402","G2400","S3403","G1401","G2300"}; 
    
    /**特殊报表 S2300*/
    private String S2300 = "S2300";
    /**特殊报表G1500*/
    private String G1500 = "G1500";
    
    private final static SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    
    public Db2Xml(){}
    
    /**
     * 把数据表转成xml文件
     * @return int  1 表示成功
     *              -1 表示失败
     *              0 表示没有需要生成xml的记录
     */
    public int DataBase2Xml() throws Exception
    {
        int result = Config.DataToXML_FAILED;
        
        /**取得需要转成xml文件的报表集合*/
        List reports  = db2XmlHandler.get_NeedToXmlReps();
        
        if(reports!=null && reports.size()!=0)
        {
            /**创建目录，目录名就是当时的时间*/
            Date today = new Date();
            SimpleDateFormat FORMAT = new SimpleDateFormat("yyyyMMddhhmmss");
            String directoryName =  FORMAT.format(today);
            
            /**创建目录*/
            fileSavePath = Config.XMLData_PATH + Config.FILESEPARATOR + directoryName;
            File directory = new File(fileSavePath);
            
            if(directory.mkdirs()==false)
            {
                FitechLog.writeLog(Config.LOG_SYSTEM_CREATESTORAGEXML,Config.SYSTEM_OPERATOR,"创建保存xml文件的目录失败！");
                return Config.DataToXML_FAILED;
            }
            /**取得报表总数*/
            reportTotal = reports.size();           
            
            /**循环把每个报表转成xml文件*/
            ReportIn reportIn = null;
            String versionId ="";
            String reportName  ="";
            String orgName  ="";
            String reportDate ="";
            for(int i=0;i<reports.size();i++)
            {
                
                try 
                {
                	boolean bool = false;
                    reportIn = (ReportIn)reports.get(i);
                    /**版本号*/
                    versionId = reportIn.getMChildReport().getComp_id().getVersionId();
                    /**子报表名*/
                    reportName =  reportIn.getMChildReport().getReportName();
                    /**机构名称*/
                    com.fitech.net.form.OrgNetForm orgNetForm=StrutsOrgNetDelegate.selectOne(reportIn.getOrgId(),bool);
                    orgName =orgNetForm!=null?orgNetForm.getOrg_name():"";
                    /**报送时间*/
                    Date repDate = reportIn.getReportDate();
                    
                    if(repDate!=null) reportDate = FORMAT.format(repDate);      
                    
                    reportsHandler(reportIn);
                } catch (Exception e){
                    log.printStackTrace(e);
                    FitechLog.writeLog(Config.LOG_SYSTEM_CREATESTORAGEXML,Config.SYSTEM_OPERATOR,"创建xml文件失败！（报表名："+reportName+",版本号："+versionId+",报送机构："+orgName+",报送时间："+reportDate+"）");
                    continue;
                }
            }    
            FitechLog.writeLog(Config.LOG_SYSTEM_CREATESTORAGEXML,Config.SYSTEM_OPERATOR,"共生成" + 
            		reportTotal + "个仓库文件," + 
            		successReportNum + "个成功," + 
            		failReportNum+"个失败！");
            result = Config.DataToXML_SUCCESS;
        }else{			//没有需要生成xml文件的记录
            result = Config.NO_DataToXML;
        }
        
        return result;
    }
   
    /**
     * 处理报表，将其转成xml文件
     * 
     * @param report ReportInForm 包含该报表的基本属性
     * @return void
     */
    public void reportsHandler(ReportIn reportIn) throws Exception
    {
        if(reportIn!=null)
        {            
            /**子报表id*/
            String childRepId = reportIn.getMChildReport().getComp_id().getChildRepId();
            /**版本号*/
            String versionId = reportIn.getMChildReport().getComp_id().getVersionId();
            /**子报表名*/
            String reportName =  "";
            /**机构名称*/
            String orgName ="";
            /**报送时间*/
            String reportDate = ""; 
                       
            /**特殊报表处理*/
            /*for(int i=0; i<specialReport.length; i++)
            {
                if(childRepId.equalsIgnoreCase(specialReport[i]))
                {
                    SpecialReport_To_Xml(reportIn);
                    return;
                }
            }*/
            
            /**处理G1500*/
            /*if(childRepId.equalsIgnoreCase(G1500))
            {
                G1500_To_Xml(reportIn);
                return;
            }*/
            
            /**处理S2300*/
            /*if(childRepId.equalsIgnoreCase(S2300))
            {
                S2300_To_Xml(reportIn);
                return;
            }*/
            
            /**根据子报表id和版本号查找出该报表的类型（点对点报表 or 清单报表）*/
            int reportType = db2XmlHandler.get_PeportStyle(childRepId,versionId);
            switch(reportType)
            {
                /** 处理点对点报表 */
                case 1:  
                {
                    P2PReport_To_Xml(reportIn);                    
                    break;
                }
                /**处理清单式报表*/
                case 2: 
                {
                    BillReport_To_Xml(reportIn);
                    break;
                }
                default:
                	reportName =  reportIn.getMChildReport().getReportName();
                	com.fitech.net.form.OrgNetForm orgNetForm=StrutsMOrgDelegate.selectOne(reportIn.getOrgId());
                	if(orgNetForm!=null) orgName =orgNetForm.getOrg_name();
	                Date repDate = reportIn.getReportDate();
	                if(repDate!=null) reportDate = FORMAT.format(repDate);  
                    FitechLog.writeLog(Config.LOG_SYSTEM_CREATESTORAGEXML,Config.SYSTEM_OPERATOR,"未知报表类型！（报表名："+reportName+",版本号："+versionId+",报送机构："+orgName+",报送时间："+reportDate+"）");
                	break;
            }
        }
    }
    
    /**
     * 处理点对点式报表，生成xml文件
     * @param reportInId Integer 实际自报表id
     */
    private void P2PReport_To_Xml(ReportIn report) throws Exception
    {
        if(report!=null)
        {
            Integer reportInId = report.getRepInId();
            
            /**子报表id*/ 
            String childRepId = report.getMChildReport().getComp_id().getChildRepId();
            
            /**版本号id*/
            String versionId = report.getMChildReport().getComp_id().getVersionId();
            
            /**创建文档对象*/
            Document document = DocumentHelper.createDocument();
            
            /**根元素是DataTrans*/
            Element root_Element = document.addElement("DataTrans");
            
            /**属性RepType T1表示是点对点式报表*/
            root_Element.addAttribute("RepType","T1");
            
            /**为根元素添加Report元素 包含该报表的一些基本属性*/
            this.addReportElement(root_Element,report);
            
            /**为根元素添加RepDatas 包含该报表的数据*/
            Element repDatas_Element = root_Element.addElement("RepDatas");
            
            /**取出该点对点报表的数据*/
            List reportData  = db2XmlHandler.get_P2PReport_Data(reportInId);
            
            if(reportData!=null && reportData.size()!=0)
            {
            	P2P_Report_Data p2pReportData=null;
            	String rowNum="",colNum="";
            	/**循环为RepDatas元素添加T1Data元素,每个T1Data元素表示一个单元格记录*/
                for(int i=0;i<reportData.size();i++)
                {   
                	p2pReportData=(P2P_Report_Data)reportData.get(i);
                                                           
                    //单元格行号 
                    rowNum = p2pReportData.getCellRow();
                                       
                    //单元格列号                    
                    colNum = p2pReportData.getCellCol();
                    
                    //单元格值
                    String value = p2pReportData.getValue();
                    //单元格名称
                    String cellName = p2pReportData.getCellName();
                    
                    if(value!=null)
                    {
                        /**判断该列是否是百分数列，如果是则除100*/
                        if(db2XmlHandler.isPercentCell(childRepId,versionId,cellName))
                            value = db2XmlHandler.toPercent(value);  
                        else
                        	value=value.trim().equals("")?DEFAULTNUMBERVALUE:value.trim();
                    }else{
                        value=DEFAULTNUMBERVALUE;
                	}
                    
                    createT1Element(repDatas_Element,rowNum,colNum,value);
                    /*if(!value.equals("")){
                    	Element t1Data_Element = repDatas_Element.addElement("T1Data");
                    	Element cellRow_Element = t1Data_Element.addElement("CellRow");
                    	cellRow_Element.setText(rowNum.trim());
                    	Element cellCol_Element = t1Data_Element.addElement("CellCol");
                    	cellCol_Element.setText(colNum.trim());
                    	Element value_Element = t1Data_Element.addElement("Value");
                    	value_Element.setText(value.trim());
                    }*/
                    /*if(((P2P_Report_Data)reportData.get(i)).getValue()!=null)
                        value_Element.setText(((P2P_Report_Data)reportData.get(i)).getValue().trim());*/
                }             
            }
            /**写xml文件*/  
            this.writeXml(document,report);
        }
    }
    
    /**、
     * 处理清单式报表，生成xml文件
     * @param reportInId Integer 实际子报表id
     * @param  childRepId String 子报表id
     * @param versionId String 版本号
     */
    public  void BillReport_To_Xml(ReportIn reportIn) throws Exception
    {
    	Db2XmlUtil db2XmlUtil = null;
        if(reportIn!=null)
        {
            Integer reportInId =reportIn.getRepInId();
           
            /**子报表id*/
            String childRepId = reportIn.getMChildReport().getComp_id().getChildRepId();
            /**版本号*/
            String versionId = reportIn.getMChildReport().getComp_id().getVersionId();
            /**子报表名*/
            String reportName =  reportIn.getMChildReport().getReportName();
            /**机构名称*/
            String orgName =StrutsMOrgDelegate.selectOne(reportIn.getOrgId()).getOrg_name();
            /**报送时间*/
            Date repDate = reportIn.getReportDate();
            String reportDate = "";
            if(repDate!=null) reportDate = FORMAT.format(repDate);      
            
            /**创建文档对象*/
            Document document = DocumentHelper.createDocument();
            
            /**根元素是DataTrans*/
            Element root_Element = document.addElement("DataTrans");
            
            /**属性RepType T2表示是清单式报表*/
            root_Element.addAttribute("RepType","T2");           
           
            
            /**为根元素添加Report元素 包含该报表的一些基本属性*/
            this.addReportElement(root_Element,reportIn);
            
            /**为根元素添加RepDatas 包含该报表的数据*/
            Element repDatas_Element = root_Element.addElement("RepDatas");
            
            ResultSet rs =null;
            ResultSetMetaData rsMetaData =null;
            
            try 
            {
            	//百分数转换成小数的列名列表
            	List percentCols=StrutsCellPercentDelegate.getPercentCellOrCol(childRepId,versionId);
            	
            	db2XmlUtil = new Db2XmlUtil();
                /**获得清单式报表的数据*/
                rs = db2XmlUtil.get_BillReport_Data(reportInId,childRepId,versionId);
                
                /*if (rs == null)  // System.out.println("----------------rs = null");*/
                if(rs!=null)
                {
                    rsMetaData = rs.getMetaData();
                    /**列的总数*/
                    int _colNum = rsMetaData.getColumnCount();
                    int colNum=0;
                    /**默认读数据时从第3列读取*/
                    int startPos = 3;
                    /**循环为RepDatas元素添加T2Data元素,每个T1Data元素表示一个单元格记录*/
                    String row="",region="",type="",value="",cellValue="",colName="";
                    int rowIndex=0;  //列序号
                    String e7="";    //客户类型
                    while(rs.next())
                    {
                        //行号
                    	row=rs.getString("Col1");
                        //数据区块类型            
                        region="";
                        
                        try
                        {
                            /**寻找该表中是否存在Type字段*/
                            type = rs.getString("Type");
                            region=type;       //region元素的值是类型的值
                            startPos = 4;      //当数据表中有"Type"字段时，从第四个字段取值
                        }catch(SQLException e){
                            region="1";        //如果不存在Type字段,region元素值为1
                            startPos = 3;	   //当数据表中没有"Type"字段时，从第三个字段取值
                        }
                        
                        if(childRepId.equals("S3600") && type.trim().equals("3")){
                        	colNum=_colNum-1;
                        }else{
                        	colNum=_colNum;
                        }
                                                
                        value = "";
                        /** 把值连接成字符串，之间用"," 号隔开 */
                        for(int i=startPos ;i<=colNum;i++)
                        {
                            /**值*/   
                        	if(childRepId.equals("S3401")){
                        		if(i==5){
                        			if(rowIndex%3==0){
                        				e7=rs.getString(i);
                        			}
                        			cellValue=e7;
                        		}else{
                        			cellValue = rs.getString(i);
                        		}	
                        	}else{
                        		cellValue = rs.getString(i);
                        	}
                            /**获得列号*/
                            colName = rsMetaData.getColumnName(i);
                            
                            if(cellValue==null){
                                 cellValue = DEFAULTNUMBERVALUE;
                            }else{
                                /**判断该列是否是百分数列，如果是则除100*/
                                if(percentCols!=null && percentCols.size()>0 && percentCols.contains(colName.toUpperCase()))
                                { 
                                    cellValue = db2XmlHandler.toPercent(cellValue);
                                }
                                cellValue = cellValue.trim();                                 
                            }
                            if(i==colNum){
                                value += cellValue;
                            }else{
                                value += cellValue+Config.SPLIT_SYMBOL_COMMA;
                            }
                        }
                        
                       
                        /** 为T2Data元素添加Value 该元素的值是 一行记录的值*/ 
                        //// System.out.println("row=" + row +"\tregion=" + region + "\tvalue=" + value);
                        createT2QDElement(repDatas_Element,row,region,value);
                        
                        /*if(!value.equals("")){
                        	Element t2Data_Element = repDatas_Element.addElement("T2Data");
                        	Element rowId_Element = t2Data_Element.addElement("RowID");
                            rowId_Element.setText(row);
                            Element region_Element = t2Data_Element.addElement("region");
                            region_Element.setText(region);
                        	Element value_Element = t2Data_Element.addElement("Value");
                        	value_Element.setText(value.toString());
                        }*/
                        rowIndex++;
                    }
                }
                this.writeXml(document,reportIn);
            } catch (Exception e) {
                FitechLog.writeLog(Config.LOG_SYSTEM_CREATESTORAGEXML,Config.SYSTEM_OPERATOR,"创建xml文件失败！（报表名："+reportName+",版本号："+versionId+",报送机构："+orgName+",报送时间："+reportDate+"）");
                log.printStackTrace(e);
            }finally{
            	if(db2XmlUtil!=null) db2XmlUtil.closeConnection();
            }
        }
    }
    
    /**
     * 处理特殊报表，生成xml文件(改成t2形式的)
     * @param reportInId Integer 实际自报表id
     */
    private void SpecialReport_To_Xml(ReportIn report) throws Exception
    {
        if(report!=null)
        {
            Integer reportInId = report.getRepInId();
            
            /**子报表id*/
            String childRepId = report.getMChildReport().getComp_id().getChildRepId();

            /**版本号*/
            String versionId = report.getMChildReport().getComp_id().getVersionId(); 
            /**创建文档对象*/
            Document document = DocumentHelper.createDocument();
            
            /**根元素是DataTrans*/
            Element root_Element = document.addElement("DataTrans");
            
            /**属性RepType T2表示是清单式报表*/
            root_Element.addAttribute("RepType","T2");
            
            /**为根元素添加Report元素 包含该报表的一些基本属性*/
            this.addReportElement(root_Element,report);
            
            /**为根元素添加RepDatas 包含该报表的数据*/
            Element repDatas_Element = root_Element.addElement("RepDatas");
            
            /**取出该特殊报表的数据*/
            List reportData  = db2XmlHandler.get_P2PReport_Data(reportInId);
            List colList=db2XmlHandler.getP2PReportCols(childRepId,versionId,Db2XmlUtil.getExceptCells(childRepId,versionId));
            
            if((reportData!=null && reportData.size()>0) && (colList!=null && colList.size()>0))
            {  
                /**查询该特殊报表中有多少行记录*/
                List rowIds = db2XmlHandler.get_specialReportRowList(reportData);
                int rowIndex=1; //清单接口文件中，数据的行号
                ArrayList colsIndex=null;
                /**按行为单位,把数据用","号隔开,并且加入xml文件*/
                P2P_Report_Data p2pReportData=null;
                String e7="";	//客户类型
                for(int i=0;i<rowIds.size();i++)
                {
                    /**循环为RepDatas元素添加T2Data元素  每个T2Data元素表示一行记录*/
                    
                    /**对每一行的列和值进行排序*/
                    SortedMap sortMap =  new TreeMap();
                    
                    for(int j=0;j<reportData.size();j++)
                    {
                    	p2pReportData=(P2P_Report_Data)reportData.get(j);
                        /**利用treeMap的特性，对该行的列进行排序*/
                        String cellRow = p2pReportData.getCellRow();
                        String cellCol = p2pReportData.getCellCol();
                        String cellName = p2pReportData.getCellName();
                        String cellValue = p2pReportData.getValue();
                       
                        if(cellRow.equals(((Integer)rowIds.get(i)).toString()))
                        {    
                            /**判断该列是否是百分数列，如果是则除100*/
                            if(db2XmlHandler.isPercentCell(childRepId,versionId,cellName))
                            {
                                cellValue = db2XmlHandler.toPercent(cellValue);
                            }
                            sortMap.put(cellCol,cellValue);     
                            if((childRepId.equals("G2300") || childRepId.equals("G2400")) && rowIndex==1){
                            	if(colsIndex==null) colsIndex=new ArrayList();
                            	colsIndex.add(cellCol);
                            }
                        }
                    }

                    /**循环取得该行的所有值,并用","号隔开*/
                    String value = "",cellValue="";
                    
                    //if(((childRepId.equals("G2300") || childRepId.equals("G2400")) && versionId.equals("0510")) && (i==rowIds.size()-1)){
                    if((childRepId.equals("G2300") || childRepId.equals("G2400")) && (i==rowIds.size()-1)){
                    	//if(childRepId.equals("G2300") || childRepId.equals("G2400")){
                    		if(colsIndex!=null && colsIndex.size()>0){
                    			cellValue="";
                            	for(int k=0;k<colsIndex.size();k++){
                            		cellValue=(String)sortMap.get(colsIndex.get(k));
                            		value+=(value.equals("")?"":Config.SPLIT_SYMBOL_COMMA) + 
                            			(sortMap.containsKey(colsIndex.get(k))==true?(cellValue==null?DEFAULTNUMBERVALUE:cellValue):" ");
                            	}
                    		}
                    	/*
                    	}else{
		                    if(sortMap.size()>0){
		                        //取map的键值对
		                        Set set = sortMap.entrySet();
		                        Iterator it = set.iterator();
		                        int j=1;
		                        Map.Entry entry=null;
		                        while(it.hasNext())
		                        {
		                            entry = (Map.Entry)it.next();
		                            String cellValue ="";
		                            if(entry.getValue() != null) cellValue = ((String) entry.getValue()).trim();
		                            if(cellValue.equals("")) cellValue = DEFAULTNUMBERVALUE;
		                      
		                            //最后一个数据不加逗号
		                            if(j==sortMap.size())
		                                value += cellValue;
		                            else
		                                value += cellValue+Config.SPLIT_SYMBOL_COMMA;
		                            j++;
		                        }
		                    }
                    	}
                    	*/
                    }else{
	                    for(int j=0;j<colList.size();j++){
	          	            cellValue=sortMap.containsKey(colList.get(j))?(sortMap.get(colList.get(j))!=null?(String)sortMap.get(colList.get(j)):""):"";
	                    	if(childRepId.equals("S3403")){
	                    		if(j==2){
	                    			if(i%3==0){
	                    				e7=cellValue;
	                    			}
	                    			value+=e7 + ",";
	                    		}else{
	                    			value+=cellValue + ",";
	                    		}
	                    	}else{
	                    		value+=cellValue + ",";
	                    	}
	                    	if(childRepId.equals("S3403") && j==2){
	                    		value+= (i<rowIds.size()-1?String.valueOf(i%3+1):"") + ",";
	                    	}
	                    }
	                    if(!value.equals("")) value=value.substring(0,value.length()-1);
                    }
                    /** 为T2Data元素添加Value 该元素的值是 一行记录的值*/
                    if(childRepId.substring(0,4).equals("G130") || childRepId.equals("G2300") || childRepId.equals("G2400"))
                    	value=(rowIndex++) + "," + value; 
                    createT2QDElement(repDatas_Element,String.valueOf(i+1),"1",value);
                    
                    /*if(!value.equals("")){
                    	Element t2Data_Element = repDatas_Element.addElement("T2Data");                        
                        //为T2Data元素添加RowID 该元素的值是 行号 
                        Element rowId_Element = t2Data_Element.addElement("RowID");
                        rowId_Element.setText(String.valueOf(i+1));                        
                        //为T2Data元素添加region
                        Element region_Element = t2Data_Element.addElement("region");
                        region_Element.setText("1");
                    	Element value_Element = t2Data_Element.addElement("Value");
                    	value_Element.setText(value);
                    }*/
                }           
            }
            /**写xml文件*/  
            this.writeXml(document,report);
        }
    }
 
    /***
     * 处理G1500
     * @param report
     */
    private void G1500_To_Xml(ReportIn report) throws Exception
    {
        if(report!=null)
        {
            Integer reportInId = report.getRepInId();
            /**子报表id*/
            String childRepId = report.getMChildReport().getComp_id().getChildRepId();
            
            /**版本号*/
            String versionId = report.getMChildReport().getComp_id().getVersionId();        
            /**创建文档对象*/
            Document document = DocumentHelper.createDocument();
            
            /**根元素是DataTrans*/
            Element root_Element = document.addElement("DataTrans");
            
            /**属性RepType T2表示是清单式报表*/
            root_Element.addAttribute("RepType","T2");
            
            /**为根元素添加Report元素 包含该报表的一些基本属性*/
            this.addReportElement(root_Element,report);
            
            /**为根元素添加RepDatas 包含该报表的数据*/
            Element repDatas_Element = root_Element.addElement("RepDatas");
            
            /**取出该特殊报表的数据*/
            List reportData  = db2XmlHandler.get_P2PReport_Data(reportInId);
            
            if(reportData!=null && reportData.size()>0)
            {  
                /**查询该特殊报表中有多少行记录*/
                List rowIds = db2XmlHandler.get_specialReportRowList(reportData);
                
                /**按行为单位,把数据用","号隔开,并且加入xml文件*/
                P2P_Report_Data p2pReportData=null;
                
                for(int i=0;i<rowIds.size();i++)
                {
                    /**循环为RepDatas元素添加T2Data元素  每个T2Data元素表示一行记录*/
                    
                    /**前21行 为清单式xml*/
                    if(i+1<21)
                    {                        
                        /**对每一行的列和值进行排序*/
                        SortedMap sortMap =  new TreeMap();
                        for(int j=0;j<reportData.size();j++)
                        {
                            /**利用treeMap的特性，对该行的列进行排序*/
                        	p2pReportData=(P2P_Report_Data)reportData.get(j);
                            String cellRow = p2pReportData.getCellRow();
                            String cellCol = p2pReportData.getCellCol();
                            String cellName = p2pReportData.getCellName();
                            String cellValue = p2pReportData.getValue();
                           
                            if(cellRow.equals(((Integer)rowIds.get(i)).toString()))
                            {    
                                /**判断该列是否是百分数列，如果是则除100*/
                                if(db2XmlHandler.isPercentCell(childRepId,versionId,cellName))
                                    cellValue = db2XmlHandler.toPercent(cellValue);
                                sortMap.put(cellCol,cellValue);                        
                            }
                        }
                       /* for(int j=0;j<reportData.size();j++)
                        {
                            //利用treeMap的特性，对该行的列进行排序
                            if(((P2P_Report_Data)reportData.get(j)).getCellRow().equals(((Integer)rowIds.get(i)).toString()))
                                sortMap.put(((P2P_Report_Data)reportData.get(j)).getCellCol(),((P2P_Report_Data)reportData.get(j)).getValue());
                        }*/

                        /**循环取得该行的所有值,并用","号隔开*/
                        String value = "";
                        
                        if(sortMap.size()>0)
                        {
                            /**取map的键值对*/
                            Set set = sortMap.entrySet();
                            Iterator it = set.iterator();
                            int j=1;
                            while(it.hasNext())
                            {
                                Map.Entry entry = (Map.Entry)it.next();
                                String cellValue ="";
                                if(entry.getValue() != null)
                                    cellValue = ((String) entry.getValue()).trim();
                                else
                                    cellValue = DEFAULTNUMBERVALUE;
                                
                                /**最后一个数据不加逗号*/
                                if(j==sortMap.size())
                                    value += cellValue.trim();
                                else
                                    value += cellValue+Config.SPLIT_SYMBOL_COMMA;
                                j++;
                            }
                        }
                        if(!value.equals("")) value=(i+1) + "," + value;
                        /** 为T2Data元素添加Value 该元素的值是 一行记录的值*/
                        createT2QDElement(repDatas_Element,String.valueOf(i+1),"1",value);
                        
                        /*if(!value.equals("")){
                        	Element t2Data_Element = repDatas_Element.addElement("T2Data");
                            //为T2Data元素添加RowID 该元素的值是 行号 
                            Element rowId_Element = t2Data_Element.addElement("RowID");
                            rowId_Element.setText(String.valueOf(i+1));                            
                            //为T2Data元素添加region
                            Element region_Element = t2Data_Element.addElement("region");
                            region_Element.setText("1");
                        	Element value_Element = t2Data_Element.addElement("Value");
                        	value_Element.setText(value);
                        }*/
                    }
                    else/**22行开始 改为点对点式*/
                    {                        
                        for(int k=0;k<reportData.size();k++)
                        {   
                        	p2pReportData=(P2P_Report_Data)reportData.get(k);
                            if(p2pReportData.getCellRow().equals(((Integer)rowIds.get(i)).toString()))
                            {
                                /**循环为RepDatas元素添加T1Data元素  每个T1Data元素表示一个单元格记录*/
                                                              
                                /**为T1Data元素添加CellRow 该元素的值是 单元格行号*/                                
                                String cellRow  = p2pReportData.getCellRow();
                               
                                /**为T1Data元素添加CellCol 该元素的值是 单元格列号*/                                
                                String cellCol = p2pReportData.getCellCol();
                            
                                /** 为T1Data元素添加CellID 该元素的值是 单元格值*/                                 
                                String cellValue = p2pReportData.getValue();
                                String cellName = p2pReportData.getCellName();
                                
                                if(cellValue!=null){
                                    /**判断该列是否是百分数列，如果是则除100*/
                                    if(db2XmlHandler.isPercentCell(childRepId,versionId,cellName))
                                        cellValue = db2XmlHandler.toPercent(cellValue);
                                    cellValue=cellValue.trim().equals("")?DEFAULTNUMBERVALUE:cellValue.trim();                                    
                                }else{
                                    cellValue = DEFAULTNUMBERVALUE;
                                }
                                
                                createT2DDElement(repDatas_Element,cellRow,cellCol,cellValue);
                                
                                /*if(!cellValue.equals("")){
                                	Element t1Data_Element = repDatas_Element.addElement("T2Data");
                                	Element cellRow_Element = t1Data_Element.addElement("CellRow");
                                	cellRow_Element.setText(cellRow.trim());
                                	Element cellCol_Element = t1Data_Element.addElement("CellCol");
                                	cellCol_Element.setText(cellCol.trim());
                                	Element value_Element = t1Data_Element.addElement("Value");
                                	value_Element.setText(cellValue);
                                }*/
                            }             
                        }
                    }
                }           
            }
            /**写xml文件*/  
            this.writeXml(document,report);
        }
    }
   
    /***
     * 处理S2300
     */
    private void S2300_To_Xml(ReportIn report) throws Exception
    {
        if(report!=null)
        {
            Integer reportInId = report.getRepInId();
            /**子报表id*/
            String childRepId = report.getMChildReport().getComp_id().getChildRepId();
            
            /**版本号*/
            String versionId = report.getMChildReport().getComp_id().getVersionId();              
            /**创建文档对象*/
            Document document = DocumentHelper.createDocument();
            
            /**根元素是DataTrans*/
            Element root_Element = document.addElement("DataTrans");
            
            /**属性RepType T2表示是清单式报表*/
            root_Element.addAttribute("RepType","T2");
            
            /**为根元素添加Report元素 包含该报表的一些基本属性*/
            this.addReportElement(root_Element,report);
            
            /**为根元素添加RepDatas 包含该报表的数据*/
            Element repDatas_Element = root_Element.addElement("RepDatas");
            
            /**取出该特殊报表的数据*/
            List reportData  = db2XmlHandler.get_P2PReport_Data(reportInId);
            
            if(reportData!=null && reportData.size()!=0)
            {  
                /**查询该特殊报表中有多少行记录*/
                List rowIds = db2XmlHandler.get_specialReportRowList(reportData);
                
                int rowIndex=1; //清单接口文件中，数据的行号
                ArrayList colsIndex=null;
                /**按行为单位,把数据用","号隔开,并且加入xml文件*/
                for(int i=0;i<rowIds.size();i++)
                {
                    /**从第十六行开始，后11行为清单式xml*/
                    if(i+1>=16)
                    {                       
                        /**对每一行的列和值进行排序*/
                        SortedMap sortMap =  new TreeMap();
                        for(int j=0;j<reportData.size();j++)
                        {
                            /**利用treeMap的特性，对该行的列进行排序*/
                            String cellRow = ((P2P_Report_Data)reportData.get(j)).getCellRow();
                            String cellCol = ((P2P_Report_Data)reportData.get(j)).getCellCol();
                            String cellName = ((P2P_Report_Data)reportData.get(j)).getCellName();
                            String cellValue = ((P2P_Report_Data)reportData.get(j)).getValue();
                           
                            if(cellRow.equals(((Integer)rowIds.get(i)).toString()))
                            {    
                                /**判断该列是否是百分数列，如果是则除100*/
                                if(db2XmlHandler.isPercentCell(childRepId,versionId,cellName))
                                    cellValue = db2XmlHandler.toPercent(cellValue);
                                sortMap.put(cellCol,cellValue);  
                                
                                if(rowIndex==1){
                                	if(colsIndex==null) colsIndex=new ArrayList();
                                	//// System.out.println("rowIndex:" + rowIndex + "\tcellCol:" + cellCol + "\tcellValue:" + cellValue);
                                	colsIndex.add(cellCol);
                                }
                            }
                        }
                       /* for(int j=0;j<reportData.size();j++)
                        {
                            //利用treeMap的特性，对该行的列进行排序
                            if(((P2P_Report_Data)reportData.get(j)).getCellRow().equals(((Integer)rowIds.get(i)).toString()))
                                sortMap.put(((P2P_Report_Data)reportData.get(j)).getCellCol(),((P2P_Report_Data)reportData.get(j)).getValue());
                        }*/

                        /**循环取得该行的所有值,并用","号隔开*/
                        String value = "";
                        
                        if(sortMap.size()>0 && (colsIndex!=null && colsIndex.size()>0))
                        {
                        	String cellValue="";
                        	for(int k=0;k<colsIndex.size();k++){
                        		cellValue=(String)sortMap.get(colsIndex.get(k));
                        		value+=(value.equals("")?"":Config.SPLIT_SYMBOL_COMMA) + 
                        			(sortMap.containsKey(colsIndex.get(k))==true?(cellValue==null?DEFAULTNUMBERVALUE:cellValue):" ");
	                            /*
	                            //取map的键值对                        	
	                            Set set = sortMap.entrySet();
	                            Iterator it = set.iterator();
	                            int j=1;
	                            while(it.hasNext())
	                            {
	                                Map.Entry entry = (Map.Entry)it.next();
	                                String cellValue ="";
	                                if(entry.getValue() != null)
	                                    cellValue = ((String) entry.getValue()).trim();
	                                else
	                                    cellValue = DEFAULTNUMBERVALUE;
	                                
	                                //最后一个数据不加逗号
	                                if(j==sortMap.size())
	                                    value += cellValue;
	                                else
	                                    value += cellValue+Config.SPLIT_SYMBOL_COMMA;
	                                j++;
	                            }*/
                        	}
                            if(!value.equals("")) value=rowIndex + (value.equals("")?"":Config.SPLIT_SYMBOL_COMMA) + value;
                            rowIndex++;
                        }
       
                        /** 为T2Data元素添加Value 该元素的值是 一行记录的值*/ 
                        createT2QDElement(repDatas_Element,String.valueOf(i+1),"1",value);
                        
                        /*if(!value.equals("")){
                        	Element t2Data_Element = repDatas_Element.addElement("T2Data");
                        	//为T2Data元素添加RowID该元素的值是行号 
                            Element rowId_Element = t2Data_Element.addElement("RowID");
                            rowId_Element.setText(String.valueOf(i+1));
                            //为T2Data元素添加region
                            Element region_Element = t2Data_Element.addElement("region");
                            region_Element.setText("1");
                            //赋值
                        	Element value_Element = t2Data_Element.addElement("Value");
                        	value_Element.setText(value);
                        }*/
                    }else{	//前15行数据改为点对点式接口形式                        
                        for(int k=0;k<reportData.size();k++)
                        {   
                            if(((P2P_Report_Data)reportData.get(k)).getCellRow().equals(((Integer)rowIds.get(i)).toString()))
                            {
                                /**循环为RepDatas元素添加T1Data元素  每个T1Data元素表示一个单元格记录*/
                                                               
                                /**为T1Data元素添加CellRow 该元素的值是 单元格行号*/ 
                                String cellRow  = ((P2P_Report_Data)reportData.get(k)).getCellRow();
                               
                                /**为T1Data元素添加CellCol 该元素的值是 单元格列号*/ 
                                String cellCol = ((P2P_Report_Data)reportData.get(k)).getCellCol();
                            
                                /** 为T1Data元素添加CellID 该元素的值是 单元格值*/ 
                                String cellValue = ((P2P_Report_Data)reportData.get(k)).getValue();
                                String cellName = ((P2P_Report_Data)reportData.get(k)).getCellName();
                                
                                if(cellValue!=null){
                                    /**判断该列是否是百分数列，如果是则除100*/
                                    if(db2XmlHandler.isPercentCell(childRepId,versionId,cellName))
                                        cellValue = db2XmlHandler.toPercent(cellValue);
                                    cellValue = cellValue.trim().equals("")?DEFAULTNUMBERVALUE:cellValue.trim();
                                }else{
                                    cellValue = DEFAULTNUMBERVALUE;
                                }
                                
                                createT2DDElement(repDatas_Element,cellRow,cellCol,cellValue);
                                
                                /*if(!cellValue.equals("")){
                                	Element t1Data_Element = repDatas_Element.addElement("T2Data");
                                	Element cellRow_Element = t1Data_Element.addElement("CellRow");
                                	cellRow_Element.setText(cellRow.trim());
                                	Element cellCol_Element = t1Data_Element.addElement("CellCol");
                                	cellCol_Element.setText(cellCol.trim());
                                	Element value_Element = t1Data_Element.addElement("Value");
                                	value_Element.setText(cellValue);
                                }*/
                            }             
                        }
                    }
                }           
            }
            /**写xml文件*/  
            this.writeXml(document,report);
        }
    }
   
    /**
     * 为根元素添加Report 元素，其中包含一些报表的基本属性
     * @param root_Element Element 根元素
     * @param report  ReportInForm  包含该报表的一些基本属性
     */
    private void addReportElement(Element root_Element,ReportIn report)
    {
        if(root_Element!=null && report!=null)
        {
            /**为根元素添加Report元素*/        
            Element report_Element = root_Element.addElement("Report");
            
            /**为Report元素添加SubRepID属性  该属性的值是 子报表id*/
            if(report.getMChildReport().getComp_id().getChildRepId()!=null)
                report_Element.addAttribute("SubRepID",report.getMChildReport().getComp_id().getChildRepId().trim());
            else
                report_Element.addAttribute("SubRepID","");
            
            /**为Report元素添加Version属性  该属性的值是 版本号*/
            if(report.getMChildReport().getComp_id().getVersionId()!=null)
                report_Element.addAttribute("Version",report.getMChildReport().getComp_id().getVersionId().trim());
            else
                report_Element.addAttribute("Version","");
            
            /**为Report元素添加RepDate属性  该属性的值是 上报时间*/
            if(report.getReportDate()!=null)
                report_Element.addAttribute("RepDate",DateFormat.format(report.getReportDate()).trim());
            else
                report_Element.addAttribute("RepDate","");
            
            /**为Report元素添加OrgID属性  该属性的值是 机构id*/
            if(report.getOrgId()!=null)
                report_Element.addAttribute("OrgID",report.getOrgId().trim());
            else
                report_Element.addAttribute("OrgID","");
            
            /**为Report元素添加RepYear属性 该属性的值是 报送年份*/
            if(report.getYear()!=null)
                report_Element.addAttribute("RepYear",report.getYear().toString().trim());
            else
                report_Element.addAttribute("RepYear","");
            
            /**为Report元素添加SubRepID属性 该属性的值是 报送期数*/
            if(report.getTerm()!=null)
                report_Element.addAttribute("RepMonth",report.getTerm().toString().trim());
            else
                report_Element.addAttribute("RepMonth","");
            
            /**为Report元素添加DataRange属性  该属性的值是 数据范围id*/
            if(report.getMDataRgType().getDataRangeId()!=null)
                report_Element.addAttribute("DataRange",report.getMDataRgType().getDataRangeId().toString().trim());
            else
                report_Element.addAttribute("DataRange","");
            
            /**为Report元素添加CurrencyID属性  该属性的值是 币种id*/
            if(report.getMCurr().getCurId()!=null)
                report_Element.addAttribute("CurrencyID",report.getMCurr().getCurId().toString().trim());
            else
                report_Element.addAttribute("CurrencyID","");
           
            /**为Report元素添加Area属性  该属性的值是 空*/
            report_Element.addAttribute("Area","");
        }
    } 
    /**
     * 根据传过来的文件名写相应的文件
     * @param fileName
     */
    private void writeXml(Document document ,ReportIn reportIn) throws Exception
    {
        /**实际报表id*/
        String reportInId = reportIn.getRepInId().toString();
        /**子报表id*/
        String childRepId = reportIn.getMChildReport().getComp_id().getChildRepId();
        /**版本号*/
        String versionId = reportIn.getMChildReport().getComp_id().getVersionId();
        /**子报表名*/
        String reportName =  reportIn.getMChildReport().getReportName();
        /**机构名称*/
        String orgName =StrutsMOrgDelegate.selectOne(reportIn.getOrgId()).getOrg_name();
        /**报送时间*/
        Date repDate = reportIn.getReportDate();
        String reportDate ="";
        if(repDate!=null)
            reportDate = FORMAT.format(repDate); 
        try 
        {
            XMLWriter writer = null;
            /** 格式化输出*/
            OutputFormat format = OutputFormat.createPrettyPrint();
            /** 指定XML编码 */
            format.setEncoding("GBK");
       
            String fileName= fileSavePath +Config.FILESEPARATOR + reportInId + "-" + childRepId + ".xml";
            
            writer= new XMLWriter(new FileWriter(new File(fileName)),format);
                        
            writer.write(document);
            writer.close();
            if(db2XmlHandler.setReportDataWarehouseFlag(Integer.valueOf(reportInId))==false)
            {  
                FitechLog.writeLog(Config.LOG_SYSTEM_CREATESTORAGEXML,Config.SYSTEM_OPERATOR,"设置报送数据标志失败！（报表名："+reportName+",版本号："+versionId+",报送机构："+orgName+",报送时间："+reportDate+"）");
                //FitechLog.writeLog(Config.LOG_SYSTEM_CREATESTORAGEXML,"系统","设置报送数据标志失败！(编号："+reportId+")"); 
                /**错误数加1*/
                failReportNum++;
            }
            else
            {
                //FitechLog.writeLog(Config.LOG_SYSTEM_CREATESTORAGEXML,"系统","生成仓库文件成功！（报表名："+reportName+",版本号："+versionId+",报送机构："+orgName+",报送时间："+reportDate+"）");
                /**正确数加1*/
                successReportNum++;  
            }  
        } 
        catch (Exception e) 
        {
            FitechLog.writeLog(Config.LOG_SYSTEM_CREATESTORAGEXML,"系统","创建xml文件失败！（报表名："+reportName+",版本号："+versionId+",报送机构："+orgName+",报送时间："+reportDate+"）");
            //FitechLog.writeLog(Config.LOG_SYSTEM_CREATESTORAGEXML,"系统","创建xml文件失败！(编号："+reportId+")");
            /**错误数加1*/
            failReportNum++;
            log.printStackTrace(e);
        }
        
    }
    
    /**
     * 创建T2类型的清单部分的XML数据域
     * 
     * @author rds
     * @date 2006-01-10
     * 
     * @param parentElement Element 父元素域
     * @param rowId String 行号
     * @param region String 数据区块标记
     * @param value String 数据值
     * @return Element 创建失败，返回null
     */
    private void createT2QDElement(Element parentElement,String rowId,String region,String value){
    	if(parentElement==null || rowId==null || region==null || value==null) return;
    	if(rowId.equals("") || region.equals("") || value.equals("")) return;
    	
    	try{
    		Element t2DataElement=parentElement.addElement("T2Data");                        
            //行号元素 
            Element rowIdElement = t2DataElement.addElement("RowID");
            rowIdElement.setText(rowId.trim());
            //数据区块标记元素
            Element regionElement = t2DataElement.addElement("region");
            regionElement.setText(region.trim());
            //数据值元素
        	Element valueElement = t2DataElement.addElement("Value");
        	valueElement.setText(value.trim());
    	}catch(Exception e){
       		log.printStackTrace(e);
    	}
    }
    
    /**
     * 创建T2类型的点对点部分的XML数据域
     * 
     * @author rds
     * @date 2006-01-10
     * 
     * @param parentElement Element 父元素域
     * @param cellRow String 单元格的行号
     * @param cellCol String 单元格的列号
     * @param cellValue String 数据值
     * @return Element 创建失败，返回null
     */
    private void createT2DDElement(Element parentElement,String cellRow,String cellCol,String cellValue){
    	if(parentElement==null || cellRow==null || cellCol==null || cellValue==null) return;
    	if(cellRow.equals("") || cellCol.equals("") || cellValue.equals("")) return;
    	
    	try{
    		//数据域
    		Element t2DataElement = parentElement.addElement("T2Data");
    		//单元格行域
        	Element cellRowElement = t2DataElement.addElement("CellRow");
        	cellRowElement.setText(cellRow.trim());
        	//单元格列域
        	Element cellColElement = t2DataElement.addElement("CellCol");
        	cellColElement.setText(cellCol.trim());
        	//数据值域
        	Element valueElement = t2DataElement.addElement("Value");
        	valueElement.setText(cellValue);
    	}catch(Exception e){
       		log.printStackTrace(e);
    	}
    }
    
    /**
     * 创建T1类型的XML数据域
     * 
     * @author rds
     * @date 2006-01-10
     * 
     * @param parentElement Element 父元素域
     * @param cellRow String 单元格的行号
     * @param cellCol String 单元格的列号
     * @param cellValue String 数据值
     * @return Element 创建失败，返回null
     */
    private void createT1Element(Element parentElement,String cellRow,String cellCol,String cellValue){
    	if(parentElement==null || cellRow==null || cellCol==null || cellValue==null) return;
    	if(cellRow.equals("") || cellCol.equals("") || cellValue.equals("")) return;
    	
    	try{
    		//数据域
    		Element t2DataElement = parentElement.addElement("T1Data");
    		//单元格行域
        	Element cellRowElement = t2DataElement.addElement("CellRow");
        	cellRowElement.setText(cellRow.trim());
        	//单元格列域
        	Element cellColElement = t2DataElement.addElement("CellCol");
        	cellColElement.setText(cellCol.trim());
        	//数据值域
        	Element valueElement = t2DataElement.addElement("Value");
        	valueElement.setText(cellValue);
    	}catch(Exception e){
       		log.printStackTrace(e);
    	}
    }
}