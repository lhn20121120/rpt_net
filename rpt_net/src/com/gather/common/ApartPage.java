/*
 * Created on 2005-5-23
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.gather.common;

import java.io.Serializable;
/**
 * @author rds
 * 
 * ��ѯ��ҳ��
 */
public class ApartPage implements Serializable{
  private final int TRUE=1;
  private final int FLASE=0;
 /**
  * ��¼����
  */
  private int count;
  
 /**
  * ��ҳ��
  */
  private int pages;
  
 /**
  * ��ǰҳ��
  */
  private int curPage;
  
 /**
  * ÿҳ��ʾ��¼��
  */
  private int rows;
  
 /**
  * ��ѯ�ĸ������� 
  */
  private String term="";
  
 /**
  * �Ƿ��������
  */
  private int isCanForward;
  
 /**
  * �Ƿ����ǰ��� 
  */
  private int isCanBack;
  
 /**
  * firstPageUrl
  */
  private String firstPageUrl;
  
 /**
  * prevPageUrl 
  */
  private String prevPageUrl;
  
 /**
  * nextPageUrl
  */
  private String nextPageUrl;
  
 /**
  * lastPageUrl
  */
  private String lastPageUrl;
  
 /**
  * ��ҳ
  */
  private int firstPage=1;
  
 /**
  * ��һҳ
  */
  private int previousPage;
  
 /**
  * ��һҳ
  */
  private int nextPage;
  
 /**
  * ���һҳ
  */
  private int lastPage;
   
 /**
  * ���캯��
  */
  public ApartPage(){
  	this.count=0;
  	this.pages=1;
  	this.curPage=1;
  	this.isCanBack=FLASE;
  	this.isCanForward=FLASE;
  }
 /**
  * setCount����
  * 
  * @param count int 
  * @return void
  */
  public void setCount(int count){
  	this.count=count;
  }
  
 /**
  * getCount����
  * 
  * @return int
  */
  public int getCount(){
  	return this.count;
  }
  
 /**
  * setPages����
  * 
  * @param pages int 
  * @return void
  */
  public void setPages(int pages){
  	this.pages=pages;
  }
  
 /**
  * getPages����
  * 
  * @return  int
  */
  public int getPages(){
  	if(this.count>0){
  	  if(Config.PER_PAGE_ROWS>0){
  	  	return (int)Math.ceil(count/(double)Config.PER_PAGE_ROWS);
  	  }else{
  	  	return 1;
  	  }
  	}else{
  	  return 1;
  	}
  }
  
 /**
  * setCurPage����
  * 
  * @param curPage int
  * @return void
  */
  public void setCurPage(int curPage){
  	this.curPage=curPage;
  }
  
 /**
  * getCurPage����
  * 
  * @return int
  */
  public int getCurPage(){
  	return this.curPage;
  }
  
 /**
  * setRows���� 
  * 
  * @param rows int 
  * @return void
  */
  public void setRows(int rows){
  	this.rows=rows;
  }
  
 /**
  * getRows����
  * 
  * @return int
  */
  public int getRows(){
  	return this.rows;
  }
  
 /**
  * setTerm����
  * 
  * @param term String
  * @return void
  */
  public void setTerm(String term){
  	this.term=term;
  }
  
 /**
  * getTerm����
  * 
  * @return String
  */
  public String getTerm(){
  	return this.term;
  }
  
 /**
  * getIsCanForward����
  * 
  * @return int
  */  
  public int getIsCanForward(){
    if(getPages()>this.curPage)
      return TRUE;
    else
      return FLASE;
  }
  
 /**
  * getIsCanBack����
  * 
  * @return int 
  */
  public int getIsCanBack(){
  	if(this.curPage>1)
  	  return TRUE;
  	else
  	  return FLASE;
  }
  
 /**
  * getFirstPageUrl���� 
  * 
  * @return String
  */
  public String getFirstPageUrl(){
    return term + (term.equals("")?"":"&") + "curPage=1";	
  }
  
 /**
  * getPrevPageUrl����
  * 
  * @return String
  */
  public String getPrevPageUrl(){
  	return term + (term.equals("")?"":"&") + "curPage=" + (curPage-1); 
  }
  
 /**
  * getNextPageUrl����
  * 
  * @return String
  */
  public String getNextPageUrl(){
  	return term + (term.equals("")?"":"&") + "curPage=" + (curPage+1);
  }
  
 /**
  * getLastPageUrl����
  * 
  * @return String
  */
  public String getLastPageUrl(){
  	return term + (term.equals("")?"":"&") + "curPage=" + getPages();
  }
  
  /**
   * ������ҳ��
   * 
   * @param firstPage int ��ҳ��
   * @return void
   */
   public void setFirstPage(int firstPage){
   	this.firstPage=firstPage;
   }
   
  /**
   * ��ȡ��ҳ��
   *
   * @return int
   */
   public int getFirstPage(){
     return this.firstPage;	
   }
   
  /**
   * ������һҳ��
   *
   * @param previousPage ��һҳ��
   * @return void
   */
   public void setPreviousPage(int previousPage){
   	this.previousPage=previousPage;
   }
   
  /**
   * ��ȡ��һҳ��
   *
   * @return int
   */
   public int getPreviousPage(){
   	return this.previousPage>0?this.previousPage:this.curPage-1;
   }
   
  /**
   * ������һҳ��
   *
   * @param nextPage ��һҳ��
   * @return void
   */
   public void setNextPage(int nextPage){
   	this.nextPage=nextPage;
   }
    
  /**
   * ��ȡ��һҳ��
   *
   * @return int
   */
   public int getNextPage(){
   	return this.nextPage>0?this.nextPage:this.curPage+1;
   }
   
  /**
   * ����βҳ��
   *
   * @param previousPage βҳ��
   * @return void
   */
   public void setLastPage(int lastPage){
   	this.lastPage=lastPage;
   }
    
  /**
   * ��ȡβ��
   *
   * @return int
   */
   public int getLastPage(){
   	return this.lastPage>0?this.lastPage:getPages();
   }
    
}
