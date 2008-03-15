<jsp:directive.include file="../taglibs.jsp"/>
<div class="content">
<ul>
<c:forEach var="category" items="${list}">
<li>${category.id} ${category}</li>
</c:forEach>
</ul>
</div>