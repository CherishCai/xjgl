/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package cn.cherish.xjgl.xjgl.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类描述：poi 读取excel 支持2003 --2007 及以上文件
 * 创建人：Cherish
 * 联系方式：18826137274/785427346@qq.com
 * 创建时间：2016年5月2日 下午1:04:06
 *
 * @version 1.0
 */
public class ExcelUtil {

    /**
     * 读取97-2003格式
     * @param filePath 文件路径
     * @throws IOException
     */
    @SuppressWarnings("rawtypes")
    public static List<Map> readExcel2003(String filePath) throws IOException {
        //返回结果集
        List<Map> valueList = new ArrayList<Map>();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filePath);
            @SuppressWarnings("resource")
            HSSFWorkbook wookbook = new HSSFWorkbook(fis);    // 创建对Excel工作簿文件的引用
            HSSFSheet sheet = wookbook.getSheetAt(0);    // 在Excel文档中，第一张工作表的缺省索引是0
            int rows = sheet.getPhysicalNumberOfRows();    // 获取到Excel文件中的所有行数­
            Map<Integer, String> keys = new HashMap<Integer, String>();
            int cells = 0;
            // 遍历行­（第1行  表头） 准备Map里的key
            HSSFRow firstRow = sheet.getRow(0);
            if (firstRow != null) {
                // 获取到Excel文件中的所有的列
                cells = firstRow.getPhysicalNumberOfCells();
                // 遍历列
                for (int j = 0; j < cells; j++) {
                    // 获取到列的值­
                    try {
                        HSSFCell cell = firstRow.getCell(j);
                        String cellValue = getCellValue(cell);
                        keys.put(j, cellValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            // 遍历行­（从第二行开始）
            for (int i = 1; i < rows; i++) {
                // 读取左上端单元格(从第二行开始)
                HSSFRow row = sheet.getRow(i);
                // 行不为空
                if (row != null) {
                    //准备当前行 所储存值的map
                    Map<String, Object> val = new HashMap<String, Object>();

                    boolean isValidRow = false;

                    // 遍历列
                    for (int j = 0; j < cells; j++) {
                        // 获取到列的值­
                        try {
                            HSSFCell cell = row.getCell(j);
                            String cellValue = getCellValue(cell);
                            val.put(keys.get(j), cellValue);
                            if (!isValidRow && cellValue != null && cellValue.trim().length() > 0) {
                                isValidRow = true;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    //第I行所有的列数据读取完毕，放入valuelist
                    if (isValidRow) {
                        valueList.add(val);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fis.close();
        }
        return valueList;
    }

    /**
     * 读取2007-2013格式
     * @param filePath 文件路径
     * @return
     * @throws IOException
     */
    @SuppressWarnings("rawtypes")
    public static List<Map> readExcel2007(String filePath) throws IOException {
        List<Map> valueList = new ArrayList<Map>();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filePath);
            @SuppressWarnings("resource")
            XSSFWorkbook xwb = new XSSFWorkbook(fis);    // 构造 XSSFWorkbook 对象，strPath 传入文件路径
            XSSFSheet sheet = xwb.getSheetAt(0);            // 读取第一章表格内容
            // 定义 row、cell
            XSSFRow row;
            // 循环输出表格中的第一行内容   表头
            Map<Integer, String> keys = new HashMap<Integer, String>();
            row = sheet.getRow(0);
            if (row != null) {
                //System.out.println("j = row.getFirstCellNum()::"+row.getFirstCellNum());
                //System.out.println("row.getPhysicalNumberOfCells()::"+row.getPhysicalNumberOfCells());
                for (int j = row.getFirstCellNum(); j <= row.getPhysicalNumberOfCells(); j++) {
                    // 通过 row.getCell(j).toString() 获取单元格内容，
                    if (row.getCell(j) != null) {
                        if (!row.getCell(j).toString().isEmpty()) {
                            keys.put(j, row.getCell(j).toString());
                        }
                    } else {
                        keys.put(j, "K-R1C" + j + "E");
                    }
                }
            }
            // 循环输出表格中的从第二行开始内容
            for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getPhysicalNumberOfRows(); i++) {
                row = sheet.getRow(i);
                if (row != null) {
                    boolean isValidRow = false;
                    Map<String, Object> val = new HashMap<String, Object>();
                    for (int j = row.getFirstCellNum(); j <= row.getPhysicalNumberOfCells(); j++) {
                        XSSFCell cell = row.getCell(j);
                        if (cell != null) {
                            String cellValue = null;
                            if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    cellValue = new DataFormatter().formatRawCellContents(cell.getNumericCellValue(), 0, "yyyy-MM-dd HH:mm:ss");
                                } else {
                                    cellValue = String.valueOf(cell.getNumericCellValue());
                                }
                            } else {
                                cellValue = cell.toString();
                            }
                            if (cellValue != null && cellValue.trim().length() <= 0) {
                                cellValue = null;
                            }
                            val.put(keys.get(j), cellValue);
                            if (!isValidRow && cellValue != null && cellValue.trim().length() > 0) {
                                isValidRow = true;
                            }
                        }
                    }

                    // 第I行所有的列数据读取完毕，放入valuelist
                    if (isValidRow) {
                        valueList.add(val);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fis.close();
        }

        return valueList;
    }

    /**
     * 文件操作 获取文件扩展名
     * @param filename 文件名称包含扩展名
     * @return
     * @Author: sunny
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    private static String getCellValue(HSSFCell cell) {
        if (cell == null)
            return null;
        String cellValue = null;
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    cellValue = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
                    break;
                }
                cellValue = new DecimalFormat("#").format(cell.getNumericCellValue());
                break;
            case HSSFCell.CELL_TYPE_STRING:
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case HSSFCell.CELL_TYPE_FORMULA:
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            case HSSFCell.CELL_TYPE_BLANK:
                cellValue = null;
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case HSSFCell.CELL_TYPE_ERROR:
                cellValue = String.valueOf(cell.getErrorCellValue());
                break;
        }
        if (cellValue != null && cellValue.trim().length() <= 0) {
            cellValue = null;
        }
        return cellValue;
    }


    /**
     * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上
     *
     * @param title   表格标题名
     * @param headers 表格属性列名数组
     * @param dataset 需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
     *                javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
     * @param out     与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
     * @param pattern 如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void exportExcel(String title, String[] headers, Collection<T> dataset, OutputStream out, String pattern) {
        // 声明一个工作薄
        @SuppressWarnings("resource")
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet(title);
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth(15);
        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.VIOLET.index);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);
        // 生成并设置另一个样式
        HSSFCellStyle style2 = workbook.createCellStyle();
        style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 生成另一个字体
        HSSFFont font2 = workbook.createFont();
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style2.setFont(font2);

        // 声明一个画图的顶级管理器
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
        // 定义注释的大小和位置,详见文档
        HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
        // 设置注释内容
        comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
        // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
        comment.setAuthor("Cherish");

        // 产生表格标题行
        HSSFRow row = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }

        // 遍历集合数据，产生数据行
        Iterator<T> it = dataset.iterator();
        int index = 0;
        while (it.hasNext()) {
            index++;
            row = sheet.createRow(index);
            T t = (T) it.next();
            // 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
            Field[] fields = t.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                HSSFCell cell = row.createCell(i);
                cell.setCellStyle(style2);
                Field field = fields[i];
                String fieldName = field.getName();
                String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                try {
                    Class tCls = t.getClass();
                    Method getMethod = tCls.getMethod(getMethodName, new Class[]{});
                    Object value = getMethod.invoke(t, new Object[]{});
                    // 判断值的类型后进行强制类型转换
                    String textValue = null;
                    // if (value instanceof Integer) {
                    // int intValue = (Integer) value;
                    // cell.setCellValue(intValue);
                    // } else if (value instanceof Float) {
                    // float fValue = (Float) value;
                    // textValue = new HSSFRichTextString(
                    // String.valueOf(fValue));
                    // cell.setCellValue(textValue);
                    // } else if (value instanceof Double) {
                    // double dValue = (Double) value;
                    // textValue = new HSSFRichTextString(
                    // String.valueOf(dValue));
                    // cell.setCellValue(textValue);
                    // } else if (value instanceof Long) {
                    // long longValue = (Long) value;
                    // cell.setCellValue(longValue);
                    // }
                    if (value instanceof Boolean) {
                        boolean bValue = (Boolean) value;
                        textValue = "男";
                        if (!bValue) {
                            textValue = "女";
                        }
                    } else if (value instanceof Date) {
                        Date date = (Date) value;
                        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                        textValue = sdf.format(date);
                    } else if (value instanceof byte[]) {
                        // 有图片时，设置行高为60px;
                        row.setHeightInPoints(60);
                        // 设置图片所在列宽度为80px,注意这里单位的一个换算
                        sheet.setColumnWidth(i, (short) (35.7 * 80));
                        // sheet.autoSizeColumn(i);
                        byte[] bsValue = (byte[]) value;
                        HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 255, (short) 6, index, (short) 6,
                                index);
                        anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_DONT_RESIZE);
                        patriarch.createPicture(anchor, workbook.addPicture(bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));
                    } else {
                        // 其它数据类型都当作字符串简单处理
                        textValue = value.toString();
                    }
                    // 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
                    if (textValue != null) {
                        Pattern p = Pattern.compile("^//d+(//.//d+)?$");
                        Matcher matcher = p.matcher(textValue);
                        if (matcher.matches()) {
                            // 是数字当作double处理
                            cell.setCellValue(Double.parseDouble(textValue));
                        } else {
                            HSSFRichTextString richString = new HSSFRichTextString(textValue);
                            HSSFFont font3 = workbook.createFont();
                            font3.setColor(HSSFColor.BLUE.index);
                            richString.applyFont(font3);
                            cell.setCellValue(richString);
                        }
                    }
                } catch (SecurityException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    // 清理资源
                }
            }

        }
        try {
            workbook.write(out);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 创建2007版Excel文件Demo
     * @throws FileNotFoundException
     * @throws IOException
     */
    @SuppressWarnings("resource")
    public static void creat2007Excel() throws FileNotFoundException, IOException {
        // 创建 一个excel文档对象
        XSSFWorkbook workBook = new XSSFWorkbook();
        XSSFSheet sheet = workBook.createSheet();// 创建一个工作薄对象
        sheet.setColumnWidth(1, 10000);// 设置第二列的宽度为
        XSSFRow row = sheet.createRow(1);// 创建一个行对象
        row.setHeightInPoints(23);// 设置行高23像素
        XSSFCellStyle style = workBook.createCellStyle();// 创建样式对象
        // 设置字体
        XSSFFont font = workBook.createFont();// 创建字体对象
        font.setFontHeightInPoints((short) 15);// 设置字体大小
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 设置粗体
        font.setFontName("黑体");// 设置为黑体字
        style.setFont(font);// 将字体加入到样式对象
        // 设置对齐方式
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);// 水平居中
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
        // 设置边框
        style.setBorderTop(HSSFCellStyle.BORDER_THICK);// 顶部边框粗线
        style.setTopBorderColor(HSSFColor.RED.index);// 设置为红色
        style.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);// 底部边框双线
        style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);// 左边边框
        style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);// 右边边框
        // 格式化日期
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
        XSSFCell cell = row.createCell(1);// 创建单元格
        cell.setCellValue(new Date());// 写入当前日期
        cell.setCellStyle(style);// 应用样式对象
        // 文件输出流
        FileOutputStream os = new FileOutputStream("style_2007.xlsx");
        workBook.write(os);// 将文档对象写入文件输出流
        os.close();// 关闭文件输出流
        System.out.println("创建成功 office 2007 excel");
    }

    /**
     * 创建2003版本的Excel文件Demo
     */
    @SuppressWarnings("resource")
    public static void creat2003Excel(OutputStream os) throws IOException {
        HSSFWorkbook workBook = new HSSFWorkbook();// 创建 一个excel文档对象
        HSSFSheet sheet = workBook.createSheet();// 创建一个工作薄对象
        sheet.setColumnWidth(1, 10000);// 设置第二列的宽度为
        HSSFRow row = sheet.createRow(1);// 创建一个行对象
        row.setHeightInPoints(23);// 设置行高23像素
        HSSFCellStyle style = workBook.createCellStyle();// 创建样式对象
        // 设置字体
        HSSFFont font = workBook.createFont();// 创建字体对象
        font.setFontHeightInPoints((short) 15);// 设置字体大小
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 设置粗体
        font.setFontName("黑体");// 设置为黑体字
        style.setFont(font);// 将字体加入到样式对象
        // 设置对齐方式
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);// 水平居中
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
        // 设置边框
        style.setBorderTop(HSSFCellStyle.BORDER_THICK);// 顶部边框粗线
        style.setTopBorderColor(HSSFColor.RED.index);// 设置为红色
        style.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);// 底部边框双线
        style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);// 左边边框
        style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);// 右边边框
        // 格式化日期
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
        HSSFCell cell = row.createCell(1);// 创建单元格
        cell.setCellValue(new Date());// 写入当前日期
        cell.setCellStyle(style);// 应用样式对象
        workBook.write(os);// 将文档对象写入文件输出流
        os.close();// 关闭文件输出流
        System.out.println("创建成功 office 2003 excel");
    }

    /**
     * 预约订单的Excel文件
     * @param os
     * @param orders
     * @throws IOException void
     */
    /*@SuppressWarnings("resource")
	public static void orderExcel(OutputStream os, Collection<Order> orders) throws IOException {
		HSSFWorkbook workBook = new HSSFWorkbook();// 创建 一个excel文档对象
			HSSFCellStyle style = workBook.createCellStyle();// 创建样式对象
			// 设置对齐方式
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);// 水平居中
			style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
			// 格式化日期
			style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
			
			int sheetNum = 1;
			int sheetLine = 30000;
			int nowLine = 0;
			Iterator<Order> it = orders.iterator();
			
			HSSFSheet sheet = null;
			while(it.hasNext()){
				Order next = it.next();
				
				if(nowLine >= sheetLine || nowLine == 0){
					//产生一个新工作表
					sheet = workBook.createSheet(sheetNum + "");
					sheet.setColumnWidth(3, 5000);
					sheet.setColumnWidth(5, 5000);
					sheet.setColumnWidth(6, 10000);
					sheet.setColumnWidth(9, 20000);
					
					HSSFRow row0 = sheet.createRow(0);
						HSSFCell cell00 = row0.createCell(0);
						cell00.setCellValue("ID");
						HSSFCell cell01 = row0.createCell(1);
						cell01.setCellValue("预约号");
						HSSFCell cell02 = row0.createCell(2);
						cell02.setCellValue("客户呢称");
						HSSFCell cell03 = row0.createCell(3);
						cell03.setCellValue("手机号码");
						HSSFCell cell04 = row0.createCell(4);
						cell04.setCellValue("预约服务");
						HSSFCell cell05 = row0.createCell(5);
						cell05.setCellValue("预约时间");
						HSSFCell cell06 = row0.createCell(6);
						cell06.setCellValue("留言");
						HSSFCell cell07 = row0.createCell(7);
						cell07.setCellValue("付款状态");
						HSSFCell cell08 = row0.createCell(8);
						cell08.setCellValue("金额");
						HSSFCell cell09 = row0.createCell(9);
						cell09.setCellValue("管理员备注");
					
					sheetNum++;
					//重新从第一行开始
					nowLine = 1;
				}
				
				// 创建一个行对象
				HSSFRow row = sheet.createRow(nowLine);
					HSSFCell cell0 = row.createCell(0);
					cell0.setCellValue(next.getId());
					HSSFCell cell1 = row.createCell(1);
					cell1.setCellValue(next.getOrderSn());
					HSSFCell cell2 = row.createCell(2);
					cell2.setCellValue(next.getNickname());
					HSSFCell cell3 = row.createCell(3);
					cell3.setCellValue(next.getTelephone());
					HSSFCell cell4 = row.createCell(4);
					cell4.setCellValue(next.getServiceNo());
					HSSFCell cell5 = row.createCell(5);
					cell5.setCellStyle(style);// 应用样式对象
					cell5.setCellValue(next.getSubscribetime());
					HSSFCell cell6 = row.createCell(6);
					cell6.setCellValue(next.getMemo());
					HSSFCell cell7 = row.createCell(7);
					cell7.setCellValue(OrderUtil.status(next.getDesc()));
					HSSFCell cell8 = row.createCell(8);
					cell8.setCellValue(next.getFee());
					HSSFCell cell9 = row.createCell(9);
					cell9.setCellValue(next.getComment());
				
				//用于调整sheetNum和nowLine，新建工作表sheet或者。。。。。
				nowLine++;
				
			}
			
		workBook.write(os);// 将文档对象写入文件输出流
		os.flush();
		os.close();// 关闭文件输出流
	}
	
	*//**
     * 健康报告的Excel文件
     * @param os
     * @param reports
     * @throws IOException void
     * @date 2016年9月7日 下午7:41:00
     *//*
	@SuppressWarnings("resource")
	public static void reportExcel(OutputStream os, Collection<Report> reports) throws IOException {
		HSSFWorkbook workBook = new HSSFWorkbook();// 创建 一个excel文档对象
			HSSFCellStyle style = workBook.createCellStyle();// 创建样式对象
			// 设置对齐方式
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);// 水平居中
			style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
			// 格式化日期
			style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
			
			int sheetNum = 1;
			int sheetLine = 30000;
			int nowLine = 0;
			Iterator<Report> it = reports.iterator();
			
			HSSFSheet sheet = null;
			while(it.hasNext()){
				Report next = it.next();
				
				if(nowLine >= sheetLine || nowLine == 0){
					//产生一个新工作表
					sheet = workBook.createSheet(sheetNum + "");
					sheet.setColumnWidth(1, 10000);
					sheet.setColumnWidth(5, 5000);
					sheet.setColumnWidth(6, 5000);
					
					HSSFRow row0 = sheet.createRow(0);
						HSSFCell cell00 = row0.createCell(0);
						cell00.setCellValue("服务编号");
						HSSFCell cell01 = row0.createCell(1);
						cell01.setCellValue("选项");
						HSSFCell cell02 = row0.createCell(2);
						cell02.setCellValue("类型");
						HSSFCell cell03 = row0.createCell(3);
						cell03.setCellValue("价格");
						HSSFCell cell04 = row0.createCell(4);
						cell04.setCellValue("内容");
						HSSFCell cell05 = row0.createCell(5);
						cell05.setCellValue("生成时间");
						HSSFCell cell06 = row0.createCell(6);
						cell06.setCellValue("修改时间");
					
					sheetNum++;
					//重新从第一行开始
					nowLine = 1;
				}
				
				// 创建一个行对象
				HSSFRow row = sheet.createRow(nowLine);
					HSSFCell cell0 = row.createCell(0);
					cell0.setCellValue(next.getServiceNo());
					HSSFCell cell1 = row.createCell(1);
					cell1.setCellValue(next.getOptions());
					HSSFCell cell2 = row.createCell(2);
					cell2.setCellValue(StringUtil.reportType(next.getType()));
					HSSFCell cell3 = row.createCell(3);
					cell3.setCellValue(next.getPrice());
					HSSFCell cell4 = row.createCell(4);
					cell4.setCellValue(next.getContent());
					HSSFCell cell5 = row.createCell(5);
					cell5.setCellStyle(style);// 应用样式对象
					cell5.setCellValue(next.getCreatetime());
					HSSFCell cell6 = row.createCell(6);
					cell6.setCellStyle(style);// 应用样式对象
					if(next.getModifytime() != null)
						cell6.setCellValue(next.getModifytime());
					else
						cell6.setCellValue("");
				
				//用于调整sheetNum和nowLine，新建工作表sheet或者。。。。。
				nowLine++;
				
			}
			
		workBook.write(os);// 将文档对象写入文件输出流
		os.flush();
		os.close();// 关闭文件输出流
	}*/
	
	/*public static void main(String[] args) {
		// 文件输出流
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String fileName = sdf.format(new Date());
			FileOutputStream os = new FileOutputStream("style_2003.zip");
			ZipOutputStream zip = new ZipOutputStream(os);
			
			ZipEntry entry = new ZipEntry(fileName + ".xls");
			zip.putNextEntry(entry);
			creat2003Excel(zip);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/


}
