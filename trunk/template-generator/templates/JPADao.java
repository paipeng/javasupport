package ${packageName};
import java.util.List;
import javax.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/** DAO for ${className}. */
@Transactional
public class ${className}Dao {
	protected Logger logger = LoggerFactory.getLogger(${className}Dao.class);
	protected EntityManager em;
	
	public void setEm(EntityManager em) {
		this.em = em;
	}
		
	public void save(${className} ${beanName}) {
		logger.debug("Saving ${beanName} " + ${beanName});
		em.persist(${beanName});
		logger.info("${className} instance is saved. Id=" + ${beanName}.getId());
	}

	public ${className} get(Integer id) {
		logger.debug("Retrieving ${beanName} " + id);
		${className} ${beanName} = em.find(${className}.class, id);
		logger.info("Found ${beanName} " + ${beanName});
		return ${beanName};
	}
	
	public void delete(Integer id){
		logger.debug("Removing ${beanName} with id=" + id);
		em.remove(get(id));
		logger.info("${className} deleted");
	}

	public List<${className}> findAll() {
		logger.debug("Retrieving all ${beanName} objects.");
		List<${className}> ret = em.createQuery("from ${className}").getResultList();
		logger.info("Found " + ret.size());
		return ret;
	}
}

