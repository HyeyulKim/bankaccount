package kr.or.oti.bankaccount.controller;

import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.server.ResponseStatusException;
import kr.or.oti.bankaccount.dto.AccountDTO;
import kr.or.oti.bankaccount.dto.AmountRequestDTO;
import kr.or.oti.bankaccount.dto.TransactionDTO;
import kr.or.oti.bankaccount.service.AccountService;
import kr.or.oti.bankaccount.service.BankService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final BankService bankService;

    @GetMapping
    public String list(Model model) {
        List<AccountDTO> accounts = accountService.getAll(null);
        model.addAttribute("accounts", accounts);
        model.addAttribute("totalCount", accounts.size());
        return "index";
    }

    @GetMapping("/new")
    public String registerForm(Model model) {
        model.addAttribute("account", new AccountDTO());
        model.addAttribute("banks", bankService.getAll());
        return "account_register";
    }

    @PostMapping
    public String register(@ModelAttribute AccountDTO account, RedirectAttributes redirectAttributes, Model model) {
        if (account.getAccountNo() == null || account.getAccountNo().isBlank()
                || account.getName() == null || account.getName().isBlank()) {
            model.addAttribute("banks", bankService.getAll());
            model.addAttribute("error", "계좌번호와 소유자명을 모두 입력해 주세요.");
            return "account_register";
        }
        try {
            accountService.create(account);
            redirectAttributes.addFlashAttribute("message", "계좌가 등록되었습니다.");
            return "redirect:/accounts";
        } catch (ResponseStatusException e) {
            model.addAttribute("banks", bankService.getAll());
            model.addAttribute("error", e.getReason());
            return "account_register";
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("banks", bankService.getAll());
            model.addAttribute("error", "이미 등록된 계좌번호입니다.");
            return "account_register";
        }
    }

    @GetMapping("/search")
    public String search(@RequestParam(value = "accountNo", required = false) String accountNo,
                         @RequestParam(value = "name", required = false) String name, Model model) {
        List<AccountDTO> results;
        if (accountNo != null && !accountNo.isBlank()) {
            try { results = List.of(accountService.get(accountNo)); }
            catch (ResponseStatusException e) { results = List.of(); }
        } else if (name != null && !name.isBlank()) {
            results = accountService.getAll(name);
        } else {
            results = List.of();
        }
        model.addAttribute("results", results);
        model.addAttribute("accountNo", accountNo);
        model.addAttribute("name", name);
        return "search";
    }

    @GetMapping("/{accountNo}")
    public String detail(@PathVariable("accountNo") String accountNo, Model model) {
        model.addAttribute("account", accountService.get(accountNo));
        return "account_detail";
    }

    @PostMapping("/{accountNo}/deposit")
    public String deposit(@PathVariable("accountNo") String accountNo, @ModelAttribute AmountRequestDTO request, RedirectAttributes redirectAttributes) {
        accountService.deposit(accountNo, request.getAmount());
        redirectAttributes.addFlashAttribute("message", "입금이 완료되었습니다.");
        return "redirect:/accounts/" + accountNo;
    }

    @PostMapping("/{accountNo}/withdraw")
    public String withdraw(@PathVariable("accountNo") String accountNo, @ModelAttribute AmountRequestDTO request, RedirectAttributes redirectAttributes) {
        accountService.withdraw(accountNo, request.getAmount());
        redirectAttributes.addFlashAttribute("message", "출금이 완료되었습니다.");
        return "redirect:/accounts/" + accountNo;
    }

    @GetMapping("/{accountNo}/transactions")
    public String transactions(@PathVariable("accountNo") String accountNo, Model model) {
        AccountDTO account = accountService.get(accountNo);
        List<TransactionDTO> transactions = accountService.getTransactions(accountNo);
        model.addAttribute("account", account);
        model.addAttribute("transactions", transactions);
        return "transaction_history";
    }
}
