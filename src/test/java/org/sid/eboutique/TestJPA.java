package org.sid.eboutique;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.sid.eboutique.entities.Categorie;
import org.sid.eboutique.entities.Produit;
import org.sid.eboutique.metier.IAdminCategoriesMetier;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestJPA {
	
	ClassPathXmlApplicationContext context;
	
	@Before
	public void setUp() throws Exception {
		context = new ClassPathXmlApplicationContext(new String[] {"applicationContext.xml"});
	}
	
	@Test
	public void test1() {
		try{
		IAdminCategoriesMetier metier = (IAdminCategoriesMetier) context.getBean("metier");
		List<Categorie> cts1 = metier.listCategories();
		metier.ajouterCategorie(new Categorie("Ordinateurs", "ordinateurs portables", null, "image1.jpg"));
		metier.ajouterCategorie(new Categorie("Imprimantes", "imprimantes Jet Encre / Laser", null, "image2.jpg"));
		List<Categorie> cts2 = metier.listCategories();
		assertTrue(cts1.size()+2 == cts2.size());
		} catch(Exception e){
			assertTrue(e.getMessage(), false); // donne echec du test et affiche le message correspondant simultanément
		}
	}
	
	@Test
	public void test2() {
		try{
		IAdminCategoriesMetier metier = (IAdminCategoriesMetier) context.getBean("metier");
		List<Produit> prods1 = metier.listProduits();
		metier.ajouterProduit(new Produit("Lenovo K120", "Lenovo NoteBook laptop", 6500.0, false, "image3.jpg", 3), 1L);
		metier.ajouterProduit(new Produit("Canon Laser 270L", "Canon printer LaserJet", 6500.0, false, "image3.jpg", 2), 2L);
		List<Produit> prods2 = metier.listProduits();
		assertTrue(prods1.size()+2 == prods2.size());
		} catch(Exception e){
			assertTrue(e.getMessage(), false); // donne echec du test et affiche le message correspondant simultanément
		}
	}

}
