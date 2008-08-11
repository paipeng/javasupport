package ${project.packageName};

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
	@RequestMapping("/main")
    public ModelMap main(){
        return new ModelMap("message", "Welcome guest! Today date is " + new Date());
    }
	
	@RequestMapping("/about")
	public void about(){
	}
}

