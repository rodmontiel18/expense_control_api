package com.rodmontiel.ec.contracts.v1.response.income;

import java.util.Collection;

import com.rodmontiel.ec.contracts.v1.response.BaseResponse;
import com.rodmontiel.ec.dto.IncomeDTO;

public class GetIncomesRs extends BaseResponse {
	public Collection<IncomeDTO> incomes;
}
