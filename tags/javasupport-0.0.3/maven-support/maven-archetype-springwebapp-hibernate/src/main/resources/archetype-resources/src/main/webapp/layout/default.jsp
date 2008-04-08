<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page"%>
<html>
<head>
<title><decorator:title default="${artifactId}" /></title>
<link href="${dollar}{pageContext.request.contextPath}/css/reset.css" rel="stylesheet" type="text/css">
<link href="${dollar}{pageContext.request.contextPath}/css/default.css" rel="stylesheet" type="text/css">
<decorator:head />
</head>
<body>
	<div class="container">
		<jsp:directive.include file="header.jsp"/>
		<decorator:body />
	</div>	
	<jsp:directive.include file="footer.jsp"/>
</body>
</html>