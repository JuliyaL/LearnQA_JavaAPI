package lib;

import org.apache.commons.lang3.RandomStringUtils;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DataGenerator {
    public static String getRandomEmail() {
        String timestamp = new SimpleDateFormat("yyyMMddHHmmss").format(new java.util.Date());
        return "learnqa" + timestamp + "@example.com";
    }
    public static String getRandomInvalidEmail() {
        String timestamp = new SimpleDateFormat("yyyMMddHHmmss").format(new java.util.Date());
        return "learnqa" + timestamp + "example.com";
    }
    public static Map<String, String> getRegistrationData() {
        Map<String, String> data = new HashMap<>();
        data.put("email", DataGenerator.getRandomEmail());
        data.put("password", "123");
        data.put("username", "learnqa");
        data.put("firstName", "learnqa");
        data.put("lastName", "learnqa");

        return data;
    }

    public static Map<String, String> getRegistrationData(Map<String, String> nonDefaultValues) {
        Map<String, String> defaultValues = DataGenerator.getRegistrationData();

        Map<String, String> userData = new HashMap<>();
        String[] keys = {"email", "password", "username", "firstName", "lastName"};
        for(String key: keys) {
            if(nonDefaultValues.containsKey(key)) {
                userData.put(key, nonDefaultValues.get(key));
            } else {
                userData.put(key, defaultValues.get(key));
            }
        }
        return userData;
    }
    public static Map<String, String> getRegistrationDataWithoutOneField(String fieldkey) {
        Map<String, String> defaultValues = DataGenerator.getRegistrationData();

        Map<String, String> userData = new HashMap<>();
        String[] keys = {"email", "password", "username", "firstName", "lastName"};
        for(String key: keys) {
            if(!key.equals(fieldkey)) {
                userData.put(key, defaultValues.get(key));
            }
        }

        return userData;
    }

    public static Map<String, String> getRegistrationDataWithShortName(String fieldkey) {
        Map<String, String> shortName = new HashMap<>();
        shortName.put(fieldkey, "k");
        return getRegistrationData(shortName);

    }

    public static Map<String, String> getRegistrationDataWithLongName(String fieldKey) {
        String generatedString = RandomStringUtils.randomAlphabetic(260);
        Map<String, String> longName = new HashMap<>();
        longName.put(fieldKey, generatedString);
        return getRegistrationData(longName);
          }


}
