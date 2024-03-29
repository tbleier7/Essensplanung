package com.tbleier.essensplanung.einkaufsliste.application.domain.einkaufsliste;

import com.tbleier.essensplanung.einkaufsliste.application.domain.Artikel;
import com.tbleier.essensplanung.einkaufsliste.application.ports.out.NotFoundException;

import java.util.*;

public class Einkaufsliste {

    private List<Zutat> zutaten = new LinkedList<>();

    Einkaufsliste(List<Zutat> zutaten) {
        this.zutaten = zutaten;
    }

    Einkaufsliste() {
    }

    public Zutat addZutat(Artikel artikel, int menge) {
        var highestIndex = zutaten.size();

        var newZutat = Zutat.CreateWithoutId(artikel, menge, highestIndex);
        zutaten.add(newZutat);
        return newZutat;
    }

    public Zutat addExistingZutat(long id, Artikel artikel, int menge) {
        var highestIndex = zutaten.size();

        var newZutat = Zutat.CreateWithId(id, artikel, menge, highestIndex);
        zutaten.add(newZutat);
        return newZutat;
    }

    public List<Zutat> getZutaten() {
        return zutaten;
    }

    public void moveZutatToIndex(long zutatId, int index) {
        var zutatToMove= zutaten.stream().filter(zutat -> zutat.getId() == zutatId).findFirst();
        zutaten.remove(zutatToMove.get());
        zutaten.add(index, zutatToMove.get());

        for (int i = 0; i < zutaten.size(); i++) {
            zutaten.get(i).moveToIndex(i);
        }
    }

    public static Einkaufsliste CreateWithZutaten(List<Zutat> zutaten) {

        zutaten = new LinkedList<>(zutaten);
        return new Einkaufsliste(zutaten);
    }

    public static Einkaufsliste CreateEmpty() {
        return new Einkaufsliste(new LinkedList<>());
    }

    public Optional<Zutat> getZutat(long zutatId) {
        return zutaten.stream().filter(zutat -> zutat.getId() == zutatId).findFirst();
    }

    public Zutat incrementZutat(long zutatId) {
        var zutatOptional = getZutat(zutatId);

        if(zutatOptional.isPresent()) {
            var zutat = zutatOptional.get();
            zutat.increment();
            return zutat;
        }
        else throw new NotFoundException();
    }

    public Zutat decrementZutat(long zutatId) {
        var zutatOptional = getZutat(zutatId);

        if(zutatOptional.isPresent()) {
            var zutat = zutatOptional.get();
            zutat.decrement();
            return zutat;
        }
        else throw new NotFoundException();
    }
}
