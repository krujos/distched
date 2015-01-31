package distsched.contorller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DummyController {

	@RequestMapping(value = "/hello")
	public String hello() {
		return "Hello";
	}
}
