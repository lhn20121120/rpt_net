package com.fitech.net.collect.util;

import org.dom4j.Document;
import org.dom4j.Element;

import com.fitech.net.common.StringTool;

/**
 * 
 * @author wdw
 *
 */
public class CollectUnitTools {
	
	
	/**������ӵĵ�Ԫ��
	 * 
	 * @param totalDoc
	 * @param resultUnit
	 * @param addUnit	��Ҫ��ӵĵ�Ԫ��
	 */
	public static void collectAddUnit(Document totalDoc,String resultUnit,String[] addUnit)
	{
		if(totalDoc==null) return;
		if(addUnit==null && addUnit.length==0){
			return;
		}
		Element resultE=(Element)totalDoc.selectSingleNode(CollectUtil.root+resultUnit);
		if(resultE==null) return;
		Element tmpE=null;
		double result=0;
		for(int i=0;i<addUnit.length;i++)
		{
			tmpE=(Element)totalDoc.selectSingleNode(CollectUtil.root+addUnit[i]);
			if(tmpE!=null && !tmpE.getText().equals("") && StringTool.strIsNum(tmpE.getText())){
				result=result+Double.parseDouble(StringTool.deleteDH(tmpE.getText()));
			}
		}
		
		if(resultE!=null){
			resultE.setText(String.valueOf(result));
		}
		
	}

	/**������ٷֱȵĵ�����Ԫ��
	 * 
	 * @param totalDoc
	 * @param resultUnit
	 * @param up	����
	 * @param down	��ĸ
	 */
	public static void collectPerUnit(Document totalDoc,String resultUnit,String upUnit,String downUnit)
	{
		if(totalDoc==null) return;
		Element resultE=(Element)totalDoc.selectSingleNode(CollectUtil.root+resultUnit);
		Element upE=(Element)totalDoc.selectSingleNode(CollectUtil.root+upUnit);
		Element downE=(Element)totalDoc.selectSingleNode(CollectUtil.root+downUnit);
		double result=0;
		if(resultE==null){
			return;
		}
		
		if(upE!=null && downE!=null){
			if(StringTool.strIsNum(upE.getText()) && StringTool.strIsNum(downE.getText())){
				
				if(Double.parseDouble(StringTool.deleteDH(upE.getText()))==0){
					resultE.setText(String.valueOf(0));
					return;
				}
				
				if(Double.parseDouble(StringTool.deleteDH(downE.getText()))==0){
					return;
				}
				
				resultE.setText(
								String.valueOf(
												(
													Double.parseDouble(StringTool.deleteDH(upE.getText()))
													/
													Double.parseDouble(StringTool.deleteDH(downE.getText()))
																						)*100
																							)
																								);
			}
		}
	}
	
	/**������ٷֱȵĵ�Ԫ�񼯺� startRow��endRow�����ǵ�����
	 * 
	 * @param totalDoc
	 * @param resultCol	������
	 * @param upCol		������
	 * @param downCol	��ĸ��
	 * @param startRow	��ʼ��
	 * @param endRow	������
	 */
	public static void collectPerUnits(Document totalDoc,String resultCol,String upCol,String downCol,int startRow,int endRow)
	{
		String resultUnit=null;
		String upUnit=null;
		String downUnit=null;
		for(int i=startRow;i<=endRow;i++)
		{
			resultUnit=CollectUtil.root+resultCol+i;
				// System.out.println("resultUnit is "+CollectUtil.root+resultCol+i);
			upUnit=CollectUtil.root+upCol+i;
				// System.out.println("upUnit is "+CollectUtil.root+upCol+i);
			downUnit=CollectUtil.root+downCol+i;
				// System.out.println("downUnit is "+CollectUtil.root+downCol+i);
			
			if(resultUnit!=null && upUnit!=null && downUnit!=null){
				collectPerUnit(totalDoc,resultUnit,upUnit,downUnit);
				// System.out.println("collect "+resultUnit+" is OK!");
			}else{
				// System.out.println("collect "+resultUnit+" is Fail!");
			}
		}
	}
	
}
