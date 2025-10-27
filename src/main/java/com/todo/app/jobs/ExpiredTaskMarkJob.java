package com.todo.app.jobs;

import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.todo.app.service.TaskManagementService;

public class ExpiredTaskMarkJob extends QuartzJobBean {
  @Autowired
  private TaskManagementService taskManager;

  @Override
  public void executeInternal(JobExecutionContext context) {
    int markedTaskNum = taskManager.markExpiredTasks();
    System.out.println("tasks update completed. Number of tasks marked as expired: " + markedTaskNum);
  }
}
