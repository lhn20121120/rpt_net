$(document).ready(function(){
	$("#orgcls").change(function(){
		//showProcessBar();
		var id=$("#orgcls").find("option:selected").val();
		
		$.get("bbsh_getOrgList.action?report.orgclsid="+id,function(data){
			$("#org").empty();
			if(data!=null){			
				$("#org").html(data);
			}
		});
	});
});