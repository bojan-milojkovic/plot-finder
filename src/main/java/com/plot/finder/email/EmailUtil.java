package com.plot.finder.email;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.thymeleaf.context.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.spring5.SpringTemplateEngine;
import com.plot.finder.plot.entities.PlotJPA;

@Component
public class EmailUtil {
	
	public JavaMailSender javaMailSender;
    private SpringTemplateEngine emailTemplateEngine;
	
	@Autowired
	public EmailUtil(JavaMailSender javaMailSender, SpringTemplateEngine emailTemplateEngine) {
		this.javaMailSender = javaMailSender;
		this.emailTemplateEngine = emailTemplateEngine;
	}

	public void sendNewPlotEmail(final PlotJPA entity) {
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			
			MimeMessageHelper helper = new MimeMessageHelper(message,
	                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
	                StandardCharsets.UTF_8.name());
			
			Context context = new Context();
			//context.setVariable("name", entity.getUserJpa().getFirstName()+" "+entity.getUserJpa().getLastName());
			context.setVariable("plot_url", "http://"+InetAddress.getLocalHost().getHostName()+"/plot/"+entity.getId());
			Map<String,Object> model = new HashMap<String,Object>();
			model.put("name", entity.getUserJpa().getFirstName()+" "+entity.getUserJpa().getLastName());
			context.setVariables(model);
			String html = emailTemplateEngine.process("new_plot_notice", context);
			
			helper.setTo(entity.getUserJpa().getEmail());
	        helper.setText(html, true);
	        helper.setSubject("New plot notice");
	        helper.setFrom("donotreply@plotfinder.com");

	        javaMailSender.send(message);
	        
			System.out.println("Message sent successfully!!");
        } catch (MailSendException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}
