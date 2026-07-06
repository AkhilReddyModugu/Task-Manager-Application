package com.amodugu.taskmanager.repository;

import com.amodugu.taskmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}