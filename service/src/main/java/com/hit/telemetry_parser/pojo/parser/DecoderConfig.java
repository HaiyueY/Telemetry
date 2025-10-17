package com.hit.telemetry_parser.pojo.parser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DecoderConfig {
    // Mapping of satellite IDs to names
    public static final Map<Integer, String> SAT_ID_NAME_DICT = new HashMap<>();

    // List of satellite names
    public static final List<String> SAT_NAME = Arrays.asList(
            "DSLWP-A",
            "DSLWP-B",
            "DSLWP-C",
            "TD-2",
            "ASRTU-1"
    );

    // Timing constants
    public static final long MAX_WAIT_TIME = 5000; // in milliseconds

    public static final double CHECK_FRAME_TIME = 0.5; // in seconds

    // Static initializer for SAT_ID_NAME_DICT
    static {
        SAT_ID_NAME_DICT.put(0x092, "DSLWP-A");
        SAT_ID_NAME_DICT.put(0x192, "DSLWP-A");
        SAT_ID_NAME_DICT.put(0x093, "DSLWP-B");
        SAT_ID_NAME_DICT.put(0x193, "DSLWP-B");
        SAT_ID_NAME_DICT.put(0x094, "DSLWP-C");
        SAT_ID_NAME_DICT.put(0x194, "DSLWP-C");
        SAT_ID_NAME_DICT.put(0x0AE, "TD-2");
        SAT_ID_NAME_DICT.put(0x032, "ASRTU-1");
        // Fixed syntax  // Fixed
    }

    // Private constructor to prevent instantiation
    private DecoderConfig() {
        throw new UnsupportedOperationException("Utility class, cannot be instantiated");
    }
}
