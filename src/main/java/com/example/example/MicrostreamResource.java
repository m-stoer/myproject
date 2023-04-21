package com.example.example;

import jakarta.inject.Singleton;
import jakarta.ws.rs.Path;
import one.microstream.enterprise.cluster.nodelibrary.helidon.StorageClusterController;

@Path("/my-microstream-path")
@Singleton
public class MicrostreamResource extends StorageClusterController
{
}
