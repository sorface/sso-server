package by.sorface.ssoserver.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.RandomStringUtils;

@UtilityClass
public class HashUtils {

    public String shortHash(final String hash) {
        return hash.substring(0, 20);
    }

    public String generateRegistryHash(final int length) {
        return RandomStringUtils.random(length, true, true);
    }

}