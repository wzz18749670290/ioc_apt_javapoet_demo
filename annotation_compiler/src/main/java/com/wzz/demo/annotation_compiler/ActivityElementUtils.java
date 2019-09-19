package com.wzz.demo.annotation_compiler;

import com.wzz.demo.annotations.BindContentView;
import com.wzz.demo.annotations.BindOnClick;
import com.wzz.demo.annotations.BindOnLongClick;
import com.wzz.demo.annotations.BindViewID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * @author wangzhenzhou
 * @createTime 2019-09-19 14:27
 * @description Activity的注解元素处理工具类
 */
public class ActivityElementUtils {
    /**
     * 将所有的BindContentView、BindViewID、BindOnClick、BindOnLongClick注解map转换为以activity为key，以ActivityElement为value的映射
     *
     * @param roundEnvironment
     * @return
     */
    public static List<ActivityElement> collect(RoundEnvironment roundEnvironment) {
        Map<String, TypeElement>             bindContentViewMap = getBindContentViewMap(roundEnvironment);
        Map<String, List<VariableElement>>   bindViewIDMap      = getBindViewIDMap(roundEnvironment);
        Map<String, List<ExecutableElement>> bindOnClickMap     = getBindOnClickMap(roundEnvironment);
        Map<String, List<ExecutableElement>> bindOnLongClickMap = getBindOnLongClickMap(roundEnvironment);

        List<ActivityElement> activityElementList = new ArrayList<>();

        Set<String> activitySets = bindContentViewMap.keySet();
        for (String activityKey : activitySets) {
            ActivityElement activityElement = new ActivityElement(bindContentViewMap.get(activityKey), bindViewIDMap.get(activityKey), bindOnClickMap.get(activityKey), bindOnLongClickMap.get(activityKey));
            activityElementList.add(activityElement);
        }
        return activityElementList;
    }

    /**
     * 获取BindContentView注解的类集合，并按照类名(activity)分类
     *
     * @param roundEnvironment
     * @return
     */
    private static Map<String, TypeElement> getBindContentViewMap(RoundEnvironment roundEnvironment) {
        //得到程序中所有写了BindView注解的元素的集合
        Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(BindContentView.class);
        //定义一个MAP用于分类
        Map<String, TypeElement> map = new HashMap<>();
        for (Element element : elementsAnnotatedWith) {
            //BindContentView注解的作用域是class，所以此处可以强转
            TypeElement typeElement = (TypeElement) element;
            //获取activity的名字
            String activityName = typeElement.getSimpleName().toString();
            if (activityName != null) {
                map.put(activityName, typeElement);
            }
        }
        return map;
    }

    /**
     * 获取BindOnClick注解的方法集合，并按照类名(activity)分类
     *
     * @param roundEnvironment
     * @return
     */
    private static Map<String, List<ExecutableElement>> getBindOnClickMap(RoundEnvironment roundEnvironment) {
        //得到程序中所有写了BindOnClick注解的元素的集合
        Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(BindOnClick.class);
        //定义一个MAP用于分类
        Map<String, List<ExecutableElement>> map = new HashMap<>();
        for (Element element : elementsAnnotatedWith) {
            //BindOnClick注解的作用域是Method，所以此处可以强转
            ExecutableElement executableElement = (ExecutableElement) element;
            //获取activity的名字
            String activityName = executableElement.getEnclosingElement().getSimpleName().toString();
            if (activityName != null) {
                List<ExecutableElement> elementList = map.get(activityName);
                if (elementList == null) {
                    elementList = new ArrayList();
                    map.put(activityName, elementList);
                }
                elementList.add(executableElement);
            }
        }
        return map;
    }

    /**
     * 获取BindOnLongClick注解的方法集合，并按照类名(activity)分类
     *
     * @param roundEnvironment
     * @return
     */
    private static Map<String, List<ExecutableElement>> getBindOnLongClickMap(RoundEnvironment roundEnvironment) {
        //得到程序中所有写了BindOnLongClick注解的元素的集合
        Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(BindOnLongClick.class);
        //定义一个MAP用于分类
        Map<String, List<ExecutableElement>> map = new HashMap<>();
        for (Element element : elementsAnnotatedWith) {
            //BindOnLongClick注解的作用域是Method，所以此处可以强转
            ExecutableElement executableElement = (ExecutableElement) element;
            //获取activity的名字
            String activityName = executableElement.getEnclosingElement().getSimpleName().toString();
            if (activityName != null) {
                List<ExecutableElement> elementList = map.get(activityName);
                if (elementList == null) {
                    elementList = new ArrayList();
                    map.put(activityName, elementList);
                }
                elementList.add(executableElement);
            }
        }
        return map;
    }

    /**
     * 获取BindViewID注解的变量集合，并按照类名(activity)分类
     *
     * @param roundEnvironment
     * @return
     */
    private static Map<String, List<VariableElement>> getBindViewIDMap(RoundEnvironment roundEnvironment) {
        //得到程序中所有写了BindView注解的元素的集合
        Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(BindViewID.class);
        //定义一个MAP用于分类
        Map<String, List<VariableElement>> map = new HashMap<>();
        for (Element element : elementsAnnotatedWith) {
            //BindView注解的作用域是FIELD，所以此处可以强转
            VariableElement variableElement = (VariableElement) element;
            //获取activity的名字
            String activityName = variableElement.getEnclosingElement().getSimpleName().toString();
            if (activityName != null) {
                List<VariableElement> elementList = map.get(activityName);
                if (elementList == null) {
                    elementList = new ArrayList();
                    map.put(activityName, elementList);
                }
                elementList.add(variableElement);
            }
        }
        return map;
    }
}
