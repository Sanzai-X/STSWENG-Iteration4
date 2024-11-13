 package com.orangeandbronze.enlistment.domain;

import jakarta.persistence.*;
import java.time.*;
import java.util.Objects;

import static org.apache.commons.lang3.Validate.*;

@Embeddable
public class Period {

    private final LocalTime startTime;
    private final LocalTime endTime;

    public Period(LocalTime startTime, LocalTime endTime) {
        notNull(startTime, "startTime can't be null");
        notNull(endTime, "endTime can't be null");
        isTrue(startTime.isBefore(endTime),
                "startTime must be before endTime; startTime: " + startTime + " endTime: " + endTime);
        isTrue(!startTime.isBefore(LocalTime.of(8, 30)), "startTime can't be before 8:30am, was: " + startTime);
        isTrue(!endTime.isAfter(LocalTime.of(17, 30)), "endTime can't be after 5:30pm, was " + endTime);
        checkIf30MinIncrement(startTime);
        checkIf30MinIncrement(endTime);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    private void checkIf30MinIncrement(LocalTime time) {
        notNull(time, "time can't be null");
        final int minute = time.getMinute();
        if (minute != 0 && minute != 30) {
            throw new InvalidTimeException("should end with ':00' or ':30', was: " + time);
        }
    }

    void checkOverlap(Period other) {
        if (this.startTime.isBefore(other.endTime) && this.endTime.isAfter(other.startTime)) {
            throw new ScheduleConflictException("Period overlap between this: " + this + " & other: " + other);
        }
    }

    @Override
    public String toString() {
        return startTime + " - " + endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Period period = (Period) o;

        if (!Objects.equals(startTime, period.startTime)) return false;
        return Objects.equals(endTime, period.endTime);
    }

    @Override
    public int hashCode() {
        int result = startTime != null ? startTime.hashCode() : 0;
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        return result;
    }

    // For JPA only! Do not call!
    private Period() {
        startTime = null;
        endTime = null;
    }
}
