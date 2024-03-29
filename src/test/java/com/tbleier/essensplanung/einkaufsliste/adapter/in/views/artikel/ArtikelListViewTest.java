package com.tbleier.essensplanung.einkaufsliste.adapter.in.views.artikel;

import com.tbleier.essensplanung.einkaufsliste.application.ports.ArtikelDTO;
import com.tbleier.essensplanung.einkaufsliste.application.ports.KategorieDTO;
import com.tbleier.essensplanung.einkaufsliste.application.domain.Einheit;
import com.tbleier.essensplanung.einkaufsliste.application.ports.in.CommandService;
import com.tbleier.essensplanung.einkaufsliste.application.ports.in.QueryService;
import com.tbleier.essensplanung.einkaufsliste.application.ports.in.commands.DeleteArtikelCommand;
import com.tbleier.essensplanung.einkaufsliste.application.ports.in.commands.SaveArtikelCommand;
import com.tbleier.essensplanung.einkaufsliste.application.ports.in.queries.ListKategorienQuery;
import com.tbleier.essensplanung.einkaufsliste.application.ports.in.queries.ListArtikelQuery;
import com.vaadin.flow.data.provider.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArtikelListViewTest {

    @Mock
    private CommandService<SaveArtikelCommand> saveArtikelCommandService;

    @Mock
    private CommandService<DeleteArtikelCommand> deleteArtikelCommandService;

    @Mock
    private QueryService<ListArtikelQuery, List<ArtikelDTO>> listAllArtikelQueryService;

    @Mock
    private QueryService<ListKategorienQuery, List<KategorieDTO>> listAllKategorienQueryService;

    private ArtikelListView testee;
    private List<ArtikelDTO> expectedArtikel;

    @BeforeEach
    public void setUp() {
        var mapper = ArtikelDTOMapper.INSTANCE;
        var factory = new ArtikelFormFactory(saveArtikelCommandService, listAllKategorienQueryService, deleteArtikelCommandService);

        givenTwoArtikel();
        givenAKategorie();
        testee = new ArtikelListView(factory, listAllArtikelQueryService, mapper);
    }

    private void givenAKategorie() {
        when(listAllKategorienQueryService.execute(any())).thenReturn(List.of(new KategorieDTO(0, "someKategorie")));
    }

    @Test
    public void should_hide_editing_form_when_initialized() {
        //Assert
        assertEquals(false,testee.artikelForm.isVisible());
    }
    
    @Test
    public void should_open_editing_form_when_adding_a_new_artikel() {
        //Act
        testee.addRezeptButton.click();

        //Assert
        assertEquals(true,testee.artikelForm.isVisible());
    }
    
    @Test
    public void should_show_all_artikel() {
        //Act
        var actual = testee.grid.getDataProvider().fetch(new Query<>()).collect(Collectors.toList());
    
        //Assert
        assertEquals(2, actual.size());
    }
    
    @Test
    public void should_add_artikel_when_new_artikel_was_saved() {
        //Arrange
        var artikelModel = new ArtikelDTO(1, "newcomer", Einheit.Stueck, "Gemüse");
        //Act
        testee.addArtikel(artikelModel);
    
        //Assert
        assertThat(testee.getArtikelDTOs(), hasItem(artikelModel));
    }

    @Test
    public void should_open_selected_artikel_in_editor() {
        //Arrange
        var artikelModel = new ArtikelDTO(1, "test", Einheit.Stueck, "Gemüse");

        //Act
        testee.grid.select(artikelModel);

        //Assert
        assertEquals(true,testee.artikelForm.isVisible());
        assertEquals(artikelModel.getName(), testee.artikelForm.name.getValue());
    }

    @Test
    public void should_remove_artikel_when_deleted() {
        //Arrange
        var artikelModel = new ArtikelDTO(1, "test", Einheit.Stueck, "Gemüse");

        //Act
        testee.removeArtikel(artikelModel);

        //Assert
        assertThat(testee.getArtikelDTOs(), not(hasItem(artikelModel)));
    }

    @Test
    public void should_leave_editing_mode_when_unselecting_an_item() {
        //Arrange
        testee.grid.select(new ArtikelDTO());

        //Act
        testee.grid.deselectAll();

        //Assert
        assertThatEditingIs(false);
    }

    private void givenTwoArtikel() {
        expectedArtikel = List.of(
                new ArtikelDTO(1,"Zwiebeln", Einheit.Stueck, "Gemüse"),
                new ArtikelDTO(2, "Schinken", Einheit.Stueck, "Gemüse"));

        when(listAllArtikelQueryService.execute(any())).thenReturn(expectedArtikel);
    }

    private void assertThatEditingIs(boolean editing) {
        var classnames = testee.getClassNames();
        var editingMode = "editing";

        if (editing)
            assertThat(classnames, hasItem(editingMode));
        else
            assertThat(classnames, not(hasItem(editingMode)));
    }
}