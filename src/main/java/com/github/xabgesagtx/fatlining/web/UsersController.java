package com.github.xabgesagtx.fatlining.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.xabgesagtx.fatlining.common.FatLiningConstants;
import com.github.xabgesagtx.fatlining.common.Messages;
import com.github.xabgesagtx.fatlining.common.MessagesService;
import com.github.xabgesagtx.fatlining.common.UsersManager;
import com.github.xabgesagtx.fatlining.common.dtos.UserDetailsDTO;
import com.github.xabgesagtx.fatlining.common.dtos.UserPasswordDTO;
import com.github.xabgesagtx.fatlining.common.validation.UserDetailsDTOValidator;
import com.github.xabgesagtx.fatlining.common.validation.UserPasswordDTOValidator;
import com.github.xabgesagtx.fatlining.model.AppUser;

/**
 * 
 * Page to view and edit user details of the logged in user
 *
 */
@Controller
@RequestMapping("user")
@PreAuthorize("isAuthenticated()")
public class UsersController {

	@Autowired
	private UsersManager manager;
	
	@Autowired
	private MessagesService messages;
	
	@Autowired
	private UserPasswordDTOValidator userPasswordDTOValidator;
	
	@Autowired
	private UserDetailsDTOValidator userDetailsDTOValidator;
	
	@InitBinder(FatLiningConstants.FIELD_PASSWORD_DTO)
	public void initBinderUserPassword(WebDataBinder binder) {
		binder.addValidators(userPasswordDTOValidator);
	}
	
	@InitBinder(FatLiningConstants.FIELD_USER_DETAILS)
	public void initBinderUserDetails(WebDataBinder binder) {
		binder.addValidators(userDetailsDTOValidator);
	}
	
	@GetMapping("details")
	public String detailsCurrentUser(Model model) {
		AppUser user = manager.getCurrentUser();
		model.addAttribute(FatLiningConstants.FIELD_USER_DETAILS, UserDetailsDTO.of(user.getId(), user.getName(), user.getEmail(), user.getGraphConfig().getGoalInKg()));
		model.addAttribute(FatLiningConstants.FIELD_URL, "/user/details");
		return FatLiningConstants.VIEW_DETAILS;
	}
	
	@PostMapping("details")
	public String updateDetailsOfUser(@Valid @ModelAttribute(FatLiningConstants.FIELD_USER_DETAILS) UserDetailsDTO userDetails, BindingResult bindingResult, Model model, RedirectAttributes redirectAttr) {
		AppUser currentUser = manager.getCurrentUser();
		if (bindingResult.hasErrors()) {
			model.addAttribute(FatLiningConstants.FIELD_URL, "/user/details");
			return FatLiningConstants.VIEW_DETAILS;
		} else if (!currentUser.getId().equals(userDetails.getId())) {
			throw new ManipulatedFormException(messages.getMessage(Messages.VALIDATION_ERROR_ID));
		} else {
			manager.update(currentUser, userDetails);
			redirectAttr.addFlashAttribute(FatLiningConstants.FIELD_SUCCESS, messages.getMessage(Messages.DETAILS_NORMAL_SUCCESS));
			return "redirect:/user/details";
		}
	}
	
	@GetMapping("password")
	public String resetPassword(Model model) {
		UserPasswordDTO userPasswordDTO = new UserPasswordDTO();
		model.addAttribute(FatLiningConstants.FIELD_PASSWORD_DTO, userPasswordDTO);
		model.addAttribute(FatLiningConstants.FIELD_URL, "/user/password");
		return "password";
	}
	
	@PostMapping("password")
	public String performResetPassword(@Valid @ModelAttribute(FatLiningConstants.FIELD_PASSWORD_DTO) UserPasswordDTO dto, BindingResult bindingResult, RedirectAttributes redirectAttr) {
		if (bindingResult.hasErrors()) {
			return FatLiningConstants.VIEW_PASSWORD;
		} else {
			AppUser user = manager.getCurrentUser();
			manager.changePassword(user, dto);
			redirectAttr.addFlashAttribute(FatLiningConstants.FIELD_SUCCESS, messages.getMessage(Messages.PASSWORD_SUCCESS));
			return "redirect:/user/password";
		}
		
	}

}
