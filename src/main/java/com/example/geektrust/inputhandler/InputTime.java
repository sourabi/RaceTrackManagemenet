package com.example.geektrust.inputhandler;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalTime;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class InputTime {
    TimeType timeType;
    LocalTime requestedTime;
}
