package com.cbrc.smis.util;
import java.io.File;
import java.util.Iterator;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.cbrc.smis.common.Config;


public class ReadXMLFile {
	private Document document = null;

	private String dbConfigPath = Config.WEBROOTPATH + "xml" + File.separator + "sharepath.xml";

	/*
	 * 获得数据库类型
	 */
	public String getSharePath(){
		//生成节点对象
		if(this.document == null){
			SAXReader saxReader = new SAXReader();
			try {
				this.document = saxReader.read(new File(dbConfigPath));
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}
		Element root = this.document.getRootElement();
		
		String dbType = "";
		for(Iterator i = root.elementIterator(); i.hasNext();){ 
			Element element = (Element)i.next(); 
			if(element.getName().equals("PATH")){
				dbType = element.getText();
			}
		}
		return dbType;
	}
}
