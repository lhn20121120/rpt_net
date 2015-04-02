/**
 * ??????????????????????????
 *
 * @param fileName String
 * @return String
 */
 function getExt(fileName){
	var pos=fileName.lastIndexOf(".");
	var ext=fileName.substring(pos+1);
	return ext;
 }
 
/**
 * ??????????????????????
 *
 * @param InputValue ????????
 * @return boolean ????????????true;??????????false
 */ 
function CheckNum(InputValue)
{
    var reg=/^([0-9])+$/
	  var isValid
		
		isValid=reg.exec(InputValue)
		if (!isValid)
		{
			//alert("???? "+InputValue+" ??????")
			return false
		}
		return true
} 

/**
 * ??????????????????????(??????????????)
 *
 * @param InputValue ????????
 * @return boolean ????????????true;??????????false
 */ 
function _checkNum(InputValue)
{
    var reg=/^([.,0-9])+$/
 //	  var reg=/^(-?\d+)(\.\d+)?$/
	  var isValid
		
		isValid=reg.exec(InputValue)
		if (!isValid)
		{
			//alert("???? "+InputValue+" ??????")
			return false
		}
		return true
} 

/**
 * ??????????????????
 *
 * @param inputValue String
 * @return String
 */
 function _trim(inputValue){
 	 if(inputValue==null) return "";
 	 
 	 return inputValue.replace(" ","");
 }
 
 /**
  * ????????????????????,??????????????true;??????????false
  *
  * @param inputValue String ??????
  * @return boolean
  */
  function isEmpty(inputValue){
  	if(inputValue==null) return true;
  	
  	if(_trim(inputValue)=="") 
  		return true;
  	else
  		return false;
  }


function Trim(InputValue)
{
	return InputValue.replace(" ","");
}
 function   getObjectLeft(e)   //???????????????
  {   
      var   l=e.offsetLeft;   
      while(e=e.offsetParent)   
      	l   +=   e.offsetLeft;   
      return   l;   
  }   
  function   getObjectTop(e)   //?????????????
  {   
      var   t=e.offsetTop;   
      while(e=e.offsetParent)   
      	t   +=   e.offsetTop;   
      return   t;   
  }
   function rdl_change(obj,du){
            obj.filters[0].MaskColor=0xFFFFFF;
            obj.filters[0].opacity=du;
            
           }
		   /**
		    * 去掉空格中的字符串
			* srcStr:源字符串
			* repStr:被替换的字符串
			* repedStr:替换后的字符串
			*/
		   function replaceAllStr(srcStr,repStr,repedStr){
             var result = ""  + srcStr;
             while(result.indexOf(repStr)!=-1)
               result = result.replace(repStr,repedStr);
             return result;
           }
           /**
		    * 数据表格提示入口主方法
			*/
           function openTb(obj) {       
             intime = window.setInterval(function(){
               upMove(obj);
             }, 10);
           }
           function upMove(obj){
	//		 obj.style.visibility='visble';
             var wh=document.body.offsetHeight + document.body.scrollTop;
	         var ww=document.body.offsetWidth;
             var objHeight = obj.offsetHeight;
             var objWidth = obj.offsetWidth;
             var objTop = obj.offsetTop;
             var stop = getObjectTop(document.getElementsByTagName('img')[0]) + 70;
             if(objTop>stop)   
              obj.style.top = objTop-10; 
             else
              clearInterval(intime);             
           }
           function resetClue(obj){
            var wh=document.body.clientHeight + document.body.scrollTop;
	        var ww=document.body.clientWidth;
            var objWidth = obj.clientWidth;        
            obj.style.top = wh ;
            obj.style.left = ww/2-objWidth/2;
           }
		   /**
		    * 关闭数据表格，回到图片界面
			*/
           function closeTb(imageDiv,dataTb){
            dataTb.style.top=-10000;
   //			dataTb.style.visibility='hidden';
            rdl_change(imageDiv,1.0);
           }
		   /**
		    * 显示数据表格区
			*/
		    function showDataTb(imageDiv,dataTb){
             rdl_change(imageDiv,0.3);
             resetClue(dataTb);
             openTb(dataTb);
           }
		   /**
		    * 检查年份输入框
			*/
		   function checkYear(obj){
			 var tmp = replaceAllStr(obj.value,' ','');
			 if(tmp=='' || !CheckNum(tmp) || parseInt(tmp)<1990)
		       return false;
			 return true;
		   }
		   /**
		    *  检查月份输入框
			*/
		   function checkMonth(obj){
             var tmp = replaceAllStr(obj.value,' ','');
			 if(tmp=='' || !CheckNum(tmp) || parseInt(tmp)<1 || parseInt(tmp)>12)
		       return false;
			 return true;
		   }
           /**
		    * 根据输入的select列表对象选中所有的列表
			*/
		   function allCheckBySelect(obj){
              var ops = obj.options;
			  var i;
			  for(i=0;i<ops.length;i++){
                ops[i].selected=true;
			  }
		   }