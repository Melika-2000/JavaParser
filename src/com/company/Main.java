package com.company;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        FileInputStream inputStream = new FileInputStream("C:\\Users\\MelikaZ\\IdeaProjects\\OO_Proj\\src\\com\\company\\Temp.java");
        JavaParser parser = new JavaParser();
        CompilationUnit cu = parser.parse(inputStream).getResult().orElseThrow();
        ClassOrInterfaceDeclaration classDeclaration = cu.getClassByName("Temp").get();

        //
        String packageName = cu.getPackageDeclaration().get().getNameAsString();
        String className = classDeclaration.getNameAsString();
        String classType = getType(classDeclaration);
        String classVisibility = getVisibility(classDeclaration);
        List<Class<?>> children = getChildren(Temp.class);
        ArrayList fields = getFields(Temp.class.getDeclaredFields());
        ArrayList constructors = getConstructors(Temp.class);

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

    public static List<Class<?>> getChildren(Class<?> parent) {
        List<Class<?>> children = new ArrayList<>();
        Package parentPackage = parent.getPackage();
        String packageName = parentPackage.getName();
        for (Class<?> clazz : getClasses(packageName)) {
            if (parent.isAssignableFrom(clazz) && !parent.equals(clazz)) {
                children.add(clazz);
            }
        }
        return children;
    }

    public static ArrayList getConstructors(Class<Temp> clazz){
        ArrayList result = new ArrayList();
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            int modifiers = constructor.getModifiers();
            String modifierStr = Modifier.toString(modifiers);
            result.add(modifierStr + " " + constructor.getName() + "(" + getParameterTypes(constructor) + ")");
        }
        return result;
    }

    private static ArrayList getFields(Field[] declaredFields){
        Field[] allFields = declaredFields;
        ArrayList names = new ArrayList<>();
        Arrays.stream(allFields).forEach(field -> names.add(field.getName()));
        return names;
    }

    private static List<Class<?>> getClasses(String packageName) {
        List<Class<?>> classes = new ArrayList<>();
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String path = packageName.replace('.', '/');
            for (java.net.URL resource : java.util.Collections.list(classLoader.getResources(path))) {
                for (String file : new java.io.File(resource.toURI()).list()) {
                    if (file.endsWith(".class")) {
                        String className = packageName + '.' + file.substring(0, file.length() - 6);
                        Class<?> clazz = Class.forName(className);
                        classes.add(clazz);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classes;
    }
    private static String getParameterTypes(Constructor<?> constructor) {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < parameterTypes.length; i++) {
            sb.append(parameterTypes[i].getSimpleName());

            if (i < parameterTypes.length - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}




/**
 *         File folder = new File("C:\\Users\\MelikaZ\\Documents\\Term 8\\OO Programming\\project\\5 - JUnit v3.7\\src\\junit\\tests");
 *         File[] listOfFiles = folder.listFiles(new FilenameFilter() {
 *             @Override
 *             public boolean accept(File dir, String name) {
 *                 return name.toLowerCase().endsWith(".java");
 *             }
 *         });
 *
 *         for (File file : listOfFiles) {
 *             if (file.isFile()) {
 *                 System.out.println(file.getName());
 *             }
 *         }
 */