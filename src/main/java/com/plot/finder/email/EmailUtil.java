package com.plot.finder.email;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import javax.mail.internet.MimeMessage;
import org.thymeleaf.context.Context;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import com.plot.finder.plot.entities.metamodels.PlotJPA;
import com.plot.finder.user.entity.UserJPA;
import org.springframework.ui.velocity.VelocityEngineUtils;

@Component
public class EmailUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(EmailUtil.class);
	
	public JavaMailSender javaMailSender;
    private VelocityEngine velocityEngine;
    private String hostName;
	
	@Autowired
	public EmailUtil(JavaMailSender javaMailSender, VelocityEngine velocityEngine, String hostName) {
		this.javaMailSender = javaMailSender;
		this.velocityEngine = velocityEngine;
		this.hostName = hostName;
	}

	public void sendNewPlotEmail(final PlotJPA entity) {
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("name", entity.getUserJpa().getFirstName()+" "+entity.getUserJpa().getLastName());
		model.put("type", (entity.getFlags().charAt(0)=='0' ? "SALE" : "RENT"));
		model.put("plot_url", hostName+"/plot/"+entity.getId());
	
		sendEmail("New plot notice", entity.getUserJpa().getEmail(), "new_plot_notice.vm", model);
	}
	
	public void plotAddAboutToExpire(final PlotJPA entity) {
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("name", entity.getUserJpa().getFirstName()+" "+entity.getUserJpa().getLastName());
		model.put("plot_url", hostName+"/plot/"+entity.getId());
		
		sendEmail("Your plot is about to expire", entity.getUserJpa().getEmail(), "plot_will_expire.vm", model);
	}
	
	public void plotAddDeleted(final PlotJPA entity) {
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("name", entity.getUserJpa().getFirstName()+" "+entity.getUserJpa().getLastName());
		model.put("title", entity.getTitle());
		
		sendEmail("Your plot has been deleted", entity.getUserJpa().getEmail(), "plot_deleted.vm", model);
	}
	
	public void userLocked(final UserJPA jpa){
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("name", jpa.getFirstName()+" "+jpa.getLastName());
		
		sendEmail("Your user account is locked", jpa.getEmail(), "account_locked_notice.vm", model);
	}
	
	public void userUnlocked(final UserJPA jpa){
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("name", jpa.getFirstName()+" "+jpa.getLastName());
		
		sendEmail("Your user account is unlocked", jpa.getEmail(), "account_unlocked_notice.vm", model);
	}
	
	public void userAccountDeleted(UserJPA jpa){
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("name", jpa.getFirstName()+" "+jpa.getLastName());
		
		sendEmail("Your user account is deleted", jpa.getEmail(), "user_account_deleted.vm", model);
	}
	
	public void confirmRegistration(final String key, final String name, final String email) {
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("name", name);
		model.put("url", hostName+"/users/act/"+key);
		
		sendEmail("User activation", email, "user_activation.vm", model);
	}
	
	public void changePasswordEmail(final String key, final String name) {
		
	}
	
	private void sendEmail(final String subject, final String to, final String templateName, final Map<String, Object> model) {
		try {
			logger.debug("Sending the '"+subject+"' email ...");
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}