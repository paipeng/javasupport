package demo.dengz1.cameldemo;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.log4j.Logger;

public class MyFileDemo {
	public static Logger logger = Logger.getLogger(MyFileDemo.class);
	final static String IN_DIR = "file://D:/tmp/camel-demo-data/in";
	final static String OUT_DIR = "file://D:/tmp/camel-demo-data/out";
	
	public static void main(String[] args) throws Exception {
		final CamelContext camelCtx = new DefaultCamelContext();
		camelCtx.addRoutes(new RouteBuilder(){
			@Override
			public void configure() throws Exception {
				logger.debug("Configuring from " + IN_DIR);
				logger.debug("Configuring to " + OUT_DIR);
				
				from(IN_DIR).
				to(OUT_DIR).
				process(new Processor(){
					@Override
					public void process(Exchange exchange) throws Exception {
						logger.info(exchange.getIn());
					}					
				});
			}			
		});		

		logger.debug("Starting up Camel");
		camelCtx.start();
		logger.info("Camel is ready.");
		
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {
				try {
					logger.debug("Stopping Camel");
					camelCtx.stop();
					logger.info("Camel is down.");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
