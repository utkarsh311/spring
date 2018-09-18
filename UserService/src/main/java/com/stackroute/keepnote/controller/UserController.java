package com.stackroute.keepnote.controller;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stackroute.keepnote.exceptions.UserAlreadyExistsException;
import com.stackroute.keepnote.model.User;
import com.stackroute.keepnote.service.UserService;

/*
 * As in this assignment, we are working on creating RESTful web service, hence annotate
 * the class with @RestController annotation. A class annotated with the @Controller annotation
 * has handler methods which return a view. However, if we use @ResponseBody annotation along
 * with @Controller annotation, it will return the data directly in a serialized 
 * format. Starting from Spring 4 and above, we can use @RestController annotation which 
 * is equivalent to using @Controller and @ResposeBody annotation
 */
@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/")
public class UserController {

	/*
	* Autowiring should be implemented for the UserService. (Use Constructor-based
	* autowiring) Please note that we should not create an object using the new
	* keyword
	*/
	private UserService userService;
	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	/*
	* Define a handler method which will create a specific user by reading the
	* Serialized object from request body and save the user details in the
	* database. This handler method should return any one of the status messages
	* basis on different situations:
	* 1. 201(CREATED) - If the user created successfully. 
	* 2. 409(CONFLICT) - If the userId conflicts with any existing user
	* 
	* This handler method should map to the URL "/user" using HTTP POST method
	*/
	@PostMapping("user")
	public ResponseEntity<?> registerUser(@RequestBody User user){
		ResponseEntity<?> responseEntity = null;
		try {
			user.setUserAddedDate(new Date());
			userService.registerUser(user);
			responseEntity = new ResponseEntity<String>("Created", HttpStatus.CREATED);
		} catch (Exception e) {
			responseEntity = new ResponseEntity<String>("Already Present", HttpStatus.CONFLICT);
		}
		return responseEntity;
	}
	/*
	* Define a handler method which will update a specific user by reading the
	* Serialized object from request body and save the updated user details in a
	* database. This handler method should return any one of the status messages
	* basis on different situations: 
	* 1. 200(OK) - If the user updated successfully.
	* 2. 404(NOT FOUND) - If the user with specified userId is not found.
	* 
	* This handler method should map to the URL "/api/v1/user/{id}" using HTTP PUT method.
	*/
	@PutMapping("user/{id}")
	public ResponseEntity<?> updateUser(@PathVariable("id") String id, @RequestBody User user){
		ResponseEntity<?> responseEntity;
		try {
			User u = userService.updateUser(id, user);
			if(u != null)
				responseEntity = new ResponseEntity<User>(u, HttpStatus.OK);
			else
				throw new Exception();
		} catch (Exception e) {
			responseEntity = new ResponseEntity<String>("User not Found", HttpStatus.NOT_FOUND);
		}
		return responseEntity;
	}
	/*
	* Define a handler method which will delete a user from a database.
	* This handler method should return any one of the status messages basis on
	* different situations: 
	* 1. 200(OK) - If the user deleted successfully from database. 
	* 2. 404(NOT FOUND) - If the user with specified userId is not found.
	*
	* This handler method should map to the URL "/api/v1/user/{id}" using HTTP Delete
	* method" where "id" should be replaced by a valid userId without {}
	*/
	@DeleteMapping("user/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable("id") String id){
		ResponseEntity<?> responseEntity;
		try {
			if(userService.deleteUser(id))
				responseEntity = new ResponseEntity<String>("Deleted", HttpStatus.OK);
			else
				throw new Exception();
		} catch (Exception e) {
			responseEntity = new ResponseEntity<String>("User not Found", HttpStatus.NOT_FOUND);
		}
		return responseEntity;
	}
	/*
	* Define a handler method which will show details of a specific user. This
	* handler method should return any one of the status messages basis on
	* different situations: 
	* 1. 200(OK) - If the user found successfully. 
	* 2. 404(NOT FOUND) - If the user with specified userId is not found. 
	* This handler method should map to the URL "/api/v1/user/{id}" using HTTP GET method where "id" should be
	* replaced by a valid userId without {}
	*/
	@GetMapping("user/{id}")
	public ResponseEntity<?> getUserDetails(@PathVariable("id") String id){
		ResponseEntity<?> responseEntity;
		try {
			User user = userService.getUserById(id);
			if(user != null)
				responseEntity = new ResponseEntity<User>(user, HttpStatus.OK);
			else
				throw new Exception();
		} catch (Exception e) {
			responseEntity = new ResponseEntity<String>("User not Found", HttpStatus.NOT_FOUND);
		}
		return responseEntity;
	}
	
}

