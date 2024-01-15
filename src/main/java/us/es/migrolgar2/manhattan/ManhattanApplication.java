package us.es.migrolgar2.manhattan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

@SpringBootApplication
public class ManhattanApplication {
	public static void main(String[] args) {
		SpringApplication.run(ManhattanApplication.class, args);
	}
	
}
