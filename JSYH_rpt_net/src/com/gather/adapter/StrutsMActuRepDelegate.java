
package com.gather.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.hibernate.Session;

import com.gather.common.Log;
import com.gather.common.StringUtil;
import com.gather.dao.DBConn;
import com.gather.hibernate.MActuRep;
import com.gather.struts.forms.MActuRepForm;
import com.gather.struts.forms.MChildReportForm;
import com.gather.struts.forms.RealReportForm;



public class StrutsMActuRepDelegate {
	
	/**
	 * @author linfeng
	 * @function 返回唯一一条记录
	 * @param subReportId  //子报表id
	 * @param versionId    //版本id
	 * @param dataRangeId  //数据范围id
	 * @param frequencyId  //频率id 
	 */
    public static MActuRepForm getCalendarDetailInfo(String subReportId,String versionId,Integer dataRangeId,Integer frequencyId){
    	  if(subReportId==null || versionId==null || dataRangeId==null || frequencyId==null) return null;
	      List retVals = new ArrayList();
	      DBConn conn=new DBConn();
	      Session session=conn.openSession();
	      String hsql="from com.gather.hibernate.MActuRep as obj"+
	                  " where obj.comp_id.childRepId='"+subReportId+"' and obj.comp_id.versionId='"+versionId+"'"+
	                  " and obj.comp_id.repFreqId="+frequencyId+" and obj.comp_id.dataRangeId="+dataRangeId;
	      //// System.out.println(hsql);
	      
	      MActuRepForm myForm=null;
	      try{
	       retVals.addAll(session.find(hsql));
	       session.close();
	      if(retVals!=null && retVals.size()>0){
	    	   myForm=new MActuRepForm();
	    	  MActuRep myPersistence = (MActuRep)retVals.get(0);
	    	  TranslatorUtil.copyPersistenceToVo(myPersistence,myForm);
	      }
	      }catch(Exception e){
	    	  e.printStackTrace();
	    	  new Log(StrutsMActuRepDelegate.class).info(":::class:StrutsMActuRepDelegate --  method: getCalendarDetailInfo 异常："+e.getMessage());  
	      }
	      finally{
	    	  try{
	    		  session.close();
	    	  }catch(Exception e){e.printStackTrace();}
	      }
	      return myForm;
    	}
    /**
     * 姬怀宝
     * 新增的返回对象的方法
     */
	/*  public static MOrgForm findById (String org){
		  if(org==null) return null;
	      List retVals = new ArrayList();
	      Session session=null;
	      try{
	      DBConn conn=new DBConn();
	      session=conn.openSession();
          retVals.addAll(session.find("from com.gather.hibernate.MOrg as obj1 where obj1.orgId='" + org+ "'"));
	       session.close();
	       if(retVals==null || retVals.size()<1) return null;
	       com.gather.struts.forms.MOrgForm mOrgFormTemp = new com.gather.struts.forms.MOrgForm();
	       com.gather.hibernate.MOrg mOrgPersistence = (com.gather.hibernate.MOrg)retVals.get(0);
	       TranslatorUtil.copyPersistenceToVo(mOrgPersistence, mOrgFormTemp);
	       return mOrgFormTemp;
	      }catch(Exception e){
	    	  e.printStackTrace();
	      }finally{
	    	  try{
	    		  session.close();
	    	  }catch(Exception e){e.printStackTrace();}
	      }
	       return null;
	   }*/
    /**
     * @author linfeng
     */
    public static List getAll (){
 	   List list=new ArrayList();
 	   List resultList=new ArrayList();
 		DBConn conn=new DBConn();
 		Session session=conn.openSession();
 		String hsql="from com.gather.hibernate.MActuRep as obj";
 		try{
 		list.addAll(session.find(hsql));
 		if(list!=null && list.size()>0){
 			for(int i=0;i<list.size();i++){
 				MActuRep report=(MActuRep)list.get(i);
 				MActuRepForm myForm=new MActuRepForm();
 				TranslatorUtil.copyPersistenceToVo(report,myForm);	
 				resultList.add(myForm);
 			}
 		}
 		}catch(Exception e){
 			e.printStackTrace();
 			new Log(StrutsMActuRepDelegate.class).info(":::class:StrutsMActuRepDelegate --  method: getAll 异常："+e.getMessage());
 		}finally{
 			try{
 				if(session!=null) session.close();
 			}catch(Exception e){e.printStackTrace();}
 		}
 	return resultList;
    }
    
    public static MActuRepForm findById(String id)
    {
    	    if(id==null)
    		return null;
    	List retVals=new ArrayList();
    	Session session=null;
    	try{
    		DBConn conn=new DBConn();
    		session=conn.openSession();
    		retVals.addAll(session.find("from com.gather.hibernate.MActuRep as macturep where macturep.comp_id.childRepId='"+id+"'"));
    	    conn.closeSession();
    	 if(retVals==null ||retVals.size()<1)return null;
    	 MActuRepForm mactuRepForm=new MActuRepForm();
    	 MActuRep mactuRep=(MActuRep)retVals.get(0);
    	 TranslatorUtil.copyPersistenceToVo(mactuRep,mactuRepForm);
    	 return mactuRepForm;
    	}catch(Exception e)
    	{
    		new Log(StrutsMActuRepDelegate.class).info(":::class:StrutsMActuRepDelegate --  method: findById 异常："+e.getMessage());
    		e.printStackTrace();
    	}
    	return null;
    }
	
	/**
	 * @author linfeng
	 * @function 获得子报表信息和 在set中封装对应的频度，范围，延迟时间等信息
	 * @param MChildReportForm List 子报表列表
	 * @return RealReportForm List   实际(全)子报表 formList 包括set
	 */
	
	public synchronized static List findRealReportSet(List subRepFormList)throws Exception {
  	  List list=new ArrayList();
  	  List listTemp=new ArrayList();
  	  String sql="";
  	  if(subRepFormList!=null && subRepFormList.size()>0){
  		  sql="from com.gather.hibernate.MChildReport as obj where ";
  		  for(int i=0;i<subRepFormList.size();i++){
  			MChildReportForm form=(MChildReportForm)subRepFormList.get(i);
        	 String subRepId=form.getChildRepId();
          	 String versionId=form.getVersionId();
          	 sql+="(obj.comp_id.childRepId='"+subRepId+"' and obj.comp_id.versionId='"+versionId+"') or ";
  		  }
  		  if(sql.substring(sql.length()-3,sql.length()).trim().equals("or")){
  			sql=sql.substring(0,sql.length()-3);
  		  }  
      //// System.out.println(sql);
      
      DBConn conn=new DBConn();
      Session session=null;
      try{
          session=conn.openSession();

      	list.addAll(session.find(sql));
     	 //// System.out.println("list.size() is: ======="+list.size());
		 for (int i=0;i<list.size();i++ ) {
		  RealReportForm realRepFormTemp = new RealReportForm();
		 //// System.out.println("className is:"+list.get(i).getClass().getName());
		  com.gather.hibernate.MChildReport morg = (com.gather.hibernate.MChildReport)list.get(i);
		    TranslatorUtil.copyPersistenceToVo(morg,realRepFormTemp);
		   listTemp.add(realRepFormTemp);
		   }
      }catch(Exception e){
      	e.printStackTrace();
      	new Log(StrutsMActuRepDelegate.class).info(":::class:StrutsMActuRepDelegate --  method: findRealReportSet 异常："+e.getMessage());
      	}finally{
		 try{
		 session.close();
		}catch(Exception e){
		   e.printStackTrace();
		}
		}
  	  } 
  	  return listTemp;
    }
	
	/**
	 * @author linfeng
	 * @function 获得实际子报表信息和 在set中封装对应的频度，范围，延迟时间等信息
	 *                            扩展set中封装的数据项到form中相应的属性中
	 * @param RealReportForm List 子报表列表 包括set
	 * @return RealReportForm List   实际(全)子报表 formList 不包括set
	 */
	
	public synchronized static List getExtendRealReports(List realReportForms){
		if(realReportForms==null || realReportForms.size()<1) return null;
		Set actualRep=null;
		RealReportForm realForm=null;
		RealReportForm resultForm=null;
		List resultList=new ArrayList();
		for(int i=0;i<realReportForms.size();i++){
			realForm=(RealReportForm)realReportForms.get(i);
			// System.out.println("getExtendRealReports::RealReportForm is"+realForm.getChildRepId());
			actualRep=realForm.getMActuReps();
			if(actualRep==null || actualRep.size()<1) continue;
			Iterator iter=actualRep.iterator();
			while(iter.hasNext()){
				resultForm=new RealReportForm();
				MActuRep actuRep=(MActuRep)iter.next();
				TranslatorUtil.copyPersistenceToVo(actuRep,realForm,resultForm);
				// System.out.println("getExtendRealReports::resultForm"+resultForm.getChildRepId());
				resultList.add(resultForm);
			}
		}
		return resultList;
	}
	
	/**
	 * @author linfeng
	 * @function 获得子报表信息和对应的频度，范围，延迟时间等信息
	 * @param MChildReportForm 子报表
	 * @return RealReportForm List   实际(全)子报表 formList
	 */
      public synchronized static List findRealReport(MChildReportForm subRepForm)throws Exception {
    	  List list=new ArrayList();
    	  List listTemp=new ArrayList();
    	  if(subRepForm!=null){
        	  DBConn conn=new DBConn();
        	  Session session=null;
        	  try{
        	  session=conn.openSession();
        	  String subRepId=subRepForm.getChildRepId();
        	  String versionId=subRepForm.getVersionId();
        	  String sql="from com.gather.hibernate.MActuRep as obj where obj.comp_id.childRepId='"+subRepId+"' and obj.comp_id.versionId='"+versionId+"'";
        	  list.addAll(session.find(sql));
        	  }catch(Exception e){
        		  new Log(StrutsMActuRepDelegate.class).info(":::class:StrutsMActuRepDelegate --  method: findRealReport 异常："+e.getMessage());
        		  e.printStackTrace();
        	  }finally{
		        	 try{
		        		 session.close();
		        	 }catch(Exception e){
		        		 e.printStackTrace();
		        	 }
		         }
        	  //
        	 // // System.out.println("list.size() is: ======="+list.size());
		      for (int i=0;i<list.size();i++ ) {
		    	  RealReportForm realRepFormTemp = new RealReportForm();
		    	  //// System.out.println("className is:"+list.get(i).getClass().getName());
		    	  com.gather.hibernate.MActuRep morg = (com.gather.hibernate.MActuRep)list.get(i);
		          TranslatorUtil.copyPersistenceToVo(morg,subRepForm,realRepFormTemp);
		          listTemp.add(realRepFormTemp);
		      }
    	  }
    	  return listTemp;
      }
      
  	/**
  	 * @author linfeng
  	 * @function 获得所有子报表对应的数据范围 数组
  	 * @param String childReportId  子报表id
  	 * @param String versionId   版本号id
  	 * @param String frequency 频率
  	 * @return dataRangeIds String[]
  	 */

        public static String[] findDataRange(String childReportId,String versionId,String freq)throws Exception {
        	List list=new ArrayList();
    	  List listTemp=new ArrayList();
    	  String[] dataRanges=null;
    	  if(childReportId!=null&&versionId!=null&&freq!=null){
        	  DBConn conn=new DBConn();
        	  Session session=null;
        	  try{
        	  session=conn.openSession();
        	  String sql="from com.gather.hibernate.MActuRep as obj where obj.comp_id.childRepId='"+childReportId+"' and obj.comp_id.versionId='"+versionId+"'"+
        	              " and obj.MRepFreq.repFreqId='"+freq+"'";
        	  list.addAll(session.find(sql));
        	  }catch(Exception e){
        		  new Log(StrutsMActuRepDelegate.class).info(":::class:StrutsMActuRepDelegate --  method: findDataRange 异常："+e.getMessage());
        		  e.printStackTrace();
        	  }finally{
		        	 try{
		        		 session.close();
		        	 }catch(Exception e){
		        		 e.printStackTrace();
		        	 }
		         }
        	  dataRanges = new String[list.size()];
		      for (int i=0;i<list.size();i++ ) {
		    	  com.gather.hibernate.MActuRep morg = (com.gather.hibernate.MActuRep)list.get(i);
		    	  dataRanges[i]=String.valueOf(morg.getMDataRgType().getDataRangeId());
		      }
    	  }
    	  return dataRanges;
        }

        
      	/**
      	 * @author linfeng
      	 * @function 获得所有子报表对应的频率 数组
      	 * @param String childReportId  子报表id
      	 * @param String versionId   版本号id
      	 * @return frequencyIds String[]
      	 */

            public static String[] findFrequency(String childReportId,String versionId)throws Exception {
            	List list=new ArrayList();
        	  List listTemp=new ArrayList();
        	  String[] freqIds=null;
        	  if(childReportId!=null&&versionId!=null){
            	  DBConn conn=new DBConn();
            	  Session session=null;
            	  try{
            	  session=conn.openSession();
            	  String sql="from com.gather.hibernate.MActuRep as obj where obj.comp_id.childRepId='"+childReportId+"' and obj.comp_id.versionId='"+versionId+"'";
            	  list.addAll(session.find(sql));
            	  }catch(Exception e){
            		  new Log(StrutsMActuRepDelegate.class).info(":::class:StrutsMActuRepDelegate --  method: findFrequency 异常："+e.getMessage());
            		  e.printStackTrace();
            	  }finally{
    		        	 try{
    		        		 session.close();
    		        	 }catch(Exception e){
    		        		 e.printStackTrace();
    		        	 }
    		         }
            	  freqIds = new String[list.size()];
    		      for (int i=0;i<list.size();i++ ) {
    		    	  
    		    	  com.gather.hibernate.MActuRep morg = (com.gather.hibernate.MActuRep)list.get(i);
    		    	  freqIds[i]=String.valueOf(morg.getMRepFreq().getRepFreqId());
    		    	  // System.out.println("=====freqIds[i] is"+freqIds[i]);
    		      }
        	  }
        	  return freqIds;
            }
            /**
             * @author linfeng
             * @function 得到子报表id关联的所有M_actu_rep表中的数据
             * @param childRepIds
             * @return MActuRep list
             */
            public static List getActuRepByChildRepIds(String[] childRepIds){
            	DBConn conn=new DBConn();
            	Session session=conn.openSession();
            	List list=new ArrayList();
            	String hsql="from com.gather.hibernate.MActuRep as obj where obj.comp_id.childRepId in ("+StringUtil.getStrForSqlIN(childRepIds)+")";
            	try{
            	 list.addAll(session.find(hsql));
            	 if(list!=null && list.size()>0){
            		return list;
            	 }
            	}catch(Exception e){
            		e.printStackTrace();
            		new Log(StrutsMActuRepDelegate.class).info(":::class:StrutsMActuRepDelegate --  method: getActuRepByChildRepIds 异常："+e.getMessage());	
            	}
            	finally{
            		try{
            			session.close();
            		}catch(Exception e){e.printStackTrace();}
            	}
            	return null;
            }
 
   /**
    * Create a new com.gather.struts.MActuRepForm object and persist (i.e. insert) it.
    *
    * @param   mActuRepForm   The object containing the data for the new com.gather.struts.MActuRepForm object
    * @exception   Exception   If the new com.gather.struts.MActuRepForm object cannot be created or persisted.
    */
   public static boolean  create (com.gather.struts.forms.MActuRepForm mActuRepForm) throws Exception {
      com.gather.hibernate.MActuRep mActuRepPersistence = new com.gather.hibernate.MActuRep ();
      try{
      TranslatorUtil.copyVoToPersistence(mActuRepPersistence, mActuRepForm);
      DBConn conn=new DBConn();
      Session session=conn.beginTransaction();
      session.save(mActuRepPersistence);
      conn.endTransaction(true);
      return true;
      }catch(Exception e)
      {
    	  new Log(StrutsMActuRepDelegate.class).info(":::class:StrutsMActuRepDelegate --  method: create 异常："+e.getMessage());
    	  throw e;
      }
   }

 }
