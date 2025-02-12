package com.metamong.mt.global.location;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class LatLng {
    private final double latitude;
    private final double longitude;
}
