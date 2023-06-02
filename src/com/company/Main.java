package com.company;

import java.io.File;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.*;

import static com.company.Utility.getClassesAsClassInfo;
import static com.company.Utility.writeToExcel;


public class Main {

    public static void main(String[] args) throws IOException {

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

        for(int i=1; i<=projectsPath.size(); i++){
            ArrayList<ClassInfo> classesInfo = getClassesAsClassInfo(projectsPath.get(i-1));
            writeToExcel(classesInfo, i);
        }
    }



}


