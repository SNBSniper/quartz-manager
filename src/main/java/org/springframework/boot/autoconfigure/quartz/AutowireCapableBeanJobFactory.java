package org.springframework.boot.autoconfigure.quartz;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.util.Assert;

/**
 * Subclass of {@link SpringBeanJobFactory} that supports auto-wiring job beans.
 *
 * @author Vedran Pavic
 * @since 2.0.0
 * @see <a href="http://blog.btmatthews.com/?p=40#comment-33797"> Inject
 *      application context dependencies in Quartz job beans</a>
 */

public class AutowireCapableBeanJobFactory extends SpringBeanJobFactory {
    private final AutowireCapableBeanFactory beanFactory;

    public AutowireCapableBeanJobFactory(AutowireCapableBeanFactory beanFactory) {
        Assert.notNull(beanFactory, "Bean factory must not be null");
        this.beanFactory = beanFactory;
    }

    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        Object jobInstance = super.createJobInstance(bundle);
        this.beanFactory.autowireBean(jobInstance);
        this.beanFactory.initializeBean(jobInstance, null);
        return jobInstance;
    }
}
