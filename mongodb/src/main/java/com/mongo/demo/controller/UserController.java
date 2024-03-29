package com.mongo.demo.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.mongo.demo.dal.UserDAL;
import com.mongo.demo.dal.UserRepository;
import com.mongo.demo.model.User;

@RestController
@RequestMapping(value = "/")
public class UserController {

	private final Logger LOG = LoggerFactory.getLogger(getClass());

	private final UserRepository userRepository;
	// define Data Access Layer object
	private final UserDAL userDAL;

	// initialize DAL object via constructor autowiring
	public UserController(UserRepository userRepository, UserDAL userDAL) {
		this.userRepository = userRepository;
		this.userDAL = userDAL;
	}

	@GetMapping("")
	public List<User> getAllUsers() {
		LOG.info("Getting all users.");
		return userRepository.findAll();
	}

	@GetMapping("/{userId}")
	public Optional<User> getUser(@PathVariable String userId) {
		LOG.info("Getting user with ID: {}.", userId);
		return userRepository.findById(userId);
	}

	@PostMapping("/create")
	public User addNewUsers(@RequestBody User user) {
		LOG.info("Saving user.");
		return userRepository.save(user);
	}

	// change method implementation to use DAL and hence MongoTemplate
	@GetMapping("/settings/{userId}")
	public Object getAllUserSettings(@PathVariable String userId) {
		Optional<User> user = userRepository.findById(userId);
		if (user.isPresent()) {
			return userDAL.getAllUserSettings(userId);
		} else {
			return "User not found.";
		}
	}

	// change method implementation to use DAL and hence MongoTemplate
	@GetMapping("/settings/{userId}/{key}")
	public String getUserSetting(@PathVariable String userId, @PathVariable String key) {
		return userDAL.getUserSetting(userId, key);
	}

	@GetMapping("/settings/{userId}/{key}/{value}")
	public String addUserSetting(@PathVariable String userId, @PathVariable String key, @PathVariable String value) {
		Optional<User> user = userRepository.findById(userId);
		if (user.isPresent()) {
			userDAL.addUserSetting(userId, key, value);
			return "Key added";
		} else {
			return "User not found.";
		}
	}
}