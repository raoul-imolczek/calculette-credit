package eu.eisti.p2k19.fintech.fbo.credit.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BisectionSolver;

import eu.eisti.p2k19.fintech.fbo.credit.taeg.EquationValeurActualisee;

/**
 * Cr�dit immobilier
 * @author Fabian
 *
 */
public class CreditImmobilier extends Credit implements CreditAmortissable {

	/**
	 * Montant de la caution vers�e au Cr�dit Logement
	 */
	private double montantCaution;
	
	/**
	 * Taux d'assurance : il est multipli� au capital emprunt� pour obtenir la mensualit� d'assurance
	 */
	private double tauxAssurance;
	
	/**
	 * Date de d�blocage initial des fonds
	 */
	private LocalDate dateDepart;
	
	public CreditImmobilier(double montantApport, double montantProjet, double mensualite, double taux, double montantCaution, double tauxAssurance, LocalDate dateDepart) throws TauxUsureException {
		super(montantApport, montantProjet, mensualite, taux);
		this.montantCaution = montantCaution;
		this.tauxAssurance = tauxAssurance;
		this.dateDepart = dateDepart;
	}

	public double getMontantCaution() {
		return montantCaution;
	}

	@Override
	public TableauAmortissement getTableauAmortissement() throws CreditPasRemboursableException {

		// Le capital emprunt� est le montant du projet auquel on retranche l'apport et auquel on ajoute la caution
		double capital = montantProjet - montantApport + montantCaution;

		TableauAmortissement tableau;
		try {
			tableau = new TableauAmortissement(capital, this.mensualite, this.taux, this.tauxAssurance, this.dateDepart);
		} catch (CreditPasRemboursableException e) {
			System.out.println("Le cr�dit n'est pas remboursable");
			throw e;
		}
		return tableau;
	
	}

	@Override
	public double getTaeg() throws CreditPasRemboursableException {
		
		TableauAmortissement tableau = getTableauAmortissement();
		
		// Initialisation de la liste des flux financiers
		List<FluxFinancier> fluxFinanciers = new ArrayList<FluxFinancier>();

		// On ajoute un flux n�gatif de d�blocage des fonds
		fluxFinanciers.add(new FluxFinancier(- this.montantProjet + this.montantApport - this.montantCaution, this.dateDepart));
		
		// On it�re sur toutes les lignes du tableau d'amortissement
		Iterator<LigneAmortissement> iterator = tableau.getLignes().iterator();
		while(iterator.hasNext()) {
			LigneAmortissement ligne = iterator.next();
			// On ajoute un flux financier par ligne du tableau d'amortissement
			fluxFinanciers.add(new FluxFinancier(ligne.getMensualite(), ligne.getDate()));
		}
		
		// Initialisation d'une �quation des valeurs actualis�es avec l'ensemble des flux financiers
        UnivariateFunction equationValeurActualisee = new EquationValeurActualisee(fluxFinanciers);
        
        // On utilise un algorithme de recherche de solution par dichotomie
        BisectionSolver bisectionSolver = new BisectionSolver();
      
        // Le taux est recherch� entre -100% et 100%, on demande � l'algorithme d'effectuer 100 it�rations pour approcher
        // un taeg pour lequel l'�quation des valeurs actualis�es se rapproche le plus possible de 0
        double taux = bisectionSolver.solve(100, equationValeurActualisee, -100d, 100d);
        
        return taux;
	}

}
