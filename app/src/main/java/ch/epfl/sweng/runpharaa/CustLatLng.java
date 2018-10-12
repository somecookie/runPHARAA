package ch.epfl.sweng.runpharaa;

public class CustLatLng {
    private Double latitude;
    private Double longitude;

    public CustLatLng() {}

    public CustLatLng(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
