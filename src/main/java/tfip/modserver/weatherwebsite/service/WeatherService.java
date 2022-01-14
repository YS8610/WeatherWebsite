package tfip.modserver.weatherwebsite.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class WeatherService {
    
    final private String API = "";

    public String[] getWeather(String city) throws IOException {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API + "&units=metric";
        RestTemplate template = new RestTemplate();

        RequestEntity<Void> req = RequestEntity.get(url)
            .accept(MediaType.APPLICATION_JSON)
            .build();

        ResponseEntity<String> resp = template.exchange(req, String.class);
        
        try( InputStream inputStream = new ByteArrayInputStream(resp.getBody().getBytes()) ){
            JsonReader reader = Json.createReader(inputStream);
            JsonObject data = reader.readObject();

            JsonArray weatherArray = data.getJsonArray("weather");
            JsonObject weatherJson = weatherArray.getJsonObject(0);
            JsonObject mainJson = data.getJsonObject("main");
            
            String weatherMain = weatherJson.getString("main");
            String weatherDesc = weatherJson.getString("description");
            String weatherIcon = weatherJson.getString("icon");
            String mainTemp = mainJson.getJsonNumber("temp").toString();
            
            return new String[] {weatherMain,weatherDesc,weatherIcon,mainTemp};
        }
    }
}
