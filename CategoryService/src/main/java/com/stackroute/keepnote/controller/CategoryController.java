package com.stackroute.keepnote.controller;

import java.util.Date;
import java.util.List;

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

import com.stackroute.keepnote.exception.CategoryDoesNoteExistsException;
import com.stackroute.keepnote.exception.CategoryNotCreatedException;
import com.stackroute.keepnote.model.Category;
import com.stackroute.keepnote.service.CategoryService;

/*
 * As in this assignment, we are working with creating RESTful web service, hence annotate
 * the class with @RestController annotation.A class annotated with @Controller annotation
 * has handler methods which returns a view. However, if we use @ResponseBody annotation along
 * with @Controller annotation, it will return the data directly in a serialized 
 * format. Starting from Spring 4 and above, we can use @RestController annotation which 
 * is equivalent to using @Controller and @ResposeBody annotation
 */
@RestController
@RequestMapping("/api/v1/")
@CrossOrigin("*")
public class CategoryController {

	/*
	 * Autowiring should be implemented for the CategoryService. (Use
	 * Constructor-based autowiring) Please note that we should not create any
	 * object using the new keyword
	 */
	CategoryService categoryservice;

	@Autowired
	public CategoryController(CategoryService cat)
	{
		this.categoryservice=cat;
	}
	
	
	/*
	 * Define a handler method which will create a category by reading the
	 * Serialized category object from request body and save the category in
	 * database. Please note that the careatorId has to be unique.This
	 * handler method should return any one of the status messages basis on
	 * different situations: 
	 * 1. 201(CREATED - In case of successful creation of the category
	 * 2. 409(CONFLICT) - In case of duplicate categoryId
	 *
	 * 
	 * This handler method should map to the URL "/api/v1/category" using HTTP POST
	 * method".
	 */
	@PostMapping("category")
	public ResponseEntity<?> registerUser(@RequestBody Category category){
		ResponseEntity<?> responseEntity = null;
		category.setCategoryCreationDate(new Date());
		try {
			if(categoryservice.createCategory(category)!=null)
			{
			responseEntity = new ResponseEntity<String>("Created", HttpStatus.CREATED);
			}
			else
				responseEntity = new ResponseEntity<String>("already present", HttpStatus.CONFLICT);

				
		} 
		catch (CategoryNotCreatedException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			responseEntity = new ResponseEntity<String>("Conflict", HttpStatus.CONFLICT);
		}
		return responseEntity;
	}
	
	/*
	 * Define a handler method which will delete a category from a database.
	 * 
	 * This handler method should return any one of the status messages basis on
	 * different situations: 1. 200(OK) - If the category deleted successfully from
	 * database. 2. 404(NOT FOUND) - If the category with specified categoryId is
	 * not found. 
	 * 
	 * This handler method should map to the URL "/api/v1/category/{id}" using HTTP Delete
	 * method" where "id" should be replaced by a valid categoryId without {}
	 */
	@DeleteMapping("category/{id}")
	public ResponseEntity<?> createCategory(@PathVariable("id") String id){
		ResponseEntity<?> responseEntity = null;
		try {
			if(categoryservice.deleteCategory(id)==true)
			{
				responseEntity = new ResponseEntity<String>("Deleted", HttpStatus.OK);
			}
			else
				responseEntity = new ResponseEntity<String>("not found", HttpStatus.NOT_FOUND);

		} catch (CategoryDoesNoteExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			responseEntity = new ResponseEntity<String>("conflict", HttpStatus.NOT_FOUND);

		}
		return responseEntity;
	}
	

	
	/*
	 * Define a handler method which will update a specific category by reading the
	 * Serialized object from request body and save the updated category details in
	 * database. This handler method should return any one of the status
	 * messages basis on different situations: 1. 200(OK) - If the category updated
	 * successfully. 2. 404(NOT FOUND) - If the category with specified categoryId
	 * is not found. 
	 * This handler method should map to the URL "/api/v1/category/{id}" using HTTP PUT
	 * method.
	 */
	@PutMapping("category/{id}")
	public ResponseEntity<?> updateCategory(@PathVariable("id") String id, @RequestBody Category category){
		ResponseEntity<?> responseEntity;
		if(categoryservice.updateCategory(category, id) != null)
		{
			responseEntity = new ResponseEntity<String>("updated", HttpStatus.OK);
		}
		else
		{
			responseEntity = new ResponseEntity<String>("not found", HttpStatus.CONFLICT);
		}
		return responseEntity;
	}
	
	/*
	 * Define a handler method which will get us the category by a userId.
	 * 
	 * This handler method should return any one of the status messages basis on
	 * different situations: 1. 200(OK) - If the category found successfully. 
	 * 
	 * 
	 * This handler method should map to the URL "/api/v1/category" using HTTP GET method
	 */
	@GetMapping("category/{id}")
	public ResponseEntity<?> getCategoryDetail(@PathVariable("id") String id)
	{
		ResponseEntity<?> responseEntity;
		if(categoryservice.getAllCategoryByUserId(id) != null)
		{
			List<Category> categories = categoryservice.getAllCategoryByUserId(id);

			responseEntity = new ResponseEntity<List< Category>>(categories,HttpStatus.OK);
		}
		else
			responseEntity = new ResponseEntity<String>("not found", HttpStatus.NOT_FOUND);
		return responseEntity;	
	}
	


}
