package com.fitech.gznx.service;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.po.AfPbocreportdata;

public class AFDataImportDelegate {
	public static Map freqMap = null;
	static{
		freqMap = new HashMap();
		freqMap.put("4", "1");
		freqMap.put("3", "2");
		freqMap.put("1", "3");
		freqMap.put("0", "4");
		freqMap.put("6", "7");
		freqMap.put("5", "8");
	}

	private static FitechException log = new FitechException(
			AFDataImportDelegate.class);
	/**
	 * 人行历史报文载入
	 * @param idxFile idx文件
	 * @param datFile dat文件
	 * @return
	 * @throws IOException
	 */
	public static boolean dataRHImport(File idxFile,File datFile) throws IOException{
		boolean result = false;
		// 连接对象和会话对象初始化
		DBConn conn = null;
		Session session = null;
		String fileName = null;
		BufferedReader readerIdx = null;
		try {
			conn = new DBConn();
			session = conn.beginTransaction();
			fileName = idxFile.getName();
			String termRH = fileName.substring(13, 21);//人行期数
			Integer year = Integer.parseInt(termRH.substring(0, 4));
			Integer term = Integer.parseInt(termRH.substring(4, 6));
			Integer day = Integer.parseInt(termRH.substring(6, 8));
			String freqRH = fileName.substring(21, 22);//人行频度
			String freqId = (String)freqMap.get(freqRH);//系统频度
			readerIdx = new BufferedReader(new FileReader(idxFile));
			String line = null;
			String hsql = null;
			while ((line = readerIdx.readLine()) != null) {
				String[] strs = line.split("\\|");
				String templateId = strs[1];
				hsql = "select mr.id.versionId from AfTemplate mr where mr.id.templateId='" + templateId 
				+ "' and to_date(mr.startDate,'yyyy-MM-dd')<=to_date('" + termRH + "','yyyyMMdd') and to_date('" + termRH + "','yyyyMMdd')<=to_date(mr.endDate,'yyyy-MM-dd')";
				String versionId = null;
				List vl = session.find(hsql);
				if(vl.size()==0){
					log.println("====================" + templateId + "缺少基础表信息!");
					continue;
				}
				versionId = (String)vl.get(0);//获取版本号
				Integer regionId = new Integer(strs[3]);
				hsql = "select o.orgId from com.fitech.gznx.po.AfOrg o where o.regionId=" + regionId;
				String orgId = (String)session.find(hsql).get(0);//这里地区码就作为机构编码
				hsql = "select a.repId from AfReport a where a.curId=1 and a.orgId='" + orgId
				+ "' and a.templateId='" + templateId + "' and a.versionId='" + versionId + "' and a.repFreqId=" + freqId
				+ " and a.year=" + year + " and a.term=" + term + " and a.day=" + day;
				List tl = session.find(hsql);
				if(tl.size()==0){
					log.println(orgId + "下的" + templateId + "报表没有载入");
					continue;
				}
				Long repId = (Long)tl.get(0);
				String dataType = strs[4];
				String curId = strs[5];
				hsql = "select p.id.colId from AfPboccell p where p.id.templateId='" + templateId +
				"' and p.id.versionId='" + versionId + "' and p.curId='" + curId + "' and p.dataType='" + dataType + "'";
				String colId = (String)session.find(hsql).get(0);
				hsql = "from AfPbocreportdata a where a.id.repId=" + repId + " and a.id.cellId in(select c.cellId from AfCellinfo c where c.templateId='" + templateId
				+ "' and c.versionId='" + versionId + "' and c.colNum='" + colId + "')";
				session.delete(hsql);
				List<String> lineList = getDataRH(datFile,strs[0]);
				for(int i=0;i<lineList.size();i++){
					com.fitech.gznx.po.AfPbocreportdata pd = new com.fitech.gznx.po.AfPbocreportdata();
					String[] lines = lineList.get(i).split("\\|");
					hsql = "select c.cellId from AfCellinfo c where c.cellPid='" + lines[1]
					+ "' and c.templateId='" + templateId + "' and c.versionId='" + versionId
					+ "' and c.colNum='" + colId + "'";//获取单元格ID
					Long cellId = (Long)session.find(hsql).get(0);
					com.fitech.gznx.po.AfPbocreportdataId id = new com.fitech.gznx.po.AfPbocreportdataId ();
					id.setCellId(cellId);
					id.setRepId(repId);
//					Object o = session.get(AfPbocreportdata.class, id);
//					if(o!=null)
//						System.out.println();
					pd.setId(id);
					pd.setCellData(lines[2]);
					session.save(pd);
				}
			}
			session.flush();
			result = true;
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			if(readerIdx!=null)
				readerIdx.close();
			// 如果连接存在，则断开，结束会话，返回
			if (conn != null)
				conn.endTransaction(result);
		}
		log.println("========================over");
		return result;
	}
	public static List getDataRH(File datFile,String num) throws Exception{
		List lineList = new ArrayList();
		String line = null;
		BufferedReader reader = null;
		try{
			reader = new BufferedReader(new FileReader(datFile));
			line = reader.readLine();
			while (line != null) {
				String[] strs = line.split("\\|");
				if(num.equals(strs[0])){
					lineList.add(line);
				}
				line = reader.readLine();
			}
		}catch(Exception e){
			throw e;
		}
		finally{
			if(reader!=null)
				reader.close();
		}
		return lineList;
	}
}
