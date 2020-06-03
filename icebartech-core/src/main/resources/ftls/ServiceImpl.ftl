package template.service.impl;

import com.icebartech.core.modules.AbstractService;
import template.repository.${className}Repository;
import template.service.${className}Service;
import template.dto.${className}DTO;
import ${packagePath}.${className};
import org.springframework.stereotype.Service;

/**
 * @author ${author}
 * @Date ${date}
 * @Description ${description}
 */

@Service
public class ${className}ServiceImpl extends AbstractService
<${className}DTO, ${className}, ${className}Repository> implements ${className}Service {

}