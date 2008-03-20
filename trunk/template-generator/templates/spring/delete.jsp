<jsp:directive.include file="../taglibs.jsp"/>
<div class="content">
	<c:choose>
	<c:when test="\${confirm=='no'}">		
		<p class="standout">Are you sure you want to delete ${className} ID \${id}?</p> 
		<p>
		<a href="\${pageContext.request.contextPath}/webapp/${classNamePath}/delete?confirm=yes&id=\${id}">YES</a>, or 
		<a href="\${pageContext.request.contextPath}/webapp/${classNamePath}/list">Back to ${className} Listing.</a>
		</p>
	</c:when>
	<c:otherwise>
		<p>${className} ID \${${beanName}.id} has been successfully deleted.</p> 
		<p>
		<a href="\${pageContext.request.contextPath}/webapp/${classNamePath}/list">Back to ${className} Listing.</a>
		</p>
	</c:otherwise>
	</c:choose>
</div>