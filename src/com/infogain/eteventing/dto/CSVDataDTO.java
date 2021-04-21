package com.infogain.eteventing.dto;

import lombok.Getter;
import lombok.Setter;

public class CSVDataDTO {
	@Getter @Setter
	String locator;
	@Getter @Setter
	String version;
	@Getter @Setter
	String parent_locator;
	@Getter @Setter
	String data;
}
