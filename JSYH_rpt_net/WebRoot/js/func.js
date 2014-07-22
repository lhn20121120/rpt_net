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