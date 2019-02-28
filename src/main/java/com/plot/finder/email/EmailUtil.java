package com.plot.finder.email;

import java.net.InetAddress;

import javax.mail.internet.MimeMessage;
import org.thymeleaf.context.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.thymeleaf.spring3.SpringTemplateEngine;

import com.plot.finder.plot.entities.PlotJPA;

@Component
public class EmailUtil {
	
	public JavaMailSender javaMailSender;
    private SpringTemplateEngine templateEngine;
	
	@Autowired
	public EmailUtil(JavaMailSender javaMailSender, SpringTemplateEngine templateEngine) {
		this.javaMailSender = javaMailSender;
		this.templateEngine = templateEngine;
	}

	public void sendNewPlotEmail(final PlotJPA entity) {
		try {
			javaMailSender.send(new MimeMessagePreparator() {
                public void prepare(MimeMessage mimeMessage)
                        throws Exception {
                    MimeMessageHelper message = new MimeMessageHelper(mimeMessage, false, "UTF-8");
                    message.setFrom("noreply@plotfinder.com");
                    message.addTo(entity.getUserJpa().getEmail());
                    message.setSubject("New Plot Notification");
                    
                    Context ctx = new Context();
                    ctx.setVariable("name", entity.getUserJpa().getFirstName()+" "+entity.getUserJpa().getLastName());
                    ctx.setVariable("plot_url", InetAddress.getLocalHost().getHostName()+"/"+entity.getId());
                    
                    message.setText(templateEngine.process("new_plot_notice", ctx), true);
                }
            });
			System.out.println("Message sent successfully!!");
        } catch (MailSendException e) {
            e.printStackTrace();
        }
	}
}
