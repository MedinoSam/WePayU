package br.ufal.ic.p2.wepayu.commands;

import br.ufal.ic.p2.wepayu.Exception.NaoHaComandoDesfazerException;
import java.util.Stack;

public class CommandManager implements CommandManagerInterface {
    private Stack<Command> historico = new Stack<>();
    private Stack<Command> redoStack = new Stack<>();

    @Override
    public void executar(Command command) {
        command.executar();
        historico.push(command);
        redoStack.clear();
    }

    @Override
    public void undo() {
        if (!historico.isEmpty()) {
            Command command = historico.pop();
            command.desfazer();
            redoStack.push(command);
        } else {
            throw new NaoHaComandoDesfazerException("Nao ha comando a desfazer.");
        }
    }

    @Override
    public void redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.executar();
            historico.push(command);
        } else {
            throw new NaoHaComandoDesfazerException("Nao ha comando a refazer.");
        }
    }
}