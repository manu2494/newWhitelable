package com.merkle.xp.whitelabel.core.services.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;

@Component(service = WorkflowProcess.class, property = {
		Constants.SERVICE_DESCRIPTION + "=Request List of Nodes for Publishing Review",
		"process.label" + "=Request List of Nodes for Publishing Review" })
public class RequestNodesListForPublishingReview implements WorkflowProcess {

	@Override
	public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap)
			throws WorkflowException {

		WorkflowData wfData = workItem.getWorkflowData();
		MetaDataMap map = wfData.getMetaDataMap();
		if (map.containsKey("publishNodesList")) {
			String game = map.get("game", String.class);
			String publishNodesString = map.get("publishNodesList", String.class);
			if (game == null) {
				publishPlatform(map, publishNodesString);
			}
		}

	}

	private void publishPlatform(MetaDataMap map, String publishNodesString) {
		String base = map.get("publishBasePath",String.class);
		int basePlatform = base.lastIndexOf("/")+1;
		Map<String, Map<String, Map<String, Set<String>>>> platformLocaleFragmentsMap = new HashMap<>();

		String[] publishNodesArray = publishNodesString.split(",");

		for (String publishNode : publishNodesArray) {
			String[] segments = publishNode.substring(basePlatform).split("/");

			if (segments.length > 0) {
				String platform = segments[0];

				platformLocaleFragmentsMap.computeIfAbsent(platform, k -> new HashMap<>());

				if (segments.length > 2 && segments[1].equals("countries")) {
					String locale = segments[2];

					platformLocaleFragmentsMap.get(platform).computeIfAbsent(locale, k -> new HashMap<>());

					if (segments.length > 4) {
						String section = segments[4];
						platformLocaleFragmentsMap.get(platform).get(locale)
								.computeIfAbsent("Sections", k -> new HashSet<>()).add(section);

						if (segments.length > 6) {
							String interactionOrDate = segments[5];
							if ("interactions".equals(interactionOrDate)) {
								platformLocaleFragmentsMap.get(platform).get(locale)
										.computeIfAbsent("Interactions", k -> new HashSet<>()).add(segments[6]);
							} else if ("dates".equals(interactionOrDate)) {
								platformLocaleFragmentsMap.get(platform).get(locale)
										.computeIfAbsent("Dates", k -> new HashSet<>()).add(segments[6]);
							}
						}
					}
				}
			}
		}

		StringBuilder commentBuilder = new StringBuilder("List of content fragments to be published:\n");

		platformLocaleFragmentsMap.forEach((platform, localeMap) -> {
			localeMap.forEach((locale, fragmentsMap) -> {
				commentBuilder.append("Platform: ").append(platform).append(" - Locale: ").append(locale)
						.append(", ");

				commentBuilder.append("Sections: ")
						.append(String.join(", ", fragmentsMap.getOrDefault("Sections", new HashSet<>())))
						.append(", Interactions: ")
						.append(String.join(", ", fragmentsMap.getOrDefault("Interactions", new HashSet<>())))
						.append(", Dates: ")
						.append(String.join(", ", fragmentsMap.getOrDefault("Dates", new HashSet<>())));

				if (!(platform.equals(getLastKey(platformLocaleFragmentsMap))
						&& locale.equals(getLastKey(localeMap)))) {
					commentBuilder.append(" |");
				}
			});
		});

		map.put("comment", commentBuilder.toString());
	}

	private static <K, V> K getLastKey(Map<K, V> map) {
		return map.keySet().stream().reduce((first, second) -> second).orElse(null);
	}

}