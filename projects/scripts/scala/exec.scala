import sweet.helper.StringHelper._

println("\n\n===> Directory entries:")
"cmd /c dir".exec{ ln => 
	if(ln.matches("\\d.*")) 
		println(ln.split("\\s+").last) 
}

println("\n\n===> Path entries:")
"cmd /c echo %PATH%".execResultAsList.mkString.split(java.io.File.pathSeparator).foreach{println} 
