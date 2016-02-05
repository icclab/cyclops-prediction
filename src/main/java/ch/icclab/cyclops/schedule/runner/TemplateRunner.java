package ch.icclab.cyclops.schedule.runner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Author: Skoviera
 * Created: 22/01/16
 * Description: Template scheduled task, implement your code here
 */
public class TemplateRunner extends AbstractRunner {
    final static Logger logger = LogManager.getLogger(TemplateRunner.class.getName());

    @Override
    public void run() {
        logger.trace("Starting scheduled TEMPLATE task");

        // your implementation
        implement_here();
    }

    /**
     * This method is being called by a scheduler in a new thread
     */
    private void implement_here() {
        // whatever you need to do on a scheduled basis
    }
}
