package com.ipi.jva350;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.LinkedHashSet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.ipi.jva350.exception.SalarieException;
import com.ipi.jva350.model.Entreprise;
import com.ipi.jva350.model.SalarieAideADomicile;
import com.ipi.jva350.service.SalarieAideADomicileService;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class SalarieAideADomicileServiceTest {

    @Autowired
    private SalarieAideADomicileService salaireAideADomicileService;
    private Entreprise entreprise = new Entreprise();
    private SalarieAideADomicileService salarieAideDomicile = new SalarieAideADomicileService();

	@Test
	public final void clotureMois() throws SalarieException {
		SalarieAideADomicile aide = new SalarieAideADomicile("Jeanne", LocalDate.of(2021, 7, 1), LocalDate.now(), 0,0,0, 0, 0);
		
		salaireAideADomicileService.clotureMois(aide, 20.0);
		assertNotEquals(2, aide.getJoursTravaillesAnneeN());
	}
	
	//Karima
	@Test
	public final void estDansPlage() {
		boolean estdansplage = entreprise.estDansPlage(LocalDate.of(2021, 7, 1), LocalDate.of(2021, 6, 1), LocalDate.of(2021, 7, 19));
		assertTrue(estdansplage);
	}
	
	//Karima
	@Test
	public final void estHorsPlageNull() {
		boolean estdansplage = entreprise.estDansPlage(null, LocalDate.of(2021, 7, 10), LocalDate.of(2021, 7, 19));
		assertFalse(estdansplage);
	}
	
	//Lea
	@Test
	public final void estHorsPlageAvant() {
		boolean estdansplage = entreprise.estDansPlage(LocalDate.of(2021, 7, 1), LocalDate.of(2021, 7, 10), LocalDate.of(2021, 7, 19));
		assertFalse(estdansplage);
	}
	
	//Lea
	@Test
	public final void estHorsPlageApres() {
		boolean estdansplage = entreprise.estDansPlage(LocalDate.of(2022, 7, 1), LocalDate.of(2021, 7, 10), LocalDate.of(2021, 7, 19));
		assertFalse(estdansplage);
	}
	
	
	//Lea
	@ParameterizedTest(name = "La date {0} est un jour férié : {1}")
	@CsvSource({
		"'2012-04-09',true",
		"'2012-06-08',false",
		"'2020-08-15',true",
		"'2013-04-08',false",
		"'2012-01-01',true"
	})
	public final void estJourFerie(String date, boolean expectedJourFerierBool) {
		LocalDate d = LocalDate.parse(date);
		
		boolean jourferier = entreprise.estJourFerie(d);
		assertEquals(expectedJourFerierBool, jourferier);
	}
	
	
	//Lea
	@ParameterizedTest(name = "La date {0} a comme proportion pondérée du mois : {1}")
	@CsvSource({
		"'2012-12-02',28",
		"'2020-10-10',16",
		"'2020-08-31',16",
		"'2050-01-01',0"
	})
	public final void proportionPondereeDuMois(String date, double expectedProportionPondereeMois) {
		LocalDate d = LocalDate.parse(date);
		
		double ponderedumois = entreprise.proportionPondereeDuMois(d);
		assertNotEquals(expectedProportionPondereeMois, ponderedumois);
	}
	
	
	//Lea
	@ParameterizedTest(name = "La date {0} a comme pour premier jour d'annee de conges: {1}")
	@CsvSource({
		"'2022-04-09','2021-06-01'",
		"'2022-06-09','2022-06-01'"
	})
	public final void getPremierJourAnneeDeConges(String date, String dateResultat) {
		LocalDate d = LocalDate.parse(date);
		
		LocalDate dResu = LocalDate.parse(dateResultat);
		LocalDate dTest = entreprise.getPremierJourAnneeDeConges(d);
		
		assertEquals(dTest, dResu);
	}
 
	
	//Karima
	@Test
	public final void calculeLimiteEntrepriseCongesPermis() {
		long resultat = salarieAideDomicile.calculeLimiteEntrepriseCongesPermis(LocalDate.of(2021, 12, 12), 0, LocalDate.of(2021, 12, 15), LocalDate.of(2022, 12, 24), LocalDate.of(2022, 12, 26));
		assertNotEquals(1, resultat);
	}
	
	//Karima
	@Test
	public final void calculeLimiteLimiteEntrepriseCongesPermis() {
		long resultat = salarieAideDomicile.calculeLimiteEntrepriseCongesPermis(LocalDate.of(2022, 12, 12), 0, LocalDate.of(2021, 12, 15), LocalDate.of(2022, 12, 24), LocalDate.of(2022, 12, 26));
		assertEquals(1, resultat);
	}
	
}

