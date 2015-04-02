/*
 * �������� 2005-7-5
 *
 */
package com.cbrc.smis.common;

/**
 * @author cb
 *
 */
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class XmlUtils {
    
    /**
     * ��ȡXML�ĵ�
     * @param File XML�ļ�
     * @return Document
     * @throws DocumentException
     */
    public static Document getDocument(File file) throws DocumentException{
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            try {
                document = reader.read(file);
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		
        return document;
    }
    
    /**
     * ȡ�ø��ڵ�
     * @param Document XML�ĵ�
     * @return Element
     */
    public static Element getRootElement(Document doc) {
        Element root = null;
        root = doc.getRootElement();
        return root;
    }
    
    /**
     * ȡ�ø��ڵ�
     * @param File XML�ļ�
     * @return Element
     */
    public static Element getRootElement(File file) {
        Document doc = null;
        try {
            doc = getDocument(file);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        
        if(doc != null) {
	        Element root = null;
	        root = doc.getRootElement();
	        return root;
        }
        else {
            return null;
        }
    }
    
    
    
    /**
     * �����ĵ�
     * @param doc
     * @param fileName
     * @return
     * @throws IOException
     */
    public static File saveDocument(Document doc,String fileName) throws IOException{
        File f = new File(fileName);
        XMLWriter xmlWriter = new XMLWriter(new FileWriter(f));
        xmlWriter.write(doc);
        xmlWriter.close();
        return f;
    }
    
    /**
     * ��XML�����ļ��ж�ȡ��Ϣ������䵽ActionForm��
     * @param xmlFile	XML�����ļ�
     * @param reportStatus String ���������ݵ������Ա������
     * @return	XmlSeniorManagerForms
     */

}


