package com.example.square;

import okhttp3.OkHttpClient;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.square.retrofit.EnableRetrofitClients;
import org.springframework.cloud.square.retrofit.core.RetrofitClient;
import org.springframework.context.annotation.Bean;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

@SpringBootApplication
@EnableRetrofitClients
public class SquareApplication {

    public static void main(String[] args) {
        SpringApplication.run(SquareApplication.class, args);
    }

    @Bean
    ApplicationRunner runner(GreetingsClient gc) {
        return events -> System.out.println("result: " + gc.hello("Spring Fans!").execute().body());
    }

    @Bean
    @LoadBalanced
    OkHttpClient.Builder okHttpClientBuilder() {
        return new OkHttpClient.Builder();
    }
}

@RetrofitClient(
        name = "greetingsClient",
        url = "http://localhost:8080"
)
interface GreetingsClient {

    @GET("/hello/{name}")
    Call<String> hello(@Path("name") String name);
}