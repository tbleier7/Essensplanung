package com.tbleier.essensplanung.einkaufsliste.application.ports.out;

import com.tbleier.essensplanung.einkaufsliste.application.domain.Kategorie;

import java.util.List;
import java.util.Optional;

public interface KategorieRepository {
    long save(Kategorie kategorie);
    Kategorie findByName(String kategorieName);

    Optional<Kategorie> findById(long id);

    List<Kategorie> findAll();

    void delete(long id);
}
