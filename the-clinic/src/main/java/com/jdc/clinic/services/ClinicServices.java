package com.jdc.clinic.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jdc.clinic.entity.Clinic;
import com.jdc.clinic.entity.Doctor;
import com.jdc.clinic.entity.Timetable;
import com.jdc.clinic.repo.ClinicRepo;
import com.jdc.clinic.repo.TimeTableRepo;
import com.jdc.clinic.repo.TownshipRepo;

@Service
public class ClinicServices {

	@Autowired
	private ClinicRepo clinicRepo;

	@Autowired
	private TimeTableRepo timeTableRepo;

	@Autowired
	private TownshipRepo townshipRepo;

	public List<Clinic> search(String keyword) {
		return clinicRepo.findByNameContainingIgnoreCase(keyword);
	}

	@Transactional
	public Clinic findById(int id) {
		return clinicRepo.findById(id).map(c -> {
			c.getMails().size();
			c.getPhone().size();
			return c;
		}).orElse(new Clinic());
	}

	public Clinic save(Clinic clinic) {

		clinic.setPhone(clinic.getPhone().stream().filter(p -> !p.isEmpty()).collect(Collectors.toList()));
		clinic.setMails(clinic.getMails().stream().filter(m -> !m.isEmpty()).collect(Collectors.toList()));

		clinic.getAddrress().setTownship(townshipRepo.getOne(clinic.getAddrress().getTownship().getId()));

		return clinicRepo.save(clinic);
	}

	public Map<Doctor, List<Timetable>> findSchedules(int id) {
		return timeTableRepo.findByClinicDoctorClinicId(id).stream()
				.collect(Collectors.groupingBy(a -> a.getClinicDoctor().getDoctor()));

	}

	public List<Clinic> findAll() {
		return clinicRepo.findAll();
	}

	public Clinic findByName(String name) {
		return clinicRepo.findByNameContainingIgnoreCase(name).get(0);
	}

	public List<Clinic> findByOwnerPhone(String phone) {
		return clinicRepo.findByOwnerPhone(phone);
	}

}
