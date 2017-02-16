import bot.AgroSystemBot;

import control.ControllerManager;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import publisher.Publisher;
import sensors.SensorsManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class Main {

    static {
        try {
            System.loadLibrary("mraajava");
        } catch (UnsatisfiedLinkError e) {
            System.err.println(
                    "Native code library failed to load. See the chapter on Dynamic Linking Problems in the SWIG Java documentation for help.\n" +
                            e);
            System.exit(1);
        }
    }

    public static void main(String[] args) {

        System.out.println("Welcome to Agrometric demonstration!");
        boolean enableBot = false;
        boolean enablePubNub = true;
        boolean enableController = true;

        SensorsManager sensorsManager = new SensorsManager();

        Scanner sc = null;
        try {
            sc = new Scanner(new File("./tokens"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String pubNubToken = sc.nextLine();
        String telegramToken = sc.nextLine();

        //Publishing data from sensors trough JSON using PubNub service
        Thread publisherThread = new Thread(() -> {
            Publisher publisher = new Publisher(pubNubToken, sensorsManager);
            publisher.start();
        });


        ControllerManager controllerManager = new ControllerManager();
        Thread controllerThread = new Thread(() ->{
            controllerManager.start(sensorsManager);
        });


        //Bot Assistant
        Thread botThread = new Thread(() -> {
            AgroSystemBot agroSystemBot;
            System.out.println("AgroSystem Telegram Assistant");
            ApiContextInitializer.init();
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
            DefaultBotOptions defaultBotOptions = new DefaultBotOptions();
            agroSystemBot = new AgroSystemBot(defaultBotOptions,true, telegramToken, sensorsManager);
            try {
                telegramBotsApi.registerBot(agroSystemBot);
            } catch (TelegramApiRequestException e) {
                e.printStackTrace();
            }
        });

        if(enableBot) {
            System.out.println("Starting bot...");
            botThread.start();
            System.out.println("Bot has been started!");
        }
        if(enablePubNub) {
            System.out.println("Starting PubNub stream...");
            publisherThread.start();
            System.out.println("PubNub has been started...");
        }
        if(enableController) {
            System.out.println("Starting PS4 controller handler...");
            controllerThread.start();
            System.out.println("PS4 controller handle has been activated");
        }
    }
}
