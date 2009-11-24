lines = []
System.in.eachLine { ln -> lines << ln }
println(lines.join(File.pathSeparator))

