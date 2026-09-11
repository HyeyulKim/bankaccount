package kr.or.oti.bankaccount.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import kr.or.oti.bankaccount.dto.BankDTO;

@Mapper
public interface BankMapper {
	int insert(BankDTO bank);

	BankDTO findById(Long bankId);

	List<BankDTO> findAll();

	int update(BankDTO bank);

	int delete(Long bankId);

	int increaseTotalAccount(Long bankId);

	int decreaseTotalAccount(Long bankId);
}
