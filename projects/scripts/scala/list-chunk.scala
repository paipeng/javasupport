def chunk[A](ls : List[A], size : Int) =  
  List.range(0, ls.size, size).map{ i => ls.slice(i, i+size) }

// Alternative to chunking.  
//var hexIdx = 0; var resIdx = 0 
//val bytes : Array[Byte] = new Array[Byte](paddedHex.length/2)
//while(hexIdx < paddedHex.length){
//  val s = paddedHex.substring(hexIdx, hexIdx + 2)
//  res(resIdx) = java.lang.Integer.parseInt(s, 16).toByte
//  resIdx += 1
//  hexIdx += 2
//}
