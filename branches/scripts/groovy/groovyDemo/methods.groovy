//no param
def m1(){ System.currentTimeMillis() }
println(m1())

//untyped param
def m2(p){ "param: class=" + p.getClass() + ", value=" + p }
println(m2(55))
println(m2(3.16))
println(m2("1 Cor 13:4-7"))
println(m2(new Object()))

//typed param
def m3(Double p){ "param: class=" + p.getClass() + ", value=" + p }
println(m3(3.16))
// println(m3(new Object())) ERROR! signature mismatched!


