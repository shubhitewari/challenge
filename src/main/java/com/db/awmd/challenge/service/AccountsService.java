package com.db.awmd.challenge.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.repository.AccountsRepository;
import com.db.awmd.challenge.web.AccountsController;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AccountsService {

  @Getter
  private final AccountsRepository accountsRepository;
  
  @Getter
  private final NotificationService notificationService;
  
  @Autowired
  public AccountsService(AccountsRepository accountsRepository, NotificationService notificationService) {
    this.accountsRepository = accountsRepository;
    this.notificationService = notificationService;
  }

  public void createAccount(Account account) {
    this.accountsRepository.createAccount(account);
  }

  public Account getAccount(String accountId) {
    return this.accountsRepository.getAccount(accountId);
  }
    
public void transferFunds(String accountFrom, String accountTo, String amount) {
	
	log.info(Thread.currentThread().getName() + " Transferring funds from : " + accountFrom + " to : " + accountTo + " amount : " + amount);  

	Account fromAccount = getAccount(accountFrom);
	Account toAccount = getAccount(accountTo);

	Object firstLock, secondLock;
	if (fromAccount.getAccountId().compareTo(toAccount.getAccountId()) < 0) {
		firstLock = fromAccount; secondLock = toAccount;
	} else {
		firstLock = toAccount; secondLock = fromAccount;
	}

	synchronized (firstLock) {
		synchronized (secondLock) {
			fromAccount.withdraw(new BigDecimal(amount));
			toAccount.deposit(new BigDecimal(amount));
			this.notificationService.notifyAboutTransfer(fromAccount, " Amount " + amount + " has been transferred to Account Id " +toAccount.getAccountId());
			this.notificationService.notifyAboutTransfer(toAccount, " Amount " + amount + " has been transferred from Account Id " +fromAccount.getAccountId());

		}
	}
	log.info(Thread.currentThread().getName() + "  Fund Transfer successful from " + accountFrom + " to " + accountTo + " amount " + amount);			
	log.info(Thread.currentThread().getName() + "Notification sent to account holders");
}

}
