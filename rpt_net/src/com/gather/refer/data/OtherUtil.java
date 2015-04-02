package com.gather.refer.data;

import java.util.Date;

import com.gather.common.BusinessCommonUtil;
import com.gather.common.Config;
import com.gather.common.DateUtil;

public class OtherUtil {
	
	/**
	 * @author linfeng
	 * @function 得到 频率和期数相应的 开始时间 和 结束时间
	 * @param freqId
	 * @param year
	 * @param term
	 * @return Date[] Date[0] 开始时间 Date[1] 结束时间
	 */
		
		public static Date[] getStartAndEndDate(String reportId,String versionId,String dataRange,String freqId,String pYear,String pTerm) throws Exception{
			Date[] tempDate=new Date[2];
			Date sd=null;    //开始时间
			Date ed=null;    //结束时间
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
			// System.out.println("开始时间: "+tempDate[0]+"结束时间: "+tempDate[1]);
			return tempDate;
		}

	/**
	 * author linfeng
	 * function 得到当前机构上报数据时应该对应的日期，给对应频率确定时间范围 (默认为当前时间)
	 * @return Date[] length 返回相应频率和时间的开始和结束时间， Date[0]表示开始时间 Date[1]表示结束时间
	 */
	public static Date[] getFrequencyAndTerm(int frequency){
		int year=Integer.parseInt(DateUtil.getToday(DateUtil.YEAR));
		int month=Integer.parseInt(DateUtil.getToday(DateUtil.MONTH));
		return getFrequencyAndTerm(frequency,year,month);
	}
	
	/**
	 * author linfeng
	 * function 得到当前机构上报数据时应该对应的日期，给对应频率确定时间范围
	 * @return Date[] length 返回相应频率和时间的开始和结束时间， Date[0]表示开始时间 Date[1]表示结束时间
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
            //	如果频率是月
			if(month==Integer.parseInt(DateUtil.getToday(DateUtil.MONTH)) && year==Integer.parseInt(DateUtil.getToday(DateUtil.YEAR))){
				//如果是当年当前月
				if(month==1){
					//如果是第一月，返回上年的年底月份
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
                    //频率月分只会显示一个月，所以返回的数组只给第一个索引赋值
					return tempDate;  
				}
			}
		}else if(frequency==Config.FREQUENCY_SEASON){
            //	如果频率是季
			if(year==Integer.parseInt(DateUtil.getToday(DateUtil.YEAR))){
			// 如果是当年
			   if(month<3){
				   //如果是当年3月，返回前一年9-12月份
					strYear=new Integer(year-1).toString();
					strMonth=new Integer(9).toString();
					tempDate[0]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR);
					strMonth=new Integer(12).toString();
					tempDate[1]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR);
					return tempDate;
			   }else if(month>3 && month<6){
				   //如果是3 至 6 月之间，返回1-3月份
					strYear=new Integer(year).toString();
					strMonth=new Integer(1).toString();
					tempDate[0]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR);
					strMonth=new Integer(3).toString();
					tempDate[1]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR);
					return tempDate; 
			   }else if(month>6 && month<9){
				   //如果是6 至 9 月之间，返回4-6月份
					strYear=new Integer(year).toString();
					strMonth=new Integer(4).toString();
					tempDate[0]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR);
					strMonth=new Integer(6).toString();
					tempDate[1]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR);
					return tempDate; 
			   }else if(month>9 && month<12){
				   //如果是9 至 12 月之间，返回7-9月份
					strYear=new Integer(year).toString();
					strMonth=new Integer(7).toString();
					tempDate[0]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR);
					strMonth=new Integer(9).toString();
					tempDate[1]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR);
					return tempDate; 
			   }
			}else{
				//季：如果不是当年
				   if(month<3){
					   //返回1-3月份
						strYear=new Integer(year).toString();
						strMonth=new Integer(1).toString();
						tempDate[0]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR+"-"+DateUtil.MONTH);
						strMonth=new Integer(3).toString();
						tempDate[1]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR+"-"+DateUtil.MONTH);
						return tempDate;
				   }else if(month>3 && month<6){
					   //如果是3 至 6 月之间，返回3-6月份
						strYear=new Integer(year).toString();
						strMonth=new Integer(3).toString();
						tempDate[0]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR+"-"+DateUtil.MONTH);
						strMonth=new Integer(6).toString();
						tempDate[1]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR+"-"+DateUtil.MONTH);
						return tempDate; 
				   }else if(month>6 && month<9){
					   //如果是6 至 9 月之间，返回6-9月份
						strYear=new Integer(year).toString();
						strMonth=new Integer(6).toString();
						tempDate[0]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR+"-"+DateUtil.MONTH);
						strMonth=new Integer(9).toString();
						tempDate[1]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR+"-"+DateUtil.MONTH);
						return tempDate; 
				   }else if(month>9 && month<12){
					   //如果是9 至 12 月之间，返回9-12月份
						strYear=new Integer(year).toString();
						strMonth=new Integer(9).toString();
						tempDate[0]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR+"-"+DateUtil.MONTH);
						strMonth=new Integer(12).toString();
						tempDate[1]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR+"-"+DateUtil.MONTH);
						return tempDate; 
				   }
				
			}
		}else if(frequency==Config.FREQUENCY_HALF_YEAR){
			 //	如果频率是半年
			if(year==Integer.parseInt(DateUtil.getToday(DateUtil.YEAR))){
				//如果是当年
				if(month>1 && month<6){
				   //如果是上半年，返回前一年的下半年
					strYear=new Integer(year-1).toString();
					strMonth=new Integer(7).toString();
					tempDate[0]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR+"-"+DateUtil.MONTH);
					strMonth=new Integer(12).toString();
					tempDate[1]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR+"-"+DateUtil.MONTH);
					return tempDate; 
				}else{
					 //如果是下半年，返回上半年
					strYear=new Integer(year).toString();
					strMonth=new Integer(1).toString();
					tempDate[0]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR+"-"+DateUtil.MONTH);
					strMonth=new Integer(6).toString();
					tempDate[1]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR+"-"+DateUtil.MONTH);
					return tempDate; 
				}
			}else{
				//如果不是当年
				if(month>1 && month<6){
					 //如果是上半年，返回上半年
					strYear=new Integer(year).toString();
					strMonth=new Integer(1).toString();
					tempDate[0]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR+"-"+DateUtil.MONTH);
					strMonth=new Integer(6).toString();
					tempDate[1]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR+"-"+DateUtil.MONTH);
					return tempDate; 
				}else{
					 //如果是下半年，返回下半年
					strYear=new Integer(year).toString();
					strMonth=new Integer(7).toString();
					tempDate[0]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR+"-"+DateUtil.MONTH);
					strMonth=new Integer(12).toString();
					tempDate[1]=DateUtil.getDateByString(strYear+"-"+strMonth,DateUtil.YEAR+"-"+DateUtil.MONTH);
					return tempDate; 
				}
			}
		}else if(frequency==Config.FREQUENCY_YEAR){
			 //	如果频率是年
			if(year==Integer.parseInt(DateUtil.getToday(DateUtil.YEAR))){
				//如果是当年，返回上一年
				strYear=new Integer(year-1).toString();
				tempDate[0]=DateUtil.getDateByString(strYear,DateUtil.YEAR);
				tempDate[1]=DateUtil.getDateByString(strYear,DateUtil.YEAR);
				return tempDate;
			}else{
				//如果是历史年份，返回此年份报表
				strYear=new Integer(year).toString();
				tempDate[0]=DateUtil.getDateByString(strYear,DateUtil.YEAR);
				tempDate[1]=DateUtil.getDateByString(strYear,DateUtil.YEAR);
				return tempDate;
			}
		}
		return null;
	}
	
	
	


}
