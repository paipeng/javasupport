package demo.dengz1.myejbapp.ejb.simple;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;

@Local(MySLSB.class)
@Stateless(name="MySLSBImpl")
public class MySLSBImpl implements MySLSB {
	public static Logger logger = Logger.getLogger(MySLSBImpl.class);
		
	@Override
	public void doSessionWork() {
		logger.info("I am working!!!");
	}
}
