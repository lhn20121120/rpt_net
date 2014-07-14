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
     * ����xml�ĵ��ĸ��ڵ�
     * @param doc
     * @return Element ���ڵ�
     */
    public static final Element getRootElement(Document doc){

        return doc.getRootElement();

     }
    
    /**
     * �жϸýڵ��Ƿ���Ҷ�ӽڵ�
     * @param ele
     * @return boolean flase����Ҷ�ӽڵ�,true��Ҷ�ӽڵ�
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
     * ����Ԫ��
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
     * ���ļ���ȡXML�������ļ���������XML�ĵ�
     * @param fileName
     * @return Document  �������xml�ĵ��ṹ
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
     * ��ʽ��XML�ĵ�,�������������
     * @param document
     * @param fileName
     * @return
     */
    public static int saveXml(Document document, String fileName){
    	int returnValue = 0;
    	try{
            XMLWriter output = null;
            /** ��ʽ�����,����IE���һ�� */
            OutputFormat format = OutputFormat.createPrettyPrint();

            /** ָ��XML�ַ������� */            
            output = new XMLWriter(new FileWriter(new File(fileName)),format);
            output.write(document);
            output.close();        

            /** ִ�гɹ�,�践��1 */
            returnValue = 1;    
    		
    	}catch(Exception e){
    		// System.out.println("Exception Path is com.sisytech.comm.xml.Util.parseXml :" + e.getMessage());
    		e.printStackTrace();
    	}
    	return returnValue;
    }

    /**
     * ��ʽ��XML�ĵ�,����ָ���ַ������
     * @param document
     * @param fileName
     * @return ���ز������, 0��ʧ��, 1��ɹ�
     */
    public static int saveXml(Document document, 
    								String fileName,
									String encoding){
    	int returnValue = 0;
    	//saveXml(document, fileName);
        /*
         * �������ַ���ͳһ��gb18030
         */
//    	if (Util.ENCODING_GBK.equals(encoding.toUpperCase()) || 
//    			Util.ENCODING_GB2312.equals(encoding.toUpperCase())) {
//    		encoding = Util.ENCODING_GB18030;
//    	}
    	try{
            XMLWriter output = null;
            /** ��ʽ�����,����IE���һ�� */
            OutputFormat format = OutputFormat.createPrettyPrint();

            /** ָ��XML�ַ������� */
            format.setEncoding(encoding);
            //output = new XMLWriter(new  FileWriter(new File(fileName)),format);
            output = new XMLWriter(new FileOutputStream(new File(fileName)),format);
            output.write(document);
            output.close();        

            /** ִ�гɹ�,�践��1 */
            returnValue = 1;    
    		
    	}catch(Exception e){
    		// System.out.println("Exception Path is com.sisytech.comm.xml.Util.parseXml :" + e.getMessage());
    		e.printStackTrace();
    	}
    	return returnValue;
    }
    
    /**
     * �õ�ĳһ����
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
     * TODO ���������ǻ��ĳ���ڵ�������Ϊname������ֵ
     * 
     * @author wunaigang(2005-4-10)	
     * @param ele
     * @param name
     * @return�����ز������
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
     * �޸�Ԫ������ֵ
     * @param ele Ҫ�޸����Ե�Ԫ��
     * @param name ������
     * @param value Ҫ�޸ĵ�ֵ
     * @return ���ز������, 0��ʧ��, 1��ɹ�
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
     * ���ӽڵ�,appendEle ��ΪmainEle���ӽڵ㡣
     * @param mainEle
     * @param appendEle
     */
    public static final void uniteAfter(Element mainEle, Element appendEle){
    	mainEle.add(appendEle);    
    }
    
    /**
     *  
     * TODO ���������ǽ�childEle�ӵ�parentElem֮�г�Ϊ���ӽڵ㣬���ҳ�Ϊ���һ���ӽڵ�
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
     * ���ӽڵ�,appendEle ��ΪmainEle�ĸ��ڵ㡣���滻mainEle�ڵ�
     * ԭ��λ�ã���mainEle��ΪappendEle���ӽڵ�
     * @param mainEle
     * @param appendEle
     * @return �ɹ����أ�����1��ʧ�ܣ�0
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
     * ���ӽڵ�,appendEle ��ΪmainEle���ֵܽڵ㡣
     * @param mainEle
     * @param appendEle
     * @return �ɹ����أ�����1��ʧ�ܣ�0
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
     * �õ���һ����Ϊname��ָ��
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
     * TODO ����������ȡ���ض�Document�е�xml-stylesheet
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
     * �õ�ĳ��ָ������Լ�
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
     * ��ָ���е����Խ�����hashmap��
     * @param pi
     * @param attributes
     */
    private static final void addPIPseudoAttributes(ProcessingInstruction pi,
    												HashMap attributes){
    	String data = pi.getStringValue();
    	String formatData = data;
    	
    	//��ʽ���ո�
    	formatData = data.replaceAll(" +", " ").replaceAll(" *= *", "=");
    	String [] arr = formatData.split(" ");
        
    	Pattern pattern = null;
    	Matcher matcher = null;
        for (int i = 0,n=arr.length;  i < n; i++) {
	        //��������������ֵ���з���
        	pattern = Pattern.compile(" *(\\w+)=\"(\\w+|.+)\"");
        	matcher = pattern.matcher(arr[i]);
        	
        	if (matcher.matches()) {        		
        		attributes.put(matcher.group(1), matcher.group(2));
    	    }
        }
    }
    
    /**
     * ��������ʽ�滻��ʽ$formula$ ��Properties�ļ����ҵ�formula��Ӧֵ��
     * �滻$formula$�����ء�
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
     * ��������ʽ�滻��ʽ$formula$ ��Properties�ļ����ҵ�formula��Ӧֵ��
     * �滻$formula$�����ء�
     * @param line��Ҫ�滻���ַ���
     * @param param�������ļ�
     * @param remove true����ƥ�䵽һ��ģʽ$var$,������Properties��û�з������Ӧ��ֵʱ����ɾ��
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
     *  �滻�����ַ�
     * @param str����Ҫ�滻���ַ���
     * @param org��Ҫ�滻�������ַ�
     * @param trg���滻��������ַ�
     * @return String �滻����ַ���
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
	 * ��xml����'&','<','>','\"','\''��һ����׼�滻.
	 * 
	 * @param   value ���滻���ַ���
	 * @return  �滻����ַ���
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
	 * ��xml����"&lt","&gt","&amp;","&quot","&apos"��xml��ԭΪ&,<,>,",\.
	 * 
	 * @param   value ���滻���ַ���
	 * @return  �滻����ַ���
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
    * �滻���� $var$ �滻var ��ֵ��Ԥ����
    * @param argStr �滻ǰSQL���
    * @param myHashMap ����HashMap
    * @return �滻���SQL
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
    * �滻���� $var$ �滻var ��ֵ����ʽ���У�
    * @param argStr �滻ǰSQL���
    * @param myHashMap ����HashMap
    * @return �滻���SQL
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
    * ������ת���ɴ�д
    * @param argStr
    * @return
    */
   public static String setVariableToUpperCase(String argStr){
      Pattern p = Pattern.compile("\\$(\\w+)(:\\w+)*\\$");
      Matcher m = p.matcher(argStr);

      //����
      while(m.find()){
         String tempStr = m.group(1);
         //�滻
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
	 * TODO ���������Ƿ���ĳ���ڵ�ָ����ŵ��ӽڵ�
	 * 
	 * @author wunaigang(2005-5-5)	
	 * @param ele ��Ҫ�����ӽڵ�Ľڵ�
	 * @param index �ӽڵ����
	 * @return ָ����ŵ��ӽڵ�
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
	 * �����ļ���,����ID,�汾ID,�������ļ����ҵ���Ӧ��Element ����
	 * 
	 * @param fileName
	 *            �ļ�·����
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