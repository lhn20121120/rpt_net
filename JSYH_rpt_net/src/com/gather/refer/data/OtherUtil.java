package com.gather.refer.data;

import java.util.Date;

import com.gather.common.BusinessCommonUtil;
import com.gather.common.Config;
import com.gather.common.DateUtil;

public class OtherUtil {
	
	/**
	 * @author linfeng
	 * @function �õ� Ƶ�ʺ�������Ӧ�� ��ʼʱ�� �� ����ʱ��
	 * @param freqId
	 * @param year
	 * @param term
	 * @return Date[] Date[0] ��ʼʱ�� Date[1] ����ʱ��
	 */
		
		public static Date[] getStartAndEndDate(String reportId,String versionId,String dataRange,String freqId,String pYear,String pTerm) throws Exception{
			Date[] tempDate=new Date[2];
			Date sd=null;    //��ʼʱ��
			Date ed=null;    //����ʱ��
			int frequency=Integer.parseInt(freqId);
			int year=Integer.parseInt(pYear);
			int term=Integer.parseInt(pTerm);
			int month=1;
			if(frequency==Config.FREQUENCY_MONTH){
				month=term;
				sd=DateUtil.getFormatDate(year+"-"+month,DateUtil.YEAR+"-"+DateUtil.MONTH);
				ed=BusinessCommonUtil.getReferDate(sd,reportId,versionId,new Integer(Integer.parseInt(dataRange)),new Integer(frequency));
				tempDate[0]=sd;
				tempDate[1]=ed;
			}else if(frequency==Config.FREQUENCY_SEASON){
				if(term==1){
					month=4;
				}else if(term==2){
					month=7;
				}else if(term==3){
					month=10;
				}else if(term==4){
					year=year+1;
					month=1;
				}
				sd=DateUtil.getFormatDate(year+"-"+month,DateUtil.YEAR+"-"+DateUtil.MONTH);
				ed=BusinessCommonUtil.getReferDate(sd,reportId,versionId,new Integer(Integer.parseInt(dataRange)),new Integer(frequency));
				tempDate[0]=sd;
				tempDate[1]=ed;
			}else if(frequency==Config.FREQUENCY_HALF_YEAR){
				if(term==1){
					month=7;
				}else{
					year=year+1;
					month=1;
				}
				sd=DateUtil.getFormatDate(year+"-"+month,DateUtil.YEAR+"-"+DateUtil.MONTH);
				ed=BusinessCommonUtil.getReferDate(sd,reportId,versionId,new Integer(Integer.parseInt(dataRange)),new Integer(frequency));
				tempDate[0]=sd;
				tempDate[1]=ed;
			}else if(frequency==Config.FREQUENCY_YEAR){
				year=year+1;
				month=1;
				sd=DateUtil.getFormatDate(year+"-"+month,DateUtil.YEAR+"-"+DateUtil.MONTH);
				ed=BusinessCommonUtil.getReferDate(sd,reportId,versionId,new Integer(Integer.parseInt(dataRange)),new Integer(frequency));
				tempDate[0]=sd;
				tempDate[1]=ed;
			}
			// System.out.println("��ʼʱ��: "+tempDate[0]+"����ʱ��: "+tempDate[1]);
			return tempDate;
		}

	/**
	 * author linfeng
	 * function �õ���ǰ�����ϱ�����ʱӦ�ö�Ӧ�����ڣ�����ӦƵ��ȷ��ʱ�䷶Χ (Ĭ��Ϊ��ǰʱ��)
	 * @return Date[] length ������ӦƵ�ʺ�ʱ��Ŀ�ʼ�ͽ���ʱ�䣬 Date[0]��ʾ��ʼʱ�� Date[1]��ʾ����ʱ��
	 */
	public static Date[] getFrequencyAndTerm(int frequency){
		int year=Integer.parseInt(DateUtil.getToday(DateUtil.YEAR));
		int month=Integer.parseInt(DateUtil.getToday(DateUtil.MONTH));
		return getFrequencyAndTerm(frequency,year,month);
	}
	
	/**
	 * author linfeng
	 * function �õ���ǰ�����ϱ�����ʱӦ�ö�Ӧ�����ڣ�����ӦƵ��ȷ��ʱ�䷶Χ
	 * @return Date[] length ������ӦƵ�ʺ�ʱ��Ŀ�ʼ�ͽ���ʱ�䣬 Date[0]��ʾ��ʼʱ�� Date[1]��ʾ����ʱ��
	 */
	public static Date[] getFrequencyAndTerm(int frequency,int year,int month){
		Date[] tempDate=new Date[2];
		//int[] tempMonth= new int[1];
		//int[] tempSeason;
		//int[] tempHalfYear;
		//int[] tempYear;
		//int tempDay=Integer.parseInt(DateUtil.getToday(DateUtil.DAY));
		
		String strYear ="";
		String strMonth ="";
		
		
		if(frequency==Config.FREQUENCY_MONTH){
            //	���Ƶ������
			if(month==Integer.parseInt(DateUtil.getToday(DateUtil.MONTH)) && year==Integer.parseInt(DateUtil.getToday(DateUtil.YEAR))){
				//����ǵ��굱ǰ��
				if(month==1){
					//����ǵ�һ�£��������������·�
					strYear=new Integer((year-1)).toString();
					strMonth="12";
					tempDate[0]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR);
					tempDate[1]=tempDate[0];
					return tempDate;
				}else{
					strYear=new Integer(year).toString();
					strMonth=new Integer(month-1).toString();
					tempDate[0]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR);
					tempDate[1]=tempDate[0];
                    //Ƶ���·�ֻ����ʾһ���£����Է��ص�����ֻ����һ��������ֵ
					return tempDate;  
				}
			}
		}else if(frequency==Config.FREQUENCY_SEASON){
            //	���Ƶ���Ǽ�
			if(year==Integer.parseInt(DateUtil.getToday(DateUtil.YEAR))){
			// ����ǵ���
			   if(month<3){
				   //����ǵ���3�£�����ǰһ��9-12�·�
					strYear=new Integer(year-1).toString();
					strMonth=new Integer(9).toString();
					tempDate[0]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR);
					strMonth=new Integer(12).toString();
					tempDate[1]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR);
					return tempDate;
			   }else if(month>3 && month<6){
				   //�����3 �� 6 ��֮�䣬����1-3�·�
					strYear=new Integer(year).toString();
					strMonth=new Integer(1).toString();
					tempDate[0]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR);
					strMonth=new Integer(3).toString();
					tempDate[1]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR);
					return tempDate; 
			   }else if(month>6 && month<9){
				   //�����6 �� 9 ��֮�䣬����4-6�·�
					strYear=new Integer(year).toString();
					strMonth=new Integer(4).toString();
					tempDate[0]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR);
					strMonth=new Integer(6).toString();
					tempDate[1]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR);
					return tempDate; 
			   }else if(month>9 && month<12){
				   //�����9 �� 12 ��֮�䣬����7-9�·�
					strYear=new Integer(year).toString();
					strMonth=new Integer(7).toString();
					tempDate[0]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR);
					strMonth=new Integer(9).toString();
					tempDate[1]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR);
					return tempDate; 
			   }
			}else{
				//����������ǵ���
				   if(month<3){
					   //����1-3�·�
						strYear=new Integer(year).toString();
						strMonth=new Integer(1).toString();
						tempDate[0]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR+"-"+DateUtil.MONTH);
						strMonth=new Integer(3).toString();
						tempDate[1]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR+"-"+DateUtil.MONTH);
						return tempDate;
				   }else if(month>3 && month<6){
					   //�����3 �� 6 ��֮�䣬����3-6�·�
						strYear=new Integer(year).toString();
						strMonth=new Integer(3).toString();
						tempDate[0]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR+"-"+DateUtil.MONTH);
						strMonth=new Integer(6).toString();
						tempDate[1]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR+"-"+DateUtil.MONTH);
						return tempDate; 
				   }else if(month>6 && month<9){
					   //�����6 �� 9 ��֮�䣬����6-9�·�
						strYear=new Integer(year).toString();
						strMonth=new Integer(6).toString();
						tempDate[0]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR+"-"+DateUtil.MONTH);
						strMonth=new Integer(9).toString();
						tempDate[1]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR+"-"+DateUtil.MONTH);
						return tempDate; 
				   }else if(month>9 && month<12){
					   //�����9 �� 12 ��֮�䣬����9-12�·�
						strYear=new Integer(year).toString();
						strMonth=new Integer(9).toString();
						tempDate[0]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR+"-"+DateUtil.MONTH);
						strMonth=new Integer(12).toString();
						tempDate[1]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR+"-"+DateUtil.MONTH);
						return tempDate; 
				   }
				
			}
		}else if(frequency==Config.FREQUENCY_HALF_YEAR){
			 //	���Ƶ���ǰ���
			if(year==Integer.parseInt(DateUtil.getToday(DateUtil.YEAR))){
				//����ǵ���
				if(month>1 && month<6){
				   //������ϰ��꣬����ǰһ����°���
					strYear=new Integer(year-1).toString();
					strMonth=new Integer(7).toString();
					tempDate[0]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR+"-"+DateUtil.MONTH);
					strMonth=new Integer(12).toString();
					tempDate[1]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR+"-"+DateUtil.MONTH);
					return tempDate; 
				}else{
					 //������°��꣬�����ϰ���
					strYear=new Integer(year).toString();
					strMonth=new Integer(1).toString();
					tempDate[0]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR+"-"+DateUtil.MONTH);
					strMonth=new Integer(6).toString();
					tempDate[1]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR+"-"+DateUtil.MONTH);
					return tempDate; 
				}
			}else{
				//������ǵ���
				if(month>1 && month<6){
					 //������ϰ��꣬�����ϰ���
					strYear=new Integer(year).toString();
					strMonth=new Integer(1).toString();
					tempDate[0]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR+"-"+DateUtil.MONTH);
					strMonth=new Integer(6).toString();
					tempDate[1]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR+"-"+DateUtil.MONTH);
					return tempDate; 
				}else{
					 //������°��꣬�����°���
					strYear=new Integer(year).toString();
					strMonth=new Integer(7).toString();
					tempDate[0]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR+"-"+DateUtil.MONTH);
					strMonth=new Integer(12).toString();
					tempDate[1]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR+"-"+DateUtil.MONTH);
					return tempDate; 
				}
			}
		}else if(frequency==Config.FREQUENCY_YEAR){
			 //	���Ƶ������
			if(year==Integer.parseInt(DateUtil.getToday(DateUtil.YEAR))){
				//����ǵ��꣬������һ��
				strYear=new Integer(year-1).toString();
				tempDate[0]=DateUtil.getDateByString(strYear,DateUtil.YEAR);
				tempDate[1]=DateUtil.getDateByString(strYear,DateUtil.YEAR);
				return tempDate;
			}else{
				//�������ʷ��ݣ����ش���ݱ���
				strYear=new Integer(year).toString();
				tempDate[0]=DateUtil.getDateByString(strYear,DateUtil.YEAR);
				tempDate[1]=DateUtil.getDateByString(strYear,DateUtil.YEAR);
				return tempDate;
			}
		}
		return null;
	}
	
	
	


}
