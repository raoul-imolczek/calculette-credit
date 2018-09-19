package eu.eisti.p2k19.fintech.fbo.credit.model;

import java.time.Duration;
import java.time.LocalDate;

public class FluxFinancier {

	/**
	 * Montant du flux
	 */
    private double montant;
    
    /**
     * Date du flux
     */
    private LocalDate date;
    
    public FluxFinancier(double montant, LocalDate date) {
		super();
		this.montant = montant;
		this.date = date;
	}

	public double getMontant() {
		return montant;
	}

	public void setMontant(double montant) {
		this.montant = montant;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public double getValeurActualisee(LocalDate startDate, double rate) {
        double res;
        res = this.montant / 
            Math.pow(
                rate / 100 + 1, 
                new Double(Duration.between(startDate.atTime(0, 0), this.date.atTime(0, 0)).toDays()) / 365
            );
        return res;
    }
    
}
