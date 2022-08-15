package com.bah.msd.mcc.api;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import com.bah.msd.mcc.domain.Registration;
import com.bah.msd.mcc.repository.RegistrationRepository;

@RestController
@RequestMapping("/registrations")
public class RegistrationAPI {

	@Autowired
	RegistrationRepository repo;

	@GetMapping
	public Iterable<Registration> getAll() {
		//  Workshop:  Implementation to return existing registrations
		return repo.findAll();
	}

	@GetMapping("/{registrationId}")
	public Optional<Registration> getRegistrationById(@PathVariable("registrationId") long id) {
		//  Workshop:  Implementation to return a single registration from an ID
		return repo.findById(id);
	}

	@PostMapping
	public ResponseEntity<?> addRegistration(@RequestBody Registration newRegistration, UriComponentsBuilder uri) {
		//  Workshop:  Implementation to add a new registration; think about data validation and error handling.
		if (newRegistration.getId() != 0 || newRegistration.getEvent_id() == null || newRegistration.getCustomer_id() == null || newRegistration.getRegistration_date() == null) {
			// Reject we'll assign the event id
			return ResponseEntity.badRequest().build();
		}
		newRegistration = repo.save(newRegistration);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(newRegistration.getId()).toUri();
		ResponseEntity<?> response = ResponseEntity.created(location).build();
		return response;
	}

	@PutMapping("/{eventId}")
	public ResponseEntity<?> putRegistration(
			@RequestBody Registration newRegistration,
			@PathVariable("eventId") long eventId) 
	{
		// Workshop: Implementation to update an event. Think about error handling.
		if (newRegistration.getEvent_id() == null || newRegistration.getCustomer_id() == null ) { //|| newRegistration.getRegistration_date() == null) {
			return ResponseEntity.badRequest().build();
		}
		newRegistration = repo.save(newRegistration);
		return ResponseEntity.ok().build();
	}	
	
	@DeleteMapping("/{eventId}")
	public ResponseEntity<?> deleteRegistrationById(@PathVariable("eventId") long id) {
		//  Workshop:  Implementation to delete an event.  For discussion (do not implement unless
		//  you are sure you have time):  Are there checks you should make to ensure validity of 
		//  data across various entities?  Where should these checks be implemented.  Are there
		//  advantages and disadvantages to separating data into separate independent entities,
		//  each with it's own "microservice"?
		repo.deleteById(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}	
	
}