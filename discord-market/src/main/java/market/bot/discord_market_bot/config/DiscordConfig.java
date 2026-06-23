package market.bot.discord_market_bot.config;

import market.bot.discord_market_bot.discord.DiscordBotListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DiscordConfig {

    @Bean(destroyMethod = "shutdown")
    public JDA jda(
            @Value("${discord.bot.token}") String token,
            DiscordBotListener discordBotListener
    ) throws InterruptedException {

        JDA jda = JDABuilder.createDefault(token)
                .addEventListeners(discordBotListener)
                .build()
                .awaitReady();

        jda.updateCommands()
                .addCommands(
                        Commands.slash(
                                        "stock",
                                        "Get the current quote for a stock"
                                )
                                .addOption(
                                        OptionType.STRING,
                                        "ticker",
                                        "Stock ticker such as GOOGL or AAPL",
                                        true
                                )
                )
                .queue();

        return jda;
    }
}