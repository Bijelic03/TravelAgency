package com.ftn.TravelOrganisation.model;

import java.util.Arrays;

public enum KorisnikUloga {
PUTNIK("Putnik"),ADMINISTRATOR("Administrator"),MENADZER("Menadžer");

private final String displayName;

	KorisnikUloga(String displayName) {
this.displayName = displayName;
}

public String getDisplayName() {
return displayName;
}

public static KorisnikUloga fromDisplayName(String displayName) {
return Arrays.stream(KorisnikUloga.values())
        .filter(enumValue -> enumValue.getDisplayName().equals(displayName))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Nema enum vrednosti za displayName: " + displayName));
}
}
