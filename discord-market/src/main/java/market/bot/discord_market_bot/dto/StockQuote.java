package market.bot.discord_market_bot.dto;



import java.math.BigDecimal;
import java.time.Instant;

public record StockQuote(
        String symbol,
        BigDecimal currentPrice,
        BigDecimal change,
        BigDecimal changePercent,
        BigDecimal high,
        BigDecimal low,
        BigDecimal open,
        BigDecimal previousClose,
        Instant updatedAt
)
{}
