package com.myimage.dao.utils;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.hibernate.SessionFactory;

public class ConnectionFilter implements Filter {

	private SessionFactory sessionFactory;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.sessionFactory = HibernateUtil.getSessionFactory();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		try {
			this.sessionFactory.getCurrentSession().beginTransaction();
			chain.doFilter(request, response);
			this.sessionFactory.getCurrentSession().getTransaction().commit();
			this.sessionFactory.getCurrentSession().close();
		} catch (Throwable e) {
			try {
				if (this.sessionFactory.getCurrentSession().getTransaction().isActive()) {
					this.sessionFactory.getCurrentSession().getTransaction().rollback();
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	@Override
	public void destroy() {

	}

}
