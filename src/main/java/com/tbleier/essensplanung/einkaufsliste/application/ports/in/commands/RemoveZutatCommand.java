package com.tbleier.essensplanung.einkaufsliste.application.ports.in.commands;

public class RemoveZutatCommand {

    private final long zutatId;

    public RemoveZutatCommand(long zutatId) {
        this.zutatId = zutatId;
    }

    public long getZutatId() {
        return zutatId;
    }
}
