package ${packageName};
<% if(origModelClassName){ %>
import ${origModelClassName}
<% } %>
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
		
	public void create(${className} ${beanName}) {
		logger.debug("Creating ${beanName} " + ${beanName});
		em.persist(${beanName});
		logger.info("${className} instance created. Id=" + ${beanName}.getId());
	}
	
	public void update(${className} ${beanName}) {
		logger.debug("Updating ${beanName} " + ${beanName});
		em.merge(${beanName});
		logger.info("${className} instance updated. Id=" + ${beanName}.getId());
	}

	public ${className} get(Integer id) {
		logger.debug("Retrieving ${beanName} " + id);
		${className} ${beanName} = em.find(${className}.class, id);
		logger.info("Found ${beanName} " + ${beanName});
		return ${beanName};
	}
	
	public ${className} delete(Integer id){
		logger.debug("Removing ${beanName} with id=" + id);
		${className} ${beanName} = get(id);
		em.remove(${beanName});
		logger.info("${className} deleted");
		return ${beanName};
	}

	public List<${className}> findAll() {
		logger.debug("Retrieving all ${beanName} objects.");
		List<${className}> ret = em.createQuery("from ${className}").getResultList();
		logger.info("Found " + ret.size());
		return ret;
	}
}

