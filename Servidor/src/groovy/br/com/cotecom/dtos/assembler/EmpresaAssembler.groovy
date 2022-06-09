package br.com.cotecom.dtos.assembler

import br.com.cotecom.domain.dto.usuario.AtendimentoDTO
import br.com.cotecom.domain.dto.usuario.EmpresaDTO
import br.com.cotecom.domain.dto.usuario.EnderecoDTO
import br.com.cotecom.domain.dto.usuario.TelefoneDTO
import br.com.cotecom.domain.usuarios.Representante
import br.com.cotecom.domain.usuarios.empresa.*

public class EmpresaAssembler {

    static EmpresaDTO crieEmpresaDTO(Empresa empresa){
        if (!empresa) return null

        ArrayList<TelefoneDTO> telefones = TelefoneAssembler.crieTelefonesDTO(empresa.telefones)
        EnderecoDTO enderecoDTO = EnderecoAssembler.crieEnderecoDTO(empresa.endereco)
        EmpresaDTO empresaDTO = new EmpresaDTO(id: empresa.id, email: empresa.email, cnpj: empresa.cnpj,
                razaoSocial: empresa.razaoSocial, nomeFantasia: empresa.nomeFantasia, telefones: telefones,
                endereco: enderecoDTO, tipo: empresa instanceof Cliente ? EmpresaDTO.CLIENTE : EmpresaDTO.FORNECEDOR)
        return empresaDTO
    }

    static Empresa crieEmpresa(EmpresaDTO empresaDTO) {
        Empresa empresa
        if(!empresaDTO) return null

        if(!empresaDTO.id){
            if(empresaDTO.tipo == EmpresaDTO.CLIENTE){
                empresa = new Cliente()
            }else if(empresaDTO.tipo == EmpresaDTO.FORNECEDOR){
                empresa = new Fornecedor()
            }
        }else{
            empresa = Empresa.get(empresaDTO.id)
        }

        if(empresa == null) return null

        empresa.nomeFantasia = empresaDTO.nomeFantasia
        empresa.razaoSocial = empresaDTO.razaoSocial
        empresa.cnpj = empresaDTO.cnpj
        empresa.email = empresaDTO.email

        TelefoneAssembler.crieTelefones(empresaDTO.telefones).each {Telefone telefone ->
            Telefone fone = empresa.telefones.find {
                it.id != null && telefone.id != null && it.id == telefone.id } as Telefone
            if(fone){
                fone.ddd = telefone.ddd
                fone.numero = telefone.numero
                fone.tipo = telefone.tipo
            }else{
                empresa.addTelefone telefone
            }
        }
        def telefonesARemover = new ArrayList()
        empresa.telefones.each {Telefone telefone ->
            TelefoneDTO fone = empresaDTO.telefones.find {TelefoneDTO dto -> dto.id == telefone.id} as TelefoneDTO
            if(!fone)
                telefonesARemover << telefone.id
        }
        telefonesARemover.each { Long tel ->
            empresa.removeFromTelefones Telefone.get(tel)
        }

        empresa.endereco = EnderecoAssembler.crieEndereco(empresaDTO.endereco)

        return empresa
    }

    static List<EmpresaDTO> crieEmpresaDTOs(List<Empresa> empresas){
        empresas.collect {crieEmpresaDTO(it)}
    }

    static Atendimento crieAtendimento(AtendimentoDTO atendimentoDTO){
        Atendimento atendimento

        if(!atendimentoDTO) return null

        if(!atendimentoDTO.id)
            atendimento = new Atendimento()
        else
            atendimento = Atendimento.get(atendimentoDTO.id)

        if(!atendimento) return null

        atendimento.representante = Representante.get(atendimentoDTO.representanteId)
        atendimento.fornecedor = Fornecedor.get(atendimentoDTO.fornecedorId)
        atendimento.cliente = Cliente.get(atendimentoDTO.clienteId)

        return atendimento
    }

    static AtendimentoDTO crieAtendimentoDTO(Atendimento atendimento){
        if(!atendimento) return null

        return new AtendimentoDTO(id: atendimento.id, representanteId: atendimento.representante.id,
                fornecedorId: atendimento.fornecedor.id, clienteId: atendimento.cliente.id)
    }

}