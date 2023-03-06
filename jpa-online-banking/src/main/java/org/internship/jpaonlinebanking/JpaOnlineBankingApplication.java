package org.internship.jpaonlinebanking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class JpaOnlineBankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpaOnlineBankingApplication.class, args);
	}

}
