package com.diploma.law.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class AppConfig extends WebSecurityConfigurerAdapter
{

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Qualifier("dataSource")
    @Autowired
    private DataSource    dataSource;

    @Value("${spring.queries.users-query}")
    private String        usersQuery;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.jdbcAuthentication().usersByUsernameQuery("select login, password, status from users  where login=?")
                .authoritiesByUsernameQuery("select u.login, s.title from users u join status s on(u.status = s.id_status) where u.login=?")
                .dataSource(dataSource).passwordEncoder(passwordEncoder);
        ;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception
    {
        httpSecurity.authorizeRequests().antMatchers("/registration").permitAll().antMatchers("/").permitAll().antMatchers("/login")
                .permitAll().antMatchers("/home/admin").hasAuthority("ADMIN").anyRequest().authenticated().and().csrf().disable()
                .formLogin().loginPage("/login").defaultSuccessUrl("/home").usernameParameter("login").passwordParameter("password").and()
                .logout().permitAll();
    }

    @Override
    public void configure(WebSecurity web) throws Exception
    {
        web.ignoring().antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/img/**");
    }

}
