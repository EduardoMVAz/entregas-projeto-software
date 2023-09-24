package com.insper.partida.equipe.exception;

public class TeamDoesNotExistException extends RuntimeException {

    public  TeamDoesNotExistException() {
        super("Time não existe!");
    }

}