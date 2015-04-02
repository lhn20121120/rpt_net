
function selDatasource(){
   document.all.FramerControl1.Close();
   
   FileForm.submit();  
}

function objSave(processAction, fileName, hrefUrl){ 
   try
   {
	   var postFile = false;
	   var postResult = false;
	   document.all.FramerControl1.HttpInit();
	  	document.all.FramerControl1.HttpAddPostCurrFile("reportFile", fileName);
	   postResult = document.all.FramerControl1.HttpPost(processAction); 
	   document.all.FramerControl1.Close();   
	      
	   window.location.href=hrefUrl;
   	}
	catch(e)
	{

	}
}

