package com.rodmontiel.ec.contracts.v1.response.expense;

import com.rodmontiel.ec.contracts.v1.response.BaseResponse;
import com.rodmontiel.ec.dto.ExpenseDTO;

public class GetExpenseByIdRs extends BaseResponse {
	public ExpenseDTO expense;
}
