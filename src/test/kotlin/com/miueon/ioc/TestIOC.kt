package com.miueon.ioc

import org.junit.Test
import kotlin.test.assertEquals

class HelloWorld {
    private var msg: String = "Fuck world"
    fun sayHello() :String{
        return msg
    }
}

class TestIOC{
    @Test
    fun testIOC() {
        val beanFactory = BeanFactory()

        val bd = BeanDefinition<Any>()
        bd.beanClassName = "com.miueon.ioc.HelloWorld"
        val bdH = BeanDefinitionHolder("hi", bd)
        beanFactory.registerBeanDefinition(bdH.beanName, bdH.beanDefinition)

        val hello = beanFactory.getBean("hi") as HelloWorld
        assertEquals("Fuck world", hello.sayHello())
    }

    @Test
    open fun testIoCProperty() {
        // 1. 创建beanFactory
        val beanFactory = BeanFactory()

        // 2. 注册bean
        val bd = BeanDefinition<Any>()
        bd.beanClassName = "com.miueon.ioc.HelloWorld"

        // 注入Property
        val pvs = PropertyValues()
        pvs.addPropertyValue(PropertyValue("msg", "Hello IoC Property!"))
        bd.propertyValues = pvs
        val bdh = BeanDefinitionHolder("helloWorld", bd)
        beanFactory.registerBeanDefinition(bdh.beanName, bdh.beanDefinition)

        // 3. 获取bean
        val hello = beanFactory.getBean("helloWorld") as HelloWorld
        assertEquals("Hello IoC Property!", hello.sayHello())
    }
}