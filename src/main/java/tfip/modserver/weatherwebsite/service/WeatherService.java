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
import tfip.modserver.weatherwebsite.model.Weather;

import static tfip.modserver.weatherwebsite.Constants.*;

@Service
public class WeatherService {
    
    final private String API;


    public WeatherService(){
        String k = System.getenv(ENV_OPENWEATHERMAP_KEY);
        if (null!=k && !k.isBlank()){
            API = k;
        }
        else{
            API= "307dba7c340e61ba1e4dde65b5e24eb9";
        }
    }

    public Weather getWeather(String city) throws IOException {
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
            String weatherTemp = mainJson.getJsonNumber("temp").toString();

            Weather weather = new Weather(weatherMain,weatherDesc,weatherIcon,weatherTemp,city);

            return weather;
        }
    }
}
