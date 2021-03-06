/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.ranger.biz;

import javax.servlet.http.HttpServletResponse;

import org.apache.ranger.common.ContextUtil;
import org.apache.ranger.common.SearchCriteria;
import org.apache.ranger.common.UserSessionBase;
import org.apache.ranger.solr.SolrAccessAuditsService;
import org.apache.ranger.view.VXAccessAudit;
import org.apache.ranger.view.VXAccessAuditList;
import org.apache.ranger.view.VXLong;
import org.apache.ranger.view.VXResponse;
import org.apache.ranger.view.VXTrxLog;
import org.apache.ranger.view.VXTrxLogList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class XAuditMgr extends XAuditMgrBase {

	@Autowired
	SolrAccessAuditsService solrAccessAuditsService;

	@Autowired
	RangerBizUtil rangerBizUtil;

	public VXTrxLog getXTrxLog(Long id) {
		checkAdminAccess();
		return super.getXTrxLog(id);
	}

	public VXTrxLog createXTrxLog(VXTrxLog vXTrxLog) {
		checkAdminAccess();
		return super.createXTrxLog(vXTrxLog);
	}

	public VXTrxLog updateXTrxLog(VXTrxLog vXTrxLog) {
		checkAdminAccess();
		return super.updateXTrxLog(vXTrxLog);
	}

	public void deleteXTrxLog(Long id, boolean force) {
		checkAdminAccess();
		super.deleteXTrxLog(id, force);
	}

	public VXTrxLogList searchXTrxLogs(SearchCriteria searchCriteria) {
		checkAdminAccess();
		return super.searchXTrxLogs(searchCriteria);
	}

	public VXLong getXTrxLogSearchCount(SearchCriteria searchCriteria) {
		checkAdminAccess();
		return super.getXTrxLogSearchCount(searchCriteria);
	}

	public VXAccessAudit createXAccessAudit(VXAccessAudit vXAccessAudit) {
		checkAdminAccess();
		return super.createXAccessAudit(vXAccessAudit);
	}

	public VXAccessAudit updateXAccessAudit(VXAccessAudit vXAccessAudit) {
		checkAdminAccess();
		return super.updateXAccessAudit(vXAccessAudit);
	}

	public void deleteXAccessAudit(Long id, boolean force) {
		checkAdminAccess();
		super.deleteXAccessAudit(id, force);
	}

	public void checkAdminAccess() {
		UserSessionBase session = ContextUtil.getCurrentUserSession();
		if (session != null) {
			if (!session.isUserAdmin()) {
				throw restErrorUtil.create403RESTException("Operation" + " denied. LoggedInUser=" + (session != null ? session.getXXPortalUser().getId() : "Not Logged In")
						+ " ,isn't permitted to perform the action.");
			}
		} else {
			VXResponse vXResponse = new VXResponse();
			vXResponse.setStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
			vXResponse.setMsgDesc("Bad Credentials");
			throw restErrorUtil.generateRESTException(vXResponse);
		}
	}

	@Override
	public VXAccessAuditList searchXAccessAudits(SearchCriteria searchCriteria) {
		if (rangerBizUtil.getAuditDBType().equalsIgnoreCase("solr")) {
			return solrAccessAuditsService.searchXAccessAudits(searchCriteria);
		} else {
			return super.searchXAccessAudits(searchCriteria);
		}
	}

	@Override
	public VXLong getXAccessAuditSearchCount(SearchCriteria searchCriteria) {
		if (rangerBizUtil.getAuditDBType().equalsIgnoreCase("solr")) {
			return solrAccessAuditsService.getXAccessAuditSearchCount(searchCriteria);
		} else {
			return super.getXAccessAuditSearchCount(searchCriteria);
		}
	}

}
