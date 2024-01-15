package us.es.migrolgar2.manhattan.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("http://localhost", "https://localhost", "ws://localhost")
				.allowedMethods("*") 
				.allowedHeaders("Authentication")
				.allowCredentials(true)
				.maxAge(3600);
		
	}
	
}
