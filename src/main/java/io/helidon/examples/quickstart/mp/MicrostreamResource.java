
package io.helidon.examples.quickstart.mp;

import jakarta.inject.Singleton;
import jakarta.ws.rs.Path;
import one.microstream.cluster.nodelibrary.helidon.StorageClusterController;

@Path("/my-microstream-path")
@Singleton
public class MicrostreamResource extends StorageClusterController
{
}
