<jsp:directive.include file="../taglibs.jsp"/>
<div class="content">
	<c:choose>
	<c:when test="${confirm}=='no'">		
		<p class="important">Are you sure you want to delete ${className} ID \${${beanName}.id}?</p> 
		<p>
		<a href="\${pageContext.request.contextPath}/webapp/${classNamePath}/delete?confirm=yes&id=\${${beanName}.id}">YES</a>, or 
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