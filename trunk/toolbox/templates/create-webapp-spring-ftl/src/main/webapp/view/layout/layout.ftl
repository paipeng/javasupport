[#noparse][#macro main title="${Application.contextPath}"]
<html>
    <head>
        <title>${title}</title>
        <link href="${Application.contextPath}/view/layout/screen.css" rel="stylesheet" type="text/css" media="screen">
    </head>
    <body>    
    
    	<div class="header">
		    <p>[/#noparse]${project.groupId}[#noparse]</p>
		</div>
		
		<div id="navcontainer">
			<ul id="navlist">
			    <li><a href="${Application.contextPath}/webapp/main">Main</a></li>
			    <li><a href="${Application.contextPath}/webapp/about">About</a></li>
			    [#-- <li><a href="${Application.contextPath}/webapp/systeminfo">SystemInfo</a></li> --]
			</ul>
		</div>
		    	
        <div class="container">        	
			<div class="content">
            [#nested]
            </div>
        </div>	
        
        <div class="footer center">
		    <p>${Application.appInfo.buildVersion}</p>
		</div>
    </body>
</html>
[/#macro]
[/#noparse]