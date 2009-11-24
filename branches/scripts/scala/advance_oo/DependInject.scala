class Param{
  var d: Double = 0 
}
class Service1 {
  var num: Int = 0
  def run = println("num: " + num)
}
class Service2 {
  var param: Param = null
  def run = println("param.d: " + param.d)
}
class Runner {
  var service1: Service1 = null
  var service2: Service2 = null
  def run = { service1.run; service2.run }  
}
val param = new Param
param.d = 123.45
val s1 = new Service1
s1.num = 67
val s2 = new Service2
s2.param = param
val runner = new Runner
runner.service1 = s1
runner.service2 = s2
runner.run
