package com.fitech.model.commoninfo.action;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;

import com.fitech.framework.comp.file.properties.FitechProperties;
import com.fitech.framework.core.common.Config;
import com.fitech.framework.core.service.BaseServiceException;
import com.fitech.framework.core.util.DateUtil;
import com.fitech.framework.core.util.StringUtil;
import com.fitech.framework.core.web.PageResults;
import com.fitech.framework.core.web.action.DefaultBaseAction;
import com.fitech.model.commoninfo.common.CommonInfoConfig;
import com.fitech.model.commoninfo.model.pojo.TMsgInfo;
import com.fitech.model.commoninfo.service.ITMsgInfoService;
import com.fitech.model.worktask.security.Operator;
import com.fitech.model.worktask.vo.WorkTaskOrgInfoVo;
import com.googlecode.jsonplugin.annotations.JSON;

public class TMsgInfoAction  extends DefaultBaseAction{
	public ITMsgInfoService tmsgService;//信息服务类
	public PageResults<TMsgInfo> msgInfoResult;//信息集合
	private Map<String, String> orgclsMap;//机构类型集合
//	private List<AfOrg> orglist;//机构集合
	private List<WorkTaskOrgInfoVo> orglist;//机构集合
	private JSONObject json;
//	private List<TOrgcls> orgclsLst;
	private List orgclsLst;
	private PageResults<TMsgInfo> pubMsgResult;//公告信息
	private PageResults<TMsgInfo> modelResult;//草稿信息
	private Integer notReadCount;//信息未读的个数
	private TMsgInfo tmsgInfo;//消息
	private Integer notReadModelCount;//草稿的个数
	private File file;//附件
	private String fileName;
	private String type;//判断哪个tabs显示
	//防止分页冲突，单独设置页数
	private Integer msgInfoResultpageNo=1;
	private Integer pubMsgResultpageNo=1;
	private Integer modelResultpageNo=1;
	private InputStream inputStream;
	private String mimeType;
	private String whereSql;//收件箱邮件查询条件
	private String pubWhereSql;//公告查询条件
	
	
	/***
	 * 检索自己的收件箱的邮件
	 * @return
	 */
	public String findMsgByOwn(){
		//获得登录用户
		HttpSession session = this.getHttpSession();
		Operator operator = (Operator) session.getAttribute(CommonInfoConfig.OPERATOR_SESSION_NAME);
		//获取userid
		String userId = operator.getOperatorId()+"";
		try {
			//已读消息
			if(pubMsgResult == null)
				pubMsgResult = new PageResults<TMsgInfo>();

		
			//查出公告信息
			pubMsgResult = tmsgService.findPubMsg(pageSize,pubMsgResultpageNo,operator.getOperatorId()+"");
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "msgview";
	}
	
	
	public String findOrgcls(){
		orgclsMap = getOrgclsMap();
		json = JSONObject.fromObject(orgclsMap);
		return "root";
	}
	/***
	 * 删除消息
	 */
	public String deleteMsg(){
		boolean flag=false;
		String msgId=(String)this.getRequest().getParameter("msgId");
		String hql="from TMsgInfo t where t.msgId="+msgId;
		try {
			TMsgInfo msgInfo=(TMsgInfo)tmsgService.findObject(hql);
			tmsgService.delete(msgInfo);
			this.getRequest().setAttribute("alertMsg", "删除成功");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			flag=false;
			this.getRequest().setAttribute("alertMsg", "删除失败");
		}
		return findMsgByOwn();
	}
	/***
	 * 删除已发送消息
	 */
	public void deletePublishMsg(){
		boolean flag=false;
		String msgId=(String)this.getRequest().getParameter("msgId");
		String hql="from TMsgInfo t where t.msgId="+msgId;
		try {
			TMsgInfo msgInfo=(TMsgInfo)tmsgService.findObject(hql);
			tmsgService.delete(msgInfo);
			this.getResponse().getWriter().print(1);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			try {
				this.getResponse().getWriter().print(0);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}
	/***
	 * 得到不重复的字符串
	 */
	public void getNoDuplicateUserNames(){
		String rtnStr="";
		String userNames=(String)this.getRequest().getParameter("userNames");
		String[] arr=null;
		if (!"".equals(userNames)) {
			
			arr=userNames.split(";");
		}
		Set<String> set=new HashSet<String>();
		for (int i = 0; arr!=null&&i < arr.length; i++) {
			set.add(arr[i]);
		}
		Iterator<String> it=set.iterator();
		while (it.hasNext()) {
			
			if(!"".equals(rtnStr)) rtnStr+=";";
			rtnStr+=it.next();
		}
		try {
			getResponse().setContentType("text/json;charset=UTF-8");//解决乱码
			this.getResponse().getWriter().write(rtnStr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/***
	 * 查询所有机构
	 * @return
	 */
	private List findAllOrgList(){
		List orgList = new ArrayList();
//		IOrgService orgService = (IOrgService) this.getBean("orgServiceImpl"); 
		String hql="from ViewWorktaskOrg vo";
		
		try {
			orgList = tmsgService.findListByHsql(hql, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return orgList;
	}
	
	/**
	 * 查询所有机构类型
	 */
	private void findAllOrgclsList(){
//		IOrgService orgService = (IOrgService) this.getBean("orgServiceImpl");
//		this.orgclsLst = orgService.findAllOrgcls();
		orgclsLst=new ArrayList();
		
	}
	
	/***
	 * 发布公告
	 * @return
	 */
	public String savePubMsg(){
		//保存邮件固定信息数据
		setDefaultMsgData(tmsgInfo);	
		

		try {
			boolean b=loadFile(fileName);//上传
			if(b==false){
				type="0";
				this.getRequest().setAttribute("alertMsg","附件大小不能超过10M!");
				return findOwnPublishMsg();
			}
			if(tmsgInfo.getTouserName()!=null){
				String[] touserNames = tmsgInfo.getTouserName().split(";");
				String[] touserIds = tmsgInfo.getTouserId().split(";");
				tmsgInfo.setMsgType(1);//设为公告
				tmsgInfo.setStatus(0);
				tmsgService.save(tmsgInfo);//保存入库
				for(int i=0;i<touserNames.length;i++){
					
					tmsgInfo.setMsgType(1);//设为公告
					tmsgInfo.setTouserId(touserIds[i]);
					tmsgInfo.setTouserName(touserNames[i]);
					tmsgInfo.setStatus(1);
					tmsgService.save(tmsgInfo);//保存入库
					
					this.getRequest().setAttribute("alertMsg", "保存成功");
				}
				
			}
				
		} catch (Exception e) {
			e.printStackTrace();
			this.getRequest().setAttribute("alertMsg", "保存失败");
		}
		
		return findDisMsgInfoList();
	}
	
	/***
	 * 回复邮件
	 * @return
	 */
	public String returnMsg(){
		//保存邮件固定信息数据
		setDefaultMsgData(tmsgInfo);
		loadFile(fileName);//上传
		if("1".equals(tmsgInfo.getPublicFlag())){//公开热点问题
			tmsgInfo.setMsgType(4);
			tmsgInfo.setTouserId("");
			tmsgInfo.setTouserName("");
//			int revert=tmsgInfo.getRevertState();
//			try {
//				TMsgInfo t=	(TMsgInfo) tmsgService.findObject("from TMsgInfo t where t.revertState="+revert);
//				
//			} catch (BaseServiceException e) {
//				e.printStackTrace();
//			}
//			tmsgInfo.setRevertState(0);
			
		}
		//问题解答
		else{
		tmsgInfo.setMsgType(3);
		}
		//清空主键
		tmsgInfo.setMsgId(null);
		
		try {
			tmsgService.savePubMsg(tmsgInfo);//保存入库
		} catch (Exception e) {
			e.printStackTrace();
		}
		return findMsgByOwn();
	}
	
	/***
	 * 保存草稿
	 * @return
	 */
	public String saveModel(){
		try {
			//保存邮件固定信息数据
			setDefaultMsgData(tmsgInfo);			
			//草稿
			tmsgInfo.setMsgState(2);
			if(tmsgInfo.getTouserName()!=null && tmsgInfo.getTouserName().equals("≮请从机构中添加机构，默认为所有机构≯"))
				tmsgInfo.setTouserName(null);
			//附件
			boolean b=loadFile(fileName);//上传
			if(b==false){
				type="0";
				return findOwnPublishMsg();
			}
					
			tmsgService.saveOrUpdate(tmsgInfo);
		} catch (BaseServiceException e) {
			e.printStackTrace();
		}
		return findModelList();
	}
	
	/***
	 * 查询所有机构和机构类型
	 * @return
	 */
	public String findOwnPublishMsg(){
		if(msgInfoResult == null)
			msgInfoResult = new PageResults<TMsgInfo>();
		
		//获得登录用户
		HttpSession session = this.getHttpSession();
		Operator operator = (Operator) session.getAttribute(CommonInfoConfig.OPERATOR_SESSION_NAME);
		String userTreeData="";
		String roleTreeData="";
		try {
			msgInfoResult = tmsgService.findOwnPublishMsg(operator.getOperatorId()+"", pageSize, msgInfoResultpageNo);
			userTreeData = tmsgService.createUserDataJSON(operator);
			roleTreeData = tmsgService.createRoleDataJSON(operator);
			String userFilePath = Config.WEBROOTPATH 
			+ File.separator+"json" + Config.FILESEPARATOR + "user_tree_data_"+operator.getOperatorId()+".json";
			String roleFilePath = Config.WEBROOTPATH 
			+File.separator+ "json" + Config.FILESEPARATOR + "role_tree_data_"+operator.getOperatorId()+".json";
			
			writeJson(userFilePath, userTreeData);
			writeJson(roleFilePath, roleTreeData);
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "publishMsg.jsp";
	}
	public void writeJson(String filePath,String treeJson){
		try {
			File file = new File(filePath);
			
			if(file.exists());
				file.delete();
			
			
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8"));

			bw.write(treeJson);
			bw.flush();
			bw.close();
			
		
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	/***
	 * 查询已经发送的信息
	 * @return
	 */
	public String findDisMsgInfoList(){
		//获得登录用户
		HttpSession session = this.getHttpSession();
		Operator operator = (Operator) session.getAttribute(CommonInfoConfig.OPERATOR_SESSION_NAME);
		try {
			findOwnPublishMsg();
			msgInfoResult = tmsgService.findOwnPublishMsg(operator.getOperatorId()+"", pageSize, msgInfoResultpageNo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "publishMsg.jsp";
	}
	
	/***
	 * 查处所有草稿信息
	 * @return
	 */
	public String findModelList(){
		findOwnPublishMsg();
		//查出所有草稿信息
		//获得登录用户
		HttpSession session = this.getHttpSession();
		Operator operator = (Operator) session.getAttribute(CommonInfoConfig.OPERATOR_SESSION_NAME);
		try {
			modelResult = tmsgService.findOwnModel(operator.getOperatorId()+"", 2,pageSize,modelResultpageNo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "publishMsg.jsp";
	}
	
	/***
	 * 保存邮件固定信息数据
	 * @param tmsgInfo
	 */
	private void setDefaultMsgData(TMsgInfo tmsgInfo){
		//保存邮件信息固定信息数据
		if(tmsgInfo==null)
			tmsgInfo = new TMsgInfo();
		//获得登录用户
		HttpSession session = this.getHttpSession();
		Operator operator = (Operator) session.getAttribute(CommonInfoConfig.OPERATOR_SESSION_NAME);
		//保存用户ID 用户姓名
		tmsgInfo.setUserId(operator.getOperatorId()+"");
		tmsgInfo.setUserName(operator.getUserName());
		//发件机构为银监局
		tmsgInfo.setUserType(2);
		String startTime = DateUtil.getToday("yyyy-MM-dd HH:mm:ss");
		tmsgInfo.setStartTime(startTime);//发件邮件时间
		tmsgInfo.setTouserType(1);//收件人为机构
		tmsgInfo.setFileNameFlag(0);//附件未同步
		tmsgInfo.setSynflag(0);//邮件未同步
		tmsgInfo.setUserType(2);//发件机构为银监局
		tmsgInfo.setMsgState(0);
	}
	
	/**
	 * 回复邮件
	 * @return
	 */
	public String replyMsg(){
		if(tmsgInfo==null || tmsgInfo.getMsgId()==null)
			return findMsgByOwn();
		try {
			tmsgInfo = tmsgService.findMsgInfoById(tmsgInfo.getMsgId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return findOwnPublishMsg();
	}
	
	/***
	 * 上传附件
	 * @param fileName
	 */
	private File upload;// 实际上传文件
	private String uploadFileName; // 上传文件名
	public boolean loadFile(String fileName){
		if(upload!=null){
			FileInputStream fis = null; 
			try {
				fis = new FileInputStream(upload); 
				int s= fis.available();
				if(s>1024*1024*10){
					
                    return false;
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			} 
			}

		String name = "";
		if (!StringUtil.isEmpty(uploadFileName)&& upload != null) {
			name = DateUtil.getToday("yyyy_MM_dd_HH_mm_ss")
			+ uploadFileName.substring(uploadFileName.lastIndexOf("."),
					uploadFileName.length());
			String path = FitechProperties.readValue("notice");
			File f = new File(path);
			if (!f.exists()) {
				f.mkdirs();
			}
			try {
				FileUtils.copyFile(upload,
						new File(path + File.separator + name));
				tmsgInfo.setFilename(name);//保存修改的附件名
				tmsgInfo.setViewFileName(uploadFileName);//保存实际用于展现的附件名
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
				
						
				
	}
	
	public String down() {

		String filePath = FitechProperties.readValue("notice") + File.separator
				+ tmsgInfo.getFilename();
		String fileName = "";
		try {
			TMsgInfo m=(TMsgInfo) tmsgService.findObject("from TMsgInfo m where m.filename='"+tmsgInfo.getFilename()+"'");
			fileName=m.getViewFileName();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		File f = new File(filePath);
		if (!f.exists()) {
			this.addActionMessage("文件不存在！");
			return findMsgByOwn();
		}
		try {
			inputStream = new FileInputStream(f);
			this.fileName = new String((fileName).getBytes("gb2312"),
					"iso-8859-1");
			mimeType = "application/x-gzip";
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "download";

	}

	
	public ITMsgInfoService getTmsgService() {
		return tmsgService;
	}

	public void setTmsgService(ITMsgInfoService tmsgService) {
		this.tmsgService = tmsgService;
	}

	
	
	@JSON(serialize = false)
	public Map<String, String> getOrgclsMap() {
		/*IOrgService orgService = (IOrgService) this.getBean("orgServiceImpl");

		List<TOrgcls> orgclsPoList = orgService.findAllOrgcls();

		this.orgclsMap = new TreeMap<String, String>();
		this.orgclsMap.put("0", "全部机构");

		if (!ListUtil.isEmpty(orgclsPoList)) {
			for (TOrgcls orgclsPo : orgclsPoList) {
				this.orgclsMap.put(orgclsPo.getOrgclsid(), orgclsPo
						.getOrgclsnm());
			}
		}*/

		return this.orgclsMap;
	}
	
	public void setOrgclsMap(Map<String, String> orgclsMap) {
		this.orgclsMap = orgclsMap;
	}

	public List getOrglist() {
		List orgList=new ArrayList();
//		IOrgService orgService = (IOrgService) this.getBean("orgServiceImpl"); 
		String hql="select vo.id.orgId,vo.id.orgName from ViewWorktaskOrg vo";
		
		try {
			List list = tmsgService.findListByHsql(hql, null);
			for (int i = 0;list!=null&& i <list.size(); i++) {
				WorkTaskOrgInfoVo vo=new WorkTaskOrgInfoVo();
				Object[] obj=(Object[])list.get(i);
				vo.setOrgId((String)obj[0]);
				vo.setOrgName((String)obj[1]);
				orgList.add(vo);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return orgList;
	}

	public void setOrglist(List orglist) {
		this.orglist = orglist;
	}


	public JSONObject getJson() {
		return json;
	}


	public void setJson(JSONObject json) {
		this.json = json;
	}


	public List getOrgclsLst() {
		return this.orgclsLst;
	}


	public void setOrgclsLst(List orgclsLst) {
		this.orgclsLst = orgclsLst;
	}


	public PageResults<TMsgInfo> getMsgInfoResult() {
		return msgInfoResult;
	}


	public void setMsgInfoResult(PageResults<TMsgInfo> msgInfoResult) {
		this.msgInfoResult = msgInfoResult;
	}


	public PageResults<TMsgInfo> getPubMsgResult() {
		return pubMsgResult;
	}


	public void setPubMsgResult(PageResults<TMsgInfo> pubMsgResult) {
		this.pubMsgResult = pubMsgResult;
	}


	public Integer getNotReadCount() {
		return notReadCount;
	}


	public void setNotReadCount(Integer notReadCount) {
		this.notReadCount = notReadCount;
	}


	public TMsgInfo getTmsgInfo() {
		return tmsgInfo;
	}


	public void setTmsgInfo(TMsgInfo tmsgInfo) {
		this.tmsgInfo = tmsgInfo;
	}

	public File getFile() {
		return file;
	}


	public void setFile(File file) {
		this.file = file;
	}


	public PageResults<TMsgInfo> getModelResult() {
		return modelResult;
	}


	public void setModelResult(PageResults<TMsgInfo> modelResult) {
		this.modelResult = modelResult;
	}


	public Integer getNotReadModelCount() {
		return notReadModelCount;
	}


	public void setNotReadModelCount(Integer notReadModelCount) {
		this.notReadModelCount = notReadModelCount;
	}


	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public Integer getMsgInfoResultpageNo() {
		return msgInfoResultpageNo;
	}


	public void setMsgInfoResultpageNo(Integer msgInfoResultpageNo) {
		this.msgInfoResultpageNo = msgInfoResultpageNo;
	}


	public Integer getPubMsgResultpageNo() {
		return pubMsgResultpageNo;
	}


	public void setPubMsgResultpageNo(Integer pubMsgResultpageNo) {
		this.pubMsgResultpageNo = pubMsgResultpageNo;
	}


	public Integer getModelResultpageNo() {
		return modelResultpageNo;
	}


	public void setModelResultpageNo(Integer modelResultpageNo) {
		this.modelResultpageNo = modelResultpageNo;
	}


	public String getWhereSql() {
		return whereSql;
	}


	public void setWhereSql(String whereSql) {
		if(whereSql.equals("请输入查询条件"))
			whereSql = null;
		this.whereSql = whereSql;
	}


	public InputStream getInputStream() {
		return inputStream;
	}


	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}


	public String getMimeType() {
		return mimeType;
	}


	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}


	public String getPubWhereSql() {
		return pubWhereSql;
	}


	public void setPubWhereSql(String pubWhereSql) {
		if(pubWhereSql.equals("请输入查询条件"))
			pubWhereSql = null;
		this.pubWhereSql = pubWhereSql;
	}


	public File getUpload() {
		return upload;
	}


	public void setUpload(File upload) {
		this.upload = upload;
	}


	public String getUploadFileName() {
		return uploadFileName;
	}


	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}
	
	
	
	
	
	
}
