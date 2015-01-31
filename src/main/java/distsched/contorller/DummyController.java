package distsched.contorller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DummyController {

	@RequestMapping(value = "/")
	public String hello() {
		return "Hello, run 'cf logs <application name>' to see what this app is up to.";
	}
}
