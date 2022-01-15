package tfip.modserver.weatherwebsite.controller;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import tfip.modserver.weatherwebsite.model.Weather;
import tfip.modserver.weatherwebsite.repo.RedisRepo;
import tfip.modserver.weatherwebsite.service.WeatherService;

@Controller
@RequestMapping(path = "/weather")
public class WeatherController {

    final private Logger logger = Logger.getLogger(WeatherController.class.getName());

    @Autowired
    WeatherService weatherService;

    @Autowired
    RedisRepo redisRepo;

    @GetMapping
    public String weather( @RequestParam(required =true) String city, Model model) {
        Weather weather;
        try {
            if (redisRepo.cityWeatherExist(city.toLowerCase())){
                weather = redisRepo.getCityWeather(city.toLowerCase());
            }
            else{
                weather = weatherService.getWeather(city);
                redisRepo.save(city.toLowerCase(),weather);
            }

            String weatherIconLink = "http://openweathermap.org/img/wn/"+weather.getWeatherIcon()+"@2x.png";
            model.addAttribute("city", city);
            model.addAttribute("weatherCondition", weather.getWeatherMain());
            model.addAttribute("weatherDesc", weather.getWeatherDesc());
            model.addAttribute("weatherIconLink", weatherIconLink);
            model.addAttribute("weatherTemp", weather.getWeatherTemp());

            // logger.info("This is from array " +weatherService.getWeather(city)[0]);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Cities not found"); 
        }

        return "weather";
    }
}
