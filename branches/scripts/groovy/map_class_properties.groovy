m = [ a : 101, b : 102 ]
println m
println m.a
println m.b
println m.class // It's looking for a hash with key 'class', not class getClass()!
println m.getClass() // Have to do this explicitly.