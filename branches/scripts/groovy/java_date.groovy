import java.text.*

println(new Date())

df = new SimpleDateFormat("MMddyyyy")
//d = df.parse("04082009")
d = df.parse("00000000")
println(d)
cal = Calendar.getInstance()
cal.setTime(d)
println(cal.get(Calendar.YEAR))
println(df.format(new Date()))

