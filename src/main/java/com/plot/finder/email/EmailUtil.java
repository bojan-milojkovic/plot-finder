package com.plot.finder.email;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.thymeleaf.context.Context;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import com.plot.finder.plot.entities.PlotJPA;
import org.springframework.ui.velocity.VelocityEngineUtils;

@Component
public class EmailUtil {
	
	public JavaMailSender javaMailSender;
    private VelocityEngine velocityEngine;
	
	@Autowired
	public EmailUtil(JavaMailSender javaMailSender, VelocityEngine velocityEngine) {
		this.javaMailSender = javaMailSender;
		this.velocityEngine = velocityEngine;
	}

	public void sendNewPlotEmail(final PlotJPA entity) {
		try {
			Map<String,Object> model = new HashMap<String,Object>();
			model.put("name", entity.getUserJpa().getFirstName()+" "+entity.getUserJpa().getLastName());
			model.put("plot_url", "http://"+InetAddress.getLocalHost().getHostName()+"/plot/"+entity.getId());
		
			sendEmail("New plot notice", entity.getUserJpa().getEmail(), "new_plot_notice.vm", model);
        } catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public void plotAddAboutToExpire(final PlotJPA entity) {
		try {
			Map<String,Object> model = new HashMap<String,Object>();
			model.put("name", entity.getUserJpa().getFirstName()+" "+entity.getUserJpa().getLastName());
			model.put("plot_url", "http://"+InetAddress.getLocalHost().getHostName()+"/plot/"+entity.getId());
			
			sendEmail("Your plot is about to expire", entity.getUserJpa().getEmail(), "plot_will_expire.vm", model);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public void plotAddDeleted(final PlotJPA entity) {
		
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("name", entity.getUserJpa().getFirstName()+" "+entity.getUserJpa().getLastName());
		model.put("title", entity.getTitle());
		
		sendEmail("Your plot has been deleted", entity.getUserJpa().getEmail(), "plot_deleted.vm", model);
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
			
			helper.setText(VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "templates/"+templateName, model), true);
			
			javaMailSender.send(message);
			System.out.println("Message sent successfully!!");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	public void confirmRegistration(final String key, final String name) {
		
	}
	
	public void changePasswordEmail(final String key, final String name) {
		
	}
}