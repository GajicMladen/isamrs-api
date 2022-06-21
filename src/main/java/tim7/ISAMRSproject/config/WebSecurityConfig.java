package tim7.ISAMRSproject.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import tim7.ISAMRSproject.auth.RestAuthenticationEntryPoint;
import tim7.ISAMRSproject.auth.TokenAuthenticationFilter;
import tim7.ISAMRSproject.service.UserService;
import tim7.ISAMRSproject.utils.TokenUtils;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	// Koristi se za hash lozinke koristeci BCrypt algoritam
	@Autowired
	public PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserService userService;
	
	// Handler za vracanje 401 prilikom nevalidne kombinacije user/pass
	@Autowired
	private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
	
	// Authentication manager iz Springa
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception{
		return super.authenticationManagerBean();
	}
	
	// Definisemo nacin utvrdjivanja korisnika pri autentifikaciji
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
			// Definisemo uputstva AuthenticationManager-u:
		
			// 1. koji servis da koristi da izvuce podatke o korisniku koji zeli da se autentifikuje
			// prilikom autentifikacije, AuthenticationManager ce sam pozivati loadUserByUsername() metodu ovog servisa
			.userDetailsService(userService) 
			
			// 2. kroz koji enkoder da provuce lozinku koju je dobio od klijenta u zahtevu 
			// da bi adekvatan hash koji dobije kao rezultat hash algoritma uporedio sa onim koji se nalazi u bazi (posto se u bazi ne cuva plain lozinka)
			.passwordEncoder(passwordEncoder);
	}
	
	@Autowired
	private TokenUtils tokenUtils;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
		.exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint).and()
		.authorizeRequests().antMatchers("/reg/**").permitAll()
		.antMatchers("/login/**").permitAll()
		.antMatchers("/api/users/**").permitAll()
		.antMatchers("/**").permitAll()
		.anyRequest().authenticated().and()
		.cors().and()
		.addFilterBefore(new TokenAuthenticationFilter(tokenUtils, userService), BasicAuthenticationFilter.class);
		
		//http.csrf().disable().authorizeRequests().antMatchers("/adventure/**").permitAll();
		http.csrf().disable();
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception{
		
		web.ignoring().antMatchers(HttpMethod.POST, "/login");
		web.ignoring().antMatchers(HttpMethod.POST, "/reg/signup");
		
		web.ignoring().antMatchers(HttpMethod.GET, "/", "/webjars/**", "/*.html", "favicon.ico", "/**/*.html",
				"/**/*.css", "/**/*.js");
	}
}

