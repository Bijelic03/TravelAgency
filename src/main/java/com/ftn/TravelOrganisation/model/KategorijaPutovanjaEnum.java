package com.ftn.TravelOrganisation.model;

import java.util.Arrays;

public enum KategorijaPutovanjaEnum {
	ZIMOVANJE("Zimovanje"), LETOVANJE("Letovanje"), LAST_MINUTE("Last minute"), NOVA_GODINA("Nova godina");

	private final String displayName;

	KategorijaPutovanjaEnum(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
    public static KategorijaPutovanjaEnum fromDisplayName(String displayName) {
        return Arrays.stream(KategorijaPutovanjaEnum.values())
                .filter(enumValue -> enumValue.getDisplayName().equals(displayName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Nema enum vrednosti za displayName: " + displayName));
    }
}
