package com.ftn.TravelOrganisation.model;

import java.util.Arrays;

public enum SmestajnaJedinicaTipEnum {
    APARTMAN("Apartman"), HOTEL_NOCENJE("Hotel (noćenje)"), HOTEL_NOCENJE_DORUCAK("Hotel (noćenje + doručak)"), HOTEL_POLUPANSION("Hotel (polupansion)");

    private final String displayName;

    SmestajnaJedinicaTipEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static SmestajnaJedinicaTipEnum fromDisplayName(String displayName) {
        return Arrays.stream(SmestajnaJedinicaTipEnum.values())
                .filter(enumValue -> enumValue.getDisplayName().equals(displayName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Nema enum vrednosti za displayName: " + displayName));
    }
}
