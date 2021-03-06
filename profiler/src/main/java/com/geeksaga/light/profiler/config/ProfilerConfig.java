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
package com.geeksaga.light.profiler.config;

import com.geeksaga.light.config.Config;
import com.geeksaga.light.config.PropertiesLoader;
import com.geeksaga.light.logger.CommonLogger;
import com.geeksaga.light.logger.LightLogger;
import com.geeksaga.light.util.SystemProperty;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * @author geeksaga
 */
@Deprecated
public class ProfilerConfig implements Config
{
    private static final LightLogger logger = CommonLogger.getLogger(ProfilerConfig.class.getName());

    private Properties properties;

    public ProfilerConfig()
    {
        this(new Properties());
    }

    public ProfilerConfig(Properties properties)
    {
        this.properties = properties;
    }

    public static Config load()
    {
        return load(SystemProperty.LIGHT_CONFIG);
    }

    public static Config load(String file)
    {
        try
        {
            return new ProfilerConfig(new PropertiesLoader().load(file));
        }
        catch (IOException e)
        {
            logger.info(e);
        }

        return new ProfilerConfig();
    }

    public static Config load(ClassLoader classLoader, String file)
    {
        try
        {
            return new ProfilerConfig(new PropertiesLoader().load(classLoader, file));
        }
        catch (IOException e)
        {
            logger.info(e);
        }

        return new ProfilerConfig();
    }

    public String read(String propertyKey, String defaultValue)
    {
        return properties.getProperty(propertyKey, defaultValue);
    }

    public boolean read(String propertyKey, boolean defaultValue)
    {
        return Boolean.valueOf(read(propertyKey, String.valueOf(defaultValue)));
    }

    public short read(String propertyKey, short defaultValue)
    {
        return Short.valueOf(read(propertyKey, String.valueOf(defaultValue)));
    }

    public int read(String propertyKey, int defaultValue)
    {
        return Integer.valueOf(read(propertyKey, String.valueOf(defaultValue)));
    }

    public long read(String propertyKey, long defaultValue)
    {
        return Long.valueOf(read(propertyKey, String.valueOf(defaultValue)));
    }

    public List<String> read(String propertyKey)
    {
        return read(propertyKey, new String[] {});
    }

    public List<String> read(String propertyKey, String[] defaultValue)
    {
        String value = properties.getProperty(propertyKey);
        if (value == null)
        {
            return read0(defaultValue);
        }

        return Arrays.asList(value.trim().split("\\s+"));
    }

    private List<String> read0(String[] values)
    {
        if (values == null || values.length == 0)
        {
            return Collections.emptyList();
        }

        return Arrays.asList(values);
    }
}