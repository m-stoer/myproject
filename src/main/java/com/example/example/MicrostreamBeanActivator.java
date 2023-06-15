package com.example.example;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.ApplicationPath;
import one.microstream.enterprise.cluster.nodelibrary.helidon.MicrostreamBeanProvider;

@ApplicationScoped
@ApplicationPath("/")
public class MicrostreamBeanActivator extends MicrostreamBeanProvider
{
}
