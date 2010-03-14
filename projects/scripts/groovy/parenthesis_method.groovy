//for all method without argument, you MUST invoke it with parenthesis!
def m(){ println "method with no arguments" }
//m  //ERROR Caught: groovy.lang.MissingPropertyException: No such property: m for class: parenthesis_method
m()  //OK

//for method with at least one argument, then you may call without parenthesis
def m(text){ println text }
m "foo"  //OK
m("bar") //OK


//Interesting overloading method
println()
//println     //ERROR: Caught: groovy.lang.MissingPropertyException: No such property: println for class: parenthesis_method
println("") //OK
println ""