package com.example.application.views.list;

import com.example.application.data.entity.Contact;
import com.example.application.data.service.CrmService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.Collections;

//UI ELEMENTS RESIDE WITHIN THIS LISTVIEW FILE.

@PageTitle("Contacts | Vaadin CRM")
@Route(value = "")
public class ListView extends VerticalLayout {
    // creating the CRM grid.
    // Calling on a built-in Vaadin class, called 'Contact class'. details can be view by right-clicking on it.
    Grid<Contact> grid = new Grid<>(Contact.class);
    // creating text-fields for the new grid.
    TextField filterText = new TextField();
    //fetching the ContactForm class.
    ContactForm form;
    private CrmService service;

    // ADDING UI COMPONENTS.
    // Also, auto-wiring the CrmService Class, for DB connectivity.
    public ListView(CrmService service) {
        this.service = service;
        // Adding a ClassName, so that it makes things easier when I write CSS class later in the project.
        addClassName("list-view");
        //making the list view the same size as the entire window.
        setSizeFull();
        //configuring the layout.
        configureGrid();
        //Configuration method for the Contact Form.
        configureForm();

        //adding toolbar components to the grid.
        add(
                //calling the getToolBar method.
                getToolBar(),
                //calling the getContent method.
                getContent()
        );

        //calling the updateList method, Updates the contact list in the DB.
        updateList();
    }

    //METHODS CREATED BELOW.

    private void updateList() {
        grid.setItems(service.findAllContacts(filterText.getValue()));
    }

    // method that creates a horizontal wrapper layout that holds the grid and the contact form next to each-other.
    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        // line below says that the grid should get 2/3s of the page space/layout.
        content.setFlexGrow(2, grid);
        // line below says that the form should get 1/3 of the page space/layout.
        content.setFlexGrow(1,form);
        content.addClassName("Content");
        content.setSizeFull();

        return content;
    }

    //method for the configureForm method.
    private void configureForm() {
        form = new ContactForm(service.findAllCompanies(), service.findAllStatuses());
        form.setWidth("25em");
    }

    //creating the getToolBar component.
    private Component getToolBar() {
        //search field with a place-holder title.
        filterText.setPlaceholder("Filter by name...");
        //Clear button
        filterText.setClearButtonVisible(true);
        //ValueChangeMode(LAZY) -> this means that the app will wait until the user stops typing, to fetch the data,
        // from the database. If we don't use this 'LAZY' mode, the app will try fetching the data after every keystroke.
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        //filterText field, will filter through records as we're typing.
        filterText.addValueChangeListener(e -> updateList());

        //Adding a button to create a new contact.
        Button addContactButton = new Button("Add contact");

        // arranging these components into a horizontal layout.
        HorizontalLayout toolbar = new HorizontalLayout(filterText, addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    // creating the configureGrid method.
    private void configureGrid(){
        //adding a className to this grid, so I can style it later in the project.
        grid.addClassName("contact-grid");
        grid.setSizeFull();;
        // Choose the addColumn String
        grid.setColumns("firstName", "lastName", "email");
        //chose the addColumn Render <Column> renderer.
        grid.addColumn(contact -> contact.getStatus().getName()).setHeader("Status");
        grid.addColumn(contact -> contact.getCompany().getName()).setHeader("Company");
        // line of code below will automatically resize all the columns.
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }
}
