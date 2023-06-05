package com.example.example;

import jakarta.inject.Singleton;
import jakarta.ws.rs.Path;
import one.microstream.enterprise.cluster.nodelibrary.common.StorageClusterControllerBase;
import one.microstream.enterprise.cluster.nodelibrary.helidon.StorageClusterController;

@Path(StorageClusterControllerBase.CONTROLLER_PATH)
@Singleton
public class MicrostreamResource extends StorageClusterController
{

}
