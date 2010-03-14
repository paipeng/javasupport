object run_template {
  def main(args: Array[String]) : Unit = {
    val ftl = new java.io.File(args(0))
    val dir = if(ftl.getParentFile == null) new java.io.File(".") 
      else ftl.getParentFile
    val out = if(args.length >=2) new java.io.FileWriter(args(1)) 
      else new java.io.OutputStreamWriter(System.out)      
    val model = new java.util.HashMap[String, Object]
    model.put("props", System.getProperties)
    val conf = new freemarker.template.Configuration
    conf.setDirectoryForTemplateLoading(dir)
    
    val template = conf.getTemplate(ftl.getName)
    template.process(model, out)
  }
}
run_template.main(args)

