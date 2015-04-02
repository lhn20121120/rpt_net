package com.fitech.model.etl.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;

import com.fitech.framework.core.service.impl.DefaultBaseService;
import com.fitech.model.etl.common.ETLConfig;
import com.fitech.model.etl.model.pojo.EtlLoadFileInfo;
import com.fitech.model.etl.model.vo.ETLFileInfoDetailDataVo;
import com.fitech.model.etl.model.vo.ETLLoadFileDetailVo;
import com.fitech.model.etl.model.vo.ETLLoadFileInfoVo;
import com.fitech.model.etl.service.IETLJdbcService;
import com.fitech.model.etl.service.IETLLoadFileDetailService;
import com.fitech.model.etl.service.IETLLoadFileInfoService;
import com.fitech.model.worktask.common.WorkTaskConfig;

public class ETLLoadFileInfoServiceImpl extends DefaultBaseService<EtlLoadFileInfo, Integer> implements IETLLoadFileInfoService{
	private IETLJdbcService jdbcService;
	private IETLLoadFileDetailService loadFileDetailService;
	
	
	
	
	
	
	@Override
	/**
	 * 根据任务类型检索数据装载配置
	 * @author 卞以刚 2012-2-21
	 **/
	public List<ETLLoadFileInfoVo> findETLLoadFileInfoByTaskId(
			ETLLoadFileInfoVo etlLoadFileInfoVo) throws Exception{
		//定义数据装载配置实例对象
		EtlLoadFileInfo etlLoadFileInfo=null;
		//定义数据装载配置实例对象集合
		List<EtlLoadFileInfo> list=null;
		//定义数据装载配置业务对象集合
		List<ETLLoadFileInfoVo> voList=null;
		//非空逻辑判断
		if(etlLoadFileInfoVo!=null && etlLoadFileInfoVo.getTaskId()!=null 
				&& !etlLoadFileInfoVo.getTaskId().equals("")){
			//初始化
			etlLoadFileInfo=new EtlLoadFileInfo();
			//转换，将业务数据转换到实例数据
			etlLoadFileInfo.setTaskId(etlLoadFileInfoVo.getTaskId());
			//根据任务id检索
			String hql="from EtlLoadFileInfo e where e.taskId="+etlLoadFileInfo.getTaskId();
			list=this.findListByHsql(hql, null);
		}
		if(list!=null && list.size()>0){
			//初始化业务对象集合
			voList=new ArrayList<ETLLoadFileInfoVo>();
			//遍历
			for (EtlLoadFileInfo e:list) {
				//定义业务对象
				ETLLoadFileInfoVo vo=new ETLLoadFileInfoVo();
				//转换实例对象到业务对象
				BeanUtils.copyProperties(vo, e);
				voList.add(vo);//添加进集合
			}
		}
		return voList;//返回业务对象集合
	}
	
	/***
	 * 根据数据库类型判断，并创建对应视图，根据视图查询用户下面所有的表名
	 */
	@Override
	public List<String> findAllTableNameByUser() throws Exception {
		List<String> nameList=new ArrayList<String>();
		try {
			//createViewSearchTableName();//创建视图
			List<Map<String, String>> tableNameList=new ArrayList<Map<String,String>>();
			String sql = "";
			if(WorkTaskConfig.DB_SERVER_TYPE.equals("sqlserver"))
				sql = "select e.name from  "+ETLConfig.CREATE_VIEW_NAME+" e order by e.name";//查询所图
			if(WorkTaskConfig.DB_SERVER_TYPE.equals("oracle"))
				sql = "select e.table_name from "+ETLConfig.CREATE_VIEW_NAME+" e order by e.table_name";
			tableNameList=jdbcService.createJdbcTemplate().queryForList(sql);
			//tableNameList=this.findListBySql(sql, null);
			for(Map<String, String> s : tableNameList){
				Set<Entry<String, String>> ss=s.entrySet();
				for(Entry<String, String> e : ss){
					if(e.getKey()==null || e.getKey().equals("") )
						continue;
					nameList.add(e.getValue());
				}
			}
		} catch (Exception e) {
			throw e;
		}finally{
			return nameList;//返回
		}
		
	}
	
	/**
	 * 创建查询所有表名称的视图
	 */
	public  void createViewSearchTableName()throws Exception{
		String createViewSql="";
		String dropViewSql="";
		//数据库判断
		if(ETLConfig.DB_SERVER_TYPE.equals("sqlserver"))
		{
			//如果视图存在，则删除
			dropViewSql="IF  EXISTS (SELECT * FROM sys.views WHERE object_id = OBJECT_ID(N'[dbo].["+
						 ETLConfig.CREATE_VIEW_NAME+"]'))DROP VIEW [dbo].["+
						 ETLConfig.CREATE_VIEW_NAME+"]";
			//创建视图
			createViewSql="create view "+ETLConfig.CREATE_VIEW_NAME+" as select name from sysobjects  where xtype = 'u' ";
		}
		if(ETLConfig.DB_SERVER_TYPE.equals("oracle")){
			createViewSql="CREATE   OR   REPLACE view "+ETLConfig.CREATE_VIEW_NAME+" as select table_name from user_tables where tablespace_name='KERONG_SP'";
		}
		try {
			//执行删除sql
			if(!dropViewSql.equals(""))	
				jdbcService.createJdbcTemplate().execute(dropViewSql);

			//执行创建sql
			System.out.println("--------创    建    视   图---------");
			if(!createViewSql.equals(""))
				jdbcService.createJdbcTemplate().execute(createViewSql);
			System.out.println("--------视图创建成功----------");
		} catch (Exception e) {
			throw e;
		}
	}


	@Override
	public List<String> findColumnNamesByTableName(String tableName)
			throws Exception {
		String sql="";
		//数据库判断
		if(ETLConfig.DB_SERVER_TYPE.equals("sqlserver"))
			sql="select name from syscolumns where id=object_id('"+tableName+"')";
		if(ETLConfig.DB_SERVER_TYPE.equals("oracle"))
			sql="select table_name from all_tables where table_name='"+tableName+"'";
		if(sql=="")
			throw new Exception();
		
		List<Map<String, String>> mapList=null;
		List<String> columnList=null;
		mapList=jdbcService.createJdbcTemplate().queryForList(sql);
		if(mapList==null || mapList.size()==0)
			return null;
		
		columnList=new ArrayList<String>();
		for(Map<String, String> m : mapList){
			Set<Entry<String, String>> s=m.entrySet();
			for(Entry<String, String> e : s){
				if(e.getKey()==null || e.getKey().equals(""))
					continue;
				columnList.add(e.getValue());
			}
		}
		return columnList;
	}

	/***
	 * 有主键则更新
	 * 无主键则保存
	 */
	@Override
	public Integer saveOrUpdateFileInfoByFileId(ETLLoadFileInfoVo vo) throws Exception {
		if(vo==null)
			throw new Exception();
		EtlLoadFileInfo info=new EtlLoadFileInfo();
		//主键为空 执行增加操作
		if(vo.getFileId()==null || vo.getFileId().equals("")){
			changeDataByCallTypeId(vo);
			BeanUtils.copyProperties(info, vo);
			this.save(info);
			
		}else{//主键不为空 执行更新操作		
			//根据主键检索单个对象
			ETLLoadFileInfoVo infoVo=this.findETLLoadFileInfoByFileId(vo.getFileId());
			BeanUtils.copyProperties(info, infoVo);//将检索出来的业务对象和实例对象交换
			changeETLLoadFileInfo(vo, info);//将形参业务对象和实例对象数据交换
			changeDataByCallTypeId(vo);
			info=new EtlLoadFileInfo();//重新实例化实例对象
			BeanUtils.copyProperties(info, vo);//讲形参业务对象和实例对象交换
			this.objectDao.getHibernateTemplate().merge(info);//执行更新
		}
		return info.getFileId();
	}
	
	/***
	 * 根据数据装载模式 选择清空相应数据
	 * @param vo
	 */
	public void changeDataByCallTypeId(ETLLoadFileInfoVo vo){
		if(vo.getCallTypeId()==null)//装载方式为null 说明并为设置具体装载方式
			return;
		switch(vo.getCallTypeId()){
			case 0://数据装载模式 0为通用模式
				vo.setCustomTypeCode(null);//清空转换模式和调用文件
				vo.setCustomWay(null);
			break;
			case 1://自定义方式
				vo.setFileTypeCode(null);//文本类型
				vo.setStartRow(null);//起始行
				vo.setFileSeper(null);//列分隔符
				vo.setTableCode(null);//数据表
				vo.setRecordsNum(null);//提交记录数	
			break;
		}
	}
	
	
	/***
	 * 保存数据源文件信息和文件列字段信息
	 */
	@Override
	public Integer saveOrUpdateFileInfo(ETLLoadFileInfoVo vo,
			List<ETLLoadFileDetailVo> detailVoList) throws Exception {
		//数据装载模式为脚本执行
		if(vo.getCustomTypeCode()!=null && vo.getCustomTypeCode().equals(ETLConfig.CUSTOM_TYPE_SCRIPT) && vo.getIsChange()){
			String cutomWay = vo.getCustomWay();
			//String fileName = uploadFileByPath(vo.getCustomWay());
			//vo.setCustomWay(fileName);
		}
		/**文件列字段信息为空,则只保存数据源文件信息*/
		if(detailVoList==null || detailVoList.size()==0)
			return saveOrUpdateFileInfoByFileId(vo);
		//获得保存后的文件ID
		Integer fileId;
		try {
			fileId = saveOrUpdateFileInfoByFileId(vo);
			
			//遍历集合
			//先根据任务ID删除文件列字段信息
			List<ETLLoadFileDetailVo> voList=loadFileDetailService.findETLLoadFileDetailByFileId(fileId);
			if(voList!=null && voList.size()>=0){	
				for(ETLLoadFileDetailVo v : voList){
					if(v!=null)
						loadFileDetailService.deleteETLLoadFileDetailByFileId(v.getId().getFileId(),v.getId().getFileColumnId());
				}
			}
			if(vo.getCallTypeId()==0){//数据装载模式 0为通用模式
				for(ETLLoadFileDetailVo d : detailVoList){
					d.getId().setFileId(fileId);
					//执行保存文件列字段信息操作
					loadFileDetailService.saveOrUpdateETLLoadFileDetail(d);
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return fileId;//返货保存后的信息
	}


	/***
	 * 交换实例对象和业务对象的数据
	 * @param info
	 * @param vo
	 */
	private void changeETLLoadFileInfo(ETLLoadFileInfoVo vo,EtlLoadFileInfo info){
		if(vo.getCallTypeId()==null || vo.getCallTypeId().equals(""))
			vo.setCallTypeId(info.getCallTypeId());
		if(vo.getCustomTypeCode()==null || vo.getCustomTypeCode().equals(""))
			vo.setCustomTypeCode(info.getCustomTypeCode());
		if(vo.getCustomWay()==null || vo.getCustomWay().equals(""))
			vo.setCustomWay(info.getCustomWay());
		if(vo.getServerId()==null || vo.getServerId()==null)
			vo.setServerId(info.getServerId());
		if(vo.getFileTypeCode()==null || vo.getFileTypeCode().equals(""))
			vo.setFileTypeCode(info.getFileTypeCode());
		if(vo.getFileName()==null || vo.getFileName().equals(""))
			vo.setFileName(info.getFileName());
		if(vo.getFilePath()==null || vo.getFilePath().equals(""))
			vo.setFilePath(info.getFilePath());
		if(vo.getFileSeper()==null || vo.getFileSeper().equals(""))
			vo.setFileSeper(info.getFileSeper());
		if(vo.getRecordsNum()==null || vo.getRecordsNum()<=0)
			vo.setRecordsNum(info.getRecordsNum());
		if(vo.getStartRow()==null || vo.getStartRow()<=0)
			vo.setStartRow(info.getStartRow());
		if(vo.getTableCode()==null || vo.getTableCode().equals(""))
			vo.setTableCode(info.getTableCode());
		if(vo.getTaskId()==null || vo.getTaskId()<=0)
			vo.setTaskId(info.getTaskId());
	}

	
	/***
	 * 根据ID检索装载配置
	 */
	@Override
	public ETLLoadFileInfoVo findETLLoadFileInfoByFileId(Integer fileId)
			throws Exception {
		if(fileId==null || fileId.equals(""))
			return null;
		//hql
		String hql="from EtlLoadFileInfo fo where fo.fileId="+fileId;
		EtlLoadFileInfo info=null;
		Object obj=this.findObject(hql);//查询单个对象
		if(obj==null)
			throw new  Exception();
		info=(EtlLoadFileInfo)obj;//转型
		ETLLoadFileInfoVo vo=new ETLLoadFileInfoVo();
		BeanUtils.copyProperties(vo, info);//复制
		return vo;//返回
	}
	
	
	/***
	 * 删除文件装载信息
	 * 一并删除文件列字段信息
	 */
	@Override
	public void deleteETLLoadFileInfoVoAndDetailByFileId(Integer fileId,List<Integer> columnId)
			throws Exception {
		if(fileId==null || fileId<=0)
			throw new Exception();
		EtlLoadFileInfo info=new EtlLoadFileInfo();
		info.setFileId(fileId);
		this.delete(info);//删除文件装载信息
		//删除文件列字段信息
		
		for(Integer c : columnId){
			if(c==null || c.equals(""))
				continue;
			loadFileDetailService.deleteETLLoadFileDetailByFileId(fileId,c);
		}
	}
	
	
	/***
	 * 根据主键查询数据装载信息和列字段信息
	 */
	@Override
	public ETLFileInfoDetailDataVo findETLFileInfoDetailDataVoByFileId(Integer fileId)
			throws Exception {
		ETLLoadFileInfoVo fileInfoVo=null;
		ETLLoadFileDetailVo detailVo=null;
		//根据主键查出单个数据装载信息
		fileInfoVo=this.findETLLoadFileInfoByFileId(fileId);
		if(fileInfoVo==null)
			throw new Exception();
		//实例化对象
		ETLFileInfoDetailDataVo dataVo=new ETLFileInfoDetailDataVo();
		//将单个数据装载信息放入该对象
		dataVo.setLoadFileInfoVo(fileInfoVo);
		//定义列字段信息集合
		List<ETLLoadFileDetailVo> detailVoList=null;
		//根据fileId查询出列字段信息集合
		detailVoList=loadFileDetailService.findETLLoadFileDetailByFileId(fileId);
		//为空 直接返回
		if(detailVoList==null || detailVoList.size()==0)
			return dataVo;
		//将集合添加进对象
		dataVo.setDetailVoList(detailVoList);
		return dataVo;//返回
	}
	
	
	@Override
	public void upLoadFile(File file,String fileName){
		File outFile = null;
		BufferedOutputStream writer = null;
		BufferedInputStream reader = null;
		
		try {
			if(fileName!=null && !fileName.equals("")){
				String path = ETLConfig.SCRIPT_PATH+File.separator+fileName;//拼接文件写入路径
				System.out.println("------开始上传文件...------");
				System.out.println("--文件名为："+fileName+",上传的文件路径为："+path+"--");
				outFile = new File(path);
				if(outFile.exists())//文件存在，则先删除该文件
					outFile.delete();
				reader = new BufferedInputStream(new FileInputStream(file));
				writer = new BufferedOutputStream(new FileOutputStream(outFile));
				//String readerS = "";
				byte[] buffer =new byte[reader.available()];
				reader.read(buffer);
				writer.write(buffer);
				writer.flush();
				System.out.println("------文件上传成功------");
				
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{//释放资源
			try {
				if(writer!=null){
					writer.close();
					writer = null;
				}
				if(reader!=null){
					reader.close();
					reader = null;
				}
				if(outFile!=null){
					outFile  = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
	}

	public IETLLoadFileDetailService getLoadFileDetailService() {
		return loadFileDetailService;
	}


	public void setLoadFileDetailService(
			IETLLoadFileDetailService loadFileDetailService) {
		this.loadFileDetailService = loadFileDetailService;
	}


	public IETLJdbcService getJdbcService() {
		return jdbcService;
	}


	public void setJdbcService(IETLJdbcService jdbcService) {
		this.jdbcService = jdbcService;
	}
	
}
