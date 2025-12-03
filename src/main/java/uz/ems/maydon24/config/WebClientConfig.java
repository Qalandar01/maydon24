package uz.ems.maydon24.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.netty.http.client.HttpClient;



@Configuration
public class WebClientConfig {

    @Bean("brbClient")
    public WebClient brbClient() {
        ObjectMapper objectMapper = new ObjectMapper();
        var exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> {
                    configurer.defaultCodecs().maxInMemorySize(30 * 1024 * 1024);
                    configurer.defaultCodecs().jackson2JsonDecoder(
                            new Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON));
                    configurer.defaultCodecs().jackson2JsonEncoder(
                            new Jackson2JsonEncoder(objectMapper, MediaType.APPLICATION_JSON));
                }).build();
        return WebClient.builder()
                .baseUrl("")
                .exchangeStrategies(exchangeStrategies)
                .clientConnector(customClientHttpConnector(10000, 60, 30))
                .build();

    }

    public static ClientHttpConnector customClientHttpConnector(int connectionTimeOutMillSec, int readTimeOutSec, int writeTimeoutSec) {
        HttpClient httpClient = HttpClient
                .create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeOutMillSec)
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(readTimeOutSec))
                        .addHandlerLast(new WriteTimeoutHandler(writeTimeoutSec)));
        return new ReactorClientHttpConnector(httpClient.wiretap(true));
    }
}