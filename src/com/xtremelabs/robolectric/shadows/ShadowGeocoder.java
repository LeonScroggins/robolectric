package com.xtremelabs.robolectric.shadows;

import android.location.Address;
import android.location.Geocoder;
import com.xtremelabs.robolectric.util.Implementation;
import com.xtremelabs.robolectric.util.Implements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A shadow for Geocoder that supports simulated responses and failures
 */
@SuppressWarnings({"UnusedDeclaration"})
@Implements(Geocoder.class)
public class ShadowGeocoder {
    private String addressLine1;
    private String city;
    private String state;
    private String zip;
    private String countryCode;
    private boolean wasCalled;
    private double lastLatitude;
    private double lastLongitude;
    private double simulatedLatitude;
    private double simulatedLongitude;
    private boolean shouldSimulateGeocodeException;

    @Implementation
    public List<Address> getFromLocation(double latitude, double longitude, int maxResults) throws IOException {
        wasCalled = true;
        this.lastLatitude = latitude;
        this.lastLongitude = longitude;
        if (shouldSimulateGeocodeException) {
            throw new IOException("Simulated geocode exception");
        }
        Address address = new Address(Locale.getDefault());
        address.setAddressLine(0, addressLine1);
        address.setLocality(city);
        address.setAdminArea(state);
        address.setPostalCode(zip);
        address.setCountryCode(countryCode);
        return oneElementList(address);
    }

    @Implementation
    public List<Address> getFromLocationName(String locationName, int maxResults) throws IOException {
        if (shouldSimulateGeocodeException) {
            throw new IOException("Simulated geocode exception");
        }
        Address address = new Address(Locale.getDefault());
        address.setLatitude(simulatedLatitude);
        address.setLongitude(simulatedLongitude);
        return oneElementList(address);
    }

    /**
     * Sets up a simulated response for {@link #getFromLocation(double, double, int)}
     *
     * @param address the address for the response
     * @param city the city for the response
     * @param state the state for the response
     * @param zip the zip code for the response
     * @param countryCode the country code for the response
     */
    public void setSimulatedResponse(String address, String city, String state, String zip, String countryCode) {
        this.addressLine1 = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.countryCode = countryCode;
    }


    /**
     * Sets up a simulated response for {@link #getFromLocationName(String, int)}}
     *
     * @param lat latitude for simulated response
     * @param lng longitude for simulated response
     */
    public void setSimulatedLatLong(double lat, double lng) {
        this.simulatedLatitude = lat;
        this.simulatedLongitude = lng;
    }

    /**
     * Sets a flag to indicate whether or not {@link #getFromLocationName(String, int)} should throw an exception to
     * simulate a failure.
     *
     * @param shouldSimulateException whether or not an exception should be thrown from {@link #getFromLocationName(String, int)}
     */
    public void setShouldSimulateGeocodeException(boolean shouldSimulateException) {
        this.shouldSimulateGeocodeException = true;
    }

    /**
     * Non-Android accessor that indicates whether {@link #getFromLocation(double, double, int)} was called.
     *
     * @return whether {@link #getFromLocation(double, double, int)} was called.
     */
    public boolean wasGetFromLocationCalled() {
        return wasCalled;
    }

    public double getLastLongitude() {
        return lastLongitude;
    }

    public double getLastLatitude() {
        return lastLatitude;
    }

    private List<Address> oneElementList(Address address) {
        ArrayList<Address> addresses = new ArrayList<Address>();
        addresses.add(address);
        return addresses;
    }
}