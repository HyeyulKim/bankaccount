package kr.or.oti.bankaccount.service;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import kr.or.oti.bankaccount.dto.BankDTO;
import kr.or.oti.bankaccount.mapper.BankMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BankService {
	private final BankMapper bankMapper;

	public BankDTO create(BankDTO bank) {
		bankMapper.insert(bank);
		return get(bank.getBankId());
	}

	public BankDTO get(Long bankId) {
		BankDTO bank = bankMapper.findById(bankId);
		if (bank == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bank not found: " + bankId);
		return bank;
	}

	public List<BankDTO> getAll() {
		return bankMapper.findAll();
	}

	public BankDTO update(Long bankId, BankDTO bank) {
		get(bankId);
		bank.setBankId(bankId);
		bankMapper.update(bank);
		return get(bankId);
	}

	public void delete(Long bankId) {
		get(bankId);
		bankMapper.delete(bankId);
	}
}
