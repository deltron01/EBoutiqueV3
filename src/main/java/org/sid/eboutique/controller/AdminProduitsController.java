package org.sid.eboutique.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.sid.eboutique.entities.Categorie;
import org.sid.eboutique.entities.Produit;
import org.sid.eboutique.metier.IAdminProduitsMetier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(value="/adminProd")
public class AdminProduitsController {
	
	@Autowired
	private IAdminProduitsMetier metier;

	
	@RequestMapping(value="/index")
	public String index(Model model){
		model.addAttribute("produit", new Produit());
		model.addAttribute("produits", metier.listProduits());
		model.addAttribute("categories", metier.listCategories());
		return "produits";
	}
	
	@RequestMapping(value="/saveProd")
	public String saveProd(@Valid Produit p, BindingResult bindingResult, Model model, MultipartFile file) throws IOException{
		
		if(bindingResult.hasErrors()){
			model.addAttribute("categories", metier.listCategories());
			model.addAttribute("produits", metier.listProduits());
			return "produits";
		}
		String filepath = System.getProperty("java.io.tmpdir");
		Long idP = p.getIdProduit();
		if(idP == null)
		idP = metier.ajouterProduit(p, p.getCategorie().getIdCategorie());
		else
			System.out.println("photo in save form : "+p.getPhoto());
			metier.modifierProduit(p);
		if(!file.isEmpty()){
			/*BufferedImage bi =*/ /*ImageIO.read(file.getInputStream());*/
			//c.setPhoto(file.getBytes());
			//c.setNomPhoto(file.getOriginalFilename());
			//String filepath = System.getProperty("java.io.tmpdir")+"/"+p.getDesignation();
			file.transferTo(new File(filepath+"/PROD_"+idP+"_"+file.getOriginalFilename()));
			p.setIdProduit(idP); p.setPhoto(filepath+"/PROD_"+idP+"_"+file.getOriginalFilename());
			metier.modifierProduit(p);
			//metier.ajouterProduit(p, p.getCategorie().getIdCategorie());
		}
			
		model.addAttribute("produit", new Produit());
		model.addAttribute("categories", metier.listCategories());
		model.addAttribute("produits", metier.listProduits());
		return "produits";
	}
	
	@RequestMapping(value="photoProd", produces=MediaType.IMAGE_JPEG_VALUE)
	@ResponseBody  // car on a pas l'intention de retourner le nom dune vue,
	               //mais on chercher à renvoyer une information dans le corps de la réponse
	public byte[] photoProd(Long idProd) throws IOException{
		Produit p = metier.getProduit(idProd);
		File f = new File(p.getPhoto());
		return IOUtils.toByteArray(new FileInputStream(f));
	}
	
	@RequestMapping(value="/suppProd")
	public String suppProd(Long idProd, Model model){
		metier.supprimerProduit(idProd);
		model.addAttribute("produit", new Produit());
		model.addAttribute("categories", metier.listCategories());
		model.addAttribute("produits", metier.listProduits());
		return "produits";
	}
	@RequestMapping(value="/editProd")
	public String edit(Long idProd, Model model){
		Produit p = metier.getProduit(idProd);
		System.out.println("photo in edit request : "+p.getPhoto());
		model.addAttribute("produit", p);
		model.addAttribute("produits", metier.listProduits());
		model.addAttribute("categories", metier.listCategories());
		return "produits";
	}
}
