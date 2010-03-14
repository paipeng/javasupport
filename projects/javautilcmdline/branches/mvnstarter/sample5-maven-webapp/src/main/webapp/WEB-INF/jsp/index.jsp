<%@page import="sample5.SampleDemo" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<body>
<h2>Hello World!</h2>
<p>Client Browser: ${header['user-agent']}</p>
<p>
    <c:set var="sList" value="<%= SampleDemo.split("ONElongsepTWO")%>"></c:set>
    -->${sList}<br/>
    <c:forEach items="${sList}" var="s">${s}<br/></c:forEach>
</p>
</body>
</html>
