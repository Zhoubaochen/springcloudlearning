package hello.config;

import static hello.security.SecurityConstants.SIGN_UP_URL;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import hello.security.JWTAuthenticationFilter;
import hello.security.JWTAuthorizationFilter;

//@Configuration
@EnableWebSecurity // 开启web层的安全
//@EnableGlobalMethodSecurity(securedEnabled = true) // 开启方法层的安全,可以继承GlobalMethodSecurityConfiguration自定义
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

//	private UserDetailsService userDetailsService;
//	private BCryptPasswordEncoder bCryptPasswordEncoder;
//
//	public WebSecurityConfig(UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
//		this.userDetailsService = userDetailsService;
//		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
//	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		// TODO Auto-generated method stub
		//super.configure(web);
		web.ignoring()// $IgnoredRequestConfigurer配置需要忽略的URL,使用AntPathRequestMatcher匹配HttpRequest地址
		 		.antMatchers("/resources/**", "/static/**","/**/favicon.ico")//匹配所有/resources,/static开头的URL
		//等同.antMatchers("/resources/**").antMatchers("/static/**");
		.antMatchers(HttpMethod.GET,"/freetoget/**")//可以忽略/freetoget的所有GET请求
		
		;


	}

	/**
	 * 配置httpSecurity，相当于<http/>
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.cors().and()
		.csrf().disable()
		.authorizeRequests().antMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()
				// 匹配/login&&HttpMethod=POST的URL
				.antMatchers("/login").permitAll()
				// 匹配/auth,权限检查
				.antMatchers("/auth").hasAuthority("AUTH_WRITE")
				// 角色检查
				.antMatchers("/admin").hasRole("ADMIN").anyRequest().authenticated().and()
		.addFilter(new JWTAuthenticationFilter(authenticationManager()))
		.addFilter(new JWTAuthorizationFilter(authenticationManager()))
		// this disables session creation on Spring Security
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	/**
	 * 配置AuthenticationManager，提供UserDetailsService或AuthenticationProvider实现类
	 */
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		// 使用自定义authenticationProvider身份验证组件
		// auth.authenticationProvider(new CustomAuthenticationProvider());

		// 使用DaoAuthenticationProvider调用自定义userDetailsService身份验证组件
		auth.userDetailsService(userDetailsService());//.passwordEncoder(bCryptPasswordEncoder());
	}

	/**
	 * 使用InMemoryUserDetailsManager把用户数据保存在内存Map，测试用
	 */
	@Bean
	public UserDetailsService userDetailsService() {
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		manager.createUser(User.withUsername("user").password("password").roles("USER").build());
		manager.createUser(User.withUsername("admin").password("password").roles("USER", "ADMIN").build());
		return manager;
	}
	
	/**
	 * 使用InMemoryUserDetailsManager把用户数据保存在内存Map，测试用
	 */
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
