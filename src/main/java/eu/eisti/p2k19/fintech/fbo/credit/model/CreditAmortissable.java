package eu.eisti.p2k19.fintech.fbo.credit.model;

public interface CreditAmortissable {

	public TableauAmortissement getTableauAmortissement() throws CreditPasRemboursableException;
	
	public double getTaeg() throws CreditPasRemboursableException;
}
