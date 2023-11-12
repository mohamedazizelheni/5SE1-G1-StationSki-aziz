package com.example.stationski.repositories;

import com.example.stationski.entities.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class InscriptionRepositoryTest {

    @Autowired
    private InscriptionRepository inscriptionRepository;
    @Autowired
    private SkieurRepository skieurRepository;
    @Autowired
    private CoursRepository coursRepository;
    @Autowired
    private MoniteurRepository moniteurRepository;

    @Test
    public void testFindByTypeAbonnement() {
        // Create test data
        Skieur skieur1 = new Skieur();
        skieur1.setAbonnement(new Abonnement());
        skieur1.getAbonnement().setTypeAbon(TypeAbonnement.ANNUEL);
        skieurRepository.save(skieur1);

        Inscription inscription1 = new Inscription();
        inscription1.setSkieur(skieur1);
        inscriptionRepository.save(inscription1);

        Skieur skieur2 = new Skieur();
        skieur2.setAbonnement(new Abonnement());
        skieur2.getAbonnement().setTypeAbon(TypeAbonnement.MENSUEL);
        skieurRepository.save(skieur2);

        Inscription inscription2 = new Inscription();
        inscription2.setSkieur(skieur2);
        inscriptionRepository.save(inscription2);

        // Call the method under test
        Set<Inscription> result = inscriptionRepository.findByTypeAbonnement(TypeAbonnement.ANNUEL);

        // Assert the results
        assertEquals(1, result.size());
        assertEquals(new HashSet<>(Set.of(inscription1)), result);
    }

    @Test
    public void testFindByNumInscription() {
        // Create test data
        Inscription inscription1 = new Inscription();
        inscription1.setNumInscription(123L);
        inscriptionRepository.save(inscription1);

        Inscription inscription2 = new Inscription();
        inscription2.setNumInscription(456L);
        inscriptionRepository.save(inscription2);

        // Call the method under test
        Inscription result = inscriptionRepository.findByNumInscription(123L);

        // Assert the result
        assertEquals(inscription1, result);
    }

    @Test
    public void testNumWeeksCoursOfMoniteurBySupport() {
        // Create a Cours associated with the Moniteur
        Cours cours = new Cours();
        cours.setNumCours(1L);
        cours.setTypeCours(TypeCours.COLLECTIF_ENFANT);
        cours.setSupport(Support.SKI);
        cours.setPrix(50.0f);
        cours.setCreneau(1);
        cours.setNiveau(1);

        coursRepository.save(cours);
        // Create a Moniteur
        Moniteur moniteur = new Moniteur();
        moniteur.setNumMoniteur(1L);
        moniteur.setNomM("Moniteur Name");
        moniteur.setPrenomM("Moniteur LastName");
        moniteur.setDateRecru(LocalDate.now());
        moniteur.setPrime(1000.0f);

        // Create a Set of Cours and associate it with the Moniteur
        Set<Cours> coursSet = new HashSet<>();
        coursSet.add(cours);
        moniteur.setCoursSet(coursSet);

        moniteurRepository.save(moniteur);
        // Create an Inscription associated with the Cours
        Inscription inscription = new Inscription();
        inscription.setNumInscription(123L);
        inscription.setNumSemaine(5);
        inscription.setCours(cours);
        inscriptionRepository.save(inscription);
        // Call the repository method
        List<Integer> result = inscriptionRepository.numWeeksCoursOfMoniteurBySupport(1L, Support.SKI);

        // Assert the result
        assertEquals(1, result.size());
        assertEquals(5, result.get(0));
    }


}