package eu.eisti.p2k19.fintech.fbo.credit.model;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BisectionSolver;

import eu.eisti.p2k19.fintech.fbo.credit.taeg.SoldeCredit;

/**
 * Tableau d'amortissement
 * @author Fabian
 *
 */
public class TableauAmortissement {

	private List<LigneAmortissement> lignes;
	
	public TableauAmortissement(double capital, double mensualite, double taux, double tauxAssurance, LocalDate dateDepart) throws CreditPasRemboursableException {

		calculerTableauAmortissement(capital, mensualite, taux, tauxAssurance, dateDepart);
		
	}

	public TableauAmortissement(double capital, int duree, double taux, double tauxAssurance, LocalDate dateDepart) throws CreditPasRemboursableException {
        UnivariateFunction soldeCredit = new SoldeCredit(duree, capital, taux, tauxAssurance);
        BisectionSolver bisectionSolver = new BisectionSolver();
        double mensualite = bisectionSolver.solve(100, soldeCredit, 0, capital);
	
        System.out.println("MENSUALITE CALCULEE: " + mensualite);
        
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_UP);
        
        calculerTableauAmortissement(capital, Double.parseDouble(df.format(mensualite).replace(',', '.')), taux, tauxAssurance, dateDepart);
	}

	private void calculerTableauAmortissement(double capital, double mensualite, double taux, double tauxAssurance,
			LocalDate dateDepart) throws CreditPasRemboursableException {
		// Il comporte autant de lignes que de mensualit�s vers�es
		this.lignes = new ArrayList<LigneAmortissement>();
		
		// La mensualit� d'assurance est fixe tout au long du cr�dit et d�pend du capital initial
		double mensualiteAssurance = capital * tauxAssurance / 100;
		
		// La date du tableau d'amortissement est initi� � partir de la date de d�part
		LocalDate date = dateDepart;
				
		// On boucle jusqu'� ce que le cr�dit soit rembours�
		while(capital > 0) {
			
			// Chaque mois, les int�r�ts sont calcul�s � partir du capital restant d�
			// auquel on multiplie le taux de p�riode (taux annuel divis� par 12, le nombre de p�riodes annuelles)
			double interets = capital * taux / 100 / 12;
			
			// La date de la prochaine �ch�ance est 1 mois plus tard (dur�e de la p�riode)
			date = date.plusMonths(1);
			
			LigneAmortissement ligne;
			
			// On teste si la mensualit� ne va pas int�gralement rembourser le cr�dit
			if (capital > mensualite - interets - mensualiteAssurance) {
				ligne = new LigneAmortissement(capital, mensualite, interets, mensualiteAssurance, date);
				// On rembourse du capital la mensualit� � laquelle on retranche les int�r�ts et l'assurance
				capital = capital - mensualite + interets + mensualiteAssurance;
			// C'est la derni�re mensualit�
			} else {
				ligne = new LigneAmortissement(capital, capital + interets + mensualiteAssurance, interets, mensualiteAssurance, date);
				capital = 0;
			}
			
			lignes.add(ligne);
		
			// Si la mensualit� ne couvre pas les int�r�ts et l'assurance, alors il n'est pas remboursable
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
