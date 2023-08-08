package com.example.meusgastoscaldas.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.meusgastoscaldas.domain.model.CentroDeCusto;
import com.example.meusgastoscaldas.domain.model.Usuario;

import java.util.List;


public interface CentroDeCustoRepository extends JpaRepository<CentroDeCusto, Long>{
    List<CentroDeCusto> findByUsuario(Usuario usuario);
}
