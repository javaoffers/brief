package com.javaoffers.batis.modelhelper.encrypt;

import com.javaoffers.batis.modelhelper.encrypt.anno.AesEncryptConfig;
import com.javaoffers.batis.modelhelper.encrypt.batis.MybatisEncryptInterceptorConfig;
import com.javaoffers.batis.modelhelper.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

import java.util.ArrayList;

/**
 * @author mingJie
 */
//@Slf4j
public class AesEncryptConfigBeanRegistrar implements ImportBeanDefinitionRegistrar , BeanFactoryPostProcessor {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata
                .getAnnotationAttributes(AesEncryptConfig.class.getName()));
        //log.info("JdbcParameter tag: {}", new JdbcParameter().getTag());
        //私钥
        String key = attributes.getString(EncryptConfigConst.KEY);
        //私钥不允许为空
        Assert.isTrue(StringUtils.isNotBlank(key), "AES key is blank");
        //获取表字段信息(这对于这些信息进行加解密)
        AnnotationAttributes[] encryptTableColumns = attributes.getAnnotationArray("encryptTableColumns");

        if(encryptTableColumns != null){
            boolean status = false;
            for(AnnotationAttributes encryptTableColumn : encryptTableColumns ){
                //表名称
                String tableName = encryptTableColumn.getString(EncryptConfigConst.TABLE_NAME);
                //表字段名称
                String[] columns = encryptTableColumn.getStringArray(EncryptConfigConst.COLUMNS);
                //将解析的信息缓存起来
                EncryptConfigContext.put(key, tableName, columns);
                status = true;
            }
            if(status){
                String beanName = "SqlAesProcessor#"+key;
                if(!registry.containsBeanDefinition(beanName)){
                    BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(SqlAesProcessor.class);
                    beanDefinitionBuilder.addConstructorArgValue(key);
                    registry.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());
                }

                //注册mybatis拦截器AES加密
                if(!registry.containsBeanDefinition(MybatisEncryptInterceptorConfig.class.getName())){
                    BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(MybatisEncryptInterceptorConfig.class);
                    registry.registerBeanDefinition(MybatisEncryptInterceptorConfig.class.getName(), beanDefinitionBuilder.getBeanDefinition());
                }
                //注册JqlAesInterceptor
                if(!registry.containsBeanDefinition(JqlAesInterceptor.class.getName())){
                    BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(JqlAesInterceptor.class);
                    registry.registerBeanDefinition(JqlAesInterceptor.class.getName(), beanDefinitionBuilder.getBeanDefinition());

                    BeanDefinitionBuilder beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(AesEncryptConfigBeanRegistrar.class);
                    registry.registerBeanDefinition(AesEncryptConfigBeanRegistrar.class.getName(), beanDefinition.getBeanDefinition());
                }
            }
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        String[] beanNamesForType = configurableListableBeanFactory.getBeanNamesForType(SqlAesProcessor.class);
        ArrayList<SqlAesProcessor> sqlAesProcessors = Lists.newArrayList();
        if(beanNamesForType != null){
            for(String beanName : beanNamesForType){
                sqlAesProcessors.add((SqlAesProcessor)configurableListableBeanFactory.getBean(beanName));
            }
        }
        JqlAesInterceptor jqlAesInterceptor = (JqlAesInterceptor)configurableListableBeanFactory.getBean(JqlAesInterceptor.class.getName());
        jqlAesInterceptor.setSqlAesProcessors(sqlAesProcessors);
    }
}
