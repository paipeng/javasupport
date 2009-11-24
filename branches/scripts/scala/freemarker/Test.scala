//http://freemarker.org/docs/pgui_quickstart_all.html
//scalac Test.scala && scala Test
import freemarker.template._
import java.util._
import java.io._

object Test {
    def main(args: Array[String]) : Unit = {
        val cfg = new Configuration
        cfg.setDirectoryForTemplateLoading(new File("."))
        cfg.setObjectWrapper(new DefaultObjectWrapper())

        val temp = cfg.getTemplate("test.ftl")

        val root = new HashMap[String, Object]
        root.put("user", "Big Joe")
        val latest = new HashMap[String, Object]
        root.put("latestProduct", latest)
        latest.put("url", "products/greenmouse.html")
        latest.put("name", "green mouse")

        val out = new OutputStreamWriter(System.out)
        temp.process(root, out)
        out.flush()
    }
}

