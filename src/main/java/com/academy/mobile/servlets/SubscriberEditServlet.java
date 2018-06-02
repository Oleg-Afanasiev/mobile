package com.academy.mobile.servlets;

import com.academy.mobile.PropertyManager;
import com.academy.mobile.dao.ConnectionManager;
import com.academy.mobile.dao.SubscriberDAO;
import com.academy.mobile.model.Gender;
import com.academy.mobile.model.Subscriber;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

public class SubscriberEditServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (ConnectionManager connectionManager = new ConnectionManager(PropertyManager.getInstance().getProperties())) {
            SubscriberDAO subscriberDAO = new SubscriberDAO(connectionManager.getConn());
            Subscriber subscriber = subscriberDAO.getById(Long.parseLong(req.getParameter("id")));
            req.setAttribute("subscriber", subscriber);
            req.getRequestDispatcher("/subscriber/subscriberEdit.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("/index.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try(ConnectionManager connectionManager = new ConnectionManager(PropertyManager.getInstance().getProperties());) {
            Subscriber subscriber = new Subscriber()
                    .withId(Long.parseLong(req.getParameter("id")))
                    .withFirstName(req.getParameter("fname"))
                    .withLastName(req.getParameter("lname"))
                    .withGender(Gender.valueOf(req.getParameter("gender").charAt(0)))
                    .withAge(Integer.parseInt(req.getParameter("age")));

            SubscriberDAO subscriberDAO = new SubscriberDAO(connectionManager.getConn());
            subscriberDAO.update(subscriber);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            resp.sendRedirect("/subscribers");
        }
    }
}
