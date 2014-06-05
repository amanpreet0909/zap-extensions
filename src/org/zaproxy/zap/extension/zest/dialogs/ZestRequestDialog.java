/*
 * Zed Attack Proxy (ZAP) and its related class files.
 * 
 * ZAP is an HTTP/HTTPS proxy for assessing web application security.
 * 
 * Copyright 2013 ZAP development team
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
package org.zaproxy.zap.extension.zest.dialogs;

import java.awt.Dimension;
import java.awt.Frame;
import java.net.MalformedURLException;
import java.net.URL;

import org.mozilla.zest.core.v1.ZestRequest;
import org.mozilla.zest.core.v1.ZestVariables;
import org.parosproxy.paros.model.SiteNode;
import org.zaproxy.zap.extension.script.ScriptNode;
import org.zaproxy.zap.extension.zest.ExtensionZest;
import org.zaproxy.zap.extension.zest.ZestScriptWrapper;
import org.zaproxy.zap.extension.zest.ZestZapUtils;
import org.zaproxy.zap.view.StandardFieldsDialog;

public class ZestRequestDialog extends StandardFieldsDialog implements ZestDialog {

	private static final String FIELD_URL = "zest.dialog.request.label.url"; 
	private static final String FIELD_METHOD = "zest.dialog.request.label.method"; 
	private static final String FIELD_HEADERS = "zest.dialog.request.label.headers"; 
	private static final String FIELD_BODY = "zest.dialog.request.label.body"; 

	private static final long serialVersionUID = 1L;

	private ExtensionZest extension = null;
	private ScriptNode parent = null;
	private ScriptNode node = null;
	private boolean add = false;
	
	private ZestRequest request = null;

	public ZestRequestDialog(ExtensionZest ext, Frame owner, Dimension dim) {
		super(owner, "zest.dialog.request.title", dim);
		this.extension = ext;
	}

	public void init (ScriptNode parent, ScriptNode node) {
		this.parent = parent;
		if (node == null) {
			this.node = new ScriptNode();
			request = new ZestRequest();
			this.node.setUserObject(request);
			add = true;
		} else {
			this.node = node;
			this.request = (ZestRequest) ZestZapUtils.getElement(node);
			add = false;
		}

		this.removeAllFields();
		this.addNodeSelectField(FIELD_URL, null, true, false);
		if (request.getUrl() != null) {
			this.setFieldValue(FIELD_URL, request.getUrl().toString());
		} else {
			this.setFieldValue(FIELD_URL, request.getUrlToken());
		}
		this.addComboField(FIELD_METHOD, new String[] {"GET", "POST", "{{" + ZestVariables.REQUEST_METHOD + "}}"}, request.getMethod());
		this.addMultilineField(FIELD_HEADERS, request.getHeaders());
		this.addMultilineField(FIELD_BODY, request.getData());
		
		// Enable right click menus
		this.addFieldListener(FIELD_URL, ZestZapUtils.stdMenuAdapter()); 
		this.addFieldListener(FIELD_HEADERS, ZestZapUtils.stdMenuAdapter()); 
		this.addFieldListener(FIELD_BODY, ZestZapUtils.stdMenuAdapter()); 

	}

	@Override
	public void siteNodeSelected(String field, SiteNode node) {
		if (node != null) {
			this.setFieldValue(FIELD_METHOD, node.getHistoryReference().getMethod());
		}
	}

	public void save() {
		try {
			this.request.setUrl(new URL(this.getStringValue(FIELD_URL)));
		} catch (MalformedURLException e) {
			// Assume this is because it includes a token
			this.request.setUrlToken(this.getStringValue(FIELD_URL));
		}
		this.request.setMethod(this.getStringValue(FIELD_METHOD));
		this.request.setHeaders(this.getStringValue(FIELD_HEADERS));
		this.request.setData(this.getStringValue(FIELD_BODY));
		
		if (add) {
			this.extension.addToParent(this.parent, this.request);
		} else {
			this.extension.updated(node);
			this.extension.display(node, false);
		}

	}

	@Override
	public String validateFields() {
		// TODO is there any validation we can do now? The below doesnt work with tokens...
		/* 
		try {
			new URL(this.getStringValue(FIELD_URL));
		} catch (MalformedURLException e) {
			return Constant.messages.getString("zest.dialog.request.error.url");
		}
		*/
		return null;
	}

	@Override
	public ZestScriptWrapper getScript() {
		return extension.getZestTreeModel().getScriptWrapper(node);
	}
	
}