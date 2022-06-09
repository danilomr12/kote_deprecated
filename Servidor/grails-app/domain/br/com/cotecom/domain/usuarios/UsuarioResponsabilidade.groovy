package br.com.cotecom.domain.usuarios

import org.apache.commons.lang.builder.HashCodeBuilder

class UsuarioResponsabilidade implements Serializable {

	Usuario usuario
	Responsabilidade responsabilidade

    static mapping = {
		id composite: ['responsabilidade', 'usuario']
		version false
	}

	boolean equals(other) {
		if (!(other instanceof UsuarioResponsabilidade)) {
			return false
		}

		other.usuario?.id == usuario?.id &&
			other.responsabilidade?.id == responsabilidade?.id
	}

	int hashCode() {
		def builder = new HashCodeBuilder()
		if (usuario) builder.append(usuario.id)
		if (responsabilidade) builder.append(responsabilidade.id)
		builder.toHashCode()
	}

	static UsuarioResponsabilidade get(long usuarioId, long responsabilidadeId) {
		UsuarioResponsabilidade.find 'from UsuarioResponsabilidade where usuario.id=:usuarioId and responsabilidade.id=:responsabilidadeId',
			[usuarioId: usuarioId, responsabilidadeId: responsabilidadeId]
	}

	static UsuarioResponsabilidade create(Usuario usuario, Responsabilidade responsabilidade, boolean flush = false) {
		new UsuarioResponsabilidade(usuario: usuario, responsabilidade: responsabilidade).save(flush: flush, insert: true)
	}

	static boolean remove(Usuario usuario, Responsabilidade responsabilidade, boolean flush = false) {
		UsuarioResponsabilidade instance = UsuarioResponsabilidade.findByUsuarioAndResponsabilidade(usuario, responsabilidade)
		instance ? instance.delete(flush: flush) : false
	}

	static void removeAll(Usuario usuario) {
		executeUpdate 'DELETE FROM UsuarioResponsabilidade WHERE usuario=:usuario', [usuario: usuario]
	}

	static void removeAll(Responsabilidade responsabilidade) {
		executeUpdate 'DELETE FROM UsuarioResponsabilidade WHERE responsabilidade=:responsabilidade', [responsabilidade: responsabilidade]
	}
}
