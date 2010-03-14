// graping arguments with default if not found.

//a = args[0] ?: "foo"
//b = args[1] ?: 33
//println([a, b])

//// Argument MUST presents.
//(a , b)  = args
//println([a, b])

a = args.size() >= 1 ? args[0] : "foo"
b = args.size() >= 2 ? args[1] : 33
println([a,b])


