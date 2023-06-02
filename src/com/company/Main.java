package com.company;

import java.io.IOException;
import java.util.*;

import static com.company.Utility.*;


public class Main {

    public static void main(String[] args) throws IOException {

        getOodExcels();
        getMicroserviceExcels();

    }

    private static void getOodExcels() throws IOException {
        ArrayList<String> projectsPath = new ArrayList<>();
        projectsPath.add("C:\\Users\\MelikaZ\\Documents\\Term 8\\OO Programming\\project\\1 - QuickUML 2001");
        projectsPath.add("C:\\Users\\MelikaZ\\Documents\\Term 8\\OO Programming\\project\\2 - Lexi v0.1.1 alpha");
        projectsPath.add("C:\\Users\\MelikaZ\\Documents\\Term 8\\OO Programming\\project\\3 - JRefactory v2.6.24");
        projectsPath.add("C:\\Users\\MelikaZ\\Documents\\Term 8\\OO Programming\\project\\4 - Netbeans v1.0.x");
        projectsPath.add("C:\\Users\\MelikaZ\\Documents\\Term 8\\OO Programming\\project\\5 - JUnit v3.7");
        projectsPath.add("C:\\Users\\MelikaZ\\Documents\\Term 8\\OO Programming\\project\\8 - MapperXML v1.9.7");
        projectsPath.add("C:\\Users\\MelikaZ\\Documents\\Term 8\\OO Programming\\project\\10 - Nutch v0.4");
        projectsPath.add("C:\\Users\\MelikaZ\\Documents\\Term 8\\OO Programming\\project\\11 - PMD v1.8");
        projectsPath.add("C:\\Users\\MelikaZ\\Documents\\Term 8\\OO Programming\\project\\JHotDraw v5.1");

        System.out.println("Creating OOD Excels:");
        for(int i=1; i<=projectsPath.size(); i++){
            ArrayList<ClassInfo> classesInfo = getClassesAsClassInfo(projectsPath.get(i-1));
            System.out.println("Creating excel " + i);
            writeClassInfoToExcel(classesInfo, i);
        }
    }

    private static void getMicroserviceExcels() throws IOException {
        ArrayList<String> microProjectsPath = new ArrayList<>();
        microProjectsPath.add("C:\\Users\\MelikaZ\\Documents\\Term 8\\OO Programming\\project\\Micro Proj\\axon-server-se-master");
        microProjectsPath.add("C:\\Users\\MelikaZ\\Documents\\Term 8\\OO Programming\\project\\Micro Proj\\dropwizard-release-2.1.x");
        microProjectsPath.add("C:\\Users\\MelikaZ\\Documents\\Term 8\\OO Programming\\project\\Micro Proj\\generator-jhipster-main");
        microProjectsPath.add("C:\\Users\\MelikaZ\\Documents\\Term 8\\OO Programming\\project\\Micro Proj\\hazelcast-master");
        microProjectsPath.add("C:\\Users\\MelikaZ\\Documents\\Term 8\\OO Programming\\project\\Micro Proj\\playframework-main");
        microProjectsPath.add("C:\\Users\\MelikaZ\\Documents\\Term 8\\OO Programming\\project\\Micro Proj\\quarkus-main");
        microProjectsPath.add("C:\\Users\\MelikaZ\\Documents\\Term 8\\OO Programming\\project\\Micro Proj\\servicecomb-java-chassis-master");
        microProjectsPath.add("C:\\Users\\MelikaZ\\Documents\\Term 8\\OO Programming\\project\\Micro Proj\\spring-cloud-netflix-main");
        microProjectsPath.add("C:\\Users\\MelikaZ\\Documents\\Term 8\\OO Programming\\project\\Micro Proj\\spring-cloud-sleuth-3.1.x");
        microProjectsPath.add("C:\\Users\\MelikaZ\\Documents\\Term 8\\OO Programming\\project\\Micro Proj\\vert.x-master");

        System.out.println("Creating Microservice Excels:");
        for(int i=1; i<=microProjectsPath.size(); i++){
            ArrayList<MicroserviceInfo> microInfo = getClassesAsMicroserviceInfo(microProjectsPath.get(i-1));
            System.out.println("Creating excel " + i);
            writeMicroInfoToExcel(microInfo, i);
        }
    }
}


