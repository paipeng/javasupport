<${pound}assign decorator=JspTaglibs["http://www.opensymphony.com/sitemesh/decorator"]>
<${pound}assign page=JspTaglibs["http://www.opensymphony.com/sitemesh/page"]>
<html>
    <head>
        <title><@decorator.title default="${artifactId}"/></title>
        <link href="${dollar}{contextPath}/theme/default/css/screen.css" rel="stylesheet" type="text/css" media="screen">
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