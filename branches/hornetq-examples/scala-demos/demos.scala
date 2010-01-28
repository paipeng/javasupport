// scalac -d out IO.scala Jms.scala
// mkcp '/apps/hornetq/lib/*' './out' './'
// scala demos.scala                
JmsTest.testSession        
JmsTest.testTempQ
JmsTest.testSendToTempQ
JmsTest.testMsgListener

