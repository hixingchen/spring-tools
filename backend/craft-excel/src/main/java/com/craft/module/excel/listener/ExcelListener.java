package com.craft.module.excel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Excel 导入泛型监听器
 * 用于 EasyExcel 读取时收集数据
 */
@Slf4j
public class ExcelListener<T> extends AnalysisEventListener<T> {

    private final List<T> dataList = new ArrayList<>();
    private final List<Integer> failRows = new ArrayList<>();

    @Override
    public void invoke(T data, AnalysisContext context) {
        dataList.add(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("Excel 解析完成，共 {} 条数据", dataList.size());
    }

    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        int rowIndex = context.readRowHolder().getRowIndex();
        log.error("第 {} 行数据解析失败: {}", rowIndex, exception.getMessage());
        failRows.add(rowIndex);
    }

    public List<T> getDataList() {
        return dataList;
    }

    public List<Integer> getFailRows() {
        return failRows;
    }

    public int getTotal() {
        return dataList.size() + failRows.size();
    }

    public int getSuccess() {
        return dataList.size();
    }

    public int getFail() {
        return failRows.size();
    }
}
