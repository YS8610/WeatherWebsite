package tfip.modserver.weatherwebsite.model;

public class Weather {
    private String weatherMain;
    private String weatherDesc;
    private String weatherIcon;
    private String weatherTemp;
    private String city;

    public Weather(String weatherMain, String weatherDesc, String weatherIcon, String weatherTemp, String city) {
        this.weatherMain = weatherMain;
        this.weatherDesc = weatherDesc;
        this.weatherIcon = weatherIcon;
        this.weatherTemp = weatherTemp;
        this.city = city;
    }

    public String getWeatherMain() {
        return weatherMain;
    }

    public void setWeatherMain(String weatherMain) {
        this.weatherMain = weatherMain;
    }

    public String getWeatherDesc() {
        return weatherDesc;
    }

    public void setWeatherDesc(String weatherDesc) {
        this.weatherDesc = weatherDesc;
    }

    public String getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public String getWeatherTemp() {
        return weatherTemp;
    }

    public void setWeatherTemp(String weatherTemp) {
        this.weatherTemp = weatherTemp;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
