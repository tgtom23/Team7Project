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

import com.bah.msd.mcc.domain.Event;
import com.bah.msd.mcc.repository.EventRepository;


@RestController
@RequestMapping("/events")
public class EventAPI {
	
	@Autowired
	EventRepository repo;
	
	@GetMapping
	public Iterable<Event> getAll() {
		// Implement a method to retrieve all events
		return repo.findAll();
	}
	
	@GetMapping("/{eventId}")
	public Optional<Event> getEventById(@PathVariable("eventId") long id) {
		// Implement a method to retrieve a single event by it's ID
		return repo.findById(id);
	}
	
	@PostMapping
	public ResponseEntity<?> addEvent(@RequestBody Event newEvent, UriComponentsBuilder uri) {
		if (newEvent.getId() != 0 || newEvent.getCode() == null || newEvent.getTitle() == null || newEvent.getDescription() == null) {
			// Reject we'll assign the event id
			return ResponseEntity.badRequest().build();
		}
		newEvent = repo.save(newEvent);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(newEvent.getId()).toUri();
		ResponseEntity<?> response = ResponseEntity.created(location).build();
		return response;
	}
	
	@PutMapping("/{eventId}")
	public ResponseEntity<?> putEvent(
			@RequestBody Event newEvent,
			@PathVariable("eventId") long eventId) 
	{
		if (newEvent.getId() != eventId || newEvent.getCode() == null || newEvent.getTitle() == null || newEvent.getDescription() == null) {
			return ResponseEntity.badRequest().build();
		}
		newEvent = repo.save(newEvent);
		return ResponseEntity.ok().build();
	}	
	
	@DeleteMapping("/{eventId}")
	public ResponseEntity<?> deleteEventById(@PathVariable("eventId") long id) {
		// repo.delete(id);
		repo.deleteById(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

/*public class EventAPI {
	ArrayList<Event> eventList = new ArrayList<Event>();

	public EventAPI() {
		Event e1 = new Event(1, "CNF001", "All-Java Conference", "Lectures and exhibits covering all Java topics");
		Event e2 = new Event(2, "WKS002", "Spring Boot Workshop", "Hands-on Spring Boot Workshop");
		Event e3 = new Event(3, "TRN001", "Angular Training Course", "Five day introductory training in Angular");
		Event e4 = new Event(4, "RNR004", "Rock n Roll Concert", "BAH Employees RocknRoll Concert");
		
		eventList.add(e1);
		eventList.add(e2);
		eventList.add(e3);
		eventList.add(e4);
		
	}

	@GetMapping
	public Collection<Event> getAll() {
		return this.eventList;
	}

	@GetMapping("/{eventId}")
	public Event getCustomerById(@PathVariable("eventId") long id) {
		
		Event event = null;
		for (int i = 0; i < eventList.size(); i++) {
			if (eventList.get(i).getId() == id) {
				event = eventList.get(i);
			}
		}
		return event;
	}*/
}
