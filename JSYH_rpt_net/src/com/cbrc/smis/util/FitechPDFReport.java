package com.cbrc.smis.util;

import java.util.List;

import com.cbrc.smis.adapter.StrutsMMainRepDelegate;
import com.cbrc.smis.form.MChildReportForm;
import com.cbrc.smis.form.MMainRepForm;
/**
 * PDF报表模板的报表类
 * 
 * @author rds
 * @date 2005-12-06
 */
public class FitechPDFReport {
	/**
	 * 报表的标题域名称
	 */
	public static final String TITLE="fitechTitle";
	
	/**
	 * 报表的子标题域名称
	 */
	//public static final String SUBTITLE="fitechSubTitle";
	public static final String SUBTITLE="fitechSubtitle";
	
	/**
	 * 报表的货币单位域名称
	 */
	public static final String CURUNIT="fitechUnit";
	
	/**
	 * 报表的版本号域名称
	 */
	public static final String version="fitechVersion";
	
	/**
	 * 保存模板信息<br>
	 * 注:<code>此方法适合于点对点式模板的保存</code>
	 * 
	 * @param mMainRepForm MMainRepForm 
	 * @param MChildReportForm mChildReportForm
	 * @param List cells
	 * @return void boolean 写操作成功，返回true;否则，返回false
	 */
	public boolean saveBaseReportTemplate(MMainRepForm mMainRepForm,MChildReportForm mChildReportForm,List cells){
		boolean resSave=false;
		
		if(mMainRepForm==null) return resSave;
		
		resSave=StrutsMMainRepDelegate.savePatch(mMainRepForm,mChildReportForm,cells);
		
		return resSave;
	}
	
	/**
	 * 保存模板信息<br>
	 * 注:<code>此方法适合于清单式模板的保存</code>
	 * 
	 * @param mMainRepForm MMainRepForm 
	 * @param MChildReportForm mChildReportForm
	 * @param List cols
	 * @return void boolean 写操作成功，返回true;否则，返回false
	 */
	public boolean saveEspReportTemplate(MMainRepForm mMainRepForm,MChildReportForm mChildReportForm,List cols){
		boolean resSave=false;
		
		if(mMainRepForm==null) return resSave;
		
		resSave=StrutsMMainRepDelegate.saveEspPatch(mMainRepForm,mChildReportForm,cols);
		
		return resSave;
	}
	
	/**
	 * 保存模板信息<br>
	 * 注:<code>此方法适合于清单式模板的保存</code>
	 * 
	 * @param mMainRepForm MMainRepForm 
	 * @param MChildReportForm mChildReportForm
	 * @param List cells
	 * @return void boolean 写操作成功，返回true;否则，返回false
	 */
}
