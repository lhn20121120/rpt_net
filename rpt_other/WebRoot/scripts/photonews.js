function topimg(simg)
{
var strimg=simg;
var strimg1="";
var strimg2="";
var strimg3="";
var allstrimg="";
 if (strimg.length>16)
 {
 strimg1=strimg.substring(0, 16);
 allstrimg=strimg1 + "...";
 }
  if (strimg.length<=16)
 {
 allstrimg=strimg;
 }
return allstrimg;
}
var focus_width=380;
var focus_height=227;
var text_height=18;
var links="";
var pics="";
var texts="";
	var imgUrl=new Array();
	var imgLink=new Array();
	var imgTz=new Array();
	var adNum=0;
	var kk = 0;
	var swf_height = focus_height+text_height;
imgUrl[kk]  = 'images/ex_photo.jpg';
	imgLink[kk] = '#';
	imgTz[kk] =  'TEST';
	kk++;

imgUrl[kk]  = '../images/ex_photo.jpg';
	imgLink[kk] = '#';
	imgTz[kk] =  'TEST';
	kk++;

imgUrl[kk]  = '../images/ex_photo.jpg';
	imgLink[kk] = '#';
	imgTz[kk] =  'TEST';
	kk++;

imgUrl[kk]  = '../images/ex_photo.jpg';
	imgLink[kk] = '#';
	imgTz[kk] =  'TEST';
	kk++;

imgUrl[kk]  = '../images/ex_photo.jpg';
	imgLink[kk] = '#';
	imgTz[kk] =  'TEST';
	kk++;
var imgNum = imgUrl.length>6?6:imgUrl.length;
	for (i=0;i<imgNum;i++) {
		if(i==0){
			pics += imgUrl[i];
			links += imgLink[i];
			texts += imgTz[i];
		}else{
			pics += "|" + imgUrl[i];
			links += "|" + imgLink[i];
			texts += "|" + imgTz[i];
		}
	}

document.write('<object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,0,0" width="'+ focus_width +'" height="'+ swf_height +'">');
document.write('<param name="allowScriptAccess" value="sameDomain"><param name="movie" value="../flash/photonews.swf"><param name="quality" value="high"><param name="bgcolor" value="#F0EFEF">');
document.write('<param name="menu" value="false"><param name=wmode value="opaque">');
document.write('<param name="FlashVars" value="pics='+pics+'&links='+links+'&texts='+texts+'&borderwidth='+focus_width+'&borderheight='+focus_height+'&textheight='+text_height+'">');
document.write('<embed src="../flash/photonews.swf" wmode="opaque" FlashVars="pics='+pics+'&links='+links+'&texts='+texts+'&borderwidth='+focus_width+'&borderheight='+focus_height+'&textheight='+text_height+'" menu="false" bgcolor="#F0EFEF" quality="high" width="'+ focus_width +'" height="'+ focus_height +'" allowScriptAccess="sameDomain" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" />');
document.write('</object>');