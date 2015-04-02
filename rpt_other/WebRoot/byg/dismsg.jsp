<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<jsp:include page="/page.jsp"  flush="true"></jsp:include>	
<table style="border:1px solid #88C5EE;width: 1050px;font-size: 12px" cellspacing="0" cellpadding="0">
					<tr>
						<td colspan="4" style="color: #0C507C;padding:4px 2px 4px 4px;background-color: #4bacff;font-size: 12px;font-weight: bolder;border-bottom: 1px solid #88C5EE;filter:progid:DXImageTransform.Microsoft.gradient(startcolorstr=#ffffff,endcolorstr=#bfe1f2,gradientType=0);">收件箱</td>
					</tr>
					<tr>
						<td align="center" width="20%" style="background:url(<%=request.getContextPath() %>/images/content_bg.jpg) repeat-x bottom #fff;padding:4px 2px 4px 4px;border-bottom: 1px solid #88C5EE;">收件人</td>
						<td align="center" width="55%" style="background:url(<%=request.getContextPath() %>/images/content_bg.jpg) repeat-x bottom #fff;padding:4px 2px 4px 4px;border-bottom: 1px solid #88C5EE;">主题</td>
						<td align="center" width="15%" style="background:url(<%=request.getContextPath() %>/images/content_bg.jpg) repeat-x bottom #fff;padding:4px 2px 4px 4px;border-bottom: 1px solid #88C5EE;">时间</td>
					</tr>
					<s:if test="msgInfoResult==null || msgInfoResult.results==null || msgInfoResult.results.size()==0">
						<tr>
							<td colspan="4" style="padding:4px 2px 4px 4px;">暂无信息</td>
						</tr>
					</s:if>
					<s:else>
					<s:iterator value="msgInfoResult.results" id="m">
						<tr onMouseOver="this.style.backgroundColor='#CDE9F8'" onMouseOut="this.style.backgroundColor='#FFFFFF'"  style="cursor: hand" onclick="window.parent.getPubMsg(<s:property value='#m.msgId'/>)">
							<td align="center" style="padding:4px 2px 4px 4px;border-bottom: 1px solid #88C5EE;"><s:property value="#m.touserName"/>&nbsp;</td>
							<td align="center" style="padding:4px 2px 4px 4px;border-bottom: 1px solid #88C5EE;">
								<s:if test="#m.content.length>50">
									<s:property value="#m.content.subString(0,50)"/>...
								</s:if>
								<s:else>
									<s:property value="#m.content"/>&nbsp;
								</s:else>
							</td>
							<td align="center" style="padding:4px 2px 4px 4px;border-bottom: 1px solid #88C5EE;"><s:property value="#m.startTime"/></td>
						</tr>
					</s:iterator>
					</s:else>
				
				</table>
				<s:if test="msgInfoResult!=null && msgInfoResult.results!=null && msgInfoResult.results.size!=0">
								<form action="tmsginfo!findOwnPublishMsg.action" id="msgcurPageForm" method="post">
							 	<table border="0" style="font-size: 12px;width: 1050px">
										<tr>
										<s:hidden name="msgInfoResultpageNo" />
										<s:hidden name="modelResultpageNo" ></s:hidden>
											<td align="left" >共<s:property value="msgInfoResult.totalCount"/>条记录
											&nbsp;&nbsp;第<s:property value="msgInfoResult.currentPage"/>/<s:property value="msgInfoResult.pageCount"/>页	
											<input type="hidden" name="currentPage" value="<s:property value='msgInfoResult.currentPage'/>"/>	
											<input type="hidden" name="pageCount" value="<s:property value='msgInfoResult.pageCount'/>"/>				
										</td>
										<td align="right">
											<img src="<%=request.getContextPath()%>/images/first.gif"  onclick="window.parent.turntoFirst('msgcurPageForm','msgInfoResultpageNo')" style="cursor: hand;vertical-align: middle;" />
									       <img src="<%=request.getContextPath()%>/images/up.gif" onclick="up('msgcurPageForm','msgInfoResultpageNo')" style="cursor: hand;vertical-align: middle;" />
									       <img src="<%=request.getContextPath()%>/images/down.gif" onclick="down('msgcurPageForm','msgInfoResultpageNo')" style="cursor: hand;vertical-align: middle;" />
									       <img src="<%=request.getContextPath()%>/images/last.gif" onclick="turntoLast('msgcurPageForm','msgInfoResultpageNo')" style="cursor: hand;vertical-align: middle;" />
											&nbsp;&nbsp;
											跳转至<input type="text" style="width: 30px;height:10px" id="form1_pageNo"/>页
											 <img src="<%=request.getContextPath()%>/images/goto.gif" onclick="turntoPage('msgcurPageForm','msgInfoResultpageNo')" style="cursor: hand;vertical-align: middle;" />
												<input  type="hidden" value="1" name="type"/>
											</td>
								
										</tr>
										
									</table>
							</form>
						</s:if>