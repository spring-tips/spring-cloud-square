package com.example.square;

import okhttp3.OkHttpClient;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClientConfiguration;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.square.retrofit.DefaultRetrofitClientConfiguration;
import org.springframework.cloud.square.retrofit.EnableRetrofitClients;
import org.springframework.cloud.square.retrofit.RetrofitClientFactoryBean;
import org.springframework.cloud.square.retrofit.core.AbstractRetrofitClientFactoryBean;
import org.springframework.cloud.square.retrofit.core.RetrofitClient;
import org.springframework.cloud.square.retrofit.core.RetrofitClientSpecification;
import org.springframework.context.annotation.Bean;
import org.springframework.nativex.hint.AccessBits;
import org.springframework.nativex.hint.NativeHint;
import org.springframework.nativex.hint.TypeHint;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;

@TypeHint(
	access = AccessBits.ALL,
	types = {
//		Retrofit.Builder.class,
//		ReactorLoadBalancer.class,
		AbstractRetrofitClientFactoryBean.class,
		RetrofitClientFactoryBean.class,
		RetrofitClientSpecification.class,
		DefaultRetrofitClientConfiguration.class,
		LoadBalancerClientConfiguration.class,
	})
@NativeHint(options = {" -H:+AddAllCharsets --enable-url-protocols=http,https "})

@EnableRetrofitClients
@SpringBootApplication
public class SquareApplication {

	@Bean
	@LoadBalanced
	OkHttpClient.Builder okHttpClientBuilder() {
		return new OkHttpClient.Builder();
	}

	@Bean
	ApplicationRunner runner(GreetingsClient gc) {
		return event -> System.out.println("result: " + gc.hello("Spring Fans!").execute().body());
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
