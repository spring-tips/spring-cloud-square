package com.example.square;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClientConfig;
import okhttp3.OkHttpClient;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.EventPublishingRunListener;
import org.springframework.cloud.client.*;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.*;
import org.springframework.cloud.loadbalancer.config.BlockingLoadBalancerClientAutoConfiguration;
import org.springframework.cloud.loadbalancer.config.LoadBalancerAutoConfiguration;
import org.springframework.cloud.loadbalancer.config.LoadBalancerCacheAutoConfiguration;
import org.springframework.cloud.loadbalancer.config.LoadBalancerStatsAutoConfiguration;
import org.springframework.cloud.loadbalancer.core.*;
import org.springframework.cloud.loadbalancer.security.OAuth2LoadBalancerClientAutoConfiguration;
import org.springframework.cloud.loadbalancer.stats.MicrometerStatsLoadBalancerLifecycle;
import org.springframework.cloud.netflix.eureka.loadbalancer.EurekaLoadBalancerClientConfiguration;
import org.springframework.cloud.square.okhttp.loadbalancer.OkHttpLoadBalancerAutoConfiguration;
import org.springframework.cloud.square.retrofit.DefaultRetrofitClientConfiguration;
import org.springframework.cloud.square.retrofit.EnableRetrofitClients;
import org.springframework.cloud.square.retrofit.RetrofitClientFactoryBean;
import org.springframework.cloud.square.retrofit.core.AbstractRetrofitClientFactoryBean;
import org.springframework.cloud.square.retrofit.core.RetrofitClient;
import org.springframework.cloud.square.retrofit.core.RetrofitClientSpecification;
import org.springframework.cloud.square.retrofit.core.RetrofitContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.ConversionService;
import org.springframework.nativex.hint.AccessBits;
import org.springframework.nativex.hint.NativeHint;
import org.springframework.nativex.hint.TypeHint;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.GenericDeclaration;
import java.util.Map;


@EnableRetrofitClients


@TypeHint(
	access = AccessBits.ALL,
	typeNames = {
//		"jdk.vm.ci.meta.JavaKind$FormatWithToString[]",
//		"sun.security.ssl.SSLContextImpl$TLSContext",
//		"sun.security.ssl.TrustManagerFactoryImpl$PKIXFactory",
//		"org.bouncycastle.jsse.BCSSLEngine",
//		"io.netty.handler.ssl.BouncyCastleAlpnSslUtils",
	},
	types = {
		ConditionalOnBlockingDiscoveryEnabled.class,
		ConditionalOnDiscoveryEnabled.class,
		ConditionalOnDiscoveryHealthIndicatorEnabled.class,
		ConditionalOnReactiveDiscoveryEnabled.class,
		EnableRetrofitClients.class,
		LoadBalancerClient.class,
		LoadBalancerClients.class,

		AbstractRetrofitClientFactoryBean.class,
		BlockingLoadBalancerClientAutoConfiguration.class,
		CachingServiceInstanceListSupplier.class,
		CommonsClientAutoConfiguration.class,
		DefaultRetrofitClientConfiguration.class,
		DelegatingServiceInstanceListSupplier.class,
		EurekaClientConfig.class,
		EurekaLoadBalancerClientConfiguration.class,
		EventPublishingRunListener.class,
		GenericDeclaration[].class, AnnotatedElement[].class,
		HostInfoEnvironmentPostProcessor.class,
		InstanceInfo.ActionType.class,
		LoadBalancerAutoConfiguration.class,
		LoadBalancerCacheAutoConfiguration.CaffeineLoadBalancerCacheManagerConfiguration.class,
		LoadBalancerCacheAutoConfiguration.DefaultLoadBalancerCacheManagerConfiguration.class,
		LoadBalancerCacheAutoConfiguration.LoadBalancerCacheManagerWarnConfiguration.class,
		LoadBalancerCacheAutoConfiguration.LoadBalancerCaffeineWarnLogger.class,
		LoadBalancerCacheAutoConfiguration.class,
		LoadBalancerClientConfiguration.BlockingOnAvoidPreviousInstanceAndRetryEnabledCondition.class,
		LoadBalancerClientConfiguration.BlockingSupportConfiguration.class,
		LoadBalancerClientConfiguration.ReactiveOnAvoidPreviousInstanceAndRetryEnabledCondition.class,
		LoadBalancerClientConfiguration.ReactiveRetryConfiguration.class,
		LoadBalancerClientConfiguration.ReactiveSupportConfiguration.class,
		LoadBalancerClientConfiguration.class,
		LoadBalancerClientConfigurationRegistrar.class,
		LoadBalancerClientSpecification.class,
		LoadBalancerStatsAutoConfiguration.class,
		MicrometerStatsLoadBalancerLifecycle.class,
		OAuth2LoadBalancerClientAutoConfiguration.class,
		OkHttpLoadBalancerAutoConfiguration.class,
		ReactiveCommonsClientAutoConfiguration.class,
		ReactorLoadBalancer.class,
		ReactorServiceInstanceLoadBalancer.class,
		Retrofit.Builder.class,
		RetrofitClientFactoryBean.class,
		RetrofitClientSpecification.class,
		RetrofitContext.class,
		RoundRobinLoadBalancer.class,
		ServiceInstance.class,
		ServiceInstanceListSupplier.class,
		org.springframework.cloud.loadbalancer.annotation.LoadBalancerClientConfiguration.class,
		org.springframework.cloud.loadbalancer.config.LoadBalancerZoneConfig.class,
		org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer.class
	})

/*@ResourceHint(
	patterns = {
//		"org/springframework/cloud/square/okhttp/loadbalancer/OkHttpLoadBalancerAutoConfiguration.class",
//		"org/springframework/cloud/square/retrofit/RetrofitAutoConfiguration.class"
	}
)*/
@NativeHint(options = {" -H:+AddAllCharsets --enable-url-protocols=http,https "})


@EnableDiscoveryClient
@SpringBootApplication
public class SquareApplication {

	@Bean
	@LoadBalanced
	OkHttpClient.Builder okHttpClientBuilder() {
		return new OkHttpClient.Builder();
	}

	@Bean
	ApplicationRunner runner(
		Map<String, ConversionService> css,
		GreetingsClient gc) {
		return event -> {
			System.out.println(" " + css.size() + " conversionService instance(s).");
			css.forEach((k, v) -> System.out.println(k + '=' + v.getClass().getName()));
			var result = gc.hello("Spring Fans!").execute().body();
			System.out.println("result: " + result);
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(SquareApplication.class, args);
	}
}


@RetrofitClient(
	"greetings"
)
interface GreetingsClient {

	@GET("/hello/{name}")
	Call<String> hello(@Path("name") String name);
}


/*


@Profile("simple")
@Configuration
class SimpleRetrofitClientFactoryBeanConfiguration {

	@Bean
	ConversionService conversionService() {
		return new DefaultConversionService();
	}

	@Bean
	RetrofitClientFactoryBean myClient(ApplicationContext context) {
		var rcfb = new RetrofitClientFactoryBean();
		rcfb.setApplicationContext(context);
		rcfb.setName("gs");
		rcfb.setType(GreetingsClient.class);
		rcfb.setUrl("http://localhost:8080/");

		return rcfb;
	}

	@Bean
	RetrofitContext retrofitContext(ObjectProvider<RetrofitClientSpecification> clientSpecifications) {
		RetrofitContext context = new RetrofitContext(DefaultRetrofitClientConfiguration.class);
		context.setConfigurations(clientSpecifications.stream().collect(Collectors.toList()));
		return context;
	}


	@Bean
	ApplicationRunner bootifulRunner(GreetingsClient gc) {
		return events -> System.out.println("result: " + gc.hello("Spring Fans!").execute().body());
	}

	@LoadBalanced
	@Bean
	OkHttpClient.Builder okHttpClientBuilder() {
		return new OkHttpClient.Builder();
	}
}

@Configuration
@Profile("raw")
class RawRetrofitConfiguration {

	@Bean
	ApplicationRunner rawRunner(ObjectFactory<HttpMessageConverters> convs,
																													ConversionService conversionService) {
		return args -> {
			var retrofit = new Retrofit.Builder()
				.baseUrl("http://localhost:8080/")
				.addConverterFactory(new SpringConverterFactory(convs, conversionService))
				.build();
			var service = retrofit.create(GreetingsClient.class);
			System.out.println(
				service
					.hello("Spring Fans")
					.execute().body()
			);
		};
	}

}

*/
