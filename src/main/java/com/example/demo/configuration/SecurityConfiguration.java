package com.example.demo.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{
	
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	private DataSource dataSource;
	
	@Autowired
    private AccessDeniedHandler accessDeniedHandler;
	
	@Autowired
	public SecurityConfiguration(BCryptPasswordEncoder bCryptPasswordEncoder, DataSource dataSource){
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.dataSource = dataSource;
	}
	
	@Value("${spring.queries.users-query}")
	private String userQuery;
	
	@Value("${spring.queries.roles-query}")
	private String rolesQuery;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth)throws Exception{
		auth.
		jdbcAuthentication()
		.usersByUsernameQuery(userQuery)
		.authoritiesByUsernameQuery(rolesQuery)
		.dataSource(dataSource)
		.passwordEncoder(bCryptPasswordEncoder);
		
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http.
			authorizeRequests()
				.antMatchers("/").permitAll()
				.antMatchers("/registration").permitAll()
				.antMatchers("/admin/**").hasAuthority("ADMIN")
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.loginPage("/login").permitAll()
				.failureUrl("/login?error=true")
				.defaultSuccessUrl("/admin/home")
				.usernameParameter("email").passwordParameter("password")
				.and()
			.logout()
				.permitAll()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/").and()
			.exceptionHandling()
				//.accessDeniedPage("/accessDenied");
				.accessDeniedHandler(accessDeniedHandler);;
		
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception{
		web
		.ignoring()
		.antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
	}

}
