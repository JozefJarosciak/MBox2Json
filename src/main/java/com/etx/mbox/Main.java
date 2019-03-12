package com.etx.mbox;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.james.mime4j.mboxiterator.CharBufferWrapper;
import org.apache.james.mime4j.mboxiterator.MboxIterator;

import javax.mail.Header;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;

import static java.util.Date.parse;

public class Main {
    static Connection conn = null;

    public static void main(String[] args) throws IOException {

        // MySQL
        final String CONN_STRING = "jdbc:mysql://localhost:3306/usenetarchives";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(CONN_STRING, "root", "");
            System.out.println("Database Connected!");
        } catch (SQLException e) {
            System.err.println(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.exit(0);

        CharsetEncoder ENCODER = Charset.forName("UTF-8").newEncoder();
        final File mboxFile = new File(args[0]);
        int counter = 0;

        try {

          //  String regex = "From: (([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
            String regex = "From \\d{10}";

            for (CharBufferWrapper message : MboxIterator.fromFile(mboxFile).fromLine(regex).charset(ENCODER.charset()).build()) {



                    counter++;

                    // Print Entire Message
                    //System.out.println(counter + " - " + message);

                    Session s = Session.getInstance(new Properties());
                    InputStream is = new ByteArrayInputStream(message.toString().getBytes());
                    MimeMessage msg = new MimeMessage(s, is);

                    // Print Some Details
                String nameFrom = "";

                /*
                try {
                    nameFrom = msg.getFrom()[0].toString();
                } catch (MessagingException e) {
                 //   e.printStackTrace();
                }
                */

                System.out.println("Message # - " + counter );
                   // System.out.println(msg.getSentDate() ); //+ " - " + msg.getAllRecipients()[0]

                    // Print Some Details

                    for (Enumeration<javax.mail.Header> e = msg.getAllHeaders(); e.hasMoreElements();) {
                        Header h = e.nextElement();

                        if (h.getName().equals("Message-ID")) {
                         //   System.out.println(h.getName() + ": " + h.getValue());
                        }

                        if (h.getName().equals("From")) {
                     //       System.out.println(h.getName() + ": " + h.getValue());
                        }

                        if (h.getName().equals("Date")) {
                            @SuppressWarnings("deprecation") Long dateParsed = null;
                            try {
                                dateParsed = parse(h.getValue());
                            } catch (Exception e1) {
                                dateParsed = null;
                            }
                            System.out.println(h.getName() + ": " + h.getValue() + " - " + dateParsed);
                        }

                        if (h.getName().equals("Subject")) {
                         //   System.out.println(h.getName() + ": " + h.getValue());
                        }

                        if (h.getName().equals("References")) {
                        //    System.out.println(h.getName() + ": " + h.getValue());
                        }
                    }

                // Print Full Headers
                System.out.println(msg.getSize());

                    // Print Body
             //   System.out.println(msg.getContent());



                System.out.println("#############################");


            }


            } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
