/**
Secrecy is the kind.

@author Zemian Deng (dengz1) Dec 17, 2008
*/
object encoder{
	val offset = 8
	val letters = (('a' to 'z') ++ ('A' to 'Z') ++ ('0' to '9')).toList
	def encode(text : String) = {
		val orig = letters
		val dest = orig.slice(offset, orig.length) ++ orig.slice(0, offset)
		val table = Map() ++ orig.zip(dest)
		text.map{ c => table.getOrElse(c, c) }.mkString
	}
	def decode(text : String) = {
		val dest = letters
		val orig = dest.slice(offset, dest.length) ++ dest.slice(0, offset)
		val table = Map() ++ orig.zip(dest)
		text.map{ c => table.getOrElse(c, c) }.mkString
	}
}
val input = args(0)
if(input == "-d")
	println(encoder.decode(args(1)))
else
	println(encoder.encode(input))
	