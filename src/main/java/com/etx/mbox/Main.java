package com.etx.mbox;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.james.mime4j.mboxiterator.CharBufferWrapper;
import org.apache.james.mime4j.mboxiterator.MboxIterator;

import javax.mail.Header;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Enumeration;
import java.util.Properties;

public class Main {

    public static void main(String[] args) throws IOException {
	// write your code here

        CharsetEncoder ENCODER = Charset.forName("ISO-8859-1").newEncoder();
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
                    System.out.println(counter + " - " + " - " + msg.getFrom()[0] + " - " + msg.getMessageID() );
                   // System.out.println( );

                    // Print Some Details



                    for (Enumeration<javax.mail.Header> e = msg.getAllHeaders(); e.hasMoreElements();) {
                        Header h = e.nextElement();

                        if (h.getName().equals("From")) {
                            System.out.println(h.getName() + ": " + h.getValue());

                        }

                        if (h.getName().equals("Date")) {
                            System.out.println(h.getName() + ": " + h.getValue());
                        }

                        if (h.getName().equals("Subject")) {
                            System.out.println(h.getName() + ": " + h.getValue());
                        }


                    }

                System.out.println("----------");


            }


            } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
