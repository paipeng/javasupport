dir = System.getProperty("user.home") + "/.groovy/lib"
if (args.length >= 1){ dir = args[0] }
println("Making path $dir")
new File(dir).mkdirs()
