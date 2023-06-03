package com.company;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MicroserviceInfo {
    String className;
    List<String> postMethods = new ArrayList<>();
    List<String> putMethods = new ArrayList<>();
    List<String> getMethods = new ArrayList<>();
    List<String> deleteMethods = new ArrayList<>();

    MicroserviceInfo(ClassOrInterfaceDeclaration clDeclaration){
        className = clDeclaration.getNameAsString();
        getApiMethods(clDeclaration);
    }

    private void getApiMethods(ClassOrInterfaceDeclaration classDeclaration) {
        for (MethodDeclaration method : classDeclaration.getMethods()) {

            if(method.getAnnotationByName("POST").isPresent() ||
                    (method.getAnnotationByName("RequestMapping").isPresent() &&
                            method.getAnnotations().toString().contains("POST"))){
                String methodName = method.getName().asString() +
                        " - Parameters: " + method.getParameters() ;
                postMethods.add(methodName);
            }

            else if(method.getAnnotationByName("PUT").isPresent() ||
                    (method.getAnnotationByName("RequestMapping").isPresent() &&
                            method.getAnnotations().toString().contains("PUT"))){
                String methodName = method.getName().asString() +
                        " - Parameters: " + method.getParameters() + " ";
                putMethods.add(methodName);
            }

            else if(method.getAnnotationByName("GET").isPresent() ||
                    (method.getAnnotationByName("RequestMapping").isPresent() &&
                            method.getAnnotations().toString().contains("GET"))){
                String methodName = method.getName().asString() +
                        " - Parameters: " + method.getParameters() + " ";
                getMethods.add(methodName);
            }

            else if(method.getAnnotationByName("DELETE").isPresent() ||
                    (method.getAnnotationByName("RequestMapping").isPresent() &&
                            method.getAnnotations().toString().contains("DELETE"))){
                String methodName = method.getName().asString() +
                        " - Parameters: " + method.getParameters() + " " ;
                deleteMethods.add(methodName);
            }
        }
    }

    public ArrayList getClassInfo(){
        ArrayList info = new ArrayList(
                Arrays.asList( className,
                        postMethods,
                        putMethods,
                        getMethods,
                        deleteMethods
                ));
        return info;
    }


}
