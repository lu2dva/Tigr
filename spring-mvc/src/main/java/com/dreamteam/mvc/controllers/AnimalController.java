
package com.dreamteam.mvc.controllers;

import com.dreamteam.dto.AnimalDTO;
import com.dreamteam.facade.AnimalFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.dreamteam.facade.SpeciesFacade;
import com.dreamteam.facade.EnvironmentFacade;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Jiri Oliva
 */
@Controller
@RequestMapping("/animals")
public class AnimalController {
    @Autowired
    private AnimalFacade animalFacade;
    
    @Autowired
    private SpeciesFacade speciesFacade;

    @Autowired
    private EnvironmentFacade environmentFacade;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
            model.addAttribute("animalList", animalFacade.getAllAnimals());
		return "animal/animal";
    }
    
    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String create(Model model) {
        model.addAttribute("data", new AnimalDTO());
        model.addAttribute("continueLink", "/pa165/animals/create-action");
        model.addAttribute("buttonLabelCode", "tigr-message-crud-create");
        model.addAttribute("speciesList", speciesFacade.getAllSpecieses());     
        model.addAttribute("environmentList", environmentFacade.getAllEnvironments());
        model.addAttribute("predatorsList", animalFacade.getAllAnimals());
        model.addAttribute("preysList", animalFacade.getAllAnimals());
        return "animal/animal-form";
    }
    
    
    @RequestMapping(value = "create-action", method = RequestMethod.POST)
    public void create(@ModelAttribute("data") AnimalDTO animal, HttpServletResponse response) throws IOException {
        animalFacade.createAnimal(animal);
        response.sendRedirect("/pa165/animals");
    }
    
    @RequestMapping("delete/{animalId}")
    public void delete(@PathVariable("animalId") Long animalId, HttpServletResponse response) throws IOException {
        animalFacade.deleteAnimal(animalId);
        response.sendRedirect("/pa165/animals");
    }
    
    @RequestMapping(value = "edit/{animalId}", method = RequestMethod.GET)
    public String edit(@PathVariable("animalId") long animalId, Model model) {
        model.addAttribute("data", animalFacade.findAnimalById(animalId));
        model.addAttribute("continueLink", "/pa165/animals/edit-action");
        model.addAttribute("buttonLabelCode", "tigr-message-crud-update");
        model.addAttribute("speciesList", speciesFacade.getAllSpecieses());     
        model.addAttribute("environmentList", environmentFacade.getAllEnvironments());
        model.addAttribute("predatorsList", animalFacade.getAllAnimals());
        List<AnimalDTO> animals = animalFacade.getAllAnimals();
        animals.remove(animalFacade.findAnimalById(animalId));
        model.addAttribute("preysList", animals);
        return "animal/animal-form";
    }

    @RequestMapping(value = "edit-action", method = RequestMethod.POST)
    public void edit(@ModelAttribute("data") AnimalDTO animal, HttpServletRequest request, HttpServletResponse response) throws IOException {

        animalFacade.changeAnimalName(animal.getId(), animal.getName());
        animalFacade.changeAnimalDescription(animal.getId(), animal.getDescription());
        animalFacade.changeAnimalCount(animal.getId(), animal.getCount());
        
        animalFacade.clearEnvironments(animal.getId());
        for (Long enviId : animal.getEnvironmentId()){
            animalFacade.addAnimalEnvironment(animal.getId(), environmentFacade.findEnvironmentById(enviId));
        }
        
        /*animalFacade.clearPredators(animal.getId());
        for (Long predatorId : animal.getPredatorId()){
            animalFacade.addAnimalPredator(animal.getId(), animalFacade.findAnimalById(predatorId));
        }*/
        
        animalFacade.clearPreys(animal.getId());
        if (animal.getPreys()!= null)
        {
            for (Long preysId : animal.getPreysId()){
                animalFacade.addAnimalPrey(animal.getId(), animalFacade.findAnimalById(preysId));
            }
        }
        
        
        response.sendRedirect("/pa165/animals");
    }
    
}
