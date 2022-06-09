package br.com.cotecom.domain.usuarios

import org.apache.commons.lang.builder.HashCodeBuilder

class UsuarioSupervisor implements Serializable {

	Usuario usuario
	Usuario supervisor

    static mapping = {
		version false
	}

	boolean equals(other) {
		if (!(other instanceof UsuarioSupervisor)) {
			return false
		}

		other.usuario?.id == usuario?.id &&
			other.supervisor?.id == supervisor?.id
	}

	int hashCode() {
		def builder = new HashCodeBuilder()
		if (usuario) builder.append(usuario.id)
		if (supervisor) builder.append(supervisor.id)
		builder.toHashCode()
	}

	static UsuarioSupervisor get(long usuarioId, long supervisorId) {
		UsuarioSupervisor.find 'from UsuarioSupervisor where usuario.id=:usuarioId and supervisor.id=:supervidorId',
                [usuarioId: usuarioId, supervisorId: supervisorId]
    }

	static UsuarioSupervisor create(Usuario usuario, Usuario supervisor, boolean flush = false) {
		new UsuarioSupervisor(usuario: usuario, supervisor: supervisor).save(flush: flush, insert: true)
	}

	static boolean remove(Usuario usuario, Usuario supervisor, boolean flush = false) {
		UsuarioSupervisor instance = UsuarioSupervisor.findByUsuarioAndSupervisor(usuario, supervisor)
		instance ? instance.delete(flush: flush) : false
	}

	static void removeAllByUsuario(Usuario usuario) {
		executeUpdate 'DELETE FROM UsuarioSupervisor WHERE usuario=:usuario', [usuario: usuario]
	}

    static void removeAllBySupervisor(Usuario supervisor) {
		executeUpdate 'DELETE FROM UsuarioSupervisor WHERE supervisor=:supervisor', [supervisor: supervisor]
	}
}
