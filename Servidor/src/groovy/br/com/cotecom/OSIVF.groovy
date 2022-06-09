package br.com.cotecom

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.springframework.orm.hibernate3.SessionFactoryUtils
import org.springframework.orm.hibernate3.SessionHolder
import org.springframework.orm.hibernate3.support.OpenSessionInViewFilter
import org.springframework.orm.hibernate3.support.OpenSessionInViewInterceptor
import org.springframework.transaction.support.TransactionSynchronizationManager

/*
 * Copyright 2002-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Servlet 2.3 Filter that binds a Hibernate Session to the thread for the entire
 * processing of the request. Intended for the "Open Session in View" pattern,
 * i.e. to allow for lazy loading in web views despite the original transactions
 * already being completed.
 *
 * <p>This filter makes Hibernate Sessions available via the current thread, which
 * will be autodetected by transaction managers. It is suitable for service layer
 * transactions via {@link org.springframework.orm.hibernate3.HibernateTransactionManager}
 * or {@link org.springframework.transaction.jta.JtaTransactionManager} as well
 * as for non-transactional execution (if configured appropriately).
 *
 * <p>NOTE: This filter will by default not flush the Hibernate Session,
 * with the flush mode set to <code>FlushMode.NEVER. It assumes to be used
 * in combination with service layer transactions that care for the flushing: The
 * active transaction manager will temporarily change the flush mode to
 * <code>FlushMode.AUTO during a read-write transaction, with the flush
 * mode reset to <code>FlushMode.NEVER at the end of each transaction.
 * If you intend to use this filter without transactions, consider changing
 * the default flush mode (through the "flushMode" property).
 *
 * <p>WARNING: Applying this filter to existing logic can cause issues that
 * have not appeared before, through the use of a single Hibernate Session for the
 * processing of an entire request. In particular, the reassociation of persistent
 * objects with a Hibernate Session has to occur at the very beginning of request
 * processing, to avoid clashes with already loaded instances of the same objects.
 *
 * <p>Alternatively, turn this filter into deferred close mode, by specifying
 * "singleSession"="false": It will not use a single session per request then,
 * but rather let each data access operation or transaction use its own session
 * (like without Open Session in View). Each of those sessions will be registered
 * for deferred close, though, actually processed at request completion.
 *
 * <p>A single session per request allows for most efficient first-level caching,
 * but can cause side effects, for example on <code>saveOrUpdate or when
 * continuing after a rolled-back transaction. The deferred close strategy is as safe
 * as no Open Session in View in that respect, while still allowing for lazy loading
 * in views (but not providing a first-level cache for the entire request).
 *
 * <p>Looks up the SessionFactory in Spring's root web application context.
 * Supports a "sessionFactoryBeanName" filter init-param in <code>web.xml;
 * the default bean name is "sessionFactory". Looks up the SessionFactory on each
 * request, to avoid initialization order issues (when using ContextLoaderServlet,
 * the root application context will get initialized <i>after this filter).
 *
 * @author Juergen Hoeller
 * @since 1.2
 * @see # setSingleSession
 * @see # setFlushMode
 * @see # lookupSessionFactory
 * @see OpenSessionInViewInterceptor
 * @see org.springframework.orm.hibernate3.HibernateInterceptor
 * @see org.springframework.orm.hibernate3.HibernateTransactionManager
 * @see org.springframework.orm.hibernate3.SessionFactoryUtils#getSession
 * @see org.springframework.transaction.support.TransactionSynchronizationManager
 */
public class OSIVF extends OpenSessionInViewFilter {


	protected void doFilterInternal(
			HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		SessionFactory sessionFactory = lookupSessionFactory(request);
		boolean participate = false;

		if (isSingleSession()) {
			// single session mode
			if (TransactionSynchronizationManager.hasResource(sessionFactory)) {
				// Do not modify the Session: just set the participate flag.
				participate = true;
			}
			else {
				logger.debug("Opening single Hibernate Session in OpenSessionInViewFilter");
				Session session = getSession(sessionFactory);
				TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
			}
		}
		else {
			// deferred close mode
			if (SessionFactoryUtils.isDeferredCloseActive(sessionFactory)) {
				// Do not modify deferred close: just set the participate flag.
				participate = true;
			}
			else {
				SessionFactoryUtils.initDeferredClose(sessionFactory);
			}
		}

		try {
			filterChain.doFilter(request, response);
		}

		finally {
			if (!participate) {
				if (isSingleSession()) {
					// single session mode
                    SessionHolder sessionHolder
					if(TransactionSynchronizationManager.hasResource(sessionFactory)){
                        logger.debug "Unbinding resource of the sessionFactory: $sessionFactory"
                        sessionHolder =
							(SessionHolder) TransactionSynchronizationManager.unbindResource(sessionFactory);
                    }else{
                        Session session = getSession(sessionFactory);
                        sessionHolder = new SessionHolder(session)
                        logger.debug "Instantiating sessionHolder: $sessionHolder"
                    }

                    logger.debug("Closing single Hibernate Session in OpenSessionInViewFilter");
					closeSession(sessionHolder.getSession(), sessionFactory);
				}
				else {
					// deferred close mode
					SessionFactoryUtils.processDeferredClose(sessionFactory);
				}
			}
		}
	}

}