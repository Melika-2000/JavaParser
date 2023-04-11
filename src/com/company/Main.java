package com.company;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        FileInputStream inputStream = new FileInputStream("C:\\Users\\MelikaZ\\IdeaProjects\\OO_Proj\\src\\com\\company\\Temp.java");JavaParser parser = new JavaParser();
        CompilationUnit cu = parser.parse(inputStream).getResult().orElseThrow();
        ClassOrInterfaceDeclaration classDeclaration = cu.getClassByName("Temp").get();

        //
        String packageName = cu.getPackageDeclaration().get().getNameAsString();
        String className = classDeclaration.getNameAsString();
        String classType = getType(classDeclaration);
        String classVisibility = getVisibility(classDeclaration);

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
}
