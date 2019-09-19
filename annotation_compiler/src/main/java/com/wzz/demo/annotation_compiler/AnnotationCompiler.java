package com.wzz.demo.annotation_compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.wzz.demo.annotations.BindContentView;
import com.wzz.demo.annotations.BindOnClick;
import com.wzz.demo.annotations.BindOnLongClick;
import com.wzz.demo.annotations.BindViewID;
import com.wzz.demo.annotations.IBinder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * 注解处理器
 *
 * @author wangzhenzhou
 * @createTime 2019-09-06 17:41
 */
@AutoService(Processor.class)
public class AnnotationCompiler extends AbstractProcessor {
    /**
     * 确定当前APT处理所有模块中的哪些注解
     *
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> supportAnnotationSet = new HashSet<>();
        supportAnnotationSet.add(BindContentView.class.getCanonicalName());
        supportAnnotationSet.add(BindOnClick.class.getCanonicalName());
        supportAnnotationSet.add(BindOnLongClick.class.getCanonicalName());
        supportAnnotationSet.add(BindViewID.class.getCanonicalName());
        return supportAnnotationSet;
    }

    /**
     * 支持的JDK版本
     *
     * @return
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    /**
     * 该方法中生成IBinder的实现类
     *
     * @param set
     * @param roundEnvironment
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        //Element
        //TypeElement:类元素
        //ExecutableElement:可执行元素
        //VariableElement:属性元素

        List<ActivityElement> activityElementList = ActivityElementUtils.collect(roundEnvironment);

        for (ActivityElement activityElement : activityElementList) {
            TypeElement             bindContentViewElement  = activityElement.getBindContentViewElement();
            List<VariableElement>   bindViewIDElements      = activityElement.getBindViewIDElements();
            List<ExecutableElement> bindOnClickElements     = activityElement.getBindOnClickElements();
            List<ExecutableElement> bindOnLongClickElements = activityElement.getBindOnLongClickElements();
            writeSingleActivityByPoet(bindContentViewElement, bindViewIDElements, bindOnClickElements, bindOnLongClickElements);
        }
        return false;
    }

    /**
     * 生成 $activityName_ViewBinding文件
     *
     * @param bindContentViewElement
     * @param bindViewIDElements
     * @param bindOnClickElements
     * @param bindOnLongClickElements
     */
    private void writeSingleActivityByPoet(TypeElement bindContentViewElement, List<VariableElement> bindViewIDElements,
                                           List<ExecutableElement> bindOnClickElements, List<ExecutableElement> bindOnLongClickElements) {
        try {
            String packageName  = processingEnv.getElementUtils().getPackageOf(bindContentViewElement).toString();
            String activityName = bindContentViewElement.getSimpleName().toString();

            //获取到用户Activity的ClassName
            ClassName target = ClassName.get(packageName, activityName);

            //构建方法
            MethodSpec.Builder bindBuilder = MethodSpec.methodBuilder("bind")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(void.class)
                    .addParameter(target, "target", Modifier.FINAL);

            //setContentView
            bindBuilder.addStatement("target.setContentView($L)", bindContentViewElement.getAnnotation(BindContentView.class).value());

            //findViewById
            /**
             * JavaPoet中的类型替换
             * $L   常量，相当于java中的%i
             * $S   字符串，相当于%s
             * $T   泛型 ，相当于T
             * $N   函数名，用来调用函数,用法如下i=getNum();
             * addStatement("i = $N()", getNum)
             */
            for (VariableElement variableElement : bindViewIDElements) {
                bindBuilder.addStatement("target.$L = ($T) target.findViewById($L)",
                        variableElement.getSimpleName().toString(),
                        ClassName.get(variableElement.asType()),
                        variableElement.getAnnotation(BindViewID.class).value());
            }

            //setOnClickListener
            ClassName View = ClassName.get("android.view", "View");
            for (ExecutableElement bindOnClickElement : bindOnClickElements) {
                int[] ids = bindOnClickElement.getAnnotation(BindOnClick.class).value();
                for (int id : ids) {
                    bindBuilder.addStatement("target.findViewById($L).setOnClickListener(new $T.OnClickListener() {\n" +
                                    "\t\t\t@Override\n" +
                                    "\t\t\tpublic void onClick(View v) {\n" +
                                    "\t\t\t\ttarget.$N(v);\n" +
                                    "\t\t\t}\n" +
                                    "\t\t});",
                            id,
                            View,
                            bindOnClickElement.getSimpleName());
                }
            }

            //setOnLongClickListener
            for (ExecutableElement bindOnLongClickElement : bindOnLongClickElements) {
                int[] ids = bindOnLongClickElement.getAnnotation(BindOnLongClick.class).value();
                for (int id : ids) {
                    bindBuilder.addStatement("target.findViewById($L).setOnLongClickListener(new $T.OnLongClickListener() {\n" +
                                    "\t\t\t@Override\n" +
                                    "\t\t\tpublic boolean onLongClick(View v) {\n" +
                                    "\t\t\t\ttarget.$N(v);\n" +
                                    "\t\t\t\treturn true;\n" +
                                    "\t\t\t}\n" +
                                    "\t\t});",
                            id,
                            View,
                            bindOnLongClickElement.getSimpleName());
                }
            }

            //方法配置完成，生成方法
            MethodSpec bind = bindBuilder.build();

            //构建$activityName_ViewBinding class
            TypeSpec viewBinding = TypeSpec.classBuilder(activityName + "_ViewBinding")
                    .addModifiers(Modifier.PUBLIC)
                    .addSuperinterface(ParameterizedTypeName.get(ClassName.get(IBinder.class), target)) //使class实现接口并定义接口泛型类型
                    .addMethod(bind)
                    .build();

            //生成java文件
            JavaFile javaFile = JavaFile.builder(packageName, viewBinding)
                    .addFileComment(" This codes are generated automatically. Do not modify!")
                    .build();

            //指定java文件写出的路径
            Filer filer = processingEnv.getFiler();
            javaFile.writeTo(filer);

            //            File file = new File("build/generated/source/apt/");
            //            if (!file.exists()) {
            //                file.mkdirs();
            //            }
            //            javaFile.writeTo(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
