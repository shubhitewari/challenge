package com.db.awmd.challenge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.service.AccountsService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountsServiceTest {

	@Autowired
	private AccountsService accountsService;

	@Test
	public void addAccount() throws Exception {
		Account account = new Account("Id-123");
		account.setBalance(new BigDecimal(1000));
		this.accountsService.createAccount(account);

		assertThat(this.accountsService.getAccount("Id-123")).isEqualTo(account);
	}

	@Test
	public void addAccount_failsOnDuplicateId() throws Exception {
		String uniqueId = "Id-" + System.currentTimeMillis();
		Account account = new Account(uniqueId);
		this.accountsService.createAccount(account);

		try {
			this.accountsService.createAccount(account);
			fail("Should have failed when adding duplicate account");
		} catch (DuplicateAccountIdException ex) {
			assertThat(ex.getMessage()).isEqualTo("Account id " + uniqueId + " already exists!");
		}

	}

	@Test
	public void transferFundsConcurrently() throws Exception {
		Account account1 = new Account("Id-1");
		account1.setBalance(new BigDecimal(1000));

		Account account2 = new Account("Id-2");
		account2.setBalance(new BigDecimal(1000));

		this.accountsService.createAccount(account1);
		this.accountsService.createAccount(account2);

		Thread t1 =  new Thread(new Runnable() {

			@Override
			public void run() {
				accountsService.transferFunds(account1.getAccountId(), account2.getAccountId(), "100");

			}
		});
		
		Thread t2 =  new Thread(new Runnable() {

			@Override
			public void run() {
				accountsService.transferFunds(account1.getAccountId(), account2.getAccountId(), "300");

			}
		});
		
		Thread t3 =  new Thread(new Runnable() {

			@Override
			public void run() {
				accountsService.transferFunds(account2.getAccountId(), account1.getAccountId(), "500");

			}
		});
		
		Thread b1 =  new Thread(new Runnable() {

			@Override
			public void run() {
				System.out.println("Account Info : " +accountsService.getAccount("Id-1"));

			}
		});

		Thread b2 =  new Thread(new Runnable() {

			@Override
			public void run() {
				System.out.println("Account Info : " +accountsService.getAccount("Id-2"));

			}
		});
		
		t1.start();
		b1.start();
		t2.start();
		t3.start();
		b2.start();	
		
	}
}
