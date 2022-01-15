package tfip.modserver.weatherwebsite.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Weather {
    private String weatherMain;
    private String weatherDesc;
    private String weatherIcon;
    private String weatherTemp;
    private String city;
}
