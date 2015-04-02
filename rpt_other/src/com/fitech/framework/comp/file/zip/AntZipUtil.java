package com.fitech.framework.comp.file.zip;

import java.io.File;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;

public class AntZipUtil extends ZipUtils{
	public void antZip(String dest,String src){
		Zip zip = new Zip();
	   zip.setBasedir(new File(src));
	   zip.setDestFile(new File(dest));
	   Project p = new Project();
	   p.setBaseDir(new File(src));
	   zip.setProject(p);
	   zip.execute();
	}
}
