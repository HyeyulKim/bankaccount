package kr.or.oti.bankaccount.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import kr.or.oti.bankaccount.dto.TransactionDTO;

@Mapper
public interface TransactionMapper {
	int insert(TransactionDTO transaction);

	List<TransactionDTO> findByAccountNo(String accountNo);
}
