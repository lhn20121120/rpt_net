package com.fitech.net.common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.cbrc.smis.common.Config;
import com.fitech.net.form.EtlIndexForm;

/**
 * ETL�������ļ�ת���ɿ������ṹ��ʾ��XML
 * 
 * @author wh
 * 
 */
public class ETLXmlToXml {
	 private final static String ETLXML_CFG = "/ETl.xml";
	 public static String fileName=ETLXmlToXml.class.getResource(ETLXML_CFG).getPath();
	/**
	 * ����XML
	 * wh
	 */
	public static List ParserXML(){
		// String fileName = Config.WEBROOTPATH + "xml"+Config.FILESEPARATOR+"ETLSourceFile.xml"; 
		String fileName = ETLXmlToXml.fileName;
	//	String fileName= "D:\\ETL.xml";
		File file = new File(fileName);
		SAXReader saxReader = new SAXReader();
		Document document=null;
		List list=new ArrayList();      	// ���ҵ��ժҪ��ʾ����ʽ
		List list_text=new ArrayList();		// ���ҵ��ժҪ
		List list_id=new ArrayList();		// ���ʾ����ʽ
		List list_help = new ArrayList();   //������ʾ��Ϣ
		try {
			document = saxReader.read(file);
			Element root = document.getRootElement();			
			List vlist = root.selectNodes("//formual/@title");
			Iterator riter = vlist.iterator();
			while (riter.hasNext()) {
				Attribute attribute = (Attribute) riter.next();
				// ȡ����һ������title����ֵ------tree_text
				list_text.add(attribute.getValue());
				Node node = attribute.getParent();
				// ȡ����һ������defaultֵ------tree_id
				list_id.add(node.selectSingleNode("default").getText());
				list_help.add(node.selectSingleNode("helpInfo").getText());
			}
			list.add(list_text);
			list.add(list_id);
			list.add(list_help);
		} catch (DocumentException de) {
			// TODO Auto-generated catch block
			de.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;	
	}
	
	/**
	 * ����һ��XML�ļ�
	 * 
	 */
	public static void CreateETLXml(List list){
		List list_text=new ArrayList();
		List list_id=new ArrayList();
		List list_help = new ArrayList();   //������ʾ��Ϣ
		//		String fileName = "D:\\ETLTest.xml"; 
	 String fileName = Config.WEBROOTPATH + "xml"+Config.FILESEPARATOR+"ETLTree.xml"; // ����һ��XML�ļ�
		list_text=(List)list.get(0);
		list_id=(List)list.get(1);
		list_help=(List)list.get(2);

		/** ����һ��XML */
		org.dom4j.Document document = org.dom4j.DocumentHelper.createDocument();
		document.setXMLEncoding("GBK");	
		Element rootElement = document.addElement("tree");
		
		rootElement.addAttribute("id", "0");
		Element entityElement = rootElement.addElement("item");
		entityElement.addAttribute("text", "��ʽ");
		entityElement.addAttribute("id", "N_1");
		entityElement.addAttribute("open", "1");		
		
		for(int j=0;j<list_text.size();j++){
			Element oneElement = entityElement.addElement("item");
			oneElement.addAttribute("text", list_text.get(j).toString());
			oneElement.addAttribute("id", list_id.get(j).toString());
			oneElement.addAttribute("orgInfo", list_help.get(j).toString());
			oneElement.addAttribute("open", "1");
		}
		try {
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("GBK");
			XMLWriter output = new XMLWriter(
					new FileWriter(new File(fileName)), format);
			output.write(document);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	

	
	/**
	 * ����һ��������ʾָ�깫ʽ��xml
	 *
	 */
	public static void createETLIndexXml(List list){
		if(list==null)return;
		List list_text=new ArrayList();
		List list_id=new ArrayList();
		List list_help = new ArrayList();   //������ʾ��Ϣ

	 String fileName = Config.WEBROOTPATH + "xml"+Config.FILESEPARATOR+"ETL_Index_Tree.xml"; // ����һ��XML�ļ�
		
		/** ����һ��XML */
		org.dom4j.Document document = org.dom4j.DocumentHelper.createDocument();
		document.setXMLEncoding("GBK");	
		Element rootElement = document.addElement("tree");
		
		rootElement.addAttribute("id", "0");
		Element entityElement = rootElement.addElement("item");
		entityElement.addAttribute("text", "ָ�깫ʽ");
		entityElement.addAttribute("id", "N_1");
		entityElement.addAttribute("open", "1");		
		
		for(int i=0;i<list.size();i++){
			EtlIndexForm etlIndexForm=(EtlIndexForm)list.get(i);
			if(etlIndexForm!=null){
				Element oneElement = entityElement.addElement("item");
				String orgInfo=etlIndexForm.getDesc()==null?"û�б�ע��Ϣ!" : etlIndexForm.getDesc();
				oneElement.addAttribute("text", etlIndexForm.getIndexName());
				oneElement.addAttribute("id","[ETLIndex:"+etlIndexForm.getIndexName()+"]");
				oneElement.addAttribute("orgInfo", orgInfo);
				oneElement.addAttribute("open", "1");
			}	
		}
		try {
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("GBK");
			XMLWriter output = new XMLWriter(
					new FileWriter(new File(fileName)), format);
			output.write(document);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
	}
	public static void main(String arg[]){		
		ETLXmlToXml etl = new ETLXmlToXml();
		List list=etl.ParserXML();
		CreateETLXml(list);
		
		
	}
	
	
}
