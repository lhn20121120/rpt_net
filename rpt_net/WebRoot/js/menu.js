   <!--
  // These scripts were originally found on cooltype.com.
  // Modified 01/01/1999 by Tobias Ratschiller for linuxapps.com

  // Modified 7th June 2000 by Brian Birtles for Mozilla 5.0
  // compatibility for phpMyAdmin

  document.onmouseover = doDocumentOnMouseOver ;
  document.onmouseout = doDocumentOnMouseOut ;

  function doDocumentOnMouseOver() {
    var eSrc = window.event.srcElement ;
    if (eSrc.className == "item") {
      window.event.srcElement.className = "highlight";
    }
  }

  function doDocumentOnMouseOut() {
    var eSrc = window.event.srcElement ;
    if (eSrc.className == "highlight") {
      window.event.srcElement.className = "item";
    }
  }


var bV=parseInt(navigator.appVersion);
NS4=(document.layers) ? true : false;
IE4=((document.all)&&(bV>=4)) ? true : false;
DOM=(!document.layers && !document.all && bV>=4) ? true : false; // A hack to guess if the browser supports the DOM
capable = (NS4 || IE4 || DOM) ? true : false;

function expandIt(){return}
function expandAll(){return}
//-->

<!--
isExpanded = false;

function getIndex(el) {
  ind = null;
  for (i=0; i<document.layers.length; i++) {
    whichEl = document.layers[i];
    if (whichEl.id == el) {
      ind = i;
      break;
    }
  }
  return ind;
}

function arrange() {
  nextY = document.layers[firstInd].pageY + document.layers[firstInd].document.height;
  for (i=firstInd+1; i<document.layers.length; i++) {
    whichEl = document.layers[i];
    if (whichEl.visibility != "hide") {
      whichEl.pageY = nextY;
      nextY += whichEl.document.height;
    }
  }
}

function initIt(){
  if (NS4) {
    for (i=0; i<document.layers.length; i++) {
      whichEl = document.layers[i];
      if (whichEl.id.indexOf("Child") != -1) whichEl.visibility = "hide";
    }
    arrange();
  } else if(IE4) {
    tempColl = document.all.tags("DIV");
    for (i=0; i<tempColl.length; i++) {
      if (tempColl(i).className == "child") tempColl(i).style.display = "none";
    }
  } else if(DOM) {
    tempColl = document.getElementsByTagName("DIV");
    for (i=0; i<tempColl.length; i++) {
      if (tempColl(i).className == "child") tempColl(i).style.visibility = "hidden";
    }
  }
}

function expandIt(el) {
  if (!capable) return;
  if (IE4) {
    expandIE(el);
  } else if(NS4) {
    expandNS(el);
  } else if(DOM) {
    expandDOM(el);
  }
}

function expandIE(el) {
  whichEl = eval(el + "Child");

        // Modified Tobias Ratschiller 01-01-99:
        // event.srcElement obviously only works when clicking directly
        // on the image. Changed that to use the images's ID instead (so
        // you've to provide a valid ID!).

  //whichIm = event.srcElement;
        whichIm = eval(el+"Img");

  if (whichEl.style.display == "none") {
    whichEl.style.display = "block";
    whichIm.src = "images/minus.gif";
  }
  else {
    whichEl.style.display = "none";
    whichIm.src = "images/plus.gif";
  }
    window.event.cancelBubble = true ;
}

function expandNS(el) {
  whichEl = eval("document." + el + "Child");
  whichIm = eval("document." + el + "Parent.document.images['imEx']");
  if (whichEl.visibility == "hide") {
    whichEl.visibility = "show";
    whichIm.src = "images/minus.gif";
  }
  else {
    whichEl.visibility = "hide";
    whichIm.src = "images/plus.gif";
  }
  arrange();
}

function expandDOM(el) {

  whichEl = document.getElementById(el + "Child");
    whichIm = document.getElementById(el + "Img");

  if (whichEl.style.visibility != "visible") {
    whichEl.style.visibility = "visible";
    whichIm.src = "images/minus.gif";
  } else {
    whichEl.style.visibility = "hidden";
    whichIm.src = "images/plus.gif";
  }

}

function showAll() {
  for (i=firstInd; i<document.layers.length; i++) {
    whichEl = document.layers[i];
    whichEl.visibility = "show";
  }
}

function expandAll(isBot) {
  // Brian Birtles 7-Jun-00 : This fn might be unnecessary (for phpMyAdmin).
  // My changes are certainly untested.
  newSrc = (isExpanded) ? "images/plus.gif" : "images/minus.gif";

  if (NS4) {
        // TR-02-01-99: Don't need that
        // document.images["imEx"].src = newSrc;
    for (i=firstInd; i<document.layers.length; i++) {
      whichEl = document.layers[i];
      if (whichEl.id.indexOf("Parent") != -1) {
        whichEl.document.images["imEx"].src = newSrc;
      }
      if (whichEl.id.indexOf("Child") != -1) {
        whichEl.visibility = (isExpanded) ? "hide" : "show";
      }
    }

    arrange();
   if (isBot && isExpanded) scrollTo(0,document.layers[firstInd].pageY);
  } else if(IE4) {
    divColl = document.all.tags("DIV");
    for (i=0; i<divColl.length; i++) {
      if (divColl(i).className == "child") {
        divColl(i).style.display = (isExpanded) ? "none" : "block";
      }
    }
    imColl = document.images.item("imEx");
    for (i=0; i<imColl.length; i++) {
      imColl(i).src = newSrc;
    }
  } else if(DOM) {
    divColl = document.getElementsByTagName("DIV");
    for (i=0; i<divColl.length; i++) {
      if (divColl(i).className == "child") {
        divColl(i).style.visibility = (isExpanded) ? "hidden" : "visible";
      }
    }
    imColl = document.getElementsByName("imEx");
    for (i=0; i<imColl.length; i++) {
      imColl(i).src = newSrc;
    }
  }

  isExpanded = !isExpanded;
}

with (document) {
  if(DOM) {
    // Brian Birtles : This is not the ideal method of doing this
    // but under the 7th June '00 Mozilla build (and many before
    // it) Mozilla did not treat text between <style> tags as
    // style information unless it was written with the one call
    // to write().
    var lstyle = "<style type='text/css'>";
    lstyle += ".child {font-family: Verdana, Arial, Helvetica, sans-serif; color: #000000; text-decoration:none; visibility:hidden}";
    lstyle += ".parent {font-family: Verdana, Arial, Helvetica, sans-serif; color: #000000; text-decoration:none;}";
    lstyle += ".item { color: darkblue; text-decoration:none; font-size: 8pt;}";
    lstyle += ".highlight { color: red; font-size: 8pt;}";
    lstyle += ".heada { font: 12px/13px; Times}";
    lstyle += ".big { color: black; text-decoration:none; font-size: 12pt;}";
    lstyle += "DIV { color:black; }";
    lstyle += "</style>";
    write(lstyle);
  } else {
    write("<style type='text/css'>");
    if (NS4) {
            write(".parent {font-family: Verdana, Arial, Helvetica, sans-serif; color: #000000; text-decoration:none; position:absolute; visibility:hidden; color: black;}");
            write(".child {font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 8pt;color: #000000; position:absolute; visibility:hidden}");
            write(".item { color: darkblue; text-decoration:none;}");
            write(".regular {font-family: Arial,Helvetica,sans-serif; position:absolute; visibility:hidden}");
            write("DIV { color:black; }");
    } else if(IE4) {
            write(".child {font-family: Verdana, Arial, Helvetica, sans-serif; color: #000000; text-decoration:none; display:none}");
            write(".parent {font-family: Verdana, Arial, Helvetica, sans-serif; color: #000000; text-decoration:none;}");
            write(".item { color: darkblue; text-decoration:none; font-size: 8pt;}");
            write(".highlight { color: red; font-size: 8pt;}");
            write(".heada { font: 12px/13px; Times}");
            write(".big { color: black; text-decoration:none; font-size: 12pt;}");
            write("DIV { color:black; }");
    }
    write("</style>");
  }
}

onload = initIt;

//-->
