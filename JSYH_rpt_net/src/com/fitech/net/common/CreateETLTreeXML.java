package com.fitech.net.common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.FitechConnection;
import com.fitech.net.hibernate.SysIbmSysrels;
import com.fitech.net.hibernate.VParameter;

public class CreateETLTreeXML {

	private static CreateETLTreeXML createETLTreeXML = null;
	
	private CreateETLTreeXML(){}
	
	public static CreateETLTreeXML getInstance(){
		if(createETLTreeXML == null){
			createETLTreeXML = new CreateETLTreeXML();
		}
		return createETLTreeXML;
	}
	
	/**创建XML文件**/
	public boolean createXML() throws SQLException,Exception{
		boolean bool = false;
		
		String fileName = com.cbrc.smis.common.Config.WEBROOTPATH + "xml" + File.separator + "ETL.xml" ;
		File file = new File(fileName);
		if(!file.exists()){
			Connection conn=null;			
			try{				 
				conn = (new FitechConnection()).getConnect(); //获取数据库连接
				
				/**创建一个XML**/
				org.dom4j.Document document = org.dom4j.DocumentHelper.createDocument();
				document.setXMLEncoding("GBK");
				
				Element rootElement = document.addElement("tree");
				rootElement.addAttribute("id", "0");

				/**增加事实表的节点**/
				Element factElement = rootElement.addElement("item");
				factElement.addAttribute("text","事实表");
				factElement.addAttribute("id","N_"+Config.FACTTABLE+"_");
				factElement.addAttribute("open","1");
				/**
				 * 找出所有事实表的节点
				 */
				this.getSubNode(Config.FACTTABLE,conn,factElement);
				
				/**增加指标表的节点**/
				Element targetElement = rootElement.addElement("item");
				targetElement.addAttribute("text","指标表");
				targetElement.addAttribute("id","N_"+Config.TARGETTABLE+"_");
				targetElement.addAttribute("open","1");
				//getNode(operator.getOrgId(), session, oneElement);
				
				try {
					OutputFormat format = OutputFormat.createPrettyPrint();
					format.setEncoding("GBK");
					XMLWriter output = new XMLWriter(
							new FileWriter(new File(fileName)), format);
					output.write(document);
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}				
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				close(conn);
			}	
		}else 
			bool = true;
		return bool;
	}
	
	/**关闭数据库连接**/
	private static void close(Connection conn) throws Exception{
		if(conn!=null){
			try{
				conn.close();
			}catch(SQLException sqle){
				throw new Exception(sqle.getMessage());
			}
		}
	}
	
	/** 查找"事实表"下的所有事实表
	 * @param vttId
	 * @param rootElement
	 * @param conn
	 */
	private void getSubNode(String vttId,Connection conn,Element rootElement) {
		try {
			List nodeList = this.getNodes(conn,vttId);
			if(nodeList != null && nodeList.size() > 0){
				for(int i=0;i<nodeList.size();i++){
					VParameter vp = (VParameter)nodeList.get(i);
					try {
						/**
						 * 列出事实表下的所有字段
						 */
						this.addFactTableChild(rootElement,vp,conn);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 添加事实表节点
	 * @param e
	 * @param vp
	 * @param conn
	 */
	private void addFactTableChild(Element e,VParameter vp,Connection conn) {
		try{
			if(vp != null) {
				Element element = e;
				element = e.addElement("item");
				element.addAttribute("text",vp.getVpTabledesc());
				element.addAttribute("id","N_"+vp.getVttId()+"_"+vp.getVpTabledesc());				
				/**获取事实表对应的字段**/
				List factColumnsList = this.getFactColumnNodes(conn,vp.getVpTableid());

				if (factColumnsList != null && factColumnsList.size() >0) {
					for (int i=0;i<factColumnsList.size();i++){
						VParameter vParameter = (VParameter)factColumnsList.get(i);
						Element child = element.addElement("item");
						child.addAttribute("text",vParameter.getVpColumndesc());
						int type = 1;
						if(vp.getVpColtype() != null) type = vp.getVpColtype().intValue();
						child.addAttribute("id","Y_"+vp.getVttId()+"_"+type+"_"+vp.getVpTabledesc()+"."+vParameter.getVpColumndesc());
					}
				}
				/**列出某个事实表对应的所有维度表**/
				Element wdElement = element.addElement("item");
				wdElement.addAttribute("text",vp.getVpTabledesc()+".维度表");
				wdElement.addAttribute("id","N_"+Config.WEIDUTABLE+"_"+vp.getVpTabledesc()+".维度表");	
				
				/**根据系统表找出事实表对应的所有维度表**/
				List wdTableNodeList = this.getWDTableNodes(conn,vp.getVpTableid());
				if(wdTableNodeList != null && wdTableNodeList.size() > 0){
					for(int i=0;i<wdTableNodeList.size();i++){
						SysIbmSysrels sysIbmSysrels = (SysIbmSysrels)wdTableNodeList.get(i);
						/**根据系统表的映射表名找到对应的维度表的详细信息**/
						VParameter wdVParameter = this.getVParameterByTableId(conn,sysIbmSysrels.getRefTBName());						
						if(wdVParameter != null){							
							List zwdTableNodeList = this.getWDTableNodes(conn,wdVParameter.getVpTableid());
							if(zwdTableNodeList != null && zwdTableNodeList.size() > 0){
								this.addZWDTableChild(wdElement,vp,wdVParameter,conn,zwdTableNodeList);
							}else
								/**添加事实表对应维度表及所有字段**/
								this.addWDTableChild(wdElement,vp,wdVParameter,conn);
						}
					}
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}		
	}

	/**
	 * 增加上下级维度的关系
	 * @param e
	 * @param fatherVParameter 事实表
	 * @param conn
	 * @param list
	 */
	private void addZWDTableChild(Element e,VParameter fatherVParameter,VParameter vpSelf,Connection conn,List list){
		try{
			if(list != null && list.size() > 0){
				for(int i=0;i<list.size();i++){
					SysIbmSysrels sysIbmSysrels = (SysIbmSysrels)list.get(i);
					VParameter wdVParameter = this.getVParameterByTableId(conn,sysIbmSysrels.getRefTBName());
					/**根据维度表找到与之相关的维度表**/
					List zwdTableNodeList = this.getWDTableNodes(conn,wdVParameter.getVpTableid());
					if(zwdTableNodeList != null && zwdTableNodeList.size() > 0){
						/**递归找出维度的多维度**/
						this.addZWDTableChild(e,fatherVParameter,wdVParameter,conn,zwdTableNodeList);
					}else{						
						Element element = e.addElement("item");
						element.addAttribute("text",wdVParameter.getVpTabledesc());
						element.addAttribute("id","N_"+wdVParameter.getVttId()+"_"+fatherVParameter.getVpTabledesc()+"."+vpSelf.getVpTabledesc()+"."+wdVParameter.getVpTabledesc());
						
						//String condition = sysIbmSysrels.getPkColNames();						
						List zwdColumnNodeList = this.getFactColumnNodes(conn,wdVParameter.getVpTableid());
						if(zwdColumnNodeList != null && zwdColumnNodeList.size() > 0){
							for(int j=0;j<zwdColumnNodeList.size();j++){
								VParameter zwdVParameter = (VParameter)zwdColumnNodeList.get(j);								
								Element zwdColumnElement = element.addElement("item");
								zwdColumnElement.addAttribute("text",zwdVParameter.getVpColumndesc());
								int type = 1;
								if(zwdVParameter.getVpColtype() != null) type = zwdVParameter.getVpColtype().intValue();
								zwdColumnElement.addAttribute("id","Y_"+zwdVParameter.getVttId()+"_"+type+"_"+fatherVParameter.getVpTabledesc()+"_"+vpSelf.getVpTabledesc()+"_"+zwdVParameter.getVpTabledesc()+"."+zwdVParameter.getVpColumndesc());
								
								/**找出对应列的所有值**/
								List wdColumnValues = this.getWDColumnValue(conn,zwdVParameter);
								if(wdColumnValues != null && wdColumnValues.size() > 0){									
									int k = 0; //对于上级标志相同的来作标识
									for(Iterator iter=wdColumnValues.iterator();iter.hasNext();){
										Object object = iter.next();
										if(object == null || object.toString().equals("")) continue;
										Element zwdColumnValueElement = zwdColumnElement.addElement("item");
										zwdColumnValueElement.addAttribute("text",object.toString().trim());
										zwdColumnValueElement.addAttribute("id","Y_4_"+type+"_"+fatherVParameter.getVpTabledesc()+"."+vpSelf.getVpTabledesc()+"."+zwdVParameter.getVpColumndesc()+"."+k+"_"+object.toString().trim());
										
										/**增加二维度的表名**/
										Element zzwdTableElement = zwdColumnValueElement.addElement("item");
										zzwdTableElement.addAttribute("text",vpSelf.getVpTabledesc());										
										zzwdTableElement.addAttribute("id","N_"+vpSelf.getVttId()+"_"+type+"_"+fatherVParameter.getVpTabledesc()+"."+vpSelf.getVpTabledesc()+"."+zwdVParameter.getVpColumndesc()+"."+k);
										
										List zzwdColumnList = this.getFactColumnNodes(conn,vpSelf.getVpTableid());
										if(zzwdColumnList != null && zzwdColumnList.size() > 0){
											for(Iterator zzwdIter=zzwdColumnList.iterator();zzwdIter.hasNext();){
												VParameter zzwdVParameter = (VParameter)zzwdIter.next();
												Element zzwdColumnElement = zzwdTableElement.addElement("item");
												zzwdColumnElement.addAttribute("text",zzwdVParameter.getVpColumndesc());
												int zzType = 1;
												if(zzwdVParameter.getVpColtype() != null) zzType = zzwdVParameter.getVpColtype().intValue();
												zzwdColumnElement.addAttribute("id","Y_"+zzwdVParameter.getVttId()+"_"+zzType+"_"+zwdVParameter.getVpColumndesc()+"."+k+"_"+zzwdVParameter.getVpTabledesc()+"."+zzwdVParameter.getVpColumndesc());
												
												/**找出二维度对应字段的值（条件是等于一维里该字段的值）**/
												String value = object.toString().trim();
												if(type == Config.WDCOLUMNTYPECHAR.intValue()) value = "'" + value + "'";
												/**根据字段值找出对应的主ID**/
												String condition = "select "+sysIbmSysrels.getPkColNames()+" from "+zwdVParameter.getVpTableid()+" where "+zwdVParameter.getVpColumnid()+"="+value;
												//String condition = object.toString().trim();
												//if(type == Config.WDCOLUMNTYPECHAR.intValue())condition = "'" + value + "'";
												
												List zzwdColumnValues = this.getZWDColumnValue(conn,sysIbmSysrels.getFkColNames(),zzwdVParameter,condition);
												if(zzwdColumnValues != null && zzwdColumnValues.size() > 0){
													for(int n=0;n<zzwdColumnValues.size();n++){
														Object zObject = zzwdColumnValues.get(n);
														if(zObject == null || zObject.toString().equals("")) continue;
														Element zzwdColumnValueElement = zzwdColumnElement.addElement("item");
														zzwdColumnValueElement.addAttribute("text",zObject.toString().trim());
														zzwdColumnValueElement.addAttribute("id","Y_4_"+zzType+"_"+fatherVParameter.getVpTabledesc()+"."+vpSelf.getVpTabledesc()+"."+zwdVParameter.getVpColumndesc()+"."
																+zzwdVParameter.getVpTabledesc()+"."+zzwdVParameter.getVpColumndesc()+"."+n+"."+object.toString()+"_"+zObject.toString().trim());
													}
												}
											}
										}
										k++;
									}
								}
							}
						}
						
					}
					
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * 添加维度表及维度表所有字段
	 * @param e
	 * @param fatherVParameter
	 * @param vp
	 * @param conn
	 */
	private void addWDTableChild(Element e,VParameter fatherVParameter,VParameter vp,Connection conn) {
		if(vp != null) {
			Element element = e.addElement("item");
			element.addAttribute("text",vp.getVpTabledesc());
			element.addAttribute("id","N_"+vp.getVttId()+"_"+fatherVParameter.getVpTabledesc()+"."+vp.getVpTabledesc());
			
			try {
				/**根据维度表名查出维度表的所有字段**/
				List wdColumnNodeList = this.getFactColumnNodes(conn,vp.getVpTableid());
				if(wdColumnNodeList != null && wdColumnNodeList.size() > 0){
					for(int i=0;i<wdColumnNodeList.size();i++){
						VParameter wdVParameter = (VParameter)wdColumnNodeList.get(i);
						Element wdColumnElement = element.addElement("item");
						wdColumnElement.addAttribute("text",wdVParameter.getVpColumndesc());
						int type = 1;
						if(wdVParameter.getVpColtype() != null) type = wdVParameter.getVpColtype().intValue();
						wdColumnElement.addAttribute("id","Y_"+wdVParameter.getVttId()+"_"+type+"_"+wdVParameter.getVpTabledesc()+"."+wdVParameter.getVpColumndesc());
						
						/**找出对应列的所有值**/
						List wdColumnValues = this.getWDColumnValue(conn,wdVParameter);
						if(wdColumnValues != null && wdColumnValues.size() > 0){
							int j = 0; //对于上级标志相同的来作标识
							for(Iterator iter=wdColumnValues.iterator();iter.hasNext();){
								Object object = iter.next();
								if(object == null || object.toString().equals("")) continue;
								Element wdColumnValueElement = wdColumnElement.addElement("item");
								wdColumnValueElement.addAttribute("text",object.toString().trim());
								wdColumnValueElement.addAttribute("id","Y_4_"+type+"_"+fatherVParameter.getVpTabledesc()+"."+wdVParameter.getVpColumndesc()+"."+j+"_"+object.toString().trim());
								j++;
							}							
						}
					}
				}
				
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	
	/**
	 * 根据维度表的表名、字段找到字段值
	 * @param conn
	 * @param vp
	 * @return
	 */
	private List getWDColumnValue(Connection conn,VParameter vp) throws Exception{
		List valuesList = null;
		if(conn == null) return valuesList;
		
		PreparedStatement stmt=null;		
		ResultSet rs=null;
		
		try{		
			String sql="select " + vp.getVpColumnid() + " from " + vp.getVpTableid() + "";			
			stmt=conn.prepareStatement(sql);			

			if(stmt.execute()==true){
				valuesList = new ArrayList();
				rs = stmt.getResultSet();
				
				while(rs.next()){
					if(vp.getVpColtype() != null && vp.getVpColtype().intValue() == Config.WDCOLUMNTYPENUMBER.intValue()){
						valuesList.add(String.valueOf(rs.getInt(vp.getVpColumnid())));
					}else
						valuesList.add(rs.getString(vp.getVpColumnid()));
				}
			}			
		}catch(SQLException sqle){		
			valuesList = null;			
			sqle.printStackTrace();		
		}catch(Exception e){				
			valuesList = null;			
			e.printStackTrace();		
		}finally{		
			if(rs!=null) rs.close();				
			if(stmt!=null) stmt.close();	
		}
		return valuesList;
	}
	
	/**
	 * 根据维度表的表名、字段找到字段值(传入一级维度的子段值)
	 * @param conn
	 * @param vp
	 * @return
	 */
	private List getZWDColumnValue(Connection conn,String column,VParameter vp,String condtion) throws Exception{
		List valuesList = null;
		if(conn == null) return valuesList;
		
		PreparedStatement stmt=null;		
		ResultSet rs=null;
		
		try{		
			String sql="select distinct " + vp.getVpColumnid() + " from " + vp.getVpTableid() + " where " + column + " in ("+ condtion + ")";	
			//String sql="select " + vp.getVpColumnid() + " from " + vp.getVpTableid() + " where " + column + "=" + condtion;
			stmt=conn.prepareStatement(sql);			

			if(stmt.execute()==true){
				valuesList = new ArrayList();
				rs = stmt.getResultSet();
				
				while(rs.next()){
					if(vp.getVpColtype() != null && vp.getVpColtype().intValue() == Config.WDCOLUMNTYPENUMBER.intValue()){
						valuesList.add(String.valueOf(rs.getInt(vp.getVpColumnid())));
					}else
						valuesList.add(rs.getString(vp.getVpColumnid()));
				}
			}			
		}catch(SQLException sqle){		
			valuesList = null;			
			sqle.printStackTrace();		
		}catch(Exception e){				
			valuesList = null;			
			e.printStackTrace();		
		}finally{		
			if(rs!=null) rs.close();				
			if(stmt!=null) stmt.close();	
		}
		return valuesList;
	}
	
	/**
	 * 根据主表名从系统表中找出相对应的维度表
	 * @param conn
	 * @param vpTableId
	 * @return
	 * @throws Exception
	 */
	private List getWDTableNodes(Connection conn,String vpTableId) throws Exception{
		List nodeList = null;
		if(conn==null) return nodeList;

		PreparedStatement stmt=null;		
		ResultSet rs=null;
		
		try{		
			String sql="select * from SYSIBM.SYSRELS sys where sys.tbname = ?";			
			stmt=conn.prepareStatement(sql);			
			stmt.setString(1,vpTableId.toUpperCase());

			if(stmt.execute()==true){
				nodeList = new ArrayList();
				rs = stmt.getResultSet();
				
				while(rs.next()){
					SysIbmSysrels sysIbmSysrels = new SysIbmSysrels();
					sysIbmSysrels.setTbName(rs.getString("tbname"));
					sysIbmSysrels.setRefTBName(rs.getString("reftbname"));
					sysIbmSysrels.setFkColNames(rs.getString("fkcolnames"));
					sysIbmSysrels.setPkColNames(rs.getString("pkcolnames"));
					nodeList.add(sysIbmSysrels);
				}
			}			
		}catch(SQLException sqle){		
			nodeList = null;			
			sqle.printStackTrace();		
		}catch(Exception e){				
			nodeList = null;			
			e.printStackTrace();		
		}finally{		
			if(rs!=null) rs.close();				
			if(stmt!=null) stmt.close();	
		}
		
		return nodeList;		
	}

	/**
	 * 根据事实表的ID查找对应的事实表的字段
	 * @param conn
	 * @param vpTableId
	 * @return
	 * @throws Exception
	 */
	private List getFactColumnNodes(Connection conn,String vpTableId) throws Exception{
		List nodeList = null;
		if(conn==null) return nodeList;

		PreparedStatement stmt=null;		
		ResultSet rs=null;
	
		try{		
			String sql="select * from V_PARAMETER vp where vp.vp_tableid = ?";			
			stmt=conn.prepareStatement(sql);			
			stmt.setString(1,vpTableId);

			if(stmt.execute()==true){
				nodeList = new ArrayList();
				rs = stmt.getResultSet();
				
				while(rs.next()){
					VParameter vp = new VParameter();
					vp.setVpId(new Integer(rs.getInt("vp_id")));
					vp.setVpColumnid(rs.getString("vp_columnid"));
					vp.setVpColumndesc(rs.getString("vp_columndesc"));
					vp.setVpTableid(rs.getString("vp_tableid"));
					vp.setVpTabledesc(rs.getString("vp_tabledesc"));
					vp.setVttId(rs.getString("vtt_id"));
					vp.setVpNote(rs.getString("vp_note"));
					try{
						vp.setVpColtype(new Integer(rs.getInt("vp_coltype")));
					}catch(Exception ex){						
					}
					
					nodeList.add(vp);
				}
			}			
		}catch(SQLException sqle){		
			nodeList = null;			
			sqle.printStackTrace();		
		}catch(Exception e){				
			nodeList = null;			
			e.printStackTrace();		
		}finally{		
			if(rs!=null) rs.close();				
			if(stmt!=null) stmt.close();	
		}
		
		return nodeList;		
	}
	
	/**
	 * 根据表ID找到参数表相关信息
	 * @param conn
	 * @param vpTableId
	 * @return
	 * @throws Exception
	 */
	private VParameter getVParameterByTableId(Connection conn,String vpTableId) throws Exception{
		VParameter vp = null;
		if(conn==null) return vp;

		PreparedStatement stmt=null;		
		ResultSet rs=null;
	
		try{		
			String sql="select * from V_PARAMETER";			
			stmt=conn.prepareStatement(sql);

			if(stmt.execute()==true){
				rs = stmt.getResultSet();
				
				while(rs.next()){
					if(vpTableId.equalsIgnoreCase(rs.getString("vp_tableid"))){
						vp = new VParameter();
						vp.setVpId(new Integer(rs.getInt("vp_id")));
						vp.setVpColumnid(rs.getString("vp_columnid"));
						vp.setVpColumndesc(rs.getString("vp_columndesc"));
						vp.setVpTableid(rs.getString("vp_tableid"));
						vp.setVpTabledesc(rs.getString("vp_tabledesc"));
						vp.setVttId(rs.getString("vtt_id"));
						vp.setVpNote(rs.getString("vp_note"));
						break;
					}
				}
			}			
		}catch(SQLException sqle){		
			vp = null;			
			sqle.printStackTrace();		
		}catch(Exception e){				
			vp = null;		
			e.printStackTrace();		
		}finally{		
			if(rs!=null) rs.close();				
			if(stmt!=null) stmt.close();	
		}
		return vp;
	}
	
	/**
	 * 根据表类型查找出该类型对应的表
	 * @param conn
	 * @param vttId
	 * @return
	 * @throws Exception
	 */
	private List getNodes(Connection conn,String vttId) throws Exception{
		List nodeList = null;
		if(conn==null) return nodeList;

		PreparedStatement stmt=null;		
		ResultSet rs=null;
	
		try{		
			String sql="select distinct vp_tabledesc,vp_tableid,vtt_id from V_PARAMETER vp where vp.VTT_ID = ?";			
			stmt=conn.prepareStatement(sql);			
			stmt.setString(1,vttId);

			if(stmt.execute()==true){
				nodeList = new ArrayList();
				rs = stmt.getResultSet();
				
				while(rs.next()){
					VParameter vp = new VParameter();
					vp.setVpTableid(rs.getString("vp_tableid"));
					vp.setVpTabledesc(rs.getString("vp_tabledesc"));
					vp.setVttId(rs.getString("vtt_id"));
					nodeList.add(vp);
				}
			}			
		}catch(SQLException sqle){		
			nodeList = null;			
			sqle.printStackTrace();		
		}catch(Exception e){				
			nodeList = null;			
			e.printStackTrace();		
		}finally{		
			if(rs!=null) rs.close();				
			if(stmt!=null) stmt.close();	
		}
		
		return nodeList;		
	}
	public static void main(String[] args){
		try{
			CreateETLTreeXML createETLTreeXML = CreateETLTreeXML.getInstance();
			createETLTreeXML.createXML();
			// System.out.println("kkkk");
		}catch(Exception e){e.printStackTrace();}
	}
}
