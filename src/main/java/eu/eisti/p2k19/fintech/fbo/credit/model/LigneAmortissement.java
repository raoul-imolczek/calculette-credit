package eu.eisti.p2k19.fintech.fbo.credit.model;

import java.time.LocalDate;

/**
 * Une ligne du tableau d'amortissement
 * @author Fabian
 *
 */
public class LigneAmortissement {

	/**
	 * La date de la p�riode
	 */
	private LocalDate date;

	/**
	 * Le capital restant d�
	 */
	private double capitalRestantDu;
	
	/**
	 * La mensualit� vers�e
	 */
	private double mensualite;
	
	/**
	 * Les int�r�ts pay�s
	 */
	private double interets;
	
	/**
	 * La mensualit� d'assurance pay�e
	 */
	private double assurance;
	
	public LigneAmortissement(double capitalRestantDu, double mensualite, double interets, double assurance,
			LocalDate date) {
		super();
		this.capitalRestantDu = capitalRestantDu;
		this.mensualite = mensualite;
		this.interets = interets;
		this.assurance = assurance;
		this.date = date;
	}

	public double getCapitalRestantDu() {
		return capitalRestantDu;
	}
	
	public void setCapitalRestantDu(double capitalRestantDu) {
		this.capitalRestantDu = capitalRestantDu;
	}
	
	public double getMensualite() {
		return mensualite;
	}
	
	public void setMensualite(double mensualite) {
		this.mensualite = mensualite;
	}
	
	public double getInterets() {
		return interets;
	}
	
	public void setInterets(double interets) {
		this.interets = interets;
	}
	
	public double getAssurance() {
		return assurance;
	}
	
	public void setAssurance(double assurance) {
		this.assurance = assurance;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	
	
}
