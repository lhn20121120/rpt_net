package com.gather.refer.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.gather.adapter.StrutsMActuRepDelegate;
import com.gather.adapter.StrutsMChildReportDelegate;
import com.gather.adapter.StrutsMOrgDelegate;
import com.gather.adapter.StrutsMRepRangeDelegate;
import com.gather.adapter.StrutsReportDelegate;
import com.gather.adapter.TranslatorUtil;
import com.gather.common.BaseInfoUtils;
import com.gather.common.BusinessCommonUtil;
import com.gather.common.Config;
import com.gather.common.DateUtil;
import com.gather.common.StringUtil;
import com.gather.hibernate.MChildReport;
import com.gather.struts.forms.MChildReportForm;
import com.gather.struts.forms.MOrgForm;
import com.gather.struts.forms.MRepRangeForm;
import com.gather.struts.forms.RealReportForm;
import com.gather.struts.forms.ReportForm;
import com.gather.struts.forms.UploadDataShowForm;

public class ShowInfoUtil {
	
	
	/**
	 * author linfeng
	 * function 得到当前机构上报数据时应该对应的日期，给对应频率确定时间范
	 * @param frequency int 频率
	 * @param year int 年
	 * @param month int 月
	 * @return Date[] length 返回相应频率和时间的开始和结束时间， Date[0]表示开始时间 Date[1]表示结束时间
	 */
	public static int[] getYearAndTermNumber(int frequency,int year,int month){
		// System.out.println("-frequencyId is :"+frequency+" month: and year is: "+year+" and month is"+month);
		//result[0] 存放年
		//result[1] 存放期数
		int[] result=new int[2];
		
		if(frequency==Config.FREQUENCY_MONTH){
			// System.out.println("---------------on month: and year is: "+year+" and month is"+month);
			// System.out.println("static month is :"+Integer.parseInt(DateUtil.getToday(DateUtil.MONTH)));
			// System.out.println("static year is :"+Integer.parseInt(DateUtil.getToday(DateUtil.YEAR)));
            //	如果频率是月
			// System.out.println("如果频率是月");
			if(year==Integer.parseInt(DateUtil.getToday(DateUtil.YEAR))){
				//如果是当年
				// System.out.println("如果是当年当前月");
				// System.out.println("------------on currenty year month");
				if(month==1){
					//如果是第一月，返回上年的年底月份(12期)
					// System.out.println("如果是第一月，返回上年的年底月份(12期)");
					result[0]=year-1;
					result[1]=12;
					return result;
				}else{
					result[0]=year;
					result[1]=month-1;
					return result; 
				}
			}else{
				result[0]=year;
				result[1]=month;
				return result;
			}
		}else if(frequency==Config.FREQUENCY_SEASON){
			// System.out.println("------------on other years month");
            //	如果频率是季
			// System.out.println("如果频率是季");
			if(year==Integer.parseInt(DateUtil.getToday(DateUtil.YEAR))){
			// 如果是当年
				// System.out.println("如果是当年");
			   if(month<=3){
				   //如果是当年3月，返回前一年9-12月份
				   // System.out.println("如果是当年3月，返回前一年9-12月份");
					result[0]=year-1;
					result[1]=4;
					return result;
			   }else if(month>3 && month<=6){
				   // System.out.println("如果是3 至 6 月之间，返回1-3月份 ");
				   //如果是3 至 6 月之间，返回1-3月份 
					result[0]=year;
					result[1]=1;
					return result;
			   }else if(month>6 && month<=9){
					result[0]=year;
					result[1]=2;
					return result;
			   }else if(month>9){
				   //如果是9 至 12 月之间，返回7-9月份
				   // System.out.println("如果是9 至 12 月之间，返回7-9月份");
					result[0]=year;
					result[1]=3;
					return result;
			   }
			}else{
				//季：如果不是当年
				// System.out.println("季：如果不是当年");
				   if(month<=3){
					   //返回1-3月份
					   // System.out.println("返回1-3月份");
						result[0]=year;
						result[1]=1;
						return result;
				   }else if(month>3 && month<=6){
					   //如果是3 至 6 月之间，返回3-6月份
					   // System.out.println("如果是3 至 6 月之间，返回3-6月份");
						result[0]=year;
						result[1]=2;
						return result;
				   }else if(month>6 && month<=9){
					   //如果是6 至 9 月之间，返回6-9月份
					   // System.out.println("如果是6 至 9 月之间，返回6-9月份");
						result[0]=year;
						result[1]=3;
						return result;
				   }else if(month>9){
					   //如果是9 至 12 月之间，返回9-12月份
					   // System.out.println("如果是9 至 12 月之间，返回9-12月份");
						result[0]=year;
						result[1]=4;
						return result;
				   }
				
			}
		}else if(frequency==Config.FREQUENCY_HALF_YEAR){
			 //	如果频率是半年
			// System.out.println("如果频率是半年");
			if(year==Integer.parseInt(DateUtil.getToday(DateUtil.YEAR))){
				//如果是当年
				// System.out.println("如果是当年");
				if(month<=6){
				   //如果是上半年，返回前一年的下半年
					// System.out.println("如果是上半年，返回前一年的下半年");
					result[0]=year-1;
					result[1]=2;
					return result;
				}else{
					 //如果是下半年，返回上半年
					// System.out.println("如果是下半年，返回上半年");
					result[0]=year;
					result[1]=1;
					return result;
				}
			}else{
				//如果不是当年
				// System.out.println("");
				if(month<=6){
					 //如果是上半年，返回上半年
					// System.out.println("如果是上半年，返回上半年");
					result[0]=year;
					result[1]=1;
					return result;
				}else{
					 //如果是下半年，返回下半年
					// System.out.println("如果是下半年，返回下半年");
					result[0]=year;
					result[1]=2;
					return result;
				}
			}
		}else if(frequency==Config.FREQUENCY_YEAR){
			 //	如果频率是年
			// System.out.println("如果频率是年");
			if(year==Integer.parseInt(DateUtil.getToday(DateUtil.YEAR))){
				//如果是当年，返回上一年
				// System.out.println("如果是当年，返回上一年");
				result[0]=year-1;
				result[1]=1;
				return result;
			}else{
				//如果是历史年份，返回此年份报表
				// System.out.println("如果是历史年份，返回此年份报表");
				result[0]=year;
				result[1]=1;
				return result;
			}
		}
		return null;
	}
	
	
	
	/**
	 * @author linfeng
	 * @function 获得机构id
	 * @param orgId String  机构id
	 * @return orgId String[]  机构id 数组
	 */
	public static String[] getOrgId(String orgId){
		List list=null;
	   try{
		list=StrutsMOrgDelegate.findOrgById(orgId);
	   }catch(Exception e){
		   // System.out.println(e.getMessage());
	   }
	   String temp="";
	   List listTemp=new ArrayList();
	   listTemp.add(orgId);
	   for(int i=0;i<list.size();i++){
		   temp=((MOrgForm)list.get(i)).getOrgId();
		   //// System.out.println("temp is:"+temp);
		   listTemp.add(temp);
	   }
	   String[] temp2=new String[list.size()+1];
		return (String[])listTemp.toArray(temp2);
	}
	
	/*
	 * @author linfeng
	 * @function 得到机构的 orgForm 列表
	 * @param string orgId 机构id
	 * @return (MOrgForm)
	 */
	public static MOrgForm getOrgFormInfo(String orgId){
		return StrutsMOrgDelegate.findById(orgId);
	}
	
	  /**
	   * @author linfeng
	   * @function 得到机构名称
	   * @param orgId
	   * @return
	   */
	  public static String getOrgName (String orgId){
		  return BaseInfoUtils.getOrgName(orgId);
	  }
	  
	  /**
	   * @author linfeng
	   * @function 得到报表相关的频率范围
	   * @param childReportId
	   * @param versionId
	   * @return frequencyId String[] 
	   */
	  public static String[] getFreqIds(String childReportId,String versionId){
		  try{
		  return StrutsMActuRepDelegate.findFrequency(childReportId,versionId);
		  }catch(Exception e){e.printStackTrace();}
		  return null;
	  }
	  
	  /**
	   * @author linfeng
	   * @function 得到相关报表的数据范围
	   * @param childReportId
	   * @param versionId
	   * @param freq
	   * @return
	   */
	  public static String[] getDataRange(String childReportId,String versionId,String freq){
		  try{
		  return StrutsMActuRepDelegate.findDataRange(childReportId,versionId,freq);
		  }catch(Exception e){e.printStackTrace();}
		  return null;
	  }
	  /**
	   * @author linfeng
	   * @function 得到子报表相应的最新版本
	   * @param String[] childReportIds 子报表id数组
	   * @return String[] newVersions 最新的版本号数组  
	   */
	  public static String[] getNewVersions(String[] childReportIds){
		  if(childReportIds==null || childReportIds.length<1) return null;
		  String[] versions=new String[childReportIds.length];
		  for(int i=0;i<childReportIds.length;i++){
			  versions[i]=getNewVersion(childReportIds[i]);
		  }
		  return versions;
	  }
	  /**
	   * @author linfeng
	   * @function 得到某版本的最新版本
	   * @param childReportId String
	   * @return String newVersion
	   */

      public static String getNewVersion(String childReportId){
    	  String[] versions=null;
    	  Map versionMap=new Hashtable();
    	  try{
    	       versions=getAllVersions(childReportId);
    	  }catch(Exception e){e.printStackTrace();}
    	  int maxVersion=0;
    	  if(versions!=null && versions.length>0){
    		  
    		  int temp;
    		  for(int i=0;i<versions.length;i++){
    			  temp=Integer.parseInt(versions[i]);
        		  if(temp>maxVersion){
        			  maxVersion=temp;
        			  versionMap.put("version",versions[i]);
        		  }
    		  }
    
    	  }
    	  return (String)versionMap.get("version");
      }
      
      /**
       * @author linfeng
       * @function 得到某个报表的所有版本
       * @param String childReportId
       * @return String[] versions 
       */
      
     public static String[] getAllVersions(String childReportId){
   	  String[] versions=null;
	  try{
	       versions=StrutsMChildReportDelegate.findVerion(childReportId);
	  }catch(Exception e){e.printStackTrace();}
	  return versions;
     }
	  
	  /**
	   * @author linfeng
	   * @function 得到子报表名称
	   * @param childRepId String
	   * @param versionId String
	   * @return childReportId String
	   */
	  public static String getChildReportName(String childRepId,String versionId){
		  return BaseInfoUtils.getChildReportName(childRepId,versionId);
	  }
	  
	  /**
	   * @author linfeng
	   * @function 得到子报表名称
	   * @param childRepId String
	   * @return childReportId String
	   */
	  public static String getChildReportName(String childRepId){ 
		  return BaseInfoUtils.getChildReportName(childRepId);
	  }
	  
	/**
	 * @author linfeng
	 * @function 获得子报表id列表
	 * @param orgId String[]  机构id 数组
	 * @return subReportIds String[]  子报表id 数组
	 */
	public static String[] getSubReportId(String[] orgIds){
		List repRangeList=StrutsMRepRangeDelegate.getSubReportIds(orgIds);
		
		if(repRangeList==null) return null;
		Map map=new Hashtable();
		String[] temp=new String[repRangeList.size()];
		for(int i=0;i<repRangeList.size();i++){
			temp[i]=((MRepRangeForm)repRangeList.get(i)).getChildRepId();
			map.put(temp[i],temp[i]);
		}
		return (String[])(map.values()).toArray(new String[map.size()]);
	}
	
	/**
	 * @author linfeng
	 * @function 获得(多机构)子报表id列表,包括机构相关信息
	 * @param orgId String[]  机构id 数组
	 * @return MediumOrgReportBean List  子报表id 列表
	 */
	public static List getMultiOrgSubReportId(String[] orgIds){
		List repRangeList=StrutsMRepRangeDelegate.getSubReportIds(orgIds);
		
		if(repRangeList==null) return null;
		List tempList=new ArrayList();
		String orgId="";
		String orgName="";
		for(int i=0;i<repRangeList.size();i++){
			MediumOrgReportBean orgRepForm=new MediumOrgReportBean(); 
			orgId=((MRepRangeForm)repRangeList.get(i)).getOrgId();
			orgName=getOrgName(orgId);
			orgRepForm.setChildRepId(((MRepRangeForm)repRangeList.get(i)).getChildRepId());
			orgRepForm.setOrgId(orgId);
			orgRepForm.setOrgName(orgName);
			tempList.add(orgRepForm);
		}
		return tempList;
	}
	
	/**
	 * @author linfeng
	 * @function 获得(多机构)子报表id列表,包括机构相关信息
	 * @param orgId String  机构id 
	 * @return MediumOrgReportBean List  子报表id 列表
	 */
	public static List getMultiOrgSubReportId(String orgId){
        String[] orgIds=getOrgId(orgId);
		return getMultiOrgSubReportId(orgIds);
	}
	
	/**
	 * @author linfeng
	 * @function 获得子报表id列表,只包括机构自己相关信息
	 * @param orgId String  机构id 
	 * @return MediumOrgReportBean List  子报表id 列表
	 */
	public static List getOneOrgSubReportId(String orgId){
        String[] orgIds=new String[1];
        orgIds[0]=orgId;
		return getMultiOrgSubReportId(orgIds);
	}
	
	
	/**
	 * @author linfeng
	 * @function 获得子报表信息列表
	 * @param subReportIds String[] 子报表id
	 * @return MChildReportForm List  子报表 formList
	 */
	public static List getMySubReports(String[] subReportIds){
		return StrutsMChildReportDelegate.getChildRepByChildIds(subReportIds);
	}
	/**
	 * @author linfeng
	 * @function 获得最新版本的子报表信息列表
	 * @param MChildReportForm List  所属机构的所有子报表 formList
	 * @return MChildReportForm Collection   所属机构的最新子报表 form集合
	 */
	public static Collection getNewVersionReports(List ChildReportForms){
		if(ChildReportForms==null) return null;
		MChildReportForm inForm=null;
		String orgId="";
		int version;
		boolean flag=false;
		Map compareMap=new Hashtable();
		Map realMap=new Hashtable();
		for(int i=0;i<ChildReportForms.size();i++){
			inForm=(MChildReportForm)ChildReportForms.get(i);
			orgId=inForm.getChildRepId();
			version=StringUtil.str2Int(inForm.getVersionId());
			//// System.out.println("version is : "+version);
			
				//如果已经存在，比较版本
				if(compareMap.get(orgId)!=null){
					if(version>((Integer)compareMap.get(orgId)).intValue()){
						compareMap.put(orgId,new Integer(version));
						realMap.put(inForm.getChildRepId(),inForm);
					}
				}else{
					//否则，直接加入列表
					compareMap.put(orgId,new Integer(version));
					realMap.put(inForm.getChildRepId(),inForm);
				}			
		}

		return realMap.values();
	}
	/**
	 * @author linfeng
	 * @function 获得最新版本的子报表信息列表(包含所有被代报机构)
	 * @param orgId  所属机构的orgId
	 * @return MChildReportForm List   所属机构的最新子报表 form集合
	 */
	public static List getNewVersionReports(String orgId){
		String[] temp=getOrgId(orgId);
		temp=getSubReportId(temp);
		List tempList=getMySubReports(temp);
		Collection tempCollection=getNewVersionReports(tempList);
		Iterator iter=tempCollection.iterator();
		List resultList=new ArrayList();
		while(iter.hasNext()){
		   resultList.add(iter.next());	
		}
		return resultList;
	}
	
	/**
	 * @author linfeng
	 * @function 获得最新版本的子报表信息列表(只包含自己)
	 * @param orgId  所属机构的orgId
	 * @return MChildReportForm List   所属机构的最新子报表 form集合
	 */
	public static List getOneOrgNewVersionReports(String orgId){
		String[] temp=new String[1];
		temp[0]=orgId;
		temp=getSubReportId(temp);
		List tempList=getMySubReports(temp);
		Collection tempCollection=getNewVersionReports(tempList);
		Iterator iter=tempCollection.iterator();
		List resultList=new ArrayList();
		while(iter.hasNext()){
		   resultList.add(iter.next());	
		}
		return resultList;
	}
	
	/**
	 * @author linfeng
	 * @function 获得实际报表信息列表  对应单个子报表form
	 * @param  MChildReportForm
	 * @return RealReportForm List   实际报表 form集合
	 */
	public static List getRealReports(MChildReportForm mChildReportForm){
		if(mChildReportForm!=null){
			try{
				return StrutsMActuRepDelegate.findRealReport(mChildReportForm);
				}catch(Exception e){e.printStackTrace();}
		}
		return null;
	}
	

	
	/**
	 * @author linfeng
	 * @function 获得当月已提交的报表信息列表
	 * @param 年
	 * @param 月
	 * @param 机构id
	 * @return MChildReportForm List   所属机构的最新子报表 formList
	 */
	public static List getReferedReports(int year,int month,String[] orgIds){
		List list=null;
		try{
			list=StrutsReportDelegate.findReferedReports(year,month,orgIds);
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * @author linfeng
	 * @function 获得实际子报表信息和 在set中封装对应的频度，范围，延迟时间等信息
	 *                            扩展set中封装的数据项到form中相应的属性中
	 * @param RealReportForm List 子报表列表 包括set
	 * @return RealReportForm List   实际(全)子报表 formList 不包括set
	 */
	public static List getExtendRealReports(String orgId){
		//得到所属机构的所有需要上报的列表(最新版本) MChildReportForm List.
		List list=getNewVersionReports(orgId);
		//获得子报表信息和 在set中封装对应的频度，范围，延迟时间等信息
		  // 实际(全)子报表 formList 包括set
		try{
		list=StrutsMActuRepDelegate.findRealReportSet(list);
		}catch(Exception e){e.printStackTrace();}
		
		//获得实际子报表信息,实际(全)子报表 formList 不包括set
		list=StrutsMActuRepDelegate.getExtendRealReports(list);
		return list;
	}
	
	/**
	 * @author linfeng
	 * @function 获得实际子报表信息和 在set中封装对应的频度，范围，延迟时间等信息
	 *                            扩展set中封装的数据项到form中相应的属性中
	 * @param RealReportForm List 子报表列表 包括set
	 * @return RealReportForm List   实际(全)子报表 formList 不包括set
	 */
	public static List getOneOrgExtendRealReports(String orgId){
		//得到所属机构的所有需要上报的列表(最新版本) MChildReportForm List.
		List list=getOneOrgNewVersionReports(orgId);
		//获得子报表信息和 在set中封装对应的频度，范围，延迟时间等信息
		  // 实际(全)子报表 formList 包括set
		try{
		list=StrutsMActuRepDelegate.findRealReportSet(list);
		}catch(Exception e){e.printStackTrace();}
		
		//获得实际子报表信息,实际(全)子报表 formList 不包括set
		list=StrutsMActuRepDelegate.getExtendRealReports(list);
		return list;
	}
	
	
	/**
	 * @author linfeng
	 * @function 获得和(多)机构相关的实际子报表信息
	 * @param RealReportForm List   实际(全)子报表 formList 不包括set
	 * @param multiOrgSubReportIds String[] 多机构的子报表id
	 * @reutrn RealReportForm List 多机构实际(全)子报表 ，formList 1，不包括set 2，新版本的
	 */
	public static List getMultiOrgSubReportIds(List realReportForms,List multiOrgSubReportIds){
		if(realReportForms==null || realReportForms.size()<1) return null;
		if(multiOrgSubReportIds==null || multiOrgSubReportIds.size()<1) return realReportForms;
		List tempList=new ArrayList();
		String childRepId="";
		String orgId="";
		String orgName="";
		MediumOrgReportBean sourceForm;
		RealReportForm tempForm;

		
		for(int i=0;i<realReportForms.size();i++){
			tempForm=(RealReportForm)realReportForms.get(i);
			// System.out.println("=========");
			// System.out.println("====getMultiOrgSubReportIds:::realReportForm.getChildRepId()"+tempForm.getChildRepId());
			// System.out.println("=========");
			for(int j=0;j<multiOrgSubReportIds.size();j++){
				//RealReportForm temp
				sourceForm=(MediumOrgReportBean)multiOrgSubReportIds.get(j);
                childRepId=sourceForm.getChildRepId();
				if(tempForm.getChildRepId().equals(childRepId)){
					RealReportForm tempBeanForm=new RealReportForm();
					TranslatorUtil.copyVoToVo(tempForm,tempBeanForm);
					tempBeanForm.setOrgId(sourceForm.getOrgId());
					tempBeanForm.setOrgName(sourceForm.getOrgName());
					// System.out.println("tempBeanForm.getChildRepId() is:"+tempBeanForm.getChildRepId());
				    tempList.add(tempBeanForm);
				}
			}
		}
		
		
		return tempList;
	}
	
	/**
	 * 	//1
	 for(int i=0;i<realReportForms.size();i++){
			tempForm=(RealReportForm)realReportForms.get(i);
			for(int j=0;j<multiOrgSubReportIds.size();j++){
				sourceForm=(MediumOrgReportBean)multiOrgSubReportIds.get(j);
                childRepId=sourceForm.getChildRepId();
				if(tempForm.getChildRepId().equals(childRepId)){
					tempForm.setOrgId(sourceForm.getOrgId());
					tempForm.setOrgName(sourceForm.getOrgName());
				    tempList.add(tempForm);
				}
			}
		}
		//2
		 * for(int i=0;i<multiOrgSubReportIds.size();i++){
			sourceForm=(MediumOrgReportBean)multiOrgSubReportIds.get(i);

			for(int j=0;j<realReportForms.size();j++){
				tempForm=(RealReportForm)realReportForms.get(j);
				
                childRepId=sourceForm.getChildRepId();
				if(tempForm.getChildRepId().equals(childRepId)){
					tempForm.setOrgId(sourceForm.getOrgId());
					tempForm.setOrgName(sourceForm.getOrgName());
				    tempList.add(tempForm);
				}
			}
		}
	 */
	
	/**
	 * @author linfeng
	 * @function 获得和(多)机构相关的实际子报表信息
	 * @param orgId  机构id
	 * @reutrn RealReportForm List 多机构实际(全)子报表 ，formList 1，不包括set 2，新版本的
	 */
	public static List getMultiOrgSubReportIds(String orgId){
		List realReportForms=getExtendRealReports(orgId);
		List multiOrgSubReportIds=getMultiOrgSubReportId(orgId);
		//把所有机构应该下载的模版，和具体机构对应的模版进行扩展，形成实际机构需要的form列表
		return getMultiOrgSubReportIds(realReportForms,multiOrgSubReportIds);
	}
	
	/**
	 * @author linfeng
	 * @function 获得和(单独)机构相关的实际子报表信息
	 * @param orgId  机构id
	 * @reutrn RealReportForm List 单机构实际(全)子报表 ，formList 1，不包括set 2，新版本的
	 */
	public static List getOneOrgSubReportIds(String orgId){
		List realReportForms=getOneOrgExtendRealReports(orgId);
		List multiOrgSubReportIds=getOneOrgSubReportId(orgId);
		//把所有机构应该下载的模版，和具体机构对应的模版进行扩展，形成实际机构需要的form列表
		return getMultiOrgSubReportIds(realReportForms,multiOrgSubReportIds);
	}

	
	/**
	 * @author linfeng
	 * @function 合并为提交的forms 和 已经提交的forms
	 * @param list showFormsList 包括(noReferedForms list and  referedForms list)
	 * @param Date searchDate 即查询传回来的日期
	 * @return list UploadDataShowForm 数据上报，得到显示的forms
	 */
	public static List mergeTwoFormList(List showFormsList,Date searchDate){
		List resultList=new ArrayList();
		int searchYear=Integer.parseInt(DateUtil.toSimpleDateFormat(searchDate,"yyyy"));
		int searchMonth=Integer.parseInt(DateUtil.toSimpleDateFormat(searchDate,"MM"));
		
		
		if(showFormsList!=null && showFormsList.size()>0){
			for(int i=0;i<showFormsList.size();i++){
				UploadDataShowForm myForm=new UploadDataShowForm();
				if(showFormsList.get(i) instanceof RealReportForm){
					RealReportForm fromForm=(RealReportForm)showFormsList.get(i);
					myForm.setDataRange(fromForm.getDataRangeId());
					myForm.setDataRangeName(fromForm.getDataRangeName());
					myForm.setDelayDays(fromForm.getDelayTime());
					myForm.setFrequency(fromForm.getRepFreqId());
					myForm.setFreqName(fromForm.getRepFreqName());
					myForm.setNormalDays(fromForm.getNormalTime());
					myForm.setReportId(fromForm.getChildRepId());
					myForm.setReportName(fromForm.getReportName());
					myForm.setSetDate(searchDate);
					int[] yearAndTerms=getYearAndTermNumber(fromForm.getRepFreqId().intValue(),searchYear,searchMonth);
					myForm.setYear(yearAndTerms[0]);
					myForm.setTerms(new Integer(yearAndTerms[1]));
					myForm.setVersion(fromForm.getVersionId());
					myForm.setIfReferedFlag(0);
					myForm.setOrgId(fromForm.getOrgId());
					myForm.setOrgName(fromForm.getOrgName());	
				}else{
					ReportForm fromForm=(ReportForm)showFormsList.get(i);
					myForm.setRealSubReportId(fromForm.getRepId());
					myForm.setReferedDate(fromForm.getReportDate());
					myForm.setReportFlag(fromForm.getReportFlag());
					myForm.setReportId(fromForm.getChildrepid());
					myForm.setDataRange(fromForm.getDatarangeid());
					myForm.setFrequency(fromForm.getFrequency());
					myForm.setReportName(fromForm.getRepName());
					myForm.setRepState(fromForm.getFileFlag());
					myForm.setSetDate(searchDate);
					//// System.out.println("====getFrequencyId() is:"+fromForm.getFrequency());
					//// System.out.println("====getDataRangeId is:"+fromForm.getDatarangeid());
					//// System.out.println("====getFrequencyName() is:"+BusinessCommonUtil.getFrequencyName(fromForm.getFrequency()));
					//// System.out.println("====getDataRangeName is:"+BusinessCommonUtil.getDataRangeName(fromForm.getDatarangeid()));
					myForm.setFreqName(BusinessCommonUtil.getFrequencyName(fromForm.getFrequency()));
					myForm.setDataRangeName(BusinessCommonUtil.getDataRangeName(fromForm.getDatarangeid()));
					myForm.setVersion(fromForm.getVersionId());
					myForm.setIfReferedFlag(1);
					int[] yearAndTerms=getYearAndTermNumber(fromForm.getFrequency().intValue(),searchYear,searchMonth);
					myForm.setYear(yearAndTerms[0]);
					myForm.setTerms(fromForm.getTerm());
					// System.out.println("====getFrequency() is:"+fromForm.getFrequency());
					// System.out.println("====searchYear is:"+searchYear);
					// System.out.println("====searchMonth is:"+searchMonth);
					// System.out.println("================================");
					// System.out.println("====myForm.getYear() is:"+myForm.getYear());
					// System.out.println("====myForm.getTerms() is:"+myForm.getTerms());
					myForm.setOrgId(fromForm.getOrgid());
					myForm.setOrgName(getOrgName(fromForm.getOrgid()));	
					}
				myForm.setMyState();
				resultList.add(myForm);
			}
		}
		return resultList;
	}
	
	
	/**
	 * @author linfeng
	 * @function 获得某具体子报表的开始和结束时间
	 * @param childReportId String 子报表id
	 * @param versionId String 版本id
	 * @return Date[]  date[0] 开始时间 date[1] 结束时间
	 */
	public static Date[] getOneReportTime(String childRepId,String versionId){
		MChildReport report=StrutsMChildReportDelegate.getTimeByPK(childRepId,versionId);
		if(report==null) return null;
		Date[] startAndEnd=new Date[2];
		startAndEnd[0]=report.getStartDate();
		startAndEnd[1]=report.getEndDate();
		return startAndEnd;
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*
		String[] temp=ShowInfoUtil.getOrgId("0003");
		for(int i=0;i<temp.length;i++){
			// System.out.println(temp[i]);	
		}
		*/
		
		int frequency=com.gather.common.Config.FREQUENCY_MONTH;
		int year=Integer.parseInt(DateUtil.getToday(DateUtil.YEAR));
		int month=Integer.parseInt(DateUtil.getToday(DateUtil.MONTH));
		int[] testDate=getYearAndTermNumber(frequency,year,month);
			//// System.out.println("year : "+testDate[0]);
			//// System.out.println("term : "+testDate[1]);

	}
}
