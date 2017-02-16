package bot;

import bot.commands.EngineControlCommand;
import org.telegram.telegrambots.api.objects.*;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingCommandBot;
import sensors.SensorsManager;

import java.util.Random;

public class AgroSystemBot extends TelegramLongPollingCommandBot {
    private Random random = new Random();
    private String botToken = "";
    private SensorsManager sensorsManager;
    public AgroSystemBot(DefaultBotOptions options, boolean allowCommandsWithUsername, String botToken, SensorsManager sensorsManager) {
        super(options, allowCommandsWithUsername);
        this.botToken = botToken;
        this.sensorsManager = sensorsManager;
        register(new EngineControlCommand(sensorsManager));
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        System.out.println(update.getMessage().getText());
    }

    @Override
    public String getBotUsername() {
        return "AgroSystemBot";
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onClosing() {
    }

}
