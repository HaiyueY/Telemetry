package com.hit.telemetry_parser.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SelectItemVO {

    private Integer key;

    private Long value;

    private String label;
}