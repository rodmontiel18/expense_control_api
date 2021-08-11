package com.rodmontiel.ec.service;

import java.sql.Timestamp;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rodmontiel.ec.contracts.v1.response.income.AddIncomeRs;
import com.rodmontiel.ec.contracts.v1.response.income.DeleteIncomeRs;
import com.rodmontiel.ec.contracts.v1.response.income.EditIncomeRs;
import com.rodmontiel.ec.contracts.v1.response.income.GetIncomeRs;
import com.rodmontiel.ec.contracts.v1.response.income.GetIncomesRs;
import com.rodmontiel.ec.dto.IncomeDTO;
import com.rodmontiel.ec.ex.GenericExceptionHandler;
import com.rodmontiel.ec.model.Category;
import com.rodmontiel.ec.model.Income;
import com.rodmontiel.ec.model.User;
import com.rodmontiel.ec.repository.IncomeRepository;
import com.rodmontiel.ec.repository.UserRepository;
import com.rodmontiel.ec.security.JwtToken;

@Service
public class IncomeService {

	@Autowired
	private IncomeRepository incomeRep;
	@Autowired
	private JwtToken tokenTools;
	@Autowired
	private UserRepository userRepository;

	public AddIncomeRs addIncome(String authData, IncomeDTO dtoIncome) throws GenericExceptionHandler, Exception {

		AddIncomeRs rs = new AddIncomeRs();

		String userEmail = tokenTools.getUsernameFromAuthorization(authData);
		User user = userRepository.getUserByEmail(userEmail)
			.orElseThrow(() -> new GenericExceptionHandler(110));

		Income incomeToSave = mapDtoToIncome(dtoIncome);
		incomeToSave.setUser(user);

		rs.income = mapIncomeToDto(incomeRep.save(incomeToSave));
		rs.success = true;

		return rs;

	}

	public EditIncomeRs editUserIncome(String authData, IncomeDTO dtoIncome)
			throws GenericExceptionHandler, Exception {

		EditIncomeRs rs = new EditIncomeRs();

		String userEmail = tokenTools.getUsernameFromAuthorization(authData);

		Income incomeToEdit = incomeRep.getIncomeByIdAndUserEmail(dtoIncome.id, userEmail)
				.orElseThrow(() -> new GenericExceptionHandler(341));

		incomeToEdit.setAmount(dtoIncome.amount);
		incomeToEdit.setDescription(dtoIncome.description);
		incomeToEdit.setIncomeDate(dtoIncome.incomeDate);

		rs.income = mapIncomeToDto(incomeRep.save(incomeToEdit));
		rs.success = true;

		return rs;

	}

	public DeleteIncomeRs deleteIncome(long incomeId) throws Exception {

		DeleteIncomeRs rs = new DeleteIncomeRs();
		incomeRep.deleteById(incomeId);
		rs.success = true;

		return rs;

	}

	public DeleteIncomeRs deleteUserIncome(String authData, long incomeId)
			throws GenericExceptionHandler, Exception {

		DeleteIncomeRs rs = new DeleteIncomeRs();

		String userEmail = tokenTools.getUsernameFromAuthorization(authData);

		incomeRep.getIncomeByIdAndUserEmail(incomeId, userEmail)
				.orElseThrow(() -> new GenericExceptionHandler(342));
		incomeRep.deleteById(incomeId);

		rs.success = true;

		return rs;
	}

	public GetIncomesRs getAllIncomes() throws Exception {
		
		GetIncomesRs rs = new GetIncomesRs();
		
		ArrayList<Income> incomes = (ArrayList<Income>) incomeRep.findAll();
		
		rs.incomes = mapIncomesToDto(incomes);
		rs.success = true;
		
		return rs;
	}
	
	public GetIncomeRs getIncomeById(long incomeId) throws GenericExceptionHandler {

		GetIncomeRs rs = new GetIncomeRs();

		Income income = incomeRep.findById(incomeId)
				.orElseThrow(() -> new GenericExceptionHandler(343));

		rs.income = mapIncomeToDto(income);
		rs.success = true;

		return rs;

	}
	
	public GetIncomeRs getUserIncomeById(String authData, long incomeId) throws GenericExceptionHandler, Exception {
		
		GetIncomeRs rs = new GetIncomeRs();
		
		String userEmail = tokenTools.getUsernameFromAuthorization(authData);
		
		Income income = incomeRep.getIncomeByIdAndUserEmail(incomeId, userEmail)
				.orElseThrow(() -> new GenericExceptionHandler(343));
		
		rs.income = mapIncomeToDto(income);
		rs.success = true;
		
		return rs;
	}

	public GetIncomesRs getUserIncomesByRangeDate(String authData, long from, long to)
			throws GenericExceptionHandler, Exception {

		GetIncomesRs rs = new GetIncomesRs();

		String userEmail = tokenTools.getUsernameFromAuthorization(authData);
		Timestamp lFrom;
		Timestamp lTo;

		try {
			lFrom = new Timestamp(from);
			lTo = new Timestamp(to);
		} catch (Exception e) {
			throw new GenericExceptionHandler(301);
		}

		ArrayList<Income> incomes = (ArrayList<Income>) incomeRep.getUserIncomesByRangeDate(userEmail, lFrom, lTo);

		rs.incomes = mapIncomesToDto(incomes);
		rs.success = true;

		return rs;
	}

	private Income mapDtoToIncome(IncomeDTO dtoIncome) {
		Income income = new Income();

		income.setAmount(dtoIncome.amount);
		
		Category category = new Category();
		category.setId(dtoIncome.categoryId);
		
		income.setCategory(category);
		income.setDescription(dtoIncome.description);
		income.setId(dtoIncome.id);
		income.setIncomeDate(dtoIncome.incomeDate);

		return income;
	}

	private IncomeDTO mapIncomeToDto(Income income) {
		IncomeDTO dtoIncome = new IncomeDTO();

		dtoIncome.amount = income.getAmount();
		dtoIncome.categoryId = income.getCategory().getId();
		dtoIncome.description = income.getDescription();
		dtoIncome.id = income.getId();
		dtoIncome.incomeDate = income.getIncomeDate();
		dtoIncome.userId = income.getUser().getId();

		return dtoIncome;
	}

	private ArrayList<IncomeDTO> mapIncomesToDto(ArrayList<Income> incomes) {

		ArrayList<IncomeDTO> dtoIncomes = new ArrayList<IncomeDTO>();

		for (Income income : incomes) {
			dtoIncomes.add(mapIncomeToDto(income));
		}

		return dtoIncomes;

	}
}
