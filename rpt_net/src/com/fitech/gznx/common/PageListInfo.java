package com.fitech.gznx.common;
import java.util.List;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Created by IntelliJ IDEA.
 * User: Rui.Feng
 * Date: 2008-5-26
 * Time: 9:45:35
 * To change this template use File | Settings | File Templates.
 */
public class PageListInfo {
    private List list;
    private int pageSize;
    private int pageCount;
    private int rowCount;
    private int curPage;
    private int curPageRowCount;

    public PageListInfo(int pageSize , int curPage) {
        this.pageSize = pageSize;
        this.curPage = curPage;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public long getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
        this.pageCount = rowCount%pageSize==0?rowCount/pageSize: rowCount/pageSize+1;
        if(curPage<pageCount){
            this.curPageRowCount =  pageSize;
        }else{
            this.curPageRowCount = (int)rowCount%pageSize;
        }
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getCurPage() {
        return curPage;
    }

    public int getPageCount() {
        return pageCount;
    }

    public int getCurPageRowCount() {
        return curPageRowCount;
    }

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	public void setCurPageRowCount(int curPageRowCount) {
		this.curPageRowCount = curPageRowCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this).append("pageSize", this.pageSize)
				.append("rowCount", this.rowCount).append("curPageRowCount",
						this.curPageRowCount).append(
						"pageCount", this.pageCount).append("curPage",
						this.curPage).toString();
	}
    
    


}
