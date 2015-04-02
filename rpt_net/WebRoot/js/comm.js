 /**
   * ��ģʽ����
   *
   * @param toUrl String ��ʾ��ҳ��URL
   * @return void
   */
  function openDialog(toUrl){
  	var fealures="dialogWidth:400px;dialogHeight:200px;dialogLeft:200px;" + 
  		"dialogTop:150px;center:yes;help:yes;resizable:no;status:no";
  
	 	var returnValues=showModalDialog(toUrl,window,fealures); 
	}
	
	/**
	 * ת��ָ����ҳ��
	 *
	 * @param url String ת��ָ��ҳ���URL
	 * @return void
	 */
	 function _goto(_url){
	 		if(_url=="") return;
	 		window.location=_url;
	 }
	 
	/**
	 * ����Ƶ�Ⱥ�Ƶ��ʱ����
	 *
	 *@param dataRangeId ���ݷ�ΧID
	 *@param repFreqId ����Ƶ��ID
	 *@param normalTime ����ʱ��
	 *@param delayTime �ӳ�ʱ��
	 *@return this
	 */
	 function MActuRep(dataRangeId,repFreqId,normalTime,delayTime){
	 	 this.dataRangeId=dataRangeId;
	 	 this.repFreqId=repFreqId;
	 	 this.normalTime=normalTime;
	 	 this.delayTime=delayTime;
	 	 return this;
	 	
	 }
	 
	/**
	 * �쳣�仯��׼
	 * 
	 * @param id 
	 * @param fieldName
	 * @param prevFallStandard
	 * @parma prevRiseStandard
	 * @param sameFallStandard
	 * @param sameRiseStandard
	 * @return this
	 */
	 function AbnormityChange(id,fieldName,prevFallStandard,prevRiseStandard,sameFallStandard,sameRiseStandard){
	 	 this.id=id;
	 	 this.fieldName=fieldName;
	 	 this.prevFallStandard=prevFallStandard;
		 this.prevRiseStandard=prevRiseStandard;
		 this.sameFallStandard=sameFallStandard;
		 this.sameRiseStandard=sameRiseStandard;
		 
		 return this;
	 }
	 
	/**
	 * ��������� 
	 *
	 * @param orgClsId 
	 * @param isAllSelected ��Ϊ1ʱ������ȫѡ����Ϊ0ʱ��Ϊȫѡ
	 * @return this
	 */
	 function OrgCls(orgClsId,isAllSelected){
	 	 this.orgClsId=orgClsId;
	 	 this.isAllSelected=isAllSelected;
	 	 
	 	 return this;
	 }