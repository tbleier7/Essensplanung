package com.tbleier.kitchenlist.application;

import com.tbleier.kitchenlist.application.domain.Kategorie;
import com.tbleier.kitchenlist.application.ports.in.CommandResult;
import com.tbleier.kitchenlist.application.ports.in.CommandService;
import com.tbleier.kitchenlist.application.ports.in.commands.SaveKategorieCommand;
import com.tbleier.kitchenlist.application.ports.out.KategorieRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class SaveKategorieService implements CommandService<SaveKategorieCommand> {

    private final KategorieRepository kategorieRepository;

    @Autowired
    public SaveKategorieService(KategorieRepository kategorieRepository) {
        this.kategorieRepository = kategorieRepository;
    }

    @Override
    public CommandResult execute(SaveKategorieCommand command) {

        Kategorie kategorie;
        var kategorieOptional = kategorieRepository.findById(command.getId());

        if(kategorieOptional.isEmpty())
            kategorie = new Kategorie(0, command.getName());
        else
            kategorie = kategorieOptional.get();
            kategorie.rename(command.getName());

        kategorieRepository.save(kategorie);
        return new CommandResult(true);
    }
}
