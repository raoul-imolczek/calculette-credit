package eu.eisti.p2k19.fintech.fbo.credit.model;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BisectionSolver;

import eu.eisti.p2k19.fintech.fbo.credit.taeg.EquationValeurActualisee;

/**
 * Crédit immobilier
 * @author Fabian
 *
 */
public class CreditImmobilier extends Credit implements CreditAmortissable {

	/**
	 * Montant de la caution versée au Crédit Logement
	 */
	private double montantCaution;
	
	/**
	 * Taux d'assurance : il est multiplié au capital emprunté pour obtenir la mensualité d'assurance
	 */
	private double tauxAssurance;
	
	/**
	 * Date de délocage initial des fonds
	 */
	private LocalDate dateDepart;
	
	public CreditImmobilier(double montantApport, double montantProjet, double mensualite, double taux, double montantCaution, double tauxAssurance, LocalDate dateDepart) throws TauxUsureException {
		super(montantApport, montantProjet, mensualite, taux);
		this.montantCaution = montantCaution;
		this.tauxAssurance = tauxAssurance;
		this.dateDepart = dateDepart;
	}

	public CreditImmobilier(double montantApport, double montantProjet, int duree, double taux, double montantCaution, double tauxAssurance, LocalDate dateDepart) throws TauxUsureException {
		super(montantApport, montantProjet, duree, taux);
		this.montantCaution = montantCaution;
		this.tauxAssurance = tauxAssurance;
		this.dateDepart = dateDepart;
	}
	
	public double getMontantCaution() {
		return montantCaution;
	}

	@Override
	public TableauAmortissement getTableauAmortissement() throws CreditPasRemboursableException {

		// Le capital emprunté est le montant du projet auquel on retranche l'apport et auquel on ajoute la caution
		double capital = montantProjet - montantApport + montantCaution;

		TableauAmortissement tableau;
		try {
			if (this.parLaMensualite) {
				tableau = new TableauAmortissement(capital, this.mensualite, this.taux, this.tauxAssurance, this.dateDepart);
			} else {
				tableau = new TableauAmortissement(capital, this.duree, this.taux, this.tauxAssurance, this.dateDepart);
			}			
		} catch (CreditPasRemboursableException e) {
			System.out.println("Le crédit n'est pas remboursable");
			throw e;
		}
		return tableau;
	
	}

	@Override
	public double getTaeg() throws CreditPasRemboursableException {
		
		TableauAmortissement tableau = getTableauAmortissement();
		
		// Initialisation de la liste des flux financiers
		List<FluxFinancier> fluxFinanciers = new ArrayList<FluxFinancier>();

		// On ajoute un flux négatif de déblocage des fonds
		fluxFinanciers.add(new FluxFinancier(- this.montantProjet + this.montantApport - this.montantCaution, this.dateDepart));
		
		// On itère sur toutes les lignes du tableau d'amortissement
		Iterator<LigneAmortissement> iterator = tableau.getLignes().iterator();
		while(iterator.hasNext()) {
			LigneAmortissement ligne = iterator.next();
			// On ajoute un flux financier par ligne du tableau d'amortissement
			fluxFinanciers.add(new FluxFinancier(ligne.getMensualite(), ligne.getDate()));
		}
		
		// Initialisation d'une équation des valeurs actualisées avec l'ensemble des flux financiers
        UnivariateFunction equationValeurActualisee = new EquationValeurActualisee(fluxFinanciers);
        
        // On utilise un algorithme de recherche de solution par dichotomie
        BisectionSolver bisectionSolver = new BisectionSolver();
      
        // Le taux est recherché entre -100% et 100%, on demande à l'algorithme d'effectuer 100 itérations pour approcher
        // un taeg pour lequel l'équation des valeurs actualisïées se rapproche le plus possible de 0
        double taux = bisectionSolver.solve(100, equationValeurActualisee, -100d, 100d);
        
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_UP);
        
        return Double.parseDouble(df.format(taux).replace(',', '.'));
	}
	
	@Override
	public int getDuree() throws CreditPasRemboursableException {
		
		if(this.parLaMensualite) {
			TableauAmortissement tableau = getTableauAmortissement();
			this.duree = tableau.getLignes().size();
		}
			
		return super.getDuree();
	}

	@Override
	public double getMensualite() throws CreditPasRemboursableException {
		
		if(!this.parLaMensualite) {
			TableauAmortissement tableau = getTableauAmortissement();
			this.mensualite = tableau.getLignes().get(0).getMensualite();
		}
			
		return super.getMensualite();
	}

}
