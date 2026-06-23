package market.bot.discord_market_bot.discord;

import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

@AllArgsConstructor
public class DiscordBotListener extends ListenerAdapter {


    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("stock")) {
            return;
        }

        OptionMapping ticker = event.getOption("ticker");

        if (ticker == null) {
            event.reply("You must provide a ticker.").queue();
            return;
        }

        String tick = ticker.getAsString();
        event.reply("Ticker received: " + ticker).queue();

    }
}


