import sweet.helper.StreamHelper._
val lines = new collection.mutable.ListBuffer[String]
System.in.foreachLine{ ln => lines += ln }
lines.toList.sort{ _ < _ }.foreach{ println(_) }
