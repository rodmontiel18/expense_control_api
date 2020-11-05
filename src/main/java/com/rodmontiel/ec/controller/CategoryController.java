package com.rodmontiel.ec.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rodmontiel.ec.contracts.v1.response.category.AddUserCategoryRs;
import com.rodmontiel.ec.contracts.v1.response.category.DeleteUserCategoryRs;
import com.rodmontiel.ec.contracts.v1.response.category.EditUserCategoryRs;
import com.rodmontiel.ec.contracts.v1.response.category.GetCategoriesByUserEmailRs;
import com.rodmontiel.ec.contracts.v1.response.category.GetCategoryByIdRs;
import com.rodmontiel.ec.dto.CategoryDTO;
import com.rodmontiel.ec.ex.GenericExceptionHandler;
import com.rodmontiel.ec.service.CategoryService;

@RestController
@RequestMapping("/categories")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@GetMapping("/user")
	public ResponseEntity<GetCategoriesByUserEmailRs> getCategoriesByUserEmail(
			@RequestHeader(value = "Authorization") String authData) throws GenericExceptionHandler, Exception {
		return new ResponseEntity<GetCategoriesByUserEmailRs>(categoryService.getCategoriesByUserEmail(authData),
				HttpStatus.OK);
	}

	@GetMapping("/user/type/{categoryType}")
	public ResponseEntity<GetCategoriesByUserEmailRs> getCategoriesByTypeAndUserEmail(
			@RequestHeader(value = "Authorization") String authData, @PathVariable int categoryType)
			throws GenericExceptionHandler, Exception {
		return new ResponseEntity<GetCategoriesByUserEmailRs>(
				categoryService.getCategoriesByTypeAndUserEmail(authData, categoryType), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<AddUserCategoryRs> addCategory(@RequestHeader(value = "Authorization") String authData,
			@RequestBody CategoryDTO category) throws GenericExceptionHandler, Exception {
		return new ResponseEntity<AddUserCategoryRs>(categoryService.addCategory(authData, category), HttpStatus.OK);
	}

	@GetMapping("/user/{categoryId}")
	public ResponseEntity<GetCategoryByIdRs> getUserCategoryById(
			@RequestHeader(value = "Authorization") String authData, @PathVariable long categoryId)
			throws GenericExceptionHandler, Exception {
		return new ResponseEntity<GetCategoryByIdRs>(categoryService.getUserCategoryById(authData, categoryId),
				HttpStatus.OK);
	}

	@PutMapping("/user")
	public ResponseEntity<EditUserCategoryRs> editUserCategory(@RequestHeader(value = "Authorization") String authData,
			@RequestBody CategoryDTO category) throws GenericExceptionHandler, Exception {
		return new ResponseEntity<EditUserCategoryRs>(categoryService.editUserCategory(authData, category),
				HttpStatus.OK);
	}

	@DeleteMapping("/user/{id}")
	public ResponseEntity<DeleteUserCategoryRs> deleteCategory(@RequestHeader(value = "Authorization") String authData,
			@PathVariable long id) throws GenericExceptionHandler, Exception {
		return new ResponseEntity<DeleteUserCategoryRs>(categoryService.deleteUserCategory(authData, id),
				HttpStatus.OK);
	}

}
