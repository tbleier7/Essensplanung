package com.tbleier.essensplanung.einkaufsliste.application.ports;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tbleier.essensplanung.einkaufsliste.application.domain.Artikel;
import com.tbleier.essensplanung.einkaufsliste.application.domain.Einheit;
import com.tbleier.essensplanung.einkaufsliste.application.domain.Kategorie;
import com.tbleier.essensplanung.einkaufsliste.application.domain.einkaufsliste.Einkaufsliste;
import com.tbleier.essensplanung.einkaufsliste.application.ports.MoveZutatService;
import com.tbleier.essensplanung.einkaufsliste.application.ports.in.commands.MoveZutatCommand;
import com.tbleier.essensplanung.einkaufsliste.application.ports.out.EinkaufslisteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MoveZutatServiceTest {

    @Mock
    private EinkaufslisteRepository einkaufslisteRepository;

    private MoveZutatService testee;
    
    @BeforeEach
    public void setUp() {
        testee = new MoveZutatService(einkaufslisteRepository);
    }
    
    @Test
    public void should_instruct_einkaufsliste_and_save_it() {
        //Arrange
        var einkaufsliste = Einkaufsliste.CreateEmpty();
        var firstZutat = einkaufsliste.addZutat(new Artikel(12L, "FirstArtikel", Einheit.Stueck, new Kategorie(33L, "SomeKategorie")), 2);
        var secondZutat = einkaufsliste.addZutat(new Artikel(32L, "SecondArtikel", Einheit.Stueck, new Kategorie(33L, "SomeKategorie")), 1);

        givenEinkaufsliste(einkaufsliste);

        //Act
        testee.execute(new MoveZutatCommand(firstZutat.getId(), secondZutat.getEinkaufslisteIndex()));
    
        //Assert
        verify(einkaufslisteRepository).save(einkaufsliste);
    }

    private void givenEinkaufsliste(Einkaufsliste einkaufsliste) {
        when(einkaufslisteRepository.getEinkaufsliste()).thenReturn(einkaufsliste);
    }
}