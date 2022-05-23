package control.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import control.security.filter.TokenAuthenticationFilter;
import control.security.service.AuthenticationService;

@EnableWebSecurity 
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private TokenAuthenticationFilter tokenAuthenticationFilter;

	@Bean
	public AuthenticationManager getAuthenticationManager() throws Exception {
		return super.authenticationManagerBean();
	}

	//Configurations for authentication
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(authenticationService).passwordEncoder(new BCryptPasswordEncoder());    
	}

	//Configuration for authorization
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
		    .antMatchers("/swagger-ui/**").permitAll()
		    .antMatchers("/v3/api-docs/**").permitAll()
			.antMatchers(HttpMethod.POST, "/auth/login").permitAll()
			.antMatchers(HttpMethod.POST, "/api/user/save").permitAll()
			.antMatchers(HttpMethod.GET, "/status").permitAll()
			.anyRequest().authenticated()
			.and().csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and().addFilterBefore(tokenAuthenticationFilter,UsernamePasswordAuthenticationFilter.class);
	}

	//Configuration for static resources
	@Override
	public void configure(WebSecurity web) throws Exception {

	}
}