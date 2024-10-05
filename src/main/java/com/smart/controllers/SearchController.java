package com.smart.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.smart.config.HelperToFindUserName;
import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class SearchController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ContactRepository contactRepository;
	@GetMapping("/search/{pattern}")
	public ResponseEntity<List<Contact>> getMethodName(@PathVariable("pattern") String pattern,Authentication authentication) {
		String username=HelperToFindUserName.getEmailOfLoggedinUser(authentication);
		User user=userRepository.getUserByEmail(username);
		List<Contact> contacts= contactRepository.findByNameContainingAndUser(pattern, user);
		contacts.addAll(contactRepository.findBySecondNameContainingAndUser(pattern, user));
		for (Contact contact : contacts) {
			System.out.println(contact.getName()+" "+contact.getSecondName());
			
		}
		return new ResponseEntity<List<Contact>>(contacts,HttpStatus.OK);		
	}
	

}
