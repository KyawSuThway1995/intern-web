package com.jdc.clinic.controller.member;

import java.time.LocalDate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jdc.clinic.entity.Account;
import com.jdc.clinic.entity.Member;
import com.jdc.clinic.services.BookingService;

@Controller
@RequestMapping("/member/bookings")
public class MemberBookingController {

	@Autowired
	BookingService bService;

	@GetMapping
	public String index(ModelMap model, HttpServletRequest request) {
		// TODO check Login user or not
		HttpSession session = request.getSession(true);

		model.put("familyMembers",
				bService.getFamilyMembersByPhone(((Account) session.getAttribute("loginUser")).getPhone()));

		model.put("clinics", bService.findClinics());

		model.put("doctors", bService.findDoctors());

		model.put("bookings",
				bService.getBookingByMemberAndDate((((Member) session.getAttribute("member")).getPhone())));

		return "/views/member/bookings";
	}

	@GetMapping("/{date}")
	public String showBookingByDate(@PathVariable("date") String date, ModelMap model) {
		model.put("bookings", bService.getBookingByDate("member", LocalDate.parse(date)));
		return "/views/member/bookings";
	}

	@PostMapping
	public String book() {
		// TODO need to consider parameter
		return "redirect:/member/bookings/**";
	}

//	@GetMapping("{id}")
//	public String findById(@PathVariable long id, ModelMap model) {
//		return "/views/member/booking";
//	}

	@GetMapping("delete/{id}")
	public String delete(@PathVariable("id") long id) {
		bService.delete(bService.findById(id).get());
		return "redirect:/member/bookings";
	}

	@PostMapping("{id}")
	public String cancel(@PathVariable long id) {
		return "redirect:/member/bookings/**";
	}
}
