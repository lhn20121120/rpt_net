<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<meta name="keywords" content="JS代码,菜单导航,JS广告代码,JS特效代码" />
<meta name="description" content="" />
<link href="<%=path %>/css/index.css" rel="stylesheet" type="text/css" />
<link href="<%=path %>/css/common1.css" rel="stylesheet" type="text/css" />
<script>
		function targetA(obj,URL){
				//var allA = document.getElementsByName("urlA");
				//for(i=0;i<allA.length;i++)
				//	allA[i].className='otherA';
				//obj.target="naviFrame";
				obj.href=URL;
				//obj.className='targetA';
				obj.click();
				window.parent.plugFrame.location.href='plug.html';
				window.parent.mainFrame.location.href='welcome.html';
				window.parent.rightFrame.location.href='right.html';
				//alert(1);
		}
	</script>
<title></title>
<style type="text/css">
<!--
/*通用*/
body{font:12px Arial,Verdana,Tahoma,"宋体";margin-left:-10px}
*{padding:0px;margin:0px;}
* li{list-style:none;}
a{text-decoration:none;color:#20537A;}
a:hover{text-decoration:underline;}

.clearfix:after {
    content: "\0020";
    display: block;
    height: 0;
    clear: both;
}
.clearfix {
    _zoom: 1;
}
*+html .clearfix {
	overflow:auto;
}

p{
margin-bottom:15px}
-->
</style>
</head>

<body style="text-align:center" >
<div class="header">
    <div class="headtop">
    	<div class="logo"><img src="image/logo.png" alt="科融数据报送平台" /></div>
        
      <div class="cloud">
        	<span>&nbsp;</span>
            <p>&nbsp;<br />&nbsp;</p>
      </div>
       
     </div>
     <div class="menu">
        <div class="mleft"><img src="image/menuleft.gif" alt="" /></div>
        <ul class="mlist">
		<li>
        <a href="" onclick="targetA(this,'navimenu.jsp')" target="naviFrame" title="报送平台"><STRONG>报送平台</STRONG></a>
        <!--<span><img src="FITETL/images/m2.gif" alt="后退" /></span>
        <span><img src="FITETL/images/m3.gif" alt="前进" /></span>-->
        </li>
        <li><a href="" onclick="targetA(this,'fitosa/menu.html')" target="naviFrame" title="分析系统"><STRONG>分析系统</STRONG></a></li>
        <li><a href=""  onclick="targetA(this,'rpt_net_cunzhen/navimenu_cunzhen.html')" target="naviFrame"  title="村镇银行"><STRONG>村镇银行</STRONG></a></li>

        </ul>        
    </div>
    </div>

<!--nav,start-->
<!--
<div class="menu_navcc">
<div class="menu_nav clearfix" >
<ul class="nav_content">
<li class="current"><a href="#" title="首页"><span>首页</span></a></li>
<li><a href="" name="urlA" onclick="targetA(this,'navimenu.html')" target="naviFrame" title="报送平台"><span>报送平台</span></a></li>
<li><a href="" name="urlA" onclick="targetA(this,'fitosa/menu.html')" target="naviFrame" title="分析系统"><span>分析系统</span></a></li>
<li><a href="" name="urlA" onclick="targetA(this,'rpt_net_cunzhen/navimenu_cunzhen.html')" target="naviFrame"  title="村镇银行"><span>村镇银行</span></a><em></em></li>
</ul>
<div class="menu_nav_right">
</div>
</div>
</div>
-->
<!--nav,end-->

</body>
</html>
