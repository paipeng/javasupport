import scala.swing._
object ScalaSwingHelloWorld extends SimpleGUIApplication {
  def top = new MainFrame{
    contents = new Label("hello world")
  }
}
