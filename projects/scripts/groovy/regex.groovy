// /word/ => string, not RegEx obj
// ~/word/ => Pattern obj
s = /.sub/
assert s.class == String.class
re = ~/.sub/
assert re.class == java.util.regex.Pattern.class

// passing string of regex, not Pattern object. Altough the // construct will escape back slash!
assert "test.sub".matches(/.*\.sub/) == true

// Using =~ operator in an IF condition. It will convert string into pattern then perform Matcher find()
if("test.sub" =~ /\.sub/)
	println("matched")
else
	println("not matched.")

// Note that =~ operator will return Matcher object!
x = "test.sub" =~ /\.sub/
assert x.class == java.util.regex.Matcher.class
assert x.find() == true   

// Text extraction.
names = "test.sub" =~ /(\w+)\.(\w+)/
println names.class
println names[0].class // All the matched groups are in first element of Matcher's object!
// println names[1].class //ERROR: java.lang.IndexOutOfBoundsException
println names[0]

assert names[0][0] == "test.sub"
assert names[0][1] == "test"
assert names[0][2] == "sub"
