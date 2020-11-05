package template.dto;

import ${packagePath}.${className};
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author ${author}
 * @Date ${date}
 * @Description ${description}
 */

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "${description}")
public class ${className}DTO extends ${className}{

    private static final long serialVersionUID = 1L;

}
