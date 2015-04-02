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
	 * function �õ���ǰ�����ϱ�����ʱӦ�ö�Ӧ�����ڣ�����ӦƵ��ȷ��ʱ�䷶
	 * @param frequency int Ƶ��
	 * @param year int ��
	 * @param month int ��
	 * @return Date[] length ������ӦƵ�ʺ�ʱ��Ŀ�ʼ�ͽ���ʱ�䣬 Date[0]��ʾ��ʼʱ�� Date[1]��ʾ����ʱ��
	 */
	public static int[] getYearAndTermNumber(int frequency,int year,int month){
		// System.out.println("-frequencyId is :"+frequency+" month: and year is: "+year+" and month is"+month);
		//result[0] �����
		//result[1] �������
		int[] result=new int[2];
		
		if(frequency==Config.FREQUENCY_MONTH){
			// System.out.println("---------------on month: and year is: "+year+" and month is"+month);
			// System.out.println("static month is :"+Integer.parseInt(DateUtil.getToday(DateUtil.MONTH)));
			// System.out.println("static year is :"+Integer.parseInt(DateUtil.getToday(DateUtil.YEAR)));
            //	���Ƶ������
			// System.out.println("���Ƶ������");
			if(year==Integer.parseInt(DateUtil.getToday(DateUtil.YEAR))){
				//����ǵ���
				// System.out.println("����ǵ��굱ǰ��");
				// System.out.println("------------on currenty year month");
				if(month==1){
					//����ǵ�һ�£��������������·�(12��)
					// System.out.println("����ǵ�һ�£��������������·�(12��)");
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
            //	���Ƶ���Ǽ�
			// System.out.println("���Ƶ���Ǽ�");
			if(year==Integer.parseInt(DateUtil.getToday(DateUtil.YEAR))){
			// ����ǵ���
				// System.out.println("����ǵ���");
			   if(month<=3){
				   //����ǵ���3�£�����ǰһ��9-12�·�
				   // System.out.println("����ǵ���3�£�����ǰһ��9-12�·�");
					result[0]=year-1;
					result[1]=4;
					return result;
			   }else if(month>3 && month<=6){
				   // System.out.println("�����3 �� 6 ��֮�䣬����1-3�·� ");
				   //�����3 �� 6 ��֮�䣬����1-3�·� 
					result[0]=year;
					result[1]=1;
					return result;
			   }else if(month>6 && month<=9){
					result[0]=year;
					result[1]=2;
					return result;
			   }else if(month>9){
				   //�����9 �� 12 ��֮�䣬����7-9�·�
				   // System.out.println("�����9 �� 12 ��֮�䣬����7-9�·�");
					result[0]=year;
					result[1]=3;
					return result;
			   }
			}else{
				//����������ǵ���
				// System.out.println("����������ǵ���");
				   if(month<=3){
					   //����1-3�·�
					   // System.out.println("����1-3�·�");
						result[0]=year;
						result[1]=1;
						return result;
				   }else if(month>3 && month<=6){
					   //�����3 �� 6 ��֮�䣬����3-6�·�
					   // System.out.println("�����3 �� 6 ��֮�䣬����3-6�·�");
						result[0]=year;
						result[1]=2;
						return result;
				   }else if(month>6 && month<=9){
					   //�����6 �� 9 ��֮�䣬����6-9�·�
					   // System.out.println("�����6 �� 9 ��֮�䣬����6-9�·�");
						result[0]=year;
						result[1]=3;
						return result;
				   }else if(month>9){
					   //�����9 �� 12 ��֮�䣬����9-12�·�
					   // System.out.println("�����9 �� 12 ��֮�䣬����9-12�·�");
						result[0]=year;
						result[1]=4;
						return result;
				   }
				
			}
		}else if(frequency==Config.FREQUENCY_HALF_YEAR){
			 //	���Ƶ���ǰ���
			// System.out.println("���Ƶ���ǰ���");
			if(year==Integer.parseInt(DateUtil.getToday(DateUtil.YEAR))){
				//����ǵ���
				// System.out.println("����ǵ���");
				if(month<=6){
				   //������ϰ��꣬����ǰһ����°���
					// System.out.println("������ϰ��꣬����ǰһ����°���");
					result[0]=year-1;
					result[1]=2;
					return result;
				}else{
					 //������°��꣬�����ϰ���
					// System.out.println("������°��꣬�����ϰ���");
					result[0]=year;
					result[1]=1;
					return result;
				}
			}else{
				//������ǵ���
				// System.out.println("");
				if(month<=6){
					 //������ϰ��꣬�����ϰ���
					// System.out.println("������ϰ��꣬�����ϰ���");
					result[0]=year;
					result[1]=1;
					return result;
				}else{
					 //������°��꣬�����°���
					// System.out.println("������°��꣬�����°���");
					result[0]=year;
					result[1]=2;
					return result;
				}
			}
		}else if(frequency==Config.FREQUENCY_YEAR){
			 //	���Ƶ������
			// System.out.println("���Ƶ������");
			if(year==Integer.parseInt(DateUtil.getToday(DateUtil.YEAR))){
				//����ǵ��꣬������һ��
				// System.out.println("����ǵ��꣬������һ��");
				result[0]=year-1;
				result[1]=1;
				return result;
			}else{
				//�������ʷ��ݣ����ش���ݱ���
				// System.out.println("�������ʷ��ݣ����ش���ݱ���");
				result[0]=year;
				result[1]=1;
				return result;
			}
		}
		return null;
	}
	
	
	
	/**
	 * @author linfeng
	 * @function ��û���id
	 * @param orgId String  ����id
	 * @return orgId String[]  ����id ����
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
	 * @function �õ������� orgForm �б�
	 * @param string orgId ����id
	 * @return (MOrgForm)
	 */
	public static MOrgForm getOrgFormInfo(String orgId){
		return StrutsMOrgDelegate.findById(orgId);
	}
	
	  /**
	   * @author linfeng
	   * @function �õ���������
	   * @param orgId
	   * @return
	   */
	  public static String getOrgName (String orgId){
		  return BaseInfoUtils.getOrgName(orgId);
	  }
	  
	  /**
	   * @author linfeng
	   * @function �õ�������ص�Ƶ�ʷ�Χ
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
	   * @function �õ���ر�������ݷ�Χ
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
	   * @function �õ��ӱ�����Ӧ�����°汾
	   * @param String[] childReportIds �ӱ���id����
	   * @return String[] newVersions ���µİ汾������  
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
	   * @function �õ�ĳ�汾�����°汾
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
       * @function �õ�ĳ����������а汾
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
	   * @function �õ��ӱ�������
	   * @param childRepId String
	   * @param versionId String
	   * @return childReportId String
	   */
	  public static String getChildReportName(String childRepId,String versionId){
		  return BaseInfoUtils.getChildReportName(childRepId,versionId);
	  }
	  
	  /**
	   * @author linfeng
	   * @function �õ��ӱ�������
	   * @param childRepId String
	   * @return childReportId String
	   */
	  public static String getChildReportName(String childRepId){ 
		  return BaseInfoUtils.getChildReportName(childRepId);
	  }
	  
	/**
	 * @author linfeng
	 * @function ����ӱ���id�б�
	 * @param orgId String[]  ����id ����
	 * @return subReportIds String[]  �ӱ���id ����
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
	 * @function ���(�����)�ӱ���id�б�,�������������Ϣ
	 * @param orgId String[]  ����id ����
	 * @return MediumOrgReportBean List  �ӱ���id �б�
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
	 * @function ���(�����)�ӱ���id�б�,�������������Ϣ
	 * @param orgId String  ����id 
	 * @return MediumOrgReportBean List  �ӱ���id �б�
	 */
	public static List getMultiOrgSubReportId(String orgId){
        String[] orgIds=getOrgId(orgId);
		return getMultiOrgSubReportId(orgIds);
	}
	
	/**
	 * @author linfeng
	 * @function ����ӱ���id�б�,ֻ���������Լ������Ϣ
	 * @param orgId String  ����id 
	 * @return MediumOrgReportBean List  �ӱ���id �б�
	 */
	public static List getOneOrgSubReportId(String orgId){
        String[] orgIds=new String[1];
        orgIds[0]=orgId;
		return getMultiOrgSubReportId(orgIds);
	}
	
	
	/**
	 * @author linfeng
	 * @function ����ӱ�����Ϣ�б�
	 * @param subReportIds String[] �ӱ���id
	 * @return MChildReportForm List  �ӱ��� formList
	 */
	public static List getMySubReports(String[] subReportIds){
		return StrutsMChildReportDelegate.getChildRepByChildIds(subReportIds);
	}
	/**
	 * @author linfeng
	 * @function ������°汾���ӱ�����Ϣ�б�
	 * @param MChildReportForm List  ���������������ӱ��� formList
	 * @return MChildReportForm Collection   ���������������ӱ��� form����
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
			
				//����Ѿ����ڣ��Ƚϰ汾
				if(compareMap.get(orgId)!=null){
					if(version>((Integer)compareMap.get(orgId)).intValue()){
						compareMap.put(orgId,new Integer(version));
						realMap.put(inForm.getChildRepId(),inForm);
					}
				}else{
					//����ֱ�Ӽ����б�
					compareMap.put(orgId,new Integer(version));
					realMap.put(inForm.getChildRepId(),inForm);
				}			
		}

		return realMap.values();
	}
	/**
	 * @author linfeng
	 * @function ������°汾���ӱ�����Ϣ�б�(�������б���������)
	 * @param orgId  ����������orgId
	 * @return MChildReportForm List   ���������������ӱ��� form����
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
	 * @function ������°汾���ӱ�����Ϣ�б�(ֻ�����Լ�)
	 * @param orgId  ����������orgId
	 * @return MChildReportForm List   ���������������ӱ��� form����
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
	 * @function ���ʵ�ʱ�����Ϣ�б�  ��Ӧ�����ӱ���form
	 * @param  MChildReportForm
	 * @return RealReportForm List   ʵ�ʱ��� form����
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
	 * @function ��õ������ύ�ı�����Ϣ�б�
	 * @param ��
	 * @param ��
	 * @param ����id
	 * @return MChildReportForm List   ���������������ӱ��� formList
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
	 * @function ���ʵ���ӱ�����Ϣ�� ��set�з�װ��Ӧ��Ƶ�ȣ���Χ���ӳ�ʱ�����Ϣ
	 *                            ��չset�з�װ�������form����Ӧ��������
	 * @param RealReportForm List �ӱ����б� ����set
	 * @return RealReportForm List   ʵ��(ȫ)�ӱ��� formList ������set
	 */
	public static List getExtendRealReports(String orgId){
		//�õ�����������������Ҫ�ϱ����б�(���°汾) MChildReportForm List.
		List list=getNewVersionReports(orgId);
		//����ӱ�����Ϣ�� ��set�з�װ��Ӧ��Ƶ�ȣ���Χ���ӳ�ʱ�����Ϣ
		  // ʵ��(ȫ)�ӱ��� formList ����set
		try{
		list=StrutsMActuRepDelegate.findRealReportSet(list);
		}catch(Exception e){e.printStackTrace();}
		
		//���ʵ���ӱ�����Ϣ,ʵ��(ȫ)�ӱ��� formList ������set
		list=StrutsMActuRepDelegate.getExtendRealReports(list);
		return list;
	}
	
	/**
	 * @author linfeng
	 * @function ���ʵ���ӱ�����Ϣ�� ��set�з�װ��Ӧ��Ƶ�ȣ���Χ���ӳ�ʱ�����Ϣ
	 *                            ��չset�з�װ�������form����Ӧ��������
	 * @param RealReportForm List �ӱ����б� ����set
	 * @return RealReportForm List   ʵ��(ȫ)�ӱ��� formList ������set
	 */
	public static List getOneOrgExtendRealReports(String orgId){
		//�õ�����������������Ҫ�ϱ����б�(���°汾) MChildReportForm List.
		List list=getOneOrgNewVersionReports(orgId);
		//����ӱ�����Ϣ�� ��set�з�װ��Ӧ��Ƶ�ȣ���Χ���ӳ�ʱ�����Ϣ
		  // ʵ��(ȫ)�ӱ��� formList ����set
		try{
		list=StrutsMActuRepDelegate.findRealReportSet(list);
		}catch(Exception e){e.printStackTrace();}
		
		//���ʵ���ӱ�����Ϣ,ʵ��(ȫ)�ӱ��� formList ������set
		list=StrutsMActuRepDelegate.getExtendRealReports(list);
		return list;
	}
	
	
	/**
	 * @author linfeng
	 * @function ��ú�(��)������ص�ʵ���ӱ�����Ϣ
	 * @param RealReportForm List   ʵ��(ȫ)�ӱ��� formList ������set
	 * @param multiOrgSubReportIds String[] ��������ӱ���id
	 * @reutrn RealReportForm List �����ʵ��(ȫ)�ӱ��� ��formList 1��������set 2���°汾��
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
	 * @function ��ú�(��)������ص�ʵ���ӱ�����Ϣ
	 * @param orgId  ����id
	 * @reutrn RealReportForm List �����ʵ��(ȫ)�ӱ��� ��formList 1��������set 2���°汾��
	 */
	public static List getMultiOrgSubReportIds(String orgId){
		List realReportForms=getExtendRealReports(orgId);
		List multiOrgSubReportIds=getMultiOrgSubReportId(orgId);
		//�����л���Ӧ�����ص�ģ�棬�;��������Ӧ��ģ�������չ���γ�ʵ�ʻ�����Ҫ��form�б�
		return getMultiOrgSubReportIds(realReportForms,multiOrgSubReportIds);
	}
	
	/**
	 * @author linfeng
	 * @function ��ú�(����)������ص�ʵ���ӱ�����Ϣ
	 * @param orgId  ����id
	 * @reutrn RealReportForm List ������ʵ��(ȫ)�ӱ��� ��formList 1��������set 2���°汾��
	 */
	public static List getOneOrgSubReportIds(String orgId){
		List realReportForms=getOneOrgExtendRealReports(orgId);
		List multiOrgSubReportIds=getOneOrgSubReportId(orgId);
		//�����л���Ӧ�����ص�ģ�棬�;��������Ӧ��ģ�������չ���γ�ʵ�ʻ�����Ҫ��form�б�
		return getMultiOrgSubReportIds(realReportForms,multiOrgSubReportIds);
	}

	
	/**
	 * @author linfeng
	 * @function �ϲ�Ϊ�ύ��forms �� �Ѿ��ύ��forms
	 * @param list showFormsList ����(noReferedForms list and  referedForms list)
	 * @param Date searchDate ����ѯ������������
	 * @return list UploadDataShowForm �����ϱ����õ���ʾ��forms
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
	 * @function ���ĳ�����ӱ���Ŀ�ʼ�ͽ���ʱ��
	 * @param childReportId String �ӱ���id
	 * @param versionId String �汾id
	 * @return Date[]  date[0] ��ʼʱ�� date[1] ����ʱ��
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
