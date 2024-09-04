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
