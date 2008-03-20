<jsp:directive.include file="../taglibs.jsp"/>
<div class="content">
<ul>
<c:forEach var="${beanName}" items="\${list}">
<li>${${beanName}.id} \${${beanName}}</li>
</c:forEach>
</ul>
</div>