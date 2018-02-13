package org.sid.eboutique.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.sid.eboutique.entities.Categorie;
import org.sid.eboutique.metier.IAdminCategoriesMetier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
@Controller
@RequestMapping(value="/adminCat")
@SessionAttributes("editedCat")
public class AdminCategoriesController implements HandlerExceptionResolver {
	
	@Autowired
	private IAdminCategoriesMetier metier;
	
	@RequestMapping(value="/index")
	public String index(Model model){
		model.addAttribute("categorie", new Categorie());
		model.addAttribute("categories", metier.listCategories());
		return "categories";
	}
	
	@RequestMapping(value="/saveCat")
	public String saveCat(@Valid Categorie c, BindingResult bindingResult, Model model, MultipartFile file) throws IOException{
		
		if(bindingResult.hasErrors()){
			model.addAttribute("categories", metier.listCategories());
			return "categories";
		}
		if(!file.isEmpty()){
			/*BufferedImage bi =*/ ImageIO.read(file.getInputStream());
			c.setPhoto(file.getBytes());
			c.setNomPhoto(file.getOriginalFilename());
		}
		if(c.getIdCategorie() != null){
			if(model.asMap().get("editedCat") != null){
				/*Categorie cat = (Categorie) model.asMap().get("editedCat");
				if(c.getPhoto() == null)                  // on choisit la méthode file.isEmpty pour eviter d'ecraser 
				                                          // l'ancien image si on ne choisit pas une nouvelle
				c.setPhoto(cat.getPhoto());*/
				if(file.isEmpty()){
					Categorie cat = (Categorie) model.asMap().get("editedCat"); // autre méthode c'est de charger cet objet de la BDD 
					                                                            // au lieu d'utiliser la variable de Session
					c.setPhoto(cat.getPhoto());
				}
			}
			metier.modifierCategorie(c);
		} else{
			metier.ajouterCategorie(c);
		}	
		model.addAttribute("categorie", new Categorie());
		model.addAttribute("categories", metier.listCategories());
		return "categories";
	}
	@RequestMapping(value="/suppCat")
	public String supp(Long idCat, Model model){
		metier.supprimerCategorie(idCat);
		model.addAttribute("categorie", new Categorie());
		model.addAttribute("categories", metier.listCategories());
		return "categories";
	}
	@RequestMapping(value="/editCat")
	public String edit(Long idCat, Model model){
		Categorie c  = metier.getCategorie(idCat);
		model.addAttribute("editedCat", c);
		model.addAttribute("categorie", c);
		model.addAttribute("categories", metier.listCategories());
		return "categories";
	}
	@RequestMapping(value="photoCat", produces=MediaType.IMAGE_JPEG_VALUE)
	@ResponseBody  // car on a pas l'intention de retourner le nom dune vue,
	               //mais on chercher à renvoyer une information dans le corps de la réponse
	public byte[] photoCat(Long idCat) throws IOException{
		Categorie c = metier.getCategorie(idCat);
		return IOUtils.toByteArray(new ByteArrayInputStream(c.getPhoto()));
	}

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object obj,
			Exception ex) {// ce handler va s'occuper de toutes les excep mem celles générées par les autres controllers. ce Resolver est chargé par Spring.
		ModelAndView mv = new ModelAndView();
		mv.addObject("categorie", new Categorie());// il le faut car la jsp attend un tel objet pour initialiser le "form" qui est sur jsp.// sinon Error tomCat
		mv.addObject("categories", metier.listCategories());
		mv.addObject("exception", ex.getMessage());
		mv.setViewName("categories");
		return mv;
		// on peut encore personnaliser la reponse en se basant sur le type d'exception (via des test"if Ex.instanceOf(???)
	}

}
