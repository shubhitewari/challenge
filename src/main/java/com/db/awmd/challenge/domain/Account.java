package com.db.awmd.challenge.domain;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.db.awmd.challenge.exception.NegativeAmountException;
import com.db.awmd.challenge.service.AccountsService;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class Account {

  @NotNull
  @NotEmpty
  private final String accountId;

  @NotNull
  @Min(value = 0, message = "Initial balance must be positive.")
  private BigDecimal balance;

  public Account(String accountId) {
    this.accountId = accountId;
    this.balance = BigDecimal.ZERO;
  }

  @JsonCreator
  public Account(@JsonProperty("accountId") String accountId,
    @JsonProperty("balance") BigDecimal balance) {
    this.accountId = accountId;
    this.balance = balance;
  }
  
  public void withdraw(BigDecimal amount) {
	  log.info(Thread.currentThread().getName() + " Withdrawing from : "+this.accountId+" amount : "+amount);
	  
	  if(amount.compareTo(this.balance)>0)throw new NegativeAmountException("Account balance cannot be negative");
      this.balance = this.balance.subtract(amount) ;
      
      log.info(Thread.currentThread().getName() + " balance in : "+this.accountId+" balance : "+balance);
  }

  public void deposit(BigDecimal amount) {
	  log.info(Thread.currentThread().getName() + " depositing into : "+this.accountId+" amount : "+amount);
	  
      this.balance = this.balance.add(amount);
      
      log.info(Thread.currentThread().getName() + " balance in : "+this.accountId+" balance : "+balance);
  }
  
   
}
