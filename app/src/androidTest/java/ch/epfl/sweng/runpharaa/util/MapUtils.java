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

interface MapUtils {

    GoogleMap FAKE_MAP =

            new GoogleMap(new IGoogleMapDelegate() {
                @Override
                public CameraPosition getCameraPosition() {
                    return null;
                }

                @Override
                public float getMaxZoomLevel() {
                    return 0;
                }

                @Override
                public float getMinZoomLevel() {
                    return 0;
                }

                @Override
                public void moveCamera(IObjectWrapper iObjectWrapper) {

                }

                @Override
                public void animateCamera(IObjectWrapper iObjectWrapper) {
                    //for overriding
                }

                @Override
                public void animateCameraWithCallback(IObjectWrapper iObjectWrapper, zzc zzc) {
                    //for overriding
                }

                @Override
                public void animateCameraWithDurationAndCallback(IObjectWrapper iObjectWrapper, int i, zzc zzc) {
                    //for overriding
                }

                @Override
                public void stopAnimation() {
                    //for overriding
                }

                @Override
                public zzz addPolyline(PolylineOptions polylineOptions) {
                    return new zzz() {
                        @Override
                        public void remove() {

                        }

                        @Override
                        public String getId() {
                            return null;
                        }

                        @Override
                        public List<LatLng> getPoints() {
                            return null;
                        }

                        @Override
                        public void setPoints(List<LatLng> list) {

                        }

                        @Override
                        public float getWidth() {
                            return 0;
                        }

                        @Override
                        public void setWidth(float v) {

                        }

                        @Override
                        public int getColor() {
                            return 0;
                        }

                        @Override
                        public void setColor(int i) {

                        }

                        @Override
                        public void setZIndex(float v) {

                        }

                        @Override
                        public float getZIndex() {
                            return 0;
                        }

                        @Override
                        public void setVisible(boolean b) {

                        }

                        @Override
                        public boolean isVisible() {
                            return false;
                        }

                        @Override
                        public boolean isGeodesic() {
                            return false;
                        }

                        @Override
                        public void setGeodesic(boolean b) {

                        }

                        @Override
                        public boolean zzb(zzz zzz) {
                            return false;
                        }

                        @Override
                        public int zzi() {
                            return 0;
                        }

                        @Override
                        public void setClickable(boolean b) {

                        }

                        @Override
                        public boolean isClickable() {
                            return false;
                        }

                        @Override
                        public Cap getStartCap() {
                            return null;
                        }

                        @Override
                        public void setStartCap(Cap cap) {

                        }

                        @Override
                        public Cap getEndCap() {
                            return null;
                        }

                        @Override
                        public void setEndCap(Cap cap) {

                        }

                        @Override
                        public int getJointType() {
                            return 0;
                        }

                        @Override
                        public void setJointType(int i) {

                        }

                        @Override
                        public List<PatternItem> getPattern() {
                            return null;
                        }

                        @Override
                        public void setPattern(List<PatternItem> list) {

                        }

                        @Override
                        public void zze(IObjectWrapper iObjectWrapper) {

                        }

                        @Override
                        public IObjectWrapper zzj() {
                            return null;
                        }

                        @Override
                        public IBinder asBinder() {
                            return null;
                        }
                    };
                }

                @Override
                public zzw addPolygon(PolygonOptions polygonOptions) {
                    return null;
                }

                @Override
                public zzt addMarker(MarkerOptions markerOptions) {
                    return new zzt() {
                        @Override
                        public void remove() {

                        }

                        @Override
                        public String getId() {
                            return null;
                        }

                        @Override
                        public LatLng getPosition() {
                            return null;
                        }

                        @Override
                        public void setPosition(LatLng latLng) {

                        }

                        @Override
                        public String getTitle() {
                            return null;
                        }

                        @Override
                        public void setTitle(String s) {

                        }

                        @Override
                        public String getSnippet() {
                            return null;
                        }

                        @Override
                        public void setSnippet(String s) {

                        }

                        @Override
                        public boolean isDraggable() {
                            return false;
                        }

                        @Override
                        public void setDraggable(boolean b) {

                        }

                        @Override
                        public void showInfoWindow() {

                        }

                        @Override
                        public void hideInfoWindow() {

                        }

                        @Override
                        public boolean isInfoWindowShown() {
                            return false;
                        }

                        @Override
                        public void setVisible(boolean b) {

                        }

                        @Override
                        public boolean isVisible() {
                            return false;
                        }

                        @Override
                        public boolean zzj(zzt zzt) {
                            return false;
                        }

                        @Override
                        public int zzi() {
                            return 0;
                        }

                        @Override
                        public void zzg(IObjectWrapper iObjectWrapper) {

                        }

                        @Override
                        public void setAnchor(float v, float v1) {

                        }

                        @Override
                        public boolean isFlat() {
                            return false;
                        }

                        @Override
                        public void setFlat(boolean b) {

                        }

                        @Override
                        public float getRotation() {
                            return 0;
                        }

                        @Override
                        public void setRotation(float v) {

                        }

                        @Override
                        public void setInfoWindowAnchor(float v, float v1) {

                        }

                        @Override
                        public float getAlpha() {
                            return 0;
                        }

                        @Override
                        public void setAlpha(float v) {

                        }

                        @Override
                        public void setZIndex(float v) {

                        }

                        @Override
                        public float getZIndex() {
                            return 0;
                        }

                        @Override
                        public void zze(IObjectWrapper iObjectWrapper) {

                        }

                        @Override
                        public IObjectWrapper zzj() {
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
                public zzk addGroundOverlay(GroundOverlayOptions groundOverlayOptions) {
                    return null;
                }

                @Override
                public zzac addTileOverlay(TileOverlayOptions tileOverlayOptions) {
                    return null;
                }

                @Override
                public void clear() {
                    //for overriding
                }

                @Override
                public int getMapType() {
                    return 0;
                }

                @Override
                public void setMapType(int i) {
                    //for overriding
                }

                @Override
                public boolean isTrafficEnabled() {
                    return false;
                }

                @Override
                public void setTrafficEnabled(boolean b) {
                    //for overriding
                }

                @Override
                public boolean isIndoorEnabled() {
                    return false;
                }

                @Override
                public boolean setIndoorEnabled(boolean b) {
                    return false;
                }

                @Override
                public boolean isMyLocationEnabled() {
                    return false;
                }

                @Override
                public void setMyLocationEnabled(boolean b) {
                    //for overriding
                }

                @Override
                public Location getMyLocation() {
                    return null;
                }

                @Override
                public void setLocationSource(ILocationSourceDelegate iLocationSourceDelegate) {
                    //for overriding
                }

                @Override
                public IUiSettingsDelegate getUiSettings() {
                    return new IUiSettingsDelegate() {
                        @Override
                        public void setAllGesturesEnabled(boolean b) {
                            //for overriding
                        }

                        @Override
                        public boolean isZoomControlsEnabled() {
                            return false;
                        }

                        @Override
                        public void setZoomControlsEnabled(boolean b) {
                            //for overriding
                        }

                        @Override
                        public boolean isCompassEnabled() {
                            return false;
                        }

                        @Override
                        public void setCompassEnabled(boolean b) {
                            //for overriding
                        }

                        @Override
                        public boolean isMyLocationButtonEnabled() {
                            return false;
                        }

                        @Override
                        public void setMyLocationButtonEnabled(boolean b) {
                            //for overriding
                        }

                        @Override
                        public boolean isScrollGesturesEnabled() {
                            return false;
                        }

                        @Override
                        public void setScrollGesturesEnabled(boolean b) {
                            //for overriding
                        }

                        @Override
                        public boolean isZoomGesturesEnabled() {
                            return false;
                        }

                        @Override
                        public void setZoomGesturesEnabled(boolean b) {
                            //for overriding
                        }

                        @Override
                        public boolean isTiltGesturesEnabled() {
                            return false;
                        }

                        @Override
                        public void setTiltGesturesEnabled(boolean b) {
                            //for overriding
                        }

                        @Override
                        public boolean isRotateGesturesEnabled() {
                            return false;
                        }

                        @Override
                        public void setRotateGesturesEnabled(boolean b) {
                            //for overriding
                        }

                        @Override
                        public boolean isIndoorLevelPickerEnabled() {
                            return false;
                        }

                        @Override
                        public void setIndoorLevelPickerEnabled(boolean b) {
                            //for overriding
                        }

                        @Override
                        public boolean isMapToolbarEnabled() {
                            return false;
                        }

                        @Override
                        public void setMapToolbarEnabled(boolean b) {
                            //for overriding
                        }

                        @Override
                        public IBinder asBinder() {
                            return null;
                        }
                    };
                }

                @Override
                public IProjectionDelegate getProjection() {
                    return null;
                }

                @Override
                public void setOnCameraChangeListener(zzl zzl) {
                    //for overriding
                }

                @Override
                public void setOnMapClickListener(zzaj zzaj) {
                    //for overriding
                }

                @Override
                public void setOnMapLongClickListener(zzan zzan) {
                    //for overriding
                }

                @Override
                public void setOnMarkerClickListener(zzar zzar) {
                    //for overriding
                }

                @Override
                public void setOnMarkerDragListener(zzat zzat) {
                    //for overriding
                }

                @Override
                public void setOnInfoWindowClickListener(zzab zzab) {
                    //for overriding
                }

                @Override
                public void setInfoWindowAdapter(zzh zzh) {
                    //for overriding
                }

                @Override
                public com.google.android.gms.internal.maps.zzh addCircle(CircleOptions circleOptions) {
                    return new com.google.android.gms.internal.maps.zzh() {
                        @Override
                        public void remove() {

                        }

                        @Override
                        public String getId() {
                            return null;
                        }

                        @Override
                        public LatLng getCenter() {
                            return null;
                        }

                        @Override
                        public void setCenter(LatLng latLng) {

                        }

                        @Override
                        public double getRadius() {
                            return 0;
                        }

                        @Override
                        public void setRadius(double v) {

                        }

                        @Override
                        public float getStrokeWidth() {
                            return 0;
                        }

                        @Override
                        public void setStrokeWidth(float v) {

                        }

                        @Override
                        public int getStrokeColor() {
                            return 0;
                        }

                        @Override
                        public void setStrokeColor(int i) {

                        }

                        @Override
                        public int getFillColor() {
                            return 0;
                        }

                        @Override
                        public void setFillColor(int i) {

                        }

                        @Override
                        public float getZIndex() {
                            return 0;
                        }

                        @Override
                        public void setZIndex(float v) {

                        }

                        @Override
                        public boolean isVisible() {
                            return false;
                        }

                        @Override
                        public void setVisible(boolean b) {

                        }

                        @Override
                        public boolean zzb(com.google.android.gms.internal.maps.zzh zzh) {
                            return false;
                        }

                        @Override
                        public int zzi() {
                            return 0;
                        }

                        @Override
                        public boolean isClickable() {
                            return false;
                        }

                        @Override
                        public void setClickable(boolean b) {

                        }

                        @Override
                        public List<PatternItem> getStrokePattern() {
                            return null;
                        }

                        @Override
                        public void setStrokePattern(List<PatternItem> list) {

                        }

                        @Override
                        public void zze(IObjectWrapper iObjectWrapper) {

                        }

                        @Override
                        public IObjectWrapper zzj() {
                            return null;
                        }

                        @Override
                        public IBinder asBinder() {
                            return null;
                        }
                    };
                }

                @Override
                public void setOnMyLocationChangeListener(zzax zzax) {
                    //for overriding
                }

                @Override
                public void setOnMyLocationButtonClickListener(zzav zzav) {
                    //for overriding
                }

                @Override
                public void snapshot(zzbs zzbs, IObjectWrapper iObjectWrapper) {
                    //for overriding
                }

                @Override
                public void setPadding(int i, int i1, int i2, int i3) {
                    //for overriding
                }

                @Override
                public boolean isBuildingsEnabled() {
                    return false;
                }

                @Override
                public void setBuildingsEnabled(boolean b) {
                    //for overriding
                }

                @Override
                public void setOnMapLoadedCallback(zzal zzal) {
                    //for overriding
                }

                @Override
                public zzn getFocusedBuilding() {
                    return null;
                }

                @Override
                public void setOnIndoorStateChangeListener(com.google.android.gms.maps.internal.zzz zzz) {
                    //for overriding
                }

                @Override
                public void setWatermarkEnabled(boolean b) {
                    //for overriding
                }

                @Override
                public void getMapAsync(zzap zzap) {
                    //for overriding
                }

                @Override
                public void onCreate(Bundle bundle) {
                    //for overriding
                }

                @Override
                public void onResume() {
                    //for overriding
                }

                @Override
                public void onPause() {
                    //for overriding
                }

                @Override
                public void onDestroy() {
                    //for overriding
                }

                @Override
                public void onLowMemory() {
                    //for overriding
                }

                @Override
                public boolean useViewLifecycleWhenInFragment() {
                    return false;
                }

                @Override
                public void onSaveInstanceState(Bundle bundle) {
                    //for overriding
                }

                @Override
                public void setContentDescription(String s) {
                    //for overriding
                }

                @Override
                public void snapshotForTest(zzbs zzbs) {
                    //for overriding
                }

                @Override
                public void setOnPoiClickListener(zzbb zzbb) {
                    //for overriding
                }

                @Override
                public void onEnterAmbient(Bundle bundle) {
                    //for overriding
                }

                @Override
                public void onExitAmbient() {
                    //for overriding
                }

                @Override
                public void setOnGroundOverlayClickListener(zzx zzx) {
                    //for overriding
                }

                @Override
                public void setOnInfoWindowLongClickListener(zzaf zzaf) {
                    //for overriding
                }

                @Override
                public void setOnPolygonClickListener(zzbd zzbd) {
                    //for overriding
                }

                @Override
                public void setOnInfoWindowCloseListener(zzad zzad) {
                    //for overriding
                }

                @Override
                public void setOnPolylineClickListener(zzbf zzbf) {
                    //for overriding
                }

                @Override
                public void setOnCircleClickListener(zzv zzv) {
                    //for overriding
                }

                @Override
                public void setMinZoomPreference(float v) {
                    //for overriding
                }

                @Override
                public void setMaxZoomPreference(float v) {
                    //for overriding
                }

                @Override
                public void resetMinMaxZoomPreference() {
                    //for overriding
                }

                @Override
                public void setLatLngBoundsForCameraTarget(LatLngBounds latLngBounds) {
                    //for overriding
                }

                @Override
                public void setOnCameraMoveStartedListener(com.google.android.gms.maps.internal.zzt zzt) {
                    //for overriding
                }

                @Override
                public void setOnCameraMoveListener(zzr zzr) {
                    //for overriding
                }

                @Override
                public void setOnCameraMoveCanceledListener(zzp zzp) {
                    //for overriding
                }

                @Override
                public void setOnCameraIdleListener(com.google.android.gms.maps.internal.zzn zzn) {
                    //for overriding
                }

                @Override
                public boolean setMapStyle(MapStyleOptions mapStyleOptions) {
                    return false;
                }

                @Override
                public void onStart() {
                    //for overriding
                }

                @Override
                public void onStop() {
                    //for overriding
                }

                @Override
                public void setOnMyLocationClickListener(zzaz zzaz) {
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
            public IObjectWrapper zza(int i) {
                return () -> null;
            }

            @Override
            public IObjectWrapper zza(String s) {
                return null;
            }

            @Override
            public IObjectWrapper zzb(String s) {
                return null;
            }

            @Override
            public IObjectWrapper zzh() {
                return () -> null;
            }

            @Override
            public IObjectWrapper zza(float v) {
                return () -> null;
            }

            @Override
            public IObjectWrapper zza(Bitmap bitmap) {
                return null;
            }

            @Override
            public IObjectWrapper zzc(String s) {
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
            public IObjectWrapper zoomIn() {
                return () -> null;
            }

            @Override
            public IObjectWrapper zoomOut() {
                return () -> null;
            }

            @Override
            public IObjectWrapper scrollBy(float v, float v1) {
                return () -> null;
            }

            @Override
            public IObjectWrapper zoomTo(float v) {
                return () -> null;
            }

            @Override
            public IObjectWrapper zoomBy(float v) {
                return () -> null;
            }

            @Override
            public IObjectWrapper zoomByWithFocus(float v, int i, int i1) {
                return () -> null;
            }

            @Override
            public IObjectWrapper newCameraPosition(CameraPosition cameraPosition) {
                return () -> null;
            }

            @Override
            public IObjectWrapper newLatLng(LatLng latLng) {
                return () -> null;
            }

            @Override
            public IObjectWrapper newLatLngZoom(LatLng latLng, float v) {
                return () -> null;
            }

            @Override
            public IObjectWrapper newLatLngBounds(LatLngBounds latLngBounds, int i) {
                return () -> null;
            }

            @Override
            public IObjectWrapper newLatLngBoundsWithSize(LatLngBounds latLngBounds, int i, int i1, int i2) {
                return () -> null;
            }

            @Override
            public IBinder asBinder() {
                return null;
            }
        });
    }

}
