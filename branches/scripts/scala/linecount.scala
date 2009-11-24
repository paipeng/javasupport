val dir = args(0)

import java.io._
import sweet.helper.FileHelper._

var count = 0
new File(dir).foreachFile{ file =>
  file.foreachLine{ ln => count += 1}
}
println(count + " lines")