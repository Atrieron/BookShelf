package main.java.dao;

import java.util.List;

import main.java.model.Book;

public interface BookDao {
	int getItemCount(Filter filter);
	List<Book> getBooks(int pageStart, int pageLength, Filter filter);
	void addBook(Book book);
	void removeBook(int id);
	void updateBook(Book book);
	Book getBookById(int id);
	void makeBookRead(int id);
}