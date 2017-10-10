package main.java.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="book")
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	@Column(name="title")
	private String title;
	@Column(name="description")
	private String description;
	@Column(name="author")
	private String author;
	@Column(name="isbn")
	private String isbn;
	@Column(name="printYear")
	private int printYear;
	@Column(name="readAlready")
	private boolean readAlready;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public int getPrintYear() {
		return printYear;
	}
	public void setPrintYear(int printYear) {
		this.printYear = printYear;
	}
	public boolean isReadAlready() {
		return readAlready;
	}
	public void setReadAlready(boolean readAlready) {
		this.readAlready = readAlready;
	}
	public Book() {
	}
	@Override
	public String toString() {
		return "Book [id=" + id + ", title=" + title + ", author=" + author + ", isbn=" + isbn + "]";
	}
	public boolean isPersisted()
	{
		return (id>0);
	}
}