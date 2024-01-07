package com.ftn.TravelOrganisation.model;

import java.util.Arrays;

public enum PrevoznoSredstvoTipEnum {
AVION("Avion"),AUTOBUS("Autobus"),BROD("Brod");

private final String displayName;

	PrevoznoSredstvoTipEnum(String displayName) {
    this.displayName = displayName;
}

public String getDisplayName() {
    return displayName;
}

public static PrevoznoSredstvoTipEnum fromDisplayName(String displayName) {
    return Arrays.stream(PrevoznoSredstvoTipEnum.values())
            .filter(enumValue -> enumValue.getDisplayName().equals(displayName))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Nema enum vrednosti za displayName: " + displayName));
}
}
