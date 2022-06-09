package br.com.cotecom.domain.dto.usuario;

import java.util.ArrayList;

public class UsuarioDTO {

    public Long id;

    public String nome;
    public String email;
    public String password;
    public int tipo;
    
    public EmpresaDTO empresa;
    public UsuarioDTO supervisor;
    public ArrayList<TelefoneDTO> telefones;

    public boolean isSwitched;
}
