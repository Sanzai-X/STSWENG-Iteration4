package com.orangeandbronze.enlistment.domain;

import org.junit.jupiter.api.*;

import static com.orangeandbronze.enlistment.domain.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

class SectionTest {

    @Test
    void newSection_same_room_diff_sked() {
        Room room = newDefaultRoom();
        Section defaultSec = new SectionBuilder().schedule(MTH830to10).room(room).build();
        assertDoesNotThrow(() -> new SectionBuilder("B").schedule(TF10to1130).room(room).build());
    }

    @Test
    void newSection_same_room_same_sked() {
        Room room = newDefaultRoom();
        Section defaultSec = new SectionBuilder().schedule(MTH830to10).room(room).build();
        assertThrows(ScheduleConflictException.class, () -> new SectionBuilder("B").schedule(MTH830to10).room(room).build());
    }

}
