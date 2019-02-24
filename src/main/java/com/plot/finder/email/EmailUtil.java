package com.plot.finder.email;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {
	
	@Autowired
	public JavaMailSender javaMailSender;

	public void sendEmail(String to, String subject, String text) {
		try {
			javaMailSender.send(new MimeMessagePreparator() {
                public void prepare(MimeMessage mimeMessage)
                        throws Exception {
                    MimeMessageHelper message = new MimeMessageHelper(mimeMessage,
                            false, "UTF-8");
                    message.setFrom("noreply@plotfinder.com");
                    message.addTo(to);
                    message.setSubject(subject);
                    message.setText(text, true);
                }
            });
			System.out.println("Message sent successfully!!");
        } catch (MailSendException e) {
            e.printStackTrace();
        }
	}
}
