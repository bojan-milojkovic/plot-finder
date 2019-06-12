package com.plot.finder.plot.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.plot.finder.email.EmailUtil;
import com.plot.finder.exception.MyRestPreconditionsException;
import com.plot.finder.plot.repository.PlotRepository;
import com.plot.finder.plot.service.PlotService;
import com.plot.finder.user.repository.UserRepository;

@Component
public class MyScheduler {
	
	private EmailUtil emailUtil;
	private PlotService plotServiceImpl;
	private PlotRepository plotRepo;
	private UserRepository userRepo;
	
	@Autowired
	public MyScheduler(EmailUtil emailUtil, PlotService plotServiceImpl, PlotRepository plotRepo, UserRepository userRepo) {
		this.emailUtil = emailUtil;
		this.plotServiceImpl = plotServiceImpl;
		this.plotRepo = plotRepo;
		this.userRepo = userRepo;
	}
	
	@Scheduled(cron = "0 0 12 * * ?")
	public void checkPlotsAboutToExpire(){
		System.out.println("\n\tscheduled 1");
		Thread task = new Thread(){
			Lock lock = new ReentrantReadWriteLock().writeLock();
			
			@Override
			public void run(){
				plotRepo.findAboutToExpire(LocalDate.now().plusDays(3))
						.stream()
						.forEach(j -> {
								try{
									lock.lock();
									emailUtil.plotAddAboutToExpire(j);
								} finally{
									lock.unlock();
								}
							});
			}
		};
		task.setPriority(1);
		task.start();
	}
	
	@Scheduled(cron = "0 0 13 * * ?")
	public void notifyPlotDeleted(){
		Thread task = new Thread(){
		    Lock lock = new ReentrantReadWriteLock().writeLock();
		    
			@Override
			public void run(){
				plotRepo.findExpired()
					.stream()
					.forEach(j -> {
						try {
				            lock.lock();
				            plotServiceImpl.delete(j.getId(), null, false);
				            emailUtil.plotAddDeleted(j);
				        } catch (MyRestPreconditionsException e) {
							e.printStackTrace();
						} finally {
				            lock.unlock();
				        }
					});
			}
		};
		task.setPriority(1);
		task.start();
	}
	
	@Scheduled(cron = "0 0 14 * * ?")
	public void deleteLockedUsers(){
		Thread task = new Thread(){
		    Lock lock = new ReentrantReadWriteLock().writeLock();
		    
			@Override
			public void run(){
				userRepo.findLockedExpired(LocalDateTime.now().minusDays(30))
					.stream()
					.forEach(u -> {
			            lock.lock();
			            emailUtil.userAccountDeleted(u);
			        	userRepo.delete(u);
			            lock.unlock();
					});
			}
		};
		task.setPriority(1);
		task.start();
	}
}