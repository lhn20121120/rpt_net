package com.fitech.model.etl.model.pojo;



/**
 * ETLCalendar entity. @author MyEclipse Persistence Tools
 */

public class ETLCalendar  implements java.io.Serializable {


    // Fields    

     private String etlDate;
     private Integer calendarType;
     private Integer workFlag = 0;
     
     

    // Constructors

    /** default constructor */
    public ETLCalendar() {
    }

    
    /** full constructor */
    public ETLCalendar(Integer calendarType, Integer workFlag) {
        this.calendarType = calendarType;
        this.workFlag = workFlag;
    }

   
    // Property accessors

    public String getEtlDate() {
        return this.etlDate;
    }
    
    public void setEtlDate(String etlDate) {
        this.etlDate = etlDate;
    }
    
    /***
     * 1：节假日  2：双休日
     * @return
     */
    public Integer getCalendarType() {
        return this.calendarType;
    }
    
    /***
     * 1：节假日  2：双休日
     * @return
     */
    public void setCalendarType(Integer calendarType) {
        this.calendarType = calendarType;
    }
    
    /***
     * 1：工作   2：不工作
     * @return
     */
    public Integer getWorkFlag() {
        return this.workFlag;
    }
    
    /***
     * 0：不工作  1：工作
     * @return
     */
    public void setWorkFlag(Integer workFlag) {
        this.workFlag = workFlag;
    }
   








}