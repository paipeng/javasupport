
package deng.estore.category;
import java.util.List;
import javax.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/** DAO for Category. */
@Transactional
public class CategoryDao {
    protected Logger logger = LoggerFactory.getLogger(CategoryDao.class);
    protected EntityManager em;
    
    public void setEm(EntityManager em) {
            this.em = em;
    }
        
    public void save(Category category) {
        logger.debug("Saving category " + category);
        em.persist(category);
        logger.info("Category instance is saved. Id=" + category.getId());
    }

    public Category get(Integer id) {
        logger.debug("Retrieving category " + id);
        Category category = em.find(Category.class, id);
        logger.info("Found category " + category);
        return category;
    }
    
    public void delete(Category category){
        logger.debug("Removing category " + category);
        em.remove(category);
        logger.info("Category deleted");
    }

	public List<Category> findAll() {
		logger.debug("Retrieving all category objects.");
		List<Category> ret = em.createQuery("from Category").getResultList();
        logger.info("Found " + ret.size());
        return ret;
	}
}

