import javax.swing._
object JavaSwingHelloWorld {
  def main(args: Array[String]) : Unit = {    
    SwingUtilities.invokeLater(new Runnable{
      def run : Unit = {
        val frame = new JFrame("Hello")
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
        
        val label = new JLabel("hello world")
        frame.getContentPane.add(label)
        
        frame.pack
        frame.setVisible(true)
      }
    })
  }
}
