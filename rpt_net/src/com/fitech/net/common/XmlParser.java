/*
 * Created on 2006-5-17
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.fitech.net.common;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.ProcessingInstruction;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class XmlParser {
	public static final String ENCODING_GBK = "GBK";
	//encoding gb18030
	public static final String ENCODING_GB18030 = "GB18030";
	//encoding gb2312
	public static final String ENCODING_GB2312 = "GB2312";
	public static final String ENCODING_UTF8 = "UTF-8";

     
    /**
     * 返回xml文档的根节点
     * @param doc
     * @return Element 根节点
     */
    public static final Element getRootElement(Document doc){

        return doc.getRootElement();

     }
    
    /**
     * 判断该节点是否是叶子节点
     * @param ele
     * @return boolean flase不是叶子节点,true是叶子节点
     */
    public static final boolean isLeafNode(Element ele){
       boolean flag = true;
       
       for(int i=0,size=ele.nodeCount(); i<size; i++){
       	  Node node = ele.node(i);
       	  if (node instanceof Element) { 
       		 flag = false;
       		 break;
       	  }
       }
       
       return flag;
    }
    
    /**
     * 遍历元素
     * @param ele
     */
    public static final void treeWalk(Element ele){
    	
    	for(int i=0,size=ele.nodeCount(); i<size; i++){
    		Node node = ele.node(i);
    		
    		if (node instanceof Element) { 
    			// System.out.println(node.getName());
    			treeWalk((Element) node);
    		}
    	}
    }
    
    /**
     * 从文件读取XML，输入文件名，返回XML文档
     * @param fileName
     * @return Document  解析后的xml文档结构
     */
    public static Document parseXml(String fileName){
    	Document doc = null;
// System.out.println("fileName = "+ fileName);    	
        SAXReader reader = new SAXReader();
		try {
			doc = reader.read(fileName);
		}  catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	    	
    	return doc;
    }
    
    
    /**
     * 格式化XML文档,并解决中文问题
     * @param document
     * @param fileName
     * @return
     */
    public static int saveXml(Document document, String fileName){
    	int returnValue = 0;
    	try{
            XMLWriter output = null;
            /** 格式化输出,类型IE浏览一样 */
            OutputFormat format = OutputFormat.createPrettyPrint();

            /** 指定XML字符集编码 */            
            output = new XMLWriter(new FileWriter(new File(fileName)),format);
            output.write(document);
            output.close();        

            /** 执行成功,需返回1 */
            returnValue = 1;    
    		
    	}catch(Exception e){
    		// System.out.println("Exception Path is com.sisytech.comm.xml.Util.parseXml :" + e.getMessage());
    		e.printStackTrace();
    	}
    	return returnValue;
    }

    /**
     * 格式化XML文档,并按指定字符集输出
     * @param document
     * @param fileName
     * @return 返回操作结果, 0表失败, 1表成功
     */
    public static int saveXml(Document document, 
    								String fileName,
									String encoding){
    	int returnValue = 0;
    	//saveXml(document, fileName);
        /*
         * 将中文字符集统一成gb18030
         */
//    	if (Util.ENCODING_GBK.equals(encoding.toUpperCase()) || 
//    			Util.ENCODING_GB2312.equals(encoding.toUpperCase())) {
//    		encoding = Util.ENCODING_GB18030;
//    	}
    	try{
            XMLWriter output = null;
            /** 格式化输出,类型IE浏览一样 */
            OutputFormat format = OutputFormat.createPrettyPrint();

            /** 指定XML字符集编码 */
            format.setEncoding(encoding);
            //output = new XMLWriter(new  FileWriter(new File(fileName)),format);
            output = new XMLWriter(new FileOutputStream(new File(fileName)),format);
            output.write(document);
            output.close();        

            /** 执行成功,需返回1 */
            returnValue = 1;    
    		
    	}catch(Exception e){
    		// System.out.println("Exception Path is com.sisytech.comm.xml.Util.parseXml :" + e.getMessage());
    		e.printStackTrace();
    	}
    	return returnValue;
    }
    
    /**
     * 得到某一属性
     * @param doc
     * @param path
     * @return Attribute
     */
    public static Attribute getAttribute(Document doc, String path){   	
    	
    	Attribute objAttribute = (Attribute)doc.selectSingleNode(path);
    	
    	return objAttribute;
    }
    
    
    /**
     *  
     * TODO 本方法的是获得某个节点中属性为name的属性值
     * 
     * @author wunaigang(2005-4-10)	
     * @param ele
     * @param name
     * @return　返回操作结果
     */
    public static String getAttributeValue(Element ele, String name){
    	String attrValue = null;
    	Attribute attribute = ele.attribute(name);

    	if (attribute != null) {    		
    		attrValue = attribute.getValue();
    	}
    	
    	return attrValue;
    }
    
    /**
     * 修改元素属性值
     * @param ele 要修改属性的元素
     * @param name 属性名
     * @param value 要修改的值
     * @return 返回操作结果, 0表失败, 1表成功
     */
    public static int modifyAttribute(Element ele, 
    							      String name,  
								      String value){
    	int returnValue = 0;
    	Attribute attribute = ele.attribute(name);

    	if (attribute != null) {
    		attribute.setText(value);
    		returnValue = 1;
    	}
    	
    	return returnValue;
    }
    
    /**
     * 增加节点,appendEle 作为mainEle的子节点。
     * @param mainEle
     * @param appendEle
     */
    public static final void uniteAfter(Element mainEle, Element appendEle){
    	mainEle.add(appendEle);    
    }
    
    /**
     *  
     * TODO 本方法的是将childEle加到parentElem之中成为其子节点，并且成为其第一个子节点
     * 
     * @author wunaigang(2005-5-6)	
     * @param childEle
     * @param brother
     */
    public static final void insertBefore(Element childEle, 
    									 Element parentEle)
    									 throws Exception{    	
    	parentEle.add(childEle);
    	List list = parentEle.elements();  
    	
    	Element ele = null;
    	Element tmpEle = null;
    	
    	for(int i=0,n=list.size();i<n-1;i++){
    		ele = (Element)list.get(i);    		
    		tmpEle = (Element)ele.clone();    		
    		parentEle.add(tmpEle);
    		parentEle.remove(ele);
    	}    	
    }
    
    /**
     * 增加节点,appendEle 作为mainEle的父节点。即替换mainEle节点
     * 原有位置，将mainEle作为appendEle的子节点
     * @param mainEle
     * @param appendEle
     * @return 成功返回１１１1　失败０0
     */
    public static final int uniteBefore(Element mainEle, Element appendEle){
    	int returnValue = 0;
    	if (!mainEle.isRootElement()) {
    		Element parentEle = mainEle.getParent();
    		parentEle.add(appendEle);
    		Element cloneEle = (Element)mainEle.clone();
    		appendEle.add(cloneEle);
    		if (parentEle.remove(mainEle)) {
    			returnValue = 1;	
    		}else{
    			returnValue = 0;	
    		}    		
    	}
    	return returnValue;   	
    }
    
    
    /**
     * 增加节点,appendEle 作为mainEle的兄弟节点。
     * @param mainEle
     * @param appendEle
     * @return 成功返回１１１1　失败０0
     */
    public static final int uniteParallel(Element mainEle, Element appendEle){
    	int returnValue = 0;
    	if (!mainEle.isRootElement()) {
    		Element parentEle = mainEle.getParent();
    		parentEle.add(appendEle);
    		returnValue = 1;
    	}
    	return returnValue;
    }
    
    
    /**
     * 得到第一个名为name的指令
     * @param document
     * @param name
     * @return ProcessingInstruction
     */
    public static final ProcessingInstruction getFirstPIWithTagName(Document document, String name){
    	ProcessingInstruction pi = null;
    	List list = document.processingInstructions();
        boolean flag = false;
    	for(int i=0,n=list.size(); i<n; i++){
    		pi = (ProcessingInstruction)list.get(i);
    		if (name.equals(pi.getTarget())){
    			flag = true;
    			break;
    		}    		
    	}
    	if (flag == false){
    		pi = null;
    	}
    	return pi;
    }
    
    /**
     *  
     * TODO 本方法的是取得特定Document中的xml-stylesheet
     * 
     * @author wunaigang(2005-5-7)	
     * @param document
     * @param name
     * @param title
     * @return
     */
    public static final String getAssociatedStylesheet(Document document, String name, String title){
    	String styleSheet = null;

    	ProcessingInstruction pi = null;
    	List list = document.processingInstructions();
        boolean flag = false;
        
    	for(int i=0,n=list.size(); i<n; i++){
    		pi = (ProcessingInstruction)list.get(i);    		
    		if (name.equals(pi.getTarget())){
    			HashMap hm = getPIAttributes(pi);	
    			if (title.equals(hm.get("title"))) {
    				styleSheet = (String)hm.get("href");
        			break;	
    			}
    		}    		
    	}
    	
    	return styleSheet;
    }
    
    /**
     * 得到某个指令的属性集
     * @param pi
     * @return
     */
    public static final HashMap getPIAttributes(ProcessingInstruction pi){
    	HashMap hm = new HashMap();
    	if (pi != null) {
    		addPIPseudoAttributes(pi, hm);	
    	}    	
        return hm;    	
    }
    
  
    /**
     * 将指令中的属性解析到hashmap中
     * @param pi
     * @param attributes
     */
    private static final void addPIPseudoAttributes(ProcessingInstruction pi,
    												HashMap attributes){
    	String data = pi.getStringValue();
    	String formatData = data;
    	
    	//格式化空格
    	formatData = data.replaceAll(" +", " ").replaceAll(" *= *", "=");
    	String [] arr = formatData.split(" ");
        
    	Pattern pattern = null;
    	Matcher matcher = null;
        for (int i = 0,n=arr.length;  i < n; i++) {
	        //将属性名和属性值进行分离
        	pattern = Pattern.compile(" *(\\w+)=\"(\\w+|.+)\"");
        	matcher = pattern.matcher(arr[i]);
        	
        	if (matcher.matches()) {        		
        		attributes.put(matcher.group(1), matcher.group(2));
    	    }
        }
    }
    
    /**
     * 用正则表达式替换公式$formula$ 从Properties文件中找到formula对应值，
     * 替换$formula$并返回。
     * @param line
     * @param param
     */
    public static final String formulaRegexp(String line, 
    										 Properties param)
    										 throws Exception{
    	// System.out.println(line);
    	String varExpr = "\\$(\\w+)\\$";
    	int endOffset = 0;
    	String result = line;
    	Pattern pattern = Pattern.compile(varExpr);
    	Matcher matcher = pattern.matcher(result);
    	
    	while(matcher.find()) {
    	   String patternStr = matcher.group(1);
    	   // System.out.println(patternStr);
    	   String value = param.getProperty(patternStr, "");
    	   if(value.trim().equals("")){
    	   	   System.out.print(String.valueOf(String.valueOf((
    	   	  		new StringBuffer("\u8B66\u544A\uFF1A\u5339\u914D\u5230\u4E00\u4E2A\u6A21\u5F0F'"))
					.append(patternStr)
					.append("',\u4F46\u662F\u5728Properties\u4E2D\u6CA1\u6709\u53D1\u73B0\u5176\u5BF9\u5E94\u7684\u503C\n"))));
    	   
    	   }else{
    	   	  line = line.replaceAll("\\$" + patternStr + "\\$", value);	
    	   }
            
    	   result = result.substring(matcher.end(), result.length());
    	   matcher = pattern.matcher(result);
    	}
    	
    	return line;
    }

    /**
     * 用正则表达式替换公式$formula$ 从Properties文件中找到formula对应值，
     * 替换$formula$并返回。
     * @param line　要替换的字符串
     * @param param　配置文件
     * @param remove true　当匹配到一个模式$var$,但是在Properties中没有发现其对应的值时将其删除
     */
    public static final String formulaRegexp(String line, 
    										 HashMap param,
											 boolean remove)
    										 throws Exception{
    	
    	String varExpr = "\\$(\\w+)\\$";
    	int endOffset = 0;
    	String result = line;
    	Pattern pattern = Pattern.compile(varExpr);
    	Matcher matcher = pattern.matcher(result);
    	
    	while(matcher.find()) {
    	   String patternStr = matcher.group(1);
    	   // System.out.println(patternStr);
    	   String value = (String)param.get(patternStr);
    	   if("".equals(value)){
    	   	   System.out.print(String.valueOf(String.valueOf((
    	   	  		new StringBuffer("\u8B66\u544A\uFF1A\u5339\u914D\u5230\u4E00\u4E2A\u6A21\u5F0F'"))
					.append(patternStr)
					.append("',\u4F46\u662F\u5728Properties\u4E2D\u6CA1\u6709\u53D1\u73B0\u5176\u5BF9\u5E94\u7684\u503C\n"))));
    	      if (remove) {
    	      	line = line.replaceAll("\\$" + patternStr + "\\$", "");
    	      }
    	   }else{
    	   	  line = line.replaceAll("\\$" + patternStr + "\\$", value);	
    	   }
            
    	   result = result.substring(matcher.end(), result.length());
    	   matcher = pattern.matcher(result);
    	}
    	
    	return line;
    }
    
    
    
    /**
     *  替换特殊字符
     * @param str　需要替换的字符串
     * @param org　要替换的特殊字符
     * @param trg　替换后的特殊字符
     * @return String 替换后的字符串
     */
    private static final String replaceAll(String str, String org, String trg){
		int iStart = 0;
		int iPos = 0;
		int iReplLength = org.length();
		int iSourceLength = str.length();
		
		StringBuffer sb = new StringBuffer();
		
		while ((iStart < iSourceLength) && ((iPos = str.indexOf(org, iStart)) > -1)) {
			if (iPos > iStart)
				sb.append(str.substring(iStart, iPos));
			sb.append(trg);
			iStart = iPos + iReplLength;
		}
		if (iStart < iSourceLength)
			sb.append(str.substring(iStart));
		
		return sb.toString();
    	
    }
    
	/**
	 * 
	 * 将xml中有'&','<','>','\"','\''做一个标准替换.
	 * 
	 * @param   value 被替换的字符串
	 * @return  替换后的字符串
	 * 
	 */
	public static String validXml(String value) {
		value = replaceAll( value, "&", "&amp;" );
		
		value = replaceAll( value, "<", "&lt;" );
		value = replaceAll( value, ">", "&gt;" );

		value = replaceAll( value, "\"", "&quot;");
		value = replaceAll( value, "'", "&apos;");		
		return value;
	}

    
	/**
	 * 
	 * 将xml中有"&lt","&gt","&amp;","&quot","&apos"的xml还原为&,<,>,",\.
	 * 
	 * @param   value 被替换的字符串
	 * @return  替换后的字符串
	 * @since   1.0.0	     
	 * @see     #validXml(String)
	 */
	public static String originalXml(String value) {
		//this method must be on the first place
		value = replaceAll( value, "&amp;", "&" );		
		value = replaceAll( value, "&lt;", "<" );
		value = replaceAll( value, "&gt;", ">" );		
		value = replaceAll( value, "&quot;", "\"");
		value = replaceAll( value, "&apos;", "'");
		return value;
	}	
    
   /**
    * 替换变量 $var$ 替换var 的值（预览）
    * @param argStr 替换前SQL语句
    * @param myHashMap 参数HashMap
    * @return 替换后的SQL
    */
   public static String setPreviewVariable(String argStr, HashMap myHashMap){      
	 Iterator keys = myHashMap.keySet().iterator();
	 while (keys.hasNext()) {
		  String key = ((String) keys.next()).toUpperCase();		  
		  if (myHashMap.get(key) != null) {
		  	argStr = argStr.replaceAll("\\$" + key + "(:\\w+)*\\$", String.valueOf(myHashMap.get(key)));	
		  }		  
	 }
	 argStr = argStr.replaceAll("\\$\\w+:", "").replaceAll("(:\\w+)*\\$", "");
     
     return argStr;
   }	

   /**
    * 替换变量 $var$ 替换var 的值（正式运行）
    * @param argStr 替换前SQL语句
    * @param myHashMap 参数HashMap
    * @return 替换后的SQL
    */
   public static String setNormalVariable(String argStr, HashMap myHashMap){      
	 Iterator keys = myHashMap.keySet().iterator();
	 
	 while (keys.hasNext()) {
		  String key = ((String) keys.next()).toUpperCase();		  
		  if (myHashMap.get(key) != null) {
		  	argStr = argStr.replaceAll("\\$" + key + "(:\\w+)*\\$", String.valueOf(myHashMap.get(key)));		  	
		  }	
	 }
	 argStr = argStr.replaceAll("\\$\\w+", "").replaceAll("(:\\w+)*\\$", "");
     
     return argStr;
   }	

   /**
    * 将变量转化成大写
    * @param argStr
    * @return
    */
   public static String setVariableToUpperCase(String argStr){
      Pattern p = Pattern.compile("\\$(\\w+)(:\\w+)*\\$");
      Matcher m = p.matcher(argStr);

      //查找
      while(m.find()){
         String tempStr = m.group(1);
         //替换
         Pattern pp = Pattern.compile("\\$("+tempStr+")");
         Matcher mm = pp.matcher(argStr);
         if (mm.find()){
             argStr = mm.replaceAll("\\$"+tempStr.toUpperCase());
         }
      }
    return argStr;
   }
   
	/**
	 *  
	 * TODO 本方法的是返回某个节点指定序号的子节点
	 * 
	 * @author wunaigang(2005-5-5)	
	 * @param ele 需要返回子节点的节点
	 * @param index 子节点序号
	 * @return 指定序号的子节点
	 * @throws Exception
	 */
	public static Element getAppointedElement(Element ele, int index)
											  throws Exception{
		List list = ele.elements();
		Element childEle = null;
		
		if (list != null && list.size() > index){
			childEle = (Element)list.get(index);
		}
		
		return childEle;
	}
	/**
	 * 根据文件名,报表ID,版本ID,到配置文件中找到对应的Element 返回
	 * 
	 * @param fileName
	 *            文件路径名
	 * @param version
	 * @param report
	 * @return Element
	 */
	public static Element getElement(String fileName, String version,
			String report) {
		Element element = null;
		try {
			File file = new File(fileName);
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(file);
			List vlist = document.selectNodes("//version/@id");
			Iterator riter = vlist.iterator();
			while (riter.hasNext()) {
				Attribute attribute = (Attribute) riter.next();
				if (attribute.getValue().equals(version)) {
					Node node = attribute.getParent();
					List list = node.selectNodes("//report/@id");
					Iterator iter = list.iterator();
					while (iter.hasNext()) {
						Attribute attribute1 = (Attribute) iter.next();
						if (attribute1.getValue().equals(report)) {
							element = (Element) attribute1.getParent();
							return element;
						}
					}

				}

			}

		} catch (DocumentException e) {
			element = null;
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception ex) {
			element = null;
			ex.printStackTrace();
		}
		return element;
	}
}