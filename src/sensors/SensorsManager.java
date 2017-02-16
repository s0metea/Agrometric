package sensors;

import com.google.gson.JsonObject;
import upm_grove.GroveTemp;

import java.util.Random;

public class SensorsManager {
    private boolean isEngineOn = true;
    private float moisture;
    private float temperature;
    private float speed;
    private float ph;
    private float pressure;
    private float waterLevel = 100;
    private float lat = 30.545f;
    private float lng = 59.854f;
    private float fuel = 100;


    private GroveTemp tempSensor;


    private JsonObject sensorsJSON;
    private JsonObject payload;
    private JsonObject latLng;

    private Random random; //For debug purposes only.

    public SensorsManager() {
        random = new Random();
        //Now we will create JSON with the following structure:
        //{
        //  "eon": {
        //      "gps": {
        //          "lat": 0,
        //          "lng": 0
        //              },
        //      "moisture": 95,
        //      "temperature": 26,
        //      "speed": 41,
        //      "ph": 7
        //  }
        //}
        //
        //And so on...

        sensorsJSON = new JsonObject();
        payload = new JsonObject();
        latLng = new JsonObject();
        payload.add("latLng", latLng);
        sensorsJSON.add("eon", payload);

        //Sensors init:
         tempSensor = new GroveTemp(1);

        //TODO: Don't forget to remove fake data generation!
        updateDataFromSensors();
    }

    public JsonObject getJSON() {
        //Filling the JSON with fresh data before sending it
        payload.addProperty("moisture", moisture);
        payload.addProperty("temperature", temperature);
        payload.addProperty("speed", speed);
        payload.addProperty("ph", ph);
        payload.addProperty("temperature", temperature);
        payload.addProperty("waterLevel", waterLevel);
        payload.addProperty("pressure", pressure);
        payload.addProperty("fuel", fuel);
        latLng.addProperty("lat", lat);
        latLng.addProperty("lng", lng);
        return sensorsJSON;
    }

    private void updateDataFromSensors() {
        Thread t = new Thread(() -> {
            while (fuel > 0 && waterLevel > 0) {
                if(isEngineOn) {
                    fuel -= 0.1;
                    waterLevel -= 0.01;
                }
                moisture = 95 + Math.round(random.nextFloat());
                if (random.nextBoolean())
                    pressure = 750 + Math.round(random.nextFloat() * 10);
                else
                    pressure = 750 - Math.round(random.nextFloat() * 10);
                ph = 7 + Math.round(random.nextFloat() / 10);
                lat += 0.0001;

                temperature = tempSensor.value();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    public boolean getEngineState() {
        return isEngineOn;
    }

    public void setEngineState(boolean state) {
        isEngineOn = state;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }

    public float getFuel() {
        return fuel;
    }

    public float getMoisture() {
        return moisture;
    }

    public void setMoisture(float moisture) {
        this.moisture = moisture;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getPh() {
        return ph;
    }

    public void setPh(float ph) {
        this.ph = ph;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public float getWaterLevel() {
        return waterLevel;
    }

    public void setWaterLevel(float waterLevel) {
        this.waterLevel = waterLevel;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public JsonObject getSensorsJSON() {
        return sensorsJSON;
    }
}
