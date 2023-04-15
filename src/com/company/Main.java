package com.company;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        String projectP1 = "C:\\Users\\MelikaZ\\Documents\\Term 8\\OO Programming\\project\\4 - Netbeans v1.0.x\\src\\org\\openide\\TopManager.java";
        ArrayList<File> classFile = getClassesAsFiles(projectP1);
        CompilationUnit cu = StaticJavaParser.parse(classFile.get(0));
        String className = getClassName(classFile.get(0));
        ClassOrInterfaceDeclaration classDeclaration = cu.getClassByName(className).get();

        //
        String packageName = cu.getPackageDeclaration().get().getNameAsString();
        String classType = getType(classDeclaration);
        String classVisibility = getVisibility(classDeclaration);
        List<SimpleName> children = getChildren(cu, className);
        List<String> fields = getFields(classDeclaration);
        String constructors = getConstructors(classDeclaration);

        boolean isStatic = classDeclaration.isStatic();
        boolean isAbstract = classDeclaration.isAbstract();
        boolean isFinal = classDeclaration.isFinal();
        boolean isInterface = classDeclaration.isInterface();

        NodeList<ClassOrInterfaceType> extendedTypes = classDeclaration.getExtendedTypes();
        NodeList<ClassOrInterfaceType> implementedTypes = classDeclaration.getImplementedTypes();

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
        System.out.println("fields: " + fields);
        System.out.println("constructors: " + constructors);

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

    public static String getConstructors(ClassOrInterfaceDeclaration classDeclaration){
        String result = "";
        List<ConstructorDeclaration> constructors = classDeclaration.getConstructors();
        for (ConstructorDeclaration constructor : constructors) {
            String name = constructor.getName().asString();
            NodeList<com.github.javaparser.ast.body.Parameter> parameters = constructor.getParameters();
            result += "[" + name + " , Parameters: ";
            for (Parameter parameter : parameters) {
                String paramName = parameter.getName().asString();
                String paramType = parameter.getType().asString();
                result += paramType + " " + paramName + " , ";
            }
            result += "]";
        }
        return result;
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
}






