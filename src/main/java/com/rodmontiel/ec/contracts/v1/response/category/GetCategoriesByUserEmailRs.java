package com.rodmontiel.ec.contracts.v1.response.category;

import java.util.Collection;

import com.rodmontiel.ec.contracts.v1.response.BaseResponse;
import com.rodmontiel.ec.dto.CategoryDTO;

public class GetCategoriesByUserEmailRs extends BaseResponse {
	
	public Collection<CategoryDTO> categories;
}