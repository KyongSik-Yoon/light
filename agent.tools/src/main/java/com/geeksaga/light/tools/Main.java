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
package com.geeksaga.light.tools;

import com.geeksaga.light.logger.CommonLogger;
import com.geeksaga.light.logger.LightLogger;
import com.geeksaga.light.tools.vm.VirtualMachineAttache;


/**
 * @author geeksaga
 */
public class Main
{
    private static LightLogger logger = CommonLogger.getLogger(Main.class.getName());

    public static void main(String[] args)
    {
        logger.info(Product.NAME + " APM Tools.");
        logger.info("Usage : ");
        logger.info("ex) light-tools.sh --help");

        VirtualMachineAttache main = new VirtualMachineAttache();
        //        main.show();
        main.attach();
    }
}