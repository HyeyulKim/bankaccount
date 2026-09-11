package kr.or.oti.bankaccount.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import kr.or.oti.bankaccount.dto.AccountDTO;
import kr.or.oti.bankaccount.dto.TransactionDTO;
import kr.or.oti.bankaccount.mapper.AccountMapper;
import kr.or.oti.bankaccount.mapper.BankMapper;
import kr.or.oti.bankaccount.mapper.TransactionMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {
	private final AccountMapper accountMapper;
	private final BankMapper bankMapper;
	private final TransactionMapper transactionMapper;

	@Transactional
	public AccountDTO create(AccountDTO account) {
		if (account.getBankId() == null || bankMapper.findById(account.getBankId()) == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valid bankId is required.");
		account.setBalance(0L);
		accountMapper.insert(account);
		bankMapper.increaseTotalAccount(account.getBankId());
		return get(account.getAccountNo());
	}

	public AccountDTO get(String accountNo) {
		AccountDTO account = accountMapper.findByAccountNo(accountNo);
		if (account == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found: " + accountNo);
		return account;
	}

	public List<AccountDTO> getAll(String name) {
		return name == null || name.isBlank() ? accountMapper.findAll() : accountMapper.findByName(name);
	}

	public AccountDTO update(String accountNo, AccountDTO account) {
		AccountDTO old = get(accountNo);
		account.setAccountNo(accountNo);
		account.setBalance(old.getBalance());
		account.setBankId(old.getBankId());
		accountMapper.update(account);
		return get(accountNo);
	}

	@Transactional
	public void delete(String accountNo) {
		AccountDTO account = get(accountNo);
		accountMapper.delete(accountNo);
		bankMapper.decreaseTotalAccount(account.getBankId());
	}

	@Transactional
	public AccountDTO deposit(String accountNo, Long amount) {
		validateAmount(amount);
		get(accountNo);
		accountMapper.deposit(accountNo, amount);
		return record(accountNo, "입금", amount);
	}

	@Transactional
	public AccountDTO withdraw(String accountNo, Long amount) {
		validateAmount(amount);
		get(accountNo);
		if (accountMapper.withdraw(accountNo, amount) == 0)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient balance.");
		return record(accountNo, "출금", amount);
	}

	public List<TransactionDTO> getTransactions(String accountNo) {
		get(accountNo);
		return transactionMapper.findByAccountNo(accountNo);
	}

	private AccountDTO record(String accountNo, String kind, Long amount) {
		AccountDTO account = get(accountNo);
		TransactionDTO tx = new TransactionDTO();
		tx.setTransactionDatetime(LocalDateTime.now());
		tx.setKind(kind);
		tx.setAmount(amount);
		tx.setBalance(account.getBalance());
		tx.setAccountNo(accountNo);
		transactionMapper.insert(tx);
		return account;
	}

	private void validateAmount(Long amount) {
		if (amount == null || amount <= 0)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount must be greater than zero.");
	}
}
