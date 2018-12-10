package ch.epfl.sweng.runpharaa.util;

import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.maps.zzac;
import com.google.android.gms.internal.maps.zze;
import com.google.android.gms.internal.maps.zzk;
import com.google.android.gms.internal.maps.zzn;
import com.google.android.gms.internal.maps.zzt;
import com.google.android.gms.internal.maps.zzw;
import com.google.android.gms.internal.maps.zzz;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate;
import com.google.android.gms.maps.internal.IGoogleMapDelegate;
import com.google.android.gms.maps.internal.ILocationSourceDelegate;
import com.google.android.gms.maps.internal.IProjectionDelegate;
import com.google.android.gms.maps.internal.IUiSettingsDelegate;
import com.google.android.gms.maps.internal.zzab;
import com.google.android.gms.maps.internal.zzad;
import com.google.android.gms.maps.internal.zzaf;
import com.google.android.gms.maps.internal.zzaj;
import com.google.android.gms.maps.internal.zzal;
import com.google.android.gms.maps.internal.zzan;
import com.google.android.gms.maps.internal.zzap;
import com.google.android.gms.maps.internal.zzar;
import com.google.android.gms.maps.internal.zzat;
import com.google.android.gms.maps.internal.zzav;
import com.google.android.gms.maps.internal.zzax;
import com.google.android.gms.maps.internal.zzaz;
import com.google.android.gms.maps.internal.zzbb;
import com.google.android.gms.maps.internal.zzbd;
import com.google.android.gms.maps.internal.zzbf;
import com.google.android.gms.maps.internal.zzbs;
import com.google.android.gms.maps.internal.zzc;
import com.google.android.gms.maps.internal.zzh;
import com.google.android.gms.maps.internal.zzl;
import com.google.android.gms.maps.internal.zzp;
import com.google.android.gms.maps.internal.zzr;
import com.google.android.gms.maps.internal.zzv;
import com.google.android.gms.maps.internal.zzx;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Cap;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;

import java.util.List;

public interface MapUtils {

    static final GoogleMap FAKE_MAP =

            new GoogleMap(new IGoogleMapDelegate() {
                @Override
                public CameraPosition getCameraPosition() throws RemoteException {
                    return null;
                }

                @Override
                public float getMaxZoomLevel() throws RemoteException {
                    return 0;
                }

                @Override
                public float getMinZoomLevel() throws RemoteException {
                    return 0;
                }

                @Override
                public void moveCamera(IObjectWrapper iObjectWrapper) throws RemoteException {

                }

                @Override
                public void animateCamera(IObjectWrapper iObjectWrapper) throws RemoteException {
                    //for overriding
                }

                @Override
                public void animateCameraWithCallback(IObjectWrapper iObjectWrapper, zzc zzc) throws RemoteException {
                    //for overriding
                }

                @Override
                public void animateCameraWithDurationAndCallback(IObjectWrapper iObjectWrapper, int i, zzc zzc) throws RemoteException {
                    //for overriding
                }

                @Override
                public void stopAnimation() throws RemoteException {
                    //for overriding
                }

                @Override
                public zzz addPolyline(PolylineOptions polylineOptions) throws RemoteException {
                    return new zzz() {
                        @Override
                        public void remove() throws RemoteException {

                        }

                        @Override
                        public String getId() throws RemoteException {
                            return null;
                        }

                        @Override
                        public List<LatLng> getPoints() throws RemoteException {
                            return null;
                        }

                        @Override
                        public void setPoints(List<LatLng> list) throws RemoteException {

                        }

                        @Override
                        public float getWidth() throws RemoteException {
                            return 0;
                        }

                        @Override
                        public void setWidth(float v) throws RemoteException {

                        }

                        @Override
                        public int getColor() throws RemoteException {
                            return 0;
                        }

                        @Override
                        public void setColor(int i) throws RemoteException {

                        }

                        @Override
                        public void setZIndex(float v) throws RemoteException {

                        }

                        @Override
                        public float getZIndex() throws RemoteException {
                            return 0;
                        }

                        @Override
                        public void setVisible(boolean b) throws RemoteException {

                        }

                        @Override
                        public boolean isVisible() throws RemoteException {
                            return false;
                        }

                        @Override
                        public boolean isGeodesic() throws RemoteException {
                            return false;
                        }

                        @Override
                        public void setGeodesic(boolean b) throws RemoteException {

                        }

                        @Override
                        public boolean zzb(zzz zzz) throws RemoteException {
                            return false;
                        }

                        @Override
                        public int zzi() throws RemoteException {
                            return 0;
                        }

                        @Override
                        public void setClickable(boolean b) throws RemoteException {

                        }

                        @Override
                        public boolean isClickable() throws RemoteException {
                            return false;
                        }

                        @Override
                        public Cap getStartCap() throws RemoteException {
                            return null;
                        }

                        @Override
                        public void setStartCap(Cap cap) throws RemoteException {

                        }

                        @Override
                        public Cap getEndCap() throws RemoteException {
                            return null;
                        }

                        @Override
                        public void setEndCap(Cap cap) throws RemoteException {

                        }

                        @Override
                        public int getJointType() throws RemoteException {
                            return 0;
                        }

                        @Override
                        public void setJointType(int i) throws RemoteException {

                        }

                        @Override
                        public List<PatternItem> getPattern() throws RemoteException {
                            return null;
                        }

                        @Override
                        public void setPattern(List<PatternItem> list) throws RemoteException {

                        }

                        @Override
                        public void zze(IObjectWrapper iObjectWrapper) throws RemoteException {

                        }

                        @Override
                        public IObjectWrapper zzj() throws RemoteException {
                            return null;
                        }

                        @Override
                        public IBinder asBinder() {
                            return null;
                        }
                    };
                }

                @Override
                public zzw addPolygon(PolygonOptions polygonOptions) throws RemoteException {
                    return null;
                }

                @Override
                public zzt addMarker(MarkerOptions markerOptions) throws RemoteException {
                    return new zzt() {
                        @Override
                        public void remove() throws RemoteException {

                        }

                        @Override
                        public String getId() throws RemoteException {
                            return null;
                        }

                        @Override
                        public LatLng getPosition() throws RemoteException {
                            return null;
                        }

                        @Override
                        public void setPosition(LatLng latLng) throws RemoteException {

                        }

                        @Override
                        public String getTitle() throws RemoteException {
                            return null;
                        }

                        @Override
                        public void setTitle(String s) throws RemoteException {

                        }

                        @Override
                        public String getSnippet() throws RemoteException {
                            return null;
                        }

                        @Override
                        public void setSnippet(String s) throws RemoteException {

                        }

                        @Override
                        public boolean isDraggable() throws RemoteException {
                            return false;
                        }

                        @Override
                        public void setDraggable(boolean b) throws RemoteException {

                        }

                        @Override
                        public void showInfoWindow() throws RemoteException {

                        }

                        @Override
                        public void hideInfoWindow() throws RemoteException {

                        }

                        @Override
                        public boolean isInfoWindowShown() throws RemoteException {
                            return false;
                        }

                        @Override
                        public void setVisible(boolean b) throws RemoteException {

                        }

                        @Override
                        public boolean isVisible() throws RemoteException {
                            return false;
                        }

                        @Override
                        public boolean zzj(zzt zzt) throws RemoteException {
                            return false;
                        }

                        @Override
                        public int zzi() throws RemoteException {
                            return 0;
                        }

                        @Override
                        public void zzg(IObjectWrapper iObjectWrapper) throws RemoteException {

                        }

                        @Override
                        public void setAnchor(float v, float v1) throws RemoteException {

                        }

                        @Override
                        public boolean isFlat() throws RemoteException {
                            return false;
                        }

                        @Override
                        public void setFlat(boolean b) throws RemoteException {

                        }

                        @Override
                        public float getRotation() throws RemoteException {
                            return 0;
                        }

                        @Override
                        public void setRotation(float v) throws RemoteException {

                        }

                        @Override
                        public void setInfoWindowAnchor(float v, float v1) throws RemoteException {

                        }

                        @Override
                        public float getAlpha() throws RemoteException {
                            return 0;
                        }

                        @Override
                        public void setAlpha(float v) throws RemoteException {

                        }

                        @Override
                        public void setZIndex(float v) throws RemoteException {

                        }

                        @Override
                        public float getZIndex() throws RemoteException {
                            return 0;
                        }

                        @Override
                        public void zze(IObjectWrapper iObjectWrapper) throws RemoteException {

                        }

                        @Override
                        public IObjectWrapper zzj() throws RemoteException {
                            return new IObjectWrapper() {
                                @Override
                                public IBinder asBinder() {
                                    return null;
                                }
                            };
                        }

                        @Override
                        public IBinder asBinder() {
                            return null;
                        }
                    };
                }

                @Override
                public zzk addGroundOverlay(GroundOverlayOptions groundOverlayOptions) throws RemoteException {
                    return null;
                }

                @Override
                public zzac addTileOverlay(TileOverlayOptions tileOverlayOptions) throws RemoteException {
                    return null;
                }

                @Override
                public void clear() throws RemoteException {
                    //for overriding
                }

                @Override
                public int getMapType() throws RemoteException {
                    return 0;
                }

                @Override
                public void setMapType(int i) throws RemoteException {
                    //for overriding
                }

                @Override
                public boolean isTrafficEnabled() throws RemoteException {
                    return false;
                }

                @Override
                public void setTrafficEnabled(boolean b) throws RemoteException {
                    //for overriding
                }

                @Override
                public boolean isIndoorEnabled() throws RemoteException {
                    return false;
                }

                @Override
                public boolean setIndoorEnabled(boolean b) throws RemoteException {
                    return false;
                }

                @Override
                public boolean isMyLocationEnabled() throws RemoteException {
                    return false;
                }

                @Override
                public void setMyLocationEnabled(boolean b) throws RemoteException {
                    //for overriding
                }

                @Override
                public Location getMyLocation() throws RemoteException {
                    return null;
                }

                @Override
                public void setLocationSource(ILocationSourceDelegate iLocationSourceDelegate) throws RemoteException {
                    //for overriding
                }

                @Override
                public IUiSettingsDelegate getUiSettings() throws RemoteException {
                    return new IUiSettingsDelegate() {
                        @Override
                        public void setAllGesturesEnabled(boolean b) throws RemoteException {
                            //for overriding
                        }

                        @Override
                        public boolean isZoomControlsEnabled() throws RemoteException {
                            return false;
                        }

                        @Override
                        public void setZoomControlsEnabled(boolean b) throws RemoteException {
                            //for overriding
                        }

                        @Override
                        public boolean isCompassEnabled() throws RemoteException {
                            return false;
                        }

                        @Override
                        public void setCompassEnabled(boolean b) throws RemoteException {
                            //for overriding
                        }

                        @Override
                        public boolean isMyLocationButtonEnabled() throws RemoteException {
                            return false;
                        }

                        @Override
                        public void setMyLocationButtonEnabled(boolean b) throws RemoteException {
                            //for overriding
                        }

                        @Override
                        public boolean isScrollGesturesEnabled() throws RemoteException {
                            return false;
                        }

                        @Override
                        public void setScrollGesturesEnabled(boolean b) throws RemoteException {
                            //for overriding
                        }

                        @Override
                        public boolean isZoomGesturesEnabled() throws RemoteException {
                            return false;
                        }

                        @Override
                        public void setZoomGesturesEnabled(boolean b) throws RemoteException {
                            //for overriding
                        }

                        @Override
                        public boolean isTiltGesturesEnabled() throws RemoteException {
                            return false;
                        }

                        @Override
                        public void setTiltGesturesEnabled(boolean b) throws RemoteException {
                            //for overriding
                        }

                        @Override
                        public boolean isRotateGesturesEnabled() throws RemoteException {
                            return false;
                        }

                        @Override
                        public void setRotateGesturesEnabled(boolean b) throws RemoteException {
                            //for overriding
                        }

                        @Override
                        public boolean isIndoorLevelPickerEnabled() throws RemoteException {
                            return false;
                        }

                        @Override
                        public void setIndoorLevelPickerEnabled(boolean b) throws RemoteException {
                            //for overriding
                        }

                        @Override
                        public boolean isMapToolbarEnabled() throws RemoteException {
                            return false;
                        }

                        @Override
                        public void setMapToolbarEnabled(boolean b) throws RemoteException {
                            //for overriding
                        }

                        @Override
                        public IBinder asBinder() {
                            return null;
                        }
                    };
                }

                @Override
                public IProjectionDelegate getProjection() throws RemoteException {
                    return null;
                }

                @Override
                public void setOnCameraChangeListener(zzl zzl) throws RemoteException {
                    //for overriding
                }

                @Override
                public void setOnMapClickListener(zzaj zzaj) throws RemoteException {
                    //for overriding
                }

                @Override
                public void setOnMapLongClickListener(zzan zzan) throws RemoteException {
                    //for overriding
                }

                @Override
                public void setOnMarkerClickListener(zzar zzar) throws RemoteException {
                    //for overriding
                }

                @Override
                public void setOnMarkerDragListener(zzat zzat) throws RemoteException {
                    //for overriding
                }

                @Override
                public void setOnInfoWindowClickListener(zzab zzab) throws RemoteException {
                    //for overriding
                }

                @Override
                public void setInfoWindowAdapter(zzh zzh) throws RemoteException {
                    //for overriding
                }

                @Override
                public com.google.android.gms.internal.maps.zzh addCircle(CircleOptions circleOptions) throws RemoteException {
                    return new com.google.android.gms.internal.maps.zzh() {
                        @Override
                        public void remove() throws RemoteException {

                        }

                        @Override
                        public String getId() throws RemoteException {
                            return null;
                        }

                        @Override
                        public LatLng getCenter() throws RemoteException {
                            return null;
                        }

                        @Override
                        public void setCenter(LatLng latLng) throws RemoteException {

                        }

                        @Override
                        public double getRadius() throws RemoteException {
                            return 0;
                        }

                        @Override
                        public void setRadius(double v) throws RemoteException {

                        }

                        @Override
                        public float getStrokeWidth() throws RemoteException {
                            return 0;
                        }

                        @Override
                        public void setStrokeWidth(float v) throws RemoteException {

                        }

                        @Override
                        public int getStrokeColor() throws RemoteException {
                            return 0;
                        }

                        @Override
                        public void setStrokeColor(int i) throws RemoteException {

                        }

                        @Override
                        public int getFillColor() throws RemoteException {
                            return 0;
                        }

                        @Override
                        public void setFillColor(int i) throws RemoteException {

                        }

                        @Override
                        public float getZIndex() throws RemoteException {
                            return 0;
                        }

                        @Override
                        public void setZIndex(float v) throws RemoteException {

                        }

                        @Override
                        public boolean isVisible() throws RemoteException {
                            return false;
                        }

                        @Override
                        public void setVisible(boolean b) throws RemoteException {

                        }

                        @Override
                        public boolean zzb(com.google.android.gms.internal.maps.zzh zzh) throws RemoteException {
                            return false;
                        }

                        @Override
                        public int zzi() throws RemoteException {
                            return 0;
                        }

                        @Override
                        public boolean isClickable() throws RemoteException {
                            return false;
                        }

                        @Override
                        public void setClickable(boolean b) throws RemoteException {

                        }

                        @Override
                        public List<PatternItem> getStrokePattern() throws RemoteException {
                            return null;
                        }

                        @Override
                        public void setStrokePattern(List<PatternItem> list) throws RemoteException {

                        }

                        @Override
                        public void zze(IObjectWrapper iObjectWrapper) throws RemoteException {

                        }

                        @Override
                        public IObjectWrapper zzj() throws RemoteException {
                            return null;
                        }

                        @Override
                        public IBinder asBinder() {
                            return null;
                        }
                    };
                }

                @Override
                public void setOnMyLocationChangeListener(zzax zzax) throws RemoteException {
                    //for overriding
                }

                @Override
                public void setOnMyLocationButtonClickListener(zzav zzav) throws RemoteException {
                    //for overriding
                }

                @Override
                public void snapshot(zzbs zzbs, IObjectWrapper iObjectWrapper) throws RemoteException {
                    //for overriding
                }

                @Override
                public void setPadding(int i, int i1, int i2, int i3) throws RemoteException {
                    //for overriding
                }

                @Override
                public boolean isBuildingsEnabled() throws RemoteException {
                    return false;
                }

                @Override
                public void setBuildingsEnabled(boolean b) throws RemoteException {
                    //for overriding
                }

                @Override
                public void setOnMapLoadedCallback(zzal zzal) throws RemoteException {
                    //for overriding
                }

                @Override
                public zzn getFocusedBuilding() throws RemoteException {
                    return null;
                }

                @Override
                public void setOnIndoorStateChangeListener(com.google.android.gms.maps.internal.zzz zzz) throws RemoteException {
                    //for overriding
                }

                @Override
                public void setWatermarkEnabled(boolean b) throws RemoteException {
                    //for overriding
                }

                @Override
                public void getMapAsync(zzap zzap) throws RemoteException {
                    //for overriding
                }

                @Override
                public void onCreate(Bundle bundle) throws RemoteException {
                    //for overriding
                }

                @Override
                public void onResume() throws RemoteException {
                    //for overriding
                }

                @Override
                public void onPause() throws RemoteException {
                    //for overriding
                }

                @Override
                public void onDestroy() throws RemoteException {
                    //for overriding
                }

                @Override
                public void onLowMemory() throws RemoteException {
                    //for overriding
                }

                @Override
                public boolean useViewLifecycleWhenInFragment() throws RemoteException {
                    return false;
                }

                @Override
                public void onSaveInstanceState(Bundle bundle) throws RemoteException {
                    //for overriding
                }

                @Override
                public void setContentDescription(String s) throws RemoteException {
                    //for overriding
                }

                @Override
                public void snapshotForTest(zzbs zzbs) throws RemoteException {
                    //for overriding
                }

                @Override
                public void setOnPoiClickListener(zzbb zzbb) throws RemoteException {
                    //for overriding
                }

                @Override
                public void onEnterAmbient(Bundle bundle) throws RemoteException {
                    //for overriding
                }

                @Override
                public void onExitAmbient() throws RemoteException {
                    //for overriding
                }

                @Override
                public void setOnGroundOverlayClickListener(zzx zzx) throws RemoteException {
                    //for overriding
                }

                @Override
                public void setOnInfoWindowLongClickListener(zzaf zzaf) throws RemoteException {
                    //for overriding
                }

                @Override
                public void setOnPolygonClickListener(zzbd zzbd) throws RemoteException {
                    //for overriding
                }

                @Override
                public void setOnInfoWindowCloseListener(zzad zzad) throws RemoteException {
                    //for overriding
                }

                @Override
                public void setOnPolylineClickListener(zzbf zzbf) throws RemoteException {
                    //for overriding
                }

                @Override
                public void setOnCircleClickListener(zzv zzv) throws RemoteException {
                    //for overriding
                }

                @Override
                public void setMinZoomPreference(float v) throws RemoteException {
                    //for overriding
                }

                @Override
                public void setMaxZoomPreference(float v) throws RemoteException {
                    //for overriding
                }

                @Override
                public void resetMinMaxZoomPreference() throws RemoteException {
                    //for overriding
                }

                @Override
                public void setLatLngBoundsForCameraTarget(LatLngBounds latLngBounds) throws RemoteException {
                    //for overriding
                }

                @Override
                public void setOnCameraMoveStartedListener(com.google.android.gms.maps.internal.zzt zzt) throws RemoteException {
                    //for overriding
                }

                @Override
                public void setOnCameraMoveListener(zzr zzr) throws RemoteException {
                    //for overriding
                }

                @Override
                public void setOnCameraMoveCanceledListener(zzp zzp) throws RemoteException {
                    //for overriding
                }

                @Override
                public void setOnCameraIdleListener(com.google.android.gms.maps.internal.zzn zzn) throws RemoteException {
                    //for overriding
                }

                @Override
                public boolean setMapStyle(MapStyleOptions mapStyleOptions) throws RemoteException {
                    return false;
                }

                @Override
                public void onStart() throws RemoteException {
                    //for overriding
                }

                @Override
                public void onStop() throws RemoteException {
                    //for overriding
                }

                @Override
                public void setOnMyLocationClickListener(zzaz zzaz) throws RemoteException {
                    //for overriding
                }

                @Override
                public IBinder asBinder() {
                    return null;
                }
            });



     static void initBitmapDescriptorFactory() {
        BitmapDescriptorFactory.zza(new zze() {
            @Override
            public IObjectWrapper zza(int i) throws RemoteException {
                return () -> null;
            }

            @Override
            public IObjectWrapper zza(String s) throws RemoteException {
                return null;
            }

            @Override
            public IObjectWrapper zzb(String s) throws RemoteException {
                return null;
            }

            @Override
            public IObjectWrapper zzh() throws RemoteException {
                return () -> null;
            }

            @Override
            public IObjectWrapper zza(float v) throws RemoteException {
                return () -> null;
            }

            @Override
            public IObjectWrapper zza(Bitmap bitmap) throws RemoteException {
                return null;
            }

            @Override
            public IObjectWrapper zzc(String s) throws RemoteException {
                return null;
            }

            @Override
            public IBinder asBinder() {
                return null;
            }
        });
    }

     static void initCameraUpdateFactory() {
        CameraUpdateFactory.zza(new ICameraUpdateFactoryDelegate() {
            @Override
            public IObjectWrapper zoomIn() throws RemoteException {
                return () -> null;
            }

            @Override
            public IObjectWrapper zoomOut() throws RemoteException {
                return () -> null;
            }

            @Override
            public IObjectWrapper scrollBy(float v, float v1) throws RemoteException {
                return () -> null;
            }

            @Override
            public IObjectWrapper zoomTo(float v) throws RemoteException {
                return () -> null;
            }

            @Override
            public IObjectWrapper zoomBy(float v) throws RemoteException {
                return () -> null;
            }

            @Override
            public IObjectWrapper zoomByWithFocus(float v, int i, int i1) throws RemoteException {
                return () -> null;
            }

            @Override
            public IObjectWrapper newCameraPosition(CameraPosition cameraPosition) throws RemoteException {
                return () -> null;
            }

            @Override
            public IObjectWrapper newLatLng(LatLng latLng) throws RemoteException {
                return () -> null;
            }

            @Override
            public IObjectWrapper newLatLngZoom(LatLng latLng, float v) throws RemoteException {
                return () -> null;
            }

            @Override
            public IObjectWrapper newLatLngBounds(LatLngBounds latLngBounds, int i) throws RemoteException {
                return () -> null;
            }

            @Override
            public IObjectWrapper newLatLngBoundsWithSize(LatLngBounds latLngBounds, int i, int i1, int i2) throws RemoteException {
                return () -> null;
            }

            @Override
            public IBinder asBinder() {
                return null;
            }
        });
    }

}
