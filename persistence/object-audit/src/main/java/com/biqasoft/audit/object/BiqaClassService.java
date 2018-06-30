/*
 * Copyright (c) 2016. com.biqasoft
 */

package com.biqasoft.audit.object;

import com.biqasoft.entity.core.BaseClass;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is service which provide constant names for every basic class
 * which is store in database
 * see usage getClassByName() method
 *
 * Created by Nikita Bakaev, ya@nbakaev.ru on 12/27/2015.
 * All Rights Reserved
 */
@Service
public class BiqaClassService {

    private Map<String, Class<? extends BaseClass>> biqaClasses = new HashMap<>();

    @PostConstruct
    public void init() {
        List<Class> classes;
        try {
            classes = findMyTypes("com.biqasoft.entity");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }

        classes.forEach(x -> this.biqaClasses.put(getName(x), x));
    }

    public Class<? extends BaseClass> getClassByName(String name) {
        Class<? extends BaseClass> aClass = biqaClasses.get(name);
        if (aClass != null){
            return aClass;
        }

        return null;
    }

    /**
     * Name used when we need unique names for BaseClassDto object as string,
     * for example key in map {@link DomainSettings#getDefaultCustomFields()}
     * @param aClass
     * @return
     */
    public String getName(Class<? extends BaseClass> aClass){
        return aClass.getSimpleName().toUpperCase();
    }

    public <T extends BaseClass> String getName(T aClass){
        return getName(aClass.getClass());
    }

    private List<Class> findMyTypes(String basePackage) throws ClassNotFoundException, IOException {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

        List<Class> candidates = new ArrayList<>();
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                resolveBasePackage(basePackage) + "/" + "**/*.class";
        Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
        for (Resource resource : resources) {
            if (resource.isReadable()) {
                MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                if (isCandidate(metadataReader)) {
                    candidates.add(Class.forName(metadataReader.getClassMetadata().getClassName()));
                }
            }
        }
        return candidates;
    }

    private String resolveBasePackage(String basePackage) {
        return ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage));
    }

    private MutableBoolean haveSuperClass(Class aClass, MutableBoolean result) {
        if (result.booleanValue()) return result;

        if (aClass.getSuperclass() != null) {
            if (aClass.getSuperclass().equals(BaseClass.class)) {
                result.setTrue();
            } else {
                haveSuperClass(aClass.getSuperclass(), result);
            }
        }
        return result;
    }

    private boolean isCandidate(MetadataReader metadataReader) throws ClassNotFoundException {
        try {
            Class c = Class.forName(metadataReader.getClassMetadata().getClassName());

            if (Modifier.isAbstract(c.getModifiers())) return false;

            return haveSuperClass(c, new MutableBoolean(false)).booleanValue();

        } catch (Throwable e) {
        }
        return false;
    }

    public Map<String, Class<? extends BaseClass>> getBiqaClasses() {
        return biqaClasses;
    }
}
