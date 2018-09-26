package eu.eisti.p2k19.fintech.fbo.credit.model;

public abstract class Credit {

	protected double montantApport;
	protected double montantProjet;
	protected double mensualite;
	protected int duree;
	protected double taux;

	private final static float TAUX_USURE = 20;
	
	protected Credit(double montantApport, double montantProjet, double mensualite, double taux) throws TauxUsureException {
		super();
		this.montantApport = montantApport;
		this.montantProjet = montantProjet;
		this.mensualite = mensualite;
		this.taux = taux;
		if (this.taux > TAUX_USURE) {
			throw new TauxUsureException();			
		}
	}

	public double getMontantApport() {
		return montantApport;
	}

	public double getMontantProjet() {
		return montantProjet;
	}

	public double getMensualite() {
		return mensualite;
	}

	public int getDuree() throws CreditPasRemboursableException {
		return duree;
	}

	public double getTaux() {
		return taux;
	}
	
	@Override
	public boolean equals(Object _credit) {

		if (_credit instanceof Credit) {
			Credit credit = (Credit) _credit;
			if (
					this.duree == credit.duree &&
					this.montantApport == credit.montantApport &&
					this.montantProjet == credit.montantProjet &&
					this.taux == credit.taux
					) {

				return true;
				
			} else {
				
				return false;
				
			}
				
		} else {
			
			return false;
			
		}
		
	}
	
	
}
