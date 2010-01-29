val q = if (args.length >=1) args(0) else "ExampleQueue"
val n = if (args.length >= 2) args(1).toInt else 100
new JmsTest(Jms.fromJndi()).testBurstMsg(q, n)

