package net.containify.starter;

import java.util.Map;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
	private final JdbcClient jdbc;

	public HomeController(JdbcClient jdbc) {
		this.jdbc = jdbc;
	}

	/** Records a visit in Postgres and answers how many there have been. */
	@GetMapping("/")
	public Map<String, Object> home() {
		jdbc.sql("INSERT INTO visits (path) VALUES (?)").param("/").update();
		long visits = jdbc.sql("SELECT count(*) FROM visits").query(Long.class).single();
		return Map.of("message", "Hello from Spring Boot on Containify", "visits", visits);
	}
}
