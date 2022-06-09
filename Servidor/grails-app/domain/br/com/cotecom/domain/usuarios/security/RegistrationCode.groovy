package br.com.cotecom.domain.usuarios.security

class RegistrationCode {

    String username
    String token = UUID.randomUUID().toString().replaceAll('-', '')

    static constraints = {
        username(email: true, blank: false)
        token(blank: false)
    }
}
