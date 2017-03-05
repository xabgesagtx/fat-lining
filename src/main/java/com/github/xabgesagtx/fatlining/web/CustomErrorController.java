package com.github.xabgesagtx.fatlining.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.xabgesagtx.fatlining.common.FatLiningConstants;
import com.github.xabgesagtx.fatlining.common.Messages;
import com.github.xabgesagtx.fatlining.common.MessagesService;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Controller to handle all errors
 *
 */
@Controller
@RequestMapping("")
@ControllerAdvice
@Slf4j
public class CustomErrorController implements ErrorController {
	
	@Autowired
	private MessagesService messages;
	
	@Override
	public String getErrorPath() {
		return "/error";
	}
	
	@RequestMapping("/403")
	public String accessDenied(Model model) {
		log.info("403");
		model.addAttribute(FatLiningConstants.FIELD_STATUS_CODE, "403");
		model.addAttribute(FatLiningConstants.FIELD_ERROR_MESSAGE, messages.getMessage(Messages.ERROR_403));
		return FatLiningConstants.VIEW_ERROR;		
	}
	
	@RequestMapping("/error")
	public String error(Model model) {
		log.info("Unexpected error");
		model.addAttribute(FatLiningConstants.FIELD_STATUS_CODE, "500");
		model.addAttribute(FatLiningConstants.FIELD_ERROR_MESSAGE, messages.getMessage(Messages.ERROR_500));
		return FatLiningConstants.VIEW_ERROR;		
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public String notFound(Exception e, Model model) {
		log.info("404: {}", e.getMessage());
		model.addAttribute(FatLiningConstants.FIELD_STATUS_CODE, "404");
		model.addAttribute(FatLiningConstants.FIELD_ERROR_MESSAGE, e.getMessage());
		return FatLiningConstants.VIEW_ERROR;
	}
	
	@ExceptionHandler(ManipulatedFormException.class)
	public String manipulatedForm(Exception e, Model model) {
		log.info("400: {}", e.getMessage());
		model.addAttribute(FatLiningConstants.FIELD_STATUS_CODE, "400");
		model.addAttribute(FatLiningConstants.FIELD_ERROR_MESSAGE, e.getMessage());
		return FatLiningConstants.VIEW_ERROR;
	}

}
