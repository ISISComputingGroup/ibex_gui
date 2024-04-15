/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2024 Science & Technology Facilities Council.
* All rights reserved.
*
* This program is distributed in the hope that it will be useful.
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0 which accompanies this distribution.
* EXCEPT AS EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM
* AND ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
* OR CONDITIONS OF ANY KIND.  See the Eclipse Public License v1.0 for more details.
*
* You should have received a copy of the Eclipse Public License v1.0
* along with this program; if not, you can obtain a copy from
* https://www.eclipse.org/org/documents/epl-v10.php or
* http://opensource.org/licenses/eclipse-1.0.php
*/
package uk.ac.stfc.isis.ibex.utilities;

import java.util.Properties;

import net.bytebuddy.agent.VirtualMachine;
import net.bytebuddy.agent.VirtualMachine.ForHotSpot;
import net.bytebuddy.agent.VirtualMachine.ForOpenJ9;

/**
 * This piece of code extracts the port on which the debugger is running.
 * Input required: Process Id of the running application.
 * 
 * 1. Resolve the following dependencies in your class path: 
 * .m2/repository/net/bytebuddy/byte-buddy/1.14.11/byte-buddy-1.14.11.jar
 * .m2/repository/net/bytebuddy/byte-buddy-agent/1.14.11/byte-buddy-agent-1.14.11.jar
 * .m2/repository/net/java/dev/jna/jna/5.12.1/jna-5.12.1.jar
 * .m2/repository/net/java/dev/jna/jna-platform/5.12.1/jna-platform-5.12.1.jar
 * 
 * 2. Change the variable processId to the process Id of the application. 
 * You can check the Task Manager or add the following java code in your program to get the process Id:
 * System.out.println(String.format("Process Id: " + String.valueOf(ProcessHandle.current().pid())));
 * 
 * 3. You may also run using eclipse and configure the process Id as a run time argument
 * 
 * @author Controls team
 *
 */
public class GetVMInfo {

	public static void main(String[] args) {
		String processId = "19999";
		if (0 < args.length) {
			processId = args[args.length - 1];
			System.out.println("Input processId:    " + processId);
		}
		try {
			VirtualMachine jvm = ForHotSpot.attach(processId);
			Properties properties = jvm.getAgentProperties();
			System.out.println(properties.toString());
			System.out.println(properties.getProperty("sun.jdwp.listenerAddress"));
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			try {
				VirtualMachine jvm = ForOpenJ9.attach(processId);
				Properties properties = jvm.getAgentProperties();
				System.out.println(properties.toString());
			} catch (Exception e1) {
				System.out.println(e1.getLocalizedMessage());
			}
		}

	}

}
