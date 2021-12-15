package ru.ulstu.mycoursework.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.ulstu.mycoursework.dao.EventDao;
import ru.ulstu.mycoursework.models.Event;

import javax.validation.Valid;

@Controller
@RequestMapping("/events")
public class EventsController {

    private final EventDao eventDao;

    @Autowired
    public EventsController(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("events", eventDao.list());
        return "events/index";
    }

    @GetMapping("/{id}")
    public String details(@PathVariable("id") int id, Model model) {
        model.addAttribute("event", eventDao.findById(id));
        return "events/details";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("event", new Event());
        return "events/create";
    }

    @PostMapping()
    public String create(@ModelAttribute("event") @Valid Event event, BindingResult bindingResult) {
        if (event.getStartTime() != null && event.getEndTime() != null && event.getStartTime().isAfter(event.getEndTime()))
            bindingResult.rejectValue("endTime", "error.event", "End time should be greater than start time");

        if (event.getDate() != null && event.getStartTime() != null && event.getEndTime() != null && eventDao.isTimeIntersect(event)) {
            bindingResult.rejectValue("startTime", "error.event", "Time should not intersect with existing events");
            bindingResult.rejectValue("endTime", "error.event", "Time should not intersect with existing events");
        }

        if (bindingResult.hasErrors())
            return "events/create";

        eventDao.create(event);
        return "redirect:/";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("event", eventDao.findById(id));
        return "events/edit";
    }

    @PatchMapping("/{id}")
    public String edit(@PathVariable("id") int id, @ModelAttribute("event") @Valid Event event, BindingResult bindingResult) {
        if (event.getStartTime() != null && event.getEndTime() != null && event.getStartTime().isAfter(event.getEndTime()))
            bindingResult.rejectValue("endTime", "error.event", "End time should be greater than start time");

        if (event.getDate() != null && event.getStartTime() != null && event.getEndTime() != null && eventDao.isTimeIntersect(event)) {
            bindingResult.rejectValue("startTime", "error.event", "Time should not intersect with existing events");
            bindingResult.rejectValue("endTime", "error.event", "Time should not intersect with existing events");
        }

        if (bindingResult.hasErrors())
            return "events/edit";

        eventDao.update(id, event);
        return "redirect:/events";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        eventDao.delete(id);
        return "redirect:/events";
    }
}
