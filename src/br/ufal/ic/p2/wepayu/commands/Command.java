package br.ufal.ic.p2.wepayu.commands;

public interface Command {

    void executar();

    void desfazer();
}