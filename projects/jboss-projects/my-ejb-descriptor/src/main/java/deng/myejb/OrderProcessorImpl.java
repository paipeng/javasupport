package deng.myejb;

import javax.ejb.*;

import org.slf4j.*;

/*
@Stateless
@org.jboss.annotation.ejb.LocalBinding(jndiBinding="custom/OrderProcessorImpl")
*/
public class OrderProcessorImpl implements OrderProcessor {
  private Logger logger = LoggerFactory.getLogger(this.getClass());
  
  @Override
  public void process(String order) {
    logger.info("Processing order: " + order);
  }
}
