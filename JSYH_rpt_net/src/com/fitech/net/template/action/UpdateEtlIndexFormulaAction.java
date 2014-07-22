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
 * ���������²�����Action����
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
   * @exception IOException�Ƿ�������/������쳣
   * @exception ServletException�Ƿ���servlet���쳣ռ��
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
          // ���³ɹ�
          messages.add("����ָ�깫ʽ�ɹ�!");
        } else {
          messages.add("����ָ�깫ʽʧ��!");
        }
      }
    } catch (Exception e) {
      updateResult = false;

      messages.add("����ָ�깫ʽʧ��!");
      log.printStackTrace(e);
    }

    FitechUtil.removeAttribute(mapping, request);

    // �ж�������ʾ��Ϣ�����н���洢��Request�����У���������
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