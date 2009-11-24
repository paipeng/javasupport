import javax.imageio._
import java.io._
import java.awt.image.BufferedImage

val imageTypes = Map(
  BufferedImage.TYPE_3BYTE_BGR 	    -> "TYPE_3BYTE_BGR", 
  BufferedImage.TYPE_4BYTE_ABGR 	  -> "TYPE_4BYTE_ABGR", 
  BufferedImage.TYPE_4BYTE_ABGR_PRE -> "TYPE_4BYTE_ABGR_PRE", 
  BufferedImage.TYPE_BYTE_BINARY 	  -> "TYPE_BYTE_BINARY", 
  BufferedImage.TYPE_BYTE_GRAY 	    -> "TYPE_BYTE_GRAY", 
  BufferedImage.TYPE_BYTE_INDEXED 	-> "TYPE_BYTE_INDEXED", 
  BufferedImage.TYPE_CUSTOM 	      -> "TYPE_CUSTOM", 
  BufferedImage.TYPE_INT_ARGB 	    -> "TYPE_INT_ARGB", 
  BufferedImage.TYPE_INT_ARGB_PRE 	-> "TYPE_INT_ARGB_PRE", 
  BufferedImage.TYPE_INT_BGR 	      -> "TYPE_INT_BGR", 
  BufferedImage.TYPE_INT_RGB 	      -> "TYPE_INT_RGB", 
  BufferedImage.TYPE_USHORT_555_RGB -> "TYPE_USHORT_555_RGB", 
  BufferedImage.TYPE_USHORT_565_RGB -> "TYPE_USHORT_565_RGB", 
  BufferedImage.TYPE_USHORT_GRAY 	  -> "TYPE_USHORT_GRAY"
)

args.foreach{ fn =>
  //val fn = args(0)
  println("Filename: " + fn)
  if(fn == "-h"){
    println("Supported image formats")
    val names = ImageIO.getReaderFormatNames.
      map{ x => x.toLowerCase }.
      toList.
      removeDuplicates
    names.foreach{ x => println(x) }  
    exit(0)
  }
  val bufferedImage = ImageIO.read(new File(fn))
  println(bufferedImage)
  println("Image type: " + imageTypes(bufferedImage.getType))
  println("ColorModel classname: " + bufferedImage.getColorModel.getClass)
}