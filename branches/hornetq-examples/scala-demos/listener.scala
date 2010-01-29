val q = if (args.length >=1) args(0) else "ExampleQueue"
new JmsTest(Jms.fromJndi()).testMesssageListener(q)

