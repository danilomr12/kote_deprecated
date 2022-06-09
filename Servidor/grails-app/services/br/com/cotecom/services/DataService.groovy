package br.com.cotecom.services

import org.apache.log4j.Logger
import org.hibernate.SessionFactory

class DataService {

    private static final log = Logger.getLogger(DataService.class)
    boolean transactional = true

    SessionFactory sessionFactory

    def refreshSession() {
        sessionFactory.getCurrentSession().flush()
        sessionFactory.getCurrentSession().clear()
    }
}
