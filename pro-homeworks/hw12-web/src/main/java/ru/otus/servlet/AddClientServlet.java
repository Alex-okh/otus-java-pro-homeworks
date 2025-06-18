package ru.otus.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.UserAuthService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

public class AddClientServlet extends HttpServlet {

    private static final String PARAM_NAME = "name";
    private static final String PARAM_ADDRESS = "address";
    private static final String PARAM_PHONE = "phone";
    private static final String ADD_PAGE_TEMPLATE = "newclient.html";

    private final transient TemplateProcessor templateProcessor;
    private final transient DBServiceClient dbServiceClient;

    public AddClientServlet(DBServiceClient clientService, TemplateProcessor templateProcessor) {
        this.templateProcessor = templateProcessor;
        this.dbServiceClient = clientService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(ADD_PAGE_TEMPLATE, Collections.emptyMap()));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String name = request.getParameter(PARAM_NAME);
        String address = request.getParameter(PARAM_ADDRESS);
        List<Phone> phones = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            String number = request.getParameter( PARAM_PHONE+i);
            if (!number.isBlank()) {
                Phone phone = new Phone(null, request.getParameter(PARAM_PHONE + i));
                phones.add(phone);
            }
        }
        Client newclient = new Client(null, name, new Address(null, address.isBlank() ? null : address), phones);
        dbServiceClient.saveClient(newclient);
        response.sendRedirect("/clients");

    }
}
