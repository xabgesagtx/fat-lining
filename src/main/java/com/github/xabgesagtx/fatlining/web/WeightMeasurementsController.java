package com.github.xabgesagtx.fatlining.web;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.xabgesagtx.fatlining.common.FatLiningConstants;
import com.github.xabgesagtx.fatlining.common.Messages;
import com.github.xabgesagtx.fatlining.common.MessagesService;
import com.github.xabgesagtx.fatlining.common.UsersManager;
import com.github.xabgesagtx.fatlining.common.WeightMeasurementManager;
import com.github.xabgesagtx.fatlining.config.ChartsConfig;
import com.github.xabgesagtx.fatlining.config.PaginationConfig;
import com.github.xabgesagtx.fatlining.model.AppUser;
import com.github.xabgesagtx.fatlining.model.WeightMeasurement;
import com.github.xabgesagtx.fatlining.web.charts.Chart;
import com.github.xabgesagtx.fatlining.web.charts.ChartFactory;
import com.github.xabgesagtx.fatlining.web.pagination.PageWrapperFactory;

/**
 * 
 * Pages for all measurements of the logged in user
 *
 */
@Controller
@RequestMapping("measurements")
@PreAuthorize("isAuthenticated()")
public class WeightMeasurementsController {
	
	@Autowired
	private WeightMeasurementManager measurementsManager;
	
	@Autowired
	private UsersManager usersManager;
	
	@Autowired
	private PageWrapperFactory pageWrapperFactory;
	
	@Autowired
	private ChartFactory chartFactory;
	
	@Autowired
	private MessagesService messages;
	
	@Autowired
	private PaginationConfig paginationConfig;
	
	@Autowired
	private ChartsConfig chartsConfig;
	
	@GetMapping("all")
	public String all(@RequestParam(value = "page", defaultValue = "0") int pageNumber, Model model) {
		if (pageNumber < 0) {
			return "redirect:/measurements/all?page=0";
		} else {
			Page<WeightMeasurement> page = measurementsManager.findAsPageDesc(pageNumber, paginationConfig.getPageSize());
			if (pageNumber >= page.getTotalPages() && page.getTotalElements() > 0) {
				return "redirect:/measurements/all?page=0";
			} else {
				model.addAttribute(FatLiningConstants.FIELD_PAGE, pageWrapperFactory.create(page));
				return FatLiningConstants.VIEW_MEASUREMENTS;
			}
		}
	}
	
	@PostMapping("{id}/delete")
	public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttr) {
		if (measurementsManager.delete(id)) {
			redirectAttr.addFlashAttribute(FatLiningConstants.FIELD_SUCCESS, messages.getMessage(Messages.MEASUREMENTS_SUCCESS_DELETED, id.toString()));
			return "redirect:/measurements/all";
		} else {
			throw new ResourceNotFoundException(messages.getMessage(Messages.MEASUREMENTS_ERROR_NOT_FOUND, id.toString()));
		}
	}
	
	@GetMapping("")
	public String addFormGet(@ModelAttribute(FatLiningConstants.FIELD_MEASUREMENT) WeightMeasurement measurement, Model model) {
		model.addAttribute(FatLiningConstants.FIELD_DAYS, chartsConfig.getDaysOptions());
		return FatLiningConstants.VIEW_ADD;
	}
	
	@PostMapping("")
	public String addFormPost(WeightMeasurement measurement, BindingResult bindingResult, RedirectAttributes redirectAttr) {
		measurementsManager.save(measurement);
		redirectAttr.addFlashAttribute(FatLiningConstants.FIELD_SUCCESS, messages.getMessage(Messages.MEASUREMENTS_SUCCESS_ADDED));
		return "redirect:/measurements";
	}
	
	@GetMapping("charts")
	@ResponseBody
	public Chart charts(@RequestParam(value="days",defaultValue="30") int days) {
		AppUser currentUser = usersManager.getCurrentUser();
		BigDecimal goalInKg = currentUser.getGraphConfig().getGoalInKg();
		if (days > chartsConfig.getMaxDays()) {
			days = chartsConfig.getMaxDays();
		}
		return chartFactory.create(goalInKg, measurementsManager.findAllForUserAndDays(days));
	}
	
	
}
