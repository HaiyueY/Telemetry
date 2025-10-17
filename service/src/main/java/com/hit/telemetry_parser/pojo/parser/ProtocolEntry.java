package com.hit.telemetry_parser.pojo.parser;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProtocolEntry {

    private int offset;

    private int width;

    private int radix;
}
