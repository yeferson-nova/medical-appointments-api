package com.ynova.medisalud.appointments.domain.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ynova.medisalud.appointments.domain.exception.InvalidScheduleException;
import com.ynova.medisalud.appointments.domain.model.TimeSlot;

public final class BusinessHoursValidator {

    private static final LocalTime WEEKDAY_START = LocalTime.of(8, 0);
    private static final LocalTime WEEKDAY_END = LocalTime.of(18, 0);
    private static final LocalTime SATURDAY_START = LocalTime.of(8, 0);
    private static final LocalTime SATURDAY_END = LocalTime.of(13, 0);

    private BusinessHoursValidator() {}

    public static void validate(java.time.LocalDateTime dateTime) {
        if (dateTime == null) {
            throw new InvalidScheduleException("La fecha y hora son obligatorias");
        }

        DayOfWeek day = dateTime.getDayOfWeek();
        LocalTime time = dateTime.toLocalTime();
        int minutes = time.getMinute();

        if (minutes != 0 && minutes != 30) {
            throw new InvalidScheduleException(
                    "Las citas solo pueden agendarse en franjas de 30 minutos (minutos: 00 o 30)");
        }

        if (day == DayOfWeek.SUNDAY) {
            throw new InvalidScheduleException("No hay atencion los domingos");
        }

        if (day == DayOfWeek.SATURDAY) {
            if (time.isBefore(SATURDAY_START) || time.isAfter(SATURDAY_END.minusMinutes(30))
                    || time.equals(SATURDAY_END)) {
                throw new InvalidScheduleException(
                        "Los sabados la atencion es de 08:00 a 13:00. Ultima franja: 12:30");
            }
            return;
        }

        if (time.isBefore(WEEKDAY_START) || time.isAfter(WEEKDAY_END.minusMinutes(30))
                || time.equals(WEEKDAY_END)) {
            throw new InvalidScheduleException(
                    "La atencion de lunes a viernes es de 08:00 a 18:00. Ultima franja: 17:30");
        }
    }

    public static List<TimeSlot> generateSlots(LocalDate date) {
        if (date == null) {
            return Collections.emptyList();
        }

        DayOfWeek day = date.getDayOfWeek();
        if (day == DayOfWeek.SUNDAY) {
            return Collections.emptyList();
        }

        LocalTime start = (day == DayOfWeek.SATURDAY) ? SATURDAY_START : WEEKDAY_START;
        LocalTime end = (day == DayOfWeek.SATURDAY) ? SATURDAY_END : WEEKDAY_END;

        List<TimeSlot> slots = new ArrayList<>();
        LocalTime current = start;
        while (current.isBefore(end)) {
            slots.add(TimeSlot.of(date.atTime(current)));
            current = current.plusMinutes(30);
        }
        return slots;
    }
}
