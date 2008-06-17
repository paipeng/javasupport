package toolbox.config
object ConfigDemo extends Application {
  println(Config.props)
  //println(toolbox.scalasupport.RichSystem.props)
}

object Config {
  val props = PropertiesFile("config.properties")  
}

import scala.collection.jcl.MapWrapper
object PropertiesFile {
  def apply(name: String): MapWrapper[String, String] = {
    val props = new java.util.Properties
    props.load(new java.io.FileReader(name))
    new MapWrapper[String, String]{
      def underlying = props.asInstanceOf[java.util.Map[String,String]]
    }
  }
}
