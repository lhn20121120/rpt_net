
/** 姬怀宝
	 * This is a delegate class to handle interaction with the backend persistence layer of hibernate. 
	 * It has a set of methods to handle persistence for MRepRange data (i.e. 
	 * com.gather.struts.forms.MRepRangeForm objects).
	 * 
	 * @author <strong>Generated by Middlegen.</strong>
	 */

package com.gather.db.helper;

import javax.servlet.http.HttpServletRequest;

public class PagerHelper {

  public static Pager getPager(HttpServletRequest httpServletRequest,
                               int totalRows) {

    //定义pager对象，用于传到页面
    Pager pager = new Pager(totalRows);

    //从Request对象中获取当前页号
    String currentPage = httpServletRequest.getParameter("currentPage");

    //如果当前页号为空，表示为首次查询该页
    //如果不为空，则刷新pager对象，输入当前页号等信息
    if (currentPage != null) {
      pager.refresh(Integer.parseInt(currentPage));
    }

    //获取当前执行的方法，首页，前一页，后一页，尾页。
    String pagerMethod = httpServletRequest.getParameter("pageMethod");

    if (pagerMethod != null) {
      if (pagerMethod.equals("first")) {
        pager.first();
      } else if (pagerMethod.equals("previous")) {
        pager.previous();
      } else if (pagerMethod.equals("next")) {
        pager.next();
      } else if (pagerMethod.equals("last")) {
        pager.last();
      }
    }
    return pager;
  }
}