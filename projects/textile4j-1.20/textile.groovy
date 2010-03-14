import net.sf.textile4j.*
text = new File(args[0]).text
println(new Textile().process(text))