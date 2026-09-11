package kr.or.oti.bankaccount.dto;

import lombok.Data;

@Data
public class AccountDTO {
    private String accountNo;
    private String name;
    private Long balance;
    private Long bankId;
}
