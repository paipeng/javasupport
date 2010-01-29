val n = if (args.length == 0) 1 else args(0).toInt
new JmsTest(Jms.fromJndi()).testBurstMsg("ExampleQueue", n)

