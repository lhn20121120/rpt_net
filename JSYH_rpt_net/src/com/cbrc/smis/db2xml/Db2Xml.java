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
 * @author Ҧ��
 * 
 * �����ݿ��ת��xml�ļ�
 *
 */
public class Db2Xml {
	/**
	 * ��Ԫ���Ĭ��ֵ
	 */
    public static final String DEFAULTNUMBERVALUE="0";

    private static FitechException log = new FitechException(Db2Xml.class);
    
    /**ΪDb2Xml���ṩ�������*/
    private Db2XmlUtil db2XmlHandler = new Db2XmlUtil();
    
    /**�ļ����·��*/
    private String fileSavePath = "";
    
    /**������ת�ַ���*/
    private SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    
    /**������Ҫתxml�ı�������*/
    private int reportTotal = 0;
    /**����ʧ�ܵı�������*/
    private int failReportNum = 0;
    /**���ɳɹ��ı�������*/
    private int successReportNum = 0;
    
    /**���ⱨ��(��Ҫ�����ļ�ʱ���T2�ı���)*/
    /*private String[] specialReport ={"G1301","G1302","G1303","G1304","G1402","G2400","S3403","G1401","S4400","G2300"};*/
    private String[] specialReport ={"G1301","G1302","G1303","G1304","G1402","G2400","S3403","G1401","G2300"}; 
    
    /**���ⱨ�� S2300*/
    private String S2300 = "S2300";
    /**���ⱨ��G1500*/
    private String G1500 = "G1500";
    
    private final static SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    
    public Db2Xml(){}
    
    /**
     * �����ݱ�ת��xml�ļ�
     * @return int  1 ��ʾ�ɹ�
     *              -1 ��ʾʧ��
     *              0 ��ʾû����Ҫ����xml�ļ�¼
     */
    public int DataBase2Xml() throws Exception
    {
        int result = Config.DataToXML_FAILED;
        
        /**ȡ����Ҫת��xml�ļ��ı�����*/
        List reports  = db2XmlHandler.get_NeedToXmlReps();
        
        if(reports!=null && reports.size()!=0)
        {
            /**����Ŀ¼��Ŀ¼�����ǵ�ʱ��ʱ��*/
            Date today = new Date();
            SimpleDateFormat FORMAT = new SimpleDateFormat("yyyyMMddhhmmss");
            String directoryName =  FORMAT.format(today);
            
            /**����Ŀ¼*/
            fileSavePath = Config.XMLData_PATH + Config.FILESEPARATOR + directoryName;
            File directory = new File(fileSavePath);
            
            if(directory.mkdirs()==false)
            {
                FitechLog.writeLog(Config.LOG_SYSTEM_CREATESTORAGEXML,Config.SYSTEM_OPERATOR,"��������xml�ļ���Ŀ¼ʧ�ܣ�");
                return Config.DataToXML_FAILED;
            }
            /**ȡ�ñ�������*/
            reportTotal = reports.size();           
            
            /**ѭ����ÿ������ת��xml�ļ�*/
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
                    /**�汾��*/
                    versionId = reportIn.getMChildReport().getComp_id().getVersionId();
                    /**�ӱ�����*/
                    reportName =  reportIn.getMChildReport().getReportName();
                    /**��������*/
                    com.fitech.net.form.OrgNetForm orgNetForm=StrutsOrgNetDelegate.selectOne(reportIn.getOrgId(),bool);
                    orgName =orgNetForm!=null?orgNetForm.getOrg_name():"";
                    /**����ʱ��*/
                    Date repDate = reportIn.getReportDate();
                    
                    if(repDate!=null) reportDate = FORMAT.format(repDate);      
                    
                    reportsHandler(reportIn);
                } catch (Exception e){
                    log.printStackTrace(e);
                    FitechLog.writeLog(Config.LOG_SYSTEM_CREATESTORAGEXML,Config.SYSTEM_OPERATOR,"����xml�ļ�ʧ�ܣ�����������"+reportName+",�汾�ţ�"+versionId+",���ͻ�����"+orgName+",����ʱ�䣺"+reportDate+"��");
                    continue;
                }
            }    
            FitechLog.writeLog(Config.LOG_SYSTEM_CREATESTORAGEXML,Config.SYSTEM_OPERATOR,"������" + 
            		reportTotal + "���ֿ��ļ�," + 
            		successReportNum + "���ɹ�," + 
            		failReportNum+"��ʧ�ܣ�");
            result = Config.DataToXML_SUCCESS;
        }else{			//û����Ҫ����xml�ļ��ļ�¼
            result = Config.NO_DataToXML;
        }
        
        return result;
    }
   
    /**
     * ����������ת��xml�ļ�
     * 
     * @param report ReportInForm �����ñ���Ļ�������
     * @return void
     */
    public void reportsHandler(ReportIn reportIn) throws Exception
    {
        if(reportIn!=null)
        {            
            /**�ӱ���id*/
            String childRepId = reportIn.getMChildReport().getComp_id().getChildRepId();
            /**�汾��*/
            String versionId = reportIn.getMChildReport().getComp_id().getVersionId();
            /**�ӱ�����*/
            String reportName =  "";
            /**��������*/
            String orgName ="";
            /**����ʱ��*/
            String reportDate = ""; 
                       
            /**���ⱨ����*/
            /*for(int i=0; i<specialReport.length; i++)
            {
                if(childRepId.equalsIgnoreCase(specialReport[i]))
                {
                    SpecialReport_To_Xml(reportIn);
                    return;
                }
            }*/
            
            /**����G1500*/
            /*if(childRepId.equalsIgnoreCase(G1500))
            {
                G1500_To_Xml(reportIn);
                return;
            }*/
            
            /**����S2300*/
            /*if(childRepId.equalsIgnoreCase(S2300))
            {
                S2300_To_Xml(reportIn);
                return;
            }*/
            
            /**�����ӱ���id�Ͱ汾�Ų��ҳ��ñ�������ͣ���Ե㱨�� or �嵥����*/
            int reportType = db2XmlHandler.get_PeportStyle(childRepId,versionId);
            switch(reportType)
            {
                /** �����Ե㱨�� */
                case 1:  
                {
                    P2PReport_To_Xml(reportIn);                    
                    break;
                }
                /**�����嵥ʽ����*/
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
                    FitechLog.writeLog(Config.LOG_SYSTEM_CREATESTORAGEXML,Config.SYSTEM_OPERATOR,"δ֪�������ͣ�����������"+reportName+",�汾�ţ�"+versionId+",���ͻ�����"+orgName+",����ʱ�䣺"+reportDate+"��");
                	break;
            }
        }
    }
    
    /**
     * �����Ե�ʽ��������xml�ļ�
     * @param reportInId Integer ʵ���Ա���id
     */
    private void P2PReport_To_Xml(ReportIn report) throws Exception
    {
        if(report!=null)
        {
            Integer reportInId = report.getRepInId();
            
            /**�ӱ���id*/ 
            String childRepId = report.getMChildReport().getComp_id().getChildRepId();
            
            /**�汾��id*/
            String versionId = report.getMChildReport().getComp_id().getVersionId();
            
            /**�����ĵ�����*/
            Document document = DocumentHelper.createDocument();
            
            /**��Ԫ����DataTrans*/
            Element root_Element = document.addElement("DataTrans");
            
            /**����RepType T1��ʾ�ǵ�Ե�ʽ����*/
            root_Element.addAttribute("RepType","T1");
            
            /**Ϊ��Ԫ�����ReportԪ�� �����ñ����һЩ��������*/
            this.addReportElement(root_Element,report);
            
            /**Ϊ��Ԫ�����RepDatas �����ñ��������*/
            Element repDatas_Element = root_Element.addElement("RepDatas");
            
            /**ȡ���õ�Ե㱨�������*/
            List reportData  = db2XmlHandler.get_P2PReport_Data(reportInId);
            
            if(reportData!=null && reportData.size()!=0)
            {
            	P2P_Report_Data p2pReportData=null;
            	String rowNum="",colNum="";
            	/**ѭ��ΪRepDatasԪ�����T1DataԪ��,ÿ��T1DataԪ�ر�ʾһ����Ԫ���¼*/
                for(int i=0;i<reportData.size();i++)
                {   
                	p2pReportData=(P2P_Report_Data)reportData.get(i);
                                                           
                    //��Ԫ���к� 
                    rowNum = p2pReportData.getCellRow();
                                       
                    //��Ԫ���к�                    
                    colNum = p2pReportData.getCellCol();
                    
                    //��Ԫ��ֵ
                    String value = p2pReportData.getValue();
                    //��Ԫ������
                    String cellName = p2pReportData.getCellName();
                    
                    if(value!=null)
                    {
                        /**�жϸ����Ƿ��ǰٷ����У���������100*/
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
            /**дxml�ļ�*/  
            this.writeXml(document,report);
        }
    }
    
    /**��
     * �����嵥ʽ��������xml�ļ�
     * @param reportInId Integer ʵ���ӱ���id
     * @param  childRepId String �ӱ���id
     * @param versionId String �汾��
     */
    public  void BillReport_To_Xml(ReportIn reportIn) throws Exception
    {
    	Db2XmlUtil db2XmlUtil = null;
        if(reportIn!=null)
        {
            Integer reportInId =reportIn.getRepInId();
           
            /**�ӱ���id*/
            String childRepId = reportIn.getMChildReport().getComp_id().getChildRepId();
            /**�汾��*/
            String versionId = reportIn.getMChildReport().getComp_id().getVersionId();
            /**�ӱ�����*/
            String reportName =  reportIn.getMChildReport().getReportName();
            /**��������*/
            String orgName =StrutsMOrgDelegate.selectOne(reportIn.getOrgId()).getOrg_name();
            /**����ʱ��*/
            Date repDate = reportIn.getReportDate();
            String reportDate = "";
            if(repDate!=null) reportDate = FORMAT.format(repDate);      
            
            /**�����ĵ�����*/
            Document document = DocumentHelper.createDocument();
            
            /**��Ԫ����DataTrans*/
            Element root_Element = document.addElement("DataTrans");
            
            /**����RepType T2��ʾ���嵥ʽ����*/
            root_Element.addAttribute("RepType","T2");           
           
            
            /**Ϊ��Ԫ�����ReportԪ�� �����ñ����һЩ��������*/
            this.addReportElement(root_Element,reportIn);
            
            /**Ϊ��Ԫ�����RepDatas �����ñ��������*/
            Element repDatas_Element = root_Element.addElement("RepDatas");
            
            ResultSet rs =null;
            ResultSetMetaData rsMetaData =null;
            
            try 
            {
            	//�ٷ���ת����С���������б�
            	List percentCols=StrutsCellPercentDelegate.getPercentCellOrCol(childRepId,versionId);
            	
            	db2XmlUtil = new Db2XmlUtil();
                /**����嵥ʽ���������*/
                rs = db2XmlUtil.get_BillReport_Data(reportInId,childRepId,versionId);
                
                /*if (rs == null)  // System.out.println("----------------rs = null");*/
                if(rs!=null)
                {
                    rsMetaData = rs.getMetaData();
                    /**�е�����*/
                    int _colNum = rsMetaData.getColumnCount();
                    int colNum=0;
                    /**Ĭ�϶�����ʱ�ӵ�3�ж�ȡ*/
                    int startPos = 3;
                    /**ѭ��ΪRepDatasԪ�����T2DataԪ��,ÿ��T1DataԪ�ر�ʾһ����Ԫ���¼*/
                    String row="",region="",type="",value="",cellValue="",colName="";
                    int rowIndex=0;  //�����
                    String e7="";    //�ͻ�����
                    while(rs.next())
                    {
                        //�к�
                    	row=rs.getString("Col1");
                        //������������            
                        region="";
                        
                        try
                        {
                            /**Ѱ�Ҹñ����Ƿ����Type�ֶ�*/
                            type = rs.getString("Type");
                            region=type;       //regionԪ�ص�ֵ�����͵�ֵ
                            startPos = 4;      //�����ݱ�����"Type"�ֶ�ʱ���ӵ��ĸ��ֶ�ȡֵ
                        }catch(SQLException e){
                            region="1";        //���������Type�ֶ�,regionԪ��ֵΪ1
                            startPos = 3;	   //�����ݱ���û��"Type"�ֶ�ʱ���ӵ������ֶ�ȡֵ
                        }
                        
                        if(childRepId.equals("S3600") && type.trim().equals("3")){
                        	colNum=_colNum-1;
                        }else{
                        	colNum=_colNum;
                        }
                                                
                        value = "";
                        /** ��ֵ���ӳ��ַ�����֮����"," �Ÿ��� */
                        for(int i=startPos ;i<=colNum;i++)
                        {
                            /**ֵ*/   
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
                            /**����к�*/
                            colName = rsMetaData.getColumnName(i);
                            
                            if(cellValue==null){
                                 cellValue = DEFAULTNUMBERVALUE;
                            }else{
                                /**�жϸ����Ƿ��ǰٷ����У���������100*/
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
                        
                       
                        /** ΪT2DataԪ�����Value ��Ԫ�ص�ֵ�� һ�м�¼��ֵ*/ 
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
                FitechLog.writeLog(Config.LOG_SYSTEM_CREATESTORAGEXML,Config.SYSTEM_OPERATOR,"����xml�ļ�ʧ�ܣ�����������"+reportName+",�汾�ţ�"+versionId+",���ͻ�����"+orgName+",����ʱ�䣺"+reportDate+"��");
                log.printStackTrace(e);
            }finally{
            	if(db2XmlUtil!=null) db2XmlUtil.closeConnection();
            }
        }
    }
    
    /**
     * �������ⱨ������xml�ļ�(�ĳ�t2��ʽ��)
     * @param reportInId Integer ʵ���Ա���id
     */
    private void SpecialReport_To_Xml(ReportIn report) throws Exception
    {
        if(report!=null)
        {
            Integer reportInId = report.getRepInId();
            
            /**�ӱ���id*/
            String childRepId = report.getMChildReport().getComp_id().getChildRepId();

            /**�汾��*/
            String versionId = report.getMChildReport().getComp_id().getVersionId(); 
            /**�����ĵ�����*/
            Document document = DocumentHelper.createDocument();
            
            /**��Ԫ����DataTrans*/
            Element root_Element = document.addElement("DataTrans");
            
            /**����RepType T2��ʾ���嵥ʽ����*/
            root_Element.addAttribute("RepType","T2");
            
            /**Ϊ��Ԫ�����ReportԪ�� �����ñ����һЩ��������*/
            this.addReportElement(root_Element,report);
            
            /**Ϊ��Ԫ�����RepDatas �����ñ��������*/
            Element repDatas_Element = root_Element.addElement("RepDatas");
            
            /**ȡ�������ⱨ�������*/
            List reportData  = db2XmlHandler.get_P2PReport_Data(reportInId);
            List colList=db2XmlHandler.getP2PReportCols(childRepId,versionId,Db2XmlUtil.getExceptCells(childRepId,versionId));
            
            if((reportData!=null && reportData.size()>0) && (colList!=null && colList.size()>0))
            {  
                /**��ѯ�����ⱨ�����ж����м�¼*/
                List rowIds = db2XmlHandler.get_specialReportRowList(reportData);
                int rowIndex=1; //�嵥�ӿ��ļ��У����ݵ��к�
                ArrayList colsIndex=null;
                /**����Ϊ��λ,��������","�Ÿ���,���Ҽ���xml�ļ�*/
                P2P_Report_Data p2pReportData=null;
                String e7="";	//�ͻ�����
                for(int i=0;i<rowIds.size();i++)
                {
                    /**ѭ��ΪRepDatasԪ�����T2DataԪ��  ÿ��T2DataԪ�ر�ʾһ�м�¼*/
                    
                    /**��ÿһ�е��к�ֵ��������*/
                    SortedMap sortMap =  new TreeMap();
                    
                    for(int j=0;j<reportData.size();j++)
                    {
                    	p2pReportData=(P2P_Report_Data)reportData.get(j);
                        /**����treeMap�����ԣ��Ը��е��н�������*/
                        String cellRow = p2pReportData.getCellRow();
                        String cellCol = p2pReportData.getCellCol();
                        String cellName = p2pReportData.getCellName();
                        String cellValue = p2pReportData.getValue();
                       
                        if(cellRow.equals(((Integer)rowIds.get(i)).toString()))
                        {    
                            /**�жϸ����Ƿ��ǰٷ����У���������100*/
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

                    /**ѭ��ȡ�ø��е�����ֵ,����","�Ÿ���*/
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
		                        //ȡmap�ļ�ֵ��
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
		                      
		                            //���һ�����ݲ��Ӷ���
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
                    /** ΪT2DataԪ�����Value ��Ԫ�ص�ֵ�� һ�м�¼��ֵ*/
                    if(childRepId.substring(0,4).equals("G130") || childRepId.equals("G2300") || childRepId.equals("G2400"))
                    	value=(rowIndex++) + "," + value; 
                    createT2QDElement(repDatas_Element,String.valueOf(i+1),"1",value);
                    
                    /*if(!value.equals("")){
                    	Element t2Data_Element = repDatas_Element.addElement("T2Data");                        
                        //ΪT2DataԪ�����RowID ��Ԫ�ص�ֵ�� �к� 
                        Element rowId_Element = t2Data_Element.addElement("RowID");
                        rowId_Element.setText(String.valueOf(i+1));                        
                        //ΪT2DataԪ�����region
                        Element region_Element = t2Data_Element.addElement("region");
                        region_Element.setText("1");
                    	Element value_Element = t2Data_Element.addElement("Value");
                    	value_Element.setText(value);
                    }*/
                }           
            }
            /**дxml�ļ�*/  
            this.writeXml(document,report);
        }
    }
 
    /***
     * ����G1500
     * @param report
     */
    private void G1500_To_Xml(ReportIn report) throws Exception
    {
        if(report!=null)
        {
            Integer reportInId = report.getRepInId();
            /**�ӱ���id*/
            String childRepId = report.getMChildReport().getComp_id().getChildRepId();
            
            /**�汾��*/
            String versionId = report.getMChildReport().getComp_id().getVersionId();        
            /**�����ĵ�����*/
            Document document = DocumentHelper.createDocument();
            
            /**��Ԫ����DataTrans*/
            Element root_Element = document.addElement("DataTrans");
            
            /**����RepType T2��ʾ���嵥ʽ����*/
            root_Element.addAttribute("RepType","T2");
            
            /**Ϊ��Ԫ�����ReportԪ�� �����ñ����һЩ��������*/
            this.addReportElement(root_Element,report);
            
            /**Ϊ��Ԫ�����RepDatas �����ñ��������*/
            Element repDatas_Element = root_Element.addElement("RepDatas");
            
            /**ȡ�������ⱨ�������*/
            List reportData  = db2XmlHandler.get_P2PReport_Data(reportInId);
            
            if(reportData!=null && reportData.size()>0)
            {  
                /**��ѯ�����ⱨ�����ж����м�¼*/
                List rowIds = db2XmlHandler.get_specialReportRowList(reportData);
                
                /**����Ϊ��λ,��������","�Ÿ���,���Ҽ���xml�ļ�*/
                P2P_Report_Data p2pReportData=null;
                
                for(int i=0;i<rowIds.size();i++)
                {
                    /**ѭ��ΪRepDatasԪ�����T2DataԪ��  ÿ��T2DataԪ�ر�ʾһ�м�¼*/
                    
                    /**ǰ21�� Ϊ�嵥ʽxml*/
                    if(i+1<21)
                    {                        
                        /**��ÿһ�е��к�ֵ��������*/
                        SortedMap sortMap =  new TreeMap();
                        for(int j=0;j<reportData.size();j++)
                        {
                            /**����treeMap�����ԣ��Ը��е��н�������*/
                        	p2pReportData=(P2P_Report_Data)reportData.get(j);
                            String cellRow = p2pReportData.getCellRow();
                            String cellCol = p2pReportData.getCellCol();
                            String cellName = p2pReportData.getCellName();
                            String cellValue = p2pReportData.getValue();
                           
                            if(cellRow.equals(((Integer)rowIds.get(i)).toString()))
                            {    
                                /**�жϸ����Ƿ��ǰٷ����У���������100*/
                                if(db2XmlHandler.isPercentCell(childRepId,versionId,cellName))
                                    cellValue = db2XmlHandler.toPercent(cellValue);
                                sortMap.put(cellCol,cellValue);                        
                            }
                        }
                       /* for(int j=0;j<reportData.size();j++)
                        {
                            //����treeMap�����ԣ��Ը��е��н�������
                            if(((P2P_Report_Data)reportData.get(j)).getCellRow().equals(((Integer)rowIds.get(i)).toString()))
                                sortMap.put(((P2P_Report_Data)reportData.get(j)).getCellCol(),((P2P_Report_Data)reportData.get(j)).getValue());
                        }*/

                        /**ѭ��ȡ�ø��е�����ֵ,����","�Ÿ���*/
                        String value = "";
                        
                        if(sortMap.size()>0)
                        {
                            /**ȡmap�ļ�ֵ��*/
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
                                
                                /**���һ�����ݲ��Ӷ���*/
                                if(j==sortMap.size())
                                    value += cellValue.trim();
                                else
                                    value += cellValue+Config.SPLIT_SYMBOL_COMMA;
                                j++;
                            }
                        }
                        if(!value.equals("")) value=(i+1) + "," + value;
                        /** ΪT2DataԪ�����Value ��Ԫ�ص�ֵ�� һ�м�¼��ֵ*/
                        createT2QDElement(repDatas_Element,String.valueOf(i+1),"1",value);
                        
                        /*if(!value.equals("")){
                        	Element t2Data_Element = repDatas_Element.addElement("T2Data");
                            //ΪT2DataԪ�����RowID ��Ԫ�ص�ֵ�� �к� 
                            Element rowId_Element = t2Data_Element.addElement("RowID");
                            rowId_Element.setText(String.valueOf(i+1));                            
                            //ΪT2DataԪ�����region
                            Element region_Element = t2Data_Element.addElement("region");
                            region_Element.setText("1");
                        	Element value_Element = t2Data_Element.addElement("Value");
                        	value_Element.setText(value);
                        }*/
                    }
                    else/**22�п�ʼ ��Ϊ��Ե�ʽ*/
                    {                        
                        for(int k=0;k<reportData.size();k++)
                        {   
                        	p2pReportData=(P2P_Report_Data)reportData.get(k);
                            if(p2pReportData.getCellRow().equals(((Integer)rowIds.get(i)).toString()))
                            {
                                /**ѭ��ΪRepDatasԪ�����T1DataԪ��  ÿ��T1DataԪ�ر�ʾһ����Ԫ���¼*/
                                                              
                                /**ΪT1DataԪ�����CellRow ��Ԫ�ص�ֵ�� ��Ԫ���к�*/                                
                                String cellRow  = p2pReportData.getCellRow();
                               
                                /**ΪT1DataԪ�����CellCol ��Ԫ�ص�ֵ�� ��Ԫ���к�*/                                
                                String cellCol = p2pReportData.getCellCol();
                            
                                /** ΪT1DataԪ�����CellID ��Ԫ�ص�ֵ�� ��Ԫ��ֵ*/                                 
                                String cellValue = p2pReportData.getValue();
                                String cellName = p2pReportData.getCellName();
                                
                                if(cellValue!=null){
                                    /**�жϸ����Ƿ��ǰٷ����У���������100*/
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
            /**дxml�ļ�*/  
            this.writeXml(document,report);
        }
    }
   
    /***
     * ����S2300
     */
    private void S2300_To_Xml(ReportIn report) throws Exception
    {
        if(report!=null)
        {
            Integer reportInId = report.getRepInId();
            /**�ӱ���id*/
            String childRepId = report.getMChildReport().getComp_id().getChildRepId();
            
            /**�汾��*/
            String versionId = report.getMChildReport().getComp_id().getVersionId();              
            /**�����ĵ�����*/
            Document document = DocumentHelper.createDocument();
            
            /**��Ԫ����DataTrans*/
            Element root_Element = document.addElement("DataTrans");
            
            /**����RepType T2��ʾ���嵥ʽ����*/
            root_Element.addAttribute("RepType","T2");
            
            /**Ϊ��Ԫ�����ReportԪ�� �����ñ����һЩ��������*/
            this.addReportElement(root_Element,report);
            
            /**Ϊ��Ԫ�����RepDatas �����ñ��������*/
            Element repDatas_Element = root_Element.addElement("RepDatas");
            
            /**ȡ�������ⱨ�������*/
            List reportData  = db2XmlHandler.get_P2PReport_Data(reportInId);
            
            if(reportData!=null && reportData.size()!=0)
            {  
                /**��ѯ�����ⱨ�����ж����м�¼*/
                List rowIds = db2XmlHandler.get_specialReportRowList(reportData);
                
                int rowIndex=1; //�嵥�ӿ��ļ��У����ݵ��к�
                ArrayList colsIndex=null;
                /**����Ϊ��λ,��������","�Ÿ���,���Ҽ���xml�ļ�*/
                for(int i=0;i<rowIds.size();i++)
                {
                    /**�ӵ�ʮ���п�ʼ����11��Ϊ�嵥ʽxml*/
                    if(i+1>=16)
                    {                       
                        /**��ÿһ�е��к�ֵ��������*/
                        SortedMap sortMap =  new TreeMap();
                        for(int j=0;j<reportData.size();j++)
                        {
                            /**����treeMap�����ԣ��Ը��е��н�������*/
                            String cellRow = ((P2P_Report_Data)reportData.get(j)).getCellRow();
                            String cellCol = ((P2P_Report_Data)reportData.get(j)).getCellCol();
                            String cellName = ((P2P_Report_Data)reportData.get(j)).getCellName();
                            String cellValue = ((P2P_Report_Data)reportData.get(j)).getValue();
                           
                            if(cellRow.equals(((Integer)rowIds.get(i)).toString()))
                            {    
                                /**�жϸ����Ƿ��ǰٷ����У���������100*/
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
                            //����treeMap�����ԣ��Ը��е��н�������
                            if(((P2P_Report_Data)reportData.get(j)).getCellRow().equals(((Integer)rowIds.get(i)).toString()))
                                sortMap.put(((P2P_Report_Data)reportData.get(j)).getCellCol(),((P2P_Report_Data)reportData.get(j)).getValue());
                        }*/

                        /**ѭ��ȡ�ø��е�����ֵ,����","�Ÿ���*/
                        String value = "";
                        
                        if(sortMap.size()>0 && (colsIndex!=null && colsIndex.size()>0))
                        {
                        	String cellValue="";
                        	for(int k=0;k<colsIndex.size();k++){
                        		cellValue=(String)sortMap.get(colsIndex.get(k));
                        		value+=(value.equals("")?"":Config.SPLIT_SYMBOL_COMMA) + 
                        			(sortMap.containsKey(colsIndex.get(k))==true?(cellValue==null?DEFAULTNUMBERVALUE:cellValue):" ");
	                            /*
	                            //ȡmap�ļ�ֵ��                        	
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
	                                
	                                //���һ�����ݲ��Ӷ���
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
       
                        /** ΪT2DataԪ�����Value ��Ԫ�ص�ֵ�� һ�м�¼��ֵ*/ 
                        createT2QDElement(repDatas_Element,String.valueOf(i+1),"1",value);
                        
                        /*if(!value.equals("")){
                        	Element t2Data_Element = repDatas_Element.addElement("T2Data");
                        	//ΪT2DataԪ�����RowID��Ԫ�ص�ֵ���к� 
                            Element rowId_Element = t2Data_Element.addElement("RowID");
                            rowId_Element.setText(String.valueOf(i+1));
                            //ΪT2DataԪ�����region
                            Element region_Element = t2Data_Element.addElement("region");
                            region_Element.setText("1");
                            //��ֵ
                        	Element value_Element = t2Data_Element.addElement("Value");
                        	value_Element.setText(value);
                        }*/
                    }else{	//ǰ15�����ݸ�Ϊ��Ե�ʽ�ӿ���ʽ                        
                        for(int k=0;k<reportData.size();k++)
                        {   
                            if(((P2P_Report_Data)reportData.get(k)).getCellRow().equals(((Integer)rowIds.get(i)).toString()))
                            {
                                /**ѭ��ΪRepDatasԪ�����T1DataԪ��  ÿ��T1DataԪ�ر�ʾһ����Ԫ���¼*/
                                                               
                                /**ΪT1DataԪ�����CellRow ��Ԫ�ص�ֵ�� ��Ԫ���к�*/ 
                                String cellRow  = ((P2P_Report_Data)reportData.get(k)).getCellRow();
                               
                                /**ΪT1DataԪ�����CellCol ��Ԫ�ص�ֵ�� ��Ԫ���к�*/ 
                                String cellCol = ((P2P_Report_Data)reportData.get(k)).getCellCol();
                            
                                /** ΪT1DataԪ�����CellID ��Ԫ�ص�ֵ�� ��Ԫ��ֵ*/ 
                                String cellValue = ((P2P_Report_Data)reportData.get(k)).getValue();
                                String cellName = ((P2P_Report_Data)reportData.get(k)).getCellName();
                                
                                if(cellValue!=null){
                                    /**�жϸ����Ƿ��ǰٷ����У���������100*/
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
            /**дxml�ļ�*/  
            this.writeXml(document,report);
        }
    }
   
    /**
     * Ϊ��Ԫ�����Report Ԫ�أ����а���һЩ����Ļ�������
     * @param root_Element Element ��Ԫ��
     * @param report  ReportInForm  �����ñ����һЩ��������
     */
    private void addReportElement(Element root_Element,ReportIn report)
    {
        if(root_Element!=null && report!=null)
        {
            /**Ϊ��Ԫ�����ReportԪ��*/        
            Element report_Element = root_Element.addElement("Report");
            
            /**ΪReportԪ�����SubRepID����  �����Ե�ֵ�� �ӱ���id*/
            if(report.getMChildReport().getComp_id().getChildRepId()!=null)
                report_Element.addAttribute("SubRepID",report.getMChildReport().getComp_id().getChildRepId().trim());
            else
                report_Element.addAttribute("SubRepID","");
            
            /**ΪReportԪ�����Version����  �����Ե�ֵ�� �汾��*/
            if(report.getMChildReport().getComp_id().getVersionId()!=null)
                report_Element.addAttribute("Version",report.getMChildReport().getComp_id().getVersionId().trim());
            else
                report_Element.addAttribute("Version","");
            
            /**ΪReportԪ�����RepDate����  �����Ե�ֵ�� �ϱ�ʱ��*/
            if(report.getReportDate()!=null)
                report_Element.addAttribute("RepDate",DateFormat.format(report.getReportDate()).trim());
            else
                report_Element.addAttribute("RepDate","");
            
            /**ΪReportԪ�����OrgID����  �����Ե�ֵ�� ����id*/
            if(report.getOrgId()!=null)
                report_Element.addAttribute("OrgID",report.getOrgId().trim());
            else
                report_Element.addAttribute("OrgID","");
            
            /**ΪReportԪ�����RepYear���� �����Ե�ֵ�� �������*/
            if(report.getYear()!=null)
                report_Element.addAttribute("RepYear",report.getYear().toString().trim());
            else
                report_Element.addAttribute("RepYear","");
            
            /**ΪReportԪ�����SubRepID���� �����Ե�ֵ�� ��������*/
            if(report.getTerm()!=null)
                report_Element.addAttribute("RepMonth",report.getTerm().toString().trim());
            else
                report_Element.addAttribute("RepMonth","");
            
            /**ΪReportԪ�����DataRange����  �����Ե�ֵ�� ���ݷ�Χid*/
            if(report.getMDataRgType().getDataRangeId()!=null)
                report_Element.addAttribute("DataRange",report.getMDataRgType().getDataRangeId().toString().trim());
            else
                report_Element.addAttribute("DataRange","");
            
            /**ΪReportԪ�����CurrencyID����  �����Ե�ֵ�� ����id*/
            if(report.getMCurr().getCurId()!=null)
                report_Element.addAttribute("CurrencyID",report.getMCurr().getCurId().toString().trim());
            else
                report_Element.addAttribute("CurrencyID","");
           
            /**ΪReportԪ�����Area����  �����Ե�ֵ�� ��*/
            report_Element.addAttribute("Area","");
        }
    } 
    /**
     * ���ݴ��������ļ���д��Ӧ���ļ�
     * @param fileName
     */
    private void writeXml(Document document ,ReportIn reportIn) throws Exception
    {
        /**ʵ�ʱ���id*/
        String reportInId = reportIn.getRepInId().toString();
        /**�ӱ���id*/
        String childRepId = reportIn.getMChildReport().getComp_id().getChildRepId();
        /**�汾��*/
        String versionId = reportIn.getMChildReport().getComp_id().getVersionId();
        /**�ӱ�����*/
        String reportName =  reportIn.getMChildReport().getReportName();
        /**��������*/
        String orgName =StrutsMOrgDelegate.selectOne(reportIn.getOrgId()).getOrg_name();
        /**����ʱ��*/
        Date repDate = reportIn.getReportDate();
        String reportDate ="";
        if(repDate!=null)
            reportDate = FORMAT.format(repDate); 
        try 
        {
            XMLWriter writer = null;
            /** ��ʽ�����*/
            OutputFormat format = OutputFormat.createPrettyPrint();
            /** ָ��XML���� */
            format.setEncoding("GBK");
       
            String fileName= fileSavePath +Config.FILESEPARATOR + reportInId + "-" + childRepId + ".xml";
            
            writer= new XMLWriter(new FileWriter(new File(fileName)),format);
                        
            writer.write(document);
            writer.close();
            if(db2XmlHandler.setReportDataWarehouseFlag(Integer.valueOf(reportInId))==false)
            {  
                FitechLog.writeLog(Config.LOG_SYSTEM_CREATESTORAGEXML,Config.SYSTEM_OPERATOR,"���ñ������ݱ�־ʧ�ܣ�����������"+reportName+",�汾�ţ�"+versionId+",���ͻ�����"+orgName+",����ʱ�䣺"+reportDate+"��");
                //FitechLog.writeLog(Config.LOG_SYSTEM_CREATESTORAGEXML,"ϵͳ","���ñ������ݱ�־ʧ�ܣ�(��ţ�"+reportId+")"); 
                /**��������1*/
                failReportNum++;
            }
            else
            {
                //FitechLog.writeLog(Config.LOG_SYSTEM_CREATESTORAGEXML,"ϵͳ","���ɲֿ��ļ��ɹ�������������"+reportName+",�汾�ţ�"+versionId+",���ͻ�����"+orgName+",����ʱ�䣺"+reportDate+"��");
                /**��ȷ����1*/
                successReportNum++;  
            }  
        } 
        catch (Exception e) 
        {
            FitechLog.writeLog(Config.LOG_SYSTEM_CREATESTORAGEXML,"ϵͳ","����xml�ļ�ʧ�ܣ�����������"+reportName+",�汾�ţ�"+versionId+",���ͻ�����"+orgName+",����ʱ�䣺"+reportDate+"��");
            //FitechLog.writeLog(Config.LOG_SYSTEM_CREATESTORAGEXML,"ϵͳ","����xml�ļ�ʧ�ܣ�(��ţ�"+reportId+")");
            /**��������1*/
            failReportNum++;
            log.printStackTrace(e);
        }
        
    }
    
    /**
     * ����T2���͵��嵥���ֵ�XML������
     * 
     * @author rds
     * @date 2006-01-10
     * 
     * @param parentElement Element ��Ԫ����
     * @param rowId String �к�
     * @param region String ����������
     * @param value String ����ֵ
     * @return Element ����ʧ�ܣ�����null
     */
    private void createT2QDElement(Element parentElement,String rowId,String region,String value){
    	if(parentElement==null || rowId==null || region==null || value==null) return;
    	if(rowId.equals("") || region.equals("") || value.equals("")) return;
    	
    	try{
    		Element t2DataElement=parentElement.addElement("T2Data");                        
            //�к�Ԫ�� 
            Element rowIdElement = t2DataElement.addElement("RowID");
            rowIdElement.setText(rowId.trim());
            //����������Ԫ��
            Element regionElement = t2DataElement.addElement("region");
            regionElement.setText(region.trim());
            //����ֵԪ��
        	Element valueElement = t2DataElement.addElement("Value");
        	valueElement.setText(value.trim());
    	}catch(Exception e){
       		log.printStackTrace(e);
    	}
    }
    
    /**
     * ����T2���͵ĵ�Ե㲿�ֵ�XML������
     * 
     * @author rds
     * @date 2006-01-10
     * 
     * @param parentElement Element ��Ԫ����
     * @param cellRow String ��Ԫ����к�
     * @param cellCol String ��Ԫ����к�
     * @param cellValue String ����ֵ
     * @return Element ����ʧ�ܣ�����null
     */
    private void createT2DDElement(Element parentElement,String cellRow,String cellCol,String cellValue){
    	if(parentElement==null || cellRow==null || cellCol==null || cellValue==null) return;
    	if(cellRow.equals("") || cellCol.equals("") || cellValue.equals("")) return;
    	
    	try{
    		//������
    		Element t2DataElement = parentElement.addElement("T2Data");
    		//��Ԫ������
        	Element cellRowElement = t2DataElement.addElement("CellRow");
        	cellRowElement.setText(cellRow.trim());
        	//��Ԫ������
        	Element cellColElement = t2DataElement.addElement("CellCol");
        	cellColElement.setText(cellCol.trim());
        	//����ֵ��
        	Element valueElement = t2DataElement.addElement("Value");
        	valueElement.setText(cellValue);
    	}catch(Exception e){
       		log.printStackTrace(e);
    	}
    }
    
    /**
     * ����T1���͵�XML������
     * 
     * @author rds
     * @date 2006-01-10
     * 
     * @param parentElement Element ��Ԫ����
     * @param cellRow String ��Ԫ����к�
     * @param cellCol String ��Ԫ����к�
     * @param cellValue String ����ֵ
     * @return Element ����ʧ�ܣ�����null
     */
    private void createT1Element(Element parentElement,String cellRow,String cellCol,String cellValue){
    	if(parentElement==null || cellRow==null || cellCol==null || cellValue==null) return;
    	if(cellRow.equals("") || cellCol.equals("") || cellValue.equals("")) return;
    	
    	try{
    		//������
    		Element t2DataElement = parentElement.addElement("T1Data");
    		//��Ԫ������
        	Element cellRowElement = t2DataElement.addElement("CellRow");
        	cellRowElement.setText(cellRow.trim());
        	//��Ԫ������
        	Element cellColElement = t2DataElement.addElement("CellCol");
        	cellColElement.setText(cellCol.trim());
        	//����ֵ��
        	Element valueElement = t2DataElement.addElement("Value");
        	valueElement.setText(cellValue);
    	}catch(Exception e){
       		log.printStackTrace(e);
    	}
    }
}