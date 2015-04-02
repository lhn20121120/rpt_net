package com.fitech.net.collect;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

import com.cbrc.smis.adapter.StrutsListingTableDelegate;
import com.fitech.net.collect.util.CollectUtil;

/**
 * 
 * @author wh 清单式报表汇总类
 * 
 */
public class ListingSpecialCollect {

	private int startCol;

	private int endCol;

	/**
	 * 初始化所有参数
	 */
	public ListingSpecialCollect(int startCol, int endCol) {
		this.startCol = startCol;
		this.endCol = endCol;
	}

	public Document start(ArrayList alDoc) {
		/** 需要汇总的Document */
		Document totalDoc=CollectUtil.initSpecListDoc(alDoc);
		if(totalDoc==null) return totalDoc;
		/** 开始汇总 */
		totalDoc=CollectUtil.collectSpecListRep(totalDoc,alDoc,startCol,endCol);

		// System.out.println("collect  OK*************");

		return totalDoc;
	}
	

		public void stop(){}
		
		public Object getLogs()
		{
			Object obj=new Object();
			return obj;
		}
		
		/**
		 * 把清单式报表的单元格入report_in_info表
		 * @return
		 */
		public void insertListRep(Document totalDoc,Integer repInId,String tableName)
		{
			//序号
			int seq=1;
			//存放sql语句的集合对象
			ArrayList sqls=new ArrayList();
			//根节点
			Element root=null;
			root=totalDoc.getRootElement();
			if(root==null) return;
			//取出所有的Subform1节点
			List sfList=root.selectNodes(CollectUtil.Subform1Name);
			if(sfList==null) return;
			if(sfList.size()==0) return;
			Element sf=null;
			List detailList=null;
			Element total=null;
			//遍历sfList
			StringBuffer sql=null;
			for(int i=0;i<sfList.size();i++)
			{
				sf=(Element)sfList.get(i);
				detailList=sf.selectNodes(CollectUtil.detailName+(i+1));
				Element detail=null;
				Element col=null;
				String colValue="";
				//处理detail部分
				if(detailList!=null && detailList.size()>0){
					for(int j=0;j<detailList.size();j++)
					{
						detail=(Element)detailList.get(j);
						sql=new StringBuffer();
						sql.append("insert into ");
						sql.append(tableName);
						sql.append("(rep_in_id,type,col1");
						for(int m=2;m<=endCol;m++)
						{
							sql.append(",");
							sql.append(CollectUtil.colName+m);
						}
						sql.append(") values(");
						sql.append(String.valueOf(repInId.intValue()));
						sql.append(",");
						sql.append("'");
						sql.append(detail.getName());
						sql.append("'");
						sql.append(",");
						sql.append("'");
						sql.append(String.valueOf(seq));
						sql.append("'");
						
						for(int n=2;n<=endCol;n++)
						{
							col=(Element)detail.selectSingleNode(CollectUtil.colName+n);
							if(col!=null){
								colValue=col.getText();
								if(colValue==null || colValue.equals("")){
									colValue="0";
								}
							}
							if(col==null){
								colValue="0";
							}
							sql.append(",");
							sql.append("'");
							sql.append(colValue);
							sql.append("'");
						}
						sql.append(")");
						sqls.add(sql.toString());
						seq++;
					}
				}
				//处理total部分
				total=(Element)sf.selectSingleNode(CollectUtil.totalName+(i+1));
				if(total!=null){
					sql=new StringBuffer();
					sql.append("insert into ");
					sql.append(tableName);
					sql.append("(rep_in_id,type,col1");
					for(int m=2;m<=endCol;m++)
					{
						sql.append(",");
						sql.append(CollectUtil.colName+m);
					}
					sql.append(") values(");
					sql.append(String.valueOf(repInId.intValue()));
					sql.append(",");
					sql.append("'");
					sql.append(detail.getName());
					sql.append("'");
					sql.append(",");
					sql.append("'");
					sql.append(String.valueOf(seq));
					sql.append("'");
					
					for(int n=2;n<=endCol;n++)
					{
						col=(Element)total.selectSingleNode(CollectUtil.colName+n);
						if(col!=null){
							colValue=col.getText();
							if(colValue==null || colValue.equals("")){
								colValue="0";
							}
						}
						if(col==null){
							colValue="0";
						}
						sql.append(",");
						sql.append("'");
						sql.append(colValue);
						sql.append("'");
					}
					sql.append(")");
					sqls.add(sql.toString());
					seq++;
				}
			}//for sfList
			
			StrutsListingTableDelegate.insertListRep(sqls);
		}
	}
