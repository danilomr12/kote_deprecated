package br.com.analise.domain;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Document
public class RespostaCompra implements Serializable {

    private static final long serialVersionUID = 36296435394301790L;

    @Id
    public String id;
    @NotBlank
    public String nomeRepresentante;
    @NotNull
    public int estadoResposta;
    public String nomeFantasiaEmpresa;
    public List<String> telefones;
    @NotNull
    public Long idRepresentante;
    @NotNull
    public Long idResposta;
    @NotNull
    public String emailRepresentante;
}
