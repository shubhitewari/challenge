package com.db.awmd.challenge;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.service.NotificationService;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EmailNotificationServiceTest {
	@Mock
	NotificationService serviceMock;
	@Test
	public void testFindTheGreatestFromAllData() {
		Account account = new Account("1", new BigDecimal("123.45"));
		doNothing().when(serviceMock).notifyAboutTransfer(account,"");	
		
	}	
}
