val q = if (args.length >=1) args(0) else "ExampleQueue"
val jms = if (args.length >= 3) Jms.fromClassName(args(2)) else Jms.fromJndi()

new JmsTest(jms).testMesssageListener(q)

