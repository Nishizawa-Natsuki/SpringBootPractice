package com.example.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.entity.Contact;
import com.example.demo.form.AdminForm;
import com.example.demo.service.AdminService;

import java.util.List;


@Controller
public class AdminController {
	
	@Autowired
	private AdminService adminService;
	
	// ログイン画面を表示
	@GetMapping("/admin/signin")
	public String showSignInForm(Model model) {
		model.addAttribute("adminForm", new AdminForm());
		return "signin"; // signin.html を表示
	}
	
	// 新規登録ページを表示
	@GetMapping("/admin/signup")
	public String showSignUpForm(Model model) {
		
		// 新しい AdminForm オブジェクトをモデルに追加し、ビューに渡す
		model.addAttribute("adminForm", new AdminForm());
		return "signup"; // signup.html を表示
	}
	
	// 管理者の新規登録処理
	@PostMapping("/admin/signup")
	public String registerAdmin(@Validated @ModelAttribute("adminForm") AdminForm adminForm, BindingResult result) {
		if (result.hasErrors()) {
			return "signup"; // エラーがあった場合再度フォームを表示
		}
		adminService.saveAdmin(adminForm); // 管理者情報を保存
		return "redirect:/admin/signin"; // 登録後にログインページへリダイレクト
	}
	
	// お問い合わせ一覧ページ
	@GetMapping("/admin/contacts")
	public String showContactList(Model model) {
		List<Contact> contacts = adminService.getAllContacts();
		model.addAttribute("contacts", contacts);
		return "contacts"; // contacts.html にリダイレクト
	}
	// お問い合わせ詳細ページ
	@GetMapping("/admin/contacts/{id}")
	public String showContactDetail(@PathVariable("id") Long id, Model model) {
		Contact contact = adminService.getContactById(id);
		// モデルに追加
		model.addAttribute("contact", contact);
		
		return "contactDetail"; // contactDetail.html表示
	}
	// 編集ページ
	
	@GetMapping("/admin/contacts/{id}/edit")
	public String showEditForm(@PathVariable("id") Long id, Model model) {
		// 編集対象の問い合わせデータを取得
		Contact contact = adminService.getContactById(id);
		model.addAttribute("contact", contact);
		return "contactEdit"; // contactEdit.htmlを表示
	}
	// 編集処理（POSTメソッド）
	@PostMapping("/admin/contacts/{id}/edit")
	public String updateContact(@PathVariable("id") Long id, @ModelAttribute("contact") Contact contact) {
		adminService.updateContact(id, contact); // 更新処理
		return "redirect:/admin/contacts"; // 更新後、お問い合わせ一覧ページにリダイレクト
		}
	// 削除処理（POSTメソッド）
	@PostMapping("/admin/contacts/{id}/delete")
	public String deleteContact(@PathVariable("id") Long id) {
		adminService.deleteContact(id); // 削除処理
		return "redirect:/admin/contacts"; // 削除後、お問い合わせ一覧ページにリダイレクト
	}
}
