package com.fitech.net.Excel2Xml;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.dom4j.Document;

import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.fitech.net.config.Config;


public class Excel2XML {
	/**
	 * excel生成xml
	 * @param excel excel绝对路径
	 * @param year
	 * @param month
	 * @param xmlpath xml存放路径
	 * @param orgCode 机构编码
	 */
	public static boolean excel2xml(String excel,String orgCode,String versionId,String fileName){
		boolean re=false;

		Name xmlname=new Name();
		xmlname.setOrgCode(orgCode);
		xmlname.setVersionid(versionId);
		xmlname.setPath(Config.REAL_ROOT_PATH+Config.REPORT_NAME+File.separator+Config.SERVICE_UP_TEMP+Config.SERVICE_UP_XML+File.separator+orgCode+File.separator);
		xmlname.setXmlName(fileName);

		//获得报表的大标题
		String title="";
		try{
			
			title = new Util().getTitle(excel);
		}catch(Exception ex){
			return false;
		}
		/** 报表类型 (分支1 法人0) */
		Integer template_type = com.fitech.net.config.Config.FR_TEMPLATE ;
		
		if(title != null && !title.equals("") && title.length()>2){
			
			String titleStr=title.trim().substring(0,2);
			if(titleStr.toUpperCase().indexOf(com.fitech.net.config.Config.FZ_SF_TAMPLATE)>-1  ||
					titleStr.toUpperCase().indexOf(com.fitech.net.config.Config.FZ_GF_TAMPLATE)>-1){
				template_type = com.fitech.net.config.Config.FZ_TEMPLATE;
			}
			
		}
		if(template_type.equals( com.fitech.net.config.Config.FZ_TEMPLATE)){
			
		}
		
		
		String subtitle=new Util().getSubtitle(excel);
		String version=StrutsMChildReportDelegate.getVersionIdByTitle(title, subtitle);
		if(!version.equals("")){
			xmlname.setVersionid(version);
		}
		
		//处理拆分表单
		List sprepList=Util.getSpRepBean();
		for(int i=0;i<sprepList.size();i++){
			SpecialRepBean sp=(SpecialRepBean)sprepList.get(i);
			//判断是否为拆分报表
			if(title.equals(sp.getTitle())){
				List partList=sp.getPartList();
				
				for(int j=0;j<partList.size();j++){
					Part part=(Part)partList.get(j);
					if(part.getSubtitle().equals(subtitle)){
						if(part.getId().substring(part.getId().indexOf("_")+1).equals(versionId)){
							xmlname.setRepId(part.getId().substring(0,part.getId().indexOf("_")));
							Document document=P2P2Xml.excel2xml_special(excel,xmlname,part);
							P2P2Xml.month = P2P2Xml.month.trim().length() < 2 ? "0"+P2P2Xml.month.trim() : P2P2Xml.month.trim();
							xmlname.setTime(P2P2Xml.year+P2P2Xml.month);
							if(document == null) return false;
							makexml(document,xmlname.getXmlName());
							re=true;
							return re;
						}						
					}
				}
			};
		}
		
		//处理没有合计的清单式
		List billList=Util.getBillBean();
		for(int i=0;i<billList.size();i++){
			BillBean bill=(BillBean)billList.get(i);
			if(bill.getTitle().equals(title)){
				Document document=BillExcel2Xml.getGBill(bill,excel,xmlname);
				if(document == null) return false;
				BillExcel2Xml.month = BillExcel2Xml.month.trim().length() < 2 ? "0"+BillExcel2Xml.month.trim() : BillExcel2Xml.month.trim();
				xmlname.setTime(BillExcel2Xml.year+BillExcel2Xml.month);
				makexml(document,xmlname.getXmlName());
				return true;
			}		
		}
		
		//处理有合计的清单式
		List bill_1List=Util.getBill_1Bean();
		for(int i=0;i<bill_1List.size();i++){
			Bill_1Bean bill_1=(Bill_1Bean)bill_1List.get(i);
		
			if(bill_1.getTitle().equals(title)&&
					(bill_1.getSubtitle()==null||bill_1.getSubtitle().equals("")||bill_1.getSubtitle().equals(subtitle))){
				Document document=BillExcel2Xml.getBill_1(bill_1,excel,xmlname);
				if(document == null) return false;
				BillExcel2Xml.month = BillExcel2Xml.month.trim().length() < 2 ? "0"+BillExcel2Xml.month.trim() : BillExcel2Xml.month.trim();
				xmlname.setTime(BillExcel2Xml.year+BillExcel2Xml.month);
				makexml(document,xmlname.getXmlName());
				return true;
			}		
		}
		//处理一些非常特殊的报表
		if(template_type.equals(com.fitech.net.config.Config.FR_TEMPLATE)){
		
			if(subtitle.equals("补充材料：  第五部分：信托资金投资有价证券风险情况表")){
				Document document=BillExcel2Xml.getS3208(excel,xmlname);
				if(document == null) return false;
				BillExcel2Xml.month = BillExcel2Xml.month.trim().length() < 2 ? "0"+BillExcel2Xml.month.trim() : BillExcel2Xml.month.trim();
				xmlname.setTime(BillExcel2Xml.year+BillExcel2Xml.month);
				makexml(document,xmlname.getXmlName());
				return true;
			}
			//if(subtitle.equals("附注2：债券托管及证券交易保证金存放情况表")){   //alter by jcm
			if(subtitle.equals("补充材料：第五部分：信托资金投资有价证券风险情况表(附注2)")){
				Document document=BillExcel2Xml.getS3209(excel,xmlname);
				//Document document=Excel2Xml4S3209.getS3209(excel,xmlname);
				if(document == null) return false;
				BillExcel2Xml.month = BillExcel2Xml.month.trim().length() < 2 ? "0"+BillExcel2Xml.month.trim() : BillExcel2Xml.month.trim();
				xmlname.setTime(BillExcel2Xml.year+BillExcel2Xml.month);
				makexml(document,xmlname.getXmlName());
				return true;
			}
			if(subtitle.equals("第一部分：信托投资公司关联交易风险状况(2)")){
				Document document=BillExcel2Xml.getS3402(excel,xmlname);
				if(document == null) return false;
				BillExcel2Xml.month = BillExcel2Xml.month.trim().length() < 2 ? "0"+BillExcel2Xml.month.trim() : BillExcel2Xml.month.trim();
				xmlname.setTime(BillExcel2Xml.year+BillExcel2Xml.month);
				makexml(document,xmlname.getXmlName());
				return true;
			}
			if(subtitle.equals("第二部分:信托投资公司最大十家客户（含集团客户）融资集中度情况表")){
				Document document=BillExcel2Xml.getS3403(excel,xmlname);
				if(document == null) return false;
				BillExcel2Xml.month = BillExcel2Xml.month.trim().length() < 2 ? "0"+BillExcel2Xml.month.trim() : BillExcel2Xml.month.trim();
				xmlname.setTime(BillExcel2Xml.year+BillExcel2Xml.month);
				makexml(document,xmlname.getXmlName());
				return true;
			}
			if(title.equals("S36债券托管及证券交易保证金存放情况表")){
				Document document=BillExcel2Xml.getS3600(excel,xmlname);
				if(document == null) return false;
				BillExcel2Xml.month = BillExcel2Xml.month.trim().length() < 2 ? "0"+BillExcel2Xml.month.trim() : BillExcel2Xml.month.trim();
				xmlname.setTime(BillExcel2Xml.year+BillExcel2Xml.month);
				makexml(document,xmlname.getXmlName());
				return true;
			}
		}
		Document document=P2P2Xml.excel2xml_common(excel,xmlname,0,template_type,subtitle);
		if(document == null) return false;
		P2P2Xml.month = P2P2Xml.month.trim().length() < 2 ? "0"+P2P2Xml.month.trim() : P2P2Xml.month.trim();		
		xmlname.setTime(P2P2Xml.year+P2P2Xml.month);		
		makexml(document,xmlname.getXmlName()); 
		re=true;
		return re;
	}

	/**
	 * 生成xml
	 * @param xmlurl
	 * 生成xml的绝对路径
	 */
	private static void makexml(Document document, String xmlurl) {
		try {
			String xmlStr = document.asXML();
			File file = new File(xmlurl);
			Config.FILENAME = file.getName();
			OutputStream out = new DataOutputStream(new FileOutputStream (file));
			StringBuffer sb = new  StringBuffer();
			sb.append(xmlStr);			
			out.write(sb.toString().getBytes("UTF-8"));
			out.flush();
			out.close();			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	

	} 
}
