package com.fitech.net.collect.bean;

import java.io.Serializable;

/**
*
* <p>Title: CollectReport  bean</p>
*
* <p>Description �������bean����¼ÿ�ű���ÿ������ÿ���л���Ȩ�޵Ľ���
*       ���������һ�λ�������</p>
*
* <p>Copyright: Copyright (c) 2007</p>
*
* <p>Company: </p>
*
* @author   ����
* @date     2009-09-11
* @version 1.0
*/

public class CollectReport implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**����ʵ�ʱ��� Id*/
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
