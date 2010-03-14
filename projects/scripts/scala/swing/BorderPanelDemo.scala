import scala.swing._
object ShowFile extends SimpleGUIApplication {
  def top = new MainFrame{
    contents = new BorderPanel{
      add(createFileOpenField, BorderPanel.Position.North)
      add(createTextArea, BorderPanel.Position.Center)
    }
    
    def createFileOpenField = new BoxPanel(Orientation.Horizontal){
        contents += new Label("File")
        contents += new TextField()
        contents += new Button("Open")      
    }
    
    def createTextArea = new TextArea(35, 80)
  }
}
