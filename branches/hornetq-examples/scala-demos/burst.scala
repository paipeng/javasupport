val n = if (args.length >= 1) args(0).toInt else 100
val q = if (args.length >= 2) args(1) else "ExampleQueue"
new JmsTest(Jms.fromJndi()).testBurstMsg(q, n)

