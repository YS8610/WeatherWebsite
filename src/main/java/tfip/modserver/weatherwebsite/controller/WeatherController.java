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

import tfip.modserver.weatherwebsite.service.WeatherService;

@Controller
@RequestMapping(path = "/weather")
public class WeatherController {

    final private Logger logger = Logger.getLogger(WeatherController.class.getName());

    @Autowired
    WeatherService weatherService;

    @GetMapping
    public String weather( @RequestParam(required =true) String city, Model model) {

        try {
            String weatherCondition = weatherService.getWeather(city)[0];
            String weatherDesc = weatherService.getWeather(city)[1];
            String weatherIcon = weatherService.getWeather(city)[2];
            String weatherTemp = weatherService.getWeather(city)[3];

            String weatherIconLink = "http://openweathermap.org/img/wn/"+weatherIcon+"@2x.png";
            
            model.addAttribute("city", city);
            model.addAttribute("weatherCondition", weatherCondition);
            model.addAttribute("weatherDesc", weatherDesc);
            model.addAttribute("weatherIconLink", weatherIconLink);
            model.addAttribute("weatherTemp", weatherTemp);

            // logger.info("This is from array " +weatherService.getWeather(city)[0]);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Cities not found"); 
        }

        return "weather";
    }
}
