package cn.bingoogolapple.aptnote.compiler;

import com.google.auto.service.AutoService;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

import cn.bingoogolapple.aptnote.annotation.VariableAnnotation;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:2017/9/20
 * 描述:
 */
@SuppressWarnings("unused")
@AutoService(Processor.class)
public class VariableProcessor extends AbstractProcessor {
    private Filer mFileUtils;
    private Elements mElementUtils;
    private Messager mMessager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFileUtils = processingEnv.getFiler();
        mElementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();

        mMessager.printMessage(Diagnostic.Kind.NOTE, "====================================== VariableProcessor init ======================================");
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        mMessager.printMessage(Diagnostic.Kind.NOTE, "====================================== VariableProcessor getSupportedSourceVersion ======================================");
        return SourceVersion.latestSupported();
    }

    /**
     * 告知 Processor 哪些注解需要处理
     *
     * @return 返回一个 Set 集合，集合内容为自定义注解的包名+类名
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        mMessager.printMessage(Diagnostic.Kind.NOTE, "====================================== VariableProcessor getSupportedAnnotationTypes ======================================");
        final Set<String> annotationTypes = new LinkedHashSet<>();
        annotationTypes.add(VariableAnnotation.class.getCanonicalName());
        return annotationTypes;
    }

    /**
     * 所有的注解处理都是从这个方法开始的，当 APT 找到所有需要处理的注解后，会回调这个方法。当没有属于该 Processor 处理的注解被使用时，不会回调该方法
     *
     * @param set              所有的由该 Processor 处理，并待处理的 Annotations「属于该 Processor 处理的注解，但并未被使用，不存在与这个集合里」
     * @param roundEnvironment 表示当前或是之前的运行环境，可以通过该对象查找到注解
     * @return 表示这组 Annotation 是否被这个 Processor 消费，如果消费「返回 true」后续子的 Processor 不会再对这组 Annotation 进行处理
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, "====================================== VariableProcessor process END ======================================");

        for (Element element : roundEnvironment.getElementsAnnotatedWith(VariableAnnotation.class)) {
            VariableElement variableElement = (VariableElement) element;

            TypeElement classElement = (TypeElement) element.getEnclosingElement();
            PackageElement packageElement = mElementUtils.getPackageOf(classElement);

            // 全类名
            String fullClassName = classElement.getQualifiedName().toString();
            // 类名
            String className = classElement.getSimpleName().toString();
            // 包名
            String packageName = packageElement.getQualifiedName().toString();
            // 父类名
            String superClassName = classElement.getSuperclass().toString();
            // 获取该注解的值
            VariableAnnotation variableAnnotation = variableElement.getAnnotation(VariableAnnotation.class);
            String value = variableAnnotation.value();

            mMessager.printMessage(Diagnostic.Kind.NOTE, "VariableProcessor : packageName = " + packageName);
            mMessager.printMessage(Diagnostic.Kind.NOTE, "VariableProcessor : className = " + className);
            mMessager.printMessage(Diagnostic.Kind.NOTE, "VariableProcessor : fullClassName = " + fullClassName);
            mMessager.printMessage(Diagnostic.Kind.NOTE, "VariableProcessor : superClassName = " + superClassName);
            mMessager.printMessage(Diagnostic.Kind.NOTE, "VariableProcessor : value = " + value);


            // 类成员名
            String variableName = variableElement.getSimpleName().toString();
            // 类成员类型
            TypeMirror typeMirror = variableElement.asType();
            String type = typeMirror.toString();

            mMessager.printMessage(Diagnostic.Kind.NOTE, "VariableProcessor : variableName = " + variableName);
            mMessager.printMessage(Diagnostic.Kind.NOTE, "VariableProcessor : type = " + type);
        }

        mMessager.printMessage(Diagnostic.Kind.NOTE, "====================================== VariableProcessor process END ======================================");

        return true;
    }
}
