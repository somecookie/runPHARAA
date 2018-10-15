package ch.epfl.sweng.runpharaa;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class CustLatLngTest {

    @Test
    public void getterTest(){
        double latitude = 10;
        double longitude = 10;
        CustLatLng ll = new CustLatLng(latitude, longitude);
        assertEquals(latitude, ll.getLatitude(), 0);
        assertEquals(longitude, ll.getLongitude(), 0);
    }

    @Test
    public void emptyConstructorTest(){
        assertNotNull(new CustLatLng());
    }

    @Test
    public void constructorTest(){

        //Check bounds for latitude
        double latitudeMin = - 91;
        double longitude = 0;
        CustLatLng l1 = new CustLatLng(latitudeMin, longitude);
        assertNotEquals(latitudeMin, l1.getLatitude());
        assertEquals(-90D, l1.getLatitude(), 0);

        double latitudeMax = 91;
        CustLatLng l2 = new CustLatLng(latitudeMax, longitude);
        assertNotEquals(latitudeMin, l2.getLatitude());
        assertEquals(90D, l2.getLatitude(), 0);

        //Check bounds for longitude
        double latitude = 0;
        double longitudeMin = -181;
        CustLatLng l3 = new CustLatLng(latitude, longitudeMin);
        assertNotEquals(longitudeMin, l3.getLongitude());
        assertEquals(((longitudeMin - 180.0D) % 360.0D + 360.0D) % 360.0D - 180.0D, l3.getLongitude(), 0);

        double longitudeMax = 181;
        CustLatLng l4 = new CustLatLng(latitude, longitudeMax);
        assertNotEquals(longitudeMax, l4.getLongitude());
        assertEquals(((longitudeMax - 180.0D) % 360.0D + 360.0D) % 360.0D - 180.0D, l4.getLongitude(), 0);


    }

}
