package be.pxl.ievent.models;

/**
 * Created by jessevandenberghe on 24/08/2017.
 */

public class GeoFence {
    private String name;
    private double lat;
    private double lng;
    private float radius;

    public GeoFence(String name, double lat, double lng, float radius) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.radius = radius;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
}
