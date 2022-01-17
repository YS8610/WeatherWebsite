package tfip.modserver.weatherwebsite.controller;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tfip.modserver.weatherwebsite.model.Weather;
import tfip.modserver.weatherwebsite.repo.RedisRepo;
import tfip.modserver.weatherwebsite.service.WeatherService;

import java.io.IOException;

@RestController
@RequestMapping(path = "/weather")
public class WeatherRestController {
   @Autowired
   private WeatherService weatherService;

   @Autowired
   private RedisRepo redisRepo;

   @GetMapping(path = "{city}", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<String> getCityWeather(@PathVariable(name = "city") String city) {
       ResponseEntity<String> responseEntity = new ResponseEntity<String>("Error",HttpStatus.OK);
       Weather weather;
       try {
           if (redisRepo.cityWeatherExist(city.toLowerCase())){
               weather = redisRepo.getCityWeather(city.toLowerCase());
           }
           else{
               weather = weatherService.getWeather(city);
               redisRepo.save(city.toLowerCase(),weather);
           }

           JsonObject jsonRestObj = Json.createObjectBuilder()
                   .add("weatherCondition",weather.getWeatherMain())
                   .add("weatherDesc",weather.getWeatherDesc())
                   .add("weatherIconLink",weather.getWeatherIcon())
                   .add("weatherTemp", weather.getWeatherTemp())
                   .add("city",city)
                   .build();
           return new ResponseEntity<String>(jsonRestObj.toString(), HttpStatus.OK);
       }
       catch (IOException e) {
           e.printStackTrace();
       }
       return responseEntity;
   }
}
