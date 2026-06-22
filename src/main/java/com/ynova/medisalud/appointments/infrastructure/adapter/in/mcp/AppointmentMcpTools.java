package com.ynova.medisalud.appointments.infrastructure.adapter.in.mcp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import com.ynova.medisalud.appointments.domain.model.Appointment;
import com.ynova.medisalud.appointments.domain.model.TimeSlot;
import com.ynova.medisalud.appointments.domain.service.AppointmentService;
import com.ynova.medisalud.appointments.domain.service.DoctorService;
import com.ynova.medisalud.appointments.domain.service.SedeService;

@Component
public class AppointmentMcpTools {

    private final AppointmentService appointmentService;
    private final DoctorService doctorService;
    private final SedeService sedeService;

    public AppointmentMcpTools(AppointmentService appointmentService,
                               DoctorService doctorService,
                               SedeService sedeService) {
        this.appointmentService = appointmentService;
        this.doctorService = doctorService;
        this.sedeService = sedeService;
    }

    @Tool(description = "Lista todas las sedes disponibles de MediSalud con su direccion y ciudad")
    public List<SedeInfo> listarSedes() {
        return sedeService.getAll().stream()
                .map(s -> new SedeInfo(s.getId(), s.getName(), s.getAddress(), s.getCity()))
                .toList();
    }

    @Tool(description = "Busca medicos por sede. Retorna los medicos que atienden en una sede especifica")
    public List<DoctorInfo> buscarMedicosPorSede(
            @ToolParam(description = "ID de la sede donde buscar medicos") Long sedeId) {
        return sedeService.getDoctorsBySede(sedeId).stream()
                .map(d -> new DoctorInfo(d.getId(), d.getName(), d.getSpecialty(), d.getEmail()))
                .toList();
    }

    @Tool(description = "Lista todos los medicos disponibles en MediSalud con su especialidad")
    public List<DoctorInfo> listarMedicos() {
        return doctorService.getAll().stream()
                .map(d -> new DoctorInfo(d.getId(), d.getName(), d.getSpecialty(), d.getEmail()))
                .toList();
    }

    @Tool(description = "Consulta las franjas horarias disponibles de un medico en una sede para un rango de fechas. Cada franja es de 30 minutos")
    public List<SlotInfo> consultarDisponibilidad(
            @ToolParam(description = "ID del medico") Long doctorId,
            @ToolParam(description = "ID de la sede") Long sedeId,
            @ToolParam(description = "Fecha inicio en formato YYYY-MM-DD") String fechaInicio,
            @ToolParam(description = "Fecha fin en formato YYYY-MM-DD") String fechaFin) {
        List<TimeSlot> slots = appointmentService.getAvailableSlots(
                doctorId, sedeId,
                LocalDate.parse(fechaInicio),
                LocalDate.parse(fechaFin));
        return slots.stream()
                .map(s -> new SlotInfo(s.start().toString(), s.end().toString()))
                .toList();
    }

    @Tool(description = "Reserva una cita medica para un paciente con un medico en una sede y horario especifico. El horario debe estar disponible y en formato ISO 8601")
    public AppointmentInfo reservarCita(
            @ToolParam(description = "ID del paciente") Long patientId,
            @ToolParam(description = "ID del medico") Long doctorId,
            @ToolParam(description = "ID de la sede") Long sedeId,
            @ToolParam(description = "Fecha y hora en formato ISO 8601, ejemplo: 2026-07-06T10:00:00") String dateTime) {
        Appointment apt = appointmentService.book(
                patientId, doctorId, sedeId, LocalDateTime.parse(dateTime));
        return toAppointmentInfo(apt);
    }

    @Tool(description = "Cancela una cita programada. Si se cancela con menos de 2 horas de antelacion se aplica una penalizacion al paciente")
    public AppointmentInfo cancelarCita(
            @ToolParam(description = "ID de la cita a cancelar") Long appointmentId) {
        Appointment apt = appointmentService.cancel(appointmentId);
        return toAppointmentInfo(apt);
    }

    @Tool(description = "Reprograma una cita existente a un nuevo horario. Cancela la cita anterior y crea una nueva validando disponibilidad")
    public AppointmentInfo reprogramarCita(
            @ToolParam(description = "ID de la cita a reprogramar") Long appointmentId,
            @ToolParam(description = "Nueva fecha y hora en formato ISO 8601") String nuevaFechaHora) {
        Appointment apt = appointmentService.reschedule(
                appointmentId, LocalDateTime.parse(nuevaFechaHora));
        return toAppointmentInfo(apt);
    }

    private AppointmentInfo toAppointmentInfo(Appointment apt) {
        return new AppointmentInfo(apt.getId(), apt.getDoctorId(), apt.getPatientId(),
                apt.getSedeId(), apt.getDateTime().toString(), apt.getStatus().name(),
                apt.isPenaltyApplied());
    }

    record SedeInfo(Long id, String nombre, String direccion, String ciudad) {}
    record DoctorInfo(Long id, String nombre, String especialidad, String email) {}
    record SlotInfo(String inicio, String fin) {}
    record AppointmentInfo(Long id, Long doctorId, Long patientId, Long sedeId,
                           String fechaHora, String estado, boolean penalizacion) {}
}
