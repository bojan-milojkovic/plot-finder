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
	                MimeMessageHelper.MULTIPART_MODE_NO,
	                StandardCharsets.UTF_8.name());
			helper.setTo(entity.getUserJpa().getEmail());
			helper.setFrom("donotreply@plotfinder.com");
			helper.setSubject("New plot notice");
			
			Context context = new Context();
			//context.setVariable("name", entity.getUserJpa().getFirstName()+" "+entity.getUserJpa().getLastName());
			//context.setVariable("plot_url", "http://"+InetAddress.getLocalHost().getHostName()+"/plot/"+entity.getId());
			Map<String,Object> model = new HashMap<String,Object>();
			model.put("name", entity.getUserJpa().getFirstName()+" "+entity.getUserJpa().getLastName());
			model.put("plot_url", "http://"+InetAddress.getLocalHost().getHostName()+"/plot/"+entity.getId());
			context.setVariables(model);
			
	        helper.setText(emailTemplateEngine.process("new_plot_notice", context), true);

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
	
	public void plotAddAboutToExpire(final PlotJPA entity) {
		try {
			Map<String,Object> model = new HashMap<String,Object>();
			model.put("name", entity.getUserJpa().getFirstName()+" "+entity.getUserJpa().getLastName());
			model.put("plot_url", "http://"+InetAddress.getLocalHost().getHostName()+"/plot/"+entity.getId());
			
			sendEmail("Your plot is about to expire", entity.getUserJpa().getEmail(), "plot_will_expire", model);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public void plotAddDeleted(final PlotJPA entity) {
		
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("name", entity.getUserJpa().getFirstName()+" "+entity.getUserJpa().getLastName());
		model.put("title", entity.getTitle());
		
		sendEmail("Your plot has been deleted", entity.getUserJpa().getEmail(), "plot_deleted", model);
	}
	
	private void sendEmail(final String subject, final String to, final String templateName, final Map<String, Object> model) {
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			
			MimeMessageHelper helper = new MimeMessageHelper(message,
			        MimeMessageHelper.MULTIPART_MODE_NO,
			        StandardCharsets.UTF_8.name());
			helper.setFrom("donotreply@plotfinder.com");
			helper.setTo(to);
			helper.setSubject(subject);
			
			Context context = new Context();
			context.setVariables(model);
			
			helper.setText(emailTemplateEngine.process(templateName, context), true);
			
			javaMailSender.send(message);
			System.out.println("Message sent successfully!!");
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void confirmRegistration(final String key, final String name) {
		
	}
	
	public void changePasswordEmail(final String key, final String name) {
		
	}
}