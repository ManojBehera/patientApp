package com.example.imac.chs_pharmacy;

import java.util.UUID;

public class BleDefinedUUIDs {
	
	public static class Service {
		final static public UUID CAP_SERVICE  = UUID.fromString("0000180b-0000-1000-8000-00805f9b34fb");
	};
	
	public static class Characteristic {
		final static public UUID INPUT_KEY   = UUID.fromString("00003001-0000-1000-8000-00805f9b34fb");
		final static public UUID INPUT_VALUE      = UUID.fromString("00003000-0000-1000-8000-00805f9b34fb");
		final static public UUID SET_POINTER      = UUID.fromString("00003003-0000-1000-8000-00805f9b34fb");
		final static public UUID READ_VALUE = UUID.fromString("00003004-0000-1000-8000-00805f9b34fb");
	}

	
}
