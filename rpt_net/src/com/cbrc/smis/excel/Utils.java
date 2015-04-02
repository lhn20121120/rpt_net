package com.cbrc.smis.excel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cbrc.smis.util.FitechException;

/**
 * 工具类
 * 
 * @author rds
 * @date 2006-04-24
 */
public class Utils {
	private static FitechException log=new FitechException(Utils.class);
	/**
	 * Bean名称
	 */
	private static String BEANNAME="item";
	private static String BEANNAME_SUBREPORT1="subReport1";
	private static String BEANNAME_SUBREPORT2="subReport2";
	private static String BEANNAME_SUBREPORT3="subReport3";
	/**
	 * 格式记录列表
	 * 
	 * @param records List 记录信息列表
	 * @param childRepId String 报表编号
	 * @return Map
	 */
	public static Map formatRecords(List records,String childRepId){
		HashMap beans=new HashMap();
		List resList=null;
		
		if(childRepId==null) return null;
		
		childRepId=childRepId.trim().toUpperCase();
		
		try{
			if(childRepId.equals("G5200")){
				resList=formatG5200Records(records);
				beans.put(BEANNAME,resList);
			}else if(childRepId.equals("G5100")){
				
			}else if(childRepId.equals("S3201")){
				resList=formatS3201Records(records);
				beans.put(BEANNAME,resList);
			}else if(childRepId.equals("S3202")){
				resList=formatS3202Records(records);
				beans.put(BEANNAME,resList);
			}else if(childRepId.equals("S3203")){
				resList=formatS3203Records(records);
				beans.put(BEANNAME,resList);
			}else if(childRepId.equals("S3204")){
				resList=formatS3204Records(records);
				beans.put(BEANNAME,resList);
			}else if(childRepId.equals("S3205")){
				resList=formatS3205Records(records);
				beans.put(BEANNAME,resList);
			}else if(childRepId.equals("S3206")){
				resList=formatS3206Records(records);
				beans.put(BEANNAME,resList);
			}else if(childRepId.equals("S3207")){
				resList=formatS3207Records(records);
				beans.put(BEANNAME,resList);
			}else if(childRepId.equals("S3209")){
				resList=formatS3209Records(records);
				beans.put(BEANNAME_SUBREPORT1,resList.get(0));
				beans.put(BEANNAME_SUBREPORT2,resList.get(1));
				beans.put(BEANNAME_SUBREPORT3,resList.get(2));
			}else if(childRepId.equals("S3300")){
				resList=formatS3300Records(records);
				beans.put(BEANNAME,resList);
			}else if(childRepId.equals("S3401")){
				resList=formatS3401Records(records);
				beans.put(BEANNAME,resList);
			}else if(childRepId.equals("S3402")){
				resList=formatS3402Records(records);
				beans.put(BEANNAME_SUBREPORT1,resList.get(0));
				beans.put(BEANNAME_SUBREPORT2,resList.get(1));
			}else if(childRepId.equals("S3500")){
				resList=formatS3500Records(records);
				beans.put(BEANNAME,resList);
			}else if(childRepId.equals("S3600")){
				resList=formatS3600Records(records);
				beans.put(BEANNAME_SUBREPORT1,resList.get(0));
				beans.put(BEANNAME_SUBREPORT2,resList.get(1));
				beans.put(BEANNAME_SUBREPORT3,resList.get(2));
			}else if(childRepId.equals("S3700")){
				resList=formatS3700Records(records);
				beans.put(BEANNAME,resList);
			}else if(childRepId.equals("S3800")){
				resList=formatS3800Records(records);
				beans.put(BEANNAME,resList);
			}else if(childRepId.equals("S4400")){
				resList=formatS4400Records(records);
				beans.put(BEANNAME,resList);
			}
		}catch(Exception e){
			beans=null;
			log.printStackTrace(e);
		}

		if(beans==null) beans=new HashMap();
			
		return beans;
	}
	
	/**
	 * 格式G5200报表记录列表
	 * 
	 * @param records List 记录列表
	 * @exception Exception
	 * @return List
	 */
	private static List formatG5200Records(List records) throws Exception{
		ArrayList resList=null;
		
		resList=new ArrayList();
		
		if(records!=null){					
			List list=null;
			for(int i=0;i<records.size();i++){
				list=(List)records.get(i);
				QDRecord record=new QDRecord();
				record.setCol1(getValue(list.get(0)));
				record.setCol2(getValue(list.get(1)));
				record.setCol3(getValue(list.get(2)));
				record.setCol4(getValue(list.get(3)));
				record.setCol5(getValue(list.get(4)));
				record.setCol6(getValue(list.get(5)));
				record.setCol7(getValue(list.get(6)));
				
				resList.add(record);
			}
		}
		
		init(resList);
		
		return resList;
	}
	
	/**
	 * 格式S3201报表记录列表
	 * 
	 * @param records List 记录列表
	 * @exception Exception
	 * @return List
	 */
	private static List formatS3201Records(List records) throws Exception{
		ArrayList resList=null;
		
		resList=new ArrayList();
		
		if(records!=null){							
			List list=null;
			for(int i=0;i<records.size();i++){
				list=(List)records.get(i);
				QDRecord record=new QDRecord();
				record.setCol1(getValue(list.get(0)));
				record.setCol2(getValue(list.get(1)));
				record.setCol3(getValue(list.get(2)));
				record.setCol4(getValue(list.get(3)));
				record.setCol5(getValue(list.get(4)));
				record.setCol6(getValue(list.get(5)));
				record.setCol7(getValue(list.get(6)));
				record.setCol8(getValue(list.get(7)));
				record.setCol9(getValue(list.get(8)));
				record.setCol10(getValue(list.get(9)));
				record.setCol11(getValue(list.get(10)));
				record.setCol12(getValue(list.get(11)));
				record.setCol13(getValue(list.get(12)));
				record.setCol14(getValue(list.get(13)));
				record.setCol15(getValue(list.get(14)));
				record.setCol16(getValue(list.get(15)));
				record.setCol17(getValue(list.get(16)));
				record.setCol18(getValue(list.get(17)));
				record.setCol19(getValue(list.get(18)));
				record.setCol20(getValue(list.get(19)));
				record.setCol21(getValue(list.get(20)));
				record.setCol22(getValue(list.get(21)));
				record.setCol23(getValue(list.get(22)));
				record.setCol24(getValue(list.get(23)));
				record.setCol25(getValue(list.get(24)));
				record.setCol26(getValue(list.get(25)));
				record.setCol27(getValue(list.get(26)));
				record.setCol28(getValue(list.get(27)));
				record.setCol29(getValue(list.get(28)));
				record.setCol30(getValue(list.get(29)));
				record.setCol31(getValue(list.get(30)));
				record.setCol32(getValue(list.get(31)));
				record.setCol33(getValue(list.get(32)));
				record.setCol34(getValue(list.get(33)));
				record.setCol35(getValue(list.get(34)));
				record.setCol36(getValue(list.get(35)));
				record.setCol37(getValue(list.get(36)));
				record.setCol38(getValue(list.get(37)));
				record.setCol39(getValue(list.get(38)));
				record.setCol40(getValue(list.get(39)));
				record.setCol41(getValue(list.get(40)));
				record.setCol42(getValue(list.get(41)));
				record.setCol43(getValue(list.get(42)));
				record.setCol44(getValue(list.get(43)));
				record.setCol45(getValue(list.get(44)));
				record.setCol46(getValue(list.get(45)));
				record.setCol47(getValue(list.get(46)));
				resList.add(record);
			}
		}
		
		init(resList);
		
		return resList;
	}
	
	/**
	 * 格式S3202报表记录列表
	 * 
	 * @param records List 记录列表
	 * @exception Exception
	 * @return List
	 */
	private static List formatS3202Records(List records) throws Exception{
		ArrayList resList=null;
		
		resList=new ArrayList();
		
		if(records!=null){					
			List list=null;
			for(int i=0;i<records.size();i++){
				list=(List)records.get(i);
				QDRecord record=new QDRecord();
				record.setCol1(getValue(list.get(0)));
				record.setCol2(getValue(list.get(1)));
				record.setCol3(getValue(list.get(2)));
				record.setCol4(getValue(list.get(3)));
				record.setCol5(getValue(list.get(4)));
				record.setCol6(getValue(list.get(5)));
				record.setCol7(getValue(list.get(6)));
				record.setCol8(getValue(list.get(7)));
				record.setCol9(getValue(list.get(8)));
				record.setCol10(getValue(list.get(9)));
				record.setCol11(getValue(list.get(10)));
				record.setCol12(getValue(list.get(11)));
				record.setCol13(getValue(list.get(12)));
				record.setCol14(getValue(list.get(13)));
				record.setCol15(getValue(list.get(14)));
				record.setCol16(getValue(list.get(15)));
				record.setCol17(getValue(list.get(16)));
				record.setCol18(getValue(list.get(17)));
				record.setCol19(getValue(list.get(18)));
				record.setCol20(getValue(list.get(19)));
				record.setCol21(getValue(list.get(20)));
				record.setCol22(getValue(list.get(21)));
				record.setCol23(getValue(list.get(22)));
				record.setCol24(getValue(list.get(23)));
				record.setCol25(getValue(list.get(24)));
				record.setCol26(getValue(list.get(25)));
				record.setCol27(getValue(list.get(26)));
				record.setCol28(getValue(list.get(27)));
				record.setCol29(getValue(list.get(28)));
				record.setCol30(getValue(list.get(29)));
				record.setCol31(getValue(list.get(30)));
				record.setCol32(getValue(list.get(31)));
				record.setCol33(getValue(list.get(32)));
				record.setCol34(getValue(list.get(33)));
				record.setCol35(getValue(list.get(34)));
				record.setCol36(getValue(list.get(35)));
				record.setCol37(getValue(list.get(36)));
				record.setCol38(getValue(list.get(37)));
				record.setCol39(getValue(list.get(38)));
				record.setCol40(getValue(list.get(39)));
				record.setCol41(getValue(list.get(40)));
				record.setCol42(getValue(list.get(41)));
				record.setCol43(getValue(list.get(42)));
				resList.add(record);
			}
		}
		
		init(resList);
		
		return resList;
	}
	
	/**
	 * 格式S3203报表记录列表
	 * 
	 * @param records List 记录列表
	 * @exception Exception
	 * @return List
	 */
	private static List formatS3203Records(List records) throws Exception{
		ArrayList resList=null;
		
		resList=new ArrayList();
		
		if(records!=null){					
			List list=null;
			for(int i=0;i<records.size();i++){
				list=(List)records.get(i);
				QDRecord record=new QDRecord();
				record.setCol1(getValue(list.get(0)));
				record.setCol2(getValue(list.get(1)));
				record.setCol3(getValue(list.get(2)));
				record.setCol4(getValue(list.get(3)));
				record.setCol5(getValue(list.get(4)));
				record.setCol6(getValue(list.get(5)));
				record.setCol7(getValue(list.get(6)));
				record.setCol8(getValue(list.get(7)));
				record.setCol9(getValue(list.get(8)));
				record.setCol10(getValue(list.get(9)));
				record.setCol11(getValue(list.get(10)));
				record.setCol12(getValue(list.get(11)));
				record.setCol13(getValue(list.get(12)));
				record.setCol14(getValue(list.get(13)));
				record.setCol15(getValue(list.get(14)));
				record.setCol16(getValue(list.get(15)));
				record.setCol17(getValue(list.get(16)));
				record.setCol18(getValue(list.get(17)));
				record.setCol19(getValue(list.get(18)));
				record.setCol20(getValue(list.get(19)));
				record.setCol21(getValue(list.get(20)));
				record.setCol22(getValue(list.get(21)));
				record.setCol23(getValue(list.get(22)));
				record.setCol24(getValue(list.get(23)));
				record.setCol25(getValue(list.get(24)));
				record.setCol26(getValue(list.get(25)));
				record.setCol27(getValue(list.get(26)));
				record.setCol28(getValue(list.get(27)));
				record.setCol29(getValue(list.get(28)));
				
				resList.add(record);
			}
		}
		
		init(resList);
		
		return resList;
	}
	
	/**
	 * 格式S3204报表记录列表
	 * 
	 * @param records List 记录列表
	 * @exception Exception
	 * @return List
	 */
	private static List formatS3204Records(List records) throws Exception{
		ArrayList resList=null;
		
		resList=new ArrayList();
		if(records!=null){				
			List list=null;
			for(int i=0;i<records.size();i++){
				list=(List)records.get(i);
				QDRecord record=new QDRecord();
				record.setCol1(getValue(list.get(0)));
				record.setCol2(getValue(list.get(1)));
				record.setCol3(getValue(list.get(2)));
				record.setCol4(getValue(list.get(3)));
				record.setCol5(getValue(list.get(4)));
				record.setCol6(getValue(list.get(5)));
				record.setCol7(getValue(list.get(6)));
				record.setCol8(getValue(list.get(7)));
							
				resList.add(record);
			}
		}
		
		init(resList);
		
		return resList;
	}
	
	/**
	 * 格式S3205报表记录列表
	 * 
	 * @param records List 记录列表
	 * @exception Exception
	 * @return List
	 */
	private static List formatS3205Records(List records) throws Exception{
		ArrayList resList=null;
		
		resList=new ArrayList();
		
		if(records!=null){
			List list=null;
			for(int i=0;i<records.size();i++){
				list=(List)records.get(i);
				QDRecord record=new QDRecord();
				record.setCol1(getValue(list.get(0)));
				record.setCol2(getValue(list.get(1)));
				record.setCol3(getValue(list.get(2)));
				record.setCol4(getValue(list.get(3)));
				record.setCol5(getValue(list.get(4)));
				record.setCol6(getValue(list.get(5)));
				record.setCol7(getValue(list.get(6)));
				record.setCol8(getValue(list.get(7)));
				record.setCol9(getValue(list.get(8)));
				record.setCol10(getValue(list.get(9)));
				record.setCol11(getValue(list.get(10)));
				record.setCol12(getValue(list.get(11)));
				record.setCol13(getValue(list.get(12)));
				record.setCol14(getValue(list.get(13)));
				record.setCol15(getValue(list.get(14)));
				record.setCol16(getValue(list.get(15)));
				record.setCol17(getValue(list.get(16)));
				record.setCol18(getValue(list.get(17)));
				record.setCol19(getValue(list.get(18)));
				record.setCol20(getValue(list.get(19)));
				record.setCol21(getValue(list.get(20)));
				
				resList.add(record);
			}
		}
		
		init(resList);
		
		return resList;
	}
	
	/**
	 * 格式S3206报表记录列表
	 * 
	 * @param records List 记录列表
	 * @exception Exception
	 * @return List
	 */
	private static List formatS3206Records(List records) throws Exception{
		ArrayList resList=null;
		
		resList=new ArrayList();
		
		if(records!=null){		
			List list=null;
			for(int i=0;i<records.size();i++){
				list=(List)records.get(i);
				QDRecord record=new QDRecord();
				record.setCol1(getValue(list.get(0)));
				record.setCol2(getValue(list.get(1)));
				record.setCol3(getValue(list.get(2)));
				record.setCol4(getValue(list.get(3)));
				record.setCol5(getValue(list.get(4)));
				record.setCol6(getValue(list.get(5)));
				record.setCol7(getValue(list.get(6)));
				record.setCol8(getValue(list.get(7)));
				record.setCol9(getValue(list.get(8)));
							
				resList.add(record);
			}
		}
		
		init(resList);
		
		return resList;
	}
	
	/**
	 * 格式S3207报表记录列表
	 * 
	 * @param records List 记录列表
	 * @exception Exception
	 * @return List
	 */
	private static List formatS3207Records(List records) throws Exception{
		ArrayList resList=null;
		
		resList=new ArrayList();
		if(records!=null){			
			List list=null;
			for(int i=0;i<records.size();i++){
				list=(List)records.get(i);
				QDRecord record=new QDRecord();
				record.setCol1(getValue(list.get(0)));
				record.setCol2(getValue(list.get(1)));
				record.setCol3(getValue(list.get(2)));
				record.setCol4(getValue(list.get(3)));
				record.setCol5(getValue(list.get(4)));
				record.setCol6(getValue(list.get(5)));
				record.setCol7(getValue(list.get(6)));
				record.setCol8(getValue(list.get(7)));
				record.setCol9(getValue(list.get(8)));
				record.setCol10(getValue(list.get(9)));
				record.setCol11(getValue(list.get(10)));
				record.setCol12(getValue(list.get(11)));
				record.setCol13(getValue(list.get(12)));
				record.setCol14(getValue(list.get(13)));
				record.setCol15(getValue(list.get(14)));
				record.setCol16(getValue(list.get(15)));
				record.setCol17(getValue(list.get(16)));
				record.setCol18(getValue(list.get(17)));
				
				resList.add(record);
			}
		}
		
		init(resList);
		
		return resList;
	}
	
	/**
	 * 格式S3209报表记录列表
	 * 
	 * @param records List 记录列表
	 * @exception Exception
	 * @return List
	 */
	private static List formatS3209Records(List records) throws Exception{
		ArrayList resList=null;
		
		resList=new ArrayList();		

		List list = null;
		List subLst1 = new ArrayList();
		List subLst2 = new ArrayList();
		List subLst3 = new ArrayList();
		
		if(records!=null){
			int index = 0;
			for (int i = 0; i < records.size(); i++) {
				list = (List) records.get(i);
				index = list.get(0) == null ? 0 : Integer.parseInt((String)list.get(0));
	
				QDRecord subRecord = new QDRecord();
				subRecord.setCol1(getValue(list.get(1)));
				subRecord.setCol2(getValue(list.get(2)));
				subRecord.setCol3(getValue(list.get(3)));
				subRecord.setCol4(getValue(list.get(4)));
				subRecord.setCol5(getValue(list.get(5)));
				subRecord.setCol6(getValue(list.get(6)));
				subRecord.setCol7(getValue(list.get(7)));
	
				if (index == 1 || index == 2)
					subRecord.setCol8(getValue(list.get(8)));
	
				switch (index) {
					case 1:
						subLst1.add(subRecord);
						break;
					case 2:
						subLst2.add(subRecord);
						break;
					case 3:
						subLst3.add(subRecord);
						break;
				}
			}
		}
		
		init(subLst1);
		init(subLst2);
		init(subLst3);
		
		resList.add(subLst1);
		resList.add(subLst2);
		resList.add(subLst3);

		return resList;
	}
	
	/**
	 * 格式S3300报表记录列表
	 * 
	 * @param records List 记录列表
	 * @exception Exception
	 * @return List
	 */
	private static List formatS3300Records(List records) throws Exception{
		ArrayList resList=null;
			
		resList=new ArrayList();
		
		if(records!=null){
			List list=null;
			for(int i=0;i<records.size();i++){
				list=(List)records.get(i);
				QDRecord record=new QDRecord();
				record.setCol1(getValue(list.get(0)));
				record.setCol2(getValue(list.get(1)));
				record.setCol3(getValue(list.get(2)));
				record.setCol4(getValue(list.get(3)));
				record.setCol5(getValue(list.get(4)));
				record.setCol6(getValue(list.get(5)));
				record.setCol7(getValue(list.get(6)));
				record.setCol8(getValue(list.get(7)));
				record.setCol9(getValue(list.get(8)));
				record.setCol10(getValue(list.get(9)));
				record.setCol11(getValue(list.get(10)));
				record.setCol12(getValue(list.get(11)));
				record.setCol13(getValue(list.get(12)));
				record.setCol14(getValue(list.get(13)));
				record.setCol15(getValue(list.get(14)));
				record.setCol16(getValue(list.get(15)));
				record.setCol17(getValue(list.get(16)));
				record.setCol18(getValue(list.get(17)));
				record.setCol19(getValue(list.get(18)));
				record.setCol20(getValue(list.get(19)));
				record.setCol21(getValue(list.get(20)));
				record.setCol22(getValue(list.get(21)));
				record.setCol23(getValue(list.get(22)));
				record.setCol24(getValue(list.get(23)));
				record.setCol25(getValue(list.get(24)));
				
				resList.add(record);
			}
		}
		
		init(resList);
		
		return resList;
	}
	
	/**
	 * 格式S3401报表记录列表
	 * 
	 * @param records List 记录列表
	 * @exception Exception
	 * @return List
	 */
	private static List formatS3401Records(List records) throws Exception{
		ArrayList resList=null;
		
		resList=new ArrayList();
		
		if(records!=null){
			List list=null;
			for(int i=0;i<records.size();i++){
				list=(List)records.get(i);
				QDRecord record=new QDRecord();
				record.setCol1(getValue(list.get(0)));
				record.setCol2(getValue(list.get(1)));
				record.setCol3(getValue(list.get(2)));
				record.setCol4(getValue(list.get(3)));
				record.setCol5(getValue(list.get(4)));
				record.setCol6(getValue(list.get(5)));
				record.setCol7(getValue(list.get(6)));
				record.setCol8(getValue(list.get(7)));
				record.setCol9(getValue(list.get(8)));
				record.setCol10(getValue(list.get(9)));
				record.setCol11(getValue(list.get(10)));
				record.setCol12(getValue(list.get(11)));
				record.setCol13(getValue(list.get(12)));
				record.setCol14(getValue(list.get(13)));
				record.setCol15(getValue(list.get(14)));
				record.setCol16(getValue(list.get(15)));
				record.setCol17(getValue(list.get(16)));
				record.setCol18(getValue(list.get(17)));
				record.setCol19(getValue(list.get(18)));
				record.setCol20(getValue(list.get(19)));
				
				resList.add(record);
			}
		}
		
		init(resList);
		
		return resList;
	}
	
	/**
	 * 格式S3402报表记录列表
	 * 
	 * @param records List 记录列表
	 * @exception Exception
	 * @return List
	 */
	private static List formatS3402Records(List records) throws Exception{
		ArrayList resList=null;
		
		resList=new ArrayList();		
		
		List subLst1 = new ArrayList();
		List subLst2 = new ArrayList();
		
		if(records!=null){
			List list = null;
			int index = 0;
			for (int i = 0; i < records.size(); i++) {
				list = (List) records.get(i);
				index = list.get(0) == null ? 0 : Integer.parseInt((String)list.get(0));
	
				QDRecord subRecord = new QDRecord();
				subRecord.setCol1(getValue(list.get(1)));
				subRecord.setCol2(getValue(list.get(2)));
				subRecord.setCol3(getValue(list.get(3)));
				subRecord.setCol4(getValue(list.get(4)));
				subRecord.setCol5(getValue(list.get(5)));
				subRecord.setCol6(getValue(list.get(6)));
				subRecord.setCol7(getValue(list.get(7)));
				subRecord.setCol8(getValue(list.get(8)));
				subRecord.setCol9(getValue(list.get(9)));
				subRecord.setCol10(getValue(list.get(10)));
				subRecord.setCol11(getValue(list.get(11)));
				subRecord.setCol12(getValue(list.get(12)));
				subRecord.setCol13(getValue(list.get(13)));
				
				if (index == 2)
					subRecord.setCol14(getValue(list.get(14)));
	
				switch (index) {
					case 1:
						subLst1.add(subRecord);
						break;
					case 2:
						subLst2.add(subRecord);
						break;
				}
			}
		}
		
		init(subLst1);
		init(subLst2);
		
		resList.add(subLst1);
		resList.add(subLst2);

		return resList;
	}
	
	/**
	 * 格式S3500报表记录列表
	 * 
	 * @param records List 记录列表
	 * @exception Exception
	 * @return List
	 */
	private static List formatS3500Records(List records) throws Exception{
		ArrayList resList=null;
		
		resList=new ArrayList();
		
		if(records!=null){
			List list=null;
			for(int i=0;i<records.size();i++){
				list=(List)records.get(i);
				QDRecord record=new QDRecord();
				record.setCol1(getValue(list.get(0)));
				record.setCol2(getValue(list.get(1)));
				record.setCol3(getValue(list.get(2)));
				record.setCol4(getValue(list.get(3)));
				record.setCol5(getValue(list.get(4)));
				record.setCol6(getValue(list.get(5)));
				record.setCol7(getValue(list.get(6)));
				record.setCol8(getValue(list.get(7)));
				record.setCol9(getValue(list.get(8)));
				record.setCol10(getValue(list.get(9)));
				record.setCol11(getValue(list.get(10)));
				record.setCol12(getValue(list.get(11)));
				record.setCol13(getValue(list.get(12)));
				record.setCol14(getValue(list.get(13)));
				record.setCol15(getValue(list.get(14)));
				record.setCol16(getValue(list.get(15)));
				record.setCol17(getValue(list.get(16)));
				record.setCol18(getValue(list.get(17)));
				record.setCol19(getValue(list.get(18)));
				
				resList.add(record);
			}
		}
		
		init(resList);
		
		return resList;
	}
	
	/**
	 * 格式S3600报表记录列表
	 * 
	 * @param records List 记录列表
	 * @exception Exception
	 * @return List
	 */
	private static List formatS3600Records(List records) throws Exception{
		ArrayList resList=null;
		
		resList=new ArrayList();		
		
		List subLst1 = new ArrayList();
		List subLst2 = new ArrayList();
		List subLst3 = new ArrayList();
		
		if(records!=null){
			List list = null;
			int index = 0;
			for (int i = 0; i < records.size(); i++) {
				list = (List) records.get(i);
				index = list.get(0) == null ? 0 : Integer.parseInt((String)list.get(0));
	
				QDRecord subRecord = new QDRecord();
				subRecord.setCol1(getValue(list.get(1)));
				subRecord.setCol2(getValue(list.get(2)));
				subRecord.setCol3(getValue(list.get(3)));
				subRecord.setCol4(getValue(list.get(4)));
				subRecord.setCol5(getValue(list.get(5)));
				subRecord.setCol6(getValue(list.get(6)));
				subRecord.setCol7(getValue(list.get(7)));
	
				if (index == 1 || index == 2)
					subRecord.setCol8(getValue(list.get(8)));
	
				switch (index) {
					case 1:
						subLst1.add(subRecord);
						break;
					case 2:
						subLst2.add(subRecord);
						break;
					case 3:
						subLst3.add(subRecord);
						break;
				}
			}
		}
		
		init(subLst1);
		init(subLst2);
		init(subLst3);
		
		resList.add(subLst1);
		resList.add(subLst2);
		resList.add(subLst3);

		return resList;
	}
	
	/**
	 * 格式S3700报表记录列表
	 * 
	 * @param records List 记录列表
	 * @exception Exception
	 * @return List
	 */
	private static List formatS3700Records(List records) throws Exception{
		ArrayList resList=null;
		
		resList=new ArrayList();
		
		if(records!=null){
			List list=null;
			for(int i=0;i<records.size();i++){
				list=(List)records.get(i);
				QDRecord record=new QDRecord();
				record.setCol1(getValue(list.get(0)));
				record.setCol2(getValue(list.get(1)));
				record.setCol3(getValue(list.get(2)));
				record.setCol4(getValue(list.get(3)));
				record.setCol5(getValue(list.get(4)));
				record.setCol6(getValue(list.get(5)));
				record.setCol7(getValue(list.get(6)));
				record.setCol8(getValue(list.get(7)));
				
				resList.add(record);
			}
		}
		
		init(resList);
		
		return resList;
	}
	
	/**
	 * 格式S3800报表记录列表
	 * 
	 * @param records List 记录列表
	 * @exception Exception
	 * @return List
	 */
	private static List formatS3800Records(List records) throws Exception{
		ArrayList resList=null;
		
		resList=new ArrayList();
		
		if(records!=null){
			List list=null;
			for(int i=0;i<records.size();i++){
				list=(List)records.get(i);
				QDRecord record=new QDRecord();
				record.setCol1(getValue(list.get(0)));
				record.setCol2(getValue(list.get(1)));
				record.setCol3(getValue(list.get(2)));
				record.setCol4(getValue(list.get(3)));
				record.setCol5(getValue(list.get(4)));
				record.setCol6(getValue(list.get(5)));
				record.setCol7(getValue(list.get(6)));
				record.setCol8(getValue(list.get(7)));
				record.setCol9(getValue(list.get(8)));
				
				resList.add(record);
			}
		}
		
		init(resList);
		
		return resList;
	}
	
	/**
	 * 格式S4400报表记录列表
	 * 
	 * @param records List 记录列表
	 * @exception Exception
	 * @return List
	 */
	private static List formatS4400Records(List records) throws Exception{
		ArrayList resList=null;
		
		resList=new ArrayList();
		
		if(records!=null){
			List list=null;
			for(int i=0;i<records.size();i++){
				list=(List)records.get(i);
				QDRecord record=new QDRecord();
				record.setCol1(getValue(list.get(0)));
				record.setCol2(getValue(list.get(1)));
				record.setCol3(getValue(list.get(2)));
				record.setCol4(getValue(list.get(3)));
				record.setCol5(getValue(list.get(4)));
				record.setCol6(getValue(list.get(5)));
				record.setCol7(getValue(list.get(6)));
				record.setCol8(getValue(list.get(7)));
				record.setCol9(getValue(list.get(8)));
				record.setCol10(getValue(list.get(9)));
				record.setCol11(getValue(list.get(10)));
				record.setCol12(getValue(list.get(11)));
				record.setCol13(getValue(list.get(12)));
				record.setCol14(getValue(list.get(13)));
				
				resList.add(record);
			}
		}
		
		init(resList);
		
		return resList;
	}
	
	/**
	 * 格式化值
	 * 
	 * @param value String 被式化对象
	 * @return String
	 */
	private static String getValue(Object obj){
		if(obj==null) return "";
		return (String)obj;
	}
	
	/**
	 * 将文件名中的汉字转为UTF8编码的串,以便下载时能正确显示另存的文件名.
	 * 
	 * @param s 原文件名
	 * @return String 重新编码后的文件名
	 */
	public static String toUtf8String(String fileName) {
		if(fileName==null) return "";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < fileName.length(); i++) {
			char c = fileName.charAt(i);
			if (c >= 0 && c <= 255) {
				sb.append(c);
			} else {
				byte[] b;
				try {
					b = Character.toString(c).getBytes("utf-8");
				} catch (Exception ex) {
					b = new byte[0];
				}
				for (int j = 0; j < b.length; j++) {
					int k = b[j];
					if (k < 0)
						k += 256;
					//toHexString 以十六进制的无符号整数形式返回一个整数参数的字符串表示形式。
					sb.append("%" + Integer.toHexString(k).toUpperCase());
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * 判断单元格是否是百分比单元格
	 * 
	 * @param cells HashMap 百分比单元格列表
	 * @param cellName String 单元格名称
	 * @return boolean 是，返回true;否则，返回false
	 */
	public static boolean isPercentCell(Map cells,String cell){
		if(cells==null || cell==null) return false;
		
		return cells.containsKey(cell);
	}
	
	/**  
	 * 提供小数位四舍五入处理。
	 *   
	 * @param v 需要四舍五入的数字  
	 * @param scale 小数点后保留几位  
	 * @return String 四舍五入后的结果  
	 */  
	public static String round(String v,int scale){ 
		if(v==null) return "";
		
		try{
			BigDecimal bdFinalVal=new BigDecimal("1.00");
			BigDecimal bdChangeVal=new BigDecimal(v);
		
			return bdChangeVal.divide(bdFinalVal,scale,BigDecimal.ROUND_HALF_UP).toString();
		}catch(Exception e){
			return "";
		}
	} 
	
	/**
	 * 初始化列表
	 * 
	 * @param List List 
	 * @return List
	 */
	private static void init(List list){
		if(list!=null && list.size()>0) return;
		
		if(list==null) list=new ArrayList();
		
		QDRecord record=new QDRecord();
		list.add(record);
	}
	
	/**
	 * 格式化数据
	 * 
	 * @param val String 数据
	 * @return String
	 */
	public static String formatVal(String val){
		String resVal="";
		if(val==null) return resVal;
			
		try{
			int pos=val.indexOf(".");
			if(pos>0){
				String prefVal=val.substring(0,pos);
				String suffVal=val.substring(pos+1);
				
				if(Integer.parseInt(suffVal)==0)
					resVal=prefVal;
				else
					resVal=val;
			}else{
				resVal=val;
			}
		}catch(Exception e){
			resVal= val != null && !val.equals("") ? val : "";
		}
		
		return resVal;
	}
	
	public static void main(String[] args){
		formatVal("123456.000000");
	}
}