package main.java.ui;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.Binder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.data.BindingValidationStatus;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

import main.java.dao.BookDao;
import main.java.model.Book;

@UIScope
@SpringView(name = BookForm.VIEW_NAME)
public class BookForm extends FormLayout  implements View {
	public static final String VIEW_NAME = "BookForm";
	private TextField title = new TextField("Title");
	// private TextField description = new TextField("Description");
	private TextArea description = new TextArea("Description");
	private TextField author = new TextField("Author");
	private TextField isbn = new TextField("ISBN");
	private TextField printYear = new TextField("Print Year");
	private Label errorMessages = new Label();
	private Button save = new Button("Save");
	private Button delete = new Button("Delete");

	@Autowired
	private BookDao bookDao;
	private Book book;
	private BookListUI bookListUI;

	private Binder<Book> binder = new Binder<>(Book.class);

	public BookForm() {
		setSizeUndefined();

		title.setMaxLength(100);
		description.setMaxLength(255);
		author.setMaxLength(100);
		isbn.setMaxLength(200);

		description.setWidth("800");
		description.setRows(3);

		title.setWidth("300");
		author.setWidth("300");
		
		HorizontalLayout buttons = new HorizontalLayout(save, delete);
		errorMessages.setVisible(false);
		addComponents(title, description, author, isbn, printYear, errorMessages, buttons);

		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(KeyCode.ENTER);

		save.addClickListener(e -> this.save());
		delete.addClickListener(e -> this.delete());

		binder.forField(title).withValidator(e -> !e.isEmpty(), "Title must not be empty").bind(Book::getTitle,
				Book::setTitle);

		binder.forField(printYear).withValidator(new Validator<String>() {
			@Override
			public ValidationResult apply(String value, ValueContext context) {
				try
				{
					Integer i = Integer.parseInt(value);
				}
				catch (Exception ex)
				{
					return  ValidationResult.error("Must enter a number");
				}
				return  ValidationResult.ok();
			}
		})
				.withConverter(new StringToIntegerConverter("Must enter a number"))
				.bind(Book::getPrintYear, Book::setPrintYear);
		binder.bindInstanceFields(this);
	}
	
	@PostConstruct
	void Init()
	{
		
	}

	public void setBookListUI(BookListUI bookListUI) {
		this.bookListUI = bookListUI;
	}

	public void setBook(Book book) {
		this.book = book;
		binder.setBean(book);

		delete.setVisible(book.isPersisted());
		setVisible(true);
		title.selectAll();
		author.setEnabled(!book.isPersisted());
		printYear.setValue(Integer.toString(book.getPrintYear()));
	}

	private void delete() {
		bookDao.removeBook(book.getId());
		bookListUI.updateList();
		setVisible(false);
	}

	private void save() {
		errorMessages.setValue("");
		errorMessages.setVisible(false);
		
		
		BinderValidationStatus<Book> validationStatus = binder.validate();
		if(validationStatus.hasErrors())
		{
			StringBuilder stringBuilder = new StringBuilder();
			for(BindingValidationStatus<?> status:validationStatus.getFieldValidationErrors())
			{
				stringBuilder.append(status.getMessage().get());
				stringBuilder.append("; ");
			}
			errorMessages.setValue(stringBuilder.toString());
			errorMessages.setVisible(true);
			return;
		}
		
		if (book.isPersisted())
			bookDao.updateBook(book);
		else
			bookDao.addBook(book);
		bookListUI.updateList();
		setVisible(false);
	}
}