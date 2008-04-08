<${pound}assign decorator=JspTaglibs["http://www.opensymphony.com/sitemesh/decorator"]>
<${pound}assign page=JspTaglibs["http://www.opensymphony.com/sitemesh/page"]>
<html>
    <head>
        <title><@decorator.title default="${artifactId}"/></title>
        <link href="${dollar}{contextPath}/css/reset.css" rel="stylesheet" type="text/css">
        <link href="${dollar}{contextPath}/css/default.css" rel="stylesheet" type="text/css">
        <@decorator.head />
    </head>
    <body>
        <div class="container">
            <${pound}include "header.ftl"/>
            <@decorator.body />
        </div>	
        	<${pound}include "footer.ftl"/>
    </body>
</html>