package market.bot.discord_market_bot.exception;

public class StockNotFoundException extends RuntimeException {
    public StockNotFoundException(String message) {
        super("Stock with that ticker does not exist! " +  message);
    }
}
