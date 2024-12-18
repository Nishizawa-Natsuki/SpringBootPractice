package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Admin;
import com.example.demo.form.AdminForm;
import com.example.demo.repository.AdminRepository;
import com.example.demo.repository.ContactRepository;
import com.example.demo.entity.Contact;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {
	
	@Autowired
	private AdminRepository adminRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private ContactRepository contactRepository;
	
	public void saveAdmin(AdminForm adminForm) {
		
		// Adminエンティティを作成、フォームデータを設定
		Admin admin = new Admin();
		admin.setLastName(adminForm.getLastName());
		admin.setFirstName(adminForm.getFirstName());
		admin.setEmail(adminForm.getEmail());
		
		// パスワードをハッシュ化して保存
		admin.setPassword(passwordEncoder.encode(adminForm.getPassword()));
		
		// データベースに保存
		adminRepository.save(admin);
	}
	
	
	public List<Contact> getAllContacts() {
		
		return contactRepository.findAll();
	}
	
	public Contact getContactById(Long id) {
		Optional<Contact> contact = contactRepository.findById(id);
		return contact.orElse(null);
	}
	
	public void updateContact(Long id, Contact contact) {
		Contact existingContact = contactRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid contact Id: " + id));
		existingContact.setLastName(contact.getLastName());
		existingContact.setFirstName(contact.getFirstName());
		existingContact.setEmail(contact.getEmail());
		existingContact.setPhone(contact.getPhone());
		existingContact.setZipCode(contact.getZipCode());
		existingContact.setAddress(contact.getAddress());
		existingContact.setBuildingName(contact.getBuildingName());
		existingContact.setContactType(contact.getContactType());
		existingContact.setBody(contact.getBody());
		contactRepository.save(existingContact); // 更新を保存
	}

	public void deleteContact(Long id) {
		contactRepository.deleteById(id); // お問い合わせを削除
	}
}
