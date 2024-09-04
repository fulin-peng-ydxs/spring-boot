package commons.resolver.excel;

import commons.model.annotations.excel.ExcelMark;
import commons.utils.ExcelUtils;
import org.springframework.stereotype.Component;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Excel导出执行器
 *
 * @author fulin-peng
 * 2024-08-30  15:32
 */
@Component
public class SimpleExcelMarkExecutor extends ExcelMarkResolver {

    /**
     * 生成exel
     * 2024/8/30 下午4:13
     * @param rows 数据集集
     * @param header key：表头-value：字段映射
     * @param isXlsx 是否导出xlsx类型，否则为xls
     * @param excelMark ExcelExport注解对象
     * @return excel的字节数组
     * @author fulin-peng
     */
    protected byte[] convertData(Collection<?> rows, Map<String, String> header, boolean isXlsx, ExcelMark excelMark) {
        return ExcelUtils.writeToBytes(Collections.singletonMap(excelMark.sheetName(), rows),
                Collections.singletonMap(excelMark.sheetName(), header), excelMark.headerIndex(), excelMark.startRowIndex(),
                excelMark.startColIndex(), isXlsx ? ExcelUtils.ExcelType.XLSX : ExcelUtils.ExcelType.XLS, null);
    }


    @Override
    protected Map<String,Object> convertData(InputStream in, Class<?> clazz, Map<String, String> header, ExcelMark excelMark) {
        return ExcelUtils.read(in, clazz, Collections.singleton(excelMark.sheetName()), header, excelMark.headerIndex(),
                excelMark.startRowIndex());
    }
}
