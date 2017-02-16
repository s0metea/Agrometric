package publisher;

import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import sensors.SensorsManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Publisher {
    private static final String version = "0.1 Beta";
    private volatile boolean shouldPublish = true; //For publisher thread
    private volatile boolean shouldAlive = true; //For maint thread with control options
    private volatile boolean isAlreadyPublishing = false;
    private PNConfiguration pnConfiguration;
    private String channelName = "main-channel";
    private PubNub pubNub;
    private SensorsManager sensorsManager;

    private String input;
    private BufferedReader br;

    public Publisher(String publishKey, SensorsManager sensorsManager) {
        this.sensorsManager = sensorsManager;

        this.pnConfiguration = new PNConfiguration();
        this.pnConfiguration.setPublishKey(publishKey);
        this.pnConfiguration.setSubscribeKey("sub-c-8101ca80-bf08-11e6-b38f-02ee2ddab7fe");
        this.pnConfiguration.setSecure(false);
        this.pubNub = new PubNub(this.pnConfiguration);

        br = new BufferedReader(new InputStreamReader(System.in));
    }

    public final void start() {
        if(!isAlreadyPublishing) {
            Thread publisher = new Thread(() -> {
                while (shouldPublish) {
                    pubNub.publish()
                            .message(sensorsManager.getJSON())
                            .channel(channelName)
                            .shouldStore(true)
                            .usePOST(true)
                            .async(new PNCallback<PNPublishResult>() {
                                @Override
                                public void onResponse(PNPublishResult result, PNStatus status) {
                                    if (status.isError()) {
                                        // something bad happened.
                                        System.out.println("Error while message publishing. Status: " + status.toString());
                                    }
                                }
                            });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println("Can't sleep :(");
                    }
                }
            });
            publisher.start();
            System.out.println("Publisher v. " + version + "\nEnter the command or type \"?\" for help.\n> ");
            while (shouldAlive) {
                startCommandHandler();
            }
            pubNub.destroy();
        } else {
            System.out.println("Another instance of publisher has already started!\n");
        }
    }

    public static void printHelp() {
        System.out.println("publisher.Publisher v." + version + " help:\n Type:\n\"stop\" for stop publishing.\n" +
                "\"start\" for publishing resume\n" +
                "\"?\" for display this help again. (Actually you have already know this command if " +
                "you are reading this text now.)\n> ");
    }

    private void startCommandHandler() {
        try {
            input = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.print("Error while reading command, try again:\n> ");
            input = "";
        }
        switch (input.toLowerCase()) {
            case "stop":
                shouldPublish = false;
                System.out.println("Publishing had been stopped.");
                break;
            case "start":
                shouldPublish = true;
                this.start();
                System.out.println("Publishing had been started.");
                break;
            case "exit":
                System.out.println("Good luck!\npublisher.Publisher is going to stop...");
                shouldAlive = false;
                break;
            case "?":
                printHelp();
                break;
        }
    }
}
