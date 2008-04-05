<jsp:directive.include file="../taglibs.jsp"/>
<div class="content">
	<c:choose>
	<c:when test="${dollar}{confirm=='no'}">		
		<p class="standout">Are you sure you want to delete User ID ${dollar}{id}?</p> 
		<p>
		<a href="${dollar}{pageContext.request.contextPath}/webapp/user/delete?confirm=yes&id=${dollar}{id}">YES</a>, or 
		<a href="${dollar}{pageContext.request.contextPath}/webapp/user/list">Back to User Listing.</a>
		</p>
	</c:when>
	<c:otherwise>
		<p>User ID ${dollar}{user.id} has been successfully deleted.</p> 
		<p>
		<a href="${dollar}{pageContext.request.contextPath}/webapp/user/list">Back to User Listing.</a>
		</p>
	</c:otherwise>
	</c:choose>
</div>