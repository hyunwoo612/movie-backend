package com.cocoh.movie.config;

import com.cocoh.movie.config.jwt.TokenAuthenticationFilter;
import com.cocoh.movie.config.jwt.TokenExceptionFilter;
import com.cocoh.movie.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final TokenAuthenticationFilter tokenAuthenticationFilter;
    private final TokenExceptionFilter tokenExceptionFilter;

    @Bean
    @Order(1)
    SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {

        // PathPattern이라는 명칭을 사용하여 특정 경로 패턴을 가진 요청을 식별하고 처리하는 기능
        // AntPathRequestMatcher 이전엔 이를 사용 했으나 삭제되었다.
        PathPatternRequestMatcher.Builder mvc = PathPatternRequestMatcher.withDefaults();

        http
                // "/api/로 시작하는 요청만 처리한다" 라는 의미
                .securityMatcher(mvc.matcher("/api/**"))
                // CSRF를 비활성화 하는 명령어로 REST API와 같이 사용자 로그인 폼이 필요 없는 경우에 사용한다.
                .csrf(AbstractHttpConfigurer::disable)
                // 이 메서드를 사용하여 HTTP 요청에 대한 인가 설정을 구성하는 데 사용됩니다.
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                mvc.matcher("/api/login/**"),
                                mvc.matcher("/api/signup"),
                                mvc.matcher("/api/movie/list"),
                                mvc.matcher("/api/movie/{id}"),
                                mvc.matcher("/api/review/**"),
                                mvc.matcher("/api/director/**"),
                                mvc.matcher("/error/**")
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                /* sessionCreationPolicy = 정책상수,
                SessionCreationPolicy.STATELESS = 스프링시큐리티가 생성하지도 않고 기존것을 사용하지도 않음
                JWT 같은 토큰 방식을 쓸때 사용하는 설정 */
                .sessionManagement((sessionManagement) -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(tokenExceptionFilter, TokenAuthenticationFilter.class)
        ;

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer configure() {
        // PathPattern이라는 명칭을 사용하여 특정 경로 패턴을 가진 요청을 식별하고 처리하는 기능
        PathPatternRequestMatcher.Builder mvc = PathPatternRequestMatcher.withDefaults();

        return web -> web.ignoring()
                .requestMatchers(mvc.matcher("/api/movie/list"))
                .requestMatchers(mvc.matcher("/api/movie/{id}"))
                .requestMatchers(mvc.matcher("/api/director/**"))
                .requestMatchers(mvc.matcher("/api/error/**"));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 이렇게 말고 더 짧게 할 수는 없는지 물어보기.
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers( "/images/**").permitAll()
                        .requestMatchers(GET, "/api/movie/list").permitAll()
                        .requestMatchers(GET, "/api/director").permitAll()
                        .requestMatchers(POST, "/api/director").permitAll()
                        .requestMatchers("/api/review/**").permitAll()
                        
                        .requestMatchers(POST, "/api/movie").hasRole("ADMIN")
                        .requestMatchers(PUT, "/api/movie/**").hasRole("ADMIN")
                        .requestMatchers(PATCH, "/api/movie/**").hasRole("ADMIN")


                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**").permitAll()

                        .anyRequest()
                        .authenticated()
                )
//                .formLogin(formLogin -> formLogin
//                        .loginPage("/login")
//                        .defaultSuccessUrl("/movie/list"))
//                .logout(logout -> logout
//                        .logoutUrl("/logout")
//                        .logoutSuccessUrl("/movie")
//                        .invalidateHttpSession(true))
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailService userDetailService) throws Exception {
        // UserDetailService 및 PasswordEncoder를 사용하여 사용자 아이디와 암호를 인증하는 AuthenticationProvider 구현
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailService);
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        return new ProviderManager(provider);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Configuration
    public static class WebConfig implements WebMvcConfigurer {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                    .allowedOriginPatterns("*")
                    .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                    .allowedHeaders("*")
                    .allowCredentials(true);
        }
    }


}
