package by.sorface.sso.web.records.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperationError {

    /**
     * Краткое описание ошибки
     */
    private String message;

    /**
     * Причена ошибки
     */
    private String details;

    /**
     * Код ошибки
     */
    private int code;

}
