package us.es.migrolgar2.manhattan.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.DispatcherType;
import us.es.migrolgar2.manhattan.user.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	
//	private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);
	
	@Autowired
    private CustomUserDetailsService customUserDetailsService;
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.cors(Customizer.withDefaults())
//			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(requests -> requests
					.dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR, DispatcherType.INCLUDE).permitAll()
					.requestMatchers("/resources/**","/webjars/**", "/WEB-INF/**").permitAll()
					.requestMatchers("/", "/index").permitAll()
					.requestMatchers("/favicon.ico").permitAll()
					.requestMatchers("/signup").anonymous() 
					.requestMatchers("/game-ws", "/lobby-ws", "/lobby-ws/**", "/topic/**", "/app/**")
							.permitAll() // TODO: Change to actual routes
					// NOTE: Matches to /game/{Any number}/
					.requestMatchers(RegexRequestMatcher.regexMatcher("^/game/[\\d]+$")).authenticated() 
					.requestMatchers(RegexRequestMatcher.regexMatcher("^/lobby/[\\d]+$"),
									 RegexRequestMatcher.regexMatcher("^/lobby/[\\d]+/lobbyReload$"),
									 RegexRequestMatcher.regexMatcher("^/lobby/[\\d]+/start$"))
							.authenticated()
					.requestMatchers("/lobby/list", "/lobby/new", "/lobby/join").authenticated()
					.anyRequest().denyAll() 
			)
//			.authorizeHttpRequests(requests -> requests
//				.dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR, DispatcherType.INCLUDE).permitAll()
//				.requestMatchers(/*"/resources/**","/webjars/**", "/WEB-INF/**", "/error", */
//								 "/favicon.ico", "/signup")
//					.permitAll()
//				.requestMatchers("/", "/game", "/index", "/game/**", "/topic/**", "/app/**", "/game-ws").permitAll()
//				.anyRequest().denyAll()
//				.requestMatchers("/**").permitAll()
				
//			)
//			.authorizeHttpRequests((requests) -> requests
//				.anyRequest().denyAll()
//			)
//			.httpBasic(withDefaults())
//			.formLogin(Customizer.withDefaults())
			.formLogin((form) -> form
				.loginPage("/login")
				.loginProcessingUrl("/login")
				.defaultSuccessUrl("/index")
				.failureUrl("/login?error") 
				.permitAll()
			)
			.logout((logout) -> logout
				.logoutSuccessUrl("/index")
				.permitAll())
			.sessionManagement(Customizer.withDefaults());
		
		return http.build();
	}
	
	@Bean 
	PasswordEncoder passwordEncoder() { 
		 return new BCryptPasswordEncoder();  
	}
	
	@Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

//	@Bean
//	public InMemoryUserDetailsManager userDetailsService() {
//		UserDetails user = User.withUsername("1")
//	            .password(passwordEncoder().encode("1"))
//	            .roles("USER")
//	            .build();
//
//		return new InMemoryUserDetailsManager(user);
//	}
	
//	@Bean
//	public CorsConfigurationSource corsConfigurationSource() {
//		CorsConfiguration configuration = new CorsConfiguration(); 
//		configuration.setAllowedOrigins(Arrays.asList("http://localhost", "https://localhost", "ws://localhost"));
//		configuration.setAllowedOriginPatterns(Arrays.asList());
//		configuration.setAllowedMethods(Arrays.asList("*"));
//		configuration.setAllowedHeaders(Arrays.asList("Authorization"));
//		configuration.setAllowCredentials(true);
//		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		source.registerCorsConfiguration("/**", configuration);
//	   return source;
//	}

}

