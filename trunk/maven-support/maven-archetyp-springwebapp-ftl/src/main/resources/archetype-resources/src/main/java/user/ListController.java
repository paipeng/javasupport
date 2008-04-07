package ${groupId}.user;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/** List and browse Controller for User. */
public class ListController extends AbstractController implements UserConstants {
	private UserDao userDao;

	public void setUserDao(UserDao that) {
		this.userDao = that;
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<User> userList = userDao.findAll();		
		return new ModelAndView(USER_LIST_VIEW, "userList", userList);
	}
}