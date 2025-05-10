package com.vaultpay.account;

import com.vaultpay.security.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    List<BankAccount> findAllByOwner(User owner);

    Optional<BankAccount> findByAccountNumberAndOwner(String accountNumber, User owner);
}
