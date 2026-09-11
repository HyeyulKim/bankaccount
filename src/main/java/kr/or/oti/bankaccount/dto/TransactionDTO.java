package kr.or.oti.bankaccount.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TransactionDTO {
	private Long transactionId;
	private LocalDateTime transactionDatetime;
	private String kind;
	private Long amount;
	private Long balance;
	private String accountNo;
}
