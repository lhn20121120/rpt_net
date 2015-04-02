package com.fitech.net.collect.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;

import com.fitech.net.collect.bean.G2300Record;
import com.fitech.net.collect.comparator.G2300Comparator;

public class Test2 {
	public void start(ArrayList al)
	{
	
		ArrayList alTotal=new ArrayList();
		G2300Record g23record = null;
		Document docTotal=DocumentHelper.createDocument();
		docTotal.addElement("F");
		Element rootTotal=docTotal.getRootElement();
		Element P1=rootTotal.addElement("P1");
		
		Element B3=P1.addElement("B3");
		B3.addText("A");
		Element C3=P1.addElement("C3");
		C3.addText("B");
		Element D3=P1.addElement("D3");
		D3.addText("C");
		Element E3=P1.addElement("E3");
		E3.addText("D");
		Element F3=P1.addElement("F3");
		F3.addText("E");
		Element G3=P1.addElement("G3");
		G3.addText("F");
		
		Element B4=P1.addElement("B4");
		B4.addText("排序");
		Element C4=P1.addElement("C4");
		C4.addText("客户名称");
		Element D4=P1.addElement("D4");
		D4.addText("客户代码");
		Element E4=P1.addElement("E4");
		E4.addText("存款余额");
		Element F4=P1.addElement("F4");
		F4.addText("其中：定期存款");
		Element G4=P1.addElement("G4");
		G4.addText("占各项存款比重（％）");
		
		//SAXReader reader=new SAXReader();
		
		for(int i=0;i<al.size();i++)
		{
			Document doc=null;
			try {
				doc=(Document)al.get(i);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			List list=null;
			
//			Element e1 = null;
//			Element e2 = null;
//			Element e3 = null;
//			Element e4 = null;
//			Element e5 = null;
			
			Element root=doc.getRootElement();
			for(int j=5; j<15; j++){
				g23record = new G2300Record();
				Element e1 = (Element)root.selectSingleNode("P1/C"+j);			
				g23record.setC(e1.getText());
				Element e2 = (Element)root.selectSingleNode("P1/D"+j);
				g23record.setD(e2.getText());
				Element e3 = (Element)root.selectSingleNode("P1/E"+j);				
				g23record.setE(e3.getText());
				Element e4 = (Element)root.selectSingleNode("P1/F"+j);
				g23record.setF(e4.getText());
				Element e5 = (Element)root.selectSingleNode("P1/G"+j);
				g23record.setG(e5.getText());
				alTotal.add(g23record);
			}
			
		}
		// System.out.println("alTotal size is "+alTotal.size());
		G2300Comparator g23Comp = new G2300Comparator();
		Collections.sort(alTotal, g23Comp);
		
		
		for(int m=1;m<11;m++)
		{
			g23record=(G2300Record)alTotal.get(m-1);
			Element A=P1.addElement("A"+(m+4));
				A.addText(""+m);
			Element B=P1.addElement("B"+(m+4));
				B.addText(""+m);
			Element C=P1.addElement("C"+(m+4));
				C.addText(g23record.getC());
			Element D=P1.addElement("D"+(m+4));
				D.addText(g23record.getD());
			Element E=P1.addElement("E"+(m+4));
				E.addText(g23record.getE());
			Element F=P1.addElement("F"+(m+4));
				F.addText(g23record.getF());
			Element G=P1.addElement("G"+(m+4));
				G.addText(g23record.getG());
		}
		
		double A11Value = 0;
		for(int n=5;n<15;n++)
		{
			A11Value=A11Value + Double.parseDouble(((Element)P1.selectSingleNode("E"+n)).getText());
		}
		
		Element E15=P1.addElement("E15");
			E15.addText(String.valueOf(A11Value));


		try {
			FileOutputStream fos=null;
			File fileTotal=new File("d:\\wdw\\fileTotal.xml");
			fos=new FileOutputStream(fileTotal);
			XMLWriter writer=new XMLWriter(fos);
			writer.write(docTotal);
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
