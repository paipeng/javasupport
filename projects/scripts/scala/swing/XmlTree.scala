import scala.swing._
object XmlTree extends SimpleGUIApplication {
  def top = new MainFrame{
    import scala.swing.BorderPnel.Position._
    contents = new BorderPanel{
      contents.add(new Label("File"), North)
      contents.add(new TextArea("File"), North)
    }
  }
}
