package kr.ac.dankook.cultureApplication.config;

import kr.ac.dankook.cultureApplication.service.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig{

    private final CustomUserDetailService customUserDetailService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http
                .csrf(AbstractHttpConfigurer::disable)
                .headers((headerConfig) ->
                        headerConfig.frameOptions(
                                HeadersConfigurer.FrameOptionsConfig::disable
                        )
                )
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                .requestMatchers(
                                        "/css/**",
                                        "/js/**",
                                        "/assets/**",
                                        "/public/**",
                                        "/auth/**").permitAll()
                                .anyRequest().authenticated()
                )
                .exceptionHandling((exceptionConfig) ->
                        exceptionConfig
                        .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/auth/login"))
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.sendRedirect("/public/except");
                        })
                )
                .formLogin((formLogin) ->
                        formLogin
                                .loginPage("/auth/login")
                                .usernameParameter("email")
                                .passwordParameter("password")
                                .loginProcessingUrl("/auth/login-proc")
                                .failureUrl("/auth/login?error=true")
                                .defaultSuccessUrl("/public/main",true)
                                .permitAll()
                )
                .logout((logoutConfig) ->
                    logoutConfig
                        .logoutUrl("/auth/logout")
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                        .logoutSuccessUrl("/public/main")

                )
                .userDetailsService(customUserDetailService);
        return http.build();
    }

}
