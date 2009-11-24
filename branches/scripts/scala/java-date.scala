val cal = new java.util.GregorianCalendar
println(cal.isLeapYear(1998))

val df = new java.text.SimpleDateFormat("yyyy/MM/dd")
val dt = df.parse("1998/02/29")
println(dt)

df.setLenient(false)
val dt2 = df.parse("1998/02/29") // --> java.text.ParseException: Unparseable date: "1998/02/29"
println(dt2)

