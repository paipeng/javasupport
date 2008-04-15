<#assign form=JspTaglibs["/WEB-INF/resources/spring-form.tld"]>
<div class="content">
	<p>
	<a href="\${Application.contextPath}/webapp/${classNamePath}/list">Back to ${className} Listing.</a>
	</p>
	<h1>Edit ${className} ID \${${beanName}.id}</h1>
	<@form.form commandName="${beanName}">
	<table>
	<% for (field in displayFields) { %>	
		<tr><td>${field[2]}</td><td><@form.input path="${field[0]}" size="25"/><br/><@form.errors cssClass="inputError" path="${field[0]}"/></td></tr>
	<% } %>
	
		<tr><td colspan="2"><input type="submit" value="Save"/></td></tr>
	</table>
	</@form.form>
</div>
