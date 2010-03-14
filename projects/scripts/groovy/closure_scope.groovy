def m1(){
	def i = 0
	def c = { i  += 1 }
	println("before $i")
	m2(c)
	println("after $i")
}
def m2(c){
	c()
}

m1()
