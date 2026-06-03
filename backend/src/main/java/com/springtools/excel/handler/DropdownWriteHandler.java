package com.springtools.excel.handler;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;

import java.util.List;
import java.util.Map;

/**
 * Excel 下拉选择处理器
 * 使用隐藏 Sheet 页 + Name 公式的方式设置下拉选择
 */
public class DropdownWriteHandler implements SheetWriteHandler {

    private final Map<Integer, List<String>> dropdownMap;

    public DropdownWriteHandler(Map<Integer, List<String>> dropdownMap) {
        this.dropdownMap = dropdownMap;
    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        Sheet sheet = writeSheetHolder.getSheet();
        Workbook workbook = writeWorkbookHolder.getWorkbook();

        dropdownMap.forEach((colIndex, options) -> {
            String hiddenSheetName = "hidden_" + colIndex;
            Sheet hiddenSheet = workbook.createSheet(hiddenSheetName);

            // 在隐藏 Sheet 中写入下拉选项数据
            for (int i = 0; i < options.size(); i++) {
                Row row = hiddenSheet.createRow(i);
                Cell cell = row.createCell(0);
                cell.setCellValue(options.get(i));
            }

            // 定义 Name 公式引用隐藏 Sheet 数据
            Name name = workbook.createName();
            name.setNameName("dropdown_" + colIndex);
            String formula = hiddenSheetName + "!$A$1:$A$" + options.size();
            name.setRefersToFormula(formula);

            // 隐藏字典 Sheet
            workbook.setSheetHidden(workbook.getSheetIndex(hiddenSheet), true);

            // 设置数据校验（从第2行开始，跳过表头）
            CellRangeAddressList addressList = new CellRangeAddressList(1, 9999, colIndex, colIndex);
            DataValidationConstraint constraint = sheet.getDataValidationHelper().createFormulaListConstraint("dropdown_" + colIndex);
            DataValidation dataValidation = sheet.getDataValidationHelper().createValidation(constraint, addressList);

            // 处理兼容性问题
            if (dataValidation instanceof XSSFDataValidation) {
                dataValidation.setSuppressDropDownArrow(true);
                dataValidation.setShowErrorBox(true);
            } else {
                dataValidation.setSuppressDropDownArrow(false);
            }

            sheet.addValidationData(dataValidation);
        });
    }
}
