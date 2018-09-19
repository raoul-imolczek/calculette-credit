package eu.eisti.p2k19.fintech.fbo.credit.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Tableau d'amortissement
 * @author Fabian
 *
 */
public class TableauAmortissement {

	private List<LigneAmortissement> lignes;
	
	public TableauAmortissement(double capital, double mensualite, double taux, double tauxAssurance, LocalDate dateDepart) throws CreditPasRemboursableException {

		// Il comporte autant de lignes que de mensualités versées
		this.lignes = new ArrayList<LigneAmortissement>();
		
		// La mensualité d'assurance est fixe tout au long du crédit et dépend du capital initial
		double mensualiteAssurance = capital * tauxAssurance / 100;
		
		// La date du tableau d'amortissement est initié à partir de la date de départ
		LocalDate date = dateDepart;
				
		// On boucle jusqu'à ce que le crédit soit remboursé
		while(capital > 0) {
			
			// Chaque mois, les intérêts sont calculés à partir du capital restant dû
			// auquel on multiplie le taux de période (taux annuel divisé par 12, le nombre de périodes annuelles)
			double interets = capital * taux / 100 / 12;
			
			// La date de la prochaine échéance est 1 mois plus tard (durée de la période)
			date = date.plusMonths(1);
			
			LigneAmortissement ligne;
			
			// On teste si la mensualité ne va pas intégralement rembourser le crédit
			if (capital > mensualite - interets - mensualiteAssurance) {
				ligne = new LigneAmortissement(capital, mensualite, interets, mensualiteAssurance, date);
				// On rembourse du capital la mensualité à laquelle on retranche les intérêts et l'assurance
				capital = capital - mensualite + interets + mensualiteAssurance;
			// C'est la dernière mensualité
			} else {
				ligne = new LigneAmortissement(capital, capital + interets + mensualiteAssurance, interets, mensualiteAssurance, date);
				capital = 0;
			}
			
			lignes.add(ligne);
		
			// Si la mensualité ne couvre pas les intérêts et l'assurance, alors il n'est pas remboursable
			if (interets + mensualiteAssurance >= mensualite) {
				throw new CreditPasRemboursableException();
			}
			
		}
		
	}

	public List<LigneAmortissement> getLignes() {
		return lignes;
	}

	public void setLignes(List<LigneAmortissement> lignes) {
		this.lignes = lignes;
	}

}
