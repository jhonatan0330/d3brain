package d3.webservice.application;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

@Configuration
public class WebClientConfig {

	@Bean
	WebClient webClient() {

		ConnectionProvider provider = ConnectionProvider.builder("api-pool").maxConnections(50)
				.pendingAcquireMaxCount(100).pendingAcquireTimeout(Duration.ofSeconds(5))
				.maxIdleTime(Duration.ofSeconds(30)).build();

		HttpClient httpClient = HttpClient.create(provider).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
				.responseTimeout(Duration.ofSeconds(30)).doOnConnected(conn -> conn
						.addHandlerLast(new ReadTimeoutHandler(30)).addHandlerLast(new WriteTimeoutHandler(10)));

		int maxSize = 20 * 1024 * 1024; // 20 MB (ajusta según necesidad)

		ExchangeStrategies strategies = ExchangeStrategies.builder()
				.codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(maxSize)).build();

		return WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient))
				.exchangeStrategies(strategies).build();
	}
}
