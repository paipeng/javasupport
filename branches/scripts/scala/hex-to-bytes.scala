/** Convert from bytes into hex string. */
def bytesToHex(bytes : List[Byte]) =
  bytes.map{ b => String.format("%02X", java.lang.Byte.valueOf(b)) }.mkString(" ")
  
println(bytesToHex("Got Scala?".getBytes.toList))

/** Convert from hex string into bytes */
def chunk[A](ls : List[A], size : Int) =  
  List.range(0, ls.size, size).map{ i => ls.slice(i, i+size) }
def hexToBytes(hex : String) : List[Byte] = {
  val cleanInput = hex.replaceAll("\\s|\\n", "")
  val evenHex = if(cleanInput.length % 2 == 0) cleanInput else "0" + cleanInput
  val hexPairs = chunk(evenHex.toList, 2)
  val bytes = hexPairs.map{ pair => java.lang.Integer.parseInt(pair.mkString, 16).toByte }
  bytes
}
  
val input = """
47 6F 74 20 53 
63 61 6C 61 3F
"""
val msg = new String(hexToBytes(input).toArray)
println(msg)
