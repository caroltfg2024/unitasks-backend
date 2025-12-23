package com.josemiguelhyb.unitasks.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController {

	@GetMapping("/hello")
	public String hello() {
		return "Organiza tu éxito académico y profesional. UniTasks es la plataforma integral diseñada para centralizar el control de tus notas y optimizar la gestión de tus tareas diarias con una fluidez incomparable.";
	}
}
