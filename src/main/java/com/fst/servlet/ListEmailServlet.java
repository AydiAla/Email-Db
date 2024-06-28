package com.fst.servlet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ListEmailServlet")
public class ListEmailServlet extends HttpServlet {

   
	private static final long serialVersionUID = 1L;
	
	
	
    //C'est une liste qui stockera les adresses email. Elle est initialisée comme une liste vide.
    private List<String> addresses = new ArrayList<>();
    
    
    //C'est le chemin vers le fichier qui stocke les adresses email. L'adresse est un chemin absolu sur le disque.
    private String addressesFilePath = "C:\\Jee\\address.txt"; 
    
    List<String> LISTE_Email = new ArrayList<>();

    //C'est la connexion à la base de données, qui sera utilisée pour les opérations de base de données.
    private Connection connection;
    
    //C'est la méthode d'initialisation du servlet. Elle est appelée lorsque le servlet est déployé pour la première fois.
    public void init() throws ServletException {
       
    	
    	
    	// Load initial addresses from the file
        try {
        	// Il lit chaque ligne du fichier et l'ajoute à la liste addresses
        	// En cas d'erreur de lecture, une exception est imprimée.
        	
    	//Le rôle de cette ligne de code est de créer un flux de lecture (BufferedReader) qui est utilisé pour lire le contenu d'un fichier texte spécifié par addressesFilePath
            BufferedReader reader = new BufferedReader(new FileReader(addressesFilePath));
            String line;
            while ((line = reader.readLine()) != null) {
                addresses.add(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately
        }

        // Initialize the database connection
       /* try {
        	//charger la drive
            Class.forName("com.mysql.cj.jdbc.Driver");
            //connection à lurl de base de donne
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
            
           System.out.print(connection);
           
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            // Handle the exception appropriately
        }*/
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<HTML>");
        out.println("<HEAD><TITLE>Titre</TITLE></HEAD>");
        out.println("<BODY>");
        out.println("<H1>Members:</H1>");
        out.println("<ul>");
        for (String address : addresses) {
            out.println("<li>" + address + "</li>");
        }
        //dans la console
       // System.out.print("LIste des emails de basse de donnés");
        //LISTE_Email=recupererEmail();
        //System.out.println(LISTE_Email);
        
        out.println("<hr>");
        out.println("<form action='ListEmailServlet' method='post'>");
        out.println("<p>Entrer votre adresse email :<input type='email' name='email'></p>");
        out.println("<input type='submit' name='subscribe' value='Ajouter'>");
        out.println("<input type='submit' name='unsubscribe' value='Supprimer'>");
        out.println("</form>");
        out.println("</BODY>");
        out.println("</HTML>");
        
        
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        // Page d'erreur si vide ou null
        if (email == null || email.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Aucune adresse e-mail spécifiée.");
            return;
        }

        // Validation de l'adresse e-mail avec une expression régulière
        //?: permet la répétition de la sous-expression
        //* signifie que cette partie peut se répéter zéro fois ou plus
        //
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z]+\\.)+[a-zA-Z]{2,7}$";
        if (!email.matches(emailRegex)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Format d'adresse e-mail invalide.");
            return;
        }

        if (request.getParameter("subscribe") != null) {
            // Subscribe logic here
            addresses.add(email);
            // Insert the email into the MySQL database
            // insertEmailToDatabase(email);
        } else if (request.getParameter("unsubscribe") != null) {
            // Unsubscribe logic here
            addresses.remove(email);
            // Remove the email from the MySQL database
            // removeEmailFromDatabase(email);
        }

        try {
            // Création dans la liste des e-mails
            BufferedWriter writer = new BufferedWriter(new FileWriter(addressesFilePath));
            for (String address : addresses) {
                writer.write(address + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer l'exception de manière appropriée
        }

        doGet(request, response);
    }


}
