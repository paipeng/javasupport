<html>
<head>
<title>${tomcatInstanceName} : tomcat instance</title>
</head>
<body>

<h1 style="color: green;">Tomcat Server Instance: ${tomcatInstanceName}</h1>
<%
	//Quick display of what's under this instance of webapps dir.
	String webappPathname = request.getRealPath("/../");
	java.io.File webapps = new java.io.File(webappPathname);
	java.io.File[] files = webapps.listFiles(new java.io.FileFilter(){
		public boolean accept(java.io.File file){ 
			if(file.isDirectory() && !(file.getName().equals("ROOT") || file.getName().startsWith("."))) 
				return true;
			return false;
		}
	});
	if(files != null && files.length>0){
		out.println("<p>Deployed web applications:");
		out.println("<ul>");
		for(java.io.File file : files){
			out.println("<li><a href='/"+file.getName()+"'>"+file.getName()+"</a></li>");
		}
		out.println("</ul>");
	}else{
		out.println("<p>No webapp found. You may deploy a webapp by copying a war file into <pre>"+
				webappPathname+"</pre> dir.</p>");	
	}
%>

</body>
</html>
