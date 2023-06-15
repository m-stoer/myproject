package com.example.example;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.ApplicationPath;
import one.microstream.enterprise.cluster.nodelibrary.common.StorageClusterControllerBase;
import one.microstream.enterprise.cluster.nodelibrary.helidon.MicrostreamBeanProvider;

@ApplicationScoped
@ApplicationPath(StorageClusterControllerBase.CONTROLLER_PATH)
public class JaxRsActivator extends MicrostreamBeanProvider
{
}
