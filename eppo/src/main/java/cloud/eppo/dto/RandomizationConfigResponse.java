package cloud.eppo.dto;

import java.util.Map;

public class RandomizationConfigResponse {
    private Map<String, FlagConfig> flags;

    public Map<String, FlagConfig> getFlags() {
        return flags;
    }
}
