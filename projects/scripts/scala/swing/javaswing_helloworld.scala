
import javax.swing._

/*
//This will not work!!!!
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
*/

val frame = new JFrame("Hello")
frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

val label = new JLabel("hello world")
frame.getContentPane.add(label)

frame.pack
frame.setVisible(true)

