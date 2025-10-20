package br.ufal.ic.p2.wepayu.commands;

public interface CommandManagerInterface {

    void executar(Command command);

    void undo();

    void redo();
}