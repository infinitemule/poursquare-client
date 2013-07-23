package com.infinitemule.hopperhack.poursquare.client

import org.springframework.context.annotation.{Bean,
                                               Configuration, 
                                               ComponentScan, 
                                               Profile}

import org.springframework.web.servlet.config.annotation.{EnableWebMvc,
                                                          WebMvcConfigurerAdapter,
                                                          ResourceHandlerRegistry}

import org.springframework.web.servlet.view.InternalResourceViewResolver

import org.springframework.data.authentication.UserCredentials

import org.springframework.data.mongodb.core.{MongoFactoryBean,
                                              MongoOperations,
                                              MongoTemplate,
                                              SimpleMongoDbFactory}

import org.springframework.data.mongodb.MongoDbFactory

import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor

import java.nio.charset.Charset

import com.mongodb.Mongo


/**
 * Configuration class for Spring MVC
 */
@EnableWebMvc
@ComponentScan(basePackages=Array("com.infinitemule.hopperhack.poursquare.client"))
@Configuration
class PoursquareClientConfig extends WebMvcConfigurerAdapter {

  // These URLs will just forward onto static content
  
  override def addResourceHandlers(registry: ResourceHandlerRegistry) = {
    registry.addResourceHandler("/bootstrap/**").addResourceLocations("/bootstrap/")
    registry.addResourceHandler("/img/**")      .addResourceLocations("/img/")
    registry.addResourceHandler("/js/**")       .addResourceLocations("/js/")
  }
  
  
  @Bean(name=Array("viewResolverJsp"))
  def getInternalResourceViewResolverJsp() = {
    
    val resolver = new InternalResourceViewResolver()
    resolver.setViewClass(classOf[org.springframework.web.servlet.view.JstlView])
    resolver.setPrefix("/WEB-INF/jsp/")
    resolver.setSuffix(".jsp")
    resolver.setOrder(2)
    resolver
  }

  
  @Bean(name=Array("viewResolverCss"))
  def getInternalResourceViewResolverCss() = {
    
    val resolver = new InternalResourceViewResolver()
    resolver.setViewClass(classOf[org.springframework.web.servlet.view.JstlView])
    resolver.setPrefix("/WEB-INF/jsp/")
    resolver.setSuffix(".jsp")
    resolver.setViewNames(Array("css/*"))
    resolver.setContentType("text/css")
    resolver.setOrder(1)
    resolver
    
  }
  
}


/**
 * Configuration for client services
 */
@Configuration
@Profile(Array("dev"))
@ComponentScan(basePackages=Array("com.infinitemule.hopperhack.poursquare.service",
                                  "com.infinitemule.hopperhack.poursquare.data"))
class PoursquareCoreConfig {
  
  
  @Bean 
  def mongoTemplate(): MongoOperations = {
    new MongoTemplate(mongoFactory()) 
  }
  
  @Bean 
  def mongoFactory(): MongoDbFactory = {
    val host = "localhost"
    
    val mongo = new Mongo(host)
    val databaseName = "test"
    
    val mongoDbFactory = new SimpleMongoDbFactory(mongo, databaseName)
    mongoDbFactory        
  }  
}


/**
 * Configuration for deploying to OpenShift
 */
@Configuration
@Profile(Array("openshift"))
@ComponentScan(basePackages=Array("com.infinitemule.hopperhack.poursquare.service",
                                  "com.infinitemule.hopperhack.poursquare.data"))
class PoursquareOpenShiftCoreConfig {
  
  @Bean 
  def mongoTemplate(): MongoOperations = {
    new MongoTemplate(mongoFactory())
 
  }

  @Bean 
  def mongoFactory(): MongoDbFactory = {
    
    val host = System.getenv("OPENSHIFT_MONGODB_DB_HOST")
    val port = System.getenv("OPENSHIFT_MONGODB_DB_PORT").toInt
    
    val username = System.getenv("OPENSHIFT_MONGODB_DB_USERNAME")
    val password = System.getenv("OPENSHIFT_MONGODB_DB_PASSWORD")
        
    val userCredentials = new UserCredentials(username,password)
    
    val mongo = new Mongo(host, port)
    val databaseName = System.getenv("OPENSHIFT_APP_NAME")
    
    val mongoDbFactory = new SimpleMongoDbFactory(mongo, databaseName, userCredentials)
    
    mongoDbFactory
    
  }
    
}

