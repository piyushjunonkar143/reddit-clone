package com.reddit;

import com.reddit.controller.UserController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class RedditProjectApplication {

	public static void main(String[] args) {
		new File(UserController.uploadDirectory).mkdir();
		SpringApplication.run(RedditProjectApplication.class, args);
	}

}
