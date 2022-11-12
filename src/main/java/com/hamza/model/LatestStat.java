package com.hamza.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LatestStat {

    private String state;
    private String country;
    private int latestCount;
    private int deltaFromPreviousDay;
}
