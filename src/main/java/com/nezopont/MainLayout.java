package com.nezopont;

import com.nezopont.entity.User;
import com.nezopont.service.UserService;
import com.nezopont.web.*;
import com.vaadin.flow.component.*;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;


@Route(value="")
public class MainLayout extends Composite<VerticalLayout> implements HasComponents, RouterLayout, BeforeEnterObserver {
    private Div childWrapper = new Div();

    private User currentUser;

    public MainLayout() {
        getContent().setSizeFull();
        HorizontalLayout loginContent = new HorizontalLayout();
        TextField usernameField = new TextField();
        usernameField.setThemeName("username-text");

        loginContent.add(new TextField(""));

        PasswordField passwordField = new PasswordField("");
        passwordField.setPlaceholder("********");
        loginContent.add(passwordField);

        Button loginButton=new Button("Bejelentkezés");
        Button regButton=new Button("Regisztráció");

        loginButton.addClickListener(e -> {
            try {
                currentUser = UserService.login(authenticate((String) usernameField.getValue(), (String) passwordField.getValue()));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            String username = (String) usernameField.getValue();
            loginContent.removeAll();
            loginButton.getUI().ifPresent(ui -> ui.navigate("fooldal"));
            loginContent.add(new Text("Hello " + username + "!"));
        });


        regButton.addClickListener( e-> {
            regButton.getUI().ifPresent(ui -> ui.navigate("registration"));
        });
        loginContent.add(loginButton);
        loginContent.add(regButton);
        loginContent.setAlignItems(FlexComponent.Alignment.CENTER);
        add(loginContent);
        H1 header = new H1("NézőPont");
        add(header);

        HorizontalLayout mainContent = new HorizontalLayout();
        VerticalLayout menuBar = new VerticalLayout();


        menuBar.setWidth("15%");
        menuBar.add(new RouterLink("Föoldal", HomeView.class));
        menuBar.add(new RouterLink("Tv műsor", TvPrograms.class));
        menuBar.add(new RouterLink("Kedvencek", Favorite.class));
        menuBar.add(new RouterLink("Kapcsolat", Contact.class));

        mainContent.add(menuBar);
        mainContent.add(childWrapper);
        mainContent.setFlexGrow(1, childWrapper);

        add(mainContent);



        getContent().setFlexGrow(1, mainContent);
        getContent().setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, header);
        getContent().setHorizontalComponentAlignment(FlexComponent.Alignment.STRETCH, mainContent);

    }
    @Override
    public void showRouterLayoutContent(HasElement content) {
        childWrapper.getElement().appendChild(content.getElement());
    }
    @Override
    public void beforeEnter(BeforeEnterEvent event) {

    }

    private User authenticate(String username, String password) throws Exception{
        User user = new User("username", "password");

        if(user.equals(null)){
            throw new Exception("Login failed");
        }else{
            return user;
        }

    }
}
