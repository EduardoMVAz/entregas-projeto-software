package com.insper.partida.tabela;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TabelaService {

    @Autowired
    private TabelaRepository tabelaRepository;

    public Tabela getTabela(String identifier) {
        return tabelaRepository.findByTime(identifier);
    }

    public List<Tabela> getTabelas() {
        return tabelaRepository.findAll();
    }

    public void saveTabela(Tabela tabela) {
        tabelaRepository.save(tabela);
    }   
}
