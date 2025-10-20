package br.ufal.ic.p2.wepayu.services;


public interface PersistenciaService {
    void salvarSistema();

    void carregarSistema();

    void zerarSistema();

    void encerrarSistema();
}