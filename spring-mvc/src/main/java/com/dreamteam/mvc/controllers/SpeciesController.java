package com.dreamteam.mvc.controllers;

import com.dreamteam.dto.SpeciesDTO;
import com.dreamteam.facade.SpeciesFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by jan.novak
 */
@Controller
@RequestMapping("/species")
public class SpeciesController {

	public static final String VIEW_LIST = "species/species";
	public static final String MODEL_ATTR_SPECIES_LIST = "speciesList";
	private static final Logger log = LoggerFactory.getLogger(SpeciesController.class);
	@Autowired
	private SpeciesFacade speciesFacade;

	@RequestMapping(method = RequestMethod.GET)
	public String list(HttpServletRequest request, Model model) {
		model.addAttribute(MODEL_ATTR_SPECIES_LIST, speciesFacade.getAllSpecieses());
		return VIEW_LIST;
	}

	@RequestMapping("delete/{speciesId}")
	public void delete(@PathVariable("speciesId") long speciesId, HttpServletResponse response) throws IOException {
		speciesFacade.deleteSpecies(speciesId);
		response.sendRedirect("/pa165/species");
	}

	@RequestMapping(value = "edit/{speciesId}", method = RequestMethod.GET)
	public String edit(@PathVariable("speciesId") long speciesId, Model model) {
		model.addAttribute("data", speciesFacade.getSpeciesById(speciesId));
		model.addAttribute("continueLink", "/pa165/species/edit-action");
		model.addAttribute("buttonLabelCode", "tigr-message-crud-update");
		return "species/species-form";
	}

	@RequestMapping(value = "edit-action", method = RequestMethod.POST)
	public void edit(@ModelAttribute("data") SpeciesDTO dto,
					 HttpServletRequest request,
					 HttpServletResponse response) throws IOException {

		speciesFacade.changeSpeciesName(dto.getId(), dto.getName());
		speciesFacade.changeSpeciesDescription(dto.getId(), dto.getDescription());
		if(dto.isInDanger())
			speciesFacade.setSpeciesInDanger(dto.getId());
		else
			speciesFacade.setSpeciesNotInDanger(dto.getId());

		response.sendRedirect("/pa165/species");
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String create(Model model) {
		model.addAttribute("data", new SpeciesDTO());
		model.addAttribute("continueLink", "/pa165/species/create-action");
		model.addAttribute("buttonLabelCode", "tigr-message-crud-create");
		return "species/species-form";
	}

	@RequestMapping(value = "create-action", method = RequestMethod.POST)
	public void create(@ModelAttribute("data") SpeciesDTO dto, HttpServletResponse response) throws IOException {
		speciesFacade.createSpecies(dto);
		response.sendRedirect("/pa165/species");
	}

}
