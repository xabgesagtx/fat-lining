package com.github.xabgesagtx.fatlining.web;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.xabgesagtx.fatlining.common.FatLiningConstants;
import com.github.xabgesagtx.fatlining.common.Messages;
import com.github.xabgesagtx.fatlining.common.MessagesService;
import com.github.xabgesagtx.fatlining.common.UsersManager;
import com.github.xabgesagtx.fatlining.common.dtos.NewUserDTO;
import com.github.xabgesagtx.fatlining.common.dtos.UserDetailsDTO;
import com.github.xabgesagtx.fatlining.common.dtos.UserPasswordDTO;
import com.github.xabgesagtx.fatlining.common.validation.NewUserDTOValidator;
import com.github.xabgesagtx.fatlining.common.validation.UserDetailsDTOValidator;
import com.github.xabgesagtx.fatlining.common.validation.UserPasswordDTOValidator;
import com.github.xabgesagtx.fatlining.model.AppUser;

/**
 * 
 * Admin section of the website
 *
 */
@Controller
@RequestMapping("admin")
public class AdminController {
	
	@Autowired
	private UsersManager manager;
	
	@Autowired
	private MessagesService messages;
	
	@Autowired
	private NewUserDTOValidator newUserDTOValidator;
	
	@Autowired
	private UserPasswordDTOValidator userPasswordDTOValidator;
	
	@Autowired
	private UserDetailsDTOValidator userDetailsDTOValidator;
	
	@InitBinder(FatLiningConstants.FIELD_NEW_USER)
	public void initBinderNewUser(WebDataBinder binder) {
		binder.addValidators(newUserDTOValidator);
	}
	
	@InitBinder(FatLiningConstants.FIELD_PASSWORD_DTO)
	public void initBinderUserPassword(WebDataBinder binder) {
		binder.addValidators(userPasswordDTOValidator);
	}
	
	@InitBinder(FatLiningConstants.FIELD_USER_DETAILS)
	public void initBinderUserDetails(WebDataBinder binder) {
		binder.addValidators(userDetailsDTOValidator);
	}

	@GetMapping("users")
	public String users(Model model) {
		model.addAttribute(FatLiningConstants.FIELD_USERS, manager.findAll());
		return FatLiningConstants.VIEW_USERS;
	}
	
	@GetMapping("users/{id}/details")
	public String details(@PathVariable("id") Long id, Model model) {
		model.addAttribute(FatLiningConstants.FIELD_USER_DETAILS, manager.findById(id).map(user -> UserDetailsDTO.of(user.getId(), user.getName(), user.getEmail(), user.getGraphConfig().getGoalInKg())).orElseThrow(() -> new ResourceNotFoundException(messages.getMessage(Messages.USERS_ERROR_NOT_FOUND, id.toString()))));
		model.addAttribute(FatLiningConstants.FIELD_URL, "/admin/users/" + id + "/details");
		return FatLiningConstants.VIEW_DETAILS; 
	}
	
	@PostMapping("users/{id}/details")
	public String detailsPost(@PathVariable("id") Long id, @Valid @ModelAttribute(FatLiningConstants.FIELD_USER_DETAILS) UserDetailsDTO userDetails, BindingResult bindingResult, Model model, RedirectAttributes redirectAttr) {
		Optional<AppUser> userOpt = manager.findById(id);
		if (!userOpt.isPresent()) {
			throw new ResourceNotFoundException(messages.getMessage(Messages.USERS_ERROR_NOT_FOUND, id.toString()));
		} else if (!id.equals(userDetails.getId())) {
			throw new ManipulatedFormException(messages.getMessage(Messages.VALIDATION_ERROR_ID));
		} else if (bindingResult.hasErrors()) {
			model.addAttribute(FatLiningConstants.FIELD_URL, "/admin/users/" + id + "/details");
			return FatLiningConstants.VIEW_DETAILS;
		} else {
			manager.update(userOpt.get(), userDetails);
			redirectAttr.addFlashAttribute(FatLiningConstants.FIELD_SUCCESS, messages.getMessage(Messages.DETAILS_ADMIN_SUCCESS, userOpt.get().getName()));
			return "redirect:/admin/users"; 
		}
	}
	
	@GetMapping("users/{id}/password")
	public String password(@PathVariable("id") Long id, Model model, @ModelAttribute(FatLiningConstants.FIELD_PASSWORD_DTO) UserPasswordDTO passwordDto) {
		if (!manager.findById(id).isPresent()) {
			throw new ResourceNotFoundException(messages.getMessage(Messages.USERS_ERROR_NOT_FOUND, id.toString()));
		}
		model.addAttribute(FatLiningConstants.FIELD_URL, "/admin/users/" + id + "/password");
		return FatLiningConstants.VIEW_PASSWORD; 
	}
	
	@PostMapping("users/{id}/password")
	public String passwordPost(@PathVariable("id") Long id, @Valid @ModelAttribute(FatLiningConstants.FIELD_PASSWORD_DTO) UserPasswordDTO dto, BindingResult bindingResult, Model model, RedirectAttributes redirectAttr) {
		Optional<AppUser> userOpt = manager.findById(id);
		if (!userOpt.isPresent()) {
			throw new ResourceNotFoundException(messages.getMessage(Messages.USERS_ERROR_NOT_FOUND, id.toString()));
		} else if (bindingResult.hasErrors()) {
			model.addAttribute(FatLiningConstants.FIELD_URL, "/admin/users/" + id + "/password");
			return FatLiningConstants.VIEW_PASSWORD;
		} else {
			manager.changePassword(userOpt.get(), dto);
			redirectAttr.addFlashAttribute(FatLiningConstants.FIELD_SUCCESS, messages.getMessage(Messages.PASSWORD_SUCCESS));
			return "redirect:/admin/users";
		}
	}
	
	@PostMapping("users/{id}/activate")
	public String activate(@PathVariable("id") Long id, RedirectAttributes redirectAttr) {
		Optional<AppUser> userOpt = manager.findById(id);
		if (userOpt.isPresent()) {
			userOpt.ifPresent(user -> {
				user.setActivated(true);
				manager.save(user);
				redirectAttr.addFlashAttribute(FatLiningConstants.FIELD_SUCCESS, messages.getMessage(Messages.USERS_SUCCESS_ACTIVATE, user.getName()));
			});
		} else {
			throw new ResourceNotFoundException(messages.getMessage(Messages.USERS_ERROR_NOT_FOUND, id.toString()));
		}
		return "redirect:/admin/users";
	}
	
	@PostMapping("users/{id}/deactivate")
	public String deactivate(@PathVariable("id") Long id, RedirectAttributes redirectAttr) {
		Optional<AppUser> userOpt = manager.findById(id);
		if (userOpt.isPresent()) {
			userOpt.ifPresent(user -> {
				user.setActivated(false);
				manager.save(user);
				redirectAttr.addFlashAttribute(FatLiningConstants.FIELD_SUCCESS, messages.getMessage(Messages.USERS_SUCCESS_DEACTIVATE, user.getName()));
			});
		} else {
			throw new ResourceNotFoundException(messages.getMessage(Messages.USERS_ERROR_NOT_FOUND, id.toString()));
		}
		return "redirect:/admin/users";
	}
	
	@GetMapping("users/new")
	public String newUser(@ModelAttribute(FatLiningConstants.FIELD_NEW_USER) NewUserDTO user) {
		return FatLiningConstants.VIEW_NEW_USER;
	}
	
	
	@PostMapping("users/new")
	public String newUserPost(@Valid @ModelAttribute(FatLiningConstants.FIELD_NEW_USER) NewUserDTO user, BindingResult bindingResult, RedirectAttributes redirectAttr) {
		if (bindingResult.hasErrors()) {
			return FatLiningConstants.VIEW_NEW_USER;
		} else {
			manager.create(user);
			redirectAttr.addFlashAttribute(FatLiningConstants.FIELD_SUCCESS, messages.getMessage(Messages.NEW_USER_SUCCESS, user.getName()));
			return "redirect:/admin/users";
		}
	}
}
