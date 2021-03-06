package com.fitech.institution.util;

import com.fitech.institution.form.AfTemplateColDefineForm;
import com.fitech.institution.po.AfTemplateColDefine;
import com.fitech.institution.po.AfTemplateColDefineId;

/**
 * This is a utilitiy class to handle value object conversion between the
 * backend persistence layer and the frontend action layer. It has a set of
 * methods to handle conversions.
 * 
 * @author <strong>Generated by Middlegen.</strong>
 */
public class TranslatorUtil {
	public static AfTemplateColDefine copyPersistenceToVo(
			AfTemplateColDefineForm form ,AfTemplateColDefine po) {
		AfTemplateColDefineId id = new AfTemplateColDefineId();
		id.setTemplateId(form.getTemplateId());
		id.setCol(form.getCol());
		po.setColName(form.getColName());
		po.setId(id);
		//BeanUtils.copyProperties(form, po);
		return po;
	}
}
