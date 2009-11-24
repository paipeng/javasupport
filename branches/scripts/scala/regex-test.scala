import java.util.regex._
val p = (s : String) => Pattern.compile(s)
val m = (t : String, p : Pattern) => { val m = p.matcher(t); m.find; m.group(0) }
m("abc123", p("""\w"""))
