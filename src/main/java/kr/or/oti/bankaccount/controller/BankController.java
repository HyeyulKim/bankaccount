package kr.or.oti.bankaccount.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import kr.or.oti.bankaccount.dto.BankDTO;
import kr.or.oti.bankaccount.service.BankService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/banks")
@RequiredArgsConstructor
public class BankController {
	private final BankService bankService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public BankDTO create(@RequestBody BankDTO bank) {
		return bankService.create(bank);
	}

	@GetMapping
	public List<BankDTO> getAll() {
		return bankService.getAll();
	}

	@GetMapping("/{bankId}")
	public BankDTO get(@PathVariable("bankId") Long bankId) {
		return bankService.get(bankId);
	}

	@PutMapping("/{bankId}")
	public BankDTO update(@PathVariable("bankId") Long bankId, @RequestBody BankDTO bank) {
		return bankService.update(bankId, bank);
	}

	@DeleteMapping("/{bankId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("bankId") Long bankId) {
		bankService.delete(bankId);
	}
}
