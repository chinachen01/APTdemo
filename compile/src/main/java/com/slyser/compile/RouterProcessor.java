package com.slyser.compile;

import com.google.auto.service.AutoService;

import com.slyser.annotation.IRouterGroup;
import com.slyser.annotation.Router;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * author: chenyong(<a href="chenyong@danlu.com">chenyong@danlu.com</a>)<br/>
 * version: 1.0.0<br/>
 * since: 2018/7/24 下午7:54<br/>
 *
 * <p>
 * 内容描述区域
 * </p>
 */
@AutoService(Processor.class)
public class RouterProcessor extends AbstractProcessor {
    private Filer filer;
    private Logger logger;
    private Elements elements;
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
        logger = new Logger(processingEnvironment.getMessager());
        elements = processingEnvironment.getElementUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new LinkedHashSet<>();
        set.add(Router.class.getCanonicalName());
        return set;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (set == null || set.isEmpty()) {
            return false;
        }
        //获取所有包含 Router 注解的 class
        Set<? extends Element> classSet = roundEnvironment.getElementsAnnotatedWith(Router.class);
        Map<String, String> nameMap = new HashMap<>();
        for (Element element : classSet) {
            Router router = element.getAnnotation(Router.class);
            String name = router.name();
            nameMap.put(name, elements.getPackageOf(element).toString() + "." + element.getSimpleName());
        }
        try {
            logger.info("创建 class");
            generateClass(nameMap);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void generateClass(Map<String, String> nameMap) throws IOException {
        TypeName map = ParameterizedTypeName.get(Map.class, String.class, String.class);
        FieldSpec routeMap = FieldSpec.builder(map, "routeMap", Modifier.PRIVATE)
                .build();
        MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addStatement("routeMap = new $T<>()", HashMap.class);
        for (Map.Entry<String, String> entry : nameMap.entrySet()) {
            constructorBuilder.addStatement("routeMap.put($S, $S)", entry.getKey(), entry.getValue());
        }
        MethodSpec constructor = constructorBuilder.build();
        MethodSpec getActivityName = MethodSpec.methodBuilder("getActivityName")
                .returns(String.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String.class, "routeName")
                .beginControlFlow("if (null != routeMap && !routeMap.isEmpty())")
                .addStatement("return routeMap.get(routeName)")
                .endControlFlow()
                .addStatement("return \"\"")
                .build();
        TypeSpec router = TypeSpec.classBuilder("Router$$GroupApp")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addField(routeMap)
                .addMethod(constructor)
                .addMethod(getActivityName)
                .addSuperinterface(IRouterGroup.class)
                .build();

        // 这里的包名如果是做组件化需要加 module
        JavaFile javaFile = JavaFile.builder("com.slyser.router", router)
                .build();
        javaFile.writeTo(filer);
    }
}
