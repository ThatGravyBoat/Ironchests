package tech.thatgravyboat.ironchests.platform;

import tech.thatgravyboat.ironchests.Constants;
import tech.thatgravyboat.ironchests.platform.services.IClientHelper;
import tech.thatgravyboat.ironchests.platform.services.IModelHelper;

import java.util.ServiceLoader;

public class ClientServices {

    public static final IClientHelper CLIENT = load(IClientHelper.class);
    public static final IModelHelper MODEL = load(IModelHelper.class);

    public static <T> T load(Class<T> clazz) {

        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        Constants.LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
