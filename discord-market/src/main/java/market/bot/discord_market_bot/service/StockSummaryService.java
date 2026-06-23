package market.bot.discord_market_bot.service;


import market.bot.discord_market_bot.dto.StockQuote;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Locale;

@Service
public class StockSummaryService {

    public String summarize(StockQuote quote) {
        BigDecimal changePercent = quote.changePercent();
        String dir;

        if (changePercent.compareTo(BigDecimal.ZERO) > 0) {
            dir = "increased";
        } else if (changePercent.compareTo(BigDecimal.ZERO)  < 0 ) {
            dir = "decreased";
        } else {
            dir = "Consolidating";
        }

        return String.format(
                Locale.US,
                """
                %s %s by %.2f%% today.
                It opened at $%.2f and is currently trading at $%.2f.
                Today's range was $%.2f to $%.2f.
                The previous closing price was $%.2f.
                """,
                quote.symbol(),
                dir,
                quote.changePercent().abs(),
                quote.open(),
                quote.currentPrice(),
                quote.low(),
                quote.high(),
                quote.previousClose()
        );

    }

}
