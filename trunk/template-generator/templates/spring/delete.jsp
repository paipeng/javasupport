<jsp:directive.include file="../taglibs.jsp"/>
<div class="content">
	<p>${className} ID \${${beanName}.id} has been successfully deleted.</p> 
	<p>
	<a href="\${pageContext.request.contextPath}/webapp/${classNamePath}/list">Back to ${className} Listing.</a>
	</p>
</div>