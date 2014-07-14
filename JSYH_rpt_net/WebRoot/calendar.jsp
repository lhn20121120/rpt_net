<%
  String path="";
  if(request.getParameter("path")!=null){
    path=(String)request.getParameter("path");
  }
%>
<link rel="alternate stylesheet" type="text/css" media="all" href="<%=path%>calendar/calendar-blue.css" title="winter">
<link rel="alternate stylesheet" type="text/css" media="all" href="<%=path%>calendar/calendar-brown.css" title="summer">
<link rel="alternate stylesheet" type="text/css" media="all" href="<%=path%>calendar/calendar-green.css" title="green">
<link rel="stylesheet" type="text/css" media="all" href="<%=path%>calendar/calendar-win2k-1.css" title="win2k-1">
<link rel="alternate stylesheet" type="text/css" media="all" href="<%=path%>calendar/calendar-win2k-2.css" title="win2k-2">
<link rel="alternate stylesheet" type="text/css" media="all" href="<%=path%>calendar/calendar-win2k-cold-1.css" title="win2k-cold-1">
<link rel="alternate stylesheet" type="text/css" media="all" href="<%=path%>calendar/calendar-win2k-cold-2.css" title="win2k-cold-2">
<!-- import the calendar script -->
<script type="text/javascript" src="<%=path%>calendar/calendar.js"></script>
<!-- import the language module -->
<script type="text/javascript" src="<%=path%>calendar/calendar-cn.js"></script>
<script language="javascript" src="<%=path%>calendar/calendar-func.js"></script>