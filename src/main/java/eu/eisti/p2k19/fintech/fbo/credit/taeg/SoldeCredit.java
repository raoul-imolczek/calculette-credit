package eu.eisti.p2k19.fintech.fbo.credit.taeg;

import org.apache.commons.math3.analysis.UnivariateFunction;

public class SoldeCredit implements UnivariateFunction {

	private int duree;
	private double capital;
	private double taux;
	private double tauxAssurance;
	
	public SoldeCredit(int duree, double capital, double taux, double tauxAssurance) {
		this.duree = duree;
		this.capital = capital;
		this.taux = taux;
	}
	
	@Override
	public double value(double mensualite) {
        double resultat = capital;
        double mensualiteAssurance = capital * tauxAssurance / 100;

        for(int i = 0; i < duree; i++) {
			if (resultat > 0) {
				double interets = resultat * taux / 100 / 12;
				resultat = resultat - mensualite + interets + mensualiteAssurance;
			} else {
				resultat = resultat - mensualite;
			}
        }

        return resultat;
	}

}
