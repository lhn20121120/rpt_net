package com.fitech.model.etl.service.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


import com.fitech.framework.core.service.impl.DefaultBaseService;
import com.fitech.model.etl.service.IETLJdbcService;
import com.fitech.model.etl.service.IGetResultSetBySQLService;

public class GetResultSetBySQLServiceImpl extends DefaultBaseService implements IGetResultSetBySQLService{
	
	private Connection conn;
	private Statement statement;
	private PreparedStatement pstmt ;
	private ResultSet rSet;
	private IETLJdbcService etlJdbcService;
	
	
	@Override
	public List getListBySQL(String sql) throws Exception {
		List<Object[]> objList = null;
		try {
			open();
			rSet = statement.executeQuery(sql);
			ResultSetMetaData rsmd = rSet.getMetaData();
			int count = rsmd.getColumnCount();
			objList = new ArrayList<Object[]>();
			Object[] columnName = new Object[count];
			for(int i=0;i<count;i++){
				columnName[i] = rsmd.getColumnName(i+1);
			}
			objList.add(columnName);
			while(rSet.next()){
				Object[] objRow = new Object[count];
				for(int i=0;i<count;i++){
					objRow[i] = rSet.getObject(i+1);
				}
				objList.add(objRow);
			}
		} catch (Exception e) {
			throw e;
		}finally{
			close();
		}
		return objList;
	}
	
	
	
	@Override
	public List getListByProc(String proc) throws Exception {
		List<Object[]> objList = null;
		try {
			open();
			String callProc = "{ call "+proc+" }";
			CallableStatement callSate = conn.prepareCall(callProc);
			callSate.execute();
			rSet = callSate.getResultSet();
			if(rSet==null)
				return null;
			
			ResultSetMetaData rsmd = rSet.getMetaData();
			int count = rsmd.getColumnCount();
			objList = new ArrayList<Object[]>();
			Object[] columnName = new Object[count];
			for(int i=0;i<count;i++){
				columnName[i] = rsmd.getColumnName(i+1);
			}
			objList.add(columnName);
			while(rSet.next()){
				Object[] objRow = new Object[count];
				for(int i=0;i<count;i++){
					objRow[i] = rSet.getObject(i+1);
				}
				objList.add(objRow);
			}
		} catch (Exception e) {
			throw e;
		}finally{
			close();
		}
		
		return objList;
	}
	
	
	


	@Override
	public boolean execSQL(String sql ,Connection conn) throws Exception {
		boolean ISSUCCESS = false;
		if(sql.isEmpty()&&sql.trim().equals("")){
			return ISSUCCESS;
		}
		try {	
//			conn = etlJdbcService.createJdbcTemplate().getDataSource().getConnection();
			pstmt = conn.prepareStatement(sql);
			ISSUCCESS = conn.createStatement().execute(sql);
		} catch (Exception e) {
			ISSUCCESS = false;
			throw e;
		}finally{
			if(pstmt!= null){
				pstmt.close();
				pstmt = null;
			}
		}
		return ISSUCCESS;
	}
	
	public boolean execSQL(String sql, String[] params,Connection conn) throws Exception {
		boolean ISSUCCESS = false;
		if(sql.isEmpty()&&sql.trim().equals("")){
			return ISSUCCESS;
		}
//		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
//			conn = etlJdbcService.createJdbcTemplate().getDataSource()
//					.getConnection();
			pstmt = conn.prepareStatement(sql);
			for (int i = 0; params != null && i < params.length; i++)
				pstmt.setString(i + 1, params[i]);
				int ret = pstmt.executeUpdate();
			if (ret != -1) {
				ISSUCCESS = true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			ISSUCCESS = false;
			throw ex;
		} finally {
			if(pstmt!= null){
				pstmt.close();
				pstmt = null;
			}
		}
		return ISSUCCESS;
	}
	

	public List<String> getInsertSQL(String tableName) throws Exception {
		String sql = "select * from "+tableName;
		List<String> insertList = new ArrayList<String>();
		try {
			open();
			rSet = statement.executeQuery(sql);
			ResultSetMetaData rsmd=rSet.getMetaData();
			int count=rsmd.getColumnCount();//获得数据表的列数
			List<String> list=new ArrayList<String>();
			System.out.println("----获取数据结果集");
			for(int i=0;i<count;i++)//遍历数据表的列数
			{	
				//获取列的名称
				String cName=rsmd.getColumnName(i+1);
				//获取列的类型
				int columnType=rsmd.getColumnType(i+1);
				
				switch (columnType) {
				case 4:
				case 2:	
					break;
				case 91://oracle数据库date类型
					cName = cName+"$#@";
					break;
				default:
					cName=cName+"@#$";//如果是字符串等类型 做字符串拼接
					break;
				}		
				list.add(cName);//列名添加进集合
			}	
			System.out.println("----开始生成insert语句");
			while(rSet.next()){//遍历结果集
				String insertsql="insert into "+tableName+"(";//拼接insert 语句
				for(int i=0;i<list.size();i++){//遍历集合
					String name=list.get(i);//获取列名
					if(name.indexOf("@#$")>0)//字符串类型判断
						name=name.substring(0,name.indexOf("@#$"));
					if(name.indexOf("$#@")>0)//oracle数据库date类型
						name=name.substring(0,name.indexOf("$#@"));
					if(i==list.size()-1)//列的最后添加左括号
						insertsql+=name+")";
					else
						insertsql+=name+",";//否则添加逗号
				}
				insertsql+=" VALUES (";
				for(int i=0;i<list.size();i++){
					String name=list.get(i);//获取列名
					String cvalue=null;
					if(name.indexOf("@#$")>0){//字符串类型判断
						name=name.substring(0,name.indexOf("@#$"));
						if(rSet.getObject(name)==null)//为空，则插入NULL数据
							cvalue="NULL";
						else{
							//获取列对应的数据值
							cvalue=rSet.getObject(name).toString().trim();
							cvalue="'"+cvalue+"'";
						}
					}else if(name.indexOf("$#@")>0){//oracle数据库date类型
						name=name.substring(0,name.indexOf("$#@"));
						if(rSet.getObject(name)==null)//为空，则插入NULL数据
							cvalue="NULL";
						else{
							//获取列对应的数据值
							cvalue=rSet.getObject(name).toString().trim();
							cvalue="to_date('"+cvalue+"','yyyy-mm-dd hh24:mi:ss')";
						}	
					}else{//非字符串类型
						if(rSet.getObject(name)==null)
							cvalue="NULL";//为空,则插入NULL数据
						else//获取对应的数据
							cvalue=rSet.getObject(name).toString().trim();
						
					}
					if(i==list.size()-1)//集合的末尾添加右括号
						insertsql+=cvalue+");\n";
					else
						insertsql+=cvalue+",";//否则添加逗号
				}
				
				insertList.add(insertsql);
			}
			System.out.println("----insert语句生成成功");
		} catch (Exception e) {
			throw e;
		}finally{
			close();
		}
		return insertList;
	}

	@Override
	public Connection getConnection() throws SQLException {
		 return etlJdbcService.createJdbcTemplate().getDataSource().getConnection();
	}

	/**
	 * 打开数据库连接
	 * @throws Exception
	 */
	private void open() throws Exception{	
		conn = etlJdbcService.createJdbcTemplate().getDataSource().getConnection();
		statement=conn.createStatement();
		
	}
	/***
	 * 关闭数据库连接
	 */
	public void close(){
		try {
			if(rSet!=null){
				rSet.close();
				rSet = null;
			}
			if(statement!=null){
				statement.close();
				statement = null;
			}	
			if(pstmt!=null){
				pstmt.close();
				pstmt = null;
			}	
			if(conn!=null){
				conn.close();
				conn = null;
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	public IETLJdbcService getEtlJdbcService() {
		return etlJdbcService;
	}



	public void setEtlJdbcService(IETLJdbcService etlJdbcService) {
		this.etlJdbcService = etlJdbcService;
	}



	
	
}
