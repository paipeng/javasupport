
<jsp:directive.include file="../taglibs.jsp"/>
<div class="content">
      

<h1>Create New Category</h1>
<form:form commandName="category">
<table>

  <tr><td>Name</td><td><form:input path="name" size="25"/><br/><form:errors cssClass="inputError" path="name"/></td></tr>

  <tr><td>Description</td><td><form:input path="description" size="25"/><br/><form:errors cssClass="inputError" path="description"/></td></tr>

	<tr><td colspan="2"><input type="submit" value="Create"/></td></tr>
</table>
</form:form>
</div>
