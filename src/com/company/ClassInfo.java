package com.company;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClassInfo {
    String projectName;
    String className;
    String packageName;
    String classType;
    String classVisibility;
    List<SimpleName> children;
    List<String> methods;
    List<String> fields;
    List<String> constructors;
    List<String> overrideMethods;
    boolean isStatic;
    boolean isAbstract;
    boolean isFinal;
    boolean isInterface;
    NodeList<ClassOrInterfaceType> extendedTypes;
    NodeList<ClassOrInterfaceType> implementedTypes;
    ArrayList<String> staticMethods = new ArrayList();
    ArrayList<String> abstractMethods = new ArrayList();
    ArrayList<String> finalMethods = new ArrayList<>();

    ClassInfo(File classFile) throws FileNotFoundException {
        CompilationUnit cu = StaticJavaParser.parse(classFile);
        this.projectName = getProjectName(classFile);
        this.className = getClassName(classFile);
        ClassOrInterfaceDeclaration classDeclaration = cu.getClassByName(className).get();
        this.packageName = cu.getPackageDeclaration().get().getNameAsString();
        this.classType = getType(classDeclaration);
        this.classVisibility = getVisibility(classDeclaration);
        this.children = getChildren(cu, className);
        this.fields = getFields(classDeclaration);
        this.constructors = getConstructors(classDeclaration);
        this.methods = getMethods(classDeclaration);
        this.overrideMethods = getOverrideMethods(classDeclaration);
        this.isStatic = classDeclaration.isStatic();
        this.isAbstract = classDeclaration.isAbstract();
        this.isFinal = classDeclaration.isFinal();
        this.isInterface = classDeclaration.isInterface();
        this.extendedTypes = classDeclaration.getExtendedTypes();
        this.implementedTypes = classDeclaration.getImplementedTypes();
        findMethodTypes(staticMethods, abstractMethods, finalMethods, classDeclaration);
    }

    public ArrayList getClassInfo(){

        ArrayList info = new ArrayList(
                Arrays.asList(projectName,
                        packageName,
                        className,
                        classType,
                        classVisibility,
                        isAbstract,
                        isStatic,
                        isFinal,
                        isInterface,
                        extendedTypes,
                        implementedTypes,
                        children,
                        constructors,
                        fields,
                        methods,
                        overrideMethods,
                        staticMethods,
                        finalMethods,
                        abstractMethods
                        ));
        return info;
    }

    private String getVisibility(ClassOrInterfaceDeclaration classD){
        if (classD.isPublic()) {
            return "public";
        } else if(classD.isProtected()) {
            return "protected";
        } else if(classD.isPrivate()) {
            return "private";
        }
        return "package-private";
    }

    private String getType(ClassOrInterfaceDeclaration classD){
        if (classD.isInterface()) {
            return "Interface";
        } else if(classD.isNestedType()){
            return "Nested";
        }
        return "Ordinary";
    }

    private String getProjectName(File classFile){
        Path path = Paths.get(classFile.getPath());
        String targetFolder = "project";
        boolean foundTargetFolder = false;
        String projectName = "";
        for (Path element : path) {
            if (foundTargetFolder) {
                projectName = element.toString();
                break;
            }
            if (element.toString().equalsIgnoreCase(targetFolder))
                foundTargetFolder = true;
        }
        return projectName;
    }

    private List<SimpleName> getChildren(CompilationUnit cu, String className){
        ClassOrInterfaceDeclaration currentClass = cu.getClassByName(className).get();
        List<SimpleName> childClasses = new ArrayList<>();
        cu.findAll(ClassOrInterfaceDeclaration.class).forEach(classDeclaration -> {
            if (classDeclaration.getExtendedTypes().stream()
                    .anyMatch(ext -> ext.getNameAsString().equals(currentClass.getNameAsString()))) {
                childClasses.add(classDeclaration.getName());
            }
        });
        return childClasses;
    }

    private ArrayList<String> getConstructors(ClassOrInterfaceDeclaration classDeclaration){
        ArrayList<String> constructorsInfo = new ArrayList<>();
        List<ConstructorDeclaration> constructors = classDeclaration.getConstructors();
        for (ConstructorDeclaration constructor : constructors) {
            constructorsInfo.add(constructor.getDeclarationAsString());
        }
        return constructorsInfo;
    }

    private List<String> getFields(ClassOrInterfaceDeclaration classDeclaration) {
        List<String> fieldList = new ArrayList<>();
        List<FieldDeclaration> fields = classDeclaration.getFields();
        for (FieldDeclaration field : fields) {
            String type = field.getCommonType().toString();
            String name = field.getVariable(0).getNameAsString();
            fieldList.add(type + " " + name);
        }
        return fieldList;
    }

    private String getClassName(File classFile){
        return classFile.getName().replace(".java", "").replace('$', '.');
    }

    private List<String> getMethods(ClassOrInterfaceDeclaration classDeclaration){
        List<String> methodsInfo = new ArrayList<>();
        List<MethodDeclaration> methods = classDeclaration.getMethods();
        for(MethodDeclaration method : methods){
            String info = "Method name: " + method.getNameAsString() +
                    " - Parameters: " + method.getParameters() +
                    " - Return Type: " + method.getType();
            methodsInfo.add(info);
        }
        return methodsInfo;
    }

    private void findMethodTypes(
            ArrayList<String> staticMethods,
            ArrayList<String> abstractMethods,
            ArrayList<String> finalMethods,
            ClassOrInterfaceDeclaration classDeclaration
    ){
        List<MethodDeclaration> methods = classDeclaration.getMethods();
        for(MethodDeclaration method : methods){
            if (method.isAbstract()){
                abstractMethods.add(method.getNameAsString());
            }
            else if (method.isFinal()){
                finalMethods.add(method.getNameAsString());
            }
            else if (method.isStatic()){
                staticMethods.add(method.getNameAsString());
            }
        }
    }

    private List<String> getOverrideMethods(ClassOrInterfaceDeclaration classDeclaration) {
        List<String> overrideMethods = new ArrayList<>();

        for (MethodDeclaration method : classDeclaration.getMethods()) {
            if(method.getAnnotationByName("Override").isPresent()){
                String result = "override method: " + method.getName();
                result += " -> parent class: " + classDeclaration.getExtendedTypes().toString();
                overrideMethods.add(result);
            }
        }
        return overrideMethods;
    }
}
