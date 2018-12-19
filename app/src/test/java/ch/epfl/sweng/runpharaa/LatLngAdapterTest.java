package ch.epfl.sweng.runpharaa;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.runpharaa.utils.LatLngAdapter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class LatLngAdapterTest {

    @Test
    public void getterTest(){
        double latitude = 10;
        double longitude = 10;
        LatLngAdapter ll = new LatLngAdapter(latitude, longitude);
        assertEquals(latitude, ll.getLatitude(), 0);
        assertEquals(longitude, ll.getLongitude(), 0);
    }

    @Test
    public void emptyConstructorTest(){
        assertNotNull(new LatLngAdapter());
    }

    @Test
    public void constructorTest(){

        //Check bounds for latitude
        double latitudeMin = - 91;
        double longitude = 0;
        LatLngAdapter l1 = new LatLngAdapter(latitudeMin, longitude);
        assertNotEquals(latitudeMin, l1.getLatitude());
        assertEquals(-90D, l1.getLatitude(), 0);

        double latitudeMax = 91;
        LatLngAdapter l2 = new LatLngAdapter(latitudeMax, longitude);
        assertNotEquals(latitudeMin, l2.getLatitude());
        assertEquals(90D, l2.getLatitude(), 0);

        //Check bounds for longitude
        double latitude = 0;
        double longitudeMin = -181;
        LatLngAdapter l3 = new LatLngAdapter(latitude, longitudeMin);
        assertNotEquals(longitudeMin, l3.getLongitude());
        assertEquals(((longitudeMin - 180.0D) % 360.0D + 360.0D) % 360.0D - 180.0D, l3.getLongitude(), 0);

        double longitudeMax = 181;
        LatLngAdapter l4 = new LatLngAdapter(latitude, longitudeMax);
        assertNotEquals(longitudeMax, l4.getLongitude());
        assertEquals(((longitudeMax - 180.0D) % 360.0D + 360.0D) % 360.0D - 180.0D, l4.getLongitude(), 0);

    }

    @Test
    public void testLatLngToCustLatLng(){
        double latitude = 10;
        double longitude = 10;
        LatLng ll = new LatLng(latitude, longitude);
        LatLng l2 = new LatLng(latitude + 1, longitude + 1);
        LatLngAdapter cll = LatLngAdapter.LatLngToCustLatLng(ll);
        assertEquals(cll.getLatitude(), 10, 0);
        assertEquals(cll.getLongitude(), 10, 0);

        List<LatLng> list = new ArrayList<>();
        list.add(ll);
        list.add(l2);
        List<LatLngAdapter> listccl = LatLngAdapter.LatLngToCustLatLng(list);
        assertEquals(listccl.get(0).getLatitude(), 10, 0);
        assertEquals(listccl.get(0).getLongitude(), 10, 0);
        assertEquals(listccl.get(1).getLatitude(), 11, 0);
        assertEquals(listccl.get(1).getLongitude(), 11, 0);

    }

}
