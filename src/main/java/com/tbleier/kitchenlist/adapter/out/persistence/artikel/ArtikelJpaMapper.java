package com.tbleier.kitchenlist.adapter.out.persistence.artikel;

import com.tbleier.kitchenlist.adapter.in.views.artikel.ArtikelModelMapper;
import com.tbleier.kitchenlist.adapter.out.persistence.kategorie.KategorieJpaEntity;
import com.tbleier.kitchenlist.adapter.out.persistence.kategorie.KategorieJpaMapper;
import com.tbleier.kitchenlist.application.domain.Artikel;
import com.tbleier.kitchenlist.application.domain.Kategorie;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring",
injectionStrategy = InjectionStrategy.CONSTRUCTOR,
uses = KategorieJpaMapper.class)
public interface ArtikelJpaMapper {
    ArtikelJpaMapper INSTANCE = Mappers.getMapper(ArtikelJpaMapper.class );

    ArtikelJpaEntity artikelToJpaEntity(Artikel kategorie);
    Artikel jpaEntityToArtikel(ArtikelJpaEntity jpaEntity);

    List<Artikel> jpaEntityToArtikel(List<ArtikelJpaEntity> jpaEntity);
}
