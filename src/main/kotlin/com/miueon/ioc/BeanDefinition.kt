package com.miueon.ioc

import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

class BeanFactory(
    val singletonObjects: MutableMap<String, Any> = ConcurrentHashMap(64),
    val beanDefinitionMap: MutableMap<String, BeanDefinition<Any>>
    = ConcurrentHashMap(),
){
    fun registerBeanDefinition(beanName: String, beanDefinition: BeanDefinition<Any>) {
        beanDefinitionMap[beanName] = beanDefinition
    }
    fun getBean(beanName: String): Any {
        var bean = singletonObjects[beanName]
        if (bean == null) {
            try {
                bean = doCreation(beanName, beanDefinitionMap[beanName]!!)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return bean!!
    }

    fun getSingleton(beanName: String): Any {
        return singletonObjects[beanName]!!
    }

    fun doCreation(beanName: String, beanDefinition: BeanDefinition<Any>): Any {
        val bean = createBeanInstance(beanDefinition)
        applyPropertyValue(bean, beanDefinition)
        return bean
    }

    fun createBeanInstance(beanDefinition: BeanDefinition<Any>): Any {
        val bean = beanDefinition.beanClass.getConstructor().newInstance()
        return bean
    }

    fun applyPropertyValue(bean: Any, beanDefinition: BeanDefinition<Any>) {
        val pvs = beanDefinition.propertyValues
        pvs?.let {
            it.propertyValueList.forEach{ti->
                val field = bean.javaClass.getDeclaredField(ti.name)
                if (field.trySetAccessible()) {
                    field.set(bean, ti.value)
                }
            }
        }
    }
}


class BeanDefinitionHolder(val beanName: String, val beanDefinition: BeanDefinition<Any>)

class BeanDefinition<T> {
    lateinit var beanClass: Class<T>
    var beanClassName: String = ""
        set(value) {
            field = value
            try {
                beanClass = Class.forName(value) as Class<T>
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            }
        }
    var propertyValues: PropertyValues? = null
}

class PropertyValues(val propertyValueList: MutableList<PropertyValue> = ArrayList()) {
    fun addPropertyValue(pv: PropertyValue) {
        propertyValueList.add(pv)
    }
}

class PropertyValue(val name: String, val value: Any)