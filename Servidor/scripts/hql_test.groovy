def sessionFactory = ctx.sessionFactory
def session = sessionFactory.getCurrentSession()


def query = session.createQuery("from RespostaContext")
def results = query.list()
