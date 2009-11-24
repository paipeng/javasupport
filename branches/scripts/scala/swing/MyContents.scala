import scala.swing._
object MyContents extends SimpleGUIApplication {
  def top = new MainFrame{
    contents = new FlowPanel{
      contents += new Label("Simple GUI")
      contents += new Button("Click Me")
      contents += new ComboBox(List("Please Select One...", "Scala", "Java")) 
    }
  }
}
