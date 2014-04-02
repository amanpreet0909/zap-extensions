/*
 * Zed Attack Proxy (ZAP) and its related class files.
 *
 * ZAP is an HTTP/HTTPS proxy for assessing web application security.
 *
 * Copyright 2012 The ZAP development team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.zaproxy.zap.extension.pscanrulesAlpha;

import java.util.ResourceBundle;

import org.parosproxy.paros.Constant;

public final class PscanUtils {

	private static ResourceBundle messages = null;
	
	private PscanUtils() {
	}
	
	public static synchronized void registerI18N() {
		if (messages == null) {
			messages = ResourceBundle.getBundle(
					PscanUtils.class.getPackage().getName() + ".resources.Messages", Constant.getLocale());
			Constant.messages.addMessageBundle("pscanalpha", messages);
		}
	}
}