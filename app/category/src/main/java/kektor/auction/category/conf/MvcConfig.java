package kektor.auction.category.conf;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
//@EnableTransactionManagement
public class MvcConfig implements WebMvcConfigurer {


//        @Bean
////    @Primary
//    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
//    public static LocalValidatorFactoryBean defaultValidator(ApplicationContext applicationContext,
//                                                             ObjectProvider<ValidationConfigurationCustomizer> customizers) {
//        LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
//        factoryBean.setConfigurationInitializer((configuration) -> customizers.orderedStream()
//                .forEach((customizer) -> customizer.customize(configuration)));
//        MessageInterpolatorFactory interpolatorFactory = new MessageInterpolatorFactory(applicationContext);
//        factoryBean.setMessageInterpolator(interpolatorFactory.getObject());
//        return factoryBean;
//    }

//    @Override
//    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
//        configurer.setDefaultTimeout()
//        configurer.registerCallableInterceptors();
//        configurer.
//        WebMvcConfigurer.super.configureAsyncSupport(configurer);
//    }

//    @Bean
//    public SpelAwareProxyProjectionFactory projectionFactory() {
//        return new SpelAwareProxyProjectionFactory();
//    }
//    @Bean
//    public static PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
//        return new PersistenceExceptionTranslationPostProcessor();
//    }

//    @Bean
//    public DataSource dataSource(){
//        HikariConfig hikariConfig = new HikariConfig();
//        hikariConfig.setJdbcUrl("jdbc:postgresql://localhost:5432/dbtest");
//        hikariConfig.setUsername("postgres");
//        hikariConfig.setPassword("13061306");
//        hikariConfig.setDriverClassName("org.postgresql.Driver");
//        hikariConfig.setConnectionTimeout(20000);
//        hikariConfig.setMaximumPoolSize(10);
//        hikariConfig.setMaxLifetime(900000);
//
//        return new HikariDataSource(hikariConfig);
//    }

    //    @Bean
//    public DataSource dataSource() {
//        BasicDataSource dataSource = new BasicDataSource();
//        dataSource.setDriverClassName("org.h2.Driver");
//        dataSource.setUrl("jdbc:h2:mem:db;DB_CLOSE_DELAY=-1");
//        dataSource.setUsername("sa");
//        dataSource.setPassword("sa");
//
//        return dataSource;
//    }

//    @Bean
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
//        LocalContainerEntityManagerFactoryBean em
//                = new LocalContainerEntityManagerFactoryBean();
//        em.setDataSource(dataSource());
//        em.setPackagesToScan("com.example.book.model");
//
//        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//        em.setJpaVendorAdapter(vendorAdapter);
//        em.setJpaProperties(newProperties());
//
//        return em;
//    }

//    @Bean
//    TransactionManager transactionManager(){
//        JpaTransactionManager transactionManager = new JpaTransactionManager();
//        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
//
//        return transactionManager;
//    }

//    @Bean
//    public LocalSessionFactoryBean sessionFactory() {
//
//        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
//        sessionFactory.setDataSource(dataSource());
//        sessionFactory.setPackagesToScan("com.example.demo.book.model");
//        sessionFactory.setHibernateProperties(newProperties());
//
//        return sessionFactory;
//    }
//
//
//    @Bean
//    public PlatformTransactionManager hibernateTransactionManager() {
//        HibernateTransactionManager transactionManager
//                = new HibernateTransactionManager();
//        transactionManager.setSessionFactory(sessionFactory().getObject());
//        return transactionManager;
//    }
//
//
//    private Properties additionalProperties() {
//        Properties properties = new Properties();
////        properties.put("hibernate.hbm2ddl.auto", "create-drop");
//        properties.put("hibernate.hbm2ddl.auto", "create-drop");
//        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
//        properties.setProperty("hibernate.format_sql", "true");
//        properties.setProperty("hibernate.show_sql", "true");
//        properties.setProperty("hibernate.use_sql_comments", "true");
//        return properties;
//    }
//
//
//    private Properties newProperties() {
//        Properties properties = new Properties();
////        properties.put("hibernate.hbm2ddl.auto", "create-drop");
//        properties.put("jakarta.persistence.schema-generation.database.action", "drop-and-create");
//        properties.put("jakarta.persistence.schema-generation.create-source", "metadata");
//        properties.put("jakarta.persistence.schema-generation.drop-source", "metadata");
////        properties.put("jakarta.persistence.schema-generation.create-script-source", "MyCreate.sql");
////        properties.put("jakarta.persistence.schema-generation.drop-script-source", "MyDrop.sql");
//
//        properties.put("jakarta.persistence.schema-generation.scripts.action", "drop-and-create");
//        properties.put("jakarta.persistence.schema-generation.scripts.create-target", "GeneratedCreate.sql");
//        properties.put("jakarta.persistence.schema-generation.scripts.drop-target", "GeneratedDrop.sql");
////        properties.put("jakarta.persistence.sql-load-script-source", "runScriptAfterAllTableCreated.sql");
//
//
//        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
//        properties.setProperty("hibernate.format_sql", "true");
//        properties.setProperty("hibernate.show_sql", "true");
//        properties.setProperty("hibernate.use_sql_comments", "true");
//
//
//        return properties;
//
//    }


//    @Bean
//    public ModelMapper getMapper() {
//        ModelMapper modelMapper = new ModelMapper();
//        modelMapper.getConfiguration()
//                .setFieldAccessLevel(PRIVATE)
//                .setMatchingStrategy(MatchingStrategies.STRICT)
//                .setFieldMatchingEnabled(true)
//                .setSkipNullEnabled(true)
//        return modelMapper;
//    }

//    @Bean
//    A regA(){
////        return new A();
//    }


///MessageSourceAutoConfiguration - i18n------------------------------

//    spring:
//        messages:
//            basename: localization/messages
//            use-code-as-default-message: false
//            fallback-to-system-locale: false
//            encoding: UTF-8
//            always-use-message-format: false
//            cache-duration: -1


//    @Bean
//    public MessageSource messageSource(MessageSourceProperties properties) {
//        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
//        if (StringUtils.hasText(properties.getBasename())) {
//            messageSource.setBasenames(StringUtils
//                    .commaDelimitedListToStringArray(StringUtils.trimAllWhitespace(properties.getBasename())));
//        }
//        if (properties.getEncoding() != null) {
//            messageSource.setDefaultEncoding(properties.getEncoding().name());
//        }
//        messageSource.setFallbackToSystemLocale(properties.isFallbackToSystemLocale());
//        Duration cacheDuration = properties.getCacheDuration();
//        if (cacheDuration != null) {
//            messageSource.setCacheMillis(cacheDuration.toMillis());
//        }
//        messageSource.setAlwaysUseMessageFormat(properties.isAlwaysUseMessageFormat());
//        messageSource.setUseCodeAsDefaultMessage(properties.isUseCodeAsDefaultMessage());
//        return messageSource;
//    }


    //------------------------------------------------------------------------------
//-----TaskExecutionAutoConfiguration-----------------------------------------------
//    spring
//      task:
//          execution:
//          threadNamePrefix: Auction-Async-Thread-
//           pool:
//               core-size: 8
//               max-size: 12
//               allow-core-thread-timeout: true
//               keep-alive: PT30S
//#              queue-capacity:
//           shutdown:
//              await-termination: true
//              await-termination-period: PT30S  #60 sec example - P3DT5H40M30S = 3Days, 5Hours, 40 Minutes and 30 Seconds
//

//
//    @Bean(name = {APPLICATION_TASK_EXECUTOR_BEAN_NAME,
//            AsyncAnnotationBeanPostProcessor.DEFAULT_TASK_EXECUTOR_BEAN_NAME})
//    public ThreadPoolTaskExecutor applicationTaskExecutor(TaskExecutionProperties properties,
//                                                          ObjectProvider<TaskExecutorCustomizer> taskExecutorCustomizers,
//                                                          ObjectProvider<TaskDecorator> taskDecorator) {
//        TaskExecutionProperties.Pool pool = properties.getPool();
//        TaskExecutionProperties.Shutdown shutdown = properties.getShutdown();
//        return new TaskExecutorBuilder()
//                .queueCapacity(pool.getQueueCapacity())
//                .corePoolSize(pool.getCoreSize())
//                .maxPoolSize(pool.getMaxSize())
//                .allowCoreThreadTimeOut(pool.isAllowCoreThreadTimeout())
//                .keepAlive(pool.getKeepAlive())
//                .awaitTermination(shutdown.isAwaitTermination())
//                .awaitTerminationPeriod(shutdown.getAwaitTerminationPeriod())
//                .threadNamePrefix(properties.getThreadNamePrefix())
//                .customizers(taskExecutorCustomizers.orderedStream()::iterator)
//                .taskDecorator(taskDecorator.getIfUnique())
//                .build();
//    }

//    @Bean
//    TaskDecorator composedTaskDecorator() {
//        return runnable -> securityTaskDecorator()
//                .andThen(loggingTaskDecorator())
//                .apply(runnable);
//    }
//
//    Function<Runnable, Runnable> loggingTaskDecorator() {
//        return runnable -> {
//            System.out.println("LOGGING SET------------------------");
//            Map<String, String> contextMap = MDC.getCopyOfContextMap();
//            return () -> {
//                try {
//                    MDC.setContextMap(contextMap);
//                    runnable.run();
//                } finally {
//                    MDC.clear();
//                }
//            };
//        };
//    }
//
//    Function<Runnable, Runnable> securityTaskDecorator() {
//        return delegate -> {
//            System.out.println("SECURITY APPLIED");
//            return new DelegatingSecurityContextRunnable(delegate);
//        };
//    }

    //// можем также конфигурировать через AsyncConfigurer
//    @Configuration
//    @EnableAsync
//    public class AsyncConfig implements AsyncConfigurer {

//-------------------------------------------------------------------------------------


//    @Bean
////    @Primary
//    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
//    public static LocalValidatorFactoryBean defaultValidator(ApplicationContext applicationContext,
//                                                             ObjectProvider<ValidationConfigurationCustomizer> customizers) {
//        LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
//        factoryBean.setConfigurationInitializer((configuration) -> customizers.orderedStream()
//                .forEach((customizer) -> customizer.customize(configuration)));
//        MessageInterpolatorFactory interpolatorFactory = new MessageInterpolatorFactory(applicationContext);
//        factoryBean.setMessageInterpolator(interpolatorFactory.getObject());
//        return factoryBean;
//    }


//    @Bean
//    public static MethodValidationPostProcessor methodValidationPostProcessor(Environment environment,
//                                                                              ObjectProvider<Validator> validator,
//                                                                              ObjectProvider<MethodValidationExcludeFilter> excludeFilters) {
//        FilteredMethodValidationPostProcessor processor = new FilteredMethodValidationPostProcessor(
//                excludeFilters.orderedStream());
//        boolean proxyTargetClass = environment.getProperty("spring.aop.proxy-target-class", Boolean.class, true);
//        processor.setProxyTargetClass(proxyTargetClass);
//        processor.setValidatorProvider(validator);
//        return processor;
//    }

//    In Java configuration, you can customize the global Validator instance,
//    as the following example shows:

//    @Configuration
//    @EnableWebMvc
//    public class WebConfig implements WebMvcConfigurer {
//
//        @Override
//        public Validator getValidator() {
//            // ...
//        }
//    }


    //--------------------------------------------------------------------------------------------

// # --- org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration-----
//   server:
//       error:
//       path: /error
//       whitelabel:
//           enabled: false
//       include-binding-errors: always
//       include-exception: true
//       include-message: on-param
//       include-stacktrace: on-param
//    --------------------------------------------------------------------------------


// -- JacksonAutoConfiguration--------------------------------------------------
//
//    spring:
//        jackson:
//            constructor-detector:
//            date-format:
//            default-leniency:
//            default-property-inclusion: non_null
//            locale:
//            property-naming-strategy:
//            time-zone:
//            visibility:
//                GETTER: NON_PRIVATE
//                //other mappings
//            mapper:
//               default-view-inclusion: true
//                //mapper features
//            deserialization:
//                //deserialization features
//            serialization:
//                //serialization features
//            generator:
//                //generator features
//            parser:
//                //parser features


//    @Bean
//    @Primary
//    ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
//        return builder.createXmlMapper(false).build();
//    }

    //------------------------------------------------------------------------------


    //-----ServletWebServerFactoryAutoConfiguration-----------------------------
/*
    server:
        address:0.0.0.0
        port:8080
        server-header:TestServer
        forward-headers-strategy:NATIVE|FRAMEWORK|NONE
        max-http-request-header-size:8KB
        shutdown:graceful|immediate
        compression:
            enabled:true
            min-response-size:50B
            mime-types:text/html,text/xml,text/plain,text/css,text/javascript,application/javascript,application/json,application/xml
            excluded-user-agents:
        http2:
            enabled:true
        ssl:
            enabled:
            ...
        servlet:
            application-display-name:AuctionApplicationDisplayName
            context-path:
            context-parameters.testParam:testVal
            register-default-servlet:false
            encoding:
                enabled:true
                charset:UTF-8
                force:true
                force-request:true
                force-response:true
                mapping .*:
            jsp:
                registered:false
                class-name:org.apache.jasper.servlet.JspServlet
                init-parameters.testParam:testValue
            session:
                cookie:
                    domain:
                    path:
                    name:
                    comment:
                    http-only:
                    max-age:
                    same-site:
                    secure:
                timeout:
                persistent:true
                session-store-directory:
                tracking-modes:COOKIE |URL |SSL
*/


//    @Bean
//
//    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> customServerFactoryCustomizer(ServerProperties serverProperties,
//                                                                                                         ObjectProvider<WebListenerRegistrar> webListenerRegistrars,
//                                                                                                         ObjectProvider<CookieSameSiteSupplier> cookieSameSiteSuppliers,
//                                                                                                         ObjectProvider<SslBundles> sslBundles) {
//
//        return serverFactory -> {
//            PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
//            map.from(serverProperties::getPort).to(serverFactory::setPort);
//            map.from(serverProperties::getAddress).to(serverFactory::setAddress);
//            map.from(serverProperties.getServlet()::getContextPath).to(serverFactory::setContextPath);
//            map.from(serverProperties.getServlet()::getApplicationDisplayName).to(serverFactory::setDisplayName);
//            map.from(serverProperties.getServlet()::isRegisterDefaultServlet).to(serverFactory::setRegisterDefaultServlet);
//            map.from(serverProperties.getServlet()::getSession).to(serverFactory::setSession);
//            map.from(serverProperties::getSsl).to(serverFactory::setSsl);
//            map.from(serverProperties.getServlet()::getJsp).to(serverFactory::setJsp);
//            map.from(serverProperties::getCompression).to(serverFactory::setCompression);
//            map.from(serverProperties::getHttp2).to(serverFactory::setHttp2);
//            map.from(serverProperties::getServerHeader).to(serverFactory::setServerHeader);
//            map.from(serverProperties.getServlet()::getContextParameters).to(serverFactory::setInitParameters);
//            map.from(serverProperties.getShutdown()).to(serverFactory::setShutdown);
////            map.from(() -> this.sslBundles).to(serverFactory::setSslBundles);
////            map.from(() -> this.cookieSameSiteSuppliers).whenNot(CollectionUtils::isEmpty)
////                    .to(serverFactory::setCookieSameSiteSuppliers);
////            this.webListenerRegistrars.forEach((registrar) -> registrar.register(serverFactory));
//        };

    //Или так. Но нужно быть уверенными, что property установлены и не null

//        return serverFactory -> {
//            serverFactory.setPort(serverProperties.getPort());
//            serverFactory.setAddress(serverProperties.getAddress());
//            serverFactory.setContextPath(serverProperties.getServlet().getContextPath());
//            serverFactory.setDisplayName(serverProperties.getServlet().getApplicationDisplayName());
//            serverFactory.setRegisterDefaultServlet(serverProperties.getServlet().isRegisterDefaultServlet());
//            serverFactory.setSession(serverProperties.getServlet().getSession());
//            serverFactory.setSsl(serverProperties.getSsl());
//            serverFactory.setJsp(serverProperties.getServlet().getJsp());
//            serverFactory.setCompression(serverProperties.getCompression());
//            serverFactory.setHttp2(serverProperties.getHttp2());
//            serverFactory.setServerHeader(serverProperties.getServerHeader());
//            serverFactory.setInitParameters(serverProperties.getServlet().getContextParameters());
//            serverFactory.setShutdown(serverProperties.getShutdown());
////            map.from(() -> this.sslBundles).to(factory::setSslBundles);
////            map.from(() -> this.cookieSameSiteSuppliers).whenNot(CollectionUtils::isEmpty)
////                    .to(factory::setCookieSameSiteSuppliers);
////            this.webListenerRegistrars.forEach((registrar) -> registrar.register(factory));
//
//        };
//
//}


//    register @jakarta.servlet.annotation.WebListener.
//    @Bean
//    WebListenerRegistrar customWebListenerRegistrar(){
//        return serverFactory -> serverFactory.addWebListeners("webListenerClassName");
//    }

//    @Bean
//    CookieSameSiteSupplier customCookieSameSiteSupplier(){
//        return CookieSameSiteSupplier.ofLax().whenHasName("testCookie");
//    }

//    @Bean
//    SslBundles customSslBundles(){
//        return bundleName -> ...
//    }

//    server
//        tomcat:
//            additional-tld-skip-patterns:
//            mbeanregistry:
//                enabled: false
//            redirect-context-root: true
//            use-relative-redirects: false
//   ...остальные смотри ниже

//    TomcatServletWebServerFactory extends ConfigurableServletWebServerFactory, поэтому этот customizer может настраивать
//    все настройки из WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> + tomcat specific
//    Есть и server-specific customizer для других серверов Netty,Jetty,Undertow

//    TomcatServletWebServerFactory наследуется от ConfigurableTomcatWebServerFactory(смотри ниже) и от
//    ConfigurableServletWebServerFactory(смотри выше). От ConfigurableTomcatWebServerFactory также наследуется
//    TomcatReactiveWebServerFactory. В общем через ConfigurableTomcatWebServerFactory - настройки от ConfigurableServletWebServerFactory
//    + настройки специфичные для томката(но общие для сервлет и реактивного сервера). А в TomcatServletWebServerFactory можно конфигурировать
//    настройки от ConfigurableServletWebServerFactory + специфичные для tomcat servlet server

//    @Bean
//    WebServerFactoryCustomizer<TomcatServletWebServerFactory> customTomcatServletWebServerFactoryCustomizer(ServerProperties serverProperties) {
//        return serverFactory -> {
//            ServerProperties.Tomcat tomcatProperties = serverProperties.getTomcat();
//            if (!ObjectUtils.isEmpty(tomcatProperties.getAdditionalTldSkipPatterns())) {
//                serverFactory.getTldSkipPatterns().addAll(tomcatProperties.getAdditionalTldSkipPatterns());
//            }
//            if (tomcatProperties.getRedirectContextRoot() != null) {
//                serverFactory.addContextCustomizers((context) -> context.setMapperContextRootRedirectEnabled(tomcatProperties.getRedirectContextRoot()));
//            }
//            serverFactory.addContextCustomizers((context) -> context.setUseRelativeRedirects(tomcatProperties.isUseRelativeRedirects()));
//            serverFactory.setDisableMBeanRegistry(!tomcatProperties.getMbeanregistry().isEnabled());
//        };
//
//    }

//    Также код из ServletWebServerFactoryConfiguration.EmbeddedTomcat
//    позволяет нам делать более специфичные кастомизации, а не юзать общий
//    WebServerFactoryCustomizer<TomcatServletWebServerFactory> (через него тоже
//    можно применить эти настройки например serverFactory.addContextCustomizers(...))
//
//    -TomcatConnectorCustomizer
//    -TomcatContextCustomizer
//    -TomcatProtocolHandlerCustomizer<?>

//    @Bean
//    TomcatConnectorCustomizer customTomcatConnectorCustomizer(){
//        return connector -> connector.setAsyncTimeout(22);
//    }
//
//    //ServletContext customizations
//    @Bean
//    TomcatContextCustomizer customTomcatContextCustomizer(){
//        return context -> context.setUseRelativeRedirects(true);
//    }
//
//    TomcatProtocolHandlerCustomizer<?> customTomcatProtocolHandlerCustomizer(){
//        return protocolHandler -> protocolHandler.addSslHostConfig();
//    }

//---------------------------------------------

//--------EmbeddedWebServerFactoryCustomizerAutoConfiguration-----------------------------

//    server:
//        tomcat:
//            accept-count: 100
////            additional-tld-skip-patterns: (устанавливаются в ServletWebServerFactoryAutoConfiguration)
//            background-processor-delay:
//            basedir: /someDir
//            connection-timeout: -1
//            keep-alive-timeout: -1
//            max-connections: 8192
//            max-http-form-post-size: 2MB
//            max-http-response-header-size: 8KB
//            max-keep-alive-requests: 100
//            max-swallow-size: 2MB
////            mbeanregistry: (устанавливаются в ServletWebServerFactoryAutoConfiguration)
////                enabled: false
//            processor-cache: 200
////            redirect-context-root: true (устанавливаются в ServletWebServerFactoryAutoConfiguration)
//            relaxed-path-chars:
//            relaxed-query-chars:
//            uri-encoding: UTF-8
////            use-relative-redirects: false (устанавливаются в ServletWebServerFactoryAutoConfiguration)
//            remoteip:
//                host-header: X-Forwarded-Host
//                port-header: X-Forwarded-Port
//                protocol-header: X-Forwarded-Proto
//                protocol-header-https-value: https
//                remote-ip-header: X-FORWARDED-FOR
//                internal-proxies:
//                trusted-proxies:
//            resource:
//                allow-caching: true
//                cache-ttl:
//            threads:
//                max: 200
//                min-spare: 10
//            accesslog:
//                enabled: true
//                buffered: true
//                encoding: UTF-8
//                locale:
//                directory: logs
//                max-days: -1
//                pattern: common
//                prefix: access_log
//                suffix: .log
//                rename-on-rotate: false
//                rotate: true
//                file-date-format: yyyy-MM-dd
//                request-attributes-enabled: false
//                check-exists: false
//                condition-if:
//                condition-unless:
//                ipv6-canonical: false
//

//Смотри EmbeddedWebServerFactoryCustomizerAutoConfiguration

//    TomcatServletWebServerFactory(смотри выше) наследуется от ConfigurableTomcatWebServerFactory  и от
//    ConfigurableServletWebServerFactory(смотри выше). От ConfigurableTomcatWebServerFactory также наследуется
//    TomcatReactiveWebServerFactory. В общем через ConfigurableTomcatWebServerFactory - настройки от ConfigurableServletWebServerFactory
//    + настройки специфичные для томката(но общие для сервлет и реактивного сервера). А в TomcatServletWebServerFactory можно конфигурировать
//    настройки от ConfigurableServletWebServerFactory + специфичные для tomcat servlet server

//    @Bean
//    WebServerFactoryCustomizer<ConfigurableTomcatWebServerFactory> customWebServerFactoryCustomizer( ServerProperties serverProperties){
//        return factory -> {
//            ServerProperties.Tomcat properties = serverProperties.getTomcat();
//            PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
//            map.from(properties::getBasedir).to(factory::setBaseDirectory);
//            map.from(properties::getBackgroundProcessorDelay)
//                    .as(Duration::getSeconds)
//                    .as(Long::intValue)
//                    .to(factory::setBackgroundProcessorDelay);
//            customizeRemoteIpValve(factory);
//            ServerProperties.Tomcat.Threads threadProperties = properties.getThreads();
//            map.from(threadProperties::getMax)
//                    .when(this::isPositive)
//                    .to((maxThreads) -> customizeMaxThreads(factory, threadProperties.getMax()));
//            map.from(threadProperties::getMinSpare)
//                    .when(this::isPositive)
//                    .to((minSpareThreads) -> customizeMinThreads(factory, minSpareThreads));
//            map.from(this.serverProperties.getMaxHttpRequestHeaderSize())
//                    .asInt(DataSize::toBytes)
//                    .when(this::isPositive)
//                    .to((maxHttpRequestHeaderSize) -> customizeMaxHttpRequestHeaderSize(factory, maxHttpRequestHeaderSize));
//            map.from(properties::getMaxHttpResponseHeaderSize)
//                    .asInt(DataSize::toBytes)
//                    .when(this::isPositive)
//                    .to((maxHttpResponseHeaderSize) -> customizeMaxHttpResponseHeaderSize(factory, maxHttpResponseHeaderSize));
//            map.from(properties::getMaxSwallowSize)
//                    .asInt(DataSize::toBytes)
//                    .to((maxSwallowSize) -> customizeMaxSwallowSize(factory, maxSwallowSize));
//            map.from(properties::getMaxHttpFormPostSize)
//                    .asInt(DataSize::toBytes)
//                    .when((maxHttpFormPostSize) -> maxHttpFormPostSize != 0)
//                    .to((maxHttpFormPostSize) -> customizeMaxHttpFormPostSize(factory, maxHttpFormPostSize));
//            map.from(properties::getAccesslog)
//                    .when(ServerProperties.Tomcat.Accesslog::isEnabled)
//                    .to((enabled) -> customizeAccessLog(factory));
//            map.from(properties::getUriEncoding).to(factory::setUriEncoding);
//            map.from(properties::getConnectionTimeout)
//                    .to((connectionTimeout) -> customizeConnectionTimeout(factory, connectionTimeout));
//            map.from(properties::getMaxConnections)
//                    .when(this::isPositive)
//                    .to((maxConnections) -> customizeMaxConnections(factory, maxConnections));
//            map.from(properties::getAcceptCount)
//                    .when(this::isPositive)
//                    .to((acceptCount) -> customizeAcceptCount(factory, acceptCount));
//            map.from(properties::getProcessorCache)
//                    .to((processorCache) -> customizeProcessorCache(factory, processorCache));
//            map.from(properties::getKeepAliveTimeout)
//                    .to((keepAliveTimeout) -> customizeKeepAliveTimeout(factory, keepAliveTimeout));
//            map.from(properties::getMaxKeepAliveRequests)
//                    .to((maxKeepAliveRequests) -> customizeMaxKeepAliveRequests(factory, maxKeepAliveRequests));
//            map.from(properties::getRelaxedPathChars)
//                    .as(this::joinCharacters)
//                    .whenHasText()
//                    .to((relaxedChars) -> customizeRelaxedPathChars(factory, relaxedChars));
//            map.from(properties::getRelaxedQueryChars)
//                    .as(this::joinCharacters)
//                    .whenHasText()
//                    .to((relaxedChars) -> customizeRelaxedQueryChars(factory, relaxedChars));
//            map.from(properties::isRejectIllegalHeader)
//                    .to((rejectIllegalHeader) -> customizeRejectIllegalHeader(factory, rejectIllegalHeader));
//            customizeStaticResources(factory);
//            customizeErrorReportValve(this.serverProperties.getError(), factory);
//        };
//    }

//----------------------------------------------------------------------

//---------HttpEncodingAutoConfiguration--------------------------------
//    server
//        servlet
//            encoding:
//                enabled: true
//                charset: UTF-8
//                force: true
//                force-request: true
//                force-response: true
//                mapping.*:

//    @Bean
//    public CharacterEncodingFilter characterEncodingFilter(ServerProperties serverProperties) {
//        Encoding encoding = serverProperties.getServlet().getEncoding();
//        CharacterEncodingFilter filter = new OrderedCharacterEncodingFilter();
//        filter.setEncoding(encoding.getCharset().name());
//        filter.setForceRequestEncoding(encoding.shouldForce(Encoding.Type.REQUEST));
//        filter.setForceResponseEncoding(encoding.shouldForce(Encoding.Type.RESPONSE));
//        return filter;
//    }


// ---------------------------------------------------------------

//---------SslAutoConfiguration----------------------------------

//    spring:
//        ssl:
//        bundle:
//            jks.*:
//            pem.*:
//            watch:
//                file:
//                    quiet-period:

// Можно регать множество SslBundleRegistrar, которые будут добавлять бандлы в регистр
//    @Bean
//    SslBundleRegistrar customSslBundleRegistrar(){
//        return registry -> registry.registerBundle("rr", SslBundle.of() );
//    }
//
//    //Или регистрация самого регистра (смотри выше)
//    @Bean
//    SslBundles customSslBundles(){
//        return bundleName -> ...
//    }
//
//    Но тут только регистрация бандлов, есть еще server.ssl общие настройки в сервере( смотри выше)
// --------------------------------------------------------------


// ------DispatcherServletAutoConfiguration-----------------------------------

//    spring:
//        mvc:
//            throw-exception-if-no-handler-found: true  #чтобы обрабатывать NoHandlerFoundException в нашем @ControllerAdvice
//            dispatch-options-request: true
//            dispatch-trace-request: true
//            log-request-details: true
//            publish-request-handled-events: true
//            servlet:
//                path: /
//                load-on-startup: -1


//    @Bean(name = DEFAULT_DISPATCHER_SERVLET_BEAN_NAME)
//    public DispatcherServlet dispatcherServlet(WebMvcProperties webMvcProperties) {
//        DispatcherServlet dispatcherServlet = new DispatcherServlet();
//        dispatcherServlet.setDispatchOptionsRequest(webMvcProperties.isDispatchOptionsRequest());
//        dispatcherServlet.setDispatchTraceRequest(webMvcProperties.isDispatchTraceRequest());
//        dispatcherServlet.setThrowExceptionIfNoHandlerFound(webMvcProperties.isThrowExceptionIfNoHandlerFound());
//        dispatcherServlet.setPublishEvents(webMvcProperties.isPublishRequestHandledEvents());
//        dispatcherServlet.setEnableLoggingRequestDetails(webMvcProperties.isLogRequestDetails());
//        return dispatcherServlet;
//    }

//    @Bean(name = DEFAULT_DISPATCHER_SERVLET_REGISTRATION_BEAN_NAME)
//    public DispatcherServletRegistrationBean dispatcherServletRegistration(DispatcherServlet dispatcherServlet,
//                                                                           WebMvcProperties webMvcProperties,
//                                                                           ObjectProvider<MultipartConfigElement> multipartConfig) {
//        DispatcherServletRegistrationBean registration = new DispatcherServletRegistrationBean(dispatcherServlet,
//                webMvcProperties.getServlet().getPath());
//        registration.setName(DEFAULT_DISPATCHER_SERVLET_BEAN_NAME);
//        registration.setLoadOnStartup(webMvcProperties.getServlet().getLoadOnStartup());
//        multipartConfig.ifAvailable(registration::setMultipartConfig);
//        return registration;
//    }

//    -------------------------------------------------------------------


//    -------WebMvcAutoConfiguration---------------------------------------------
// Можно настраивать через обычные WebMvcConfigurer, все они подтянуться (включая дополнительный
// WebMvcAutoConfigurationAdapter implements WebMvcConfigurer, который регается как бин в WebMvcAutoConfiguration с order=0 )
// и в зависимости от order применятся поверх WebMvcConfigurationSupport (через DelegatingWebMvcConfiguration).
// Либо можно сразу свой WebMvcConfigurationSupport как бин зарегать, тогда WebMvcAutoConfiguration
// Не отработает вообще  @ConditionalOnMissingBean(WebMvcConfigurationSupport.class)


//    spring:
//        web:
//            resources:
//                add-mappings: false  # проброс NoHandlerFoundException (смотри ниже)
//                static-locations: classpath:/META-INF/resources/,classpath:/resources/,classpath:/static/,classpath:/public/
//                cache:
//                    period:
//                    use-last-modified: true
//                    cachecontrol:
//                        cache-private: true
//                        cache-public: false
//                        max-age:
//                        must-revalidate: true
//                        no-cache: true
//                        no-store: true
//                        no-transform: true
//                        proxy-revalidate: true
//                        s-max-age:
//                        stale-if-error:
//                        stale-while-revalidate:
//                    chain:
//                        enabled: false
//                        cache: true
//                        compressed: true
//                        strategy:
//                            content:
//                                enabled: false
//                                paths: /**
//                            fixed:
//                                enabled: false
//                                paths: /**
//                                version:
//            locale-resolver: ACCEPT_HEADER
//            locale:
//
//        mvc:
//            throw-exception-if-no-handler-found: true  #чтобы обрабатывать NoHandlerFoundException в нашем @ControllerAdvice
//            #    dispatch-options-request: true
//            #    dispatch-trace-request: true
//            log-request-details: true
//            #    publish-request-handled-events: true
//            #    servlet:
//            #      path: /
//            #      load-on-startup: -1
//            async:
//            #      request-timeout: PT10000S
//                request-timeout: PT10S
//            pathmatch:
//                matching-strategy: path-pattern-parser
//            contentnegotiation:
//                favor-parameter: true
//                parameter-name: testParam
//                media-types:
//                *
//            view:
//                prefix:
//                suffix:
//            message-codes-resolver-format: PREFIX_ERROR_CODE
//            static-path-pattern: /**
//            webjars-path-pattern: /webjars/**
//            format:
//                date: dd/MM/yyyy
//                time: HH:mm:ss
//                date-time: yyyy-MM-dd HH:mm:ss
//            log-resolved-exception: true
//            problemdetails:
//                enabled: true


//    @Bean
////    @ConditionalOnMissingBean(HiddenHttpMethodFilter.class)
////    @ConditionalOnProperty(prefix = "spring.mvc.hiddenmethod.filter", name = "enabled")
//    public OrderedHiddenHttpMethodFilter hiddenHttpMethodFilter() {
//        return new OrderedHiddenHttpMethodFilter();
//    }

//    @Bean
////    @ConditionalOnMissingBean(FormContentFilter.class)
////    @ConditionalOnProperty(prefix = "spring.mvc.formcontent.filter", name = "enabled", matchIfMissing = true)
//    public OrderedFormContentFilter formContentFilter() {
//        return new OrderedFormContentFilter();
//    }


//Подбираются все бины типа HttpMessageConverter в HttpMessageConvertersAutoConfiguration
//И засовываются в HttpMessageConverters, который в свою очередь подбирается и устанавливается WebMvcAutoConfigurationAdapter
//    HttpMessageConverter<TestObj> customHttpMessageConverter(){
//        return new HttpMessageConverter<TestObj>() {...};
//    }

//    @Bean
//    public InternalResourceViewResolver customViewResolver(WebMvcProperties mvcProperties) {
//        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
//        resolver.setPrefix(mvcProperties.getView().getPrefix());
//        resolver.setSuffix(mvcProperties.getView().getSuffix());
//        return resolver;
//    }
//
//    @Bean
//    public BeanNameViewResolver beanNameViewResolver() {
//        BeanNameViewResolver resolver = new BeanNameViewResolver();
//        resolver.setOrder(Ordered.LOWEST_PRECEDENCE - 10);
//        return resolver;
//    }

//    @Bean
//    public ContentNegotiatingViewResolver viewResolver(BeanFactory beanFactory) {
//        ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
//        resolver.setContentNegotiationManager(beanFactory.getBean(ContentNegotiationManager.class));
//        // ContentNegotiatingViewResolver uses all the other view resolvers to locate
//        // a view so it should have a high precedence
//        resolver.setOrder(Ordered.HIGHEST_PRECEDENCE);
//        return resolver;
//    }


//   WebMvcAutoConfigurationAdapter подбирает все бины типа Converter,
//    GenericConverter,Printer, Parser и регает их в FormatterRegistry registry
//    @Bean
//    Converter customConverter(){
//        return new Converter() { ...
//        }
//    }
//    @Bean
//    GenericConverter customGenericConverter(){
//        return new GenericConverter() { ...
//        }
//    }
//    @Bean
//    Printer customPrinter(){
//        return new Printer() { ...
//        }
//    }
//    @Bean
//    Parser customParser(){
//        return new Parser() {...}
//    }


//Можно переопределить RequestMappingHandlerMapping,RequestMappingHandlerAdapter,ExceptionHandlerExceptionResolver
//на кастомные через WebMvcRegistrations. подхватывается в EnableWebMvcConfiguration
//    @Bean
//    WebMvcRegistrations customWebMvcRegistration(){
//        return new WebMvcRegistrations() {
//            @Override
//            public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
//            }
//
//            @Override
//            public RequestMappingHandlerAdapter getRequestMappingHandlerAdapter() {
//            }
//
//            @Override
//            public ExceptionHandlerExceptionResolver getExceptionHandlerExceptionResolver() {
//            }
//        };
//    }


//    @Bean
//    public LocaleResolver localeResolver(WebProperties webProperties) {
//        if (webProperties.getLocaleResolver() == WebProperties.LocaleResolver.FIXED) {
//            return new FixedLocaleResolver(webProperties.getLocale());
//        }
//        AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
//        localeResolver.setDefaultLocale(webProperties.getLocale());
//        return localeResolver;
//    }
//
//    @Bean
//    public FlashMapManager flashMapManager() {
//        return new SessionFlashMapManager();
//    }


//    можно установить дефолтный @ControllerAdvice ResponseEntityExceptionHandler свойством problemdetails.enabled
//    @ConditionalOnProperty(prefix = "spring.mvc.problemdetails", name = "enabled", havingValue = "true")
//    @Bean
//    ResponseEntityExceptionHandler problemDetailsExceptionHandler() {
//        return new ResponseEntityExceptionHandler(){};
//    }

//    -------------------------------------------------------------------


//    -------MultipartAutoConfiguration-------------------------------------------------

//    spring:
//        servlet:
//        multipart:
//            enabled: true
//            file-size-threshold: OB
//            location:
//            max-file-size: 1MB
//            max-request-size: 10MB
//            resolve-lazily: false
//            strict-servlet-compliance: false

//    The programmatic equivalent of MultipartConfig used to configure multi-part handling for a Servlet when registering a Servlet via code.
//    @Bean
//    public MultipartConfigElement multipartConfigElement(MultipartProperties multipartProperties) {
//        return multipartProperties.createMultipartConfig();
//    }
//
//    @Bean(name = DispatcherServlet.MULTIPART_RESOLVER_BEAN_NAME)
//    public StandardServletMultipartResolver multipartResolver(MultipartProperties multipartProperties) {
//        StandardServletMultipartResolver multipartResolver = new StandardServletMultipartResolver();
//        multipartResolver.setResolveLazily(multipartProperties.isResolveLazily());
//        return multipartResolver;
//    }

//    -------------------------------------------------------------------


//-------HttpMessageConvertersAutoConfiguration-------------------------------------------------

//    spring:
//        mvc:
//            converters:
//                preferred-json-mapper: jackson


//    @Bean
//    public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
//        return new HttpMessageConverters(converters.orderedStream().toList());
//    }


//By default, this converter supports all media types (*/*), and writes with a Content-Type of text/plain.
// This can be overridden by setting the supportedMediaTypes property.
//    @Bean
//    public StringHttpMessageConverter stringHttpMessageConverter(Environment environment) {
//        Encoding encoding = Binder.get(environment).bindOrCreate("server.servlet.encoding", Encoding.class);
//        StringHttpMessageConverter converter = new StringHttpMessageConverter(encoding.getCharset());
//        converter.setWriteAcceptCharset(false);
//        return converter;
//    }


//    By default, this converter supports application/json and application/*+json with UTF-8 character set.
//    This can be overridden by setting the supportedMediaTypes property.
//    @Bean
//    MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(ObjectMapper objectMapper) {
//        return new MappingJackson2HttpMessageConverter(objectMapper);
//    }

// ------------------------------------------------------------------


//-------SqlInitializationAutoConfiguration-------------------------------------------------
//    spring
//        sql:
//            init:
//                enabled: true
//                mode: always #    By default, SQL database initialization is only performed when using an embedded in-memory database. To always initialize an SQL database, irrespective of its type, set spring.sql.init.mode to always
//                platform: #Можно тогда использовать специфичные schema-${platform}.sql data-${platform}.sql
//                data-locations:
//                schema-locations:
//                encoding: UTF-8
//                continue-on-error: false
//                separator: ;
//                username:
//                password:


    //Кастомная Инициализация бд схемой и данными
//    @Bean
//    SqlDataSourceScriptDatabaseInitializer customDataSourceScriptDatabaseInitializer(DataSource dataSource,
//                                                                               SqlInitializationProperties properties) {
//        return new SqlDataSourceScriptDatabaseInitializer( dataSource, properties);
//    }


    // ---------------------------------------------------------------------------------------





    // ---------------------------------------------------------------------------------------


//---------JdbcTemplateAutoConfiguration---------------------------------

//    spring:
//        jdbc:
//            template:
//                fetch-size: -1
//                max-rows: -1
//                query-timeout:


//    @Bean
//    @Primary
//    JdbcTemplate jdbcTemplate(DataSource dataSource, JdbcProperties properties) {
//        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
//        JdbcProperties.Template template = properties.getTemplate();
//        jdbcTemplate.setFetchSize(template.getFetchSize());
//        jdbcTemplate.setMaxRows(template.getMaxRows());
//        if (template.getQueryTimeout() != null) {
//            jdbcTemplate.setQueryTimeout((int) template.getQueryTimeout().getSeconds());
//        }
//        return jdbcTemplate;
//    }
//
//    @Bean
//    @Primary
//    NamedParameterJdbcTemplate namedParameterJdbcTemplate(JdbcTemplate jdbcTemplate) {
//        return new NamedParameterJdbcTemplate(jdbcTemplate);
//    }
//

    //---------------------------------------------------------


//---------TransactionAutoConfiguration---------------------------------

//    spring:
//        transaction:
//            default-timeout:
//            rollback-on-commit-failure:


//    конкретный TransactionManager(если их несколько) используемый для аннотаций, активируемых с помощью
//    @EnableTransactionManagement (@Transactionl и тд)
//    можно указывать через TransactionManagementConfigurer

    //Кастомизация PlatformTransactionManager
//    PlatformTransactionManagerCustomizer<AbstractPlatformTransactionManager> customPlatformTransactionManagerCustomizer(){
//        return transactionManager -> {
//            transactionManager.setDefaultTimeout();
//            transactionManager.setTransactionSynchronization(SYNCHRONIZATION_ALWAYS);
//            transactionManager.setNestedTransactionAllowed(true);
//            transactionManager.setTransactionSynchronizationName();
//            transactionManager.setValidateExistingTransaction();
//            transactionManager.setGlobalRollbackOnParticipationFailure();
//            transactionManager.setRollbackOnCommitFailure();
//        };
//    }


    //---------------------------------------------------------


    //---------HibernateJpaAutoConfiguration------------------------------------------

//    spring:
//        jpa:
//            database: DEFAULT|DB2|DERBY|H2|HANA|HSQL|INFORMIX|MYSQL|ORACLE|POSTGRESQL|SQL_SERVER|SYBASE #Target database to operate on, auto-detected by default. Can be alternatively set using the "databasePlatform" property.
//            database-platform: #Name of the target database to operate on, auto-detected by default. Can be alternatively set using the "Database" enum.
//            defer-datasource-initialization: true #Whether to defer DataSource initialization until after any EntityManagerFactory beans have been created and initialized
//            open-in-view: false #Register OpenEntityManagerInViewInterceptor. Binds a JPA EntityManager to the thread for the entire processing of the request
//            mapping-resources: #Mapping resources (equivalent to "mapping-file" entries in persistence.xml).
//            show-sql: true
//            generate-ddl: true
//            hibernate:
//                ddl-auto: create
//                naming:
//                    physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
//                    implicit-strategy:
//            properties:
//                jakarta:
//                    persistence:
//                        schema-generation:
//                            database:
//                                action: drop-and-create
//                            create-source: metadata-then-script
//                            create-script-source: schemaKek.sql (доп скрипт для генерации схемы)
//                            drop-source: metadata
//                            drop-script-source: MyDrop.sql
//                            scripts:    # Генерация скрипта в файл
//                                action: drop-and-create
//                                create-target: GeneratedCreate.sql
//                                drop-target: GeneratedDrop.sql
//                        sql-load-script-source: dataKek.sql  # Загрузка данных
//
//                hibernate:
//                    hbm2ddl:
//                    import_files_sql_extractor: org.hibernate.tool.schema.internal.script.MultiLineSqlScriptExtractor #Многострочные выражения
//                    show_sql: true
//                    format_sql: true
//                    use_sql_comments: true
//                    dialect: org.hibernate.dialect.PostgreSQLDialect
//                    generate_statistics: true
//                    order_inserts: true
//                    batch_versioned_data: true
//                    jdbc:
//                        batch_size: 100


//    @Bean
//    HibernatePropertiesCustomizer customHibernatePropertiesCustomizer(){
//        return hibernateProperties -> {
//            hibernateProperties.put(AvailableSettings.HBM2DDL_AUTO, "someValue");
//            hibernateProperties.put(AvailableSettings.*, "someValue");
//        };
//    }

//    @Bean
//    PhysicalNamingStrategy customPhysicalNamingStrategy(){
//        return new PhysicalNamingStrategy() {
//            ...
//        }
//    }
//
//    @Bean
//    ImplicitNamingStrategy customImplicitNamingStrategy(){
//        return new ImplicitNamingStrategy() {
//            ...
//        }
//    }

//    @Bean
//    EntityManagerFactoryBuilderCustomizer entityManagerFactoryBuilderCustomizer(){
//        return builder -> {
//            builder.setPersistenceUnitPostProcessors();
//        };
//    }
//
//    @Bean
//    static PersistenceManagedTypes persistenceManagedTypes(BeanFactory beanFactory, ResourceLoader resourceLoader) {
//        List<String> packages = EntityScanPackages.get(beanFactory).getPackageNames();
//        if (packages.isEmpty() && AutoConfigurationPackages.has(beanFactory)) {
//            packages = AutoConfigurationPackages.get(beanFactory);
//        }
//        String[] packagesToScan =  StringUtils.toStringArray(packages);;
//        return new PersistenceManagedTypesScanner(resourceLoader).scan(packagesToScan);
//    }
//

//    @Bean
//    public OpenEntityManagerInViewInterceptor openEntityManagerInViewInterceptor(JpaProperties jpaProperties) {
//        if (jpaProperties.getOpenInView() == null) {
//            logger.warn("spring.jpa.open-in-view is enabled by default. "
//                    + "Therefore, database queries may be performed during view "
//                    + "rendering. Explicitly configure spring.jpa.open-in-view to disable this warning");
//        }
//        return new OpenEntityManagerInViewInterceptor();
//    }

//    @Bean
//    public WebMvcConfigurer openEntityManagerInViewInterceptorConfigurer(
//            OpenEntityManagerInViewInterceptor interceptor) {
//        return new WebMvcConfigurer() {
//
//            @Override
//            public void addInterceptors(InterceptorRegistry registry) {
//                registry.addWebRequestInterceptor(interceptor);
//            }
//
//        };
//    }





//---------Вариант, как все это объявлено (приблизительно!) в HibernateJpaConfiguration------------
//
//    @Bean
//    public PlatformTransactionManager transactionManager(
//            ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers,
//            LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
//        JpaTransactionManager transactionManager = new JpaTransactionManager();
////        transactionManager.setEntityManagerFactory(entityManagerFactoryBean.getObject());
//        transactionManagerCustomizers.ifAvailable((customizers) -> customizers.customize(transactionManager));
//        return transactionManager;
//    }
//
//    @Bean
//    public JpaVendorAdapter jpaVendorAdapter(JpaProperties jpaProperties ) {
//        AbstractJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
//        adapter.setShowSql(jpaProperties.isShowSql());
//        if (jpaProperties.getDatabase() != null) {
//            adapter.setDatabase(jpaProperties.getDatabase());
//        }
//        if (jpaProperties.getDatabasePlatform() != null) {
//            adapter.setDatabasePlatform(jpaProperties.getDatabasePlatform());
//        }
//        adapter.setGenerateDdl(jpaProperties.isGenerateDdl());
//        return adapter;
//    }
//
//    @Bean
//    @Primary
//    static PersistenceManagedTypes persistenceManagedTypes(BeanFactory beanFactory, ResourceLoader resourceLoader) {
//        List<String> packages = EntityScanPackages.get(beanFactory).getPackageNames();
//        if (packages.isEmpty() && AutoConfigurationPackages.has(beanFactory)) {
//            packages = AutoConfigurationPackages.get(beanFactory);
//        }
//        String[] packagesToScan =  StringUtils.toStringArray(packages);
//        return new PersistenceManagedTypesScanner(resourceLoader).scan(packagesToScan);
//    }
//
//
//    @Bean
//    public EntityManagerFactoryBuilder entityManagerFactoryBuilder(JpaVendorAdapter jpaVendorAdapter,
//                                                                   ObjectProvider<PersistenceUnitManager> persistenceUnitManager,
//                                                                   ObjectProvider<EntityManagerFactoryBuilderCustomizer> customizers,
//                                                                   JpaProperties jpaProperties) {
//        EntityManagerFactoryBuilder builder = new EntityManagerFactoryBuilder(jpaVendorAdapter,
//                jpaProperties.getProperties(), persistenceUnitManager.getIfAvailable());
//        customizers.orderedStream().forEach((customizer) -> customizer.customize(builder));
//        return builder;
//    }
//
//
//    @Bean
//    @Primary
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource,
//                                                                                               HibernateProperties hibernateProperties,
//                                                                                               JpaProperties jpaProperties,
//                                                                                               List<HibernatePropertiesCustomizer> hibernatePropertiesCustomizers,
//                                                                                               EntityManagerFactoryBuilder factoryBuilder,
//                                                                                              PersistenceManagedTypes persistenceManagedTypes) {
//
//        HibernateSettings settings = new HibernateSettings()
//                .ddlAuto(() -> "create")
//                .hibernatePropertiesCustomizers(hibernatePropertiesCustomizers);
//        Map<String, Object> vendorProperties = hibernateProperties.determineHibernateProperties(
//                jpaProperties.getProperties(), settings);
//
////        Look org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration.entityManagerFactory
////        customizeVendorProperties(vendorProperties);
//
//        List<String> mappingResources = jpaProperties.getMappingResources();
//        String[] mappings = !ObjectUtils.isEmpty(mappingResources) ? StringUtils.toStringArray(mappingResources) : null;
//
//        return factoryBuilder
//                .dataSource(dataSource)
////                .packages("com.example.book.model", "com.example.book.model.cte")
//                .managedTypes(persistenceManagedTypes)
//                .properties(vendorProperties)
//                .mappingResources(mappings)
////                .jta(isJta())
//                .build();
//    }

//------------------------------------------------------------------------------------

//---------Кастомный вариант, с LocalContainerEntityManagerFactoryBean и JpaTransactionManager ------------

//
//    @Bean
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
//
//        LocalContainerEntityManagerFactoryBean em
//                = new LocalContainerEntityManagerFactoryBean();
//
//        em.setDataSource(dataSource);
//        em.setPackagesToScan("com.example.demo.book.model", "com.example.demo.book.model.cte");
////        em.setJtaDataSource();
//
//        JpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
//        em.setJpaVendorAdapter(adapter);
//
//        var hibernatePropertiesMap = new HashMap<String, Object>();
//        hibernatePropertiesMap.put(AvailableSettings.HBM2DDL_AUTO, "create");
////        hibernatePropertiesMap.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
//        hibernatePropertiesMap.put(AvailableSettings.FORMAT_SQL, "true");
//        hibernatePropertiesMap.put(AvailableSettings.SHOW_SQL, "true");
//        hibernatePropertiesMap.put(AvailableSettings.USE_SQL_COMMENTS, "true");
//        hibernatePropertiesMap.put(AvailableSettings.IMPLICIT_NAMING_STRATEGY, SpringImplicitNamingStrategy.class.getName());
//        hibernatePropertiesMap.put(AvailableSettings.PHYSICAL_NAMING_STRATEGY, CamelCaseToUnderscoresNamingStrategy.class.getName());
//
//
//
//        em.setJpaPropertyMap(hibernatePropertiesMap);
//
////        em.setLoadTimeWeaver();
////        em.setManagedTypes();
////        em.setPersistenceUnitManager();
////        em.setPersistenceUnitName();
////        em.setPersistenceUnitRootLocation();
////        em.setMappingResources();
////        em.setPersistenceUnitPostProcessors();
////        em.setPersistenceXmlLocation();
////        em.setSharedCacheMode();
////        em.setValidationMode();
////        em.setResourceLoader();
////        em.setBeanClassLoader();
////        em.setBeanFactory();
////        em.setBootstrapExecutor();
////        em.setJpaPropertyMap();
////        em.setJpaProperties();
////        em.setJpaDialect();
////        em.setPersistenceProvider();
////        em.setPersistenceProviderClass();
////        em.setEntityManagerFactoryInterface();
////        em.setEntityManagerInterface();
////        em.setEntityManagerInitializer();
////        em.setBeanName();
//
//
//        return em;
//    }
//
//
//    @Bean
//    PlatformTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean entityManagerFactory){
//        JpaTransactionManager transactionManager = new JpaTransactionManager();
//        transactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
//        return transactionManager;
//    }

    //------------------------------------------------------------------------------------
    //---------Кастомный вариант(походу не рабочий с Hibernate ORM 6.x), с LocalSessionFactoryBean и HibernateTransactionManager------------


//   !!!!!В классе LocalSessionFactoryBean и HibernateTransactionManager обозначено:
//    NOTE: Hibernate ORM 6.x is officially only supported as a JPA provider. Please use org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
//    with org.springframework.orm.jpa.JpaTransactionManager there instead.


//    @Bean
//    public LocalSessionFactoryBean entityManagerFactory(DataSource dataSource) {
//
//        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
//        sessionFactory.setDataSource(dataSource);
//        sessionFactory.setPackagesToScan("com.example.demo.book.model", "com.example.demo.book.model.cte");
//
//        Properties hibernateProperties = new Properties();
//        hibernateProperties.put(AvailableSettings.HBM2DDL_AUTO, "create");
////        hibernateProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
//        hibernateProperties.put(AvailableSettings.FORMAT_SQL, "true");
//        hibernateProperties.put(AvailableSettings.SHOW_SQL, "true");
//        hibernateProperties.put(AvailableSettings.USE_SQL_COMMENTS, "true");
//        hibernateProperties.put(AvailableSettings.IMPLICIT_NAMING_STRATEGY, SpringImplicitNamingStrategy.class.getName());
//        hibernateProperties.put(AvailableSettings.PHYSICAL_NAMING_STRATEGY, CamelCaseToUnderscoresNamingStrategy.class.getName());
//
//        sessionFactory.setHibernateProperties(hibernateProperties);
//
//        sessionFactory.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
//        sessionFactory.setImplicitNamingStrategy(new SpringImplicitNamingStrategy());
//
//
////        sessionFactory.setMetadataSources();
////        sessionFactory.setEntityInterceptor();
////        sessionFactory.setBeanFactory();
////        sessionFactory.setCacheRegionFactory();
////        sessionFactory.setAnnotatedClasses();
////        sessionFactory.setAnnotatedPackages();
////        sessionFactory.setBootstrapExecutor();
////        sessionFactory.setCacheableMappingLocations();
////        sessionFactory.setConfigLocation();
////        sessionFactory.setConfigLocations();
////        sessionFactory.setCurrentTenantIdentifierResolver();
////        sessionFactory.setEntityTypeFilters();
////        sessionFactory.setHibernateIntegrators();
////        sessionFactory.setImplicitNamingStrategy();
////        sessionFactory.setPhysicalNamingStrategy();
////        sessionFactory.setJtaTransactionManager();
////        sessionFactory.setMappingLocations();
////        sessionFactory.setMappingJarLocations();
////        sessionFactory.setMappingResources();
////        sessionFactory.setMappingDirectoryLocations();
////        sessionFactory.setResourceLoader();
////        sessionFactory.setJdbcExceptionTranslator();
//        return sessionFactory;
//    }
//
//
//    @Bean
//    public HibernateTransactionManager hibernateTransactionManager(LocalSessionFactoryBean sessionFactory) {
//        HibernateTransactionManager transactionManager
//                = new HibernateTransactionManager();
//        transactionManager.setSessionFactory(sessionFactory.getObject());
//
//        return transactionManager;
//    }
//
//
//


}
