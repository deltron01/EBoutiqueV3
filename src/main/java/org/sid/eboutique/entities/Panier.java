package org.sid.eboutique.entities;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Panier { // entity non persistante.
	
	private Map<Long, LigneCommande> items = new HashMap<Long, LigneCommande>();
	
	public void addArticle(Produit p, int quantite){
		LigneCommande lc = items.get(p.getIdProduit());
		if(lc == null){
			LigneCommande art = new LigneCommande(quantite, p.getPrix());
			art.setProduit(p);
			items.put(p.getIdProduit(), art);
		} else{
			lc.setQuantite(lc.getQuantite() + quantite);
		}
	}
	
	public Collection<LigneCommande> getItems(){
		return items.values();
	}
	
	public double getTotal(){
		double total = 0;
		for(LigneCommande lc:items.values()){
			total+= lc.getQuantite() * lc.getPrix();
		}
		return total;
	}
	public int getSize(){
		return items.size();
	}
	public void removeItem(Long idProduit){
		items.remove(idProduit);
	}

}
