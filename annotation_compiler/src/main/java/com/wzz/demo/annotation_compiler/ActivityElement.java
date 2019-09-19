package com.wzz.demo.annotation_compiler;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * @author wangzhenzhou
 * @createTime 2019-09-19 13:51
 * @description Activity的注解元素
 */
public class ActivityElement {
    private TypeElement             bindContentViewElement;
    private List<VariableElement>   bindViewIDElements;
    private List<ExecutableElement> bindOnClickElements;
    private List<ExecutableElement> bindOnLongClickElements;

    public ActivityElement(TypeElement bindContentViewElement, List<VariableElement> bindViewIDElements, List<ExecutableElement> bindOnClickElements, List<ExecutableElement> bindOnLongClickElements) {
        this.bindContentViewElement = bindContentViewElement;
        this.bindViewIDElements = bindViewIDElements;
        this.bindOnClickElements = bindOnClickElements;
        this.bindOnLongClickElements = bindOnLongClickElements;
    }

    public TypeElement getBindContentViewElement() {
        return bindContentViewElement;
    }

    public void setBindContentViewElement(TypeElement bindContentViewElement) {
        this.bindContentViewElement = bindContentViewElement;
    }

    public List<VariableElement> getBindViewIDElements() {
        return bindViewIDElements;
    }

    public void setBindViewIDElements(List<VariableElement> bindViewIDElements) {
        this.bindViewIDElements = bindViewIDElements;
    }

    public List<ExecutableElement> getBindOnClickElements() {
        return bindOnClickElements;
    }

    public void setBindOnClickElements(List<ExecutableElement> bindOnClickElements) {
        this.bindOnClickElements = bindOnClickElements;
    }

    public List<ExecutableElement> getBindOnLongClickElements() {
        return bindOnLongClickElements;
    }

    public void setBindOnLongClickElements(List<ExecutableElement> bindOnLongClickElements) {
        this.bindOnLongClickElements = bindOnLongClickElements;
    }
}
