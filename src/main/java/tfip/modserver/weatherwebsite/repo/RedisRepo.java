package tfip.modserver.weatherwebsite.repo;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.stream.JsonParser;
import jakarta.json.stream.JsonParser.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import tfip.modserver.weatherwebsite.model.Weather;

import java.io.StringReader;
import java.util.concurrent.TimeUnit;

@Repository
public class RedisRepo {

    @Autowired
    @Qualifier("redisDB")
    private RedisTemplate<String, String> template;

    private String jsonParser(String jsonString , String key){
        JsonParser parser = Json.createParser( new StringReader(jsonString) );
        Event event = parser.next();
        String stringwithQuote = parser.getObjectStream().filter(e -> e.getKey().equalsIgnoreCase(key))
                .map(e->e.getValue().toString())
                .reduce("",(s0,s1) -> s0+s1);
        parser.close();
        return stringwithQuote.substring(1,stringwithQuote.length()-1);
    }

    public void save(String city, Weather weather){
        JsonObject weatherJson = Json.createObjectBuilder()
                .add("weatherMain",weather.getWeatherMain() )
                .add("weatherDesc", weather.getWeatherDesc() )
                .add("weatherIcon",weather.getWeatherIcon() )
                .add("weatherTemp",weather.getWeatherTemp() )
                .add("city", weather.getCity() )
                .build();
        template.opsForValue().set(city,weatherJson.toString(),5, TimeUnit.MINUTES);
    }

    public Weather getCityWeather(String city){
            String cityWeatherJsonString = template.opsForValue().get(city);
            String weatherMain = jsonParser(cityWeatherJsonString,"weatherMain");
            String weatherDesc = jsonParser(cityWeatherJsonString,"weatherDesc");
            String weatherIcon = jsonParser(cityWeatherJsonString,"weatherIcon");
            String weatherTemp = jsonParser(cityWeatherJsonString,"weatherTemp");
            return new Weather(weatherMain,weatherDesc,weatherIcon,weatherTemp,city);
    }

    public boolean cityWeatherExist(String city){
        return template.hasKey(city);
    }
}
