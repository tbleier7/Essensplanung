package com.tbleier.kitchenlist.adapter.in.views;

import com.tbleier.kitchenlist.application.domain.Artikel;

public class DeleteArtikelEvent extends ArtikelFormEvent {
    public DeleteArtikelEvent(ArtikelForm source, Artikel artikel) {
        super(source, artikel);
    }
}