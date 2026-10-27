package com.aula.pos.appinscricao.service;

import com.aula.pos.appinscricao.dto.InscricaoRequest;
import com.aula.pos.appinscricao.dto.InscricaoResponse;

public interface InscricaoService {
    InscricaoResponse criar(InscricaoRequest request);
}
