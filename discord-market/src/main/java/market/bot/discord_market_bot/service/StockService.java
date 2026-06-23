package market.bot.discord_market_bot.service;


import lombok.RequiredArgsConstructor;
import market.bot.discord_market_bot.client.FinnHubClient;
import market.bot.discord_market_bot.dto.FinnHubQuoteResponse;
import market.bot.discord_market_bot.dto.StockQuote;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class StockService {
    private FinnHubClient finnHubClient;


    public StockQuote getStockQuote(String ticker) {
        FinnHubQuoteResponse quote = finnHubClient.fetchStockQuote(ticker);
        return new StockQuote(
                ticker,
                quote.currentPrice(),
                quote.change(),
                quote.changePercent(),
                quote.high(),
                quote.low(),
                quote.open(),
                quote.previousClose(),
                Instant.ofEpochSecond(quote.timeStamp())
        );
    }
}
