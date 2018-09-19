package eu.eisti.p2k19.fintech.fbo.credit.taeg;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.math3.analysis.UnivariateFunction;

import eu.eisti.p2k19.fintech.fbo.credit.model.FluxFinancier;

public class EquationValeurActualisee implements UnivariateFunction {

	private List<FluxFinancier> fluxFinanciers;

	public EquationValeurActualisee(List<FluxFinancier> fluxFinanciers) {
		this.fluxFinanciers = fluxFinanciers;
	}
	
	@Override
	public double value(double taux) {
		ordonnerFluxFinanciers();
        LocalDate dateDepart = this.fluxFinanciers.get(0).getDate();
        
        double resultat = 0;

        // On itère sur tous les flux financiers
        Iterator<FluxFinancier> iterator = fluxFinanciers.iterator();
        while(iterator.hasNext()) {
        	FluxFinancier fluxFinancier = iterator.next();
        	// Pour chaque flux, on calcule la valeur actualisée et on les additionne tous
        	resultat += fluxFinancier.getValeurActualisee(dateDepart, taux);        
        }
        
        return resultat;
	}
	
	/**
	 * Ordonnancement des flux dans l'ordre chronologique
	 */
    private void ordonnerFluxFinanciers() {
    	// On compare les flux par rapport à la méthode getDate
        this.fluxFinanciers.sort(Comparator.comparing(FluxFinancier::getDate));    
    }

}
