package com.gooood.GooooCoin;

//import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@ServletComponentScan(basePackages = "com.gooood.GooooCoin.conf.*")
public class GooooCoinApplication {

	public static void main(String[] args) {
		SpringApplication.run(GooooCoinApplication.class, args);
	}

}
