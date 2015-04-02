package com.fitech.net.collect.bean;

import java.io.Serializable;

/**
*
* <p>Title: CollectReport  bean</p>
*
* <p>Description 汇总情况bean，记录每张报表每个期数每个有汇总权限的金融
*       机构的最后一次汇总数据</p>
*
* <p>Copyright: Copyright (c) 2007</p>
*
* <p>Company: </p>
*
* @author   龚明
* @date     2009-09-11
* @version 1.0
*/

public class CollectReport implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**内网实际表单表 Id*/
   private Integer repInId = null;
   
   private CollectReportPK id = new CollectReportPK();
   

    public CollectReportPK getId() {
        return id;
    }
    
    public void setId(CollectReportPK id) {
        this.id = id;
    }

    public CollectReport() {}

    public Integer getRepInId() {
        return repInId;
    }

    public void setRepInId(Integer repInId) {
        this.repInId = repInId;
    }
   
}
