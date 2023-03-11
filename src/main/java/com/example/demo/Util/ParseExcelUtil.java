package com.example.demo.Util;

import com.example.demo.eneity.Course;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import static org.apache.poi.ss.usermodel.CellType.STRING;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ParseExcelUtil {
    public static File multipartFileToFile(MultipartFile file) throws Exception {
        File toFile = null;
        if (file.equals("") || file.getSize() <= 0) {
            file = null;
        } else {
            InputStream ins = null;
            ins = file.getInputStream();
            System.out.println(file.getOriginalFilename());
            toFile = new File(file.getOriginalFilename());
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }
    public static void inputStreamToFile(InputStream ins,File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static List<Course> parseExcel(File file) {
        List<Course> list = new ArrayList<Course>();
//        File file = null;
        InputStream input = null;
        if (file!=null) {
            // 判断文件是否是Excel(2003、2007)
            String suffix = file.getName().substring(file.getName().lastIndexOf("."));
//            file = new File(path);
            System.out.println(file);
            try {
                input = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                System.out.println("未找到指定的文件！");
            }
            // Excel2003
            if (".xls".equals(suffix)) {
                POIFSFileSystem fileSystem = null;
                // 工作簿
                HSSFWorkbook workBook = null;
                try {
                    fileSystem = new POIFSFileSystem(input);
                    workBook = new HSSFWorkbook(fileSystem);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // 获取第一个工作簿
                HSSFSheet sheet = workBook.getSheetAt(0);
                list = getContent(sheet);
                // Excel2007
            } else if (".xlsx".equals(suffix)) {
                XSSFWorkbook workBook = null;
                try {
                    workBook = new XSSFWorkbook(input);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // 获取第一个工作簿
                XSSFSheet sheet = workBook.getSheetAt(0);
                list = getContent(sheet);
                System.out.println(list);
            }
        } else {
            System.out.println("非法的文件路径!");
        }
        return list;
    }

    // 获取Excel内容
    @SuppressWarnings("deprecation")
    public static List<Course> getContent(Sheet sheet) {
        List<Course> list = new ArrayList<Course>();
        // Excel数据总行数
        int rowCount = sheet.getPhysicalNumberOfRows();
        // 遍历数据行，略过标题行，从第二行开始
        for (int i = 1; i < rowCount; i++) {
            Course courseBean = new Course();
            Row row = sheet.getRow(i);
            int cellCount = row.getPhysicalNumberOfCells();
            // 遍历行单元格
            for (int j = 0; j < cellCount; j++) {
                Cell cell = row.getCell(j);
                cell.setCellType(CellType.STRING);
                switch (j) {
                    case 0:
                        // 根据cell中的类型来输出数据
                        cell.setCellType(CellType.STRING);
                        if (cell.getCellType() == STRING) {
                            courseBean.setCourseName( cell.getStringCellValue());
                        }
                        break;
                    case 1:
                        cell.setCellType(STRING);
                        if (cell.getCellType() == STRING) {
                            courseBean.setClassroomNum((String) cell.getStringCellValue());
                        }
                        break;
                    case 2:
                        cell.setCellType(STRING);
                        if (cell.getCellType() == STRING) {
                            courseBean.setStartWeek((String) cell.getStringCellValue());
                        }
                        break;
                    case 3:
                        cell.setCellType(STRING);
                        if (cell.getCellType() == STRING) {
                            courseBean.setEndWeek((String) cell.getStringCellValue());
                        }
                        break;
                    case 4:
                        cell.setCellType(STRING);
                        if (cell.getCellType() == STRING) {
                            courseBean.setWeek((String) cell.getStringCellValue());
                        }
                        break;
                    case 5:
                        cell.setCellType(STRING);
                        if (cell.getCellType() == STRING) {
                            courseBean.setLesson((String) cell.getStringCellValue());
                        }
                        break;
                    case 6:
                        cell.setCellType(STRING);
                        if (cell.getCellType() == STRING) {
                            courseBean.setIsEmpty((String) cell.getStringCellValue());
                        }
                        break;
                }
            }
            list.add(courseBean);
        }
        return list;
    }
}
