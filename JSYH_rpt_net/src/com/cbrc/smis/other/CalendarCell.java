package com.cbrc.smis.other;
/**
 * 模块:工作日历设定
 * 
 * @author 姚捷
 * 一个日历单元格
 *@param day 日期
 *@param isSetting 是否已经被设置成工作日
 */
public class CalendarCell {
	private int day;
	private boolean isSetting;
	
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public boolean isSetting() {
		return isSetting;
	}
	public void setSetting(boolean isSetting) {
		this.isSetting = isSetting;
	}
	
	
}
