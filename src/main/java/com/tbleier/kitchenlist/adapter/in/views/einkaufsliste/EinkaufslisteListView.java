package com.tbleier.kitchenlist.adapter.in.views.einkaufsliste;

import com.tbleier.kitchenlist.adapter.in.views.MainLayout;
import com.tbleier.kitchenlist.application.ports.ZutatDTO;
import com.tbleier.kitchenlist.application.ports.in.CommandService;
import com.tbleier.kitchenlist.application.ports.in.QueryService;
import com.tbleier.kitchenlist.application.ports.in.commands.MoveZutatCommand;
import com.tbleier.kitchenlist.application.ports.in.commands.RemoveZutatCommand;
import com.tbleier.kitchenlist.application.ports.in.queries.ListZutatenQuery;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.grid.dnd.GridDropLocation;
import com.vaadin.flow.component.grid.dnd.GridDropMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@PageTitle("Einkaufsliste")
@Route(value = "", layout = MainLayout.class)
public class EinkaufslisteListView extends VerticalLayout {

    private final QueryService<ListZutatenQuery, List<ZutatDTO>> einkaufsListeQueryService;
    private final AddArtikelDialogFactory addArtikelDialogFactory;
    private final CommandService<RemoveZutatCommand> removeZutatCommandService;
    private final CommandService<MoveZutatCommand> moveZutatCommandService;
    Grid<ZutatDTO> grid = new Grid<>(ZutatDTO.class, false);

    private List<ZutatDTO> zutatenDTOs = new ArrayList<>();
    Button addArtikelButton;

    ZutatDTO draggedItem;

    @Autowired
    public EinkaufslisteListView(QueryService<ListZutatenQuery, List<ZutatDTO>> einkaufsListeQueryService,
                                 AddArtikelDialogFactory addArtikelDialogFactory,
                                 CommandService<RemoveZutatCommand> removeZutatCommandService,
                                 CommandService<MoveZutatCommand> moveZutatCommandService) {
        this.einkaufsListeQueryService = einkaufsListeQueryService;
        this.addArtikelDialogFactory = addArtikelDialogFactory;
        this.removeZutatCommandService = removeZutatCommandService;
        this.moveZutatCommandService = moveZutatCommandService;

        addClassName("einkaufsliste-list-view");
        setSizeFull();
        configureGrid();

        add(getToolbar(), grid);

    }

    private Component getToolbar() {

        addArtikelButton = new Button("Eintrag hinzufügen");
        addArtikelButton.addClickListener(event -> openDialog());

        HorizontalLayout toolbar = new HorizontalLayout(addArtikelButton);
        toolbar.addClassName("einkaufsliste-toolbar");

        return toolbar;
    }

    private void openDialog() {

        var dialog = addArtikelDialogFactory.CreateDialog();

        dialog.addListener(AddZutatEvent.class, e -> {
            addEinkaufslistenposition(e.getModel());
        });

        dialog.open();
    }

    private void configureGrid() {
        grid.addClassName("artikel-grid");
        grid.setSizeFull();

        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        ((GridMultiSelectionModel<?>) grid.getSelectionModel())
                .setSelectAllCheckboxVisibility(
                        GridMultiSelectionModel.SelectAllCheckboxVisibility.HIDDEN
                );

        grid.addSelectionListener(selection -> {
            if(selection.getFirstSelectedItem().isPresent()) {
                removeZutat(selection.getFirstSelectedItem().get());
            }
        });

        grid.addColumn(ZutatDTO::getArtikelName).setHeader("Artikel");

        grid.addComponentColumn(item -> {
            var decrementAmountButton = new Button("-");
            return decrementAmountButton;
        });

        var mengeColumn = grid.addColumn(ZutatDTO::getMenge).setHeader("Menge");
        mengeColumn.setTextAlign(ColumnTextAlign.CENTER);

        grid.addComponentColumn(item -> {
            var incrementAmountButton = new Button("+");
            return incrementAmountButton;
        });

        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.setRowsDraggable(true);
        grid.addDragStartListener(
                event -> {
                    // store current dragged item so we know what to drop
                    draggedItem = event.getDraggedItems().get(0);
                    grid.setDropMode(GridDropMode.BETWEEN);
                }
        );

        grid.addDragEndListener(
                event -> {
                    draggedItem = null;
                    // Once dragging has ended, disable drop mode so that
                    // it won't look like other dragged items can be dropped
                    grid.setDropMode(null);
                }
        );

        grid.addDropListener(
                event -> {
                    ZutatDTO dropOverItem = event.getDropTargetItem().get();
                    if (!dropOverItem.equals(draggedItem)) {
                        // reorder dragged item the backing gridItems container
                        zutatenDTOs.remove(draggedItem);
                        // calculate drop index based on the dropOverItem
                        int dropIndex = zutatenDTOs.indexOf(dropOverItem) + (event.getDropLocation() == GridDropLocation.BELOW ? 1 : 0);
                        zutatenDTOs.add(dropIndex, draggedItem);
                        moveZutatCommandService.execute(new MoveZutatCommand(draggedItem.getId(), dropIndex));
                        grid.getDataProvider().refreshAll();
                    }
                }
        );

        zutatenDTOs = new ArrayList<>(einkaufsListeQueryService.execute(new ListZutatenQuery()));
        grid.setItems(zutatenDTOs);
    }

    public List<ZutatDTO> getZutatDTOs() {
        return zutatenDTOs;
    }

    public void addEinkaufslistenposition(ZutatDTO listenposition) {
        zutatenDTOs.add(listenposition);
        grid.setItems(zutatenDTOs);
    }

    public void removeZutat(ZutatDTO zutatDTO) {
        var result = removeZutatCommandService.execute(new RemoveZutatCommand(zutatDTO.getId()));

        if(result.isSuccessful()) {
            zutatenDTOs.remove(zutatDTO);
            grid.setItems(zutatenDTOs);
        }
    }
}

