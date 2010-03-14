var index = 0
var result = 0

//===> Regular method declarations and usages.
def m() : Int = { index +=1; index }
println(m())  // => 1
println(m)    // => 2   Notice that it invoked without parenthesis!

// same as above with inferred return type 
def m2() =  { index +=1; index }
println(m2)    // => 3
println(m2())  // => 4

// similar as above with out param parenthesis when defining!
def m3 =  { index +=1; index }
println(m3)      // => 5
//println(m3())  // ERROR! you can't invoke it with parenthesis now!



//===> Closure/Function as object/variable

//  var_name  closure parameters
//  |         |     closure block
val closure = () => { index +=1; index }
println(closure())        // => 6          Invoking closure with parenthesis
println(closure)          // => <function> Not invoking closure, but to pass in as an object!
println(closure.toString) // => <function> Just like above line, use it as object.


//===> Closure/Function as method parameter

//notice that now you DECLARE the closure type, not defining it in this m3 method!
def m3(cParam : () => Int) = cParam()

//use m3 by create closure on the fly
result = m3(() => { index +=1; index }) // 
println(result)      // => 7

//use m3 by pass in a previously saved closure object
result = m3(closure) 
println(result)      // => 8


//===> Closure/Function as method parameter without parenthesis, (also called by-name function/method)
def byName(func : => Int) : Int = func

//To pass into by-name closure, you simle enclose in braces, not even '=>' is needed.
result = byName({ index +=1; index })
println(result)      // => 9

//parenthesis can be omitted too.
result = byName{ index +=1; index }
println(result)      // => 10

//Note that you can't pass a closure object to a by-name fuction/method.
//result = byName(closure) // Error!
//result = byName(() => { index +=1; index }) // Error!

