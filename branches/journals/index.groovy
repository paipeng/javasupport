import net.sf.textile4j.*

appPath = application.getRealPath("/")
action = request.getParameter("action")

if (action == "create" || action == "update") {	
	filename = new java.text.SimpleDateFormat("MMddyyyy").format(new Date()) + ".txt"
	file = new File(appPath + "/" + filename)
	processActionName = "Create"
	text = ""
	if (file.exists()) {
		text = file.text
		processActionName = "Update"
	} else {
		file.write("")
	}
		
	println("""
		<html>
		<head><title>Create New Journal : ${file.name}</title></head>
		<body>
			<p>Journal Entry: ${file}</p>
			<a href="${request.contextPath}/index.groovy">Back To Index</a>
			<hr/>
			<form>
				<textarea name="notes" cols="80" rows="30">${text}</textarea>
				<input name="filename" value="${filename}" type="hidden" />
				<input name="action" value="process${processActionName}" type="hidden" />
				<br/>
				<input type="Submit" value="${processActionName}"/>
			</form>
		</body>
		</html>
	""")
} else if (action == "processCreate" || action == "processUpdate") {
	filename = request.getParameter("filename")
	text = request.getParameter("notes")
	new File(appPath + "/" + filename).write(text)
	//request.getRequestDispatcher("${request.contextPath}/index.groovy?action=view&filename=${filename}").forward(request, response)
	response.sendRedirect("${request.contextPath}/index.groovy?action=view&filename=${filename}")
} else if (action == "view") {
	format = request.getParameter("format")
	filename = request.getParameter("filename")
	text = new File(appPath + "/" + filename).text
	println("""
		<html>
		<head><title>Journal : ${filename} </title></head>
		<body>
		<a href="${request.contextPath}/index.groovy">Back To Index</a>
		| <a href="${request.contextPath}/index.groovy?action=update&filename=${filename}">Update File</a>
		""")	
	if (format == null || format == "textile") {
		println("""
			| <a href="${request.contextPath}/index.groovy?action=view&filename=${filename}&format=plain">Plain Text View</a>
			<hr>
			${new Textile().process(text)}
		""")
	}else if (format == "plain") {
		println("""
			| <a href="${request.contextPath}/index.groovy?action=view&filename=${filename}&format=textile">Textile View</a>
			<hr>
			<pre>${text}</pre>
		""")
	}
	println("""
		</body>
		</html>
	""")		
} else {
	println("""
		<html>
		<head><title>Zemian Journals</title></head>
		<body>
			<p>Technical Journals in Textile Format.</p>
			<a href="${request.contextPath}/index.groovy?action=create">Today's Journal</a>
			<hr/>
			<table>""")
			
	new File(appPath).eachFile { file ->
		if (file.isFile() && file.name =~ /\d+\.txt$/) {
			println("""
					<tr><td><a href="${request.contextPath}/index.groovy?action=view&filename=${file.name}">${file.name}</a></td></tr>
			""")
		}
	}
	
	println("""
			</table>
		</body>
		</html>
	""")
}