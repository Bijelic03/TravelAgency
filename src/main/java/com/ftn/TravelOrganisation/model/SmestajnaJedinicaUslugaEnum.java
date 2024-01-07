package com.ftn.TravelOrganisation.model;

import java.util.Arrays;

public enum SmestajnaJedinicaUslugaEnum {
    WIFI("WiFi"), KUPATILO("Kupatilo"), TV("Televizor"), KLIMA("Klima");

    private final String displayName;

    SmestajnaJedinicaUslugaEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static SmestajnaJedinicaUslugaEnum fromDisplayName(String displayName) {
        return Arrays.stream(SmestajnaJedinicaUslugaEnum.values())
                .filter(enumValue -> enumValue.getDisplayName().equals(displayName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Nema enum vrednosti za displayName: " + displayName));
    }
}
