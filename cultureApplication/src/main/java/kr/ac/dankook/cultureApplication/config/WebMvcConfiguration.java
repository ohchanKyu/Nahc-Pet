package kr.ac.dankook.cultureApplication.config;

import kr.ac.dankook.cultureApplication.interceptor.PostInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final PostInterceptor postInterceptor;

    @Bean
    public MappingJackson2JsonView jsonView(){
        return new MappingJackson2JsonView();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(postInterceptor)
                .addPathPatterns("/public/board/type/**", "/private/board/me/**");
    }

}
