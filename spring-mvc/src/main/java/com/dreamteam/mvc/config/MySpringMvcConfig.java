package com.dreamteam.mvc.config;

import com.dreamteam.mvc.interceptors.AuthenticationInterceptor;
import com.dreamteam.sampledata.SampleDataConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.validation.Validator;

@EnableWebMvc
@Configuration
@Import({SampleDataConfiguration.class})
@ComponentScan(basePackages = "com.dreamteam.mvc.controllers")
public class MySpringMvcConfig extends WebMvcConfigurerAdapter {

	//two weeks - in seconds
	public static final int COOKIE_MAX_AGE = 14 * 24 * 60 * 60;

	//Interceptors
	@Bean
    public AuthenticationInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor();
    }

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		return new LocaleChangeInterceptor();
	}

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor());
		registry.addInterceptor(localeChangeInterceptor());
	}

	/**
	 * Enables default Tomcat servlet that serves static files.
	 */
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	/**
	 * Provides mapping from view names to JSP pages in WEB-INF/jsp directory.
	 */
	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/jsp/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}

	/**
	 * Provides JSR-303 Validator.
	 */
	@Bean
	public Validator validator() {
		return new LocalValidatorFactoryBean();
	}

	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("TigrMessages");
		messageSource.setUseCodeAsDefaultMessage(true);
		return messageSource;
	}

	@Bean("localeResolver")
	public CookieLocaleResolver cookieLocaleResolver() {
		//do NOT set default locale - spring will take default from request
		CookieLocaleResolver localeResolver = new CookieLocaleResolver();
		localeResolver.setCookiePath("/pa165/");
		localeResolver.setCookieMaxAge(COOKIE_MAX_AGE);
		localeResolver.setCookieName("locale");
		return localeResolver;
	}

}