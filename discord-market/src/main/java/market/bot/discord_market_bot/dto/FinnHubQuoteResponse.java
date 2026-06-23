package market.bot.discord_market_bot.dto;



import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record FinnHubQuoteResponse(
      @JsonProperty("c")  BigDecimal currentPrice,
      @JsonProperty("d")  BigDecimal change,
      @JsonProperty("dp") BigDecimal changePercent,
      @JsonProperty("h")  BigDecimal high,
      @JsonProperty("l")  BigDecimal low,
      @JsonProperty("o")  BigDecimal open,
      @JsonProperty("pc") BigDecimal previousClose,
      @JsonProperty("t")  long timeStamp
) {
}
