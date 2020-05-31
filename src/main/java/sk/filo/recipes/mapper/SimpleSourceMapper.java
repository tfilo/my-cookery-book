package sk.filo.recipes.mapper;

import java.util.ArrayList;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import sk.filo.recipes.domain.Source;

/**
 *
 * @author tomas
 */
@Mapper(
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public class SimpleSourceMapper {

    public String mapSourceToSourceURL(Source source) {
        return source != null ? source.getUrl() : null;
    }

    public List<String> mapSourceListToSourceURLList(List<Source> sources) {
        if (sources != null) {
            final List<String> result = new ArrayList<>();
            sources.forEach(source -> {
                result.add(mapSourceToSourceURL(source));
            });
            return result;
        }
        return null;
    }
}
