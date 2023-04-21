package com.company;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

       // String projectP1 = "C:\\Users\\MelikaZ\\Documents\\Term 8\\OO Programming\\project\\4 - Netbeans v1.0.x\\src\\org\\openide\\TopManager.java";
        String projectP1 = "C:\\Users\\MelikaZ\\IdeaProjects\\OO_Proj\\src\\com\\company\\Temp.java";
        ArrayList<File> classFile = getClassesAsFiles(projectP1);
        CompilationUnit cu = StaticJavaParser.parse(classFile.get(0));
        String className = getClassName(classFile.get(0));
        ClassOrInterfaceDeclaration classDeclaration = cu.getClassByName(className).get();

        //project name?
        String packageName = cu.getPackageDeclaration().get().getNameAsString();
        String classType = getType(classDeclaration);
        String classVisibility = getVisibility(classDeclaration);
        List<SimpleName> children = getChildren(cu, className);
        List<String> fields = getFields(classDeclaration);
        List<String> constructors = getConstructors(classDeclaration);
        String methods = getMethods(classDeclaration);
        List<String> overrideMethods = getOverrideMethods(classDeclaration);

        boolean isStatic = classDeclaration.isStatic();
        boolean isAbstract = classDeclaration.isAbstract();
        boolean isFinal = classDeclaration.isFinal();
        boolean isInterface = classDeclaration.isInterface();

        NodeList<ClassOrInterfaceType> extendedTypes = classDeclaration.getExtendedTypes();
        NodeList<ClassOrInterfaceType> implementedTypes = classDeclaration.getImplementedTypes();

        ArrayList<String> staticMethods = new ArrayList();
        ArrayList<String> abstractMethods = new ArrayList();
        ArrayList<String> finalMethods = new ArrayList<>();
        findMethodTypes(staticMethods, abstractMethods, finalMethods, classDeclaration);

        System.out.println("Class Name: " + className);
        System.out.println("Package Name: " + packageName);
        System.out.println("classType: " + classType);
        System.out.println("classVisibility: " + classVisibility);
        System.out.println("extendedTypes: " + extendedTypes);
        System.out.println("implementedTypes: " + implementedTypes);
        System.out.println("isStatic: " + isStatic);
        System.out.println("isAbstract: " + isAbstract);
        System.out.println("isFinal: " + isFinal);
        System.out.println("isInterface: " + isInterface);
        System.out.println("children: " + children);
        System.out.println("constructors: " + constructors);
        System.out.println("fields: " + fields);
        System.out.println("Methods: \n" + methods);
        System.out.println("overrideMethods: " + overrideMethods);
        System.out.println("static methods: " + staticMethods);
        System.out.println("final methods: " + finalMethods);
        System.out.println("abstract methods: " + abstractMethods);


    }

    public static String getVisibility(ClassOrInterfaceDeclaration classD){
        if (classD.isPublic()) {
            return "public";
        } else if(classD.isProtected()) {
            return "protected";
        } else if(classD.isPrivate()) {
            return "private";
        }
        return "package-private";
    }

    public static String getType(ClassOrInterfaceDeclaration classD){
        if (classD.isInterface()) {
            return "Interface";
        } else if(classD.isNestedType()){
            return "Nested";
        }
        return "Ordinary";
    }

    public static List<SimpleName> getChildren(CompilationUnit cu, String className){
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

    public static ArrayList<String> getConstructors(ClassOrInterfaceDeclaration classDeclaration){
        ArrayList<String> constructorsInfo = new ArrayList<>();
        List<ConstructorDeclaration> constructors = classDeclaration.getConstructors();
        for (ConstructorDeclaration constructor : constructors) {
            constructorsInfo.add(constructor.getDeclarationAsString());
        }
        return constructorsInfo;
    }

    public static List<String> getFields(ClassOrInterfaceDeclaration classDeclaration) {
        List<String> fieldList = new ArrayList<>();
        List<FieldDeclaration> fields = classDeclaration.getFields();
        for (FieldDeclaration field : fields) {
            String type = field.getCommonType().toString();
            String name = field.getVariable(0).getNameAsString();
            fieldList.add(type + " " + name);
        }
        return fieldList;
    }

    public static ArrayList<File> getClassesAsFiles(String path){
        File file = new File(path);
        Stack<File> s = new Stack<>();
        ArrayList<File> files = new ArrayList<>();
        s.push(file);
        while (!s.empty()) {
            File tmpF = s.pop();
            if (tmpF.isFile() && tmpF.getName().endsWith(".java")) {
                files.add(tmpF);
            } else if (tmpF.isDirectory()) {
                File[] f = tmpF.listFiles();
                for (File fpp : f) {
                    s.push(fpp);
                }
            }
        }
        return files;
    }

    public static String getClassName(File classFile){
        return classFile.getName().replace(".java", "").replace('$', '.');
    }

    public static String getMethods(ClassOrInterfaceDeclaration classDeclaration){
        String result = "";
        List<MethodDeclaration> methods = classDeclaration.getMethods();
        for(MethodDeclaration method : methods){
            result += "Method name: " + method.getNameAsString() +
                    ", Parameters: " + method.getParameters() +
                    ", Return Type: " + method.getType() + "\n";
        }
        return result;
    }

    public static void findMethodTypes(
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

    public static List<String> getOverrideMethods(ClassOrInterfaceDeclaration classDeclaration) {
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






