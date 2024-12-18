package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.form.ContactForm;
import com.example.demo.service.ContactService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
	
@Controller
public class ContactController {
	
	@Autowired // ContactServiceを自動的に注入
	private ContactService contactService;
	
	@GetMapping("/contact") // お問い合わせフォームを表示する
	public String contact(Model model) {
		model.addAttribute("contactForm", new ContactForm()); // 新しい ContactForm オブジェクトをモデルに追加し、ビューに渡す
		
		return "contact"; // contact.html を表示
	}
	
	// POSTリクエスト /contact に対応するメソッド  フォーム送信時に実行
	@PostMapping("/contact")
	public String contact(@Validated @ModelAttribute("contactForm") ContactForm contactForm, BindingResult errorResult, HttpServletRequest request) {
		
		if (errorResult.hasErrors()) { // フォームにエラーがあれば、再度 contact.html を表示する
			return "contact";
		}
		
		HttpSession session = request.getSession(); // フォームデータをセッションに保存
		session.setAttribute("contactForm", contactForm);
		
		return "redirect:/contact/confirm"; // 確認画面へリダイレクト
	}
	
	// GETリクエスト /contact/confirm に対応するメソッド
	// 確認画面を表示する
	@GetMapping("/contact/confirm")
	public String confirm(Model model, HttpServletRequest request) {
		// セッションからフォームデータを取得
		HttpSession session = request.getSession();
		ContactForm contactForm = (ContactForm) session.getAttribute("contactForm");
		model.addAttribute("contactForm", contactForm);
		
		return "confirmation"; // confirmation.html を表示
	}
	
	// POSTリクエスト /contact/register に対応するメソッド
	// フォームデータをデータベースに保存
	@PostMapping("/contact/register")
	public String register(Model model, HttpServletRequest request) {
		// セッションからフォームデータを取得
		HttpSession session = request.getSession();
		ContactForm contactForm = (ContactForm) session.getAttribute("contactForm");
		// ContactService を使ってデータベースに保存
		contactService.saveContact(contactForm);
		
		// 完了画面へリダイレクト
		return "redirect:/contact/complete";
	}
	
	// GETリクエスト /contact/complete に対応するメソッド
	// 完了画面を表示
	@GetMapping("/contact/complete")
	public String complete(Model model, HttpServletRequest request) {
		
		// セッションが存在しない場合お問い合わせフォームにリダイレクト
		if (request.getSession(false) == null) {
			
			return "redirect:/contact";
		}
		
		// セッションからフォームデータを取得し、モデルに追加
		HttpSession session = request.getSession();
		ContactForm contactForm = (ContactForm) session.getAttribute("contactForm");
		model.addAttribute("contactForm", contactForm);
		
		// セッションを無効化して終了
		session.invalidate();
		
		return "completion"; // completion.html を表示
	}
}