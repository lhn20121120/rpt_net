package com.fitech.net.collect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import com.fitech.net.collect.util.CollectUtil;
import com.fitech.net.common.StringTool;

/**
 * 
 * @author wng.wl 特殊报表汇总类
 * 
 */
public class SpecialCollect {
	private ArrayList hs = new ArrayList();

	private ArrayList list = null;

	/* 从配置文件取得 */
	private int startRow = 0;

	private int endRow = 0;

	private char startCol;

	private char endCol;

	private char compareCol; // 排序中比较的列

	private List totalValue = new ArrayList(); // 要合计的单元格列表

	private List commonCell = new ArrayList(); // 排序的行除外的普通单元格
	
	private int addAndComp = 0;


	/**
	 * 初始化所有参数
	 */
	public SpecialCollect(int startRow,int endRow,char startCol,char endCol,char compareCol, List totalValue, List commonCell) {
		
		this.startRow =startRow;
		this.endRow = endRow;
		this.startCol = startCol;
		this.endCol = endCol;
		this.compareCol = compareCol;
		this.totalValue = totalValue; // 要合计的单元格列表
		this.commonCell = commonCell; // 排序的行除外的普通单元格
	}
	/**
	 * 初始化所有参数
	 */
	public SpecialCollect(int startRow,int endRow,char startCol,char endCol,char compareCol, List totalValue, List commonCell,int addAndComp) {
		
		this.startRow =startRow;
		this.endRow = endRow;
		this.startCol = startCol;
		this.endCol = endCol;
		this.compareCol = compareCol;
		this.totalValue = totalValue; // 要合计的单元格列表
		this.commonCell = commonCell; // 排序的行除外的普通单元格
		this.addAndComp = addAndComp;
	}
	// 开始汇总数据
	public  Document start(ArrayList arrayList) {
		this.list = arrayList;
		Iterator iter = arrayList.iterator();
		while (iter.hasNext()) {
			this.parseEachDoc((Document) iter.next());
		}

		// 排序
		ArrayList reList = sortArr(compareCol, hs);
		
		if (reList == null) {
			// System.out.println("排序出错!");
		}
		return this.writeToXML(reList, CollectUtil.initDoc(arrayList));
	}
//	 开始汇总数据
	public  Document start(ArrayList arrayList,Document doc) {
		this.list = arrayList;
		Iterator iter = arrayList.iterator();
		while (iter.hasNext()) {
			this.parseEachDoc((Document) iter.next());
		}

		// 排序
		ArrayList reList = sortArr(compareCol, hs);
		
		if (reList == null) {
			// System.out.println("排序出错!");
		}
		return this.writeToXML(reList, doc);
	}


	/**
	 * 排序
	 * 
	 * @param ch
	 *            从配置文件取得的要排序的列
	 */
	public ArrayList sortArr(char ch, ArrayList list) {
		/* 要比较列在数组中的下标 */
		int index = ch - startCol;
		Object templet1[] = null;
		Object templet2[] = null;
		CellObject co1 = null;
		CellObject co2 = null;
		double comp1 = 0.0;
		double comp2 = 0.0;
		ArrayList HDataList = new ArrayList();
        
		/* 把有数据的行放在一个List中 */
		for (int k = 0; k < list.size(); k++) {
			Object temporaryList[] = (Object[]) list.get(k);
			CellObject cellObj = (CellObject) temporaryList[index];
			if (cellObj != null) {
				if (cellObj.getOneCell() != null
						&& !cellObj.getOneCell().equals("")) {
					HDataList.add(temporaryList);
				}
			}
		}
		ArrayList tempList = HDataList;
	
		for (int i = 0; i < tempList.size(); i++) {
			for (int j = i + 1; j < tempList.size(); j++) {

				templet1 = (Object[]) tempList.get(i);
				templet2 = (Object[]) tempList.get(j);
				co1 = (CellObject) templet1[index];
				co2 = (CellObject) templet2[index];
				if (co2 != null && co1 != null) {
					if (co2.getOneCell() == null || co2.getOneCell().equals("")
							|| co1.getOneCell() == null
							|| co1.getOneCell().equals("")) {
						 System.out.println("~~~~" + ch + "列为空,不用比较!");
						continue;
					}
					try {
						comp1 = Double.parseDouble(co1.getOneCell());
						comp2 = Double.parseDouble(co2.getOneCell());
					} catch (Exception e) {
						 System.out.println("类型转换错误!");
						e.printStackTrace();
					}
				} else {
					 System.out.println("templet2[index]~~~~object[" + j
							+ "]为空!");
				}
				if (comp1 < comp2) {
					Object tempObj[] = (Object[]) HDataList.get(i);
					HDataList.set(i, (Object[]) HDataList.get(j));
					HDataList.set(j, tempObj);				
					
				}

			}
			
		}
		return HDataList;
	}

	// 解析每个Document对象
	public void parseEachDoc(Document doc) {
		int index = startRow;

		while (index <= endRow) {
			// 对象数组,用于存放一行记录,数组的每一个元素是一个对象,代表一个单元格
			Object objArr[] = new Object[endCol - startCol+1];
			int i = 0;
			char cr = startCol;
			while (cr <= endCol) {
				/* 构建一个对象,用于保存一个单元格内容 */
				CellObject cObj = new CellObject();
				/* 初始化数组中的对象元素 */				
				objArr[i] = new Object();

				Node node = doc.selectSingleNode("/F/P1/" + cr + index);

				if (node != null) {
					// 循环比较列
					for (int j = 0; j < (endCol - startCol+1); j++) {
						char tmp = (char) (startCol + i);

						if (cr == tmp) {
							cObj.setOneCell(node.getText());
							objArr[i] = cObj;
						}
					}
				} else {
					objArr[i] = cObj;
				}
				cr++;
				i++;
			}
			hs.add(objArr);
			index++;
		}
	}

	// 将汇总数据写如汇总文件
	public Document writeToXML(ArrayList list, Document doc) {
		/*如果有行为空,就默认为空串*/
		if(list.size()<hs.size()){			
			CellObject cObj = new CellObject();
			cObj.setOneCell("");
			Object oArr[] = new Object[endCol - startCol+1];
			int size=hs.size()-list.size();
			
			for(int rowIndex=0;rowIndex<size;rowIndex++)
			{
				for(int colIndex=0;colIndex<oArr.length;colIndex++)
				{
					oArr[colIndex] = new Object();
					oArr[colIndex]=cObj;
				}				
				list.add(list.size(),oArr);
			}
		}
		
	    int ind=0;
		int index=startRow;
		while (ind <=endRow-startRow) {
			Object templet[] = (Object[]) list.get(ind);
			char cr = startCol;

			while (cr <= endCol) {
				Element root = doc.getRootElement();
				Element element = root.element("P1");
				Node node=doc.selectSingleNode("/F/P1/"+cr+String.valueOf(index));
				
				if(node == null){
					element.addElement(cr + String.valueOf(index));
					node = doc.selectSingleNode("/F/P1/"+cr+String.valueOf(index));
				}	
			/*	
				if (node == null) {
					Element root = doc.getRootElement();
					Element p = root.element("P1");
					Element temp = p.addElement(cr + String.valueOf(index));

					for (int j = startCol, i = 0; j <= endCol; j++, i++) {
						if (cr == (startCol + i)) {
							 初始化数组中的对象 
							CellObject cObj = (CellObject) templet[i];
							if (cObj.getOneCell() != null) {
								temp.setText(cObj.getOneCell());
							}
						}
					}
					cr++;
					continue;

				}*/
		//	if (node != null)
		//	 {
				for (int j = startCol, i = 0; j <= endCol; j++, i++) {
					if (cr == (startCol + i)) {
						/* 初始化数组中的对象 */
						CellObject cObj = (CellObject) templet[i];
						try{
							if (cObj.getOneCell() != null) {
								node.setText(cObj.getOneCell());
							}
						}catch(Exception e)
						{
							// System.out.println("SpecialCollect 从CellObject类中取单元格错误!"+i+"______"+j);
							e.printStackTrace();
						}
					}
				}
		//	}

				cr++;
			}
			index++;
			ind++;
		}
		/* 排序行除外的单元格 */

		// 写入合计的单元格值
/*		if(this.totalValue!=null){
			this.setAllTotal(doc);
		}
*/		
		// 写入排序的行除外的普通单元格
		if(this.commonCell!=null){
			this.setCommonCell(doc);
		}

		return doc;
	}

	

	/**
	 * 求合计列
	 */
	public void setAllTotal(Document doc) {
		List totalList = this.totalValue;
		ArrayList lists = this.getTemplets();
		double count = 0.0;
		Node node = null;
		String colStr;
		if (totalList != null) {
			try {
				for (int i = 0; i < totalList.size(); i++) {
					node = doc.selectSingleNode("/F/P1/" + totalList.get(i));
					colStr = FJString(totalList.get(i).toString());
					if (colStr != null) { 
						char ch = colStr.charAt(0); // 得到合计的列
						int index = ch - startCol;
						for (int j = 0; j < 10; j++) {
							Object obj[] = (Object[]) lists.get(j);
							CellObject cellObject = (CellObject) obj[index]; // 取得合计列的单元格值
							if (cellObject.getOneCell() != null) {
								if (!(cellObject.getOneCell().equals(""))) {
									count += Double.parseDouble(cellObject
											.getOneCell());
								}
							}
						}
					}
					node.setText(String.valueOf(count));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 写入排序的行除外的普通单元格
	 * 
	 * @param doc
	 */
	public void setCommonCell(Document doc) {
		double allNet = 0.0;
		Node node = null;
		List commonList = this.commonCell; // 普通单元格列表
		if (commonList != null) {
			try {
				for (int i = 0; i < commonList.size(); i++) { 
					allNet = 0.0;
					node=doc.selectSingleNode("/F/P1/"+ commonList.get(i));

					
					if(node == null){
						Element root = doc.getRootElement();
						Element element = root.element("P1");
						element.addElement(commonList.get(i)+"");
						node = doc.selectSingleNode("/F/P1/"+commonList.get(i));
					}	
				/*	if(node == null)
						node = doc.addElement("/F/P1/"+ commonList.get(i));		
					*/
						for (int j = 0; j < list.size(); j++) {
							Document nets = (Document) list.get(j);
							if(nets != null){
								Node	nodes = nets.selectSingleNode("/F/P1/"+ commonList.get(i));
							 
								if (!(nodes.getText().equals(""))) {
									allNet += Double.parseDouble(StringTool
											.deleteDH(nodes.getText()));
								}
								node.setText(String.valueOf(allNet));
							}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public ArrayList getTemplets() {
		return this.hs;
	}

	/**
	 * 用于分解字符串的方法; 如把E15 中的 E 分解出来
	 * 
	 * @return char
	 */
	public String FJString(String str) {
		String colStr = null;
		char[] charArray = str.toCharArray();
		for (int i = charArray.length - 1; i >= 0; i--) {
			try {
				Integer.parseInt(String.valueOf(charArray[i]));
			} catch (Exception ex) {
				String backStr = new String(str);
				colStr = backStr.substring(0, i + 1);

			}
		}
		return colStr;
	}

	/**
	 * To filter the String to a special one wanted
	 * 
	 * @param d
	 *            the double data we want to filter
	 * @param i
	 *            the special thing
	 * @return the String has been specialled
	 */
	public String filter(double d, int i) {
		String trans = String.valueOf(d);
		int index = trans.indexOf(".");

		if (index != -1) {
			String trans2 = trans.substring(index + 1);

			if (trans2.length() >= i) {
				String str1 = trans.substring(index + i, index + i + 1);

				char ch = trans.charAt(index + 1 + i);

				if (ch >= '5' && ch <= '9') {
					String str2 = String.valueOf(Integer.parseInt(str1) + 1);

					return trans.substring(0, index + i) + str2;
				}
			}
		}
		return trans;
	}
}
