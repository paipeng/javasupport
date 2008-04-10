<div class="content">
	<#if confirm=="yes">
		<p>${className} ID \${${beanName}.id} has been successfully deleted.</p> 
		<p>
		<a href="\${Application.contextPath}/webapp/${classNamePath}/list">Back to ${className} Listing.</a>
		</p>
	<#else>		
		<p class="standout">Are you sure you want to delete ${className} ID \${id}?</p> 
		<p>
		<a href="\${Application.contextPath}/webapp/${classNamePath}/delete?confirm=yes&id=\${id}">YES</a>, or 
		<a href="\${Application.contextPath}/webapp/${classNamePath}/list">Back to ${className} Listing.</a>
		</p>
	</#if>
</div>