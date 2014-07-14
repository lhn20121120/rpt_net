package com.fitech.net.collect;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

import com.cbrc.smis.adapter.StrutsListingTableDelegate;
import com.fitech.net.collect.util.CollectUtil;

/**
 * 
 * @author wh �嵥ʽ���������
 * 
 */
public class ListingSpecialCollect {

	private int startCol;

	private int endCol;

	/**
	 * ��ʼ�����в���
	 */
	public ListingSpecialCollect(int startCol, int endCol) {
		this.startCol = startCol;
		this.endCol = endCol;
	}

	public Document start(ArrayList alDoc) {
		/** ��Ҫ���ܵ�Document */
		Document totalDoc=CollectUtil.initSpecListDoc(alDoc);
		if(totalDoc==null) return totalDoc;
		/** ��ʼ���� */
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
		 * ���嵥ʽ����ĵ�Ԫ����report_in_info��
		 * @return
		 */
		public void insertListRep(Document totalDoc,Integer repInId,String tableName)
		{
			//���
			int seq=1;
			//���sql���ļ��϶���
			ArrayList sqls=new ArrayList();
			//���ڵ�
			Element root=null;
			root=totalDoc.getRootElement();
			if(root==null) return;
			//ȡ�����е�Subform1�ڵ�
			List sfList=root.selectNodes(CollectUtil.Subform1Name);
			if(sfList==null) return;
			if(sfList.size()==0) return;
			Element sf=null;
			List detailList=null;
			Element total=null;
			//����sfList
			StringBuffer sql=null;
			for(int i=0;i<sfList.size();i++)
			{
				sf=(Element)sfList.get(i);
				detailList=sf.selectNodes(CollectUtil.detailName+(i+1));
				Element detail=null;
				Element col=null;
				String colValue="";
				//����detail����
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
				//����total����
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
