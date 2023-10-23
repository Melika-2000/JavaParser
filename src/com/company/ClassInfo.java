package com.company;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClassInfo {
    public static final int CLASS_TYPE_ORDINARY = 1;
    public static final int CLASS_TYPE_INTERFACE = 2;
    public static final int CLASS_TYPE_NESTED = 3;
    public static final int CLASS_VISIBILITY_PUBLIC = 1;
    public static final int CLASS_VISIBILITY_PROTECTED = 3;
    public static final int CLASS_VISIBILITY_PRIVATE = 2;
    private String projectName;
    private  String className;
    private String packageName;
    private int classType;
    private int classVisibility;
    private int isStatic;
    private int isAbstract;
    private int isFinal;
    private int isInterface;
    private List<String> methods;
    private List<String> fields;
    private List<String> constructors;
    private List<String> instantiations;
    private NodeList<ClassOrInterfaceType> extendedTypes;
    private NodeList<ClassOrInterfaceType> implementedTypes;
    private ArrayList<String> staticMethods = new ArrayList();
    private ArrayList<String> abstractMethods = new ArrayList();
    private ArrayList<String> finalMethods = new ArrayList<>();
    private ArrayList<String> children = new ArrayList<>();
    private ArrayList<String> overrideMethods = new ArrayList<>();
    private ClassOrInterfaceDeclaration classDeclaration;

    ClassInfo(File classFile, ClassOrInterfaceDeclaration clDeclaration) throws FileNotFoundException {
        CompilationUnit cu = StaticJavaParser.parse(classFile);
        projectName = getProjectName(classFile);
        className = clDeclaration.getNameAsString();
        classDeclaration = clDeclaration;
        packageName = getPackageName(cu);
        classType = getType(classDeclaration);
        classVisibility = getVisibility(classDeclaration);
        fields = getFields(classDeclaration);
        constructors = getConstructors(classDeclaration);
        methods = getMethods(classDeclaration);
        isStatic = (classDeclaration.isStatic())? 1 : 0;
        isAbstract = (classDeclaration.isAbstract())? 1 : 0;
        isFinal = (classDeclaration.isFinal())? 1 : 0;
        isInterface = (classDeclaration.isInterface())? 1 : 0;
        extendedTypes = classDeclaration.getExtendedTypes();
        implementedTypes = classDeclaration.getImplementedTypes();
        instantiations = getInstantiations(cu);
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
                        abstractMethods,
                        instantiations
                        ));
        return info;
    }

    private String getPackageName(CompilationUnit cu){
        if(cu.getPackageDeclaration().isEmpty())
            return " ";
        return cu.getPackageDeclaration().get().getNameAsString();
    }

    private int getVisibility(ClassOrInterfaceDeclaration classD){
        if(classD.isProtected())
            return CLASS_VISIBILITY_PROTECTED;
        else if(classD.isPrivate())
            return CLASS_VISIBILITY_PRIVATE;
        return CLASS_VISIBILITY_PUBLIC;
    }

    private int getType(ClassOrInterfaceDeclaration classD){
        if (classD.isInterface())
            return CLASS_TYPE_INTERFACE;
        else if(classD.isNestedType())
            return CLASS_TYPE_NESTED;
        return CLASS_TYPE_ORDINARY;
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

    private void addChildren(ClassInfo child){
        children.add(child.className);
    }

    public void setChildren(ArrayList<ClassInfo> classesInfo){
        for(ClassOrInterfaceType parent : extendedTypes){
            for(ClassInfo clazz : classesInfo){
                boolean parentClassFound = clazz.className.equals(parent.getName().asString());
                if(parentClassFound){
                    setOverrideMethods(clazz);
                    clazz.addChildren(this);
                    break;
                }
            }
        }
    }

    private void setOverrideMethods(ClassInfo clazz){
        for(String method : methods){
            String methodName = extractMethodName(method);
            List<String> parentMethods = clazz.methods;
            for(String parentMethod : parentMethods){
                String parentMethodName = extractMethodName(parentMethod);
                if(methodName.equals(parentMethodName)){
                    overrideMethods.add(methodName + " parent: " + clazz.className);
                }
            }
        }
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

    private List<String> getInstantiations(CompilationUnit cu){
        List<String> insList = new ArrayList<>();
        List<ObjectCreationExpr> instantiations = cu.findAll(ObjectCreationExpr.class);
        for(ObjectCreationExpr ins : instantiations){
            boolean classAlreadyAdded = insList.contains(ins.getTypeAsString());
            if(!classAlreadyAdded)
                insList.add(ins.getTypeAsString());
        }
        return insList;
    }

    public ArrayList getRelationships(ArrayList<ClassInfo> classesInfo){
        ArrayList allRelationships = new ArrayList();
        ArrayList<String> associations = new ArrayList();
        ArrayList<String> delegations = new ArrayList();
        ArrayList<String> compositionAggregation = new ArrayList();
        for(int i=0; i<classesInfo.size(); i++) {
            try {
                ClassInfo selectedClass = classesInfo.get(i);
                boolean selectedClassIsNotThisClass = !this.className.equals(selectedClass.className);
                if (selectedClassIsNotThisClass) {
                    if (hasDelegationRelationship(selectedClass))
                        delegations.add(selectedClass.className);
                    if (hasAssociationRelationship(selectedClass)) {
                        associations.add(selectedClass.className);
                        if (hasCompositionAggregation(selectedClass))
                            compositionAggregation.add(selectedClass.className);
                    }
                }

            } catch (Exception e){}
        }
        allRelationships.add(associations);
        allRelationships.add(compositionAggregation);
        allRelationships.add(delegations);
        return allRelationships;
    }

    private boolean hasAssociationRelationship(ClassInfo selectedClass){
        if(instantiations.contains(selectedClass.className) ||
                selectedClass.instantiations.contains(this.className)) {
            return true;
        }
        for(String field : fields) {
            if (field.contains(selectedClass.className))
                return true;
        }
        for(String field : selectedClass.fields) {
            if (field.contains(this.className))
                return true;
        }
        return false;
    }

    private boolean hasDelegationRelationship(ClassInfo selectedClass){
        for(String field : fields){
            if(field.contains(selectedClass.className)){
                for(String method : methods){
                    for(String selectedClassMethod : selectedClass.methods){
                        String selectedClassMethodName = extractMethodName(selectedClassMethod);
                        if(method.contains(selectedClassMethodName))
                            return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean hasCompositionAggregation(ClassInfo selectedClass){
        List<ConstructorDeclaration> constructors = classDeclaration.getConstructors();
        for (ConstructorDeclaration constructor : constructors) {
            if(constructor.getBody().toString().contains(selectedClass.className))
                return true;
        }
        List<ConstructorDeclaration> selectedClassConstructors = selectedClass.classDeclaration.getConstructors();
        for (ConstructorDeclaration constructor : selectedClassConstructors) {
            if(constructor.getBody().toString().contains(className))
                return true;
        }
        return false;
    }

    private String extractMethodName(String methodInfo){
        methodInfo = methodInfo.substring(methodInfo.indexOf(":") + 1);
        String methodName = methodInfo.substring(0, methodInfo.indexOf("-"));
        return methodName;
    }

}
