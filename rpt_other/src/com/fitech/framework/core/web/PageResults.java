package com.fitech.framework.core.web;

import java.util.ArrayList;
import java.util.List;

public class PageResults<T> {
    private List<T> results=new ArrayList<T>();
    private int totalCount=0;
    private int pageSize=15;
    private int pageCount=0;
    private int currentPage=1;
    private String orderBy;
    private boolean isASC;
    
    public boolean isASC() {
		return isASC;
	}

	public void setASC(boolean isASC) {
		this.isASC = isASC;
	}

	/** Creates a new instance of PageResults */
    public PageResults() {
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageCount() {
        
        return (this.totalCount+this.pageSize-1)/this.pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
    	if(currentPage<=0) currentPage=1;
        this.currentPage = currentPage;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	
	public int getResultsFrom(){
		return (currentPage-1)*pageSize;
	}
	
	public int getResultsEnd(){
		return pageSize*currentPage;
		
	}
    
    public void resetPageNo() {

        if (totalCount < pageSize * (currentPage-1)) {
            currentPage = 1;
        }

    }
}
