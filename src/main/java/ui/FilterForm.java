package main.java.ui;

import com.vaadin.data.Binder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.data.BindingValidationStatus;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;

import main.java.dao.ComparatorType;
import main.java.dao.Filter;
import main.java.dao.ReadFilterStatus;

public class FilterForm extends FormLayout {
	private BookListUI bookListUI;
	Filter currentFilter, filter;
	private CheckBox userTitleFilterCheckBox = new CheckBox("");
	private TextField titleFilter = new TextField("");
	private CheckBox userAuthorFilterCheckBox = new CheckBox("");
	private TextField authorFilter = new TextField("");
	private NativeSelect<ReadFilterStatus> readFilterStatus = new NativeSelect<>("");
	private Binder<Filter> binder = new Binder<>(Filter.class);
	private Button applyButton = new Button("Apply");
	private Button resetButton = new Button("Reset");
	private Button closeButton = new Button("Close");
	private CheckBox userDateFilterCheckBox = new CheckBox("Print year");
	private NativeSelect<ComparatorType> dateComparatorType = new NativeSelect<>("");
	private TextField dateAfter = new TextField("");
	private Label dateAndLabel = new Label("and");
	private TextField dateBefore = new TextField("");
	private HorizontalLayout dateAndSector = new HorizontalLayout(dateAndLabel, dateBefore);
	private Label errorMessages = new Label();
	
	public FilterForm(BookListUI bookListUI) {
		errorMessages.setVisible(false);
		this.bookListUI = bookListUI;
		
		binder.forField(dateAfter).withValidator(new Validator<String>() {
			@Override
			public ValidationResult apply(String value, ValueContext context) {
				try
				{
					Integer i = Integer.parseInt(value);
				}
				catch (Exception ex)
				{
					return  ValidationResult.error("First date must be a number");
				}
				return  ValidationResult.ok();
			}
		})
				.withConverter(new StringToIntegerConverter("Must enter a number"))
				.bind(Filter::getDateAfter, Filter::setDateAfter);
		
		binder.forField(dateBefore).withValidator(new Validator<String>() {
			@Override
			public ValidationResult apply(String value, ValueContext context) {
				try
				{
					Integer i = Integer.parseInt(value);
				}
				catch (Exception ex)
				{
					return  ValidationResult.error("Second date must be a number");
				}
				return  ValidationResult.ok();
			}
		})
				.withConverter(new StringToIntegerConverter("Must enter a number"))
				.bind(Filter::getDateBefore, Filter::setDateBefore);
		
		binder.bind(readFilterStatus, "readFilter");
		binder.bind(userDateFilterCheckBox, "useDateFilter");
		binder.bind(dateComparatorType, "dateComparatorType");
		binder.bind(userTitleFilterCheckBox, "useTitleFilter");
		binder.bind(titleFilter, "titleFilter");
		binder.bind(userAuthorFilterCheckBox, "useAuthorFilter");
		binder.bind(authorFilter, "authorFilter");
		
		Label titleTitle = new Label("Title");
		HorizontalLayout titleFilterLauout = new HorizontalLayout(userTitleFilterCheckBox, titleTitle, titleFilter);
		titleFilterLauout.setComponentAlignment(userTitleFilterCheckBox, Alignment.BOTTOM_CENTER);
		titleFilterLauout.setComponentAlignment(titleTitle, Alignment.BOTTOM_CENTER);
		
		Label authorTitle = new Label("Author");
		HorizontalLayout authorFilterLauout = new HorizontalLayout(userAuthorFilterCheckBox, authorTitle, authorFilter);
		authorFilterLauout.setComponentAlignment(userAuthorFilterCheckBox, Alignment.BOTTOM_CENTER);
		authorFilterLauout.setComponentAlignment(authorTitle, Alignment.BOTTOM_CENTER);
		
		titleFilter.addValueChangeListener(new ValueChangeListener<String>() {
			@Override
			public void valueChange(ValueChangeEvent<String> event) {
				userTitleFilterCheckBox.setValue(true);
			}
		});
		
		authorFilter.addValueChangeListener(new ValueChangeListener<String>() {
			@Override
			public void valueChange(ValueChangeEvent<String> event) {
				userAuthorFilterCheckBox.setValue(true);
			}
		});
		
		dateAndSector.setComponentAlignment(dateAndLabel, Alignment.BOTTOM_CENTER);
		HorizontalLayout dateFilterLayout = new HorizontalLayout(userDateFilterCheckBox, dateComparatorType, dateAfter, dateAndSector);
		dateFilterLayout.setComponentAlignment(userDateFilterCheckBox, Alignment.BOTTOM_CENTER);
		
		dateComparatorType.addValueChangeListener(new ValueChangeListener<ComparatorType>() {
			@Override
			public void valueChange(ValueChangeEvent<ComparatorType> event) {
				boolean wasVisible = dateAndSector.isVisible();
				dateAndSector.setVisible(event.getValue()==ComparatorType.BETWEEN);
				if(wasVisible&&!dateAndSector.isVisible())
				{
					try
					{
						Integer i = Integer.parseInt(dateBefore.getValue());
					}
					catch (Exception ex)
					{
						dateBefore.setValue("0");
					}
				}
				userDateFilterCheckBox.setValue(true);
			}
		});
		
		ValueChangeListener<String> dateChangedListener = new ValueChangeListener<String>() {
			@Override
			public void valueChange(ValueChangeEvent<String> event) {
				userDateFilterCheckBox.setValue(true);
			}
		}; 
		
		dateAfter.addValueChangeListener(dateChangedListener);
		dateBefore.addValueChangeListener(dateChangedListener);
		
		dateComparatorType.setItems(ComparatorType.values());		
		
		readFilterStatus.setItems(ReadFilterStatus.values());
		applyButton.addClickListener(e -> this.applyFilter());
		resetButton.addClickListener(e->this.resetFilter());
		resetButton.setDescription("Reset all changes made to current filter");
		closeButton.addClickListener(e->bookListUI.setFilterFormVisibility(false));
		
		HorizontalLayout buttons = new HorizontalLayout(applyButton, resetButton, closeButton);
		
		Label readAlreadyTitle = new Label("Read already");
		HorizontalLayout readFilterLayout = new HorizontalLayout(readAlreadyTitle, readFilterStatus);
		readFilterLayout.setComponentAlignment(readAlreadyTitle, Alignment.BOTTOM_CENTER);
		
		addComponents(titleFilterLauout, authorFilterLauout, dateFilterLayout, readFilterLayout, errorMessages, buttons);
	}

	private void updateVisibility()
	{
		dateAndSector.setVisible(currentFilter.getDateComparatorType()==ComparatorType.BETWEEN);
	}
	
	private void copyFilter(Filter dest, Filter source)
	{
		dest.setReadFilter(source.getReadFilter());
		
		dest.setDateComparatorType(source.getDateComparatorType());
		dest.setDateAfter(source.getDateAfter());
		dest.setDateBefore(source.getDateBefore());
		dest.setUseDateFilter(source.getUseDateFilter());
		
		dest.setTitleFilter(source.getTitleFilter());
		dest.setUseTitleFilter(source.getUseTitleFilter());
		
		dest.setAuthorFilter(source.getAuthorFilter());
		dest.setUseAuthorFilter(source.getUseAuthorFilter());
	}
	
	public void setFilter(Filter filter) {
		currentFilter = filter;
		this.filter = new Filter();
		
		copyFilter(this.filter, currentFilter);
		
		updateVisibility();
		
		binder.setBean(this.filter);
		userDateFilterCheckBox.setValue(filter.getUseDateFilter());
	}	
	
	private void applyFilter()
	{
		errorMessages.setValue("");
		errorMessages.setVisible(false);
		
		
		BinderValidationStatus<Filter> validationStatus = binder.validate();
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
		
		copyFilter(currentFilter, filter);
		
		bookListUI.applyFilter();
	}
	
	private void resetFilter()
	{
		binder.readBean(currentFilter);
		
		dateBefore.setValue(Integer.toString(currentFilter.getDateBefore()));
		dateAfter.setValue(Integer.toString(currentFilter.getDateAfter()));
	}
}