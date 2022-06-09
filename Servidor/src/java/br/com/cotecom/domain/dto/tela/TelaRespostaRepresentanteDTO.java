package br.com.cotecom.domain.dto.tela;

import br.com.cotecom.domain.dto.cotacao.CotacaoDTO;
import br.com.cotecom.domain.dto.resposta.ItemRespostaDTO;
import br.com.cotecom.domain.dto.resposta.RespostaDTO;
import br.com.cotecom.domain.dto.usuario.UsuarioDTO;

import java.util.List;


public class TelaRespostaRepresentanteDTO {

    public CotacaoDTO cotacao;
    public RespostaDTO resposta;
    public List<ItemRespostaDTO> itensResposta;
    public UsuarioDTO representante;
    public UsuarioDTO comprador;

}
