package org.apache.rocketmq.samples.springboot.enhancer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.spring.support.DefaultRocketMQListenerContainer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * 对DefaultRocketMQListenerContainer进行增强，在完成afterPropertiesSet方法调用之后对DefaultMQPushConsumer属性值进行修改，减少执行线程数
 * @author luojing
 * @since 2025/01/11
 */
@Component
public class DefaultRocketMQListenerContainerBeanPostProcessor implements BeanPostProcessor {


    /**
     * 修改线程的配置
     *
     * @param bean     the new bean instance
     * @param beanName the name of the bean
     */
    @Override
    public Object postProcessAfterInitialization(@Nonnull Object bean,@Nonnull String beanName) throws BeansException {

        if (bean instanceof DefaultRocketMQListenerContainer) {
            DefaultMQPushConsumer consumer = ((DefaultRocketMQListenerContainer) bean).getConsumer();
            Optional.ofNullable(consumer)
                    .ifPresent(c -> {
                        int clientCallbackExecutorThreads = Runtime.getRuntime().availableProcessors();
                        if(clientCallbackExecutorThreads >4){
                            clientCallbackExecutorThreads = 4;
                        }
                        consumer.setClientCallbackExecutorThreads(clientCallbackExecutorThreads);
                    });
        }
        return bean;
    }
}
