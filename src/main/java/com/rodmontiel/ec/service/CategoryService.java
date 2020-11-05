package com.rodmontiel.ec.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rodmontiel.ec.contracts.v1.response.category.AddUserCategoryRs;
import com.rodmontiel.ec.contracts.v1.response.category.DeleteUserCategoryRs;
import com.rodmontiel.ec.contracts.v1.response.category.EditUserCategoryRs;
import com.rodmontiel.ec.contracts.v1.response.category.GetAllCategoriesRs;
import com.rodmontiel.ec.contracts.v1.response.category.GetCategoriesByUserEmailRs;
import com.rodmontiel.ec.contracts.v1.response.category.GetCategoryByIdRs;
import com.rodmontiel.ec.dto.CategoryDTO;
import com.rodmontiel.ec.ex.GenericExceptionHandler;
import com.rodmontiel.ec.model.Category;
import com.rodmontiel.ec.model.Expense;
import com.rodmontiel.ec.model.Income;
import com.rodmontiel.ec.model.User;
import com.rodmontiel.ec.repository.CategoryRepository;
import com.rodmontiel.ec.repository.ExpenseRepository;
import com.rodmontiel.ec.repository.IncomeRepository;
import com.rodmontiel.ec.repository.UserRepository;
import com.rodmontiel.ec.security.JwtToken;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRep;
	@Autowired
	private ExpenseRepository expenseRep;
	@Autowired
	private IncomeRepository incomeRep;
	@Autowired
	private JwtToken tokenTools;
	@Autowired
	private UserRepository userRepository;

	public AddUserCategoryRs addCategory(String authData, CategoryDTO categoryDto)
			throws GenericExceptionHandler, Exception {

		AddUserCategoryRs rs = new AddUserCategoryRs();
		String userEmail = tokenTools.getUsernameFromAuthorization(authData);
		User user = userRepository.getUserByEmail(userEmail)
				.orElseThrow(() -> new GenericExceptionHandler(130));

		ArrayList<Category> categories = (ArrayList<Category>) categoryRep.getCategoryByNameAndUser(categoryDto.name,
				userEmail);
		if (!categories.isEmpty())
			throw new GenericExceptionHandler(281);

		Category category = mapDtoToCategory(categoryDto);
		category.setUser(user);

		Category savedCategory = categoryRep.save(category);

		rs.category = mapCategoryToDto(savedCategory);
		rs.success = true;
		return rs;

	}

	public DeleteUserCategoryRs deleteUserCategory(String authData, long categoryId)
			throws GenericExceptionHandler, Exception {

		DeleteUserCategoryRs rs = new DeleteUserCategoryRs();
		String userEmail = tokenTools.getUsernameFromAuthorization(authData);

		Category originalCat = categoryRep.getCategoryByUserEmailAndCategoryId(categoryId, userEmail)
				.orElseThrow(() -> new GenericExceptionHandler(282));

		if (originalCat.getType() == 1) {
			ArrayList<Expense> expenses = (ArrayList<Expense>) expenseRep.getUserExpensesByCategory(originalCat.getId(),
					userEmail);
			if (expenses != null && !expenses.isEmpty()) {
				throw new GenericExceptionHandler(283);
			}
		} else {
			ArrayList<Income> incomes = (ArrayList<Income>) incomeRep.getUserIncomesByCategory(originalCat.getId(),
					userEmail);
			if (incomes != null && !incomes.isEmpty()) {
				throw new GenericExceptionHandler(284);
			}
		}

		categoryRep.deleteById(categoryId);
		rs.success = true;

		return rs;

	}

	public EditUserCategoryRs editUserCategory(String authData, CategoryDTO categoryDto)
			throws GenericExceptionHandler, Exception {

		EditUserCategoryRs rs = new EditUserCategoryRs();

		String userEmail = tokenTools.getUsernameFromAuthorization(authData);
		User user = userRepository.getUserByEmail(userEmail)
				.orElseThrow(() -> new GenericExceptionHandler(130));

		Category originalCat = categoryRep.getCategoryByUserEmailAndCategoryId(categoryDto.id, userEmail)
				.orElseThrow(() -> new GenericExceptionHandler(285));

		ArrayList<Category> categories = (ArrayList<Category>) categoryRep.getCategoryByNameAndUser(categoryDto.name,
				userEmail);

		if (!categories.isEmpty() && categories.get(0).getId() != originalCat.getId())
			throw new GenericExceptionHandler(286);

		Category updatedCat = mapDtoToCategory(categoryDto);
		updatedCat.setId(originalCat.getId());
		updatedCat.setUser(user);
		updatedCat = categoryRep.save(updatedCat);

		rs.category = mapCategoryToDto(updatedCat);
		rs.success = true;

		return rs;

	}

	public GetAllCategoriesRs getAllCategories(String authData) throws Exception {

		GetAllCategoriesRs rs = new GetAllCategoriesRs();

		ArrayList<Category> categories = (ArrayList<Category>) categoryRep.findAll();
		rs.categories = mapCategoriesToDto(categories);
		rs.success = true;
		return rs;

	}

	public GetCategoriesByUserEmailRs getCategoriesByTypeAndUserEmail(String authData, int categoryType)
			throws GenericExceptionHandler, Exception {

		GetCategoriesByUserEmailRs rs = new GetAllCategoriesRs();

		String userEmail = tokenTools.getUsernameFromAuthorization(authData);

		ArrayList<Category> categories = (ArrayList<Category>) categoryRep.getCategoriesByTypeAndUserEmail(categoryType,
				userEmail);

		rs.categories = mapCategoriesToDto(categories);
		rs.success = true;

		return rs;
	}

	public GetCategoriesByUserEmailRs getCategoriesByUserEmail(String authData)
			throws GenericExceptionHandler, Exception {

		GetCategoriesByUserEmailRs rs = new GetCategoriesByUserEmailRs();

		String userEmail = tokenTools.getUsernameFromAuthorization(authData);

		ArrayList<Category> categories = (ArrayList<Category>) categoryRep.getCategoriesByUserEmail(userEmail);

		rs.categories = mapCategoriesToDto(categories);
		rs.success = true;

		return rs;

	}

	public GetCategoryByIdRs getCategoryById(long id) throws GenericExceptionHandler, Exception {

		GetCategoryByIdRs rs = new GetCategoryByIdRs();

		Category cat = categoryRep.findById(id)
			.orElseThrow(() -> new GenericExceptionHandler(287));

		rs.category = mapCategoryToDto(cat);
		rs.success = true;

		return rs;

	}

	public GetCategoryByIdRs getUserCategoryById(String authData, long categoryId)
			throws GenericExceptionHandler, Exception {
		GetCategoryByIdRs rs = new GetCategoryByIdRs();

		String userEmail = tokenTools.getUsernameFromAuthorization(authData);

		Category category = categoryRep.getCategoryByUserEmailAndCategoryId(categoryId, userEmail)
				.orElseThrow(() -> new GenericExceptionHandler(287));

		rs.category = mapCategoryToDto(category);
		rs.success = true;

		return rs;
	}

	private ArrayList<CategoryDTO> mapCategoriesToDto(ArrayList<Category> categories) {
		ArrayList<CategoryDTO> dtoCats = new ArrayList<CategoryDTO>();
		for (Category category : categories) {
			dtoCats.add(mapCategoryToDto(category));
		}
		return dtoCats;
	}

	private CategoryDTO mapCategoryToDto(Category category) {
		CategoryDTO catDto = new CategoryDTO();
		catDto.color = category.getColor();
		catDto.description = category.getDescription();
		catDto.id = category.getId();
		catDto.name = category.getName();
		catDto.type = category.getType();
		return catDto;
	}

	private Category mapDtoToCategory(CategoryDTO dtoCat) {
		Category category = new Category();
		category.setColor(dtoCat.color);
		category.setDescription(dtoCat.description);
		category.setId(dtoCat.id);
		category.setName(dtoCat.name);
		category.setType(dtoCat.type);
		return category;
	}
}
