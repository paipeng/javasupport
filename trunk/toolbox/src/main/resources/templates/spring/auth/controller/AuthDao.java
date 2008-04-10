package ${packageName}.auth;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ${packageName}.user.User;
import ${packageName}.user.UserDao;

/** DAO for User. */
@Transactional
public class AuthDao extends UserDao {	
	public User authenticate(String username, String plainPassword){
        logger.debug("Searching for user with " + username);
        List<User> users = em.createQuery("select u from User u where u.username=:un").setParameter("un", username).getResultList();
        
        if(users.size() == 0)
            throw new PasswordNotMatchException();
            
        logger.debug("Found user, checking password");
        User foundUser = users.get(0);
        if(!foundUser.getPassword().equals(plainPassword))
            throw new PasswordNotMatchException();
            
        logger.info("User "+ foundUser.getId()+" authenticated.");
        return foundUser;
    }
}

