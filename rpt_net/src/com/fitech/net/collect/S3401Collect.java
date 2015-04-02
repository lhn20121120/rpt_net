package com.fitech.net.collect;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

import com.fitech.net.collect.util.Collect;
import com.fitech.net.collect.util.CollectUtil;
import com.fitech.net.common.StringTool;

/** 汇总3401单张报表 */
public class S3401Collect implements Collect{
	private int startCol;

	private int endCol;
	 
	private int specialCol1=-1;
	private int specialCol2=-1;
	private String[] specialCol = null;
	/**
	 * 初始化所有参数
	 */
	public S3401Collect(int startCol, int endCol, String[] specialCol) {

		this.startCol = startCol;
		this.endCol = endCol;
		this.specialCol = specialCol;
		if(this.specialCol.length>0){
			specialCol1=Integer.parseInt(this.specialCol[0]);
			specialCol2=Integer.parseInt(this.specialCol[1]);
		}
	}

	
	public Document start(ArrayList alDoc)
	{
		
		
		/**需要汇总的Document*/
		Document totalDoc=CollectUtil.initListDoc(alDoc);
		
		/**汇总detail*/
		totalDoc=CollectUtil.collectDetail(totalDoc,alDoc);
		Element total=(Element)totalDoc.selectSingleNode(CollectUtil.listRoot+CollectUtil.totalName);
		List detailList=null;
		detailList=totalDoc.selectNodes(CollectUtil.listRoot+CollectUtil.detailName);
		/**汇总total*/
		if(detailList!=null && detailList.size()>=1){
			for(int i=startCol;i<=endCol;i++)
			{
				if(i==specialCol1 || i==specialCol2) break;
				double result=0;
				Element detail=null;
				for(int j=0;j<detailList.size();j++)
				{
					detail=(Element)detailList.get(j);
					if(detail.selectSingleNode(CollectUtil.colName+i+"-"+1)!=null && StringTool.strIsNum(detail.selectSingleNode(CollectUtil.colName+i+"-"+1).getText())){
						result=result+Double.parseDouble(StringTool.deleteDH(detail.selectSingleNode(CollectUtil.colName+i+"-"+1).getText()));
					}
					if(detail.selectSingleNode(CollectUtil.colName+i+"-"+2)!=null && StringTool.strIsNum(detail.selectSingleNode(CollectUtil.colName+i+"-"+2).getText())){
						result=result+Double.parseDouble(StringTool.deleteDH(detail.selectSingleNode(CollectUtil.colName+i+"-"+2).getText()));
					}
					if(detail.selectSingleNode(CollectUtil.colName+i+"-"+3)!=null && StringTool.strIsNum(detail.selectSingleNode(CollectUtil.colName+i+"-"+3).getText())){
						result=result+Double.parseDouble(StringTool.deleteDH(detail.selectSingleNode(CollectUtil.colName+i+"-"+3).getText()));
					}
				}
				if(total.selectSingleNode(CollectUtil.colName+i)!=null){
					total.selectSingleNode(CollectUtil.colName+i).setText(String.valueOf(result));
				}
			}
			
			/**汇总COL14*/
			Element detail1=null;
			double result1=0;
			for(int m=0;m<detailList.size();m++)
			{
				detail1=(Element)detailList.get(m);
				if(detail1.selectSingleNode(CollectUtil.colName+specialCol1)!=null && StringTool.strIsNum(detail1.selectSingleNode(CollectUtil.colName+specialCol1).getText())){
					result1=result1+Double.parseDouble(StringTool.deleteDH(detail1.selectSingleNode(CollectUtil.colName+specialCol1).getText()));
				}
			}
			if(total.selectSingleNode(CollectUtil.colName+specialCol1)!=null){
				total.selectSingleNode(CollectUtil.colName+specialCol1).setText(String.valueOf(result1));
			}
			
			/**汇总COL15*/
			Element detail2=null;
			double result2=0;
			for(int m=0;m<detailList.size();m++)
			{
				detail2=(Element)detailList.get(m);
				if(detail2.selectSingleNode(CollectUtil.colName+specialCol2)!=null && StringTool.strIsNum(detail2.selectSingleNode(CollectUtil.colName+specialCol2).getText())){
					result2=result2+Double.parseDouble(StringTool.deleteDH(detail2.selectSingleNode(CollectUtil.colName+specialCol2).getText()));
				}
			}
			if(total.selectSingleNode(CollectUtil.colName+specialCol2)!=null){
				total.selectSingleNode(CollectUtil.colName+specialCol2).setText(String.valueOf(result2));
			}
		}
		
		
		// System.out.println("collect S3401 is OK*************");
		
		return totalDoc;
	}

	public void stop(){}
	
	public Object getLogs()
	{
		Object obj=new Object();
		return obj;
	}
	
	
}
