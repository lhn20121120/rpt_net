package com.fitech.net.template.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.net.adapter.StrutsFormulaDelegate;
import com.fitech.net.form.EtlIndexForm;

/**
 * 参数表更新操作的Action对象
 *
 */
public final class UpdateEtlIndexFormulaAction extends Action {
  private static FitechException log = new FitechException(UpdateEtlIndexFormulaAction.class);

  /**
   * @param mapping
   *            Action mapping.
   * @param form
   *            Action form.
   * @param request
   *            HTTP request.
   * @param response
   *            HTTP response.
   * @exception IOException是否有输入/输出的异常
   * @exception ServletException是否有servlet的异常占用
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
                               HttpServletResponse response) throws IOException, ServletException {
    MessageResources resources    = this.getResources(request);
    FitechMessages   messages     = new FitechMessages();
    EtlIndexForm     etlIndexForm = (EtlIndexForm)form;
    // RequestUtils.populate(etlIndexForm, request);
    String           indexName    = request.getParameter("indexName");
    String           formual      = request.getParameter("formual");
    String           desc         = request.getParameter("desc");

    if (indexName != null && !indexName.equals("")) {
      etlIndexForm.setIndexName(indexName);
    }

    if (formual != null && !formual.equals("")) {
      etlIndexForm.setFormual(formual);
    }

    if (desc != null && !desc.equals("")) {
      etlIndexForm.setDesc(desc);
    }

    String curPage = "";

    if (request.getParameter("curPage") != null) {
      curPage = (String)request.getParameter("curPage");
    }

    boolean updateResult = false;

    try {
      if (etlIndexForm != null) {
        updateResult = StrutsFormulaDelegate.updateEtlIndex(etlIndexForm);

        if (updateResult == true) {
          // 更新成功
          messages.add("更新指标公式成功!");
        } else {
          messages.add("更新指标公式失败!");
        }
      }
    } catch (Exception e) {
      updateResult = false;

      messages.add("更新指标公式失败!");
      log.printStackTrace(e);
    }

    FitechUtil.removeAttribute(mapping, request);

    // 判断有无提示信息，如有将其存储在Request对象中，返回请求
    if (messages != null && messages.getSize() > 0) {
      request.setAttribute(Config.MESSAGES, messages);
    }

    String path = "";

    if (updateResult == true) {
      form = null;
      path = mapping.findForward("update").getPath() + "?curPage=" + curPage + "&indexName=" + etlIndexForm.getIndexName();
    } else {
      path = mapping.getInputForward().getPath();
    }

    path = path == null ? mapping.findForward(Config.FORWARD_SYS_ERROR_PAGE).getPath() : path;

    return new ActionForward(path);
  }
} 