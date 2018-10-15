package ch.epfl.sweng.runpharaa;

/**
 * Custom class that copy the {@link com.google.android.gms.maps.model.LatLng} class
 * Useful for it's empty constructor need in database exchanges
 */
public class CustLatLng {
    private Double latitude;
    private Double longitude;

    /**
     * Empty constructor needed for database exchanges
     */
    public CustLatLng() {
        //For database
    }

    /**
     * Basic constructor that takes a latitude and a longitude
     * @param latitude
     * @param longitude
     */
    public CustLatLng(Double latitude, Double longitude) {
        //The bound used are from the LatLng google class
        this.latitude = Math.max(-90.0D, Math.min(90.0D, latitude));
        if (-180.0D <= longitude && longitude < 180.0D){
            this.longitude = longitude;
        }
        else{
            this.longitude = ((longitude - 180.0D) % 360.0D + 360.0D) % 360.0D - 180.0D;
        }

    }

    /**
     * Return the latitude as a {@link Double}
     * @return
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * Return the longitude as a {@link Double}
     * @return
     */
    public Double getLongitude() {
        return longitude;
    }
}
