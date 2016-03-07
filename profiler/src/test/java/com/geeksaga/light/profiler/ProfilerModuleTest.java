/*
 * Copyright 2015 GeekSaga.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.geeksaga.light.profiler;

import com.geeksaga.light.agent.Module;
import org.apache.logging.log4j.core.config.xml.XmlConfigurationFactory;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.instrument.Instrumentation;

import static org.mockito.Mockito.mock;

/**
 * @author geeksaga
 */
public class ProfilerModuleTest {

    @BeforeClass
    public static void init() {
        System.setProperty(XmlConfigurationFactory.CONFIGURATION_FILE_PROPERTY, "log4j2.xml");
    }

    @Test
    public void testStart() {
        Instrumentation instrumentation = mock(Instrumentation.class);

        Module module = new ProfilerModule(instrumentation);
        module.start();
        module.stop();
    }
}