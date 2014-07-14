package com.fitech.gznx.action;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MCellFormuForm;
import com.fitech.gznx.po.AfValidateformula;
import com.fitech.gznx.service.StrutsAFCellFormuDelegate;

public class DownBJGXAction extends Action{

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		MCellFormuForm mCellFormuForm=(MCellFormuForm)form;
		//获取表达式集合
		String fileName = mCellFormuForm.getChildRepId()+"_"+mCellFormuForm.getVersionId()+".txt";
		String filePath = Config.TEMP_DIR+fileName;
		List<AfValidateformula> resList=StrutsAFCellFormuDelegate.selectAllFormulaByTemplate(mCellFormuForm.getChildRepId(),mCellFormuForm.getVersionId());
		File file = new File(filePath);
		long length = file.length();
		
		if(!file.canWrite()){
			file.createNewFile();
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		try {
			for(AfValidateformula a : resList){
				String bj = "";
				int validateTypeId = a.getValidateTypeId().intValue();
				System.out.println(validateTypeId);
				if(validateTypeId==2)
					bj="BJ:";
				String forName = a.getFormulaName();
				if(forName.indexOf("※")<0)//不包含※ 则添加
					forName = "※"+forName;
				String s = bj+a.getFormulaValue()+forName;
				bw.write(s);
				bw.write("\r\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			bw.flush();
			if(bw!=null)
				bw.close();
		}
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition","attachment;filename=" + fileName); 
		response.setHeader("Content_Length",String.valueOf(length));
		
		OutputStream output = null;
		InputStream input = null;
		try {
			output = response.getOutputStream();
			input = new FileInputStream(file);
			byte[] buffer = new byte[input.available()];
			input.read(buffer);
			output.write(buffer);
			output.flush();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(output!=null)
				output.close();
			if(input!=null)
				input.close();
		}
		
		return null;
	}
	
}
