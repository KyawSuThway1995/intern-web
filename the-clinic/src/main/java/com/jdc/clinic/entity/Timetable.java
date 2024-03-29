package com.jdc.clinic.entity;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Timetable implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private DayOfWeek day;

	private LocalTime timeFrom;

	private LocalTime timeTo;

	@Embedded
	private SecurityInfo security;

	@ManyToOne
	private ClinicDoctor clinicDoctor;

}