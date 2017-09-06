package com.example.demo.service;

import com.example.demo.entity.User;

public interface UserService {
	public User findUserByEmail(String email);
	public void saveUser(User user);

}
