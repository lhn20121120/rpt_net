package com.fitech.gznx.util;

import java.io.File;
import java.util.List;

import com.fitech.gznx.common.Config;

public class CleanTempFile {
	
	public static boolean cleanTempFile(List filelist){
		
		boolean result = false;
		
		if(filelist!=null&&filelist.size()!=0){
			
			for(int i=0 ; i<filelist.size(); i++){
				
				//System.out.println((String)filelist.get(i));
			
				File file = new File((String)filelist.get(i));
				
				file.delete();
				
				//System.out.println("ɾ����ʱ�ļ�������"+(String)filelist.get(i));
			
			}
		}
		return result;
	}

}
