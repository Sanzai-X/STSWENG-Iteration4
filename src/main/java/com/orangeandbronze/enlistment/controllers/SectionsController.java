package com.orangeandbronze.enlistment.controllers;

import com.orangeandbronze.enlistment.domain.*;
import com.orangeandbronze.enlistment.domain.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.*;

import java.time.*;
import java.util.*;

@Transactional
@Controller
@RequestMapping("sections")
@SessionAttributes("admin")
class SectionsController {

    @Autowired
    private SubjectRepository subjectRepo;
    @Autowired
    private AdminRepository adminRepo;
    @Autowired
    private RoomRepository roomRepo;
    @Autowired
    private SectionRepository sectionRepo;
    @Autowired
    private FacultyRepository facultyRepo;


    public SectionsController(SubjectRepository subjectRepo, AdminRepository adminRepo, RoomRepository roomRepo, SectionRepository sectionRepo, FacultyRepository facultyRepo) {
        // no null check for easier testing
        this.subjectRepo = subjectRepo;
        this.adminRepo = adminRepo;
        this.roomRepo = roomRepo;
        this.sectionRepo = sectionRepo;
        this.facultyRepo = facultyRepo;
    }

    @ModelAttribute("admin")
    public Admin admin(Integer id) {
        return adminRepo.findById(id).orElseThrow(() -> new NoSuchElementException("no admin found for adminId " + id));
    }

    @GetMapping
    public String showPage(Model model, Integer id) {
        Admin admin = id == null ? (Admin) model.getAttribute("admin") :
                adminRepo.findById(id).orElseThrow(() -> new NoSuchElementException("no admin found for adminId " + id));
        model.addAttribute("admin", admin);
        model.addAttribute("subjects", subjectRepo.findAll());
        model.addAttribute("rooms", roomRepo.findAll());
        model.addAttribute("sections", sectionRepo.findAll());
        model.addAttribute("faculty", facultyRepo.findAll());
        return "sections";
    }

    @PostMapping
    public String createSection(String sectionId, String subjectId, Days days, String roomId, String startTime, String endTime, int facultyID,RedirectAttributes redirectAttributes) {
        Room room = roomRepo.findById(roomId).orElseThrow(() -> new IllegalArgumentException("Room not found"));
        Subject subject = subjectRepo.findById(subjectId).orElseThrow(() -> new IllegalArgumentException("Subject not found"));
        Period period = new Period(LocalTime.parse(startTime), LocalTime.parse(endTime));
        Schedule schedule = new Schedule(days, period);
        Section section = new Section(sectionId, subject, schedule, room);
        if(facultyID != -1){
            Faculty faculty = facultyRepo.findById(facultyID).orElseThrow(() -> new IllegalArgumentException("Faculty not found"));
            section.assignFaculty(faculty);
        }

        sectionRepo.save(section);
        redirectAttributes.addFlashAttribute("sectionSuccessMessage", "Successfully created new section " + sectionId);
        return "redirect:sections";
    }

    @ExceptionHandler(EnlistmentException.class)
    public String handleException(RedirectAttributes redirectAttrs, EnlistmentException e) {
        redirectAttrs.addFlashAttribute("sectionExceptionMessage", e.getMessage());
        return "redirect:sections";
    }
}
