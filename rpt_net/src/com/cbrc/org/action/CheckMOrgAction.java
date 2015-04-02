package com.cbrc.org.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.org.adapter.StrutsMOrgDelegate;
import com.cbrc.org.form.MOrgForm;
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;

/**
 * ���ݻ�������id����ʾ�û������͵Ļ�����Ϣ
 *
 * @author ����
 */
public final class CheckMOrgAction extends Action {
	private static FitechException log = new FitechException(ViewMOrgAction.class); 
	
	   /**
	    * @exception IOException  IO�쳣
	    * @exception ServletException  ServletException�쳣
	    */
   
    
    public ActionForward execute(
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response
   )
      throws IOException, ServletException {

        FitechMessages messages = new FitechMessages();
        MessageResources resources = getResources(request);
            
        // �Ѳ�ѯ�����Ž�form
        MOrgForm mOrgForm = (MOrgForm) form;
        RequestUtils.populate(mOrgForm, request);
        List listFromDB  = null;
        List myList=null;
        int recordCount = 0; // ��¼����
        int offset = 0; // ƫ����
        int limit = 0; // ÿҳ��ʾ�ļ�¼��
    
        //
        ApartPage aPage = new ApartPage();
        String strCurPage =null;
        if(request.getAttribute("curPage")!=null)
            strCurPage = (String)request.getAttribute("curPage");
        
        if (strCurPage != null) 
        {
            if (!strCurPage.equals(""))
                aPage.setCurPage(new Integer(strCurPage).intValue());
        } 
        else
            aPage.setCurPage(1);
        // ����ƫ����
        offset =(aPage.getCurPage()-1)*Config.PER_PAGE_ROWS;
        limit =  Config.PER_PAGE_ROWS;
        
        try { 
        	if(mOrgForm!=null){
            // ȡ�ü�¼����
            recordCount = StrutsMOrgDelegate.getRecordCount(mOrgForm);
            // �����ݿ��ѯ��ҳ�ļ�¼
           
            if (recordCount > 0)
                listFromDB = StrutsMOrgDelegate.select(mOrgForm, offset, limit);
        	}
 
        } catch (Exception e) {
            log.printStackTrace(e);
            messages.add(resources.getMessage("orgcls.select.fail"));
        }
        // �Ƴ�request��session��Χ�ڵ�����
        FitechUtil.removeAttribute(mapping, request);
        // ��ApartPage��������request��Χ��
        aPage.setTerm(this.getTerm(mOrgForm));
        aPage.setCount(recordCount);
        request.setAttribute(Config.APART_PAGE_OBJECT, aPage);
     
        request.setAttribute("ChildRepId", mOrgForm.getChildRepId());
        request.setAttribute("VersionId", mOrgForm.getVersionId());
        request.setAttribute("orgClsName", mOrgForm.getOrgClsName());
        request.setAttribute("orgClsId", mOrgForm.getOrgClsId());
        request.setAttribute("orgName", mOrgForm.getOrgName());
       /* if(listFromDB!=null && listFromDB.size()>0){
        	myList=new ArrayList();
        for(int i=0;i<listFromDB.size();i++){
        	OrgDetail orgDetailForm=new OrgDetail();
        	orgDetailForm.setOrgClsName(mOrgForm.getOrgClsName());
        	orgDetailForm.setOrgName(((MOrgForm)listFromDB.get(i)).getOrgName());
        	orgDetailForm.setOrgId(((MOrgForm)listFromDB.get(i)).getOrgId());
        	orgDetailForm.setChildRepId(request.getAttribute("ChildRepId").toString());
        	orgDetailForm.setVersionId(((MOrgForm)listFromDB.get(i)).getVersionId());
        	orgDetailForm.setOrgClsId(((MOrgForm)listFromDB.get(i)).getOrgClsId());
        	myList.add(orgDetailForm);
        	}
        }*/
 
        if (messages.getMessages() != null && messages.getMessages().size() > 0)
            request.setAttribute(Config.MESSAGES, messages);
        /*
         * ���StrutsMOrgDelegate���з��ص�reslist����Ϊ�ղ��Ҷ���Ĵ�С����0��
         *  �򷵻�һ������reslist���ϵ�request����
         */   
        if (listFromDB != null && listFromDB.size() > 0)
            request.setAttribute(Config.RECORDS, listFromDB);
        // ���ص�ҳ��view
        return mapping.findForward("check");
    }
    
    /**
     * �����ѯ����
     * @param mOrgForm ������ѯ����
     * @return  ���ݲ�ѯ���������ɵ�url
     */
    private String getTerm(MOrgForm mOrgForm) {
        String term = "";

        String orgName = mOrgForm.getOrgName();
        String orgClsId = mOrgForm.getOrgClsId();
        String orgClsName = mOrgForm.getOrgClsName();
        String versionId=mOrgForm.getVersionId();
        String childRepId=mOrgForm.getChildRepId();
        String orgId=mOrgForm.getOrgId();
        
        if (orgName != null && !orgName.equals("")) {
            term += (term.indexOf("?")>=0 ? "&" : "?");
            term += "orgName=" + orgName;
        }

        if (orgClsId != null && !orgClsId.equals("")) {
            term += (term.indexOf("?")>=0 ? "&" : "?");
            term += "orgClsId=" + orgClsId;
        }
        if (orgClsName != null && !orgClsName.equals("")) {
            term += (term.indexOf("?")>=0 ? "&" : "?");
            term += "orgClsName=" + orgClsName;
        }
        if (versionId != null && !versionId.equals("")) {
            term += (term.indexOf("?")>=0 ? "&" : "?");
            term += "versionId=" + versionId;
        }
        if (childRepId != null && !childRepId.equals("")) {
            term += (term.indexOf("?")>=0 ? "&" : "?");
            term += "childRepId=" + childRepId;
        }
        if (orgId != null && !orgId.equals("")) {
            term += (term.indexOf("?")>=0 ? "&" : "?");
            term += "orgId=" + orgId;
        }
       
	    if(term.indexOf("?")>=0)
	           term = term.substring(term.indexOf("?")+1);
        //// System.out.println("term" + term);
        return term;
    }
}