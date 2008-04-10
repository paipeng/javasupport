<div class="content">

	<p>
	<a href="\${Application.contextPath}/webapp/${classNamePath}/create">Create New ${className}</a>
	</p>
	
	<h1>Listing of ${className}</h1>
	
	<table>
	
		<tr>
			<td>ID</td>
			<td>Actions</td>
		</tr>	
		
		<#list userList as user>
		<tr>
			<td>\${${beanName}.id}</td>
			<td>
			<a href="\${Application.contextPath}/webapp/${classNamePath}/show?id=\${${beanName}.id}">Show</a> |
			<a href="\${Application.contextPath}/webapp/${classNamePath}/edit?id=\${${beanName}.id}">Edit</a> |
			<a href="\${Application.contextPath}/webapp/${classNamePath}/delete?id=\${${beanName}.id}">Delete</a>
			</td>
		</tr>	
		</#list>
	</table>
</div>