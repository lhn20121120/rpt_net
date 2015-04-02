<%@ page language="java" pageEncoding="GB2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:html locale="true">
<%
	java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
	java.util.Calendar calendar = java.util.Calendar.getInstance();
	String date = format.format(calendar.getTime());
	String rtTitle = "";
	if(com.cbrc.smis.common.Config.REMINDTIPS != null)
		rtTitle = com.cbrc.smis.common.Config.REMINDTIPS.getRtTitle();
%>
<head>
	<html:base />
	<title>��ܰС��ʿ</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../css/common.css" type="text/css" rel="stylesheet">
	<script type="text/javascript">
		function checkTitle(){
			var rtTitle = document.getElementById('rtTitle');
			if(rtTitle.value.Trim() == ""){
				alert("��������ʿ��������!");
				rtTitle.focus();
				return false;
			}
            else                                //-----------------------
            {                                   // gongming 2008-07-25
                return checkLength(rtTitle,100);
            }
		}
		
		function _back(){
			window.location="<%=request.getContextPath()%>/system_mgr/viewRemindTips.jsp";
		}
			 
		String.prototype.Trim=function(){
			return this.replace(/(^\s*)|(\s*$)/g,"");
		}
    
 //-------------------------------------------------
 // gongming 2008-7-25
        function checkLength(pObj,length)
        {
          var i,sum;
          sum = 0;
          strTemp = pObj.value;
          for(i=0;i< strTemp.length;i++)
          {
            if ((strTemp.charCodeAt(i)>=0) && (strTemp.charCodeAt(i)<=128))
              sum = sum + 1;
            else
              sum = sum + 2;
          }
          if(sum > length){
            alert('�ַ����Ȳ��ܳ����涨���ȣ�');
            pObj.focus();
            return false;
          }
          return true;
        }
//--------------------------------------------------
	</script>
</head>
<body>
	<table border="0" width="98%" align="center">
		<tr>
			<td height="4"></td>
		</tr>
		<tr>
			<td>
				��ǰλ�� >> ϵͳ���� >> ��ʿ����
			</td>
		</tr>
		<tr>
			<td height="4"></td>
		</tr>
	</table>
	<html:form action="/system_mgr/saveRemindTips" method="post" styleId="form1" onsubmit="return checkTitle()" >
	    <table width="80%" border="0" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
		    
		      <tr class="titletab">
		            <th align="center">�趨����С��ʿ</th>
		      </tr>
		      <tr>
			      <td bgcolor="#ffffff">
			      	<table width="100%" border="0" align="center">
			      		<TR bgcolor="#FFFFFF">	
							<TD>��ʿ���ڣ�</TD>
							<TD><%=date%> : <font color="red"><strong>�������һ�ٸ��ַ�!</strong></font></TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD>��ʿ���ݣ�</TD>
							<td><html:textarea property="rtTitle"  cols="80" rows="10" value="<%=rtTitle%>"/></td>
						</tr>
						<tr>
							<td colspan="2" align="center" bgcolor="#ffffff">
								<html:submit value="�� ��" styleClass="input-button"/>
								<input type="button" class="input-button" onclick="_back()" value="�� ��">
							</td>
						</tr>
						<input type="hidden" name="rtDate" value="<%=date%>"/>
			      	</table>
			      </td> 
		       </tr>
		      </table>
        </html:form>
</body>
</html:html>
