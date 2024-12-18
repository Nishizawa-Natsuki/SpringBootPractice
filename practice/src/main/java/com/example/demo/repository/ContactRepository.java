package com.example.demo.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long> {
	Optional<Contact> findById(Long id);
}