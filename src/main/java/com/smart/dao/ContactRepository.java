package com.smart.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.entities.Contact;
import com.smart.entities.User;


public interface ContactRepository extends JpaRepository<Contact, Integer>{
	@Query("from Contact where user.id=:id")
	public Page<Contact> findAllBYUserId(@Param("id")int id,Pageable pageable);
	public List<Contact> findByNameContainingAndUser(String pattern,User user);
	public List<Contact> findBySecondNameContainingAndUser(String pattern,User user);
	@Query("from Contact where user.id=:id")
	public List<Contact> findByUser(@Param("id") int userId);
}
