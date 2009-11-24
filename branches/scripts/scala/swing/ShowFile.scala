import scala.swing._
import swing.event._
object ShowFile extends SimpleGUIApplication {
  def top = new MainFrame{    
    val textArea = {
      import java.awt.Font
      val ta = new TextArea(35, 80)
      ta.editable = false
      ta.font = new Font("Courier New", Font.PLAIN, 12) 
      ta
    }
    
    val fileOpenField = new BoxPanel(Orientation.Horizontal){
        val tf = new TextField()
        contents += new Label("File")
        contents += tf
        contents += new Button("Open"){
          reactions += {
            case ButtonClicked(_) => openFile(tf.text)
          }
        }
    }
    
    def openFile(fn : String) = {
      import scala.io.Source
      textArea.text = Source.fromFile(fn).getLines.mkString
    }
    
    contents = new BorderPanel{
      add(fileOpenField, BorderPanel.Position.North)
      add(textArea, BorderPanel.Position.Center)
    }
  }
}
