package market.bot.discord_market_bot.client;

import lombok.AllArgsConstructor;
import market.bot.discord_market_bot.exception.StockNotFoundException;
import market.bot.discord_market_bot.dto.FinnHubQuoteResponse;
import org.springframework.web.reactive.function.client.WebClient;

@AllArgsConstructor


/*
Takes a request checks the symbol (ticker) of the stocks  using HTTP.
*/

public class FinnHubClient {
    private final WebClient webClient;
    private final String apiKey;

    public FinnHubQuoteResponse fetchStockQuote(String ticker)  {
        FinnHubQuoteResponse response =  webClient.get()
                .uri(builder ->
                        builder.scheme("https")
                                .host("finnhub.io")
                                .path("/api/v1/quote")
                                .queryParam("symbol", ticker)
                                .queryParam("token", apiKey)
                                .build()


                )
                .retrieve()
                .bodyToMono(FinnHubQuoteResponse.class)
                .block();

        if (response == null || response.timeStamp() == 0) {
            throw new StockNotFoundException(ticker);
        }
        return response;
    }

}
