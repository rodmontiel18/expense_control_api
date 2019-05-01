package com.as.ec.controller;

import java.util.List;

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

import com.as.ec.model.Category;
import com.as.ec.model.dal.CategoryService;
import com.as.ec.model.dal.UserService;
import com.as.ec.model.ex.CategoryNotFoundException;
import com.as.ec.model.ex.RepeatedValueEx;

@RestController
@RequestMapping("/categories")
public class CategoryController {
	
	private CategoryService service;
	private UserService uService;
	
	@Autowired
	public CategoryController(CategoryService catService, UserService userService) {
		this.service = catService;
		this.uService = userService;
	}
	
	@GetMapping("{id}")
	public ResponseEntity<Category> getCategoryById(@PathVariable long id){
		Category category = service.findById(id).orElseThrow(() -> new CategoryNotFoundException("No se encontro la categoria con el id: " + id));
		return new ResponseEntity<Category>(category, HttpStatus.OK);
	}
	
	@GetMapping("user/{userId}")
	public ResponseEntity<List<Category>> getCategoriesByUser(@PathVariable long userId) {
		try {
			List<Category> categories = service.getCategoriesByUser(userId);
			return new ResponseEntity<List<Category>>(categories, HttpStatus.OK);

		} catch(Exception e) {
			return new ResponseEntity<List<Category>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping
	public ResponseEntity<Category> addUserCategory(@RequestBody Category category) {
		
		Category lCategory = service.searchCategoriesByUserAndName(category.getUser().getId(), category.getName());
		if(lCategory != null)
			throw new RepeatedValueEx("La categoria que quieres agregar ya existe");
		
		lCategory = service.save(category);
		return new ResponseEntity<Category>(lCategory, HttpStatus.OK);
		
	}

	@PutMapping("{id}")
	public ResponseEntity<Category> editCategory(@RequestBody Category category, @PathVariable long id){

		
		Category sCategory = service.searchCategoriesByUserAndName(category.getUser().getId(), category.getName());
		if(sCategory != null)
			throw new RepeatedValueEx("El nombre que quieres usar ya existe en otra categoria");
		
		Category updatedCategory = service.findById(id)
					.map(lCategory ->  {
						lCategory.setDescription(category.getDescription());
						lCategory.setId(category.getId());
						lCategory.setName(category.getName());
						lCategory.setUser(
								uService.findById(category.getUser().getId())
									.orElseThrow(() -> new CategoryNotFoundException("No se encontro el usuario asociado a la categoria"))
								);
						return service.save(lCategory);
					})
					.orElseGet(() -> {
						category.setId(id);
						return service.save(category);
					});
		return new ResponseEntity<Category>(updatedCategory, HttpStatus.OK);
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<?> deleteCategory(@PathVariable long id){
		service.deleteById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
