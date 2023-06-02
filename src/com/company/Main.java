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


    private static void writeToExcel(ArrayList<ClassInfo> classesInfo, int projectNumber) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet spreadsheet = workbook.createSheet(" Classes Info ");
        XSSFRow row;
        Map<Integer, Object[]> classInfoMap = new TreeMap<>();
        classInfoMap.put(1,createLabels().toArray());

        for(ClassInfo clazz : classesInfo){
            clazz.setChildren(classesInfo);
        }

        for(int i=0; i<classesInfo.size(); i++){
            try {
                ClassInfo clazz = classesInfo.get(i);
                ArrayList classInfo = clazz.getClassInfo();
                ArrayList classRelationshipInfo = clazz.getRelationships(classesInfo);
                classInfo.addAll(classRelationshipInfo);
                classInfoMap.put(i+2, classInfo.toArray());
            }
            catch (Exception e){
            }
        }

        Set<Integer> keyId = classInfoMap.keySet();
        int rowId = 0;

        for (Integer key : keyId) {
            row = spreadsheet.createRow(rowId++);
            Object[] objectArr = classInfoMap.get(key);
            int cellId = 0;
            for (Object obj : objectArr) {
                try {

                    Cell cell = row.createCell(cellId++);
                    if (obj instanceof Integer) {
                        cell.setCellValue((Integer) obj);
                    } else {
                        cell.setCellValue(String.valueOf(obj));
                    }
                }
                catch (Exception e){

                }
            }
        }
        try {
            FileOutputStream out = new FileOutputStream(new File("Project_" + projectNumber + ".xlsx"));
            workbook.write(out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<ClassInfo> getClassesAsClassInfo(String path) throws IOException {
        ArrayList<ClassInfo> classesInfo = new ArrayList<>();
        Files.walk(Paths.get(path))
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith(".java"))
                .forEach(p -> {
                    try {
                        File classFile = new File(p.toString());
                        CompilationUnit cu = StaticJavaParser.parse(classFile);
                        cu.findAll(ClassOrInterfaceDeclaration.class).forEach(cl -> {
                                try {
                                    classesInfo.add(new ClassInfo(classFile,cl));
                                } catch (FileNotFoundException e) { }
                            }
                        );
                    } catch (Exception e) { }
                });
        return classesInfo;
    }


    public static List<String> createLabels(){
        List<String> list = Arrays.asList("Project Name", "Package_Name","Class_Name", "Class_Type", "Class_Visibility",
                "Class_is_Abstract", "Class_is_Static", "Class_is_Final", "Class_Is_Interface", "Extends", "Implements",
                "Children", "Constructor", "Fields", "Methods", "Override", "has_static_method", "has_final_method",
                "Has_abstract_method", "Instantiations", "Associations", "Composition/Aggregation", "Delegation");
        return list;
    }

}


