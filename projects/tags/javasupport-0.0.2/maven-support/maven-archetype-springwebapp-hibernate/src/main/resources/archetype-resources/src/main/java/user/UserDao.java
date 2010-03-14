package ${groupId}.user;

import java.util.List;
import javax.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author thebugslayer
 */
@Transactional
public class UserDao {
    protected Logger logger = LoggerFactory.getLogger(UserDao.class);
    protected EntityManager em;
    
    public void setEm(EntityManager em) {
            this.em = em;
    }
    
    public boolean exists(String username){        
        logger.debug("Searching for user with " + username);
        List<User> users = em.createQuery("select u from User u where u.username=:un").setParameter("un", username).getResultList();
        return users.size() > 0;
    }
    
    public User authenticate(String username, String plainPassword){
        logger.debug("Searching for user with " + username);
        List<User> users = em.createQuery("select u from User u where u.username=:un").setParameter("un", username).getResultList();
        
        if(users.size() == 0)
            throw new UsernameNotFoundException();
            
        logger.debug("Found user, checking password");
        User foundUser = users.get(0);
        if(!foundUser.getPassword().equals(plainPassword))
            throw new PasswordNotMatchException();
            
        logger.info("User "+ foundUser.getId()+" authenticated.");
        return foundUser;
    }

    public void save(User user) {
        logger.debug("Saving user " + user.getUsername());
        em.persist(user);
        logger.info("User saved with id=" + user.getId());
    }

    public User get(Integer id) {
        logger.debug("Retrieving user " + id);
        User user = em.find(User.class, id);
        logger.info("Found user " + user);
        return user;
    }
    
    public void delete(User user){
        logger.debug("Removing user " + user);
        em.remove(user);
        logger.info("User deleted");
    }
}
