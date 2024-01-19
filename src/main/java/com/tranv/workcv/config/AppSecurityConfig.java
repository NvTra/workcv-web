package com.tranv.workcv.config;

import javax.servlet.Filter;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig {
	@Autowired
	private DataSource securityDataSource;

	@Autowired
	public void configureAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	public UserDetailsService userDetailsService() {
		JdbcDaoImpl userDetailsManager = new JdbcDaoImpl();
		userDetailsManager.setDataSource(securityDataSource);
		userDetailsManager.setUsersByUsernameQuery("select email, password, isActive from user where email=?");
		userDetailsManager.setAuthoritiesByUsernameQuery(
				"select user.email, role.role_name from user, role " + "where email =? and user.role_id = role.id");
		return userDetailsManager;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

//
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.jdbcAuthentication().dataSource(securityDataSource)
//				.usersByUsernameQuery("select email, password,status from user where email=?")
//				.authoritiesByUsernameQuery("select user.email, role.role_name from user, role"
//						+ " where email =? and user.role_id = role.id");
//		;
//	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.addFilterBefore(characterEncodingFilter(), CsrfFilter.class);

		http.csrf().disable().authorizeRequests(configuarer -> configuarer

				.antMatchers("/", "/sign-up").permitAll()

				.antMatchers("/auth/**", "/recruitment/**", "/apply", "/company", "/recruitment", "/save-job")
				.permitAll()

				.antMatchers("/savefile/**").permitAll()

				.antMatchers("/user/addUser").permitAll()
				
				.antMatchers("/company/detail-company").permitAll()
				
				.antMatchers("/user/**").hasAnyRole("EMPLOYER", "CANDIDATE")

				.antMatchers("/admin/**").hasRole("ADMIN")

//				.antMatchers("/**").permitAll()

				.antMatchers("/resources/**").permitAll()

				.antMatchers("/resources/assets/**").permitAll()

				.antMatchers("/css/**").permitAll()

				.antMatchers("/js/**").permitAll()

				.antMatchers("/fonts/**").permitAll()

				.antMatchers("/images/**").permitAll()

				.anyRequest().authenticated())

				.formLogin(configuarer -> configuarer

						.loginPage("/auth/login").loginProcessingUrl("/authenticateTheUser")
						.failureUrl("/auth/login?error=true").usernameParameter("email").passwordParameter("password")
						.permitAll())

				.logout(configuarer -> configuarer.logoutUrl("/auth/logout").logoutSuccessUrl("/").permitAll()
						.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")).deleteCookies("JSESSIONID"))

				.exceptionHandling(configuarer -> configuarer.accessDeniedPage("/auth/access-denied"));

		return http.build();

	}
	/*
	 * @Override protected void configure(HttpSecurity http) throws Exception {
	 * http.addFilterBefore(characterEncodingFilter(), CsrfFilter.class);
	 * http.csrf().disable().authorizeRequests()
	 * 
	 * .antMatchers("/",
	 * "/sign-up").permitAll().antMatchers("/savefile/**").permitAll()
	 * .antMatchers("/user/addUser").permitAll()
	 * 
	 * .antMatchers("/user/**").hasAnyRole("EMPLOYER", "CANDIDATE")
	 * 
	 * .antMatchers("/**").permitAll()
	 * 
	 * .antMatchers("/resources/**").permitAll()
	 * 
	 * .antMatchers("/resources/assets/**").permitAll()
	 * 
	 * .antMatchers("/css/**").permitAll()
	 * 
	 * .antMatchers("/js/**").permitAll()
	 * 
	 * .antMatchers("/fonts/**").permitAll()
	 * 
	 * .antMatchers("/images/**").permitAll()
	 * 
	 * .anyRequest().authenticated()
	 * 
	 * .and().formLogin().loginPage("/showFormLogin").loginProcessingUrl(
	 * "/authenticateTheUser")
	 * .failureUrl("/auth/login?error=true").usernameParameter("email").
	 * passwordParameter("password") .permitAll()
	 * 
	 * .and().logout().logoutUrl("/auth/logout").logoutSuccessUrl("/").permitAll()
	 * .logoutRequestMatcher(new AntPathRequestMatcher("/logout",
	 * "GET")).deleteCookies("JSESSIONID").and()
	 * .exceptionHandling().accessDeniedPage("/auth/access-denied").and().
	 * sessionManagement();
	 * 
	 * }
	 */

	@Bean
	public Filter characterEncodingFilter() {
		CharacterEncodingFilter filter = new CharacterEncodingFilter();
		filter.setEncoding("UTF-8");
		filter.setForceEncoding(true);
		return filter;
	}

}
