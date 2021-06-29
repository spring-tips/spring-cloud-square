package com.example.square;

import okhttp3.OkHttpClient;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.square.retrofit.DefaultRetrofitClientConfiguration;
import org.springframework.cloud.square.retrofit.EnableRetrofitClients;
import org.springframework.cloud.square.retrofit.RetrofitClientFactoryBean;
import org.springframework.cloud.square.retrofit.core.AbstractRetrofitClientFactoryBean;
import org.springframework.cloud.square.retrofit.core.RetrofitClient;
import org.springframework.cloud.square.retrofit.core.RetrofitClientSpecification;
import org.springframework.cloud.square.retrofit.core.RetrofitContext;
import org.springframework.cloud.square.retrofit.support.SpringConverterFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.nativex.hint.*;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.stream.Collectors;

// todo write a NativeConfiguration that finds all the beans with @RetrofitClient on
// it as i did in the Retrosocket project

@ResourceHint(
	patterns = {
		"org/springframework/cloud/square/okhttp/loadbalancer/OkHttpLoadBalancerAutoConfiguration.class",
		"org/springframework/cloud/square/retrofit/RetrofitAutoConfiguration.class"
	}
)
@NativeHint(options = {" -H:+AddAllCharsets --enable-url-protocols=http "})
@JdkProxyHint(types = GreetingsClient.class)
@TypeHint(typeNames = {


	"com.oracle.svm.core.hub.Target_java_lang_invoke_TypeDescriptor_OfField[]",
	"jdk.vm.ci.meta.JavaKind$FormatWithToString[]",
	"java.lang.reflect.AnnotatedElement[]",
	"java.lang.reflect.GenericDeclaration[]"})

@TypeHint(types = {
	GreetingsClient.class,
	DefaultRetrofitClientConfiguration.class,
	RetrofitClientSpecification.class,
	EnableRetrofitClients.class,
	AbstractRetrofitClientFactoryBean.class,
	RetrofitClientFactoryBean.class,},
	access = AccessBits.ALL)
@SpringBootApplication
//@EnableRetrofitClients
public class SquareApplication {

	public static void main(String[] args) {
		SpringApplication.run(SquareApplication.class, args);
	}
}

@Configuration
@EnableRetrofitClients
class AutoRetrofitClientConfiguration {


	@Bean
	ApplicationRunner bootifulRunner(GreetingsClient gc) {
		return events -> {
			System.out.println("result: " + gc.hello("Spring Fans!").execute().body());
		};
	}


}

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
		return events -> {
			System.out.println("result: " + gc.hello("Spring Fans!").execute().body());
		};
	}

	//	@LoadBalanced
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

/*@RetrofitClient(
	"greeting-service"
)*/
@RetrofitClient(
	name = "greetingsClient",
	url = "http://localhost:8080"
)
interface GreetingsClient {

	@GET("/hello/{name}")
	Call<String> hello(@Path("name") String name);
}