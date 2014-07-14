package com.gather.down.reports;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Blob;

import com.gather.common.Config;
import com.gather.servlets.AutoSynchronized;

public class BlobMaker {

     private java.sql.Blob _pdf=null;
     private String _fileName="";
     private String _outDir=AutoSynchronized.SMMIS_PATH+Config.FILESEPARATOR+Config.DOWNLOAD_REPORT+Config.FILESEPARATOR+"pdf";
     
     
     public File make(Blob pdf,String fileName){
    	 this._pdf=pdf;
    	 this._fileName=fileName;
    	 return process();
     }
     
     private File process(){
    	 // System.out.println("-file name is:-"+this._outDir+Config.FILESEPARATOR+this._fileName);
		 File pdfFile = new File(this._outDir+Config.FILESEPARATOR+this._fileName);
		InputStream in = null;		
		if (this._pdf != null) {
			try{
				in = this._pdf.getBinaryStream();
			}
			catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		try{
			FileOutputStream out = new FileOutputStream(pdfFile);
			byte[] buffer = new byte[1024];
			while (in.read(buffer) != -1)
				out.write(buffer);
			in.close();
			out.flush();
			out.close();
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}		
		return pdfFile;
     }

}
