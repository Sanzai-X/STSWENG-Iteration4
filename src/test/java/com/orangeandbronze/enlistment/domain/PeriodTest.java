package com.orangeandbronze.enlistment.domain;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import java.time.*;
import java.util.stream.*;

import static org.junit.jupiter.api.Assertions.*;

class PeriodTest {

    @Test
    void newPeriod_valid_minute() {
        assertDoesNotThrow(() -> new Period(LocalTime.of(8, 30), LocalTime.of(10, 0)));
    }

    @Test
    void newPeriod_invalid_minute() {
        assertAll(
                () -> assertThrows(InvalidTimeException.class, () -> new Period(LocalTime.of(9, 15), LocalTime.of(10, 0))),
                () -> assertThrows(InvalidTimeException.class, () -> new Period(LocalTime.of(8, 30), LocalTime.of(10, 15)))
        );
    }

    private static Stream<Arguments> overlappingPeriods() {
        return Stream.of(
                Arguments.of(new Period(LocalTime.of(8, 30), LocalTime.of(10, 0)),
                        new Period(LocalTime.of(9, 0), LocalTime.of(11, 0))),
                Arguments.of(new Period(LocalTime.of(8, 30), LocalTime.of(10, 0)),
                        new Period(LocalTime.of(9, 0), LocalTime.of(9, 30))),
                Arguments.of(new Period(LocalTime.of(9, 0), LocalTime.of(11, 0)),
                        new Period(LocalTime.of(8, 30), LocalTime.of(10, 0))),
                Arguments.of(new Period(LocalTime.of(9, 0), LocalTime.of(9, 30)),
                        new Period(LocalTime.of(8, 30), LocalTime.of(10, 0))));
    }

    @ParameterizedTest
    @MethodSource("overlappingPeriods")
    void checkOverlap_overlapping(Period p1, Period p2) {
		assertThrows(ScheduleConflictException.class, () -> p1.checkOverlap(p2));
    }

    private static Stream<Arguments> notOverlappingPeriods() {
        return Stream.of(
                Arguments.of(new Period(LocalTime.of(8, 30), LocalTime.of(10, 0)),
                        new Period(LocalTime.of(10, 0), LocalTime.of(11, 0))),
                Arguments.of(new Period(LocalTime.of(9, 30), LocalTime.of(10, 0)),
                        new Period(LocalTime.of(9, 0), LocalTime.of(9, 30))),
                Arguments.of(new Period(LocalTime.of(9, 0), LocalTime.of(11, 0)),
                        new Period(LocalTime.of(11, 30), LocalTime.of(13, 0))),
                Arguments.of(new Period(LocalTime.of(9, 0), LocalTime.of(9, 30)),
                        new Period(LocalTime.of(12, 30), LocalTime.of(13, 0))));
    }

    @ParameterizedTest
    @MethodSource("notOverlappingPeriods")
    void checkOverlap_not_overlapping(Period p1, Period p2) {
        assertDoesNotThrow(() -> p1.checkOverlap(p2));
    }
}
