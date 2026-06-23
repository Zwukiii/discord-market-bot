package market.bot.discord_market_bot.discord;

import market.bot.discord_market_bot.dto.StockQuote;
import market.bot.discord_market_bot.service.StockService;
import market.bot.discord_market_bot.service.StockSummaryService;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class DiscordBotListener extends ListenerAdapter {

    private final StockService stockService;
    private final StockSummaryService stockSummaryService;

    public DiscordBotListener(
            StockService stockService,
            StockSummaryService stockSummaryService
    ) {
        this.stockService = stockService;
        this.stockSummaryService = stockSummaryService;
    }

    @Override
    public void onSlashCommandInteraction(
            SlashCommandInteractionEvent event
    ) {
        switch (event.getName()) {
            case "stock" -> handleStockCommand(event);
            case "crypto" -> handleCryptoCommand(event);
            default -> {
                // Ignore commands this listener does not handle.
            }
        }
    }

    private void handleStockCommand(
            SlashCommandInteractionEvent event
    ) {
        OptionMapping tickerOption = event.getOption("ticker");

        if (tickerOption == null) {
            event.reply("You must provide a stock ticker.")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        String ticker = tickerOption.getAsString()
                .trim()
                .toUpperCase(Locale.ROOT);

        event.deferReply().queue();

        try {
            StockQuote quote = stockService.getStockQuote(ticker);
            String stockSummary = stockSummaryService.summarize(quote);

            String response = String.format(
                    Locale.US,
                    """
                    **%s Stock Quote**
                    Current price: $%.2f
                    Change: %.2f
                    Change percent: %.2f%%
                    High: $%.2f
                    Low: $%.2f
                    Open: $%.2f
                    Previous close: $%.2f

                    **Summary**
                    %s
                    """,
                    quote.symbol(),
                    quote.currentPrice(),
                    quote.change(),
                    quote.changePercent(),
                    quote.high(),
                    quote.low(),
                    quote.open(),
                    quote.previousClose(),
                    stockSummary
            );

            event.getHook()
                    .sendMessage(response)
                    .queue();

        } catch (Exception exception) {
            exception.printStackTrace();

            event.getHook()
                    .sendMessage(
                            "Could not retrieve stock data for `"
                                    + ticker
                                    + "`."
                    )
                    .queue();
        }
    }

    private void handleCryptoCommand(
            SlashCommandInteractionEvent event
    ) {
        OptionMapping symbolOption = event.getOption("symbol");

        if (symbolOption == null) {
            event.reply("You must provide a crypto symbol.")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        String symbol = symbolOption.getAsString()
                .trim()
                .toUpperCase(Locale.ROOT);

        String finnhubSymbol;

        try {
            finnhubSymbol = toFinnhubCryptoSymbol(symbol);
        } catch (IllegalArgumentException exception) {
            event.reply(exception.getMessage())
                    .setEphemeral(true)
                    .queue();
            return;
        }

        event.reply(
                        "Crypto received: **"
                                + symbol
                                + "**\nFinnhub symbol: `"
                                + finnhubSymbol
                                + "`"
                )
                .queue();
    }

    private String toFinnhubCryptoSymbol(String symbol) {
        return switch (symbol) {
            case "SOL" -> "BINANCE:SOLUSDT";
            case "BTC" -> "BINANCE:BTCUSDT";
            case "ETH" -> "BINANCE:ETHUSDT";
            default -> throw new IllegalArgumentException(
                    "Unsupported crypto symbol: `"
                            + symbol
                            + "`. Try SOL, BTC, or ETH."
            );
        };
    }
}