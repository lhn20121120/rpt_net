
function printNavi(a){
  var returnValue="";
  returnValue+=tab_start;
  for(i=0;i<a;i++){
    tab_1="<td valign=\"bottom\" class=\"tabOff\"><a href=\""+herf[i]+"\" target=\""+target[i]+"\"><img src=\""+tab_pic_off[i]+"\" border=\"0\" alt=\""+alt[i]+"\"></a></td>"+tab_off;
    returnValue+=tab_1;
  }
  tab_on=tab_on_l+"<td valign=\"bottom\" class=\"tabOn\"><a href=\""+herf[i]+"\" target=\""+target[i]+"\"><img src=\""+tab_pic_on[a]+"\" border=\"0\" alt=\""+alt[i]+"\"></a></td>"+tab_on_r;
  returnValue+=tab_on;
  for(i=a+1;i<tab_pic_on.length;i++){
    tab_2="<td valign=\"bottom\" class=\"tabOff\"><a href=\""+herf[i]+"\" target=\""+target[i]+"\"><img src=\""+tab_pic_off[i]+"\" border=\"0\" alt=\""+alt[i]+"\"></a></td>"+tab_off;
    returnValue=returnValue+tab_2;
  }
  returnValue+=tab_end;
  return returnValue;
}

function findObj(n, d) {
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function rollIn(object,style){
  object.className=style;
}

function rollOut(object,style){
  object.className=style;
}

function setNaviBar(a){
  objName="naviBar?navi";
  index=window.parent.navi.document.naviIndex.ids;
  if(index.value!=a){
    newText=printNavi(a);
      window.parent.document.title=customerName;
      if ((obj=findObj(objName))!=null) with (obj)
        if (document.layers) {document.write(unescape(newText)); document.close();}
        else innerHTML = unescape(newText);
    index.value=a;
  }
}

function setCos(a){
  a=customerName;
}

function Dconfirm(type){
  var confirm1="在您点下确认按钮后,接下来的动作将不可撤销,您确定吗?";
  var confirm2="您确定执行该操作么？";
  var confirmString="";
  switch(type){
    case "1":confirmString=confirm1;
    break;
    case "2":confirmString=confirm2;
    break;
  }
  if(confirm(confirmString))
    return true;
  else
    return false;
}

function checkForm(formindex,formobj){
  do{
    len=formindex.indexOf("*")
    tmpindex=formindex.substring(0,len);
    if(tmpindex!=""){
      if(formobj.elements[tmpindex].value==""){
        alert("请填写完整");
        formobj.elements[tmpindex].focus();
        return false;
      }
    }
    formindex=formindex.substring(len+1,formindex.length);
  }while(len!=-1);
  formobj.submit();
}

function checkForm2(formindex,formobj,submit){
  do{
    len=formindex.indexOf("*")
    tmpindex=formindex.substring(0,len);
    
    if(tmpindex!=""){
      for(i=0;i<formobj.elements.length;i++){
        if(formobj.elements[i].name==tmpindex&&formobj.elements[i].value==""){
          alert("请填写完整");
          formobj.elements[i].focus();
          return false;
        }
      }
    }
    formindex=formindex.substring(len+1,formindex.length);
  }while(len!=-1);
  if(submit=="1"){
    if(Dconfirm("1"))
      formobj.submit();
    else
      return false;
  }
  else
    return false;
}
