package com.cbrc.smis.hibernate;

public class Bulletin
{
    private Integer bullId;
    
    private String  bullTitle;
    
    private String  bullContent;
    
    private Integer bullState;
    
    private String  addTime;

    private String  uploadFileName;
    
    private String virtualName;
    
    public String getUploadFileName()
    {
        return uploadFileName;
    }

    public void setUploadFileName(String uploadFileName)
    {
        this.uploadFileName = uploadFileName;
    }

    public String getVirtualName()
    {
        return virtualName;
    }

    public void setVirtualName(String virtualName)
    {
        this.virtualName = virtualName;
    }

    public String getAddTime()
    {
        return addTime;
    }

    public void setAddTime(String addTime)
    {
        this.addTime = addTime;
    }

    public Integer getBullState()
    {
        return bullState;
    }

    public void setBullState(Integer bullState)
    {
        this.bullState = bullState;
    }

    public Integer getBullId()
    {
        return bullId;
    }

    public void setBullId(Integer bullId)
    {
        this.bullId = bullId;
    }

   

    public String getBullTitle()
    {
        return bullTitle;
    }

    public void setBullTitle(String bullTitle)
    {
        this.bullTitle = bullTitle;
    }

    public String getBullContent()
    {
        return bullContent;
    }

    public void setBullContent(String bullContent)
    {
        this.bullContent = bullContent;
    }   
}
