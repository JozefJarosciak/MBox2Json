package com.etx.mbox;
import org.apache.james.mime4j.mboxiterator.CharBufferWrapper;
import org.apache.james.mime4j.mboxiterator.MboxIterator;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Properties;

public class Main {

    public static void main(String[] args) throws IOException {
	// write your code here

        CharsetEncoder ENCODER = Charset.forName("ISO-8859-1").newEncoder();
        final File mboxFile = new File("C:\\Users\\jjaros01\\Downloads\\201210.mbox");
        int counter = 0;


            for (CharBufferWrapper message : MboxIterator.fromFile(mboxFile).charset(ENCODER.charset()).build()) {


                try {

                    counter++;

                    // Print Entire Message
                    //System.out.println(counter + " - " + message);

                    Session s = Session.getInstance(new Properties());
                    InputStream is = new ByteArrayInputStream(message.toString().getBytes());
                    MimeMessage msg = new MimeMessage(s, is);

                    // Print Just Sent Date and Message-ID
                    System.out.println(counter + " - " + msg.getSentDate().toString() + " - " + msg.getMessageID() + " - " + msg.getSubject());


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }



    }
}
