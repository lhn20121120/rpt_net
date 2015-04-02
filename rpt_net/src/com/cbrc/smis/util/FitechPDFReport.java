package com.cbrc.smis.util;

import java.util.List;

import com.cbrc.smis.adapter.StrutsMMainRepDelegate;
import com.cbrc.smis.form.MChildReportForm;
import com.cbrc.smis.form.MMainRepForm;
/**
 * PDF����ģ��ı�����
 * 
 * @author rds
 * @date 2005-12-06
 */
public class FitechPDFReport {
	/**
	 * ����ı���������
	 */
	public static final String TITLE="fitechTitle";
	
	/**
	 * ������ӱ���������
	 */
	//public static final String SUBTITLE="fitechSubTitle";
	public static final String SUBTITLE="fitechSubtitle";
	
	/**
	 * ����Ļ��ҵ�λ������
	 */
	public static final String CURUNIT="fitechUnit";
	
	/**
	 * ����İ汾��������
	 */
	public static final String version="fitechVersion";
	
	/**
	 * ����ģ����Ϣ<br>
	 * ע:<code>�˷����ʺ��ڵ�Ե�ʽģ��ı���</code>
	 * 
	 * @param mMainRepForm MMainRepForm 
	 * @param MChildReportForm mChildReportForm
	 * @param List cells
	 * @return void boolean д�����ɹ�������true;���򣬷���false
	 */
	public boolean saveBaseReportTemplate(MMainRepForm mMainRepForm,MChildReportForm mChildReportForm,List cells){
		boolean resSave=false;
		
		if(mMainRepForm==null) return resSave;
		
		resSave=StrutsMMainRepDelegate.savePatch(mMainRepForm,mChildReportForm,cells);
		
		return resSave;
	}
	
	/**
	 * ����ģ����Ϣ<br>
	 * ע:<code>�˷����ʺ����嵥ʽģ��ı���</code>
	 * 
	 * @param mMainRepForm MMainRepForm 
	 * @param MChildReportForm mChildReportForm
	 * @param List cols
	 * @return void boolean д�����ɹ�������true;���򣬷���false
	 */
	public boolean saveEspReportTemplate(MMainRepForm mMainRepForm,MChildReportForm mChildReportForm,List cols){
		boolean resSave=false;
		
		if(mMainRepForm==null) return resSave;
		
		resSave=StrutsMMainRepDelegate.saveEspPatch(mMainRepForm,mChildReportForm,cols);
		
		return resSave;
	}
	
	/**
	 * ����ģ����Ϣ<br>
	 * ע:<code>�˷����ʺ����嵥ʽģ��ı���</code>
	 * 
	 * @param mMainRepForm MMainRepForm 
	 * @param MChildReportForm mChildReportForm
	 * @param List cells
	 * @return void boolean д�����ɹ�������true;���򣬷���false
	 */
}
