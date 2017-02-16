package bot.commands;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.*;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import sensors.SensorsManager;

public class EngineControlCommand extends BotCommand {

    private SensorsManager sensorsManager;
    public EngineControlCommand(SensorsManager sensorsManager) {
        super("engine", "Engine control command");
        this.sensorsManager = sensorsManager;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        SendMessage answer = new SendMessage();
        String answerText = "Go away!";
        answer.setChatId(chat.getId());
        if (user.getUserName().toLowerCase().contains("sometea")) {
            if (strings.length != 0) {
                switch (strings[0]) {
                    case "on":
                        sensorsManager.setEngineState(true);
                        answerText = "You have turned engine on";
                        break;
                    case "off":
                        sensorsManager.setEngineState(false);
                        answerText = "You have turned engine off";
                        break;
                }
            } else {
                if (sensorsManager.getEngineState()) {
                    answerText = "Engine is on";
                } else {
                    answerText = "Engine is off";
                }
            }
        }
        answer.setText(answerText);
        try {
            absSender.sendMessage(answer);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
