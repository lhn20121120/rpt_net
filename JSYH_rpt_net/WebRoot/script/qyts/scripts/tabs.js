  function showDiv(obj,num,len)
{
 for(var id = 1;id<=len;id++)
 {
  var ss=obj+id;
  var snav =obj+"nav"+id;
  var snava =snav+"a";
  if(id==num){
	try{document.getElementById(snava).className="aa"}catch(e){};
	try{document.getElementById(ss).style.display="block"}catch(e){};
	try{document.getElementById(snav).className="active"}catch(e){};
  }else{
	try{document.getElementById(snava).className="a"}catch(e){};
	try{document.getElementById(ss).style.display="none"}catch(e){};
	try{document.getElementById(snav).className=""}catch(e){};
  }
 }  
}