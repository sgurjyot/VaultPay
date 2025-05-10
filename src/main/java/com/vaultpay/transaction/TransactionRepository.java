package com.vaultpay.transaction;

import com.vaultpay.account.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByFromAccountOrToAccount(BankAccount from, BankAccount to);

    List<Transaction> findAllByFromAccount(BankAccount from);

    List<Transaction> findAllByToAccount(BankAccount to);
}
