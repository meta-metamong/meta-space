package com.metamong.mt.global.location;

public interface LocationProvider {

    LatLng convertAddressToLatLng(String address) throws AmbiguousAddressException;
}
