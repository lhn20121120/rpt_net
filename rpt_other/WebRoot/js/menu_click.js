				
			
			/**
			 * 模板管理 
			 */
			 var MBGL="MBGL";
			/**
			 * 数据管理
			 */
			 var SJGL="SJGL";
			/**
			 * 参数设定
			 */
			 var CSSD="CSSD";
			/**
			 * 系统管理
			 */
			 var XTGL="XTGL";
			 
			/**
			 * 点击功能图片后触发的事件
			 *
			 */
			 function _click(_do){
			 	 if(_do=="") return ;
			 	 
			 	 var _url_navi="";
			 	 var _url_main="";
			 	 var _status="";
			 	 if(_do==MBGL){
			 	 	 _url_navi="mbgl/navi.moban.htm";
			 	 	 _url_main="mbgl/main.moban.htm";
			 	 	 _status="模板管理";
			 	 }else if(_do==SJGL){
			 	 	 _url_navi="sjcx/navi.chaxun.htm";
			 	 	 _url_main="sjcx/main.chaxun.htm";
			 	 	 _status="数据管理";
			 	 }else if(_do==CSSD){
			 	 	 _url_navi="cldy/navi.celue.htm";
			 	 	 _url_main="cldy/main.celue.htm";
			 	 	 _status="参数设定";
			 	 }else{
			 	 	 _url_navi="xtgl/navi.xitong.htm";
			 	 	 _url_main="xtgl/main.xitong.htm";
			 	 	 _status="系统管理";
			 	 }
			 	 
			 	 if(_url_navi=="" || _url_main=="") return;
			 	 
			 	 window.status=_status;
			 	 window.parent.parent.naviFrame.location.assign(_url_navi);
			 	 window.parent.parent.mainFrame.location.assign(_url_main);
			 }