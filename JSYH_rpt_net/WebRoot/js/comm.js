 /**
   * 打开模式窗口
   *
   * @param toUrl String 显示的页面URL
   * @return void
   */
  function openDialog(toUrl){
  	var fealures="dialogWidth:400px;dialogHeight:200px;dialogLeft:200px;" + 
  		"dialogTop:150px;center:yes;help:yes;resizable:no;status:no";
  
	 	var returnValues=showModalDialog(toUrl,window,fealures); 
	}
	
	/**
	 * 转到指定的页面
	 *
	 * @param url String 转到指定页面的URL
	 * @return void
	 */
	 function _goto(_url){
	 		if(_url=="") return;
	 		window.location=_url;
	 }
	 
	/**
	 * 报送频度和频送时间类
	 *
	 *@param dataRangeId 数据范围ID
	 *@param repFreqId 报送频度ID
	 *@param normalTime 正常时间
	 *@param delayTime 延迟时间
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
	 * 异常变化标准
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
	 * 机构类别类 
	 *
	 * @param orgClsId 
	 * @param isAllSelected 当为1时，不是全选；当为0时，为全选
	 * @return this
	 */
	 function OrgCls(orgClsId,isAllSelected){
	 	 this.orgClsId=orgClsId;
	 	 this.isAllSelected=isAllSelected;
	 	 
	 	 return this;
	 }