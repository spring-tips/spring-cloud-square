package com.example.square;

import okhttp3.OkHttpClient;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.square.retrofit.EnableRetrofitClients;
import org.springframework.cloud.square.retrofit.core.RetrofitClient;
import org.springframework.cloud.square.retrofit.support.SpringConverterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.nativex.hint.AccessBits;
import org.springframework.nativex.hint.JdkProxyHint;
import org.springframework.nativex.hint.NativeHint;
import org.springframework.nativex.hint.TypeHint;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;
@NativeHint (options = " -H:+AddAllCharsets ")
@JdkProxyHint(types = GreetingsClient.class)
@TypeHint(types = GreetingsClient.class, access = AccessBits.ALL)
@SpringBootApplication
// @EnableRetrofitClients
public class SquareApplication {

	public static void main(String[] args) {
		SpringApplication.run(SquareApplication.class, args);
	}

	@Bean
	ConversionService conversionService() {
		return new DefaultConversionService();
	}

	@Bean
	ApplicationRunner rawRunner (ObjectFactory<HttpMessageConverters> convs,
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

/*

	@Bean
	ApplicationRunner runner(GreetingsClient gc) {
		return events -> {
			for (int i = 0; i < 3; i++)
				System.out.println("result: " + gc.hello("Spring Fans!").get());
		};
	}
*/

	@Bean
//	@LoadBalanced
	OkHttpClient.Builder okHttpClientBuilder() {
		return new OkHttpClient.Builder();
	}
}

/*@RetrofitClient(
	"greeting-service"
)*/
//@RetrofitClient(
//	name = "greetingsClient",
//	url = "http://localhost:8080"
//)
interface GreetingsClient {

	@GET("/hello/{name}")
	Call<String> hello(@Path("name") String name);
}