package hello.security;

import static hello.security.SecurityConstants.EXPIRATION_TIME;
import static hello.security.SecurityConstants.HEADER_STRING;
import static hello.security.SecurityConstants.SECRET;
import static hello.security.SecurityConstants.TOKEN_PREFIX;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import hello.domain.AccountCredentials;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
/**
 * 登录拦截器，生成Token，注意request参数默认取的是小写username&password
 * @author Administrator
 *
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private AuthenticationManager authenticationManager;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		setFilterProcessesUrl("/login");
		this.authenticationManager = authenticationManager;
	}

	@Override
	public void setPostOnly(boolean postOnly) {
		// TODO Auto-generated method stub
		super.setPostOnly(false);
	}
	/**
	 * 解析user，发送authenticationManager进行登录验证
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
			throws AuthenticationException {
		//JSON反序列化
		//AccountCredentials creds = new ObjectMapper().readValue(req.getInputStream(), AccountCredentials.class);
		String username=req.getParameter("username");
		String password =req.getParameter("password");
		return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,
				password, new ArrayList<>()));
	}

	/**
	 * 登录验证成功后，生成JWT返回
	 */
	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
			Authentication auth) throws IOException, ServletException {

		String token = Jwts.builder()
				// 保存权限（角色）
				.claim("authorities", "ROLE_ADMIN,AUTH_WRITE")
				// 用户名写入标题
				.setSubject(auth.getPrincipal().toString())
				 // 有效期设置
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				// 签名设置
				.signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
				.compact();
		res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
	}
}