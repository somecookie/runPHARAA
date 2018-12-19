package ch.epfl.sweng.runpharaa.utils;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom class that copy the {@link com.google.android.gms.maps.model.LatLng} class
 * Useful for it's empty constructor need in database exchanges
 */
public class LatLngAdapter {
    private Double latitude;
    private Double longitude;

    /**
     * Empty constructor needed for database exchanges
     */
    public LatLngAdapter() {
    }

    /**
     * Basic constructor that takes a latitude and a longitude
     *
     * @param latitude  a double
     * @param longitude a double
     */
    public LatLngAdapter(Double latitude, Double longitude) {
        //The bound used are from the LatLng google class
        this.latitude = Math.max(-90.0D, Math.min(90.0D, latitude));
        if (-180.0D <= longitude && longitude < 180.0D) {
            this.longitude = longitude;
        } else {
            this.longitude = ((longitude - 180.0D) % 360.0D + 360.0D) % 360.0D - 180.0D;
        }
    }

    /**
     * Return the latitude as a {@link Double}
     *
     * @return the latitude
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * Return the longitude as a {@link Double}
     *
     * @return the longitude
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * Compute the distance in meters between a track (its starting point) and a given coordinate.
     * See formula at: http://www.movable-type.co.uk/scripts/latlong.html
     * Set p1 as this and p2 as other
     *
     * @param otherLocation another location
     * @return the distance between a point and the track (this)
     */
    public double distance(LatLngAdapter otherLocation) {
        int R = 6378137; //Earth's mean radius in meter

        //angular differences in radians
        double dLat = Math.toRadians(latitude - otherLocation.getLatitude());
        double dLong = Math.toRadians(longitude - otherLocation.getLongitude());

        //this' and other's latitudes in radians
        double lat1 = Math.toRadians(latitude);
        double lat2 = Math.toRadians(otherLocation.getLatitude());

        //compute some factor a
        double a1 = Math.sin(dLat / 2) * Math.sin(dLat / 2);
        double a2 = Math.cos(lat1) * Math.cos(lat2);
        double a3 = Math.sin(dLong / 2) * Math.sin(dLong / 2);

        double a = a1 + a2 * a3;

        //compute some factor c
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    /**
     * Convert the current {@link LatLngAdapter} into a {@link LatLng}
     *
     * @return
     */
    public LatLng ToLatLng() {
        return new LatLng(latitude, longitude);
    }

    /**
     * Convert given LatLng to LatLngAdapter.
     *
     * @param p the LatLng to convert
     * @return the converted LatLng
     */
    public static LatLngAdapter LatLngToCustLatLng(LatLng p) {
        return new LatLngAdapter(p.latitude, p.longitude);
    }

    /**
     * Convert List<LatLng> to List<LatLngAdapter>.
     *
     * @param l the list to convert
     * @return the converted list
     */
    public static List<LatLngAdapter> LatLngToCustLatLng(List<LatLng> l) {
        List<LatLngAdapter> result = new ArrayList<>();
        for (LatLng p : l) {
            result.add(LatLngToCustLatLng(p));
        }
        return result;
    }

    /**
     * Convert a list of {@link LatLngAdapter} to a list of {@link LatLng}
     *
     * @param l the list to convert
     * @return the converted list
     */
    public static List<LatLng> CustLatLngToLatLng(List<LatLngAdapter> l) {
        List<LatLng> result = new ArrayList<>();
        for (LatLngAdapter p : l) {
            result.add(new LatLng(p.latitude, p.longitude));
        }
        return result;
    }
}
