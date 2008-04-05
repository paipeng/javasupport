<%@ include file="../taglibs.jsp"%>
<div class="header">
	<p>${artifactId}</p>
</div>

<ul class="solidblockmenu">
<li><a href="${dollar}{pageContext.request.contextPath}/webapp/welcome">Welcome</a></li>
<li><a href="${dollar}{pageContext.request.contextPath}/webapp/help">Help</a></li>
<li><a href="${dollar}{pageContext.request.contextPath}/webapp/systeminfo">SystemInfo</a></li>
<li><a href="${dollar}{pageContext.request.contextPath}/webapp/user/show">UserProfile</a></li>
</ul>
<br style="clear: left" />
