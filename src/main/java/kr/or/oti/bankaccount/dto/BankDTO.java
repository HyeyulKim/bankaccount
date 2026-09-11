package kr.or.oti.bankaccount.dto;

import lombok.Data;

@Data
public class BankDTO {
	private Long bankId;
	private String bankName;
	private Long totalAccount;
}
