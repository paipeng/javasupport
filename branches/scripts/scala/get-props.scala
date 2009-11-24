import scala.collection.jcl.Conversion._
val props = System.getProperties
for(x <- props.stringPropertyNames if x.contains("sep")){ 
  println(x + ": " + props(x))
}

