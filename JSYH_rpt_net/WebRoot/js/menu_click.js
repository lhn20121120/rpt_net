				
			
			/**
			 * ģ����� 
			 */
			 var MBGL="MBGL";
			/**
			 * ���ݹ���
			 */
			 var SJGL="SJGL";
			/**
			 * �����趨
			 */
			 var CSSD="CSSD";
			/**
			 * ϵͳ����
			 */
			 var XTGL="XTGL";
			 
			/**
			 * �������ͼƬ�󴥷����¼�
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
			 	 	 _status="ģ�����";
			 	 }else if(_do==SJGL){
			 	 	 _url_navi="sjcx/navi.chaxun.htm";
			 	 	 _url_main="sjcx/main.chaxun.htm";
			 	 	 _status="���ݹ���";
			 	 }else if(_do==CSSD){
			 	 	 _url_navi="cldy/navi.celue.htm";
			 	 	 _url_main="cldy/main.celue.htm";
			 	 	 _status="�����趨";
			 	 }else{
			 	 	 _url_navi="xtgl/navi.xitong.htm";
			 	 	 _url_main="xtgl/main.xitong.htm";
			 	 	 _status="ϵͳ����";
			 	 }
			 	 
			 	 if(_url_navi=="" || _url_main=="") return;
			 	 
			 	 window.status=_status;
			 	 window.parent.parent.naviFrame.location.assign(_url_navi);
			 	 window.parent.parent.mainFrame.location.assign(_url_main);
			 }