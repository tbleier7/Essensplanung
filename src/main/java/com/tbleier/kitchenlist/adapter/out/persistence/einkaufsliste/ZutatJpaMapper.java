package com.tbleier.kitchenlist.adapter.out.persistence.einkaufsliste;

import com.tbleier.kitchenlist.adapter.out.persistence.artikel.ArtikelJpaMapper;
import com.tbleier.kitchenlist.application.domain.einkaufsliste.Zutat;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = ArtikelJpaMapper.class)
public interface ZutatJpaMapper {
    ZutatJpaMapper INSTANCE = Mappers.getMapper(ZutatJpaMapper.class );

    ZutatJpaEntity zutatToJpaEntity(Zutat zutat);
    List<ZutatJpaEntity> zutatToJpaEntity(List<Zutat> zutat);
    Zutat jpaEntityToZutat(ZutatJpaEntity zutatJpaEntity);
    List<Zutat> jpaEntityToZutat(List<ZutatJpaEntity> zutatJpaEntities);
}
