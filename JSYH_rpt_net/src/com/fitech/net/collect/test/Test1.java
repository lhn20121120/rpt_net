package com.fitech.net.collect.test;

import java.io.File;
import java.util.ArrayList;


public class Test1 {

	public static void main(String[] args)
	{
		ArrayList alDoc=new ArrayList();
		File file1=new File("d:\\wdw\\collectTest\\insert\\test1.xml");
		File file2=new File("d:\\wdw\\collectTest\\insert\\test2.xml");
		File file3=new File("d:\\wdw\\collectTest\\insert\\test3.xml");
		File file4=new File("d:\\wdw\\collectTest\\insert\\test4.xml");
		File file5=new File("d:\\wdw\\collectTest\\insert\\test5.xml");
		
		ArrayList al=new ArrayList();
		al.add(file1);
		al.add(file2);
		al.add(file3);
		al.add(file4);
		al.add(file5);
		
		AddXMLInDB doXML=new AddXMLInDB();
		doXML.insertXML(al);
		alDoc=doXML.getXML();
		
		
		
		Test2 test2=new Test2();
		test2.start(alDoc);
	}
	
	
}
