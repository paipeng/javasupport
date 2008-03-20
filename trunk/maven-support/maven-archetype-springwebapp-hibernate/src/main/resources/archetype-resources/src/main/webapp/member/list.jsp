<jsp:directive.include file="../taglibs.jsp"/>
<div class="content">

	<p>
	<a href="${dollar}{pageContext.request.contextPath}/webapp/user/create">Create New User</a>
	</p>
	
	<h1>Listing of User</h1>
	
	<table>
	
		<tr>
			<td>ID</td>
			<td>Actions</td>
		</tr>	
		
		<c:forEach var="user" items="${dollar}{userList}">
		<tr>
			<td>${dollar}{user.id}</td>
			<td>
			<a href="${dollar}{pageContext.request.contextPath}/webapp/member/profile?id=${dollar}{user.id}">Show</a> |
			<a href="${dollar}{pageContext.request.contextPath}/webapp/member/edit?id=${dollar}{user.id}">Edit</a> |
			<a href="${dollar}{pageContext.request.contextPath}/webapp/member/delete?id=${dollar}{user.id}">Delete</a>
			</td>
		</tr>	
		</c:forEach>
	</table>
</div>