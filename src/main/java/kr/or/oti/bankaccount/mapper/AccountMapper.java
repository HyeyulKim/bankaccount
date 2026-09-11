package kr.or.oti.bankaccount.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import kr.or.oti.bankaccount.dto.AccountDTO;

@Mapper
public interface AccountMapper {
	int insert(AccountDTO account);

	AccountDTO findByAccountNo(String accountNo);

	List<AccountDTO> findAll();

	List<AccountDTO> findByName(String name);

	int update(AccountDTO account);

	int delete(String accountNo);

	int deposit(@Param("accountNo") String accountNo, @Param("amount") Long amount);

	int withdraw(@Param("accountNo") String accountNo, @Param("amount") Long amount);
}
