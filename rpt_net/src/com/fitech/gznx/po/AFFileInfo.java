package com.fitech.gznx.po;

import java.sql.Blob;
import java.util.HashSet;
import java.util.Set;

public class AFFileInfo implements java.io.Serializable {


    // Fields    

     private Integer fileId;
     private Blob fileContent;
     private String fileName;
     private String fileContentType;
     private Integer fileSize;
     private Integer fileType;
   


    // Constructors

    /** default constructor */
    public AFFileInfo() {
    }

	/** minimal constructor */
    public AFFileInfo(Integer fileId) {
        this.fileId = fileId;
    }
    
    /** full constructor */
    public AFFileInfo(Integer fileId, Blob fileContent, String fileName,  String fileContentType, Integer fileSize, Integer fileType) {
        this.fileId = fileId;
        this.fileContent = fileContent;
        this.fileName = fileName;
        this.fileContentType =  fileContentType;
        this.fileSize = fileSize;
        this.fileType = fileType;        
    }

   
    // Property accessors

    public Integer getFileId() {
        return this.fileId;
    }
    
    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public Blob getFileContent() {
        return this.fileContent;
    }
    
    public void setFileContent(Blob fileContent) {
        this.fileContent = fileContent;
    }

    public String getFileName() {
        return this.fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    

    public String getFileContentType()
	{
		return fileContentType;
	}

	public void setFileContentType(String fileContentType)
	{
		this.fileContentType = fileContentType;
	}

	public Integer getFileSize() {
        return this.fileSize;
    }
    
    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public Integer getFileType() {
        return this.fileType;
    }
    
    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

}
