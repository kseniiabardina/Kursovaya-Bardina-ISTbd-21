package ru.ulstu.mycoursework.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ulstu.mycoursework.dao.EventDao;
import ru.ulstu.mycoursework.viewmodels.calendar.CalendarDayOfWeek;
import ru.ulstu.mycoursework.viewmodels.calendar.CalendarHour;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@Component
public class CalendarService {

    private final EventDao eventDao;

    @Autowired
    public CalendarService(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    public CalendarHour[] getCalendar(LocalDate date) {
        var calendarHours = new CalendarHour[24];
        var monday = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        for (var i = 0; i < 24; i++) {
            var hour = i;
            var calendarDaysOfWeek = new CalendarDayOfWeek[7];

            for (var j = 0; j < 7; j++) {
                var dayOfWeek = monday.plusDays(j);
                var event = eventDao
                        .list()
                        .stream()
                        .filter(e -> e.getDate().isEqual(dayOfWeek) && hour >= e.getStartTime().getHour() && hour < e.getEndTime().getHour())
                        .findAny()
                        .orElse(null);

                calendarDaysOfWeek[j] = new CalendarDayOfWeek(
                        dayOfWeek.getDayOfWeek().name(),
                        dayOfWeek,
                        event == null ? null : event.getId(),
                        event == null ? null : event.getName());
            }

            calendarHours[i] = new CalendarHour(i, calendarDaysOfWeek);
        }

        return calendarHours;
    }
}
