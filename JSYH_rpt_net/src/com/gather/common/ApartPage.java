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
 * 查询分页类
 */
public class ApartPage implements Serializable{
  private final int TRUE=1;
  private final int FLASE=0;
 /**
  * 记录总数
  */
  private int count;
  
 /**
  * 总页数
  */
  private int pages;
  
 /**
  * 当前页数
  */
  private int curPage;
  
 /**
  * 每页显示记录数
  */
  private int rows;
  
 /**
  * 查询的附加条件 
  */
  private String term="";
  
 /**
  * 是否可向后浏览
  */
  private int isCanForward;
  
 /**
  * 是否可向前浏览 
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
  * 首页
  */
  private int firstPage=1;
  
 /**
  * 上一页
  */
  private int previousPage;
  
 /**
  * 下一页
  */
  private int nextPage;
  
 /**
  * 最后一页
  */
  private int lastPage;
   
 /**
  * 构造函数
  */
  public ApartPage(){
  	this.count=0;
  	this.pages=1;
  	this.curPage=1;
  	this.isCanBack=FLASE;
  	this.isCanForward=FLASE;
  }
 /**
  * setCount方法
  * 
  * @param count int 
  * @return void
  */
  public void setCount(int count){
  	this.count=count;
  }
  
 /**
  * getCount方法
  * 
  * @return int
  */
  public int getCount(){
  	return this.count;
  }
  
 /**
  * setPages方法
  * 
  * @param pages int 
  * @return void
  */
  public void setPages(int pages){
  	this.pages=pages;
  }
  
 /**
  * getPages方法
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
  * setCurPage方法
  * 
  * @param curPage int
  * @return void
  */
  public void setCurPage(int curPage){
  	this.curPage=curPage;
  }
  
 /**
  * getCurPage方法
  * 
  * @return int
  */
  public int getCurPage(){
  	return this.curPage;
  }
  
 /**
  * setRows方法 
  * 
  * @param rows int 
  * @return void
  */
  public void setRows(int rows){
  	this.rows=rows;
  }
  
 /**
  * getRows方法
  * 
  * @return int
  */
  public int getRows(){
  	return this.rows;
  }
  
 /**
  * setTerm方法
  * 
  * @param term String
  * @return void
  */
  public void setTerm(String term){
  	this.term=term;
  }
  
 /**
  * getTerm方法
  * 
  * @return String
  */
  public String getTerm(){
  	return this.term;
  }
  
 /**
  * getIsCanForward方法
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
  * getIsCanBack方法
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
  * getFirstPageUrl方法 
  * 
  * @return String
  */
  public String getFirstPageUrl(){
    return term + (term.equals("")?"":"&") + "curPage=1";	
  }
  
 /**
  * getPrevPageUrl方法
  * 
  * @return String
  */
  public String getPrevPageUrl(){
  	return term + (term.equals("")?"":"&") + "curPage=" + (curPage-1); 
  }
  
 /**
  * getNextPageUrl方法
  * 
  * @return String
  */
  public String getNextPageUrl(){
  	return term + (term.equals("")?"":"&") + "curPage=" + (curPage+1);
  }
  
 /**
  * getLastPageUrl方法
  * 
  * @return String
  */
  public String getLastPageUrl(){
  	return term + (term.equals("")?"":"&") + "curPage=" + getPages();
  }
  
  /**
   * 设置首页码
   * 
   * @param firstPage int 首页码
   * @return void
   */
   public void setFirstPage(int firstPage){
   	this.firstPage=firstPage;
   }
   
  /**
   * 获取首页码
   *
   * @return int
   */
   public int getFirstPage(){
     return this.firstPage;	
   }
   
  /**
   * 设置上一页码
   *
   * @param previousPage 上一页码
   * @return void
   */
   public void setPreviousPage(int previousPage){
   	this.previousPage=previousPage;
   }
   
  /**
   * 获取上一页码
   *
   * @return int
   */
   public int getPreviousPage(){
   	return this.previousPage>0?this.previousPage:this.curPage-1;
   }
   
  /**
   * 设置下一页码
   *
   * @param nextPage 下一页码
   * @return void
   */
   public void setNextPage(int nextPage){
   	this.nextPage=nextPage;
   }
    
  /**
   * 获取下一页码
   *
   * @return int
   */
   public int getNextPage(){
   	return this.nextPage>0?this.nextPage:this.curPage+1;
   }
   
  /**
   * 设置尾页码
   *
   * @param previousPage 尾页码
   * @return void
   */
   public void setLastPage(int lastPage){
   	this.lastPage=lastPage;
   }
    
  /**
   * 获取尾码
   *
   * @return int
   */
   public int getLastPage(){
   	return this.lastPage>0?this.lastPage:getPages();
   }
    
}
