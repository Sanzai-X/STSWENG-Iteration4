package com.orangeandbronze.enlistment.controllers;

import com.orangeandbronze.enlistment.domain.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.web.servlet.mvc.support.*;
import java.util.Optional;

import static com.orangeandbronze.enlistment.domain.Days.MTH;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SectionsControllerTest {

    @Mock
    private SectionRepository sectionRepo;

    @Mock
    private RoomRepository roomRepo;

    @Mock
    private SubjectRepository subjectRepo;

    @Mock
    private AdminRepository adminRepo;

    @Mock

    private FacultyRepository facultyRepo;

    private SectionsController sectionsController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        sectionsController = new SectionsController(subjectRepo, adminRepo, roomRepo, sectionRepo, facultyRepo);
    }

    @Test
    void createSection_save_new_section_to_repository() {
        // Given
        String sectionId = "CS101";
        String subjectId = "Math";
        String roomId = "A1";
        Days days = MTH;
        String startTime = "09:00";
        String endTime = "10:30";
        int facultyID = -1;


        Room room = mock(Room.class);
        Subject subject = mock(Subject.class);
        Faculty faculty = mock(Faculty.class);


        when(roomRepo.findById(roomId)).thenReturn(Optional.of(room));
        when(subjectRepo.findById(subjectId)).thenReturn(Optional.of(subject));
        when(facultyRepo.findById(facultyID)).thenReturn(Optional.of(faculty));


        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
        ArgumentCaptor<Section> sectionCaptor = ArgumentCaptor.forClass(Section.class);

        // When
        String result = sectionsController.createSection(sectionId, subjectId, days, roomId, startTime, endTime, facultyID,redirectAttributes);

        // Then
        verify(sectionRepo).save(sectionCaptor.capture());
        Section savedSection = sectionCaptor.getValue();
        assertEquals(sectionId, savedSection.getSectionId());
        assertEquals(subject, savedSection.getSubject());
        assertEquals(room, savedSection.getRoom());

        assertEquals("Successfully created new section " + sectionId,
                redirectAttributes.getFlashAttributes().get("sectionSuccessMessage"));
        assertEquals("redirect:sections", result);
    }
}