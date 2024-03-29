package com.jdc.clinic.controller.partner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jdc.clinic.entity.Doctor;
import com.jdc.clinic.entity.Partner;
import com.jdc.clinic.services.ClinicServices;
import com.jdc.clinic.services.DoctorService;

@Controller
@RequestMapping("/partner/doctors")
public class DoctorController {

	@Autowired
	private DoctorService dService;

	@Autowired
	private ClinicServices cService;

	@GetMapping
	public String all(ModelMap model, HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		model.put("doctors", dService.findAll());
		model.put("clinics", cService.findByOwnerPhone(((Partner) session.getAttribute("partnerUser")).getPhone()));
		return "/views/partner/doctors";
	}

	@GetMapping("{clinicId}")
	public String index(@PathVariable int clinicId, ModelMap model) {
		model.put("doctors", dService.getDoctorsByClinicId(clinicId));
		return "/views/partner/doctors";
	}

	@GetMapping("create")
	public String create(ModelMap model) {
		model.put("doctor", new Doctor());
		model.put("id", dService.findLast().getId() + 1);
		model.put("clinics", cService.findAll());
		return "/views/partner/doctor-edit";
	}

	@GetMapping("edit/{id}")
	public String edit(@PathVariable int id, ModelMap model, HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		model.put("doctor", dService.findById(id));
		model.put("clinics", cService.findByOwnerPhone(((Partner) session.getAttribute("partnerUser")).getPhone()));
		return "/views/partner/doctor-edit";
	}


	@PostMapping("edit/{id}")
	public String save(Doctor doctor, BindingResult result, @PathVariable int id) {
		if (result.hasErrors()) {
			return "/views/partner/doctor-edit";
		}
		if (dService.findById(id).isPresent()) {
			doctor.setId(id);
		}
		dService.save(doctor);
		return "redirect:/partner/doctors";
	}

	@GetMapping("details/{id}")
	public String findById(@PathVariable int id, ModelMap model) {
		model.put("doctor", dService.findById(id).get());
		return "/views/partner/doctor";
	}

	@GetMapping("delete/{id}")
	public String delete(@PathVariable("id") int id) {
		dService.delete(dService.findById(id).get());
		return "redirect:/partner/doctors";
	}
}
