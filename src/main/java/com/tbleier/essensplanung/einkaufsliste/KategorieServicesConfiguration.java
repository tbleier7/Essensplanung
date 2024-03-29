package com.tbleier.essensplanung.einkaufsliste;

import com.tbleier.essensplanung.einkaufsliste.adapter.in.views.interceptors.CommandExceptionHandler;
import com.tbleier.essensplanung.einkaufsliste.application.ports.KategorieDTO;
import com.tbleier.essensplanung.einkaufsliste.adapter.in.views.kategorie.KategorieModelMapper;
import com.tbleier.essensplanung.einkaufsliste.application.DeleteKategorieService;
import com.tbleier.essensplanung.einkaufsliste.application.FindAllKategorienService;
import com.tbleier.essensplanung.einkaufsliste.application.SaveKategorieService;
import com.tbleier.essensplanung.einkaufsliste.application.ports.in.CommandService;
import com.tbleier.essensplanung.einkaufsliste.application.ports.in.QueryService;
import com.tbleier.essensplanung.einkaufsliste.application.ports.in.commands.SaveKategorieCommand;
import com.tbleier.essensplanung.einkaufsliste.application.ports.in.queries.ListKategorienQuery;
import com.tbleier.essensplanung.einkaufsliste.application.ports.in.commands.DeleteKategorieCommand;
import com.tbleier.essensplanung.einkaufsliste.application.ports.out.KategorieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class KategorieServicesConfiguration {

    private final KategorieRepository kategorieRepository;
    private final KategorieModelMapper kategorieModelMapper;

    @Autowired
    public KategorieServicesConfiguration(KategorieRepository kategorieRepository, KategorieModelMapper kategorieModelMapper) {
        this.kategorieRepository = kategorieRepository;
        this.kategorieModelMapper = kategorieModelMapper;
    }

    @Bean
    CommandService<SaveKategorieCommand> resolveSaveKategorieCommandService() {
        return new CommandExceptionHandler<>(new SaveKategorieService(kategorieRepository));
    }

    @Bean
    CommandService<DeleteKategorieCommand> resolveDeleteKategorieCommandService() {
        return new CommandExceptionHandler<>(new DeleteKategorieService(kategorieRepository));
    }

    @Bean
    QueryService<ListKategorienQuery, List<KategorieDTO>> resolveListKategorienQueryService() {
        return new FindAllKategorienService(kategorieRepository, kategorieModelMapper);
    }
}
