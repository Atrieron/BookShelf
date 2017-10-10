package main.java.dao;

import java.util.List;

import javax.annotation.PostConstruct;

import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;

import main.java.HibernateUtil;
import main.java.model.Book;

@SpringComponent
@UIScope
public class BookDaoHibernate implements BookDao {
	private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();	

	public BookDaoHibernate() {
	}
	
	@PostConstruct
	public void init(){
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Book> getBooks(int pageStart, int pageLength, Filter filter) {
		Session session = sessionFactory.openSession();
		if(pageLength>-1)
		{
			String selectionLine = filter.getSelectionLine();
			Query query = session.createQuery("from Book" + selectionLine);
			query.setFirstResult(pageStart);
			query.setMaxResults(pageLength);
			List<Book> res = query.list();
			session.close();
			return res;
		}
		else
		{
			List<Book> res = session.createQuery("from Book" + filter.getSelectionLine()).list();
			session.close();
			return res;
		}
	}

	@Override
	@Transactional
	public void addBook(Book book) {
		Session session = sessionFactory.openSession();
		Transaction trans = session.beginTransaction();
		session.persist(book);
		trans.commit();
		session.close();
	}

	@Override
	@Transactional
	public void removeBook(int id) {
		Session session = sessionFactory.openSession();
		Book book = (Book)session.load(Book.class, new Integer(id));
		if(book!=null)
		{
			Transaction trans = session.beginTransaction();
			session.delete(book);
			trans.commit();
		}
		session.close();
	}

	@Override
	@Transactional
	public void updateBook(Book book) {
		Session session = sessionFactory.openSession();
		Transaction trans = session.beginTransaction();
		book.setReadAlready(false);
		session.update(book);
		trans.commit();
		session.close();
	}

	@Override
	@Transactional
	public Book getBookById(int id) {
		Session session = sessionFactory.openSession();
		Book book = (Book)session.load(Book.class, new Integer(id));
		String st = book.toString();
		session.close();
		return book;
	}	
	
	@Override
	@Transactional
	public void makeBookRead(int id) {
		Session session = sessionFactory.openSession();
		Book book = (Book)session.load(Book.class, new Integer(id));
		if(book!=null)
		{
			Transaction trans = session.beginTransaction();
			book.setReadAlready(true);
			session.update(book);
			trans.commit();
		}
		session.close();
	}

	@Override
	public int getItemCount(Filter filter) {
		String selectionLine = filter.getSelectionLine();
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from Book" + selectionLine);
		ScrollableResults scrollableResults = query.scroll();
        scrollableResults.last();
        int totalRecords=scrollableResults.getRowNumber()+1;
        scrollableResults.close();
        session.close();
        return totalRecords;
	}
}