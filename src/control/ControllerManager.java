package control;


import mraa.Dir;
import mraa.Gpio;
import net.java.games.input.*;
import sensors.SensorsManager;

import java.util.Random;
import java.util.Stack;

import static java.lang.Thread.sleep;

public class ControllerManager {

    private boolean shouldControl = true;
    private int PINDIR1 = 4;
    private int PINDIR2 = 7;
    private int PINPWR1 = 5;
    private int PINPWR2 = 6;
    private int lat = 0;
    private int lng = 0;



    private Gpio leftWheelsDir, rightWheelsDir;
    private Gpio leftWheelsSpeed, rightWheelsSpeed;
    private SensorsManager sensorsManager;
    private Random random;
    public ControllerManager() {
        initialize();
    }

    public ControllerManager(int PINDIR1, int PINDIR2, int PINPWR1, int PINPWR2) {
        this.PINDIR1 = PINDIR1;
        this.PINDIR2 = PINDIR2;
        this.PINPWR1 = PINPWR1;
        this.PINPWR2 = PINPWR2;
        initialize();
    }

    private void initialize() {
        leftWheelsDir = new Gpio(PINDIR1, false);
        rightWheelsDir = new Gpio(PINDIR2, false);
        leftWheelsSpeed = new Gpio(PINPWR1, false);
        rightWheelsSpeed = new Gpio(PINPWR2, false);
        leftWheelsDir.dir(Dir.DIR_OUT);
        rightWheelsDir.dir(Dir.DIR_OUT);
        leftWheelsSpeed.dir(Dir.DIR_OUT);
        rightWheelsSpeed.dir(Dir.DIR_OUT);
        leftWheelsSpeed.write(0);
        rightWheelsSpeed.write(0);
        random = new Random();
    }

    public void start(SensorsManager sensorsManager) {
        this.sensorsManager = sensorsManager;
        Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
        if (controllers.length == 0) {
            System.out.println("Found no controllers.");
            System.exit(0);
        }
        Controller ps4 = null;

        for (Controller controller : controllers) {
            if (controller.getType().toString().equals("Gamepad")) {
                ps4 = controller;
            }
        }
        if (ps4 == null) {
            System.out.println("Can't find PS4 gamepad!");
            System.exit(1);
        }
        ps4.setEventQueueSize(10);
        Event event = new Event();
        while (shouldControl) {
            ps4.poll();
            while (ps4.getEventQueue().getNextEvent(event)) {
                float value = event.getValue();
                String button = event.getComponent().getIdentifier().toString();
                if(!button.equals("x") && !button.equals("y") && !button.equals("z")) {
                //    System.out.println(button);
                }
                switch (button) {
                    case "ry":
                        leftWheelsDir.write(1);
                        rightWheelsDir.write(0);
                        System.out.println("Moving forward");
                        setSpeed(1);
                        System.out.println(lat);
                        lat=lat+1;
                        break;
                    case "rx":
                        leftWheelsDir.write(0);
                        rightWheelsDir.write(1);
                        System.out.println("Moving backward");
                        setSpeed(1);
                        System.out.println(lat);
                        lat=lat-1;
                        break;
                    case "A":
                        System.out.println("Stop it!");
                        setSpeed(0);
                        break;
                    case "B":
                        if(lng<0)
                            while (lng<0) {
                                leftWheelsDir.write(1);
                                rightWheelsDir.write(1);
                                System.out.println("Turning right");
                                turnRight();
                                lng = lng + 1;
                            }
                        if(lng>0)
                            while (lng<0) {
                                leftWheelsDir.write(0);
                                rightWheelsDir.write(0);
                                System.out.println("Turning left");
                                turnLeft();
                                lng = lng - 1;
                            }
                        if(lat>0)
                            while (lat>0) {
                                leftWheelsDir.write(0);
                                rightWheelsDir.write(1);
                                System.out.println("Moving backward");
                                setSpeed(1);
                                System.out.println(lat);
                                lat = lat - 1;
                            }
                        if(lat<0)
                            while (lat<0) {
                                leftWheelsDir.write(1);
                                rightWheelsDir.write(0);
                                System.out.println("Moving forward");
                                System.out.println(lat);
                                setSpeed(1);
                                System.out.println(lat);
                                lat = lat + 1;
                            }
                                break;
                    case "Y":
                        leftWheelsDir.write(0);
                        rightWheelsDir.write(0);
                        System.out.println("Turning left");
                        System.out.println(lng);
                        lng=lng-1;
                        System.out.println(lng);
                        turnLeft();
                        break;
                    case "Z":
                        leftWheelsDir.write(1);
                        rightWheelsDir.write(1);
                        System.out.println("Turning right");
                        System.out.println(lng);
                        turnRight();
                        lng=lng+1;
                        break;
                }
            }
            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            shouldControl = sensorsManager.getEngineState();
        }
    }

    public void turnLeft() {
        rightWheelsSpeed.write(0);
        leftWheelsSpeed.write(1);
    }

    public void turnRight() {
        leftWheelsSpeed.write(0);
        rightWheelsSpeed.write(1);
    }

    private void setSpeed(int speed) {
        leftWheelsSpeed.write(speed);
        rightWheelsSpeed.write(speed);
        if(speed == 0) {
            sensorsManager.setSpeed(speed);
        } else {
            sensorsManager.setSpeed(Math.round(random.nextFloat()) + 15);
        }
    }
}