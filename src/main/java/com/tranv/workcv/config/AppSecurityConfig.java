package com.tranv.workcv.config;

import javax.servlet.Filter;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private DataSource securityDataSource;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(securityDataSource)
				.usersByUsernameQuery("select email, password,status from user where email=?")
				.authoritiesByUsernameQuery("select user.email, role.role_name from user, role"
						+ " where email =? and user.role_id = role.id");
		;
	}

//	ROLE_EMPLOYER
//	ROLE_CANDIDATE
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.addFilterBefore(characterEncodingFilter(), CsrfFilter.class);
		http.csrf().disable().authorizeRequests()

				.antMatchers("/", "/sign-up").permitAll().antMatchers("/savefile/**").permitAll()
				.antMatchers("/user/addUser").permitAll()

				.antMatchers("/user/**").hasAnyRole("EMPLOYER", "CANDIDATE")

				.antMatchers("/**").permitAll()
				

				.antMatchers("/resources/**").permitAll()
				
				.antMatchers("/resources/assets/**").permitAll()

				.antMatchers("/css/**").permitAll()

				.antMatchers("/js/**").permitAll()

				.antMatchers("/fonts/**").permitAll()

				.antMatchers("/images/**").permitAll()

				.anyRequest().authenticated()

				.and().formLogin().loginPage("/showFormLogin").loginProcessingUrl("/authenticateTheUser")
				.failureUrl("/login?error=true").usernameParameter("email").passwordParameter("password").permitAll()

				.and().logout().logoutUrl("/auth/logout").logoutSuccessUrl("/auth/logoutSuccessful").permitAll()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")).deleteCookies("JSESSIONID").and()
				.exceptionHandling().accessDeniedPage("/auth/access-denied").and().sessionManagement();

	}
//	security
//	 @Override
//	    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//	        auth.userDetailsService(applicationUserService).passwordEncoder(passwordEncoder());
//	    }
//
//
//	    @Bean
//	    public PasswordEncoder passwordEncoder() {
//	        return new BCryptPasswordEncoder();
//	    }

	@Bean
	public Filter characterEncodingFilter() {
		CharacterEncodingFilter filter = new CharacterEncodingFilter();
		filter.setEncoding("UTF-8");
		filter.setForceEncoding(true);
		return filter;
	}

}
