package com.merkle.xp.whitelabel.core.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import com.merkle.xp.whitelabel.core.models.ActionSocialShare;
import com.merkle.xp.whitelabel.core.utils.link.MerkleLink;
import com.merkle.xp.whitelabel.core.utils.link.LinkManager;
import com.day.cq.wcm.api.Page;
import lombok.Getter;

@Model(adaptables = SlingHttpServletRequest.class, adapters = ActionSocialShare.class, resourceType = ActionSocialShare.RESOURCE_TYPE,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ActionSocialShare {

    public static final String RESOURCE_TYPE = "whitelabel/components/action";

    @Inject
    protected Page currentPage;

    @Self
    protected LinkManager linkManager;

    @ValueMapValue
    @Getter
    protected String emailSubject;

    @ValueMapValue
    @Getter
    protected String emailBody;

    @Getter
    private String socialLink;

    @PostConstruct
    protected void init() {
        if (linkManager != null) {
            MerkleLink linkObj = linkManager.get(currentPage).build();
            if (linkObj.isValid()) {
                socialLink = linkObj.getExternalizedURL();
                emailBody = StringUtils.replace(emailBody, "[PAGE-URL]", socialLink);
                emailSubject = StringUtils.replace(emailSubject, "[PAGE-URL]", socialLink);
            }
        }
    }


}
