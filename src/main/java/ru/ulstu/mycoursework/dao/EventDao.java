package ru.ulstu.mycoursework.dao;

import org.springframework.stereotype.Component;
import ru.ulstu.mycoursework.models.Event;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

@Component
public class EventDao {
    private static int EVENT_INDEX = 0;
    private ArrayList<Event> events;

    {
        events = new ArrayList<>();
        events.add(new Event(++EVENT_INDEX, "Test event 1", "Tester1, Tester2", LocalDate.now(), LocalTime.of(12, 0), LocalTime.of(15, 0)));
        events.add(new Event(++EVENT_INDEX, "Test event 2", "Tester2, Tester3", LocalDate.now().plusDays(1), LocalTime.of(14, 0), LocalTime.of(17, 0)));
    }

    public ArrayList<Event> list() {
        return events;
    }

    public Event findById(int id) {
        return events.stream().filter(e -> e.getId() == id).findAny().orElse(null);
    }

    public void create(Event event) {
        event.setId(++EVENT_INDEX);
        events.add(event);
    }

    public void update(int id, Event event) {
        var eventFromDb = findById(id);
        eventFromDb.setName(event.getName());
        eventFromDb.setDate(event.getDate());
        eventFromDb.setStartTime(event.getStartTime());
        eventFromDb.setEndTime(event.getEndTime());
    }

    public void delete(int id) {
        events.removeIf(e -> e.getId() == id);
    }

    public boolean isTimeIntersect(Event event) {
        return list()
                .stream()
                .anyMatch(e -> e.getDate().isEqual(event.getDate())
                        && e.getId() != event.getId()
                        && ((e.getStartTime().isBefore(event.getStartTime()) && e.getEndTime().isAfter(event.getStartTime()))
                        || (e.getStartTime().isBefore(event.getEndTime()) && e.getEndTime().isAfter(event.getEndTime()))
                        || (e.getEndTime().isBefore(event.getEndTime()) && e.getStartTime().isAfter(event.getStartTime()))));
    }
}
