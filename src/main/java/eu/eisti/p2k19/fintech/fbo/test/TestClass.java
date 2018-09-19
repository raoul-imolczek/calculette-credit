package eu.eisti.p2k19.fintech.fbo.test;

import java.time.LocalDate;
import java.time.Month;
import java.util.Iterator;

import eu.eisti.p2k19.fintech.fbo.credit.model.CreditImmobilier;
import eu.eisti.p2k19.fintech.fbo.credit.model.CreditPasRemboursableException;
import eu.eisti.p2k19.fintech.fbo.credit.model.LigneAmortissement;
import eu.eisti.p2k19.fintech.fbo.credit.model.TableauAmortissement;
import eu.eisti.p2k19.fintech.fbo.credit.model.TauxUsureException;

/**
 * Classe d'illustration Hello World
 * 
 * @author Fabian
 *
 */
public class TestClass {

	public static void main(String[] args) {

		CreditImmobilier monCredit1;
		
		try {
			// On itialise un crédit
			monCredit1 = new CreditImmobilier(20_000, 200_000, 1000, 1.5, 1000, 0.05, LocalDate.of(2019, Month.FEBRUARY, 1));

			TableauAmortissement tableau;

			// On récupère le tableau d'amortissement
			tableau = monCredit1.getTableauAmortissement();

			Iterator<LigneAmortissement> iterator = tableau.getLignes().iterator();
			
			// On affiche les lignes dans la console
			while(iterator.hasNext()) {
				LigneAmortissement ligne = iterator.next();
				System.out.println(ligne.getDate() + " " + ligne.getCapitalRestantDu() + " " + ligne.getInterets() + " " + ligne.getAssurance() + " "+ ligne.getMensualite());
			}
		
			// On calcule et affiche le TAEG
			System.out.println("TAEG: " + monCredit1.getTaeg());

		
		} catch (TauxUsureException e1) {
			
			System.out.println("Le crédit dépasse le taux d'usure");
			
		} catch (CreditPasRemboursableException e) {

			System.out.println("Impossible d'afficher le tableau d'amortissement");
			
		}
				
	}

}
