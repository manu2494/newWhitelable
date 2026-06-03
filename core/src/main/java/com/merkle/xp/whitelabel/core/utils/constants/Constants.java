package com.merkle.xp.whitelabel.core.utils.constants;

public interface Constants {
    /**
     * MerkleLink Constants
     */
    String ATTR_TARGET = "target";
    String ATTR_ARIA_LABEL = "aria-label";
    String ATTR_TITLE = "title";
    String PN_LINK_URL = "linkURL";
    String PN_LINK_TARGET = "linkTarget";
    String PN_LINK_ACCESSIBILITY_LABEL = "linkAccessibilityLabel";
    String PN_LINK_TITLE_ATTRIBUTE = "linkTitleAttribute";
    String PAGE_LANGUAGE = "pageLanguage";
    String DATE_FORMAT = "dd-MM-yyyy";
    String APPLICATION_JSON = "application/json";
    String TEXT_CSV = "text/csv";
    String UTF_8 = "utf-8";
    String JSON = "json";
    String SLASH = "/";
    String DOT = ".";
    String COMMA = ",";
    String HTML_EXTENSION = ".html";

    /**
     * generic Constants
     */
    String UTILITY_CLASS = "Utility class";
    String SYSTEM_USER = "merkle-service";
   
    String GENERIC_LIST_BASE_PATH = "/etc/acs-commons/lists";
    String CF_DATA_REL_PATH = "jcr:content/data";
    String CQ_MODEL = "cq:model";
    String CF_MODEL = "cfModel";
    String NAME = "name";
    String CF_ROOT_PATH = "cfRootPath";
    String OVERRIDE_EXISTING = "orverrideExisting";
    String KEY = "key";
    String FILE = "file";
    String S_QUOTE="\"";
    String CSV_ESCAPE_CHARC = "\"\"";
    String EMPTY="";
    String NEWLINE="\n";
    String METADATA_NODE_PATH = "jcr:content/metadata";
    String ZIP_EXTENSION = ".zip";
    String XML_EXTENSION = "_metadata.xml";
    String TYPE = "type";
    String PROPERTY = "property";
    String DC_FORMAT = "jcr:content/metadata/dc:format";
    String PROPERTY_VALUE = "property.value";
    String APPLICATION_XML = "application/xml";
    String PROPERTY_OPERATION = "property.operation";
    String P_LIMIT = "p.limit";
    String ORDER_BY_SORT = "orderby.sort";
    String JCR_LAST_MODIFIED = "@jcr:content/jcr:lastModified";
    String PACKAGING = "Packaging";
    String PACKSHOT = "Packshot";
    String ESKO_CSV_FOLDER_PATH = "/csv-report";
    String VERTICAL_LINE_SEPARATOR = " | ";

    String DURATION = "duration";
	String JCR_METADATA_TAGS = "jcr:content/data/master/cq:tags";
	String MINUTES = "m";
	String HOURS = "h";

    String CQ_PAGE =  "cq" + ":" + "Page";

    String TEASER_COMP = "whitelabel/components/teaser";
    String COLLECTION_FRAGMENT_MODEL_PATH = "/conf/whitelabel-test/settings/dam/cfm/models/collection";
    
    // PLACEHOLDERS
    String PLACEHOLDER_COMMA = "<comma>";
    String PLACEHOLDER_NEWLINE = "<lb>";

    String NO_CACHE = "no-cache";
}
